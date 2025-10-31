import request from '@/utils/adminRequest'

// 分页查询套餐
export const getSetmealPageApi = (params) => request.get('/admin/setmeal/page', { params })

// 新增套餐
export const addSetmealApi = (data) => request.post('/admin/setmeal', data)

// 修改套餐
export const updateSetmealApi = (data) => request.put('/admin/setmeal', data)

// 删除套餐（单个或多个）
export const deleteSetmealApi = (ids) => {
  if (!Array.isArray(ids)) ids = [ids]
  const query = ids.map(id => `ids=${id}`).join('&')
  return request.delete(`/admin/setmeal?${query}`)
}


//修改套餐状态
export const updateSetmealStatusApi = ({ id, status }) => 
  request.post(`/admin/setmeal/status/${status}`, null, { params: { id } })

// 根据分类ID查询套餐列表
export const getSetmealListByCategoryApi = (categoryId) =>
  request.get('/admin/setmeal/list', { params: { categoryId } })

// 根据id查询套餐（用于回显）
export const querySetmealByIdApi = (id) => request.get(`/admin/setmeal/${id}`)
