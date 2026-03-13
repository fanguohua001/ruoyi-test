package com.ruoyi.wms.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.wms.domain.WmsLocation;
import com.ruoyi.wms.mapper.WmsLocationMapper;
import com.ruoyi.wms.service.IWmsLocationService;

/**
 * 库位 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsLocationServiceImpl implements IWmsLocationService
{
    @Autowired
    private WmsLocationMapper wmsLocationMapper;

    /**
     * 查询库位信息
     *
     * @param locationId 库位 ID
     * @return 库位信息
     */
    @Override
    public WmsLocation selectWmsLocationById(Long locationId)
    {
        return wmsLocationMapper.selectLocationById(locationId);
    }

    /**
     * 查询库位列表
     *
     * @param wmsLocation 库位信息
     * @return 库位
     */
    @Override
    public List<WmsLocation> selectWmsLocationList(WmsLocation wmsLocation)
    {
        return wmsLocationMapper.selectLocationList(wmsLocation);
    }

    /**
     * 新增库位信息
     *
     * @param wmsLocation 库位信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertWmsLocation(WmsLocation wmsLocation)
    {
        return wmsLocationMapper.insertLocation(wmsLocation);
    }

    /**
     * 修改库位信息
     *
     * @param wmsLocation 库位信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateWmsLocation(WmsLocation wmsLocation)
    {
        return wmsLocationMapper.updateLocation(wmsLocation);
    }

    /**
     * 删除库位信息
     *
     * @param locationIds 需要删除的库位 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsLocationByIds(Long[] locationIds)
    {
        return wmsLocationMapper.deleteLocationByIds(locationIds);
    }

    /**
     * 删除库位信息
     *
     * @param locationId 库位 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsLocationById(Long locationId)
    {
        return wmsLocationMapper.deleteLocationById(locationId);
    }

    /**
     * 校验库位编码是否唯一
     *
     * @param wmsLocation 库位信息
     * @return 结果
     */
    @Override
    public boolean checkLocationCodeUnique(WmsLocation wmsLocation)
    {
        Long locationId = wmsLocation.getLocationId() == null ? -1L : wmsLocation.getLocationId();
        WmsLocation info = wmsLocationMapper.checkLocationCodeUnique(wmsLocation.getLocationCode());
        if (info != null && info.getLocationId().longValue() != locationId.longValue())
        {
            return false;
        }
        return true;
    }
}
