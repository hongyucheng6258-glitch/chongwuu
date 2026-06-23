<template>
  <div class="stores-page">
    <div class="page-card">
      <div class="hero-banner">
        <h1>附近宠物商店</h1>
        <p>基于您的实时定位，搜索周边真实的宠物商店、宠物医院</p>
      </div>

      <div class="search-bar">
        <el-input v-model="keyword" placeholder="搜索宠物店、宠物医院..." clearable @keyup.enter="searchPoi" size="large">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-button type="primary" @click="locateMe" :loading="locating">
          <el-icon><Location /></el-icon> 定位我
        </el-button>
        <el-button type="success" @click="searchPoi" :loading="loading">
          <el-icon><Search /></el-icon> 搜附近
        </el-button>
      </div>

      <!-- 定位状态提示 -->
      <el-alert v-if="!userLocation && !loading" title="点击「定位我」获取当前位置，即可搜索附近宠物商店" type="info" :closable="false" style="margin-bottom: 16px;" />
      <el-alert v-if="userLocation" :title="`当前位置：${userLocation.lat.toFixed(4)}, ${userLocation.lng.toFixed(4)}`" type="success" :closable="false" style="margin-bottom: 16px;" />

      <!-- 地图容器 -->
      <el-card class="map-card" v-loading="mapLoading">
        <template #header>
          <div class="map-header">
            <span>周边地图</span>
            <el-radio-group v-model="mapType" size="small" @change="switchMapType">
              <el-radio-button label="amap">地图</el-radio-button>
              <el-radio-button label="list">列表</el-radio-button>
            </el-radio-group>
          </div>
        </template>
        <div v-show="mapType === 'amap'" id="stores-map" class="map-container"></div>
        <div v-show="mapType === 'list'" class="list-view">
          <el-empty v-if="stores.length === 0" description="暂无数据，请先定位并搜索" />
          <div v-for="(store, idx) in stores" :key="idx" class="store-card" @click="navigateTo(store)">
            <div class="store-icon">{{ store.type === '宠物医院' ? '🏥' : (store.type === '宠物美容' ? '✂️' : '🏪') }}</div>
            <div class="store-info">
              <h3>{{ store.name }}</h3>
              <p class="store-addr"><el-icon><Location /></el-icon> {{ store.address }}</p>
              <p class="store-dist" v-if="store.distance">📍 {{ store.distance }}</p>
              <p class="store-type" v-if="store.type">{{ store.type }}</p>
            </div>
            <el-button size="small" type="primary">导航</el-button>
          </div>
        </div>
      </el-card>

      <!-- 商店列表 -->
      <div class="stores-list" v-loading="loading" v-if="mapType === 'amap'">
        <el-empty v-if="stores.length === 0 && !loading" description="暂无数据，请先定位并搜索附近宠物商店" />
        <div v-for="(store, idx) in stores" :key="idx" class="store-card" @click="navigateTo(store)">
          <div class="store-icon">{{ store.type === '宠物医院' ? '🏥' : (store.type === '宠物美容' ? '✂️' : '🏪') }}</div>
          <div class="store-info">
            <h3>{{ store.name }}</h3>
            <p class="store-addr"><el-icon><Location /></el-icon> {{ store.address }}</p>
            <p class="store-dist" v-if="store.distance">📍 {{ store.distance }}</p>
            <p class="store-type" v-if="store.type"><el-tag size="small">{{ store.type }}</el-tag></p>
          </div>
          <div class="store-actions">
            <el-button size="small" type="primary" @click.stop="navigateTo(store)">导航到店</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { Search, Location } from '@element-plus/icons-vue'

const stores = ref([])
const loading = ref(false)
const mapLoading = ref(false)
const locating = ref(false)
const keyword = ref('')
const mapType = ref('amap')
const userLocation = ref(null)

// 高德地图相关
let map = null
let markers = []

onMounted(() => {
  initAmap()
})

