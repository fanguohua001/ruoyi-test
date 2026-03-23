# WMS PDA 移动端阶段一实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 开发 WMS PDA 移动端基础框架和入库作业模块（收货、质检、上架）

**Architecture:** 基于 uni-app 框架，采用组件化设计。核心组件 ScanInput 统一处理三种扫码方式（PDA 硬件、摄像头、手动）。入库模块包含列表页和三个作业页，每个页面独立可测试。

**Tech Stack:**
- 框架：uni-app (Vue 3)
- UI 组件：uni-ui
- 扫码：uni.scanCode + PDA 广播接收
- HTTP: 现有 request.js 工具

---

## 文件结构总览

### 新增文件
```
RuoYi-App-master/
├── pages/wms/
│   ├── home.vue              # WMS 工作台
│   └── inbound/
│       ├── list.vue          # 入库单列表
│       ├── receive.vue       # 收货作业
│       ├── quality.vue       # 质检作业
│       └── putaway.vue       # 上架作业
├── components/wms/
│   ├── ScanInput.vue         # 扫码输入组件
│   ├── ProductCard.vue       # 物料卡片
│   └── StatusTag.vue         # 状态标签
├── api/wms/
│   └── index.js              # API 统一导出
└── utils/
    ├── pda-scan.js           # PDA 扫码工具
    └── offline-queue.js      # 离线队列工具
```

### 修改文件
- `pages.json` - 添加 WMS 页面路由

---

## Task 1: 配置 WMS API 统一导出

**Files:**
- Create: `RuoYi-App-master/api/wms/index.js`

- [ ] **Step 1: 创建 API 统一导出文件**

```javascript
// api/wms/index.js
import {
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

// 后续模块导出将在这里添加
// import { listOutbound, ... } from './outbound'

export {
  // 入库
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
}
```

- [ ] **Step 2: 验证导出路径**

确认 `RuoYi-App-master/api/wms/inbound.js` 存在且导出正确

- [ ] **Step 3: Commit**

```bash
cd RuoYi-App-master
git add api/wms/index.js
git commit -m "feat(wms): 添加入库 API 统一导出"
```

---

## Task 2: 开发 PDA 扫码工具

**Files:**
- Create: `RuoYi-App-master/utils/pda-scan.js`

- [ ] **Step 1: 创建 PDA 扫码工具**

```javascript
// utils/pda-scan.js

/**
 * PDA 扫码工具
 * 支持三种扫码方式：PDA 硬件扫码、摄像头扫码、手动输入
 */

let scanCallback = null
let pdaInitialized = false

/**
 * 初始化 PDA 硬件扫码（广播接收）
 * 仅在有 PDA SDK 的设备上生效
 */
export function initPdaScan() {
  // #ifdef APP-PLUS
  try {
    // 尝试获取主线程 Android 对象
    const main = plus.android.runtimeMainActivity()
    if (!main) return false

    // 注册广播接收器（通用 PDA 扫码广播）
    const BroadcastReceiver = plus.android.importClass('android.content.BroadcastReceiver')
    const IntentFilter = plus.android.importClass('android.content.IntentFilter')

    main.registerReceiver(new BroadcastReceiver({
      onReceive: function(ctx, intent) {
        if (scanCallback) {
          const action = intent.getAction()
          // 尝试获取扫码数据（不同厂商可能不同）
          const data = intent.getStringExtra('SCAN_DATA') ||
                       intent.getStringExtra('barcode_string') ||
                       intent.getStringExtra('data')
          if (data) {
            scanCallback(data)
            // 播放扫码成功提示音
            playScanSound('success')
          }
        }
      }
    }), new IntentFilter('android.intent.action.SCAN_RESULT'))

    pdaInitialized = true
    return true
  } catch (e) {
    console.log('PDA 初始化失败:', e)
    return false
  }
  // #endif

  return false
}

/**
 * 注册扫码回调
 */
export function onScanResult(callback) {
  scanCallback = callback
}

/**
 * 移除扫码回调
 */
export function offScanResult() {
  scanCallback = null
}

/**
 * 摄像头扫码
 */
export async function scanByCamera(scanType = 'barCode') {
  return new Promise((resolve, reject) => {
    uni.scanCode({
      scanType: [scanType, 'qrCode'],
      success: (res) => {
        playScanSound('success')
        resolve(res.result)
      },
      fail: (err) => {
        if (err.errMsg !== 'scanCode:fail cancel') {
          playScanSound('error')
        }
        reject(err)
      }
    })
  })
}

/**
 * 播放扫码提示音
 * 注意：声音文件为可选，如无声音文件则不播放
 */
function playScanSound(type) {
  // #ifdef APP-PLUS
  try {
    // 检查声音文件是否存在（可选功能）
    const soundPath = type === 'success' ? '_www/sounds/success.mp3' : '_www/sounds/error.mp3'
    plus.io.resolveLocalFileSystemURL(soundPath, (entry) => {
      if (entry) {
        const sound = plus.audio.createPlayer(soundPath)
        sound.play()
      }
    }, (err) => {
      // 声音文件不存在，跳过播放
      console.log('声音文件不存在，跳过播放')
    })
  } catch (e) {
    // 忽略声音播放失败
    console.log('声音播放失败:', e)
  }
  // #endif
}

/**
 * 检查是否 PDA 设备
 */
export function isPdaDevice() {
  return pdaInitialized
}

export default {
  initPdaScan,
  onScanResult,
  offScanResult,
  scanByCamera,
  isPdaDevice
}
```

- [ ] **Step 2: 验证文件创建成功**

```bash
ls -la RuoYi-App-master/utils/pda-scan.js
```

- [ ] **Step 3: Commit**

```bash
cd RuoYi-App-master
git add utils/pda-scan.js
git commit -m "feat(wms): 添加 PDA 扫码工具"
```

---

## Task 3: 开发离线队列工具

**Files:**
- Create: `RuoYi-App-master/utils/offline-queue.js`

- [ ] **Step 1: 创建离线队列工具**

