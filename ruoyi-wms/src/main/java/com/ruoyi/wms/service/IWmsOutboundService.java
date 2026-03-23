package com.ruoyi.wms.service;

import java.util.List;
import com.ruoyi.wms.domain.WmsOutboundOrder;
import com.ruoyi.wms.domain.WmsOutboundItem;

/**
 * 出库单 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsOutboundService
{
    /**
     * 查询出库单信息
     *
     * @param outboundId 出库单 ID
     * @return 出库单信息
     */
    public WmsOutboundOrder selectWmsOutboundOrderById(Long outboundId);

    /**
     * 查询出库单列表
     *
     * @param wmsOutboundOrder 出库单信息
     * @return 出库单集合
     */
    public List<WmsOutboundOrder> selectWmsOutboundOrderList(WmsOutboundOrder wmsOutboundOrder);

    /**
     * 新增出库单
     *
     * @param wmsOutboundOrder 出库单信息
     * @return 结果
     */
    public int insertWmsOutboundOrder(WmsOutboundOrder wmsOutboundOrder);

    /**
     * 修改出库单
     *
     * @param wmsOutboundOrder 出库单信息
     * @return 结果
     */
    public int updateWmsOutboundOrder(WmsOutboundOrder wmsOutboundOrder);

    /**
     * 批量删除出库单
     *
     * @param outboundIds 需要删除的出库单 ID 数组
     * @return 结果
     */
    public int deleteWmsOutboundOrderByIds(Long[] outboundIds);

    /**
     * 删除出库单信息
     *
     * @param outboundId 出库单 ID
     * @return 结果
     */
    public int deleteWmsOutboundOrderById(Long outboundId);

    /**
     * 校验出库单号是否唯一
     *
     * @param wmsOutboundOrder 出库单信息
     * @return 结果
     */
    public boolean checkOutboundNoUnique(WmsOutboundOrder wmsOutboundOrder);

    /**
     * 拣货（FIFO 策略）
     *
     * @param outboundId 出库单 ID
     * @param itemId 明细 ID
     * @param qty 拣货数量
     * @return 结果
     */
    public int picking(Long outboundId, Long itemId, java.math.BigDecimal qty);

    /**
     * 复核
     *
     * @param outboundId 出库单 ID
     * @return 结果
     */
    public int review(Long outboundId);

    /**
     * 打包
     *
     * @param outboundId 出库单 ID
     * @return 结果
     */
    public int pack(Long outboundId);

    /**
     * 发货
     *
     * @param outboundId 出库单 ID
     * @return 结果
     */
    public int ship(Long outboundId);

    /**
     * 完成出库单
     *
     * @param outboundId 出库单 ID
     * @return 结果
     */
    public int finishOutboundOrder(Long outboundId);
}
