import request from '@/utils/request'

// 查询供应商列表
export function listSupplier(query) {
  return request({
    url: '/wms/supplier/list',
    method: 'get',
    params: query
  })
}

// 查询所有供应商列表（下拉选择）
export function allSupplier() {
  return request({
    url: '/wms/supplier/all',
    method: 'get'
  })
}

// 查询供应商详情
export function getSupplier(supplierId) {
  return request({
    url: '/wms/supplier/' + supplierId,
    method: 'get'
  })
}

// 新增供应商
export function addSupplier(data) {
  return request({
    url: '/wms/supplier',
    method: 'post',
    data: data
  })
}

// 修改供应商
export function editSupplier(data) {
  return request({
    url: '/wms/supplier',
    method: 'put',
    data: data
  })
}

// 删除供应商
export function delSupplier(supplierIds) {
  return request({
    url: '/wms/supplier/' + supplierIds,
    method: 'delete'
  })
}

// 导出供应商列表
export function exportSupplier(query) {
  return request({
    url: '/wms/supplier/export',
    method: 'post',
    params: query
  })
}
