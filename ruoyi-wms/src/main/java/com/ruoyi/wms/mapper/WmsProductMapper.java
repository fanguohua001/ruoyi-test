package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsProduct;

/**
 * 商品 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsProductMapper
{
    /**
     * 查询商品信息
     *
     * @param productId 商品 ID
     * @return 商品信息
     */
    public WmsProduct selectProductById(Long productId);

    /**
     * 查询商品列表
     *
     * @param wmsProduct 商品信息
     * @return 商品集合
     */
    public List<WmsProduct> selectProductList(WmsProduct wmsProduct);

    /**
     * 根据商品编码查询
     *
     * @param productCode 商品编码
     * @return 商品信息
     */
    public WmsProduct checkProductCodeUnique(String productCode);

    /**
     * 新增商品
     *
     * @param wmsProduct 商品信息
     * @return 结果
     */
    public int insertProduct(WmsProduct wmsProduct);

    /**
     * 修改商品
     *
     * @param wmsProduct 商品信息
     * @return 结果
     */
    public int updateProduct(WmsProduct wmsProduct);

    /**
     * 删除商品
     *
     * @param productId 商品 ID
     * @return 结果
     */
    public int deleteProductById(Long productId);

    /**
     * 批量删除商品
     *
     * @param productIds 需要删除的商品 ID 数组
     * @return 结果
     */
    public int deleteProductByIds(Long[] productIds);
}
