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
