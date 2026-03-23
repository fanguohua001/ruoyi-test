# WMS 移动终端 (PDA) 功能设计规格

**日期**: 2026-03-23
**状态**: 待评审
**版本**: 1.0

---

## 1. 概述

### 1.1 项目背景

基于现有 RuoYi 框架 WMS 模块，开发面向 PDA 手持终端的移动应用，支持仓库作业的移动化操作。

### 1.2 目标设备

- **主要设备**: PDA 手持终端（斑马、霍尼韦尔等工业级设备）
- **次要支持**: 普通手机/平板（摄像头扫码）

### 1.3 核心功能

| 优先级 | 模块 | 功能点 |
|--------|------|--------|
| P1 | 入库作业 | 收货、质检、上架 |
| P2 | 出库作业 | 拣货、复核、发货 |
| P3 | 盘点作业 | 盘点单执行、盘点录入 |
| P4 | 移库作业 | 移库单执行 |
| P5 | 查询功能 | 库存查询、库位查询、物料查询 |

---

## 2. 系统架构

### 2.1 目录结构

```
RuoYi-App-master/
├── pages/wms/
│   ├── home.vue              # WMS 工作台（入口）
│   ├── inbound/
│   │   ├── list.vue          # 入库单列表
│   │   ├── receive.vue       # 收货作业
│   │   ├── quality.vue       # 质检作业
│   │   └── putaway.vue       # 上架作业
│   ├── outbound/
│   │   ├── list.vue          # 出库单列表
│   │   ├── picking.vue       # 拣货作业
│   │   ├── review.vue        # 复核作业
│   │   └── ship.vue          # 发货作业
│   ├── stockCheck/
│   │   ├── list.vue          # 盘点单列表
│   │   └── execute.vue       # 盘点执行
│   ├── transfer/
│   │   ├── list.vue          # 移库单列表
│   │   └── execute.vue       # 移库执行
│   └── query/
│       ├── inventory.vue     # 库存查询
│       ├── location.vue      # 库位查询
│       └── product.vue       # 物料查询
├── components/wms/
│   ├── ScanInput.vue         # 扫码输入组件（核心）
│   ├── ProductCard.vue       # 物料卡片
│   ├── LocationCard.vue      # 库位卡片
│   └── StatusTag.vue         # 状态标签
├── api/wms/
│   ├── inbound.js            # 入库 API（已有，需扩展）
│   ├── outbound.js           # 出库 API（已有，需扩展）
│   ├── stockCheck.js         # 盘点 API（已有，需扩展）
│   ├── transfer.js           # 移库 API（已有，需扩展）
│   ├── inventory.js          # 库存 API（已有，需扩展）
│   └── index.js              # API 统一导出
└── utils/
    ├── pda-scan.js           # PDA 扫码工具
    └── offline-queue.js      # 离线队列工具
```

### 2.2 技术栈

- **框架**: uni-app (Vue 3)
- **UI 组件**: uni-ui
- **扫码**: uni.scanCode + PDA 原生 SDK（广播接收）
- **离线存储**: uni.setStorage + 本地队列

---

## 3. 核心组件设计

### 3.1 ScanInput 组件

**功能**: 统一处理三种扫码输入方式

```vue
<template>
  <view class="scan-input">
    <!-- 扫码按钮 -->
    <button @click="triggerScan">扫码</button>
    <!-- 手动输入框 -->
    <input v-model="code" placeholder="或手动输入" />
    <!-- 提交按钮 -->
    <button @click="submit">确认</button>
  </view>
</template>

<script>
export default {
  props: {
    scanType: { type: String, default: 'barcode' }, // barcode / qrcode
    autoFocus: { type: Boolean, default: true }
  },
  methods: {
    // PDA 硬件扫码（广播接收）
    initPdaScan() {
      // 监听 PDA 广播
      plus.android.broadcastReceiver(...)
    },
    // 摄像头扫码
    async triggerScan() {
      const result = await uni.scanCode({ type: this.scanType })
      this.$emit('scan', result.result)
    },
    // 提交
    submit() {
      this.$emit('submit', this.code)
    }
  }
}
</script>
```

### 3.2 离线队列工具

```javascript
// utils/offline-queue.js

// 添加任务到队列
export function addToQueue(type, data) {
  const task = {
    id: Date.now(),
    type,      // 'receive' / 'picking' / 'check'
    data,
    timestamp: Date.now(),
    synced: false
  }
  // 存入本地存储
  uni.setStorageSync('offline_queue_' + type, task)
}

// 同步队列（网络恢复时调用）
export async function syncQueue(type) {
  const tasks = getPendingTasks(type)
  for (const task of tasks) {
    try {
      await apiCall(task.type, task.data)
      markAsSynced(task.id)
    } catch (e) {
      // 同步失败，保留在队列
    }
  }
}
```

