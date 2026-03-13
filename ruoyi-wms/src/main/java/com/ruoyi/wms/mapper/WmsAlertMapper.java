package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsAlert;

/**
 * 库存预警 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsAlertMapper
{
    /**
     * 查询预警信息
     *
     * @param alertId 预警 ID
     * @return 预警信息
     */
    public WmsAlert selectAlertById(Long alertId);

    /**
     * 查询预警列表
     *
     * @param wmsAlert 预警信息
     * @return 预警集合
     */
    public List<WmsAlert> selectAlertList(WmsAlert wmsAlert);

    /**
     * 新增预警
     *
     * @param wmsAlert 预警信息
     * @return 结果
     */
    public int insertAlert(WmsAlert wmsAlert);

    /**
     * 修改预警
     *
     * @param wmsAlert 预警信息
     * @return 结果
     */
    public int updateAlert(WmsAlert wmsAlert);

    /**
     * 删除预警
     *
     * @param alertId 预警 ID
     * @return 结果
     */
    public int deleteAlertById(Long alertId);

    /**
     * 批量删除预警
     *
     * @param alertIds 需要删除的预警 ID 数组
     * @return 结果
     */
    public int deleteAlertByIds(Long[] alertIds);
}
