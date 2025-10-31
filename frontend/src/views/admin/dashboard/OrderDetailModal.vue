<template>
  <div class="order-detail-container p-6">
    <!-- 标题 -->
    <h2 class="text-2xl font-bold mb-6 border-b pb-2">
      Order Details - {{ order.number }}
    </h2>

    <!-- 基本信息卡片 -->
    <div class="info-card grid grid-cols-2 gap-6 mb-6">
      <div class="info-item"><span class="label">Order Time:</span>{{ order.orderTime }}</div>
      <div class="info-item"><span class="label">Estimated Delivery:</span>{{ order.estimatedDeliveryTime || '-' }}</div>
      <div class="info-item"><span class="label">Amount Received:</span>RM{{ order.amount }}</div>
      <div class="info-item"><span class="label">Payment Method:</span>{{ payMethodText }}</div>
      <div class="info-item"><span class="label">Delivery Status:</span>{{ deliveryStatusText }}</div>
      <div class="info-item"><span class="label">Consignee:</span>{{ order.consignee }} | {{ order.phone }}</div>
      <div class="info-item col-span-2"><span class="label">Address:</span>{{ order.address }}</div>
      <div class="info-item col-span-2"><span class="label">Remark:</span>{{ order.remark || '-' }}</div>
    </div>

    <!-- 订单明细 -->
    <h3 class="text-xl font-semibold mb-3">Order Items</h3>
    <div class="table-wrapper mb-6">
      <table class="order-detail-table">
        <thead>
          <tr>
            <th>Dish/Set Meal</th>
            <th>Quantity</th>
            <th>Amount</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in order.orderDetails" :key="item.id">
            <td>{{ item.name }}</td>
            <td>{{ item.number }}</td>
            <td>RM{{ item.amount }}</td>
          </tr>
          <tr v-if="!(order.orderDetails && order.orderDetails.length)">
            <td colspan="3" class="text-center text-gray-500">No Order Details</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 返回按钮 -->
    <div class="flex justify-start">
      <button @click="goBack" class="btn-back">
        ← Back
      </button>
    </div>
  </div>
</template>

  <script setup>
  import { ref, onMounted, computed } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { getOrderDetailApi } from '@/api/order'
  
  const route = useRoute()
  const router = useRouter()
  
  const order = ref({
    number: '',
    orderTime: '',
    estimatedDeliveryTime: '',
    amount: 0,
    payMethod: 1,
    deliveryStatus: 0,
    consignee: '',
    phone: '',
    address: '',
    remark: '',
    orderDetails: []
  })
  
  const fetchOrder = async (id) => {
  try {
    const res = await getOrderDetailApi(id) // ✅ 用单条详情接口
    if (res.code === 1 && res.data) {
      order.value = res.data
    }
  } catch (err) {
    console.error('Failed to retrieve order details', err)
  }
}

onMounted(() => {
  const id = route.params.id
  fetchOrder(id)
})


  
  onMounted(() => {
    const id = route.params.id
    fetchOrder(id)
  })
  
  const payMethodText = computed(() => order.value.payMethod === 1 ? 'Cash on delivery' : 'Online Payment')
  
  const deliveryStatusText = computed(() => {
    switch(order.value.deliveryStatus) {
      case 0: return 'Pending Delivery'
      case 1: return 'Delivered'
      default: return 'Unknow'
    }
  })
  
  const goBack = () => router.back()
  </script>
  
  <style scoped>
  .page-wrapper {
  background-color: #f3f4f6; /* 整体暗淡灰 */
  min-height: 100vh;
  padding: 2rem 0;
  display: flex;
  justify-content: center;
}

.order-detail-container {
  max-width: 900px;
  width: 100%;
  background-color: #ffffff; /* 卡片白色 */
  padding: 2rem;
  border-radius: 12px;
  border: 1px solid #d1d5db; /* 框线 - 柔和灰 */
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

  
  /* 信息卡片 */
  .info-card {
    background-color: #fefefe;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  }
  .info-item {
    padding: 8px 0;
  }
  .label {
    font-weight: 600;
    color: #333;
  }
  
  /* 表格 */
  .table-wrapper {
    overflow-x: auto;
  }
  .order-detail-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 14px;
  }
  .order-detail-table th,
  .order-detail-table td {
    padding: 10px 12px;
    border: 1px solid #e2e8f0;
    text-align: left;
  }
  .order-detail-table th {
    background-color: #f1f5f9;
    font-weight: 600;
  }
  .order-detail-table tbody tr:hover {
    background-color: #f9fafb;
  }
  
  /* 返回按钮 */
  .btn-back {
    background-color: #fbbf24; /* 黄色 */
    color: #111;
    font-weight: 600;
    padding: 8px 16px;
    border-radius: 8px;
    transition: all 0.2s ease;
  }
  .btn-back:hover {
    background-color: #f59e0b;
  }
  </style>
  