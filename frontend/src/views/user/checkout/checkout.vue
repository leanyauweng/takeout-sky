<template>
  <div class="mobile-frame">
    <!-- 顶部标题 -->
    <div class="header">
      <span class="back-btn" @click="goBack">←</span>
      <h2>Payment</h2>
    </div>

    <div class="content-area">
      <!-- 收货地址 -->
      <div class="address-section" v-if="address">
        <h3>Delivery Address</h3>
        <p>{{ address.consignee }} ({{ address.sex === 'M' ? 'Mr.' : 'Ms.' }}) {{ address.phone }}</p>
        <p>{{ address.stateName }} · {{ address.cityName }} · {{ address.detail }}</p>
      </div>

      <!-- 订单概要 -->
      <div class="order-summary" v-if="order">
        <h3>Order Summary</h3>
        <p>Order Number: {{ order.orderNumber }}</p>
        <p>Order Time: {{ order.orderTime }}</p>
        <p>Order Amount: RM {{ order.amount }}</p>
      </div>

      <!-- 付款方式选择 -->
      <div class="payment-method">
        <h3>Select Payment Method</h3>
        <div class="methods">
          <div
            class="method"
            v-for="m in paymentMethods"
            :key="m.id"
            :class="{ active: selectedMethod === m.id }"
            @click="selectedMethod = m.id"
          >
            {{ m.name }}
          </div>
        </div>
      </div>

      <!-- 支付汇总 -->
      <div class="payment-summary" v-if="order">
        <p class="payment-row">
          <span>Amount Due:</span>
          <span class="amount">RM {{ order.amount }}</span>
        </p>
      </div>
    </div>

    <!-- 支付按钮 -->
    <div class="footer">
      <button class="pay-btn" @click="handlePay" :disabled="loading">
        {{ loading ? 'Processing Payment...' : 'Confirm Payment' }}
      </button>
    </div>
  </div>
</template>



<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/userRequest'
import { getDefaultAddressApi } from '@/api/addressBook'
import { ElMessage } from 'element-plus'

const route = useRoute()     // 用于读取 query
const router = useRouter()   // 用于跳转
const orderNumber = route.query.orderNumber

const address = ref(null)
const order = ref(null)
const loading = ref(false)

// 模拟付款方式
const paymentMethods = [
  { id: 1, name: 'Alipay' },
  { id: 2, name: 'WeChat Pay' },
  { id: 3, name: 'Bank Card' }
]
const selectedMethod = ref(1)

// 返回
const goBack = () => {
  router.push('/user/orders')
}

// 获取订单详情
const getOrderDetailApi = (orderNumber) => {
  return request.get(`/user/order/detail/${orderNumber}`)
}

const fetchData = async () => {
  // 默认地址
  const addressRes = await getDefaultAddressApi()
  if(addressRes.code === 1) {
    address.value = addressRes.data
  }

  // 订单详情
  if(orderNumber) {
    const res = await getOrderDetailApi(orderNumber)
    if(res.code === 1){
      order.value = res.data
    } else {
      ElMessage.error('Failed to get order details')
    }
  }
}

onMounted(() => {
  fetchData()
})

// 支付按钮逻辑
const handlePay = async () => {
  if(!order.value) return
  loading.value = true
  try {
    const res = await request.post('/user/payment', null, { params: { orderNumber: order.value.orderNumber } })
    if(res.code === 1){
      ElMessage.success('Payment successful')
      // 可跳转到支付成功页面或订单列表
      router.push('/user/orders')
    } else {
      ElMessage.error('Payment failed')
    }
  } catch(e) {
    ElMessage.error('Payment failed')
    console.error(e)
  } finally {
    loading.value = false
  }
}
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
  box-shadow: 0 0 20px rgba(0,0,0,0.2);
  display: flex;
  flex-direction: column;
  position: relative;
}

.header {
  background-color: #fff;
  color: #000;
  text-align: center;
  font-size: 13px;
  font-weight: bold;
  border-bottom: 1px solid #eee;
}
.back-btn {
  position: absolute;
  color: #fa0303;
  left: 10px;
  top: 11px;  
  font-size: 30px;
  cursor: pointer;
  user-select: none;
}
.content-area {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
}

.address-section, .order-summary, .payment-method, .payment-summary {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #eee;
  border-radius: 10px;
}

.payment-method .methods {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.payment-method .method {
  flex: 1;
  text-align: center;
  padding: 8px 0;
  border: 1px solid #ccc;
  border-radius: 6px;
  cursor: pointer;
}

.payment-method .method.active {
  border-color: #409eff;
  background-color: #e6f0ff;
  color: #409eff;
}

.payment-summary .payment-row {
  display: flex;
  justify-content: space-between; /* 左右分开 */
  align-items: center;
  padding: 10px 5px;
}

.payment-summary .amount {
  color: #f56c6c;
  font-weight: bold;
}


.footer {
  padding: 12px;
  border-top: 1px solid #eee;
}

.pay-btn {
  width: 100%;
  padding: 12px;
  background-color: #37b24d;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
}
</style>
