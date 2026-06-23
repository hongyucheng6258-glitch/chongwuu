<template>
  <div class="container">
    <div class="page-header">
      <h1 class="page-title"><span class="title-icon">🔔</span> 消息中心</h1>
      <el-button v-if="notifications.length > 0" link type="primary" @click="markAllRead">
        全部已读
      </el-button>
    </div>

    <div v-loading="loading" class="notification-list">
      <div v-if="notifications.length === 0" class="empty-state">
        <el-empty description="暂无消息" :image-size="80" />
      </div>

      <div
        v-for="msg in notifications"
        :key="msg.id"
        class="notification-item"
        :class="{ unread: msg.isRead === 0 }"
        @click="handleClick(msg)"
      >
        <div class="msg-icon">
          <span v-if="msg.msgType === 'ORDER'">📦</span>
          <span v-else-if="msg.msgType === 'RETURN'">↩️</span>
          <span v-else-if="msg.msgType === 'PROMO'">🎉</span>
          <span v-else>📢</span>
        </div>
        <div class="msg-content">
          <div class="msg-title">{{ msg.title }}</div>
          <div class="msg-body">{{ msg.content }}</div>
          <div class="msg-time">{{ msg.createdAt?.substring(0, 16)?.replace('T', ' ') }}</div>
        </div>
        <div class="msg-actions">
          <el-button v-if="msg.isRead === 0" link type="primary" size="small" @click.stop="markRead(msg.id)">
            标记已读
          </el-button>
          <el-button link type="danger" size="small" @click.stop="remove(msg.id)">
            删除
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { notificationApi } from '../api'

const router = useRouter()
const notifications = ref([])
const loading = ref(false)

const loadNotifications = async () => {
  loading.value = true
  try {
    notifications.value = await notificationApi.list()
  } catch (e) {
    ElMessage.error('加载消息失败')
  } finally {
    loading.value = false
  }
}

const markRead = async (id) => {
  try {
    await notificationApi.markAsRead(id)
    const msg = notifications.value.find(n => n.id === id)
    if (msg) msg.isRead = 1
    ElMessage.success('已标记为已读')
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const markAllRead = async () => {
  try {
    await notificationApi.markAllAsRead()
    notifications.value.forEach(n => n.isRead = 1)
    ElMessage.success('全部已读')
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const remove = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除这条消息？', '提示', { type: 'warning' })
    await notificationApi.delete(id)
    notifications.value = notifications.value.filter(n => n.id !== id)
    ElMessage.success('删除成功')
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

const handleClick = (msg) => {
  if (msg.orderId) {
    router.push(`/order/${msg.orderId}`)
  }
  if (msg.isRead === 0) {
    markRead(msg.id)
  }
}

onMounted(loadNotifications)
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 26px;
  font-weight: 800;
  color: var(--paw-ink);
  margin: 0;
}

.title-icon {
  font-size: 28px;
}

.notification-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 18px 20px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-lg);
  border: 1px solid var(--paw-border-light);
  cursor: pointer;
  transition: all var(--paw-fast);
}

.notification-item:hover {
  box-shadow: var(--paw-shadow-md);
  transform: translateY(-1px);
}

.notification-item.unread {
  background: linear-gradient(135deg, var(--paw-coral-50), var(--paw-amber-50));
  border-color: var(--paw-coral-light);
}

.msg-icon {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: var(--paw-cream);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}

.msg-content {
  flex: 1;
  min-width: 0;
}

.msg-title {
  font-size: 15px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 6px;
}

.msg-body {
  font-size: 13px;
  color: var(--paw-ink-2);
  line-height: 1.6;
  margin-bottom: 8px;
}

.msg-time {
  font-size: 12px;
  color: var(--paw-ink-4);
}

.msg-actions {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex-shrink: 0;
}

.empty-state {
  padding: 60px 0;
}
</style>
