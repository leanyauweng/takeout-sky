<template>
  <div class="address-edit-page">
    <el-card class="form-card">
      <div class="header">
        <span class="back-btn" @click="router.back()">←</span>
        <h2>Add New Address</h2>
      </div>

      <el-form
        :model="form"
        :rules="rules"
        ref="addressFormRef"
        label-width="90px"
        class="address-form"
      >
        <!-- 收货人 -->
        <el-form-item label="Consignee" prop="consignee">
          <el-input v-model="form.consignee" placeholder="Please enter the consignee name" />
        </el-form-item>

        <!-- 性别 -->
        <el-form-item label="Gender" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio label="1">Male</el-radio>
            <el-radio label="2">Female</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 手机号 -->
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="form.phone" placeholder="Please enter phone number" maxlength="10" />
        </el-form-item>

        <!-- 州属 -->
        <el-form-item label="State" prop="stateName">
          <el-input v-model="form.stateName" placeholder="Please enter state name" />
        </el-form-item>

        <!-- 城市 -->
        <el-form-item label="City" prop="cityName">
          <el-input v-model="form.cityName" placeholder="Please enter city/town name" />
        </el-form-item>

        <!-- 邮编 -->
        <el-form-item label="Postcode" prop="postcode">
          <el-input v-model="form.postcode" placeholder="Please enter postal code" maxlength="10" />
        </el-form-item>

        <!-- 详细地址 -->
        <el-form-item label="Detail Address" prop="detail">
          <el-input v-model="form.detail" placeholder="Please enter detailed address (street, floor, etc.)" />
        </el-form-item>

        <!-- 标签 -->
        <el-form-item label="Label" prop="label">
          <el-input v-model="form.label" placeholder="e.g. Home / Office / School" />
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item class="form-actions">
          <el-button class="save-btn" @click="handleSubmit">Submit</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { addAddressApi } from '@/api/addressBook'
import { ElMessage } from 'element-plus'

const router = useRouter()
const addressFormRef = ref(null)

const form = reactive({
  consignee: '',
  sex: '',
  phone: '',
  stateName: '',
  cityName: '',
  postcode: '',
  detail: '',
  label: '',
  isDefault: false
})

// 校验规则
const rules = {
  consignee: [{ required: true, message: 'Please enter consignee name', trigger: 'blur' }],
  sex: [{ required: true, message: 'Please select gender', trigger: 'change' }],
  phone: [{ required: true, message: 'Please enter phone number', trigger: 'blur' }],
  stateName: [{ required: true, message: 'Please enter state name', trigger: 'blur' }],
  cityName: [{ required: true, message: 'Please enter city/town name', trigger: 'blur' }],
  postcode: [{ required: true, message: 'Please enter postal code', trigger: 'blur' }],
  detail: [{ required: true, message: 'Please enter detailed address', trigger: 'blur' }],
  label: [{ required: true, message: 'Please enter label', trigger: 'blur' }]
}

// 提交方法
const handleSubmit = () => {
  addressFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      const data = { ...form, isDefault: form.isDefault ? 1 : 0 }
      await addAddressApi(data)
      ElMessage.success('Address added successfully')
      router.back()
    } catch (err) {
      console.error(err)
      ElMessage.error('Failed to add address')
    }
  })
}
</script>

  
  <style scoped>
  .address-edit-page {
    position: relative;
    height: 55vh;
    display: flex;
    justify-content: center;
    align-items: center;
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
  left: 25px;
  top: 40px;  
  font-size: 30px;
  cursor: pointer;
  user-select: none;
}
  
  .form-card {
    width: 95%;
    max-width: 520px;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 12px;
    padding: 15px;
  }
  
  .title {
  background-color: #fff;
  color: #000;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  border-bottom: 1px solid #eee;
}
  
  .address-form {
    margin-top: 20px;
  }

  .form-actions {
  text-align: center;
}

.save-btn {
  background-color: #67c23a !important; /* Element Plus 成功绿色 */
  color: white !important;
  border: none;
  border-radius: 8px;
  padding: 10px 40px;
  font-weight: 600;
  font-size: 15px;
  box-shadow: 0 3px 8px rgba(103, 194, 58, 0.3);
  transition: 0.2s;
  width: 150px;
}

.save-btn:hover {
  background-color: #5daf34 !important;
}
  </style>
  