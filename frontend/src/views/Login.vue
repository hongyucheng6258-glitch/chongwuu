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
            <div class="brand-name">初晴萌宠空间</div>
            <div class="brand-sub">让每一只萌宠都幸福</div>
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
          <p>欢迎回来，请选择登录方式</p>
        </div>

        <!-- 登录方式切换 -->
        <div class="login-tabs">
          <button
            class="login-tab"
            :class="{ active: loginType === 'password' }"
            @click="loginType = 'password'"
          >账号密码</button>
          <button
            class="login-tab"
            :class="{ active: loginType === 'sms' }"
            @click="loginType = 'sms'"
          >手机验证码</button>
        </div>

        <!-- 账号密码登录 -->
        <el-form v-if="loginType === 'password'" ref="formRef" :model="form" :rules="rules" size="large" label-position="top">
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

        <!-- 手机验证码登录 -->
        <el-form v-else ref="smsFormRef" :model="smsForm" :rules="smsRules" size="large" label-position="top">
          <el-form-item prop="phone" label="手机号">
            <el-input v-model="smsForm.phone" placeholder="请输入手机号" :prefix-icon="Phone" maxlength="11" />
          </el-form-item>
          <el-form-item prop="code" label="验证码">
            <div class="code-input-row">
              <el-input v-model="smsForm.code" placeholder="请输入验证码" maxlength="6" @keyup.enter="handleSmsLogin" />
              <button
                type="button"
                class="send-code-btn"
                :disabled="countdown > 0 || smsSending"
                @click="handleSendCode"
              >
                {{ countdown > 0 ? countdown + 's' : (smsSending ? '发送中...' : '获取验证码') }}
              </button>
            </div>
          </el-form-item>
          <el-form-item>
            <button type="button" class="submit-btn" :class="{ loading: smsLoading }" :disabled="smsLoading" @click="handleSmsLogin">
              <span v-if="!smsLoading">登 录</span>
              <span v-else class="loading-dots">
                <span></span><span></span><span></span>
              </span>
            </button>
          </el-form-item>
        </el-form>

        <div class="form-footer">
          还没有账号？<el-link type="primary" @click="$router.push('/register')">立即注册 →</el-link>
        </div>

        <!-- 第三方登录 -->
        <el-divider>第三方账号登录</el-divider>
        <div class="social-login">
          <button class="social-btn gitee" @click="handleGiteeLogin" :disabled="giteeLoading">
            <svg viewBox="0 0 24 24" class="social-icon">
              <path d="M11.984 0A12 12 0 0 0 0 12a12 12 0 0 0 12 12 12 12 0 0 0 12-12A12 12 0 0 0 12 0a12 12 0 0 0-.016 0zm6.09 5.333c.328 0 .593.266.592.593v1.482a.594.594 0 0 1-.593.592H9.777c-.982 0-1.778.796-1.778 1.778v5.926c0 .982.796 1.778 1.778 1.778h4.444c.982 0 1.778-.796 1.778-1.778v-.296a.593.593 0 0 0-.592-.593h-2.963a.593.593 0 0 1-.593-.592v-1.482a.593.593 0 0 1 .593-.592h4.63c.982 0 1.778.796 1.778 1.778v2.667a4 4 0 0 1-4 4H8.074a4 4 0 0 1-4-4V9.333a4 4 0 0 1 4-4h9.074c.164 0 .328 0 .49.002l.436-.002z" fill="currentColor"/>
            </svg>
            <span>{{ giteeLoading ? '跳转中...' : 'Gitee 登录' }}</span>
          </button>
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
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { User, Lock, Phone } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { authApi } from '../api'
import { useUserStore } from '../store/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)
const giteeLoading = ref(false)

// 登录方式切换
const loginType = ref('password')

// 账号密码登录
const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// 短信验证码登录
const smsFormRef = ref()
const smsForm = reactive({ phone: '', code: '' })
const smsRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { pattern: /^\d{6}$/, message: '验证码必须是6位数字', trigger: 'blur' }
  ]
}
const smsLoading = ref(false)
const smsSending = ref(false)
const countdown = ref(0)
let countdownTimer = null

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

