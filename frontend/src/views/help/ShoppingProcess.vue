<template>
  <div class="help-page paw-pattern">
    <!-- Hero Section -->
    <div class="hero">
      <div class="hero-content animate-fade-up">
        <span class="hero-icon">🛒</span>
        <h1 class="hero-title">购物流程</h1>
        <p class="hero-desc">简单六步，轻松为您的爱宠挑选心仪好物</p>
      </div>
    </div>

    <div class="page-container">
      <!-- Steps Section -->
      <section class="section animate-fade-up delay-1">
        <h2 class="section-title">
          <el-icon><Guide /></el-icon>
          购物六步走
        </h2>
        <p class="section-subtitle">跟随以下步骤，轻松完成购物</p>

        <el-steps :active="6" align-center class="shopping-steps">
          <el-step title="浏览商品" description="发现心仪好物" icon="Search">
            <template #icon>
              <div class="step-icon">🔍</div>
            </template>
          </el-step>
          <el-step title="加入购物车" description="选择规格数量" icon="ShoppingCart">
            <template #icon>
              <div class="step-icon">🛒</div>
            </template>
          </el-step>
          <el-step title="确认订单" description="核对收货信息" icon="Document">
            <template #icon>
              <div class="step-icon">📋</div>
            </template>
          </el-step>
          <el-step title="选择支付" description="多种支付方式" icon="CreditCard">
            <template #icon>
              <div class="step-icon">💳</div>
            </template>
          </el-step>
          <el-step title="等待配送" description="实时物流追踪" icon="Van">
            <template #icon>
              <div class="step-icon">🚚</div>
            </template>
          </el-step>
          <el-step title="确认收货" description="验收满意商品" icon="CircleCheck">
            <template #icon>
              <div class="step-icon">✅</div>
            </template>
          </el-step>
        </el-steps>
      </section>

      <!-- Detail Cards -->
      <section class="section animate-fade-up delay-2">
        <h2 class="section-title">
          <el-icon><InfoFilled /></el-icon>
          详细说明
        </h2>

        <div class="detail-grid">
          <el-card v-for="(step, index) in steps" :key="index" class="detail-card" shadow="hover">
            <div class="card-header">
              <span class="card-number">{{ index + 1 }}</span>
              <span class="card-icon">{{ step.icon }}</span>
            </div>
            <h3 class="card-title">{{ step.title }}</h3>
            <p class="card-desc">{{ step.desc }}</p>
            <ul class="card-tips">
              <li v-for="(tip, i) in step.tips" :key="i">
                <el-icon class="tip-icon"><CircleCheck /></el-icon>
                {{ tip }}
              </li>
            </ul>
          </el-card>
        </div>
      </section>

      <!-- Tips Section -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><Warning /></el-icon>
          温馨提示
        </h2>
        <div class="tips-box">
          <div class="tip-item" v-for="(tip, index) in warmTips" :key="index">
            <span class="tip-badge">{{ tip.badge }}</span>
            <div>
              <h4>{{ tip.title }}</h4>
              <p>{{ tip.content }}</p>
            </div>
          </div>
        </div>
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

const steps = [
  {
    icon: '🔍',
    title: '浏览商品',
    desc: '通过首页分类导航、搜索功能或推荐列表，浏览我们丰富的宠物商品。支持按品牌、价格、销量等条件筛选。',
    tips: [
      '使用顶部搜索栏快速查找商品',
      '通过分类标签精准定位所需品类',
      '查看商品详情页了解规格参数',
      '参考其他宠物主人的真实评价'
    ]
  },
  {
    icon: '🛒',
    title: '加入购物车',
    desc: '选择商品规格（如尺寸、口味、颜色等），设置购买数量，点击"加入购物车"按钮。',
    tips: [
      '选择适合宠物体型的规格',
      '注意查看商品保质期信息',
      '购物车商品可随时修改或删除',
      '支持收藏商品方便下次购买'
    ]
  },
  {
    icon: '📋',
    title: '确认订单',
    desc: '进入购物车结算页面，核对商品信息、收货地址、配送方式等，确认无误后提交订单。',
    tips: [
      '仔细核对收货地址和联系方式',
      '选择合适的配送方式',
      '可使用优惠券或会员折扣',
      '确认订单金额和商品明细'
    ]
  },
  {
    icon: '💳',
    title: '选择支付',
    desc: '支持微信支付、支付宝、银行卡等多种支付方式，选择您最方便的支付渠道完成付款。',
    tips: [
      '支持主流第三方支付平台',
      '订单30分钟内完成支付有效',
      '支付成功后系统自动通知',
      '如支付失败可重新发起支付'
    ]
  },
  {
    icon: '🚚',
    title: '等待配送',
    desc: '支付成功后，商家将尽快为您发货。您可以在"我的订单"中实时查看物流状态。',
    tips: [
      '订单状态实时更新推送',
      '支持查看物流轨迹详情',
      '可联系客服咨询配送进度',
      '配送途中如有问题及时反馈'
    ]
  },
  {
    icon: '✅',
    title: '确认收货',
    desc: '收到商品后，请当面验收。确认商品完好无损后，在订单详情中点击"确认收货"。',
    tips: [
      '签收前检查商品外观包装',
      '确认商品数量和规格正确',
      '如有问题可拒签并联系客服',
      '收货后可对商品进行评价'
    ]
  }
]

const warmTips = [
  {
    badge: '限时',
    title: '订单支付时限',
    content: '提交订单后请在30分钟内完成支付，超时订单将自动取消，商品库存将释放给其他用户。'
  },
  {
    badge: '优惠',
    title: '优惠叠加使用',
    content: '会员折扣与优惠券可叠加使用（特殊活动除外），结算页面会自动为您计算最优价格。'
  },
  {
    badge: '安全',
    title: '支付安全保障',
    content: '所有支付均通过加密通道传输，我们不存储您的支付密码，请放心使用。'
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
  background: linear-gradient(135deg, var(--paw-coral-50) 0%, var(--paw-amber-50) 100%);
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
  letter-spacing: -0.5px;
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

/* ── Steps ── */
.shopping-steps {
  margin: 0 auto;
  max-width: 900px;
}

.step-icon {
  font-size: 28px;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--paw-coral-50);
  border-radius: 50%;
}

/* ── Detail Grid ── */
.detail-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.detail-card {
  border: none;
  transition: all var(--paw-normal);
}

.detail-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--paw-shadow-md);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.card-number {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, var(--paw-coral), var(--paw-amber));
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  font-size: 14px;
}

.card-icon {
  font-size: 32px;
}

.card-title {
  font-size: 17px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 8px;
}

.card-desc {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.7;
  margin-bottom: 16px;
}

.card-tips {
  list-style: none;
  padding: 0;
  margin: 0;
}

.card-tips li {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.6;
  padding: 4px 0;
}

.tip-icon {
  color: var(--paw-sage);
  flex-shrink: 0;
  margin-top: 2px;
}

/* ── Tips Box ── */
.tips-box {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 20px 24px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  border-left: 4px solid var(--paw-coral);
}

.tip-badge {
  flex-shrink: 0;
  padding: 4px 12px;
  background: var(--paw-coral);
  color: #fff;
  border-radius: var(--paw-radius-full);
  font-size: 12px;
  font-weight: 700;
}

.tip-item h4 {
  font-size: 15px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 4px;
}

.tip-item p {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.6;
}

/* ── Back Button ── */
.back-section {
  text-align: center;
  padding: 32px 0 64px;
}

@media (max-width: 768px) {
  .detail-grid {
    grid-template-columns: 1fr;
  }
  .hero-title {
    font-size: 28px;
  }
}
</style>
