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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.wms.domain.WmsAlert;
import com.ruoyi.wms.service.IWmsAlertService;


/**
 * 库存预警 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/wms/alert")
public class WmsAlertController extends BaseController
{
    @Autowired
    private IWmsAlertService wmsAlertService;

    /**
     * 查询预警列表
     */
    @PreAuthorize("@ss.hasPermi('wms:alert:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsAlert wmsAlert)
    {
        startPage();
        List<WmsAlert> list = wmsAlertService.selectWmsAlertList(wmsAlert);
        return getDataTable(list);
    }

    /**
     * 导出预警列表
     */
    @PreAuthorize("@ss.hasPermi('wms:alert:export')")
    @Log(title = "库存预警", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsAlert wmsAlert)
    {
        List<WmsAlert> list = wmsAlertService.selectWmsAlertList(wmsAlert);
        ExcelUtil<WmsAlert> util = new ExcelUtil<WmsAlert>(WmsAlert.class);
        util.exportExcel(response, list, "库存预警数据");
    }

    /**
     * 获取预警详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:alert:query')")
    @GetMapping(value = "/{alertId}")
    public AjaxResult getInfo(@PathVariable("alertId") Long alertId)
    {
        return success(wmsAlertService.selectWmsAlertById(alertId));
    }

    /**
     * 处理预警
     */
    @PreAuthorize("@ss.hasPermi('wms:alert:handle')")
    @Log(title = "库存预警", businessType = BusinessType.UPDATE)
    @PutMapping("/handle/{alertId}")
    public AjaxResult handle(@PathVariable Long alertId,
                             @RequestBody String handleRemark)
    {
        return toAjax(wmsAlertService.handleAlert(alertId, getUsername(), handleRemark));
    }

    /**
     * 生成库存预警
     */
    @PreAuthorize("@ss.hasPermi('wms:alert:generate')")
    @Log(title = "库存预警", businessType = BusinessType.OTHER)
    @PostMapping("/generate")
    public AjaxResult generate()
    {
        return toAjax(wmsAlertService.generateAlerts());
    }

    /**
     * 检查近效期商品
     */
    @PreAuthorize("@ss.hasPermi('wms:alert:checkExpiry')")
    @Log(title = "库存预警", businessType = BusinessType.OTHER)
    @PostMapping("/checkExpiry/{days}")
    public TableDataInfo checkExpiry(@PathVariable Integer days)
    {
        wmsAlertService.checkNearExpiry(days);
        return getDataTable(wmsAlertService.selectWmsAlertList(new WmsAlert()));
    }

    /**
     * 检查呆滞料
     */
    @PreAuthorize("@ss.hasPermi('wms:alert:checkSlowMoving')")
    @Log(title = "库存预警", businessType = BusinessType.OTHER)
    @PostMapping("/checkSlowMoving/{days}")
    public TableDataInfo checkSlowMoving(@PathVariable Integer days)
    {
        wmsAlertService.checkSlowMoving(days);
        return getDataTable(wmsAlertService.selectWmsAlertList(new WmsAlert()));
    }
}
