package com.ruoyi.wms.service.impl;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.wms.domain.WmsSequence;
import com.ruoyi.wms.mapper.WmsSequenceMapper;
import com.ruoyi.wms.service.IWmsSequenceService;

/**
 * 序号管理 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsSequenceServiceImpl implements IWmsSequenceService
{
    @Autowired
    private WmsSequenceMapper wmsSequenceMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String SEQUENCE_KEY_PREFIX = "wms:seq:";
    private static final String SEQUENCE_LOCK_PREFIX = "lock:seq:";

    /**
     * 获取下一个序号
     *
     * @param bizType 业务类型
     * @return 生成的序号
     */
    @Override
    public Long getNextSequence(String bizType)
    {
        String redisKey = SEQUENCE_KEY_PREFIX + bizType;
        Long currentSeq = redisTemplate.opsForValue().increment(redisKey);
        return currentSeq != null ? currentSeq - 1 : 0;
    }

    /**
     * 生成完整的单号
     *
     * @param bizType 业务类型
     * @return 生成的单号（前缀 + 序号）
     */
    @Override
    @Transactional
    public String generateNumber(String bizType)
    {
        String redisKey = SEQUENCE_KEY_PREFIX + bizType;
        String lockKey = SEQUENCE_LOCK_PREFIX + bizType;

        // 尝试获取分布式锁
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(locked))
        {
            // 获取锁失败，等待重试
            int retryCount = 0;
            while (retryCount < 5)
            {
                try
                {
                    Thread.sleep(50);
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                    throw new ServiceException("生成单号失败：" + e.getMessage());
                }

                locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
                if (Boolean.TRUE.equals(locked))
                {
                    break;
                }
                retryCount++;
            }

            if (Boolean.FALSE.equals(locked))
            {
                throw new ServiceException("获取序号锁失败，请稍后重试");
            }
        }

        try
        {
            // 先检查 Redis 中是否已有序列号
            Object existingValue = redisTemplate.opsForValue().get(redisKey);

            // 如果 Redis 中存在但值无效（非数字），则删除后重新初始化
            if (existingValue != null) {
                try {
                    // 尝试 increment，如果失败则说明值无效
                    redisTemplate.opsForValue().increment(redisKey, 0);
                } catch (Exception e) {
                    // Redis 值无效，删除后重新初始化
                    redisTemplate.delete(redisKey);
                    existingValue = null;
                }
            }

            // 使用 Redis 的 increment 原子操作获取下一个序号
            Long seq = redisTemplate.opsForValue().increment(redisKey);
            Long currentSeq;

            if (seq == 1 || existingValue == null)
            {
                // 第一次生成，从数据库加载初始序号
                WmsSequence sequence = wmsSequenceMapper.selectSequenceByBizType(bizType);
                if (sequence == null)
                {
                    redisTemplate.delete(redisKey);
                    throw new ServiceException("序号配置不存在，请先配置序号规则");
                }

                // 使用数据库中的初始序号
                currentSeq = sequence.getCurrentSeq();

                // 更新 Redis 中的序号为数据库序号 +1
                redisTemplate.opsForValue().set(redisKey, currentSeq + 1);
            }
            else
            {
                // increment 返回的是自增后的值，当前序号需要减 1
                currentSeq = seq - 1;
            }

            // 从数据库获取序号配置（前缀和长度）
            WmsSequence sequence = wmsSequenceMapper.selectSequenceByBizType(bizType);
            if (sequence == null)
            {
                throw new ServiceException("序号配置不存在");
            }

            String prefix = StringUtils.defaultString(sequence.getPrefix(), "");
            Integer seqLength = sequence.getSeqLength() != null ? sequence.getSeqLength() : 6;
            String seqStr = String.format("%0" + seqLength + "d", currentSeq);

            return prefix + seqStr;
        }
        finally
        {
            // 释放锁
            redisTemplate.delete(lockKey);
        }
    }
}
