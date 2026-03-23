package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsInboundOrder;
import com.ruoyi.wms.domain.WmsInboundItem;

/**
 * 入库单 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsInboundMapper
{
    /**
     * 查询入库单信息
     *
     * @param inboundId 入库单 ID
     * @return 入库单信息
     */
    public WmsInboundOrder selectInboundOrderById(Long inboundId);

    /**
     * 查询入库单列表
     *
     * @param wmsInboundOrder 入库单信息
     * @return 入库单集合
     */
    public List<WmsInboundOrder> selectInboundOrderList(WmsInboundOrder wmsInboundOrder);

    /**
     * 根据入库单号查询
     *
     * @param inboundNo 入库单号
     * @return 入库单信息
     */
    public WmsInboundOrder checkInboundNoUnique(String inboundNo);

    /**
     * 新增入库单
     *
     * @param wmsInboundOrder 入库单信息
     * @return 结果
     */
    public int insertInboundOrder(WmsInboundOrder wmsInboundOrder);

    /**
     * 修改入库单
     *
     * @param wmsInboundOrder 入库单信息
     * @return 结果
     */
    public int updateInboundOrder(WmsInboundOrder wmsInboundOrder);

    /**
     * 删除入库单
     *
     * @param inboundId 入库单 ID
     * @return 结果
     */
    public int deleteInboundOrderById(Long inboundId);

    /**
     * 批量删除入库单
     *
     * @param inboundIds 需要删除的入库单 ID 数组
     * @return 结果
     */
    public int deleteInboundOrderByIds(Long[] inboundIds);

    /**
     * 查询入库明细列表
     *
     * @param inboundId 入库单 ID
     * @return 入库明细集合
     */
    public List<WmsInboundItem> selectWmsInboundItemByInboundId(Long inboundId);

    /**
     * 新增入库明细
     *
     * @param wmsInboundItem 入库明细信息
     * @return 结果
     */
    public int insertWmsInboundItem(WmsInboundItem wmsInboundItem);

    /**
     * 修改入库明细
     *
     * @param wmsInboundItem 入库明细信息
     * @return 结果
     */
    public int updateWmsInboundItem(WmsInboundItem wmsInboundItem);

    /**
     * 删除入库明细
     *
     * @param itemIds 入库明细 ID 数组
     * @return 结果
     */
    public int deleteWmsInboundItemByIds(Long[] itemIds);

    /**
     * 根据入库单 ID 删除入库明细
     *
     * @param inboundId 入库单 ID
     * @return 结果
     */
    public int deleteWmsInboundItemByInboundId(Long inboundId);

    /**
     * 根据明细 ID 查询入库明细
     *
     * @param itemId 明细 ID
     * @return 入库明细信息
     */
    public WmsInboundItem selectWmsInboundItemByItemId(Long itemId);
}
