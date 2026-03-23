<!-- components/wms/ProductCard.vue -->
<template>
  <view class="product-card">
    <view class="product-card__header">
      <text class="product-card__name">{{ product.productName }}</text>
      <text class="product-card__code">{{ product.productCode }}</text>
    </view>

    <view class="product-card__body">
      <view class="product-card__row">
        <text class="product-card__label">规格型号:</text>
        <text class="product-card__value">{{ product.specification || '-' }}</text>
      </view>
      <view class="product-card__row">
        <text class="product-card__label">单位:</text>
        <text class="product-card__value">{{ product.unit || '-' }}</text>
      </view>
      <view class="product-card__row" v-if="showBatch">
        <text class="product-card__label">批次号:</text>
        <text class="product-card__value">{{ product.batchNo || '-' }}</text>
      </view>
      <view class="product-card__row" v-if="showQty">
        <text class="product-card__label">数量:</text>
        <text class="product-card__value product-card__qty">{{ product.qty }}</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'ProductCard',
  props: {
    product: {
      type: Object,
      required: true
    },
    showBatch: {
      type: Boolean,
      default: false
    },
    showQty: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style lang="scss" scoped>
.product-card {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.08);

  &__header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 15rpx;
    border-bottom: 1rpx solid #f0f0f0;
  }

  &__name {
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
    flex: 1;
  }

  &__code {
    font-size: 24rpx;
    color: #999;
    margin-left: 20rpx;
  }

  &__body {
    padding-top: 15rpx;
  }

  &__row {
    display: flex;
    align-items: center;
    margin-bottom: 10rpx;
  }

  &__label {
    font-size: 26rpx;
    color: #666;
    width: 140rpx;
  }

  &__value {
    font-size: 28rpx;
    color: #333;
    flex: 1;
  }

  &__qty {
    font-size: 32rpx;
    font-weight: 600;
    color: #1890ff;
  }
}
</style>
