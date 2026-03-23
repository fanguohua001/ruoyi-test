package com.ruoyi.wms.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Redis 序列号清理命令行运行器
 * 用于在应用启动时清理 Redis 中的序列号数据
 *
 * 使用方法：
 * 1. 修改 cleanOnStart = true
 * 2. 启动应用
 * 3. 应用启动后会自动清理 Redis 序列号
 * 4. 清理完成后将 cleanOnStart 改回 false
 */
@Component
public class RedisSequenceCleanupRunner implements CommandLineRunner {

    // 是否启动时清理：true=清理，false=不清理
    private static final boolean cleanOnStart = false;

    private final RedisSequenceCleaner redisSequenceCleaner;

    public RedisSequenceCleanupRunner(RedisSequenceCleaner redisSequenceCleaner) {
        this.redisSequenceCleaner = redisSequenceCleaner;
    }

    @Override
    public void run(String... args) throws Exception {
        if (cleanOnStart) {
            System.out.println("========================================");
            System.out.println("开始清理 Redis 序列号数据...");
            System.out.println("========================================");

            // 打印清理前状态
            System.out.println("\n【清理前状态】");
            printStatus();

            // 执行清理
            redisSequenceCleaner.cleanAllSequences();
            redisSequenceCleaner.cleanAllSequenceLocks();

            // 打印清理后状态
            System.out.println("\n【清理后状态】");
            printStatus();

            System.out.println("\n========================================");
            System.out.println("Redis 序列号清理完成！");
            System.out.println("========================================");
            System.out.println("提示：请将 cleanOnStart 改回 false 并重启应用");
        }
    }

    private void printStatus() {
        redisSequenceCleaner.printSequenceStatus();
    }
}
