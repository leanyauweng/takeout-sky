<template>
  <div class="home-page">
    <!-- 顶部栏 -->
    <header class="header">
  <div class="location">🌤️Takeout-sky(Home)</div>
    <div class="shop-status" :class="statusClass">
      {{ shopStatusText }}
    </div>
  </header>


    <!-- 轮播图 -->
    <div class="banner">
    <img :src="images[currentIndex]" alt="优惠活动" />
  </div>

    <!-- 分类区域 -->
    <div class="categories">
      <div class="category" v-for="item in categories" :key="item.id">
        <div class="icon">{{ item.icon }}</div>
        <div class="label">{{ item.name }}</div>
      </div>
    </div>

    <!-- 热销菜品 Top10 -->
    <div class="recommend">
  <h3 class="title">🔥 Best-selling dishes Top10</h3>
  <div class="setmeal-list">
    <div class="setmeal" v-for="meal in recommendList" :key="meal.id">
      <img :src="meal.image" alt="meal" />
      <div class="info">
        <!-- 第一行：菜名 + 价格 -->
        <div class="row">
          <span class="name">{{ meal.name }}</span>
          <span class="price">RM{{ meal.price }}</span>
        </div>
        <!-- 第二行：销量 + 加号 -->
        <div class="row">
          <span class="sales">{{ getSalesTag(meal.sales) }} {{ meal.sales }}</span>
          <button class="add-btn" @click="addToCart(meal)">+</button>
        </div>
      </div>
    </div>
  </div>
</div>

  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { getTop10DishesApi } from '@/api/dish'
import { addToCartApi, getShopStatusApi } from '@/api/shoppingCart'
import { useCartStore } from '@/api/cart'
import { ElMessage } from 'element-plus'

const cartStore = useCartStore()

const images = [
  '/src/assets/take.jpg',
  '/src/assets/take2.jpg',
  '/src/assets/take3.jpg'
]
const currentIndex = ref(0)
let intervalId = null

const shopStatus = ref(0) // 0 或 1
const shopStatusText = ref('Loading...')
const statusClass = ref('status-loading') // 可根据状态切换样式

const categories = ref([
  { id: 1, name: 'Set Meal', icon: '🍱' },
  { id: 2, name: 'Snacks', icon: '🍡' },
  { id: 3, name: 'Drinks', icon: '🍵' },
  { id: 4, name: 'Dessert', icon: '🍮' }
])

const recommendList = ref([])

// 获取 Top10 菜品
const fetchTop10 = async () => {
  try {
    const res = await getTop10DishesApi()
    if (res.code === 1) {
      recommendList.value = res.data
    } else {
      console.warn('Failed to get Top10 dishes:', res.msg)
    }
  } catch (e) {
    console.error('API call error:', e)
  }
}

// 计算销量标识
const getSalesTag = (sales) => {
  if (sales >= 50) return '🏆 Best Seller'
  if (sales >= 20) return '🥇 Hot Item'
  if (sales >= 10) return '🔥 Popular'
  if (sales > 0) return '⭐ Trending'
  return ''
}

// 获取店铺状态
const fetchShopStatus = async () => {
  try {
    const res = await getShopStatusApi()
    if (res.code === 1) {
      // ✅ 处理 Java 序列化对象，确保是数字
      shopStatus.value = Number(res.data?.fields?.[0]?.value ?? res.data ?? 0)
      shopStatusText.value = shopStatus.value === 1 ? 'Open' : 'Closed'
      statusClass.value = shopStatus.value === 1 ? 'status-open' : 'status-closed'
    }
  } catch (e) {
    console.error(e)
    shopStatusText.value = 'Failed to fetch status'
    statusClass.value = 'status-error'
  }
}

// 添加商品到购物车
const addToCart = async (meal) => {
  console.log('shopStatus.value', shopStatus.value)
  // ✅ 确保 shopStatus 是数字类型
  if (shopStatus.value !== 1) {
    ElMessage({
      message: 'The shop is currently closed. Unable to add items to the cart.',
      type: 'warning',
      duration: 2000
    })
    return
  }

  try {
    const data = { dishId: meal.id, number: 1 } // 根据后端接口字段调整
    const res = await addToCartApi(data)

    if (res.code === 1) {
      cartStore.increment(1)
      ElMessage({
        message: 'Added to cart successfully',
        type: 'success',
        duration: 1500
      })
    } else {
      ElMessage({
        message: 'Failed to add to cart: ' + (res.msg || 'Unknown error'),
        type: 'error',
        duration: 2000
      })
    }
  } catch (e) {
    console.error('Error adding to cart:', e)
    ElMessage({
      message: 'Failed to add to cart: ' + (e?.message || 'Unknown error'),
      type: 'error',
      duration: 2000
    })
  }
}

