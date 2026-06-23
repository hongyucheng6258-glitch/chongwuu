<template>
  <div class="store-detail-page">
    <el-page-header @back="$router.back()" title="返回" />
    <div class="page-card" v-loading="loading">
      <template v-if="store">
        <div class="store-hero">
          <h1>🏪 {{ store.name }}</h1>
          <el-tag :type="store.status === 1 ? 'success' : 'info'">{{ store.status === 1 ? '营业中' : '歇业' }}</el-tag>
        </div>
        <el-card class="info-card">
          <div class="info-row">
            <el-icon><Location /></el-icon>
            <span>{{ store.address }}</span>
          </div>
          <div class="info-row" v-if="store.phone">
            <el-icon><Phone /></el-icon>
            <span>{{ store.phone }}</span>
          </div>
          <div class="info-row" v-if="store.businessHours">
            <el-icon><Clock /></el-icon>
            <span>营业时间：{{ store.businessHours }}</span>
          </div>
          <div class="info-row" v-if="store.description">
            <p>{{ store.description }}</p>
          </div>
        </el-card>
        <!-- 地图展示 -->
        <el-card v-if="store.latitude && store.longitude" class="map-card">
          <template #header><span>店铺位置</span></template>
          <div id="store-map" class="map-container"></div>
          <div class="map-actions" style="margin-top: 12px;">
            <el-button type="primary" @click="openNavigation">开始导航</el-button>
          </div>
        </el-card>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { storeApi } from '../api'

const route = useRoute()
const router = useRouter()
const store = ref(null)
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    store.value = await storeApi.detail(route.params.id)
    await nextTick()
    initMap()
  } catch (e) {}
  loading.value = false
})

const initMap = () => {
  if (!store.value?.latitude || !store.value?.longitude) return
  // 使用静态地图图片作为简化方案（生产环境建议接入百度/高德地图SDK）
  const mapDiv = document.getElementById('store-map')
  if (!mapDiv) return
  const lat = store.value.latitude
  const lng = store.value.longitude
  mapDiv.innerHTML = `
    <div style="display:flex;align-items:center;justify-content:center;height:100%;background:#f0f5f0;border-radius:8px;text-align:center;padding:20px;">
      <div>
        <div style="font-size:48px;">📍</div>
        <p style="margin:8px 0;font-weight:bold;">${store.value.name}</p>
        <p style="font-size:13px;color:#666;">${store.value.address}</p>
        <p style="font-size:12px;color:#999;">坐标: ${lat}, ${lng}</p>
        <p style="font-size:11px;color:#999;margin-top:8px;">💡 生产环境可接入百度/高德地图SDK实现实时地图与导航</p>
      </div>
    </div>
  `
}

const openNavigation = () => {
  if (!store.value?.latitude || !store.value?.longitude) return
  const name = encodeURIComponent(store.value.name)
  const lat = store.value.latitude
  const lng = store.value.longitude
  // 优先使用高德地图
  const url = `https://uri.amap.com/navigation?to=${lng},${lat},${name}&mode=car&callnative=1`
  window.open(url, '_blank')
}
</script>

<style scoped>
.store-detail-page { padding: 24px; max-width: 900px; margin: 0 auto; }
.store-hero { display: flex; align-items: center; gap: 12px; margin: 20px 0; }
.store-hero h1 { margin: 0; font-size: 24px; }
.info-card { margin-bottom: 20px; }
.info-row { display: flex; align-items: center; gap: 8px; padding: 8px 0; color: #555; }
.map-card { margin-bottom: 20px; }
.map-container { width: 100%; height: 300px; border-radius: 8px; overflow: hidden; }
</style>
