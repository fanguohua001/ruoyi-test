<!-- pages/wms/transfer/execute.vue -->
<template>
  <view class="execute-page">
    <!-- 移库单信息 -->
    <view class="transfer-info">
      <view class="info-row">
        <text class="info-label">移库单号:</text>
        <text class="info-value">{{ transferInfo.transferNo }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">状态:</text>
        <text class="info-value">{{ statusText }}</text>
      </view>
    </view>

    <!-- 移库信息卡片 -->
    <view class="transfer-detail">
      <view class="detail-item">
        <view class="detail-header">
          <uni-icons type="location" size="20" color="#1890ff"></uni-icons>
          <text class="detail-title">源库位</text>
        </view>
        <text class="detail-code">{{ transferInfo.fromLocationCode || '-' }}</text>
        <text class="detail-name">{{ transferInfo.fromLocationName || '-' }}</text>
      </view>

      <view class="transfer-arrow">
        <uni-icons type="arrowright" size="30" color="#1890ff"></uni-icons>
      </view>

      <view class="detail-item">
        <view class="detail-header">
          <uni-icons type="location" size="20" color="#52c41a"></uni-icons>
          <text class="detail-title">目标库位</text>
        </view>
        <text class="detail-code">{{ transferInfo.toLocationCode || '-' }}</text>
        <text class="detail-name">{{ transferInfo.toLocationName || '-' }}</text>
      </view>
    </view>

    <!-- 物料信息 -->
    <view class="product-section">
      <view class="section-title">移库物料</view>
      <wms-product-card :product="productInfo" :show-batch="true" :show-qty="true" />
    </view>

    <!-- 扫码确认区 -->
    <view class="scan-section">
      <wms-scan-input
        v-model="fromLocationScan"
        label="扫源库位"
        placeholder="扫描源库位确认"
        @submit="onFromLocationScan"
      />

      <view v-if="fromLocationVerified" class="verified-tip">
        <uni-icons type="checkmarkcircle" size="20" color="#52c41a"></uni-icons>
        <text>源库位验证成功</text>
      </view>

      <wms-scan-input
        v-model="toLocationScan"
        label="扫目标库位"
        placeholder="扫描目标库位确认"
        @submit="onToLocationScan"
      />

      <view v-if="toLocationVerified" class="verified-tip">
        <uni-icons type="checkmarkcircle" size="20" color="#52c41a"></uni-icons>
        <text>目标库位验证成功</text>
      </view>
    </view>

    <!-- 移库数量 -->
    <view class="form-section">
      <view class="form-item">
        <text class="form-label">移库数量</text>
        <input
          v-model="transferQty"
          type="number"
          class="form-input"
          placeholder="请输入移库数量"
        />
      </view>
      <view class="form-total">
        <text>可用数量：{{ availableQty }}</text>
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="action-section">
      <button class="submit-btn" @click="onSubmit">确认移库</button>
    </view>
  </view>
</template>

<script>
import { getTransfer, executeTransfer } from '@/api/wms/transfer'
import WmsScanInput from '@/components/wms/ScanInput.vue'
import WmsProductCard from '@/components/wms/ProductCard.vue'

// 移库单状态映射
const statusMap = {
  '1': '待执行',
  '2': '执行中',
  '3': '已完成',
  '4': '已取消'
}

export default {
  components: { WmsScanInput, WmsProductCard },
  data() {
    return {
      transferId: '',
      transferInfo: {},
      productInfo: {},
      fromLocationScan: '',
      toLocationScan: '',
      fromLocationVerified: false,
      toLocationVerified: false,
      transferQty: '',
      availableQty: 0
    }
  },
  computed: {
    statusText() {
      return statusMap[this.transferInfo.status] || '-'
    }
  },
  onLoad(options) {
    if (options.transferId) {
      this.transferId = options.transferId
      this.loadTransferDetail()
    }
  },
  methods: {
    // 加载移库单详情
    async loadTransferDetail() {
      try {
        const res = await getTransfer(this.transferId)
        this.transferInfo = res
        this.productInfo = {
          productName: res.productName,
          productCode: res.productCode,
          specification: res.specification,
          unit: res.unit,
          batchNo: res.batchNo,
          qty: res.transferQty
        }
        this.availableQty = res.transferQty || 0
        this.transferQty = String(this.availableQty)
      } catch (e) {
        uni.showToast({ title: '加载失败', icon: 'none' })
      }
    },

    // 源库位扫码
    onFromLocationScan(code) {
      if (code === this.transferInfo.fromLocationCode) {
        this.fromLocationVerified = true
        uni.showToast({ title: '源库位验证成功', icon: 'success' })
      } else {
        uni.showToast({ title: '源库位不匹配', icon: 'none' })
      }
    },

    // 目标库位扫码
    onToLocationScan(code) {
      if (code === this.transferInfo.toLocationCode) {
        this.toLocationVerified = true
        uni.showToast({ title: '目标库位验证成功', icon: 'success' })
      } else {
        uni.showToast({ title: '目标库位不匹配', icon: 'none' })
      }
    },

    // 提交移库
    async onSubmit() {
      if (!this.fromLocationVerified || !this.toLocationVerified) {
        uni.showToast({ title: '请先验证源库位和目标库位', icon: 'none' })
        return
      }

      if (!this.transferQty) {
        uni.showToast({ title: '请输入移库数量', icon: 'none' })
        return
      }

      try {
        await executeTransfer(this.transferId, parseFloat(this.transferQty))

        uni.showToast({ title: '移库成功', icon: 'success' })

        // 清空并刷新
        this.fromLocationScan = ''
        this.toLocationScan = ''
        this.fromLocationVerified = false
        this.toLocationVerified = false
        this.transferQty = ''
        this.loadTransferDetail()
      } catch (e) {
        uni.showToast({ title: '移库失败', icon: 'none' })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.execute-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
}

.transfer-info {
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

.transfer-detail {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;

  .detail-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20rpx;
    background-color: #f5f5f5;
    border-radius: 8rpx;

    .detail-header {
      display: flex;
      align-items: center;
      gap: 10rpx;
      margin-bottom: 15rpx;

      .detail-title {
        font-size: 28rpx;
        color: #333;
        font-weight: 600;
      }
    }

    .detail-code {
      font-size: 32rpx;
      font-weight: 600;
      color: #1890ff;
      margin-bottom: 5rpx;
    }

    .detail-name {
      font-size: 24rpx;
      color: #666;
    }
  }

  .transfer-arrow {
    padding: 0 20rpx;
  }
}

.product-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;

  .section-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 15rpx;
  }
}

.scan-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;

  .verified-tip {
    display: flex;
    align-items: center;
    gap: 10rpx;
    padding: 15rpx;
    background-color: #f6ffed;
    border-radius: 8rpx;
    margin-bottom: 15rpx;
    color: #52c41a;
    font-size: 28rpx;
  }
}

.form-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;

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
  margin-top: 20rpx;

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