```javascript
// utils/offline-queue.js

/**
 * 离线队列工具
 * 支持作业数据的本地缓存和网络恢复后的自动同步
 */

const QUEUE_KEY_PREFIX = 'wms_offline_queue_'
const SYNC_STATUS_KEY = 'wms_sync_status'

// 作业类型
export const TASK_TYPES = {
  RECEIVE: 'receive',
  QUALITY: 'quality',
  PUTAWAY: 'putaway',
  PICKING: 'picking',
  STOCK_CHECK: 'stock_check',
  TRANSFER: 'transfer'
}

/**
 * 添加任务到队列
 */
export function addToQueue(type, data) {
  const task = {
    id: Date.now().toString() + Math.random().toString(36).substr(2, 5),
    type,
    data,
    timestamp: Date.now(),
    synced: false,
    retryCount: 0
  }

  const queue = getQueue(type)
  queue.push(task)
  saveQueue(type, queue)

  return task.id
}

/**
 * 获取队列
 */
export function getQueue(type) {
  const key = QUEUE_KEY_PREFIX + type
  const data = uni.getStorageSync(key)
  return data ? JSON.parse(data) : []
}

/**
 * 保存队列
 */
export function saveQueue(type, queue) {
  const key = QUEUE_KEY_PREFIX + type
  uni.setStorageSync(key, JSON.stringify(queue))
}

/**
 * 获取待同步任务
 */
export function getPendingTasks(type) {
  const queue = getQueue(type)
  return queue.filter(task => !task.synced)
}

/**
 * 标记任务已同步
 */
export function markAsSynced(taskId) {
  // 遍历所有队列查找并更新
  Object.values(TASK_TYPES).forEach(type => {
    const queue = getQueue(type)
    const task = queue.find(t => t.id === taskId)
    if (task) {
      task.synced = true
      saveQueue(type, queue)
    }
  })
}

/**
 * 清理已同步任务（保留最近 100 条）
 */
export function cleanSyncedTasks(type, keepCount = 100) {
  const queue = getQueue(type)
  const synced = queue.filter(t => t.synced)
  const pending = queue.filter(t => !t.synced)

  // 保留最新的 keepCount 条已同步记录
  const syncedToKeep = synced.slice(-keepCount)

  const newQueue = [...pending, ...syncedToKeep]
  saveQueue(type, newQueue)
}

/**
 * 获取同步状态
 */
export function getSyncStatus() {
  const data = uni.getStorageSync(SYNC_STATUS_KEY)
  return data || {
    lastSyncTime: 0,
    isOnline: true,
    syncing: false
  }
}

/**
 * 更新同步状态
 */
export function updateSyncStatus(status) {
  const current = getSyncStatus()
  uni.setStorageSync(SYNC_STATUS_KEY, { ...current, ...status })
}

/**
 * 网络状态监听
 */
export function initNetworkMonitor() {
  // #ifdef APP-PLUS
  uni.onNetworkStatusChange((res) => {
    updateSyncStatus({ isOnline: res.isConnected })
    if (res.isConnected) {
      // 网络恢复，触发同步
      syncAllQueues()
    }
  })
  // #endif
}

/**
 * 同步队列（需要外部提供 API 调用方法）
 */
export async function syncQueue(type, apiFn) {
  const pending = getPendingTasks(type)
  if (pending.length === 0) return { success: 0, failed: 0 }

  updateSyncStatus({ syncing: true })

  let success = 0
  let failed = 0

  for (const task of pending) {
    try {
      await apiFn(task.data)
      markAsSynced(task.id)
      success++
    } catch (e) {
      task.retryCount++
      if (task.retryCount >= 3) {
        // 超过 3 次重试，标记为失败但保留
        markAsSynced(task.id)
      }
      failed++
    }
  }

  updateSyncStatus({
    syncing: false,
    lastSyncTime: Date.now()
  })

  cleanSyncedTasks(type)

  return { success, failed }
}

/**
 * 同步所有队列
 */
export async function syncAllQueues(apiMap) {
  const results = {}
  for (const type of Object.keys(TASK_TYPES)) {
    if (apiMap[type]) {
      results[type] = await syncQueue(type, apiMap[type])
    }
  }
  return results
}

export default {
  TASK_TYPES,
  addToQueue,
  getQueue,
  saveQueue,
  getPendingTasks,
  markAsSynced,
  cleanSyncedTasks,
  getSyncStatus,
  updateSyncStatus,
  initNetworkMonitor,
  syncQueue,
  syncAllQueues
}
```

- [ ] **Step 2: Commit**

```bash
cd RuoYi-App-master
git add utils/offline-queue.js
git commit -m "feat(wms): 添加离线队列工具"
```

---

## Task 4: 开发扫码输入组件 ScanInput

**Files:**
- Create: `RuoYi-App-master/components/wms/ScanInput.vue`

- [ ] **Step 1: 创建 components/wms 目录**

```bash
mkdir -p RuoYi-App-master/components/wms
```

- [ ] **Step 2: 创建 ScanInput 组件**

