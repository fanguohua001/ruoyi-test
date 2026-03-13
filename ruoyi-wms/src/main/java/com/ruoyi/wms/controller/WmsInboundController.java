package com.ruoyi.wms.controller;

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
import com.ruoyi.wms.domain.WmsInboundOrder;
import com.ruoyi.wms.service.IWmsInboundService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 入库单 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/inbound")
public class WmsInboundController extends BaseController
{
    @Autowired
    private IWmsInboundService wmsInboundService;

    /**
     * 查询入库单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsInboundOrder wmsInboundOrder)
    {
        startPage();
        List<WmsInboundOrder> list = wmsInboundService.selectWmsInboundOrderList(wmsInboundOrder);
        return getDataTable(list);
    }

    /**
     * 导出入库单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:export')")
    @Log(title = "入库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsInboundOrder wmsInboundOrder)
    {
        List<WmsInboundOrder> list = wmsInboundService.selectWmsInboundOrderList(wmsInboundOrder);
        ExcelUtil<WmsInboundOrder> util = new ExcelUtil<WmsInboundOrder>(WmsInboundOrder.class);
        util.exportExcel(response, list, "入库单数据");
    }

    /**
     * 获取入库单详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:query')")
    @GetMapping(value = "/{inboundId}")
    public AjaxResult getInfo(@PathVariable("inboundId") Long inboundId)
    {
        return success(wmsInboundService.selectWmsInboundOrderById(inboundId));
    }

    /**
     * 新增入库单
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:add')")
    @Log(title = "入库管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsInboundOrder wmsInboundOrder)
    {
        if (!wmsInboundService.checkInboundNoUnique(wmsInboundOrder))
        {
            return error("新增入库单'" + wmsInboundOrder.getInboundNo() + "'失败，入库单号已存在");
        }
        wmsInboundOrder.setCreateBy(getUsername());
        return toAjax(wmsInboundService.insertWmsInboundOrder(wmsInboundOrder));
    }

    /**
     * 修改入库单
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:edit')")
    @Log(title = "入库管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsInboundOrder wmsInboundOrder)
    {
        if (!wmsInboundService.checkInboundNoUnique(wmsInboundOrder))
        {
            return error("修改入库单'" + wmsInboundOrder.getInboundNo() + "'失败，入库单号已存在");
        }
        wmsInboundOrder.setUpdateBy(getUsername());
        return toAjax(wmsInboundService.updateWmsInboundOrder(wmsInboundOrder));
    }

    /**
     * 删除入库单
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:remove')")
    @Log(title = "入库管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{inboundIds}")
    public AjaxResult remove(@PathVariable Long[] inboundIds)
    {
        return toAjax(wmsInboundService.deleteWmsInboundOrderByIds(inboundIds));
    }

    /**
     * 收货
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:receive')")
    @Log(title = "入库管理", businessType = BusinessType.UPDATE)
    @PutMapping("/receive")
    public AjaxResult receive(@PathVariable Long inboundId, @RequestBody java.math.BigDecimal qty)
    {
        return toAjax(wmsInboundService.receive(inboundId, null, qty));
    }

    /**
     * 质检
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:quality')")
    @Log(title = "入库管理", businessType = BusinessType.UPDATE)
    @PutMapping("/quality")
    public AjaxResult qualityCheck(@PathVariable Long inboundId,
                                    @PathVariable Long itemId,
                                    @RequestBody java.math.BigDecimal[] qtys)
    {
        return toAjax(wmsInboundService.qualityCheck(inboundId, itemId, qtys[0], qtys[1]));
    }

    /**
     * 上架
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:putaway')")
    @Log(title = "入库管理", businessType = BusinessType.UPDATE)
    @PutMapping("/putaway")
    public AjaxResult putAway(@PathVariable Long inboundId,
                               @PathVariable Long itemId,
                               @PathVariable Long locationId)
    {
        return toAjax(wmsInboundService.putAway(inboundId, itemId, locationId));
    }
}
