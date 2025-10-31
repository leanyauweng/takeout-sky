<template>
  <div class="add-dish-container">
    <el-card shadow="hover">
      <h2>Add Dishes</h2>
      <div class="dish-layout">
        <!-- 左侧分类 -->
        <div class="left-categories">
          <el-menu
            :default-active="activeCategoryId"
            class="el-menu-vertical-demo"
            @select="handleCategoryClick"
          >
            <el-menu-item
              v-for="cat in categoryList"
              :key="cat.id"
              :index="String(cat.id)"
            >
              {{ cat.name }}
            </el-menu-item>
          </el-menu>
        </div>

        <!-- 中间菜品列表 -->
        <div class="center-dishes">
          <div v-if="dishList.length === 0" class="no-dish">No dishes available</div>

          <div
            v-for="dish in dishList"
            :key="dish.id"
            class="dish-row"
            :class="{ selected: isSelected(dish.id) }"
            @click="addDishToSelected(dish)"
          >
            <img v-if="dish.image" :src="dish.image" alt="" class="dish-row-image" />

            <div class="dish-name">{{ dish.name }}</div>
            <div
              class="dish-status"
              :class="dish.status === 1 ? 'on-sale' : 'off-sale'"
            >
              {{ dish.status === 1 ? 'Available' : 'Unavailable' }}
            </div>
            <div class="dish-price">RM{{ dish.price }}</div>
          </div>
        </div>

        <!-- 右侧已选菜品 -->
        <div class="right-selected">
          <h3>Selected Dishes ({{ selectedDishes.length }})</h3>

          <div
            v-for="dish in selectedDishes"
            :key="dish.id"
            class="dish-row selected"
          >
            <!-- 图片 -->
            <img
              v-if="dish.image"
              :src="dish.image"
              alt=""
              class="dish-row-image"
            />

            <!-- 名称 -->
            <div class="dish-name">{{ dish.name }}</div>

            <!-- 状态 -->
            <div
              class="dish-status"
              :class="dish.status === 1 ? 'on-sale' : 'off-sale'"
            >
              {{ dish.status === 1 ? 'Available' : 'Unavailable' }}
            </div>

            <!-- 价格 -->
            <div class="dish-price">RM{{ dish.price }}</div>

            <!-- 数量 + 删除按钮 -->
            <div class="dish-actions">
              <span>× {{ dish.quantity }}</span>
              <el-button
                type="text"
                size="small"
                @click.stop="removeSelectedDish(dish.id)"
              >
                ❌
              </el-button>
            </div>
          </div>

          <!-- 总价 -->
          <div class="total">Total: RM{{ totalPrice.toFixed(2) }}</div>

          <!-- 底部按钮 -->
          <div class="actions">
            <el-button @click="handleCancel">Cancel</el-button>
            <el-button type="primary" @click="handleSave">Save</el-button>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>


<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCategoryListApi } from '@/api/category'
import { getDishListByCategoryApi } from '@/api/dish'

const router = useRouter()
const route = useRoute()

const selectedDishes = ref([])
const dishList = ref([])
const categoryList = ref([])
const activeCategoryId = ref(null)
const setmealId = ref(null)

// 表单信息
const form = ref({
  name: '',
  categoryId: null,
  price: '',
  image: '',
  description: ''
})

// 添加菜品
const addDishToSelected = (dish) => {
  const existing = selectedDishes.value.find(d => d.id === dish.id)
  if (existing) {
    existing.quantity += 1
  } else {
    selectedDishes.value.push({ 
      id: dish.id,
  name: dish.name,
  price: Number(dish.price) || 0,
  quantity: 1,
  image: dish.image || '',      // 保证有图片
  status: dish.status ?? 1      // 保证有状态
    })
  }
  console.log('🟢 addDishToSelected - current selectedDishes:', selectedDishes.value)
}


// 删除菜品（数量 -1）
const removeSelectedDish = (dishId) => {
  const index = selectedDishes.value.findIndex(item => item.id === dishId)
  if (index !== -1) {
    const item = selectedDishes.value[index]
    if (item.quantity > 1) {
      item.quantity -= 1
    } else {
      selectedDishes.value.splice(index, 1)
    }
  }
}

// 判断菜品是否被选中
const isSelected = (id) => {
  return selectedDishes.value.some(d => d.id === id)
}

// 计算总价
const totalPrice = computed(() =>
  selectedDishes.value.reduce(
    (sum, d) => sum + (Number(d.price) || 0) * (Number(d.quantity) || 0),
    0
  )
)


// 加载分类
const fetchCategoryList = async () => {
  const res = await getCategoryListApi({ type: 1 })
  categoryList.value = res.data || []
  if (categoryList.value.length > 0) {
    handleCategoryClick(categoryList.value[0].id)
  }
}

