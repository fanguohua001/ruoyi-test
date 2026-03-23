package com.ruoyi.wms.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 入库明细对象 wms_inbound_item
 *
 * @author ruoyi
 */
public class WmsInboundItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细 ID */
    @Excel(name = "明细 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long itemId;

    /** 入库单 ID */
    @Excel(name = "入库单 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long inboundId;

    /** 物料 ID */
    @Excel(name = "物料 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long productId;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String productCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String productName;

    /** 应收数量 */
    @Excel(name = "应收数量")
    private BigDecimal expectedQty;

    /** 实收数量 */
    @Excel(name = "实收数量")
    private BigDecimal receivedQty;

    /** 合格数量 */
    @Excel(name = "合格数量")
    private BigDecimal qualifiedQty;

    /** 不合格数量 */
    @Excel(name = "不合格数量")
    private BigDecimal unqualifiedQty;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

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

    /** 上架库位 ID */
    @Excel(name = "上架库位 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long locationId;

    /** 状态（0 待收货 1 已收货 2 待质检 3 已质检 4 已上架） */
    @Excel(name = "状态", readConverterExp = "0=待收货，1=已收货，2=待质检，3=已质检，4=已上架")
    private String status;

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getInboundId()
    {
        return inboundId;
    }

    public void setInboundId(Long inboundId)
    {
        this.inboundId = inboundId;
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

    public BigDecimal getExpectedQty()
    {
        return expectedQty;
    }

    public void setExpectedQty(BigDecimal expectedQty)
    {
        this.expectedQty = expectedQty;
    }

    public BigDecimal getReceivedQty()
    {
        return receivedQty;
    }

    public void setReceivedQty(BigDecimal receivedQty)
    {
        this.receivedQty = receivedQty;
    }

    public BigDecimal getQualifiedQty()
    {
        return qualifiedQty;
    }

    public void setQualifiedQty(BigDecimal qualifiedQty)
    {
        this.qualifiedQty = qualifiedQty;
    }

    public BigDecimal getUnqualifiedQty()
    {
        return unqualifiedQty;
    }

    public void setUnqualifiedQty(BigDecimal unqualifiedQty)
    {
        this.unqualifiedQty = unqualifiedQty;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
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

    public Long getLocationId()
    {
        return locationId;
    }

    public void setLocationId(Long locationId)
    {
        this.locationId = locationId;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("itemId", getItemId())
            .append("inboundId", getInboundId())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("expectedQty", getExpectedQty())
            .append("receivedQty", getReceivedQty())
            .append("qualifiedQty", getQualifiedQty())
            .append("unqualifiedQty", getUnqualifiedQty())
            .append("unit", getUnit())
            .append("batchNo", getBatchNo())
            .append("productionDate", getProductionDate())
            .append("expiryDate", getExpiryDate())
            .append("locationId", getLocationId())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
