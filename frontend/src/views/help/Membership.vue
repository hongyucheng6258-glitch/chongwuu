<template>
  <div class="help-page paw-pattern">
    <!-- Hero Section -->
    <div class="hero">
      <div class="hero-content animate-fade-up">
        <span class="hero-icon">👑</span>
        <h1 class="hero-title">会员介绍</h1>
        <p class="hero-desc">成为初晴萌宠空间会员，尊享专属优惠与贴心服务</p>
      </div>
    </div>

    <div class="page-container">
      <!-- Membership Tiers -->
      <section class="section animate-fade-up delay-1">
        <h2 class="section-title">
          <el-icon><Medal /></el-icon>
          会员等级
        </h2>
        <p class="section-subtitle">消费越多，等级越高，权益越丰厚</p>

        <div class="tier-grid">
          <el-card
            v-for="(tier, index) in tiers"
            :key="index"
            :class="['tier-card', { featured: tier.featured }]"
            shadow="hover"
          >
            <div v-if="tier.featured" class="featured-badge">最受欢迎</div>
            <div class="tier-icon">{{ tier.icon }}</div>
            <h3 class="tier-name">{{ tier.name }}</h3>
            <div class="tier-condition">
              <el-tag :type="tier.tagType" size="small">{{ tier.condition }}</el-tag>
            </div>
            <div class="tier-discount">
              <span class="discount-label">专享折扣</span>
              <span class="discount-value">{{ tier.discount }}</span>
            </div>
            <ul class="tier-benefits">
              <li v-for="(benefit, i) in tier.benefits" :key="i">
                <span class="benefit-icon">{{ benefit.icon }}</span>
                <span>{{ benefit.text }}</span>
              </li>
            </ul>
          </el-card>
        </div>
      </section>

      <!-- Growth Rules -->
      <section class="section animate-fade-up delay-2">
        <h2 class="section-title">
          <el-icon><TrendCharts /></el-icon>
          成长规则
        </h2>
        <p class="section-subtitle">了解会员积分与等级提升机制</p>

        <div class="rules-grid">
          <el-card class="rule-card" shadow="hover">
            <div class="rule-icon">💰</div>
            <h4>消费积分</h4>
            <p>每消费 <strong>1元</strong> 获得 <strong>1积分</strong>，积分可兑换优惠券或礼品</p>
          </el-card>
          <el-card class="rule-card" shadow="hover">
            <div class="rule-icon">📅</div>
            <h4>年度清零</h4>
            <p>积分每年12月31日清零，请在有效期内及时使用</p>
          </el-card>
          <el-card class="rule-card" shadow="hover">
            <div class="rule-icon">⬆️</div>
            <h4>等级升级</h4>
            <p>累计消费达到对应金额后自动升级，升级后立即享受新权益</p>
          </el-card>
          <el-card class="rule-card" shadow="hover">
            <div class="rule-icon">🎁</div>
            <h4>生日特权</h4>
            <p>会员生日当月可领取专属生日礼包，含优惠券和积分奖励</p>
          </el-card>
        </div>
      </section>

      <!-- Points Table -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><DataLine /></el-icon>
          积分兑换
        </h2>

        <el-table :data="pointsData" stripe class="points-table">
          <el-table-column prop="points" label="所需积分" width="140" />
          <el-table-column prop="reward" label="兑换内容" />
          <el-table-column prop="value" label="价值" width="120" />
          <el-table-column prop="limit" label="兑换限制" width="140" />
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