// 初始化高德地图（只加载地图，不加载数据）
const initAmap = async () => {
  mapLoading.value = true
  await nextTick()

  if (!window.AMap) {
    await loadAmapScript()
  }

  if (!window.AMap) {
    mapLoading.value = false
    return
  }

  const mapDiv = document.getElementById('stores-map')
  if (!mapDiv) {
    mapLoading.value = false
    return
  }

  // 默认中心点（全国视角）
  const center = userLocation.value ? [userLocation.value.lng, userLocation.value.lat] : [116.397428, 39.90923]

  map = new window.AMap.Map('stores-map', {
    zoom: userLocation.value ? 14 : 4,
    center: center,
    viewMode: '2D'
  })

  // 添加定位控件
  map.addControl(new window.AMap.Geolocation({
    position: 'RB',
    offset: [20, 60],
    zoomToAccuracy: true
  }))

  mapLoading.value = false
}

// 加载高德地图脚本（含 PlaceSearch 插件）
const loadAmapScript = () => {
  return new Promise((resolve) => {
    if (window.AMap) {
      resolve()
      return
    }
    // 使用 JSAPI Loader 安全加载
    window._AMapSecurityConfig = { securityJsCode: 'b722c389c7cb001fdc45d58920e0ab7a' }
    const script = document.createElement('script')
    script.type = 'text/javascript'
    script.src = 'https://webapi.amap.com/maps?v=2.0&key=49a5330f8c1c7b530ef94573c2744aaf&plugin=AMap.PlaceSearch,AMap.Geolocation'
    script.onerror = () => resolve()
    script.onload = () => resolve()
    document.head.appendChild(script)
  })
}

// 添加商店标记到地图
const addStoreMarkers = () => {
  if (!map || !window.AMap) return

  // 清除旧标记
  markers.forEach(m => map.remove(m))
  markers = []

  stores.value.forEach((store, idx) => {
    if (store.longitude && store.latitude) {
      const marker = new window.AMap.Marker({
        position: [store.longitude, store.latitude],
        map: map,
        title: store.name,
        label: {
          content: (idx + 1).toString(),
          offset: new window.AMap.Pixel(0, -30),
          direction: 'top'
        }
      })

      // 点击标记显示信息窗体
      marker.on('click', () => {
        const infoWindow = new window.AMap.InfoWindow({
          content: `
            <div style="padding:10px;min-width:220px;">
              <h4 style="margin:0 0 8px;">${store.name}</h4>
              <p style="margin:4px 0;font-size:12px;color:#666;">${store.address}</p>
              <p style="margin:4px 0;font-size:12px;color:#ff6b4a;">${store.distance || ''}</p>
              <p style="margin:4px 0;font-size:12px;">${store.type || ''}</p>
              <button onclick="window.open('https://uri.amap.com/navigation?to=${store.longitude},${store.latitude},${encodeURIComponent(store.name)}&mode=car')" 
                style="margin-top:8px;padding:6px 16px;background:#ff6b4a;color:#fff;border:none;border-radius:4px;cursor:pointer;font-size:13px;">导航到店</button>
            </div>
          `,
          offset: new window.AMap.Pixel(0, -35)
        })
        infoWindow.open(map, [store.longitude, store.latitude])
      })

      markers.push(marker)
    }
  })

  // 自动调整视野
  if (markers.length > 0) {
    map.setFitView(markers, false, [60, 60, 60, 60])
  }
}

// 定位当前位置
const locateMe = () => {
  locating.value = true
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        userLocation.value = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        }
        locating.value = false
        // 地图中心移到当前位置
        if (map) {
          map.setCenter([position.coords.longitude, position.coords.latitude])
          map.setZoom(14)
          // 添加当前位置标记
          new window.AMap.Marker({
            position: [position.coords.longitude, position.coords.latitude],
            map: map,
            title: '我的位置',
            icon: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png'
          })
        }
        // 自动搜索附近
        searchPoi()
      },
      (error) => {
        locating.value = false
        alert('定位失败，请确保已允许位置权限')
      }
    )
  } else {
    locating.value = false
    alert('您的浏览器不支持地理定位')
  }
}

