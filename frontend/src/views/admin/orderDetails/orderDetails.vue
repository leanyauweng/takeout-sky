<template>
  <div class="order-page p-6">
    <!-- Filter Bar -->
    <el-form :inline="true" :model="queryForm" class="filter-bar mb-4">
      <el-form-item label="Order Number">
        <el-input v-model="queryForm.number" placeholder="Enter order number" clearable></el-input>
      </el-form-item>

      <el-form-item label="Phone Number">
        <el-input v-model="queryForm.phone" placeholder="Enter phone number" clearable></el-input>
      </el-form-item>

      <el-form-item label="Order Time">
        <el-date-picker
          v-model="queryForm.dateRange"
          type="daterange"
          start-placeholder="Start date"
          end-placeholder="End date"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="handleSearch">Search</el-button>
        <el-button @click="handleReset">Reset</el-button>
      </el-form-item>
    </el-form>

    <!-- Status Tabs -->
    <el-tabs v-model="currentStatus" type="border-card" class="mb-4">
      <el-tab-pane label="All Orders" name="all"></el-tab-pane>
      <el-tab-pane label="Pending" name="pending"></el-tab-pane>
      <el-tab-pane label="To Deliver" name="confirmed"></el-tab-pane>
      <el-tab-pane label="Delivering" name="delivering"></el-tab-pane>
      <el-tab-pane label="Completed" name="completed"></el-tab-pane>
      <el-tab-pane label="Canceled" name="canceled"></el-tab-pane>
    </el-tabs>

    <!-- Orders Table -->
    <el-table
      v-loading="loading"
      :data="orderList"
      border
      stripe
      style="width: 100%">
      
      <!-- Dynamic Columns -->
      <el-table-column 
        v-for="col in columns" 
        :key="col.prop" 
        :prop="col.prop" 
        :label="col.label" 
        :width="col.width" 
        :align="col.align || 'center'">

        <template v-slot="scope">
          <span v-if="col.prop === 'status'">{{ formatStatus(scope.row.status) }}</span>
          
          <span v-else-if="col.prop === 'actions'">
            <el-button
              v-for="btn in getActionButtons(scope.row)"
              :key="btn.label"
              :type="btn.type"
              size="mini"
              @click="btn.handler(scope.row)"
              style="margin-right: 5px">
              {{ btn.label }}
            </el-button>
          </span>
          
          <span v-else>
            <!-- If it's the phone number column, add + -->
            <template v-if="col.prop === 'phone'">+{{ scope.row.phone }}</template>
            <template v-else>{{ scope.row[col.prop] }}</template>
          </span>
        </template>
      </el-table-column>

    </el-table>

    <!-- Pagination -->
    <div class="pagination mt-4 flex justify-end">
      <el-pagination
        v-model:current-page="query.page"
        v-model:page-size="query.pageSize"
        :page-sizes="[5,10,20,50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handlePageChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>


<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { pageOrdersApi, acceptOrderApi, deliverOrderApi, completeOrderApi, rejectOrderApi, cancelOrderApi } from '@/api/order'

const router = useRouter()

// 查询条件
const queryForm = reactive({
  number: '',
  phone: '',
  dateRange: []
})

const query = reactive({
  page: 1,
  pageSize: 10
})

const currentStatus = ref('all') // all, pending, toDeliver, delivering, completed, canceled
const orderList = ref([])
const loading = ref(false)
const total = ref(0)

// 状态 -> 列映射
const columnsMap = {
  all: [
    { label: 'Order Number', prop: 'number', width: '220' },
    { label: 'Status', prop: 'status', width: '220' },
    { label: 'Customer', prop: 'consignee', width: '220' },
    { label: 'Phone', prop: 'phone', width: '220' },
    { label: 'Address', prop: 'address', width: '260' },
    { label: 'Order Time', prop: 'orderTime', width: '200' },
    { label: 'Amount', prop: 'amount', width: '200' },
    { label: 'Actions', prop: 'actions', width: '280' }
  ],
  pending: [
    { label: 'Order Number', prop: 'number', width: '260' },
    { label: 'Dishes', prop: 'dishes', width: '260' },
    { label: 'Address', prop: 'address', width: '260' },
    { label: 'Estimated Delivery', prop: 'estimatedDeliveryTime', width: '260' },
    { label: 'Remark', prop: 'remark', width: '260' },
    { label: 'Tableware', prop: 'tablewareNumber', width: '240', align: 'center' },
    { label: 'Actions', prop: 'actions', width: '280', align: 'center' }
  ],
  confirmed: [
    { label: 'Order Number', prop: 'number', width: '260' },
    { label: 'Dishes', prop: 'dishes', width: '260' },
    { label: 'Address', prop: 'address', width: '260' },
    { label: 'Estimated Delivery', prop: 'estimatedDeliveryTime', width: '260' },
    { label: 'Remark', prop: 'remark', width: '260' },
    { label: 'Tableware', prop: 'tablewareNumber', width: '240' },
    { label: 'Actions', prop: 'actions', width: '280' }
  ],
  delivering: [
    { label: 'Order Number', prop: 'number', width: '260' },
    { label: 'Dishes', prop: 'dishes', width: '260' },
    { label: 'Address', prop: 'address', width: '260' },
    { label: 'Estimated Delivery', prop: 'estimatedDeliveryTime', width: '260' },
    { label: 'Remark', prop: 'remark', width: '260' },
    { label: 'Tableware', prop: 'tablewareNumber', width: '240' },
    { label: 'Actions', prop: 'actions', width: '280' }
  ],
  completed: [
    { label: 'Order Number', prop: 'number', width: '220' },
    { label: 'Customer', prop: 'consignee', width: '220' },
    { label: 'Phone', prop: 'phone', width: '220' },
    { label: 'Address', prop: 'address', width: '260' },
    { label: 'Amount', prop: 'amount', width: '200' },
    { label: 'Order Time', prop: 'orderTime', width: '200' },
    { label: 'Remark', prop: 'remark', width: '220' },
    { label: 'Actions', prop: 'actions', width: '280' }
  ],
  canceled: [
    { label: 'Order Number', prop: 'number', width: '200' },
    { label: 'Customer', prop: 'consignee', width: '200' },
    { label: 'Phone', prop: 'phone', width: '180' },
    { label: 'Address', prop: 'address', width: '260' },
    { label: 'Amount', prop: 'amount', width: '180' },
    { label: 'Order Time', prop: 'orderTime', width: '180' },
    { label: 'Canceled At', prop: 'cancelTime', width: '180' },
    { label: 'Remark', prop: 'remark', width: '180' },
    { label: 'Actions', prop: 'actions', width: '260' }
  ]
}

