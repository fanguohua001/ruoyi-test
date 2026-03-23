package com.ruoyi.wms.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.wms.domain.WmsInventory;
import com.ruoyi.wms.domain.WmsTransfer;
import com.ruoyi.wms.mapper.WmsInventoryMapper;
import com.ruoyi.wms.mapper.WmsTransferMapper;
import com.ruoyi.wms.service.IWmsTransferService;
import com.ruoyi.wms.service.IWmsSequenceService;

/**
 * 移库单 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsTransferServiceImpl implements IWmsTransferService
{
    @Autowired
    private WmsTransferMapper wmsTransferMapper;

    @Autowired
    private WmsInventoryMapper wmsInventoryMapper;

    @Autowired
    private IWmsSequenceService sequenceService;

    /**
     * 查询移库单信息
     *
     * @param transferId 移库单 ID
     * @return 移库单信息
     */
    @Override
    public WmsTransfer selectWmsTransferById(Long transferId)
    {
        return wmsTransferMapper.selectTransferById(transferId);
    }

    /**
     * 查询移库单列表
     *
     * @param wmsTransfer 移库单信息
     * @return 移库单
     */
    @Override
    public List<WmsTransfer> selectWmsTransferList(WmsTransfer wmsTransfer)
    {
        return wmsTransferMapper.selectTransferList(wmsTransfer);
    }

    /**
     * 新增移库单
     *
     * @param wmsTransfer 移库单信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertWmsTransfer(WmsTransfer wmsTransfer)
    {
        // 自动生成移库单号
        String transferNo = sequenceService.generateNumber("transfer");
        wmsTransfer.setTransferNo(transferNo);

        return wmsTransferMapper.insertTransfer(wmsTransfer);
    }

    /**
     * 修改移库单
     *
     * @param wmsTransfer 移库单信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateWmsTransfer(WmsTransfer wmsTransfer)
    {
        return wmsTransferMapper.updateTransfer(wmsTransfer);
    }

    /**
     * 批量删除移库单
     *
     * @param transferIds 需要删除的移库单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsTransferByIds(Long[] transferIds)
    {
        return wmsTransferMapper.deleteTransferByIds(transferIds);
    }

    /**
     * 删除移库单信息
     *
     * @param transferId 移库单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsTransferById(Long transferId)
    {
        return wmsTransferMapper.deleteTransferById(transferId);
    }

    /**
     * 校验移库单号是否唯一
     *
     * @param wmsTransfer 移库单信息
     * @return 结果
     */
    @Override
    public boolean checkTransferNoUnique(WmsTransfer wmsTransfer)
    {
        WmsTransfer transfer = wmsTransferMapper.checkTransferNoUnique(wmsTransfer.getTransferNo());
        return transfer == null;
    }

    /**
     * 执行移库
     *
     * @param transferId 移库单 ID
     * @param productId 物料 ID
     * @param qty 移库数量
     * @return 结果
     */
    @Override
    @Transactional
    public int executeTransfer(Long transferId, Long productId, BigDecimal qty)
    {
        WmsTransfer transfer = wmsTransferMapper.selectTransferById(transferId);
        if (transfer == null) {
            throw new RuntimeException("移库单不存在");
        }

        // 使用传入的参数，如果没有则使用移库单中的参数
        Long pid = productId != null ? productId : transfer.getProductId();
        BigDecimal q = qty != null ? qty : transfer.getQty();

        if (pid == null || q == null) {
            throw new RuntimeException("物料 ID 和移库数量不能为空");
        }

        // 查询源库位库存
        WmsInventory fromInventory = wmsInventoryMapper.selectInventoryByParams(
            pid,
            transfer.getWarehouseId(),
            transfer.getFromLocationId(),
            null
        );

        if (fromInventory == null || fromInventory.getQtyAvailable().compareTo(q) < 0) {
            throw new RuntimeException("源库位库存不足");
        }

        // 扣减源库位库存
        fromInventory.setQtyOnHand(fromInventory.getQtyOnHand().subtract(q));
        fromInventory.setQtyAvailable(fromInventory.getQtyAvailable().subtract(q));
        wmsInventoryMapper.updateInventory(fromInventory);

        // 增加目标库位库存
        WmsInventory toInventory = wmsInventoryMapper.selectInventoryByParams(
            pid,
            transfer.getWarehouseId(),
            transfer.getToLocationId(),
            null
        );

        if (toInventory == null) {
            toInventory = new WmsInventory();
            toInventory.setProductId(fromInventory.getProductId());
            toInventory.setProductCode(fromInventory.getProductCode());
            toInventory.setProductName(fromInventory.getProductName());
            toInventory.setWarehouseId(transfer.getWarehouseId());
            toInventory.setLocationId(transfer.getToLocationId());
            toInventory.setQtyOnHand(q);
            toInventory.setQtyAvailable(q);
            toInventory.setQtyLocked(BigDecimal.ZERO);
            toInventory.setQtyDamaged(BigDecimal.ZERO);
            toInventory.setStatus("1");
            toInventory.setCreateTime(new Date());
            wmsInventoryMapper.insertInventory(toInventory);
        } else {
            toInventory.setQtyOnHand(toInventory.getQtyOnHand().add(q));
            toInventory.setQtyAvailable(toInventory.getQtyAvailable().add(q));
            wmsInventoryMapper.updateInventory(toInventory);
        }

        // 更新移库单状态
        transfer.setStatus("2");
        return wmsTransferMapper.updateTransfer(transfer);
    }
}
