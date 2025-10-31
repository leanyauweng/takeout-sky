import request from '@/utils/adminRequest'

// 获取今日营业数据
export const getOverviewApi = () => request.get('/admin/workspace/businessData')

// 获取订单总览
export const getOrderOverviewApi = () => request.get('/admin/workspace/overviewOrders')

// 获取菜品总览
export const getDishOverviewApi = () => request.get('/admin/workspace/overviewDishes')

// 获取套餐总览
export const getSetmealOverviewApi = () => request.get('/admin/workspace/overviewSetmeals')