// 搜索附近POI（使用高德地图 JS API PlaceSearch 插件）
const searchPoi = async () => {
  if (!userLocation.value) {
    alert('请先点击「定位我」获取当前位置')
    return
  }

  loading.value = true
  mapLoading.value = true

  const lat = userLocation.value.lat
  const lng = userLocation.value.lng
  // 更精确的搜索关键词
  const keywords = keyword.value || '宠物店|宠物医院|宠物诊所'

  console.log('🔍 开始搜索附近POI:', { lat, lng, keywords })

  try {
    // 确保插件已加载
    if (!window.AMap || !window.AMap.PlaceSearch) {
      console.log('📦 加载 PlaceSearch 插件...')
      await new Promise((resolve) => {
        window.AMap.plugin(['AMap.PlaceSearch'], resolve)
      })
    }

    const placeSearch = new window.AMap.PlaceSearch({
      pageSize: 50, // 增加返回数量
      pageIndex: 1,
      extensions: 'all'
    })

    console.log('📍 执行 searchNearBy...', `半径: 5000米`)

    placeSearch.searchNearBy(keywords, [lng, lat], 5000, function(status, result) {
      console.log('🎯 搜索回调 - 状态:', status, '结果:', result)
      
      loading.value = false
      mapLoading.value = false

      if (status === 'complete' && result.poiList && result.poiList.pois && result.poiList.pois.length > 0) {
        console.log('✅ 找到', result.poiList.pois.length, '个POI')
        console.log('🔍 完整result对象:', result)
        console.log('🔍 result.poiList.pois数组:', result.poiList.pois)
        console.log('🔍 第一个POI原始数据:', result.poiList.pois[0])
        console.log('🔍 POI数组类型:', Array.isArray(result.poiList.pois))
        
        // 直接赋值原始数据测试
        const pois = result.poiList.pois
        console.log('📍 原始pois:', pois)
        console.log('📍 原始pois长度:', pois.length)
        
        // 简化版本测试
        const testArray = []
        for (let i = 0; i < Math.min(3, pois.length); i++) {
          const poi = pois[i]
          console.log(`🏪 处理第${i}个POI:`, poi)
          const storeItem = {
            name: poi.name || '未知商店',
            address: poi.address || '地址未知',
            longitude: poi.location ? poi.location.lng : 0,
            latitude: poi.location ? poi.location.lat : 0,
            distance: poi.distance ? formatDistance(poi.distance) : '',
            type: poi.type || '宠物服务',
            tel: poi.tel || ''
          }
          console.log(`🏪 生成的storeItem:`, storeItem)
          testArray.push(storeItem)
        }
        
        console.log('📍 testArray长度:', testArray.length)
        console.log('📍 testArray内容:', testArray)
        
        stores.value = testArray
        console.log('📍 赋值后 stores.value:', stores.value)
        console.log('📍 赋值后 stores.value.length:', stores.value.length)
        console.log('📍 stores本身:', stores)

        // 更新地图标记
        if (mapType.value === 'amap' && map) {
          addStoreMarkers()
        }
      } else {
        console.warn('⚠️ 5km范围内无结果，扩大到10km重试...')
        stores.value = []
        
        // 扩大搜索范围，使用更通用的关键词
        placeSearch.searchNearBy('宠物', [lng, lat], 10000, (s2, r2) => {
          console.log('🎯 10km搜索回调 - 状态:', s2, '结果:', r2)
          
          if (s2 === 'complete' && r2.poiList && r2.poiList.pois && r2.poiList.pois.length > 0) {
             console.log('✅ 10km范围找到', r2.poiList.pois.length, '个POI')
             console.log('🔍 第一个POI原始数据:', r2.poiList.pois[0])
             
             stores.value = r2.poiList.pois.map(poi => {
               const storeData = {
                 name: poi.name || '未知商店',
                 address: poi.address || (poi.pname || '') + (poi.cityname || '') + (poi.adname || '') || '地址未知',
                 longitude: poi.location ? poi.location.lng : (poi.lng || 0),
                 latitude: poi.location ? poi.location.lat : (poi.lat || 0),
                 distance: poi.distance ? formatDistance(poi.distance) : '',
                 type: poi.type ? poi.type.split(';')[0] : '宠物服务',
                 tel: poi.tel || ''
               }
               console.log('🏪 处理POI:', poi.name, '→', storeData)
               return storeData
             })
             
             console.log('📍 10km处理后的商店数据:', stores.value)
             console.log('📍 stores.value.length:', stores.value.length)
             
             if (mapType.value === 'amap' && map) addStoreMarkers()
          } else {
            console.error('❌ 10km范围内仍无结果')
            alert('附近暂无宠物相关商店，请尝试更换搜索词或位置')
          }
        })
      }
    })
  } catch (e) {
    console.error('❌ 搜索异常:', e)
    alert('搜索失败: ' + e.message)
    loading.value = false
    mapLoading.value = false
  }
}

