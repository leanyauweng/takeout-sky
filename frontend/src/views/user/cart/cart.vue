<template>
  <div class="checkout-page">
    <header class="header">
      <div class="location">🛒Shopping Cart List</div>
    </header>

    <div v-if="cartList.length === 0" class="empty">
      <span>🛒</span>
      <p>Sorry, your shopping cart is empty.</p>
      <p>Browse our delicious dishes now!</p>
      <button class="go-shop-btn" @click="goShop">Shop Now</button>
    </div>

    <div v-else class="cart-items">
      <div v-for="item in cartList" :key="item.id" class="cart-item">
        <img :src="item.image" alt="item.name" />
        <div class="info">
          <div class="info-row">
            <h4>{{ item.name }}</h4>
            <p class="price">RM{{ item.amount }}</p>
          </div>
          <div class="quantity-btns">
            <button class="add-btn" @click="addToCart(item)">+</button>
            <span class="item-count">{{ item.number }}</span>
            <button class="reduce-btn" @click="reduceItem(item)">-</button>
          </div>
        </div>
      </div>
    </div>

    <div class="checkout-footer" v-if="cartList.length > 0">
      <p class="total">Total: RM{{ totalAmount }}</p>
      <button class="submit-btn" :disabled="shopStatus !== 1" @click="submitOrder">
        Place Order
      </button>
    </div>
  </div>
</template>


<script setup>
import { ref, onMounted, computed } from 'vue'
import { getCartListApi, addToCartApi, subFromCartApi, cleanCartApi, getShopStatusApi } from '@/api/shoppingCart'
import { getDefaultAddressApi } from '@/api/addressBook'
import { submitOrderApi } from '@/api/userOrder'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/api/cart'

const cartList = ref([])
const shopStatus = ref(0)
const cartStore = useCartStore()
const router = useRouter()

// 获取购物车数据
const fetchCart = async () => {
  const res = await getCartListApi()
  if (res.code === 1) {
    cartList.value = res.data
  }
}

// 获取店铺营业状态
const fetchShopStatus = async () => {
  const res = await getShopStatusApi()
  if (res.code === 1) {
    shopStatus.value = res.data
  }
}

// 添加菜品到购物车
const addToCart = async (item) => {
  const payload = { dishId: item.dishId || undefined, setmealId: item.setmealId || undefined, number: 1 }
  const res = await addToCartApi(payload)
  if (res.code === 1) {
    item.number += 1
    cartStore.increment(1)
  }
}

// 减少购物车中菜品数量
const reduceItem = async (item) => {
  if (item.number <= 0) return
  const payload = { dishId: item.dishId || undefined, setmealId: item.setmealId || undefined }
  const res = await subFromCartApi(payload)
  if (res.code === 1) {
    cartStore.decrement(1)
    item.number -= 1

    // 数量为 0 时移除该项
    if (item.number === 0) {
      cartList.value = cartList.value.filter(i => i !== item)
    }
  }
}

// 计算总价
const totalAmount = computed(() => {
  return cartList.value.reduce((sum, item) => sum + (item.amount * item.number), 0)
})

// 提交订单
const submitOrder = async () => {
  if (cartList.value.length === 0) {
    ElMessage.warning('Your cart is empty')
    return
  }

  // 获取默认收货地址
  const addressRes = await getDefaultAddressApi()
  if (addressRes.code !== 1 || !addressRes.data) {
    ElMessage.warning('Please set a default delivery address first')
    return
  }

  const addressBookId = addressRes.data.id  // 默认地址ID

  const orderItems = cartList.value.map(item => ({
    dishId: item.dishId,
    setmealId: item.setmealId,
    number: item.number
  }))

  try {
    const res = await submitOrderApi({
      addressBookId,        // 传递默认地址ID给后端
      remark: '',           // 用户备注（可选）
      tablewareNumber: 1,   // 餐具数量（可调整）
      packAmount: 0,        // 打包费（可调整）
      items: orderItems
    })

    if (res.code === 1) {
      cartStore.clearCart()
      router.push({
        path: '/checkout',
        query: { orderNumber: res.data.orderNumber }
      })

      ElMessage.success('Order submitted successfully')
      await cleanCartApi()
      cartList.value = []
    }
  } catch (e) {
    ElMessage.error('Order submission failed')
    console.error(e)
  }
}

// 跳转到菜单页
const goShop = () => {
  router.push('/user/menu')
}

// 页面加载时执行
onMounted(async () => {
  await fetchCart()
  await fetchShopStatus()
})
</script>



<style scoped>
.checkout-page {
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

.location {
  font-family: "Poppins", "Nunito", sans-serif;
  font-size: 22px;
  font-weight: 700;
  color: #2c3e50;
  letter-spacing: 0.8px;
}

.cart-items {
  max-height: 590px;
  overflow-y: auto;
  margin-top: 10px;
}

.cart-item {
  display: flex;
  margin-bottom: 10px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  overflow: hidden;
  width: 330px;
  height: 100px;
}

.cart-item img {
  width: 120px;
  height: 100px;
  object-fit: cover;
}

.info {
  flex: 1;
  padding: 10px;
}

.quantity-btns {
  display: flex;
  align-items: center;
  justify-content: center; /* 或者 space-between 根据需要 */
  margin-top: -5px; /* 调整整体往上移动 */
  gap: 10px;
}

.price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 14px;
}

.info-row {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: -12px;
  gap: 30px;
}

.add-btn, .reduce-btn {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  border: none;
  font-size: 18px;
  cursor: pointer;
}

.add-btn { background-color: #03742e; color: #fff; }
.reduce-btn { background-color: #f56c6c; color: #fff; }

.item-count {
  display: inline-block;
  width: 24px;
  text-align: center;
  margin: 0 6px;
}

.total {
  display: inline-block;
  padding: 8px 12px;
  background-color: #d4cbcb;  /* 浅灰色背景 */
  color: #333333;             /* 深灰色文字 */
  font-weight: bold;
  border-radius: 8px;
  border: 1px solid #d4cbcb;  /* 浅灰色边框，更柔和 */
  font-size: 16px;
}

.checkout-footer {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.submit-btn {
  padding: 10px 20px;
  background-color: #37b24d; /* 绿色 */
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.empty {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 400px;
  color: #0009;
  font-size: 20px;
  font-weight: 500;
  gap: 5px;
}

.empty span {
  display: block;           /* 单独占一行 */
  font-size: 60px;          /* 放大图标 */
  text-align: center;       /* 居中 */
  color: #f56c6c;           /* 红色或者你喜欢的颜色 */
}

.empty p {
  margin: 2px;        /* 去掉默认上下间距 */
}

/* 第二行文字更小 */
.empty p:nth-of-type(2) {
  font-size: 14px;          /* 比第一行小 */
  color: #666;              /* 可选：颜色更淡 */
}


.go-shop-btn {
  padding: 8px 16px;
  background-color: #37b24d;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin: 5px;
}

.go-shop-btn:hover {
  background-color: #2f9e45;
}

</style>