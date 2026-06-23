<template>
  <div class="admin-layout">
    <aside class="sidebar">
      <div class="sidebar-logo" @click="$router.push('/')">
        <div class="logo-badge">
          <svg viewBox="0 0 32 32" class="logo-svg">
            <ellipse cx="16" cy="20" rx="7" ry="8" fill="currentColor"/>
            <ellipse cx="6" cy="11" rx="3.5" ry="4.5" fill="currentColor"/>
            <ellipse cx="26" cy="11" rx="3.5" ry="4.5" fill="currentColor"/>
            <ellipse cx="11" cy="5" rx="3" ry="3.8" fill="currentColor"/>
            <ellipse cx="21" cy="5" rx="3" ry="3.8" fill="currentColor"/>
          </svg>
        </div>
        <div class="logo-text-group">
          <span class="logo-text">初晴萌宠空间</span>
          <span class="logo-sub">管理后台</span>
        </div>
      </div>

      <el-menu :default-active="activeMenu" router class="sidebar-menu">
        <el-menu-item index="/admin">
          <el-icon><DataAnalysis /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/admin/products">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/categories">
          <el-icon><Menu /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/orders">
          <el-icon><List /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/stores">
          <el-icon><Shop /></el-icon>
          <span>商店管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/videos">
          <el-icon><VideoCamera /></el-icon>
          <span>视频管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <span>会员管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/reviews">
          <el-icon><Star /></el-icon>
          <span>评价管理</span>
        </el-menu-item>
      </el-menu>

      <div class="sidebar-footer">
        <button class="back-btn" @click="$router.push('/')">
          <el-icon><Back /></el-icon>
          <span>返回前台</span>
        </button>
      </div>
    </aside>

    <div class="main-area">
      <header class="admin-header">
        <div class="header-left">
          <span class="header-title">{{ pageTitle }}</span>
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
              <el-dropdown-item @click="$router.push('/orders')">前台订单</el-dropdown-item>
              <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </header>
      <div class="admin-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { DataAnalysis, Goods, Menu, List, Back, Shop, VideoCamera, User, Star } from '@element-plus/icons-vue'
import { useUserStore } from '../../store/user'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta.title || '管理后台')

const logout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: 100vh;
  background: var(--paw-bg);
}

/* ── Sidebar ── */
.sidebar {
  width: 240px;
  background: var(--paw-ink);
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.sidebar-logo {
  height: 68px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 20px;
  cursor: pointer;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
  transition: background var(--paw-fast);
}

.sidebar-logo:hover {
  background: rgba(255, 255, 255, 0.03);
}

.logo-badge {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-amber) 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  box-shadow: 0 4px 12px rgba(255, 107, 74, 0.3);
  flex-shrink: 0;
}

.logo-svg {
  width: 22px;
  height: 22px;
}

.logo-text-group {
  display: flex;
  flex-direction: column;
}

.logo-text {
  font-size: 16px;
  font-weight: 800;
  color: #fff;
  letter-spacing: -0.3px;
}

.logo-sub {
  font-size: 11px;
  color: var(--paw-ink-4);
  font-weight: 500;
}

.sidebar-menu {
  flex: 1;
  border-right: none;
  background: transparent;
  padding: 12px;
}

.sidebar-menu :deep(.el-menu-item) {
  color: var(--paw-ink-4);
  border-radius: var(--paw-radius-md);
  margin-bottom: 4px;
  height: 44px;
  line-height: 44px;
  font-weight: 500;
  transition: all var(--paw-fast);
}

.sidebar-menu :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.05);
  color: #fff;
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-coral-light) 100%);
  color: #fff;
  box-shadow: var(--paw-shadow-coral);
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
}

.back-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px;
  border-radius: var(--paw-radius-md);
  border: 1px solid rgba(255, 255, 255, 0.1);
  background: transparent;
  color: var(--paw-ink-4);
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--paw-fast);
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.05);
  color: #fff;
  border-color: rgba(255, 255, 255, 0.15);
}

/* ── Main Area ── */
.main-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.admin-header {
  height: 68px;
  background: var(--paw-card);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  border-bottom: 1px solid var(--paw-border-light);
  box-shadow: var(--paw-shadow-xs);
}

.header-title {
  font-size: 20px;
  font-weight: 800;
  color: var(--paw-ink);
}

.user-chip {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 5px 14px 5px 5px;
  border-radius: var(--paw-radius-full);
  background: var(--paw-surface);
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

.admin-content {
  flex: 1;
  overflow-y: auto;
  padding: 28px;
  background: var(--paw-bg);
}
</style>
