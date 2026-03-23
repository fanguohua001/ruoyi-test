<!-- pages/wms/query/product.vue -->
<template>
  <view class="product-query">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <view class="search-bar__input">
        <uni-icons type="search" size="20" color="#999"></uni-icons>
        <input
          v-model="searchKey"
          placeholder="请输入物料编码/名称"
          @confirm="onSearch"
        />
      </view>
      <button class="search-bar__btn" @click="onSearch">搜索</button>
    </view>

    <!-- 仓库筛选 -->
    <view class="warehouse-filter">
      <picker mode="selector" :range="warehouseOptions" @change="onWarehouseChange">
        <view class="warehouse-picker">
          <text>{{ selectedWarehouse || '选择仓库' }}</text>
          <uni-icons type="arrowdown" size="14" color="#999"></uni-icons>
        </view>
      </picker>
    </view>

    <!-- 列表内容 -->
    <scroll-view
      class="list-scroll"
      scroll-y
      refresher-enabled
      :refresher-triggered="refreshing"
      @refresherrefresh="onRefresh"
      @scrolltolower="loadMore"
    >
      <view v-if="list.length === 0" class="empty-tip">
        <text>暂无数据</text>
      </view>

      <view v-else class="list-container">
        <view
          v-for="item in list"
          :key="item.productId"
          class="list-item"
          @click="onItemClick(item)"
        >
          <view class="item-header">
            <text class="item-name">{{ item.productName }}</text>
            <text class="item-code">{{ item.productCode }}</text>
          </view>
          <view class="item-body">
            <view class="item-row">
              <text class="item-label">规格型号:</text>
              <text class="item-value">{{ item.specification || '-' }}</text>
            </view>
            <view class="item-row">
              <text class="item-label">单位:</text>
              <text class="item-value">{{ item.unit || '-' }}</text>
            </view>
            <view class="item-row">
              <text class="item-label">类别:</text>
              <text class="item-value">{{ item.categoryName || '-' }}</text>
            </view>
            <view class="item-row">
              <text class="item-label">库存数量:</text>
              <text class="item-value item-qty">{{ item.totalQty || 0 }}</text>
            </view>
            <view class="item-row">
              <text class="item-label">可用数量:</text>
              <text class="item-value" :class="item.availableQty > 0 ? 'text-green' : 'text-red'">{{ item.availableQty || 0 }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 加载更多 -->
      <view class="load-more" v-if="hasMore">
        <text>加载更多...</text>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { listProduct } from '@/api/wms/product'

export default {
  data() {
    return {
      searchKey: '',
      warehouseId: '',
      list: [],
      page: 1,
      pageSize: 10,
      total: 0,
      refreshing: false,
      hasMore: true,
      warehouseOptions: ['全部仓库', '北京仓', '上海仓', '广州仓'],
      selectedWarehouse: '全部仓库'
    }
  },
  onLoad() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData(isRefresh = false) {
      if (isRefresh) {
        this.refreshing = true
        this.page = 1
        this.list = []
      }

      try {
        const res = await listProduct({
          page: this.page,
          limit: this.pageSize,
          searchKey: this.searchKey,
          warehouseId: this.warehouseId
        })

        const newList = res.rows || res.list || []
        this.total = res.total || newList.length

        if (isRefresh) {
          this.list = newList
        } else {
          this.list = [...this.list, ...newList]
        }

        this.hasMore = this.list.length < this.total
        this.page++
      } catch (e) {
        console.error('加载失败:', e)
      } finally {
        this.refreshing = false
      }
    },

    // 搜索
    onSearch() {
      this.loadData(true)
    },

    // 仓库选择
    onWarehouseChange(e) {
      this.selectedWarehouse = this.warehouseOptions[e.detail.value]
      this.warehouseId = e.detail.value === 0 ? '' : String(e.detail.value)
      this.loadData(true)
    },

    // 下拉刷新
    onRefresh() {
      this.loadData(true)
    },

    // 加载更多
    loadMore() {
      if (this.hasMore && !this.refreshing) {
        this.loadData()
      }
    },

    // 点击条目
    onItemClick(item) {
      uni.showToast({ title: item.productName, icon: 'none' })
    }
  }
}
</script>

<style lang="scss" scoped>
.product-query {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f5f5;
}

.search-bar {
  display: flex;
  align-items: center;
  padding: 15rpx;
  background-color: #fff;
  gap: 15rpx;

  &__input {
    flex: 1;
    display: flex;
    align-items: center;
    background-color: #f5f5f5;
    border-radius: 8rpx;
    padding: 10rpx 15rpx;

    input {
      flex: 1;
      margin-left: 10rpx;
      font-size: 28rpx;
    }
  }

  &__btn {
    padding: 10rpx 25rpx;
    font-size: 28rpx;
    background-color: #1890ff;
    color: #fff;
    border: none;
    border-radius: 8rpx;

    &::after {
      border: none;
    }
  }
}

.warehouse-filter {
  padding: 15rpx;
  background-color: #fff;
  margin-bottom: 1rpx;

  .warehouse-picker {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15rpx 20rpx;
    background-color: #f5f5f5;
    border-radius: 8rpx;
    font-size: 28rpx;
    color: #333;
  }
}

.list-scroll {
  flex: 1;
  padding: 15rpx;
}

.empty-tip {
  text-align: center;
  padding: 100rpx 0;
  color: #999;
  font-size: 28rpx;
}

.list-container {
  display: flex;
  flex-direction: column;
  gap: 15rpx;
}

.list-item {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;

  .item-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 15rpx;
    border-bottom: 1rpx solid #f0f0f0;

    .item-name {
      font-size: 30rpx;
      font-weight: 600;
      color: #333;
      flex: 1;
    }

    .item-code {
      font-size: 24rpx;
      color: #999;
      margin-left: 20rpx;
    }
  }

  .item-body {
    padding-top: 15rpx;
  }

  .item-row {
    display: flex;
    align-items: center;
    margin-bottom: 10rpx;

    .item-label {
      font-size: 26rpx;
      color: #999;
      width: 140rpx;
    }

    .item-value {
      font-size: 28rpx;
      color: #333;
      flex: 1;

      &.item-qty {
        font-size: 32rpx;
        font-weight: 600;
        color: #1890ff;
      }

      &.text-green {
        color: #52c41a;
      }

      &.text-red {
        color: #f5222d;
      }
    }
  }
}

.load-more {
  text-align: center;
  padding: 20rpx;
  color: #999;
  font-size: 26rpx;
}
</style>
