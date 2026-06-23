<template>
  <div class="home">
    <!-- ── Hero Section ── -->
    <section class="hero">
      <!-- 有机形状装饰 -->
      <div class="blob blob-1 animate-blob"></div>
      <div class="blob blob-2 animate-blob" style="animation-delay: -3s"></div>
      <div class="blob blob-3 animate-blob" style="animation-delay: -5s"></div>

      <!-- 浮动爪印 -->
      <div class="floating-paw paw-1">🐾</div>
      <div class="floating-paw paw-2">🐾</div>
      <div class="floating-paw paw-3">🐾</div>

      <div class="hero-inner">
        <div class="hero-text animate-fade-up">
          <div class="hero-badge">
            <span class="badge-dot"></span>
            全场满 99 包邮 · 新人专享 8 折
          </div>
          <h1 class="hero-title">
            给你的萌宠<br>
            <span class="gradient-text">最好的选择</span>
          </h1>
          <p class="hero-desc">
            精选全球优质宠物用品，从粮食到玩具，从护理到出行。<br>
            让每一只毛孩子都健康、快乐、有品质地成长。
          </p>
          <div class="hero-actions">
            <button class="cta-primary" @click="scrollToProducts">
              <span>立即选购</span>
              <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <line x1="5" y1="12" x2="19" y2="12"/>
                <polyline points="12 5 19 12 12 19"/>
              </svg>
            </button>
            <div class="hero-stats">
              <div class="stat">
                <span class="stat-num">500+</span>
                <span class="stat-label">优质商品</span>
              </div>
              <div class="stat-divider"></div>
              <div class="stat">
                <span class="stat-num">10万+</span>
                <span class="stat-label">毛孩子家长</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Hero 视觉卡片 -->
        <div class="hero-visual animate-fade-up delay-2">
          <div class="hero-card hero-card-1 animate-float">
            <div class="card-emoji">🦴</div>
            <div class="card-label">营养主食</div>
            <div class="card-price">¥39 起</div>
          </div>
          <div class="hero-card hero-card-2 animate-float" style="animation-delay: -2s">
            <div class="card-emoji">🎾</div>
            <div class="card-label">趣味玩具</div>
            <div class="card-price">¥19 起</div>
          </div>
          <div class="hero-card hero-card-3 animate-float" style="animation-delay: -1s">
            <div class="card-emoji">🧴</div>
            <div class="card-label">护理美容</div>
            <div class="card-price">¥29 起</div>
          </div>
          <div class="hero-circle">
            <div class="hero-circle-inner">
              <span class="circle-emoji">🐶</span>
              <span class="circle-text">初晴萌宠空间</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ── 特色栏 ── -->
    <section class="features">
      <div class="features-inner">
        <div class="feature-item">
          <div class="feature-icon" style="background: var(--paw-coral-50); color: var(--paw-coral)">
            <svg viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="1" y="3" width="15" height="13"/><polygon points="16 8 20 8 23 11 23 16 16 16 16 8"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/></svg>
          </div>
          <div>
            <div class="feature-title">极速配送</div>
            <div class="feature-desc">24小时内发货</div>
          </div>
        </div>
        <div class="feature-item">
          <div class="feature-icon" style="background: var(--paw-sage-50); color: var(--paw-sage)">
            <svg viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 12l2 2 4-4"/><path d="M21 12c0 4.97-4.03 9-9 9s-9-4.03-9-9 4.03-9 9-9c1.66 0 3.22.45 4.56 1.24"/></svg>
          </div>
          <div>
            <div class="feature-title">正品保证</div>
            <div class="feature-desc">品牌授权认证</div>
          </div>
        </div>
        <div class="feature-item">
          <div class="feature-icon" style="background: var(--paw-amber-50); color: var(--paw-amber)">
            <svg viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 3h18v18H3z"/><path d="M3 9h18"/><path d="M9 21V9"/></svg>
          </div>
          <div>
            <div class="feature-title">七天退换</div>
            <div class="feature-desc">无忧售后保障</div>
          </div>
        </div>
        <div class="feature-item">
          <div class="feature-icon" style="background: var(--paw-sky-50); color: var(--paw-sky)">
            <svg viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
          </div>
          <div>
            <div class="feature-title">宠物顾问</div>
            <div class="feature-desc">1对1专业咨询</div>
          </div>
        </div>
      </div>
    </section>

    <!-- ── 为你推荐 ── -->
    <div class="container" v-if="recommendedProducts.length > 0">
      <div class="recommend-section">
        <div class="recommend-header">
          <div class="recommend-title-group">
            <h2>🎯 {{ userStore.isLogin ? '为你推荐' : '热门推荐' }}</h2>
            <span class="section-sub">{{ userStore.isLogin ? '根据你的喜好精选' : '大家都在买的好物' }}</span>
          </div>
          <button class="refresh-btn" @click="refreshRecommendations" :disabled="refreshing">
            <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" :class="{ spinning: refreshing }">
              <polyline points="23 4 23 10 17 10"></polyline>
              <polyline points="1 20 1 14 7 14"></polyline>
              <path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15"></path>
            </svg>
            换一批
          </button>
        </div>
        <div class="recommend-scroll-wrapper">
          <div class="recommend-scroll">
            <div
              v-for="(product, index) in recommendedProducts"
              :key="product.id"
              class="recommend-card animate-fade-up"
              :style="{ animationDelay: `${index * 0.06}s` }"
              @click="$router.push(`/product/${product.id}`)"
            >
              <div class="recommend-img">
                <img :src="product.image" :alt="product.name" @error="imgError" />
                <div v-if="product.stock <= 0" class="sold-out-overlay">
                  <span class="sold-out-text">已售罄</span>
                </div>
                <div v-if="product.rating && product.rating >= 4.5" class="recommend-badge">好评</div>
              </div>
              <div class="recommend-info">
                <h3 class="recommend-name">{{ product.name }}</h3>
                <div class="recommend-meta" v-if="product.rating">
                  <span class="recommend-rating">
                    <svg viewBox="0 0 24 24" width="12" height="12" fill="currentColor"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>
                    {{ product.rating.toFixed(1) }}
                  </span>
                </div>
                <div class="recommend-bottom">
                  <div class="recommend-price-group">
                    <span class="recommend-price-symbol">¥</span>
                    <span class="recommend-price-num">{{ product.discountPrice || product.price }}</span>
                    <span v-if="product.discountPrice && product.discountPrice < product.price" class="recommend-original-price">¥{{ product.price }}</span>
                  </div>
                  <button class="recommend-cart-btn" :disabled="product.stock <= 0" @click.stop="addToCart(product)">
                    <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                      <line x1="12" y1="5" x2="12" y2="19"/>
                      <line x1="5" y1="12" x2="19" y2="12"/>
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ── 分类导航 ── -->
    <div class="container">
      <div class="category-bar">
        <div
          v-for="cat in categories"
          :key="cat.id"
          class="category-pill"
          :class="{ active: activeCategory === cat.id }"
          @click="selectCategory(cat.id)"
        >
          <span class="pill-icon">{{ cat.icon || '🐾' }}</span>
          <span class="pill-name">{{ cat.name }}</span>
        </div>
      </div>

      <!-- 商品列表 -->
      <div id="products" class="products-section">
        <div class="section-header">
          <h2>{{ activeCategory ? categoryName : (keyword ? `搜索「${keyword}」` : '全部商品') }}</h2>
          <div class="section-count" v-if="total > 0">共 {{ total }} 件商品</div>
        </div>

        <div v-loading="loading" class="products-grid">
          <div
            v-for="(product, index) in products"
            :key="product.id"
            class="product-card animate-fade-up"
            :style="{ animationDelay: `${index * 0.05}s` }"
            @click="$router.push(`/product/${product.id}`)"
          >
            <div class="product-img">
              <img :src="product.image" :alt="product.name" @error="imgError" />
              <div v-if="product.stock <= 0" class="sold-out-overlay">
                <span class="sold-out-text">已售罄</span>
              </div>
              <div v-else-if="product.stock <= 10" class="low-stock-tag">
                仅剩 {{ product.stock }} 件
              </div>
            </div>
            <div class="product-info">
              <h3 class="product-name">{{ product.name }}</h3>
              <p class="product-desc">{{ product.description }}</p>
              <div class="product-bottom">
                <div class="price-group">
                  <span class="price-symbol">¥</span>
                  <span class="price-num">{{ product.discountPrice || product.price }}</span>
                  <span v-if="product.discountPrice && product.discountPrice < product.price" class="original-price">¥{{ product.price }}</span>
                </div>
                <button
                  class="add-cart-btn"
                  :disabled="product.stock <= 0"
                  @click.stop="addToCart(product)"
                >
                  <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                    <line x1="12" y1="5" x2="12" y2="19"/>
                    <line x1="5" y1="12" x2="19" y2="12"/>
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>

        <el-empty v-if="!loading && products.length === 0" description="暂无商品" />

        <div class="pagination" v-if="total > 0">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="pageSize"
            v-model:current-page="currentPage"
            @current-change="loadProducts"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { productApi, categoryApi, cartApi, recommendApi } from '../api'
