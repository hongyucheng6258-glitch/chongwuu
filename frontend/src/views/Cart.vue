<template>
  <div class="container">
    <div class="page-header">
      <h1 class="page-title">
        <span class="title-icon">🛒</span> 我的购物车
      </h1>
      <span class="item-count" v-if="cartItems.length > 0">{{ cartItems.length }} 件商品</span>
    </div>

    <div v-loading="loading">
      <div v-if="cartItems.length > 0" class="cart-list">
        <div class="cart-header">
          <div class="col-check">
            <el-checkbox :model-value="isAllChecked" @change="handleCheckAll">全选</el-checkbox>
          </div>
          <span class="col-info">商品信息</span>
          <span class="col-price">单价</span>
          <span class="col-qty">数量</span>
          <span class="col-sub">小计</span>
          <span class="col-op">操作</span>
        </div>

        <div
          v-for="item in cartItems"
          :key="item.id"
          class="cart-item"
          :class="{ selected: selectedIds.includes(item.id) }"
        >
          <div class="col-check">
            <el-checkbox :model-value="selectedIds.includes(item.id)" @change="toggleSelect(item.id)" />
          </div>
          <div class="col-info">
            <div class="item-img" @click="$router.push(`/product/${item.productId}`)">
              <img :src="item.product?.image" @error="imgError" />
            </div>
            <div class="item-detail">
              <span class="item-name" @click="$router.push(`/product/${item.productId}`)">{{ item.product?.name }}</span>
              <span class="item-stock">库存 {{ item.product?.stock }}</span>
            </div>
          </div>
          <span class="col-price price-text">¥{{ item.product?.price }}</span>
          <div class="col-qty">
            <el-input-number v-model="item.quantity" :min="1" :max="item.product?.stock || 1" size="small" @change="updateQty(item)" />
          </div>
          <span class="col-sub subtotal">¥{{ ((item.product?.price || 0) * item.quantity).toFixed(2) }}</span>
          <div class="col-op">
            <button class="remove-btn" @click="removeItem(item.id)">
              <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="3 6 5 6 21 6"/>
                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
              </svg>
            </button>
          </div>
        </div>

        <div class="cart-footer">
          <div class="footer-left">
            <el-checkbox :model-value="isAllChecked" @change="handleCheckAll">全选</el-checkbox>
            <button class="text-btn" @click="removeSelected">删除选中</button>
            <button class="text-btn" @click="clearCart">清空购物车</button>
          </div>
          <div class="footer-right">
            <div class="total-info">
              <span class="total-label">已选 <b>{{ selectedIds.length }}</b> 件</span>
              <span class="total-price">¥{{ totalAmount }}</span>
            </div>
            <button class="checkout-btn" :disabled="selectedIds.length === 0" @click="checkout">
              去结算
              <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <line x1="5" y1="12" x2="19" y2="12"/>
                <polyline points="12 5 19 12 12 19"/>
              </svg>
            </button>
          </div>
        </div>
      </div>

      <div v-else class="empty-state">
        <div class="empty-emoji">🐾</div>
        <p class="empty-text">购物车空空如也~</p>
        <p class="empty-sub">快去挑选你喜欢的宠物用品吧</p>
        <button class="go-shop-btn" @click="$router.push('/')">去逛逛</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { cartApi } from '../api'

const router = useRouter()
const cartItems = ref([])
const loading = ref(false)
const selectedIds = ref([])

const isAllChecked = computed(() =>
  cartItems.value.length > 0 && selectedIds.value.length === cartItems.value.length
)

const totalAmount = computed(() => {
  return cartItems.value
    .filter(i => selectedIds.value.includes(i.id))
    .reduce((sum, i) => sum + (i.product?.price || 0) * i.quantity, 0)
    .toFixed(2)
})

const loadCart = async () => {
  loading.value = true
  try {
    cartItems.value = await cartApi.list()
  } finally {
    loading.value = false
  }
}

const handleCheckAll = (val) => {
  selectedIds.value = val ? cartItems.value.map(i => i.id) : []
}

const toggleSelect = (id) => {
  const idx = selectedIds.value.indexOf(id)
  if (idx > -1) {
    selectedIds.value.splice(idx, 1)
  } else {
    selectedIds.value.push(id)
  }
}

const updateQty = async (item) => {
  await cartApi.update(item.id, item.quantity)
}

const removeItem = (id) => {
  ElMessageBox.confirm('确定要删除这件商品吗？', '提示', { type: 'warning' })
    .then(async () => {
      await cartApi.remove(id)
      ElMessage.success('已删除')
      selectedIds.value = selectedIds.value.filter(i => i !== id)
      loadCart()
    })
    .catch(() => {})
}

