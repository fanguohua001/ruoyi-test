package com.ruoyi.wms.mapper;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.wms.domain.WmsInventory;

/**
 * 库存 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsInventoryMapper
{
    /**
     * 查询库存信息
     *
     * @param inventoryId 库存 ID
     * @return 库存信息
     */
    public WmsInventory selectInventoryById(Long inventoryId);

    /**
     * 查询库存列表
     *
     * @param wmsInventory 库存信息
     * @return 库存集合
     */
    public List<WmsInventory> selectInventoryList(WmsInventory wmsInventory);

    /**
     * 查询实时库存数量
     *
     * @param productId 商品 ID
     * @param warehouseId 仓库 ID
     * @return 库存数量
     */
    public BigDecimal selectQtyByProduct(Long productId, Long warehouseId);

    /**
     * 新增库存
     *
     * @param wmsInventory 库存信息
     * @return 结果
     */
    public int insertInventory(WmsInventory wmsInventory);

    /**
     * 修改库存
     *
     * @param wmsInventory 库存信息
     * @return 结果
     */
    public int updateInventory(WmsInventory wmsInventory);

    /**
     * 删除库存
     *
     * @param inventoryId 库存 ID
     * @return 结果
     */
    public int deleteInventoryById(Long inventoryId);

    /**
     * 批量删除库存
     *
     * @param inventoryIds 需要删除的库存 ID 数组
     * @return 结果
     */
    public int deleteInventoryByIds(Long[] inventoryIds);

    /**
     * 增加库存数量
     *
     * @param wmsInventory 库存信息
     * @return 结果
     */
    public int increaseInventory(WmsInventory wmsInventory);

    /**
     * 减少库存数量
     *
     * @param wmsInventory 库存信息
     * @return 结果
     */
    public int decreaseInventory(WmsInventory wmsInventory);
}
