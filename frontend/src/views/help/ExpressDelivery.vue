<template>
  <div class="help-page paw-pattern">
    <!-- Hero Section -->
    <div class="hero">
      <div class="hero-content animate-fade-up">
        <span class="hero-icon">⚡</span>
        <h1 class="hero-title">极速配送</h1>
        <p class="hero-desc">闪电般的配送速度，让您的爱宠更快享用心仪好物</p>
      </div>
    </div>

    <div class="page-container">
      <!-- Delivery Options -->
      <section class="section animate-fade-up delay-1">
        <h2 class="section-title">
          <el-icon><Van /></el-icon>
          配送方式
        </h2>
        <p class="section-subtitle">多种配送方式，满足不同时效需求</p>

        <div class="delivery-grid">
          <el-card v-for="(item, index) in deliveryOptions" :key="index" :class="['delivery-card', { primary: item.primary }]" shadow="hover">
            <div v-if="item.primary" class="primary-badge">推荐</div>
            <div class="delivery-icon">{{ item.icon }}</div>
            <h3 class="delivery-name">{{ item.name }}</h3>
            <div class="delivery-time">
              <el-icon><Timer /></el-icon>
              <span>{{ item.time }}</span>
            </div>
            <p class="delivery-desc">{{ item.desc }}</p>
            <div class="delivery-price">
              <span class="price-label">配送费</span>
              <span class="price-value">{{ item.price }}</span>
            </div>
          </el-card>
        </div>
      </section>

      <!-- Coverage -->
      <section class="section animate-fade-up delay-2">
        <h2 class="section-title">
          <el-icon><MapLocation /></el-icon>
          配送范围
        </h2>
        <p class="section-subtitle">覆盖全国大部分地区，偏远地区也能送达</p>

        <div class="coverage-grid">
          <div class="coverage-item">
            <div class="coverage-icon">🏙️</div>
            <h4>一线城市</h4>
            <p>北京、上海、广州、深圳</p>
            <el-tag type="success" size="small">极速达 2小时</el-tag>
          </div>
          <div class="coverage-item">
            <div class="coverage-icon">🌆</div>
            <h4>二线城市</h4>
            <p>杭州、成都、武汉、南京等</p>
            <el-tag type="success" size="small">次日达</el-tag>
          </div>
          <div class="coverage-item">
            <div class="coverage-icon">🏘️</div>
            <h4>三线城市</h4>
            <p>全国300+地级市</p>
            <el-tag type="warning" size="small">2-3天送达</el-tag>
          </div>
          <div class="coverage-item">
            <div class="coverage-icon">🏔️</div>
            <h4>偏远地区</h4>
            <p>西藏、新疆、青海等</p>
            <el-tag type="info" size="small">3-7天送达</el-tag>
          </div>
        </div>
      </section>

      <!-- Tracking -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><Position /></el-icon>
          物流追踪
        </h2>
        <p class="section-subtitle">实时掌握包裹动态，安心等待收货</p>

        <el-timeline class="tracking-timeline">
          <el-timeline-item
            v-for="(step, index) in trackingSteps"
            :key="index"
            :timestamp="step.time"
            :type="step.type"
            :hollow="index > 0"
            placement="top"
          >
            <div class="timeline-content">
              <h4>{{ step.title }}</h4>
              <p>{{ step.desc }}</p>
            </div>
          </el-timeline-item>
        </el-timeline>
      </section>

      <!-- Special Items -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><Box /></el-icon>
          特殊商品配送说明
        </h2>

        <el-table :data="specialItems" stripe class="special-table">
          <el-table-column prop="type" label="商品类型" width="140" />
          <el-table-column prop="packaging" label="包装方式" />
          <el-table-column prop="note" label="配送说明" />
          <el-table-column prop="time" label="预计时效" width="120" />
        </el-table>
      </section>

      <!-- Back Button -->
      <div class="back-section animate-fade-up delay-4">
        <el-button type="primary" size="large" @click="router.push('/')">
          <el-icon><HomeFilled /></el-icon>
          返回首页
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const deliveryOptions = [
  {
    icon: '⚡',
    name: '同城极速达',
    time: '下单后2小时内送达',
    desc: '主城区范围内，由专属骑手配送，适合急需的宠物用品和生鲜食品。',
    price: '12元起',
    primary: true
  },
  {
    icon: '🚀',
    name: '次日达',
    time: '次日18:00前送达',
    desc: '覆盖全国一二线城市，今日下单明日送达，高效便捷。',
    price: '8元起',
    primary: false
  },
  {
    icon: '📦',
    name: '标准快递',
    time: '2-5个工作日送达',
    desc: '全国大部分地区均可送达，经济实惠的配送选择。',
    price: '6元起',
    primary: false
  },
  {
    icon: '🧊',
    name: '冷链配送',
    time: '1-3个工作日送达',
    desc: '专为宠物生鲜食品设计，全程冷链保鲜，确保食品新鲜安全。',
    price: '15元起',
    primary: false
  }
]

