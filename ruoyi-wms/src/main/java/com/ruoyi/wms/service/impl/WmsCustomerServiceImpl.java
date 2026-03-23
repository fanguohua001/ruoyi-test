package com.ruoyi.wms.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.wms.domain.WmsCustomer;
import com.ruoyi.wms.mapper.WmsCustomerMapper;
import com.ruoyi.wms.service.IWmsCustomerService;

/**
 * 客户 Service 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WmsCustomerServiceImpl implements IWmsCustomerService
{
    @Autowired
    private WmsCustomerMapper wmsCustomerMapper;

    /**
     * 查询客户信息
     *
     * @param customerId 客户 ID
     * @return 客户信息
     */
    @Override
    public WmsCustomer selectWmsCustomerById(Long customerId)
    {
        return wmsCustomerMapper.selectWmsCustomerById(customerId);
    }

    /**
     * 查询客户列表
     *
     * @param wmsCustomer 客户信息
     * @return 客户信息
     */
    @Override
    public List<WmsCustomer> selectWmsCustomerList(WmsCustomer wmsCustomer)
    {
        return wmsCustomerMapper.selectWmsCustomerList(wmsCustomer);
    }

    /**
     * 查询所有有效的客户列表（用于下拉选择）
     *
     * @return 客户信息
     */
    @Override
    public List<WmsCustomer> selectWmsCustomerAll()
    {
        return wmsCustomerMapper.selectWmsCustomerAll();
    }

    /**
     * 新增客户
     *
     * @param wmsCustomer 客户信息
     * @return 结果
     */
    @Override
    public int insertWmsCustomer(WmsCustomer wmsCustomer)
    {
        wmsCustomer.setCreateTime(DateUtils.getNowDate());
        return wmsCustomerMapper.insertWmsCustomer(wmsCustomer);
    }

    /**
     * 修改客户
     *
     * @param wmsCustomer 客户信息
     * @return 结果
     */
    @Override
    public int updateWmsCustomer(WmsCustomer wmsCustomer)
    {
        wmsCustomer.setUpdateTime(DateUtils.getNowDate());
        return wmsCustomerMapper.updateWmsCustomer(wmsCustomer);
    }

    /**
     * 批量删除客户
     *
     * @param customerIds 需要删除的客户 ID
     * @return 结果
     */
    @Override
    public int deleteWmsCustomerByIds(Long[] customerIds)
    {
        return wmsCustomerMapper.deleteWmsCustomerByIds(customerIds);
    }

    /**
     * 删除客户信息
     *
     * @param customerId 客户 ID
     * @return 结果
     */
    @Override
    public int deleteWmsCustomerById(Long customerId)
    {
        return wmsCustomerMapper.deleteWmsCustomerById(customerId);
    }

    /**
     * 校验客户编码是否唯一
     *
     * @param wmsCustomer 客户信息
     * @return 结果
     */
    @Override
    public boolean checkCustomerCodeUnique(WmsCustomer wmsCustomer)
    {
        Long customerId = wmsCustomer.getCustomerId() == null ? -1L : wmsCustomer.getCustomerId();
        WmsCustomer info = wmsCustomerMapper.checkCustomerCodeUnique(wmsCustomer.getCustomerCode());
        if (info != null && info.getCustomerId().longValue() != customerId.longValue())
        {
            return false;
        }
        return true;
    }
}
