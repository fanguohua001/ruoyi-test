package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsPurchaseOrder;
import com.ruoyi.wms.domain.WmsPurchaseOrderItem;

/**
 * 采购订单 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsPurchaseOrderMapper
{
    /**
     * 查询采购订单信息
     *
     * @param orderId 订单 ID
     * @return 采购订单信息
     */
    public WmsPurchaseOrder selectPurchaseOrderById(Long orderId);

    /**
     * 查询采购订单列表
     *
     * @param wmsPurchaseOrder 采购订单信息
     * @return 采购订单集合
     */
    public List<WmsPurchaseOrder> selectPurchaseOrderList(WmsPurchaseOrder wmsPurchaseOrder);

    /**
     * 根据订单号查询
     *
     * @param orderNo 订单编号
     * @return 采购订单信息
     */
    public WmsPurchaseOrder checkOrderNoUnique(String orderNo);

    /**
     * 新增采购订单
     *
     * @param wmsPurchaseOrder 采购订单信息
     * @return 结果
     */
    public int insertPurchaseOrder(WmsPurchaseOrder wmsPurchaseOrder);

    /**
     * 修改采购订单
     *
     * @param wmsPurchaseOrder 采购订单信息
     * @return 结果
     */
    public int updatePurchaseOrder(WmsPurchaseOrder wmsPurchaseOrder);

    /**
     * 删除采购订单
     *
     * @param orderId 订单 ID
     * @return 结果
     */
    public int deletePurchaseOrderById(Long orderId);

    /**
     * 批量删除采购订单
     *
     * @param orderIds 需要删除的订单 ID 数组
     * @return 结果
     */
    public int deletePurchaseOrderByIds(Long[] orderIds);

    /**
     * 查询订单明细列表
     *
     * @param orderId 订单 ID
     * @return 订单明细集合
     */
    public List<WmsPurchaseOrderItem> selectPurchaseOrderItemByOrderId(Long orderId);

    /**
     * 新增订单明细
     *
     * @param wmsPurchaseOrderItem 订单明细信息
     * @return 结果
     */
    public int insertPurchaseOrderItem(WmsPurchaseOrderItem wmsPurchaseOrderItem);

    /**
     * 修改订单明细
     *
     * @param wmsPurchaseOrderItem 订单明细信息
     * @return 结果
     */
    public int updatePurchaseOrderItem(WmsPurchaseOrderItem wmsPurchaseOrderItem);

    /**
     * 删除订单明细
     *
     * @param itemIds 订单明细 ID 数组
     * @return 结果
     */
    public int deletePurchaseOrderItemByIds(Long[] itemIds);

    /**
     * 根据订单 ID 删除订单明细
     *
     * @param orderId 订单 ID
     * @return 结果
     */
    public int deletePurchaseOrderItemByOrderId(Long orderId);

    /**
     * 根据明细 ID 查询订单明细
     *
     * @param itemId 明细 ID
     * @return 订单明细信息
     */
    public WmsPurchaseOrderItem selectPurchaseOrderItemByItemId(Long itemId);
}
