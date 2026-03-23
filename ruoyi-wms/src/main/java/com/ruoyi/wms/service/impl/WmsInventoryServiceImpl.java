package com.ruoyi.wms.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.wms.domain.WmsInventory;
import com.ruoyi.wms.domain.WmsProduct;
import com.ruoyi.wms.mapper.WmsInventoryMapper;
import com.ruoyi.wms.mapper.WmsProductMapper;
import com.ruoyi.wms.service.IWmsInventoryService;
import com.ruoyi.wms.service.IWmsInventoryLedgerService;

/**
 * 库存 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsInventoryServiceImpl implements IWmsInventoryService
{
    @Autowired
    private WmsInventoryMapper wmsInventoryMapper;

    @Autowired
    private WmsProductMapper wmsProductMapper;

    @Autowired
    private IWmsInventoryLedgerService inventoryLedgerService;

    /**
     * 查询库存信息
     *
     * @param inventoryId 库存 ID
     * @return 库存信息
     */
    @Override
    public WmsInventory selectWmsInventoryById(Long inventoryId)
    {
        return wmsInventoryMapper.selectInventoryById(inventoryId);
    }

    /**
     * 查询库存列表
     *
     * @param wmsInventory 库存信息
     * @return 库存
     */
    @Override
    public List<WmsInventory> selectWmsInventoryList(WmsInventory wmsInventory)
    {
        return wmsInventoryMapper.selectInventoryList(wmsInventory);
    }

    /**
     * 查询实时库存数量
     *
     * @param productId 物料 ID
     * @param warehouseId 仓库 ID
     * @return 库存数量
     */
    @Override
    public BigDecimal getStockQty(Long productId, Long warehouseId)
    {
        return wmsInventoryMapper.selectQtyByProduct(productId, warehouseId);
    }

    /**
     * 库存增加
     *
     * @param wmsInventory 库存信息
     * @param qty 数量
     * @return 结果
     */
    @Override
    @Transactional
    public int increaseStock(WmsInventory wmsInventory, BigDecimal qty)
    {
        WmsInventory inventory = wmsInventoryMapper.selectInventoryByParams(
            wmsInventory.getProductId(),
            wmsInventory.getWarehouseId(),
            wmsInventory.getLocationId(),
            wmsInventory.getBatchNo()
        );

        if (inventory == null) {
            // 新增库存记录
            WmsProduct product = wmsProductMapper.selectProductById(wmsInventory.getProductId());
            wmsInventory.setProductName(product != null ? product.getProductName() : "");
            wmsInventory.setProductCode(product != null ? product.getProductCode() : "");
            wmsInventory.setQtyOnHand(qty);
            wmsInventory.setQtyAvailable(qty);
            wmsInventory.setQtyLocked(BigDecimal.ZERO);
            wmsInventory.setQtyDamaged(BigDecimal.ZERO);
            wmsInventory.setUnitCost(BigDecimal.ZERO);
            wmsInventory.setTotalCost(BigDecimal.ZERO);
            wmsInventory.setStatus("1");
            wmsInventory.setCreateTime(new Date());
            return wmsInventoryMapper.insertInventory(wmsInventory);
        } else {
            // 更新库存记录
            BigDecimal newQtyOnHand = inventory.getQtyOnHand().add(qty);
            BigDecimal newQtyAvailable = inventory.getQtyAvailable().add(qty);
            inventory.setQtyOnHand(newQtyOnHand);
            inventory.setQtyAvailable(newQtyAvailable);
            inventory.setUpdateTime(new Date());
            return wmsInventoryMapper.updateInventory(inventory);
        }
    }

    /**
     * 库存减少
     *
     * @param wmsInventory 库存信息
     * @param qty 数量
     * @return 结果
     */
    @Override
    @Transactional
    public int decreaseStock(WmsInventory wmsInventory, BigDecimal qty)
    {
        WmsInventory inventory = wmsInventoryMapper.selectInventoryByParams(
            wmsInventory.getProductId(),
            wmsInventory.getWarehouseId(),
            wmsInventory.getLocationId(),
            wmsInventory.getBatchNo()
        );

        if (inventory == null) {
            throw new RuntimeException("库存记录不存在");
        }

        BigDecimal newQtyOnHand = inventory.getQtyOnHand().subtract(qty);
        BigDecimal newQtyAvailable = inventory.getQtyAvailable().subtract(qty);

        if (newQtyOnHand.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("库存不足");
        }

        inventory.setQtyOnHand(newQtyOnHand);
        inventory.setQtyAvailable(newQtyAvailable);
        inventory.setUpdateTime(new Date());
        return wmsInventoryMapper.updateInventory(inventory);
    }

    /**
     * 库存锁定
     *
     * @param inventoryId 库存 ID
     * @param qty 锁定数量
     * @return 结果
     */
    @Override
    @Transactional
    public int lockStock(Long inventoryId, BigDecimal qty)
    {
        WmsInventory inventory = wmsInventoryMapper.selectInventoryById(inventoryId);
        if (inventory == null) {
            throw new RuntimeException("库存记录不存在");
        }

        if (inventory.getQtyAvailable().compareTo(qty) < 0) {
            throw new RuntimeException("可用库存不足");
        }

        BigDecimal newQtyAvailable = inventory.getQtyAvailable().subtract(qty);
        BigDecimal newQtyLocked = (inventory.getQtyLocked() != null ? inventory.getQtyLocked() : BigDecimal.ZERO).add(qty);

        inventory.setQtyAvailable(newQtyAvailable);
        inventory.setQtyLocked(newQtyLocked);
        inventory.setUpdateTime(new Date());
        return wmsInventoryMapper.updateInventory(inventory);
    }

    /**
     * 库存解锁
     *
     * @param inventoryId 库存 ID
     * @param qty 解锁数量
     * @return 结果
     */
    @Override
    @Transactional
    public int unlockStock(Long inventoryId, BigDecimal qty)
    {
        WmsInventory inventory = wmsInventoryMapper.selectInventoryById(inventoryId);
        if (inventory == null) {
            throw new RuntimeException("库存记录不存在");
        }

        BigDecimal newQtyAvailable = inventory.getQtyAvailable().add(qty);
        BigDecimal newQtyLocked = inventory.getQtyLocked().subtract(qty);

        if (newQtyLocked.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("解锁数量超过锁定数量");
        }

        inventory.setQtyAvailable(newQtyAvailable);
        inventory.setQtyLocked(newQtyLocked);
        inventory.setUpdateTime(new Date());
        return wmsInventoryMapper.updateInventory(inventory);
    }

    /**
     * 库存冻结
     *
     * @param inventoryId 库存 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int freezeStock(Long inventoryId)
    {
        WmsInventory inventory = wmsInventoryMapper.selectInventoryById(inventoryId);
        if (inventory == null) {
            throw new RuntimeException("库存记录不存在");
        }

        inventory.setStatus("3");
        inventory.setUpdateTime(new Date());
        return wmsInventoryMapper.updateInventory(inventory);
    }

    /**
     * 库存解冻
     *
     * @param inventoryId 库存 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int unfreezeStock(Long inventoryId)
    {
        WmsInventory inventory = wmsInventoryMapper.selectInventoryById(inventoryId);
        if (inventory == null) {
            throw new RuntimeException("库存记录不存在");
        }

        inventory.setStatus("1");
        inventory.setUpdateTime(new Date());
        return wmsInventoryMapper.updateInventory(inventory);
    }

    /**
     * 检查库存预警
     *
     * @return 预警列表
     */
    @Override
    public List<WmsInventory> checkStockAlert()
    {
        return wmsInventoryMapper.selectInventoryLowStock();
    }

    /**
     * 入库增加库存（带履历记录）
     *
     * @param wmsInventory 库存信息
     * @param qty 数量
     * @param referenceType 关联单据类型
     * @param referenceId 关联单据 ID
     * @param referenceNo 关联单据号
     * @return 结果
     */
    @Override
    @Transactional
    public int increaseStockWithLedger(WmsInventory wmsInventory, BigDecimal qty,
                                        String referenceType, Long referenceId, String referenceNo)
    {
        // 先更新库存
        int result = increaseStock(wmsInventory, qty);

        // 记录库存履历
        WmsInventory inventory = wmsInventoryMapper.selectInventoryByParams(
            wmsInventory.getProductId(),
            wmsInventory.getWarehouseId(),
            wmsInventory.getLocationId(),
            wmsInventory.getBatchNo()
        );

        // 如果查询不到库存记录，使用传入的参数
        if (inventory == null) {
            inventory = wmsInventory;
        }

        inventoryLedgerService.recordTransaction(
            "1", // 交易类型：1 入库
            inventory.getWarehouseId(),
            inventory.getLocationId(),
            inventory.getProductId(),
            inventory.getProductCode(),
            inventory.getProductName(),
            inventory.getBatchNo(),
            qty, // 变动数量（正数）
            inventory.getUnitCost() != null ? inventory.getUnitCost() : BigDecimal.ZERO,
            referenceType,
            referenceId,
            referenceNo,
            "入库单增加库存"
        );

        return result;
    }

    /**
     * 增加库存（带履历记录）- 调试版本
     */
    public int increaseStockWithLedgerDebug(WmsInventory wmsInventory, BigDecimal qty,
                                        String referenceType, Long referenceId, String referenceNo)
    {
        System.out.println("=== 开始增加库存 ===");
        System.out.println("Product ID: " + wmsInventory.getProductId());
        System.out.println("Warehouse ID: " + wmsInventory.getWarehouseId());
        System.out.println("Location ID: " + wmsInventory.getLocationId());
        System.out.println("Quantity: " + qty);
        System.out.println("Reference: " + referenceType + " / " + referenceId + " / " + referenceNo);

        int result = increaseStock(wmsInventory, qty);
        System.out.println("库存更新结果：" + result);

        WmsInventory inventory = wmsInventoryMapper.selectInventoryByParams(
            wmsInventory.getProductId(),
            wmsInventory.getWarehouseId(),
            wmsInventory.getLocationId(),
            wmsInventory.getBatchNo()
        );

        if (inventory == null) {
            inventory = wmsInventory;
        }

        inventoryLedgerService.recordTransaction(
            "1",
            inventory.getWarehouseId(),
            inventory.getLocationId(),
            inventory.getProductId(),
            inventory.getProductCode(),
            inventory.getProductName(),
            inventory.getBatchNo(),
            qty,
            inventory.getUnitCost() != null ? inventory.getUnitCost() : BigDecimal.ZERO,
            referenceType,
            referenceId,
            referenceNo,
            "入库单增加库存"
        );

        System.out.println("=== 库存增加完成 ===");
        return result;
    }

    /**
     * 出库减少库存（带履历记录）
     *
     * @param wmsInventory 库存信息
     * @param qty 数量
     * @param referenceType 关联单据类型
     * @param referenceId 关联单据 ID
     * @param referenceNo 关联单据号
     * @return 结果
     */
    @Override
    @Transactional
    public int decreaseStockWithLedger(WmsInventory wmsInventory, BigDecimal qty,
                                        String referenceType, Long referenceId, String referenceNo)
    {
        // 先更新库存
        int result = decreaseStock(wmsInventory, qty);

        // 记录库存履历
        WmsInventory inventory = wmsInventoryMapper.selectInventoryByParams(
            wmsInventory.getProductId(),
            wmsInventory.getWarehouseId(),
            wmsInventory.getLocationId(),
            wmsInventory.getBatchNo()
        );

        // 如果查询不到库存记录，使用传入的参数
        if (inventory == null) {
            // 出库时库存记录不存在，说明库存不足，抛出异常
            throw new RuntimeException("库存记录不存在，无法出库");
        }

        inventoryLedgerService.recordTransaction(
            "2", // 交易类型：2 出库
            inventory.getWarehouseId(),
            inventory.getLocationId(),
            inventory.getProductId(),
            inventory.getProductCode(),
            inventory.getProductName(),
            inventory.getBatchNo(),
            qty.negate(), // 变动数量（负数）
            inventory.getUnitCost() != null ? inventory.getUnitCost() : BigDecimal.ZERO,
            referenceType,
            referenceId,
            referenceNo,
            "出库单减少库存"
        );

        return result;
    }
}
