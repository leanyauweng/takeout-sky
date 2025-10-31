<template>
  <div class="dish-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="title">Dish Management</span>
        </div>
      </template>

      <!-- 查询条件 / Filter Bar -->
      <div class="filter-bar" style="display: flex; flex-wrap: wrap; align-items: center; gap: 10px;">
        <el-form inline :model="query" style="flex: 1; display: flex; align-items: center; gap: 10px;">
          <el-form-item label="" style="margin-bottom: 0;">
            <span style="margin-right: 6px;">Dish Name:</span>
            <el-input v-model="query.name" placeholder="Enter dish name" clearable style="width: 200px" />
          </el-form-item>

          <el-form-item label="" style="margin-bottom: 0;">
            <span style="margin-right: 6px;">Category:</span>
            <el-select v-model="query.categoryId" placeholder="Select category" clearable style="width: 180px">
              <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>

          <el-form-item label="" style="margin-bottom: 0;">
            <span style="margin-right: 6px;">Status:</span>
            <el-select v-model="query.status" placeholder="Select status" clearable style="width: 120px">
              <el-option label="Unavailable" :value="0"></el-option>
              <el-option label="Available" :value="1"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item style="margin-bottom: 0;">
            <el-button type="primary" @click="handleSearch">Search</el-button>
            <el-button @click="handleReset">Reset</el-button>
          </el-form-item>
        </el-form>

        <div style="margin-left: auto; display: flex; align-items: center; gap: 10px;">
          <el-button type="danger" @click="handleDeleteSelected" :disabled="!multipleSelection.length">Delete Selected</el-button>
          <el-button type="warning" @click="handleAddDish">+ Add Dish</el-button>
        </div>
      </div>

      <!-- 表格 / Table -->
      <el-table
        :data="dishList"
        v-loading="loading"
        border
        @selection-change="handleSelectionChange"
        style="width: 100%; margin-top: 15px;"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="Dish Name" align="center" />
        <el-table-column prop="categoryName" label="Category" align="center" />
        <el-table-column label="Price" align="center">
          <template #default="{ row }">
            <span>RM {{ Number(row.price).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Image" align="center">
          <template #default="{ row }">
            <img v-if="row.image" :src="row.image" alt="" style="width: 45px; height: 45px; border-radius: 6px; object-fit: cover;" />
          </template>
        </el-table-column>
        <el-table-column label="Status" align="center">
          <template #default="{ row }">
            <el-switch v-model="row.localStatus" :active-value="1" :inactive-value="0" @change="val => handleStatusChange(row, val)" />
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="Last Updated" width="200" align="center" />
        <el-table-column label="Actions" width="250" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" text @click="handleEdit(row)">Edit</el-button>
            <el-button type="danger" size="small" text @click="handleDelete(row)">Delete</el-button>
            <el-button type="warning" size="small" text @click="toggleStatus(row)">
              {{ row.localStatus === 1 ? 'Dectivate' : 'Activate' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 / Pagination -->
      <div class="pagination-bar">
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.pageSize"
          :total="total"
          :page-sizes="[5, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handlePageChange"
          @size-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDishPageApi, deleteDishApi, updateDishStatusApi } from '@/api/dish'
import { getCategoryListApi } from '@/api/category'

const router = useRouter()
const loading = ref(false)
const dishList = ref([])
const total = ref(0)
const multipleSelection = ref([])

const query = reactive({ page: 1, pageSize: 10, name: '', categoryId: '', status: '' })
const categoryList = ref([])

// 获取菜品列表 / Fetch dish list
const fetchDishList = async () => {
  loading.value = true
  try {
    const res = await getDishPageApi(query)
    dishList.value = (res.data.records || []).map(d => ({ ...d, localStatus: d.status }))
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

// 获取分类列表 / Fetch category list
const fetchCategoryList = async () => {
  const res = await getCategoryListApi({ type: 1 })
  categoryList.value = res.data || []
}

// 查询 / Search
const handleSearch = () => { query.page = 1; fetchDishList() }
// 重置 / Reset
const handleReset = () => { query.name = ''; query.categoryId = ''; query.status = ''; query.page = 1; fetchDishList() }

// 分页 / Pagination
const handlePageChange = () => { fetchDishList() }

// 选中行 / Row selection
const handleSelectionChange = val => { multipleSelection.value = val }

// 单个删除 / Delete one
const handleDelete = async row => {
  if (row.localStatus === 1) {
    ElMessage.warning(`Dish "${row.name}" is available and cannot be deleted`)
    return
  }
  try {
    await ElMessageBox.confirm(`Are you sure to delete dish "${row.name}"?`, 'Warning', { type: 'warning' })
    await deleteDishApi([row.id])
    ElMessage.success('Deleted successfully')
    fetchDishList()
  } catch {}
}

// 批量删除 / Delete selected
const handleDeleteSelected = async () => {
  if (!multipleSelection.value.length) return
  const inSale = multipleSelection.value.filter(d => d.localStatus === 1)
  if (inSale.length > 0) {
    ElMessage.warning(`Cannot delete these dishes as they are available: ${inSale.map(d => d.name).join(', ')}`)
    return
  }
  const ids = multipleSelection.value.map(d => d.id)
  try {
    await ElMessageBox.confirm('Are you sure to delete selected dishes?', 'Warning', { type: 'warning' })
    await deleteDishApi(ids)
    ElMessage.success('Deleted successfully')
    fetchDishList()
  } catch {}
}

// 状态切换 / Toggle status
const handleStatusChange = async (row, newStatus) => {
  if (row.status === newStatus) return
  const oldStatus = row.status
  try {
    await updateDishStatusApi(row.id, newStatus)
    row.status = newStatus
    row.localStatus = newStatus
    ElMessage.success('Status updated successfully')
  } catch {
    row.localStatus = oldStatus
    ElMessage.error('Status update failed')
  }
}
const toggleStatus = row => handleStatusChange(row, row.localStatus === 1 ? 0 : 1)

// 新增 / 编辑 / Add / Edit
const handleAddDish = () => router.push('/admin/dish/add')
const handleEdit = row => router.push({ path: '/admin/dish/add', query: { id: row.id } })

onMounted(() => {
  fetchCategoryList()
  fetchDishList()
})
</script>


<style scoped>
.dish-page { padding: 10px }
.filter-bar { justify-content: space-between; margin-bottom: 15px }
.pagination-bar { display: flex; justify-content: flex-end; margin-top: 15px }
.title { font-size: 18px; font-weight: bold }
</style>