import { useUserStore } from '../store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const categories = ref([])
const products = ref([])
const recommendedProducts = ref([])
const refreshing = ref(false)
const loading = ref(false)
const activeCategory = ref(null)
const keyword = ref('')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

const categoryName = computed(() => {
  const c = categories.value.find(i => i.id === activeCategory.value)
  return c ? c.name : '全部商品'
})

const loadCategories = async () => {
  categories.value = await categoryApi.list()
}

const loadProducts = async () => {
  loading.value = true
  try {
    const data = await productApi.list({
      categoryId: activeCategory.value,
      keyword: keyword.value,
      page: currentPage.value - 1,
      size: pageSize.value
    })
    products.value = data.content || []
    total.value = data.totalElements || 0
  } finally {
    loading.value = false
  }
}

const selectCategory = (id) => {
  activeCategory.value = activeCategory.value === id ? null : id
  keyword.value = ''
  currentPage.value = 1
  loadProducts()
}

const scrollToProducts = () => {
  document.getElementById('products')?.scrollIntoView({ behavior: 'smooth' })
}

const imgError = (e) => {
  e.target.style.display = 'none'
  e.target.parentElement.classList.add('img-fallback')
}

const addToCart = async (product) => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  await cartApi.add({ productId: product.id, quantity: 1 })
  ElMessage.success('已加入购物车')
}

