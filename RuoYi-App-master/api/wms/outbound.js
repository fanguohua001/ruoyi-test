import request from '@/utils/request'

// 查询出库单列表
export function listOutbound(query) {
  return request({
    url: '/wms/outbound/list',
    method: 'get',
    params: query
  })
}

// 查询出库单详情
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
export function editOutbound(data) {
  return request({
    url: '/wms/outbound',
    method: 'put',
    data: data
  })
}

// 删除出库单
export function delOutbound(outboundIds) {
  return request({
    url: '/wms/outbound/' + outboundIds,
    method: 'delete'
  })
}

// 拣货操作
export function picking(outboundId, itemId, qty) {
  return request({
    url: '/wms/outbound/picking?outboundId=' + outboundId +
         '&itemId=' + itemId,
    method: 'put',
    data: qty
  })
}

// 复核操作
export function reviewOutbound(outboundId) {
  return request({
    url: '/wms/outbound/review/' + outboundId,
    method: 'put'
  })
}

// 打包操作
export function packOutbound(outboundId) {
  return request({
    url: '/wms/outbound/pack/' + outboundId,
    method: 'put'
  })
}

// 发货操作
export function shipOutbound(outboundId) {
  return request({
    url: '/wms/outbound/ship/' + outboundId,
    method: 'put'
  })
}

// 完成出库单
export function finishOutbound(outboundId) {
  return request({
    url: '/wms/outbound/finish?outboundId=' + outboundId,
    method: 'put'
  })
}

// 检查出库单是否已完成
export function checkOutboundFinished(outboundId) {
  return request({
    url: '/wms/outbound/checkFinished/' + outboundId,
    method: 'get'
  })
}