// 页面加载与卸载处理
onMounted(() => {
  intervalId = setInterval(() => {
    currentIndex.value = (currentIndex.value + 1) % images.length
  }, 3000) // 每 3 秒切换轮播图
  fetchTop10()
  fetchShopStatus()
})

onBeforeUnmount(() => {
  clearInterval(intervalId)
})
</script>


<style scoped>
html, body {
  height: 100%;
  margin: 0;
  overflow: hidden; /* 整个页面不滚动 */
}

.home-page {
  padding: 10px;
  background: url('@/assets/userlogin.jpg') no-repeat center/cover;
  background-size: cover; /* 保证背景铺满容器 */
  height: 97.4%;           /* 填满整个页面 */
  display: flex;
  flex-direction: column;
}



.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fbbb4a;
  color: white;
  padding: 10px;
  border-radius: 10px;
}

.shop-status {
  font-weight: bold;
  padding: 4px 10px;
  border-radius: 8px;
  color: white;
}

.status-open {
  background-color: #03742e; /* 绿色：营业中 */
}

.status-closed {
  background-color: #d9534f; /* 红色：打样中 */
}

.status-loading {
  background-color: #999; /* 灰色：加载中 */
}

.status-error {
  background-color: #ff9800; /* 橙色：获取失败 */
}


.location {
  font-family: "Poppins", "Nunito", sans-serif;
  font-size: 22px;
  font-weight: 700;
  color: #2c3e50;
  letter-spacing: 0.8px;
}




.search-input {
  border: none;
  border-radius: 8px;
  padding: 6px 10px;
  width: 55%;
}

.banner img {
  width: 100%;
  height: 200px;
  border-radius: 10px;
  margin: 10px 0;
}

.categories {
  display: flex;
  justify-content: space-around;
  text-align: center;
  margin-bottom: 10px;
}

.category {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.category .icon {
  font-size: 30px;
}

.recommend .title {
  font-size: 18px;
  margin: 10px 0;
}

.setmeal-list {
  max-height: 400px;            /* 固定高度，可滚动 */
  overflow-y: auto;
  display: grid;                /* 改成网格布局 */
  grid-template-columns: repeat(2, 1fr); /* 每行 2 个卡片 */
  gap: 10px;                    /* 卡片间距 */
  padding-right: 5px;           /* 避免滚动条覆盖内容 */
  padding-bottom: 5px;
}

/* 可选：自定义滚动条样式 */
.setmeal-list::-webkit-scrollbar {
  width: 6px;
}

.setmeal-list::-webkit-scrollbar-thumb {
  background-color: rgba(0,0,0,0.2);
  border-radius: 3px;
}

.setmeal-list::-webkit-scrollbar-track {
  background-color: rgba(0,0,0,0.05);
}

.setmeal:last-child {
  margin-bottom: 60px;  /* 最后一个卡片底部留空 */
}


.setmeal {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  overflow: hidden;
  text-align: center;
  min-height: 160px;       /* 改成最小高度，内容多时自动撑开 */
  display: flex;
  flex-direction: column;
  justify-content: flex-start; /* 改为顶部对齐，防止内容被压缩 */
}


.setmeal img {
  width: 100%;
  height:150px;               
  object-fit: cover;
}

.info {
  padding: 5px;
  display: flex;
  flex-direction: column;
  justify-content: center; /* 垂直居中，也可以改成 flex-start */
  gap: 2px;                /* 每行之间的间距 */
}

.price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 14px;
}

.sales {
  font-size: 12px;
  color: #666;
  margin-right: 8px;
  margin-top: 2px;
}

.add-btn {
  background-color: #03742e;
  color: white;
  border: none;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  font-size: 16px;
  cursor: pointer;
  margin-bottom: 4px;
}
</style>
