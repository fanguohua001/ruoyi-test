package com.ruoyi.web.controller.wms;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.wms.utils.RedisSequenceCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Redis 序列号清理控制器
 */
@RestController
@RequestMapping("/wms/sequence")
public class WmsSequenceController extends BaseController {

    @Autowired
    private RedisSequenceCleaner redisSequenceCleaner;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String[] SEQUENCE_KEYS = {
        "wms:seq:inbound", "wms:seq:outbound", "wms:seq:transaction",
        "wms:seq:customer_order", "wms:seq:check", "wms:seq:transfer"
    };

    private static final String[] BIZ_TYPES = {
        "inbound", "outbound", "transaction", "customer_order", "check", "transfer"
    };

    /**
     * 清理所有序列号数据
     */
    @DeleteMapping("/clean")
    public AjaxResult cleanAllSequences() {
        redisSequenceCleaner.cleanAllSequences();
        redisSequenceCleaner.cleanAllSequenceLocks();
        return AjaxResult.success("已清理所有 WMS 序列号数据");
    }

    /**
     * 清理指定业务类型的序列号
     * @param bizType 业务类型 (inbound, outbound, transaction, customer_order, check, transfer)
     */
    @DeleteMapping("/clean/{bizType}")
    public AjaxResult cleanSequence(@PathVariable String bizType) {
        redisSequenceCleaner.cleanSequence(bizType);
        redisSequenceCleaner.cleanAllSequenceLocks();
        return AjaxResult.success("已清理序列号：" + bizType);
    }

    /**
     * 查看序列号状态
     */
    @GetMapping("/status")
    public AjaxResult getSequenceStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        List<String> locks = new ArrayList<>();

        for (String key : SEQUENCE_KEYS) {
            Object value = redisTemplate.opsForValue().get(key);
            status.put(key, value != null ? value.toString() : "不存在");
        }

        Set<String> lockKeys = redisTemplate.keys("lock:seq:*");
        if (lockKeys != null && !lockKeys.isEmpty()) {
            locks.addAll(lockKeys);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sequences", status);
        result.put("locks", locks);

        return AjaxResult.success(result);
    }
}
