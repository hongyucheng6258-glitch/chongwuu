<template>
  <div class="container">
    <div class="page-header">
      <h1 class="page-title"><span class="title-icon">📦</span> 订单结算</h1>
    </div>

    <div class="checkout" v-loading="loading">
      <div class="section">
        <div class="section-head">
          <span class="section-num">1</span>
          <h2 class="section-title">收货信息</h2>
          <el-button link type="primary" @click="loadAddresses" size="small">刷新地址</el-button>
        </div>
        <!-- 已保存地址快捷选择 -->
        <div v-if="savedAddresses.length > 0" class="saved-addresses">
          <div v-for="addr in savedAddresses" :key="addr.id" class="saved-addr" :class="{ active: selectedAddrId === addr.id }" @click="selectAddr(addr)">
            <div class="addr-tag-row">
              <strong>{{ addr.name }}</strong> {{ addr.phone }}
              <el-tag v-if="addr.isDefault === 1" type="danger" size="small">默认</el-tag>
            </div>
            <div class="addr-text">{{ addr.address }}</div>
          </div>
        </div>
        <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" size="large">
          <el-form-item label="收货人" prop="receiver">
            <el-input v-model="form.receiver" placeholder="请输入收货人姓名" />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入手机号" />
          </el-form-item>
          <el-form-item label="收货地址" prop="address">
            <el-input v-model="form.address" type="textarea" :rows="2" placeholder="请输入详细收货地址" />
          </el-form-item>
        </el-form>
      </div>

      <div class="section">
        <div class="section-head">
          <span class="section-num">2</span>
          <h2 class="section-title">商品清单</h2>
        </div>
        <div class="order-items">
          <div v-for="item in cartItems" :key="item.id" class="order-item">
            <div class="item-img">
              <img :src="item.product?.image" @error="imgError" />
            </div>
            <div class="item-info">
              <span class="item-name">{{ item.product?.name }}</span>
              <span class="item-price">
                ¥{{ item.product?.discountPrice || item.product?.price }} × {{ item.quantity }}
                <span v-if="item.product?.discountPrice && item.product?.discountPrice < item.product?.price" class="item-original">¥{{ item.product?.price }}</span>
              </span>
            </div>
            <span class="item-sub">¥{{ ((item.product?.discountPrice || item.product?.price) * item.quantity).toFixed(2) }}</span>
          </div>
        </div>
      </div>

      <div class="submit-bar">
        <div class="total">
          <div class="total-detail">
            <span class="total-label">共 {{ cartItems.length }} 件商品</span>
            <span v-if="userStore.memberLevel > 0" class="member-discount">
              {{ memberLevelName }}专享价，已省 ¥{{ discountAmount }}
            </span>
          </div>
          <div class="total-price-row">
            <span v-if="originalAmount > totalAmount" class="original-total">¥{{ originalAmount }}</span>
            <span class="total-price">¥{{ totalAmount }}</span>
          </div>
        </div>
        <button class="submit-btn" :disabled="submitting" @click="submitOrder">
          <span v-if="!submitting">提交订单</span>
          <span v-else class="loading-dots"><span></span><span></span><span></span></span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { cartApi, orderApi, addressApi } from '../api'
import { useUserStore } from '../store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const formRef = ref()
const loading = ref(false)
const submitting = ref(false)
const cartItems = ref([])
const savedAddresses = ref([])
const selectedAddrId = ref(null)

const form = reactive({
  receiver: '',
  phone: '',
  address: ''
})

