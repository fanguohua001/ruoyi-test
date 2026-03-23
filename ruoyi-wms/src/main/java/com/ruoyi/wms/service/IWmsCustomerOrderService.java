package com.ruoyi.wms.service;

import java.util.List;
import com.ruoyi.wms.domain.WmsCustomerOrder;
import com.ruoyi.wms.domain.WmsCustomerOrderItem;

/**
 * 客户订单 Service 接口
 *
 * @author ruoyi
 */
public interface IWmsCustomerOrderService
{
    /**
     * 查询客户订单信息
     *
     * @param orderId 订单 ID
     * @return 客户订单信息
     */
    public WmsCustomerOrder selectCustomerOrderById(Long orderId);

    /**
     * 查询客户订单列表
     *
     * @param wmsCustomerOrder 客户订单信息
     * @return 客户订单集合
     */
    public List<WmsCustomerOrder> selectCustomerOrderList(WmsCustomerOrder wmsCustomerOrder);

    /**
     * 新增客户订单
     *
     * @param wmsCustomerOrder 客户订单信息
     * @return 结果
     */
    public int insertCustomerOrder(WmsCustomerOrder wmsCustomerOrder);

    /**
     * 修改客户订单
     *
     * @param wmsCustomerOrder 客户订单信息
     * @return 结果
     */
    public int updateCustomerOrder(WmsCustomerOrder wmsCustomerOrder);

    /**
     * 批量删除客户订单
     *
     * @param orderIds 需要删除的订单 ID 数组
     * @return 结果
     */
    public int deleteCustomerOrderByIds(Long[] orderIds);

    /**
     * 删除客户订单信息
     *
     * @param orderId 订单 ID
     * @return 结果
     */
    public int deleteCustomerOrderById(Long orderId);

    /**
     * 校验订单编号是否唯一
     *
     * @param wmsCustomerOrder 客户订单信息
     * @return 结果
     */
    public boolean checkOrderNoUnique(WmsCustomerOrder wmsCustomerOrder);

    /**
     * 完成客户订单
     *
     * @param orderId 订单 ID
     * @return 结果
     */
    public int finishCustomerOrder(Long orderId);

    /**
     * 查询已完成的客户订单列表（用于出库单关联）
     *
     * @param wmsCustomerOrder 客户订单信息
     * @return 客户订单集合
     */
    public List<WmsCustomerOrder> listFinishedOrders(WmsCustomerOrder wmsCustomerOrder);
}
