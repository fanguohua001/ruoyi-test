package com.ruoyi.wms.service;

import java.util.List;
import com.ruoyi.wms.domain.WmsCustomer;

/**
 * 客户 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsCustomerService
{
    /**
     * 查询客户信息
     *
     * @param customerId 客户 ID
     * @return 客户信息
     */
    public WmsCustomer selectWmsCustomerById(Long customerId);

    /**
     * 查询客户列表
     *
     * @param wmsCustomer 客户信息
     * @return 客户集合
     */
    public List<WmsCustomer> selectWmsCustomerList(WmsCustomer wmsCustomer);

    /**
     * 查询所有有效的客户列表（用于下拉选择）
     *
     * @return 客户集合
     */
    public List<WmsCustomer> selectWmsCustomerAll();

    /**
     * 新增客户
     *
     * @param wmsCustomer 客户信息
     * @return 结果
     */
    public int insertWmsCustomer(WmsCustomer wmsCustomer);

    /**
     * 修改客户
     *
     * @param wmsCustomer 客户信息
     * @return 结果
     */
    public int updateWmsCustomer(WmsCustomer wmsCustomer);

    /**
     * 批量删除客户
     *
     * @param customerIds 需要删除的客户 ID 数组
     * @return 结果
     */
    public int deleteWmsCustomerByIds(Long[] customerIds);

    /**
     * 删除客户信息
     *
     * @param customerId 客户 ID
     * @return 结果
     */
    public int deleteWmsCustomerById(Long customerId);

    /**
     * 校验客户编码是否唯一
     *
     * @param wmsCustomer 客户信息
     * @return 结果
     */
    public boolean checkCustomerCodeUnique(WmsCustomer wmsCustomer);
}
