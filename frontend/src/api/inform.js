import request from '@/utils/adminRequest'

// 获取公告列表
export const getInformListApi = (params) => request.get('/admin/inform/page', { params })

// 新增公告
export const addInformApi = (data) => request.post('/admin/inform', data)

// 修改公告
export const updateInformApi = (data) => request.put('/admin/inform', data)

// 删除公告
export const deleteInformApi = (id) => request.delete(`/admin/inform/${id}`)