```vue
<!-- components/wms/ScanInput.vue -->
<template>
  <view class="scan-input">
    <view class="scan-input__header">
      <text class="scan-input__label">{{ label }}</text>
      <view class="scan-input__actions">
        <!-- PDA 硬件扫码按钮 -->
        <button
          v-if="supportPda"
          class="scan-input__btn scan-input__btn--pda"
          @click="triggerPdaScan"
        >
          <uni-icons type="scan" size="20"></uni-icons>
          <text>PDA 扫码</text>
        </button>

        <!-- 摄像头扫码按钮 -->
        <button
          class="scan-input__btn scan-input__btn--camera"
          @click="triggerCameraScan"
        >
          <uni-icons type="camera" size="20"></uni-icons>
          <text>扫码</text>
        </button>
      </view>
    </view>

    <view
      class="scan-input__body"
      :class="{ 'scan-input__body--success': scanSuccess, 'scan-input__body--error': scanError }"
    >
      <input
        v-model="inputValue"
        class="scan-input__field"
        :placeholder="placeholder"
        :disabled="disabled"
        @input="onInput"
        @confirm="onConfirm"
      />
    </view>

    <view class="scan-input__footer">
      <button
        class="scan-input__submit"
        :disabled="!canSubmit"
        @click="onConfirm"
      >
        确认
      </button>
    </view>

    <!-- 扫码结果提示 -->
    <uni-transition
      v-if="showScanTip"
      :mode-class="['fade', 'slide-top']"
      :show="showScanTip"
      @click="hideScanTip"
    >
      <view class="scan-tip" :class="`scan-tip--${scanTipType}`">
        <text>{{ scanTipText }}</text>
      </view>
    </uni-transition>
  </view>
</template>

<script>
import { initPdaScan, onScanResult, offScanResult, scanByCamera } from '@/utils/pda-scan'

export default {
  name: 'ScanInput',
  props: {
    modelValue: {
      type: String,
      default: ''
    },
    label: {
      type: String,
      default: '扫码'
    },
    placeholder: {
      type: String,
      default: '请扫描或输入条码'
    },
    scanType: {
      type: String,
      default: 'barCode'
    },
    autoFocus: {
      type: Boolean,
      default: true
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:modelValue', 'submit', 'scan'],
  data() {
    return {
      inputValue: '',
      supportPda: false,
      scanSuccess: false,
      scanError: false,
      showScanTip: false,
      scanTipText: '',
      scanTipType: 'success'
    }
  },
  computed: {
    canSubmit() {
      return this.inputValue.trim() && !this.disabled
    }
  },
  mounted() {
    // 初始化 PDA
    this.supportPda = initPdaScan()
    if (this.supportPda) {
      onScanResult(this.handleScanResult)
    }

    // 自动聚焦
    if (this.autoFocus && !this.disabled) {
      this.$nextTick(() => {
        // #ifdef H5
        const input = document.querySelector('.scan-input__field')
        if (input) input.focus()
        // #endif
      })
    }
  },
  beforeUnmount() {
    if (this.supportPda) {
      offScanResult()
    }
  },
  methods: {
    // PDA 硬件扫码（通过广播回调自动触发）
    triggerPdaScan() {
      this.showScanTip = true
      this.scanTipText = '请扫描条码...'
      this.scanTipType = 'info'
    },

    // 处理扫码结果（PDA 广播）
    handleScanResult(data) {
      this.inputValue = data
      this.$emit('update:modelValue', data)
      this.$emit('scan', data)
      this.showScanSuccess('扫码成功')
    },

    // 摄像头扫码
    async triggerCameraScan() {
      try {
        const result = await scanByCamera(this.scanType)
        this.inputValue = result
        this.$emit('update:modelValue', result)
        this.$emit('scan', result)
      } catch (e) {
        if (e.errMsg !== 'scanCode:fail cancel') {
          this.showScanError('扫码失败')
        }
      }
    },

    // 输入处理
    onInput(value) {
      this.$emit('update:modelValue', value)
    },

    // 确认提交
    onConfirm() {
      if (this.canSubmit) {
        this.$emit('submit', this.inputValue.trim())
      }
    },

    // 显示成功提示
    showScanSuccess(text) {
      this.scanSuccess = true
      this.scanTipText = text
      this.scanTipType = 'success'
      this.showScanTip = true

      setTimeout(() => {
        this.scanSuccess = false
      }, 1000)
    },

    // 显示失败提示
    showScanError(text) {
      this.scanError = true
      this.scanTipText = text
      this.scanTipType = 'error'
      this.showScanTip = true

      setTimeout(() => {
        this.scanError = false
      }, 2000)
    },

    // 隐藏提示
    hideScanTip() {
      this.showScanTip = false
    }
  }
}
</script>

<style lang="scss" scoped>
.scan-input {
  padding: 20rpx;
  background-color: #fff;

  &__header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15rpx;
  }

  &__label {
    font-size: 28rpx;
    color: #666;
    font-weight: 500;
  }

  &__actions {
    display: flex;
    gap: 15rpx;
  }

  &__btn {
    display: flex;
    align-items: center;
    gap: 5rpx;
    padding: 10rpx 20rpx;
    font-size: 24rpx;
    background-color: #1890ff;
    color: #fff;
    border: none;
    border-radius: 8rpx;

    &--pda {
      background-color: #13c2c2;
    }

    &::after {
      border: none;
    }
  }

  &__body {
    border: 2rpx solid #e8e8e8;
    border-radius: 8rpx;
    padding: 10rpx;
    transition: border-color 0.3s;

    &--success {
      border-color: #52c41a;
    }

    &--error {
      border-color: #f5222d;
    }
  }

  &__field {
    width: 100%;
    height: 44px;
    font-size: 32rpx;
    line-height: 44px;
  }

  &__footer {
    margin-top: 20rpx;
  }

  &__submit {
    width: 100%;
    height: 48px;
    line-height: 48px;
    font-size: 32rpx;
    background-color: #1890ff;
    color: #fff;
    border: none;
    border-radius: 8rpx;

    &:disabled {
      background-color: #d9d9d9;
    }

    &::after {
      border: none;
    }
  }
}

.scan-tip {
  position: fixed;
  top: 100rpx;
  left: 50%;
  transform: translateX(-50%);
  padding: 15rpx 30rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
  z-index: 999;

  &--success {
    background-color: rgba(82, 196, 26, 0.9);
    color: #fff;
  }

  &--error {
    background-color: rgba(245, 34, 45, 0.9);
    color: #fff;
  }

  &--info {
    background-color: rgba(24, 144, 255, 0.9);
    color: #fff;
  }
}
</style>
```

- [ ] **Step 3: Commit**

```bash
cd RuoYi-App-master
git add components/wms/ScanInput.vue
git commit -m "feat(wms): 添加扫码输入组件"
```

---

## Task 5: 开发物料卡片组件

**Files:**
- Create: `RuoYi-App-master/components/wms/ProductCard.vue`

- [ ] **Step 1: 创建 ProductCard 组件**

```vue
<!-- components/wms/ProductCard.vue -->
<template>
  <view class="product-card">
    <view class="product-card__header">
      <text class="product-card__name">{{ product.productName }}</text>
      <text class="product-card__code">{{ product.productCode }}</text>
    </view>

    <view class="product-card__body">
      <view class="product-card__row">
        <text class="product-card__label">规格型号:</text>
        <text class="product-card__value">{{ product.specification || '-' }}</text>
      </view>
      <view class="product-card__row">
        <text class="product-card__label">单位:</text>
        <text class="product-card__value">{{ product.unit || '-' }}</text>
      </view>
      <view class="product-card__row" v-if="showBatch">
        <text class="product-card__label">批次号:</text>
        <text class="product-card__value">{{ product.batchNo || '-' }}</text>
      </view>
      <view class="product-card__row" v-if="showQty">
        <text class="product-card__label">数量:</text>
        <text class="product-card__value product-card__qty">{{ product.qty }}</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'ProductCard',
  props: {
    product: {
      type: Object,
      required: true
    },
    showBatch: {
      type: Boolean,
      default: false
    },
    showQty: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style lang="scss" scoped>
.product-card {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.08);

  &__header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 15rpx;
    border-bottom: 1rpx solid #f0f0f0;
  }

  &__name {
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
    flex: 1;
  }

  &__code {
    font-size: 24rpx;
    color: #999;
    margin-left: 20rpx;
  }

  &__body {
    padding-top: 15rpx;
  }

  &__row {
    display: flex;
    align-items: center;
    margin-bottom: 10rpx;
  }

  &__label {
    font-size: 26rpx;
    color: #666;
    width: 140rpx;
  }

  &__value {
    font-size: 28rpx;
    color: #333;
    flex: 1;
  }

  &__qty {
    font-size: 32rpx;
    font-weight: 600;
    color: #1890ff;
  }
}
</style>
```

- [ ] **Step 2: Commit**

```bash
cd RuoYi-App-master
git add components/wms/ProductCard.vue
git commit -m "feat(wms): 添加物料卡片组件"
```

---

## Task 6: 开发状态标签组件

**Files:**
- Create: `RuoYi-App-master/components/wms/StatusTag.vue`

- [ ] **Step 1: 创建 StatusTag 组件**

```vue
<!-- components/wms/StatusTag.vue -->
<template>
  <view class="status-tag" :class="`status-tag--${type}`">
    <text>{{ text }}</text>
  </view>
</template>

<script>
// 入库单状态映射
const INBOUND_STATUS_MAP = {
  '1': { text: '待收货', type: 'warning' },
  '2': { text: '收货中', type: 'info' },
  '3': { text: '待质检', type: 'info' },
  '4': { text: '待上架', type: 'info' },
  '5': { text: '已入库', type: 'success' }
}

// 出库单状态映射
const OUTBOUND_STATUS_MAP = {
  '1': { text: '待审核', type: 'warning' },
  '2': { text: '待拣货', type: 'warning' },
  '3': { text: '拣货中', type: 'info' },
  '4': { text: '待复核', type: 'info' },
  '5': { text: '待打包', type: 'info' },
  '6': { text: '待发货', type: 'info' },
  '7': { text: '已发货', type: 'success' },
  '8': { text: '已取消', type: 'error' }
}

export default {
  name: 'StatusTag',
  props: {
    status: {
      type: [String, Number],
      required: true
    },
    type: {
      type: String,
      default: '' // warning, info, success, error
    },
    statusMap: {
      type: Object,
      default: null
    }
  },
  computed: {
    config() {
      const map = this.statusMap || INBOUND_STATUS_MAP
      return map[this.status] || { text: this.status, type: this.type || 'default' }
    },
    text() {
      return this.config.text
    },
    tagType() {
      return this.config.type
    }
  }
}
</script>

<style lang="scss" scoped>
.status-tag {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 6rpx 16rpx;
  border-radius: 4rpx;
  font-size: 24rpx;
  line-height: 1.5;

  &--warning {
    background-color: #fff7e6;
    color: #faad14;
  }

  &--info {
    background-color: #e6f7ff;
    color: #1890ff;
  }

  &--success {
    background-color: #f6ffed;
    color: #52c41a;
  }

  &--error {
    background-color: #fff1f0;
    color: #f5222d;
  }

  &--default {
    background-color: #f5f5f5;
    color: #666;
  }
}
</style>
```