const rules = {
  receiver: [{ required: true, message: '请输入收货人', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  address: [{ required: true, message: '请输入收货地址', trigger: 'blur' }]
}

const totalAmount = computed(() => {
  return cartItems.value
    .reduce((sum, i) => sum + (i.product?.discountPrice || i.product?.price || 0) * i.quantity, 0)
    .toFixed(2)
})

const originalAmount = computed(() => {
  return cartItems.value
    .reduce((sum, i) => sum + (i.product?.price || 0) * i.quantity, 0)
    .toFixed(2)
})

const discountAmount = computed(() => {
  return (parseFloat(originalAmount.value) - parseFloat(totalAmount.value)).toFixed(2)
})

const memberLevelName = computed(() => {
  return userStore.memberLevelName
})

const loadCart = async () => {
  loading.value = true
  try {
    const all = await cartApi.list()
    const ids = (route.query.ids || '').split(',').filter(Boolean).map(Number)
    cartItems.value = ids.length ? all.filter(i => ids.includes(i.id)) : all
    if (cartItems.value.length === 0) {
      ElMessage.warning('请先选择商品')
      router.push('/cart')
    }
    // 加载已保存地址
    loadAddresses()
  } finally {
    loading.value = false
  }
}

const loadAddresses = async () => {
  try { savedAddresses.value = await addressApi.list() } catch (e) {}
}

const selectAddr = (addr) => {
  selectedAddrId.value = addr.id
  form.receiver = addr.name
  form.phone = addr.phone
  form.address = addr.address
}

const submitOrder = async () => {
  await formRef.value.validate()
  submitting.value = true
  try {
    const cartItemIds = cartItems.value.map(i => i.id)
    await orderApi.create({
      cartItemIds,
      receiver: form.receiver,
      phone: form.phone,
      address: form.address
    })
    ElMessage.success('下单成功！')
    router.push('/orders')
  } finally {
    submitting.value = false
  }
}

const imgError = (e) => {
  e.target.style.display = 'none'
}

onMounted(loadCart)
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
}

.page-header {
  margin-bottom: 24px;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 26px;
  font-weight: 800;
  color: var(--paw-ink);
}

.title-icon {
  font-size: 28px;
}

.checkout {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.section {
  background: var(--paw-card);
  border-radius: var(--paw-radius-lg);
  padding: 28px;
  box-shadow: var(--paw-shadow-sm);
  border: 1px solid var(--paw-border-light);
}

.section-head {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.section-num {
  width: 28px;
  height: 28px;
  background: linear-gradient(135deg, var(--paw-coral), var(--paw-coral-light));
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 800;
  box-shadow: var(--paw-shadow-coral);
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  color: var(--paw-ink);
}

.order-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px;
  border-radius: var(--paw-radius-md);
  background: var(--paw-surface);
  transition: background var(--paw-fast);
}

.order-item:hover {
  background: var(--paw-coral-50);
}

.item-img {
  width: 60px;
  height: 60px;
  border-radius: var(--paw-radius-sm);
  overflow: hidden;
  background: var(--paw-cream);
  flex-shrink: 0;
  border: 1px solid var(--paw-border-light);
}

.item-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--paw-ink);
}

.item-price {
  font-size: 13px;
  color: var(--paw-ink-3);
}

.item-sub {
  font-size: 16px;
  font-weight: 800;
  color: var(--paw-coral);
}

.submit-bar {
  background: var(--paw-card);
  border-radius: var(--paw-radius-lg);
  padding: 20px 28px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 24px;
  box-shadow: var(--paw-shadow-sm);
  border: 1px solid var(--paw-border-light);
}

.total {
  text-align: right;
}

.total-detail {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.total-label {
  font-size: 14px;
  color: var(--paw-ink-3);
}

.member-discount {
  font-size: 12px;
  color: var(--paw-coral);
  font-weight: 600;
}

.total-price-row {
  display: flex;
  align-items: baseline;
  gap: 8px;
  justify-content: flex-end;
}

.original-total {
  font-size: 16px;
  color: var(--paw-ink-4);
  text-decoration: line-through;
}

.total-price {
  font-size: 28px;
  color: var(--paw-coral);
  font-weight: 900;
}

.item-original {
  text-decoration: line-through;
  color: var(--paw-ink-4);
  margin-left: 6px;
  font-size: 12px;
}

.submit-btn {
  padding: 14px 36px;
  border-radius: var(--paw-radius-full);
  border: none;
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-coral-light) 100%);
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  box-shadow: var(--paw-shadow-coral);
  transition: all var(--paw-normal);
  min-height: 48px;
  min-width: 140px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 14px 32px rgba(255, 107, 74, 0.35);
}

.submit-btn:disabled {
  opacity: 0.7;
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

.saved-addresses { margin-bottom: 20px; display: flex; flex-direction: column; gap: 10px; }
.saved-addr { padding: 12px; border: 1px solid var(--el-border-color); border-radius: 8px; cursor: pointer; transition: all 0.2s; }
.saved-addr:hover { border-color: var(--paw-coral); }
.saved-addr.active { border-color: var(--paw-coral); background: var(--paw-coral-light); }
.addr-tag-row { display: flex; align-items: center; gap: 8px; font-size: 14px; margin-bottom: 4px; }
.addr-text { font-size: 13px; color: #666; }
</style>
