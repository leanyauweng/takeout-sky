<template>
  <div class="employee-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="title">Employee Management</span> <!-- 员工管理 -->
        </div>
      </template>

      <!-- 条件查询 / Search Filters -->
      <div class="filter-bar" style="display: flex; justify-content: space-between; align-items: center;">
        <div style="display: flex; align-items: center;">
          <span style="margin-right: 6px; font-weight: 500;">Name:</span>
          <el-input
            v-model="query.name"
            placeholder="Enter employee name"
            clearable
            style="width: 200px; margin-right: 10px"
          />
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="handleReset" style="margin-left: 5px">Reset</el-button>
        </div>

        <div>
          <el-button type="success" @click="handleAdd">Add Employee</el-button>
        </div>
      </div>

      <!-- 表格数据 / Table -->
      <el-table
        v-loading="loading"
        :data="employeeList"
        border
        style="width: 100%; margin-top: 20px"
      >
        <el-table-column prop="name" label="Name" align="center" /> <!-- 员工姓名 -->
        <el-table-column prop="username" label="Username" align="center" /> <!-- 账号 -->
        <el-table-column prop="phone" label="Phone" align="center" /> <!-- 手机号 -->
        <el-table-column prop="status" label="Status" align="center"> <!-- 账号状态 -->
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? 'Enabled' : 'Disabled' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="Last Updated" align="center" /> <!-- 最后操作时间 -->
        <el-table-column label="Actions" align="center" width="220"> <!-- 操作 -->
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleEdit(row)">Edit</el-button>
            <el-button
              size="small"
              type="warning"
              @click="handleEnable(row)"
              v-if="row.status === 0"
            >
              Enable
            </el-button>
            <el-button
              size="small"
              type="info"
              @click="handleDisable(row)"
              v-else
            >
              Disable
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 / Pagination -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handlePageChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑员工弹框 / Add/Edit Employee Dialog -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form
        :model="form"
        ref="formRef"
        label-width="100px"
        :rules="rules"
      >
        <el-form-item label="Name" prop="name">
          <el-input v-model="form.name" placeholder="Enter name" />
        </el-form-item>

        <el-form-item label="Username" prop="username">
          <el-input v-model="form.username" placeholder="Enter username" />
        </el-form-item>

        <el-form-item label="Phone" prop="phone">
          <el-input v-model="form.phone" maxlength="11" placeholder="Enter phone number" />
        </el-form-item>

        <el-form-item label="Gender" prop="sex">
      <el-select v-model="form.sex" placeholder="Select gender">
        <el-option label="Male" :value="1" />
        <el-option label="Female" :value="2" />
      </el-select>
    </el-form-item>


        <el-form-item label="ID Number" prop="idNumber">
          <el-input v-model="form.idNumber" maxlength="18" placeholder="Enter ID number" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitForm">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getEmployeePageApi,
  addEmployeeApi,
  updateEmployeeApi,
  startOrStopEmployeeApi,
} from '@/api/employee'

const query = ref({
  name: '',
  page: 1,
  pageSize: 10
})

const employeeList = ref([])
const total = ref(0)
const loading = ref(false)

// 弹框数据 / Dialog data
const dialogVisible = ref(false)
const dialogTitle = ref('Add Employee')
const form = ref({
  id: null,
  name: '',
  username: '',
  phone: '',
  sex: '',
  idNumber: '',
  status: 1
})

const formRef = ref(null)

// 获取员工列表 / Fetch employee list
const getEmployeeList = async () => {
  loading.value = true
  try {
    const res = await getEmployeePageApi(query.value)
    employeeList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

// 查询 / Search
const handleSearch = () => {
  query.value.page = 1
  getEmployeeList()
}

// 重置 / Reset
const handleReset = () => {
  query.value.name = ''
  query.value.page = 1
  getEmployeeList()
}

// 分页 / Pagination
const handlePageChange = () => getEmployeeList()

// 添加员工 / Add employee
const handleAdd = () => {
  dialogTitle.value = 'Add Employee'
  form.value = { id: null, name: '', username: '', phone: '', sex: '', idNumber: '', status: 1 }
  dialogVisible.value = true
  nextTick(() => {
    formRef.value?.resetFields()
  })
}

// 编辑员工 / Edit employee
const handleEdit = (row) => {
  dialogTitle.value = 'Edit Employee'
  dialogVisible.value = true

  nextTick(() => {
    // 重置表单
    formRef.value?.resetFields()

    // 重新赋值（确保 sex 是数字）
    form.value = {
      id: row.id,
      name: row.name,
      username: row.username,
      phone: row.phone,
      idNumber: row.idNumber,
      sex: Number(row.sex),  // ✅ 确保是数字，否则下拉不选中
      status: row.status
    }
  })
}


// 提交表单 / Submit form
const submitForm = async () => {
  formRef.value.validate(async valid => {
    if (!valid) return
    if (form.value.id) {
      await updateEmployeeApi(form.value)
      ElMessage.success('Updated successfully')
    } else {
      await addEmployeeApi(form.value)
      ElMessage.success('Added successfully')
    }
    dialogVisible.value = false
    getEmployeeList()
  })
}

// 启用 / Enable
const handleEnable = async row => {
  await startOrStopEmployeeApi(row.id, 1)
  ElMessage.success('Enabled')
  getEmployeeList()
}

// 禁用 / Disable
const handleDisable = async row => {
  await startOrStopEmployeeApi(row.id, 0)
  ElMessage.success('Disabled')
  getEmployeeList()
}

// 表单校验规则 / Form validation
const rules = {
  username: [
    { required: true, message: 'Please enter a username', trigger: 'blur' },
    { min: 2, max: 20, message: 'Length must be 2-20 characters', trigger: 'blur' }
  ],
  name: [
    { required: true, message: 'Please enter the employee name', trigger: 'blur' },
    { min: 2, max: 20, message: 'Length must be 2-20 characters', trigger: 'blur' }
  ],
  sex: [
    { required: true, message: 'Please select gender', trigger: 'change' },
  ],
  phone: [
    { required: true, message: 'Please enter phone number', trigger: 'blur' },
    { pattern: /^01\d{8}$/, message: 'Phone must start with 01 and be 10 digits', trigger: 'blur' }
  ],
  idNumber: [
    { required: true, message: 'Please enter ID number', trigger: 'blur' },
    { pattern: /^\d{12}$/, message: 'ID number must be 12 digits', trigger: 'blur' }
  ]
}

onMounted(() => getEmployeeList())
</script>

<style scoped>
.employee-page {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title {
  font-size: 18px;
  font-weight: bold;
}
.filter-bar {
  margin-bottom: 10px;
  overflow: hidden;
}
.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
}
</style>
