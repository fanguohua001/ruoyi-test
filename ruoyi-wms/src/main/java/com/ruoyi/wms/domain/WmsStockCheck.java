package com.ruoyi.wms.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 盘点单对象 wms_stock_check
 *
 * @author ruoyi
 */
public class WmsStockCheck extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 盘点单 ID */
    @Excel(name = "盘点单 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long checkId;

    /** 盘点单号 */
    @Excel(name = "盘点单号")
    private String checkNo;

    /** 盘点类型（1 定期盘点 2 循环盘点 3 动盘 4 静盘） */
    @Excel(name = "盘点类型", readConverterExp = "1=定期盘点，2=循环盘点，3=动盘，4=静盘")
    private String checkType;

    /** 仓库 ID */
    @Excel(name = "仓库 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long warehouseId;

    /** 库位 ID */
    @Excel(name = "库位 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long locationId;

    /** 物料 ID */
    @Excel(name = "物料 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long productId;

    /** 状态（0 草稿 1 盘点中 2 待审核 3 已完成 4 已取消） */
    @Excel(name = "状态", readConverterExp = "0=草稿，1=盘点中，2=待审核，3=已完成，4=已取消")
    private String status;

    /** 计划盘点日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划盘点日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planDate;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 应盘数量 */
    @Excel(name = "应盘数量")
    private Integer totalQty;

    /** 已盘数量 */
    @Excel(name = "已盘数量")
    private Integer checkedQty;

    /** 差异数量 */
    @Excel(name = "差异数量")
    private Integer diffQty;

    /** 差异金额 */
    @Excel(name = "差异金额")
    private BigDecimal diffAmount;

    public Long getCheckId()
    {
        return checkId;
    }

    public void setCheckId(Long checkId)
    {
        this.checkId = checkId;
    }

    public String getCheckNo()
    {
        return checkNo;
    }

    public void setCheckNo(String checkNo)
    {
        this.checkNo = checkNo;
    }

    public String getCheckType()
    {
        return checkType;
    }

    public void setCheckType(String checkType)
    {
        this.checkType = checkType;
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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getPlanDate()
    {
        return planDate;
    }

    public void setPlanDate(Date planDate)
    {
        this.planDate = planDate;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Integer getTotalQty()
    {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty)
    {
        this.totalQty = totalQty;
    }

    public Integer getCheckedQty()
    {
        return checkedQty;
    }

    public void setCheckedQty(Integer checkedQty)
    {
        this.checkedQty = checkedQty;
    }

    public Integer getDiffQty()
    {
        return diffQty;
    }

    public void setDiffQty(Integer diffQty)
    {
        this.diffQty = diffQty;
    }

    public BigDecimal getDiffAmount()
    {
        return diffAmount;
    }

    public void setDiffAmount(BigDecimal diffAmount)
    {
        this.diffAmount = diffAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("checkId", getCheckId())
            .append("checkNo", getCheckNo())
            .append("checkType", getCheckType())
            .append("warehouseId", getWarehouseId())
            .append("locationId", getLocationId())
            .append("productId", getProductId())
            .append("status", getStatus())
            .append("planDate", getPlanDate())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("totalQty", getTotalQty())
            .append("checkedQty", getCheckedQty())
            .append("diffQty", getDiffQty())
            .append("diffAmount", getDiffAmount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
