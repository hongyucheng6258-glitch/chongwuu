<template>
  <div class="help-page paw-pattern">
    <!-- Hero Section -->
    <div class="hero">
      <div class="hero-content animate-fade-up">
        <span class="hero-icon">🔄</span>
        <h1 class="hero-title">退换货政策</h1>
        <p class="hero-desc">保障您的购物权益，放心为爱宠选购好物</p>
      </div>
    </div>

    <div class="page-container">
      <!-- Policy Overview -->
      <section class="section animate-fade-up delay-1">
        <h2 class="section-title">
          <el-icon><Document /></el-icon>
          退换货总则
        </h2>

        <div class="policy-overview">
          <div class="overview-card">
            <div class="overview-icon">✅</div>
            <h4>7天无理由退换</h4>
            <p>自签收之日起7天内，商品完好且不影响二次销售的情况下，可申请无理由退换货。</p>
          </div>
          <div class="overview-card">
            <div class="overview-icon">🔧</div>
            <h4>15天质量问题</h4>
            <p>自签收之日起15天内，如商品存在质量问题，可免费退换货，运费由我们承担。</p>
          </div>
          <div class="overview-card">
            <div class="overview-icon">🛡️</div>
            <h4>30天质量保障</h4>
            <p>自签收之日起30天内，如出现非人为质量问题，可申请维修或更换。</p>
          </div>
        </div>
      </section>

      <!-- Returnable Items -->
      <section class="section animate-fade-up delay-2">
        <h2 class="section-title">
          <el-icon><CircleCheck /></el-icon>
          适用退换货商品
        </h2>

        <div class="item-grid">
          <div class="item-card returnable">
            <div class="item-header">
              <el-icon class="item-check"><SuccessFilled /></el-icon>
              <h4>支持退换货</h4>
            </div>
            <ul>
              <li v-for="item in returnableItems" :key="item">
                <el-icon><Check /></el-icon>
                {{ item }}
              </li>
            </ul>
          </div>
          <div class="item-card non-returnable">
            <div class="item-header">
              <el-icon class="item-cross"><CircleClose /></el-icon>
              <h4>不支持退换货</h4>
            </div>
            <ul>
              <li v-for="item in nonReturnableItems" :key="item">
                <el-icon><Close /></el-icon>
                {{ item }}
              </li>
            </ul>
          </div>
        </div>
      </section>

      <!-- Process -->
      <section class="section animate-fade-up delay-2">
        <h2 class="section-title">
          <el-icon><Guide /></el-icon>
          退换货流程
        </h2>

        <el-steps :active="5" align-center class="return-steps">
          <el-step title="提交申请" description="在订单详情中申请">
            <template #icon>
              <div class="step-icon">📝</div>
            </template>
          </el-step>
          <el-step title="审核通过" description="客服1-2个工作日审核">
            <template #icon>
              <div class="step-icon">✅</div>
            </template>
          </el-step>
          <el-step title="寄回商品" description="按指引打包寄回">
            <template #icon>
              <div class="step-icon">📦</div>
            </template>
          </el-step>
          <el-step title="验收商品" description="仓库检查商品状态">
            <template #icon>
              <div class="step-icon">🔍</div>
            </template>
          </el-step>
          <el-step title="完成退换" description="退款或换新发出">
            <template #icon>
              <div class="step-icon">🎉</div>
            </template>
          </el-step>
        </el-steps>
      </section>

      <!-- Conditions -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><InfoFilled /></el-icon>
          退换货条件
        </h2>

        <el-table :data="conditionsData" stripe class="conditions-table" :header-cell-style="{ background: 'var(--paw-coral-50)', color: 'var(--paw-ink)' }">
          <el-table-column prop="type" label="退换类型" width="140" />
          <el-table-column prop="timeLimit" label="时限要求" width="140" />
          <el-table-column prop="condition" label="适用条件" />
          <el-table-column prop="shipping" label="运费承担" width="120" />
        </el-table>
      </section>

      <!-- Important Notes -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><Warning /></el-icon>
          重要提示
        </h2>

        <div class="notes-list">
          <el-alert
            v-for="(note, index) in importantNotes"
            :key="index"
            :title="note.title"
            :description="note.desc"
            :type="note.type"
            show-icon
            :closable="false"
            class="note-alert"
          />
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

