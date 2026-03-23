package com.ruoyi.wms.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物料分类对象 wms_category
 *
 * @author ruoyi
 */
public class WmsCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类 ID */
    @Excel(name = "分类 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long categoryId;

    /** 分类编码 */
    @Excel(name = "分类编码")
    @NotBlank(message = "分类编码不能为空")
    @Size(min = 0, max = 50, message = "分类编码长度不能超过 50 个字符")
    private String categoryCode;

    /** 分类名称 */
    @Excel(name = "分类名称")
    @NotBlank(message = "分类名称不能为空")
    @Size(min = 0, max = 100, message = "分类名称长度不能超过 100 个字符")
    private String categoryName;

    /** 父分类 ID */
    @Excel(name = "父分类 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long parentId;

    /** 排序 */
    @Excel(name = "排序")
    private Integer orderNum;

    /** 状态（0 正常 1 停用） */
    @Excel(name = "状态", readConverterExp = "0=正常，1=停用")
    private String status;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCategoryCode()
    {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode)
    {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("categoryId", getCategoryId())
            .append("categoryCode", getCategoryCode())
            .append("categoryName", getCategoryName())
            .append("parentId", getParentId())
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
