package com.ruoyi.wms.service;

import java.util.List;
import com.ruoyi.wms.domain.WmsPurchaseOrder;
import com.ruoyi.wms.domain.WmsPurchaseOrderItem;

/**
 * 采购订单 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsPurchaseOrderService
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
     * 批量删除采购订单
     *
     * @param orderIds 需要删除的订单 ID 数组
     * @return 结果
     */
    public int deletePurchaseOrderByIds(Long[] orderIds);

    /**
     * 删除采购订单信息
     *
     * @param orderId 订单 ID
     * @return 结果
     */
    public int deletePurchaseOrderById(Long orderId);

    /**
     * 校验订单编号是否唯一
     *
     * @param wmsPurchaseOrder 采购订单信息
     * @return 结果
     */
    public boolean checkOrderNoUnique(WmsPurchaseOrder wmsPurchaseOrder);

    /**
     * 完成采购订单
     *
     * @param orderId 订单 ID
     * @return 结果
     */
    public int finishPurchaseOrder(Long orderId);
}
