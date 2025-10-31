import request from '@/utils/adminRequest'

// 订单分页查询
export const pageOrdersApi = ({ number, phone, status, page = 1, pageSize = 10 }) => {
    return request.get('/admin/order/page', {
      params: { number, phone, status, page, pageSize }
    })
  }

// 商家接单
export const acceptOrderApi = (id) =>
  request.post(`/admin/order/accept/${id}`)

// 派送订单
export const deliverOrderApi = (id) =>
  request.post(`/admin/order/deliver/${id}`)

// 完成订单
export const completeOrderApi = (id) =>
  request.post(`/admin/order/complete/${id}`)

// 商家拒单
export const rejectOrderApi = (id) =>
  request.post(`/admin/order/reject/${id}`)

// 根据订单ID查看详情
export const getOrderDetailApi = (id) =>
  request.get(`/admin/order/${id}`)

// 商家取消订单
export const cancelOrderApi = (id) =>
    request.post(`/admin/order/cancel/${id}`)