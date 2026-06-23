<template>
  <div class="profile-page">
    <!-- 🎨 装饰背景元素 -->
    <div class="bg-decoration">
      <div class="blob blob-1 animate-blob"></div>
      <div class="blob blob-2 animate-blob" style="animation-delay: 2s;"></div>
    </div>

    <div class="profile-container">
      <!-- ✨ 个人资料英雄区 -->
      <div class="hero-card animate-fade-up">
        <div class="hero-bg"></div>
        <div class="hero-content">
          <div class="avatar-section">
            <div class="avatar-wrapper">
              <el-avatar :size="88" class="user-avatar">{{ userStore.nickname?.charAt(0)?.toUpperCase() || 'U' }}</el-avatar>
              <div class="avatar-ring" :style="{ borderColor: levelColor }"></div>
            </div>
          </div>
          <div class="user-info-section">
            <h2 class="user-name">{{ userStore.nickname || userStore.username }}</h2>
            <el-tag :type="levelTagType" size="small" class="member-tag" effect="dark">
              <span class="tag-dot" :style="{ background: levelColor }"></span>
              {{ userStore.memberLevelName }}
            </el-tag>
          </div>
          <div class="stats-row">
            <div class="stat-item">
              <span class="stat-value">{{ orderStats.total }}</span>
              <span class="stat-label">全部订单</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <span class="stat-value">{{ orderStats.pending }}</span>
              <span class="stat-label">待发货</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <span class="stat-value">{{ orderStats.shipped }}</span>
              <span class="stat-label">待收货</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <span class="stat-value">{{ orderStats.done }}</span>
              <span class="stat-label">已完成</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 📦 订单快捷看板 -->
      <div class="section-card animate-fade-up delay-1">
        <div class="section-header">
          <span class="section-icon">📋</span>
          <h3>我的订单</h3>
          <el-button link type="primary" class="section-more" @click="$router.push('/orders')">查看全部 ›</el-button>
        </div>
        <div class="order-board">
          <div class="order-item" @click="$router.push('/orders')">
            <div class="order-icon-box" style="background: var(--paw-coral-50)">
              <span class="order-icon">💰</span>
            </div>
            <span class="order-label">待付款</span>
            <el-badge v-if="orderStats.pendingPay > 0" :value="orderStats.pendingPay" class="order-badge" />
          </div>
          <div class="order-item" @click="$router.push('/orders')">
            <div class="order-icon-box" style="background: var(--paw-amber-50)">
              <span class="order-icon">📦</span>
            </div>
            <span class="order-label">待发货</span>
            <el-badge v-if="orderStats.pending > 0" :value="orderStats.pending" class="order-badge" />
          </div>
          <div class="order-item" @click="$router.push('/orders')">
            <div class="order-icon-box" style="background: var(--paw-sky-50)">
              <span class="order-icon">🚚</span>
            </div>
            <span class="order-label">待收货</span>
            <el-badge v-if="orderStats.shipped > 0" :value="orderStats.shipped" class="order-badge" />
          </div>
          <div class="order-item" @click="$router.push('/orders')">
            <div class="order-icon-box" style="background: var(--paw-sage-50)">
              <span class="order-icon">⭐</span>
            </div>
            <span class="order-label">待评价</span>
          </div>
        </div>
      </div>

      <!-- 🏆 会员中心卡片 -->
      <div class="section-card animate-fade-up delay-2">
        <div class="section-header">
          <span class="section-icon">🏆</span>
          <h3>会员中心</h3>
        </div>
        <div class="member-banner" :style="{ background: `linear-gradient(135deg, ${levelColor}22, ${levelColor}11)` }">
          <div class="member-icon-box" :style="{ background: levelColor }">
            <span class="member-badge-icon">{{ memberIcon }}</span>
          </div>
          <div class="member-banner-info">
            <div class="member-banner-title">{{ userStore.memberLevelName }}</div>
            <div class="member-banner-desc">{{ memberDesc }}</div>
          </div>
        </div>

        <!-- 会员权益 -->
        <div class="perks-grid">
          <div class="perk-chip" v-for="perk in memberPerks" :key="perk.label">
            <span class="perk-chip-icon">{{ perk.icon }}</span>
            <div class="perk-chip-info">
              <span class="perk-chip-label">{{ perk.label }}</span>
              <span class="perk-chip-value">{{ perk.value }}</span>
            </div>
          </div>
        </div>

        <!-- 等级一览 -->
        <div class="level-section">
          <div class="level-subtitle">等级一览</div>
          <div class="level-timeline">
            <div
              v-for="(lv, idx) in allLevels"
              :key="lv.level"
              class="level-node"
              :class="{ 'is-active': lv.level === userStore.memberLevel, 'is-past': lv.level < userStore.memberLevel }"
            >
              <div class="level-dot" :style="{ borderColor: lv.color, background: lv.level <= userStore.memberLevel ? lv.color : '#fff' }">
                <span v-if="lv.level < userStore.memberLevel" class="level-check">✓</span>
                <span v-else class="level-node-icon">{{ lv.icon }}</span>
              </div>
              <div class="level-node-info">
                <span class="level-node-name">{{ lv.name }}</span>
                <span class="level-node-discount">{{ lv.discount }}</span>
              </div>
              <div class="level-connector" v-if="idx < allLevels.length - 1" :class="{ 'is-filled': lv.level < userStore.memberLevel }"></div>
            </div>
          </div>
        </div>
      </div>

      <!-- ⚡ 快捷入口 -->
      <div class="section-card animate-fade-up delay-3">
        <div class="section-header">
          <span class="section-icon">⚡</span>
          <h3>快捷入口</h3>
        </div>
        <div class="quick-grid">
          <div class="quick-item" @click="$router.push('/orders')">
            <div class="quick-icon-box" style="background: linear-gradient(135deg, #FF6B4A, #FF8E6E)">
              <el-icon :size="22" color="#fff"><List /></el-icon>
            </div>
            <span class="quick-label">我的订单</span>
          </div>
          <div class="quick-item" @click="$router.push('/addresses')">
            <div class="quick-icon-box" style="background: linear-gradient(135deg, #5CB87A, #7DD49A)">
              <el-icon :size="22" color="#fff"><Location /></el-icon>
            </div>
            <span class="quick-label">地址管理</span>
          </div>
          <div class="quick-item" @click="$router.push('/cart')">
            <div class="quick-icon-box" style="background: linear-gradient(135deg, #FFB830, #FFD166)">
              <el-icon :size="22" color="#fff"><ShoppingCart /></el-icon>
            </div>
            <span class="quick-label">购物车</span>
          </div>
          <div class="quick-item" @click="$router.push('/notifications')">
            <div class="quick-icon-box" style="background: linear-gradient(135deg, #4A9FE8, #7BB8F0)">
              <el-icon :size="22" color="#fff"><Bell /></el-icon>
            </div>
            <span class="quick-label">消息通知</span>
          </div>
        </div>
      </div>

      <!-- 👤 个人信息编辑 -->
      <div class="section-card animate-fade-up delay-4">
        <div class="section-header">
          <span class="section-icon">👤</span>
          <h3>个人信息</h3>
        </div>
        <el-form :model="form" label-width="70px" v-loading="loading" class="profile-form">
          <el-form-item label="用户名">
            <el-input :model-value="userStore.username" disabled>
              <template #prefix><el-icon><User /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.nickname" placeholder="请输入昵称" maxlength="20">
              <template #prefix><el-icon><Edit /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" placeholder="请输入邮箱">
              <template #prefix><el-icon><Message /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11">
              <template #prefix><el-icon><Phone /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveProfile" :loading="saving" round size="large" class="save-btn">
              保存修改
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '../store/user'
import { userApi, orderApi } from '../api'
import { ElMessage } from 'element-plus'
import { User, Edit, Message, Phone, List, Location, ShoppingCart, Bell } from '@element-plus/icons-vue'

