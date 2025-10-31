<template>
  <div class="menu-page">
    <!-- 顶部标题 -->
    <header class="header">
      <div class="location">🧾Takeout-sky (Menu)</div>
      <div class="shop-status" :class="statusClass">
        {{ shopStatusText }}
      </div>
    </header>

    <div class="type-toggle">
      <button :class="{ active: type === 1 }" @click="switchType(1)">Dishes</button>
      <button :class="{ active: type === 2 }" @click="switchType(2)">Set Meals</button>
    </div>

    <div class="menu-content">
      <!-- 左侧分类 -->
      <div class="category-column">
        <button
          v-for="cat in categories"
          :key="cat.id"
          :class="{ active: selectedCategoryId === cat.id }"
          @click="selectCategory(cat.id)"
        >
          {{ cat.name }}
        </button>
      </div>

      <!-- 右侧菜品/套餐 -->
      <div class="dish-column">
        <div class="dish-list">
          <div v-for="item in dishList" :key="item.id" class="dish-item">
            <img :src="item.image" alt="dish" />
            <div class="info">
              <div class="info-row">
                <h4>{{ item.name }}</h4>
                <p class="price">RM{{ item.price }}</p>
              </div>
              <div class="quantity-btns">
                <button class="add-btn" @click="addToCart(item)">+</button>
                <span class="item-count">{{ getItemCount(item) }}</span>
                <button class="reduce-btn" @click="reduceItem(item)">-</button>
              </div>
            </div>
          </div>

          <!-- 无菜品时提示 -->
          <div v-if="dishList.length === 0" class="empty">No dishes available</div>
        </div>

        <!-- 清空购物车按钮 -->
        <div class="clear-cart-container">
          <button class="clear-cart-btn" @click="clearCart">Clear Cart</button>
        </div>
      </div>
    </div>
  </div>
</template>

  
<script setup>
import { ref, onMounted } from 'vue'
import { getCategoryListApi } from '@/api/category'  
import { getDishListByCategoryApi } from '@/api/dish'
import { getSetmealListByCategoryApi } from '@/api/setmeal'
import { useCartStore } from '@/api/cart'
import { addToCartApi, getShopStatusApi, cleanCartApi, subFromCartApi, getCartListApi } from '@/api/shoppingCart'
import { ElMessage } from 'element-plus'

const categories = ref([])
const selectedCategoryId = ref(null)
const dishList = ref([])
const cartStore = useCartStore()
const type = ref(1) // 默认 1 菜品分类，2 套餐分类
const shopStatus = ref(0) // 0 或 1
const shopStatusText = ref('Loading...')
const statusClass = ref('status-loading') 
const cartMap = ref({}) // 用 id -> 数量 映射

// 获取店铺状态
const fetchShopStatus = async () => {
  try {
    const res = await getShopStatusApi()
    if (res.code === 1) {
      shopStatus.value = res.data
      shopStatusText.value = shopStatus.value === 1 ? 'Open' : 'Closed'
      statusClass.value = shopStatus.value === 1 ? 'status-open' : 'status-closed'
    }
  } catch (e) {
    console.error('获取店铺状态失败', e)
    shopStatusText.value = 'Failed to load'
    statusClass.value = 'status-error'
  }
}

// 选择分类
const selectCategory = async (categoryId) => {
  selectedCategoryId.value = categoryId
  await loadDishes(categoryId)
}

// 切换菜品 / 套餐
const switchType = async (t) => {
  type.value = t
  // 重新加载分类和列表
  await loadCategories()
}

// 加载分类
const loadCategories = async () => {
  const res = await getCategoryListApi({ type: type.value })
  categories.value = res.data || []
  if (categories.value.length > 0) {
    selectCategory(categories.value[0].id)
  } else {
    dishList.value = []
  }
}

// 加载菜品或套餐
const loadDishes = async (categoryId) => {
  let res
  if (type.value === 2) {
    res = await getSetmealListByCategoryApi(categoryId)
  } else {
    res = await getDishListByCategoryApi(categoryId)
  }
  dishList.value = res.data
}

// 加载购物车数量
const loadCartNumbers = async () => {
  const res = await getCartListApi()
  if (res.code === 1) {
    const map = {}
    res.data.forEach(item => {
      const id = item.dishId || item.setmealId
      map[id] = item.number
    })
    cartMap.value = map
  }
}

// 获取单个菜品/套餐数量
const getItemCount = (item) => {
  const id = item.dishId || item.setmealId || item.id
  return cartMap.value[id] || 0
}

