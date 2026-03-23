package com.ruoyi.wms.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.wms.domain.WmsInboundOrder;
import com.ruoyi.wms.domain.WmsInboundItem;
import com.ruoyi.wms.domain.WmsInventory;
import com.ruoyi.wms.mapper.WmsInboundMapper;
import com.ruoyi.wms.mapper.WmsInventoryMapper;
import com.ruoyi.wms.service.IWmsInboundService;
import com.ruoyi.wms.service.IWmsInventoryService;
import com.ruoyi.wms.service.IWmsSequenceService;

/**
 * 入库单 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsInboundServiceImpl implements IWmsInboundService
{
    @Autowired
    private WmsInboundMapper wmsInboundMapper;

    @Autowired
    private WmsInventoryMapper wmsInventoryMapper;

    @Autowired
    private IWmsSequenceService sequenceService;

    @Autowired
    private IWmsInventoryService inventoryService;

    /**
     * 查询入库单信息
     *
     * @param inboundId 入库单 ID
     * @return 入库单信息
     */
    @Override
    public WmsInboundOrder selectWmsInboundOrderById(Long inboundId)
    {
        WmsInboundOrder order = wmsInboundMapper.selectInboundOrderById(inboundId);
        if (order != null) {
            List<WmsInboundItem> items = wmsInboundMapper.selectWmsInboundItemByInboundId(inboundId);
            order.setItems(items);
        }
        return order;
    }

    /**
     * 查询入库单列表
     *
     * @param wmsInboundOrder 入库单信息
     * @return 入库单
     */
    @Override
    public List<WmsInboundOrder> selectWmsInboundOrderList(WmsInboundOrder wmsInboundOrder)
    {
        List<WmsInboundOrder> list = wmsInboundMapper.selectInboundOrderList(wmsInboundOrder);
        for (WmsInboundOrder order : list) {
            List<WmsInboundItem> items = wmsInboundMapper.selectWmsInboundItemByInboundId(order.getInboundId());
            order.setItems(items);
        }
        return list;
    }

    /**
     * 新增入库单
     *
     * @param wmsInboundOrder 入库单信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertWmsInboundOrder(WmsInboundOrder wmsInboundOrder)
    {
        // 自动生成入库单号
        String inboundNo = sequenceService.generateNumber("inbound");
        wmsInboundOrder.setInboundNo(inboundNo);

        int rows = wmsInboundMapper.insertInboundOrder(wmsInboundOrder);
        List<WmsInboundItem> items = wmsInboundOrder.getItems();
        if (StringUtils.isNotNull(items))
        {
            for (WmsInboundItem item : items)
            {
                item.setInboundId(wmsInboundOrder.getInboundId());
                wmsInboundMapper.insertWmsInboundItem(item);
            }
        }

        // 如果入库单是确认完成状态（is_finished=1），则更新库存和台账
        if (wmsInboundOrder.getIsFinished() != null && wmsInboundOrder.getIsFinished() == 1) {
            finishInboundOrder(wmsInboundOrder.getInboundId());
        }

        return rows;
    }

    /**
     * 修改入库单
     *
     * @param wmsInboundOrder 入库单信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateWmsInboundOrder(WmsInboundOrder wmsInboundOrder)
    {
        // 删除原有的入库明细
        wmsInboundMapper.deleteWmsInboundItemByInboundId(wmsInboundOrder.getInboundId());
        // 新增新的入库明细
        List<WmsInboundItem> items = wmsInboundOrder.getItems();
        if (StringUtils.isNotNull(items))
        {
            for (WmsInboundItem item : items)
            {
                item.setInboundId(wmsInboundOrder.getInboundId());
                wmsInboundMapper.insertWmsInboundItem(item);
            }
        }
        return wmsInboundMapper.updateInboundOrder(wmsInboundOrder);
    }

    /**
     * 批量删除入库单
     *
     * @param inboundIds 需要删除的入库单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsInboundOrderByIds(Long[] inboundIds)
    {
        for (Long inboundId : inboundIds) {
            wmsInboundMapper.deleteWmsInboundItemByInboundId(inboundId);
        }
        return wmsInboundMapper.deleteInboundOrderByIds(inboundIds);
    }

    /**
     * 删除入库单信息
     *
     * @param inboundId 入库单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsInboundOrderById(Long inboundId)
    {
        wmsInboundMapper.deleteWmsInboundItemByInboundId(inboundId);
        return wmsInboundMapper.deleteInboundOrderById(inboundId);
    }

    /**
     * 校验入库单号是否唯一
     *
     * @param wmsInboundOrder 入库单信息
     * @return 结果
     */
    @Override
    public boolean checkInboundNoUnique(WmsInboundOrder wmsInboundOrder)
    {
        Long inboundId = wmsInboundOrder.getInboundId() == null ? -1L : wmsInboundOrder.getInboundId();
        WmsInboundOrder info = wmsInboundMapper.checkInboundNoUnique(wmsInboundOrder.getInboundNo());
        if (info != null && info.getInboundId().longValue() != inboundId.longValue())
        {
            return false;
        }
        return true;
    }

    /**
     * 收货
     *
     * @param inboundId 入库单 ID
     * @param itemId 明细 ID
     * @param qty 收货数量
     * @return 结果
     */
    @Override
    @Transactional
    public int receive(Long inboundId, Long itemId, BigDecimal qty)
    {
        WmsInboundOrder order = wmsInboundMapper.selectInboundOrderById(inboundId);
        if (order == null) {
            throw new RuntimeException("入库单不存在");
        }

        // 更新入库单状态为收货中
        order.setStatus("2");
        wmsInboundMapper.updateInboundOrder(order);

        // 更新入库明细
        if (itemId != null) {
            WmsInboundItem item = wmsInboundMapper.selectWmsInboundItemByItemId(itemId);
            if (item != null) {
                item.setReceivedQty(qty);
                item.setStatus("1");
                wmsInboundMapper.updateWmsInboundItem(item);
            }
        }

        // 更新入库单数量
        BigDecimal receivedQty = order.getReceivedQty() != null ? order.getReceivedQty() : BigDecimal.ZERO;
        order.setReceivedQty(receivedQty.add(qty));
        wmsInboundMapper.updateInboundOrder(order);

        return 1;
    }

    /**
     * 质检
     *
     * @param inboundId 入库单 ID
     * @param itemId 明细 ID
     * @param qualifiedQty 合格数量
     * @param unqualifiedQty 不合格数量
     * @return 结果
     */
    @Override
    @Transactional
    public int qualityCheck(Long inboundId, Long itemId, BigDecimal qualifiedQty, BigDecimal unqualifiedQty)
    {
        WmsInboundItem item = wmsInboundMapper.selectWmsInboundItemByItemId(itemId);
        if (item == null) {
            throw new RuntimeException("入库明细不存在");
        }

        item.setQualifiedQty(qualifiedQty);
        item.setUnqualifiedQty(unqualifiedQty);
        item.setStatus("3");
        wmsInboundMapper.updateWmsInboundItem(item);

        // 更新入库单状态
        WmsInboundOrder order = wmsInboundMapper.selectInboundOrderById(inboundId);
        if (order != null) {
            order.setQualifiedQty(order.getQualifiedQty() != null ? order.getQualifiedQty().add(qualifiedQty) : qualifiedQty);
            order.setUnqualifiedQty(order.getUnqualifiedQty() != null ? order.getUnqualifiedQty().add(unqualifiedQty) : unqualifiedQty);
            order.setStatus("4");
            wmsInboundMapper.updateInboundOrder(order);
        }

        return 1;
    }

    /**
     * 上架
     *
     * @param inboundId 入库单 ID
     * @param itemId 明细 ID
     * @param locationId 库位 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int putAway(Long inboundId, Long itemId, Long locationId)
    {
        WmsInboundItem item = wmsInboundMapper.selectWmsInboundItemByItemId(itemId);
        if (item == null) {
            throw new RuntimeException("入库明细不存在");
        }

        item.setLocationId(locationId);
        item.setStatus("4");
        wmsInboundMapper.updateWmsInboundItem(item);

        // 增加库存
        WmsInboundOrder order = wmsInboundMapper.selectInboundOrderById(inboundId);
        if (order != null) {
            // 调用库存服务增加库存
            // 这里简化处理，实际需要调用 WmsInventoryService
        }

        // 检查是否所有明细都已上架，如果是则更新入库单状态为已完成
        List<WmsInboundItem> items = wmsInboundMapper.selectWmsInboundItemByInboundId(inboundId);
        boolean allPutAway = true;
        for (WmsInboundItem i : items) {
            if (!"4".equals(i.getStatus())) {
                allPutAway = false;
                break;
            }
        }

        if (allPutAway && order != null) {
            order.setStatus("5");
            wmsInboundMapper.updateInboundOrder(order);
        }

        return 1;
    }

    /**
     * 完成入库单（草稿转正式）
     *
     * @param inboundId 入库单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int finishInboundOrder(Long inboundId)
    {
        WmsInboundOrder order = wmsInboundMapper.selectInboundOrderById(inboundId);
        if (order == null) {
            throw new RuntimeException("入库单不存在");
        }

        // 检查是否已完成
        if (order.getIsFinished() != null && order.getIsFinished() == 1) {
            throw new RuntimeException("入库单已完成，无法重复操作");
        }

        // 获取入库明细
        List<WmsInboundItem> items = wmsInboundMapper.selectWmsInboundItemByInboundId(inboundId);
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("入库单没有明细，无法完成");
        }

        // 遍历明细，更新库存
        for (WmsInboundItem item : items) {
            WmsInventory inventory = new WmsInventory();
            inventory.setProductId(item.getProductId());
            inventory.setProductCode(item.getProductCode());
            inventory.setProductName(item.getProductName());
            inventory.setWarehouseId(order.getWarehouseId());
            inventory.setLocationId(item.getLocationId());
            inventory.setBatchNo(item.getBatchNo());
            inventory.setProductionDate(item.getProductionDate());
            inventory.setExpiryDate(item.getExpiryDate());

            BigDecimal receivedQty = item.getReceivedQty() != null ? item.getReceivedQty() : item.getExpectedQty();
            if (receivedQty != null && receivedQty.compareTo(BigDecimal.ZERO) > 0) {
                // 增加库存并记录履历
                inventoryService.increaseStockWithLedger(
                    inventory,
                    receivedQty,
                    "wms_inbound_order",
                    inboundId,
                    order.getInboundNo()
                );
            }
        }

        // 设置为已完成
        order.setIsFinished(1);
        // 设置状态为已入库
        order.setStatus("5");
        order.setActualDate(new Date());
        wmsInboundMapper.updateInboundOrder(order);

        return 1;
    }
}
