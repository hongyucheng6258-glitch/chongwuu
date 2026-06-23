<template>
  <div class="video-detail-page">
    <el-page-header @back="$router.back()" title="返回视频列表" />
    <div class="page-card" v-loading="loading">
      <template v-if="video">
        <!-- 视频播放器 -->
        <div class="video-player-wrapper">
          <video
            v-if="video.videoUrl"
            ref="videoPlayer"
            class="video-player"
            :poster="video.coverUrl"
            controls
            preload="metadata"
            @play="onPlay"
          >
            <source :src="video.videoUrl" type="video/mp4" />
            您的浏览器不支持视频播放，请升级浏览器。
          </video>
          <div v-else class="player-placeholder">
            <svg viewBox="0 0 24 24" width="64" height="64" fill="white"><polygon points="8,5 19,12 8,19"/></svg>
            <p>视频播放区域</p>
            <span>暂无视频源</span>
          </div>
        </div>

        <!-- 视频信息 -->
        <div class="video-header">
          <h1 class="video-title">{{ video.title }}</h1>
          <div class="video-meta">
            <span class="meta-item">
              <el-icon><View /></el-icon> {{ video.viewCount }} 次观看
            </span>
            <span class="meta-divider">|</span>
            <el-tag size="small" type="primary">{{ categoryName(video.category) }}</el-tag>
          </div>
        </div>

        <!-- 视频描述 -->
        <el-card v-if="video.description" class="desc-card" shadow="never">
          <template #header>
            <div class="desc-header">
              <el-icon><Document /></el-icon>
              <span>视频简介</span>
            </div>
          </template>
          <p class="desc-text">{{ video.description }}</p>
        </el-card>

        <!-- 关联商品 -->
        <div v-if="video.productId" class="product-link">
          <el-button type="primary" size="large" @click="$router.push(`/product/${video.productId}`)">
            <el-icon><Goods /></el-icon>
            查看关联商品
          </el-button>
        </div>

        <!-- 推荐视频 -->
        <div v-if="relatedVideos.length > 0" class="related-section">
          <h3 class="section-title">
            <el-icon><VideoCamera /></el-icon>
            相关推荐
          </h3>
          <div class="related-grid">
            <div
              v-for="rv in relatedVideos"
              :key="rv.id"
              class="related-card"
              @click="goToVideo(rv.id)"
            >
              <div class="related-cover">
                <img v-if="rv.coverUrl" :src="rv.coverUrl" alt="封面" />
                <div v-else class="related-fallback" :style="{ background: getGradient(rv.id) }">
                  <svg viewBox="0 0 24 24" width="24" height="24" fill="white"><polygon points="8,5 19,12 8,19"/></svg>
                </div>
                <div class="related-play">
                  <svg viewBox="0 0 24 24" width="16" height="16" fill="white"><polygon points="8,5 19,12 8,19"/></svg>
                </div>
              </div>
              <div class="related-info">
                <h5>{{ rv.title }}</h5>
                <span>{{ rv.viewCount }} 次观看</span>
              </div>
            </div>
          </div>
        </div>
      </template>

      <el-empty v-else-if="!loading" description="视频不存在或已删除" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { videoApi } from '../api'
import { View, Document, Goods, VideoCamera } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const video = ref(null)
const loading = ref(false)
const relatedVideos = ref([])
const videoPlayer = ref(null)

const gradients = [
  'linear-gradient(135deg, #667eea, #764ba2)',
  'linear-gradient(135deg, #f093fb, #f5576c)',
  'linear-gradient(135deg, #4facfe, #00f2fe)',
  'linear-gradient(135deg, #43e97b, #38f9d7)',
  'linear-gradient(135deg, #fa709a, #fee140)',
  'linear-gradient(135deg, #a8edea, #fed6e3)',
  'linear-gradient(135deg, #ff9a9e, #fecfef)',
  'linear-gradient(135deg, #fbc2eb, #a6c1ee)',
]

const getGradient = (id) => gradients[(id - 1) % gradients.length]

onMounted(async () => {
  loading.value = true
  try {
    video.value = await videoApi.detail(route.params.id)
    // 加载相关推荐视频（同分类）
    if (video.value?.category) {
      const related = await videoApi.byCategory(video.value.category)
      relatedVideos.value = related.filter(v => v.id !== video.value.id).slice(0, 4)
    }
  } catch (e) {
    console.error('加载视频失败:', e)
  }
  loading.value = false
})

const goToVideo = (id) => {
  router.push(`/video/${id}`)
}

const onPlay = () => {
  // 视频开始播放时的回调
  console.log('视频开始播放')
}

const categoryName = (c) => {
  const map = { PET_PLAY: '萌宠日常', PET_FOOD: '喂养指南', PET_CARE: '宠物护理', PRODUCT_INTRO: '商品介绍' }
  return map[c] || c
}
</script>

<style scoped>
.video-detail-page { padding: 24px; max-width: 900px; margin: 0 auto; }

/* 视频播放器 */
.video-player-wrapper {
  margin: 20px 0;
  border-radius: 12px;
  overflow: hidden;
  background: #000;
  aspect-ratio: 16 / 9;
  display: flex;
  align-items: center;
  justify-content: center;
}
.video-player {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
.player-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #1a1a2e, #16213e, #0f3460);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  gap: 12px;
}
.player-placeholder p { font-size: 18px; margin: 0; }
.player-placeholder span { font-size: 12px; opacity: 0.6; }

/* 视频信息 */
.video-header {
  margin-bottom: 16px;
}
.video-title {
  font-size: 22px;
  margin: 0 0 10px;
  line-height: 1.4;
}
.video-meta {
  color: #999;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 10px;
}
.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}
.meta-divider { color: #ddd; }

/* 描述卡片 */
.desc-card { margin-bottom: 20px; }
.desc-header {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
}
.desc-text {
  margin: 0;
  line-height: 1.8;
  color: #555;
}

/* 关联商品 */
.product-link {
  margin-bottom: 24px;
}
.product-link .el-button {
  display: flex;
  align-items: center;
  gap: 6px;
}

/* 推荐视频 */
.related-section { margin-top: 24px; }
.section-title {
  font-size: 18px;
  margin: 0 0 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.related-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}
@media (max-width: 768px) {
  .related-grid { grid-template-columns: repeat(2, 1fr); }
}
.related-card {
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  background: white;
  border: 1px solid var(--el-border-color);
  transition: all 0.2s;
}
.related-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.related-cover {
  height: 100px;
  position: relative;
  overflow: hidden;
}
.related-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.related-fallback {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.related-play {
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s;
}
.related-card:hover .related-play { opacity: 1; }
.related-info { padding: 8px; }
.related-info h5 {
  margin: 0 0 4px;
  font-size: 13px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.related-info span {
  font-size: 11px;
  color: #aaa;
}
</style>
