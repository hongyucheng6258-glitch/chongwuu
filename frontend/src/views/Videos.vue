<template>
  <div class="videos-page">
    <div class="page-card">
      <div class="hero-banner">
        <h1>🐾 宠物视频</h1>
        <p>萌宠日常、喂养指南、商品评测，精彩视频看不停</p>
      </div>

      <div class="category-tabs">
        <el-radio-group v-model="activeCategory" size="small" @change="filterByCategory">
          <el-radio-button value="">全部</el-radio-button>
          <el-radio-button value="PET_PLAY">萌宠日常</el-radio-button>
          <el-radio-button value="PET_FOOD">喂养指南</el-radio-button>
          <el-radio-button value="PET_CARE">宠物护理</el-radio-button>
          <el-radio-button value="PRODUCT_INTRO">商品介绍</el-radio-button>
        </el-radio-group>
      </div>

      <div v-loading="loading">
        <el-empty v-if="videos.length === 0 && !loading" description="暂无视频" />
        <div class="video-grid" v-else>
          <div v-for="video in videos" :key="video.id" class="video-card" @click="$router.push(`/video/${video.id}`)">
            <div class="video-cover">
              <div class="video-placeholder">
                <svg viewBox="0 0 24 24" width="48" height="48" fill="white"><polygon points="8,5 19,12 8,19"/></svg>
              </div>
              <span class="video-category">{{ categoryName(video.category) }}</span>
            </div>
            <div class="video-info">
              <h4>{{ video.title }}</h4>
              <p>{{ video.description || '暂无简介' }}</p>
              <div class="video-meta">
                <span>👁 {{ video.viewCount || 0 }} 次观看</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="pagination-wrap" v-if="total > size">
        <el-pagination layout="prev, pager, next" :total="total" :page-size="size" @current-change="pageChange" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { videoApi } from '../api'

const videos = ref([])
const loading = ref(false)
const page = ref(0)
const size = 12
const total = ref(0)
const activeCategory = ref('')

onMounted(() => loadVideos())

const loadVideos = async () => {
  loading.value = true
  try {
    const data = await videoApi.list(page.value, size)
    videos.value = data.content || []
    total.value = data.totalElements || 0
  } catch (e) {}
  loading.value = false
}

const filterByCategory = async (cat) => {
  if (!cat) return loadVideos()
  loading.value = true
  try { videos.value = await videoApi.byCategory(cat); total.value = videos.value.length } catch (e) {}
  loading.value = false
}

const pageChange = (p) => { page.value = p - 1; loadVideos() }

const categoryName = (c) => {
  const map = { PET_PLAY: '萌宠日常', PET_FOOD: '喂养指南', PET_CARE: '宠物护理', PRODUCT_INTRO: '商品介绍' }
  return map[c] || c
}
</script>

<style scoped>
.videos-page { padding: 24px; max-width: 1100px; margin: 0 auto; }
.hero-banner { text-align: center; padding: 40px 20px; background: linear-gradient(135deg, var(--paw-sky-light), var(--paw-coral-light)); border-radius: 16px; margin-bottom: 24px; }
.hero-banner h1 { margin: 0 0 8px; font-size: 28px; }
.hero-banner p { color: #666; margin: 0; }
.category-tabs { margin-bottom: 20px; }
.video-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }
@media (max-width: 900px) { .video-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 500px) { .video-grid { grid-template-columns: 1fr; } }
.video-card { border-radius: 12px; overflow: hidden; cursor: pointer; background: white; border: 1px solid var(--el-border-color); transition: all 0.2s; }
.video-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.1); }
.video-cover { height: 160px; background: linear-gradient(135deg, #667eea, #764ba2); position: relative; display: flex; align-items: center; justify-content: center; }
.video-placeholder { width: 56px; height: 56px; background: rgba(255,255,255,0.2); border-radius: 50%; display: flex; align-items: center; justify-content: center; }
.video-category { position: absolute; top: 8px; right: 8px; background: rgba(0,0,0,0.5); color: white; padding: 2px 8px; border-radius: 4px; font-size: 11px; }
.video-info { padding: 12px; }
.video-info h4 { margin: 0 0 6px; font-size: 15px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.video-info p { margin: 0 0 8px; font-size: 12px; color: #999; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; }
.video-meta { font-size: 12px; color: #aaa; }
.pagination-wrap { margin-top: 24px; display: flex; justify-content: center; }
</style>