const tiers = [
  {
    icon: '🐾',
    name: '普通用户',
    condition: '注册即可',
    tagType: 'info',
    discount: '无折扣',
    featured: false,
    benefits: [
      { icon: '✅', text: '注册即送50积分' },
      { icon: '✅', text: '享受基础配送服务' },
      { icon: '✅', text: '参与商城促销活动' },
      { icon: '✅', text: '商品评价获积分' }
    ]
  },
  {
    icon: '🥈',
    name: '银卡会员',
    condition: '累计消费满500元',
    tagType: '',
    discount: '9.8折',
    featured: false,
    benefits: [
      { icon: '✅', text: '享受全场9.8折优惠' },
      { icon: '✅', text: '每月1张免运费券' },
      { icon: '✅', text: '生日月双倍积分' },
      { icon: '✅', text: '新品优先购买权' },
      { icon: '✅', text: '专属客服通道' }
    ]
  },
  {
    icon: '🥇',
    name: '金卡会员',
    condition: '累计消费满2000元',
    tagType: 'warning',
    discount: '9.5折',
    featured: true,
    benefits: [
      { icon: '✅', text: '享受全场9.5折优惠' },
      { icon: '✅', text: '每月3张免运费券' },
      { icon: '✅', text: '生日月三倍积分' },
      { icon: '✅', text: '新品优先购买权' },
      { icon: '✅', text: '专属客服通道' },
      { icon: '✅', text: '年度宠物体检优惠券' },
      { icon: '✅', text: '会员专属折扣日' }
    ]
  },
  {
    icon: '💎',
    name: '钻石会员',
    condition: '累计消费满5000元',
    tagType: 'danger',
    discount: '9折',
    featured: false,
    benefits: [
      { icon: '✅', text: '享受全场9折优惠' },
      { icon: '✅', text: '无限免运费' },
      { icon: '✅', text: '所有月份双倍积分' },
      { icon: '✅', text: '新品抢先体验' },
      { icon: '✅', text: 'VIP一对一客服' },
      { icon: '✅', text: '年度宠物体检优惠券x2' },
      { icon: '✅', text: '线下活动优先邀请' },
      { icon: '✅', text: '专属定制礼品' }
    ]
  }
]

const pointsData = [
  { points: '100积分', reward: '5元无门槛优惠券', value: '5元', limit: '每月3次' },
  { points: '200积分', reward: '10元满50减优惠券', value: '10元', limit: '每月2次' },
  { points: '500积分', reward: '宠物零食大礼包', value: '30元', limit: '每季度1次' },
  { points: '1000积分', reward: '50元无门槛优惠券', value: '50元', limit: '每月1次' },
  { points: '2000积分', reward: '品牌猫粮/狗粮1袋', value: '80元', limit: '每半年1次' },
  { points: '5000积分', reward: '年度宠物健康体检卡', value: '200元', limit: '每年1次' }
]
</script>

<style scoped>
.help-page {
  min-height: 100vh;
  background: var(--paw-bg);
}

/* ── Hero ── */
.hero {
  background: linear-gradient(135deg, var(--paw-amber-50) 0%, var(--paw-coral-50) 100%);
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

/* ── Tier Grid ── */
.tier-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.tier-card {
  text-align: center;
  border: none;
  position: relative;
  transition: all var(--paw-normal);
  overflow: hidden;
}

.tier-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--paw-shadow-lg);
}

.tier-card.featured {
  border: 2px solid var(--paw-amber);
  background: linear-gradient(180deg, #FFFBF5 0%, #FFF6E0 100%);
}

.featured-badge {
  position: absolute;
  top: 12px;
  right: -28px;
  background: var(--paw-amber);
  color: #fff;
  font-size: 11px;
  font-weight: 700;
  padding: 4px 32px;
  transform: rotate(45deg);
}

.tier-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.tier-name {
  font-size: 20px;
  font-weight: 800;
  color: var(--paw-ink);
  margin-bottom: 8px;
}

.tier-condition {
  margin-bottom: 16px;
}

.tier-discount {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
  padding: 12px;
  background: var(--paw-coral-50);
  border-radius: var(--paw-radius-sm);
}

.discount-label {
  font-size: 12px;
  color: var(--paw-ink-3);
}

.discount-value {
  font-size: 28px;
  font-weight: 800;
  color: var(--paw-coral);
}

.tier-benefits {
  list-style: none;
  padding: 0;
  margin: 0;
  text-align: left;
}

.tier-benefits li {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: var(--paw-ink-2);
  padding: 6px 0;
  border-bottom: 1px solid var(--paw-border-light);
}

.tier-benefits li:last-child {
  border-bottom: none;
}

.benefit-icon {
  flex-shrink: 0;
}

/* ── Rules Grid ── */
.rules-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.rule-card {
  text-align: center;
  border: none;
  transition: all var(--paw-normal);
}

.rule-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--paw-shadow-md);
}

.rule-icon {
  font-size: 36px;
  margin-bottom: 12px;
}

.rule-card h4 {
  font-size: 16px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 8px;
}

.rule-card p {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.6;
}

/* ── Points Table ── */
.points-table {
  border-radius: var(--paw-radius-md);
  overflow: hidden;
}

/* ── Back Button ── */
.back-section {
  text-align: center;
  padding: 32px 0 64px;
}

@media (max-width: 1024px) {
  .tier-grid,
  .rules-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .tier-grid,
  .rules-grid {
    grid-template-columns: 1fr;
  }
  .hero-title {
    font-size: 28px;
  }
}
</style>
