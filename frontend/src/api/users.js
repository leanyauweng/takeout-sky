import request from '@/utils/userRequest'

// ✅ 用户端登录
export const userLoginApi = (data) => request.post('/user/user/login', data)
