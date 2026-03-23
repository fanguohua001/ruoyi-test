package com.ruoyi.wms.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.wms.domain.WmsInventoryLedger;
import com.ruoyi.wms.mapper.WmsInventoryLedgerMapper;
import com.ruoyi.wms.mapper.WmsInventoryMapper;
import com.ruoyi.wms.domain.WmsInventory;
import com.ruoyi.wms.service.IWmsInventoryLedgerService;
import com.ruoyi.wms.service.IWmsSequenceService;
import com.ruoyi.common.utils.StringUtils;

/**
 * 库存台账 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsInventoryLedgerServiceImpl implements IWmsInventoryLedgerService
{
    @Autowired
    private WmsInventoryLedgerMapper wmsInventoryLedgerMapper;

    @Autowired
    private WmsInventoryMapper wmsInventoryMapper;

    @Autowired
    private IWmsSequenceService sequenceService;

    /**
     * 查询库存台账
     *
     * @param ledgerId 库存台账 ID
     * @return 库存台账
     */
    @Override
    public WmsInventoryLedger selectWmsInventoryLedgerById(Long ledgerId)
    {
        return wmsInventoryLedgerMapper.selectWmsInventoryLedgerById(ledgerId);
    }

    /**
     * 查询库存台账列表
     *
     * @param wmsInventoryLedger 库存台账
     * @return 库存台账
     */
    @Override
    public List<WmsInventoryLedger> selectWmsInventoryLedgerList(WmsInventoryLedger wmsInventoryLedger)
    {
        return wmsInventoryLedgerMapper.selectWmsInventoryLedgerList(wmsInventoryLedger);
    }

    /**
     * 新增库存台账
     *
     * @param wmsInventoryLedger 库存台账
     * @return 结果
     */
    @Override
    public int insertWmsInventoryLedger(WmsInventoryLedger wmsInventoryLedger)
    {
        return wmsInventoryLedgerMapper.insertWmsInventoryLedger(wmsInventoryLedger);
    }

    /**
     * 修改库存台账
     *
     * @param wmsInventoryLedger 库存台账
     * @return 结果
     */
    @Override
    public int updateWmsInventoryLedger(WmsInventoryLedger wmsInventoryLedger)
    {
        return wmsInventoryLedgerMapper.updateWmsInventoryLedger(wmsInventoryLedger);
    }

    /**
     * 批量删除库存台账
     *
     * @param ledgerIds 需要删除的库存台账 ID
     * @return 结果
     */
    @Override
    public int deleteWmsInventoryLedgerByIds(Long[] ledgerIds)
    {
        return wmsInventoryLedgerMapper.deleteWmsInventoryLedgerByIds(ledgerIds);
    }

    /**
     * 删除库存台账信息
     *
     * @param ledgerId 库存台账 ID
     * @return 结果
     */
    @Override
    public int deleteWmsInventoryLedgerById(Long ledgerId)
    {
        return wmsInventoryLedgerMapper.deleteWmsInventoryLedgerById(ledgerId);
    }

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
    @Override
    public int recordTransaction(String transactionType, Long warehouseId, Long locationId,
                                  Long productId, String productCode, String productName,
                                  String batchNo, BigDecimal qtyChange, BigDecimal unitCost,
                                  String referenceType, Long referenceId, String referenceNo, String remark)
    {
        // 生成交易编号
        String transactionNo = sequenceService.generateNumber("transaction");

        // 查询当前库存
        WmsInventory inventory = wmsInventoryMapper.selectInventoryByParams(productId, warehouseId, locationId, batchNo);

        BigDecimal qtyBefore = BigDecimal.ZERO;
        BigDecimal qtyAfter;

        if (inventory != null) {
            qtyBefore = inventory.getQtyOnHand() != null ? inventory.getQtyOnHand() : BigDecimal.ZERO;
        }

        // 计算变动后数量
        qtyAfter = qtyBefore.add(qtyChange);

        // 创建台账记录
        WmsInventoryLedger ledger = new WmsInventoryLedger();
        ledger.setTransactionNo(transactionNo);
        ledger.setTransactionType(transactionType);
        ledger.setWarehouseId(warehouseId);
        ledger.setLocationId(locationId);
        ledger.setProductId(productId);
        ledger.setProductCode(productCode);
        ledger.setProductName(productName);
        ledger.setBatchNo(batchNo);
        ledger.setQtyBefore(qtyBefore);
        ledger.setQtyChange(qtyChange);
        ledger.setQtyAfter(qtyAfter);
        ledger.setUnitCost(unitCost != null ? unitCost : BigDecimal.ZERO);
        ledger.setReferenceType(referenceType);
        ledger.setReferenceId(referenceId);
        ledger.setReferenceNo(referenceNo);
        ledger.setRemark(remark);

        return wmsInventoryLedgerMapper.insertWmsInventoryLedger(ledger);
    }
}
