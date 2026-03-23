<!-- pages/wms/stockCheck/execute.vue -->
<template>
  <view class="execute-page">
    <!-- 盘点单信息 -->
    <view class="check-info">
      <view class="info-row">
        <text class="info-label">盘点单号:</text>
        <text class="info-value">{{ checkInfo.checkNo }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">盘点类型:</text>
        <text class="info-value">{{ checkInfo.checkType === '1' ? '定期盘点' : '临时盘点' }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">盘点进度:</text>
        <text class="info-value">{{ checkedCount }}/{{ totalCount }}</text>
      </view>
    </view>

    <!-- 待盘点物料列表 -->
    <view class="material-list">
      <view class="list-title">待盘点物料</view>

      <view v-for="item in materialList" :key="item.itemId"
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
            <text class="location-text">{{ item.locationCode || '-' }}</text>
          </view>
          <text class="material-info">系统数量：{{ item.systemQty }}</text>
          <text class="material-info" v-if="item.checkedQty">已盘：{{ item.checkedQty }}</text>
        </view>
      </view>
    </view>

    <!-- 盘点操作区 -->
    <view v-if="selectedItem" class="execute-section">
      <wms-product-card :product="selectedItem" :show-batch="true" />

      <view class="scan-section">
        <wms-scan-input
          v-model="scanCode"
          label="扫物料"
          placeholder="扫描物料条码确认"
          @submit="onProductScan"
        />
      </view>

      <view v-if="scanMatch" class="scan-verified">
        <uni-icons type="checkmarkcircle" size="20" color="#52c41a"></uni-icons>
        <text>物料验证成功</text>
      </view>

      <view class="form-section">
        <view class="form-item">
          <text class="form-label">系统数量</text>
          <text class="form-value">{{ selectedItem.systemQty }}</text>
        </view>
        <view class="form-item">
          <text class="form-label">实盘数量</text>
          <input
            v-model="actualQty"
            type="number"
            class="form-input"
            placeholder="请输入实盘数量"
          />
        </view>
        <view class="form-diff" v-if="diffQty !== undefined">
          <text>差异：</text>
          <text :class="diffQty < 0 ? 'diff-negative' : 'diff-positive'">
            {{ diffQty > 0 ? '+' : '' }}{{ diffQty }}
          </text>
        </view>
      </view>

      <view class="action-section">
        <button class="submit-btn" @click="onSubmit">提交盘点</button>
      </view>
    </view>

    <!-- 完成盘点按钮 -->
    <view v-if="allChecked" class="finish-section">
      <button class="finish-btn" @click="onFinishCheck">完成盘点</button>
    </view>
  </view>
</template>

<script>
import { getStockCheck, recordStockCheck } from '@/api/wms/stockCheck'
import WmsScanInput from '@/components/wms/ScanInput.vue'
import WmsProductCard from '@/components/wms/ProductCard.vue'

export default {
  components: { WmsScanInput, WmsProductCard },
  data() {
    return {
      checkId: '',
      checkInfo: {},
      materialList: [],
      selectedId: '',
      selectedItem: null,
      scanCode: '',
      scanMatch: false,
      actualQty: ''
    }
  },
  computed: {
    diffQty() {
      if (!this.selectedItem || this.actualQty === '') return undefined
      return parseFloat(this.actualQty) - parseFloat(this.selectedItem.systemQty)
    },
    checkedCount() {
      return this.materialList.filter(item => item.checkedQty !== undefined).length
    },
    totalCount() {
      return this.materialList.length
    },
    allChecked() {
      return this.totalCount > 0 && this.checkedCount === this.totalCount
    }
  },
  onLoad(options) {
    if (options.checkId) {
      this.checkId = options.checkId
      this.loadCheckDetail()
    }
  },
  methods: {
    // 加载盘点单详情
    async loadCheckDetail() {
      try {
        const res = await getStockCheck(this.checkId)
        this.checkInfo = res
        this.materialList = res.items || []
      } catch (e) {
        uni.showToast({ title: '加载失败', icon: 'none' })
      }
    },

    // 选择物料
    onSelectItem(item) {
      this.selectedId = item.itemId
      this.selectedItem = { ...item }
      this.actualQty = String(item.systemQty)
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

    // 提交盘点
    async onSubmit() {
      if (!this.actualQty) {
        uni.showToast({ title: '请输入实盘数量', icon: 'none' })
        return
      }

      try {
        await recordStockCheck(this.checkId, this.selectedItem.itemId, parseFloat(this.actualQty))

        uni.showToast({ title: '盘点成功', icon: 'success' })

        // 清空并刷新
        this.selectedId = ''
        this.selectedItem = null
        this.scanCode = ''
        this.actualQty = ''
        this.scanMatch = false
        this.loadCheckDetail()
      } catch (e) {
        uni.showToast({ title: '盘点失败', icon: 'none' })
      }
    },

    // 完成盘点
    async onFinishCheck() {
      try {
        await recordStockCheck(this.checkId)
        uni.showToast({ title: '盘点完成', icon: 'success' })
        // 返回列表
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
.execute-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
}

.check-info {
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
      border-color: #fa8c16;
      background-color: #fff7e6;
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

.execute-section {
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

    .form-value {
      font-size: 32rpx;
      color: #333;
      font-weight: 600;
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
    display: flex;
    align-items: center;
    gap: 10rpx;
    padding: 15rpx;
    background-color: #f5f5f5;
    border-radius: 8rpx;
    font-size: 28rpx;

    .diff-positive {
      color: #52c41a;
      font-weight: 600;
    }

    .diff-negative {
      color: #f5222d;
      font-weight: 600;
    }
  }
}

.action-section {
  margin-top: 30rpx;

  .submit-btn {
    width: 100%;
    height: 48px;
    background-color: #fa8c16;
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
