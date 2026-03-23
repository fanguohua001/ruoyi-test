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
import com.ruoyi.wms.domain.WmsSupplier;
import com.ruoyi.wms.service.IWmsSupplierService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 供应商 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/supplier")
public class WmsSupplierController extends BaseController
{
    @Autowired
    private IWmsSupplierService wmsSupplierService;

    /**
     * 查询供应商列表
     */
    @PreAuthorize("@ss.hasPermi('wms:supplier:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsSupplier wmsSupplier)
    {
        startPage();
        List<WmsSupplier> list = wmsSupplierService.selectWmsSupplierList(wmsSupplier);
        return getDataTable(list);
    }

    /**
     * 查询所有有效的供应商列表（用于下拉选择）
     */
    @PreAuthorize("@ss.hasPermi('wms:supplier:list')")
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<WmsSupplier> list = wmsSupplierService.selectWmsSupplierAll();
        return success(list);
    }

    /**
     * 导出供应商列表
     */
    @PreAuthorize("@ss.hasPermi('wms:supplier:export')")
    @Log(title = "供应商", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsSupplier wmsSupplier)
    {
        List<WmsSupplier> list = wmsSupplierService.selectWmsSupplierList(wmsSupplier);
        ExcelUtil<WmsSupplier> util = new ExcelUtil<WmsSupplier>(WmsSupplier.class);
        util.exportExcel(response, list, "供应商数据");
    }

    /**
     * 获取供应商详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:supplier:query')")
    @GetMapping(value = "/{supplierId}")
    public AjaxResult getInfo(@PathVariable("supplierId") Long supplierId)
    {
        return success(wmsSupplierService.selectWmsSupplierById(supplierId));
    }

    /**
     * 新增供应商
     */
    @PreAuthorize("@ss.hasPermi('wms:supplier:add')")
    @Log(title = "供应商", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsSupplier wmsSupplier)
    {
        if (!wmsSupplierService.checkSupplierCodeUnique(wmsSupplier))
        {
            return error("新增供应商'" + wmsSupplier.getSupplierName() + "'失败，供应商编码已存在");
        }
        wmsSupplier.setCreateBy(getUsername());
        return toAjax(wmsSupplierService.insertWmsSupplier(wmsSupplier));
    }

    /**
     * 修改供应商
     */
    @PreAuthorize("@ss.hasPermi('wms:supplier:edit')")
    @Log(title = "供应商", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsSupplier wmsSupplier)
    {
        if (!wmsSupplierService.checkSupplierCodeUnique(wmsSupplier))
        {
            return error("修改供应商'" + wmsSupplier.getSupplierName() + "'失败，供应商编码已存在");
        }
        wmsSupplier.setUpdateBy(getUsername());
        return toAjax(wmsSupplierService.updateWmsSupplier(wmsSupplier));
    }

    /**
     * 删除供应商
     */
    @PreAuthorize("@ss.hasPermi('wms:supplier:remove')")
    @Log(title = "供应商", businessType = BusinessType.DELETE)
	@DeleteMapping("/{supplierIds}")
    public AjaxResult remove(@PathVariable Long[] supplierIds)
    {
        return toAjax(wmsSupplierService.deleteWmsSupplierByIds(supplierIds));
    }
}
