import request from '@/utils/userRequest'

// 用户下单
export const submitOrderApi = (data) =>
  request.post('/user/order/submit', data)

// 客户催单
export const reminderOrderApi = (id) =>
  request.get(`/user/order/reminder/${id}`)

// 再来一单
export const reorderApi = (orderId) =>
  request.get(`/user/order/reorder/${orderId}`)

// 取消订单（可附加原因）
export const cancelOrderApi = (id, reason) =>
  request.post(`/user/order/cancel/${id}`, null, { params: { reason } })

// 用户付款
export const payOrderApi = (orderNumber) => {
  return request.post('/user/payment', null, {
    params: { orderNumber }
  })
}
//获取订单信息
export const getOrderDetailApi = (orderNumber) => {
  return request.get(`/user/order/detail/${orderNumber}`)
}
//根据id获取全部订单
export const getUserOrders = () => request.get('/user/order/list')