// 格式化距离
const formatDistance = (meters) => {
  if (!meters) return ''
  const m = parseInt(meters)
  if (m < 1000) return `${m}米`
  return `${(m / 1000).toFixed(1)}公里`
}

// 导航到商店
const navigateTo = (store) => {
  if (!store.latitude || !store.longitude) {
    alert('该商店暂无位置信息')
    return
  }
  const name = encodeURIComponent(store.name)
  let url = `https://uri.amap.com/navigation?to=${store.longitude},${store.latitude},${name}&mode=car&callnative=1`
  // 如果已定位，自动将用户当前位置设为起点
  if (userLocation.value) {
    url += `&from=${userLocation.value.lng},${userLocation.value.lat},我的位置`
  }
  window.open(url, '_blank')
}

// 切换地图类型
const switchMapType = (type) => {
  if (type === 'amap') {
    nextTick(() => {
      if (map) {
        addStoreMarkers()
        if (markers.length > 0) {
          map.setFitView(markers, false, [60, 60, 60, 60])
        }
      }
    })
  }
}
</script>

<style scoped>
.stores-page { padding: 24px; max-width: 1200px; margin: 0 auto; }
.hero-banner { text-align: center; padding: 40px 20px; background: linear-gradient(135deg, var(--paw-coral-light), var(--paw-sky-light)); border-radius: 16px; margin-bottom: 24px; }
.hero-banner h1 { margin: 0 0 8px; font-size: 28px; }
.hero-banner p { color: #666; margin: 0; }
.search-bar { display: flex; gap: 12px; margin-bottom: 16px; max-width: 700px; }
.search-bar .el-input { flex: 1; }

.map-card { margin-bottom: 20px; }
.map-header { display: flex; justify-content: space-between; align-items: center; }
.map-container { width: 100%; height: 450px; border-radius: 8px; }
.list-view { max-height: 450px; overflow-y: auto; }

.stores-list { margin-top: 20px; }
.store-card { display: flex; align-items: center; gap: 16px; padding: 16px; border: 1px solid var(--el-border-color); border-radius: 12px; margin-bottom: 12px; cursor: pointer; transition: all 0.2s; }
.store-card:hover { border-color: var(--paw-coral); box-shadow: 0 4px 12px rgba(0,0,0,0.06); transform: translateY(-2px); }
.store-icon { font-size: 36px; }
.store-info { flex: 1; }
.store-info h3 { margin: 0 0 6px; font-size: 16px; }
.store-info p { margin: 4px 0; font-size: 13px; color: #666; display: flex; align-items: center; gap: 4px; }
.store-dist { color: var(--paw-coral) !important; font-weight: 600; }
.store-type { margin-top: 4px; }
.store-actions { display: flex; flex-direction: column; gap: 8px; align-items: flex-end; }
</style>
