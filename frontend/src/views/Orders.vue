<template>
  <div class="container">
    <div class="page-header">
      <h1 class="page-title"><span class="title-icon">📋</span> 我的订单</h1>
    </div>

    <div v-loading="loading">
      <div v-if="orders.length > 0" class="order-list">
        <div v-for="order in orders" :key="order.id" class="order-card animate-fade-up">
          <div class="order-header" @click="$router.push(`/order/${order.id}`)">
            <div class="order-meta">
              <span class="order-no">订单号: {{ order.orderNo }}</span>
              <span class="order-time">{{ formatTime(order.createdAt) }}</span>
            </div>
            <span class="status-pill" :class="'s' + order.status">
              {{ statusText(order.status) }}
            </span>
          </div>

          <div class="order-items" @click="$router.push(`/order/${order.id}`)">
            <div v-for="item in order.items" :key="item.id" class="order-item">
              <div class="item-emoji">🐾</div>
              <div class="item-info">
                <span class="item-name">{{ item.productName }}</span>
                <span class="item-price">¥{{ item.price }} × {{ item.quantity }}</span>
              </div>
              <span class="item-sub">¥{{ (item.price * item.quantity).toFixed(2) }}</span>
            </div>
          </div>

          <div class="order-footer">
            <div class="order-info">
              <div class="info-line">
                <span class="info-icon">👤</span>
                {{ order.receiver }} · {{ order.phone }}
              </div>
              <div class="info-line">
                <span class="info-icon">📍</span>
                {{ order.address }}
              </div>
            </div>
            <div class="order-total">
              <span class="total-count">共 {{ itemCount(order) }} 件商品</span>
              <span class="total-amount">¥{{ order.totalAmount }}</span>
              <div class="order-actions">
                <button class="detail-btn" @click="$router.push(`/order/${order.id}`)">查看详情</button>
                <button v-if="order.status === 0" class="action-btn pay-btn" @click.stop="payOrder(order.id)">去支付</button>
                <button v-if="[0, 1].includes(order.status)" class="action-btn cancel-btn" @click.stop="cancelOrder(order.id)">取消订单</button>
                <button v-if="order.status === 2" class="action-btn receive-btn" @click.stop="confirmReceive(order.id)">确认收货</button>
                <button v-if="[2, 3].includes(order.status) && order.status !== -2" class="action-btn return-btn" @click.stop="requestReturn(order.id)">申请退单</button>
                <button v-if="order.status === 3 && order.reviewed === 0" class="action-btn review-btn" @click.stop="openReview(order)">去评价</button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="empty-state">
        <div class="empty-emoji">📦</div>
        <p class="empty-text">暂无订单</p>
        <p class="empty-sub">快去下单，为毛孩子挑选好物吧</p>
        <button class="go-shop-btn" @click="$router.push('/')">去购物</button>
      </div>
    </div>

    <!-- 评价弹窗 -->
    <el-dialog v-model="reviewVisible" title="发表评价" width="450px">
      <el-form :model="reviewForm">
        <el-form-item label="评分">
          <el-rate v-model="reviewForm.rating" :max="5" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="reviewForm.content" type="textarea" :rows="3" placeholder="分享你的使用体验..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReview" :loading="reviewing">提交评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { orderApi, reviewApi } from '../api'

const orders = ref([])
const loading = ref(false)
const reviewVisible = ref(false)
const reviewing = ref(false)
const reviewForm = ref({ rating: 5, content: '', orderId: null, productId: null })

const statusText = (s) => {
  const map = {
    0: '待支付', 1: '已支付', 2: '已发货', 3: '已收货',
    4: '已完成', '-1': '已取消', '-2': '退单中', '-4': '已退单'
  }
  return map[String(s)] || '未知'
}

const itemCount = (order) => order.items?.reduce((s, i) => s + i.quantity, 0) || 0
const formatTime = (t) => t ? t.replace('T', ' ').substring(0, 19) : ''

const loadOrders = async () => {
  loading.value = true
  try { orders.value = await orderApi.list() } finally { loading.value = false }
}

const payOrder = async (id) => {
  try { await orderApi.pay(id); ElMessage.success('支付成功'); loadOrders() } catch (e) {}
}

const cancelOrder = async (id) => {
  const { value: reason } = await ElMessageBox.prompt('请输入取消原因（可选）', '取消订单', {
    inputPlaceholder: '取消原因...', confirmButtonText: '确认取消', cancelButtonText: '再想想'
  }).catch(() => null)
  if (reason === null) return
  await orderApi.cancel(id, reason || '用户主动取消')
  ElMessage.success('订单已取消')
  loadOrders()
}

const confirmReceive = async (id) => {
  await ElMessageBox.confirm('确认已收到商品？', '确认收货', { type: 'info' })
  try { await orderApi.receive(id); ElMessage.success('已确认收货'); loadOrders() } catch (e) {}
}

const requestReturn = async (id) => {
  const { value: reason } = await ElMessageBox.prompt('请输入退单理由', '申请退单', {
    inputPlaceholder: '退单理由...', confirmButtonText: '提交申请', cancelButtonText: '取消'
  }).catch(() => null)
  if (!reason) return
  try { await orderApi.requestReturn(id, reason); ElMessage.success('退单申请已提交'); loadOrders() } catch (e) {}
}

const openReview = (order) => {
  reviewForm.value = {
    rating: 5, content: '',
    orderId: order.id,
    productId: order.items?.[0]?.productId
  }
  reviewVisible.value = true
}

