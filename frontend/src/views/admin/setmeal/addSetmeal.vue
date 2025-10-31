<template>
  <div class="add-setmeal-container">
    <el-card shadow="hover">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <!-- 套餐名称 -->
        <el-form-item label="Set Meal Name" prop="name">
          <el-input
            v-model="form.name"
            placeholder="Enter set meal name"
            style="width: 250px"
            clearable
          />
        </el-form-item>

        <!-- 套餐分类 -->
        <el-form-item label="Category" prop="categoryId">
          <el-select
            v-model="form.categoryId"
            placeholder="Select category"
            style="width: 250px"
          >
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <!-- 套餐价格 -->
        <el-form-item label="Price" prop="price">
          <el-input
            v-model="form.price"
            type="number"
            placeholder="Enter price"
            style="width: 250px"
          />
        </el-form-item>

        <!-- 套餐菜品 -->
        <el-form-item label="Dishes">
          <el-button type="primary" @click="goAddDish">+ Add Dish</el-button>
        </el-form-item>

        <!-- 套餐图片 -->
        <el-form-item label="Image" prop="image">
          <div
            class="upload-wrapper"
            style="display: flex; align-items: center; gap: 16px;"
          >
            <!-- 上传区域 -->
            <el-upload
              class="upload-demo"
              :http-request="uploadImage"
              :show-file-list="false"
              :accept="'image/*'"
              :before-upload="beforeUpload"
              style="width: 120px; height: 120px; border: 1px dashed #d9d9d9; display: flex; justify-content: center; align-items: center; cursor: pointer;"
            >
              <template v-if="!form.image">
                <span style="font-size: 32px; color: #909399;">+</span>
                <div style="font-size: 12px; color: #909399;">Upload Image</div>
              </template>
              <template v-else>
                <img
                  :src="form.image"
                  alt="Set Meal Image"
                  style="width: 120px; height: 120px; object-fit: cover;"
                />
              </template>
            </el-upload>

            <!-- 提示文字 -->
            <div style="font-size: 12px; color: #909399; line-height: 1.4;">
              Image size must not exceed 2MB<br />
              Recommended size: 200×200 or 300×300 pixels
            </div>
          </div>
        </el-form-item>

        <!-- 套餐描述 -->
        <el-form-item label="Description" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="Enter description (max 200 characters)"
            style="width: 700px"
            maxlength="200"
            show-word-limit
            rows="4"
          />
        </el-form-item>

        <!-- 底部操作按钮 -->
        <el-form-item>
          <el-button @click="handleCancel">Cancel</el-button>
          <el-button type="primary" @click="handleSave">Save</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>


<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { uploadImageApi } from '@/api/common'
import { getCategoryListApi } from '@/api/category'
import { addSetmealApi, querySetmealByIdApi, updateSetmealApi } from '@/api/setmeal'
import { getDishByIdApi } from '@/api/dish'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute() 
const formRef = ref()
const dishList = ref([])
// page-level 已选菜品（回显）
const selectedDishes = ref([])
const { id } = route.query


// 表单数据
const form = ref({
  name: '',
  categoryId: null,
  price: '',
  image: '',
  description: ''
})

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入套餐名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择套餐分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入套餐价格', trigger: 'blur' }],
  image: [{ required: true, message: '请上传套餐图片', trigger: 'change' }]
}

// 监控 form 的变化
watch(
  () => form.value,
  (newVal) => {
    // 序列化存储
    sessionStorage.setItem('tempSetmealForm', JSON.stringify(newVal))
  },
  { deep: true } // 深度监听对象内部变化
)

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片！')
  }
  return isImage // false 会阻止上传
}


// 分类列表
const categoryList = ref([])

// 获取分类列表
const fetchCategoryList = async () => {
  const res = await getCategoryListApi({ type: 2 })
  categoryList.value = res.data || []
}

// 上传图片方法
const uploadImage = async (param) => {
  const file = param.file
  try {
    const res = await uploadImageApi(file)
    form.value.image = res.data // 后端返回图片 URL
    ElMessage.success('图片上传成功')
  } catch (error) {
    ElMessage.error('图片上传失败')
  }
}

// 保存套餐
const handleSave = async () => {
  // 基本校验
  if (!form.value.name) {
    ElMessage.warning('请输入套餐名称')
    return
  }
  if (!form.value.categoryId) {
    ElMessage.warning('请选择套餐分类')
    return
  }
  if (!form.value.image) {
    ElMessage.warning('请上传套餐图片')
    return
  }
  if (!form.value.price) {
    ElMessage.warning('请输入套餐售价')
    return
  }
  if (selectedDishes.value.length === 0) {
    ElMessage.warning('请至少添加俩个菜品')
    return
  }

  // 构造后端 SetmealDish 数组
  const setmealDishes = selectedDishes.value.map(d => ({
    dishId: d.id,
    name: d.name,
    price: d.price,
    copies: d.quantity ?? 1,
    status: d.status ?? 1
  }))

  // 构造 payload
  const payload = { ...form.value, setmealDishes }
  const editId = Number(route.query.id)
  console.log('route.query.id =', route.query.id)
  console.log('editId =', editId)
  console.log('payload =', payload)

  try {
    if (!isNaN(editId) && editId > 0) {
      // 编辑已有套餐 → PUT
      payload.id = editId
      await updateSetmealApi(payload)
      ElMessage.success('套餐已更新 (PUT)')
    } else {
      // 新增套餐 → POST
      await addSetmealApi(payload)
      ElMessage.success('套餐已新增 (POST)')
    }

    sessionStorage.removeItem('tempSetmealForm')
    sessionStorage.removeItem('tempSetmealDishes')
    router.push('/admin/setmeal')
  } catch (err) {
    console.error(err)
    ElMessage.error('保存失败')
  }
}



