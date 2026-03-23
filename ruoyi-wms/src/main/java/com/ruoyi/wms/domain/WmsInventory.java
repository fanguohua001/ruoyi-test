package com.ruoyi.wms.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 库存对象 wms_inventory
 *
 * @author ruoyi
 */
public class WmsInventory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 库存 ID */
    @Excel(name = "库存 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long inventoryId;

    /** 仓库 ID */
    @Excel(name = "仓库 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long warehouseId;

    /** 库位 ID */
    @Excel(name = "库位 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long locationId;

    /** 物料 ID */
    @Excel(name = "物料 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long productId;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String productCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String productName;

    /** 批次号 */
    @Excel(name = "批次号")
    private String batchNo;

    /** 生产日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生产日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date productionDate;

    /** 有效期至 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期至", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiryDate;

    /** 序列号 */
    @Excel(name = "序列号")
    private String serialNo;

    /** 现有数量 */
    @Excel(name = "现有数量")
    private BigDecimal qtyOnHand;

    /** 可用数量 */
    @Excel(name = "可用数量")
    private BigDecimal qtyAvailable;

    /** 锁定数量 */
    @Excel(name = "锁定数量")
    private BigDecimal qtyLocked;

    /** 损坏数量 */
    @Excel(name = "损坏数量")
    private BigDecimal qtyDamaged;

    /** 单位成本 */
    @Excel(name = "单位成本")
    private BigDecimal unitCost;

    /** 总成本 */
    @Excel(name = "总成本")
    private BigDecimal totalCost;

    /** 状态（1 正常 2 锁定 3 冻结） */
    @Excel(name = "状态", readConverterExp = "1=正常，2=锁定，3=冻结")
    private String status;

    /** 最后盘点日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后盘点日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastStockCheckDate;

    public Long getInventoryId()
    {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId)
    {
        this.inventoryId = inventoryId;
    }

    public Long getWarehouseId()
    {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId)
    {
        this.warehouseId = warehouseId;
    }

    public Long getLocationId()
    {
        return locationId;
    }

    public void setLocationId(Long locationId)
    {
        this.locationId = locationId;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

    public Date getProductionDate()
    {
        return productionDate;
    }

    public void setProductionDate(Date productionDate)
    {
        this.productionDate = productionDate;
    }

    public Date getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public String getSerialNo()
    {
        return serialNo;
    }

    public void setSerialNo(String serialNo)
    {
        this.serialNo = serialNo;
    }

    public BigDecimal getQtyOnHand()
    {
        return qtyOnHand;
    }

    public void setQtyOnHand(BigDecimal qtyOnHand)
    {
        this.qtyOnHand = qtyOnHand;
    }

    public BigDecimal getQtyAvailable()
    {
        return qtyAvailable;
    }

    public void setQtyAvailable(BigDecimal qtyAvailable)
    {
        this.qtyAvailable = qtyAvailable;
    }

    public BigDecimal getQtyLocked()
    {
        return qtyLocked;
    }

    public void setQtyLocked(BigDecimal qtyLocked)
    {
        this.qtyLocked = qtyLocked;
    }

    public BigDecimal getQtyDamaged()
    {
        return qtyDamaged;
    }

    public void setQtyDamaged(BigDecimal qtyDamaged)
    {
        this.qtyDamaged = qtyDamaged;
    }

    public BigDecimal getUnitCost()
    {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost)
    {
        this.unitCost = unitCost;
    }

    public BigDecimal getTotalCost()
    {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost)
    {
        this.totalCost = totalCost;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getLastStockCheckDate()
    {
        return lastStockCheckDate;
    }

    public void setLastStockCheckDate(Date lastStockCheckDate)
    {
        this.lastStockCheckDate = lastStockCheckDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("inventoryId", getInventoryId())
            .append("warehouseId", getWarehouseId())
            .append("locationId", getLocationId())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("batchNo", getBatchNo())
            .append("productionDate", getProductionDate())
            .append("expiryDate", getExpiryDate())
            .append("serialNo", getSerialNo())
            .append("qtyOnHand", getQtyOnHand())
            .append("qtyAvailable", getQtyAvailable())
            .append("qtyLocked", getQtyLocked())
            .append("qtyDamaged", getQtyDamaged())
            .append("unitCost", getUnitCost())
            .append("totalCost", getTotalCost())
            .append("status", getStatus())
            .append("lastStockCheckDate", getLastStockCheckDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
