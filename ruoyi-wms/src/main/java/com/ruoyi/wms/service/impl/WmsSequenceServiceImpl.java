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
        String lockKey = SEQUENCE_LOCK_PREFIX + bizType;

        // 获取分布式锁
        if (!tryLock(lockKey))
        {
            throw new ServiceException("获取序号锁失败，请稍后重试");
        }

        try
        {
            // 从数据库获取序号配置
            WmsSequence sequence = wmsSequenceMapper.selectSequenceByBizType(bizType);
            if (sequence == null)
            {
                throw new ServiceException("序号配置不存在，请先配置序号规则");
            }

            // 使用数据库中的当前序号
            Long currentSeq = sequence.getCurrentSeq();

            // 更新数据库中的序号（+1）
            wmsSequenceMapper.updateSequenceCurrentSeq(sequence.getSequenceId(), currentSeq + 1);

            return currentSeq;
        }
        finally
        {
            // 释放锁
            releaseLock(lockKey);
        }
    }

    /**
     * 尝试获取分布式锁
     */
    private boolean tryLock(String lockKey)
    {
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);

        if (Boolean.TRUE.equals(locked))
        {
            return true;
        }

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
                return false;
            }

            locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
            if (Boolean.TRUE.equals(locked))
            {
                return true;
            }
            retryCount++;
        }

        return false;
    }

    /**
     * 释放分布式锁
     */
    private void releaseLock(String lockKey)
    {
        redisTemplate.delete(lockKey);
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
        String lockKey = SEQUENCE_LOCK_PREFIX + bizType;

        // 使用 Redis 分布式锁保证并发安全
        if (!tryLock(lockKey))
        {
            throw new ServiceException("获取序号锁失败，请稍后重试");
        }

        try
        {
            // 从数据库获取序号配置
            WmsSequence sequence = wmsSequenceMapper.selectSequenceByBizType(bizType);
            if (sequence == null)
            {
                throw new ServiceException("序号配置不存在，请先配置序号规则");
            }

            // 使用数据库中的当前序号作为本次单号的序号
            Long currentSeq = sequence.getCurrentSeq();

            // 更新数据库中的序号（+1），为下次生成做准备
            wmsSequenceMapper.updateSequenceCurrentSeq(sequence.getSequenceId(), currentSeq + 1);

            // 生成单号：前缀 + 序号（补零）
            String prefix = StringUtils.defaultString(sequence.getPrefix(), "");
            Integer seqLength = sequence.getSeqLength() != null ? sequence.getSeqLength() : 6;
            String seqStr = String.format("%0" + seqLength + "d", currentSeq);

            return prefix + seqStr;
        }
        finally
        {
            // 释放锁
            releaseLock(lockKey);
        }
    }
}
