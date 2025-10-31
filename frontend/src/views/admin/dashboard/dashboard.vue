<template>
  <div class="dashboard-container p-4 space-y-6">

    <!-- Card 1: Today's Data -->
    <div class="card dashboard-1 bg-gray-50 rounded-lg p-4 shadow-sm">
      <div class="flex justify-between items-center today-header">
        <div class="font-semibold">
          Today's Data {{ today }}
        </div>
        <button 
          class="text-blue-500 hover:underline text-sm"
          style="background: transparent !important; border: none !important; outline: none !important;"
          @click="toggleTodayDetails"
        >
          View Details &gt;
        </button>
      </div>

      <div class="stat-cards-row">
        <div class="stat-card">
          <div class="stat-title">Turnover</div>
          <div class="stat-value">RM{{ businessData.turnover }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-title">Valid Orders</div>
          <div class="stat-value">{{ businessData.validOrderCount }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-title">Order Completion Rate</div>
          <div class="stat-value">{{ businessData.orderCompletionRate }}%</div>
        </div>
        <div class="stat-card">
          <div class="stat-title">Average Order Value</div>
          <div class="stat-value">RM{{ businessData.unitPrice }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-title">New Users</div>
          <div class="stat-value">{{ businessData.newUsers }}</div>
        </div>
      </div>
    </div>

    <!-- Card 2: Order Management -->
    <div class="card dashboard-2 bg-gray-50 rounded-lg p-4 shadow-sm">
      <div class="flex justify-between items-center today-header">
        <div class="text-lg">
          <span style="font-weight: 700;">Order Management</span> {{ today }}
        </div>
        <button 
          class="text-blue-500 hover:underline text-sm"
          style="background: transparent !important; border: none !important; outline: none !important;"
          @click="toggleOrderDetails"
        >
          Order Details &gt;
        </button>
      </div>

      <div class="stat-cards-row">
        <!-- Waiting Orders -->
        <div class="stat-card flex flex-col items-center gap-2">
          <ClockIcon style="width:40px; height:40px;"/>
          <div class="flex items-center gap-1">
            <div class="stat-title">Waiting</div>
          </div>
          <div class="stat-value">{{ orderOverview.waitingOrders }}</div>
        </div>

        <!-- Out for Delivery -->
        <div class="stat-card flex flex-col items-center gap-2">
          <Delivery style="width:40px; height:40px;"/>
          <div class="flex items-center gap-1">
            <div class="stat-title">Out for Delivery</div>
          </div>
          <div class="stat-value">{{ orderOverview.deliveredOrders }}</div>
        </div>

        <!-- Completed -->
        <div class="stat-card flex flex-col items-center gap-2">
          <check style="width:40px; height:40px;"/>
          <div class="flex items-center gap-1">
            <div class="stat-title">Completed</div>
          </div>
          <div class="stat-value">{{ orderOverview.completedOrders }}</div>
        </div>

        <!-- Cancelled -->
        <div class="stat-card flex flex-col items-center gap-2">
          <Cancel style="width:40px; height:40px;"/>
          <div class="flex items-center gap-1">
            <div class="stat-title">Cancelled</div>
          </div>
          <div class="stat-value">{{ orderOverview.cancelledOrders }}</div>
        </div>

        <!-- All Orders -->
        <div class="stat-card flex flex-col items-center gap-2">
          <list style="width:40px; height:40px;"/>
          <div class="flex items-center gap-1">
            <div class="stat-title">All Orders</div>
          </div>
          <div class="stat-value">{{ orderOverview.allOrders }}</div>
        </div>
      </div>
    </div>

    <!-- Card 3: Dishes & Set Meals Overview -->
    <div class="flex gap-4 mx-auto dashboard-3-container">

      <!-- Left: Dishes Overview -->
      <div class="card card-dashboard" style="width:49%; height:130px;">
        <div class="flex justify-between items-center today-header">
          <div class="text-lg">
            <span style="font-weight: 700;">Dishes Overview</span>
          </div>
          <button 
            class="text-blue-500 hover:underline text-sm"
            style="background: transparent !important; border: none !important; outline: none !important;"
            @click="manageDishes"
          >
            Manage Dishes &gt;
          </button>
        </div>

        <div class="flex justify-between items-center gap-6 dish-status-section">
          <div class="dish-stat-card">
            <sold style="width:40px; height:40px;"/>
            <div class="stat-title">Available</div>
            <div class="stat-value">{{ dishOverview.sold }}</div>
          </div>
          <div class="dish-stat-card">
            <unsold style="width:40px; height:40px;"/>
            <div class="stat-title">Unavailable</div>
            <div class="stat-value">{{ dishOverview.discontinued }}</div>
          </div>
          <button class="btn-dashboard" @click="addDish">+ Add Dish</button>
        </div>
      </div>

      <!-- Right: Set Meals Overview -->
      <div class="card card-dashboard" style="width:49%; height:130px;">
        <div class="flex justify-between items-center today-header">
          <div class="text-lg"></div>
          <button 
            class="text-blue-500 hover:underline text-sm"
            style="background: transparent !important; border: none !important; outline: none !important;"
            @click="manageSetmeals"
          >
            Manage Set Meals &gt;
          </button>
        </div>
        <div class="flex justify-between items-center gap-6 dish-status-section">     
          <div class="dish-stat-card">
            <sold style="width:40px; height:40px;"/>
            <div class="stat-title">Available</div>
            <div class="stat-value">{{ setmealOverview.sold }}</div>
          </div>
          <div class="dish-stat-card">
            <unsold style="width:40px; height:40px;"/>
            <div class="stat-title">Unavailable</div>
            <div class="stat-value">{{ setmealOverview.discontinued }}</div>
          </div>
          <button class="btn-dashboard" @click="addSetmeal">+ Add Set Meal</button>
        </div>
      </div>
    </div>

    <!-- Card 4: Order Info -->
    <div class="card order-info-card bg-gray-50 rounded-lg p-4 shadow-sm w-full">
      <div class="order-header">
        <div class="text-lg">
          <span style="font-weight: 700;">Order Info</span>
        </div>
        <div class="space-x-2 order-filter-buttons">
          <span style="font-weight: 700;">Waiting Orders Today ({{ orderOverview.waitingOrders }})</span>
        </div>
      </div>

      <table class="order-table">
        <thead>
          <tr>
            <th style="width: 150px;">Order No</th>
            <th style="width: 180px;">Dishes</th>
            <th style="width: 280px;">Address</th>
            <th style="width: 160px;">Estimated Delivery</th>
            <th style="width: 120px;">Amount</th>
            <th style="width: 150px;">Notes</th>
            <th style="width: 110px; text-align:center">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="orders.length === 0">
            <td colspan="7" class="text-center text-gray-400">No Orders</td>
          </tr>
          <tr v-for="order in orders" :key="order.id">
            <td>{{ order.number }}</td>
            <td>{{ order.dishes }}</td>
            <td>{{ order.address }}</td>
            <td>{{ order.estimatedTime }}</td>
            <td>RM{{ order.amount }}</td>
            <td>{{ order.note }}</td>
            <td class="actions">
              <button class="btn-green" @click="acceptOrder(order.id)">Accept</button>
              <button class="btn-red" @click="rejectOrder(order.id)">Reject</button>
              <button class="btn-gray" @click="() => router.push(`/admin/order/${order.id}`)">View</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- Pagination -->
  <div class="pagination mt-4">
    <el-pagination
      v-model:current-page="query.page"
      v-model:page-size="query.pageSize"
      :page-sizes="[5, 10, 20, 50]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @current-change="handlePageChange"
      @size-change="handleSizeChange"
    />
  </div>

</template>


<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getOverviewApi, getOrderOverviewApi, getDishOverviewApi, getSetmealOverviewApi } from '@/api/dashboard'
import { pageOrdersApi, acceptOrderApi, rejectOrderApi, getOrderDetailApi } from '@/api/order'
import ClockIcon from '@/icons/ClockIcon.vue'
import check from '@/icons/check.vue'
import list from '@/icons/list.vue'
import sold from '@/icons/sold.vue'
import unsold from '@/icons/unsold.vue'
import Delivery from '@/icons/delivery.vue'
import Cancel from '@/icons/cancel.vue'
import { ElMessage } from 'element-plus'



const router = useRouter()
const today = new Date().toLocaleDateString()

// 今日数据
const businessData = reactive({
  turnover: 0,
  validOrderCount: 0,
  orderCompletionRate: 0,
  unitPrice: 0,
  newUsers: 0
})

// 订单总览
const orderOverview = reactive({
  waitingOrders: 0,
  deliveredOrders: 0,
  completedOrders: 0,
  cancelledOrders: 0,
  allOrders: 0
})

// 菜品总览
const dishOverview = reactive({
  sold: 0,
  discontinued: 0
})

// 套餐总览
const setmealOverview = reactive({
  sold: 0,
  discontinued: 0
})

const orders = ref([])
const query = ref({
  number: '',
  phone: '',
  status: 2,      // 默认展示待派送或你需要的状态
  page: 1,
  pageSize: 10
})

const total = ref(0)
const loading = ref(false)

// 获取订单列表
const fetchOrders = async () => {
  loading.value = true
  try {
    const res = await pageOrdersApi(query.value)
    const orderList = res.data?.records || []
    orders.value = orderList.map(order => ({
      ...order,
      status: Number(order.status),
      dishes: (order.orderDetails || []).map(d => d.name).join('、') || '',
      estimatedTime: order.estimatedDeliveryTime || '',
      note: order.remark || ''
    }))
    total.value = res.data?.total || 0
  } catch (err) {
    console.error('获取订单列表失败', err)
    orders.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

//分页事件
const handlePageChange = (page) => {
  query.value.page = page
  fetchOrders()
}

const handleSizeChange = (pageSize) => {
  query.value.pageSize = pageSize
  fetchOrders()
}



// 接单
const acceptOrder = async (id) => {
  try {
    await acceptOrderApi(id)
    ElMessage({
      message: 'Order successfully received',
      type: 'success',
      duration: 2000
    })
    fetchOrders()
  } catch (error) {
    ElMessage({
      message: 'Failed to accept order',
      type: 'error',
      duration: 2000
    })
  }
}
// 拒单
const rejectOrder = async (id) => {
  try {
    await rejectOrderApi(id)
    ElMessage({
      message: 'Order rejection successful',
      type: 'success',
      duration: 2000
    })
    fetchOrders()
  } catch (error) {
    ElMessage({
      message: 'Failed to reject order',
      type: 'error',
      duration: 2000
    })
  }
}

const toggleTodayDetails = () => {
  router.push('/admin/chart')
}

const toggleOrderDetails = () => {
  router.push('/admin/orderDetails')
}

const manageDishes = () => {
  router.push('/admin/dish')
}
const manageSetmeals = () => {
  router.push('/admin/setmeal')
}


const addDish = () => {
  router.push('/admin/dish/add')
}

const addSetmeal = () => {
  router.push('/admin/setmeal/add')
}

// 获取接口数据
onMounted(async () => {
  const res1 = await getOverviewApi()
  Object.assign(businessData, res1.data)

  const res2 = await getOrderOverviewApi()
  Object.assign(orderOverview, res2.data)

  const res3 = await getDishOverviewApi()
  Object.assign(dishOverview, res3.data)

  const res4 = await getSetmealOverviewApi()
  Object.assign(setmealOverview, res4.data)

  fetchOrders()
})
</script>

<style scoped>
.dashboard-container {
  flex-direction: column;
  gap: 1.5rem;
}

.card {
  background-color: #f9fafb;
  padding: 1rem;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.stat-cards-row {
  display: flex;
  gap: 1rem;
}

.stat-card {
  flex: none;
  width: 17%;
  min-width: 120px;
  background-color: #0f1a58ce;
  border-radius: 0.5rem;
  padding: 1rem;
  text-align: center;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.stat-title {
  font-size: 0.875rem;
  color: #555;
  margin-bottom: 0.25rem;
}

.stat-value {
  font-size: 1.25rem;
  font-weight: 600;
  color: #111;
}

/* 大框 1 水平排列小框 */
.dashboard-1 .stat-cards-row {
  display: flex;
  gap: 1rem;
  justify-content: space-between;
}

/* 大框 1 小框内部垂直排列保持原样 */
.dashboard-1 .stat-card {
  flex: none;
  min-width: 100px;
  background-color: #fdfdf2ce;
  border-radius: 0.5rem;
  padding: 1rem;
  text-align: left;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column; /* 保持标题在上，数据在下 */
  justify-content: center;
}

.today-header {
  display: flex !important;
  justify-content: space-between !important;
  align-items: center !important;
}

/* 大框 2：订单管理独立处理 */
.dashboard-2 {
  display: flex;
  flex-direction: column;      /* 保持标题在上，小框在下 */
  gap: 1rem;
  padding: 1rem;
}

/* 小框水平排列 */
.dashboard-2 .stat-cards-row {
  display: flex;
  gap: 1rem;
  justify-content: space-between; /* 左右分布 */
}

/* 如果需要限定宽度或滚动 */
.dashboard-2 .stat-card {
  display: flex;             /* 启用 flex 布局 */
  justify-content: space-between; /* 左右两端对齐 */
  align-items: center;       /* 垂直居中 */
  padding: 1rem;
  background-color: #fdfdf2ce;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  min-width: 120px;
}


/* 限定大框 3 容器 */
.dashboard-3-container {
  display: flex;
  gap: 2rem;       /* 左右中框间距 */
  justify-content: space-between;
  height: 130px;   /* 父容器高度 */
}

/* 左右中框特有样式 */
.card-dashboard {
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* 小框 + 底部按钮 */
  height: 100%;
  box-sizing: border-box;
  background-color: #f9fafb;       /* 保持和全局.card一致 */
  padding: 1rem;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

/* 单独控制 dish-status-section 内的小框为左右排列 */
.dish-status-section .stat-card {
  display: flex;
  justify-content: space-between; /* 左右两端对齐 */
  align-items: center;            /* 垂直居中 */
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  padding: 0.75rem 1rem;
  width: 40%;                     /* 让两个框左右并排 */
  text-align: left;               /* 左对齐文字 */
  transition: all 0.2s ease;
}

.dish-status-section .stat-card:hover {
  box-shadow: 0 3px 6px rgba(0,0,0,0.15);
}

.dish-status-section {
  display: flex;
  justify-content: space-between; /* 左右对齐 */
  align-items: center;
  gap: 1rem; /* 两个小框间距 */
}

.dish-stat-card {
  flex: 1;               /* 平均分配父容器宽度 */
  display: flex;         
  justify-content: space-between; /* 左右排列标题和数值 */
  align-items: center;
  background-color: #fdfdf2ce; 
  padding: 0.75rem 1rem;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  text-align: left;       /* 避免全局 text-align:center */
}

.dish-stat-card .stat-title {
  font-size: 0.875rem;
  color: #555;
}

.dish-stat-card .stat-value {
  font-size: 1.25rem;
  font-weight: 600;
  color: #111;
}

.btn-dashboard {
  background-color: #fbbb4a;   /* Tailwind bg-blue-500 */
  color: #000;                 /* 文字白色 */
  font-weight: 500;            /* 中等粗 */
  padding: 0.5rem 1.25rem;     /* px-5 py-2 */
  border-radius: 0.5rem;       /* rounded */
  box-shadow: 0 2px 6px rgba(0,0,0,0.15); /* 阴影 */
  transition: background-color 0.2s;
}

.btn-dashboard:hover {
  background-color: #fbbb4a;   
}

/* 限定大框 4 容器 */
/* 独立订单信息卡片样式 */
.order-info-card {
  background-color: #f8fafc;        /* 比灰底略亮 */
  border: 1px solid #e2e8f0;        /* 柔和边框 */
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
  width: 97.5%; 
}

/* 表格样式美化 */
.order-info-card table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
  color: #333;
}

.order-info-card th {
  background-color: #f1f5f9;
  font-weight: 600;
  text-align: left;
  padding: 10px;
}

.order-info-card td {
  padding: 10px;
  border-top: 1px solid #e5e7eb;
}

.order-info-card tbody tr:hover {
  background-color: #f9fafb;
}

/* 按钮风格统一 */
.order-info-card .btn-green {
  background-color: #dcfce7;
  color: #166534;
  padding: 4px 8px;
  border-radius: 6px;
  transition: all 0.2s;
}
.order-info-card .btn-green:hover {
  background-color: #bbf7d0;
}

.order-info-card .btn-red {
  background-color: #fee2e2;
  color: #991b1b;
  padding: 4px 8px;
  border-radius: 6px;
  transition: all 0.2s;
}
.order-info-card .btn-red:hover {
  background-color: #fecaca;
}

.order-info-card .btn-gray {
  background-color: #f3f4f6;
  color: #374151;
  padding: 4px 8px;
  border-radius: 6px;
  transition: all 0.2s;
}
.order-info-card .btn-gray:hover {
  background-color: #e5e7eb;
}

.order-header {
  display: flex;
  justify-content: space-between; /* 左右分布 */
  align-items: center; /* 垂直居中 */
  margin-bottom: 1rem;
}

/* 独立作用于这个页面，不会影响全局 */
.order-filter-buttons button {
  min-width: 150px;            /* 让按钮更长一点 */
  padding: 6px 14px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.2s ease;
  border: 1px solid #555;

}

/* 黄色按钮 - 待接单 */
.order-filter-buttons .btn-waiting {
  background-color: #fbbb4a;
  color: #000; 
}

.order-filter-buttons .btn-waiting:hover {
  background-color: #fbbb4a;
}

/* 蓝色按钮 - 待派送 */
.order-filter-buttons .btn-delivered {
  background-color: #fff;
  color: #000; 
}

.order-filter-buttons .btn-delivered:hover {
  background-color: #fff;
}

.order-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed; /* ✅ 保证平均分布 */
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
}

/* 只让“操作”列居中 */
.order-table th.col-action,
.order-table td.col-action {
  text-align: left;       /* 或 right，根据需要 */
  padding-left: 70px;     /* 调整距离左边的距离 */
  padding-right: 20px; 
}



.order-table thead {
  background-color: #f8f8f8;
}

.order-table th {
  font-weight: 600;
  width: calc(100% / 7); /* ✅ 7列平均分布 */
}

.order-table tr:nth-child(even) {
  background-color: #fafafa;
}

.order-table tr:hover {
  background-color: #f3f4f6;
}

/* 操作列按钮样式 */
.actions {
  display: flex;
  gap: 6px;
}

.btn-green {
  background: #22c55e;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
}

.btn-red {
  background: #ef4444;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
}

.btn-gray {
  background: #9ca3af;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
}
.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
}
</style>
