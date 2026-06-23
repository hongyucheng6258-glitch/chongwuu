<template>
  <div class="dashboard">
    <h2 class="page-title">📊 数据概览</h2>

    <!-- 概览卡片 -->
    <div class="stat-cards" v-loading="loading">
      <div class="stat-card">
        <div class="stat-icon" style="background: var(--paw-coral-light)">📦</div>
        <div class="stat-info"><span class="stat-num">{{ overview.totalOrders || 0 }}</span><span class="stat-label">总订单数</span></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: var(--paw-sage-50)">💰</div>
        <div class="stat-info"><span class="stat-num">¥{{ Number(overview.totalSales || 0).toFixed(0) }}</span><span class="stat-label">总销售额</span></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: var(--paw-sky-50)">👤</div>
        <div class="stat-info"><span class="stat-num">{{ overview.totalUsers || 0 }}</span><span class="stat-label">总用户数</span></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: var(--paw-amber-50)">🛍️</div>
        <div class="stat-info"><span class="stat-num">{{ overview.onSaleProducts || 0 }}</span><span class="stat-label">在售商品</span></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #f0f5ff">📈</div>
        <div class="stat-info"><span class="stat-num">¥{{ Number(overview.todaySales || 0).toFixed(0) }}</span><span class="stat-label">今日销售额</span></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #e6f7e6">🆕</div>
        <div class="stat-info"><span class="stat-num">{{ overview.newUsersToday || 0 }}</span><span class="stat-label">今日新用户</span></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #fee">⏳</div>
        <div class="stat-info"><span class="stat-num">{{ overview.pendingOrders || 0 }}</span><span class="stat-label">待支付</span></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #fdf6ec">🔄</div>
        <div class="stat-info"><span class="stat-num">{{ overview.returnedOrders || 0 }}</span><span class="stat-label">退单数</span></div>
      </div>
    </div>

    <!-- 销售趋势 + 订单状态 -->
    <div class="chart-row">
      <el-card class="chart-card">
        <template #header><span>📈 近7日销售趋势</span></template>
        <div class="trend-chart">
          <div class="trend-item" v-for="item in salesTrend" :key="item.date">
            <div class="trend-bar-wrapper">
              <div class="trend-bar" :style="{ height: trendHeight(item.sales) + '%', background: 'linear-gradient(180deg, var(--paw-coral), var(--paw-coral-light))' }"></div>
            </div>
            <div class="trend-label">{{ item.date }}</div>
            <div class="trend-value">¥{{ Number(item.sales || 0).toFixed(0) }}</div>
          </div>
        </div>
      </el-card>
      <el-card class="chart-card">
        <template #header><span>📊 订单状态分布</span></template>
        <div class="bar-chart">
          <div class="bar-item" v-for="item in orderStatusData" :key="item.label">
            <span class="bar-label">{{ item.label }}</span>
            <div class="bar-track"><div class="bar-fill" :style="{ width: item.percent + '%', background: item.color }"></div></div>
            <span class="bar-value">{{ item.value }}</span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 分类分布 + 会员分布 -->
    <div class="chart-row">
      <el-card class="chart-card">
        <template #header><span>🥧 分类销售占比</span></template>
        <div class="pie-list">
          <div v-for="item in categoryDistribution" :key="item.categoryId" class="pie-item">
            <span class="pie-name">{{ item.categoryName }}</span>
            <div class="pie-bar-track">
              <div class="pie-bar-fill" :style="{ width: item.percentage + '%' }"></div>
            </div>
            <span class="pie-value">¥{{ Number(item.sales || 0).toFixed(0) }}</span>
            <span class="pie-percent">{{ item.percentage }}%</span>
          </div>
        </div>
      </el-card>
      <el-card class="chart-card">
        <template #header><span>👥 会员等级分布</span></template>
        <div class="member-list">
          <div v-for="item in memberDistribution" :key="item.level" class="member-item">
            <span class="member-badge" :class="'level-' + item.level">{{ item.name }}</span>
            <div class="member-bar-track">
              <div class="member-bar-fill" :style="{ width: memberPercent(item.count) + '%' }"></div>
            </div>
            <span class="member-count">{{ item.count }} 人</span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 热销商品 -->
    <el-card class="chart-card" style="margin-bottom: 24px;">
      <template #header><span>🔥 热销商品 TOP5</span></template>
      <el-table :data="topProducts" style="width: 100%" :header-cell-style="{ background: '#f5f7fa' }">
        <el-table-column type="index" label="排名" width="60" align="center" />
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="salesCount" label="销量" width="100" align="center" />
        <el-table-column label="销售额" width="120" align="right">
          <template #default="{ row }">¥{{ Number(row.salesAmount || 0).toFixed(2) }}</template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 用户增长 -->
    <el-card class="chart-card" style="margin-bottom: 24px;">
      <template #header><span>📈 近7日用户增长</span></template>
      <div class="user-growth-chart">
        <div class="growth-item" v-for="item in userGrowth" :key="item.date">
          <div class="growth-bars">
            <div class="growth-bar new" :style="{ height: growthHeight(item.newUsers, 'new') + '%' }"></div>
            <div class="growth-bar cumulative" :style="{ height: growthHeight(item.cumulativeUsers, 'cumulative') + '%' }"></div>
          </div>
          <div class="growth-label">{{ item.date }}</div>
          <div class="growth-values">
            <span class="new-users">+{{ item.newUsers }}</span>
          </div>
        </div>
      </div>
      <div class="growth-legend">
        <span><span class="legend-dot" style="background: var(--paw-coral)"></span> 新增用户</span>
        <span><span class="legend-dot" style="background: var(--paw-sage)"></span> 累计用户</span>
      </div>
    </el-card>

    <div class="quick-links">
      <h3>快捷入口</h3>
      <div class="links-row">
        <el-button @click="$router.push('/admin/products')">📦 商品管理</el-button>
        <el-button @click="$router.push('/admin/stores')">🏪 商店管理</el-button>
        <el-button @click="$router.push('/admin/orders')">📋 订单管理</el-button>
        <el-button @click="$router.push('/admin/videos')">🎬 视频管理</el-button>
        <el-button @click="$router.push('/admin/users')">👥 会员管理</el-button>
        <el-button @click="$router.push('/admin/reviews')">⭐ 评价管理</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { statsApi } from '../../api'

