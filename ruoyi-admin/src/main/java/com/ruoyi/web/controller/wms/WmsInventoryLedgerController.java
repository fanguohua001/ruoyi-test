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
import com.ruoyi.wms.domain.WmsInventoryLedger;
import com.ruoyi.wms.service.IWmsInventoryLedgerService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 库存台账 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/inventoryLedger")
public class WmsInventoryLedgerController extends BaseController
{
    @Autowired
    private IWmsInventoryLedgerService wmsInventoryLedgerService;

    /**
     * 查询库存台账列表
     */
    @PreAuthorize("@ss.hasPermi('wms:inventoryLedger:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsInventoryLedger wmsInventoryLedger)
    {
        startPage();
        List<WmsInventoryLedger> list = wmsInventoryLedgerService.selectWmsInventoryLedgerList(wmsInventoryLedger);
        return getDataTable(list);
    }

    /**
     * 导出库存台账列表
     */
    @PreAuthorize("@ss.hasPermi('wms:inventoryLedger:export')")
    @Log(title = "库存台账", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsInventoryLedger wmsInventoryLedger)
    {
        List<WmsInventoryLedger> list = wmsInventoryLedgerService.selectWmsInventoryLedgerList(wmsInventoryLedger);
        ExcelUtil<WmsInventoryLedger> util = new ExcelUtil<WmsInventoryLedger>(WmsInventoryLedger.class);
        util.exportExcel(response, list, "库存台账数据");
    }

    /**
     * 获取库存台账详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:inventoryLedger:query')")
    @GetMapping(value = "/{ledgerId}")
    public AjaxResult getInfo(@PathVariable("ledgerId") Long ledgerId)
    {
        return success(wmsInventoryLedgerService.selectWmsInventoryLedgerById(ledgerId));
    }
}
