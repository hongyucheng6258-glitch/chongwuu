<template>
  <div class="container" v-loading="loading">
    <div v-if="product" class="detail animate-fade-up">
      <!-- 面包屑 -->
      <div class="breadcrumb">
        <span @click="$router.push('/')">首页</span>
        <span class="separator">/</span>
        <span class="current">{{ product.name }}</span>
      </div>

      <div class="detail-card">
        <div class="detail-left">
          <div class="img-box">
            <img :src="product.image" :alt="product.name" @error="imgError" />
            <div class="img-deco"></div>
          </div>
        </div>

        <div class="detail-right">
          <div class="product-badge" v-if="product.stock > 0 && product.stock <= 10">
            <span class="badge-icon">🔥</span> 仅剩 {{ product.stock }} 件
          </div>

          <h1 class="title">{{ product.name }}</h1>

          <p class="desc">{{ product.description }}</p>

          <div class="price-card">
            <div class="price-row">
              <span class="price-symbol">¥</span>
              <span class="price-num">{{ product.discountPrice || product.price }}</span>
              <span v-if="product.discountPrice && product.discountPrice < product.price" class="original-price">¥{{ product.price }}</span>
              <el-tag v-if="product.discountRate && product.discountRate < 1" type="danger" size="small" style="margin-left: 10px;">{{ Math.round(product.discountRate * 100) }}折</el-tag>
            </div>
            <div v-if="userStore.isLogin && userStore.memberLevel > 0 && product.discountPrice && product.discountPrice < product.price" class="member-price-tip">
              <span class="member-tag">{{ userStore.memberLevelName }}专享价</span>
              <span class="member-save">已为您节省 ¥{{ (product.price - product.discountPrice).toFixed(2) }}</span>
            </div>
            <div class="stock-tag" v-if="product.stock > 0">
              <span class="dot"></span> 库存充足 · {{ product.stock }} 件
            </div>
            <div class="stock-tag out" v-else>
              <span class="dot"></span> 已售罄
            </div>
          </div>

          <div class="info-rows">
            <div class="info-row">
              <span class="info-label">配送</span>
              <span class="info-value">下单后24小时内发货 · 满99包邮</span>
            </div>
            <div class="info-row">
              <span class="info-label">保障</span>
              <span class="info-value">正品保证 · 七天无理由退换</span>
            </div>
          </div>

          <div class="quantity">
            <span class="qty-label">数量</span>
            <el-input-number v-model="quantity" :min="1" :max="product.stock || 1" :disabled="product.stock <= 0" />
          </div>

          <div class="actions">
            <button
              class="btn-cart"
              :disabled="product.stock <= 0"
              @click="addToCart"
            >
              <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="9" cy="21" r="1"/>
                <circle cx="20" cy="21" r="1"/>
                <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
              </svg>
              加入购物车
            </button>
            <button
              class="btn-buy"
              :disabled="product.stock <= 0"
              @click="buyNow"
            >
              立即购买
            </button>
          </div>
        </div>
      </div>

      <!-- 视频展示 -->
      <div v-if="product.videoId || videos.length > 0" class="section">
        <h3 class="section-title">📹 产品视频</h3>
        <div class="video-inline" v-if="product.videoId">
          <el-button type="primary" @click="$router.push(`/video/${product.videoId}`)">🎬 查看产品视频介绍</el-button>
        </div>
      </div>

      <!-- 商品评价 -->
      <div class="section">
        <h3 class="section-title">⭐ 商品评价 ({{ reviews.length }})</h3>
        <div v-if="reviews.length > 0" class="reviews-list">
          <div v-for="r in reviews" :key="r.id" class="review-item">
            <div class="review-header">
              <el-rate :model-value="r.rating" disabled size="small" />
              <span class="review-time">{{ r.createdAt ? r.createdAt.substring(0, 10) : '' }}</span>
            </div>
            <p class="review-content">{{ r.content || '用户未填写评价内容' }}</p>
          </div>
        </div>
        <el-empty v-else description="暂无评价" :image-size="60" />
      </div>

      <!-- 相似推荐 -->
      <div v-if="similarProducts.length > 0" class="section similar-section">
        <h3 class="section-title">🛍️ 相似推荐</h3>
        <div class="similar-grid">
          <div
            v-for="(item, index) in similarProducts"
            :key="item.id"
            class="similar-card animate-fade-up"
            :style="{ animationDelay: `${index * 0.05}s` }"
            @click="$router.push(`/product/${item.id}`)"
          >
            <div class="similar-img">
              <img :src="item.image" :alt="item.name" @error="imgError" />
            </div>
            <div class="similar-info">
              <h4 class="similar-name">{{ item.name }}</h4>
              <div class="similar-price">
                <span class="similar-price-symbol">¥</span>
                <span class="similar-price-num">{{ item.discountPrice || item.price }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { productApi, cartApi, reviewApi, videoApi, recommendApi } from '../api'
import { useUserStore } from '../store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const product = ref(null)
const quantity = ref(1)
const loading = ref(true)
const reviews = ref([])
const videos = ref([])
const similarProducts = ref([])

const loadProduct = async () => {
  loading.value = true
  try {
    product.value = await productApi.detail(route.params.id)
    // 并行加载评价、视频和相似推荐
    const [revs, vids, sim] = await Promise.all([
      reviewApi.byProduct(route.params.id).catch(() => []),
      videoApi.byProduct(route.params.id).catch(() => []),
      recommendApi.similar(route.params.id, 4).catch(() => [])
    ])
    reviews.value = revs
    videos.value = vids
    similarProducts.value = sim
  } finally {
    loading.value = false
  }
}

const checkLogin = () => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return false
  }
  return true
}