const removeSelected = async () => {
  if (selectedIds.value.length === 0) return
  await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 件商品吗？`, '提示', { type: 'warning' })
  for (const id of selectedIds.value) {
    await cartApi.remove(id)
  }
  ElMessage.success('已删除选中商品')
  selectedIds.value = []
  loadCart()
}

const clearCart = async () => {
  await ElMessageBox.confirm('确定清空购物车吗？', '提示', { type: 'warning' })
  await cartApi.clear()
  ElMessage.success('购物车已清空')
  selectedIds.value = []
  loadCart()
}

const checkout = () => {
  router.push({ path: '/checkout', query: { ids: selectedIds.value.join(',') } })
}

const imgError = (e) => {
  e.target.style.display = 'none'
}

onMounted(loadCart)
</script>

<style scoped>
.container {
  max-width: 1000px;
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
}

.title-icon {
  font-size: 28px;
}

.item-count {
  font-size: 14px;
  color: var(--paw-ink-3);
  font-weight: 600;
  background: var(--paw-card);
  padding: 6px 14px;
  border-radius: var(--paw-radius-full);
  border: 1px solid var(--paw-border);
}

.cart-list {
  background: var(--paw-card);
  border-radius: var(--paw-radius-lg);
  overflow: hidden;
  box-shadow: var(--paw-shadow-sm);
  border: 1px solid var(--paw-border-light);
}

.cart-header, .cart-item {
  display: flex;
  align-items: center;
  padding: 16px 24px;
  gap: 16px;
}

.cart-header {
  background: var(--paw-surface);
  color: var(--paw-ink-3);
  font-size: 13px;
  font-weight: 600;
  border-bottom: 1px solid var(--paw-border-light);
}

.cart-item {
  border-bottom: 1px solid var(--paw-border-light);
  transition: background var(--paw-fast);
}

.cart-item.selected {
  background: var(--paw-coral-50);
}

.col-check {
  width: 50px;
}

.col-info {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 14px;
}

.item-img {
  width: 72px;
  height: 72px;
  border-radius: var(--paw-radius-md);
  overflow: hidden;
  background: var(--paw-cream);
  cursor: pointer;
  flex-shrink: 0;
  border: 1px solid var(--paw-border-light);
}

.item-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-detail {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.item-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--paw-ink);
  cursor: pointer;
  transition: color var(--paw-fast);
}

.item-name:hover {
  color: var(--paw-coral);
}

.item-stock {
  font-size: 12px;
  color: var(--paw-ink-4);
}

.col-price {
  width: 100px;
  text-align: center;
}

.price-text {
  font-size: 15px;
  color: var(--paw-ink-2);
  font-weight: 600;
}

.col-qty {
  width: 140px;
}

.col-sub {
  width: 120px;
  text-align: center;
}

.subtotal {
  font-size: 17px;
  font-weight: 800;
  color: var(--paw-coral);
}

.col-op {
  width: 60px;
  text-align: center;
}

.remove-btn {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  border: none;
  background: transparent;
  color: var(--paw-ink-4);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--paw-fast);
}

.remove-btn:hover {
  background: #fee;
  color: #f56c6c;
}

.cart-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  background: var(--paw-surface);
  border-top: 1px solid var(--paw-border-light);
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.text-btn {
  background: none;
  border: none;
  color: var(--paw-ink-3);
  font-size: 13px;
  cursor: pointer;
  transition: color var(--paw-fast);
}

.text-btn:hover {
  color: var(--paw-coral);
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 24px;
}

.total-info {
  text-align: right;
}

.total-label {
  font-size: 13px;
  color: var(--paw-ink-3);
}

.total-label b {
  color: var(--paw-coral);
}

.total-price {
  display: block;
  font-size: 28px;
  color: var(--paw-coral);
  font-weight: 900;
}

.checkout-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 14px 32px;
  border-radius: var(--paw-radius-full);
  border: none;
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-coral-light) 100%);
  color: #fff;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  box-shadow: var(--paw-shadow-coral);
  transition: all var(--paw-normal);
}

.checkout-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 14px 32px rgba(255, 107, 74, 0.35);
}

.checkout-btn:disabled {
  background: var(--paw-ink-4);
  cursor: not-allowed;
  box-shadow: none;
}

.empty-state {
  text-align: center;
  padding: 80px 20px;
}

.empty-emoji {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 18px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 8px;
}

.empty-sub {
  font-size: 14px;
  color: var(--paw-ink-3);
  margin-bottom: 24px;
}

.go-shop-btn {
  padding: 12px 32px;
  border-radius: var(--paw-radius-full);
  border: none;
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-coral-light) 100%);
  color: #fff;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  box-shadow: var(--paw-shadow-coral);
  transition: all var(--paw-normal);
}

.go-shop-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 14px 32px rgba(255, 107, 74, 0.35);
}
</style>