const returnableItems = [
  '宠物玩具（未使用、包装完好）',
  '宠物服饰（未洗涤、吊牌完好）',
  '宠物窝/垫（未使用、包装完好）',
  '宠物推车/背包（未使用、配件齐全）',
  '宠物食器/水器（未拆封）',
  '宠物美容工具（未拆封）',
  '宠物智能设备（未激活、包装完好）'
]

const nonReturnableItems = [
  '已拆封的宠物食品（猫粮、狗粮、零食等）',
  '已拆封的宠物药品/保健品',
  '定制类商品（刻字项圈、定制服装等）',
  '活体宠物',
  '已使用的消耗品（尿垫、猫砂等）',
  '促销特价商品（特殊说明除外）',
  '已过保质期的商品'
]

const conditionsData = [
  { type: '无理由退货', timeLimit: '签收后7天内', condition: '商品完好、包装齐全、不影响二次销售', shipping: '买家承担' },
  { type: '质量问题退货', timeLimit: '签收后15天内', condition: '商品存在非人为质量缺陷', shipping: '商家承担' },
  { type: '质量问题换货', timeLimit: '签收后30天内', condition: '商品存在非人为质量缺陷', shipping: '商家承担' },
  { type: '发错/漏发', timeLimit: '签收后7天内', condition: '收到商品与订单不符', shipping: '商家承担' },
  { type: '商品损坏', timeLimit: '签收后48小时内', condition: '运输途中造成的损坏（需提供照片）', shipping: '商家承担' }
]

const importantNotes = [
  {
    title: '退回商品请保持原状',
    desc: '退回商品时请使用原包装或同等防护包装，确保商品在运输途中不会损坏。如因包装不当导致二次损坏，可能影响退款金额。',
    type: 'warning'
  },
  {
    title: '生鲜食品特殊政策',
    desc: '宠物生鲜食品由于保质期短，不支持无理由退换。如收到时已变质或包装破损，请在签收后24小时内联系客服处理。',
    type: 'error'
  },
  {
    title: '退换货期间请勿使用商品',
    desc: '申请退换货后，请停止使用该商品。如商品出现额外磨损或损坏，可能影响退换货审核结果。',
    type: 'info'
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
  background: linear-gradient(135deg, var(--paw-coral-50) 0%, var(--paw-sage-50) 100%);
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

/* ── Policy Overview ── */
.policy-overview {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.overview-card {
  text-align: center;
  padding: 32px 24px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  transition: all var(--paw-normal);
}

.overview-card:hover {
  box-shadow: var(--paw-shadow-md);
  transform: translateY(-4px);
}

.overview-icon {
  font-size: 40px;
  margin-bottom: 16px;
}

.overview-card h4 {
  font-size: 17px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 8px;
}

.overview-card p {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.6;
}

/* ── Item Grid ── */
.item-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.item-card {
  padding: 24px;
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
}

.item-card.returnable {
  background: var(--paw-sage-50);
  border: 1px solid var(--paw-sage-100);
}

.item-card.non-returnable {
  background: var(--paw-coral-50);
  border: 1px solid var(--paw-coral-100);
}

.item-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
}

.item-check {
  color: var(--paw-sage);
  font-size: 20px;
}

.item-cross {
  color: var(--paw-coral);
  font-size: 20px;
}

.item-header h4 {
  font-size: 16px;
  font-weight: 700;
  color: var(--paw-ink);
}

.item-card ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.item-card li {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: var(--paw-ink-2);
  padding: 6px 0;
}

.returnable li .el-icon {
  color: var(--paw-sage);
}

.non-returnable li .el-icon {
  color: var(--paw-coral);
}

/* ── Steps ── */
.return-steps {
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

/* ── Conditions Table ── */
.conditions-table {
  border-radius: var(--paw-radius-md);
  overflow: hidden;
}

/* ── Notes ── */
.notes-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.note-alert {
  border-radius: var(--paw-radius-md) !important;
}

/* ── Back Button ── */
.back-section {
  text-align: center;
  padding: 32px 0 64px;
}

@media (max-width: 768px) {
  .policy-overview,
  .item-grid {
    grid-template-columns: 1fr;
  }
  .hero-title {
    font-size: 28px;
  }
}
</style>
