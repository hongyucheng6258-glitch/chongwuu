<template>
  <div class="order-detail-page">
    <el-page-header @back="$router.back()" title="返回订单列表" />
    <div class="page-card" v-loading="loading">
      <template v-if="order">
        <div class="order-header">
          <h2>订单详情</h2>
          <el-tag :type="statusTagType" size="large">{{ statusText }}</el-tag>
        </div>

        <el-card class="info-card">
          <template #header><span>订单信息</span></template>
          <p><strong>订单编号：</strong>{{ order.orderNo }}</p>
          <p><strong>下单时间：</strong>{{ order.createdAt }}</p>
          <p><strong>订单状态：</strong>{{ statusText }}</p>
          <p v-if="order.cancelReason"><strong>取消原因：</strong>{{ order.cancelReason }}</p>
          <p v-if="order.returnReason"><strong>退单理由：</strong>{{ order.returnReason }}</p>
          <p><strong>总金额：</strong><span class="price">¥{{ order.totalAmount }}</span></p>
        </el-card>

        <el-card class="info-card">
          <template #header><span>收货信息</span></template>
          <p><strong>收货人：</strong>{{ order.receiver }}</p>
          <p><strong>手机号：</strong>{{ order.phone }}</p>
          <p><strong>地址：</strong>{{ order.address }}</p>
        </el-card>

        <el-card class="info-card">
          <template #header><span>商品清单</span></template>
          <div v-for="item in order.items" :key="item.id" class="order-item">
            <div class="item-info">
              <strong>{{ item.productName }}</strong>
              <span class="item-qty">×{{ item.quantity }}</span>
            </div>
            <span class="item-price">¥{{ item.price }}</span>
          </div>
        </el-card>

        <!-- 用户操作按钮 -->
        <div class="action-bar" v-if="canShowActions">
          <!-- 状态0：待支付 → 去支付 -->
          <el-button v-if="order.status === 0" type="primary" @click="payOrder" :loading="acting">去支付</el-button>
          <!-- 状态0或1：取消订单 -->
          <el-button v-if="[0, 1].includes(order.status)" type="danger" @click="cancelOrder" :loading="acting">取消订单</el-button>
          <!-- 状态2：确认收货 -->
          <el-button v-if="order.status === 2" type="success" @click="confirmReceive" :loading="acting">确认收货</el-button>
          <!-- 状态2或3：申请退单 -->
          <el-button v-if="[2, 3].includes(order.status)" @click="requestReturn" :loading="acting">申请退单</el-button>
          <!-- 状态3：去评价 -->
          <el-button v-if="order.status === 3 && order.reviewed === 0" type="primary" @click="goReview">去评价</el-button>
          <!-- 状态4：已评价 -->
          <el-tag v-if="order.status === 4" type="success">已评价</el-tag>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { orderApi } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '../store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const order = ref(null)
const loading = ref(false)
const acting = ref(false)

onMounted(async () => {
  loading.value = true
  try { order.value = await orderApi.detail(route.params.id) } catch (e) {}
  loading.value = false
})

const statusMap = {
  0: '待支付', 1: '已支付', 2: '已发货', 3: '已收货',
  4: '已完成', '-1': '已取消', '-2': '申请退单中',
  '-3': '退单被驳回', '-4': '已退单'
}
const statusText = computed(() => statusMap[String(order.value?.status)] || '未知')

const statusTagType = computed(() => {
  const map = { 0: 'warning', 1: '', 2: 'info', 3: 'success', 4: 'success', '-1': 'info', '-2': 'warning', '-3': 'danger', '-4': 'info' }
  return map[String(order.value?.status)] || 'info'
})

const canShowActions = computed(() => {
  if (!order.value) return false
  // 管理员不能在前端操作自己的订单（简化处理，管理员走后台）
  return !userStore.isAdmin || route.path.startsWith('/admin/')
})

const payOrder = async () => {
  acting.value = true
  try {
    order.value = await orderApi.pay(order.value.id)
    ElMessage.success('支付成功')
  } catch (e) {}
  acting.value = false
}

const cancelOrder = async () => {
  const { value: reason } = await ElMessageBox.prompt('请输入取消原因（可选）', '取消订单', {
    inputPlaceholder: '取消原因...',
    confirmButtonText: '确认取消',
    cancelButtonText: '再想想'
  }).catch(() => null)
  if (reason === null) return
  acting.value = true
  try {
    order.value = await orderApi.cancel(order.value.id, reason || '用户主动取消')
    ElMessage.success('订单已取消')
  } catch (e) {}
  acting.value = false
}

const confirmReceive = async () => {
  await ElMessageBox.confirm('确认已收到商品？', '确认收货', { type: 'info' })
  acting.value = true
  try {
    order.value = await orderApi.receive(order.value.id)
    ElMessage.success('已确认收货')
  } catch (e) {}
  acting.value = false
}

const requestReturn = async () => {
  const { value: reason } = await ElMessageBox.prompt('请输入退单理由', '申请退单', {
    inputPlaceholder: '退单理由...',
    confirmButtonText: '提交申请',
    cancelButtonText: '取消'
  }).catch(() => null)
  if (!reason) return
  acting.value = true
  try {
    order.value = await orderApi.requestReturn(order.value.id, reason)
    ElMessage.success('退单申请已提交')
  } catch (e) {}
  acting.value = false
}

const goReview = () => {
  router.push({
    path: '/orders',
    query: { reviewOrderId: order.value.id, reviewProductId: order.value.items?.[0]?.productId }
  })
}
</script>

<style scoped>
.order-detail-page { padding: 24px; max-width: 800px; margin: 0 auto; }
.order-header { display: flex; align-items: center; gap: 12px; margin: 20px 0; }
.order-header h2 { margin: 0; }
.info-card { margin-bottom: 16px; }
.info-card p { margin: 6px 0; font-size: 14px; }
.price { color: var(--paw-coral); font-weight: bold; font-size: 18px; }
.order-item { display: flex; justify-content: space-between; align-items: center; padding: 8px 0; border-bottom: 1px solid #f0f0f0; }
.order-item:last-child { border-bottom: none; }
.item-qty { color: #999; margin-left: 8px; }
.item-price { color: var(--paw-coral); font-weight: 500; }
.action-bar { display: flex; gap: 12px; flex-wrap: wrap; padding: 20px 0; }
</style>
