import { createRouter, createWebHistory } from 'vue-router'
import adminRoutes from './adminRouter'
import userRoutes from './userRouter'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    ...adminRoutes,
    ...userRoutes
  ]
})

export default router
