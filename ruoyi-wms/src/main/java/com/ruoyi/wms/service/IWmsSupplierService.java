package com.ruoyi.wms.service;

import java.util.List;
import com.ruoyi.wms.domain.WmsSupplier;

/**
 * 供应商 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsSupplierService
{
    /**
     * 查询供应商信息
     *
     * @param supplierId 供应商 ID
     * @return 供应商信息
     */
    public WmsSupplier selectWmsSupplierById(Long supplierId);

    /**
     * 查询供应商列表
     *
     * @param wmsSupplier 供应商信息
     * @return 供应商集合
     */
    public List<WmsSupplier> selectWmsSupplierList(WmsSupplier wmsSupplier);

    /**
     * 查询所有有效的供应商列表（用于下拉选择）
     *
     * @return 供应商集合
     */
    public List<WmsSupplier> selectWmsSupplierAll();

    /**
     * 新增供应商
     *
     * @param wmsSupplier 供应商信息
     * @return 结果
     */
    public int insertWmsSupplier(WmsSupplier wmsSupplier);

    /**
     * 修改供应商
     *
     * @param wmsSupplier 供应商信息
     * @return 结果
     */
    public int updateWmsSupplier(WmsSupplier wmsSupplier);

    /**
     * 批量删除供应商
     *
     * @param supplierIds 需要删除的供应商 ID 数组
     * @return 结果
     */
    public int deleteWmsSupplierByIds(Long[] supplierIds);

    /**
     * 删除供应商信息
     *
     * @param supplierId 供应商 ID
     * @return 结果
     */
    public int deleteWmsSupplierById(Long supplierId);

    /**
     * 校验供应商编码是否唯一
     *
     * @param wmsSupplier 供应商信息
     * @return 结果
     */
    public boolean checkSupplierCodeUnique(WmsSupplier wmsSupplier);
}
