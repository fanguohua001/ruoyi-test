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
