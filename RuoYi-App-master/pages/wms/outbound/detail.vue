<!-- pages/wms/outbound/detail.vue -->
<template>
  <view class="outbound-detail">
    <!-- 头部信息 -->
    <view class="detail-header">
      <view class="header-row">
        <text class="header-label">出库单号:</text>
        <text class="header-value">{{ detail.outboundNo || '-' }}</text>
      </view>
      <view class="header-row">
        <text class="header-label">客户:</text>
        <text class="header-value">{{ detail.customerName || '-' }}</text>
      </view>
      <view class="header-row">
        <text class="header-label">状态:</text>
        <wms-status-tag :status="detail.status" :status-map="outboundStatusMap" />
      </view>
    </view>

    <!-- 明细列表 -->
    <view class="detail-section">
      <text class="section-title">物料明细</text>
      <scroll-view class="items-scroll" scroll-y>
        <view v-if="items.length === 0" class="empty-tip">
          <text>暂无明细数据</text>
        </view>
        <view v-else class="items-list">
          <view
            v-for="(item, index) in items"
            :key="item.itemId"
            class="item-card"
          >
            <view class="item-header">
              <text class="item-index">{{ index + 1 }}</text>
              <text class="item-name">{{ item.productName }}</text>
            </view>
            <view class="item-body">
              <view class="item-row">
                <text class="item-label">物料编码:</text>
                <text class="item-value">{{ item.productCode }}</text>
              </view>
              <view class="item-row">
                <text class="item-label">单位:</text>
                <text class="item-value">{{ item.unit || '-' }}</text>
              </view>
              <view class="item-row">
                <text class="item-label">订单数量:</text>
                <text class="item-value">{{ item.orderQty || 0 }}</text>
              </view>
              <view class="item-row">
                <text class="item-label">已拣货:</text>
                <text class="item-value">{{ item.pickedQty || 0 }}</text>
              </view>
              <view class="item-row">
                <text class="item-label">已发货:</text>
                <text class="item-value">{{ item.shippedQty || 0 }}</text>
              </view>
              <view class="item-row" v-if="item.pickingStrategy">
                <text class="item-label">拣货策略:</text>
                <text class="item-value">{{ getPickingStrategyText(item.pickingStrategy) }}</text>
              </view>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import { getOutbound } from '@/api/wms/outbound'
import WmsStatusTag from '@/components/wms/StatusTag.vue'

// 出库单状态映射
const outboundStatusMap = {
  '1': { text: '待审核', type: 'warning' },
  '2': { text: '待拣货', type: 'warning' },
  '3': { text: '拣货中', type: 'info' },
  '4': { text: '待复核', type: 'info' },
  '5': { text: '待打包', type: 'info' },
  '6': { text: '待发货', type: 'info' },
  '7': { text: '已发货', type: 'success' },
  '8': { text: '已取消', type: 'error' }
}

// 拣货策略映射
const pickingStrategyMap = {
  '1': 'FIFO (先进先出)',
  '2': 'LIFO (后进先出)',
  '3': '近效期优先'
}

export default {
  components: { WmsStatusTag },
  data() {
    return {
      outboundId: '',
      detail: {},
      items: [],
      loading: false,
      outboundStatusMap
    }
  },
  onLoad(options) {
    if (options.outboundId) {
      this.outboundId = options.outboundId
      this.loadDetail()
    }
  },
  methods: {
    async loadDetail() {
      this.loading = true
      try {
        const res = await getOutbound(this.outboundId)
        this.detail = res || {}
        this.items = res.items || []
      } catch (e) {
        console.error('加载失败:', e)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    getPickingStrategyText(strategy) {
      return pickingStrategyMap[strategy] || strategy || '-'
    }
  }
}
</script>

<style lang="scss" scoped>
.outbound-detail {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f5f5;
}

.detail-header {
  background-color: #fff;
  padding: 20rpx;
  margin-bottom: 15rpx;

  .header-row {
    display: flex;
    align-items: center;
    padding: 10rpx 0;

    .header-label {
      font-size: 26rpx;
      color: #999;
      width: 140rpx;
    }

    .header-value {
      font-size: 28rpx;
      color: #333;
      flex: 1;
    }
  }
}

.detail-section {
  flex: 1;
  background-color: #fff;
  margin: 15rpx;
  border-radius: 12rpx;
  padding: 20rpx;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .section-title {
    display: block;
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 15rpx;
  }
}

.items-scroll {
  flex: 1;
  overflow-y: auto;
}

.empty-tip {
  text-align: center;
  padding: 100rpx 0;
  color: #999;
  font-size: 28rpx;
}

.items-list {
  display: flex;
  flex-direction: column;
  gap: 15rpx;
}

.item-card {
  background-color: #f9f9f9;
  border-radius: 8rpx;
  padding: 15rpx;
  border: 1rpx solid #eee;
}

.item-header {
  display: flex;
  align-items: center;
  padding-bottom: 10rpx;
  border-bottom: 1rpx solid #eee;
  margin-bottom: 10rpx;

  .item-index {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 40rpx;
    height: 40rpx;
    background-color: #722ed1;
    color: #fff;
    border-radius: 50%;
    font-size: 24rpx;
    margin-right: 10rpx;
  }

  .item-name {
    font-size: 28rpx;
    font-weight: 600;
    color: #333;
    flex: 1;
  }
}

.item-body {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.item-row {
  display: flex;
  align-items: center;

  .item-label {
    font-size: 24rpx;
    color: #999;
    width: 160rpx;
  }

  .item-value {
    font-size: 26rpx;
    color: #333;
    flex: 1;
  }
}
</style>
