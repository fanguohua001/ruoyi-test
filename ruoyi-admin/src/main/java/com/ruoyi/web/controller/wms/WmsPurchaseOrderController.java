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
import com.ruoyi.wms.domain.WmsPurchaseOrder;
import com.ruoyi.wms.service.IWmsPurchaseOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 采购订单 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/purchaseOrder")
public class WmsPurchaseOrderController extends BaseController
{
    @Autowired
    private IWmsPurchaseOrderService wmsPurchaseOrderService;

    /**
     * 查询采购订单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:purchaseOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsPurchaseOrder wmsPurchaseOrder)
    {
        startPage();
        List<WmsPurchaseOrder> list = wmsPurchaseOrderService.selectPurchaseOrderList(wmsPurchaseOrder);
        return getDataTable(list);
    }

    /**
     * 导出采购订单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:purchaseOrder:export')")
    @Log(title = "采购订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsPurchaseOrder wmsPurchaseOrder)
    {
        List<WmsPurchaseOrder> list = wmsPurchaseOrderService.selectPurchaseOrderList(wmsPurchaseOrder);
        ExcelUtil<WmsPurchaseOrder> util = new ExcelUtil<WmsPurchaseOrder>(WmsPurchaseOrder.class);
        util.exportExcel(response, list, "采购订单数据");
    }

    /**
     * 获取采购订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:purchaseOrder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(wmsPurchaseOrderService.selectPurchaseOrderById(orderId));
    }

    /**
     * 新增采购订单
     */
    @PreAuthorize("@ss.hasPermi('wms:purchaseOrder:add')")
    @Log(title = "采购订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsPurchaseOrder wmsPurchaseOrder)
    {
        wmsPurchaseOrder.setCreateBy(getUsername());
        return toAjax(wmsPurchaseOrderService.insertPurchaseOrder(wmsPurchaseOrder));
    }

    /**
     * 修改采购订单
     */
    @PreAuthorize("@ss.hasPermi('wms:purchaseOrder:edit')")
    @Log(title = "采购订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsPurchaseOrder wmsPurchaseOrder)
    {
        // 检查订单是否已完成，如果已完成则不允许修改
        WmsPurchaseOrder existingOrder = wmsPurchaseOrderService.selectPurchaseOrderById(wmsPurchaseOrder.getOrderId());
        if (existingOrder != null && existingOrder.getOrderStatus() != null && existingOrder.getOrderStatus() == 1) {
            return error("修改采购订单'" + wmsPurchaseOrder.getOrderNo() + "'失败，订单已完成，不允许修改");
        }

        if (!wmsPurchaseOrderService.checkOrderNoUnique(wmsPurchaseOrder))
        {
            return error("修改采购订单'" + wmsPurchaseOrder.getOrderNo() + "'失败，订单编号已存在");
        }
        wmsPurchaseOrder.setUpdateBy(getUsername());
        return toAjax(wmsPurchaseOrderService.updatePurchaseOrder(wmsPurchaseOrder));
    }

    /**
     * 删除采购订单
     */
    @PreAuthorize("@ss.hasPermi('wms:purchaseOrder:remove')")
    @Log(title = "采购订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(wmsPurchaseOrderService.deletePurchaseOrderByIds(orderIds));
    }

    /**
     * 完成采购订单
     */
    @PreAuthorize("@ss.hasPermi('wms:purchaseOrder:finish')")
    @Log(title = "采购订单", businessType = BusinessType.UPDATE)
    @PutMapping("/finish")
    public AjaxResult finish(@RequestParam Long orderId)
    {
        return toAjax(wmsPurchaseOrderService.finishPurchaseOrder(orderId));
    }
}
