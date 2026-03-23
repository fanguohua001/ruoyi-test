package com.ruoyi.wms.service;

import java.util.List;
import com.ruoyi.wms.domain.WmsAlert;

/**
 * 库存预警 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsAlertService
{
    /**
     * 查询预警信息
     *
     * @param alertId 预警 ID
     * @return 预警信息
     */
    public WmsAlert selectWmsAlertById(Long alertId);

    /**
     * 查询预警列表
     *
     * @param wmsAlert 预警信息
     * @return 预警集合
     */
    public List<WmsAlert> selectWmsAlertList(WmsAlert wmsAlert);

    /**
     * 新增预警
     *
     * @param wmsAlert 预警信息
     * @return 结果
     */
    public int insertWmsAlert(WmsAlert wmsAlert);

    /**
     * 修改预警
     *
     * @param wmsAlert 预警信息
     * @return 结果
     */
    public int updateWmsAlert(WmsAlert wmsAlert);

    /**
     * 批量删除预警
     *
     * @param alertIds 需要删除的预警 ID 数组
     * @return 结果
     */
    public int deleteWmsAlertByIds(Long[] alertIds);

    /**
     * 删除预警信息
     *
     * @param alertId 预警 ID
     * @return 结果
     */
    public int deleteWmsAlertById(Long alertId);

    /**
     * 处理预警
     *
     * @param alertId 预警 ID
     * @param handleBy 处理人
     * @param handleRemark 处理备注
     * @return 结果
     */
    public int handleAlert(Long alertId, String handleBy, String handleRemark);

    /**
     * 生成库存预警
     *
     * @return 结果
     */
    public int generateAlerts();

    /**
     * 检查近效期物料
     *
     * @param days 天数
     * @return 结果
     */
    public int checkNearExpiry(int days);

    /**
     * 检查呆滞料
     *
     * @param days 天数
     * @return 结果
     */
    public int checkSlowMoving(int days);
}
