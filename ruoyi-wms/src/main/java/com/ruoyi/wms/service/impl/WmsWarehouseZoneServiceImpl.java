package com.ruoyi.wms.service.impl;

import java.util.List;
import com.ruoyi.wms.domain.WmsWarehouseZone;
import com.ruoyi.wms.mapper.WmsWarehouseZoneMapper;
import com.ruoyi.wms.service.IWmsWarehouseZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 库区 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsWarehouseZoneServiceImpl implements IWmsWarehouseZoneService
{
    @Autowired
    private WmsWarehouseZoneMapper wmsWarehouseZoneMapper;

    /**
     * 查询库区信息
     *
     * @param zoneId 库区 ID
     * @return 库区信息
     */
    @Override
    public WmsWarehouseZone selectWmsWarehouseZoneById(Long zoneId)
    {
        return wmsWarehouseZoneMapper.selectWmsWarehouseZoneById(zoneId);
    }

    /**
     * 查询库区列表
     *
     * @param wmsWarehouseZone 库区信息
     * @return 库区
     */
    @Override
    public List<WmsWarehouseZone> selectWmsWarehouseZoneList(WmsWarehouseZone wmsWarehouseZone)
    {
        return wmsWarehouseZoneMapper.selectWmsWarehouseZoneList(wmsWarehouseZone);
    }

    /**
     * 查询所有有效的库区列表（用于下拉选择）
     *
     * @return 库区集合
     */
    @Override
    public List<WmsWarehouseZone> selectWmsWarehouseZoneAll()
    {
        return wmsWarehouseZoneMapper.selectWmsWarehouseZoneAll();
    }

    /**
     * 根据仓库 ID 查询库区列表
     *
     * @param warehouseId 仓库 ID
     * @return 库区集合
     */
    @Override
    public List<WmsWarehouseZone> selectWmsWarehouseZoneByWarehouseId(Long warehouseId)
    {
        return wmsWarehouseZoneMapper.selectWmsWarehouseZoneByWarehouseId(warehouseId);
    }

    /**
     * 新增库区
     *
     * @param wmsWarehouseZone 库区信息
     * @return 结果
     */
    @Override
    public int insertWmsWarehouseZone(WmsWarehouseZone wmsWarehouseZone)
    {
        return wmsWarehouseZoneMapper.insertWmsWarehouseZone(wmsWarehouseZone);
    }

    /**
     * 修改库区
     *
     * @param wmsWarehouseZone 库区信息
     * @return 结果
     */
    @Override
    public int updateWmsWarehouseZone(WmsWarehouseZone wmsWarehouseZone)
    {
        return wmsWarehouseZoneMapper.updateWmsWarehouseZone(wmsWarehouseZone);
    }

    /**
     * 批量删除库区
     *
     * @param zoneIds 需要删除的库区 ID
     * @return 结果
     */
    @Override
    public int deleteWmsWarehouseZoneByIds(Long[] zoneIds)
    {
        return wmsWarehouseZoneMapper.deleteWmsWarehouseZoneByIds(zoneIds);
    }

    /**
     * 删除库区信息
     *
     * @param zoneId 库区 ID
     * @return 结果
     */
    @Override
    public int deleteWmsWarehouseZoneById(Long zoneId)
    {
        return wmsWarehouseZoneMapper.deleteWmsWarehouseZoneById(zoneId);
    }

    /**
     * 校验库区编码是否唯一
     *
     * @param wmsWarehouseZone 库区信息
     * @return 结果
     */
    @Override
    public boolean checkZoneCodeUnique(WmsWarehouseZone wmsWarehouseZone)
    {
        Long zoneId = wmsWarehouseZone.getZoneId() == null ? -1L : wmsWarehouseZone.getZoneId();
        WmsWarehouseZone info = wmsWarehouseZoneMapper.checkZoneCodeUnique(wmsWarehouseZone.getZoneCode());
        if (info != null && info.getZoneId().longValue() != zoneId.longValue())
        {
            return false;
        }
        return true;
    }
}
