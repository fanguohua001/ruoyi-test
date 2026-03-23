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
import com.ruoyi.wms.domain.WmsProduct;
import com.ruoyi.wms.service.IWmsProductService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 物料 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/product")
public class WmsProductController extends BaseController
{
    @Autowired
    private IWmsProductService wmsProductService;

    /**
     * 查询物料列表
     */
    @PreAuthorize("@ss.hasPermi('wms:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsProduct wmsProduct)
    {
        startPage();
        List<WmsProduct> list = wmsProductService.selectWmsProductList(wmsProduct);
        return getDataTable(list);
    }

    /**
     * 导出物料列表
     */
    @PreAuthorize("@ss.hasPermi('wms:product:export')")
    @Log(title = "物料管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsProduct wmsProduct)
    {
        List<WmsProduct> list = wmsProductService.selectWmsProductList(wmsProduct);
        ExcelUtil<WmsProduct> util = new ExcelUtil<WmsProduct>(WmsProduct.class);
        util.exportExcel(response, list, "物料数据");
    }

    /**
     * 获取物料详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:product:query')")
    @GetMapping(value = "/{productId}")
    public AjaxResult getInfo(@PathVariable("productId") Long productId)
    {
        return success(wmsProductService.selectWmsProductById(productId));
    }

    /**
     * 新增物料
     */
    @PreAuthorize("@ss.hasPermi('wms:product:add')")
    @Log(title = "物料管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsProduct wmsProduct)
    {
        if (!wmsProductService.checkProductCodeUnique(wmsProduct))
        {
            return error("新增物料'" + wmsProduct.getProductName() + "'失败，物料编码已存在");
        }
        wmsProduct.setCreateBy(getUsername());
        return toAjax(wmsProductService.insertWmsProduct(wmsProduct));
    }

    /**
     * 修改物料
     */
    @PreAuthorize("@ss.hasPermi('wms:product:edit')")
    @Log(title = "物料管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsProduct wmsProduct)
    {
        if (!wmsProductService.checkProductCodeUnique(wmsProduct))
        {
            return error("修改物料'" + wmsProduct.getProductName() + "'失败，物料编码已存在");
        }
        wmsProduct.setUpdateBy(getUsername());
        return toAjax(wmsProductService.updateWmsProduct(wmsProduct));
    }

    /**
     * 删除物料
     */
    @PreAuthorize("@ss.hasPermi('wms:product:remove')")
    @Log(title = "物料管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{productIds}")
    public AjaxResult remove(@PathVariable Long[] productIds)
    {
        return toAjax(wmsProductService.deleteWmsProductByIds(productIds));
    }
}
