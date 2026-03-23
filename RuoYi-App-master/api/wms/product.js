import request from '@/utils/request'

// 查询物料列表
export function listProduct(query) {
  return request({
    url: '/wms/product/list',
    method: 'get',
    params: query
  })
}

// 查询物料详情
export function getProduct(productId) {
  return request({
    url: '/wms/product/' + productId,
    method: 'get'
  })
}

// 新增物料
export function addProduct(data) {
  return request({
    url: '/wms/product',
    method: 'post',
    data: data
  })
}

// 修改物料
export function editProduct(data) {
  return request({
    url: '/wms/product',
    method: 'put',
    data: data
  })
}

// 删除物料
export function delProduct(productIds) {
  return request({
    url: '/wms/product/' + productIds,
    method: 'delete'
  })
}

// 导出物料列表
export function exportProduct(query) {
  return request({
    url: '/wms/product/export',
    method: 'post',
    params: query
  })
}
