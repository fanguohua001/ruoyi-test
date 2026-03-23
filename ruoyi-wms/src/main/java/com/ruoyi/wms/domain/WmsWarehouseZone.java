package com.ruoyi.wms.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 库区对象 wms_warehouse_zone
 *
 * @author ruoyi
 */
public class WmsWarehouseZone extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 库区 ID */
    @Excel(name = "库区 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long zoneId;

    /** 库区编码 */
    @Excel(name = "库区编码")
    @NotBlank(message = "库区编码不能为空")
    @Size(min = 0, max = 50, message = "库区编码长度不能超过 50 个字符")
    private String zoneCode;

    /** 库区名称 */
    @Excel(name = "库区名称")
    @NotBlank(message = "库区名称不能为空")
    @Size(min = 0, max = 200, message = "库区名称长度不能超过 200 个字符")
    private String zoneName;

    /** 仓库 ID */
    @Excel(name = "仓库 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long warehouseId;

    /** 仓库编码 */
    @Excel(name = "仓库编码")
    private String warehouseCode;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String warehouseName;

    /** 库区类型（1 存储区 2 拣货区 3 收货区 4 发货区 5 退货区 6 质检区） */
    @Excel(name = "库区类型", readConverterExp = "1=存储区，2=拣货区，3=收货区，4=发货区，5=退货区，6=质检区")
    private String zoneType;

    /** 顺序号 */
    @Excel(name = "顺序号")
    private Integer orderNum;

    /** 状态（0 正常 1 停用） */
    @Excel(name = "状态", readConverterExp = "0=正常，1=停用")
    private String status;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public Long getZoneId()
    {
        return zoneId;
    }

    public void setZoneId(Long zoneId)
    {
        this.zoneId = zoneId;
    }

    public String getZoneCode()
    {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode)
    {
        this.zoneCode = zoneCode;
    }

    public String getZoneName()
    {
        return zoneName;
    }

    public void setZoneName(String zoneName)
    {
        this.zoneName = zoneName;
    }

    public Long getWarehouseId()
    {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId)
    {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseCode()
    {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode)
    {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName()
    {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName)
    {
        this.warehouseName = warehouseName;
    }

    public String getZoneType()
    {
        return zoneType;
    }

    public void setZoneType(String zoneType)
    {
        this.zoneType = zoneType;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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
            .append("zoneId", getZoneId())
            .append("zoneCode", getZoneCode())
            .append("zoneName", getZoneName())
            .append("warehouseId", getWarehouseId())
            .append("warehouseCode", getWarehouseCode())
            .append("warehouseName", getWarehouseName())
            .append("zoneType", getZoneType())
            .append("orderNum", getOrderNum())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
