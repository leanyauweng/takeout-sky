import request from '@/utils/adminRequest'


// ✅ 管理端员工登录
export const loginApi = (data) => request.post('/admin/employee/login', data)

// ✅ 修改密码（如果有）
export const changePasswordApi = (data) => request.put('/admin/employee/changePassword', data)

// ✅ 获取员工分页列表
export const getEmployeePageApi = (params) => request.get('/admin/employee/page', { params })

// ✅ 新增员工
export const addEmployeeApi = (data) => request.post('/admin/employee', data)

// ✅ 修改员工
export const updateEmployeeApi = (data) => request.put('/admin/employee', data)

// 启用/禁用员工账号
export const startOrStopEmployeeApi = (id, status) => {
    return request.post(`/admin/employee/status/${status}`, null, { params: { id } })
  }

  