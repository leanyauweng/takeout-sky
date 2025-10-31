import request from '@/utils/adminRequest'

// 上传图片
export const uploadImageApi = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/admin/common/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}
