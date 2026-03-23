import request from '@/utils/request'

// 查询库存台账列表
export function listLedger(query) {
  return request({
    url: '/wms/inventoryLedger/list',
    method: 'get',
    params: query
  })
}

// 查询库存台账详细
export function getLedger(ledgerId) {
  return request({
    url: '/wms/inventoryLedger/' + ledgerId,
    method: 'get'
  })
}

// 导出库存台账
export function exportLedger(query) {
  return request({
    url: '/wms/inventoryLedger/export',
    method: 'post',
    data: query
  })
}