const userStore = useUserStore()
const loading = ref(false)
const saving = ref(false)
const form = ref({ nickname: '', email: '', phone: '' })
const orderStats = ref({ total: 0, pending: 0, shipped: 0, done: 0, pendingPay: 0 })

const levelColors = ['#909399', '#409EFF', '#E6A23C', '#F56C6C']
const levelColor = computed(() => levelColors[userStore.memberLevel] || levelColors[0])

const levelTagType = computed(() => {
  const map = { 0: 'info', 1: 'primary', 2: 'warning', 3: 'danger' }
  return map[userStore.memberLevel] || 'info'
})

const memberIcon = computed(() => {
  const icons = ['👤', '🥈', '🥇', '💎']
  return icons[userStore.memberLevel] || '👤'
})

const memberDesc = computed(() => {
  const descs = [
    '注册即可成为普通用户，消费可积累经验升级',
    '银卡会员享受全场95折优惠',
    '金卡会员享受全场88折优惠',
    '钻石会员享受全场80折专属优惠'
  ]
  return descs[userStore.memberLevel] || descs[0]
})

const memberPerks = computed(() => {
  const level = userStore.memberLevel
  const base = [
    { icon: '🛡️', label: '正品保证', value: '全场商品正品保障' },
    { icon: '🚚', label: '配送服务', value: '满99元包邮' },
    { icon: '↩️', label: '退换政策', value: '七天无理由退换' }
  ]
  if (level >= 1) base.push({ icon: '💰', label: '会员折扣', value: '全场商品享折扣' })
  if (level >= 2) base.push({ icon: '🎁', label: '专属礼包', value: '生日月专属优惠券' })
  if (level >= 3) base.push({ icon: '⭐', label: '优先发货', value: '订单优先处理发货' })
  return base
})

