<!-- pages/wms/outbound/review.vue -->
<template>
  <view class="review-page">
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

    <!-- 待复核物料列表 -->
    <view class="material-list">
      <view class="list-title">待复核物料</view>

      <view v-for="item in reviewList" :key="item.itemId"
        class="material-item"
        :class="{ active: selectedId === item.itemId }"
        @click="onSelectItem(item)"
      >
        <view class="material-header">
          <text class="material-name">{{ item.productName }}</text>
          <text class="material-code">{{ item.productCode }}</text>
        </view>
        <view class="material-body">
          <text class="material-info">应复核：{{ item.pickedQty }}</text>
          <text class="material-info">已复核：{{ item.reviewedQty || 0 }}</text>
        </view>
      </view>
    </view>

    <!-- 复核操作区 -->
    <view v-if="selectedItem" class="review-section">
      <wms-product-card :product="selectedItem" :show-batch="true" :show-qty="true" />

      <view class="scan-section">
        <wms-scan-input
          v-model="scanCode"
          label="扫物料"
          placeholder="扫描物料条码复核"
          @submit="onProductScan"
        />
      </view>

      <view v-if="scanMatch" class="scan-verified">
        <uni-icons type="checkmarkcircle" size="20" color="#52c41a"></uni-icons>
        <text>扫码匹配成功</text>
      </view>

      <view class="form-section">
        <view class="form-item">
          <text class="form-label">复核数量</text>
          <input
            v-model="reviewQty"
            type="number"
            class="form-input"
            placeholder="请输入复核数量"
          />
        </view>
        <view class="form-diff" v-if="diffQty !== 0">
          <text :class="diffQty < 0 ? 'diff-negative' : 'diff-positive'">
            {{ diffQty > 0 ? '+' : '' }}{{ diffQty }}
          </text>
        </view>
      </view>

      <view class="action-section">
        <button class="submit-btn" @click="onSubmit">确认复核</button>
      </view>
    </view>

    <!-- 完成复核按钮 -->
    <view v-if="reviewList.length > 0 && allReviewed" class="finish-section">
      <button class="finish-btn" @click="onFinishReview">完成复核</button>
    </view>
  </view>
</template>

<script>
import { getOutbound, reviewOutbound } from '@/api/wms/outbound'
import WmsScanInput from '@/components/wms/ScanInput.vue'
import WmsProductCard from '@/components/wms/ProductCard.vue'

export default {
  components: { WmsScanInput, WmsProductCard },
  data() {
    return {
      outboundId: '',
      orderInfo: {},
      reviewList: [],
      selectedId: '',
      selectedItem: null,
      scanCode: '',
      scanMatch: false,
      reviewQty: ''
    }
  },
  computed: {
    diffQty() {
      if (!this.selectedItem || !this.reviewQty) return 0
      return parseFloat(this.reviewQty) - parseFloat(this.selectedItem.pickedQty)
    },
    allReviewed() {
      return this.reviewList.every(item => item.reviewedQty >= item.pickedQty)
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
        // 筛选已拣货但未复核或未完全复核的物料
        this.reviewList = (res.items || []).filter(i =>
          i.pickedQty > 0 && (!i.reviewedQty || i.reviewedQty < i.pickedQty)
        )
      } catch (e) {
        uni.showToast({ title: '加载失败', icon: 'none' })
      }
    },

    // 选择物料
    onSelectItem(item) {
      this.selectedId = item.itemId
      this.selectedItem = { ...item, qty: item.pickedQty - (item.reviewedQty || 0) }
      this.reviewQty = String(item.pickedQty - (item.reviewedQty || 0))
      this.scanMatch = false
      this.scanCode = ''
    },

    // 物料扫码
    onProductScan(code) {
      // 验证物料是否匹配
      if (this.selectedItem.productCode === code) {
        this.scanMatch = true
        uni.showToast({ title: '物料匹配成功', icon: 'success' })
      } else {
        uni.showToast({ title: '物料不匹配', icon: 'none' })
      }
    },

    // 提交复核
    async onSubmit() {
      if (!this.reviewQty) {
        uni.showToast({ title: '请输入复核数量', icon: 'none' })
        return
      }

      try {
        // 调用复核 API
        await reviewOutbound(this.outboundId, this.selectedItem.itemId, parseFloat(this.reviewQty))

        uni.showToast({ title: '复核成功', icon: 'success' })

        // 清空并刷新
        this.selectedId = ''
        this.selectedItem = null
        this.scanCode = ''
        this.reviewQty = ''
        this.scanMatch = false
        this.loadOrderDetail()
      } catch (e) {
        uni.showToast({ title: '复核失败', icon: 'none' })
      }
    },

    // 完成复核
    async onFinishReview() {
      try {
        await reviewOutbound(this.outboundId)
        uni.showToast({ title: '复核完成', icon: 'success' })
        // 返回列表或上一页
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (e) {
        uni.showToast({ title: '操作失败', icon: 'none' })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.review-page {
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

.review-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.scan-section {
  margin-bottom: 20rpx;
}

.scan-verified {
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

  .form-diff {
    padding: 15rpx;
    border-radius: 8rpx;
    text-align: center;
    font-size: 32rpx;
    font-weight: 600;

    .diff-positive {
      color: #faad14;
    }

    .diff-negative {
      color: #f5222d;
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

.finish-section {
  margin-top: 20rpx;

  .finish-btn {
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
