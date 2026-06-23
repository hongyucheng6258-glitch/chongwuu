<template>
  <div class="help-page paw-pattern">
    <!-- Hero Section -->
    <div class="hero">
      <div class="hero-content animate-fade-up">
        <span class="hero-icon">🔧</span>
        <h1 class="hero-title">返修退换</h1>
        <p class="hero-desc">商品质量问题无忧售后，专业团队为您解决</p>
      </div>
    </div>

    <div class="page-container">
      <!-- Quality Guarantee -->
      <section class="section animate-fade-up delay-1">
        <h2 class="section-title">
          <el-icon><Shield /></el-icon>
          质量保障承诺
        </h2>
        <p class="section-subtitle">我们为每一件商品提供严格的质量保障</p>

        <div class="guarantee-grid">
          <div class="guarantee-card" v-for="(item, index) in guarantees" :key="index">
            <div class="guarantee-icon">{{ item.icon }}</div>
            <h4>{{ item.title }}</h4>
            <p>{{ item.desc }}</p>
            <div class="guarantee-period">
              <el-icon><Clock /></el-icon>
              <span>{{ item.period }}</span>
            </div>
          </div>
        </div>
      </section>

      <!-- Repair Process -->
      <section class="section animate-fade-up delay-2">
        <h2 class="section-title">
          <el-icon><SetUp /></el-icon>
          返修流程
        </h2>
        <p class="section-subtitle">商品出现质量问题，按以下步骤申请返修</p>

        <el-steps :active="6" align-center class="repair-steps">
          <el-step title="发现问题" description="发现商品质量异常">
            <template #icon>
              <div class="step-icon">🔍</div>
            </template>
          </el-step>
          <el-step title="拍照取证" description="拍摄问题部位照片">
            <template #icon>
              <div class="step-icon">📸</div>
            </template>
          </el-step>
          <el-step title="提交申请" description="上传照片并描述问题">
            <template #icon>
              <div class="step-icon">📝</div>
            </template>
          </el-step>
          <el-step title="审核确认" description="客服核实问题并确认方案">
            <template #icon>
              <div class="step-icon">✅</div>
            </template>
          </el-step>
          <el-step title="寄回维修" description="按指引寄回商品">
            <template #icon>
              <div class="step-icon">📦</div>
            </template>
          </el-step>
          <el-step title="修好返回" description="维修完成寄回给您">
            <template #icon>
              <div class="step-icon">🎉</div>
            </template>
          </el-step>
        </el-steps>
      </section>

      <!-- Repair Scenarios -->
      <section class="section animate-fade-up delay-2">
        <h2 class="section-title">
          <el-icon><Sort /></el-icon>
          售后处理方案
        </h2>
        <p class="section-subtitle">根据不同情况，提供最合适的解决方案</p>

        <el-table :data="repairScenarios" stripe class="repair-table" :header-cell-style="{ background: 'var(--paw-coral-50)', color: 'var(--paw-ink)' }">
          <el-table-column prop="problem" label="问题描述" width="160" />
          <el-table-column prop="example" label="常见案例" />
          <el-table-column prop="solution" label="解决方案" width="120" />
          <el-table-column prop="timeLimit" label="处理时限" width="120" />
          <el-table-column prop="cost" label="费用承担" width="120" />
        </el-table>
      </section>

      <!-- Warranty Periods -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><Calendar /></el-icon>
          商品质保期
        </h2>

        <div class="warranty-list">
          <div class="warranty-item" v-for="(item, index) in warrantyPeriods" :key="index">
            <div class="warranty-left">
              <span class="warranty-icon">{{ item.icon }}</span>
              <div>
                <h4>{{ item.category }}</h4>
                <p>{{ item.examples }}</p>
              </div>
            </div>
            <div class="warranty-right">
              <div class="warranty-badge" :style="{ background: item.color }">
                {{ item.period }}
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Important Notes -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><Warning /></el-icon>
          注意事项
        </h2>

        <div class="notes-grid">
          <div class="note-card" v-for="(note, index) in importantNotes" :key="index">
            <div class="note-icon">{{ note.icon }}</div>
            <h4>{{ note.title }}</h4>
            <p>{{ note.content }}</p>
          </div>
        </div>
      </section>

      <!-- Non-Warranty -->
      <section class="section animate-fade-up delay-3">
        <h2 class="section-title">
          <el-icon><CircleClose /></el-icon>
          不在保修范围内
        </h2>

        <div class="non-warranty-box">
          <div class="nw-item" v-for="(item, index) in nonWarrantyItems" :key="index">
            <el-icon class="nw-icon"><Close /></el-icon>
            <span>{{ item }}</span>
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

