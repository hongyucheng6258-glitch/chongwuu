<template>
  <div class="layout">
    <header class="header" :class="{ scrolled: isScrolled }">
      <div class="header-inner">
        <div class="logo" @click="router.push('/')">
          <div class="logo-badge">
            <svg viewBox="0 0 32 32" class="logo-svg">
              <ellipse cx="16" cy="20" rx="7" ry="8" fill="currentColor"/>
              <ellipse cx="6" cy="11" rx="3.5" ry="4.5" fill="currentColor"/>
              <ellipse cx="26" cy="11" rx="3.5" ry="4.5" fill="currentColor"/>
              <ellipse cx="11" cy="5" rx="3" ry="3.8" fill="currentColor"/>
              <ellipse cx="21" cy="5" rx="3" ry="3.8" fill="currentColor"/>
            </svg>
          </div>
          <span class="logo-text">初晴萌宠空间</span>
          <span class="logo-sub">让每一只萌宠都幸福</span>
        </div>

        <div class="search-box">
          <el-input
            v-model="keyword"
            placeholder="搜索宠物用品..."
            size="large"
            @keyup.enter="search"
          >
            <template #append>
              <el-button :icon="Search" @click="search" />
            </template>
          </el-input>
        </div>

        <div class="nav-actions">
          <div class="badge-wrapper">
            <button class="nav-btn" @click="goCart">
              <svg viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="9" cy="21" r="1"/>
                <circle cx="20" cy="21" r="1"/>
                <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
              </svg>
              <span class="btn-label">购物车</span>
            </button>
            <span v-if="cartCount > 0" class="badge-dot">{{ cartCount > 99 ? '99+' : cartCount }}</span>
          </div>

          <button class="nav-btn" @click="router.push('/stores')">
            <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
              <circle cx="12" cy="10" r="3"/>
            </svg>
            <span class="btn-label">附近商店</span>
          </button>

          <button class="nav-btn" @click="router.push('/videos')">
            <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polygon points="5 3 19 12 5 21 5 3"/>
            </svg>
            <span class="btn-label">视频</span>
          </button>

          <button class="nav-btn" @click="router.push('/ai-chat')">
            <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M12 2a10 10 0 1 0 10 10H12V2z"/>
              <path d="M12 2a10 10 0 0 1 10 10H12V2z"/>
              <path d="M6 12h.01"/>
              <path d="M10 12h.01"/>
              <path d="M14 12h.01"/>
            </svg>
            <span class="btn-label">AI助手</span>
          </button>

          <template v-if="userStore.isLogin">
            <div class="badge-wrapper">
              <button class="nav-btn" @click="router.push('/notifications')">
                <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/>
                  <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
                </svg>
                <span class="btn-label">消息</span>
              </button>
              <span v-if="unreadCount > 0" class="badge-dot">{{ unreadCount > 99 ? '99+' : unreadCount }}</span>
            </div>
            <el-dropdown trigger="click">
              <div class="user-chip">
                <div class="avatar">{{ userStore.username?.charAt(0).toUpperCase() }}</div>
                <span class="user-name">{{ userStore.username }}</span>
                <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                  <polyline points="6 9 12 15 18 9"/>
                </svg>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="router.push('/profile')">
                    <el-icon><User /></el-icon> 个人中心
                  </el-dropdown-item>
                  <el-dropdown-item @click="router.push('/orders')">
                    <el-icon><List /></el-icon> 我的订单
                  </el-dropdown-item>
                  <el-dropdown-item @click="router.push('/addresses')">
                    <el-icon><Location /></el-icon> 收货地址
                  </el-dropdown-item>
                  <el-dropdown-item v-if="userStore.isAdmin" @click="router.push('/admin')" divided>
                    <el-icon><Setting /></el-icon> 管理后台
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="logout">
                    <el-icon><SwitchButton /></el-icon> 退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <button class="login-btn" @click="router.push('/login')">
              登录 / 注册
            </button>
          </template>
        </div>
      </div>
    </header>

    <main class="main">
      <router-view />
    </main>

    <footer class="footer">
      <div class="footer-wave">
        <svg viewBox="0 0 1440 80" preserveAspectRatio="none">
          <path d="M0,40 C240,80 480,0 720,32 C960,64 1200,16 1440,40 L1440,80 L0,80 Z" fill="var(--paw-ink)"/>
        </svg>
      </div>
      <div class="footer-inner paw-pattern">
        <div class="footer-cols">
          <div class="footer-brand">
            <div class="logo-badge small">
              <svg viewBox="0 0 32 32" class="logo-svg">
                <ellipse cx="16" cy="20" rx="7" ry="8" fill="currentColor"/>
                <ellipse cx="6" cy="11" rx="3.5" ry="4.5" fill="currentColor"/>
                <ellipse cx="26" cy="11" rx="3.5" ry="4.5" fill="currentColor"/>
                <ellipse cx="11" cy="5" rx="3" ry="3.8" fill="currentColor"/>
                <ellipse cx="21" cy="5" rx="3" ry="3.8" fill="currentColor"/>
              </svg>
            </div>
            <div>
              <div class="footer-name">初晴萌宠空间</div>
              <div class="footer-tag">初晴萌宠空间 · 让每一只萌宠都幸福</div>
            </div>
          </div>
          <div class="footer-links">
            <div class="footer-col">
              <h4>购物指南</h4>
              <router-link to="/help/shopping-process">购物流程</router-link>
              <router-link to="/help/membership">会员介绍</router-link>
              <router-link to="/help/faq">常见问题</router-link>
            </div>
            <div class="footer-col">
              <h4>配送方式</h4>
              <router-link to="/help/self-pickup">上门自提</router-link>
              <router-link to="/help/express-delivery">极速配送</router-link>
              <router-link to="/help/shipping-fees">配送费用</router-link>
            </div>
            <div class="footer-col">
              <h4>售后服务</h4>
              <router-link to="/help/return-policy">退换货政策</router-link>
              <router-link to="/help/refund-guide">退款说明</router-link>
              <router-link to="/help/repair-return">返修退换</router-link>
            </div>
          </div>
        </div>
        <div class="footer-bottom">
          <span>© 2026 初晴萌宠空间 · 宠物用品商城</span>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, List, Setting, SwitchButton, User, Location } from '@element-plus/icons-vue'
