package com.ruoyi.wms.mapper;

import java.util.List;
import com.ruoyi.wms.domain.WmsCustomerOrder;
import com.ruoyi.wms.domain.WmsCustomerOrderItem;

/**
 * 客户订单 Mapper 接口
 *
 * @author ruoyi
 */
public interface WmsCustomerOrderMapper
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
     * 根据订单号查询
     *
     * @param orderNo 订单编号
     * @return 客户订单信息
     */
    public WmsCustomerOrder checkOrderNoUnique(String orderNo);

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
     * 删除客户订单
     *
     * @param orderId 订单 ID
     * @return 结果
     */
    public int deleteCustomerOrderById(Long orderId);

    /**
     * 批量删除客户订单
     *
     * @param orderIds 需要删除的订单 ID 数组
     * @return 结果
     */
    public int deleteCustomerOrderByIds(Long[] orderIds);

    /**
     * 查询订单明细列表
     *
     * @param orderId 订单 ID
     * @return 订单明细集合
     */
    public List<WmsCustomerOrderItem> selectCustomerOrderItemByOrderId(Long orderId);

    /**
     * 新增订单明细
     *
     * @param wmsCustomerOrderItem 订单明细信息
     * @return 结果
     */
    public int insertCustomerOrderItem(WmsCustomerOrderItem wmsCustomerOrderItem);

    /**
     * 修改订单明细
     *
     * @param wmsCustomerOrderItem 订单明细信息
     * @return 结果
     */
    public int updateCustomerOrderItem(WmsCustomerOrderItem wmsCustomerOrderItem);

    /**
     * 删除订单明细
     *
     * @param itemIds 订单明细 ID 数组
     * @return 结果
     */
    public int deleteCustomerOrderItemByIds(Long[] itemIds);

    /**
     * 根据订单 ID 删除订单明细
     *
     * @param orderId 订单 ID
     * @return 结果
     */
    public int deleteCustomerOrderItemByOrderId(Long orderId);

    /**
     * 根据明细 ID 查询订单明细
     *
     * @param itemId 明细 ID
     * @return 订单明细信息
     */
    public WmsCustomerOrderItem selectCustomerOrderItemByItemId(Long itemId);
}
