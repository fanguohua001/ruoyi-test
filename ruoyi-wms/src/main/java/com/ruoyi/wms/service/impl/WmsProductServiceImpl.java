package com.ruoyi.wms.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.wms.domain.WmsProduct;
import com.ruoyi.wms.mapper.WmsProductMapper;
import com.ruoyi.wms.service.IWmsProductService;

/**
 * 商品 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsProductServiceImpl implements IWmsProductService
{
    @Autowired
    private WmsProductMapper wmsProductMapper;

    /**
     * 查询商品信息
     *
     * @param productId 商品 ID
     * @return 商品信息
     */
    @Override
    public WmsProduct selectWmsProductById(Long productId)
    {
        return wmsProductMapper.selectProductById(productId);
    }

    /**
     * 查询商品列表
     *
     * @param wmsProduct 商品信息
     * @return 商品
     */
    @Override
    public List<WmsProduct> selectWmsProductList(WmsProduct wmsProduct)
    {
        return wmsProductMapper.selectProductList(wmsProduct);
    }

    /**
     * 新增商品信息
     *
     * @param wmsProduct 商品信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertWmsProduct(WmsProduct wmsProduct)
    {
        return wmsProductMapper.insertProduct(wmsProduct);
    }

    /**
     * 修改商品信息
     *
     * @param wmsProduct 商品信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateWmsProduct(WmsProduct wmsProduct)
    {
        return wmsProductMapper.updateProduct(wmsProduct);
    }

    /**
     * 删除商品信息
     *
     * @param productIds 需要删除的商品 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsProductByIds(Long[] productIds)
    {
        return wmsProductMapper.deleteProductByIds(productIds);
    }

    /**
     * 删除商品信息
     *
     * @param productId 商品 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsProductById(Long productId)
    {
        return wmsProductMapper.deleteProductById(productId);
    }

    /**
     * 校验商品编码是否唯一
     *
     * @param wmsProduct 商品信息
     * @return 结果
     */
    @Override
    public boolean checkProductCodeUnique(WmsProduct wmsProduct)
    {
        Long productId = wmsProduct.getProductId() == null ? -1L : wmsProduct.getProductId();
        WmsProduct info = wmsProductMapper.checkProductCodeUnique(wmsProduct.getProductCode());
        if (info != null && info.getProductId().longValue() != productId.longValue())
        {
            return false;
        }
        return true;
    }
}
