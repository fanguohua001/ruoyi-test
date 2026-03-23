import request from '@/utils/request'

// 查询预警列表
export function listAlert(query) {
  return request({
    url: '/wms/alert/list',
    method: 'get',
    params: query
  })
}

// 查询预警详情
export function getAlert(alertId) {
  return request({
    url: '/wms/alert/' + alertId,
    method: 'get'
  })
}

// 处理预警
export function handleAlert(alertId, handleRemark) {
  return request({
    url: '/wms/alert/handle/' + alertId,
    method: 'put',
    data: handleRemark
  })
}

// 生成库存预警
export function generateAlert() {
  return request({
    url: '/wms/alert/generate',
    method: 'post'
  })
}

// 检查近效期商品
export function checkExpiry(days) {
  return request({
    url: '/wms/alert/checkExpiry/' + days,
    method: 'post'
  })
}

// 检查呆滞料
export function checkSlowMoving(days) {
  return request({
    url: '/wms/alert/checkSlowMoving/' + days,
    method: 'post'
  })
}

// 导出预警列表
export function exportAlert(query) {
  return request({
    url: '/wms/alert/export',
    method: 'post',
    params: query
  })
}
