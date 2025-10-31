<script setup>
import { EditPen, SwitchButton } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { ref, onMounted, onUnmounted } from 'vue'
import { changePasswordApi } from '@/api/employee'
import { getShopStatusApi, setShopStatusApi } from '@/api/shop'
import { 
  Monitor,
  DataLine,
  Tickets,
  ShoppingBag,
  Dish,
  Collection,
  User
} from '@element-plus/icons-vue'

const router = useRouter()
const loginName = ref('')
const shopStatus = ref('') // 店铺状态
const dialogVisible = ref(false)
const formRef = ref(null)
const changePasswordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 修改密码规则
const rules = {
  oldPassword: [{ required: true, message: 'Please enter old password', trigger: 'blur' }],
  newPassword: [{ required: true, message: 'Please enter new password', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: 'Please confirm new password', trigger: 'blur' },
    { validator: (rule, value) => value === changePasswordForm.value.newPassword, message: 'Passwords do not match', trigger: 'blur' }
  ]
}

// 登出
const logOut = () => {
  ElMessageBox.confirm('Are you sure to log out?', 'Warning', {
    confirmButtonText: 'Confirm',
    cancelButtonText: 'Cancel',
    type: 'warning'
  }).then(() => {
    localStorage.removeItem('loginUser')
    router.push('/login')
    ElMessage.success('Logged out successfully')
  })
}

// 修改密码
const changePassword = () => { dialogVisible.value = true }
const submitChangePassword = async () => {
  await formRef.value.validate()
  const res = await changePasswordApi(changePasswordForm.value)
  if(res.code === 1){
    ElMessage.success('Password changed successfully, please log in again')
    localStorage.removeItem('loginUser')
    router.push('/login')
  } else {
    ElMessage.error(res.msg || 'Change failed')
  }
}

// 获取店铺状态
const fetchShopStatus = async () => {
  try {
    const res = await getShopStatusApi()
    shopStatus.value = res.data === 1 ? 'Open' : 'Closed'
  } catch (error) {
    console.error('Failed to fetch shop status', error)
    shopStatus.value = 'Unknown'
  }
}

// 切换店铺状态
const toggleShopStatus = async () => {
  const newStatus = shopStatus.value === 'Open' ? 0 : 1
  try {
    await setShopStatusApi(newStatus)
    shopStatus.value = newStatus === 1 ? 'Open' : 'Closed'
    ElMessage.success('Shop status updated')
  } catch (error) {
    ElMessage.error('Update failed')
  }
}

let ws = null
let reconnectTimer = null
const connectWebSocket = () => {
  const sid = JSON.parse(localStorage.getItem('loginUser') || '{}')?.id || 'admin'
  ws = new WebSocket(`ws://localhost:8080/ws/${sid}`)

  ws.onopen = () => { console.log('✅ WebSocket connected') }
  ws.onmessage = (event) => {
    try {
      const data = JSON.parse(event.data)
      if (data.type === 1) {
        // 新订单
        ElMessage({
          message: `📦 New order received! Order No: ${data.content}`,
          type: 'success',
          duration: 4000
        })
        playSound()
      } else if (data.type === 2) {
        // 催单消息
        ElMessage({
          message: `⏰ Customer is urging! Order No: ${data.content}`,
          type: 'warning',
          duration: 4000
        })
        playSound()
      }
    } catch (err) {
      console.error(err)
    }
  }

  ws.onclose = () => { reconnectTimer = setTimeout(connectWebSocket, 3000) }
  ws.onerror = () => ws.close()
}

const playSound = () => {
  try {
    const audio = new Audio('/sounds/new-order.mp3')
    audio.play().catch(err => console.warn('Audio play failed:', err))
  } catch(e) { console.warn(e) }
}

onMounted(() => {
  const loginUser = JSON.parse(localStorage.getItem('loginUser') || '{}')
  if (loginUser && loginUser.token) loginName.value = loginUser.name
  fetchShopStatus()
  connectWebSocket()
})

onUnmounted(() => {
  if(ws) ws.close()
  if(reconnectTimer) clearTimeout(reconnectTimer)
})
</script>