// 发送短信验证码
const handleSendCode = async () => {
  const valid = await smsFormRef.value.validateField('phone').catch(() => false)
  if (!valid) return

  smsSending.value = true
  try {
    const res = await authApi.sendSmsCode({ phone: smsForm.phone })
    ElMessage.success('验证码已发送')
    // 在控制台打印验证码（开发调试）
    console.log('%c【短信验证码】', 'color: #FF6B4A; font-size: 16px; font-weight: bold;')
    console.log('%c手机号: ' + smsForm.phone, 'color: #333;')
    console.log('%c验证码: ' + res.code, 'color: #FF6B4A; font-size: 20px; font-weight: bold;')
    console.log('%c有效期: 5 分钟', 'color: #999;')

    // 开始倒计时
    countdown.value = 60
    countdownTimer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(countdownTimer)
      }
    }, 1000)
  } catch (e) {
    ElMessage.error(e.message || '发送失败')
  } finally {
    smsSending.value = false
  }
}

// 短信验证码登录
const handleSmsLogin = async () => {
  await smsFormRef.value.validate()
  smsLoading.value = true
  try {
    const data = await authApi.smsLogin({ phone: smsForm.phone, code: smsForm.code })
    userStore.setLogin(data)
    ElMessage.success('登录成功')
    router.push(data.user.role === 'ADMIN' ? '/admin' : '/')
  } finally {
    smsLoading.value = false
  }
}

// Gitee 第三方登录
const handleGiteeLogin = async () => {
  giteeLoading.value = true
  try {
    const res = await authApi.getGiteeAuthorizeUrl()
    // 跳转到 Gitee 授权页面
    window.location.href = res.authorizeUrl
  } catch (e) {
    ElMessage.error('获取 Gitee 授权链接失败')
  } finally {
    giteeLoading.value = false
  }
}

// 处理 Gitee 回调
onMounted(() => {
  const { giteeLogin, token, user, error, msg } = route.query
  if (giteeLogin === '1' && token && user) {
    try {
      const userData = JSON.parse(decodeURIComponent(user))
      userStore.setLogin({ token, user: userData })
      ElMessage.success('Gitee 登录成功')
      router.replace(userData.role === 'ADMIN' ? '/admin' : '/')
    } catch (e) {
      ElMessage.error('Gitee 登录数据解析失败')
    }
  } else if (error) {
    const errorMsg = msg ? decodeURIComponent(msg) : 'Gitee 登录失败'
    ElMessage.error(errorMsg)
    // 清除 URL 中的错误参数
    router.replace('/login')
  }
})
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

/* ── 登录方式切换 ── */
.login-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  background: var(--paw-surface);
  padding: 4px;
  border-radius: var(--paw-radius-full);
}

.login-tab {
  flex: 1;
  padding: 10px 0;
  border: none;
  background: transparent;
  border-radius: var(--paw-radius-full);
  font-size: 14px;
  font-weight: 600;
  color: var(--paw-ink-2);
  cursor: pointer;
  transition: all var(--paw-fast);
}

.login-tab.active {
  background: #fff;
  color: var(--paw-coral);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* ── 验证码输入行 ── */
.code-input-row {
  display: flex;
  gap: 10px;
}

.code-input-row .el-input {
  flex: 1;
}

.send-code-btn {
  padding: 0 16px;
  border-radius: var(--paw-radius-full);
  border: 1px solid var(--paw-coral);
  background: transparent;
  color: var(--paw-coral);
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  white-space: nowrap;
  transition: all var(--paw-fast);
  min-width: 100px;
}

.send-code-btn:hover:not(:disabled) {
  background: var(--paw-coral);
  color: #fff;
}

.send-code-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  border-color: #ccc;
  color: #999;
}

/* ── 第三方登录 ── */
.social-login {
  display: flex;
  justify-content: center;
  gap: 12px;
}

.social-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 24px;
  border-radius: var(--paw-radius-full);
  border: 1px solid #dcdfe6;
  background: #fff;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  color: var(--paw-ink);
  transition: all var(--paw-fast);
}

.social-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.social-btn:disabled {
  opacity: 0.6;
  cursor: wait;
}

.social-btn.gitee {
  border-color: #c71d23;
  color: #c71d23;
}

.social-btn.gitee:hover:not(:disabled) {
  background: #c71d23;
  color: #fff;
}

.social-icon {
  width: 20px;
  height: 20px;
}

/* ── Test Accounts ── */
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
