package com.ruoyi.wms.service;

import java.util.List;
import com.ruoyi.wms.domain.WmsProduct;

/**
 * 物料 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsProductService
{
    /**
     * 查询物料信息
     *
     * @param productId 物料 ID
     * @return 物料信息
     */
    public WmsProduct selectWmsProductById(Long productId);

    /**
     * 查询物料列表
     *
     * @param wmsProduct 物料信息
     * @return 物料集合
     */
    public List<WmsProduct> selectWmsProductList(WmsProduct wmsProduct);

    /**
     * 新增物料
     *
     * @param wmsProduct 物料信息
     * @return 结果
     */
    public int insertWmsProduct(WmsProduct wmsProduct);

    /**
     * 修改物料
     *
     * @param wmsProduct 物料信息
     * @return 结果
     */
    public int updateWmsProduct(WmsProduct wmsProduct);

    /**
     * 批量删除物料
     *
     * @param productIds 需要删除的物料 ID 数组
     * @return 结果
     */
    public int deleteWmsProductByIds(Long[] productIds);

    /**
     * 删除物料信息
     *
     * @param productId 物料 ID
     * @return 结果
     */
    public int deleteWmsProductById(Long productId);

    /**
     * 校验物料编码是否唯一
     *
     * @param wmsProduct 物料信息
     * @return 结果
     */
    public boolean checkProductCodeUnique(WmsProduct wmsProduct);
}
