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