watch(() => route.query.keyword, (val) => {
  keyword.value = val || ''
  activeCategory.value = null
  currentPage.value = 1
  loadProducts()
})

const loadRecommendations = async () => {
  try {
    if (userStore.isLogin) {
      // 已登录：个性化推荐
      recommendedProducts.value = await recommendApi.forMe(6)
    } else {
      // 未登录：热门商品推荐
      recommendedProducts.value = await recommendApi.hot(6)
    }
  } catch (e) {
    recommendedProducts.value = []
  }
}

const refreshRecommendations = async () => {
  if (refreshing.value) return
  refreshing.value = true
  try {
    if (userStore.isLogin) {
      // 已登录：请求更多推荐（size=12），随机取6个
      const all = await recommendApi.forMe(12)
      recommendedProducts.value = shuffleArray(all).slice(0, 6)
    } else {
      // 未登录：请求更多热门（size=12），随机取6个
      const all = await recommendApi.hot(12)
      recommendedProducts.value = shuffleArray(all).slice(0, 6)
    }
  } catch (e) {
    // 静默失败
  } finally {
    refreshing.value = false
  }
}

const shuffleArray = (arr) => {
  const a = [...arr]
  for (let i = a.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [a[i], a[j]] = [a[j], a[i]]
  }
  return a
}

onMounted(async () => {
  await loadCategories()
  keyword.value = route.query.keyword || ''
  loadProducts()
  loadRecommendations()
})
</script>

<style scoped>
/* ── Hero ── */
.hero {
  position: relative;
  overflow: hidden;
  background: linear-gradient(180deg, var(--paw-cream) 0%, var(--paw-bg) 100%);
  padding: 60px 24px 40px;
}

.blob {
  position: absolute;
  border-radius: 50% 40% 60% 50% / 50% 60% 40% 50%;
  filter: blur(40px);
  pointer-events: none;
}

.blob-1 {
  width: 300px;
  height: 300px;
  background: var(--paw-coral);
  opacity: 0.12;
  top: -60px;
  right: 10%;
}

.blob-2 {
  width: 240px;
  height: 240px;
  background: var(--paw-sage);
  opacity: 0.1;
  bottom: -40px;
  left: 5%;
}

.blob-3 {
  width: 180px;
  height: 180px;
  background: var(--paw-amber);
  opacity: 0.1;
  top: 30%;
  left: 40%;
}

.floating-paw {
  position: absolute;
  font-size: 24px;
  opacity: 0.15;
  animation: float 6s ease-in-out infinite;
}

