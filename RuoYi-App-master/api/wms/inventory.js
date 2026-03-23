import request from '@/utils/request'

// 查询库存列表
export function listInventory(query) {
  return request({
    url: '/wms/inventory/list',
    method: 'get',
    params: query
  })
}

// 查询库存详情
export function getInventory(inventoryId) {
  return request({
    url: '/wms/inventory/' + inventoryId,
    method: 'get'
  })
}

// 查询实时库存数量
export function getStockQty(productId, warehouseId) {
  return request({
    url: '/wms/inventory/qty?productId=' + productId +
         '&warehouseId=' + warehouseId,
    method: 'get'
  })
}

// 库存冻结
export function freezeInventory(inventoryId) {
  return request({
    url: '/wms/inventory/freeze/' + inventoryId,
    method: 'put'
  })
}

// 库存解冻
export function unfreezeInventory(inventoryId) {
  return request({
    url: '/wms/inventory/unfreeze/' + inventoryId,
    method: 'put'
  })
}

// 检查库存预警
export function checkAlert() {
  return request({
    url: '/wms/inventory/alert/check',
    method: 'get'
  })
}

// 导出库存列表
export function exportInventory(query) {
  return request({
    url: '/wms/inventory/export',
    method: 'post',
    params: query
  })
}
