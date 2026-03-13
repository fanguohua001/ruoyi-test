package com.ruoyi.wms.service;

import java.util.List;
import com.ruoyi.wms.domain.WmsProduct;

/**
 * 商品 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsProductService
{
    /**
     * 查询商品信息
     *
     * @param productId 商品 ID
     * @return 商品信息
     */
    public WmsProduct selectWmsProductById(Long productId);

    /**
     * 查询商品列表
     *
     * @param wmsProduct 商品信息
     * @return 商品集合
     */
    public List<WmsProduct> selectWmsProductList(WmsProduct wmsProduct);

    /**
     * 新增商品
     *
     * @param wmsProduct 商品信息
     * @return 结果
     */
    public int insertWmsProduct(WmsProduct wmsProduct);

    /**
     * 修改商品
     *
     * @param wmsProduct 商品信息
     * @return 结果
     */
    public int updateWmsProduct(WmsProduct wmsProduct);

    /**
     * 批量删除商品
     *
     * @param productIds 需要删除的商品 ID 数组
     * @return 结果
     */
    public int deleteWmsProductByIds(Long[] productIds);

    /**
     * 删除商品信息
     *
     * @param productId 商品 ID
     * @return 结果
     */
    public int deleteWmsProductById(Long productId);

    /**
     * 校验商品编码是否唯一
     *
     * @param wmsProduct 商品信息
     * @return 结果
     */
    public boolean checkProductCodeUnique(WmsProduct wmsProduct);
}