import { useUserStore } from '../store/user'
import { cartApi, notificationApi } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const keyword = ref('')
const cartCount = ref(0)
const unreadCount = ref(0)
const isScrolled = ref(false)

const search = () => {
  if (keyword.value.trim()) {
    router.push({ path: '/', query: { keyword: keyword.value.trim() } })
  }
}

const goCart = () => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  router.push('/cart')
}

const logout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/')
}

const loadCartCount = async () => {
  if (userStore.isLogin) {
    try {
      const data = await cartApi.list()
      cartCount.value = data.length
    } catch (e) {
      // 忽略
    }
  }
}

const loadUnreadCount = async () => {
  if (userStore.isLogin) {
    try {
      const data = await notificationApi.unreadCount()
      unreadCount.value = data.count || 0
    } catch (e) {
      // 忽略
    }
  }
}

const handleScroll = () => {
  isScrolled.value = window.scrollY > 10
}

onMounted(() => {
  loadCartCount()
  loadUnreadCount()
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

router.afterEach(() => {
  loadCartCount()
  loadUnreadCount()
})
</script>

<style scoped>
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: var(--paw-bg);
}

/* ── Header ── */
.header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 251, 245, 0.72);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-bottom: 1px solid rgba(236, 229, 219, 0.5);
  transition: all var(--paw-normal);
}

.header.scrolled {
  background: rgba(255, 251, 245, 0.92);
  box-shadow: var(--paw-shadow-sm);
}

.header-inner {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 24px;
  height: 68px;
  display: flex;
  align-items: center;
  gap: 24px;
}

/* ── Logo ── */
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  white-space: nowrap;
  transition: transform var(--paw-fast);
}

.logo:hover {
  transform: scale(1.02);
}