const allLevels = [
  { level: 0, name: '普通用户', discount: '无折扣', icon: '👤', color: '#909399' },
  { level: 1, name: '银卡会员', discount: '95折', icon: '🥈', color: '#409EFF' },
  { level: 2, name: '金卡会员', discount: '88折', icon: '🥇', color: '#E6A23C' },
  { level: 3, name: '钻石会员', discount: '80折', icon: '💎', color: '#F56C6C' }
]

onMounted(async () => {
  loading.value = true
  try {
    const [profileData, statsData] = await Promise.all([
      userApi.profile(),
      loadOrderStats()
    ])
    form.value.nickname = profileData.nickname || ''
    form.value.email = profileData.email || ''
    form.value.phone = profileData.phone || ''
    userStore.updateUser(profileData)
  } catch (e) {}
  loading.value = false
})

const loadOrderStats = async () => {
  try {
    const data = await orderApi.adminList()
    if (data && Array.isArray(data)) {
      orderStats.value = {
        total: data.length,
        pendingPay: data.filter(o => o.status === 'PENDING').length,
        pending: data.filter(o => o.status === 'PAID').length,
        shipped: data.filter(o => o.status === 'SHIPPED').length,
        done: data.filter(o => o.status === 'RECEIVED' || o.status === 'COMPLETED').length
      }
    }
  } catch (e) {
    // 可能不是管理员，尝试获取用户自己的订单
    try {
      const data = await orderApi.list()
      if (data && Array.isArray(data)) {
        orderStats.value = {
          total: data.length,
          pendingPay: data.filter(o => o.status === 'PENDING').length,
          pending: data.filter(o => o.status === 'PAID').length,
          shipped: data.filter(o => o.status === 'SHIPPED').length,
          done: data.filter(o => o.status === 'RECEIVED' || o.status === 'COMPLETED').length
        }
      }
    } catch (e2) {}
  }
}

const saveProfile = async () => {
  saving.value = true
  try {
    const data = await userApi.updateProfile(form.value)
    userStore.updateUser(data)
    ElMessage.success('个人信息已更新')
  } catch (e) {}
  saving.value = false
}
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  padding: var(--paw-space-xl);
  position: relative;
  overflow: hidden;
}

/* ── 装饰背景 ── */
.bg-decoration {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 0;
}

.blob {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
  opacity: 0.2;
  pointer-events: none;
}

.blob-1 {
  width: 400px;
  height: 400px;
  background: var(--paw-coral);
  top: -100px;
  right: -100px;
}

.blob-2 {
  width: 300px;
  height: 300px;
  background: var(--paw-amber);
  bottom: -50px;
  left: -80px;
}

.profile-container {
  max-width: 720px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  gap: var(--paw-space-lg);
}

