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
import com.ruoyi.wms.domain.WmsStockCheck;
import com.ruoyi.wms.service.IWmsStockCheckService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 盘点单 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/stockCheck")
public class WmsStockCheckController extends BaseController
{
    @Autowired
    private IWmsStockCheckService wmsStockCheckService;

    /**
     * 查询盘点单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:stockCheck:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsStockCheck wmsStockCheck)
    {
        startPage();
        List<WmsStockCheck> list = wmsStockCheckService.selectWmsStockCheckList(wmsStockCheck);
        return getDataTable(list);
    }

    /**
     * 导出盘点单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:stockCheck:export')")
    @Log(title = "盘点单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsStockCheck wmsStockCheck)
    {
        List<WmsStockCheck> list = wmsStockCheckService.selectWmsStockCheckList(wmsStockCheck);
        ExcelUtil<WmsStockCheck> util = new ExcelUtil<WmsStockCheck>(WmsStockCheck.class);
        util.exportExcel(response, list, "盘点单数据");
    }

    /**
     * 获取盘点单详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:stockCheck:query')")
    @GetMapping(value = "/{checkId}")
    public AjaxResult getInfo(@PathVariable("checkId") Long checkId)
    {
        return success(wmsStockCheckService.selectWmsStockCheckById(checkId));
    }

    /**
     * 新增盘点单
     */
    @PreAuthorize("@ss.hasPermi('wms:stockCheck:add')")
    @Log(title = "盘点单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsStockCheck wmsStockCheck)
    {
        if (!wmsStockCheckService.checkCheckNoUnique(wmsStockCheck))
        {
            return error("新增盘点单'" + wmsStockCheck.getCheckNo() + "'失败，盘点单号已存在");
        }
        wmsStockCheck.setCreateBy(getUsername());
        return toAjax(wmsStockCheckService.insertWmsStockCheck(wmsStockCheck));
    }

    /**
     * 修改盘点单
     */
    @PreAuthorize("@ss.hasPermi('wms:stockCheck:edit')")
    @Log(title = "盘点单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsStockCheck wmsStockCheck)
    {
        if (!wmsStockCheckService.checkCheckNoUnique(wmsStockCheck))
        {
            return error("修改盘点单'" + wmsStockCheck.getCheckNo() + "'失败，盘点单号已存在");
        }
        wmsStockCheck.setUpdateBy(getUsername());
        return toAjax(wmsStockCheckService.updateWmsStockCheck(wmsStockCheck));
    }

    /**
     * 删除盘点单
     */
    @PreAuthorize("@ss.hasPermi('wms:stockCheck:remove')")
    @Log(title = "盘点单管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{checkIds}")
    public AjaxResult remove(@PathVariable Long[] checkIds)
    {
        return toAjax(wmsStockCheckService.deleteWmsStockCheckByIds(checkIds));
    }

    /**
     * 开始盘点
     */
    @PreAuthorize("@ss.hasPermi('wms:stockCheck:edit')")
    @Log(title = "盘点单管理", businessType = BusinessType.UPDATE)
    @PutMapping("/start/{checkId}")
    public AjaxResult startCheck(@PathVariable Long checkId)
    {
        return toAjax(wmsStockCheckService.startCheck(checkId));
    }

    /**
     * 完成盘点
     */
    @PreAuthorize("@ss.hasPermi('wms:stockCheck:edit')")
    @Log(title = "盘点单管理", businessType = BusinessType.UPDATE)
    @PutMapping("/finish/{checkId}")
    public AjaxResult finishCheck(@PathVariable Long checkId)
    {
        return toAjax(wmsStockCheckService.finishCheck(checkId));
    }

    /**
     * 审核盘点单
     */
    @PreAuthorize("@ss.hasPermi('wms:stockCheck:edit')")
    @Log(title = "盘点单管理", businessType = BusinessType.UPDATE)
    @PutMapping("/approve/{checkId}")
    public AjaxResult approveCheck(@PathVariable Long checkId)
    {
        return toAjax(wmsStockCheckService.approveCheck(checkId));
    }
}
