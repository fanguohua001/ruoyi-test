# WMS 移动端 (PDA) 开发计划

**设计文档**: `docs/superpowers/specs/2026-03-23-wms-mobile-pda-design.md`

---

## 阶段一：基础框架 + 入库模块（优先级 P1）

### 任务 1: 搭建 WMS 移动端框架
- [ ] 创建 `pages/wms/home.vue` - WMS 工作台首页
- [ ] 配置 `pages.json` 添加 WMS 页面路由
- [ ] 创建 `api/wms/index.js` - API 统一导出

### 任务 2: 开发核心组件
- [ ] 创建 `components/wms/ScanInput.vue` - 扫码输入组件
- [ ] 创建 `utils/pda-scan.js` - PDA 扫码工具
- [ ] 创建 `utils/offline-queue.js` - 离线队列工具
- [ ] 创建 `components/wms/ProductCard.vue` - 物料卡片
- [ ] 创建 `components/wms/StatusTag.vue` - 状态标签

### 任务 3: 入库模块
- [ ] 创建 `pages/wms/inbound/list.vue` - 入库单列表
- [ ] 创建 `pages/wms/inbound/receive.vue` - 收货作业
- [ ] 创建 `pages/wms/inbound/quality.vue` - 质检作业
- [ ] 创建 `pages/wms/inbound/putaway.vue` - 上架作业

---

## 阶段二：出库模块（优先级 P2）

### 任务 4: 出库模块
- [ ] 创建 `pages/wms/outbound/list.vue` - 出库单列表
- [ ] 创建 `pages/wms/outbound/picking.vue` - 拣货作业
- [ ] 创建 `pages/wms/outbound/review.vue` - 复核作业
- [ ] 创建 `pages/wms/outbound/ship.vue` - 发货作业

---

## 阶段三：盘点 + 移库模块（优先级 P3/P4）

### 任务 5: 盘点模块
- [ ] 创建 `pages/wms/stockCheck/list.vue` - 盘点单列表
- [ ] 创建 `pages/wms/stockCheck/execute.vue` - 盘点执行

### 任务 6: 移库模块
- [ ] 创建 `pages/wms/transfer/list.vue` - 移库单列表
- [ ] 创建 `pages/wms/transfer/execute.vue` - 移库执行

---

## 阶段四：查询功能（优先级 P5）

### 任务 7: 查询模块
- [ ] 创建 `pages/wms/query/inventory.vue` - 库存查询
- [ ] 创建 `pages/wms/query/location.vue` - 库位查询
- [ ] 创建 `pages/wms/query/product.vue` - 物料查询

---

## 开发顺序建议

1. **先做 ScanInput 组件** - 这是所有模块的基础
2. **再做入库模块** - 作业频率最高
3. **然后做出库模块** - 作业频率次高
4. **最后做盘点、移库、查询**

---

## 技术准备

### PDA 扫码对接
```javascript
// 需要调研具体 PDA 型号的 SDK
// 斑马设备：EMDK for Android
// 霍尼韦尔：DataCapture SDK
// 国产 PDA：通常提供广播接收方式
```

### uni-app 配置
```javascript
// manifest.json 需要配置
"android": {
  "permissions": [
    "<uses-permission android:name=\"android.permission.CAMERA\"/>",
    "<uses-feature android:name=\"android.hardware.camera\"/>",
    "<uses-feature android:name=\"android.hardware.camera.autofocus\"/>"
  ]
}
```

---

## 下一步行动

请选择：
1. 开始阶段一（基础框架 + 入库模块）
2. 先看 Demo 示例
3. 调整开发计划