const loading = ref(false)
const overview = ref({})
const salesTrend = ref([])
const categoryDistribution = ref([])
const topProducts = ref([])
const userGrowth = ref([])
const memberDistribution = ref([])

onMounted(async () => {
  loading.value = true
  try {
    const data = await statsApi.dashboard()
    overview.value = data.overview || {}
    salesTrend.value = data.salesTrend || []
    categoryDistribution.value = data.categoryDistribution || []
    topProducts.value = data.topProducts || []
    userGrowth.value = data.userGrowth || []
    memberDistribution.value = data.memberDistribution || []
  } catch (e) {
    console.error('加载统计数据失败', e)
  }
  loading.value = false
})

const orderStatusData = computed(() => {
  const o = overview.value
  const items = [
    { label: '待支付', value: o.pendingOrders || 0, color: '#e6a23c' },
    { label: '已支付', value: o.paidOrders || 0, color: '#409eff' },
    { label: '已发货', value: o.shippedOrders || 0, color: '#909399' },
    { label: '已完成', value: o.completedOrders || 0, color: '#67c23a' },
    { label: '已取消', value: o.cancelledOrders || 0, color: '#f56c6c' },
    { label: '已退单', value: o.returnedOrders || 0, color: '#ff6b6b' }
  ]
  const max = Math.max(...items.map(i => i.value), 1)
  return items.map(i => ({ ...i, percent: Math.round((i.value / max) * 100) }))
})

const trendHeight = (sales) => {
  const max = Math.max(...salesTrend.value.map(i => Number(i.sales || 0)), 1)
  return max > 0 ? Math.max((Number(sales || 0) / max) * 100, 5) : 5
}

const memberPercent = (count) => {
  const total = memberDistribution.value.reduce((sum, i) => sum + Number(i.count || 0), 0)
  return total > 0 ? Math.max((count / total) * 100, 5) : 5
}

const growthHeight = (value, type) => {
  if (type === 'new') {
    const max = Math.max(...userGrowth.value.map(i => Number(i.newUsers || 0)), 1)
    return max > 0 ? Math.max((Number(value || 0) / max) * 100, 5) : 5
  } else {
    const max = Math.max(...userGrowth.value.map(i => Number(i.cumulativeUsers || 0)), 1)
    return max > 0 ? Math.max((Number(value || 0) / max) * 100, 5) : 5
  }
}
</script>

<style scoped>
.dashboard { padding: 24px; }
.page-title { font-size: 22px; margin: 0 0 24px; }
.stat-cards { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 24px; }
@media (max-width: 1200px) { .stat-cards { grid-template-columns: repeat(2, 1fr); } }
.stat-card { display: flex; align-items: center; gap: 14px; padding: 18px; background: white; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); border: 1px solid var(--el-border-color-light); }
.stat-icon { width: 48px; height: 48px; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-size: 24px; }
.stat-info { display: flex; flex-direction: column; }
.stat-num { font-size: 22px; font-weight: 800; }
.stat-label { font-size: 13px; color: #999; }
.chart-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 24px; }
@media (max-width: 900px) { .chart-row { grid-template-columns: 1fr; } }
.chart-card { border-radius: 12px; }

