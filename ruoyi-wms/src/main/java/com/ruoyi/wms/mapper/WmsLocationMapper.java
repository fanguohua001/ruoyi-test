package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsLocation;

/**
 * 库位 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsLocationMapper
{
    /**
     * 查询库位信息
     *
     * @param locationId 库位 ID
     * @return 库位信息
     */
    public WmsLocation selectLocationById(Long locationId);

    /**
     * 查询库位列表
     *
     * @param wmsLocation 库位信息
     * @return 库位集合
     */
    public List<WmsLocation> selectLocationList(WmsLocation wmsLocation);

    /**
     * 根据库位编码查询
     *
     * @param locationCode 库位编码
     * @return 库位信息
     */
    public WmsLocation checkLocationCodeUnique(String locationCode);

    /**
     * 新增库位
     *
     * @param wmsLocation 库位信息
     * @return 结果
     */
    public int insertLocation(WmsLocation wmsLocation);

    /**
     * 修改库位
     *
     * @param wmsLocation 库位信息
     * @return 结果
     */
    public int updateLocation(WmsLocation wmsLocation);

    /**
     * 删除库位
     *
     * @param locationId 库位 ID
     * @return 结果
     */
    public int deleteLocationById(Long locationId);

    /**
     * 批量删除库位
     *
     * @param locationIds 需要删除的库位 ID 数组
     * @return 结果
     */
    public int deleteLocationByIds(Long[] locationIds);
}