---

## 4. 功能模块详细设计

### 4.1 入库作业模块

#### 4.1.1 入库单列表页
- 展示待收货、收货中、待质检、待上架的入库单
- 支持按入库单号、供应商筛选
- 点击进入详情页

#### 4.1.2 收货作业页
```
流程:
1. 扫描/选择入库单
2. 扫描物料条码
3. 输入收货数量
4. 提交收货

关键字段:
- 应收数量 / 实收数量
- 批次号（可选）
- 生产日期/有效期（可选）
```

#### 4.1.3 质检作业页
```
流程:
1. 选择待质检入库单
2. 扫描/选择物料
3. 输入合格数量、不合格数量
4. 提交质检

关键字段:
- 合格数量
- 不合格数量
- 不合格原因（可选）
```

#### 4.1.4 上架作业页
```
流程:
1. 选择待上架入库单
2. 扫描/选择物料
3. 扫描目标库位
4. 输入上架数量
5. 提交上架

关键字段:
- 库位编码
- 上架数量
```

### 4.2 出库作业模块

#### 4.2.1 出库单列表页
- 展示待拣货、拣货中、待复核、待发货的出库单
- 支持按出库单号、客户筛选

#### 4.2.2 拣货作业页
```
流程:
1. 扫描/选择出库单
2. 系统显示拣货任务（物料 + 库位）
3. 扫描库位确认
4. 扫描物料确认
5. 输入拣货数量
6. 提交拣货

关键特性:
- 支持按拣货路径排序
- 支持部分拣货
```

#### 4.2.3 复核作业页
```
流程:
1. 扫描/选择出库单
2. 扫描已拣货物料
3. 系统比对拣货数量
4. 确认复核

关键特性:
- 差异提示
- 支持重新拣货
```

#### 4.2.4 发货作业页
```
流程:
1. 扫描/选择出库单
2. 确认发货
3. 更新状态

关键字段:
- 发货日期
- 物流信息（可选）
```

### 4.3 盘点作业模块

#### 4.3.1 盘点单列表页
- 展示盘点中、待审核的盘点单

#### 4.3.2 盘点执行页
```
流程:
1. 选择盘点单
2. 扫描库位
3. 扫描物料
4. 输入实盘数量
5. 提交盘点

关键特性:
- 显示系统数量（盲盘/明盘可选）
- 差异数量实时计算
- 支持复盘
```

### 4.4 移库作业模块

#### 4.4.1 移库单列表页
- 展示待执行的移库单

#### 4.4.2 移库执行页
```
流程:
1. 选择移库单
2. 扫描源库位
3. 扫描物料
4. 扫描目标库位
5. 输入移库数量
6. 提交移库
```

### 4.5 查询模块

#### 4.5.1 库存查询
- 按物料编码/名称查询
- 按仓库/库位查询
- 显示：现有数量、可用数量、锁定数量

#### 4.5.2 库位查询
- 按库位编码查询
- 显示：库位状态、存放物料

#### 4.5.3 物料查询
- 按编码/名称查询
- 显示：规格型号、单位、库存上下限

---

## 5. 界面设计规范

### 5.1 布局规范

```
页面结构:
┌────────────────────────┐
│   导航栏 (标题 + 返回)   │  高度：44px
├────────────────────────┤
│   搜索/筛选区           │  高度：60px
├────────────────────────┤
│                        │
│   内容区               │
│   (列表/表单)          │
│                        │
├────────────────────────┤
│   底部操作栏           │  高度：50px
└────────────────────────┘
```

### 5.2 控件规范

| 控件 | 最小尺寸 | 说明 |
|------|----------|------|
| 按钮 | 44px × 120px | 适合手套操作 |
| 输入框 | 高度 44px | 字体 16px |
| 列表项 | 高度 60px | 左右留白 15px |
| 文字 | ≥16px | 正文 16px，标题 18px |

### 5.3 配色方案

```css
/* 主色调 - 若依蓝 */
--primary: #1890ff;

/* 状态色 */
--success: #52c41a;  /* 完成/正常 */
--warning: #faad14;  /* 待处理/警告 */
--error: #f5222d;    /* 失败/错误 */
--info: #1890ff;     /* 进行中 */

/* 背景色 */
--bg-page: #f5f5f5;
--bg-card: #ffffff;

/* 文字色 */
--text-primary: #333333;
--text-secondary: #666666;
--text-disabled: #999999;
```

