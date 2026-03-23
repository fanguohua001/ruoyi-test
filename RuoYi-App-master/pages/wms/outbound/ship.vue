<!-- pages/wms/outbound/ship.vue -->
<template>
  <view class="ship-page">
    <!-- 出库单信息 -->
    <view class="order-info">
      <view class="info-row">
        <text class="info-label">出库单号:</text>
        <text class="info-value">{{ orderInfo.outboundNo }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">客户:</text>
        <text class="info-value">{{ orderInfo.customerName }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">状态:</text>
        <text class="info-value">{{ statusText }}</text>
      </view>
    </view>

    <!-- 复核完成提示 -->
    <view v-if="!canShip" class="tip-section">
      <uni-icons type="info" size="24" color="#faad14"></uni-icons>
      <text class="tip-text">请先完成复核操作</text>
    </view>

    <!-- 发货操作区 -->
    <view v-if="canShip" class="ship-section">
      <view class="package-list">
        <view class="list-title">包裹信息</view>

        <view class="package-item">
          <view class="package-row">
            <text class="package-label">包裹数量</text>
            <input
              v-model="packageCount"
              type="number"
              class="package-input"
              placeholder="请输入包裹数量"
            />
          </view>
          <view class="package-row">
            <text class="package-label">物流单号</text>
            <input
              v-model="trackingNo"
              class="package-input"
              placeholder="请输入物流单号（可选）"
            />
          </view>
          <view class="package-row">
            <text class="package-label">物流公司</text>
            <picker mode="selector" :range="logisticsCompanies" @change="onLogisticsChange">
              <view class="package-picker">{{ selectedLogistics || '选择物流公司' }}</view>
            </picker>
          </view>
        </view>
      </view>

      <view class="action-section">
        <button class="ship-btn" @click="onShip">确认发货</button>
      </view>
    </view>

    <!-- 已发货状态 -->
    <view v-if="isShipped" class="shipped-status">
      <uni-icons type="checkmarkcircle" size="40" color="#52c41a"></uni-icons>
      <text class="shipped-text">已发货</text>
      <text class="shipped-date">发货时间：{{ shipTime }}</text>
    </view>
  </view>
</template>

<script>
import { getOutbound, shipOutbound } from '@/api/wms/outbound'

// 出库单状态映射
const statusMap = {
  '1': '待审核',
  '2': '待拣货',
  '3': '拣货中',
  '4': '待复核',
  '5': '待打包',
  '6': '待发货',
  '7': '已发货',
  '8': '已取消'
}

export default {
  data() {
    return {
      outboundId: '',
      orderInfo: {},
      packageCount: '1',
      trackingNo: '',
      logisticsCompanies: ['顺丰速运', '中通快递', '圆通速递', '申通快递', '韵达快递', '邮政 EMS', '其他'],
      selectedLogistics: '',
      logisticsIndex: 0
    }
  },
  computed: {
    statusText() {
      return statusMap[this.orderInfo.status] || '-'
    },
    canShip() {
      // 待打包或待发货状态可以发货
      return this.orderInfo.status === '5' || this.orderInfo.status === '6'
    },
    isShipped() {
      return this.orderInfo.status === '7'
    },
    shipTime() {
      return this.orderInfo.shipTime || '-'
    }
  },
  onLoad(options) {
    if (options.outboundId) {
      this.outboundId = options.outboundId
      this.loadOrderDetail()
    }
  },
  methods: {
    // 加载订单详情
    async loadOrderDetail() {
      try {
        const res = await getOutbound(this.outboundId)
        this.orderInfo = res
      } catch (e) {
        uni.showToast({ title: '加载失败', icon: 'none' })
      }
    },

    // 选择物流公司
    onLogisticsChange(e) {
      this.logisticsIndex = e.detail.value
      this.selectedLogistics = this.logisticsCompanies[this.logisticsIndex]
    },

    // 提交发货
    async onShip() {
      if (!this.packageCount) {
        uni.showToast({ title: '请输入包裹数量', icon: 'none' })
        return
      }

      try {
        await shipOutbound(this.outboundId, {
          packageCount: parseInt(this.packageCount),
          trackingNo: this.trackingNo,
          logisticsCompany: this.selectedLogistics
        })

        uni.showToast({ title: '发货成功', icon: 'success' })

        // 刷新详情
        this.loadOrderDetail()
      } catch (e) {
        uni.showToast({ title: '发货失败', icon: 'none' })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.ship-page {
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

.tip-section {
  display: flex;
  align-items: center;
  gap: 15rpx;
  padding: 30rpx;
  background-color: #fff7e6;
  border-radius: 12rpx;
  margin-bottom: 20rpx;

  .tip-text {
    font-size: 28rpx;
    color: #faad14;
  }
}

.ship-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.package-list {
  .list-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 15rpx;
  }
}

.package-item {
  .package-row {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;

    .package-label {
      font-size: 26rpx;
      color: #666;
      width: 160rpx;
    }

    .package-input {
      flex: 1;
      height: 44px;
      border: 2rpx solid #e8e8e8;
      border-radius: 8rpx;
      padding: 0 15rpx;
      font-size: 28rpx;
    }

    .package-picker {
      flex: 1;
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

  .ship-btn {
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

.shipped-status {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 20rpx;
  background-color: #fff;
  border-radius: 12rpx;

  .shipped-text {
    font-size: 36rpx;
    font-weight: 600;
    color: #52c41a;
    margin-top: 20rpx;
  }

  .shipped-date {
    font-size: 26rpx;
    color: #999;
    margin-top: 10rpx;
  }
}
</style>
