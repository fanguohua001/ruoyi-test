package com.ruoyi.wms.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 库存预警对象 wms_alert
 *
 * @author ruoyi
 */
public class WmsAlert extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预警 ID */
    @Excel(name = "预警 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long alertId;

    /** 商品 ID */
    @Excel(name = "商品 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long productId;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String productCode;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String productName;

    /** 仓库 ID */
    @Excel(name = "仓库 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long warehouseId;

    /** 预警类型（1 低于安全库存 2 高于最高库存 3 近效期 4 已过期 5 呆滞） */
    @Excel(name = "预警类型", readConverterExp = "1=低于安全库存，2=高于最高库存，3=近效期，4=已过期，5=呆滞")
    private String alertType;

    /** 预警级别（1 提示 2 警告 3 严重） */
    @Excel(name = "预警级别", readConverterExp = "1=提示，2=警告，3=严重")
    private String alertLevel;

    /** 当前库存 */
    @Excel(name = "当前库存")
    private BigDecimal qtyOnHand;

    /** 安全库存 */
    @Excel(name = "安全库存")
    private BigDecimal safetyStock;

    /** 有效期至 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期至", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiryDate;

    /** 距离过期天数 */
    @Excel(name = "距离过期天数")
    private Integer daysUntilExpiry;

    /** 最后移动日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后移动日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastMoveDate;

    /** 状态（0 未处理 1 已处理 2 已忽略） */
    @Excel(name = "状态", readConverterExp = "0=未处理，1=已处理，2=已忽略")
    private String status;

    /** 处理人 */
    @Excel(name = "处理人")
    private String handleBy;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    /** 处理备注 */
    @Excel(name = "处理备注")
    private String handleRemark;

    public Long getAlertId()
    {
        return alertId;
    }

    public void setAlertId(Long alertId)
    {
        this.alertId = alertId;
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

    public Long getWarehouseId()
    {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId)
    {
        this.warehouseId = warehouseId;
    }

    public String getAlertType()
    {
        return alertType;
    }

    public void setAlertType(String alertType)
    {
        this.alertType = alertType;
    }

    public String getAlertLevel()
    {
        return alertLevel;
    }

    public void setAlertLevel(String alertLevel)
    {
        this.alertLevel = alertLevel;
    }

    public BigDecimal getQtyOnHand()
    {
        return qtyOnHand;
    }

    public void setQtyOnHand(BigDecimal qtyOnHand)
    {
        this.qtyOnHand = qtyOnHand;
    }

    public BigDecimal getSafetyStock()
    {
        return safetyStock;
    }

    public void setSafetyStock(BigDecimal safetyStock)
    {
        this.safetyStock = safetyStock;
    }

    public Date getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public Integer getDaysUntilExpiry()
    {
        return daysUntilExpiry;
    }

    public void setDaysUntilExpiry(Integer daysUntilExpiry)
    {
        this.daysUntilExpiry = daysUntilExpiry;
    }

    public Date getLastMoveDate()
    {
        return lastMoveDate;
    }

    public void setLastMoveDate(Date lastMoveDate)
    {
        this.lastMoveDate = lastMoveDate;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getHandleBy()
    {
        return handleBy;
    }

    public void setHandleBy(String handleBy)
    {
        this.handleBy = handleBy;
    }

    public Date getHandleTime()
    {
        return handleTime;
    }

    public void setHandleTime(Date handleTime)
    {
        this.handleTime = handleTime;
    }

    public String getHandleRemark()
    {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark)
    {
        this.handleRemark = handleRemark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("alertId", getAlertId())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("warehouseId", getWarehouseId())
            .append("alertType", getAlertType())
            .append("alertLevel", getAlertLevel())
            .append("qtyOnHand", getQtyOnHand())
            .append("safetyStock", getSafetyStock())
            .append("expiryDate", getExpiryDate())
            .append("daysUntilExpiry", getDaysUntilExpiry())
            .append("lastMoveDate", getLastMoveDate())
            .append("status", getStatus())
            .append("handleBy", getHandleBy())
            .append("handleTime", getHandleTime())
            .append("handleRemark", getHandleRemark())
            .append("createTime", getCreateTime())
            .toString();
    }
}
