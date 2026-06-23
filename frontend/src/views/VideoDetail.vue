<template>
  <div class="video-detail-page">
    <el-page-header @back="$router.back()" title="返回视频列表" />
    <div class="page-card" v-loading="loading">
      <template v-if="video">
        <div class="video-player">
          <div class="player-placeholder">
            <svg viewBox="0 0 24 24" width="64" height="64" fill="white"><polygon points="8,5 19,12 8,19"/></svg>
            <p>视频播放区域</p>
            <span>💡 生产环境可通过video标签或播放器SDK加载实际视频</span>
          </div>
        </div>
        <h1 class="video-title">{{ video.title }}</h1>
        <div class="video-meta">
          <span>👁 {{ video.viewCount }} 次观看</span>
          <span class="meta-divider">|</span>
          <el-tag size="small">{{ categoryName(video.category) }}</el-tag>
        </div>
        <el-card v-if="video.description" class="desc-card">
          <p>{{ video.description }}</p>
        </el-card>
        <div v-if="video.productId" class="product-link">
          <el-button type="primary" @click="$router.push(`/product/${video.productId}`)">查看关联商品 →</el-button>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { videoApi } from '../api'

const route = useRoute()
const video = ref(null)
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try { video.value = await videoApi.detail(route.params.id) } catch (e) {}
  loading.value = false
})

const categoryName = (c) => {
  const map = { PET_PLAY: '萌宠日常', PET_FOOD: '喂养指南', PET_CARE: '宠物护理', PRODUCT_INTRO: '商品介绍' }
  return map[c] || c
}
</script>

<style scoped>
.video-detail-page { padding: 24px; max-width: 900px; margin: 0 auto; }
.video-player { margin: 20px 0; border-radius: 12px; overflow: hidden; }
.player-placeholder { width: 100%; height: 420px; background: linear-gradient(135deg, #1a1a2e, #16213e, #0f3460); display: flex; flex-direction: column; align-items: center; justify-content: center; color: white; gap: 12px; }
.player-placeholder p { font-size: 18px; }
.player-placeholder span { font-size: 12px; opacity: 0.6; }
.video-title { font-size: 24px; margin: 16px 0 8px; }
.video-meta { color: #999; font-size: 14px; margin-bottom: 20px; display: flex; align-items: center; gap: 8px; }
.meta-divider { color: #ddd; }
.desc-card { margin-bottom: 20px; }
.product-link { margin-bottom: 20px; }
</style>
