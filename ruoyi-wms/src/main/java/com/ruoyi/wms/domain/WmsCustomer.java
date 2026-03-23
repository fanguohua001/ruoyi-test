package com.ruoyi.wms.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户对象 wms_customer
 *
 * @author ruoyi
 */
public class WmsCustomer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 客户 ID */
    @Excel(name = "客户 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long customerId;

    /** 客户编码 */
    @Excel(name = "客户编码")
    @NotBlank(message = "客户编码不能为空")
    @Size(min = 0, max = 50, message = "客户编码长度不能超过 50 个字符")
    private String customerCode;

    /** 客户名称 */
    @Excel(name = "客户名称")
    @NotBlank(message = "客户名称不能为空")
    @Size(min = 0, max = 200, message = "客户名称长度不能超过 200 个字符")
    private String customerName;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contact;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 状态（0 正常 1 停用） */
    @Excel(name = "状态", readConverterExp = "0=正常，1=停用")
    private String status;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
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

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
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
            .append("customerId", getCustomerId())
            .append("customerCode", getCustomerCode())
            .append("customerName", getCustomerName())
            .append("contact", getContact())
            .append("phone", getPhone())
            .append("address", getAddress())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
