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