.paw-1 { top: 15%; left: 8%; animation-delay: 0s; }
.paw-2 { top: 60%; right: 12%; animation-delay: -2s; font-size: 32px; }
.paw-3 { top: 25%; right: 30%; animation-delay: -4s; font-size: 20px; }

.hero-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 40px;
  position: relative;
  z-index: 1;
}

.hero-text {
  flex: 1;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: rgba(255, 107, 74, 0.08);
  border: 1px solid rgba(255, 107, 74, 0.15);
  color: var(--paw-coral);
  padding: 6px 16px;
  border-radius: var(--paw-radius-full);
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 20px;
}

.badge-dot {
  width: 6px;
  height: 6px;
  background: var(--paw-coral);
  border-radius: 50%;
  animation: float 2s ease-in-out infinite;
}

.hero-title {
  font-size: 52px;
  font-weight: 900;
  line-height: 1.15;
  color: var(--paw-ink);
  margin-bottom: 20px;
  letter-spacing: -1px;
}

.gradient-text {
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-amber) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-desc {
  font-size: 16px;
  color: var(--paw-ink-2);
  line-height: 1.7;
  margin-bottom: 32px;
}

.hero-actions {
  display: flex;
  align-items: center;
  gap: 32px;
}

.cta-primary {
  display: flex;
  align-items: center;
  gap: 8px;
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-coral-light) 100%);
  color: #fff;
  border: none;
  padding: 14px 32px;
  border-radius: var(--paw-radius-full);
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  box-shadow: var(--paw-shadow-coral);
  transition: all var(--paw-normal);
}

.cta-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 16px 36px rgba(255, 107, 74, 0.35);
}

.hero-stats {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat {
  display: flex;
  flex-direction: column;
}

.stat-num {
  font-size: 24px;
  font-weight: 800;
  color: var(--paw-ink);
}

.stat-label {
  font-size: 12px;
  color: var(--paw-ink-3);
}

.stat-divider {
  width: 1px;
  height: 32px;
  background: var(--paw-border);
}

/* ── Hero Visual ── */
.hero-visual {
  position: relative;
  width: 420px;
  height: 380px;
  flex-shrink: 0;
}

.hero-circle {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-amber) 100%);
  border-radius: 50% 45% 55% 50% / 50% 55% 45% 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: var(--paw-shadow-lg), inset 0 -20px 40px rgba(0,0,0,0.08);
  animation: blob-morph 8s ease-in-out infinite;
}

.hero-circle-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.circle-emoji {
  font-size: 64px;
}

.circle-text {
  color: #fff;
  font-size: 14px;
  font-weight: 800;
  letter-spacing: 1px;
}

.hero-card {
  position: absolute;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  border-radius: var(--paw-radius-md);
  padding: 14px 18px;
  box-shadow: var(--paw-shadow-md);
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 110px;
}

.hero-card-1 {
  top: 10%;
  left: 0;
}

.hero-card-2 {
  bottom: 15%;
  left: 5%;
}

.hero-card-3 {
  top: 20%;
  right: 0;
}

.card-emoji {
  font-size: 28px;
}

.card-label {
  font-size: 13px;
  font-weight: 700;
  color: var(--paw-ink);
}

.card-price {
  font-size: 15px;
  font-weight: 800;
  color: var(--paw-coral);
}

/* ── Features ── */
.features {
  padding: 0 24px;
  margin-top: -20px;
  position: relative;
  z-index: 2;
}

