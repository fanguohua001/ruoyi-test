package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsCategory;

/**
 * 物料分类 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsCategoryMapper
{
    /**
     * 查询物料分类信息
     *
     * @param categoryId 分类 ID
     * @return 物料分类信息
     */
    public WmsCategory selectWmsCategoryById(Long categoryId);

    /**
     * 查询物料分类列表
     *
     * @param wmsCategory 物料分类信息
     * @return 物料分类集合
     */
    public List<WmsCategory> selectWmsCategoryList(WmsCategory wmsCategory);

    /**
     * 查询所有有效的分类列表（用于下拉选择）
     *
     * @return 物料分类集合
     */
    public List<WmsCategory> selectWmsCategoryAll();

    /**
     * 新增物料分类
     *
     * @param wmsCategory 物料分类信息
     * @return 结果
     */
    public int insertWmsCategory(WmsCategory wmsCategory);

    /**
     * 修改物料分类
     *
     * @param wmsCategory 物料分类信息
     * @return 结果
     */
    public int updateWmsCategory(WmsCategory wmsCategory);

    /**
     * 删除物料分类
     *
     * @param categoryId 物料分类 ID
     * @return 结果
     */
    public int deleteWmsCategoryById(Long categoryId);

    /**
     * 批量删除物料分类
     *
     * @param categoryIds 需要删除的物料分类 ID 数组
     * @return 结果
     */
    public int deleteWmsCategoryByIds(Long[] categoryIds);

    /**
     * 校验分类编码唯一性
     *
     * @param categoryCode 分类编码
     * @return 物料分类信息
     */
    public WmsCategory checkCategoryCodeUnique(String categoryCode);
}
