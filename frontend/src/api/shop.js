import request from '@/utils/adminRequest'  // 如果你有封装axios

// 获取店铺状态
export const getShopStatusApi = () => {
  return request.get('/admin/shop/status')
}

// 设置店铺状态（1=营业中，0=打烊中）
export const setShopStatusApi = (status) => {
  return request.put(`/admin/shop/${status}`)
}
