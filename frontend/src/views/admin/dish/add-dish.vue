<template>
  <div class="add-dish-container">
    <el-card shadow="hover">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <!-- 菜品名称 / Dish Name -->
        <el-form-item label="Dish Name" prop="name">
          <el-input v-model="form.name" placeholder="Enter dish name" style="width: 250px" clearable />
        </el-form-item>

        <!-- 分类 / Category -->
        <el-form-item label="Category" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="Select category" style="width: 250px">
            <el-option v-for="cat in categoryList" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>

        <!-- 价格 / Price -->
        <el-form-item label="Price" prop="price">
          <el-input v-model.number="form.price" type="number" placeholder="Enter price" style="width: 250px" />
        </el-form-item>

        <!-- 口味 / Flavors -->
        <el-form-item label="Flavors" prop="flavors">
          <div
            v-for="(flavor, index) in form.flavors"
            :key="index"
            style="display: flex; flex-direction: column; gap: 8px; margin-bottom: 16px; padding: 10px; border: 1px solid #eee; border-radius: 8px;"
          >
            <el-input
              v-model="flavor.name"
              placeholder="Enter flavor name (e.g., Sweetness / Spicy / Extra)"
              style="width: 260px"
            />
            <el-input
              v-model="flavor.value"
              placeholder="Enter options (e.g., None, Less, More)"
              type="textarea"
              :rows="2"
              style="width: 260px"
            />
            <el-button
              type="danger"
              size="small"
              plain
              @click="removeFlavor(index)"
              style="width: 80px"
            >
              Delete
            </el-button>
          </div>

          <el-button type="primary" plain @click="addFlavor" style="margin-top: 10px;">
            + Add Flavor
          </el-button>
        </el-form-item>

        <!-- 图片 / Image -->
        <el-form-item label="Image" prop="image">
          <div class="upload-wrapper">
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
                <img :src="form.image" alt="Dish Image" style="width: 120px; height: 120px; object-fit: cover;" />
              </template>
            </el-upload>
            <div style="font-size: 12px; color: #909399; line-height: 1.4; margin-top: 4px;">
              Max size 2MB<br>Recommended size: 200x200 or 300x300
            </div>
          </div>
        </el-form-item>

        <!-- 描述 / Description -->
        <el-form-item label="Description" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="Enter dish description (max 200 characters)"
            maxlength="200"
            show-word-limit
            rows="4"
            style="width: 700px"
          />
        </el-form-item>

        <!-- 底部按钮 / Actions -->
        <el-form-item>
          <el-button @click="handleCancel">Cancel</el-button>
          <el-button type="primary" @click="handleSave">Save</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCategoryListApi } from '@/api/category'
import { uploadImageApi } from '@/api/common'
import { addDishApi, getDishByIdApi, updateDishApi } from '@/api/dish'

const router = useRouter()
const route = useRoute()
const formRef = ref()

// 表单数据 / Form data
const form = ref({
  name: '',
  categoryId: null,
  price: '',
  image: '',
  description: '',
  flavors: []
})

// 表单验证 / Validation rules
const rules = {
  name: [{ required: true, message: 'Please enter dish name', trigger: 'blur' }],
  categoryId: [{ required: true, message: 'Please select a category', trigger: 'change' }],
  price: [{ required: true, message: 'Please enter price', trigger: 'blur' }],
  image: [{ required: true, message: 'Please upload an image', trigger: 'change' }],
  flavors: [
    {
      validator: (rule, value) => {
        if (!value || value.length === 0) return new Error('Please add at least one flavor')
        for (let f of value) {
          if (!f.name || !f.value) return new Error('Flavor name and options cannot be empty')
        }
        return true
      },
      trigger: 'change'
    }
  ]
}

const categoryList = ref([])

// 获取分类列表 / Fetch category list
const fetchCategoryList = async () => {
  const res = await getCategoryListApi({ type: 1 })
  categoryList.value = res.data || []
}

// 口味操作 / Flavors
const addFlavor = () => form.value.flavors.push({ name: '', value: '' })
const removeFlavor = index => form.value.flavors.splice(index, 1)

// 图片上传 / Image upload
const beforeUpload = file => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) ElMessage.error('Only image files are allowed!')
  return isImage
}
const uploadImage = async param => {
  const file = param.file
  try {
    const res = await uploadImageApi(file)
    form.value.image = res.data
    ElMessage.success('Image uploaded successfully')
  } catch {
    ElMessage.error('Image upload failed')
  }
}

// 保存 / Save
const handleSave = async () => {
  try {
    await formRef.value.validate()
    const payload = { ...form.value }
    if (payload.id) {
      await updateDishApi(payload)
    } else {
      await addDishApi(payload)
    }
    ElMessage.success('Saved successfully')
    router.push('/admin/dish')
  } catch (err) {
    console.error(err)
    ElMessage.error('Save failed, please check the form')
  }
}

// 取消 / Cancel
const handleCancel = () => router.push('/admin/dish')

// 编辑回显 / Edit mode
onMounted(async () => {
  const { id } = route.query
  await fetchCategoryList()
  if (id) {
    try {
      const res = await getDishByIdApi(id)
      if (res.data) {
        Object.assign(form.value, {
          id: res.data.id,
          name: res.data.name,
          categoryId: res.data.categoryId,
          price: res.data.price,
          image: res.data.image,
          description: res.data.description,
          flavors: res.data.flavors ? [...res.data.flavors] : []
        })
      }
    } catch (err) {
      console.error(err)
      ElMessage.error('Failed to fetch dish info')
    }
  }
})
</script>

  
  <style scoped>
  .add-dish-container { padding: 20px }
  .upload-wrapper { display: flex; flex-direction: column; align-items: flex-start; gap: 4px }
  </style>
  