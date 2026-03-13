package com.ruoyi.wms.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 入库单对象 wms_inbound_order
 *
 * @author ruoyi
 */
public class WmsInboundOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 入库单 ID */
    @Excel(name = "入库单 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long inboundId;

    /** 入库单号 */
    @Excel(name = "入库单号")
    private String inboundNo;

    /** 入库类型（1 采购入库 2 退货入库 3 调拨入库） */
    @Excel(name = "入库类型", readConverterExp = "1=采购入库，2=退货入库，3=调拨入库")
    private String inboundType;

    /** 来源类型（1 采购订单 2 退货单 3 调拨单） */
    @Excel(name = "来源类型", readConverterExp = "1=采购订单，2=退货单，3=调拨单")
    private String sourceType;

    /** 来源单号 */
    @Excel(name = "来源单号")
    private String sourceNo;

    /** 供应商 ID */
    @Excel(name = "供应商 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long supplierId;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 仓库 ID */
    @Excel(name = "仓库 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long warehouseId;

    /** 状态（0 草稿 1 待收货 2 收货中 3 待质检 4 待上架 5 已完成 6 已取消） */
    @Excel(name = "状态", readConverterExp = "0=草稿，1=待收货，2=收货中，3=待质检，4=待上架，5=已完成，6=已取消")
    private String status;

    /** 总数量 */
    @Excel(name = "总数量")
    private BigDecimal totalQty;

    /** 已收货数量 */
    @Excel(name = "已收货数量")
    private BigDecimal receivedQty;

    /** 合格数量 */
    @Excel(name = "合格数量")
    private BigDecimal qualifiedQty;

    /** 不合格数量 */
    @Excel(name = "不合格数量")
    private BigDecimal unqualifiedQty;

    /** 预计到货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计到货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expectedDate;

    /** 实际到货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际到货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualDate;

    public Long getInboundId()
    {
        return inboundId;
    }

    public void setInboundId(Long inboundId)
    {
        this.inboundId = inboundId;
    }

    public String getInboundNo()
    {
        return inboundNo;
    }

    public void setInboundNo(String inboundNo)
    {
        this.inboundNo = inboundNo;
    }

    public String getInboundType()
    {
        return inboundType;
    }

    public void setInboundType(String inboundType)
    {
        this.inboundType = inboundType;
    }

    public String getSourceType()
    {
        return sourceType;
    }

    public void setSourceType(String sourceType)
    {
        this.sourceType = sourceType;
    }

    public String getSourceNo()
    {
        return sourceNo;
    }

    public void setSourceNo(String sourceNo)
    {
        this.sourceNo = sourceNo;
    }

    public Long getSupplierId()
    {
        return supplierId;
    }

    public void setSupplierId(Long supplierId)
    {
        this.supplierId = supplierId;
    }

    public String getSupplierName()
    {
        return supplierName;
    }

    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }

    public Long getWarehouseId()
    {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId)
    {
        this.warehouseId = warehouseId;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public BigDecimal getTotalQty()
    {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty)
    {
        this.totalQty = totalQty;
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

    public Date getExpectedDate()
    {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate)
    {
        this.expectedDate = expectedDate;
    }

    public Date getActualDate()
    {
        return actualDate;
    }

    public void setActualDate(Date actualDate)
    {
        this.actualDate = actualDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("inboundId", getInboundId())
            .append("inboundNo", getInboundNo())
            .append("inboundType", getInboundType())
            .append("sourceType", getSourceType())
            .append("sourceNo", getSourceNo())
            .append("supplierId", getSupplierId())
            .append("supplierName", getSupplierName())
            .append("warehouseId", getWarehouseId())
            .append("status", getStatus())
            .append("totalQty", getTotalQty())
            .append("receivedQty", getReceivedQty())
            .append("qualifiedQty", getQualifiedQty())
            .append("unqualifiedQty", getUnqualifiedQty())
            .append("expectedDate", getExpectedDate())
            .append("actualDate", getActualDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
