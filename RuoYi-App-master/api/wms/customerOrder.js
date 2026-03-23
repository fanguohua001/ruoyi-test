import request from '@/utils/request'

// 查询客户订单列表
export function listCustomerOrder(query) {
  return request({
    url: '/wms/customerOrder/list',
    method: 'get',
    params: query
  })
}

// 查询客户订单详情
export function getCustomerOrder(orderId) {
  return request({
    url: '/wms/customerOrder/' + orderId,
    method: 'get'
  })
}

// 新增客户订单
export function addCustomerOrder(data) {
  return request({
    url: '/wms/customerOrder',
    method: 'post',
    data: data
  })
}

// 修改客户订单
export function editCustomerOrder(data) {
  return request({
    url: '/wms/customerOrder',
    method: 'put',
    data: data
  })
}

// 删除客户订单
export function delCustomerOrder(orderIds) {
  return request({
    url: '/wms/customerOrder/' + orderIds,
    method: 'delete'
  })
}

// 完成客户订单
export function finishCustomerOrder(orderId) {
  return request({
    url: '/wms/customerOrder/finish?orderId=' + orderId,
    method: 'put'
  })
}

// 查询已完成的客户订单列表
export function listFinishedOrders(query) {
  return request({
    url: '/wms/customerOrder/finished/list',
    method: 'get',
    params: query
  })
}

// 导出客户订单列表
export function exportCustomerOrder(query) {
  return request({
    url: '/wms/customerOrder/export',
    method: 'post',
    params: query
  })
}
