package com.ruoyi.web.controller.wms;

import java.util.Date;
import java.util.List;
import java.util.Calendar;
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
import com.ruoyi.wms.domain.WmsInboundOrder;
import com.ruoyi.wms.service.IWmsInboundService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.DateUtils;

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
    public TableDataInfo list(WmsInboundOrder wmsInboundOrder,
                              @RequestParam(required = false) String beginTime,
                              @RequestParam(required = false) String endTime)
    {
        // 处理日期范围查询参数
        if (beginTime != null && !beginTime.isEmpty()) {
            try {
                wmsInboundOrder.setStartTime(DateUtils.parseDate(beginTime));
            } catch (Exception e) {
                // 日期格式错误，忽略
            }
        }
        if (endTime != null && !endTime.isEmpty()) {
            try {
                // 将结束时间设置为当天的 23:59:59，以便包含整天
                Date endDate = DateUtils.parseDate(endTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                wmsInboundOrder.setEndTime(calendar.getTime());
            } catch (Exception e) {
                // 日期格式错误，忽略
            }
        }
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
    public void export(HttpServletResponse response, WmsInboundOrder wmsInboundOrder,
                       @RequestParam(required = false) String beginTime,
                       @RequestParam(required = false) String endTime)
    {
        // 处理日期范围查询参数
        if (beginTime != null && !beginTime.isEmpty()) {
            try {
                wmsInboundOrder.setStartTime(DateUtils.parseDate(beginTime));
            } catch (Exception e) {
                // 日期格式错误，忽略
            }
        }
        if (endTime != null && !endTime.isEmpty()) {
            try {
                Date endDate = DateUtils.parseDate(endTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                wmsInboundOrder.setEndTime(calendar.getTime());
            } catch (Exception e) {
                // 日期格式错误，忽略
            }
        }
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
        // 检查入库单是否已完成，如果已完成则不允许修改
        WmsInboundOrder existingOrder = wmsInboundService.selectWmsInboundOrderById(wmsInboundOrder.getInboundId());
        if (existingOrder != null && existingOrder.getIsFinished() != null && existingOrder.getIsFinished() == 1) {
            return error("修改入库单'" + wmsInboundOrder.getInboundNo() + "'失败，入库单已完成，不允许修改");
        }

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
    public AjaxResult receive(@RequestParam Long inboundId,
                              @RequestParam(required = false) Long itemId,
                              @RequestBody java.math.BigDecimal qty)
    {
        return toAjax(wmsInboundService.receive(inboundId, itemId, qty));
    }

    /**
     * 质检
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:quality')")
    @Log(title = "入库管理", businessType = BusinessType.UPDATE)
    @PutMapping("/quality")
    public AjaxResult qualityCheck(@RequestParam Long inboundId,
                                    @RequestParam Long itemId,
                                    @RequestParam java.math.BigDecimal qualifiedQty,
                                    @RequestParam java.math.BigDecimal unqualifiedQty)
    {
        return toAjax(wmsInboundService.qualityCheck(inboundId, itemId, qualifiedQty, unqualifiedQty));
    }

    /**
     * 上架
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:putaway')")
    @Log(title = "入库管理", businessType = BusinessType.UPDATE)
    @PutMapping("/putaway")
    public AjaxResult putAway(@RequestParam Long inboundId,
                               @RequestParam Long itemId,
                               @RequestParam Long locationId)
    {
        return toAjax(wmsInboundService.putAway(inboundId, itemId, locationId));
    }

    /**
     * 完成入库单
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:finish')")
    @Log(title = "入库管理", businessType = BusinessType.UPDATE)
    @PutMapping("/finish")
    public AjaxResult finish(@RequestParam Long inboundId)
    {
        return toAjax(wmsInboundService.finishInboundOrder(inboundId));
    }

    /**
     * 检查入库单是否已完成
     */
    @PreAuthorize("@ss.hasPermi('wms:inbound:query')")
    @GetMapping("/checkFinished/{inboundId}")
    public AjaxResult checkFinished(@PathVariable("inboundId") Long inboundId)
    {
        WmsInboundOrder order = wmsInboundService.selectWmsInboundOrderById(inboundId);
        if (order == null) {
            return error("入库单不存在");
        }
        boolean isFinished = order.getIsFinished() != null && order.getIsFinished() == 1;
        return success(isFinished);
    }
}
