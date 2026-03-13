package com.ruoyi.wms.service;

import java.util.List;
import com.ruoyi.wms.domain.WmsStockCheck;

/**
 * 盘点单 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsStockCheckService
{
    /**
     * 查询盘点单信息
     *
     * @param checkId 盘点单 ID
     * @return 盘点单信息
     */
    public WmsStockCheck selectWmsStockCheckById(Long checkId);

    /**
     * 查询盘点单列表
     *
     * @param wmsStockCheck 盘点单信息
     * @return 盘点单集合
     */
    public List<WmsStockCheck> selectWmsStockCheckList(WmsStockCheck wmsStockCheck);

    /**
     * 新增盘点单
     *
     * @param wmsStockCheck 盘点单信息
     * @return 结果
     */
    public int insertWmsStockCheck(WmsStockCheck wmsStockCheck);

    /**
     * 修改盘点单
     *
     * @param wmsStockCheck 盘点单信息
     * @return 结果
     */
    public int updateWmsStockCheck(WmsStockCheck wmsStockCheck);

    /**
     * 批量删除盘点单
     *
     * @param checkIds 需要删除的盘点单 ID 数组
     * @return 结果
     */
    public int deleteWmsStockCheckByIds(Long[] checkIds);

    /**
     * 删除盘点单信息
     *
     * @param checkId 盘点单 ID
     * @return 结果
     */
    public int deleteWmsStockCheckById(Long checkId);

    /**
     * 校验盘点单号是否唯一
     *
     * @param wmsStockCheck 盘点单信息
     * @return 结果
     */
    public boolean checkCheckNoUnique(WmsStockCheck wmsStockCheck);

    /**
     * 开始盘点
     *
     * @param checkId 盘点单 ID
     * @return 结果
     */
    public int startCheck(Long checkId);

    /**
     * 完成盘点
     *
     * @param checkId 盘点单 ID
     * @return 结果
     */
    public int finishCheck(Long checkId);

    /**
     * 审核盘点单
     *
     * @param checkId 盘点单 ID
     * @return 结果
     */
    public int approveCheck(Long checkId);
}
