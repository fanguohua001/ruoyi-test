package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsProduct;

/**
 * 物料 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsProductMapper
{
    /**
     * 查询物料信息
     *
     * @param productId 物料 ID
     * @return 物料信息
     */
    public WmsProduct selectProductById(Long productId);

    /**
     * 查询物料列表
     *
     * @param wmsProduct 物料信息
     * @return 物料集合
     */
    public List<WmsProduct> selectProductList(WmsProduct wmsProduct);

    /**
     * 根据物料编码查询
     *
     * @param productCode 物料编码
     * @return 物料信息
     */
    public WmsProduct checkProductCodeUnique(String productCode);

    /**
     * 新增物料
     *
     * @param wmsProduct 物料信息
     * @return 结果
     */
    public int insertProduct(WmsProduct wmsProduct);

    /**
     * 修改物料
     *
     * @param wmsProduct 物料信息
     * @return 结果
     */
    public int updateProduct(WmsProduct wmsProduct);

    /**
     * 删除物料
     *
     * @param productId 物料 ID
     * @return 结果
     */
    public int deleteProductById(Long productId);

    /**
     * 批量删除物料
     *
     * @param productIds 需要删除的物料 ID 数组
     * @return 结果
     */
    public int deleteProductByIds(Long[] productIds);
}