const addToCart = async () => {
  if (!checkLogin()) return
  await cartApi.add({ productId: product.value.id, quantity: quantity.value })
  ElMessage.success('已加入购物车')
}

const buyNow = async () => {
  if (!checkLogin()) return
  await cartApi.add({ productId: product.value.id, quantity: quantity.value })
  router.push('/cart')
}

const imgError = (e) => {
  e.target.style.display = 'none'
  e.target.parentElement.classList.add('img-fallback')
}

onMounted(loadProduct)
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  min-height: 400px;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
  font-size: 13px;
  color: var(--paw-ink-3);
}

.breadcrumb span {
  cursor: pointer;
  transition: color var(--paw-fast);
}

.breadcrumb span:hover {
  color: var(--paw-coral);
}

.separator {
  color: var(--paw-ink-4) !important;
  cursor: default !important;
}

.current {
  color: var(--paw-ink) !important;
  font-weight: 600;
  cursor: default !important;
}

.detail-card {
  display: flex;
  gap: 40px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-xl);
  padding: 32px;
  box-shadow: var(--paw-shadow-md);
  border: 1px solid var(--paw-border-light);
}

.detail-left {
  flex: 0 0 440px;
}

.img-box {
  width: 440px;
  height: 440px;
  background: linear-gradient(135deg, var(--paw-cream), var(--paw-coral-50));
  border-radius: var(--paw-radius-lg);
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.img-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.img-box.img-fallback::before {
  content: '🐾';
  font-size: 120px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.img-deco {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 80px;
  height: 80px;
  background: radial-gradient(circle, rgba(255,107,74,0.08) 0%, transparent 70%);
  border-radius: 50%;
  pointer-events: none;
}

.detail-right {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.product-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: var(--paw-amber-50);
  color: var(--paw-amber);
  padding: 6px 14px;
  border-radius: var(--paw-radius-full);
  font-size: 12px;
  font-weight: 700;
  margin-bottom: 12px;
  width: fit-content;
}

.title {
  font-size: 28px;
  font-weight: 800;
  color: var(--paw-ink);
  margin-bottom: 10px;
  line-height: 1.3;
}

.desc {
  font-size: 14px;
  color: var(--paw-ink-3);
  line-height: 1.7;
  margin-bottom: 24px;
}

.price-card {
  background: linear-gradient(135deg, var(--paw-coral-50) 0%, var(--paw-amber-50) 100%);
  border-radius: var(--paw-radius-lg);
  padding: 24px;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.price-row {
  display: flex;
  align-items: baseline;
  gap: 2px;
}

.member-price-tip {
  margin-top: 10px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.member-tag {
  font-size: 12px;
  font-weight: 600;
  color: var(--paw-coral);
  background: rgba(255, 107, 74, 0.1);
  padding: 2px 8px;
  border-radius: 4px;
}

.member-save {
  font-size: 12px;
  color: var(--paw-ink-3);
}

.price-symbol {
  font-size: 20px;
  font-weight: 800;
  color: var(--paw-coral);
}

.price-num {
  font-size: 40px;
  font-weight: 900;
  color: var(--paw-coral);
  letter-spacing: -1px;
}

.stock-tag {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 600;
  color: var(--paw-sage);
}

.stock-tag .dot {
  width: 8px;
  height: 8px;
  background: var(--paw-sage);
  border-radius: 50%;
}

.stock-tag.out {
  color: var(--paw-ink-3);
}

.stock-tag.out .dot {
  background: var(--paw-ink-4);
}

.info-rows {
  margin-bottom: 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 16px;
}

.info-label {
  font-size: 13px;
  color: var(--paw-ink-3);
  font-weight: 600;
  width: 40px;
  flex-shrink: 0;
}

.info-value {
  font-size: 13px;
  color: var(--paw-ink-2);
}

.quantity {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 28px;
}

.qty-label {
  font-size: 14px;
  color: var(--paw-ink-2);
  font-weight: 600;
}

.actions {
  display: flex;
  gap: 14px;
  margin-top: auto;
}

.btn-cart {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 14px;
  border-radius: var(--paw-radius-full);
  border: 2px solid var(--paw-coral);
  background: transparent;
  color: var(--paw-coral);
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  transition: all var(--paw-normal);
}

.btn-cart:hover:not(:disabled) {
  background: var(--paw-coral-50);
  transform: translateY(-2px);
}

.btn-cart:disabled {
  border-color: var(--paw-ink-4);
  color: var(--paw-ink-4);
  cursor: not-allowed;
}

.btn-buy {
  flex: 1;
  padding: 14px;
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

.btn-buy:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 14px 32px rgba(255, 107, 74, 0.35);
}

.btn-buy:disabled {
  background: var(--paw-ink-4);
  cursor: not-allowed;
  box-shadow: none;
}

@media (max-width: 768px) {
  .detail-card {
    flex-direction: column;
    padding: 20px;
  }
  .detail-left {
    flex: none;
  }
  .img-box {
    width: 100%;
    height: 300px;
  }
}

.section { margin-top: 32px; background: white; border-radius: 12px; padding: 24px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.section-title { font-size: 18px; margin: 0 0 16px; }
.video-inline { padding: 16px 0; }
.reviews-list { display: flex; flex-direction: column; gap: 16px; }
.review-item { border-bottom: 1px solid #f0f0f0; padding-bottom: 12px; }
.review-item:last-child { border-bottom: none; }
.review-header { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.review-time { font-size: 12px; color: #999; }
.review-content { margin: 0; font-size: 14px; color: #555; line-height: 1.6; }

/* ── 相似推荐 ── */
.similar-section { margin-top: 32px; }
.similar-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}
.similar-card {
  background: var(--paw-card);
  border-radius: var(--paw-radius-lg);
  overflow: hidden;
  cursor: pointer;
  border: 1px solid var(--paw-border-light);
  transition: all var(--paw-normal);
}
.similar-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--paw-shadow-md);
}
.similar-img {
  width: 100%;
  height: 160px;
  overflow: hidden;
  background: linear-gradient(135deg, var(--paw-cream), var(--paw-coral-50));
}
.similar-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.similar-img.img-fallback::before {
  content: '🐾';
  font-size: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}
.similar-info {
  padding: 12px;
}
.similar-name {
  font-size: 13px;
  font-weight: 600;
  color: var(--paw-ink);
  margin: 0 0 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.similar-price {
  display: flex;
  align-items: baseline;
  gap: 1px;
}
.similar-price-symbol {
  font-size: 12px;
  font-weight: 700;
  color: var(--paw-coral);
}
.similar-price-num {
  font-size: 18px;
  font-weight: 800;
  color: var(--paw-coral);
}

@media (max-width: 768px) {
  .similar-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
