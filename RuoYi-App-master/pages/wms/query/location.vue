<!-- pages/wms/query/location.vue -->
<template>
  <view class="location-query">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <view class="search-bar__input">
        <uni-icons type="search" size="20" color="#999"></uni-icons>
        <input
          v-model="searchKey"
          placeholder="请输入库位编码"
          @confirm="onSearch"
        />
      </view>
      <button class="search-bar__btn" @click="onSearch">搜索</button>
    </view>

    <!-- 状态筛选 -->
    <view class="filter-tabs">
      <view
        v-for="item in statusOptions"
        :key="item.value"
        class="filter-tab"
        :class="{ active: currentStatus === item.value }"
        @click="onStatusChange(item.value)"
      >
        {{ item.label }}
      </view>
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
          :key="item.locationId"
          class="list-item"
          @click="onItemClick(item)"
        >
          <view class="item-header">
            <view class="header-left">
              <text class="item-name">{{ item.locationName }}</text>
              <text class="item-code">{{ item.locationCode }}</text>
            </view>
            <view class="status-tag" :class="`status-${item.status}`">
              {{ item.status === '1' ? '空闲' : '占用' }}
            </view>
          </view>
          <view class="item-body">
            <view class="item-row">
              <text class="item-label">仓库:</text>
              <text class="item-value">{{ item.warehouseName || '-' }}</text>
            </view>
            <view class="item-row">
              <text class="item-label">库区:</text>
              <text class="item-value">{{ item.zoneName || '-' }}</text>
            </view>
            <view class="item-row">
              <text class="item-label">容量:</text>
              <text class="item-value">{{ item.capacity || '-' }}</text>
            </view>
            <view class="item-row" v-if="item.status === '2'">
              <text class="item-label">存放物料:</text>
              <text class="item-value">{{ item.productName || '-' }}</text>
            </view>
            <view class="item-row" v-if="item.status === '2'">
              <text class="item-label">库存数量:</text>
              <text class="item-value item-qty">{{ item.currentQty || 0 }}</text>
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
import { listLocation } from '@/api/wms/location'

export default {
  data() {
    return {
      searchKey: '',
      currentStatus: '',
      list: [],
      page: 1,
      pageSize: 10,
      total: 0,
      refreshing: false,
      hasMore: true,
      statusOptions: [
        { label: '全部', value: '' },
        { label: '空闲', value: '1' },
        { label: '占用', value: '2' }
      ]
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
        const res = await listLocation({
          page: this.page,
          limit: this.pageSize,
          status: this.currentStatus,
          searchKey: this.searchKey
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

    // 状态筛选
    onStatusChange(value) {
      this.currentStatus = value
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
      uni.showToast({ title: item.locationName, icon: 'none' })
    }
  }
}
</script>

<style lang="scss" scoped>
.location-query {
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

.filter-tabs {
  display: flex;
  background-color: #fff;
  padding: 15rpx;
  gap: 15rpx;
  overflow-x: auto;

  .filter-tab {
    padding: 8rpx 20rpx;
    font-size: 26rpx;
    color: #666;
    background-color: #f5f5f5;
    border-radius: 30rpx;
    white-space: nowrap;

    &.active {
      color: #fff;
      background-color: #1890ff;
    }
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

    .header-left {
      flex: 1;
      display: flex;
      flex-direction: column;

      .item-name {
        font-size: 30rpx;
        font-weight: 600;
        color: #333;
      }

      .item-code {
        font-size: 24rpx;
        color: #999;
        margin-top: 5rpx;
      }
    }

    .status-tag {
      padding: 6rpx 16rpx;
      border-radius: 4rpx;
      font-size: 24rpx;

      &.status-1 {
        background-color: #f6ffed;
        color: #52c41a;
      }

      &.status-2 {
        background-color: #e6f7ff;
        color: #1890ff;
      }
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
