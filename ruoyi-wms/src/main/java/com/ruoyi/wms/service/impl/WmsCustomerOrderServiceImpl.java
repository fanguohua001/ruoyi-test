package com.ruoyi.wms.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.wms.domain.WmsCustomerOrder;
import com.ruoyi.wms.domain.WmsCustomerOrderItem;
import com.ruoyi.wms.mapper.WmsCustomerOrderMapper;
import com.ruoyi.wms.service.IWmsCustomerOrderService;
import com.ruoyi.wms.service.IWmsSequenceService;

/**
 * 客户订单 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsCustomerOrderServiceImpl implements IWmsCustomerOrderService
{
    @Autowired
    private WmsCustomerOrderMapper wmsCustomerOrderMapper;

    @Autowired
    private IWmsSequenceService sequenceService;

    /**
     * 查询客户订单信息
     *
     * @param orderId 订单 ID
     * @return 客户订单信息
     */
    @Override
    public WmsCustomerOrder selectCustomerOrderById(Long orderId)
    {
        WmsCustomerOrder order = wmsCustomerOrderMapper.selectCustomerOrderById(orderId);
        if (order != null) {
            List<WmsCustomerOrderItem> items = wmsCustomerOrderMapper.selectCustomerOrderItemByOrderId(orderId);
            order.setItems(items);
        }
        return order;
    }

    /**
     * 查询客户订单列表
     *
     * @param wmsCustomerOrder 客户订单信息
     * @return 客户订单
     */
    @Override
    public List<WmsCustomerOrder> selectCustomerOrderList(WmsCustomerOrder wmsCustomerOrder)
    {
        List<WmsCustomerOrder> list = wmsCustomerOrderMapper.selectCustomerOrderList(wmsCustomerOrder);
        for (WmsCustomerOrder order : list) {
            List<WmsCustomerOrderItem> items = wmsCustomerOrderMapper.selectCustomerOrderItemByOrderId(order.getOrderId());
            order.setItems(items);
        }
        return list;
    }

    /**
     * 新增客户订单
     *
     * @param wmsCustomerOrder 客户订单信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertCustomerOrder(WmsCustomerOrder wmsCustomerOrder)
    {
        // 自动生成订单编号
        String orderNo = sequenceService.generateNumber("customer_order");
        wmsCustomerOrder.setOrderNo(orderNo);

        // 计算合计
        calculateTotals(wmsCustomerOrder);

        int rows = wmsCustomerOrderMapper.insertCustomerOrder(wmsCustomerOrder);
        List<WmsCustomerOrderItem> items = wmsCustomerOrder.getItems();
        if (StringUtils.isNotNull(items))
        {
            for (WmsCustomerOrderItem item : items)
            {
                item.setOrderId(wmsCustomerOrder.getOrderId());
                wmsCustomerOrderMapper.insertCustomerOrderItem(item);
            }
        }
        return rows;
    }

    /**
     * 修改客户订单
     *
     * @param wmsCustomerOrder 客户订单信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateCustomerOrder(WmsCustomerOrder wmsCustomerOrder)
    {
        // 计算合计
        calculateTotals(wmsCustomerOrder);

        // 删除原有的订单明细
        wmsCustomerOrderMapper.deleteCustomerOrderItemByOrderId(wmsCustomerOrder.getOrderId());
        // 新增新的订单明细
        List<WmsCustomerOrderItem> items = wmsCustomerOrder.getItems();
        if (StringUtils.isNotNull(items))
        {
            for (WmsCustomerOrderItem item : items)
            {
                item.setOrderId(wmsCustomerOrder.getOrderId());
                wmsCustomerOrderMapper.insertCustomerOrderItem(item);
            }
        }
        return wmsCustomerOrderMapper.updateCustomerOrder(wmsCustomerOrder);
    }

    /**
     * 批量删除客户订单
     *
     * @param orderIds 需要删除的订单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteCustomerOrderByIds(Long[] orderIds)
    {
        for (Long orderId : orderIds) {
            wmsCustomerOrderMapper.deleteCustomerOrderItemByOrderId(orderId);
        }
        return wmsCustomerOrderMapper.deleteCustomerOrderByIds(orderIds);
    }

    /**
     * 删除客户订单信息
     *
     * @param orderId 订单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteCustomerOrderById(Long orderId)
    {
        wmsCustomerOrderMapper.deleteCustomerOrderItemByOrderId(orderId);
        return wmsCustomerOrderMapper.deleteCustomerOrderById(orderId);
    }

    /**
     * 校验订单编号是否唯一
     *
     * @param wmsCustomerOrder 客户订单信息
     * @return 结果
     */
    @Override
    public boolean checkOrderNoUnique(WmsCustomerOrder wmsCustomerOrder)
    {
        Long orderId = wmsCustomerOrder.getOrderId() == null ? -1L : wmsCustomerOrder.getOrderId();
        WmsCustomerOrder info = wmsCustomerOrderMapper.checkOrderNoUnique(wmsCustomerOrder.getOrderNo());
        if (info != null && info.getOrderId().longValue() != orderId.longValue())
        {
            return false;
        }
        return true;
    }

    /**
     * 完成客户订单
     *
     * @param orderId 订单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int finishCustomerOrder(Long orderId)
    {
        WmsCustomerOrder order = wmsCustomerOrderMapper.selectCustomerOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 检查是否已完成
        if (order.getOrderStatus() != null && order.getOrderStatus() == 1) {
            throw new RuntimeException("订单已完成，无法重复操作");
        }

        // 设置为已完成
        order.setOrderStatus(1);
        wmsCustomerOrderMapper.updateCustomerOrder(order);

        return 1;
    }

    /**
     * 计算订单合计
     *
     * @param wmsCustomerOrder 客户订单信息
     */
    private void calculateTotals(WmsCustomerOrder wmsCustomerOrder)
    {
        List<WmsCustomerOrderItem> items = wmsCustomerOrder.getItems();
        if (StringUtils.isNull(items) || items.isEmpty()) {
            wmsCustomerOrder.setTotalQty(BigDecimal.ZERO);
            wmsCustomerOrder.setTotalAmount(BigDecimal.ZERO);
            return;
        }

        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (WmsCustomerOrderItem item : items) {
            // 计算不含税金额
            if (item.getUnitPrice() != null && item.getQuantity() != null) {
                item.setAmount(item.getUnitPrice().multiply(item.getQuantity()));
            } else {
                item.setAmount(BigDecimal.ZERO);
            }

            // 计算税率和含税金额
            if (item.getTaxRate() == null) {
                item.setTaxRate(BigDecimal.valueOf(13.0)); // 默认 13% 税率
            }
            if (item.getAmount() != null) {
                BigDecimal taxAmount = item.getAmount().multiply(item.getTaxRate().divide(BigDecimal.valueOf(100)));
                item.setAmountTax(item.getAmount().add(taxAmount));
                item.setUnitPriceTax(item.getUnitPrice() != null ?
                    item.getUnitPrice().multiply(BigDecimal.ONE.add(item.getTaxRate().divide(BigDecimal.valueOf(100)))) : BigDecimal.ZERO);
            } else {
                item.setAmountTax(BigDecimal.ZERO);
                item.setUnitPriceTax(BigDecimal.ZERO);
            }

            totalQty = totalQty.add(item.getQuantity() != null ? item.getQuantity() : BigDecimal.ZERO);
            totalAmount = totalAmount.add(item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO);
        }

        wmsCustomerOrder.setTotalQty(totalQty);
        wmsCustomerOrder.setTotalAmount(totalAmount);
    }

    /**
     * 查询已完成的客户订单列表（用于出库单关联）
     *
     * @param wmsCustomerOrder 客户订单信息
     * @return 客户订单集合
     */
    @Override
    public List<WmsCustomerOrder> listFinishedOrders(WmsCustomerOrder wmsCustomerOrder)
    {
        // 设置订单状态为已完成
        wmsCustomerOrder.setOrderStatus(1);
        List<WmsCustomerOrder> list = wmsCustomerOrderMapper.selectCustomerOrderList(wmsCustomerOrder);
        for (WmsCustomerOrder order : list) {
            List<WmsCustomerOrderItem> items = wmsCustomerOrderMapper.selectCustomerOrderItemByOrderId(order.getOrderId());
            order.setItems(items);
        }
        return list;
    }
}
