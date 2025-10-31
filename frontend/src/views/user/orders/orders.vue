<template>
  <div class="orders-page">
    <h2 class="page-title">My Orders</h2>

    <!-- 订单列表 -->
    <div v-for="order in orderList" :key="order.id" class="order-card">
      <!-- 订单头部 -->
      <div class="order-header">
        <div class="order-info">
          <span class="order-number">Order No: {{ order.number }} </span>
          <span class="order-time">Date: {{ order.createTime }}</span>
        </div>

        <!-- 右侧按钮 -->
        <!-- 按钮：不同状态对应不同操作 -->
        <button
          v-if="parseInt(order.status) === 1"
          class="pay-btn"
          @click="goPay(order.number)"
        >
          Pay Now
        </button>

        <button
          v-else-if="parseInt(order.status) === 3"
          class="reminder-btn"
          @click="handleReminder(order.id)"
        >
          Remind Seller
        </button>

        <button
          v-else-if="parseInt(order.status) === 5"
          class="reorder-btn"
          @click="handleReorder(order.id)"
        >
          Order Again
        </button>

        <button
          v-else-if="parseInt(order.status) === 2"
          class="cancel-btn"
          @click="handleCancel(order.id)"
        >
          Cancel Order
        </button>
      </div>

      <!-- 状态 -->
      <div class="order-status">{{ formatStatus(order.status) }}</div>

      <!-- 订单明细 -->
      <div class="order-items">
        <div v-for="item in order.items" :key="item.id" class="order-item">
          <span class="item-name">{{ item.name }}</span>
          <span class="item-number">×{{ item.number }}</span>
          <span class="item-amount">RM{{ item.amount }}</span>
        </div>
      </div>

      <!-- 订单尾部 -->
      <div class="order-footer">
        <span>Total:</span>
        <b>RM{{ order.amount }}</b>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserOrders, reorderApi, reminderOrderApi, cancelOrderApi } from '@/api/userOrder'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const orderList = ref([])

// 加载用户订单
const loadOrders = async () => {
  try {
    const res = await getUserOrders()
    if (res.code === 1) {
      orderList.value = res.data
    } else {
      console.error('获取订单失败:', res.msg)
    }
  } catch (error) {
    console.error('请求订单接口出错:', error)
  }
}

// 格式化订单状态
const formatStatus = (status) => {
  switch (status) {
    case 1: return 'Pending Payment'
    case 2: return 'Awaiting Acceptance'
    case 3: return 'Accepted'
    case 4: return 'Delivering'
    case 5: return 'Completed'
    case 6: return 'Cancelled'
    default: return 'Unknown'
  }
}

// 去支付
const goPay = (orderNumber) => {
  router.push({
    path: "/checkout",
    query: { orderNumber },
  })
}

// 再来一单
const handleReorder = async (orderId) => {
  try {
    const res = await reorderApi(orderId)
    if (res.code === 1) {
      const newOrderNumber = res.data.orderNumber
      router.push({
        path: '/checkout',
        query: { orderNumber: newOrderNumber }
      })
    } else {
      console.error('再来一单失败:', res.msg)
    }
  } catch (error) {
    console.error('调用再来一单接口出错:', error)
  }
}

// 催单
const handleReminder = async (orderId) => {
  try {
    const res = await reminderOrderApi(orderId)
    if (res.code === 1) {
      ElMessage.success("Reminder sent to the seller!")
    } else {
      ElMessage.warning(res.msg || "Failed to send reminder")
    }
  } catch (err) {
    ElMessage.error("Request failed")
  }
}

// 取消订单
const handleCancel = async (orderId) => {
  try {
    await ElMessageBox.confirm(
      'Are you sure you want to cancel this order?',
      'Cancel Order',
      { confirmButtonText: 'Confirm', cancelButtonText: 'Cancel', type: 'warning' }
    )

    const res = await cancelOrderApi(orderId)
    if (res.code === 1) {
      ElMessage.success('Order has been cancelled')
      loadOrders()
    } else {
      ElMessage.warning(res.msg || 'Failed to cancel')
    }
  } catch (err) {
    if (err !== 'cancel') ElMessage.error('Cancel request failed')
  }
}

onMounted(loadOrders)
</script>


<style scoped>
.orders-page {
  padding: 12px;
  background: #f5f5f5;
  
  height:68.5vh;        /* 保证占满屏幕高度 */
  overflow-y: auto;
  
}
.orders-page::-webkit-scrollbar {
  width: 6px;
}

.orders-page::-webkit-scrollbar-thumb {
  background-color: rgba(0,0,0,0.2);
  border-radius: 3px;
}

.orders-page::-webkit-scrollbar-track {
  background-color: rgba(0,0,0,0.05);
}

.page-title {
  font-size: 20px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 16px;
}

.order-card {
  background: #fff;
  border-radius: 10px;
  padding: 12px;
  margin-bottom: 12px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}



.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;
  font-size: 14px;
  color: #555;
  margin-bottom: 8px;
}
.pay-btn {
  background-color: #37b24d;
  color: white;
  margin-left: auto;
  border: none;
  margin-top: 8px;
  border-radius: 8px;
  padding: 6px 14px;
  font-size: 14px;
  cursor: pointer;
}

.reminder-btn{
  background-color: #ffa94d;
  color: white;
  margin-left: auto;
  border: none;
  margin-top: 8px;
  border-radius: 8px;
  padding: 6px 14px;
  font-size: 14px;
  cursor: pointer;
}

.reorder-btn{
  background-color: #228be6;
  color: white;
  margin-left: auto;
  border: none;
  margin-top: 8px;
  border-radius: 8px;
  padding: 6px 14px;
  font-size: 14px;
  cursor: pointer;
}

.cancel-btn {
  background-color: #f03e3e;
  color: white;
  margin-left: auto;
  border: none;
  margin-top: 8px;
  border-radius: 8px;
  padding: 6px 14px;
  font-size: 14px;
  cursor: pointer;
}

.order-header .order-number,
.order-header .order-time,
.order-header .order-status {
  margin-bottom: 4px;
}

.order-status {
  color: #f56c6c;
  font-weight: bold;
}

.order-items {
  border-top: 1px solid #eee;
  border-bottom: 1px solid #eee;
  padding: 8px 0;
}

.order-item {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  padding: 4px 0;
}

.order-footer {
  display: flex;
  justify-content: flex-end;
  font-size: 16px;
  margin-top: 8px;
  font-weight: bold;
}
</style>
