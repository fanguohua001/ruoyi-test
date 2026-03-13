package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsStockCheck;

/**
 * 盘点单 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsStockCheckMapper
{
    /**
     * 查询盘点单信息
     *
     * @param checkId 盘点单 ID
     * @return 盘点单信息
     */
    public WmsStockCheck selectStockCheckById(Long checkId);

    /**
     * 查询盘点单列表
     *
     * @param wmsStockCheck 盘点单信息
     * @return 盘点单集合
     */
    public List<WmsStockCheck> selectStockCheckList(WmsStockCheck wmsStockCheck);

    /**
     * 根据盘点单号查询
     *
     * @param checkNo 盘点单号
     * @return 盘点单信息
     */
    public WmsStockCheck checkCheckNoUnique(String checkNo);

    /**
     * 新增盘点单
     *
     * @param wmsStockCheck 盘点单信息
     * @return 结果
     */
    public int insertStockCheck(WmsStockCheck wmsStockCheck);

    /**
     * 修改盘点单
     *
     * @param wmsStockCheck 盘点单信息
     * @return 结果
     */
    public int updateStockCheck(WmsStockCheck wmsStockCheck);

    /**
     * 删除盘点单
     *
     * @param checkId 盘点单 ID
     * @return 结果
     */
    public int deleteStockCheckById(Long checkId);

    /**
     * 批量删除盘点单
     *
     * @param checkIds 需要删除的盘点单 ID 数组
     * @return 结果
     */
    public int deleteStockCheckByIds(Long[] checkIds);
}
