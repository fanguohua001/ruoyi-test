import request from '@/utils/request'

// 查询客户订单列表
export function listCustomerOrder(query) {
  return request({
    url: '/wms/customerOrder/list',
    method: 'get',
    params: query
  })
}

// 查询客户订单详细
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
export function updateCustomerOrder(data) {
  return request({
    url: '/wms/customerOrder',
    method: 'put',
    data: data
  })
}

// 删除客户订单
export function delCustomerOrder(orderId) {
  return request({
    url: '/wms/customerOrder/' + orderId,
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

// 导出客户订单
export function exportCustomerOrder(query) {
  return request({
    url: '/wms/customerOrder/export',
    method: 'post',
    data: query
  })
}

// 查询已完成客户订单列表（用于出库单关联）
export function listFinishedCustomerOrder(query) {
  return request({
    url: '/wms/customerOrder/finished/list',
    method: 'get',
    params: query
  })
}
