<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { loginApi } from '@/api/employee'

const router = useRouter()
const formRef = ref(null)

const loginForm = ref({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: 'Please enter your username', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Please enter your password', trigger: 'blur' }
  ]
}

const login = async () => {
  await formRef.value.validate()
  const result = await loginApi(loginForm.value)
  if (result.code === 1) {
    localStorage.setItem('adminInfo', JSON.stringify(result.data))
    ElMessage.success('Login successful')
    router.push('/admin')
  } else {
    ElMessage.error(result.msg || 'Login failed')
  }
}

const reset = () => {
  formRef.value.resetFields()
  loginForm.value = {username: '', password: ''}
}
</script>

<template>
  <div class="container">
    <el-card class="login-form">
      <el-form :model="loginForm" :rules="rules" ref="formRef">
        <p class="title">Food Delivery Admin</p>
        <div class="field">
        <el-form-item label="Username" prop="username">
          <el-input v-model="loginForm.username" placeholder="Please enter your username" ></el-input>
        </el-form-item>
      </div>
        <div class="field">
        <el-form-item label="Password" prop="password">
          <el-input v-model="loginForm.password" placeholder="Please enter your password" show-password ></el-input>
        </el-form-item>
      </div>
        <el-form-item>
  <div class="button-row">
    <el-button type="primary" @click="login">Login</el-button>
    <el-button @click="reset">Reset</el-button>
  </div>
</el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: url('@/assets/adminlogin.jpg') no-repeat center/cover;
}

.login-form {
  width: 450px;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(100, 80, 50, 0.1);
  background: #fffaf3;
  color: #333;
  margin-left: 400px;
  margin-top: -180px;
}


.title {
  text-align: center;
  font-weight: 600; /* 不必太粗，400~600即可 */
  color: #333; /* 或 #222 更清晰 */
  font-size: 36px; /* 稍微小一点，视觉更协调 */
  margin-bottom: 25px;
  font-family: "Poppins", "Roboto", "Helvetica Neue", sans-serif;
  letter-spacing: 1px; /* 微调字间距，更高级 */
  line-height: 1.2; /* 高度舒适 */
}


.button-row .el-button {
  background: #d29d5c;
  color: #ffffff;
  padding: 8px;
  width: 120px; /* 调整成你想要的长度 */
  font-weight: bold;
  margin-left: 55px;
}



</style>