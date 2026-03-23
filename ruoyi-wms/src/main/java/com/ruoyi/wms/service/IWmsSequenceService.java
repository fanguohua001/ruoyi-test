package com.ruoyi.wms.service;

import com.ruoyi.wms.domain.WmsSequence;

/**
 * 序号管理 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsSequenceService
{
    /**
     * 获取下一个序号
     *
     * @param bizType 业务类型
     * @return 生成的序号
     */
    public Long getNextSequence(String bizType);

    /**
     * 生成完整的单号
     *
     * @param bizType 业务类型
     * @return 生成的单号（前缀 + 序号）
     */
    public String generateNumber(String bizType);
}
