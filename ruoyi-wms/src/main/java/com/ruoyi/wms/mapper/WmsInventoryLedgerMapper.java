package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsInventoryLedger;

/**
 * 库存台账 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsInventoryLedgerMapper
{
    /**
     * 查询库存台账
     *
     * @param ledgerId 库存台账 ID
     * @return 库存台账
     */
    public WmsInventoryLedger selectWmsInventoryLedgerById(Long ledgerId);

    /**
     * 查询库存台账列表
     *
     * @param wmsInventoryLedger 库存台账
     * @return 库存台账集合
     */
    public List<WmsInventoryLedger> selectWmsInventoryLedgerList(WmsInventoryLedger wmsInventoryLedger);

    /**
     * 新增库存台账
     *
     * @param wmsInventoryLedger 库存台账
     * @return 结果
     */
    public int insertWmsInventoryLedger(WmsInventoryLedger wmsInventoryLedger);

    /**
     * 修改库存台账
     *
     * @param wmsInventoryLedger 库存台账
     * @return 结果
     */
    public int updateWmsInventoryLedger(WmsInventoryLedger wmsInventoryLedger);

    /**
     * 删除库存台账
     *
     * @param ledgerId 库存台账 ID
     * @return 结果
     */
    public int deleteWmsInventoryLedgerById(Long ledgerId);

    /**
     * 批量删除库存台账
     *
     * @param ledgerIds 需要删除的数据 ID
     * @return 结果
     */
    public int deleteWmsInventoryLedgerByIds(Long[] ledgerIds);
}