/* 销售趋势 */
.trend-chart { display: flex; align-items: flex-end; justify-content: space-around; height: 200px; padding: 20px 0; gap: 8px; }
.trend-item { flex: 1; display: flex; flex-direction: column; align-items: center; gap: 6px; }
.trend-bar-wrapper { flex: 1; width: 100%; display: flex; align-items: flex-end; justify-content: center; }
.trend-bar { width: 60%; border-radius: 4px 4px 0 0; transition: height 0.6s ease; min-height: 4px; }
.trend-label { font-size: 12px; color: #999; }
.trend-value { font-size: 11px; color: var(--paw-coral); font-weight: 700; }

/* 订单状态 */
.bar-chart { display: flex; flex-direction: column; gap: 10px; padding: 10px 0; }
.bar-item { display: flex; align-items: center; gap: 8px; }
.bar-label { width: 70px; font-size: 13px; color: #666; text-align: right; }
.bar-track { flex: 1; height: 14px; background: #f0f0f0; border-radius: 7px; overflow: hidden; }
.bar-fill { height: 100%; border-radius: 7px; transition: width 0.6s ease; }
.bar-value { width: 30px; font-size: 13px; font-weight: 700; color: #333; }

/* 分类占比 */
.pie-list { display: flex; flex-direction: column; gap: 12px; padding: 10px 0; }
.pie-item { display: flex; align-items: center; gap: 10px; }
.pie-name { width: 80px; font-size: 13px; color: #666; }
.pie-bar-track { flex: 1; height: 12px; background: #f0f0f0; border-radius: 6px; overflow: hidden; }
.pie-bar-fill { height: 100%; background: linear-gradient(90deg, var(--paw-coral), var(--paw-coral-light)); border-radius: 6px; transition: width 0.6s ease; }
.pie-value { width: 80px; font-size: 12px; color: #999; text-align: right; }
.pie-percent { width: 45px; font-size: 12px; font-weight: 700; color: var(--paw-coral); text-align: right; }

/* 会员分布 */
.member-list { display: flex; flex-direction: column; gap: 14px; padding: 10px 0; }
.member-item { display: flex; align-items: center; gap: 12px; }
.member-badge { padding: 4px 12px; border-radius: 12px; font-size: 12px; font-weight: 700; color: #fff; }
.member-badge.level-0 { background: #909399; }
.member-badge.level-1 { background: #67c23a; }
.member-badge.level-2 { background: #e6a23c; }
.member-badge.level-3 { background: #f56c6c; }
.member-bar-track { flex: 1; height: 12px; background: #f0f0f0; border-radius: 6px; overflow: hidden; }
.member-bar-fill { height: 100%; background: linear-gradient(90deg, var(--paw-sage), var(--paw-sage-light)); border-radius: 6px; transition: width 0.6s ease; }
.member-count { width: 60px; font-size: 13px; color: #666; text-align: right; }

/* 用户增长 */
.user-growth-chart { display: flex; align-items: flex-end; justify-content: space-around; height: 180px; padding: 20px 0; gap: 8px; }
.growth-item { flex: 1; display: flex; flex-direction: column; align-items: center; gap: 6px; }
.growth-bars { flex: 1; width: 100%; display: flex; align-items: flex-end; justify-content: center; gap: 4px; }
.growth-bar { width: 40%; border-radius: 4px 4px 0 0; transition: height 0.6s ease; min-height: 4px; }
.growth-bar.new { background: linear-gradient(180deg, var(--paw-coral), var(--paw-coral-light)); }
.growth-bar.cumulative { background: linear-gradient(180deg, var(--paw-sage), var(--paw-sage-light)); }
.growth-label { font-size: 12px; color: #999; }
.growth-values { font-size: 11px; }
.new-users { color: var(--paw-coral); font-weight: 700; }
.growth-legend { display: flex; justify-content: center; gap: 24px; margin-top: 12px; font-size: 13px; color: #666; }
.legend-dot { display: inline-block; width: 10px; height: 10px; border-radius: 50%; margin-right: 6px; }

.quick-links { margin-top: 20px; }
.quick-links h3 { font-size: 18px; margin: 0 0 16px; }
.links-row { display: flex; gap: 10px; flex-wrap: wrap; }
</style>
