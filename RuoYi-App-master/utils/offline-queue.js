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
