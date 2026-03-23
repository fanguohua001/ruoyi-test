import request from '@/utils/request'

// 查询入库单列表
export function listInbound(query) {
  return request({
    url: '/wms/inbound/list',
    method: 'get',
    params: query
  })
}

// 查询入库单详情
export function getInbound(inboundId) {
  return request({
    url: '/wms/inbound/' + inboundId,
    method: 'get'
  })
}

// 新增入库单
export function addInbound(data) {
  return request({
    url: '/wms/inbound',
    method: 'post',
    data: data
  })
}

// 修改入库单
export function editInbound(data) {
  return request({
    url: '/wms/inbound',
    method: 'put',
    data: data
  })
}

// 删除入库单
export function delInbound(inboundIds) {
  return request({
    url: '/wms/inbound/' + inboundIds,
    method: 'delete'
  })
}

// 收货操作
export function receive(inboundId, itemId, qty) {
  let url = '/wms/inbound/receive?inboundId=' + inboundId
  if (itemId) {
    url += '&itemId=' + itemId
  }
  return request({
    url: url,
    method: 'put',
    data: qty
  })
}

// 质检操作
export function qualityCheck(inboundId, itemId, qualifiedQty, unqualifiedQty) {
  return request({
    url: '/wms/inbound/quality?inboundId=' + inboundId +
         '&itemId=' + itemId +
         '&qualifiedQty=' + qualifiedQty +
         '&unqualifiedQty=' + unqualifiedQty,
    method: 'put'
  })
}

// 上架操作
export function putAway(inboundId, itemId, locationId) {
  return request({
    url: '/wms/inbound/putaway?inboundId=' + inboundId +
         '&itemId=' + itemId +
         '&locationId=' + locationId,
    method: 'put'
  })
}

// 完成入库单
export function finishInbound(inboundId) {
  return request({
    url: '/wms/inbound/finish?inboundId=' + inboundId,
    method: 'put'
  })
}

// 检查入库单是否已完成
export function checkInboundFinished(inboundId) {
  return request({
    url: '/wms/inbound/checkFinished/' + inboundId,
    method: 'get'
  })
}
