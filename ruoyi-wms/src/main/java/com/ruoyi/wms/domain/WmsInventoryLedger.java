package com.ruoyi.wms.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 库存台账对象 wms_inventory_ledger
 *
 * @author ruoyi
 */
public class WmsInventoryLedger extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 台账 ID */
    @Excel(name = "台账 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long ledgerId;

    /** 交易编号 */
    @Excel(name = "交易编号")
    private String transactionNo;

    /** 交易类型（1 入库 2 出库 3 盘点调整 4 移库 5 冻结/解冻） */
    @Excel(name = "交易类型", readConverterExp = "1=入库，2=出库，3=盘点调整，4=移库，5=冻结/解冻")
    private String transactionType;

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

    /** 变动前数量 */
    @Excel(name = "变动前数量")
    private BigDecimal qtyBefore;

    /** 变动数量 (+入 -出) */
    @Excel(name = "变动数量")
    private BigDecimal qtyChange;

    /** 变动后数量 */
    @Excel(name = "变动后数量")
    private BigDecimal qtyAfter;

    /** 单位成本 */
    @Excel(name = "单位成本")
    private BigDecimal unitCost;

    /** 关联单据类型（wms_inbound_order/wms_outbound_order 等） */
    @Excel(name = "关联单据类型")
    private String referenceType;

    /** 关联单据 ID */
    @Excel(name = "关联单据 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long referenceId;

    /** 关联单据号（入库单号/出库单号） */
    @Excel(name = "关联单据号")
    private String referenceNo;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public Long getLedgerId()
    {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId)
    {
        this.ledgerId = ledgerId;
    }

    public String getTransactionNo()
    {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo)
    {
        this.transactionNo = transactionNo;
    }

    public String getTransactionType()
    {
        return transactionType;
    }

    public void setTransactionType(String transactionType)
    {
        this.transactionType = transactionType;
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

    public BigDecimal getQtyBefore()
    {
        return qtyBefore;
    }

    public void setQtyBefore(BigDecimal qtyBefore)
    {
        this.qtyBefore = qtyBefore;
    }

    public BigDecimal getQtyChange()
    {
        return qtyChange;
    }

    public void setQtyChange(BigDecimal qtyChange)
    {
        this.qtyChange = qtyChange;
    }

    public BigDecimal getQtyAfter()
    {
        return qtyAfter;
    }

    public void setQtyAfter(BigDecimal qtyAfter)
    {
        this.qtyAfter = qtyAfter;
    }

    public BigDecimal getUnitCost()
    {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost)
    {
        this.unitCost = unitCost;
    }

    public String getReferenceType()
    {
        return referenceType;
    }

    public void setReferenceType(String referenceType)
    {
        this.referenceType = referenceType;
    }

    public Long getReferenceId()
    {
        return referenceId;
    }

    public void setReferenceId(Long referenceId)
    {
        this.referenceId = referenceId;
    }

    public String getReferenceNo()
    {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo)
    {
        this.referenceNo = referenceNo;
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
            .append("ledgerId", getLedgerId())
            .append("transactionNo", getTransactionNo())
            .append("transactionType", getTransactionType())
            .append("warehouseId", getWarehouseId())
            .append("locationId", getLocationId())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("batchNo", getBatchNo())
            .append("qtyBefore", getQtyBefore())
            .append("qtyChange", getQtyChange())
            .append("qtyAfter", getQtyAfter())
            .append("unitCost", getUnitCost())
            .append("referenceType", getReferenceType())
            .append("referenceId", getReferenceId())
            .append("referenceNo", getReferenceNo())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .toString();
    }
}