// 跳转到添加菜品
const goAddDish = () => {
  if (route.query.id) {
    // 编辑模式跳转，带上套餐 id
    router.push({
      path: '/admin/setmeal/addDish',
      query: { id: route.query.id }
    })
  } else {
    // 新增模式
    router.push('/admin/setmeal/addDish')
  }
}

// 取消返回
const handleCancel = () => {
  sessionStorage.removeItem('tempSetmealForm')
  sessionStorage.removeItem('tempSetmealDishes')
  if (route.query.id) {
    sessionStorage.removeItem(`setmealDishes_${route.query.id}`)
  }
  router.push('/admin/setmeal')
}


onMounted(async () => {
  const { id } = route.query

  if (id) {
    // 编辑模式

    // 1️⃣ 先尝试读取缓存菜品
    const tempDishes = sessionStorage.getItem(`setmealDishes_${id}`)
    if (tempDishes) {
      try {
        selectedDishes.value = JSON.parse(tempDishes).map(d => ({
          id: d.id,
          name: d.name,
          price: Number(d.price) || 0,
          quantity: Number(d.quantity) || 1,
          image: d.image || '',
          status: d.status ?? 1
        }))
        console.log('🟢 读取缓存菜品:', selectedDishes.value)
      } catch (e) {
        console.error('解析缓存失败', e)
        sessionStorage.removeItem(`setmealDishes_${id}`)
      }
    }

    // 2️⃣ 无论缓存是否存在，都拉数据库回填基础信息
    try {
      const res = await querySetmealByIdApi(id)
      if (res.data) {
        // 回填表单基础信息
        Object.assign(form.value, {
          name: res.data.name,
          categoryId: res.data.categoryId,
          price: res.data.price,
          image: res.data.image,
          description: res.data.description
        })
        console.log('🟢 基础信息回填完成:', form.value)

        // 如果缓存不存在，再拉数据库菜品列表
        if (!tempDishes) {
          const setmealDishes = res.data.setmealDishes || []
          const fullDishes = []

          for (const item of setmealDishes) {
            try {
              const dishRes = await getDishByIdApi(item.dishId)
              const dish = dishRes.data
              fullDishes.push({
                id: dish.id,
                name: dish.name,
                price: Number(dish.price) || 0,
                quantity: item.copies || 1,
                image: dish.image || '',
                status: dish.status ?? 1
              })
            } catch (err) {
              console.error(`查询菜品 ${item.dishId} 失败`, err)
            }
          }

          selectedDishes.value = fullDishes
          sessionStorage.setItem(`setmealDishes_${id}`, JSON.stringify(fullDishes))
          console.log('🟩 [缓存写入成功] tempSetmealDishes =', fullDishes)
        }
      }
    } catch (e) {
      console.error('拉数据库失败', e)
    }
  } else {
    // 新增模式：从 sessionStorage 恢复表单和已选菜品
    const tempForm = sessionStorage.getItem('tempSetmealForm')
    if (tempForm) {
      Object.assign(form.value, JSON.parse(tempForm))
    }
    const tmp = sessionStorage.getItem('tempSetmealDishes')
    if (tmp) {
      try {
        selectedDishes.value = JSON.parse(tmp)
      } catch (e) {
        console.error(e)
        sessionStorage.removeItem('tempSetmealDishes')
      }
    }
  }

  fetchCategoryList()
})




</script>

<style scoped>
.add-setmeal-container {
  padding: 20px;
}
.preview img {
  width: 120px;
  height: 120px;
  margin-top: 10px;
  border-radius: 8px;
  object-fit: cover;
}

.image-uploader {
  display: inline-block;
}

.image-wrapper {
  width: 120px;
  height: 120px;
  border: 1px dashed #dcdcdc;
  border-radius: 6px;
  overflow: hidden;
  position: relative;
  cursor: pointer;
}

.uploaded-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.overlay {
  position: absolute;
  top: 0; left: 0;
  width: 100%; height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  background-color: rgba(0,0,0,0.4);
  opacity: 0;
  transition: opacity 0.3s;
}

.image-wrapper:hover .overlay {
  opacity: 1;
}

.image-placeholder {
  width: 120px;
  height: 120px;
  border: 1px dashed #dcdcdc;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
  cursor: pointer;
  font-size: 16px;
}

.image-placeholder .plus {
  font-size: 36px;
  line-height: 36px;
}
</style>