<template>
  <div class="container">
    <el-container>
      <!-- 顶部区域 -->
      <el-header class="header-bar"> 
        <div class="header-left">
          <img src="@/assets/logo.jpg" alt="logo" class="logo-img" />
        </div>

        <div class="header-center">
          Take-Out Management System
          <!-- 店铺状态显示 + 点击切换 + hover 提示 -->
          <div
            class="shop-status"
            :class="shopStatus === 'Open' ? 'open' : shopStatus === 'Closed' ? 'closed' : 'unknown'"
            @click="toggleShopStatus"
            :title="'Click to toggle shop status'"
          >
            {{ shopStatus }}
          </div>
        </div>

        <div class="header-right">
          <el-button type="text" @click="changePassword"><el-icon><EditPen /></el-icon>Change Password</el-button>
          <el-button type="text" @click="logOut"><el-icon><SwitchButton /></el-icon>Logout [{{loginName}}]</el-button>
        </div>
      </el-header>

      <el-container>
        <!-- 左侧区域 -->
        <el-aside width="200px">
          <el-scrollbar>
            <el-menu router :default-active="$route.path">
              <el-menu-item index="/admin/dashboard" class="fixed-gray-bg menu-item-space">
                <el-icon><Monitor /></el-icon>Dashboard
              </el-menu-item>
              <el-menu-item index="/admin/chart" class="fixed-gray-bg menu-item-space">
                <el-icon><DataLine /></el-icon>Analytics
              </el-menu-item>
              <el-menu-item index="/admin/orderDetails" class="fixed-gray-bg menu-item-space">
                <el-icon><Tickets /></el-icon>Orders
              </el-menu-item>
              <el-menu-item index="/admin/setmeal" class="fixed-gray-bg menu-item-space">
                <el-icon><ShoppingBag /></el-icon>Set Meals
              </el-menu-item>
              <el-menu-item index="/admin/dish" class="fixed-gray-bg menu-item-space">
                <el-icon><Dish /></el-icon>Dishes
              </el-menu-item>
              <el-menu-item index="/admin/category" class="fixed-gray-bg menu-item-space">
                <el-icon><Collection /></el-icon>Categories
              </el-menu-item>
              <el-menu-item index="/admin/employee" class="fixed-gray-bg menu-item-space">
                <el-icon><User /></el-icon>Employees
              </el-menu-item>
            </el-menu>
          </el-scrollbar>
        </el-aside>

        <el-main><router-view /></el-main>
      </el-container>
    </el-container>
  </div>

  <!-- 修改密码弹窗 -->
  <el-dialog v-model="dialogVisible" title="Change Password" width="600px">
    <el-form :model="changePasswordForm" :rules="rules" ref="formRef" label-width="180px" label-position="left">
      <el-form-item label="Old Password" prop="oldPassword">
        <el-input v-model="changePasswordForm.oldPassword" type="password"></el-input>
      </el-form-item>
      <el-form-item label="New Password" prop="newPassword">
        <el-input v-model="changePasswordForm.newPassword" type="password"></el-input>
      </el-form-item>
      <el-form-item label="Confirm New Password" prop="confirmPassword">
        <el-input v-model="changePasswordForm.confirmPassword" type="password"></el-input>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Cancel</el-button>
      <el-button type="primary" @click="submitChangePassword">Submit</el-button>
    </template>
  </el-dialog>
</template>


<style scoped>
.fixed-gray-bg {
  background-color: #c0c0c0 !important;
  color: #333;
  font-weight: 500;
}
.fixed-gray-bg:hover {
  background-color: #e0e0e0 !important;
}
.menu-item-space { margin-bottom: 2px; }

.header-bar {
  display: flex; justify-content: space-between; align-items: center; height: 60px;
  padding: 0 20px; background-color: #fbbb4a; color: #000;
}
.header-left { display: flex; align-items: center; }
.logo-img { height: 55px; }
.header-center {
  flex-grow: 1;
  display: flex;
  align-items: center;

  font-weight: 600; /* 半粗，现代感 */
  font-size: 28px; /* 保持可读性 */
  font-family: "Poppins", "Roboto", "Helvetica Neue", sans-serif; /* 现代字体 */
  color: #333; /* 深灰色，比纯黑温和 */
  letter-spacing: 1px; /* 微调字间距，更高级 */
  line-height: 1.2; /* 垂直空间舒适 */
}

.header-right > .el-button { margin-left: 12px; color: #000; font-size: 18px; }

/* 店铺状态样式 */
.shop-status {
  font-weight: bold;
  margin-left: 20px;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 16px;
  color: #fff;
  cursor: pointer;
  transition: background-color 0.3s;
}
.shop-status.open { background-color: #4caf50; }    /* 绿色 */
.shop-status.closed { background-color: #f44336; }  /* 红色 */
.shop-status.unknown { background-color: #999; }    /* 灰色 */

/* hover 提示更明显 */
.shop-status:hover {
  opacity: 0.8;
}
</style>
