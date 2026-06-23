<template>
  <div class="ai-chat-page">
    <div class="page-card">
      <div class="chat-header">
        <div class="chat-avatar">🐼</div>
        <div>
          <h2>AI 智能助手</h2>
          <p>问商品、聊养护、查订单，我都能帮你~</p>
        </div>
      </div>

      <div class="quick-actions">
        <el-button size="small" @click="quickAsk('我想给猫咪买什么猫粮好？')">🐱 猫粮推荐</el-button>
        <el-button size="small" @click="quickAsk('狗狗日常该怎么喂养？')">🐶 狗狗喂养</el-button>
        <el-button size="small" @click="quickAsk('推荐一些热门商品')">🛒 热门商品</el-button>
        <el-button size="small" @click="quickAsk('我的订单什么时候发货？')">📦 订单查询</el-button>
        <el-button size="small" @click="quickAsk('我的购物车有什么？')">🛒 购物车</el-button>
        <el-button size="small" @click="quickAsk('我的会员等级和权益')">💎 会员权益</el-button>
        <el-button size="small" @click="quickAsk('附近有什么门店？')">🏪 门店查询</el-button>
        <el-button size="small" @click="quickAsk('有什么宠物零食推荐？')">🍖 零食推荐</el-button>
      </div>

      <div class="chat-messages" ref="msgContainer">
        <div v-if="messages.length === 0" class="welcome-msg">
          <div class="welcome-icon">🐾</div>
          <p>你好！我是初晴萌宠空间的AI助手~</p>
          <p class="hint">可以问我宠物护理、商品推荐、订单相关的问题哦！</p>
          <div class="welcome-features">
            <div class="feature-item">🛒 商品推荐</div>
            <div class="feature-item">📦 订单查询</div>
            <div class="feature-item">🐱🐶 宠物护理</div>
            <div class="feature-item">💎 会员权益</div>
            <div class="feature-item">🏪 门店查询</div>
            <div class="feature-item">🛒 购物车</div>
          </div>
        </div>
        <div v-for="(msg, i) in messages" :key="i" :class="['message', msg.role]">
          <div v-if="msg.role === 'user'" class="msg-bubble">{{ msg.content }}</div>
          <div v-else class="msg-bubble ai-bubble" v-html="formatAiMessage(msg.content)"></div>
        </div>
        <div v-if="sending" class="message assistant">
          <div class="msg-bubble ai-bubble typing">
            <span class="dot"></span><span class="dot"></span><span class="dot"></span>
          </div>
        </div>
      </div>

      <div class="chat-input">
        <el-input
          v-model="inputText"
          placeholder="输入你的问题..."
          @keyup.enter="send"
          :disabled="sending"
          size="large"
        >
          <template #append>
            <el-button :icon="Promotion" @click="send" :loading="sending" />
          </template>
        </el-input>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { Promotion } from '@element-plus/icons-vue'
import { aiApi } from '../api'

const messages = ref([])
const inputText = ref('')
const sending = ref(false)
const msgContainer = ref(null)

const quickAsk = (text) => {
  inputText.value = text
  send()
}

const send = async () => {
  const text = inputText.value.trim()
  if (!text || sending.value) return
  inputText.value = ''
  messages.value.push({ role: 'user', content: text })
  await scrollBottom()

  const history = messages.value.slice(0, -1).slice(-16).map(m => ({
    role: m.role,
    content: m.content
  }))

  sending.value = true
  try {
    const data = await aiApi.chat({ message: text, history })
    messages.value.push({ role: 'assistant', content: data.reply })
  } catch (e) {
    messages.value.push({ role: 'assistant', content: '抱歉，我现在无法回复，请稍后再试~ 您也可以尝试刷新页面重试。' })
  }
  sending.value = false
  await scrollBottom()
}

const formatAiMessage = (content) => {
  if (!content) return ''
  return content
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\n/g, '<br>')
}

const scrollBottom = async () => {
  await nextTick()
  if (msgContainer.value) {
    msgContainer.value.scrollTop = msgContainer.value.scrollHeight
  }
}
</script>

<style scoped>
.ai-chat-page { padding: 24px; max-width: 700px; margin: 0 auto; height: calc(100vh - 160px); display: flex; flex-direction: column; }
.page-card { flex: 1; display: flex; flex-direction: column; }
.chat-header { display: flex; align-items: center; gap: 16px; padding-bottom: 16px; border-bottom: 1px solid var(--el-border-color); }
.chat-avatar { font-size: 40px; }
.chat-header h2 { margin: 0 0 4px; font-size: 20px; }
.chat-header p { margin: 0; color: #999; font-size: 13px; }
.quick-actions { display: flex; gap: 8px; flex-wrap: wrap; padding: 12px 0; }
.chat-messages { flex: 1; overflow-y: auto; padding: 16px 0; }
.welcome-msg { text-align: center; padding: 40px 20px; }
.welcome-icon { font-size: 48px; margin-bottom: 12px; }
.welcome-msg p { margin: 4px 0; color: #666; }
.welcome-msg .hint { font-size: 13px; color: #999; }
.welcome-features { display: flex; flex-wrap: wrap; gap: 8px; justify-content: center; margin-top: 20px; }
.feature-item { padding: 6px 14px; background: var(--el-bg-color-page); border: 1px solid var(--el-border-color); border-radius: 20px; font-size: 13px; color: #666; cursor: default; }
.message { margin-bottom: 16px; display: flex; }
.message.user { justify-content: flex-end; }
.message.user .msg-bubble { background: var(--paw-coral); color: white; }
.message.assistant .msg-bubble { background: var(--el-bg-color-page); border: 1px solid var(--el-border-color); }
.msg-bubble { max-width: 80%; padding: 10px 16px; border-radius: 14px; font-size: 14px; line-height: 1.7; white-space: pre-wrap; }
.ai-bubble { white-space: normal; }
.ai-bubble strong { color: var(--paw-coral); font-weight: 600; }

/* Typing animation */
.typing { display: flex; gap: 4px; align-items: center; padding: 4px 0; }
.dot { width: 8px; height: 8px; border-radius: 50%; background: #ccc; animation: blink 1.4s infinite both; }
.dot:nth-child(2) { animation-delay: 0.2s; }
.dot:nth-child(3) { animation-delay: 0.4s; }
@keyframes blink { 0%, 80%, 100% { opacity: 0.3; } 40% { opacity: 1; } }

.chat-input { padding-top: 12px; border-top: 1px solid var(--el-border-color); }
</style>
