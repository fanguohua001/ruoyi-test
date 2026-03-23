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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.wms.domain.WmsOutboundOrder;
import com.ruoyi.wms.service.IWmsOutboundService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 出库单 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/outbound")
public class WmsOutboundController extends BaseController
{
    @Autowired
    private IWmsOutboundService wmsOutboundService;

    /**
     * 查询出库单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsOutboundOrder wmsOutboundOrder)
    {
        startPage();
        List<WmsOutboundOrder> list = wmsOutboundService.selectWmsOutboundOrderList(wmsOutboundOrder);
        return getDataTable(list);
    }

    /**
     * 导出出库单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:export')")
    @Log(title = "出库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsOutboundOrder wmsOutboundOrder)
    {
        List<WmsOutboundOrder> list = wmsOutboundService.selectWmsOutboundOrderList(wmsOutboundOrder);
        ExcelUtil<WmsOutboundOrder> util = new ExcelUtil<WmsOutboundOrder>(WmsOutboundOrder.class);
        util.exportExcel(response, list, "出库单数据");
    }

    /**
     * 获取出库单详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:query')")
    @GetMapping(value = "/{outboundId}")
    public AjaxResult getInfo(@PathVariable("outboundId") Long outboundId)
    {
        return success(wmsOutboundService.selectWmsOutboundOrderById(outboundId));
    }

    /**
     * 新增出库单
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:add')")
    @Log(title = "出库管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsOutboundOrder wmsOutboundOrder)
    {
        if (!wmsOutboundService.checkOutboundNoUnique(wmsOutboundOrder))
        {
            return error("新增出库单'" + wmsOutboundOrder.getOutboundNo() + "'失败，出库单号已存在");
        }
        wmsOutboundOrder.setCreateBy(getUsername());
        return toAjax(wmsOutboundService.insertWmsOutboundOrder(wmsOutboundOrder));
    }

    /**
     * 修改出库单
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:edit')")
    @Log(title = "出库管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsOutboundOrder wmsOutboundOrder)
    {
        // 检查出库单是否已完成，如果已完成则不允许修改
        WmsOutboundOrder existingOrder = wmsOutboundService.selectWmsOutboundOrderById(wmsOutboundOrder.getOutboundId());
        if (existingOrder != null && existingOrder.getIsFinished() != null && existingOrder.getIsFinished() == 1) {
            return error("修改出库单'" + wmsOutboundOrder.getOutboundNo() + "'失败，出库单已完成，不允许修改");
        }

        if (!wmsOutboundService.checkOutboundNoUnique(wmsOutboundOrder))
        {
            return error("修改出库单'" + wmsOutboundOrder.getOutboundNo() + "'失败，出库单号已存在");
        }
        wmsOutboundOrder.setUpdateBy(getUsername());
        return toAjax(wmsOutboundService.updateWmsOutboundOrder(wmsOutboundOrder));
    }

    /**
     * 删除出库单
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:remove')")
    @Log(title = "出库管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{outboundIds}")
    public AjaxResult remove(@PathVariable Long[] outboundIds)
    {
        return toAjax(wmsOutboundService.deleteWmsOutboundOrderByIds(outboundIds));
    }

    /**
     * 拣货（FIFO）
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:picking')")
    @Log(title = "出库管理", businessType = BusinessType.UPDATE)
    @PutMapping("/picking")
    public AjaxResult picking(@RequestParam Long outboundId,
                               @RequestParam Long itemId,
                               @RequestBody java.math.BigDecimal qty)
    {
        return toAjax(wmsOutboundService.picking(outboundId, itemId, qty));
    }

    /**
     * 复核
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:review')")
    @Log(title = "出库管理", businessType = BusinessType.UPDATE)
    @PutMapping("/review/{outboundId}")
    public AjaxResult review(@PathVariable Long outboundId)
    {
        return toAjax(wmsOutboundService.review(outboundId));
    }

    /**
     * 打包
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:pack')")
    @Log(title = "出库管理", businessType = BusinessType.UPDATE)
    @PutMapping("/pack/{outboundId}")
    public AjaxResult pack(@PathVariable Long outboundId)
    {
        return toAjax(wmsOutboundService.pack(outboundId));
    }

    /**
     * 发货
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:ship')")
    @Log(title = "出库管理", businessType = BusinessType.UPDATE)
    @PutMapping("/ship/{outboundId}")
    public AjaxResult ship(@PathVariable Long outboundId)
    {
        return toAjax(wmsOutboundService.ship(outboundId));
    }

    /**
     * 完成出库单
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:finish')")
    @Log(title = "出库管理", businessType = BusinessType.UPDATE)
    @PutMapping("/finish")
    public AjaxResult finish(@RequestParam Long outboundId)
    {
        return toAjax(wmsOutboundService.finishOutboundOrder(outboundId));
    }

    /**
     * 检查出库单是否已完成
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:query')")
    @GetMapping("/checkFinished/{outboundId}")
    public AjaxResult checkFinished(@PathVariable("outboundId") Long outboundId)
    {
        WmsOutboundOrder order = wmsOutboundService.selectWmsOutboundOrderById(outboundId);
        if (order == null) {
            return error("出库单不存在");
        }
        boolean isFinished = order.getIsFinished() != null && order.getIsFinished() == 1;
        return success(isFinished);
    }
}