const guarantees = [
  {
    icon: '✅',
    title: '正品保证',
    desc: '所有商品均为品牌授权正品，支持官方验证。如发现假冒伪劣商品，承诺假一赔三。',
    period: '永久有效'
  },
  {
    icon: '🔧',
    title: '质量保修',
    desc: '非人为因素导致的质量问题，提供免费维修或更换服务。',
    period: '7天-1年'
  },
  {
    icon: '🔄',
    title: '无忧退换',
    desc: '质量问题商品15天内免费退换，运费由我们承担。',
    period: '签收后15天'
  },
  {
    icon: '💬',
    title: '专属客服',
    desc: '售后问题一对一跟进，直到问题完全解决为止。',
    period: '工作日全天'
  }
]

const repairScenarios = [
  { problem: '功能故障', example: '宠物自动喂食器无法正常出粮、饮水机不出水', solution: '免费维修/换新', timeLimit: '7个工作日', cost: '商家承担' },
  { problem: '外观缺陷', example: '宠物窝缝线开裂、拉链损坏、掉色严重', solution: '免费更换', timeLimit: '5个工作日', cost: '商家承担' },
  { problem: '尺寸不符', example: '收到的宠物衣服尺寸与描述不符', solution: '免费换货', timeLimit: '3个工作日', cost: '商家承担' },
  { problem: '配件缺失', example: '猫爬架缺少螺丝、玩具缺少配件', solution: '补发配件', timeLimit: '3个工作日', cost: '商家承担' },
  { problem: '人为损坏', example: '宠物咬坏电线、人为摔碎陶瓷食器', solution: '付费维修', timeLimit: '10个工作日', cost: '买家承担' },
  { problem: '过保维修', example: '超过质保期出现的故障', solution: '付费维修', timeLimit: '10个工作日', cost: '买家承担' }
]

const warrantyPeriods = [
  {
    icon: '🤖',
    category: '智能设备',
    examples: '自动喂食器、智能饮水机、宠物摄像头、自动猫砂盆',
    period: '1年质保',
    color: 'var(--paw-coral)'
  },
  {
    icon: '🏠',
    category: '宠物家具',
    examples: '猫爬架、宠物窝、狗屋、航空箱',
    period: '6个月质保',
    color: 'var(--paw-amber)'
  },
  {
    icon: '👗',
    category: '宠物服饰',
    examples: '宠物衣服、鞋子、雨衣、项圈牵引绳',
    period: '3个月质保',
    color: 'var(--paw-sage)'
  },
  {
    icon: '🍽️',
    category: '宠物食器',
    examples: '不锈钢食盆、陶瓷水碗、慢食碗、自动饮水机',
    period: '6个月质保',
    color: 'var(--paw-sky)'
  },
  {
    icon: '🎾',
    category: '宠物玩具',
    examples: '逗猫棒、磨牙玩具、互动玩具、飞盘',
    period: '1个月质保',
    color: 'var(--paw-rose)'
  },
  {
    icon: '✂️',
    category: '美容工具',
    examples: '宠物推剪器、指甲剪、梳子、吹水机',
    period: '6个月质保',
    color: 'var(--paw-ink-3)'
  }
]

