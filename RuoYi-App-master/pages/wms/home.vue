<!-- pages/wms/home.vue -->
<template>
  <view class="wms-home">
    <view class="home-header">
      <text class="home-title">WMS 工作台</text>
      <text class="home-subtitle">移动仓储管理</text>
    </view>

    <view class="home-section">
      <text class="section-title">入库作业</text>
      <view class="home-grid">
        <view class="home-item" @click="navigateTo('/pages/wms/inbound/list')">
          <view class="home-item__icon home-item__icon--inbound">
            <uni-icons type="download" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">入库单列表</text>
        </view>
        <view class="home-item" @click="navigateTo('/pages/wms/inbound/receive')">
          <view class="home-item__icon home-item__icon--receive">
            <uni-icons type="cart" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">收货</text>
        </view>
        <view class="home-item" @click="navigateTo('/pages/wms/inbound/quality')">
          <view class="home-item__icon home-item__icon--quality">
            <uni-icons type="checkmarkempty" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">质检</text>
        </view>
        <view class="home-item" @click="navigateTo('/pages/wms/inbound/putaway')">
          <view class="home-item__icon home-item__icon--putaway">
            <uni-icons type="upload" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">上架</text>
        </view>
      </view>
    </view>

    <view class="home-section">
      <text class="section-title">出库作业</text>
      <view class="home-grid">
        <view class="home-item" @click="navigateTo('/pages/wms/outbound/list')">
          <view class="home-item__icon home-item__icon--outbound">
            <uni-icons type="shop" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">出库单列表</text>
        </view>
        <view class="home-item" @click="navigateTo('/pages/wms/outbound/picking')">
          <view class="home-item__icon home-item__icon--picking">
            <uni-icons type="gear" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">拣货</text>
        </view>
        <view class="home-item" @click="navigateTo('/pages/wms/outbound/review')">
          <view class="home-item__icon home-item__icon--review">
            <uni-icons type="eye" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">复核</text>
        </view>
        <view class="home-item" @click="navigateTo('/pages/wms/outbound/ship')">
          <view class="home-item__icon home-item__icon--ship">
            <uni-icons type="paperplane" size="30" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">发货</text>
        </view>
      </view>
    </view>

    <view class="home-section">
      <text class="section-title">其他功能</text>
      <view class="home-grid home-grid--small">
        <view class="home-item home-item--small" @click="navigateTo('/pages/wms/stockCheck/list')">
          <view class="home-item__icon home-item__icon--check">
            <uni-icons type="compose" size="24" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">盘点</text>
        </view>
        <view class="home-item home-item--small" @click="navigateTo('/pages/wms/transfer/list')">
          <view class="home-item__icon home-item__icon--transfer">
            <uni-icons type="swap" size="24" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">移库</text>
        </view>
        <view class="home-item home-item--small" @click="navigateTo('/pages/wms/query/inventory')">
          <view class="home-item__icon home-item__icon--query">
            <uni-icons type="search" size="24" color="#fff"></uni-icons>
          </view>
          <text class="home-item__text">查询</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    navigateTo(url) {
      uni.navigateTo({ url })
    }
  }
}
</script>

<style lang="scss" scoped>
.wms-home {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
}

.home-header {
  padding: 30rpx 20rpx;
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  border-radius: 12rpx;
  margin-bottom: 20rpx;

  .home-title {
    display: block;
    font-size: 36rpx;
    font-weight: 600;
    color: #fff;
  }

  .home-subtitle {
    display: block;
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.8);
    margin-top: 10rpx;
  }
}

.home-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;

  .section-title {
    display: block;
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 20rpx;
  }
}

.home-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;

  &--small {
    grid-template-columns: repeat(3, 1fr);
  }
}

.home-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 10rpx;

  &--small {
    padding: 15rpx 10rpx;
  }

  &__icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 12rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 10rpx;

    &--inbound { background-color: #1890ff; }
    &--receive { background-color: #52c41a; }
    &--quality { background-color: #faad14; }
    &--putaway { background-color: #13c2c2; }
    &--outbound { background-color: #722ed1; }
    &--picking { background-color: #eb2f96; }
    &--review { background-color: #1890ff; }
    &--ship { background-color: #52c41a; }
    &--check { background-color: #fa8c16; }
    &--transfer { background-color: #13c2c2; }
    &--query { background-color: #8c8c8c; }
  }

  &__text {
    font-size: 24rpx;
    color: #666;
    text-align: center;
  }
}

.home-tip {
  text-align: center;
  color: #999;
  font-size: 24rpx;
  margin-top: 15rpx;
}
</style>