/* ── 英雄区 ── */
.hero-card {
  position: relative;
  border-radius: var(--paw-radius-lg);
  overflow: hidden;
  background: var(--paw-card);
  box-shadow: var(--paw-shadow-md);
}

.hero-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-coral-light) 40%, var(--paw-amber) 100%);
  opacity: 0.08;
}

.hero-content {
  position: relative;
  padding: var(--paw-space-xl) var(--paw-space-lg);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--paw-space-md);
}

.avatar-section {
  position: relative;
}

.avatar-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-avatar {
  background: linear-gradient(135deg, var(--paw-coral), var(--paw-coral-light));
  font-size: 36px;
  font-weight: 700;
  border-radius: 50%;
  box-shadow: 0 8px 24px rgba(255, 107, 74, 0.3);
  transition: transform var(--paw-normal);
}

.user-avatar:hover {
  transform: scale(1.05);
}

.avatar-ring {
  position: absolute;
  inset: -4px;
  border-radius: 50%;
  border: 3px solid;
  opacity: 0.3;
}

.user-info-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--paw-space-xs);
}

.user-name {
  font-size: 24px;
  font-weight: 700;
  color: var(--paw-ink);
  margin: 0;
}

.member-tag {
  padding: 4px 14px;
  border-radius: var(--paw-radius-full);
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 6px;
}

.tag-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  display: inline-block;
}

/* ── 统计行 ── */
.stats-row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--paw-space-lg);
  padding: var(--paw-space-md) var(--paw-space-lg);
  background: var(--paw-surface);
  border-radius: var(--paw-radius-md);
  width: 100%;
  max-width: 480px;
  margin-top: var(--paw-space-sm);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  cursor: pointer;
  transition: transform var(--paw-fast);
  min-width: 60px;
}

.stat-item:hover {
  transform: translateY(-2px);
}

.stat-value {
  font-size: 20px;
  font-weight: 800;
  color: var(--paw-coral);
}

.stat-label {
  font-size: 12px;
  color: var(--paw-ink-3);
  font-weight: 500;
}

.stat-divider {
  width: 1px;
  height: 32px;
  background: var(--paw-border);
}

/* ── 通用卡片 ── */
.section-card {
  background: var(--paw-card);
  border-radius: var(--paw-radius-lg);
  padding: var(--paw-space-lg);
  box-shadow: var(--paw-shadow-sm);
  transition: box-shadow var(--paw-normal), transform var(--paw-normal);
}

.section-card:hover {
  box-shadow: var(--paw-shadow-md);
}

.section-header {
  display: flex;
  align-items: center;
  gap: var(--paw-space-sm);
  margin-bottom: var(--paw-space-md);
  padding-bottom: var(--paw-space-md);
  border-bottom: 2px solid var(--paw-border-light);
}

.section-header h3 {
  font-size: 16px;
  font-weight: 700;
  color: var(--paw-ink);
  margin: 0;
  flex: 1;
}

.section-icon {
  font-size: 20px;
}

.section-more {
  font-size: 13px;
}

/* ── 订单看板 ── */
.order-board {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--paw-space-sm);
}

.order-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--paw-space-sm);
  padding: var(--paw-space-md) var(--paw-space-xs);
  border-radius: var(--paw-radius-md);
  cursor: pointer;
  transition: all var(--paw-normal);
  position: relative;
}

.order-item:hover {
  background: var(--paw-surface);
  transform: translateY(-3px);
}

.order-icon-box {
  width: 48px;
  height: 48px;
  border-radius: var(--paw-radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
}

.order-icon {
  font-size: 22px;
}

.order-label {
  font-size: 13px;
  font-weight: 600;
  color: var(--paw-ink-2);
}

.order-badge {
  position: absolute;
  top: 4px;
  right: 10px;
}

/* ── 会员卡片 ── */
.member-banner {
  display: flex;
  align-items: center;
  gap: var(--paw-space-md);
  padding: var(--paw-space-md);
  border-radius: var(--paw-radius-md);
  margin-bottom: var(--paw-space-md);
}

.member-icon-box {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.member-badge-icon {
  font-size: 28px;
  filter: brightness(1.2);
}

.member-banner-info {
  flex: 1;
}

.member-banner-title {
  font-size: 18px;
  font-weight: 700;
  color: var(--paw-ink);
}

.member-banner-desc {
  font-size: 13px;
  color: var(--paw-ink-3);
  margin-top: 4px;
}

/* ── 权益网格 ── */
.perks-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--paw-space-sm);
  margin-bottom: var(--paw-space-md);
}

