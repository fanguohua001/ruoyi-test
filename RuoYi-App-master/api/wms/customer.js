import request from '@/utils/request'

// 查询客户列表
export function listCustomer(query) {
  return request({
    url: '/wms/customer/list',
    method: 'get',
    params: query
  })
}

// 查询所有客户列表（下拉选择）
export function allCustomer() {
  return request({
    url: '/wms/customer/all',
    method: 'get'
  })
}

// 查询客户详情
export function getCustomer(customerId) {
  return request({
    url: '/wms/customer/' + customerId,
    method: 'get'
  })
}

// 新增客户
export function addCustomer(data) {
  return request({
    url: '/wms/customer',
    method: 'post',
    data: data
  })
}

// 修改客户
export function editCustomer(data) {
  return request({
    url: '/wms/customer',
    method: 'put',
    data: data
  })
}

// 删除客户
export function delCustomer(customerIds) {
  return request({
    url: '/wms/customer/' + customerIds,
    method: 'delete'
  })
}

// 导出客户列表
export function exportCustomer(query) {
  return request({
    url: '/wms/customer/export',
    method: 'post',
    params: query
  })
}
