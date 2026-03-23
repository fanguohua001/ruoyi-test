package com.ruoyi.wms.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 移库单对象 wms_transfer
 *
 * @author ruoyi
 */
public class WmsTransfer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 移库单 ID */
    @Excel(name = "移库单 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long transferId;

    /** 移库单号 */
    @Excel(name = "移库单号")
    private String transferNo;

    /** 仓库 ID */
    @Excel(name = "仓库 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long warehouseId;

    /** 源库位 ID */
    @Excel(name = "源库位 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long fromLocationId;

    /** 目标库位 ID */
    @Excel(name = "目标库位 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long toLocationId;

    /** 状态（0 草稿 1 待执行 2 已完成 3 已取消） */
    @Excel(name = "状态", readConverterExp = "0=草稿，1=待执行，2=已完成，3=已取消")
    private String status;

    /** 物料 ID */
    @Excel(name = "物料 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long productId;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String productCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String productName;

    /** 移库数量 */
    @Excel(name = "移库数量")
    private java.math.BigDecimal qty;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public Long getTransferId()
    {
        return transferId;
    }

    public void setTransferId(Long transferId)
    {
        this.transferId = transferId;
    }

    public String getTransferNo()
    {
        return transferNo;
    }

    public void setTransferNo(String transferNo)
    {
        this.transferNo = transferNo;
    }

    public Long getWarehouseId()
    {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId)
    {
        this.warehouseId = warehouseId;
    }

    public Long getFromLocationId()
    {
        return fromLocationId;
    }

    public void setFromLocationId(Long fromLocationId)
    {
        this.fromLocationId = fromLocationId;
    }

    public Long getToLocationId()
    {
        return toLocationId;
    }

    public void setToLocationId(Long toLocationId)
    {
        this.toLocationId = toLocationId;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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

    public java.math.BigDecimal getQty()
    {
        return qty;
    }

    public void setQty(java.math.BigDecimal qty)
    {
        this.qty = qty;
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
            .append("transferId", getTransferId())
            .append("transferNo", getTransferNo())
            .append("warehouseId", getWarehouseId())
            .append("fromLocationId", getFromLocationId())
            .append("toLocationId", getToLocationId())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("qty", getQty())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