- [ ] **Step 2: Commit**

```bash
cd RuoYi-App-master
git add components/wms/StatusTag.vue
git commit -m "feat(wms): 添加状态标签组件"
```

---

## Task 7: 更新 pages.json 添加 WMS 路由

**Files:**
- Modify: `RuoYi-App-master/pages.json`

- [ ] **Step 1: 在 pages.json 中添加 WMS 页面路由**

在 `pages` 数组中添加以下页面（在现有页面之后添加）:

```javascript
{
  "path": "pages/wms/home",
  "style": {
    "navigationBarTitleText": "WMS 工作台"
  }
},
{
  "path": "pages/wms/inbound/list",
  "style": {
    "navigationBarTitleText": "入库单列表"
  }
},
{
  "path": "pages/wms/inbound/receive",
  "style": {
    "navigationBarTitleText": "收货作业"
  }
},
{
  "path": "pages/wms/inbound/quality",
  "style": {
    "navigationBarTitleText": "质检作业"
  }
},
{
  "path": "pages/wms/inbound/putaway",
  "style": {
    "navigationBarTitleText": "上架作业"
  }
}
```

- [ ] **Step 2: Commit**

```bash
cd RuoYi-App-master
git add pages.json
git commit -m "feat(wms): 添加入库模块页面路由"
```

---

## Task 8: 开发 WMS 工作台首页

**Files:**
- Create: `RuoYi-App-master/pages/wms/home.vue`

- [ ] **Step 1: 创建 WMS 工作台页面**

```vue
<!-- pages/wms/home.vue -->
<template>
  <view class="wms-home">
    <view class="home-header">
      <text class="home-title">WMS 工作台</text>
      <text class="home-subtitle">移动仓储管理</text>
    </view>

    <view class="home-section">
      <text class="section-title">入库作业</text>
      <view class="home-grid">
        <view class="home-item" @click="navigateTo('/pages/wms/inbound/list')">
          <view class="home-item__icon home-item__icon--inbound">
            <uni-icons type="download" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">入库单列表</text>
        </view>
        <view class="home-item" @click="navigateTo('/pages/wms/inbound/receive')">
          <view class="home-item__icon home-item__icon--receive">
            <uni-icons type="cart" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">收货</text>
        </view>
        <view class="home-item" @click="navigateTo('/pages/wms/inbound/quality')">
          <view class="home-item__icon home-item__icon--quality">
            <uni-icons type="checkmarkempty" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">质检</text>
        </view>
        <view class="home-item" @click="navigateTo('/pages/wms/inbound/putaway')">
          <view class="home-item__icon home-item__icon--putaway">
            <uni-icons type="upload" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">上架</text>
        </view>
      </view>
    </view>

    <view class="home-section">
      <text class="section-title">出库作业</text>
      <view class="home-grid">
        <view class="home-item">
          <view class="home-item__icon home-item__icon--outbound">
            <uni-icons type="shop" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">出库单列表</text>
        </view>
        <view class="home-item">
          <view class="home-item__icon home-item__icon--picking">
            <uni-icons type="gear" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">拣货</text>
        </view>
        <view class="home-item">
          <view class="home-item__icon home-item__icon--review">
            <uni-icons type="eye" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">复核</text>
        </view>
        <view class="home-item">
          <view class="home-item__icon home-item__icon--ship">
            <uni-icons type="paperplane" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">发货</text>
        </view>
      </view>
      <view class="home-tip">建设中...</view>
    </view>

    <view class="home-section">
      <text class="section-title">其他功能</text>
      <view class="home-grid home-grid--small">
        <view class="home-item home-item--small">
          <view class="home-item__icon home-item__icon--check">
            <uni-icons type="compose" size="24" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">盘点</text>
        </view>
        <view class="home-item home-item--small">
          <view class="home-item__icon home-item__icon--transfer">
            <uni-icons type="swap" size="24" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">移库</text>
        </view>
        <view class="home-item home-item--small">
          <view class="home-item__icon home-item__icon--query">
            <uni-icons type="search" size="24" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">查询</text>
        </view>
      </view>
      <view class="home-tip">建设中...</view>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    navigateTo(url) {
      uni.navigateTo({ url })
    }
  }
}
</script>

<style lang="scss" scoped>
.wms-home {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
}

.home-header {
  padding: 30rpx 20rpx;
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  border-radius: 12rpx;
  margin-bottom: 20rpx;

  .home-title {
    display: block;
    font-size: 36rpx;
    font-weight: 600;
    color: #fff;
  }

  .home-subtitle {
    display: block;
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.8);
    margin-top: 10rpx;
  }
}

.home-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;

  .section-title {
    display: block;
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 20rpx;
  }
}

.home-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;

  &--small {
    grid-template-columns: repeat(3, 1fr);
  }
}

.home-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 10rpx;

  &--small {
    padding: 15rpx 10rpx;
  }

  &__icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 12rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 10rpx;

    &--inbound { background-color: #1890ff; }
    &--receive { background-color: #52c41a; }
    &--quality { background-color: #faad14; }
    &--putaway { background-color: #13c2c2; }
    &--outbound { background-color: #722ed1; }
    &--picking { background-color: #eb2f96; }
    &--review { background-color: #1890ff; }
    &--ship { background-color: #52c41a; }
    &--check { background-color: #fa8c16; }
    &--transfer { background-color: #13c2c2; }
    &--query { background-color: #8c8c8c; }
  }

  &__text {
    font-size: 24rpx;
    color: #666;
    text-align: center;
  }
}

.home-tip {
  text-align: center;
  color: #999;
  font-size: 24rpx;
  margin-top: 15rpx;
}
</style>
```

- [ ] **Step 2: Commit**

```bash
cd RuoYi-App-master
git add pages/wms/home.vue
git commit -m "feat(wms): 添加 WMS 工作台首页"
```

---

## Task 9: 开发入库单列表页

**Files:**
- Create: `RuoYi-App-master/pages/wms/inbound/list.vue`

- [ ] **Step 1: 创建入库单列表页面**