.perk-chip {
  display: flex;
  align-items: center;
  gap: var(--paw-space-sm);
  padding: var(--paw-space-sm) var(--paw-space-md);
  background: var(--paw-surface);
  border-radius: var(--paw-radius-sm);
  transition: all var(--paw-fast);
}

.perk-chip:hover {
  background: var(--paw-coral-50);
  transform: translateX(3px);
}

.perk-chip-icon {
  font-size: 20px;
  flex-shrink: 0;
}

.perk-chip-info {
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.perk-chip-label {
  font-size: 13px;
  font-weight: 600;
  color: var(--paw-ink);
}

.perk-chip-value {
  font-size: 11px;
  color: var(--paw-ink-3);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* ── 等级时间线 ── */
.level-section {
  padding-top: var(--paw-space-sm);
}

.level-subtitle {
  font-size: 14px;
  font-weight: 600;
  color: var(--paw-ink);
  margin-bottom: var(--paw-space-md);
}

.level-timeline {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.level-node {
  display: flex;
  align-items: center;
  gap: var(--paw-space-md);
  padding: var(--paw-space-sm) var(--paw-space-md);
  border-radius: var(--paw-radius-sm);
  position: relative;
  transition: all var(--paw-normal);
}

.level-node.is-active {
  background: var(--paw-coral-50);
}

.level-node:not(.is-active):hover {
  background: var(--paw-surface);
}

.level-dot {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 2px solid;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all var(--paw-normal);
  font-size: 14px;
}

.level-node.is-active .level-dot {
  box-shadow: 0 0 0 4px var(--paw-coral-50);
  transform: scale(1.1);
}

.level-check {
  color: #fff;
  font-size: 16px;
  font-weight: 700;
}

.level-node-icon {
  font-size: 14px;
}

.level-node-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.level-node-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--paw-ink);
}

.level-node-discount {
  font-size: 12px;
  color: var(--paw-ink-3);
}

.level-node.is-active .level-node-name {
  color: var(--paw-coral);
}

.level-connector {
  position: absolute;
  left: calc(18px + var(--paw-space-md) - 1px);
  bottom: -14px;
  width: 2px;
  height: 16px;
  background: var(--paw-border);
  z-index: 0;
}

.level-connector.is-filled {
  background: var(--paw-coral-light);
}

/* ── 快捷入口 ── */
.quick-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--paw-space-sm);
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--paw-space-sm);
  padding: var(--paw-space-md);
  border-radius: var(--paw-radius-md);
  cursor: pointer;
  transition: all var(--paw-normal);
}

.quick-item:hover {
  background: var(--paw-surface);
  transform: translateY(-3px);
}

.quick-icon-box {
  width: 48px;
  height: 48px;
  border-radius: var(--paw-radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.quick-label {
  font-size: 13px;
  font-weight: 600;
  color: var(--paw-ink-2);
}

/* ── 个人信息表单 ── */
.profile-form {
  max-width: 500px;
}

.profile-form .el-form-item {
  margin-bottom: 18px;
}

.save-btn {
  width: 100%;
  font-size: 15px;
  padding: 12px 0;
  margin-top: var(--paw-space-sm);
}

/* ── 响应式 ── */
@media (max-width: 640px) {
  .profile-page {
    padding: var(--paw-space-md);
  }

  .stats-row {
    gap: var(--paw-space-sm);
    padding: var(--paw-space-sm);
    max-width: 100%;
  }

  .stat-value {
    font-size: 17px;
  }

  .order-board {
    grid-template-columns: repeat(4, 1fr);
  }

  .order-item {
    padding: var(--paw-space-sm) 4px;
  }

  .order-icon-box {
    width: 40px;
    height: 40px;
  }

  .order-icon {
    font-size: 18px;
  }

  .perks-grid {
    grid-template-columns: 1fr;
  }

  .quick-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .hero-content {
    padding: var(--paw-space-lg) var(--paw-space-md);
  }
}
</style>