const trackingSteps = [
  { time: '10:30', title: '订单已提交', desc: '您的订单已成功提交，等待商家确认', type: 'primary' },
  { time: '10:35', title: '商家已接单', desc: '商家已确认订单，正在准备商品', type: 'primary' },
  { time: '11:20', title: '商品已出库', desc: '商品已打包完成，等待快递揽收', type: 'primary' },
  { time: '12:00', title: '快递已揽收', desc: '包裹已被快递员取走，开始运输', type: 'warning' },
  { time: '14:30', title: '运输中', desc: '包裹已到达本地分拣中心', type: 'warning' },
  { time: '16:00', title: '派送中', desc: '快递员正在为您派送，请保持手机畅通', type: 'success' },
  { time: '16:45', title: '已签收', desc: '包裹已签收，感谢您的购买！', type: 'success' }
]

const specialItems = [
  { type: '🐾 活体宠物', packaging: '专用运输箱 + 保温层', note: '需提前预约，专人专车配送，全程监控', time: '预约制' },
  { type: '🥩 生鲜食品', packaging: '泡沫箱 + 冰袋 + 干冰', note: '全程冷链运输，温度实时监控', time: '1-2天' },
  { type: '💊 宠物药品', packaging: '防震缓冲 + 密封包装', note: '避光防潮，特殊标识处理', time: '2-3天' },
  { type: '🧴 大容量用品', packaging: '加固纸箱 + 防漏处理', note: '大件商品可能分包裹配送', time: '3-5天' }
]
</script>

<style scoped>
.help-page {
  min-height: 100vh;
  background: var(--paw-bg);
}

/* ── Hero ── */
.hero {
  background: linear-gradient(135deg, var(--paw-coral-50) 0%, var(--paw-sky-50) 100%);
  padding: 64px 24px 48px;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.hero::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  right: 0;
  height: 40px;
  background: var(--paw-bg);
  border-radius: 32px 32px 0 0;
}

.hero-content {
  position: relative;
  z-index: 1;
}

.hero-icon {
  font-size: 56px;
  display: block;
  margin-bottom: 16px;
  animation: float 3s ease-in-out infinite;
}

.hero-title {
  font-size: 36px;
  font-weight: 800;
  color: var(--paw-ink);
  margin-bottom: 12px;
}

.hero-desc {
  font-size: 16px;
  color: var(--paw-ink-2);
  max-width: 480px;
  margin: 0 auto;
  line-height: 1.6;
}

/* ── Section ── */
.section {
  margin-bottom: 48px;
}

.section-title {
  font-size: 22px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-title .el-icon {
  color: var(--paw-coral);
}

.section-subtitle {
  font-size: 14px;
  color: var(--paw-ink-3);
  margin-bottom: 32px;
}

/* ── Delivery Grid ── */
.delivery-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.delivery-card {
  text-align: center;
  border: none;
  position: relative;
  transition: all var(--paw-normal);
  overflow: hidden;
}

.delivery-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--paw-shadow-lg);
}

.delivery-card.primary {
  border: 2px solid var(--paw-coral);
  background: linear-gradient(180deg, #FFFBF5 0%, var(--paw-coral-50) 100%);
}

.primary-badge {
  position: absolute;
  top: 12px;
  right: -28px;
  background: var(--paw-coral);
  color: #fff;
  font-size: 11px;
  font-weight: 700;
  padding: 4px 32px;
  transform: rotate(45deg);
}

.delivery-icon {
  font-size: 40px;
  margin-bottom: 12px;
}

.delivery-name {
  font-size: 17px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 8px;
}

.delivery-time {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  font-size: 13px;
  color: var(--paw-coral);
  font-weight: 600;
  margin-bottom: 12px;
}

.delivery-desc {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.6;
  margin-bottom: 16px;
}

.delivery-price {
  padding: 10px;
  background: var(--paw-coral-50);
  border-radius: var(--paw-radius-sm);
}

.price-label {
  font-size: 12px;
  color: var(--paw-ink-3);
  display: block;
  margin-bottom: 4px;
}

.price-value {
  font-size: 20px;
  font-weight: 800;
  color: var(--paw-coral);
}

/* ── Coverage Grid ── */
.coverage-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.coverage-item {
  text-align: center;
  padding: 28px 20px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  transition: all var(--paw-normal);
}

.coverage-item:hover {
  box-shadow: var(--paw-shadow-sm);
  transform: translateY(-2px);
}

.coverage-icon {
  font-size: 36px;
  margin-bottom: 12px;
}

.coverage-item h4 {
  font-size: 16px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 6px;
}

.coverage-item p {
  font-size: 13px;
  color: var(--paw-ink-2);
  margin-bottom: 12px;
}

/* ── Timeline ── */
.tracking-timeline {
  max-width: 700px;
  margin: 0 auto;
}

.timeline-content h4 {
  font-size: 15px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 4px;
}

.timeline-content p {
  font-size: 13px;
  color: var(--paw-ink-2);
}

/* ── Special Table ── */
.special-table {
  border-radius: var(--paw-radius-md);
  overflow: hidden;
}

/* ── Back Button ── */
.back-section {
  text-align: center;
  padding: 32px 0 64px;
}

@media (max-width: 1024px) {
  .delivery-grid,
  .coverage-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .delivery-grid,
  .coverage-grid {
    grid-template-columns: 1fr;
  }
  .hero-title {
    font-size: 28px;
  }
}
</style>