```vue
<!-- pages/wms/inbound/list.vue -->
<template>
  <view class="inbound-list">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <view class="search-bar__input">
        <uni-icons type="search" size="20" color="#999"></uni-icons>
        <input
          v-model="searchKey"
          placeholder="请输入入库单号/供应商"
          @confirm="onSearch"
        />
      </view>
      <button class="search-bar__btn" @click="onSearch">搜索</button>
    </view>

    <!-- 状态筛选 -->
    <view class="filter-tabs">
      <view
        v-for="item in statusOptions"
        :key="item.value"
        class="filter-tab"
        :class="{ active: currentStatus === item.value }"
        @click="onStatusChange(item.value)"
      >
        {{ item.label }}
      </view>
    </view>

    <!-- 列表内容 -->
    <scroll-view
      class="list-scroll"
      scroll-y
      refresher-enabled
      :refresher-triggered="refreshing"
      @refresherrefresh="onRefresh"
      @scrolltolower="loadMore"
    >
      <view v-if="list.length === 0" class="empty-tip">
        <text>暂无数据</text>
      </view>

      <view v-else class="list-container">
        <view
          v-for="item in list"
          :key="item.inboundId"
          class="list-item"
          @click="onItemClick(item)"
        >
          <view class="item-header">
            <text class="item-no">{{ item.inboundNo }}</text>
            <wms-status-tag :status="item.status" />
          </view>
          <view class="item-body">
            <view class="item-row">
              <text class="item-label">供应商:</text>
              <text class="item-value">{{ item.supplierName || '-' }}</text>
            </view>
            <view class="item-row">
              <text class="item-label">总数量:</text>
              <text class="item-value">{{ item.totalQty || 0 }}</text>
            </view>
            <view class="item-row">
              <text class="item-label">已收货:</text>
              <text class="item-value">{{ item.receivedQty || 0 }}</text>
            </view>
            <view class="item-row">
              <text class="item-label">预计到货:</text>
              <text class="item-value">{{ item.expectedDate || '-' }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 加载更多 -->
      <view class="load-more" v-if="hasMore">
        <text>加载更多...</text>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { listInbound } from '@/api/wms/inbound'
import WmsStatusTag from '@/components/wms/StatusTag.vue'

export default {
  components: { WmsStatusTag },
  data() {
    return {
      searchKey: '',
      currentStatus: '',
      list: [],
      page: 1,
      pageSize: 10,
      total: 0,
      refreshing: false,
      hasMore: true,
      statusOptions: [
        { label: '全部', value: '' },
        { label: '待收货', value: '1' },
        { label: '收货中', value: '2' },
        { label: '待质检', value: '3' },
        { label: '待上架', value: '4' },
        { label: '已入库', value: '5' }
      ]
    }
  },
  onLoad() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData(isRefresh = false) {
      if (isRefresh) {
        this.refreshing = true
        this.page = 1
        this.list = []
      }

      try {
        const res = await listInbound({
          page: this.page,
          limit: this.pageSize,
          status: this.currentStatus,
          searchKey: this.searchKey
        })

        const newList = res.rows || res.list || []
        this.total = res.total || newList.length

        if (isRefresh) {
          this.list = newList
        } else {
          this.list = [...this.list, ...newList]
        }

        this.hasMore = this.list.length < this.total
        this.page++
      } catch (e) {
        console.error('加载失败:', e)
      } finally {
        this.refreshing = false
      }
    },

    // 搜索
    onSearch() {
      this.loadData(true)
    },

    // 状态筛选
    onStatusChange(value) {
      this.currentStatus = value
      this.loadData(true)
    },

    // 下拉刷新
    onRefresh() {
      this.loadData(true)
    },

    // 加载更多
    loadMore() {
      if (this.hasMore && !this.refreshing) {
        this.loadData()
      }
    },

    // 点击条目
    onItemClick(item) {
      // 根据状态跳转到不同页面
      const status = item.status
      if (status === '1' || status === '2') {
        // 待收货/收货中 -> 收货页面
        uni.navigateTo({
          url: `/pages/wms/inbound/receive?inboundId=${item.inboundId}`
        })
      } else if (status === '3') {
        // 待质检 -> 质检页面
        uni.navigateTo({
          url: `/pages/wms/inbound/quality?inboundId=${item.inboundId}`
        })
      } else if (status === '4') {
        // 待上架 -> 上架页面
        uni.navigateTo({
          url: `/pages/wms/inbound/putaway?inboundId=${item.inboundId}`
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.inbound-list {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f5f5;
}

.search-bar {
  display: flex;
  align-items: center;
  padding: 15rpx;
  background-color: #fff;
  gap: 15rpx;

  &__input {
    flex: 1;
    display: flex;
    align-items: center;
    background-color: #f5f5f5;
    border-radius: 8rpx;
    padding: 10rpx 15rpx;

    input {
      flex: 1;
      margin-left: 10rpx;
      font-size: 28rpx;
    }
  }

  &__btn {
    padding: 10rpx 25rpx;
    font-size: 28rpx;
    background-color: #1890ff;
    color: #fff;
    border: none;
    border-radius: 8rpx;

    &::after {
      border: none;
    }
  }
}

.filter-tabs {
  display: flex;
  background-color: #fff;
  padding: 15rpx;
  gap: 15rpx;
  overflow-x: auto;

  .filter-tab {
    padding: 8rpx 20rpx;
    font-size: 26rpx;
    color: #666;
    background-color: #f5f5f5;
    border-radius: 30rpx;
    white-space: nowrap;

    &.active {
      color: #fff;
      background-color: #1890ff;
    }
  }
}

.list-scroll {
  flex: 1;
  padding: 15rpx;
}

.empty-tip {
  text-align: center;
  padding: 100rpx 0;
  color: #999;
  font-size: 28rpx;
}

.list-container {
  display: flex;
  flex-direction: column;
  gap: 15rpx;
}

.list-item {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;

  .item-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 15rpx;
    border-bottom: 1rpx solid #f0f0f0;
  }

  .item-no {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
  }

  .item-body {
    padding-top: 15rpx;
  }

  .item-row {
    display: flex;
    align-items: center;
    margin-bottom: 10rpx;

    .item-label {
      font-size: 26rpx;
      color: #999;
      width: 140rpx;
    }

    .item-value {
      font-size: 28rpx;
      color: #333;
      flex: 1;
    }
  }
}

.load-more {
  text-align: center;
  padding: 20rpx;
  color: #999;
  font-size: 26rpx;
}
</style>
```

- [ ] **Step 2: Commit**

```bash
cd RuoYi-App-master
git add pages/wms/inbound/list.vue
git commit -m "feat(wms): 添加入库单列表页"
```

---

## Task 10: 开发收货作业页

**Files:**
- Create: `RuoYi-App-master/pages/wms/inbound/receive.vue`

- [ ] **Step 1: 创建收货作业页面**

