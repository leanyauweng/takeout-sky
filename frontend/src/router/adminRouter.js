// src/router/adminRouter.js
import Layout from '@/views/admin/layout/layout.vue'
import Login from '@/views/admin/login/login.vue'
import Category from '@/views/admin/category/category.vue'
import Chart from '@/views/admin/chart/chart.vue'
import DashBoard from '@/views/admin/dashboard/dashboard.vue'
import Dish from '@/views/admin/dish/dish.vue'
import addDish from '@/views/admin/dish/add-dish.vue'
import Employee from '@/views/admin/employee/employee.vue'
import OrderDetails from '@/views/admin/orderDetails/orderDetails.vue'
import Setmeal from '@/views/admin/setmeal/setmeal.vue'
import Statistics from '@/views/admin/statistics/statistics.vue'
import AddSetmeal from '@/views/admin/setmeal/addSetmeal.vue'   
import AddDish from '@/views/admin/setmeal/components/AddDish.vue' 


const adminRoutes = [
  {
    path: '/',
    redirect: '/login' // ✅ 默认打开登录页
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/admin',
    component: Layout,
    redirect: '/admin/dashboard', // ✅ 登录后跳转后台主页
    children: [
      { path: 'category', name: 'Category', component: Category },
      { path: 'chart', name: 'Chart', component: Chart },
      { path: 'dashboard', name: 'DashBoard', component: DashBoard },
      { path: 'dish', name: 'Dish', component: Dish },
      { path: 'dish/add', name: 'addDish', component: addDish },
      { path: 'employee', name: 'Employee', component: Employee },
      { path: 'orderDetails', name: 'OrderDetails', component: OrderDetails },
      { path: 'setmeal', name: 'Setmeal', component: Setmeal },
      { path: 'setmeal/add', name: 'AddSetmeal', component: AddSetmeal },   
      { path: 'setmeal/addDish', name: 'AddDish', component: AddDish }, 
      { path: 'order/:id', name: 'OrderDetail', component: () => import('@/views/admin/dashboard/OrderDetailModal.vue') },
      { path: 'statistics', name: 'Statistics', component: Statistics }
    ]
  }
]

export default adminRoutes
