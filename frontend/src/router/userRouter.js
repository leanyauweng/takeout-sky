// src/router/userRouter.js
import { createRouter, createWebHistory } from 'vue-router'

// 引入页面组件
import UserLayout from '@/views/user/userLayout/UserLayout.vue'
import Home from '@/views/user/home/home.vue'
import Login from '@/views/user/login/login.vue'
import Cart from '@/views/user/cart/cart.vue'
import Checkout from '@/views/user/checkout/checkout.vue'
import Orders from '@/views/user/orders/orders.vue'
import Profile from '@/views/user/profile/address.vue'
import Menu from '@/views/user/menu/menu.vue'
import AddAddress from '@/views/user/profile/addAddress.vue'
import EditAddress from '@/views/user/profile/editAddress.vue'


const userRoutes = [
  {
    path: '/user',
    redirect: '/user/login'
  },
  {
    path: '/user/login',
    name: 'UserLogin',
    component: Login
  },
  {
    path: '/user',
    component: UserLayout, // ✅ 外层布局
    children: [
      { path: 'home', name: 'UserHome', component: Home },
      { path: 'menu', name: 'UserMenu', component: Menu },
      { path: 'cart', name: 'UserCart', component: Cart },
      { path: 'orders', name: 'UserOrders', component: Orders },
      { path: 'profile', name: 'UserProfile', component: Profile },
      { path: 'add-address', name: 'UserAddAddress', component: AddAddress },
      { path: 'edit-address', name: 'UserEditAddress', component: EditAddress },
    ]
  },
  {
    path: '/checkout',
    name: 'UserCheckout',
    component: Checkout, // 结算页
  }
]

export default userRoutes