```vue
<!-- pages/wms/inbound/receive.vue -->
<template>
  <view class="receive-page">
    <!-- 入库单信息 -->
    <view class="order-info">
      <view class="info-row">
        <text class="info-label">入库单号:</text>
        <text class="info-value">{{ orderInfo.inboundNo }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">供应商:</text>
        <text class="info-value">{{ orderInfo.supplierName }}</text>
      </view>
    </view>

    <!-- 扫码区 -->
    <view class="scan-section">
      <wms-scan-input
        v-model="scanCode"
        label="扫物料"
        placeholder="扫描物料条码"
        @submit="onScanSubmit"
      />
    </view>

    <!-- 物料信息 -->
    <view v-if="currentItem" class="product-section">
      <wms-product-card :product="currentItem" :show-batch="true" />

      <view class="form-section">
        <view class="form-item">
          <text class="form-label">应收数量</text>
          <text class="form-value">{{ currentItem.expectedQty }}</text>
        </view>
        <view class="form-item">
          <text class="form-label">实收数量</text>
          <input
            v-model="receiveQty"
            type="number"
            class="form-input"
            placeholder="请输入实收数量"
          />
        </view>
        <view class="form-item">
          <text class="form-label">批次号</text>
          <input
            v-model="batchNo"
            class="form-input"
            placeholder="请输入批次号（可选）"
          />
        </view>
        <view class="form-item" v-if="showDatePickers">
          <text class="form-label">生产日期</text>
          <picker mode="date" @change="onProductionDateChange">
            <view class="form-picker">{{ productionDate || '选择日期' }}</view>
          </picker>
        </view>
        <view class="form-item" v-if="showDatePickers">
          <text class="form-label">有效期至</text>
          <picker mode="date" @change="onExpiryDateChange">
            <view class="form-picker">{{ expiryDate || '选择日期' }}</view>
          </picker>
        </view>
      </view>

      <view class="action-section">
        <button class="submit-btn" @click="onSubmit">确认收货</button>
      </view>
    </view>

    <!-- 收货明细列表 -->
    <view class="receive-list">
      <view class="list-title">收货明细</view>
      <view v-for="item in receiveList" :key="item.itemId" class="list-item">
        <view class="list-row">
          <text class="list-label">{{ item.productName }}</text>
          <text class="list-value">已收：{{ item.receivedQty }}/{{ item.expectedQty }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getInbound, receive } from '@/api/wms/inbound'
import WmsScanInput from '@/components/wms/ScanInput.vue'
import WmsProductCard from '@/components/wms/ProductCard.vue'

export default {
  components: { WmsScanInput, WmsProductCard },
  data() {
    return {
      inboundId: '',
      orderInfo: {},
      scanCode: '',
      currentItem: null,
      receiveQty: '',
      batchNo: '',
      productionDate: '',
      expiryDate: '',
      receiveList: [],
      showDatePickers: false
    }
  },
  onLoad(options) {
    if (options.inboundId) {
      this.inboundId = options.inboundId
      this.loadOrderDetail()
    }
  },
  methods: {
    // 加载订单详情
    async loadOrderDetail() {
      try {
        const res = await getInbound(this.inboundId)
        this.orderInfo = res
        this.receiveList = res.items || []
      } catch (e) {
        uni.showToast({ title: '加载失败', icon: 'none' })
      }
    },

    // 扫码提交
    onScanSubmit(code) {
      // 查找匹配的物料
      const item = this.receiveList.find(i =>
        i.productCode === code ||
        this.getProductBarcode(i) === code
      )

      if (item) {
        this.currentItem = { ...item }
        this.receiveQty = String(item.expectedQty - item.receivedQty)
      } else {
        uni.showToast({ title: '未找到匹配物料', icon: 'none' })
      }
    },

    // 获取物料条码
    getProductBarcode(item) {
      // 这里可以根据实际业务逻辑获取条码
      return item.productCode
    },

    // 提交收货
    async onSubmit() {
      if (!this.receiveQty) {
        uni.showToast({ title: '请输入实收数量', icon: 'none' })
        return
      }

      try {
        await receive(this.inboundId, this.currentItem.itemId, {
          qty: parseFloat(this.receiveQty),
          batchNo: this.batchNo,
          productionDate: this.productionDate,
          expiryDate: this.expiryDate
        })

        uni.showToast({ title: '收货成功', icon: 'success' })

        // 清空并刷新
        this.scanCode = ''
        this.currentItem = null
        this.receiveQty = ''
        this.batchNo = ''
        this.loadOrderDetail()
      } catch (e) {
        uni.showToast({ title: '收货失败', icon: 'none' })
      }
    },

    onProductionDateChange(e) {
      this.productionDate = e.detail.value
    },

    onExpiryDateChange(e) {
      this.expiryDate = e.detail.value
    }
  }
}
</script>

<style lang="scss" scoped>
.receive-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
}

.order-info {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;

  .info-row {
    display: flex;
    margin-bottom: 10rpx;

    .info-label {
      font-size: 26rpx;
      color: #999;
      width: 140rpx;
    }

    .info-value {
      font-size: 28rpx;
      color: #333;
      flex: 1;
    }
  }
}

.scan-section {
  margin-bottom: 20rpx;
}

.product-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.form-section {
  padding-top: 20rpx;

  .form-item {
    display: flex;
    flex-direction: column;
    margin-bottom: 20rpx;

    .form-label {
      font-size: 26rpx;
      color: #666;
      margin-bottom: 10rpx;
    }

    .form-value {
      font-size: 32rpx;
      color: #333;
      font-weight: 600;
    }

    .form-input {
      height: 44px;
      border: 2rpx solid #e8e8e8;
      border-radius: 8rpx;
      padding: 0 15rpx;
      font-size: 28rpx;
    }

    .form-picker {
      height: 44px;
      line-height: 44px;
      border: 2rpx solid #e8e8e8;
      border-radius: 8rpx;
      padding: 0 15rpx;
      font-size: 28rpx;
      color: #333;
    }
  }
}

.action-section {
  margin-top: 30rpx;

  .submit-btn {
    width: 100%;
    height: 48px;
    background-color: #1890ff;
    color: #fff;
    font-size: 32rpx;
    border: none;
    border-radius: 8rpx;

    &::after {
      border: none;
    }
  }
}

.receive-list {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;

  .list-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 15rpx;
  }

  .list-item {
    padding: 15rpx 0;
    border-bottom: 1rpx solid #f0f0f0;

    .list-row {
      display: flex;
      justify-content: space-between;

      .list-label {
        font-size: 28rpx;
        color: #333;
      }

      .list-value {
        font-size: 28rpx;
        color: #1890ff;
      }
    }
  }
}
</style>
```

- [ ] **Step 2: Commit**

```bash
cd RuoYi-App-master
git add pages/wms/inbound/receive.vue
git commit -m "feat(wms): 添加收货作业页"
```

---

## Task 11: 开发质检作业页

**Files:**
- Create: `RuoYi-App-master/pages/wms/inbound/quality.vue`

- [ ] **Step 1: 创建质检作业页面**

