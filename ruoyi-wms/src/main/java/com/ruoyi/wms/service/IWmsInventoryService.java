package com.ruoyi.wms.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.wms.domain.WmsInventory;

/**
 * 库存 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsInventoryService
{
    /**
     * 查询库存信息
     *
     * @param inventoryId 库存 ID
     * @return 库存信息
     */
    public WmsInventory selectWmsInventoryById(Long inventoryId);

    /**
     * 查询库存列表
     *
     * @param wmsInventory 库存信息
     * @return 库存集合
     */
    public List<WmsInventory> selectWmsInventoryList(WmsInventory wmsInventory);

    /**
     * 查询实时库存
     *
     * @param productId 物料 ID
     * @param warehouseId 仓库 ID
     * @return 库存数量
     */
    public java.math.BigDecimal getStockQty(Long productId, Long warehouseId);

    /**
     * 库存增加
     *
     * @param wmsInventory 库存信息
     * @param qty 数量
     * @return 结果
     */
    public int increaseStock(WmsInventory wmsInventory, java.math.BigDecimal qty);

    /**
     * 库存减少
     *
     * @param wmsInventory 库存信息
     * @param qty 数量
     * @return 结果
     */
    public int decreaseStock(WmsInventory wmsInventory, java.math.BigDecimal qty);

    /**
     * 库存锁定
     *
     * @param inventoryId 库存 ID
     * @param qty 锁定数量
     * @return 结果
     */
    public int lockStock(Long inventoryId, java.math.BigDecimal qty);

    /**
     * 库存解锁
     *
     * @param inventoryId 库存 ID
     * @param qty 解锁数量
     * @return 结果
     */
    public int unlockStock(Long inventoryId, java.math.BigDecimal qty);

    /**
     * 库存冻结
     *
     * @param inventoryId 库存 ID
     * @return 结果
     */
    public int freezeStock(Long inventoryId);

    /**
     * 库存解冻
     *
     * @param inventoryId 库存 ID
     * @return 结果
     */
    public int unfreezeStock(Long inventoryId);

    /**
     * 检查库存预警
     *
     * @return 预警列表
     */
    public List<WmsInventory> checkStockAlert();

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
    public int increaseStockWithLedger(WmsInventory wmsInventory, BigDecimal qty,
                                        String referenceType, Long referenceId, String referenceNo);

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
    public int decreaseStockWithLedger(WmsInventory wmsInventory, BigDecimal qty,
                                        String referenceType, Long referenceId, String referenceNo);
}
