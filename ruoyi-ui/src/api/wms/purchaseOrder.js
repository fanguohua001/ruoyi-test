import request from '@/utils/request'

// 查询采购订单列表
export function listPurchaseOrder(query) {
  return request({
    url: '/wms/purchaseOrder/list',
    method: 'get',
    params: query
  })
}

// 查询采购订单详细
export function getPurchaseOrder(orderId) {
  return request({
    url: '/wms/purchaseOrder/' + orderId,
    method: 'get'
  })
}

// 新增采购订单
export function addPurchaseOrder(data) {
  return request({
    url: '/wms/purchaseOrder',
    method: 'post',
    data: data
  })
}

// 修改采购订单
export function updatePurchaseOrder(data) {
  return request({
    url: '/wms/purchaseOrder',
    method: 'put',
    data: data
  })
}

// 删除采购订单
export function delPurchaseOrder(orderId) {
  return request({
    url: '/wms/purchaseOrder/' + orderId,
    method: 'delete'
  })
}

// 完成采购订单
export function finishPurchaseOrder(orderId) {
  return request({
    url: '/wms/purchaseOrder/finish?orderId=' + orderId,
    method: 'put'
  })
}

// 导出采购订单
export function exportPurchaseOrder(query) {
  return request({
    url: '/wms/purchaseOrder/export',
    method: 'post',
    data: query
  })
}
