<!-- pages/wms/inbound/detail.vue -->
<template>
  <view class="inbound-detail">
    <!-- 头部信息 -->
    <view class="detail-header">
      <view class="header-row">
        <text class="header-label">入库单号:</text>
        <text class="header-value">{{ detail.inboundNo || '-' }}</text>
      </view>
      <view class="header-row">
        <text class="header-label">供应商:</text>
        <text class="header-value">{{ detail.supplierName || '-' }}</text>
      </view>
      <view class="header-row">
        <text class="header-label">状态:</text>
        <wms-status-tag :status="detail.status" />
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
                <text class="item-label">应收数量:</text>
                <text class="item-value">{{ item.expectedQty || 0 }}</text>
              </view>
              <view class="item-row">
                <text class="item-label">实收数量:</text>
                <text class="item-value">{{ item.receivedQty || 0 }}</text>
              </view>
              <view class="item-row">
                <text class="item-label">合格数量:</text>
                <text class="item-value">{{ item.qualifiedQty || 0 }}</text>
              </view>
              <view class="item-row">
                <text class="item-label">不合格数量:</text>
                <text class="item-value">{{ item.unqualifiedQty || 0 }}</text>
              </view>
              <view class="item-row" v-if="item.batchNo">
                <text class="item-label">批次号:</text>
                <text class="item-value">{{ item.batchNo }}</text>
              </view>
              <view class="item-row" v-if="item.productionDate">
                <text class="item-label">生产日期:</text>
                <text class="item-value">{{ formatDate(item.productionDate) }}</text>
              </view>
              <view class="item-row" v-if="item.expiryDate">
                <text class="item-label">有效期至:</text>
                <text class="item-value">{{ formatDate(item.expiryDate) }}</text>
              </view>
              <view class="item-row" v-if="item.locationId">
                <text class="item-label">库位:</text>
                <text class="item-value">{{ item.locationId }}</text>
              </view>
              <view class="item-row" v-if="item.status">
                <text class="item-label">明细状态:</text>
                <text class="item-value">{{ getItemStatusText(item.status) }}</text>
              </view>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import { getInbound } from '@/api/wms/inbound'
import WmsStatusTag from '@/components/wms/StatusTag.vue'

// 入库明细状态映射
const itemStatusMap = {
  '0': '待收货',
  '1': '已收货',
  '2': '待质检',
  '3': '已质检',
  '4': '已上架'
}

export default {
  components: { WmsStatusTag },
  data() {
    return {
      inboundId: '',
      detail: {},
      items: [],
      loading: false
    }
  },
  onLoad(options) {
    if (options.inboundId) {
      this.inboundId = options.inboundId
      this.loadDetail()
    }
  },
  methods: {
    async loadDetail() {
      this.loading = true
      try {
        const res = await getInbound(this.inboundId)
        console.log('API 返回:', res)
        this.detail = res.data || res || {}
        this.items = (res.data && res.data.items) || res.items || []
        console.log('明细数据:', this.items)
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
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      return d.toLocaleDateString('zh-CN')
    },
    getItemStatusText(status) {
      return itemStatusMap[status] || status || '-'
    }
  }
}
</script>

<style lang="scss" scoped>
.inbound-detail {
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
    background-color: #1890ff;
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