// 当前列
const columns = computed(() => columnsMap[currentStatus.value] || columnsMap['all'])

// 状态文字
const formatStatus = (status) => {
  switch(status){
    case 1: return 'Pending Payment'
    case 2: return 'To Be Accepted'
    case 3: return 'To Be Delivered'
    case 4: return 'Delivering'
    case 5: return 'Completed'
    case 6: return 'Canceled'
    default: return 'Unknown'
  }
}

// 查询接口
const getOrderList = async () => {
  loading.value = true
  try {
    const params = {
      number: queryForm.number?.trim() || undefined, 
      phone: queryForm.phone?.trim() || undefined,   
      page: query.page,
      pageSize: query.pageSize
    }

    // 状态映射
    const statusMap = {
      all: null,        // All
      pending: 2,       // To Be Accepted
      confirmed: 3,     // Accepted
      delivering: 4,    // Delivering
      completed: 5,     // Completed
      canceled: 6       // Canceled
    }
    if(currentStatus.value !== 'all'){
      params.status = statusMap[currentStatus.value]
    }

    // 时间筛选
    if(queryForm.dateRange && queryForm.dateRange.length === 2){
      params.startDate = queryForm.dateRange[0]
      params.endDate = queryForm.dateRange[1]
    }

    const res = await pageOrdersApi(params)
    if(res.code === 1 && res.data){
      orderList.value = res.data.records.map(item => ({
        ...item,
        dishes: item.orderDetails.map(d => d.name).join(', ')
      }))
      total.value = res.data.total
    }

  } catch(err){
    console.error('Failed to fetch order list', err)
  } finally {
    loading.value = false
  }
}


// 操作按钮逻辑
const getActionButtons = (row) => {
  const btns = []

  if(currentStatus.value === 'pending'){
    btns.push({ label: 'Accept', type: 'success', handler: async (r)=> { await acceptOrder(r.id) } })
    btns.push({ label: 'Reject', type: 'danger', handler: async (r)=> { await rejectOrder(r.id) } })
    btns.push({ label: 'View', type: 'primary', handler: (r)=> router.push(`/admin/order/${r.id}`) })
  } else if(currentStatus.value === 'confirmed'){
    btns.push({ label: 'Deliver', type: 'success', handler: async (r)=> { await deliverOrder(r.id) } })
    btns.push({ label: 'Cancel', type: 'danger', handler: async (r)=> { await cancelOrder(r.id) } })
    btns.push({ label: 'View', type: 'primary', handler: (r)=> router.push(`/admin/order/${r.id}`) })
  } else if(currentStatus.value === 'delivering'){
    btns.push({ label: 'Complete', type: 'success', handler: async (r)=> { await completeOrder(r.id) } })
    btns.push({ label: 'View', type: 'primary', handler: (r)=> router.push(`/admin/order/${r.id}`) })
  } else {
    btns.push({ label: 'View', type: 'primary', handler: (r)=> router.push(`/admin/order/${r.id}`) })
  }

  return btns
}

// 按钮操作方法
const acceptOrder = async (id) => {
  try{
    await acceptOrderApi(id)
    getOrderList()
  }catch(err){ console.error(err) }
}
const deliverOrder = async (id) => {
  try{
    await deliverOrderApi(id)
    getOrderList()
  }catch(err){ console.error(err) }
}
const completeOrder = async (id) => {
  try{
    await completeOrderApi(id)
    getOrderList()
  }catch(err){ console.error(err) }
}
const rejectOrder = async (id) => {
  try{
    await rejectOrderApi(id)
    getOrderList()
  }catch(err){ console.error(err) }
}
const cancelOrder = async (id) => {
  try{
    await cancelOrderApi(id)
    getOrderList()
  }catch(err){ console.error(err) }
}

// 查询/重置
const handleSearch = () => {
  query.page = 1
  getOrderList()
}
const handleReset = () => {
  queryForm.number = ''
  queryForm.phone = ''
  queryForm.dateRange = []
  query.page = 1
  getOrderList()
}

// 监听状态变化
watch(currentStatus, () => {
  query.page = 1
  getOrderList()
})

// 标签页切换
const handleStatusChange = (tab) => {
  currentStatus.value = tab.name
}

// 分页
const handlePageChange = () => {
  getOrderList()
}

onMounted(() => {
  getOrderList()
})
</script>


<style scoped>
.order-page {
  background-color: #f3f4f6;
  min-height: 100vh;
  padding: 20px;
}
.filter-bar .el-form-item {
  margin-right: 10px;
}

.el-table {
  font-size: 14px;
}

.el-table th {
  background-color: #f8f8f8 !important;
  font-weight: 600;
  color: #333;
}

.el-table td {
  padding: 10px 8px;
}

</style>
