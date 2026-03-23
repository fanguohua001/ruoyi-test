package com.ruoyi.wms.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.wms.domain.WmsInventoryLedger;

/**
 * 库存台账 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsInventoryLedgerService
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
     * 批量删除库存台账
     *
     * @param ledgerIds 需要删除的库存台账 ID 集合
     * @return 结果
     */
    public int deleteWmsInventoryLedgerByIds(Long[] ledgerIds);

    /**
     * 删除库存台账信息
     *
     * @param ledgerId 库存台账 ID
     * @return 结果
     */
    public int deleteWmsInventoryLedgerById(Long ledgerId);

    /**
     * 记录库存变动
     *
     * @param transactionType 交易类型（1 入库 2 出库 3 盘点调整 4 移库 5 冻结/解冻）
     * @param warehouseId 仓库 ID
     * @param locationId 库位 ID
     * @param productId 物料 ID
     * @param productCode 物料编码
     * @param productName 物料名称
     * @param batchNo 批次号
     * @param qtyChange 变动数量（正数入库，负数出库）
     * @param unitCost 单位成本
     * @param referenceType 关联单据类型
     * @param referenceId 关联单据 ID
     * @param referenceNo 关联单据号
     * @param remark 备注
     * @return 结果
     */
    public int recordTransaction(String transactionType, Long warehouseId, Long locationId,
                                  Long productId, String productCode, String productName,
                                  String batchNo, BigDecimal qtyChange, BigDecimal unitCost,
                                  String referenceType, Long referenceId, String referenceNo, String remark);
}
