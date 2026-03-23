import request from '@/utils/request'

// 查询库区列表
export function listZone(query) {
  return request({
    url: '/wms/zone/list',
    method: 'get',
    params: query
  })
}

// 查询所有有效的库区列表
export function allZone() {
  return request({
    url: '/wms/zone/all',
    method: 'get'
  })
}

// 根据仓库 ID 查询库区列表
export function listZoneByWarehouse(warehouseId) {
  return request({
    url: '/wms/zone/byWarehouse/' + warehouseId,
    method: 'get'
  })
}

// 查询库区详细
export function getZone(zoneId) {
  return request({
    url: '/wms/zone/' + zoneId,
    method: 'get'
  })
}

// 新增库区
export function addZone(data) {
  return request({
    url: '/wms/zone',
    method: 'post',
    data: data
  })
}

// 修改库区
export function updateZone(data) {
  return request({
    url: '/wms/zone',
    method: 'put',
    data: data
  })
}

// 删除库区
export function delZone(zoneId) {
  return request({
    url: '/wms/zone/' + zoneId,
    method: 'delete'
  })
}

// 导出库区
export function exportZone(query) {
  return request({
    url: '/wms/zone/export',
    method: 'post',
    params: query
  })
}
