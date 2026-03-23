package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsWarehouse;

/**
 * 仓库 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsWarehouseMapper
{
    /**
     * 查询仓库信息
     *
     * @param warehouseId 仓库 ID
     * @return 仓库信息
     */
    public WmsWarehouse selectWmsWarehouseById(Long warehouseId);

    /**
     * 查询仓库列表
     *
     * @param wmsWarehouse 仓库信息
     * @return 仓库集合
     */
    public List<WmsWarehouse> selectWmsWarehouseList(WmsWarehouse wmsWarehouse);

    /**
     * 查询所有有效的仓库列表（用于下拉选择）
     *
     * @return 仓库集合
     */
    public List<WmsWarehouse> selectWmsWarehouseAll();

    /**
     * 新增仓库
     *
     * @param wmsWarehouse 仓库信息
     * @return 结果
     */
    public int insertWmsWarehouse(WmsWarehouse wmsWarehouse);

    /**
     * 修改仓库
     *
     * @param wmsWarehouse 仓库信息
     * @return 结果
     */
    public int updateWmsWarehouse(WmsWarehouse wmsWarehouse);

    /**
     * 删除仓库
     *
     * @param warehouseId 仓库 ID
     * @return 结果
     */
    public int deleteWmsWarehouseById(Long warehouseId);

    /**
     * 批量删除仓库
     *
     * @param warehouseIds 需要删除的仓库 ID 数组
     * @return 结果
     */
    public int deleteWmsWarehouseByIds(Long[] warehouseIds);

    /**
     * 校验仓库编码唯一性
     *
     * @param warehouseCode 仓库编码
     * @return 仓库信息
     */
    public WmsWarehouse checkWarehouseCodeUnique(String warehouseCode);
}
