package com.ruoyi.wms.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.wms.domain.WmsAlert;
import com.ruoyi.wms.domain.WmsInventory;
import com.ruoyi.wms.domain.WmsProduct;
import com.ruoyi.wms.mapper.WmsAlertMapper;
import com.ruoyi.wms.mapper.WmsInventoryMapper;
import com.ruoyi.wms.mapper.WmsProductMapper;
import com.ruoyi.wms.service.IWmsAlertService;

/**
 * 库存预警 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsAlertServiceImpl implements IWmsAlertService
{
    @Autowired
    private WmsAlertMapper wmsAlertMapper;

    @Autowired
    private WmsInventoryMapper wmsInventoryMapper;

    @Autowired
    private WmsProductMapper wmsProductMapper;

    /**
     * 查询预警信息
     *
     * @param alertId 预警 ID
     * @return 预警信息
     */
    @Override
    public WmsAlert selectWmsAlertById(Long alertId)
    {
        return wmsAlertMapper.selectAlertById(alertId);
    }

    /**
     * 查询预警列表
     *
     * @param wmsAlert 预警信息
     * @return 预警
     */
    @Override
    public List<WmsAlert> selectWmsAlertList(WmsAlert wmsAlert)
    {
        return wmsAlertMapper.selectAlertList(wmsAlert);
    }

    /**
     * 新增预警
     *
     * @param wmsAlert 预警信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertWmsAlert(WmsAlert wmsAlert)
    {
        return wmsAlertMapper.insertAlert(wmsAlert);
    }

    /**
     * 修改预警
     *
     * @param wmsAlert 预警信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateWmsAlert(WmsAlert wmsAlert)
    {
        return wmsAlertMapper.updateAlert(wmsAlert);
    }

    /**
     * 批量删除预警
     *
     * @param alertIds 需要删除的预警 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsAlertByIds(Long[] alertIds)
    {
        return wmsAlertMapper.deleteAlertByIds(alertIds);
    }

    /**
     * 删除预警信息
     *
     * @param alertId 预警 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsAlertById(Long alertId)
    {
        return wmsAlertMapper.deleteAlertById(alertId);
    }

    /**
     * 处理预警
     *
     * @param alertId 预警 ID
     * @param handleBy 处理人
     * @param handleRemark 处理备注
     * @return 结果
     */
    @Override
    @Transactional
    public int handleAlert(Long alertId, String handleBy, String handleRemark)
    {
        WmsAlert alert = wmsAlertMapper.selectAlertById(alertId);
        if (alert == null) {
            throw new RuntimeException("预警记录不存在");
        }

        alert.setStatus("1");
        alert.setHandleBy(handleBy);
        alert.setHandleTime(new Date());
        alert.setHandleRemark(handleRemark);
        return wmsAlertMapper.updateAlert(alert);
    }

    /**
     * 生成库存预警
     *
     * @return 结果
     */
    @Override
    @Transactional
    public int generateAlerts()
    {
        int count = 0;

        // 检查低于安全库存和高于最高库存
        List<WmsInventory> inventories = wmsInventoryMapper.selectInventoryList(new WmsInventory());
        for (WmsInventory inventory : inventories) {
            WmsProduct product = wmsProductMapper.selectProductById(inventory.getProductId());
            if (product != null && product.getSafetyStock() != null) {
                // 检查低于安全库存
                if (inventory.getQtyOnHand().compareTo(product.getSafetyStock()) < 0) {
                    WmsAlert alert = new WmsAlert();
                    alert.setProductId(inventory.getProductId());
                    alert.setProductCode(inventory.getProductCode());
                    alert.setProductName(inventory.getProductName());
                    alert.setWarehouseId(inventory.getWarehouseId());
                    alert.setAlertType("1");
                    alert.setAlertLevel("2");
                    alert.setQtyOnHand(inventory.getQtyOnHand());
                    alert.setSafetyStock(product.getSafetyStock());
                    alert.setStatus("0");
                    alert.setCreateTime(new Date());
                    wmsAlertMapper.insertAlert(alert);
                    count++;
                }

                // 检查高于最高库存
                if (product.getMaxStock() != null && inventory.getQtyOnHand().compareTo(product.getMaxStock()) > 0) {
                    WmsAlert alert = new WmsAlert();
                    alert.setProductId(inventory.getProductId());
                    alert.setProductCode(inventory.getProductCode());
                    alert.setProductName(inventory.getProductName());
                    alert.setWarehouseId(inventory.getWarehouseId());
                    alert.setAlertType("2");
                    alert.setAlertLevel("1");
                    alert.setQtyOnHand(inventory.getQtyOnHand());
                    alert.setSafetyStock(product.getMaxStock());
                    alert.setStatus("0");
                    alert.setCreateTime(new Date());
                    wmsAlertMapper.insertAlert(alert);
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * 检查近效期物料
     *
     * @param days 天数
     * @return 结果
     */
    @Override
    @Transactional
    public int checkNearExpiry(int days)
    {
        int count = 0;
        Date today = new Date();
        List<WmsInventory> inventories = wmsInventoryMapper.selectInventoryList(new WmsInventory());

        for (WmsInventory inventory : inventories) {
            if (inventory.getExpiryDate() != null) {
                long daysUntilExpiry = (inventory.getExpiryDate().getTime() - today.getTime()) / (1000 * 60 * 60 * 24);
                if (daysUntilExpiry >= 0 && daysUntilExpiry <= days) {
                    WmsAlert alert = new WmsAlert();
                    alert.setProductId(inventory.getProductId());
                    alert.setProductCode(inventory.getProductCode());
                    alert.setProductName(inventory.getProductName());
                    alert.setWarehouseId(inventory.getWarehouseId());
                    alert.setAlertType("3");
                    alert.setAlertLevel("2");
                    alert.setQtyOnHand(inventory.getQtyOnHand());
                    alert.setExpiryDate(inventory.getExpiryDate());
                    alert.setDaysUntilExpiry((int) daysUntilExpiry);
                    alert.setStatus("0");
                    alert.setCreateTime(new Date());
                    wmsAlertMapper.insertAlert(alert);
                    count++;
                } else if (daysUntilExpiry < 0) {
                    // 已过期
                    WmsAlert alert = new WmsAlert();
                    alert.setProductId(inventory.getProductId());
                    alert.setProductCode(inventory.getProductCode());
                    alert.setProductName(inventory.getProductName());
                    alert.setWarehouseId(inventory.getWarehouseId());
                    alert.setAlertType("4");
                    alert.setAlertLevel("3");
                    alert.setQtyOnHand(inventory.getQtyOnHand());
                    alert.setExpiryDate(inventory.getExpiryDate());
                    alert.setDaysUntilExpiry((int) daysUntilExpiry);
                    alert.setStatus("0");
                    alert.setCreateTime(new Date());
                    wmsAlertMapper.insertAlert(alert);
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * 检查呆滞料
     *
     * @param days 天数
     * @return 结果
     */
    @Override
    @Transactional
    public int checkSlowMoving(int days)
    {
        // 这里简化处理，实际需要根据库存移动记录来判断
        // 可以根据 lastStockCheckDate 或者其他最后移动日期来判断
        int count = 0;
        Date today = new Date();
        List<WmsInventory> inventories = wmsInventoryMapper.selectInventoryList(new WmsInventory());

        for (WmsInventory inventory : inventories) {
            if (inventory.getLastStockCheckDate() != null) {
                long daysSinceLastMove = (today.getTime() - inventory.getLastStockCheckDate().getTime()) / (1000 * 60 * 60 * 24);
                if (daysSinceLastMove > days) {
                    WmsAlert alert = new WmsAlert();
                    alert.setProductId(inventory.getProductId());
                    alert.setProductCode(inventory.getProductCode());
                    alert.setProductName(inventory.getProductName());
                    alert.setWarehouseId(inventory.getWarehouseId());
                    alert.setAlertType("5");
                    alert.setAlertLevel("1");
                    alert.setQtyOnHand(inventory.getQtyOnHand());
                    alert.setLastMoveDate(inventory.getLastStockCheckDate());
                    alert.setStatus("0");
                    alert.setCreateTime(new Date());
                    wmsAlertMapper.insertAlert(alert);
                    count++;
                }
            }
        }

        return count;
    }
}
