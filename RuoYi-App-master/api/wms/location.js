import request from '@/utils/request'

// 查询库位列表
export function listLocation(query) {
  return request({
    url: '/wms/location/list',
    method: 'get',
    params: query
  })
}

// 查询库位详情
export function getLocation(locationId) {
  return request({
    url: '/wms/location/' + locationId,
    method: 'get'
  })
}

// 查询空闲库位
export function getAvailableLocations(warehouseId, zoneId) {
  return request({
    url: '/wms/location/available?warehouseId=' + warehouseId +
         '&zoneId=' + zoneId,
    method: 'get'
  })
}

// 库位锁定
export function lockLocation(locationId) {
  return request({
    url: '/wms/location/lock/' + locationId,
    method: 'put'
  })
}

// 库位解锁
export function unlockLocation(locationId) {
  return request({
    url: '/wms/location/unlock/' + locationId,
    method: 'put'
  })
}

// 导出库位列表
export function exportLocation(query) {
  return request({
    url: '/wms/location/export',
    method: 'post',
    params: query
  })
}
