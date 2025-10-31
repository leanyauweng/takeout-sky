import request from '@/utils/adminRequest'

// 获取统计图表数据（营业额趋势）
export const getTurnoverReportApi = (params) => request.get('/admin/report/turnoverStatistics', { params })

// 用户统计图表
export const getUserReportApi = (params) => request.get('/admin/report/userStatistics', { params })

// 订单统计图表
export const getOrderReportApi = (params) => request.get('/admin/report/orderStatistics', { params })

// 销量前十图表
export const getTop10ReportApi = (params) => request.get('/admin/report/top10', { params })

// 导出运营数据报表
export const exportStatisticsApi = () => request.get('/admin/report/export', { responseType: 'blob' })
