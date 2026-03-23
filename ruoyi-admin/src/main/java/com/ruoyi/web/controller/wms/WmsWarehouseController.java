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
import com.ruoyi.wms.domain.WmsWarehouse;
import com.ruoyi.wms.service.IWmsWarehouseService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 仓库 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/warehouse")
public class WmsWarehouseController extends BaseController
{
    @Autowired
    private IWmsWarehouseService wmsWarehouseService;

    /**
     * 查询仓库列表
     */
    @PreAuthorize("@ss.hasPermi('wms:warehouse:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsWarehouse wmsWarehouse)
    {
        startPage();
        List<WmsWarehouse> list = wmsWarehouseService.selectWmsWarehouseList(wmsWarehouse);
        return getDataTable(list);
    }

    /**
     * 查询所有有效的仓库列表（用于下拉选择）
     */
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<WmsWarehouse> list = wmsWarehouseService.selectWmsWarehouseAll();
        return success(list);
    }

    /**
     * 导出仓库列表
     */
    @PreAuthorize("@ss.hasPermi('wms:warehouse:export')")
    @Log(title = "仓库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsWarehouse wmsWarehouse)
    {
        List<WmsWarehouse> list = wmsWarehouseService.selectWmsWarehouseList(wmsWarehouse);
        ExcelUtil<WmsWarehouse> util = new ExcelUtil<WmsWarehouse>(WmsWarehouse.class);
        util.exportExcel(response, list, "仓库数据");
    }

    /**
     * 获取仓库详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:warehouse:query')")
    @GetMapping(value = "/{warehouseId}")
    public AjaxResult getInfo(@PathVariable("warehouseId") Long warehouseId)
    {
        return success(wmsWarehouseService.selectWmsWarehouseById(warehouseId));
    }

    /**
     * 新增仓库
     */
    @PreAuthorize("@ss.hasPermi('wms:warehouse:add')")
    @Log(title = "仓库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsWarehouse wmsWarehouse)
    {
        if (!wmsWarehouseService.checkWarehouseCodeUnique(wmsWarehouse))
        {
            return error("新增仓库'" + wmsWarehouse.getWarehouseName() + "'失败，仓库编码已存在");
        }
        wmsWarehouse.setCreateBy(getUsername());
        return toAjax(wmsWarehouseService.insertWmsWarehouse(wmsWarehouse));
    }

    /**
     * 修改仓库
     */
    @PreAuthorize("@ss.hasPermi('wms:warehouse:edit')")
    @Log(title = "仓库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsWarehouse wmsWarehouse)
    {
        if (!wmsWarehouseService.checkWarehouseCodeUnique(wmsWarehouse))
        {
            return error("修改仓库'" + wmsWarehouse.getWarehouseName() + "'失败，仓库编码已存在");
        }
        wmsWarehouse.setUpdateBy(getUsername());
        return toAjax(wmsWarehouseService.updateWmsWarehouse(wmsWarehouse));
    }

    /**
     * 删除仓库
     */
    @PreAuthorize("@ss.hasPermi('wms:warehouse:remove')")
    @Log(title = "仓库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{warehouseIds}")
    public AjaxResult remove(@PathVariable Long[] warehouseIds)
    {
        return toAjax(wmsWarehouseService.deleteWmsWarehouseByIds(warehouseIds));
    }
}
