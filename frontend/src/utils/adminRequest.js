import axios from 'axios'
import router from '@/router'
import { ElMessage } from 'element-plus'

// 创建一个新的 axios 实例
export const adminRequest = axios.create({
  baseURL: '/api',
  timeout: 50000
})

// 请求拦截器
adminRequest.interceptors.request.use(
  (config) => {
    const adminInfo = JSON.parse(localStorage.getItem('adminInfo'))
    if (adminInfo?.token) {
      config.headers.token = adminInfo.token

    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器
adminRequest.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code && res.code !== 1) {
      ElMessage.error(res.msg || '请求失败')
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    return res
  },
  (error) => {
    if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      localStorage.removeItem('adminInfo')
      router.push('/login')
    } else {
      ElMessage.error(error.response?.data?.msg || '接口访问异常')
    }
    return Promise.reject(error)
  }
)

export default adminRequest