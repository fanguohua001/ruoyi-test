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
 * 出库单对象 wms_outbound_order
 *
 * @author ruoyi
 */
public class WmsOutboundOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 出库单 ID */
    @Excel(name = "出库单 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long outboundId;

    /** 出库单号 */
    @Excel(name = "出库单号")
    private String outboundNo;

    /** 出库类型（1 销售出库 2 采购退货 3 调拨出库 4 其他出库） */
    @Excel(name = "出库类型", readConverterExp = "1=销售出库，2=采购退货，3=调拨出库，4=其他出库")
    private String outboundType;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 客户 ID */
    @Excel(name = "客户 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long customerId;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 仓库 ID */
    @Excel(name = "仓库 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long warehouseId;

    /** 状态（0 无 1 待审核 2 待拣货 3 拣货中 4 待复核 5 待打包 6 待发货 7 已发货 8 已取消） */
    @Excel(name = "状态", readConverterExp = "0=无，1=待审核，2=待拣货，3=拣货中，4=待复核，5=待打包，6=待发货，7=已发货，8=已取消")
    private String status;

    /** 是否完成（0 草稿 1 已完成） */
    @Excel(name = "是否完成", readConverterExp = "0=草稿，1=已完成")
    private Integer isFinished;

    /** 优先级（1 普通 2 加急 3 特急） */
    @Excel(name = "优先级", readConverterExp = "1=普通，2=加急，3=特急")
    private String priority;

    /** 波次号 */
    @Excel(name = "波次号")
    private String waveNo;

    /** 总数量 */
    @Excel(name = "总数量")
    private BigDecimal totalQty;

    /** 已拣货数量 */
    @Excel(name = "已拣货数量")
    private BigDecimal pickedQty;

    /** 已发货数量 */
    @Excel(name = "已发货数量")
    private BigDecimal shippedQty;

    /** 预计发货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计发货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expectedDate;

    /** 实际发货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际发货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualDate;

    /** 出库明细列表 */
    private List<WmsOutboundItem> items;

    public Long getOutboundId()
    {
        return outboundId;
    }

    public void setOutboundId(Long outboundId)
    {
        this.outboundId = outboundId;
    }

    public String getOutboundNo()
    {
        return outboundNo;
    }

    public void setOutboundNo(String outboundNo)
    {
        this.outboundNo = outboundNo;
    }

    public String getOutboundType()
    {
        return outboundType;
    }

    public void setOutboundType(String outboundType)
    {
        this.outboundType = outboundType;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
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

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public String getWaveNo()
    {
        return waveNo;
    }

    public void setWaveNo(String waveNo)
    {
        this.waveNo = waveNo;
    }

    public BigDecimal getTotalQty()
    {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty)
    {
        this.totalQty = totalQty;
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

    public List<WmsOutboundItem> getItems()
    {
        return items;
    }

    public void setItems(List<WmsOutboundItem> items)
    {
        this.items = items;
    }

    public Integer getIsFinished()
    {
        return isFinished;
    }

    public void setIsFinished(Integer isFinished)
    {
        this.isFinished = isFinished;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("outboundId", getOutboundId())
            .append("outboundNo", getOutboundNo())
            .append("outboundType", getOutboundType())
            .append("orderNo", getOrderNo())
            .append("customerId", getCustomerId())
            .append("customerName", getCustomerName())
            .append("warehouseId", getWarehouseId())
            .append("status", getStatus())
            .append("isFinished", getIsFinished())
            .append("priority", getPriority())
            .append("waveNo", getWaveNo())
            .append("totalQty", getTotalQty())
            .append("pickedQty", getPickedQty())
            .append("shippedQty", getShippedQty())
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
