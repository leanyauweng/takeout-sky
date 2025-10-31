import request from '@/utils/adminRequest'

// 获取分类分页列表
export const getCategoryPageApi = (params) =>
  request.get('/admin/category/page', { params })

// 获取分类列表（根据类型）
export const getCategoryListApi = (params) =>
  request.get('/admin/category/list', { params })

// 新增分类
export const addCategoryApi = (data) =>
  request.post('/admin/category', data)

// 修改分类
export const updateCategoryApi = (data) =>
  request.put('/admin/category', data)

// 删除分类
export const deleteCategoryApi = (id) =>
  request.delete('/admin/category', { params: { id } })

// 启用/禁用分类
export const updateCategoryStatusApi = (id, status) =>
  request.post(`/admin/category/status/${status}`, null, { params: { id } })
