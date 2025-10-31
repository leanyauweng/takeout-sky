<template>
  <div class="setmeal-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="title">Set Meal Management</span>
        </div>
      </template>

      <!-- 查询条件 -->
      <div
        class="filter-bar"
        style="display: flex; flex-wrap: wrap; align-items: center; gap: 10px;"
      >
        <!-- 查询条件 -->
        <el-form
          inline
          :model="query"
          style="flex: 1; display: flex; align-items: center; gap: 10px;"
        >
          <el-form-item label="" style="margin-bottom: 0; height: 40px;">
            <span style="margin-right: 6px; line-height: 40px;">Set Meal Name:</span>
            <el-input
              v-model="query.name"
              placeholder="Enter set meal name"
              clearable
              style="width: 200px"
            />
          </el-form-item>

          <el-form-item label="" style="margin-bottom: 0; height: 40px;">
            <span style="margin-right: 6px; line-height: 40px;">Category:</span>
            <el-select
              v-model="query.categoryId"
              placeholder="Select category"
              clearable
              style="width: 180px"
            >
              <el-option
                v-for="item in categoryList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="" style="margin-bottom: 0; height: 40px;">
            <span style="margin-right: 6px; line-height: 40px;">Status:</span>
            <el-select
              v-model="query.status"
              placeholder="Select status"
              clearable
              style="width: 120px"
            >
              <el-option label="Inactive" :value="0"></el-option>
              <el-option label="Active" :value="1"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item style="margin-bottom: 0; height: 40px;">
            <el-button type="primary" @click="handleSearch">Search</el-button>
            <el-button @click="handleReset">Reset</el-button>
          </el-form-item>
        </el-form>

        <!-- 右侧按钮 -->
        <div
          style="margin-left: auto; height: 40px; display: flex; align-items: center; gap: 10px;"
        >
          <el-button
            type="danger"
            @click="handleDeleteSelected"
            :disabled="!multipleSelection.length"
          >
            Delete Selected
          </el-button>
          <el-button type="warning" @click="handleAddSetmeal">
            + Add New Set Meal
          </el-button>
        </div>
      </div>

      <!-- 套餐表格 -->
      <el-table
        :data="setmealList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        border
        style="width: 100%; margin-top: 15px"
      >
        <el-table-column type="selection" width="55"></el-table-column>

        <!-- 套餐名称 -->
        <el-table-column prop="name" label="Set Meal Name" align="center" />

        <!-- 套餐图片 -->
        <el-table-column label="Image" align="center">
          <template #default="{ row }">
            <img
              v-if="row.image"
              :src="row.image"
              alt="Set Meal Image"
              style="
                width: 45px;
                height: 45px;
                border-radius: 6px;
                object-fit: cover;
                box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
              "
            />
          </template>
        </el-table-column>

        <!-- 套餐分类 -->
        <el-table-column prop="categoryName" label="Category" align="center" />

        <!-- 套餐价格 -->
        <el-table-column label="Price" align="center">
          <template #default="{ row }">
            <span style="font-weight: 500; color: #333;">RM {{ Number(row.price).toFixed(2) }}</span>
          </template>
        </el-table-column>

        <!-- 售卖状态 -->
        <el-table-column label="Status" align="center">
          <template #default="{ row }">
            <el-switch
              v-model="row.localStatus"
              :active-value="1"
              :inactive-value="0"
              @change="val => handleStatusChange(row, val)"
            />
          </template>
        </el-table-column>

        <!-- 最后操作时间 -->
        <el-table-column
          prop="updateTime"
          label="Last Updated"
          width="280"
          align="center"
        />

        <!-- 操作列 -->
        <el-table-column label="Actions" width="250" align="center">
          <template #default="{ row }">
            <el-button size="small" type="primary" text @click="handleEdit(row)">Edit</el-button>
            <el-button size="small" type="danger" text @click="handleDelete(row)">Delete</el-button>
            <el-button
              size="small"
              type="warning"
              text
              @click="toggleStatus(row)"
            >
              {{ row.localStatus === 1 ? 'Deactivate' : 'Activate' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getSetmealPageApi,
  deleteSetmealApi,
  updateSetmealStatusApi
} from '@/api/setmeal'
import { getCategoryListApi } from '@/api/category'
import { useRouter } from 'vue-router'

const loading = ref(false)
const setmealList = ref([])
const total = ref(0)
const multipleSelection = ref([])
const router = useRouter()

// 查询条件
const query = reactive({
  page: 1,
  pageSize: 10,
  name: '',
  categoryId: '',
  status: ''
})

// 分类下拉列表
const categoryList = ref([])

// 获取分类列表
const fetchCategoryList = async () => {
  const res = await getCategoryListApi({ type: 2 })
  categoryList.value = res.data || []
}

// 获取套餐列表
const fetchSetmealList = async () => {
  loading.value = true
  try {
    const res = await getSetmealPageApi(query)
    setmealList.value = (res.data.records || []).map(item => ({
      ...item,
      localStatus: item.status
    }))
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

// 查询 / 重置
const handleSearch = () => {
  query.page = 1
  fetchSetmealList()
}
const handleReset = () => {
  query.name = ''
  query.categoryId = ''
  query.status = ''
  query.page = 1
  fetchSetmealList()
}

// 分页
const handlePageChange = () => {
  fetchSetmealList()
}

// 选中行变化
const handleSelectionChange = (val) => {
  multipleSelection.value = val
}

// 单个删除
const handleDelete = async (row) => {
  if (row.localStatus === 1) {
    ElMessage.warning(`Setmeal "${row.name}" is currently on sale and cannot be deleted.`)
    return
  }

  try {
    await ElMessageBox.confirm(
      `Are you sure you want to delete the setmeal "${row.name}"?`,
      'Warning',
      { type: 'warning' }
    )
    await deleteSetmealApi([row.id])
    ElMessage.success('Deleted successfully')
    fetchSetmealList()
  } catch {}
}

// 批量删除
const handleDeleteSelected = async () => {
  if (!multipleSelection.value.length) return

  // 检查是否有在售套餐
  const inSaleNames = multipleSelection.value
    .filter(item => item.localStatus === 1)
    .map(item => item.name)

  if (inSaleNames.length > 0) {
    ElMessage.warning(`The following setmeals are on sale and cannot be deleted: ${inSaleNames.join(', ')}`)
    return
  }

  const ids = multipleSelection.value.map(item => item.id)
  try {
    await ElMessageBox.confirm(
      'Are you sure you want to delete the selected setmeals?',
      'Warning',
      { type: 'warning' }
    )
    await deleteSetmealApi(ids)
    ElMessage.success('Deleted successfully')
    fetchSetmealList()
  } catch {}
}

// 状态切换（开关 + 起售/停售按钮）
const handleStatusChange = async (row, newStatus) => {
  if (row.status === newStatus) return
  const oldStatus = row.status
  try {
    await updateSetmealStatusApi({ id: row.id, status: newStatus })
    row.status = newStatus
    row.localStatus = newStatus
    ElMessage.success('Status updated successfully')
  } catch {
    row.localStatus = oldStatus
    ElMessage.error('Failed to update status')
  }
}

const toggleStatus = (row) => {
  const newStatus = row.localStatus === 1 ? 0 : 1
  handleStatusChange(row, newStatus)
}

// 新增套餐
const handleAddSetmeal = () => {
  router.push('/admin/setmeal/add')
}

// 编辑
const handleEdit = (row) => {
  if (!row.id) return
  // 跳转到 AddSetmeal 页面，并传递套餐 id
  router.push({
    path: '/admin/setmeal/add',
    query: { id: row.id }
  })
}

// 初始化
onMounted(() => {
  fetchCategoryList()
  fetchSetmealList()
})
</script>


<style scoped>
.setmeal-page {
  padding: 10px;
}

.filter-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.pagination-bar {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
}

.title {
  font-size: 18px;
  font-weight: bold;
}
</style>
