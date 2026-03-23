package com.ruoyi.wms.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.wms.domain.WmsOutboundOrder;
import com.ruoyi.wms.domain.WmsOutboundItem;
import com.ruoyi.wms.domain.WmsInventory;
import com.ruoyi.wms.mapper.WmsOutboundMapper;
import com.ruoyi.wms.mapper.WmsInventoryMapper;
import com.ruoyi.wms.service.IWmsOutboundService;
import com.ruoyi.wms.service.IWmsInventoryService;
import com.ruoyi.wms.service.IWmsSequenceService;

/**
 * 出库单 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsOutboundServiceImpl implements IWmsOutboundService
{
    @Autowired
    private WmsOutboundMapper wmsOutboundMapper;

    @Autowired
    private WmsInventoryMapper wmsInventoryMapper;

    @Autowired
    private IWmsSequenceService sequenceService;

    @Autowired
    private IWmsInventoryService inventoryService;

    /**
     * 查询出库单信息
     *
     * @param outboundId 出库单 ID
     * @return 出库单信息
     */
    @Override
    public WmsOutboundOrder selectWmsOutboundOrderById(Long outboundId)
    {
        WmsOutboundOrder order = wmsOutboundMapper.selectOutboundOrderById(outboundId);
        if (order != null) {
            List<WmsOutboundItem> items = wmsOutboundMapper.selectWmsOutboundItemByOutboundId(outboundId);
            order.setItems(items);
        }
        return order;
    }

    /**
     * 查询出库单列表
     *
     * @param wmsOutboundOrder 出库单信息
     * @return 出库单
     */
    @Override
    public List<WmsOutboundOrder> selectWmsOutboundOrderList(WmsOutboundOrder wmsOutboundOrder)
    {
        List<WmsOutboundOrder> list = wmsOutboundMapper.selectOutboundOrderList(wmsOutboundOrder);
        for (WmsOutboundOrder order : list) {
            List<WmsOutboundItem> items = wmsOutboundMapper.selectWmsOutboundItemByOutboundId(order.getOutboundId());
            order.setItems(items);
        }
        return list;
    }

    /**
     * 新增出库单
     *
     * @param wmsOutboundOrder 出库单信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertWmsOutboundOrder(WmsOutboundOrder wmsOutboundOrder)
    {
        // 自动生成出库单号
        String outboundNo = sequenceService.generateNumber("outbound");
        wmsOutboundOrder.setOutboundNo(outboundNo);

        int rows = wmsOutboundMapper.insertOutboundOrder(wmsOutboundOrder);
        List<WmsOutboundItem> items = wmsOutboundOrder.getItems();
        if (StringUtils.isNotNull(items))
        {
            for (WmsOutboundItem item : items)
            {
                item.setOutboundId(wmsOutboundOrder.getOutboundId());
                wmsOutboundMapper.insertWmsOutboundItem(item);
            }
        }

        // 如果出库单是确认完成状态（is_finished=1），则更新库存和台账
        if (wmsOutboundOrder.getIsFinished() != null && wmsOutboundOrder.getIsFinished() == 1) {
            finishOutboundOrder(wmsOutboundOrder.getOutboundId());
        }

        return rows;
    }

    /**
     * 修改出库单
     *
     * @param wmsOutboundOrder 出库单信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateWmsOutboundOrder(WmsOutboundOrder wmsOutboundOrder)
    {
        // 删除原有的出库明细
        wmsOutboundMapper.deleteWmsOutboundItemByOutboundId(wmsOutboundOrder.getOutboundId());
        // 新增新的出库明细
        List<WmsOutboundItem> items = wmsOutboundOrder.getItems();
        if (StringUtils.isNotNull(items))
        {
            for (WmsOutboundItem item : items)
            {
                item.setOutboundId(wmsOutboundOrder.getOutboundId());
                wmsOutboundMapper.insertWmsOutboundItem(item);
            }
        }
        return wmsOutboundMapper.updateOutboundOrder(wmsOutboundOrder);
    }

    /**
     * 批量删除出库单
     *
     * @param outboundIds 需要删除的出库单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsOutboundOrderByIds(Long[] outboundIds)
    {
        for (Long outboundId : outboundIds) {
            wmsOutboundMapper.deleteWmsOutboundItemByOutboundId(outboundId);
        }
        return wmsOutboundMapper.deleteOutboundOrderByIds(outboundIds);
    }

    /**
     * 删除出库单信息
     *
     * @param outboundId 出库单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsOutboundOrderById(Long outboundId)
    {
        wmsOutboundMapper.deleteWmsOutboundItemByOutboundId(outboundId);
        return wmsOutboundMapper.deleteOutboundOrderById(outboundId);
    }

    /**
     * 校验出库单号是否唯一
     *
     * @param wmsOutboundOrder 出库单信息
     * @return 结果
     */
    @Override
    public boolean checkOutboundNoUnique(WmsOutboundOrder wmsOutboundOrder)
    {
        Long outboundId = wmsOutboundOrder.getOutboundId() == null ? -1L : wmsOutboundOrder.getOutboundId();
        WmsOutboundOrder info = wmsOutboundMapper.checkOutboundNoUnique(wmsOutboundOrder.getOutboundNo());
        if (info != null && info.getOutboundId().longValue() != outboundId.longValue())
        {
            return false;
        }
        return true;
    }

    /**
     * 拣货（FIFO 策略）
     *
     * @param outboundId 出库单 ID
     * @param itemId 明细 ID
     * @param qty 拣货数量
     * @return 结果
     */
    @Override
    @Transactional
    public int picking(Long outboundId, Long itemId, BigDecimal qty)
    {
        WmsOutboundOrder order = wmsOutboundMapper.selectOutboundOrderById(outboundId);
        if (order == null) {
            throw new RuntimeException("出库单不存在");
        }

        // 更新出库单状态为拣货中
        order.setStatus("3");
        wmsOutboundMapper.updateOutboundOrder(order);

        // 更新出库明细
        if (itemId != null) {
            WmsOutboundItem item = wmsOutboundMapper.selectWmsOutboundItemByItemId(itemId);
            if (item != null) {
                item.setPickedQty(qty);
                wmsOutboundMapper.updateWmsOutboundItem(item);
            }
        }

        // 更新出库单数量
        BigDecimal pickedQty = order.getPickedQty() != null ? order.getPickedQty() : BigDecimal.ZERO;
        order.setPickedQty(pickedQty.add(qty));
        wmsOutboundMapper.updateOutboundOrder(order);

        return 1;
    }

    /**
     * 复核
     *
     * @param outboundId 出库单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int review(Long outboundId)
    {
        WmsOutboundOrder order = wmsOutboundMapper.selectOutboundOrderById(outboundId);
        if (order == null) {
            throw new RuntimeException("出库单不存在");
        }

        order.setStatus("4");
        return wmsOutboundMapper.updateOutboundOrder(order);
    }

    /**
     * 打包
     *
     * @param outboundId 出库单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int pack(Long outboundId)
    {
        WmsOutboundOrder order = wmsOutboundMapper.selectOutboundOrderById(outboundId);
        if (order == null) {
            throw new RuntimeException("出库单不存在");
        }

        order.setStatus("5");
        return wmsOutboundMapper.updateOutboundOrder(order);
    }

    /**
     * 发货
     *
     * @param outboundId 出库单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int ship(Long outboundId)
    {
        WmsOutboundOrder order = wmsOutboundMapper.selectOutboundOrderById(outboundId);
        if (order == null) {
            throw new RuntimeException("出库单不存在");
        }

        order.setStatus("7");
        order.setShippedQty(order.getPickedQty());

        // 扣减库存
        List<WmsOutboundItem> items = wmsOutboundMapper.selectWmsOutboundItemByOutboundId(outboundId);
        for (WmsOutboundItem item : items) {
            if (item.getOrderQty() != null && item.getOrderQty().compareTo(BigDecimal.ZERO) > 0) {
                WmsInventory inventory = new WmsInventory();
                inventory.setProductId(item.getProductId());
                inventory.setProductCode(item.getProductCode());
                inventory.setProductName(item.getProductName());
                inventory.setWarehouseId(order.getWarehouseId());

                // 减少库存并记录履历
                inventoryService.decreaseStockWithLedger(
                    inventory,
                    item.getOrderQty(),
                    "wms_outbound_order",
                    outboundId,
                    order.getOutboundNo()
                );
            }
        }

        return wmsOutboundMapper.updateOutboundOrder(order);
    }

    /**
     * 完成出库单
     *
     * @param outboundId 出库单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int finishOutboundOrder(Long outboundId)
    {
        WmsOutboundOrder order = wmsOutboundMapper.selectOutboundOrderById(outboundId);
        if (order == null) {
            throw new RuntimeException("出库单不存在");
        }

        // 检查是否已完成
        if (order.getIsFinished() != null && order.getIsFinished() == 1) {
            throw new RuntimeException("出库单已完成，无法重复操作");
        }

        // 获取出库明细
        List<WmsOutboundItem> items = wmsOutboundMapper.selectWmsOutboundItemByOutboundId(outboundId);
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("出库单没有明细，无法完成");
        }

        // 遍历明细，扣减库存
        for (WmsOutboundItem item : items) {
            WmsInventory inventory = new WmsInventory();
            inventory.setProductId(item.getProductId());
            inventory.setProductCode(item.getProductCode());
            inventory.setProductName(item.getProductName());
            inventory.setWarehouseId(order.getWarehouseId());

            BigDecimal orderQty = item.getOrderQty() != null ? item.getOrderQty() : BigDecimal.ZERO;
            if (orderQty.compareTo(BigDecimal.ZERO) > 0) {
                // 减少库存并记录履历
                inventoryService.decreaseStockWithLedger(
                    inventory,
                    orderQty,
                    "wms_outbound_order",
                    outboundId,
                    order.getOutboundNo()
                );
            }
        }

        // 设置为已完成
        order.setIsFinished(1);
        order.setStatus("7");
        order.setActualDate(new Date());
        return wmsOutboundMapper.updateOutboundOrder(order);
    }
}
