package com.ruoyi.wms.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Redis 序列号清理工具类
 * 用于清理 WMS 模块中 Redis 存储的序列号数据
 */
@Component
public class RedisSequenceCleaner {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String[] SEQUENCE_KEYS = {
        "wms:seq:inbound",
        "wms:seq:outbound",
        "wms:seq:transaction",
        "wms:seq:customer_order",
        "wms:seq:check",
        "wms:seq:transfer"
    };

    /**
     * 清理所有序列号数据
     */
    public void cleanAllSequences() {
        for (String key : SEQUENCE_KEYS) {
            redisTemplate.delete(key);
        }
        System.out.println("已清理所有 WMS 序列号：");
        for (String key : SEQUENCE_KEYS) {
            System.out.println("  - " + key);
        }
    }

    /**
     * 清理指定业务类型的序列号
     * @param bizType 业务类型 (inbound, outbound, transaction, customer_order, check, transfer)
     */
    public void cleanSequence(String bizType) {
        String key = "wms:seq:" + bizType;
        redisTemplate.delete(key);
        System.out.println("已清理序列号：" + key);
    }

    /**
     * 清理所有序列号锁
     */
    public void cleanAllSequenceLocks() {
        Set<String> keys = redisTemplate.keys("lock:seq:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            System.out.println("已清理 " + keys.size() + " 个序列号锁");
        }
    }

    /**
     * 检查序列号是否存在
     * @param bizType 业务类型
     * @return 是否存在
     */
    public boolean hasSequence(String bizType) {
        String key = "wms:seq:" + bizType;
        return redisTemplate.hasKey(key);
    }

    /**
     * 打印当前序列号状态
     */
    public void printSequenceStatus() {
        System.out.println("=== WMS Redis 序列号状态 ===");
        for (String key : SEQUENCE_KEYS) {
            Object value = redisTemplate.opsForValue().get(key);
            System.out.println(key + " = " + (value != null ? value : "不存在"));
        }
        System.out.println("=== 序列号锁状态 ===");
        Set<String> lockKeys = redisTemplate.keys("lock:seq:*");
        if (lockKeys != null && !lockKeys.isEmpty()) {
            for (String lockKey : lockKeys) {
                System.out.println(lockKey + " = 已锁定");
            }
        } else {
            System.out.println("无锁定的序列号");
        }
    }
}
