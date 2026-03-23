package com.ruoyi.wms.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户订单对象 wms_customer_order
 *
 * @author ruoyi
 */
public class WmsCustomerOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单 ID */
    @Excel(name = "订单 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long orderId;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 客户编码 */
    @Excel(name = "客户编码")
    private String customerCode;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 订单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderDate;

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

    /** 发货状态（0 未发货 1 部分发货 2 已发货） */
    @Excel(name = "发货状态", readConverterExp = "0=未发货，1=部分发货，2=已发货")
    private String shipStatus;

    /** 说明 */
    @Excel(name = "说明")
    private String remark;

    /** 订单明细列表 */
    private List<WmsCustomerOrderItem> items;

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

    public String getCustomerCode()
    {
        return customerCode;
    }

    public void setCustomerCode(String customerCode)
    {
        this.customerCode = customerCode;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public Date getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
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

    public String getShipStatus()
    {
        return shipStatus;
    }

    public void setShipStatus(String shipStatus)
    {
        this.shipStatus = shipStatus;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public List<WmsCustomerOrderItem> getItems()
    {
        return items;
    }

    public void setItems(List<WmsCustomerOrderItem> items)
    {
        this.items = items;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("customerCode", getCustomerCode())
            .append("customerName", getCustomerName())
            .append("orderDate", getOrderDate())
            .append("requiredDate", getRequiredDate())
            .append("totalQty", getTotalQty())
            .append("totalAmount", getTotalAmount())
            .append("orderStatus", getOrderStatus())
            .append("shipStatus", getShipStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("items", getItems())
            .toString();
    }
}
