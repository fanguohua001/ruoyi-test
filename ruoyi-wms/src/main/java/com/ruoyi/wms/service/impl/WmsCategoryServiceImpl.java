package com.ruoyi.wms.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.wms.domain.WmsCategory;
import com.ruoyi.wms.mapper.WmsCategoryMapper;
import com.ruoyi.wms.service.IWmsCategoryService;

/**
 * 物料分类 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsCategoryServiceImpl implements IWmsCategoryService
{
    @Autowired
    private WmsCategoryMapper wmsCategoryMapper;

    /**
     * 查询物料分类信息
     *
     * @param categoryId 分类 ID
     * @return 物料分类信息
     */
    @Override
    public WmsCategory selectWmsCategoryById(Long categoryId)
    {
        return wmsCategoryMapper.selectWmsCategoryById(categoryId);
    }

    /**
     * 查询物料分类列表
     *
     * @param wmsCategory 物料分类信息
     * @return 物料分类
     */
    @Override
    public List<WmsCategory> selectWmsCategoryList(WmsCategory wmsCategory)
    {
        return wmsCategoryMapper.selectWmsCategoryList(wmsCategory);
    }

    /**
     * 查询所有有效的分类列表
     *
     * @return 物料分类集合
     */
    @Override
    public List<WmsCategory> selectWmsCategoryAll()
    {
        return wmsCategoryMapper.selectWmsCategoryAll();
    }

    /**
     * 新增物料分类
     *
     * @param wmsCategory 物料分类信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertWmsCategory(WmsCategory wmsCategory)
    {
        return wmsCategoryMapper.insertWmsCategory(wmsCategory);
    }

    /**
     * 修改物料分类
     *
     * @param wmsCategory 物料分类信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateWmsCategory(WmsCategory wmsCategory)
    {
        return wmsCategoryMapper.updateWmsCategory(wmsCategory);
    }

    /**
     * 批量删除物料分类
     *
     * @param categoryIds 需要删除的物料分类 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsCategoryByIds(Long[] categoryIds)
    {
        return wmsCategoryMapper.deleteWmsCategoryByIds(categoryIds);
    }

    /**
     * 删除物料分类信息
     *
     * @param categoryId 物料分类 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsCategoryById(Long categoryId)
    {
        return wmsCategoryMapper.deleteWmsCategoryById(categoryId);
    }

    /**
     * 校验分类编码是否唯一
     *
     * @param wmsCategory 物料分类信息
     * @return 结果
     */
    @Override
    public boolean checkCategoryCodeUnique(WmsCategory wmsCategory)
    {
        Long categoryId = wmsCategory.getCategoryId() == null ? -1L : wmsCategory.getCategoryId();
        WmsCategory info = wmsCategoryMapper.checkCategoryCodeUnique(wmsCategory.getCategoryCode());
        if (info != null && info.getCategoryId().longValue() != categoryId.longValue())
        {
            return false;
        }
        return true;
    }
}
