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
