import request from '@/utils/userRequest'

// 添加商品到购物车
export const addToCartApi = (data) => request.post('/user/shoppingCart/add', data)

// 减少商品数量
export const subFromCartApi = (data) => request.post('/user/shoppingCart/sub', data)

// 获取购物车列表
export const getCartListApi = () => request.get('/user/shoppingCart/list')

// 清空购物车
export const cleanCartApi = () => request.delete('/user/shoppingCart/clean')


// 获取店铺营业状态
export const getShopStatusApi = () => {return request.get('/user/shop/status')}