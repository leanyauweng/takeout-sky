import request from '@/utils/userRequest'

// 查询当前用户所有地址
export const getAddressListApi = () => request.get('/user/addressBook/list')

// 新增地址
export const addAddressApi = (data) => request.post('/user/addressBook', data)

// 根据 id 查询地址
export const getAddressByIdApi = (id) => request.get(`/user/addressBook/${id}`)

// 修改地址
export const updateAddressApi = (data) => request.put('/user/addressBook', data)

// 设置默认地址
export const setDefaultAddressApi = (data) => request.put('/user/addressBook/default', data)

// 删除地址
export const deleteAddressApi = (id) => request.delete('/user/addressBook', { params: { id } })

// 查询当前用户默认地址
export const getDefaultAddressApi = () => request.get('/user/addressBook/default')
