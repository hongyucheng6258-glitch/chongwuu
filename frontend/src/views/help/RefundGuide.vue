<template>
  <div class="help-page paw-pattern">
    <!-- Hero Section -->
    <div class="hero">
      <div class="hero-content animate-fade-up">
        <span class="hero-icon">💳</span>
        <h1 class="hero-title">退款说明</h1>
        <p class="hero-desc">透明高效的退款流程，保障您的资金安全</p>
      </div>
    </div>

    <div class="page-container">
      <!-- Refund Process -->
      <section class="section animate-fade-up delay-1">
        <h2 class="section-title">
          <el-icon><Guide /></el-icon>
          退款流程
        </h2>
        <p class="section-subtitle">从申请到到账，全程透明可追踪</p>

        <el-steps :active="5" align-center class="refund-steps">
          <el-step title="提交申请" description="选择退款原因并提交">
            <template #icon>
              <div class="step-icon">📝</div>
            </template>
          </el-step>
          <el-step title="商家审核" description="1-2个工作日审核">
            <template #icon>
              <div class="step-icon">🔍</div>
            </template>
          </el-step>
          <el-step title="商品回收" description="退回商品并验收">
            <template #icon>
              <div class="step-icon">📦</div>
            </template>
          </el-step>
          <el-step title="财务处理" description="系统处理退款">
            <template #icon>
              <div class="step-icon">💰</div>
            </template>
          </el-step>
          <el-step title="退款到账" description="款项返回原账户">
            <template #icon>
              <div class="step-icon">✅</div>
            </template>
          </el-step>
        </el-steps>
      </section>

      <!-- Refund Timeline -->
      <section class="section animate-fade-up delay-2">
        <h2 class="section-title">
          <el-icon><Timer /></el-icon>
          退款时效
        </h2>
        <p class="section-subtitle">不同阶段的处理时间说明</p>

        <div class="timeline-grid">
          <div class="timeline-card" v-for="(item, index) in refundTimeline" :key="index">
            <div class="timeline-num">{{ index + 1 }}</div>
            <h4>{{ item.stage }}</h4>
            <p class="timeline-desc">{{ item.desc }}</p>
            <div class="timeline-time">
              <el-icon><Clock /></el-icon>
              <span>{{ item.time }}</span>
            </div>
          </div>
        </div>
      </section>

      <!-- Refund Methods -->
      <section class="section animate-fade-up delay-2">
        <h2 class="section-title">
          <el-icon><CreditCard /></el-icon>
          退款方式
        </h2>
        <p class="section-subtitle">退款将原路返回您的支付账户</p>

        <el-table :data="refundMethods" stripe class="refund-table" :header-cell-style="{ background: 'var(--paw-coral-50)', color: 'var(--paw-ink)' }">
          <el-table-column prop="method" label="支付方式" width="140" />
          <el-table-column prop="refundTo" label="退款至" />
          <el-table-column prop="time" label="到账时间" width="160" />
          <el-table-column prop="note" label="备注" />
        </el-table>
      </section>

      <!-- Refund Scenarios -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><Sort /></el-icon>
          退款场景
        </h2>

        <div class="scenario-list">
          <div class="scenario-item" v-for="(item, index) in refundScenarios" :key="index">
            <div class="scenario-header">
              <span class="scenario-icon">{{ item.icon }}</span>
              <div>
                <h4>{{ item.title }}</h4>
                <el-tag :type="item.tagType" size="small">{{ item.tag }}</el-tag>
              </div>
            </div>
            <p class="scenario-desc">{{ item.desc }}</p>
            <div class="scenario-detail">
              <span class="detail-label">退款金额：</span>
              <span class="detail-value">{{ item.amount }}</span>
            </div>
          </div>
        </div>
      </section>

      <!-- Refund Status -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><InfoFilled /></el-icon>
          退款状态说明
        </h2>

        <div class="status-grid">
          <div class="status-item" v-for="(status, index) in refundStatuses" :key="index">
            <div class="status-dot" :style="{ background: status.color }"></div>
            <div>
              <h4>{{ status.name }}</h4>
              <p>{{ status.desc }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- FAQ -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><QuestionFilled /></el-icon>
          退款常见问题
        </h2>

        <el-collapse class="faq-collapse">
          <el-collapse-item title="退款可以退到其他账户吗？" name="1">
            <p class="faq-answer">退款仅支持原路返回，即退回到您支付时使用的账户。如需更换退款账户，请联系客服提供相关证明材料。</p>
          </el-collapse-item>
          <el-collapse-item title="优惠券和积分怎么退？" name="2">
            <p class="faq-answer">订单退款时，使用的优惠券将自动返还至您的账户（如在有效期内）。积分将在退款完成后自动退回。</p>
          </el-collapse-item>
          <el-collapse-item title="退款审核不通过怎么办？" name="3">
            <p class="faq-answer">您可以在退款详情中查看不通过的原因。如认为审核有误，可通过"申诉"按钮提交补充材料，或联系在线客服协助处理。</p>
          </el-collapse-item>
          <el-collapse-item title="只退部分商品可以吗？" name="4">
            <p class="faq-answer">可以。在退款申请中选择需要退款的商品即可，系统会自动计算退款金额。未退商品将继续正常配送。</p>
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

const refundTimeline = [
  { stage: '提交申请', desc: '在订单详情页选择退款商品、填写退款原因并提交申请', time: '即时' },
  { stage: '商家审核', desc: '商家收到退款申请后进行审核，核实退款原因和商品状态', time: '1-2个工作日' },
  { stage: '商品回收', desc: '需要退货的订单，请在审核通过后7天内寄回商品', time: '视物流时效' },
  { stage: '财务处理', desc: '仓库确认收到退回商品后，财务系统开始处理退款', time: '1个工作日' },
  { stage: '退款到账', desc: '退款金额原路返回至您的支付账户', time: '1-3个工作日' }
]

const refundMethods = [
  { method: '微信支付', refundTo: '微信钱包', time: '1-3个工作日', note: '实时到账或T+1' },
  { method: '支付宝', refundTo: '支付宝余额', time: '1-3个工作日', note: '实时到账或T+1' },
  { method: '银行卡', refundTo: '原银行卡', time: '3-7个工作日', note: '各银行处理速度不同' },
  { method: '信用卡', refundTo: '原信用卡', time: '5-10个工作日', note: '以银行账单周期为准' },
  { method: '余额支付', refundTo: '初晴萌宠空间账户余额', time: '即时到账', note: '可立即使用' }
]

const refundScenarios = [
  {
    icon: '❌',
    title: '未发货退款',
    tag: '极速退款',
    tagType: 'success',
    desc: '订单支付后商家尚未发货，您申请退款后将立即处理，无需寄回商品。',
    amount: '全额退款（含运费）'
  },
  {
    icon: '🔄',
    title: '已发货退款',
    tag: '需退货',
    tagType: 'warning',
    desc: '商品已发出但尚未签收，您可申请拒收。如已签收，需按退货流程寄回商品。',
    amount: '商品金额 + 运费（质量问题）'
  },
  {
    icon: '⚠️',
    title: '部分退款',
    tag: '部分退',
    tagType: 'info',
    desc: '订单中部分商品需要退款，退回对应商品后系统自动计算退款金额。',
    amount: '退回商品的实际支付金额'
  },
  {
    icon: '🎁',
    title: '赠品/附件退款',
    tag: '随主商品',
    tagType: 'info',
    desc: '如主商品退款，附赠的赠品和配件需一并退回。缺少赠品可能影响退款金额。',
    amount: '按实际退回商品计算'
  }
]

const refundStatuses = [
  { name: '待审核', desc: '退款申请已提交，等待商家审核', color: 'var(--paw-amber)' },
  { name: '审核通过', desc: '商家已同意退款，等待退货/处理', color: 'var(--paw-sky)' },
  { name: '退款中', desc: '财务正在处理退款，即将到账', color: 'var(--paw-coral)' },
  { name: '退款成功', desc: '退款已完成，款项已到账', color: 'var(--paw-sage)' },
  { name: '退款关闭', desc: '退款申请已取消或被拒绝', color: 'var(--paw-ink-4)' }
]
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
.refund-steps {
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
  background: var(--paw-coral-50);
  border-radius: 50%;
}

/* ── Timeline Grid ── */
.timeline-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
}

