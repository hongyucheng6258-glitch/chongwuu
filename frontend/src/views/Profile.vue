<template>
  <div class="profile-page">
    <div class="page-card">
      <h2>个人中心</h2>
      <el-card class="info-card">
        <div class="profile-header">
          <el-avatar :size="80" class="user-avatar">{{ userStore.nickname?.charAt(0)?.toUpperCase() || 'U' }}</el-avatar>
          <div class="profile-name">
            <h3>{{ userStore.nickname || userStore.username }}</h3>
            <el-tag :type="levelTagType" size="small">{{ userStore.memberLevelName }}</el-tag>
          </div>
        </div>
      </el-card>

      <el-card class="info-card member-card">
        <template #header><span>会员中心</span></template>
        <div class="member-info">
          <div class="member-level-row">
            <div class="member-icon">{{ memberIcon }}</div>
            <div class="member-detail">
              <div class="member-title">{{ userStore.memberLevelName }}</div>
              <div class="member-desc">{{ memberDesc }}</div>
            </div>
          </div>
          <div class="member-perks">
            <div class="perk-title">会员权益</div>
            <div class="perk-list">
              <div class="perk-item" v-for="perk in memberPerks" :key="perk.label">
                <span class="perk-icon">{{ perk.icon }}</span>
                <span class="perk-label">{{ perk.label }}</span>
                <span class="perk-value">{{ perk.value }}</span>
              </div>
            </div>
          </div>
          <div class="member-levels">
            <div class="perk-title">等级一览</div>
            <div class="level-list">
              <div v-for="lv in allLevels" :key="lv.level" class="level-item" :class="{ active: lv.level === userStore.memberLevel }">
                <div class="level-badge" :style="{ background: lv.color }">{{ lv.icon }}</div>
                <div class="level-info">
                  <span class="level-name">{{ lv.name }}</span>
                  <span class="level-discount">{{ lv.discount }}</span>
                </div>
                <el-tag v-if="lv.level === userStore.memberLevel" type="success" size="small">当前</el-tag>
              </div>
            </div>
          </div>
        </div>
      </el-card>

      <el-card class="info-card">
        <template #header><span>基本信息</span></template>
        <el-form :model="form" label-width="80px" v-loading="loading">
          <el-form-item label="用户名">
            <el-input :model-value="userStore.username" disabled />
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.nickname" placeholder="请输入昵称" maxlength="20" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveProfile" :loading="saving">保存修改</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-card class="info-card">
        <template #header><span>快捷入口</span></template>
        <div class="quick-links">
          <el-button @click="$router.push('/addresses')">
            <el-icon><Location /></el-icon> 收货地址管理
          </el-button>
          <el-button @click="$router.push('/orders')">
            <el-icon><List /></el-icon> 我的订单
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '../store/user'
import { userApi } from '../api'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const loading = ref(false)
const saving = ref(false)
const form = ref({ nickname: '', email: '', phone: '' })

const levelTagType = computed(() => {
  const map = { 0: 'info', 1: '', 2: 'warning', 3: 'danger' }
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
    const data = await userApi.profile()
    form.value.nickname = data.nickname || ''
    form.value.email = data.email || ''
    form.value.phone = data.phone || ''
    // 刷新store中的用户信息（会员等级等可能已在后台更新）
    userStore.updateUser(data)
  } catch (e) {}
  loading.value = false
})

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
.profile-page { padding: 24px; max-width: 700px; margin: 0 auto; }
.page-card h2 { font-size: 22px; margin-bottom: 20px; }
.info-card { margin-bottom: 20px; }
.profile-header { display: flex; align-items: center; gap: 20px; }
.profile-name h3 { margin: 0 0 8px 0; }
.user-avatar { background: var(--paw-coral); font-size: 32px; }
.quick-links { display: flex; gap: 12px; flex-wrap: wrap; }

.member-card { border: 1px solid var(--paw-coral-light); }
.member-level-row { display: flex; align-items: center; gap: 16px; margin-bottom: 20px; }
.member-icon { font-size: 48px; }
.member-title { font-size: 20px; font-weight: 700; color: var(--paw-ink); }
.member-desc { font-size: 13px; color: var(--paw-ink-3); margin-top: 4px; }

.perk-title { font-size: 15px; font-weight: 600; color: var(--paw-ink); margin: 16px 0 10px; }
.perk-list { display: flex; flex-direction: column; gap: 8px; }
.perk-item { display: flex; align-items: center; gap: 10px; padding: 8px 12px; background: var(--paw-surface); border-radius: 8px; }
.perk-icon { font-size: 18px; }
.perk-label { font-size: 13px; font-weight: 600; color: var(--paw-ink); flex: 1; }
.perk-value { font-size: 12px; color: var(--paw-ink-3); }

.level-list { display: flex; flex-direction: column; gap: 8px; }
.level-item { display: flex; align-items: center; gap: 12px; padding: 10px 12px; border-radius: 8px; border: 1px solid var(--paw-border-light); transition: all 0.2s; }
.level-item.active { border-color: var(--paw-coral); background: var(--paw-coral-50); }
.level-badge { width: 36px; height: 36px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 18px; color: #fff; }
.level-info { flex: 1; display: flex; flex-direction: column; }
.level-name { font-size: 14px; font-weight: 600; color: var(--paw-ink); }
.level-discount { font-size: 12px; color: var(--paw-ink-3); }
</style>
