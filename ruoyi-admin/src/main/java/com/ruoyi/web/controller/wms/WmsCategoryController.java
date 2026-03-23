package com.ruoyi.web.controller.wms;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.wms.domain.WmsCategory;
import com.ruoyi.wms.service.IWmsCategoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 物料分类 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/category")
public class WmsCategoryController extends BaseController
{
    @Autowired
    private IWmsCategoryService wmsCategoryService;

    /**
     * 查询物料分类列表
     */
    @PreAuthorize("@ss.hasPermi('wms:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsCategory wmsCategory)
    {
        startPage();
        List<WmsCategory> list = wmsCategoryService.selectWmsCategoryList(wmsCategory);
        return getDataTable(list);
    }

    /**
     * 查询所有有效的分类列表（用于下拉选择）
     */
    @PreAuthorize("@ss.hasPermi('wms:category:query')")
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<WmsCategory> list = wmsCategoryService.selectWmsCategoryAll();
        return success(list);
    }

    /**
     * 导出物料分类列表
     */
    @PreAuthorize("@ss.hasPermi('wms:category:export')")
    @Log(title = "物料分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsCategory wmsCategory)
    {
        List<WmsCategory> list = wmsCategoryService.selectWmsCategoryList(wmsCategory);
        ExcelUtil<WmsCategory> util = new ExcelUtil<WmsCategory>(WmsCategory.class);
        util.exportExcel(response, list, "物料分类数据");
    }

    /**
     * 获取物料分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:category:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") Long categoryId)
    {
        return success(wmsCategoryService.selectWmsCategoryById(categoryId));
    }

    /**
     * 新增物料分类
     */
    @PreAuthorize("@ss.hasPermi('wms:category:add')")
    @Log(title = "物料分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsCategory wmsCategory)
    {
        if (!wmsCategoryService.checkCategoryCodeUnique(wmsCategory))
        {
            return error("新增分类'" + wmsCategory.getCategoryName() + "'失败，分类编码已存在");
        }
        wmsCategory.setCreateBy(getUsername());
        return toAjax(wmsCategoryService.insertWmsCategory(wmsCategory));
    }

    /**
     * 修改物料分类
     */
    @PreAuthorize("@ss.hasPermi('wms:category:edit')")
    @Log(title = "物料分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsCategory wmsCategory)
    {
        if (!wmsCategoryService.checkCategoryCodeUnique(wmsCategory))
        {
            return error("修改分类'" + wmsCategory.getCategoryName() + "'失败，分类编码已存在");
        }
        wmsCategory.setUpdateBy(getUsername());
        return toAjax(wmsCategoryService.updateWmsCategory(wmsCategory));
    }

    /**
     * 删除物料分类
     */
    @PreAuthorize("@ss.hasPermi('wms:category:remove')")
    @Log(title = "物料分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds)
    {
        return toAjax(wmsCategoryService.deleteWmsCategoryByIds(categoryIds));
    }
}