.timeline-card {
  text-align: center;
  padding: 24px 16px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  transition: all var(--paw-normal);
}

.timeline-card:hover {
  box-shadow: var(--paw-shadow-sm);
  transform: translateY(-2px);
}

.timeline-num {
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
  margin: 0 auto 12px;
}

.timeline-card h4 {
  font-size: 15px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 8px;
}

.timeline-desc {
  font-size: 12px;
  color: var(--paw-ink-2);
  line-height: 1.5;
  margin-bottom: 12px;
}

.timeline-time {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  font-size: 12px;
  color: var(--paw-coral);
  font-weight: 600;
}

/* ── Refund Table ── */
.refund-table {
  border-radius: var(--paw-radius-md);
  overflow: hidden;
}

/* ── Scenario List ── */
.scenario-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.scenario-item {
  padding: 24px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  border-left: 4px solid var(--paw-coral);
  transition: all var(--paw-normal);
}

.scenario-item:hover {
  box-shadow: var(--paw-shadow-sm);
}

.scenario-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.scenario-icon {
  font-size: 28px;
}

.scenario-header h4 {
  font-size: 16px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 4px;
}

.scenario-desc {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.6;
  margin-bottom: 12px;
}

.scenario-detail {
  display: flex;
  align-items: center;
  gap: 8px;
  padding-top: 12px;
  border-top: 1px solid var(--paw-border-light);
}

.detail-label {
  font-size: 13px;
  color: var(--paw-ink-3);
}

.detail-value {
  font-size: 14px;
  font-weight: 700;
  color: var(--paw-coral);
}

/* ── Status Grid ── */
.status-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
}

.status-item {
  text-align: center;
  padding: 20px 12px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
}

.status-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin: 0 auto 12px;
}

.status-item h4 {
  font-size: 14px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 6px;
}

.status-item p {
  font-size: 12px;
  color: var(--paw-ink-2);
  line-height: 1.5;
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

@media (max-width: 1024px) {
  .timeline-grid,
  .status-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .timeline-grid,
  .status-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .hero-title {
    font-size: 28px;
  }
}
</style>