// 添加购物车
const addToCart = async (item) => {
  if (shopStatus.value !== 1) {
    ElMessage({
      message: 'The shop is closed. You cannot add items to the cart.',
      type: 'warning',
      duration: 2000
    })
    return
  }

  try {
    const payload = type.value === 2
      ? { setmealId: item.id, number: 1 }
      : { dishId: item.id, number: 1 }

    const res = await addToCartApi(payload)

    if (res.code === 1) {
      // 更新 cartMap 对应数量
      const id = item.dishId || item.setmealId || item.id
      cartMap.value[id] = (cartMap.value[id] || 0) + 1
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
    console.error('添加购物车出错:', e)
    ElMessage({
      message: 'Error adding to cart: ' + (e?.message || 'Unknown error'),
      type: 'error',
      duration: 2000
    })
  }
}

// 减少购物车数量
const reduceItem = async (item) => {
  const id = item.dishId || item.setmealId || item.id
  const currentCount = cartMap.value[id] || 0

  if (currentCount <= 0) {
    ElMessage({
      message: 'Item count is already 0',
      type: 'warning',
      duration: 1500
    })
    return
  }

  try {
    const payload = type.value === 2
      ? { setmealId: item.id }
      : { dishId: item.id }

    const res = await subFromCartApi(payload)

    if (res.code === 1) {
      // 成功减少数量
      if (currentCount > 1) {
        cartMap.value[id] -= 1
      } else {
        delete cartMap.value[id]
      }
      cartStore.decrement(1)
      ElMessage({
        message: 'Item removed successfully',
        type: 'success',
        duration: 1500
      })
    } else {
      ElMessage({
        message: 'Failed to remove item: ' + (res.msg || 'Unknown error'),
        type: 'warning',
        duration: 2000
      })
    }
  } catch (e) {
    console.error('减少购物车数量失败', e)
    ElMessage({
      message: 'Error reducing item: ' + (e?.message || 'Unknown error'),
      type: 'error',
      duration: 2000
    })
  }
}

// 清空购物车
const clearCart = async () => {
  try {
    await cleanCartApi() // 清空后端购物车
    cartStore.clearCart(0) // 清空全局数量
    cartMap.value = {} // 清空本地 cartMap

    ElMessage({
      message: 'Cart cleared successfully',
      type: 'success',
      duration: 1500
    })
  } catch (e) {
    console.error('清空购物车失败', e)
    ElMessage({
      message: 'Failed to clear cart: ' + (e?.message || 'Unknown error'),
      type: 'error',
      duration: 2000
    })
  }
}

// 页面初始化
onMounted(async () => {
  // 获取分类列表，type=1表示菜品/套餐分类
  const res = await getCategoryListApi({ type: 1 })
  categories.value = res.data || []
  if (categories.value.length > 0) {
    selectCategory(categories.value[0].id)
  }

  await fetchShopStatus()
  await loadCategories()
  await loadCartNumbers()
})
</script>

  <style scoped>
  .menu-page {
    padding: 10px;
  background: url('@/assets/userlogin.jpg') no-repeat center/cover;
  background-size: cover; /* 保证背景铺满容器 */
  height: 97.4%;           /* 填满整个页面 */
  display: flex;
  flex-direction: column;
  }
  
  /* 顶部标题 */
  .menu-header h2 {
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  /* 主内容区域：左右布局 */
  .menu-content {
    display: flex;
    height: calc(100vh - 60px); /* 减去顶部标题高度，可根据需要调整 */
  }

  /* 顶部区域 */
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

/* 左侧类型 */

.type-toggle {
  justify-content: center;
  gap: 10px; /* 两个按钮间距 */
  margin: 10px 0 15px 0;
}

.type-toggle button {
  padding: 8px 20px;
  border: 1px solid #f8d33d;
  border-radius: 20px;
  background-color: #fff;
  color: #333;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.type-toggle button.active {
  background-color: #f8d33d;
  color: #333;
  box-shadow: 0 4px 8px rgba(248,211,61,0.2);
}


.type-toggle button:hover:not(.active) {
  background-color: #e6f0ff;
}

  /* 左侧分类列 */
  .category-column {
  width: 25%;
  /* border-right: 1px solid #eee; */
  display: flex;
  flex-direction: column;
  gap: 10px; /* 上下按钮间距 */
  padding: 5px ;
  overflow-y: auto;
}

.category-column button {
  padding: 8px 15px;
  border: 1px solid #f8d33d;      /* 黄色边框 */
  border-radius: 15px;
  background-color: #fff;
  color: #333;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: left;                /* 文本靠左 */
}

.category-column button.active {
  background-color: #f8d33d;       /* 黄色背景 */
  color: #333;                      /* 文字颜色 */
  box-shadow: 0 4px 8px rgba(248,211,61,0.2);
}

  
  /* 右侧菜品列 */
  .dish-column {
    width: 75%; /* 右侧宽度 */
    padding-left: px;
    overflow-y: auto;
    padding-right: 5px;
    max-height: 610px; 
  }
  /* 可选：自定义滚动条样式 */
.dish-column::-webkit-scrollbar {
  width: 6px;
}

.dish-column::-webkit-scrollbar-thumb {
  background-color: rgba(0,0,0,0.2);
  border-radius: 3px;
}

.dish-column::-webkit-scrollbar-track {
  background-color: rgba(0,0,0,0.05);
}
  
  .dish-list {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }
  
  .dish-item {
    background: #fff;
    border-radius: 10px;
    box-shadow: 0 2px 6px rgba(0,0,0,0.1);
    overflow: hidden;
    text-align: center;
  }
  
  .dish-item img {
    width: 100%;
    height: 150px;
    object-fit: cover;
  }
  
  .info {
    padding: 8px;  
  }
  
  .price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 14px;
}   
  .info-row {
  display: flex;
  justify-content: space-between; /* 左右对齐 */
  align-items: center;           /* 垂直居中 */
}

.info-row h4,
.info-row .price {
  margin: 0;
  font-weight: 500;
}

  
  .add-btn {
    background: #03742e;
    color: white;
    border: none;
    border-radius: 50%;
    width: 28px;
    height: 28px;
    font-size: 18px;
    cursor: pointer;
    margin-top: 5px;
  }

  .item-count {
  display: inline-block;
  width: 24px;       /* 固定宽度，让数字居中 */
  text-align: center;
  margin: 0 6px;     /* 左右各 6px 间距 */
  font-weight: bold;
  color: #333;
}


  .reduce-btn{
    background: #f56c6c;
    color: white;
    border: none;
    border-radius: 50%;
    width: 28px;
    height: 28px;
    font-size: 18px;
    cursor: pointer;
    margin-top: 5px;
  }
  
  .empty {
    grid-column: span 2;
    text-align: center;
    color: #999;
    padding: 20px 0;
  }

  .clear-cart-container {
  text-align: center;
  margin-top: 10px;
}
.clear-cart-btn {
  padding: 8px 20px;
  background-color: #f56c6c;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
  </style>
  