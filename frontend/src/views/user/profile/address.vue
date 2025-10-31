<template>
  <div class="profile-page">
    <!-- 背景层 -->
    <div class="background"></div>
    <!-- 用户信息 -->
    <div class="title">
      <h2>My Address</h2>
    </div>

    <div class="content-card">
      <!-- 地址列表 -->
      <div class="address-section">
        <div class="address-header">
          <h3>Delivery Address</h3>
          <el-button class="add-btn" @click="goToAddAddress" size="small">+ Add Address</el-button>
        </div>

        <div v-if="addressList.length === 0" class="no-address">
          No address yet, please click "Add Address" to add one～
        </div>

        <el-card
          v-for="addr in addressList"
          :key="addr.id"
          class="address-card"
          :class="{ 'default-address': addr.isDefault === 1 }"
        >
          <div class="address-info">
            <div class="top-row">
              <span class="name">Name: {{ addr.consignee }}</span>
              <el-tag v-if="addr.isDefault === 1" type="success" size="small">Default</el-tag>
            </div>
            <div class="phone">
              Phone: {{ addr.phone }}
            </div>
            <div class="detail">
              Address: {{ addr.detail }}
            </div>
          </div>

          <div class="address-actions">
            <el-button size="small" @click="setDefault(addr)">Set as Default</el-button>
            <el-button class="btn-text blue" size="small" @click="editAddress(addr)">Edit</el-button>
            <el-button class="btn-text red" size="small" @click="deleteAddress(addr.id)">Delete</el-button>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAddressListApi, setDefaultAddressApi, deleteAddressApi } from '@/api/addressBook' // API path
import { useRouter } from 'vue-router'

const router = useRouter()
const addressList = ref([])

// 获取地址列表
const getAddressList = async () => {
  const res = await getAddressListApi()
  addressList.value = res.data || []
}

// 设为默认地址
const setDefault = async (addr) => {
  await setDefaultAddressApi({ id: addr.id })
  ElMessage.success('Set as default successfully')
  getAddressList()
}

// 删除地址
const deleteAddress = async (id) => {
  ElMessageBox.confirm('Are you sure you want to delete this address?', 'Confirmation', {
    type: 'warning',
  })
    .then(async () => {
      await deleteAddressApi(id)
      ElMessage.success('Deleted successfully')
      getAddressList()
    })
    .catch(() => {})
}

// 跳转新增地址
const goToAddAddress = () => {
  router.push('/user/add-address')
}

// 跳转编辑地址
const editAddress = (addr) => {
  router.push({ path: '/user/edit-address', query: { id: addr.id } })
}

const formatTime = (time) => {
  return time ? new Date(time).toLocaleString() : '-'
}

onMounted(() => {
  getAddressList()
})
</script>


<style scoped>
.profile-page {
  position: relative;
  min-height: 50vh;
}

.title {
  background-color: #fff;
  color: #000;
  text-align: center;
  font-size: 13px;
  font-weight: bold;
  border-bottom: 1px solid #eee;
}

.background {
  background: url('@/assets/userlogin.jpg') no-repeat center/cover;
  filter: blur(6px);
  position: absolute;
  inset: 0;
  z-index: -1;
}

.content-card {
  max-width: 700px;
  width: 100%;
  max-height: 90vh;     /* 保证不超出屏幕 */
  overflow-y: auto;     /* 如果内容太多，仅内部可滚动 */
  background: rgba(255, 255, 255, 0.85);
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}


.info p {
  margin: 4px 0;
  color: #555;
}

.address-section {
  margin-top: 10px;
  max-height: 679px;
  overflow-y: auto
}

.address-section::-webkit-scrollbar {
  width: 6px;
}

.address-section::-webkit-scrollbar-thumb {
  background-color: rgba(0,0,0,0.2);
  border-radius: 3px;
}

.address-section::-webkit-scrollbar-track {
  background-color: rgba(0,0,0,0.05);
}

.address-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.address-header h3 {
  font-size: 16px;      /* 字体稍小一点 */
  font-weight: 600;     /* 保留一定的视觉权重 */
  color: #333;           /* 深灰色，和主标题区分 */
  margin: 0;
}

.add-btn {
  background-color: white !important;  /* 白色背景 */
  color: #e74c3c !important;           /* 红色字体 */
  border: none !important;             /* 去掉边框 */
  font-weight: 500;
  transition: 0.2s;
}

.add-btn:hover {
  background-color: #ffecec !important; /* 悬浮时淡红背景 */
  color: #c0392b !important;
}


.address-card {
  margin-top: 12px;
  padding: 12px;
  transition: 0.3s;
}

.address-card.default-address {
  border-left: 4px solid #67c23a;
  background-color: #f6ffed;
}

.address-info .top-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  font-weight: 500;
  margin-bottom: 4px;
}

.address-info .detail {
  color: #666;
  margin-top: 4px;
}

.address-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 8px;
}

.no-address {
  color: #999;
  text-align: center;
  padding: 40px 0;
}

/* 通用文字按钮样式 */
.btn-text {
  border: none !important;
  box-shadow: none !important;
  padding: 4px 8px;
  font-weight: 500;
  cursor: pointer;
}

/* 鼠标悬停时轻微变化 */
.btn-text:hover {
  opacity: 0.8;
}

/* 蓝色文字（编辑） */
.btn-text.blue {
  color: #409eff;
}

/* 红色文字（删除） */
.btn-text.red {
  color: #f56c6c;
}
</style>
