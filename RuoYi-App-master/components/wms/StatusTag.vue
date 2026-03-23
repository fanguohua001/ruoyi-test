<!-- components/wms/StatusTag.vue -->
<template>
  <view class="status-tag" :class="`status-tag--${type}`">
    <text>{{ text }}</text>
  </view>
</template>

<script>
// 入库单状态映射
const INBOUND_STATUS_MAP = {
  '1': { text: '待收货', type: 'warning' },
  '2': { text: '收货中', type: 'info' },
  '3': { text: '待质检', type: 'info' },
  '4': { text: '待上架', type: 'info' },
  '5': { text: '已入库', type: 'success' }
}

// 出库单状态映射
const OUTBOUND_STATUS_MAP = {
  '1': { text: '待审核', type: 'warning' },
  '2': { text: '待拣货', type: 'warning' },
  '3': { text: '拣货中', type: 'info' },
  '4': { text: '待复核', type: 'info' },
  '5': { text: '待打包', type: 'info' },
  '6': { text: '待发货', type: 'info' },
  '7': { text: '已发货', type: 'success' },
  '8': { text: '已取消', type: 'error' }
}

export default {
  name: 'StatusTag',
  props: {
    status: {
      type: [String, Number],
      required: true
    },
    type: {
      type: String,
      default: '' // warning, info, success, error
    },
    statusMap: {
      type: Object,
      default: null
    }
  },
  computed: {
    config() {
      const map = this.statusMap || INBOUND_STATUS_MAP
      return map[this.status] || { text: this.status, type: this.type || 'default' }
    },
    text() {
      return this.config.text
    },
    tagType() {
      return this.config.type
    }
  }
}
</script>

<style lang="scss" scoped>
.status-tag {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 6rpx 16rpx;
  border-radius: 4rpx;
  font-size: 24rpx;
  line-height: 1.5;

  &--warning {
    background-color: #fff7e6;
    color: #faad14;
  }

  &--info {
    background-color: #e6f7ff;
    color: #1890ff;
  }

  &--success {
    background-color: #f6ffed;
    color: #52c41a;
  }

  &--error {
    background-color: #fff1f0;
    color: #f5222d;
  }

  &--default {
    background-color: #f5f5f5;
    color: #666;
  }
}
</style>
