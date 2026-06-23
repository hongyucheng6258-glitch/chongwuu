<template>
  <div class="help-page paw-pattern">
    <!-- Hero Section -->
    <div class="hero">
      <div class="hero-content animate-fade-up">
        <span class="hero-icon">🏪</span>
        <h1 class="hero-title">上门自提</h1>
        <p class="hero-desc">就近门店自提，省去配送等待，即刻拥有心仪好物</p>
      </div>
    </div>

    <div class="page-container">
      <!-- How It Works -->
      <section class="section animate-fade-up delay-1">
        <h2 class="section-title">
          <el-icon><Guide /></el-icon>
          自提流程
        </h2>
        <p class="section-subtitle">简单四步，轻松到店取货</p>

        <el-steps :active="4" align-center class="pickup-steps">
          <el-step title="选择自提" description="下单时选择门店自提">
            <template #icon>
              <div class="step-icon">📍</div>
            </template>
          </el-step>
          <el-step title="选择门店" description="选择就近的初晴萌宠空间门店">
            <template #icon>
              <div class="step-icon">🏪</div>
            </template>
          </el-step>
          <el-step title="等待备货" description="门店收到订单后准备商品">
            <template #icon>
              <div class="step-icon">📦</div>
            </template>
          </el-step>
          <el-step title="到店取货" description="凭取货码到店领取">
            <template #icon>
              <div class="step-icon">✅</div>
            </template>
          </el-step>
        </el-steps>
      </section>

      <!-- Pickup Locations -->
      <section class="section animate-fade-up delay-2">
        <h2 class="section-title">
          <el-icon><Location /></el-icon>
          自提门店
        </h2>
        <p class="section-subtitle">覆盖全城主要区域，总有一家在您身边</p>

        <div class="store-grid">
          <el-card v-for="(store, index) in stores" :key="index" class="store-card" shadow="hover">
            <div class="store-header">
              <span class="store-icon">🐾</span>
              <div>
                <h3 class="store-name">{{ store.name }}</h3>
                <el-tag :type="store.status === '营业中' ? 'success' : 'danger'" size="small">
                  {{ store.status }}
                </el-tag>
              </div>
            </div>
            <div class="store-info">
              <p><el-icon><Location /></el-icon> {{ store.address }}</p>
              <p><el-icon><Clock /></el-icon> {{ store.hours }}</p>
              <p><el-icon><Phone /></el-icon> {{ store.phone }}</p>
            </div>
            <div class="store-features">
              <el-tag v-for="f in store.features" :key="f" size="small" type="info" effect="plain">
                {{ f }}
              </el-tag>
            </div>
          </el-card>
        </div>
      </section>

      <!-- Rules -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><Document /></el-icon>
          自提须知
        </h2>

        <div class="rules-list">
          <div class="rule-item" v-for="(rule, index) in rules" :key="index">
            <div class="rule-header">
              <span class="rule-num">{{ index + 1 }}</span>
              <h4>{{ rule.title }}</h4>
            </div>
            <p>{{ rule.content }}</p>
          </div>
        </div>
      </section>

      <!-- FAQ -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><QuestionFilled /></el-icon>
          自提常见问题
        </h2>

        <el-collapse class="faq-collapse">
          <el-collapse-item title="下单后多久可以到店取货？" name="1">
            <p class="faq-answer">通常下单后1-2小时内门店即可备货完成，您会收到短信或APP推送通知。高峰期（如促销活动期间）可能需要等待更长时间，具体以通知为准。</p>
          </el-collapse-item>
          <el-collapse-item title="可以代他人取货吗？" name="2">
            <p class="faq-answer">可以。代取人需出示取货码（短信或APP中获取）以及代取人身份证件，门店工作人员核对无误后即可取货。</p>
          </el-collapse-item>
          <el-collapse-item title="到店后商品有问题怎么办？" name="3">
            <p class="faq-answer">到店取货时请当面验货。如发现商品存在质量问题，可当场要求门店更换。取货离开门店后，按正常退换货流程处理。</p>
          </el-collapse-item>
          <el-collapse-item title="超过取货期限怎么办？" name="4">
            <p class="faq-answer">自提订单需在备货完成后3天内到店取货。超期未取的订单将自动取消并退款。如需延期，请联系门店客服申请延期保留。</p>
          </el-collapse-item>
        </el-collapse>
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