```vue
<!-- pages/wms/inbound/quality.vue -->
<template>
  <view class="quality-page">
    <!-- 入库单信息 -->
    <view class="order-info">
      <view class="info-row">
        <text class="info-label">入库单号:</text>
        <text class="info-value">{{ orderInfo.inboundNo }}</text>
      </view>
    </view>

    <!-- 待质检物料列表 -->
    <view class="material-list">
      <view class="list-title">待质检物料</view>

      <view v-for="item in pendingList" :key="item.itemId"
        class="material-item"
        :class="{ active: selectedId === item.itemId }"
        @click="onSelectItem(item)"
      >
        <view class="material-header">
          <text class="material-name">{{ item.productName }}</text>
          <text class="material-code">{{ item.productCode }}</text>
        </view>
        <view class="material-body">
          <text class="material-info">应收：{{ item.expectedQty }}</text>
          <text class="material-info">已收：{{ item.receivedQty }}</text>
        </view>
      </view>
    </view>

    <!-- 质检操作区 -->
    <view v-if="selectedItem" class="quality-section">
      <wms-product-card :product="selectedItem" :show-batch="true" :show-qty="true" />

      <view class="form-section">
        <view class="form-item">
          <text class="form-label">合格数量</text>
          <input
            v-model="qualifiedQty"
            type="number"
            class="form-input"
            placeholder="请输入合格数量"
          />
        </view>
        <view class="form-item">
          <text class="form-label">不合格数量</text>
          <input
            v-model="unqualifiedQty"
            type="number"
            class="form-input"
            placeholder="请输入不合格数量"
          />
        </view>
        <view class="form-total">
          <text>合计：{{ totalQty }} / 应收：{{ selectedItem.expectedQty }}</text>
        </view>
      </view>

      <view class="action-section">
        <button class="submit-btn" @click="onSubmit">确认质检</button>
      </view>
    </view>
  </view>
</template>

<script>
import { getInbound, qualityCheck } from '@/api/wms/inbound'
import WmsProductCard from '@/components/wms/ProductCard.vue'

export default {
  components: { WmsProductCard },
  data() {
    return {
      inboundId: '',
      orderInfo: {},
      pendingList: [],
      selectedId: '',
      selectedItem: null,
      qualifiedQty: '',
      unqualifiedQty: ''
    }
  },
  computed: {
    totalQty() {
      const q = parseFloat(this.qualifiedQty) || 0
      const u = parseFloat(this.unqualifiedQty) || 0
      return q + u
    }
  },
  onLoad(options) {
    if (options.inboundId) {
      this.inboundId = options.inboundId
      this.loadOrderDetail()
    }
  },
  methods: {
    // 加载订单详情
    async loadOrderDetail() {
      try {
        const res = await getInbound(this.inboundId)
        this.orderInfo = res
        // 筛选已收货但未质检的物料
        this.pendingList = (res.items || []).filter(i =>
          i.receivedQty > 0 && i.status < '3'
        )
      } catch (e) {
        uni.showToast({ title: '加载失败', icon: 'none' })
      }
    },

    // 选择物料
    onSelectItem(item) {
      this.selectedId = item.itemId
      this.selectedItem = { ...item, qty: item.receivedQty }
      this.qualifiedQty = String(item.receivedQty)
      this.unqualifiedQty = '0'
    },

    // 提交质检
    async onSubmit() {
      if (!this.qualifiedQty || !this.unqualifiedQty) {
        uni.showToast({ title: '请输入质检数量', icon: 'none' })
        return
      }

      if (this.totalQty !== this.selectedItem.receivedQty) {
        uni.showToast({
          title: '合格 + 不合格数量不等于已收数量',
          icon: 'none'
        })
        return
      }

      try {
        await qualityCheck(
          this.inboundId,
          this.selectedItem.itemId,
          parseFloat(this.qualifiedQty),
          parseFloat(this.unqualifiedQty)
        )

        uni.showToast({ title: '质检成功', icon: 'success' })

        // 清空并刷新
        this.selectedId = ''
        this.selectedItem = null
        this.qualifiedQty = ''
        this.unqualifiedQty = ''
        this.loadOrderDetail()
      } catch (e) {
        uni.showToast({ title: '质检失败', icon: 'none' })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.quality-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
}

.order-info {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.material-list {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;

  .list-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 15rpx;
  }

  .material-item {
    padding: 20rpx;
    border: 2rpx solid #e8e8e8;
    border-radius: 8rpx;
    margin-bottom: 15rpx;

    &.active {
      border-color: #1890ff;
      background-color: #e6f7ff;
    }

    .material-header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10rpx;

      .material-name {
        font-size: 30rpx;
        font-weight: 600;
        color: #333;
        flex: 1;
      }

      .material-code {
        font-size: 24rpx;
        color: #999;
      }
    }

    .material-body {
      display: flex;
      gap: 30rpx;

      .material-info {
        font-size: 26rpx;
        color: #666;
      }
    }
  }
}

.quality-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
}

.form-section {
  padding-top: 20rpx;

  .form-item {
    margin-bottom: 20rpx;

    .form-label {
      display: block;
      font-size: 26rpx;
      color: #666;
      margin-bottom: 10rpx;
    }

    .form-input {
      width: 100%;
      height: 44px;
      border: 2rpx solid #e8e8e8;
      border-radius: 8rpx;
      padding: 0 15rpx;
      font-size: 28rpx;
    }
  }

  .form-total {
    padding: 15rpx;
    background-color: #f5f5f5;
    border-radius: 8rpx;
    text-align: center;
    font-size: 28rpx;
    color: #666;
  }
}

.action-section {
  margin-top: 30rpx;

  .submit-btn {
    width: 100%;
    height: 48px;
    background-color: #52c41a;
    color: #fff;
    font-size: 32rpx;
    border: none;
    border-radius: 8rpx;

    &::after {
      border: none;
    }
  }
}
</style>
```

- [ ] **Step 2: Commit**

```bash
cd RuoYi-App-master
git add pages/wms/inbound/quality.vue
git commit -m "feat(wms): 添加质检作业页"
```

---

## Task 12: 开发上架作业页

**Files:**
- Create: `RuoYi-App-master/pages/wms/inbound/putaway.vue`

- [ ] **Step 1: 创建上架作业页面**

