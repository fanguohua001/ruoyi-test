package com.ruoyi.wms.service;

import java.util.List;
import com.ruoyi.wms.domain.WmsTransfer;

/**
 * 移库单 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsTransferService
{
    /**
     * 查询移库单信息
     *
     * @param transferId 移库单 ID
     * @return 移库单信息
     */
    public WmsTransfer selectWmsTransferById(Long transferId);

    /**
     * 查询移库单列表
     *
     * @param wmsTransfer 移库单信息
     * @return 移库单集合
     */
    public List<WmsTransfer> selectWmsTransferList(WmsTransfer wmsTransfer);

    /**
     * 新增移库单
     *
     * @param wmsTransfer 移库单信息
     * @return 结果
     */
    public int insertWmsTransfer(WmsTransfer wmsTransfer);

    /**
     * 修改移库单
     *
     * @param wmsTransfer 移库单信息
     * @return 结果
     */
    public int updateWmsTransfer(WmsTransfer wmsTransfer);

    /**
     * 批量删除移库单
     *
     * @param transferIds 需要删除的移库单 ID 数组
     * @return 结果
     */
    public int deleteWmsTransferByIds(Long[] transferIds);

    /**
     * 删除移库单信息
     *
     * @param transferId 移库单 ID
     * @return 结果
     */
    public int deleteWmsTransferById(Long transferId);

    /**
     * 校验移库单号是否唯一
     *
     * @param wmsTransfer 移库单信息
     * @return 结果
     */
    public boolean checkTransferNoUnique(WmsTransfer wmsTransfer);

    /**
     * 执行移库
     *
     * @param transferId 移库单 ID
     * @param productId 商品 ID
     * @param qty 移库数量
     * @return 结果
     */
    public int executeTransfer(Long transferId, Long productId, java.math.BigDecimal qty);
}
