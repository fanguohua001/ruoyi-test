package com.ruoyi.wms.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 仓库对象 wms_warehouse
 *
 * @author ruoyi
 */
public class WmsWarehouse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 仓库 ID */
    @Excel(name = "仓库 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long warehouseId;

    /** 仓库编码 */
    @Excel(name = "仓库编码")
    @NotBlank(message = "仓库编码不能为空")
    @Size(min = 0, max = 50, message = "仓库编码长度不能超过 50 个字符")
    private String warehouseCode;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    @NotBlank(message = "仓库名称不能为空")
    @Size(min = 0, max = 200, message = "仓库名称长度不能超过 200 个字符")
    private String warehouseName;

    /** 仓库类型（1 原料仓 2 成品仓 3 辅料仓 4 备件仓） */
    @Excel(name = "仓库类型", readConverterExp = "1=原料仓，2=成品仓，3=辅料仓，4=备件仓")
    private String warehouseType;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 负责人 */
    @Excel(name = "负责人")
    private String personInCharge;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 面积（平方米） */
    @Excel(name = "面积")
    private java.math.BigDecimal area;

    /** 状态（0 正常 1 停用） */
    @Excel(name = "状态", readConverterExp = "0=正常，1=停用")
    private String status;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

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

    public String getWarehouseType()
    {
        return warehouseType;
    }

    public void setWarehouseType(String warehouseType)
    {
        this.warehouseType = warehouseType;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPersonInCharge()
    {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge)
    {
        this.personInCharge = personInCharge;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public java.math.BigDecimal getArea()
    {
        return area;
    }

    public void setArea(java.math.BigDecimal area)
    {
        this.area = area;
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
            .append("warehouseId", getWarehouseId())
            .append("warehouseCode", getWarehouseCode())
            .append("warehouseName", getWarehouseName())
            .append("warehouseType", getWarehouseType())
            .append("address", getAddress())
            .append("personInCharge", getPersonInCharge())
            .append("phone", getPhone())
            .append("area", getArea())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
