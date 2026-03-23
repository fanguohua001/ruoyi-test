<!-- pages/wms/outbound/picking.vue -->
<template>
  <view class="picking-page">
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
    </view>

    <!-- 待拣货物料列表 -->
    <view class="material-list">
      <view class="list-title">待拣货物料</view>

      <view v-for="item in pickingList" :key="item.itemId"
        class="material-item"
        :class="{ active: selectedId === item.itemId }"
        @click="onSelectItem(item)"
      >
        <view class="material-header">
          <text class="material-name">{{ item.productName }}</text>
          <text class="material-code">{{ item.productCode }}</text>
        </view>
        <view class="material-body">
          <view class="material-location">
            <uni-icons type="location" size="16" color="#1890ff"></uni-icons>
            <text class="location-text">{{ item.locationCode || '未分配库位' }}</text>
          </view>
          <text class="material-info">应拣：{{ item.requiredQty }}</text>
          <text class="material-info">已拣：{{ item.pickedQty || 0 }}</text>
        </view>
      </view>
    </view>

    <!-- 拣货操作区 -->
    <view v-if="selectedItem" class="picking-section">
      <wms-product-card :product="selectedItem" :show-batch="true" :show-qty="true" />

      <view class="scan-section">
        <wms-scan-input
          v-model="scanCode"
          label="扫库位"
          placeholder="扫描库位条码确认"
          @submit="onLocationScan"
        />
      </view>

      <view v-if="locationVerified" class="location-verified">
        <uni-icons type="checkmarkcircle" size="20" color="#52c41a"></uni-icons>
        <text>库位验证成功</text>
      </view>

      <view class="form-section">
        <view class="form-item">
          <text class="form-label">拣货数量</text>
          <input
            v-model="pickingQty"
            type="number"
            class="form-input"
            placeholder="请输入拣货数量"
          />
        </view>
        <view class="form-total">
          <text>待拣货：{{ toPickQty }}</text>
        </view>
      </view>

      <view class="action-section">
        <button class="submit-btn" @click="onSubmit">确认拣货</button>
      </view>
    </view>
  </view>
</template>

<script>
import { getOutbound, picking } from '@/api/wms/outbound'
import WmsScanInput from '@/components/wms/ScanInput.vue'
import WmsProductCard from '@/components/wms/ProductCard.vue'

export default {
  components: { WmsScanInput, WmsProductCard },
  data() {
    return {
      outboundId: '',
      orderInfo: {},
      pickingList: [],
      selectedId: '',
      selectedItem: null,
      scanCode: '',
      locationVerified: false,
      pickingQty: ''
    }
  },
  computed: {
    toPickQty() {
      if (!this.selectedItem) return 0
      const required = parseFloat(this.selectedItem.requiredQty) || 0
      const picked = parseFloat(this.selectedItem.pickedQty) || 0
      return required - picked
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
        // 筛选待拣货或拣货中的物料
        this.pickingList = (res.items || []).filter(i =>
          i.requiredQty > 0 && (!i.pickedQty || i.pickedQty < i.requiredQty)
        )
      } catch (e) {
        uni.showToast({ title: '加载失败', icon: 'none' })
      }
    },

    // 选择物料
    onSelectItem(item) {
      this.selectedId = item.itemId
      this.selectedItem = { ...item, qty: item.requiredQty - (item.pickedQty || 0) }
      this.pickingQty = String(item.requiredQty - (item.pickedQty || 0))
      this.locationVerified = false
      this.scanCode = ''
    },

    // 库位扫码
    onLocationScan(code) {
      // 验证库位是否匹配
      if (this.selectedItem.locationCode === code) {
        this.locationVerified = true
        uni.showToast({ title: '库位验证成功', icon: 'success' })
      } else {
        uni.showToast({ title: '库位不匹配', icon: 'none' })
      }
    },

    // 提交拣货
    async onSubmit() {
      if (!this.pickingQty) {
        uni.showToast({ title: '请输入拣货数量', icon: 'none' })
        return
      }

      try {
        await picking(
          this.outboundId,
          this.selectedItem.itemId,
          parseFloat(this.pickingQty)
        )

        uni.showToast({ title: '拣货成功', icon: 'success' })

        // 清空并刷新
        this.selectedId = ''
        this.selectedItem = null
        this.scanCode = ''
        this.pickingQty = ''
        this.locationVerified = false
        this.loadOrderDetail()
      } catch (e) {
        uni.showToast({ title: '拣货失败', icon: 'none' })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.picking-page {
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
      border-color: #eb2f96;
      background-color: #fff0f6;
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
      flex-wrap: wrap;
      gap: 20rpx;
      align-items: center;

      .material-location {
        display: flex;
        align-items: center;
        gap: 5rpx;
        background-color: #e6f7ff;
        padding: 5rpx 10rpx;
        border-radius: 4rpx;

        .location-text {
          font-size: 24rpx;
          color: #1890ff;
        }
      }

      .material-info {
        font-size: 24rpx;
        color: #666;
      }
    }
  }
}

.picking-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
}

.scan-section {
  margin-bottom: 20rpx;
}

.location-verified {
  display: flex;
  align-items: center;
  gap: 10rpx;
  padding: 15rpx;
  background-color: #f6ffed;
  border-radius: 8rpx;
  margin-bottom: 20rpx;
  color: #52c41a;
  font-size: 28rpx;
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
    background-color: #eb2f96;
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
