package com.ruoyi.wms.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.wms.domain.WmsStockCheck;
import com.ruoyi.wms.mapper.WmsStockCheckMapper;
import com.ruoyi.wms.service.IWmsStockCheckService;
import com.ruoyi.wms.service.IWmsSequenceService;

/**
 * 盘点单 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsStockCheckServiceImpl implements IWmsStockCheckService
{
    @Autowired
    private WmsStockCheckMapper wmsStockCheckMapper;

    @Autowired
    private IWmsSequenceService sequenceService;

    /**
     * 查询盘点单信息
     *
     * @param checkId 盘点单 ID
     * @return 盘点单信息
     */
    @Override
    public WmsStockCheck selectWmsStockCheckById(Long checkId)
    {
        return wmsStockCheckMapper.selectStockCheckById(checkId);
    }

    /**
     * 查询盘点单列表
     *
     * @param wmsStockCheck 盘点单信息
     * @return 盘点单
     */
    @Override
    public List<WmsStockCheck> selectWmsStockCheckList(WmsStockCheck wmsStockCheck)
    {
        return wmsStockCheckMapper.selectStockCheckList(wmsStockCheck);
    }

    /**
     * 新增盘点单
     *
     * @param wmsStockCheck 盘点单信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertWmsStockCheck(WmsStockCheck wmsStockCheck)
    {
        // 自动生成盘点单号
        String checkNo = sequenceService.generateNumber("check");
        wmsStockCheck.setCheckNo(checkNo);

        return wmsStockCheckMapper.insertStockCheck(wmsStockCheck);
    }

    /**
     * 修改盘点单
     *
     * @param wmsStockCheck 盘点单信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateWmsStockCheck(WmsStockCheck wmsStockCheck)
    {
        return wmsStockCheckMapper.updateStockCheck(wmsStockCheck);
    }

    /**
     * 批量删除盘点单
     *
     * @param checkIds 需要删除的盘点单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsStockCheckByIds(Long[] checkIds)
    {
        return wmsStockCheckMapper.deleteStockCheckByIds(checkIds);
    }

    /**
     * 删除盘点单信息
     *
     * @param checkId 盘点单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWmsStockCheckById(Long checkId)
    {
        return wmsStockCheckMapper.deleteStockCheckById(checkId);
    }

    /**
     * 校验盘点单号是否唯一
     *
     * @param wmsStockCheck 盘点单信息
     * @return 结果
     */
    @Override
    public boolean checkCheckNoUnique(WmsStockCheck wmsStockCheck)
    {
        WmsStockCheck stockCheck = wmsStockCheckMapper.checkCheckNoUnique(wmsStockCheck.getCheckNo());
        return stockCheck == null;
    }

    /**
     * 开始盘点
     *
     * @param checkId 盘点单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int startCheck(Long checkId)
    {
        WmsStockCheck stockCheck = wmsStockCheckMapper.selectStockCheckById(checkId);
        if (stockCheck == null) {
            throw new RuntimeException("盘点单不存在");
        }

        stockCheck.setStatus("1");
        stockCheck.setStartTime(new Date());
        return wmsStockCheckMapper.updateStockCheck(stockCheck);
    }

    /**
     * 完成盘点
     *
     * @param checkId 盘点单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int finishCheck(Long checkId)
    {
        WmsStockCheck stockCheck = wmsStockCheckMapper.selectStockCheckById(checkId);
        if (stockCheck == null) {
            throw new RuntimeException("盘点单不存在");
        }

        stockCheck.setStatus("2");
        stockCheck.setEndTime(new Date());
        return wmsStockCheckMapper.updateStockCheck(stockCheck);
    }

    /**
     * 审核盘点单
     *
     * @param checkId 盘点单 ID
     * @return 结果
     */
    @Override
    @Transactional
    public int approveCheck(Long checkId)
    {
        WmsStockCheck stockCheck = wmsStockCheckMapper.selectStockCheckById(checkId);
        if (stockCheck == null) {
            throw new RuntimeException("盘点单不存在");
        }

        stockCheck.setStatus("3");
        return wmsStockCheckMapper.updateStockCheck(stockCheck);
    }
}