.logo-badge {
  width: 38px;
  height: 38px;
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-amber) 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  box-shadow: var(--paw-shadow-coral);
}

.logo-badge.small {
  width: 32px;
  height: 32px;
  border-radius: 10px;
}

.logo-svg {
  width: 22px;
  height: 22px;
}

.logo-text {
  font-size: 20px;
  font-weight: 800;
  color: var(--paw-ink);
  letter-spacing: -0.5px;
}

.logo-sub {
  font-size: 11px;
  color: var(--paw-ink-3);
  font-weight: 500;
  margin-top: 2px;
}

/* ── Search ── */
.search-box {
  flex: 1;
  max-width: 480px;
}

/* ── Nav Actions ── */
.nav-actions {
  display: flex;
  align-items: center;
  gap: 14px;
  white-space: nowrap;
}

.nav-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  background: var(--paw-coral-50);
  border: none;
  color: var(--paw-coral);
  padding: 8px 16px;
  border-radius: var(--paw-radius-full);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--paw-fast);
}

.nav-btn:hover {
  background: var(--paw-coral);
  color: #fff;
  transform: translateY(-1px);
}

.btn-label {
  margin-left: 2px;
}

/* ── User Chip ── */
.user-chip {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 5px 14px 5px 5px;
  border-radius: var(--paw-radius-full);
  background: var(--paw-card);
  border: 1px solid var(--paw-border);
  transition: all var(--paw-fast);
  outline: none;
}

.user-chip:hover {
  border-color: var(--paw-coral);
  box-shadow: var(--paw-shadow-sm);
}

.avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--paw-coral), var(--paw-amber));
  color: #fff;
  font-size: 14px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--paw-ink);
}

/* ── Login Button ── */
.login-btn {
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-coral-light) 100%);
  color: #fff;
  border: none;
  padding: 9px 20px;
  border-radius: var(--paw-radius-full);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: var(--paw-shadow-coral);
  transition: all var(--paw-fast);
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 28px rgba(255, 107, 74, 0.35);
}

/* ── Main ── */
.main {
  flex: 1;
}

/* ── Footer ── */
.footer {
  position: relative;
  margin-top: 64px;
}

.footer-wave {
  line-height: 0;
  margin-bottom: -1px;
}

.footer-wave svg {
  width: 100%;
  height: 60px;
  display: block;
}

.footer-inner {
  background: var(--paw-ink);
  padding: 48px 24px 24px;
}

.footer-cols {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  gap: 48px;
  flex-wrap: wrap;
}

.footer-brand {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.footer-name {
  font-size: 20px;
  font-weight: 800;
  color: #fff;
  margin-bottom: 4px;
}

.footer-tag {
  font-size: 13px;
  color: var(--paw-ink-4);
}

.footer-links {
  display: flex;
  gap: 64px;
  flex-wrap: wrap;
}

.footer-col {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.footer-col h4 {
  color: #fff;
  font-size: 14px;
  font-weight: 700;
  margin-bottom: 4px;
}

.footer-col span,
.footer-col a {
  color: var(--paw-ink-4);
  font-size: 13px;
  cursor: pointer;
  transition: color var(--paw-fast);
  text-decoration: none;
}

.footer-col span:hover,
.footer-col a:hover {
  color: var(--paw-coral);
}

.footer-bottom {
  max-width: 1200px;
  margin: 32px auto 0;
  padding-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  text-align: center;
  color: var(--paw-ink-4);
  font-size: 13px;
}

/* ── Badge wrapper ── */
.badge-wrapper {
  position: relative;
  display: inline-flex;
}

.badge-dot {
  position: absolute;
  top: -6px;
  right: -6px;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  background: var(--paw-coral);
  color: #fff;
  font-size: 11px;
  font-weight: 700;
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid var(--paw-cream);
  line-height: 1;
}

@media (max-width: 768px) {
  .btn-label {
    display: none;
  }
  .logo-sub {
    display: none;
  }
  .footer-links {
    gap: 32px;
  }
}
</style>
