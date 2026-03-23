package com.ruoyi.wms.mapper;

import org.apache.ibatis.annotations.Param;
import com.ruoyi.wms.domain.WmsSequence;

/**
 * 序号管理 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsSequenceMapper
{
    /**
     * 根据业务类型查询序号信息（带锁）
     *
     * @param bizType 业务类型
     * @return 序号信息
     */
    public WmsSequence selectSequenceByBizType(String bizType);

    /**
     * 新增序号管理
     *
     * @param wmsSequence 序号管理信息
     * @return 结果
     */
    public int insertSequence(WmsSequence wmsSequence);

    /**
     * 更新序号当前值
     *
     * @param sequenceId 序号 ID
     * @param currentSeq 当前序号值
     * @return 结果
     */
    public int updateSequenceCurrentSeq(@Param("sequenceId") Long sequenceId, @Param("currentSeq") Long currentSeq);
}
