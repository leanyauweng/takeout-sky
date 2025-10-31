<template>
    <div class="mobile-frame">
      <div class="content-area">
        <!-- ✅ 这里的 router-view 会加载子页面（home、cart、orders、profile） -->
        <router-view />
      </div>
  
      <!-- ✅ 底部导航栏 -->
      <nav class="tab-bar">
        <router-link to="/user/home" class="tab-item">🏡</router-link>
        <router-link to="/user/menu" class="tab-item">🧾</router-link>
        <router-link to="/user/cart" class="tab-item">🛒
        <span v-if="cartStore.cartCount > 0" class="badge">{{ cartStore.cartCount }}</span>
      </router-link>
        <router-link to="/user/orders" class="tab-item">🚚</router-link>
        <router-link to="/user/profile" class="tab-item">🧑‍🍳</router-link>
      </nav>
    </div>
  </template>
  
  <script setup>
import { ref, onMounted } from 'vue'
import { useCartStore } from '@/api/cart'

const cartStore = useCartStore()

onMounted(() => {
  cartStore.fetchCartCount() // 刷新页面时自动获取购物车数量
})

</script>

  <style scoped>
  .mobile-frame {
    width: 375px;
    height: 812px;
    border: 10px solid #000;
    border-radius: 30px;
    margin: 20px auto;
    overflow: hidden;
    background: #fff;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
    display: flex;
    flex-direction: column;
    position: relative;
  }
  
  .content-area {
    flex: 1;
    overflow-y: auto;
    padding-bottom: 60px;
  }
  
  .tab-bar {
  height: 60px;
  background: rgba(251, 187, 74, 0.6); /* 半透明橙色 */
  backdrop-filter: blur(10px); /* 模糊背景 */
  -webkit-backdrop-filter: blur(10px); /* 兼容 iOS Safari */
  border-top: 1px solid rgba(255, 255, 255, 0.3);
  display: flex;
  justify-content: space-around;
  align-items: center;
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
}

  
  .tab-item {
    text-decoration: none;
    font-size: 24px;
    color: #555;
  }
  
  .tab-item.router-link-active {
    color: #409eff;
  }

  .tab-item {
  position: relative;
  font-size: 24px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.badge {
  position: absolute;
  top: -6px;        /* 调整垂直位置 */
  right: -10px;     /* 调整水平位置 */
  background-color: #ff4d4f; /* 红色 */
  color: #fff;
  font-size: 12px;  /* 字号 */
  font-weight: bold;
  width: 18px;      /* 宽度 */
  height: 18px;     /* 高度 */
  line-height: 18px;/* 文字垂直居中 */
  text-align: center;
  border-radius: 50%; /* 圆形 */
  box-shadow: 0 0 2px rgba(0,0,0,0.3); /* 阴影让数字更立体 */
  pointer-events: none; /* 防止覆盖点击 */
  transition: all 0.2s ease;
}

  </style>
  