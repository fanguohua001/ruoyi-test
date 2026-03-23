<template>
  <view class="work-container">
    <!-- 轮播图 -->
    <uni-swiper-dot class="uni-swiper-dot-box" :info="data" :current="current" field="content">
      <swiper class="swiper-box" :current="swiperDotIndex" @change="changeSwiper">
        <swiper-item v-for="(item, index) in data" :key="index">
          <view class="swiper-item" @click="clickBannerItem(item)">
            <image :src="item.image" mode="aspectFill" :draggable="false" />
          </view>
        </swiper-item>
      </swiper>
    </uni-swiper-dot>

    <!-- 宫格组件 -->
    <uni-section title="系统管理" type="line"></uni-section>
    <view class="grid-body">
      <uni-grid :column="4" :showBorder="false" @change="changeSystemGrid">
        <uni-grid-item>
          <view class="grid-item-box">
            <uni-icons type="person-filled" size="30"></uni-icons>
            <text class="text">用户管理</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box">
            <uni-icons type="staff-filled" size="30"></uni-icons>
            <text class="text">角色管理</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box">
            <uni-icons type="color" size="30"></uni-icons>
            <text class="text">菜单管理</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box">
            <uni-icons type="settings-filled" size="30"></uni-icons>
            <text class="text">部门管理</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box">
            <uni-icons type="heart-filled" size="30"></uni-icons>
            <text class="text">岗位管理</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box">
            <uni-icons type="bars" size="30"></uni-icons>
            <text class="text">字典管理</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box">
            <uni-icons type="gear-filled" size="30"></uni-icons>
            <text class="text">参数设置</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box">
            <uni-icons type="chat-filled" size="30"></uni-icons>
            <text class="text">通知公告</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="grid-item-box">
            <uni-icons type="wallet-filled" size="30"></uni-icons>
            <text class="text">日志管理</text>
          </view>
        </uni-grid-item>
      </uni-grid>
    </view>


      
    <!-- WMS 仓储 -->
    <uni-section title="WMS 仓储" type="line"></uni-section>
    <view class="wms-home">
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
  </view>
</template>

<script>
  export default {
    data() {
      return {
        current: 0,
        swiperDotIndex: 0,
        data: [{
            image: '/static/images/banner/banner01.jpg'
          },
          {
            image: '/static/images/banner/banner02.jpg'
          },
          {
            image: '/static/images/banner/banner03.jpg'
          }
        ]
      }
    },
    methods: {
      clickBannerItem(item) {
        console.info(item)
      },
      changeSwiper(e) {
        this.current = e.detail.current
      },
      changeSystemGrid(e) {
        this.$modal.showToast('模块建设中~')
      },
      navigateTo(url) {
        uni.navigateTo({ url })
      }
    }
  }
</script>

<style lang="scss" scoped>
  /* #ifndef APP-NVUE */
  page {
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    background-color: #fff;
    min-height: 100%;
    height: auto;
  }

  view {
    font-size: 14px;
    line-height: inherit;
  }
  /* #endif */

  .text {
    text-align: center;
    font-size: 26rpx;
    margin-top: 10rpx;
  }

  .grid-item-box {
    flex: 1;
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 15px 0;
  }

  .uni-margin-wrap {
    width: 690rpx;
    width: 100%;
    ;
  }

  .swiper {
    height: 300rpx;
  }

  .swiper-box {
    height: 150px;
  }

  .swiper-item {
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: #fff;
    height: 300rpx;
    line-height: 300rpx;
  }

  @media screen and (min-width: 500px) {
    .uni-swiper-dot-box {
      width: 400px;
      /* #ifndef APP-NVUE */
      margin: 0 auto;
      /* #endif */
      margin-top: 8px;
    }

    .image {
      width: 100%;
    }
  }

  /* WMS 新功能样式 */
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
