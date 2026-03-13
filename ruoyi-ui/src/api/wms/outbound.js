import request from '@/utils/request'

// 查询出库单列表
export function listOutbound(query) {
  return request({
    url: '/wms/outbound/list',
    method: 'get',
    params: query
  })
}

// 查询出库单详细
export function getOutbound(outboundId) {
  return request({
    url: '/wms/outbound/' + outboundId,
    method: 'get'
  })
}

// 新增出库单
export function addOutbound(data) {
  return request({
    url: '/wms/outbound',
    method: 'post',
    data: data
  })
}

// 修改出库单
export function updateOutbound(data) {
  return request({
    url: '/wms/outbound',
    method: 'put',
    data: data
  })
}

// 删除出库单
export function delOutbound(outboundId) {
  return request({
    url: '/wms/outbound/' + outboundId,
    method: 'delete'
  })
}

// 拣货
export function picking(outboundId, itemId, qty) {
  return request({
    url: '/wms/outbound/picking/' + outboundId + '/' + itemId,
    method: 'put',
    data: qty
  })
}

// 复核
export function review(outboundId) {
  return request({
    url: '/wms/outbound/review/' + outboundId,
    method: 'put'
  })
}

// 打包
export function pack(outboundId) {
  return request({
    url: '/wms/outbound/pack/' + outboundId,
    method: 'put'
  })
}

// 发货
export function ship(outboundId) {
  return request({
    url: '/wms/outbound/ship/' + outboundId,
    method: 'put'
  })
}

// 导出出库单
export function exportOutbound(query) {
  return request({
    url: '/wms/outbound/export',
    method: 'post',
    data: query
  })
}
