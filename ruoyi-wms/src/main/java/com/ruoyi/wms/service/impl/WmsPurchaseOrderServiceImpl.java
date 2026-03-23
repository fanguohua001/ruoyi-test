package com.ruoyi.wms.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.wms.domain.WmsPurchaseOrder;
import com.ruoyi.wms.domain.WmsPurchaseOrderItem;
import com.ruoyi.wms.mapper.WmsPurchaseOrderMapper;
import com.ruoyi.wms.service.IWmsPurchaseOrderService;
import com.ruoyi.wms.service.IWmsSequenceService;

/**
 * 采购订单 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsPurchaseOrderServiceImpl implements IWmsPurchaseOrderService
{
    @Autowired
    private WmsPurchaseOrderMapper wmsPurchaseOrderMapper;

    @Autowired
    private IWmsSequenceService sequenceService;

    /**
     * 查询采购订单信息
     *
     * @param orderId 订单 ID
     * @return 采购订单信息
     */
    @Override
    public WmsPurchaseOrder selectPurchaseOrderById(Long orderId)
    {
        WmsPurchaseOrder order = wmsPurchaseOrderMapper.selectPurchaseOrderById(orderId);
        if (order != null) {
            List<WmsPurchaseOrderItem> items = wmsPurchaseOrderMapper.selectPurchaseOrderItemByOrderId(orderId);
            order.setItems(items);
        }
        return order;
    }

    /**
     * 查询采购订单列表
     *
     * @param wmsPurchaseOrder 采购订单信息
     * @return 采购订单集合
     */
    @Override
    public List<WmsPurchaseOrder> selectPurchaseOrderList(WmsPurchaseOrder wmsPurchaseOrder)
    {
        return wmsPurchaseOrderMapper.selectPurchaseOrderList(wmsPurchaseOrder);
    }

    /**
     * 新增采购订单
     *
     * @param wmsPurchaseOrder 采购订单信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPurchaseOrder(WmsPurchaseOrder wmsPurchaseOrder)
    {
        // 自动生成订单号
        String orderNo = sequenceService.generateNumber("purchase_order");
        wmsPurchaseOrder.setOrderNo(orderNo);

        int rows = wmsPurchaseOrderMapper.insertPurchaseOrder(wmsPurchaseOrder);
        List<WmsPurchaseOrderItem> items = wmsPurchaseOrder.getItems();
        if (StringUtils.isNotNull(items))
        {
            int lineNo = 1;
            for (WmsPurchaseOrderItem item : items)
            {
                item.setOrderId(wmsPurchaseOrder.getOrderId());
                item.setLineNo(lineNo++);

                // 确保数量、单价等字段有默认值
                if (item.getQuantity() == null) {
                    item.setQuantity(BigDecimal.ZERO);
                }
                if (item.getUnitPrice() == null) {
                    item.setUnitPrice(BigDecimal.ZERO);
                }
                if (item.getTaxRate() == null) {
                    item.setTaxRate(new BigDecimal("13"));
                }

                wmsPurchaseOrderMapper.insertPurchaseOrderItem(item);
            }
        }

        return rows;
    }

    /**
     * 修改采购订单
     *
     * @param wmsPurchaseOrder 采购订单信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePurchaseOrder(WmsPurchaseOrder wmsPurchaseOrder)
    {
        // 删除原有的订单明细
        wmsPurchaseOrderMapper.deletePurchaseOrderItemByOrderId(wmsPurchaseOrder.getOrderId());
        // 新增新的订单明细
        List<WmsPurchaseOrderItem> items = wmsPurchaseOrder.getItems();
        if (StringUtils.isNotNull(items))
        {
            int lineNo = 1;
            for (WmsPurchaseOrderItem item : items)
            {
                item.setOrderId(wmsPurchaseOrder.getOrderId());
                item.setLineNo(lineNo++);
                wmsPurchaseOrderMapper.insertPurchaseOrderItem(item);
            }
        }
        return wmsPurchaseOrderMapper.updatePurchaseOrder(wmsPurchaseOrder);
    }

    /**
     * 批量删除采购订单
     *
     * @param orderIds 需要删除的订单 ID 数组
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePurchaseOrderByIds(Long[] orderIds)
    {
        for (Long orderId : orderIds) {
            wmsPurchaseOrderMapper.deletePurchaseOrderItemByOrderId(orderId);
        }
        return wmsPurchaseOrderMapper.deletePurchaseOrderByIds(orderIds);
    }

    /**
     * 删除采购订单信息
     *
     * @param orderId 订单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePurchaseOrderById(Long orderId)
    {
        wmsPurchaseOrderMapper.deletePurchaseOrderItemByOrderId(orderId);
        return wmsPurchaseOrderMapper.deletePurchaseOrderById(orderId);
    }

    /**
     * 校验订单编号是否唯一
     *
     * @param wmsPurchaseOrder 采购订单信息
     * @return 结果
     */
    @Override
    public boolean checkOrderNoUnique(WmsPurchaseOrder wmsPurchaseOrder)
    {
        Long orderId = wmsPurchaseOrder.getOrderId() == null ? -1L : wmsPurchaseOrder.getOrderId();
        WmsPurchaseOrder info = wmsPurchaseOrderMapper.checkOrderNoUnique(wmsPurchaseOrder.getOrderNo());
        if (info != null && info.getOrderId().longValue() != orderId.longValue())
        {
            return false;
        }
        return true;
    }

    /**
     * 完成采购订单
     *
     * @param orderId 订单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int finishPurchaseOrder(Long orderId)
    {
        WmsPurchaseOrder order = wmsPurchaseOrderMapper.selectPurchaseOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("采购订单不存在");
        }

        // 检查是否已完成
        if (order.getOrderStatus() != null && order.getOrderStatus() == 1) {
            throw new RuntimeException("采购订单已完成，不允许重复操作");
        }

        // 检查是否有明细
        List<WmsPurchaseOrderItem> items = wmsPurchaseOrderMapper.selectPurchaseOrderItemByOrderId(orderId);
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("采购订单没有明细，无法完成");
        }

        // 设置为已完成
        order.setOrderStatus(1);
        wmsPurchaseOrderMapper.updatePurchaseOrder(order);

        return 1;
    }
}