.features-inner {
  max-width: 1200px;
  margin: 0 auto;
  background: var(--paw-card);
  border-radius: var(--paw-radius-lg);
  padding: 24px 32px;
  display: flex;
  justify-content: space-around;
  gap: 24px;
  box-shadow: var(--paw-shadow-md);
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.feature-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.feature-title {
  font-size: 14px;
  font-weight: 700;
  color: var(--paw-ink);
}

.feature-desc {
  font-size: 12px;
  color: var(--paw-ink-3);
}

/* ── Container ── */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

/* ── Category Bar ── */
.category-bar {
  display: flex;
  gap: 10px;
  overflow-x: auto;
  padding: 8px 0 16px;
  margin-bottom: 8px;
  scrollbar-width: none;
}

.category-bar::-webkit-scrollbar {
  display: none;
}

.category-pill {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 18px;
  border-radius: var(--paw-radius-full);
  background: var(--paw-card);
  border: 1px solid var(--paw-border);
  cursor: pointer;
  white-space: nowrap;
  transition: all var(--paw-fast);
  box-shadow: var(--paw-shadow-xs);
}

.category-pill:hover {
  border-color: var(--paw-coral);
  transform: translateY(-2px);
  box-shadow: var(--paw-shadow-sm);
}

.category-pill.active {
  background: linear-gradient(135deg, var(--paw-coral) 0%, var(--paw-coral-light) 100%);
  border-color: transparent;
  color: #fff;
  box-shadow: var(--paw-shadow-coral);
}

.pill-icon {
  font-size: 18px;
}

.pill-name {
  font-size: 14px;
  font-weight: 600;
}

/* ── Section Header ── */
.section-header {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  margin: 24px 0 20px;
}

.section-header h2 {
  font-size: 24px;
  font-weight: 800;
  color: var(--paw-ink);
  position: relative;
  padding-left: 16px;
}

.section-header h2::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 24px;
  background: linear-gradient(180deg, var(--paw-coral), var(--paw-amber));
  border-radius: var(--paw-radius-full);
}

.section-count {
  font-size: 13px;
  color: var(--paw-ink-3);
  font-weight: 500;
}

/* ── Products Grid ── */
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
  min-height: 200px;
}

.product-card {
  background: var(--paw-card);
  border-radius: var(--paw-radius-md);
  overflow: hidden;
  cursor: pointer;
  transition: all var(--paw-normal);
  box-shadow: var(--paw-shadow-xs);
  border: 1px solid var(--paw-border-light);
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: var(--paw-shadow-lg);
  border-color: var(--paw-coral-100);
}

.product-img {
  width: 100%;
  height: 220px;
  background: linear-gradient(135deg, var(--paw-cream), var(--paw-coral-50));
  position: relative;
  overflow: hidden;
}

.product-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--paw-slow);
}

.product-card:hover .product-img img {
  transform: scale(1.08);
}

.product-img.img-fallback::before {
  content: '🐾';
  font-size: 60px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.sold-out-overlay {
  position: absolute;
  inset: 0;
  background: rgba(45, 42, 38, 0.55);
  display: flex;
  align-items: center;
  justify-content: center;
}

.sold-out-text {
  color: #fff;
  padding: 6px 20px;
  border-radius: var(--paw-radius-full);
  font-size: 14px;
  font-weight: 700;
  background: rgba(255, 107, 74, 0.9);
}

.low-stock-tag {
  position: absolute;
  top: 12px;
  right: 12px;
  background: var(--paw-amber);
  color: #fff;
  padding: 4px 12px;
  border-radius: var(--paw-radius-full);
  font-size: 11px;
  font-weight: 700;
  box-shadow: var(--paw-shadow-sm);
}

.product-info {
  padding: 16px;
}

.product-name {
  font-size: 15px;
  font-weight: 700;
  color: var(--paw-ink);
  margin-bottom: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 42px;
  line-height: 1.4;
}

.product-desc {
  font-size: 12px;
  color: var(--paw-ink-3);
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.price-group {
  display: flex;
  align-items: baseline;
  gap: 2px;
}

.price-symbol {
  font-size: 13px;
  font-weight: 700;
  color: var(--paw-coral);
}

.price-num {
  font-size: 22px;
  font-weight: 900;
  color: var(--paw-coral);
}

.add-cart-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: none;
  background: linear-gradient(135deg, var(--paw-coral), var(--paw-coral-light));
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all var(--paw-fast);
  box-shadow: var(--paw-shadow-coral);
}

.add-cart-btn:hover:not(:disabled) {
  transform: scale(1.15) rotate(90deg);
  box-shadow: 0 8px 20px rgba(255, 107, 74, 0.4);
}

.add-cart-btn:active:not(:disabled) {
  transform: scale(0.95);
}

.add-cart-btn:disabled {
  background: var(--paw-ink-4);
  cursor: not-allowed;
  box-shadow: none;
  opacity: 0.5;
}

.original-price {
  font-size: 13px;
  color: var(--paw-ink-4);
  text-decoration: line-through;
  margin-left: 6px;
}

.recommend-section {
  margin: 32px 0;
}

.recommend-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.recommend-title-group {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.recommend-title-group h2 {
  font-size: 24px;
  font-weight: 800;
  color: var(--paw-ink);
  position: relative;
  padding-left: 16px;
}

.recommend-title-group h2::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 24px;
  background: linear-gradient(180deg, var(--paw-coral), var(--paw-amber));
  border-radius: var(--paw-radius-full);
}

.refresh-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border: 1px solid var(--paw-border-light);
  border-radius: var(--paw-radius-full);
  background: var(--paw-card);
  color: var(--paw-ink-2);
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--paw-fast);
  white-space: nowrap;
}

