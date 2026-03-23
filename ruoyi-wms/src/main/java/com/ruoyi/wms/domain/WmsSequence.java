package com.ruoyi.wms.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 序号管理对象 wms_sequence
 *
 * @author ruoyi
 */
public class WmsSequence extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 ID */
    @Excel(name = "序号 ID", cellType = Excel.ColumnType.NUMERIC)
    private Long sequenceId;

    /** 业务类型（inbound 入库单 outbound 出库单 check 盘点单 transfer 移库单） */
    @Excel(name = "业务类型")
    private String bizType;

    /** 序号前缀 */
    @Excel(name = "序号前缀")
    private String prefix;

    /** 当前序号 */
    @Excel(name = "当前序号", cellType = Excel.ColumnType.NUMERIC)
    private Long currentSeq;

    /** 序号长度（不足补 0） */
    @Excel(name = "序号长度", cellType = Excel.ColumnType.NUMERIC)
    private Integer seqLength;

    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    public void setSequenceId(Long sequenceId)
    {
        this.sequenceId = sequenceId;
    }

    public Long getSequenceId()
    {
        return sequenceId;
    }

    public void setBizType(String bizType)
    {
        this.bizType = bizType;
    }

    public String getBizType()
    {
        return bizType;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setCurrentSeq(Long currentSeq)
    {
        this.currentSeq = currentSeq;
    }

    public Long getCurrentSeq()
    {
        return currentSeq;
    }

    public void setSeqLength(Integer seqLength)
    {
        this.seqLength = seqLength;
    }

    public Integer getSeqLength()
    {
        return seqLength;
    }

    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate()
    {
        return updateDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sequenceId", getSequenceId())
            .append("bizType", getBizType())
            .append("prefix", getPrefix())
            .append("currentSeq", getCurrentSeq())
            .append("seqLength", getSeqLength())
            .append("updateDate", getUpdateDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
