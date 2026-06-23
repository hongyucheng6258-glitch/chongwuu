<template>
  <div class="help-page paw-pattern">
    <!-- Hero Section -->
    <div class="hero">
      <div class="hero-content animate-fade-up">
        <span class="hero-icon">💰</span>
        <h1 class="hero-title">配送费用</h1>
        <p class="hero-desc">透明合理的配送费用，会员还可享受免运费特权</p>
      </div>
    </div>

    <div class="page-container">
      <!-- Fee Table -->
      <section class="section animate-fade-up delay-1">
        <h2 class="section-title">
          <el-icon><Tickets /></el-icon>
          配送费用标准
        </h2>
        <p class="section-subtitle">根据配送方式和地区不同，费用有所差异</p>

        <el-table :data="feeData" stripe class="fee-table" :header-cell-style="{ background: 'var(--paw-coral-50)', color: 'var(--paw-ink)' }">
          <el-table-column prop="method" label="配送方式" width="140" />
          <el-table-column prop="area" label="配送区域" />
          <el-table-column prop="fee" label="配送费用" width="120">
            <template #default="{ row }">
              <span class="fee-amount">{{ row.fee }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="预计时效" width="140" />
        </el-table>
      </section>

      <!-- Free Shipping -->
      <section class="section animate-fade-up delay-2">
        <h2 class="section-title">
          <el-icon><Present /></el-icon>
          免运费条件
        </h2>
        <p class="section-subtitle">满足以下任一条件即可享受免费配送</p>

        <div class="free-grid">
          <div class="free-card" v-for="(item, index) in freeConditions" :key="index">
            <div class="free-icon">{{ item.icon }}</div>
            <h4>{{ item.title }}</h4>
            <p>{{ item.desc }}</p>
            <el-tag :type="item.tagType" size="small">{{ item.condition }}</el-tag>
          </div>
        </div>
      </section>

      <!-- Member Free Shipping -->
      <section class="section animate-fade-up delay-2">
        <h2 class="section-title">
          <el-icon><Medal /></el-icon>
          会员免运费特权
        </h2>

        <div class="member-grid">
          <div class="member-card" v-for="(member, index) in memberFreeShipping" :key="index">
            <div class="member-badge" :style="{ background: member.color }">
              {{ member.icon }}
            </div>
            <div class="member-info">
              <h4>{{ member.name }}</h4>
              <p>{{ member.benefit }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- Weight Rules -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><Scale /></el-icon>
          重量与体积计费
        </h2>
        <p class="section-subtitle">超出基础重量/体积将产生额外费用</p>

        <div class="weight-rules">
          <div class="weight-item">
            <div class="weight-header">
              <span class="weight-icon">📏</span>
              <h4>基础标准</h4>
            </div>
            <p>单件商品重量不超过 <strong>5kg</strong>，且三边之和不超过 <strong>60cm</strong>，按标准配送费收取。</p>
          </div>
          <div class="weight-item">
            <div class="weight-header">
              <span class="weight-icon">📦</span>
              <h4>超重商品</h4>
            </div>
            <p>超过5kg的部分，每增加1kg加收 <strong>2元</strong>。大容量猫砂、狗粮等较重商品可能产生额外费用。</p>
          </div>
          <div class="weight-item">
            <div class="weight-header">
              <span class="weight-icon">📐</span>
              <h4>超大体积</h4>
            </div>
            <p>超过基础体积标准的商品，每增加10cm加收 <strong>3元</strong>。大型猫爬架、宠物窝等可能产生额外费用。</p>
          </div>
        </div>
      </section>

      <!-- Remote Area -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><Warning /></el-icon>
          偏远地区附加费
        </h2>

        <el-alert
          title="偏远地区配送说明"
          description="西藏、新疆、青海、内蒙古、甘肃等偏远地区，标准快递配送费将在基础费用上附加5-15元。具体附加费以结算页面显示为准。"
          type="info"
          show-icon
          :closable="false"
        />
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

const feeData = [
  { method: '⚡ 同城极速达', area: '主城区（距门店5km内）', fee: '12元', time: '2小时内' },
  { method: '⚡ 同城极速达', area: '近郊区（距门店5-15km）', fee: '18元', time: '3小时内' },
  { method: '🚀 次日达', area: '一线城市城区', fee: '8元', time: '次日18:00前' },
  { method: '🚀 次日达', area: '二线城市城区', fee: '10元', time: '次日18:00前' },
  { method: '📦 标准快递', area: '江浙沪地区', fee: '6元', time: '1-2天' },
  { method: '📦 标准快递', area: '全国大部分地区', fee: '8元', time: '2-5天' },
  { method: '📦 标准快递', area: '偏远地区', fee: '15-25元', time: '5-7天' },
  { method: '🧊 冷链配送', area: '一线城市', fee: '15元', time: '1-2天' },
  { method: '🧊 冷链配送', area: '全国大部分地区', fee: '20元', time: '2-3天' }
]

const freeConditions = [
  {
    icon: '🛒',
    title: '满额免运费',
    desc: '订单金额达到指定金额，即可享受免费标准快递配送。',
    condition: '满99元包邮',
    tagType: 'success'
  },
  {
    icon: '🏪',
    title: '门店自提',
    desc: '选择到门店自提，无需支付任何配送费用。',
    condition: '永久免费',
    tagType: 'success'
  },
  {
    icon: '🎫',
    title: '运费券抵扣',
    desc: '使用运费优惠券可抵扣配送费用，优惠券可通过活动或积分兑换获得。',
    condition: '会员每月领取',
    tagType: 'warning'
  },
  {
    icon: '🎉',
    title: '活动免运费',
    desc: '商城不定期举办免运费促销活动，活动期间所有订单免运费。',
    condition: '关注活动公告',
    tagType: 'danger'
  }
]

const memberFreeShipping = [
  {
    icon: '🐾',
    name: '普通用户',
    benefit: '满99元享受标准快递免运费',
    color: 'var(--paw-ink-4)'
  },
  {
    icon: '🥈',
    name: '银卡会员',
    benefit: '每月1张免运费券（极速达/次日达可用）',
    color: '#9CA3AF'
  },
  {
    icon: '🥇',
    name: '金卡会员',
    benefit: '每月3张免运费券，满69元即享标准快递免运费',
    color: 'var(--paw-amber)'
  },
  {
    icon: '💎',
    name: '钻石会员',
    benefit: '所有配送方式永久免运费，无门槛限制',
    color: 'var(--paw-coral)'
  }
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

/* ── Fee Table ── */
.fee-table {
  border-radius: var(--paw-radius-md);
  overflow: hidden;
}

.fee-amount {
  color: var(--paw-coral);
  font-weight: 700;
}

/* ── Free Grid ── */
.free-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.free-card {
  text-align: center;
  padding: 28px 20px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  transition: all var(--paw-normal);
}

.free-card:hover {
  box-shadow: var(--paw-shadow-md);
  transform: translateY(-4px);
}

.free-icon {
  font-size: 36px;
  margin-bottom: 12px;
}

.free-card h4 {
  font-size: 16px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 8px;
}

.free-card p {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.6;
  margin-bottom: 12px;
}

/* ── Member Grid ── */
.member-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.member-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  transition: all var(--paw-normal);
}

.member-card:hover {
  box-shadow: var(--paw-shadow-sm);
  transform: translateY(-2px);
}

.member-badge {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
}

.member-info h4 {
  font-size: 15px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 4px;
}

.member-info p {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.5;
}

/* ── Weight Rules ── */
.weight-rules {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.weight-item {
  padding: 20px 24px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  border-left: 4px solid var(--paw-amber);
}

.weight-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.weight-icon {
  font-size: 24px;
}

.weight-item h4 {
  font-size: 15px;
  font-weight: 700;
  color: var(--paw-ink);
}

.weight-item p {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.6;
  padding-left: 34px;
}

.weight-item strong {
  color: var(--paw-coral);
}

/* ── Back Button ── */
.back-section {
  text-align: center;
  padding: 32px 0 64px;
}

@media (max-width: 1024px) {
  .free-grid,
  .member-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .free-grid,
  .member-grid {
    grid-template-columns: 1fr;
  }
  .hero-title {
    font-size: 28px;
  }
}
</style>
