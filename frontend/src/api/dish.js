import request from '@/utils/adminRequest'

// 分页查询菜品
export const getDishPageApi = (params) => request.get('/admin/dish/page', { params })

// 新增菜品
export const addDishApi = (data) => request.post('/admin/dish', data)

// 修改菜品
export const updateDishApi = (data) => request.put('/admin/dish', data)

// 删除菜品
export const deleteDishApi = (ids) => request.delete('/admin/dish', { params: { ids } })

// 修改菜品状态（启售/停售）
export const updateDishStatusApi = (id, status) =>
  request.post(`/admin/dish/status/${status}`, null, { params: { id } })

// 根据分类ID查询菜品列表
export const getDishListByCategoryApi = (categoryId) =>
  request.get('/admin/dish/list', { params: { categoryId } })

// 根据ID查询菜品
export const getDishByIdApi = (id) => request.get(`/admin/dish/${id}`)

// 调用后台 admin/dish/top10 接口
export const getTop10DishesApi = () => {
  return request.get('/admin/dish/top10')
}