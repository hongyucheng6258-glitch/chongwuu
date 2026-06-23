<template>
  <div class="help-page paw-pattern">
    <!-- Hero Section -->
    <div class="hero">
      <div class="hero-content animate-fade-up">
        <span class="hero-icon">❓</span>
        <h1 class="hero-title">常见问题</h1>
        <p class="hero-desc">汇集宠物主人最关心的问题，快速找到您需要的答案</p>
      </div>
    </div>

    <div class="page-container">
      <!-- FAQ Categories -->
      <section class="section animate-fade-up delay-1">
        <div class="faq-categories">
          <el-button
            v-for="cat in categories"
            :key="cat.key"
            :type="activeCategory === cat.key ? 'primary' : 'default'"
            round
            @click="activeCategory = cat.key"
          >
            {{ cat.icon }} {{ cat.label }}
          </el-button>
        </div>
      </section>

      <!-- FAQ List -->
      <section class="section animate-fade-up delay-2">
        <el-collapse v-model="activeNames" accordion class="faq-collapse">
          <el-collapse-item
            v-for="(item, index) in filteredFaqs"
            :key="index"
            :name="index"
            class="faq-item"
          >
            <template #title>
              <div class="faq-title">
                <span class="faq-icon">{{ item.icon }}</span>
                <span class="faq-question">{{ item.question }}</span>
                <el-tag v-if="item.hot" type="danger" size="small" class="hot-tag">热门</el-tag>
              </div>
            </template>
            <div class="faq-answer">
              <p v-for="(para, i) in item.answer" :key="i">{{ para }}</p>
              <div v-if="item.extra" class="faq-extra">
                <el-alert
                  :title="item.extra.title"
                  :description="item.extra.desc"
                  :type="item.extra.type"
                  show-icon
                  :closable="false"
                />
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </section>

      <!-- Contact Section -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><Service /></el-icon>
          没有找到答案？
        </h2>
        <div class="contact-box">
          <div class="contact-item">
            <span class="contact-icon">💬</span>
            <div>
              <h4>在线客服</h4>
              <p>工作时间：每天 9:00 - 22:00</p>
            </div>
          </div>
          <div class="contact-item">
            <span class="contact-icon">📞</span>
            <div>
              <h4>客服热线</h4>
              <p>400-8888-PAWS</p>
            </div>
          </div>
          <div class="contact-item">
            <span class="contact-icon">📧</span>
            <div>
              <h4>邮件反馈</h4>
              <p>support@pawspce.com</p>
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
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const activeCategory = ref('all')
const activeNames = ref(0)

const categories = [
  { key: 'all', label: '全部问题', icon: '📋' },
  { key: 'order', label: '订单相关', icon: '📦' },
  { key: 'payment', label: '支付相关', icon: '💳' },
  { key: 'shipping', label: '配送相关', icon: '🚚' },
  { key: 'after', label: '售后相关', icon: '🔄' },
  { key: 'account', label: '账户相关', icon: '👤' }
]