.refresh-btn:hover:not(:disabled) {
  border-color: var(--paw-coral);
  color: var(--paw-coral);
  background: var(--paw-coral-50);
}

.refresh-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.refresh-btn svg.spinning {
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 横向滚动容器 */
.recommend-scroll-wrapper {
  width: 100%;
  overflow: hidden;
}

.recommend-scroll {
  display: flex;
  gap: 16px;
  overflow-x: auto;
  overflow-y: hidden;
  scroll-snap-type: x mandatory;
  scroll-behavior: smooth;
  padding: 4px 0 12px 0;
  -ms-overflow-style: none;
  scrollbar-width: none;
  touch-action: pan-x;
}

.recommend-scroll::-webkit-scrollbar {
  display: none;
}

/* 推荐卡片 */
.recommend-card {
  flex: 0 0 180px;
  min-width: 180px;
  background: var(--paw-card);
  border-radius: var(--paw-radius-lg);
  overflow: hidden;
  cursor: pointer;
  transition: all var(--paw-normal);
  box-shadow: var(--paw-shadow-xs);
  border: 1px solid var(--paw-border-light);
  scroll-snap-align: start;
}

.recommend-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--paw-shadow-lg);
  border-color: var(--paw-coral-100);
}

.recommend-img {
  width: 100%;
  height: 180px;
  background: linear-gradient(135deg, var(--paw-cream), var(--paw-coral-50));
  position: relative;
  overflow: hidden;
}

.recommend-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--paw-slow);
}

.recommend-card:hover .recommend-img img {
  transform: scale(1.08);
}

.recommend-img.img-fallback::before {
  content: '🐾';
  font-size: 48px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.recommend-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  background: linear-gradient(135deg, var(--paw-coral), var(--paw-amber));
  color: #fff;
  font-size: 11px;
  font-weight: 700;
  padding: 3px 10px;
  border-radius: var(--paw-radius-full);
  box-shadow: 0 2px 8px rgba(255, 107, 74, 0.3);
}

.recommend-info {
  padding: 12px;
}

.recommend-name {
  font-size: 13px;
  font-weight: 600;
  color: var(--paw-ink);
  margin: 0 0 6px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 36px;
  line-height: 1.4;
}

.recommend-meta {
  margin-bottom: 8px;
}

.recommend-rating {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  font-size: 12px;
  font-weight: 600;
  color: var(--paw-amber);
}

.recommend-rating svg {
  color: var(--paw-amber);
}

.recommend-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.recommend-price-group {
  display: flex;
  align-items: baseline;
  gap: 2px;
}

.recommend-price-symbol {
  font-size: 12px;
  font-weight: 700;
  color: var(--paw-coral);
}

.recommend-price-num {
  font-size: 18px;
  font-weight: 900;
  color: var(--paw-coral);
}

.recommend-original-price {
  font-size: 11px;
  color: var(--paw-ink-4);
  text-decoration: line-through;
  margin-left: 4px;
}

.recommend-cart-btn {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  border: none;
  background: linear-gradient(135deg, var(--paw-coral), var(--paw-coral-light));
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all var(--paw-fast);
  box-shadow: var(--paw-shadow-coral);
  flex-shrink: 0;
}

.recommend-cart-btn:hover:not(:disabled) {
  transform: scale(1.15) rotate(90deg);
  box-shadow: 0 6px 16px rgba(255, 107, 74, 0.4);
}

.recommend-cart-btn:disabled {
  background: var(--paw-ink-4);
  cursor: not-allowed;
  box-shadow: none;
}

.section-sub {
  font-size: 13px;
  color: var(--paw-ink-3);
}

.pagination {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

/* ── Responsive ── */
@media (max-width: 900px) {
  .hero-inner {
    flex-direction: column;
    text-align: center;
  }
  .hero-title {
    font-size: 36px;
  }
  .hero-visual {
    display: none;
  }
  .hero-actions {
    flex-direction: column;
    gap: 20px;
  }
  .features-inner {
    flex-direction: column;
    gap: 16px;
  }
}
</style>
