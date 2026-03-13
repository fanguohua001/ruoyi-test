import request from '@/utils/request'

// 查询入库单列表
export function listInbound(query) {
  return request({
    url: '/wms/inbound/list',
    method: 'get',
    params: query
  })
}

// 查询入库单详细
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
export function updateInbound(data) {
  return request({
    url: '/wms/inbound',
    method: 'put',
    data: data
  })
}

// 删除入库单
export function delInbound(inboundId) {
  return request({
    url: '/wms/inbound/' + inboundId,
    method: 'delete'
  })
}

// 收货
export function receive(inboundId, qty) {
  return request({
    url: '/wms/inbound/receive?inboundId=' + inboundId,
    method: 'put',
    data: qty
  })
}

// 质检
export function qualityCheck(inboundId, itemId, qualifiedQty, unqualifiedQty) {
  return request({
    url: '/wms/inbound/quality?inboundId=' + inboundId + '&itemId=' + itemId,
    method: 'put',
    data: [qualifiedQty, unqualifiedQty]
  })
}

// 上架
export function putAway(inboundId, itemId, locationId) {
  return request({
    url: '/wms/inbound/putaway?inboundId=' + inboundId + '&itemId=' + itemId + '&locationId=' + locationId,
    method: 'put'
  })
}

// 导出入库单
export function exportInbound(query) {
  return request({
    url: '/wms/inbound/export',
    method: 'post',
    data: query
  })
}
