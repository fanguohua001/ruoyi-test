// api/wms/index.js
// WMS 模块 API 统一导出文件

// 入库模块
export {
  listInbound,
  getInbound,
  addInbound,
  editInbound,
  delInbound,
  receive,
  qualityCheck,
  putAway,
  finishInbound,
  checkInboundFinished
} from './inbound'

// 出库模块
export {
  listOutbound,
  getOutbound,
  addOutbound,
  editOutbound,
  delOutbound,
  picking,
  review,
  ship
} from './outbound'

// 库存模块
export {
  listInventory,
  getInventory,
  getStockQty,
  freezeInventory,
  unfreezeInventory,
  checkAlert,
  exportInventory
} from './inventory'

// 移库模块
export {
  listTransfer,
  getTransfer,
  addTransfer,
  executeTransfer
} from './transfer'

// 盘点模块
export {
  listStockCheck,
  getStockCheck,
  addStockCheck,
  recordStockCheck
} from './stockCheck'

// 基础数据模块
export {
  listProduct,
  getProduct,
  addProduct,
  editProduct,
  delProduct
} from './product'

// 预警模块
export {
  listAlert,
  getAlert,
  handleAlert
} from './alert'

// 采购订单模块
export {
  listPurchaseOrder,
  getPurchaseOrder,
  addPurchaseOrder,
  editPurchaseOrder,
  delPurchaseOrder
} from './purchaseOrder'

// 客户模块
export {
  listCustomer,
  getCustomer,
  addCustomer,
  editCustomer,
  delCustomer
} from './customer'

// 客户订单模块
export {
  listCustomerOrder,
  getCustomerOrder,
  addCustomerOrder,
  editCustomerOrder,
  delCustomerOrder
} from './customerOrder'

// 供应商模块
export {
  listSupplier,
  getSupplier,
  addSupplier,
  editSupplier,
  delSupplier
} from './supplier'
