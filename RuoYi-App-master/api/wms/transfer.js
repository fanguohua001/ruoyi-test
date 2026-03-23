import request from '@/utils/request'

// 查询移库单列表
export function listTransfer(query) {
  return request({
    url: '/wms/transfer/list',
    method: 'get',
    params: query
  })
}

// 查询移库单详情
export function getTransfer(transferId) {
  return request({
    url: '/wms/transfer/' + transferId,
    method: 'get'
  })
}

// 新增移库单
export function addTransfer(data) {
  return request({
    url: '/wms/transfer',
    method: 'post',
    data: data
  })
}

// 修改移库单
export function editTransfer(data) {
  return request({
    url: '/wms/transfer',
    method: 'put',
    data: data
  })
}

// 删除移库单
export function delTransfer(transferIds) {
  return request({
    url: '/wms/transfer/' + transferIds,
    method: 'delete'
  })
}

// 执行移库
export function executeTransfer(transferId, productId, qty) {
  return request({
    url: '/wms/transfer/execute?transferId=' + transferId +
         '&productId=' + productId,
    method: 'post',
    data: qty
  })
}

// 导出移库单列表
export function exportTransfer(query) {
  return request({
    url: '/wms/transfer/export',
    method: 'post',
    params: query
  })
}