```vue
<!-- pages/wms/inbound/putaway.vue -->
<template>
  <view class="putaway-page">
    <!-- 入库单信息 -->
    <view class="order-info">
      <view class="info-row">
        <text class="info-label">入库单号:</text>
        <text class="info-value">{{ orderInfo.inboundNo }}</text>
      </view>
    </view>

    <!-- 待上架物料列表 -->
    <view class="material-list">
      <view class="list-title">待上架物料</view>

      <view v-for="item in pendingList" :key="item.itemId"
        class="material-item"
        :class="{ active: selectedId === item.itemId }"
        @click="onSelectItem(item)"
      >
        <view class="material-header">
          <text class="material-name">{{ item.productName }}</text>
          <text class="material-code">{{ item.productCode }}</text>
        </view>
        <view class="material-body">
          <text class="material-info">合格：{{ item.qualifiedQty }}</text>
          <text class="material-info">已上架：{{ item.putawayQty || 0 }}</text>
        </view>
      </view>
    </view>

    <!-- 上架操作区 -->
    <view v-if="selectedItem" class="putaway-section">
      <wms-product-card :product="selectedItem" :show-batch="true" :show-qty="true" />

      <view class="scan-section">
        <wms-scan-input
          v-model="locationCode"
          label="扫库位"
          placeholder="扫描目标库位条码"
          @submit="onLocationScan"
        />
      </view>

      <view v-if="selectedLocation" class="location-info">
        <text class="location-name">{{ selectedLocation.locationName }}</text>
        <text class="location-code">{{ selectedLocation.locationCode }}</text>
      </view>

      <view class="form-section">
        <view class="form-item">
          <text class="form-label">上架数量</text>
          <input
            v-model="putawayQty"
            type="number"
            class="form-input"
            placeholder="请输入上架数量"
          />
        </view>
        <view class="form-total">
          <text>待上架：{{ toPutawayQty }}</text>
        </view>
      </view>

      <view class="action-section">
        <button class="submit-btn" @click="onSubmit">确认上架</button>
      </view>
    </view>
  </view>
</template>

<script>
import { getInbound, putAway } from '@/api/wms/inbound'
import WmsScanInput from '@/components/wms/ScanInput.vue'
import WmsProductCard from '@/components/wms/ProductCard.vue'

export default {
  components: { WmsScanInput, WmsProductCard },
  data() {
    return {
      inboundId: '',
      orderInfo: {},
      pendingList: [],
      selectedId: '',
      selectedItem: null,
      locationCode: '',
      selectedLocation: null,
      putawayQty: ''
    }
  },
  computed: {
    toPutawayQty() {
      if (!this.selectedItem) return 0
      const qualified = parseFloat(this.selectedItem.qualifiedQty) || 0
      const putaway = parseFloat(this.selectedItem.putawayQty) || 0
      return qualified - putaway
    }
  },
  onLoad(options) {
    if (options.inboundId) {
      this.inboundId = options.inboundId
      this.loadOrderDetail()
    }
  },
  methods: {
    // 加载订单详情
    async loadOrderDetail() {
      try {
        const res = await getInbound(this.inboundId)
        this.orderInfo = res
        // 筛选已质检但未上架的物料
        this.pendingList = (res.items || []).filter(i =>
          i.qualifiedQty > 0 && (!i.putawayQty || i.putawayQty < i.qualifiedQty)
        )
      } catch (e) {
        uni.showToast({ title: '加载失败', icon: 'none' })
      }
    },

    // 选择物料
    onSelectItem(item) {
      this.selectedId = item.itemId
      this.selectedItem = { ...item, qty: item.qualifiedQty - (item.putawayQty || 0) }
      this.putawayQty = String(item.qualifiedQty - (item.putawayQty || 0))
    },

    // 库位扫码
    onLocationScan(code) {
      // 这里应该调用库位 API 获取库位详情
      // 简化处理，直接使用扫码结果
      this.selectedLocation = {
        locationCode: code,
        locationName: code
      }
    },

    // 提交上架
    async onSubmit() {
      if (!this.locationCode) {
        uni.showToast({ title: '请扫描库位', icon: 'none' })
        return
      }

      if (!this.putawayQty) {
        uni.showToast({ title: '请输入上架数量', icon: 'none' })
        return
      }

      try {
        await putAway(
          this.inboundId,
          this.selectedItem.itemId,
          this.selectedLocation.locationId || 0
        )

        uni.showToast({ title: '上架成功', icon: 'success' })

        // 清空并刷新
        this.selectedId = ''
        this.selectedItem = null
        this.locationCode = ''
        this.selectedLocation = null
        this.putawayQty = ''
        this.loadOrderDetail()
      } catch (e) {
        uni.showToast({ title: '上架失败', icon: 'none' })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.putaway-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
}

.order-info {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.material-list {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;

  .list-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 15rpx;
  }

  .material-item {
    padding: 20rpx;
    border: 2rpx solid #e8e8e8;
    border-radius: 8rpx;
    margin-bottom: 15rpx;

    &.active {
      border-color: #1890ff;
      background-color: #e6f7ff;
    }

    .material-header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10rpx;
    }

    .material-body {
      display: flex;
      gap: 30rpx;

      .material-info {
        font-size: 26rpx;
        color: #666;
      }
    }
  }
}

.putaway-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
}

.scan-section {
  margin-bottom: 20rpx;
}

.location-info {
  display: flex;
  justify-content: space-between;
  padding: 20rpx;
  background-color: #f5f5f5;
  border-radius: 8rpx;
  margin-bottom: 20rpx;

  .location-name, .location-code {
    font-size: 28rpx;
    color: #333;
  }
}

.form-section {
  padding-top: 20rpx;

  .form-item {
    margin-bottom: 20rpx;

    .form-label {
      display: block;
      font-size: 26rpx;
      color: #666;
      margin-bottom: 10rpx;
    }

    .form-input {
      width: 100%;
      height: 44px;
      border: 2rpx solid #e8e8e8;
      border-radius: 8rpx;
      padding: 0 15rpx;
      font-size: 28rpx;
    }
  }

  .form-total {
    padding: 15rpx;
    background-color: #f5f5f5;
    border-radius: 8rpx;
    text-align: center;
    font-size: 28rpx;
    color: #666;
  }
}

.action-section {
  margin-top: 30rpx;

  .submit-btn {
    width: 100%;
    height: 48px;
    background-color: #13c2c2;
    color: #fff;
    font-size: 32rpx;
    border: none;
    border-radius: 8rpx;

    &::after {
      border: none;
    }
  }
}
</style>
```

- [ ] **Step 2: Commit**

```bash
cd RuoYi-App-master
git add pages/wms/inbound/putaway.vue
git commit -m "feat(wms): 添加上架作业页"
```

---

## Task 13: 清理未使用的导入语句

**Files:**
- Modify: `RuoYi-App-master/pages/wms/inbound/receive.vue`
- Modify: `RuoYi-App-master/pages/wms/inbound/quality.vue`
- Modify: `RuoYi-App-master/pages/wms/inbound/putaway.vue`

- [ ] **Step 1: 检查并清理未使用的组件导入**

确保所有页面的组件导入是正确的，删除未使用的导入

- [ ] **Step 2: Commit**

```bash
cd RuoYi-App-master
git add pages/wms/
git commit -m "chore(wms): 清理未使用的导入"
```

---

## Task 14: 验证编译

**Files:**
- All created files

- [ ] **Step 1: 运行 uni-app 编译检查**

```bash
cd RuoYi-App-master
# 如果使用 HBuilderX，打开项目后点击编译
# 或使用 CLI
npm run dev:h5
```

- [ ] **Step 2: 检查编译错误**

确保没有语法错误和导入错误

- [ ] **Step 3: 提交最终代码**

```bash
cd RuoYi-App-master
git add .
git commit -m "feat(wms): 阶段一开发完成 - 基础框架和入库模块"
```

---

## 完成检查清单

开发完成后，请确认以下内容：
- [ ] 所有组件文件创建成功
- [ ] 所有页面文件创建成功
- [ ] pages.json 路由配置正确
- [ ] 代码编译无错误
- [ ] Git 提交记录清晰

---

## 下一步

阶段一完成后，可以继续进行：
- 阶段二：出库模块（拣货、复核、发货）
- 阶段三：盘点 + 移库模块
- 阶段四：查询功能
