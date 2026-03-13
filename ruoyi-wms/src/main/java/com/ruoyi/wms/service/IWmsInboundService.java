package com.ruoyi.wms.service;

import java.util.List;
import com.ruoyi.wms.domain.WmsInboundOrder;
import com.ruoyi.wms.domain.WmsInboundItem;

/**
 * 入库单 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsInboundService
{
    /**
     * 查询入库单信息
     *
     * @param inboundId 入库单 ID
     * @return 入库单信息
     */
    public WmsInboundOrder selectWmsInboundOrderById(Long inboundId);

    /**
     * 查询入库单列表
     *
     * @param wmsInboundOrder 入库单信息
     * @return 入库单集合
     */
    public List<WmsInboundOrder> selectWmsInboundOrderList(WmsInboundOrder wmsInboundOrder);

    /**
     * 新增入库单
     *
     * @param wmsInboundOrder 入库单信息
     * @return 结果
     */
    public int insertWmsInboundOrder(WmsInboundOrder wmsInboundOrder);

    /**
     * 修改入库单
     *
     * @param wmsInboundOrder 入库单信息
     * @return 结果
     */
    public int updateWmsInboundOrder(WmsInboundOrder wmsInboundOrder);

    /**
     * 批量删除入库单
     *
     * @param inboundIds 需要删除的入库单 ID 数组
     * @return 结果
     */
    public int deleteWmsInboundOrderByIds(Long[] inboundIds);

    /**
     * 删除入库单信息
     *
     * @param inboundId 入库单 ID
     * @return 结果
     */
    public int deleteWmsInboundOrderById(Long inboundId);

    /**
     * 校验入库单号是否唯一
     *
     * @param wmsInboundOrder 入库单信息
     * @return 结果
     */
    public boolean checkInboundNoUnique(WmsInboundOrder wmsInboundOrder);

    /**
     * 收货
     *
     * @param inboundId 入库单 ID
     * @param itemId 明细 ID
     * @param qty 收货数量
     * @return 结果
     */
    public int receive(Long inboundId, Long itemId, java.math.BigDecimal qty);

    /**
     * 质检
     *
     * @param inboundId 入库单 ID
     * @param itemId 明细 ID
     * @param qualifiedQty 合格数量
     * @param unqualifiedQty 不合格数量
     * @return 结果
     */
    public int qualityCheck(Long inboundId, Long itemId, java.math.BigDecimal qualifiedQty, java.math.BigDecimal unqualifiedQty);

    /**
     * 上架
     *
     * @param inboundId 入库单 ID
     * @param itemId 明细 ID
     * @param locationId 库位 ID
     * @return 结果
     */
    public int putAway(Long inboundId, Long itemId, Long locationId);
}
