import request from '@/utils/request'

// 查询客户列表
export function listCustomer(query) {
  return request({
    url: '/wms/customer/list',
    method: 'get',
    params: query
  })
}

// 查询所有有效的客户列表
export function allCustomer() {
  return request({
    url: '/wms/customer/all',
    method: 'get'
  })
}

// 查询客户详细
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
export function updateCustomer(data) {
  return request({
    url: '/wms/customer',
    method: 'put',
    data: data
  })
}

// 删除客户
export function delCustomer(customerId) {
  return request({
    url: '/wms/customer/' + customerId,
    method: 'delete'
  })
}

// 导出客户
export function exportCustomer(query) {
  return request({
    url: '/wms/customer/export',
    method: 'post',
    params: query
  })
}
