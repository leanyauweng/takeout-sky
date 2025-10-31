<template>
  <div class="statistics-container">
    <div class="toolbar">
      <div class="time-buttons">
        <el-button
          v-for="item in timeOptions"
          :key="item.label"
          type="primary"
          plain
          :class="{ active: currentType === item.type }"
          @click="changeDateRange(item.type)"
        >
          {{ item.label }}
        </el-button>
      </div>
      <div class="date-info">
        Selected Date: {{ dateRange.begin }} to {{ dateRange.end }}
      </div>
      <el-button type="success" @click="exportData">Export Data</el-button>
    </div>

    <div class="charts">
      <div class="chart-box" ref="turnoverChart"></div>
      <div class="chart-box" ref="userChart"></div>
      <div class="chart-box" ref="orderChart"></div>
      <div class="chart-box" ref="topChart"></div>
    </div>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import {
  getTurnoverReportApi,
  getUserReportApi,
  getOrderReportApi,
  getTop10ReportApi,
  exportStatisticsApi
} from '@/api/charts'

/* ----------------- 通用数据处理 ----------------- */
function parseData(value, toNumber = true) {
  if (Array.isArray(value)) return toNumber ? value.map(Number) : value
  if (typeof value === 'string' && value.includes(',')) {
    return toNumber ? value.split(',').map(Number) : value.split(',')
  }
  return []
}

/* ----------------- 时间范围控制 ----------------- */
const currentType = ref('7') // default last 7 days
const dateRange = ref({ begin: '', end: '' })
const timeOptions = [
  { label: 'Yesterday', type: '1' },
  { label: 'Last 7 Days', type: '7' },
  { label: 'Last 30 Days', type: '30' },
  { label: 'This Month', type: 'month' }
]

// 日期格式化函数
function formatDate(date) {
  return date.toISOString().split('T')[0]
}

// 计算时间范围
function getDateRange(type) {
  const today = new Date()
  const end = formatDate(today)
  let begin = ''

  switch (type) {
    case '1': // Yesterday
      const yesterday = new Date(today)
      yesterday.setDate(today.getDate() - 1)
      begin = formatDate(yesterday)
      break
    case '7': // Last 7 days
      const seven = new Date(today)
      seven.setDate(today.getDate() - 6)
      begin = formatDate(seven)
      break
    case '30': // Last 30 days
      const thirty = new Date(today)
      thirty.setDate(today.getDate() - 29)
      begin = formatDate(thirty)
      break
    case 'month': // This month
      const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1)
      begin = formatDate(firstDayOfMonth)
      break
  }
  return { begin, end }
}

function changeDateRange(type) {
  currentType.value = type
  dateRange.value = getDateRange(type)
  loadAllCharts()
}

/* ----------------- Export ----------------- */
function exportData() {
  exportStatisticsApi({ begin: dateRange.value.begin, end: dateRange.value.end })
    .then(res => {
      const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = 'Statistics_Report.xlsx'
      a.click()
      window.URL.revokeObjectURL(url)
    })
}

/* ----------------- Chart Loading ----------------- */
const turnoverChart = ref(null)
const userChart = ref(null)
const orderChart = ref(null)
const topChart = ref(null)

async function loadAllCharts() {
  await Promise.all([
    loadTurnoverChart(),
    loadUserChart(),
    loadOrderChart(),
    loadTop10Chart()
  ])
}

/* --- Turnover Chart --- */
async function loadTurnoverChart() {
  const { data } = await getTurnoverReportApi({ begin: dateRange.value.begin, end: dateRange.value.end })
  const dateArr = parseData(data.dateList, false)
  const turnoverArr = parseData(data.turnoverList)

  const chart = echarts.init(turnoverChart.value)
  chart.setOption({
    title: { text: 'Turnover Statistics' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: dateArr },
    yAxis: { type: 'value' },
    series: [{ name: 'Turnover', type: 'line', smooth: true, data: turnoverArr }]
  })
}

/* --- User Chart --- */
async function loadUserChart() {
  const { data } = await getUserReportApi({ begin: dateRange.value.begin, end: dateRange.value.end })
  const dateArr = parseData(data.dateList, false)
  const totalUserArr = parseData(data.totalUserList)
  const newUserArr = parseData(data.newUserList)

  const chart = echarts.init(userChart.value)
  chart.setOption({
    title: { text: 'User Growth Statistics' },
    tooltip: { trigger: 'axis' },
    legend: { data: ['New Users', 'Total Users'] },
    xAxis: { type: 'category', data: dateArr },
    yAxis: { type: 'value' },
    series: [
      { name: 'New Users', type: 'line', smooth: true, data: newUserArr },
      { name: 'Total Users', type: 'line', smooth: true, data: totalUserArr }
    ]
  })
}

/* --- Order Chart --- */
async function loadOrderChart() {
  const { data } = await getOrderReportApi({ begin: dateRange.value.begin, end: dateRange.value.end })
  const dateArr = parseData(data.dateList, false)
  const orderCountArr = parseData(data.orderCountList)
  const validOrderCountArr = parseData(data.validOrderCountList)
  const orderCompletionRateArr = parseData(data.orderCompletionRateList)

  const chart = echarts.init(orderChart.value)
  chart.setOption({
    title: { text: 'Order Completion Statistics' },
    tooltip: { trigger: 'axis' },
    legend: { data: ['Total Orders', 'Valid Orders', 'Completion Rate'] },
    xAxis: { type: 'category', data: dateArr },
    yAxis: [
      { type: 'value', name: 'Order Count' },
      { type: 'value', name: 'Completion Rate (%)' }
    ],
    series: [
      { name: 'Total Orders', type: 'bar', data: orderCountArr },
      { name: 'Valid Orders', type: 'bar', data: validOrderCountArr },
      { name: 'Completion Rate', type: 'line', yAxisIndex: 1, smooth: true, data: orderCompletionRateArr }
    ]
  })
}

/* --- Top 10 Products --- */
async function loadTop10Chart() {
  const { data } = await getTop10ReportApi({ begin: dateRange.value.begin, end: dateRange.value.end })
  const nameArr = parseData(data.nameList, false)
  const numberArr = parseData(data.numberList)

  const chart = echarts.init(topChart.value)
  chart.setOption({
    title: { text: 'Top 10 Products' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'value' },
    yAxis: { type: 'category', data: nameArr.reverse() },
    series: [{ name: 'Sales', type: 'bar', data: numberArr.reverse() }]
  })
}

onMounted(() => {
  changeDateRange('7')
  loadAllCharts()
})
</script>

<style scoped>
.statistics-container {
  padding: 20px;
}
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}
.time-buttons {
  display: flex;
  gap: 10px;
}
.time-buttons .active {
  background-color: #409eff;
  color: #fff;
}
.date-info {
  font-weight: 500;
  color: #333;
}
.charts {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-gap: 30px;
}
.chart-box {
  height: 400px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
  padding: 10px;
  min-width: 600px;
}
</style>
