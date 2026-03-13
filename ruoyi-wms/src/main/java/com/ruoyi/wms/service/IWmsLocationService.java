package com.ruoyi.wms.service;

import java.util.List;
import com.ruoyi.wms.domain.WmsLocation;

/**
 * 库位 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsLocationService
{
    /**
     * 查询库位信息
     *
     * @param locationId 库位 ID
     * @return 库位信息
     */
    public WmsLocation selectWmsLocationById(Long locationId);

    /**
     * 查询库位列表
     *
     * @param wmsLocation 库位信息
     * @return 库位集合
     */
    public List<WmsLocation> selectWmsLocationList(WmsLocation wmsLocation);

    /**
     * 新增库位
     *
     * @param wmsLocation 库位信息
     * @return 结果
     */
    public int insertWmsLocation(WmsLocation wmsLocation);

    /**
     * 修改库位
     *
     * @param wmsLocation 库位信息
     * @return 结果
     */
    public int updateWmsLocation(WmsLocation wmsLocation);

    /**
     * 批量删除库位
     *
     * @param locationIds 需要删除的库位 ID 数组
     * @return 结果
     */
    public int deleteWmsLocationByIds(Long[] locationIds);

    /**
     * 删除库位信息
     *
     * @param locationId 库位 ID
     * @return 结果
     */
    public int deleteWmsLocationById(Long locationId);

    /**
     * 校验库位编码是否唯一
     *
     * @param wmsLocation 库位信息
     * @return 结果
     */
    public boolean checkLocationCodeUnique(WmsLocation wmsLocation);
}
