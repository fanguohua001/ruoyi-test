package com.ruoyi.wms.domain;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 库位对象 wms_location
 *
 * @author ruoyi
 */
public class WmsLocation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 库位 ID */
    @Excel(name = "库位 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long locationId;

    /** 库位编码 */
    @Excel(name = "库位编码")
    @NotBlank(message = "库位编码不能为空")
    @Size(min = 0, max = 50, message = "库位编码长度不能超过 50 个字符")
    private String locationCode;

    /** 库位名称 */
    @Excel(name = "库位名称")
    @NotBlank(message = "库位名称不能为空")
    @Size(min = 0, max = 200, message = "库位名称长度不能超过 200 个字符")
    private String locationName;

    /** 库位类型（1 存储位 2 拣货位 3 暂存位） */
    @Excel(name = "库位类型", readConverterExp = "1=存储位，2=拣货位，3=暂存位")
    private String locationType;

    /** 区域编码 */
    @Excel(name = "区域编码")
    private String areaCode;

    /** 行号 */
    @Excel(name = "行号")
    private String rowNo;

    /** 列号 */
    @Excel(name = "列号")
    private String columnNo;

    /** 层号 */
    @Excel(name = "层号")
    private String levelNo;

    /** 最大承重 */
    @Excel(name = "最大承重")
    private BigDecimal maxWeight;

    /** 最大体积 */
    @Excel(name = "最大体积")
    private BigDecimal maxVolume;

    /** 状态（0 禁用 1 启用 2 占用 3 锁定） */
    @Excel(name = "状态", readConverterExp = "0=禁用，1=启用，2=占用，3=锁定")
    private String status;

    public Long getLocationId()
    {
        return locationId;
    }

    public void setLocationId(Long locationId)
    {
        this.locationId = locationId;
    }

    public String getLocationCode()
    {
        return locationCode;
    }

    public void setLocationCode(String locationCode)
    {
        this.locationCode = locationCode;
    }

    public String getLocationName()
    {
        return locationName;
    }

    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    public String getLocationType()
    {
        return locationType;
    }

    public void setLocationType(String locationType)
    {
        this.locationType = locationType;
    }

    public String getAreaCode()
    {
        return areaCode;
    }

    public void setAreaCode(String areaCode)
    {
        this.areaCode = areaCode;
    }

    public String getRowNo()
    {
        return rowNo;
    }

    public void setRowNo(String rowNo)
    {
        this.rowNo = rowNo;
    }

    public String getColumnNo()
    {
        return columnNo;
    }

    public void setColumnNo(String columnNo)
    {
        this.columnNo = columnNo;
    }

    public String getLevelNo()
    {
        return levelNo;
    }

    public void setLevelNo(String levelNo)
    {
        this.levelNo = levelNo;
    }

    public BigDecimal getMaxWeight()
    {
        return maxWeight;
    }

    public void setMaxWeight(BigDecimal maxWeight)
    {
        this.maxWeight = maxWeight;
    }

    public BigDecimal getMaxVolume()
    {
        return maxVolume;
    }

    public void setMaxVolume(BigDecimal maxVolume)
    {
        this.maxVolume = maxVolume;
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
            .append("locationId", getLocationId())
            .append("locationCode", getLocationCode())
            .append("locationName", getLocationName())
            .append("locationType", getLocationType())
            .append("areaCode", getAreaCode())
            .append("rowNo", getRowNo())
            .append("columnNo", getColumnNo())
            .append("levelNo", getLevelNo())
            .append("maxWeight", getMaxWeight())
            .append("maxVolume", getMaxVolume())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
