import axios from 'axios'
import router from '@/router'
import { ElMessage } from 'element-plus'

export const userRequest = axios.create({
  baseURL: '/api',
  timeout: 50000
})

userRequest.interceptors.request.use(
  (config) => {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (userInfo?.token) {
        config.headers.authentication = userInfo.token

    }
    return config
  },
  (error) => Promise.reject(error)
)

userRequest.interceptors.response.use(
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
      localStorage.removeItem('userInfo')
      router.push('/user/login')
    } else {
      ElMessage.error(error.response?.data?.msg || '接口访问异常')
    }
    return Promise.reject(error)
  }
)

export default userRequest