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
import com.ruoyi.wms.domain.WmsCustomer;
import com.ruoyi.wms.service.IWmsCustomerService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 客户 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/customer")
public class WmsCustomerController extends BaseController
{
    @Autowired
    private IWmsCustomerService wmsCustomerService;

    /**
     * 查询客户列表
     */
    @PreAuthorize("@ss.hasPermi('wms:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsCustomer wmsCustomer)
    {
        startPage();
        List<WmsCustomer> list = wmsCustomerService.selectWmsCustomerList(wmsCustomer);
        return getDataTable(list);
    }

    /**
     * 查询所有有效的客户列表（用于下拉选择）
     */
    @PreAuthorize("@ss.hasPermi('wms:customer:list')")
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<WmsCustomer> list = wmsCustomerService.selectWmsCustomerAll();
        return success(list);
    }

    /**
     * 导出客户列表
     */
    @PreAuthorize("@ss.hasPermi('wms:customer:export')")
    @Log(title = "客户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsCustomer wmsCustomer)
    {
        List<WmsCustomer> list = wmsCustomerService.selectWmsCustomerList(wmsCustomer);
        ExcelUtil<WmsCustomer> util = new ExcelUtil<WmsCustomer>(WmsCustomer.class);
        util.exportExcel(response, list, "客户数据");
    }

    /**
     * 获取客户详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:customer:query')")
    @GetMapping(value = "/{customerId}")
    public AjaxResult getInfo(@PathVariable("customerId") Long customerId)
    {
        return success(wmsCustomerService.selectWmsCustomerById(customerId));
    }

    /**
     * 新增客户
     */
    @PreAuthorize("@ss.hasPermi('wms:customer:add')")
    @Log(title = "客户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsCustomer wmsCustomer)
    {
        if (!wmsCustomerService.checkCustomerCodeUnique(wmsCustomer))
        {
            return error("新增客户'" + wmsCustomer.getCustomerName() + "'失败，客户编码已存在");
        }
        wmsCustomer.setCreateBy(getUsername());
        return toAjax(wmsCustomerService.insertWmsCustomer(wmsCustomer));
    }

    /**
     * 修改客户
     */
    @PreAuthorize("@ss.hasPermi('wms:customer:edit')")
    @Log(title = "客户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsCustomer wmsCustomer)
    {
        if (!wmsCustomerService.checkCustomerCodeUnique(wmsCustomer))
        {
            return error("修改客户'" + wmsCustomer.getCustomerName() + "'失败，客户编码已存在");
        }
        wmsCustomer.setUpdateBy(getUsername());
        return toAjax(wmsCustomerService.updateWmsCustomer(wmsCustomer));
    }

    /**
     * 删除客户
     */
    @PreAuthorize("@ss.hasPermi('wms:customer:remove')")
    @Log(title = "客户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{customerIds}")
    public AjaxResult remove(@PathVariable Long[] customerIds)
    {
        return toAjax(wmsCustomerService.deleteWmsCustomerByIds(customerIds));
    }
}
