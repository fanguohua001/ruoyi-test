package com.ruoyi.wms.domain;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物料对象 wms_product
 *
 * @author ruoyi
 */
public class WmsProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 物料 ID */
    @Excel(name = "物料 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long productId;

    /** 物料编码 */
    @Excel(name = "物料编码")
    @NotBlank(message = "物料编码不能为空")
    @Size(min = 0, max = 50, message = "物料编码长度不能超过 50 个字符")
    private String productCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    @NotBlank(message = "物料名称不能为空")
    @Size(min = 0, max = 200, message = "物料名称长度不能超过 200 个字符")
    private String productName;

    /** 分类 */
    @Excel(name = "分类")
    private String category;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specification;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 安全库存 */
    @Excel(name = "安全库存")
    private BigDecimal safetyStock;

    /** 最高库存 */
    @Excel(name = "最高库存")
    private BigDecimal maxStock;

    /** 保质期天数 */
    @Excel(name = "保质期天数")
    private Integer shelfLifeDays;

    /** 状态（0 停用 1 启用） */
    @Excel(name = "状态", readConverterExp = "0=停用，1=启用")
    private String status;

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

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getSpecification()
    {
        return specification;
    }

    public void setSpecification(String specification)
    {
        this.specification = specification;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public BigDecimal getSafetyStock()
    {
        return safetyStock;
    }

    public void setSafetyStock(BigDecimal safetyStock)
    {
        this.safetyStock = safetyStock;
    }

    public BigDecimal getMaxStock()
    {
        return maxStock;
    }

    public void setMaxStock(BigDecimal maxStock)
    {
        this.maxStock = maxStock;
    }

    public Integer getShelfLifeDays()
    {
        return shelfLifeDays;
    }

    public void setShelfLifeDays(Integer shelfLifeDays)
    {
        this.shelfLifeDays = shelfLifeDays;
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
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("category", getCategory())
            .append("specification", getSpecification())
            .append("unit", getUnit())
            .append("safetyStock", getSafetyStock())
            .append("maxStock", getMaxStock())
            .append("shelfLifeDays", getShelfLifeDays())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