const faqs = [
  {
    category: 'order',
    icon: '📦',
    question: '如何查看我的订单状态？',
    hot: true,
    answer: [
      '登录后点击顶部导航栏的用户头像，选择"我的订单"即可查看所有订单。每个订单都会显示当前状态，包括待支付、待发货、配送中、已完成等。'
    ]
  },
  {
    category: 'order',
    icon: '❌',
    question: '订单可以取消吗？',
    answer: [
      '未支付的订单可以随时取消。已支付但未发货的订单，可在订单详情页申请取消，退款将在1-3个工作日内原路返回。已发货的订单无法直接取消，需要收到商品后申请退货。'
    ]
  },
  {
    category: 'payment',
    icon: '💳',
    question: '支持哪些支付方式？',
    hot: true,
    answer: [
      '我们支持微信支付、支付宝、银联云闪付、信用卡/借记卡等主流支付方式。部分商品还支持货到付款（仅限部分城市）。'
    ]
  },
  {
    category: 'payment',
    icon: '⏰',
    question: '支付超时怎么办？',
    answer: [
      '订单提交后需在30分钟内完成支付，超时订单将自动取消。如果您的支付遇到问题，建议更换支付方式重试。如已扣款但订单未显示支付成功，请联系客服处理。'
    ],
    extra: {
      title: '温馨提示',
      desc: '支付遇到问题时请勿重复提交，以免造成多次扣款。',
      type: 'warning'
    }
  },
  {
    category: 'shipping',
    icon: '🚚',
    question: '一般多久能收到商品？',
    hot: true,
    answer: [
      '同城极速配送：下单后2小时内送达（限主城区）。',
      '标准快递：1-3个工作日送达（全国大部分地区）。',
      '偏远地区：3-7个工作日送达。',
      '具体配送时间以订单详情页显示为准，您可实时追踪物流信息。'
    ]
  },
  {
    category: 'shipping',
    icon: '📍',
    question: '如何修改收货地址？',
    answer: [
      '未发货的订单可以在订单详情页修改收货地址。已发货的订单无法修改地址，建议您联系快递公司进行转寄，或联系我们的客服协助处理。'
    ]
  },
  {
    category: 'after',
    icon: '🔄',
    question: '商品可以退换货吗？',
    answer: [
      '大部分商品支持7天无理由退换货。生鲜类宠物食品、已拆封的药品等特殊商品不支持无理由退货。详细退换货政策请查看"退换货政策"页面。'
    ]
  },
  {
    category: 'after',
    icon: '💰',
    question: '退款多久能到账？',
    answer: [
      '退款审核通过后，款项将在1-3个工作日内原路返回您的支付账户。信用卡退款可能需要5-10个工作日。具体到账时间以各支付平台处理速度为准。'
    ]
  },
  {
    category: 'account',
    icon: '👤',
    question: '如何修改个人信息？',
    answer: [
      '登录后进入"个人中心"，可以修改头像、昵称、手机号、密码等个人信息。手机号修改需要验证原手机号和新手机号。'
    ]
  },
  {
    category: 'account',
    icon: '🐾',
    question: '如何查看我的会员等级和积分？',
    answer: [
      '登录后进入"个人中心"，在会员信息区域可以查看当前会员等级、累计消费金额、可用积分以及距下一等级的进度。'
    ]
  }
]

const filteredFaqs = computed(() => {
  if (activeCategory.value === 'all') return faqs
  return faqs.filter(f => f.category === activeCategory.value)
})
</script>

<style scoped>
.help-page {
  min-height: 100vh;
  background: var(--paw-bg);
}

/* ── Hero ── */
.hero {
  background: linear-gradient(135deg, var(--paw-sky-50) 0%, var(--paw-coral-50) 100%);
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
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-title .el-icon {
  color: var(--paw-coral);
}

/* ── Categories ── */
.faq-categories {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

/* ── FAQ Collapse ── */
.faq-collapse {
  border: none;
}

.faq-item {
  margin-bottom: 12px;
  border-radius: var(--paw-radius-md) !important;
  overflow: hidden;
  border: 1px solid var(--paw-border-light) !important;
  background: var(--paw-card);
  transition: all var(--paw-normal);
}

.faq-item:hover {
  border-color: var(--paw-coral-100) !important;
  box-shadow: var(--paw-shadow-sm);
}

.faq-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 15px;
  font-weight: 600;
  color: var(--paw-ink);
}

.faq-icon {
  font-size: 20px;
  flex-shrink: 0;
}

.hot-tag {
  flex-shrink: 0;
}

.faq-answer {
  padding: 8px 20px 20px 50px;
}

.faq-answer p {
  font-size: 14px;
  color: var(--paw-ink-2);
  line-height: 1.8;
  margin-bottom: 8px;
}

.faq-extra {
  margin-top: 12px;
}

/* ── Contact Box ── */
.contact-box {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 24px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  transition: all var(--paw-normal);
}

.contact-item:hover {
  box-shadow: var(--paw-shadow-sm);
  transform: translateY(-2px);
}

.contact-icon {
  font-size: 32px;
  flex-shrink: 0;
}

.contact-item h4 {
  font-size: 15px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 4px;
}

.contact-item p {
  font-size: 13px;
  color: var(--paw-ink-3);
}

/* ── Back Button ── */
.back-section {
  text-align: center;
  padding: 32px 0 64px;
}

@media (max-width: 768px) {
  .contact-box {
    grid-template-columns: 1fr;
  }
  .hero-title {
    font-size: 28px;
  }
}
</style>
