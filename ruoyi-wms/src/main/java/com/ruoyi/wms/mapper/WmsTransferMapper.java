package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsTransfer;

/**
 * 移库单 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsTransferMapper
{
    /**
     * 查询移库单信息
     *
     * @param transferId 移库单 ID
     * @return 移库单信息
     */
    public WmsTransfer selectTransferById(Long transferId);

    /**
     * 查询移库单列表
     *
     * @param wmsTransfer 移库单信息
     * @return 移库单集合
     */
    public List<WmsTransfer> selectTransferList(WmsTransfer wmsTransfer);

    /**
     * 根据移库单号查询
     *
     * @param transferNo 移库单号
     * @return 移库单信息
     */
    public WmsTransfer checkTransferNoUnique(String transferNo);

    /**
     * 新增移库单
     *
     * @param wmsTransfer 移库单信息
     * @return 结果
     */
    public int insertTransfer(WmsTransfer wmsTransfer);

    /**
     * 修改移库单
     *
     * @param wmsTransfer 移库单信息
     * @return 结果
     */
    public int updateTransfer(WmsTransfer wmsTransfer);

    /**
     * 删除移库单
     *
     * @param transferId 移库单 ID
     * @return 结果
     */
    public int deleteTransferById(Long transferId);

    /**
     * 批量删除移库单
     *
     * @param transferIds 需要删除的移库单 ID 数组
     * @return 结果
     */
    public int deleteTransferByIds(Long[] transferIds);
}
