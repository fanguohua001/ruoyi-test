package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsOutboundOrder;
import com.ruoyi.wms.domain.WmsOutboundItem;

/**
 * 出库单 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsOutboundMapper
{
    /**
     * 查询出库单信息
     *
     * @param outboundId 出库单 ID
     * @return 出库单信息
     */
    public WmsOutboundOrder selectOutboundOrderById(Long outboundId);

    /**
     * 查询出库单列表
     *
     * @param wmsOutboundOrder 出库单信息
     * @return 出库单集合
     */
    public List<WmsOutboundOrder> selectOutboundOrderList(WmsOutboundOrder wmsOutboundOrder);

    /**
     * 根据出库单号查询
     *
     * @param outboundNo 出库单号
     * @return 出库单信息
     */
    public WmsOutboundOrder checkOutboundNoUnique(String outboundNo);

    /**
     * 新增出库单
     *
     * @param wmsOutboundOrder 出库单信息
     * @return 结果
     */
    public int insertOutboundOrder(WmsOutboundOrder wmsOutboundOrder);

    /**
     * 修改出库单
     *
     * @param wmsOutboundOrder 出库单信息
     * @return 结果
     */
    public int updateOutboundOrder(WmsOutboundOrder wmsOutboundOrder);

    /**
     * 删除出库单
     *
     * @param outboundId 出库单 ID
     * @return 结果
     */
    public int deleteOutboundOrderById(Long outboundId);

    /**
     * 批量删除出库单
     *
     * @param outboundIds 需要删除的出库单 ID 数组
     * @return 结果
     */
    public int deleteOutboundOrderByIds(Long[] outboundIds);

    /**
     * 查询出库明细列表
     *
     * @param outboundId 出库单 ID
     * @return 出库明细集合
     */
    public List<WmsOutboundItem> selectWmsOutboundItemByOutboundId(Long outboundId);

    /**
     * 新增出库明细
     *
     * @param wmsOutboundItem 出库明细信息
     * @return 结果
     */
    public int insertWmsOutboundItem(WmsOutboundItem wmsOutboundItem);

    /**
     * 修改出库明细
     *
     * @param wmsOutboundItem 出库明细信息
     * @return 结果
     */
    public int updateWmsOutboundItem(WmsOutboundItem wmsOutboundItem);

    /**
     * 删除出库明细
     *
     * @param itemIds 出库明细 ID 数组
     * @return 结果
     */
    public int deleteWmsOutboundItemByIds(Long[] itemIds);

    /**
     * 根据出库单 ID 删除出库明细
     *
     * @param outboundId 出库单 ID
     * @return 结果
     */
    public int deleteWmsOutboundItemByOutboundId(Long outboundId);

    /**
     * 根据明细 ID 查询出库明细
     *
     * @param itemId 明细 ID
     * @return 出库明细信息
     */
    public WmsOutboundItem selectWmsOutboundItemByItemId(Long itemId);
}