const submitReview = async () => {
  reviewing.value = true
  try {
    await reviewApi.create({
      orderId: reviewForm.value.orderId,
      productId: reviewForm.value.productId,
      rating: reviewForm.value.rating,
      content: reviewForm.value.content
    })
    ElMessage.success('评价成功')
    reviewVisible.value = false
    loadOrders()
  } catch (e) {}
  reviewing.value = false
}

onMounted(loadOrders)
</script>

<style scoped>
.container { max-width: 900px; margin: 0 auto; padding: 24px; }
.page-header { margin-bottom: 24px; }
.page-title { display: flex; align-items: center; gap: 10px; font-size: 26px; font-weight: 800; color: var(--paw-ink); }
.title-icon { font-size: 28px; }
.order-list { display: flex; flex-direction: column; gap: 16px; }
.order-card { background: var(--paw-card); border-radius: var(--paw-radius-lg); overflow: hidden; box-shadow: var(--paw-shadow-sm); border: 1px solid var(--paw-border-light); transition: box-shadow var(--paw-normal); }
.order-card:hover { box-shadow: var(--paw-shadow-md); }
.order-header { display: flex; align-items: center; justify-content: space-between; padding: 16px 24px; background: var(--paw-surface); border-bottom: 1px solid var(--paw-border-light); cursor: pointer; }
.order-meta { display: flex; flex-direction: column; gap: 4px; }
.order-no { font-size: 13px; font-weight: 600; color: var(--paw-ink-2); }
.order-time { font-size: 12px; color: var(--paw-ink-4); }
.status-pill { padding: 5px 14px; border-radius: var(--paw-radius-full); font-size: 12px; font-weight: 700; }
.status-pill.s0 { background: var(--paw-amber-50); color: var(--paw-amber); }
.status-pill.s1 { background: var(--paw-sky-50); color: var(--paw-sky); }
.status-pill.s2 { background: #f0f5ff; color: #409eff; }
.status-pill.s3 { background: var(--paw-sage-50); color: #67c23a; }
.status-pill.s4 { background: var(--paw-sage-50); color: var(--paw-sage); }
.status-pill.s-1, .status-pill.s-4 { background: #fee; color: #f56c6c; }
.status-pill.s-2 { background: #fdf6ec; color: #e6a23c; }
.order-items { padding: 16px 24px; display: flex; flex-direction: column; gap: 12px; cursor: pointer; }
.order-item { display: flex; align-items: center; gap: 12px; }
.item-emoji { width: 36px; height: 36px; border-radius: var(--paw-radius-sm); background: var(--paw-cream); display: flex; align-items: center; justify-content: center; font-size: 18px; flex-shrink: 0; }
.item-info { flex: 1; display: flex; flex-direction: column; gap: 2px; }
.item-name { font-size: 14px; font-weight: 600; color: var(--paw-ink); }
.item-price { font-size: 12px; color: var(--paw-ink-3); }
.item-sub { font-size: 15px; font-weight: 700; color: var(--paw-coral); }
.order-footer { display: flex; align-items: flex-start; justify-content: space-between; padding: 16px 24px; background: var(--paw-surface); border-top: 1px solid var(--paw-border-light); gap: 24px; }
.order-info { display: flex; flex-direction: column; gap: 6px; }
.info-line { display: flex; align-items: center; gap: 6px; font-size: 12px; color: var(--paw-ink-3); }
.info-icon { font-size: 13px; }
.order-total { display: flex; flex-direction: column; align-items: flex-end; gap: 8px; flex-shrink: 0; }
.total-count { font-size: 12px; color: var(--paw-ink-3); }
.total-amount { font-size: 20px; font-weight: 900; color: var(--paw-coral); }
.order-actions { display: flex; gap: 8px; flex-wrap: wrap; }
.detail-btn { padding: 6px 14px; border-radius: var(--paw-radius-full); border: 1px solid var(--el-border-color); background: transparent; color: var(--paw-ink-2); font-size: 12px; font-weight: 600; cursor: pointer; transition: all var(--paw-fast); }
.detail-btn:hover { border-color: var(--paw-coral); color: var(--paw-coral); }
.action-btn { padding: 6px 14px; border-radius: var(--paw-radius-full); border: none; font-size: 12px; font-weight: 600; cursor: pointer; }
.pay-btn { background: var(--paw-coral); color: white; }
.receive-btn { background: #67c23a; color: white; }
.cancel-btn { border: 1px solid #f0c0c0; background: transparent; color: #f56c6c; }
.return-btn { border: 1px solid #e6a23c; background: transparent; color: #e6a23c; }
.review-btn { background: var(--paw-sky); color: white; }
.empty-state { text-align: center; padding: 80px 20px; }
.empty-emoji { font-size: 64px; margin-bottom: 16px; }
.empty-text { font-size: 18px; font-weight: 700; color: var(--paw-ink); margin-bottom: 8px; }
.empty-sub { font-size: 14px; color: var(--paw-ink-3); margin-bottom: 24px; }
.go-shop-btn { padding: 12px 32px; border-radius: var(--paw-radius-full); border: none; background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-coral-light) 100%); color: #fff; font-size: 15px; font-weight: 700; cursor: pointer; box-shadow: var(--paw-shadow-coral); }
.go-shop-btn:hover { transform: translateY(-2px); box-shadow: 0 14px 32px rgba(255, 107, 74, 0.35); }
</style>
