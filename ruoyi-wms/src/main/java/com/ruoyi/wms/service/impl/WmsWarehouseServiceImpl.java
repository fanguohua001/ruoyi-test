package com.ruoyi.wms.service.impl;

import java.util.List;
import com.ruoyi.wms.domain.WmsWarehouse;
import com.ruoyi.wms.mapper.WmsWarehouseMapper;
import com.ruoyi.wms.service.IWmsWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 仓库 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsWarehouseServiceImpl implements IWmsWarehouseService
{
    @Autowired
    private WmsWarehouseMapper wmsWarehouseMapper;

    /**
     * 查询仓库信息
     *
     * @param warehouseId 仓库 ID
     * @return 仓库信息
     */
    @Override
    public WmsWarehouse selectWmsWarehouseById(Long warehouseId)
    {
        return wmsWarehouseMapper.selectWmsWarehouseById(warehouseId);
    }

    /**
     * 查询仓库列表
     *
     * @param wmsWarehouse 仓库信息
     * @return 仓库
     */
    @Override
    public List<WmsWarehouse> selectWmsWarehouseList(WmsWarehouse wmsWarehouse)
    {
        return wmsWarehouseMapper.selectWmsWarehouseList(wmsWarehouse);
    }

    /**
     * 查询所有有效的仓库列表（用于下拉选择）
     *
     * @return 仓库集合
     */
    @Override
    public List<WmsWarehouse> selectWmsWarehouseAll()
    {
        return wmsWarehouseMapper.selectWmsWarehouseAll();
    }

    /**
     * 新增仓库
     *
     * @param wmsWarehouse 仓库信息
     * @return 结果
     */
    @Override
    public int insertWmsWarehouse(WmsWarehouse wmsWarehouse)
    {
        return wmsWarehouseMapper.insertWmsWarehouse(wmsWarehouse);
    }

    /**
     * 修改仓库
     *
     * @param wmsWarehouse 仓库信息
     * @return 结果
     */
    @Override
    public int updateWmsWarehouse(WmsWarehouse wmsWarehouse)
    {
        return wmsWarehouseMapper.updateWmsWarehouse(wmsWarehouse);
    }

    /**
     * 批量删除仓库
     *
     * @param warehouseIds 需要删除的仓库 ID
     * @return 结果
     */
    @Override
    public int deleteWmsWarehouseByIds(Long[] warehouseIds)
    {
        return wmsWarehouseMapper.deleteWmsWarehouseByIds(warehouseIds);
    }

    /**
     * 删除仓库信息
     *
     * @param warehouseId 仓库 ID
     * @return 结果
     */
    @Override
    public int deleteWmsWarehouseById(Long warehouseId)
    {
        return wmsWarehouseMapper.deleteWmsWarehouseById(warehouseId);
    }

    /**
     * 校验仓库编码是否唯一
     *
     * @param wmsWarehouse 仓库信息
     * @return 结果
     */
    @Override
    public boolean checkWarehouseCodeUnique(WmsWarehouse wmsWarehouse)
    {
        Long warehouseId = wmsWarehouse.getWarehouseId() == null ? -1L : wmsWarehouse.getWarehouseId();
        WmsWarehouse info = wmsWarehouseMapper.checkWarehouseCodeUnique(wmsWarehouse.getWarehouseCode());
        if (info != null && info.getWarehouseId().longValue() != warehouseId.longValue())
        {
            return false;
        }
        return true;
    }
}
