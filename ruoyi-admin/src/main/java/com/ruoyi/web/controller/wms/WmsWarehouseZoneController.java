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
import com.ruoyi.wms.domain.WmsWarehouseZone;
import com.ruoyi.wms.service.IWmsWarehouseZoneService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 库区 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/zone")
public class WmsWarehouseZoneController extends BaseController
{
    @Autowired
    private IWmsWarehouseZoneService wmsWarehouseZoneService;

    /**
     * 查询库区列表
     */
    @PreAuthorize("@ss.hasPermi('wms:zone:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsWarehouseZone wmsWarehouseZone)
    {
        startPage();
        List<WmsWarehouseZone> list = wmsWarehouseZoneService.selectWmsWarehouseZoneList(wmsWarehouseZone);
        return getDataTable(list);
    }

    /**
     * 查询所有有效的库区列表（用于下拉选择）
     */
    @PreAuthorize("@ss.hasPermi('wms:zone:query')")
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<WmsWarehouseZone> list = wmsWarehouseZoneService.selectWmsWarehouseZoneAll();
        return success(list);
    }

    /**
     * 根据仓库 ID 查询库区列表
     */
    @PreAuthorize("@ss.hasPermi('wms:zone:query')")
    @GetMapping("/byWarehouse/{warehouseId}")
    public AjaxResult listByWarehouse(@PathVariable("warehouseId") Long warehouseId)
    {
        List<WmsWarehouseZone> list = wmsWarehouseZoneService.selectWmsWarehouseZoneByWarehouseId(warehouseId);
        return success(list);
    }

    /**
     * 导出库区列表
     */
    @PreAuthorize("@ss.hasPermi('wms:zone:export')")
    @Log(title = "库区", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsWarehouseZone wmsWarehouseZone)
    {
        List<WmsWarehouseZone> list = wmsWarehouseZoneService.selectWmsWarehouseZoneList(wmsWarehouseZone);
        ExcelUtil<WmsWarehouseZone> util = new ExcelUtil<WmsWarehouseZone>(WmsWarehouseZone.class);
        util.exportExcel(response, list, "库区数据");
    }

    /**
     * 获取库区详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:zone:query')")
    @GetMapping(value = "/{zoneId}")
    public AjaxResult getInfo(@PathVariable("zoneId") Long zoneId)
    {
        return success(wmsWarehouseZoneService.selectWmsWarehouseZoneById(zoneId));
    }

    /**
     * 新增库区
     */
    @PreAuthorize("@ss.hasPermi('wms:zone:add')")
    @Log(title = "库区", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsWarehouseZone wmsWarehouseZone)
    {
        if (!wmsWarehouseZoneService.checkZoneCodeUnique(wmsWarehouseZone))
        {
            return error("新增库区'" + wmsWarehouseZone.getZoneName() + "'失败，库区编码已存在");
        }
        wmsWarehouseZone.setCreateBy(getUsername());
        return toAjax(wmsWarehouseZoneService.insertWmsWarehouseZone(wmsWarehouseZone));
    }

    /**
     * 修改库区
     */
    @PreAuthorize("@ss.hasPermi('wms:zone:edit')")
    @Log(title = "库区", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsWarehouseZone wmsWarehouseZone)
    {
        if (!wmsWarehouseZoneService.checkZoneCodeUnique(wmsWarehouseZone))
        {
            return error("修改库区'" + wmsWarehouseZone.getZoneName() + "'失败，库区编码已存在");
        }
        wmsWarehouseZone.setUpdateBy(getUsername());
        return toAjax(wmsWarehouseZoneService.updateWmsWarehouseZone(wmsWarehouseZone));
    }

    /**
     * 删除库区
     */
    @PreAuthorize("@ss.hasPermi('wms:zone:remove')")
    @Log(title = "库区", businessType = BusinessType.DELETE)
	@DeleteMapping("/{zoneIds}")
    public AjaxResult remove(@PathVariable Long[] zoneIds)
    {
        return toAjax(wmsWarehouseZoneService.deleteWmsWarehouseZoneByIds(zoneIds));
    }
}
