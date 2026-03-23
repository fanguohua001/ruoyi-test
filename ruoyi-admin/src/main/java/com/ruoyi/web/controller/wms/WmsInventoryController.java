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
import com.ruoyi.wms.domain.WmsInventory;
import com.ruoyi.wms.service.IWmsInventoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import java.math.BigDecimal;

/**
 * 库存 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/inventory")
public class WmsInventoryController extends BaseController
{
    @Autowired
    private IWmsInventoryService wmsInventoryService;

    /**
     * 查询库存列表
     */
    @PreAuthorize("@ss.hasPermi('wms:inventory:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsInventory wmsInventory)
    {
        startPage();
        List<WmsInventory> list = wmsInventoryService.selectWmsInventoryList(wmsInventory);
        return getDataTable(list);
    }

    /**
     * 导出库存列表
     */
    @PreAuthorize("@ss.hasPermi('wms:inventory:export')")
    @Log(title = "库存管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsInventory wmsInventory)
    {
        List<WmsInventory> list = wmsInventoryService.selectWmsInventoryList(wmsInventory);
        ExcelUtil<WmsInventory> util = new ExcelUtil<WmsInventory>(WmsInventory.class);
        util.exportExcel(response, list, "库存数据");
    }

    /**
     * 获取库存详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:inventory:query')")
    @GetMapping(value = "/{inventoryId}")
    public AjaxResult getInfo(@PathVariable("inventoryId") Long inventoryId)
    {
        return success(wmsInventoryService.selectWmsInventoryById(inventoryId));
    }

    /**
     * 查询实时库存数量
     */
    @GetMapping("/qty")
    public AjaxResult getStockQty(Long productId, Long warehouseId)
    {
        BigDecimal qty = wmsInventoryService.getStockQty(productId, warehouseId);
        return success(qty);
    }

    /**
     * 库存冻结
     */
    @PreAuthorize("@ss.hasPermi('wms:inventory:freeze')")
    @Log(title = "库存管理", businessType = BusinessType.OTHER)
    @PutMapping("/freeze/{inventoryId}")
    public AjaxResult freeze(@PathVariable Long inventoryId)
    {
        return toAjax(wmsInventoryService.freezeStock(inventoryId));
    }

    /**
     * 库存解冻
     */
    @PreAuthorize("@ss.hasPermi('wms:inventory:unfreeze')")
    @Log(title = "库存管理", businessType = BusinessType.OTHER)
    @PutMapping("/unfreeze/{inventoryId}")
    public AjaxResult unfreeze(@PathVariable Long inventoryId)
    {
        return toAjax(wmsInventoryService.unfreezeStock(inventoryId));
    }

    /**
     * 检查库存预警
     */
    @PreAuthorize("@ss.hasPermi('wms:inventory:list')")
    @GetMapping("/alert/check")
    public TableDataInfo checkAlert()
    {
        List<WmsInventory> list = wmsInventoryService.checkStockAlert();
        return getDataTable(list);
    }
}
