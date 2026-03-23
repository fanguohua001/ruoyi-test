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
import com.ruoyi.wms.domain.WmsCustomerOrder;
import com.ruoyi.wms.service.IWmsCustomerOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 客户订单 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/customerOrder")
public class WmsCustomerOrderController extends BaseController
{
    @Autowired
    private IWmsCustomerOrderService wmsCustomerOrderService;

    /**
     * 查询客户订单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:customerOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsCustomerOrder wmsCustomerOrder)
    {
        startPage();
        List<WmsCustomerOrder> list = wmsCustomerOrderService.selectCustomerOrderList(wmsCustomerOrder);
        return getDataTable(list);
    }

    /**
     * 导出客户订单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:customerOrder:export')")
    @Log(title = "客户订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsCustomerOrder wmsCustomerOrder)
    {
        List<WmsCustomerOrder> list = wmsCustomerOrderService.selectCustomerOrderList(wmsCustomerOrder);
        ExcelUtil<WmsCustomerOrder> util = new ExcelUtil<WmsCustomerOrder>(WmsCustomerOrder.class);
        util.exportExcel(response, list, "客户订单数据");
    }

    /**
     * 获取客户订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:customerOrder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(wmsCustomerOrderService.selectCustomerOrderById(orderId));
    }

    /**
     * 新增客户订单
     */
    @PreAuthorize("@ss.hasPermi('wms:customerOrder:add')")
    @Log(title = "客户订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsCustomerOrder wmsCustomerOrder)
    {
        wmsCustomerOrder.setCreateBy(getUsername());
        return toAjax(wmsCustomerOrderService.insertCustomerOrder(wmsCustomerOrder));
    }

    /**
     * 修改客户订单
     */
    @PreAuthorize("@ss.hasPermi('wms:customerOrder:edit')")
    @Log(title = "客户订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsCustomerOrder wmsCustomerOrder)
    {
        // 检查订单是否已完成，如果已完成则不允许修改
        WmsCustomerOrder existingOrder = wmsCustomerOrderService.selectCustomerOrderById(wmsCustomerOrder.getOrderId());
        if (existingOrder != null && existingOrder.getOrderStatus() != null && existingOrder.getOrderStatus() == 1) {
            return error("修改客户订单'" + wmsCustomerOrder.getOrderNo() + "'失败，订单已完成，不允许修改");
        }

        if (!wmsCustomerOrderService.checkOrderNoUnique(wmsCustomerOrder))
        {
            return error("修改客户订单'" + wmsCustomerOrder.getOrderNo() + "'失败，订单编号已存在");
        }
        wmsCustomerOrder.setUpdateBy(getUsername());
        return toAjax(wmsCustomerOrderService.updateCustomerOrder(wmsCustomerOrder));
    }

    /**
     * 删除客户订单
     */
    @PreAuthorize("@ss.hasPermi('wms:customerOrder:remove')")
    @Log(title = "客户订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(wmsCustomerOrderService.deleteCustomerOrderByIds(orderIds));
    }

    /**
     * 完成客户订单
     */
    @PreAuthorize("@ss.hasPermi('wms:customerOrder:finish')")
    @Log(title = "客户订单", businessType = BusinessType.UPDATE)
    @PutMapping("/finish")
    public AjaxResult finish(@RequestParam Long orderId)
    {
        return toAjax(wmsCustomerOrderService.finishCustomerOrder(orderId));
    }

    /**
     * 查询已完成的客户订单列表（用于出库单关联）
     */
    @PreAuthorize("@ss.hasPermi('wms:outbound:add')")
    @GetMapping("/finished/list")
    public TableDataInfo finishedList(WmsCustomerOrder wmsCustomerOrder)
    {
        startPage();
        List<WmsCustomerOrder> list = wmsCustomerOrderService.listFinishedOrders(wmsCustomerOrder);
        return getDataTable(list);
    }
}
