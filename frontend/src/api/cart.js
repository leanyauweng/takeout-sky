import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCartListApi } from '@/api/shoppingCart'

export const useCartStore = defineStore('cart', () => {
  const cartCount = ref(0)

  const setCartCount = (count) => {
    cartCount.value = count
  }

  const increment = (num = 1) => {
    cartCount.value += num
  }

  const decrement = (num = 1) => {
    cartCount.value = Math.max(cartCount.value - num, 0)
  }

  const clearCart = () => {
    cartCount.value = 0
  }

  // ✅ 页面初始化调用，从后端同步购物车数量
  const fetchCartCount = async () => {
    try {
      const res = await getCartListApi()
      const total = res.data.reduce((sum, item) => sum + item.number, 0)
      cartCount.value = total
    } catch (err) {
      console.error('获取购物车数量失败', err)
    }
  }

  return { cartCount, setCartCount, increment, decrement, clearCart, fetchCartCount }
})