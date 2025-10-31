<template>
  <div class="category-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="title">Category Management</span>
        </div>
      </template>

      <!-- 查询条件 -->
      <div class="filter-bar" style="display: flex; align-items: center; gap: 10px; flex-wrap: wrap;">
        <!-- 分类名称 -->
        <el-form-item label="" style="margin-bottom: 0;">
          <span style="margin-right: 6px;">Name:</span>
          <el-input
            v-model="query.name"
            placeholder="Enter category name"
            clearable
            style="width: 200px"
          />
        </el-form-item>

        <!-- 分类类型 -->
        <el-form-item label="" style="margin-bottom: 0;">
          <span style="margin-right: 6px;">Category Type:</span>
          <el-select
            v-model="query.type"
            placeholder="Select category type"
            clearable
            style="width: 180px"
          >
            <el-option label="Dish Category" :value="1" />
            <el-option label="Setmeal Category" :value="2" />
          </el-select>
        </el-form-item>

        <!-- 查询 / 重置 -->
        <el-button type="primary" @click="getList">Search</el-button>
        <el-button @click="resetQuery">Reset</el-button>

        <div style="flex: 1"></div>

        <!-- 新增按钮 -->
        <el-button type="success" @click="openDialog('add', 1)" style="margin-right: 10px">
          + Add Dish Category
        </el-button>
        <el-button type="warning" @click="openDialog('add', 2)">
          + Add Setmeal Category
        </el-button>
      </div>

      <!-- 分类表格 -->
      <el-table
        :data="tableData"
        v-loading="loading"
        border
        style="width: 100%; margin-top: 15px"
      >
        <el-table-column prop="name" label="Category Name" align="center" />
        <el-table-column prop="type" label="Category Type" align="center">
          <template #default="{ row }">
            <el-tag type="info">{{ row.type === 1 ? 'Dish Category' : 'Setmeal Category' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="Sort" align="center" />
        <el-table-column label="Status" align="center">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="changeStatus(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="Last Updated" align="center" />
        <el-table-column label="Actions" width="250" align="center">
          <template #default="{ row }">
            <el-button size="small" type="primary" text @click="openDialog('edit', null, row)">
              Edit
            </el-button>
            <el-button size="small" type="danger" text @click="handleDelete(row.id)">
              Delete
            </el-button>
            <el-button
              size="small"
              type="warning"
              text
              v-if="row.status === 1"
              @click="changeStatus(row, 0)"
            >
              Disable
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
          @current-change="getList"
          @size-change="getList"
        />
      </div>
    </el-card>

    <!-- 新增/修改弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="450px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="Category Name" prop="name">
          <el-input v-model="form.name" placeholder="Enter category name" />
        </el-form-item>
        <el-form-item label="Category Type" prop="type">
          <el-select v-model="form.type" placeholder="Select category type">
            <el-option label="Dish Category" :value="1" />
            <el-option label="Setmeal Category" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="Sort" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit">Confirm</el-button>
      </template>
    </el-dialog>
  </div>
</template>


<script setup>
import { ref, reactive, onMounted } from 'vue'
import {
  getCategoryPageApi,
  addCategoryApi,
  updateCategoryApi,
  deleteCategoryApi,
  updateCategoryStatusApi
} from '@/api/category'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const query = reactive({
  name: '',
  type: null,
  page: 1,
  pageSize: 10
})

// 表单相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = reactive({
  id: null,
  name: '',
  type: null,
  sort: 0
})
const rules = {
  name: [{ required: true, message: 'Please enter category name', trigger: 'blur' }],
  type: [{ required: true, message: 'Please select category type', trigger: 'change' }]
}

const getList = async () => {
  loading.value = true
  try {
    const res = await getCategoryPageApi(query)
    tableData.value = res.data.records || res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  query.name = ''
  query.type = null
  query.page = 1
  getList()
}

const openDialog = (mode, type = null, row = null) => {
  dialogVisible.value = true
  if (mode === 'add') {
    dialogTitle.value = type === 1 ? 'Add new dish category' : 'Add new setmeal category'
    Object.assign(form, { id: null, name: '', type, sort: 0 })
  } else if (mode === 'edit' && row) {
    dialogTitle.value = 'Edit Category'
    Object.assign(form, { ...row })
  }
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    const api = form.id ? updateCategoryApi : addCategoryApi
    await api(form)
    ElMessage.success(form.id ? 'Modification successful' : 'Added successfully')
    dialogVisible.value = false
    getList()
  })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('Are you sure to delete this category?', 'Warning', { type: 'warning' })
    .then(async () => {
      await deleteCategoryApi(id)
      ElMessage.success('Delete successful')
      getList()
    })
    .catch(() => {})
}

const changeStatus = async (row, forceStatus) => {
  const newStatus = forceStatus !== undefined ? forceStatus : row.status
  await updateCategoryStatusApi(row.id, newStatus)
  ElMessage.success(newStatus === 1 ? 'Enabled' : 'Disabled')
  getList()
}

onMounted(getList)
</script>

<style scoped>
.category-page {
  padding: 10px;
}

.filter-bar {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
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