// 加载菜品
const handleCategoryClick = async (categoryId) => {
  activeCategoryId.value = categoryId
  const res = await getDishListByCategoryApi(categoryId)
  dishList.value = res.data || []
}

const handleSave = () => {
  if (selectedDishes.value.length <= 1) {
    ElMessage.warning('请至少添加俩个菜品')
    return
  }

  try {
    const key = setmealId.value ? `setmealDishes_${setmealId.value}` : 'tempSetmealDishes'
    sessionStorage.setItem(key, JSON.stringify(selectedDishes.value))
    console.log('🟢 handleSave - 缓存写入:', key, selectedDishes.value)
    ElMessage.success('已保存已选菜品')

    router.push({
      path: '/admin/setmeal/add',
      query: { id: setmealId.value || undefined } // 保持编辑模式id
    })
  } catch (e) {
    console.error(e)
    ElMessage.error('保存已选菜品失败，请重试')
  }
}


// 取消
const handleCancel = () => {
  router.push({
  path: '/admin/setmeal/add',
  query: { id: setmealId.value } // 带上当前套餐 id
})

}

onMounted(() => {
  const { id } = route.query
  if (route.query.id) {
    setmealId.value = Number(route.query.id)
    const tempDishes = sessionStorage.getItem(`setmealDishes_${id}`)
    if (tempDishes) {
      selectedDishes.value = JSON.parse(tempDishes).map(d => ({
        id: d.id,
        name: d.name,
        price: Number(d.price) || 0,
        quantity: Number(d.quantity) || 1,
        image: d.image || '',
        status: d.status ?? 1
      }))
      console.log('🟢 编辑模式读取缓存:', selectedDishes.value)
    } else {
      console.log('🟢 编辑模式无缓存')
    }
  } else {
    const tempDishes = sessionStorage.getItem('tempSetmealDishes')
    if (tempDishes) {
      selectedDishes.value = JSON.parse(tempDishes).map(d => ({
        id: d.id,
        name: d.name,
        price: Number(d.price) || 0,
        quantity: Number(d.quantity) || 1,
        image: d.image || '',
        status: d.status ?? 1
      }))
      console.log('🟢 新增模式读取缓存:', selectedDishes.value)
    } else {
      console.log('🟢 新增模式无缓存')
    }
  }

  fetchCategoryList()
})

</script>

<style scoped>
.add-dish-container {
  padding: 20px;
}

.dish-layout {
  display: flex;
  gap: 16px;
}

/* 左侧分类菜单样式 */
.left-categories .el-menu-item {
  font-size: 16px;       /* 字体大一点 */
  font-weight: 600;      /* 字体加粗 */
  color: #333;           /* 深色更清晰 */
  letter-spacing: 0.5px; /* 字间距微调 */
}

/* 鼠标悬停时效果 */
.left-categories .el-menu-item:hover {
  background-color: #f5f7fa;
  color: #409eff;
  font-weight: 700;
}

/* 选中状态高亮 */
.left-categories .el-menu-item.is-active {
  background-color: #ecf5ff;
  color: #409eff;
  font-weight: 700;
}


.center-dishes {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 10px;
  overflow-y: auto;
}

.dish-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  width: 90%; 
  min-height: 80px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  cursor: pointer;
  transition: 0.2s;
}

.dish-row:hover {
  background-color: #f5f7fa;
}

.dish-row.selected {
  border: 1px solid #409eff;
  background-color: #ecf5ff;
}

.dish-row-image {
  width: 80px;
  height: 80px;
  object-fit: contain;
  border-radius: 4px;
  margin-right: 20px;
}

.dish-name {
  flex: 1;
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.dish-status {
  margin: 0 80px; /* 控制名字和价格之间的距离 */
  color: #666;    /* 简单灰色字体 */
  font-size: 16px;
}

.dish-row-price {
  font-size: 16px;
  color: #666;
  min-width: 80px;
  text-align: right;
}

.right-selected {
  flex: 1;
  padding: 20px;
  background-color: #fafafa;
  border-left: 1px solid #ebeef5;
  overflow-y: auto;
}

.dish-card {
  display: flex;
  flex-direction: column;
  padding: 10px;
  text-align: center;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  margin-bottom: 12px;
  transition: box-shadow 0.2s ease;
}

.dish-card:hover {
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.dish-card.selected {
  border: 1px solid #409eff;
  background-color: #ecf5ff;
}

.dish-card-image {
  width: 100%;
  height: 80px;
  object-fit: cover;
  border-radius: 6px;
  margin-bottom: 6px;
}

.dish-card-price {
  font-size: 14px;
  color: #666;
  margin-top: 6px;
}

.setmeal-price-input {
  margin-top: 10px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: #333;
}

</style>