const importantNotes = [
  {
    icon: '📸',
    title: '拍照留证',
    content: '发现质量问题后，请第一时间拍摄清晰的照片或视频，包括问题部位、商品整体和包装信息。这些证据将帮助加快审核速度。'
  },
  {
    icon: '📦',
    title: '妥善包装',
    content: '寄回维修时请使用原包装或同等防护包装，确保商品在运输途中不会造成二次损坏。如因包装不当导致损坏，维修费用可能需要买家承担。'
  },
  {
    icon: '📋',
    title: '保留凭证',
    content: '请保留好购买凭证（订单截图、发票等）和物流单号，以便在售后过程中提供必要的信息。'
  },
  {
    icon: '⏰',
    title: '及时申请',
    content: '发现质量问题后请尽快提交售后申请，不要等到超过质保期。越早申请，处理速度越快。'
  }
]

const nonWarrantyItems = [
  '因使用不当或人为因素造成的损坏（如宠物啃咬、摔落等）',
  '未经授权的拆卸、改装或维修导致的故障',
  '超过质保期后的质量问题',
  '因不可抗力（如火灾、水灾等）造成的损坏',
  '正常使用产生的磨损和老化',
  '商品外观的轻微色差（非质量问题）',
  '消耗品（尿垫、猫砂、食品等）的自然消耗'
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

/* ── Guarantee Grid ── */
.guarantee-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.guarantee-card {
  text-align: center;
  padding: 32px 24px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  transition: all var(--paw-normal);
}

.guarantee-card:hover {
  box-shadow: var(--paw-shadow-md);
  transform: translateY(-4px);
}

.guarantee-icon {
  font-size: 40px;
  margin-bottom: 16px;
}

.guarantee-card h4 {
  font-size: 17px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 8px;
}

.guarantee-card p {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.6;
  margin-bottom: 16px;
}

.guarantee-period {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  font-size: 13px;
  color: var(--paw-coral);
  font-weight: 600;
}

/* ── Steps ── */
.repair-steps {
  max-width: 900px;
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

/* ── Repair Table ── */
.repair-table {
  border-radius: var(--paw-radius-md);
  overflow: hidden;
}

/* ── Warranty List ── */
.warranty-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.warranty-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  transition: all var(--paw-normal);
}

.warranty-item:hover {
  box-shadow: var(--paw-shadow-sm);
}

.warranty-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.warranty-icon {
  font-size: 28px;
}

.warranty-left h4 {
  font-size: 15px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 4px;
}

.warranty-left p {
  font-size: 12px;
  color: var(--paw-ink-3);
}

.warranty-badge {
  padding: 6px 16px;
  color: #fff;
  border-radius: var(--paw-radius-full);
  font-size: 13px;
  font-weight: 700;
  white-space: nowrap;
}

/* ── Notes Grid ── */
.notes-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.note-card {
  padding: 24px 20px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  box-shadow: var(--paw-shadow-xs);
  transition: all var(--paw-normal);
}

.note-card:hover {
  box-shadow: var(--paw-shadow-sm);
  transform: translateY(-2px);
}

.note-icon {
  font-size: 32px;
  margin-bottom: 12px;
}

.note-card h4 {
  font-size: 15px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 8px;
}

.note-card p {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.6;
}

/* ── Non-Warranty ── */
.non-warranty-box {
  padding: 24px;
  background: var(--paw-coral-50);
  border-radius: var(--paw-radius-md);
  border: 1px solid var(--paw-coral-100);
}

.nw-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: var(--paw-ink-2);
  padding: 8px 0;
  border-bottom: 1px solid var(--paw-coral-100);
}

.nw-item:last-child {
  border-bottom: none;
}

.nw-icon {
  color: var(--paw-coral);
  flex-shrink: 0;
}

/* ── Back Button ── */
.back-section {
  text-align: center;
  padding: 32px 0 64px;
}

@media (max-width: 1024px) {
  .guarantee-grid,
  .notes-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .guarantee-grid,
  .notes-grid {
    grid-template-columns: 1fr;
  }
  .hero-title {
    font-size: 28px;
  }
}
</style>
