<template>
    <div class="login-container">
      <div class="login-box">
        <h2 class="title">Delivery App 🛎️</h2>
        <p class="subtitle">Please log in to continue</p>
  
        <el-form :model="loginForm" :rules="rules" ref="formRef" class="form">
          <el-form-item prop="phone">
            <el-input
              v-model="loginForm.phone"
              placeholder="Phone number"
              clearable
              prefix-icon="el-icon-mobile-phone"
            />
          </el-form-item>
  
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              placeholder="Password"
              show-password
              prefix-icon="el-icon-lock"
            />
          </el-form-item>
  
          <el-form-item>
            <div class="button-row">
            <el-button
              type="primary"
              class="login-btn"
              :loading="loading"
              @click="handleLogin"
            >
              Log in
            </el-button>
          </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { ElMessage } from 'element-plus'
  import { userLoginApi } from '@/api/users'

  
  const router = useRouter()
  const formRef = ref(null)
  const loading = ref(false)
  
  const loginForm = ref({
    phone: '',
    password: ''
  })
  
  const rules = {
    phone: [
      { required: true, message: 'Please enter your phone number', trigger: 'blur' }
    ],
    password: [
      { required: true, message: 'Please enter your password', trigger: 'blur' }
    ]
  }
  
  const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true

  try {
    const res = await userLoginApi(loginForm.value) // 调用真实接口
    if (res.code === 1) {
      localStorage.setItem(
    'userInfo',
    JSON.stringify({ token: res.data.authentication, name: res.data.name })
  )
      ElMessage.success('Login successful!')
      router.push('/user/home') // 登录成功跳转首页
    } else {
      ElMessage.error(res.msg || 'Login failed')
    }
  } catch (err) {
    console.error(err)
    ElMessage.error('Login failed')
  } finally {
    loading.value = false
  }
}
  </script>
  
  <style scoped>
  .login-container {
    width: 375px;
    height: 812px;
    margin: 20px auto;
    display: flex;
    justify-content: center;
    align-items: center;
    background: url('@/assets/userlogin.jpg') no-repeat center center / cover;
    border-radius: 30px;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .login-box {
    width: 60%;
    background: #fffaf3;
    padding: 30px 20px;
    border-radius: 20px;
    text-align: center;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  }
  
  .title {
  font-size: 22px; /* 保留原来大小，适合作为副标题 */
  font-weight: 600; /* 比主标题略轻 */
  color: #555; /* 比主标题略浅，和 #333 主标题区分 */
  font-family: "Poppins", "Roboto", "Helvetica Neue", sans-serif; /* 现代字体 */
  letter-spacing: 0.5px; /* 微调字间距，更舒适 */
  line-height: 1.3; /* 让文字更通透 */
  text-align: center;
  margin-bottom: 15px; /* 与下面内容保持间距 */
}

  
.subtitle {
  color: #777; /* 比原来的 #888 稍深一点，更协调 */
  font-size: 16px; /* 稍微大一点，比 14px 更易读 */
  font-weight: 400; /* 轻量感，不抢主标题注意力 */
  font-family: "Poppins", "Roboto", "Helvetica Neue", sans-serif; /* 与主标题统一字体 */
  text-align: center;
  margin-bottom: 25px; /* 与输入框保持适当间距 */
  line-height: 1.4; /* 阅读舒适 */
}

  
  .form {
    margin-top: 20px;
  }
  
  .login-btn {
    width: 100%;
    border-radius: 20px;
  }

  .button-row .el-button {
  background: #d29d5c;
  color: #ffffff;
  width: 220px; /* 调整成你想要的长度 */
  padding: 8px;
  font-weight: bold;
  margin-left: 0px;
}
  </style>
  