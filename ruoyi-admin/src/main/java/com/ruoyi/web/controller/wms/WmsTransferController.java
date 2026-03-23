package com.ruoyi.web.controller.wms;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.wms.domain.WmsTransfer;
import com.ruoyi.wms.service.IWmsTransferService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 移库单 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/transfer")
public class WmsTransferController extends BaseController
{
    @Autowired
    private IWmsTransferService wmsTransferService;

    /**
     * 查询移库单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:transfer:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsTransfer wmsTransfer)
    {
        startPage();
        List<WmsTransfer> list = wmsTransferService.selectWmsTransferList(wmsTransfer);
        return getDataTable(list);
    }

    /**
     * 导出移库单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:transfer:export')")
    @Log(title = "移库单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsTransfer wmsTransfer)
    {
        List<WmsTransfer> list = wmsTransferService.selectWmsTransferList(wmsTransfer);
        ExcelUtil<WmsTransfer> util = new ExcelUtil<WmsTransfer>(WmsTransfer.class);
        util.exportExcel(response, list, "移库单数据");
    }

    /**
     * 获取移库单详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:transfer:query')")
    @GetMapping(value = "/{transferId}")
    public AjaxResult getInfo(@PathVariable("transferId") Long transferId)
    {
        return success(wmsTransferService.selectWmsTransferById(transferId));
    }

    /**
     * 新增移库单
     */
    @PreAuthorize("@ss.hasPermi('wms:transfer:add')")
    @Log(title = "移库单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsTransfer wmsTransfer)
    {
        if (!wmsTransferService.checkTransferNoUnique(wmsTransfer))
        {
            return error("新增移库单'" + wmsTransfer.getTransferNo() + "'失败，移库单号已存在");
        }
        wmsTransfer.setCreateBy(getUsername());
        return toAjax(wmsTransferService.insertWmsTransfer(wmsTransfer));
    }

    /**
     * 修改移库单
     */
    @PreAuthorize("@ss.hasPermi('wms:transfer:edit')")
    @Log(title = "移库单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsTransfer wmsTransfer)
    {
        if (!wmsTransferService.checkTransferNoUnique(wmsTransfer))
        {
            return error("修改移库单'" + wmsTransfer.getTransferNo() + "'失败，移库单号已存在");
        }
        wmsTransfer.setUpdateBy(getUsername());
        return toAjax(wmsTransferService.updateWmsTransfer(wmsTransfer));
    }

    /**
     * 删除移库单
     */
    @PreAuthorize("@ss.hasPermi('wms:transfer:remove')")
    @Log(title = "移库单管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{transferIds}")
    public AjaxResult remove(@PathVariable Long[] transferIds)
    {
        return toAjax(wmsTransferService.deleteWmsTransferByIds(transferIds));
    }

    /**
     * 执行移库
     */
    @PreAuthorize("@ss.hasPermi('wms:transfer:edit')")
    @Log(title = "移库单管理", businessType = BusinessType.UPDATE)
    @PostMapping("/execute")
    public AjaxResult executeTransfer(
            @RequestParam Long transferId,
            @RequestParam Long productId,
            @RequestParam BigDecimal qty)
    {
        return toAjax(wmsTransferService.executeTransfer(transferId, productId, qty));
    }
}
