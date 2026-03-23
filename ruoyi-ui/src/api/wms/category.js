import request from '@/utils/request'

// 查询物料分类列表
export function listCategory(query) {
  return request({
    url: '/wms/category/list',
    method: 'get',
    params: query
  })
}

// 查询所有有效的分类列表
export function allCategory() {
  return request({
    url: '/wms/category/all',
    method: 'get'
  })
}

// 查询物料分类详细
export function getCategory(categoryId) {
  return request({
    url: '/wms/category/' + categoryId,
    method: 'get'
  })
}

// 新增物料分类
export function addCategory(data) {
  return request({
    url: '/wms/category',
    method: 'post',
    data: data
  })
}

// 修改物料分类
export function updateCategory(data) {
  return request({
    url: '/wms/category',
    method: 'put',
    data: data
  })
}

// 删除物料分类
export function delCategory(categoryId) {
  return request({
    url: '/wms/category/' + categoryId,
    method: 'delete'
  })
}

// 导出物料分类
export function exportCategory(query) {
  return request({
    url: '/wms/category/export',
    method: 'post',
    params: query
  })
}