### 5.4 扫码反馈

| 状态 | 视觉反馈 | 声音反馈 |
|------|----------|----------|
| 扫码成功 | 绿色边框闪烁 | 短促"滴"声 |
| 扫码失败 | 红色边框闪烁 | 长促"滴滴"声 |
| 扫码无匹配 | 黄色边框闪烁 | 两短声 |

---

## 6. 离线策略

### 6.1 离线场景

| 功能 | 离线支持 | 同步策略 |
|------|----------|----------|
| 收货 | 支持 | 网络恢复自动同步 |
| 质检 | 支持 | 网络恢复自动同步 |
| 上架 | 支持 | 网络恢复自动同步 |
| 拣货 | 支持 | 网络恢复自动同步 |
| 盘点录入 | 支持 | 网络恢复自动同步 |
| 移库 | 支持 | 网络恢复自动同步 |
| 列表查询 | 不支持 | 显示离线提示 |
| 详情查询 | 不支持 | 显示离线提示 |

### 6.2 离线数据结构

```javascript
// 本地存储结构
{
  offline_queue: {
    receive: [/* 收货任务队列 */],
    picking: [/* 拣货任务队列 */],
    check: [/* 盘点任务队列 */],
    transfer: [/* 移库任务队列 */]
  },
  offline_cache: {
    inbound_orders: [/* 入库单缓存 */],
    outbound_orders: [/* 出库单缓存 */],
    stock_checks: [/* 盘点单缓存 */]
  },
  sync_status: {
    last_sync_time: 1234567890,
    is_online: true
  }
}
```

---

## 7. API 接口

### 7.1 需要新增/扩展的 API

| 接口 | 方法 | 说明 |
|------|------|------|
| `/wms/inbound/receive` | PUT | 收货（已有） |
| `/wms/inbound/quality` | PUT | 质检（已有） |
| `/wms/inbound/putaway` | PUT | 上架（已有） |
| `/wms/outbound/picking` | PUT | 拣货（已有） |
| `/wms/outbound/review` | PUT | 复核（已有） |
| `/wms/outbound/ship` | PUT | 发货（已有） |
| `/wms/stockCheck/record` | POST | 盘点录入（新增） |
| `/wms/transfer/execute` | POST | 移库执行（已有） |
| `/wms/inventory/detail` | GET | 库存详情（已有） |
| `/wms/location/detail` | GET | 库位详情（新增） |
| `/wms/product/detail` | GET | 物料详情（已有） |

---

## 8. 开发计划

### 阶段一：基础框架 + 入库模块（P1）
- [ ] 搭建 WMS 移动端框架
- [ ] 开发 ScanInput 组件
- [ ] 开发离线队列工具
- [ ] 入库单列表页
- [ ] 收货作业页
- [ ] 质检作业页
- [ ] 上架作业页

### 阶段二：出库模块（P2）
- [ ] 出库单列表页
- [ ] 拣货作业页
- [ ] 复核作业页
- [ ] 发货作业页

### 阶段三：盘点 + 移库（P3/P4）
- [ ] 盘点单列表页
- [ ] 盘点执行页
- [ ] 移库单列表页
- [ ] 移库执行页

### 阶段四：查询功能（P5）
- [ ] 库存查询页
- [ ] 库位查询页
- [ ] 物料查询页
- [ ] WMS 工作台首页

---

## 9. 测试计划

### 9.1 功能测试
- 扫码功能测试（PDA 硬件 + 摄像头 + 手动）
- 离线模式测试
- 数据同步测试

### 9.2 兼容性测试
- PDA 设备测试（斑马、霍尼韦尔）
-  Android 版本兼容性

### 9.3 性能测试
- 大量数据列表渲染
- 离线队列同步性能

---

## 10. 附录

### 10.1 PDA 设备对接参考

**斑马设备 EMDK 集成**:
```javascript
// 监听 PDA 扫码广播
plus.android.broadcastReceiver({
  onReceive: function(context, intent) {
    const scanData = intent.getStringExtra("SCAN_DATA")
    // 处理扫码结果
  }
}, 'com.zebra.scanner.action.DATA')
```

### 10.2 uni-app 扫码 API
```javascript
uni.scanCode({
  scanType: ['qrCode', 'barCode'],
  success: function(res) {
    console.log(res.result)
  }
})
```
