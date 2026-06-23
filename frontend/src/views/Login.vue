<template>
  <div class="auth-page">
    <!-- 左侧品牌面板 -->
    <div class="brand-panel">
      <div class="brand-bg">
        <div class="brand-blob brand-blob-1 animate-blob"></div>
        <div class="brand-blob brand-blob-2 animate-blob" style="animation-delay: -3s"></div>
        <div class="brand-paw brand-paw-1">🐾</div>
        <div class="brand-paw brand-paw-2">🐾</div>
        <div class="brand-paw brand-paw-3">🐾</div>
      </div>
      <div class="brand-content">
        <div class="brand-logo">
          <div class="logo-badge">
            <svg viewBox="0 0 32 32" class="logo-svg">
              <ellipse cx="16" cy="20" rx="7" ry="8" fill="currentColor"/>
              <ellipse cx="6" cy="11" rx="3.5" ry="4.5" fill="currentColor"/>
              <ellipse cx="26" cy="11" rx="3.5" ry="4.5" fill="currentColor"/>
              <ellipse cx="11" cy="5" rx="3" ry="3.8" fill="currentColor"/>
              <ellipse cx="21" cy="5" rx="3" ry="3.8" fill="currentColor"/>
            </svg>
          </div>
          <div>
            <div class="brand-name">PawSpace</div>
            <div class="brand-sub">萌宠空间</div>
          </div>
        </div>
        <h1 class="brand-title">欢迎回来 🐾</h1>
        <p class="brand-desc">
          登录你的账号，继续为毛孩子<br>
          挑选最好的产品和最贴心的服务
        </p>
        <div class="brand-features">
          <div class="brand-feature">
            <span class="check">✓</span> 专属会员折扣
          </div>
          <div class="brand-feature">
            <span class="check">✓</span> 订单实时追踪
          </div>
          <div class="brand-feature">
            <span class="check">✓</span> 宠物健康顾问
          </div>
        </div>
        <div class="brand-emoji-row">
          <span class="brand-emoji animate-float">🐶</span>
          <span class="brand-emoji animate-float" style="animation-delay: -1s">🐱</span>
          <span class="brand-emoji animate-float" style="animation-delay: -2s">🐰</span>
          <span class="brand-emoji animate-float" style="animation-delay: -3s">🐹</span>
        </div>
      </div>
    </div>

    <!-- 右侧表单面板 -->
    <div class="form-panel">
      <div class="form-container">
        <div class="form-header">
          <h2>登录账号</h2>
          <p>欢迎回来，请输入您的账号信息</p>
        </div>

        <el-form ref="formRef" :model="form" :rules="rules" size="large" label-position="top">
          <el-form-item prop="username" label="用户名">
            <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
          </el-form-item>
          <el-form-item prop="password" label="密码">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password @keyup.enter="handleLogin" />
          </el-form-item>
          <el-form-item>
            <button type="button" class="submit-btn" :class="{ loading: loading }" :disabled="loading" @click="handleLogin">
              <span v-if="!loading">登 录</span>
              <span v-else class="loading-dots">
                <span></span><span></span><span></span>
              </span>
            </button>
          </el-form-item>
        </el-form>

        <div class="form-footer">
          还没有账号？<el-link type="primary" @click="$router.push('/register')">立即注册 →</el-link>
        </div>

        <el-divider>快速体验</el-divider>
        <div class="test-accounts">
          <button class="test-tag admin" @click="fillAdmin">
            <span class="tag-dot"></span>
            管理员 admin / admin123
          </button>
          <button class="test-tag user" @click="fillUser">
            <span class="tag-dot"></span>
            普通用户 user / user123
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { authApi } from '../api'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)

const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const fillAdmin = () => { form.username = 'admin'; form.password = 'admin123' }
const fillUser = () => { form.username = 'user'; form.password = 'user123' }

const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    const data = await authApi.login(form)
    userStore.setLogin(data)
    ElMessage.success('登录成功')
    router.push(data.user.role === 'ADMIN' ? '/admin' : '/')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
}

/* ── Brand Panel ── */
.brand-panel {
  flex: 0 0 46%;
  background: linear-gradient(135deg, #FF6B4A 0%, #FF8E6E 40%, #FFB830 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  padding: 48px;
}

.brand-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
}

