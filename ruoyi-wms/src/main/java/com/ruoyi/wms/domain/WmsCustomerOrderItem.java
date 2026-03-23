package com.ruoyi.wms.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户订单明细对象 wms_customer_order_item
 *
 * @author ruoyi
 */
public class WmsCustomerOrderItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细 ID */
    private Long itemId;

    /** 订单 ID */
    private Long orderId;

    /** 物料编码 */
    private String productCode;

    /** 物料名称 */
    private String productName;

    /** 不含税单价 */
    private BigDecimal unitPrice;

    /** 数量 */
    private BigDecimal quantity;

    /** 不含税金额 */
    private BigDecimal amount;

    /** 税率（%） */
    private BigDecimal taxRate;

    /** 含税单价 */
    private BigDecimal unitPriceTax;

    /** 含税金额 */
    private BigDecimal amountTax;

    /** 备注 */
    private String remark;

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
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

    public BigDecimal getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity()
    {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity)
    {
        this.quantity = quantity;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getTaxRate()
    {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate)
    {
        this.taxRate = taxRate;
    }

    public BigDecimal getUnitPriceTax()
    {
        return unitPriceTax;
    }

    public void setUnitPriceTax(BigDecimal unitPriceTax)
    {
        this.unitPriceTax = unitPriceTax;
    }

    public BigDecimal getAmountTax()
    {
        return amountTax;
    }

    public void setAmountTax(BigDecimal amountTax)
    {
        this.amountTax = amountTax;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("itemId", getItemId())
            .append("orderId", getOrderId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("unitPrice", getUnitPrice())
            .append("quantity", getQuantity())
            .append("amount", getAmount())
            .append("taxRate", getTaxRate())
            .append("unitPriceTax", getUnitPriceTax())
            .append("amountTax", getAmountTax())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
