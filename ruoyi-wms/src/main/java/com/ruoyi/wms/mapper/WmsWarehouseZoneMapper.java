package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsWarehouseZone;

/**
 * 库区 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsWarehouseZoneMapper
{
    /**
     * 查询库区信息
     *
     * @param zoneId 库区 ID
     * @return 库区信息
     */
    public WmsWarehouseZone selectWmsWarehouseZoneById(Long zoneId);

    /**
     * 查询库区列表
     *
     * @param wmsWarehouseZone 库区信息
     * @return 库区集合
     */
    public List<WmsWarehouseZone> selectWmsWarehouseZoneList(WmsWarehouseZone wmsWarehouseZone);

    /**
     * 查询所有有效的库区列表（用于下拉选择）
     *
     * @return 库区集合
     */
    public List<WmsWarehouseZone> selectWmsWarehouseZoneAll();

    /**
     * 根据仓库 ID 查询库区列表
     *
     * @param warehouseId 仓库 ID
     * @return 库区集合
     */
    public List<WmsWarehouseZone> selectWmsWarehouseZoneByWarehouseId(Long warehouseId);

    /**
     * 新增库区
     *
     * @param wmsWarehouseZone 库区信息
     * @return 结果
     */
    public int insertWmsWarehouseZone(WmsWarehouseZone wmsWarehouseZone);

    /**
     * 修改库区
     *
     * @param wmsWarehouseZone 库区信息
     * @return 结果
     */
    public int updateWmsWarehouseZone(WmsWarehouseZone wmsWarehouseZone);

    /**
     * 删除库区
     *
     * @param zoneId 库区 ID
     * @return 结果
     */
    public int deleteWmsWarehouseZoneById(Long zoneId);

    /**
     * 批量删除库区
     *
     * @param zoneIds 需要删除的库区 ID 数组
     * @return 结果
     */
    public int deleteWmsWarehouseZoneByIds(Long[] zoneIds);

    /**
     * 校验库区编码唯一性
     *
     * @param zoneCode 库区编码
     * @return 库区信息
     */
    public WmsWarehouseZone checkZoneCodeUnique(String zoneCode);
}
