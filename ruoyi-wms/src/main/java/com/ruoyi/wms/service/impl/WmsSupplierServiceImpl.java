package com.ruoyi.wms.service.impl;

import java.util.List;
import com.ruoyi.wms.domain.WmsSupplier;
import com.ruoyi.wms.mapper.WmsSupplierMapper;
import com.ruoyi.wms.service.IWmsSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 供应商 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsSupplierServiceImpl implements IWmsSupplierService
{
    @Autowired
    private WmsSupplierMapper wmsSupplierMapper;

    /**
     * 查询供应商信息
     *
     * @param supplierId 供应商 ID
     * @return 供应商信息
     */
    @Override
    public WmsSupplier selectWmsSupplierById(Long supplierId)
    {
        return wmsSupplierMapper.selectWmsSupplierById(supplierId);
    }

    /**
     * 查询供应商列表
     *
     * @param wmsSupplier 供应商信息
     * @return 供应商
     */
    @Override
    public List<WmsSupplier> selectWmsSupplierList(WmsSupplier wmsSupplier)
    {
        return wmsSupplierMapper.selectWmsSupplierList(wmsSupplier);
    }

    /**
     * 查询所有有效的供应商列表（用于下拉选择）
     *
     * @return 供应商集合
     */
    @Override
    public List<WmsSupplier> selectWmsSupplierAll()
    {
        return wmsSupplierMapper.selectWmsSupplierAll();
    }

    /**
     * 新增供应商
     *
     * @param wmsSupplier 供应商信息
     * @return 结果
     */
    @Override
    public int insertWmsSupplier(WmsSupplier wmsSupplier)
    {
        return wmsSupplierMapper.insertWmsSupplier(wmsSupplier);
    }

    /**
     * 修改供应商
     *
     * @param wmsSupplier 供应商信息
     * @return 结果
     */
    @Override
    public int updateWmsSupplier(WmsSupplier wmsSupplier)
    {
        return wmsSupplierMapper.updateWmsSupplier(wmsSupplier);
    }

    /**
     * 批量删除供应商
     *
     * @param supplierIds 需要删除的供应商 ID
     * @return 结果
     */
    @Override
    public int deleteWmsSupplierByIds(Long[] supplierIds)
    {
        return wmsSupplierMapper.deleteWmsSupplierByIds(supplierIds);
    }

    /**
     * 删除供应商信息
     *
     * @param supplierId 供应商 ID
     * @return 结果
     */
    @Override
    public int deleteWmsSupplierById(Long supplierId)
    {
        return wmsSupplierMapper.deleteWmsSupplierById(supplierId);
    }

    /**
     * 校验供应商编码是否唯一
     *
     * @param wmsSupplier 供应商信息
     * @return 结果
     */
    @Override
    public boolean checkSupplierCodeUnique(WmsSupplier wmsSupplier)
    {
        Long supplierId = wmsSupplier.getSupplierId() == null ? -1L : wmsSupplier.getSupplierId();
        WmsSupplier info = wmsSupplierMapper.checkSupplierCodeUnique(wmsSupplier.getSupplierCode());
        if (info != null && info.getSupplierId().longValue() != supplierId.longValue())
        {
            return false;
        }
        return true;
    }
}
