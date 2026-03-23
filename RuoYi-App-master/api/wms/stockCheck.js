import request from '@/utils/request'

// 查询盘点单列表
export function listStockCheck(query) {
  return request({
    url: '/wms/stockCheck/list',
    method: 'get',
    params: query
  })
}

// 查询盘点单详情
export function getStockCheck(checkId) {
  return request({
    url: '/wms/stockCheck/' + checkId,
    method: 'get'
  })
}

// 新增盘点单
export function addStockCheck(data) {
  return request({
    url: '/wms/stockCheck',
    method: 'post',
    data: data
  })
}

// 修改盘点单
export function editStockCheck(data) {
  return request({
    url: '/wms/stockCheck',
    method: 'put',
    data: data
  })
}

// 删除盘点单
export function delStockCheck(checkIds) {
  return request({
    url: '/wms/stockCheck/' + checkIds,
    method: 'delete'
  })
}

// 开始盘点
export function startCheck(checkId) {
  return request({
    url: '/wms/stockCheck/start/' + checkId,
    method: 'put'
  })
}

// 完成盘点
export function finishCheck(checkId) {
  return request({
    url: '/wms/stockCheck/finish/' + checkId,
    method: 'put'
  })
}

// 审核盘点单
export function approveCheck(checkId) {
  return request({
    url: '/wms/stockCheck/approve/' + checkId,
    method: 'put'
  })
}

// 导出盘点单列表
export function exportStockCheck(query) {
  return request({
    url: '/wms/stockCheck/export',
    method: 'post',
    params: query
  })
}