const stores = [
  {
    name: '初晴萌宠空间·中心广场店',
    status: '营业中',
    address: '市中心商业广场B1层 L108号',
    hours: '周一至周日 10:00 - 22:00',
    phone: '021-8888-1001',
    features: ['宠物洗护', '活体展示', '免费停车']
  },
  {
    name: '初晴萌宠空间·大学城店',
    status: '营业中',
    address: '大学城商业街A栋 2楼 205号',
    hours: '周一至周日 9:30 - 21:30',
    phone: '021-8888-1002',
    features: ['学生优惠', '宠物寄养', '美容造型']
  },
  {
    name: '初晴萌宠空间·滨江花园店',
    status: '营业中',
    address: '滨江大道88号 滨江花园商业中心 1楼',
    hours: '周一至周日 10:00 - 21:00',
    phone: '021-8888-1003',
    features: ['江景体验', '宠物咖啡', '免费WiFi']
  },
  {
    name: '初晴萌宠空间·科技园店',
    status: '营业中',
    address: '高新技术开发区科技大道168号',
    hours: '周一至周五 10:00 - 20:00 / 周末 10:00 - 21:00',
    phone: '021-8888-1004',
    features: ['企业团购', '宠物医疗', '夜间取货']
  }
]

const rules = [
  {
    title: '取货时间',
    content: '收到"备货完成"通知后，请在3天内前往门店取货。门店营业时间内均可取货。'
  },
  {
    title: '取货凭证',
    content: '到店时请出示取货码（可在订单详情中查看），工作人员扫码确认后即可领取商品。'
  },
  {
    title: '当面验货',
    content: '取货时请当面检查商品数量、外观和包装。如有问题可当场提出，门店将为您更换。'
  },
  {
    title: '身份验证',
    content: '取货时可能需要验证下单人身份信息（手机号后四位），请确保信息准确。'
  },
  {
    title: '取消自提',
    content: '如需取消自提订单，请在门店备货前操作。备货完成后取消需到店办理或联系客服。'
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
  background: linear-gradient(135deg, var(--paw-sage-50) 0%, var(--paw-coral-50) 100%);
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

/* ── Steps ── */
.pickup-steps {
  max-width: 800px;
  margin: 0 auto;
}

.step-icon {
  font-size: 28px;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--paw-sage-50);
  border-radius: 50%;
}

/* ── Store Grid ── */
.store-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.store-card {
  border: none;
  transition: all var(--paw-normal);
}

.store-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--paw-shadow-md);
}

.store-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.store-icon {
  font-size: 28px;
}

.store-name {
  font-size: 16px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 4px;
}

.store-info p {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: var(--paw-ink-2);
  padding: 4px 0;
}

.store-info .el-icon {
  color: var(--paw-coral);
}

.store-features {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid var(--paw-border-light);
}

/* ── Rules ── */
.rules-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.rule-item {
  padding: 20px 24px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  border-left: 4px solid var(--paw-sage);
}

.rule-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.rule-num {
  width: 28px;
  height: 28px;
  background: var(--paw-sage);
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  font-size: 13px;
  flex-shrink: 0;
}

.rule-item h4 {
  font-size: 15px;
  font-weight: 700;
  color: var(--paw-ink);
}

.rule-item p {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.6;
  padding-left: 40px;
}

/* ── FAQ ── */
.faq-collapse {
  border: none;
}

.faq-answer {
  font-size: 14px;
  color: var(--paw-ink-2);
  line-height: 1.8;
  padding: 4px 0;
}

/* ── Back Button ── */
.back-section {
  text-align: center;
  padding: 32px 0 64px;
}

@media (max-width: 768px) {
  .store-grid {
    grid-template-columns: 1fr;
  }
  .hero-title {
    font-size: 28px;
  }
}
</style>
