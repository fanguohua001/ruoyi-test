package com.ruoyi.wms.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 采购订单对象 wms_purchase_order
 *
 * @author ruoyi
 */
public class WmsPurchaseOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单 ID */
    @Excel(name = "订单 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long orderId;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 订单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderDate;

    /** 供应商 ID */
    @Excel(name = "供应商 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long supplierId;

    /** 供应商编码 */
    @Excel(name = "供应商编码")
    private String supplierCode;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 要求到货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "要求到货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date requiredDate;

    /** 订单数量合计 */
    @Excel(name = "订单数量合计")
    private BigDecimal totalQty;

    /** 金额合计 */
    @Excel(name = "金额合计")
    private BigDecimal totalAmount;

    /** 订单状态（0 草稿 1 完成） */
    @Excel(name = "订单状态", readConverterExp = "0=草稿，1=完成")
    private Integer orderStatus;

    /** 说明 */
    @Excel(name = "说明")
    private String remark;

    /** 订单明细列表 */
    private java.util.List<WmsPurchaseOrderItem> items;

    /** 开始时间（用于日期范围查询） */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /** 结束时间（用于日期范围查询） */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public Date getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
    }

    public Long getSupplierId()
    {
        return supplierId;
    }

    public void setSupplierId(Long supplierId)
    {
        this.supplierId = supplierId;
    }

    public String getSupplierCode()
    {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName()
    {
        return supplierName;
    }

    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }

    public Date getRequiredDate()
    {
        return requiredDate;
    }

    public void setRequiredDate(Date requiredDate)
    {
        this.requiredDate = requiredDate;
    }

    public BigDecimal getTotalQty()
    {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty)
    {
        this.totalQty = totalQty;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public Integer getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public java.util.List<WmsPurchaseOrderItem> getItems()
    {
        return items;
    }

    public void setItems(java.util.List<WmsPurchaseOrderItem> items)
    {
        this.items = items;
    }

    public Date getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("orderDate", getOrderDate())
            .append("supplierId", getSupplierId())
            .append("supplierCode", getSupplierCode())
            .append("supplierName", getSupplierName())
            .append("requiredDate", getRequiredDate())
            .append("totalQty", getTotalQty())
            .append("totalAmount", getTotalAmount())
            .append("orderStatus", getOrderStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("items", getItems())
            .append("beginTime", getBeginTime())
            .append("endTime", getEndTime())
            .toString();
    }
}
