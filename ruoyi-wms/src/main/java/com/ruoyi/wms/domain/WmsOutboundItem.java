package com.ruoyi.wms.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 出库明细对象 wms_outbound_item
 *
 * @author ruoyi
 */
public class WmsOutboundItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细 ID */
    @Excel(name = "明细 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long itemId;

    /** 出库单 ID */
    @Excel(name = "出库单 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long outboundId;

    /** 商品 ID */
    @Excel(name = "商品 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long productId;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String productCode;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String productName;

    /** 订单数量 */
    @Excel(name = "订单数量")
    private BigDecimal orderQty;

    /** 已拣货数量 */
    @Excel(name = "已拣货数量")
    private BigDecimal pickedQty;

    /** 已发货数量 */
    @Excel(name = "已发货数量")
    private BigDecimal shippedQty;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 拣货策略（1 FIFO 2 LIFO 3 近效期优先） */
    @Excel(name = "拣货策略", readConverterExp = "1=FIFO,2=LIFO,3=近效期优先")
    private String pickingStrategy;

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getOutboundId()
    {
        return outboundId;
    }

    public void setOutboundId(Long outboundId)
    {
        this.outboundId = outboundId;
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

    public BigDecimal getOrderQty()
    {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty)
    {
        this.orderQty = orderQty;
    }

    public BigDecimal getPickedQty()
    {
        return pickedQty;
    }

    public void setPickedQty(BigDecimal pickedQty)
    {
        this.pickedQty = pickedQty;
    }

    public BigDecimal getShippedQty()
    {
        return shippedQty;
    }

    public void setShippedQty(BigDecimal shippedQty)
    {
        this.shippedQty = shippedQty;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getPickingStrategy()
    {
        return pickingStrategy;
    }

    public void setPickingStrategy(String pickingStrategy)
    {
        this.pickingStrategy = pickingStrategy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("itemId", getItemId())
            .append("outboundId", getOutboundId())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("orderQty", getOrderQty())
            .append("pickedQty", getPickedQty())
            .append("shippedQty", getShippedQty())
            .append("unit", getUnit())
            .append("pickingStrategy", getPickingStrategy())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