.brand-blob {
  position: absolute;
  border-radius: 50% 40% 60% 50% / 50% 60% 40% 50%;
  filter: blur(30px);
}

.brand-blob-1 {
  width: 280px;
  height: 280px;
  background: rgba(255, 255, 255, 0.15);
  top: -60px;
  right: -40px;
}

.brand-blob-2 {
  width: 220px;
  height: 220px;
  background: rgba(255, 255, 255, 0.1);
  bottom: -40px;
  left: -30px;
}

.brand-paw {
  position: absolute;
  font-size: 28px;
  opacity: 0.15;
}

.brand-paw-1 { top: 20%; right: 15%; transform: rotate(15deg); }
.brand-paw-2 { bottom: 30%; left: 10%; transform: rotate(-20deg); font-size: 36px; }
.brand-paw-3 { top: 50%; left: 20%; transform: rotate(30deg); font-size: 22px; }

.brand-content {
  position: relative;
  z-index: 1;
  color: #fff;
  max-width: 400px;
}

.brand-logo {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 48px;
}

.logo-badge {
  width: 48px;
  height: 48px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.logo-svg {
  width: 28px;
  height: 28px;
}

.brand-name {
  font-size: 24px;
  font-weight: 900;
  letter-spacing: -0.5px;
}

.brand-sub {
  font-size: 12px;
  opacity: 0.8;
}

.brand-title {
  font-size: 40px;
  font-weight: 900;
  margin-bottom: 16px;
  line-height: 1.2;
}

.brand-desc {
  font-size: 15px;
  line-height: 1.7;
  opacity: 0.9;
  margin-bottom: 32px;
}

.brand-features {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 40px;
}

.brand-feature {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 15px;
  opacity: 0.95;
}

.check {
  width: 22px;
  height: 22px;
  background: rgba(255, 255, 255, 0.25);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 700;
  flex-shrink: 0;
}

.brand-emoji-row {
  display: flex;
  gap: 16px;
}

.brand-emoji {
  font-size: 36px;
}

/* ── Form Panel ── */
.form-panel {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--paw-cream);
  padding: 48px 24px;
}

.form-container {
  width: 100%;
  max-width: 380px;
}

.form-header {
  margin-bottom: 32px;
}

.form-header h2 {
  font-size: 28px;
  font-weight: 800;
  color: var(--paw-ink);
  margin-bottom: 8px;
}

.form-header p {
  font-size: 14px;
  color: var(--paw-ink-3);
}

.submit-btn {
  width: 100%;
  padding: 14px;
  border-radius: var(--paw-radius-full);
  border: none;
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-coral-light) 100%);
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  box-shadow: var(--paw-shadow-coral);
  transition: all var(--paw-normal);
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 48px;
}

.submit-btn:hover:not(.loading) {
  transform: translateY(-2px);
  box-shadow: 0 14px 32px rgba(255, 107, 74, 0.35);
}

.submit-btn.loading {
  opacity: 0.8;
  cursor: wait;
}

.loading-dots {
  display: flex;
  gap: 4px;
}

.loading-dots span {
  width: 8px;
  height: 8px;
  background: #fff;
  border-radius: 50%;
  animation: dot-bounce 1.4s infinite ease-in-out both;
}

.loading-dots span:nth-child(1) { animation-delay: -0.32s; }
.loading-dots span:nth-child(2) { animation-delay: -0.16s; }

@keyframes dot-bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

.form-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: var(--paw-ink-2);
}

.test-accounts {
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: center;
}

.test-tag {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border-radius: var(--paw-radius-full);
  border: 1px solid;
  background: transparent;
  cursor: pointer;
  font-size: 13px;
  font-weight: 600;
  transition: all var(--paw-fast);
}

.test-tag.admin {
  border-color: var(--paw-coral-100);
  color: var(--paw-coral);
}

.test-tag.admin:hover {
  background: var(--paw-coral-50);
  transform: translateY(-1px);
}

.test-tag.user {
  border-color: var(--paw-sage-100);
  color: var(--paw-sage);
}

.test-tag.user:hover {
  background: var(--paw-sage-50);
  transform: translateY(-1px);
}

.tag-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: currentColor;
}

@media (max-width: 768px) {
  .brand-panel {
    display: none;
  }
}
</style>
