import request from '../utils/request'

// 鉴权
export const authApi = {
  login: data => request.post('/auth/login', data),
  register: data => request.post('/auth/register', data),
  // Gitee 第三方登录
  getGiteeAuthorizeUrl: () => request.get('/auth/gitee/authorize-url'),
  // 短信验证码登录
  sendSmsCode: data => request.post('/auth/sms/send', data),
  smsLogin: data => request.post('/auth/sms/login', data)
}

// 用户
export const userApi = {
  profile: () => request.get('/user/profile'),
  updateProfile: data => request.put('/user/profile', data),
  adminList: () => request.get('/user/admin/list'),
  updateMemberLevel: (id, level) => request.put(`/user/admin/${id}/member-level`, { level })
}

// 地址
export const addressApi = {
  list: () => request.get('/address'),
  create: data => request.post('/address', data),
  update: (id, data) => request.put(`/address/${id}`, data),
  delete: id => request.delete(`/address/${id}`),
  setDefault: id => request.put(`/address/${id}/default`)
}

// 分类
export const categoryApi = {
  list: () => request.get('/categories/list'),
  create: data => request.post('/categories', data),
  update: (id, data) => request.put(`/categories/${id}`, data),
  delete: id => request.delete(`/categories/${id}`)
}

// 商品
export const productApi = {
  list: params => request.get('/products/list', { params }),
  detail: id => request.get(`/products/detail/${id}`),
  adminList: params => request.get('/products/admin', { params }),
  create: data => request.post('/products', data),
  update: (id, data) => request.put(`/products/${id}`, data),
  delete: id => request.delete(`/products/${id}`)
}

// 购物车
export const cartApi = {
  list: () => request.get('/cart'),
  add: data => request.post('/cart', data),
  update: (id, quantity) => request.put(`/cart/${id}`, null, { params: { quantity } }),
  remove: id => request.delete(`/cart/${id}`),
  clear: () => request.delete('/cart')
}

// 订单
export const orderApi = {
  create: data => request.post('/orders', data),
  list: () => request.get('/orders'),
  detail: id => request.get(`/orders/${id}`),
  pay: id => request.put(`/orders/${id}/pay`),
  receive: id => request.put(`/orders/${id}/receive`),
  cancel: (id, reason) => request.put(`/orders/${id}/cancel`, { reason }),
  requestReturn: (id, reason) => request.put(`/orders/${id}/return`, { reason }),
  // 管理端
  adminList: () => request.get('/orders/admin'),
  ship: id => request.put(`/orders/admin/${id}/ship`),
  returnReview: (id, approved) => request.put(`/orders/admin/${id}/return-review`, { approved }),
  directReturn: (id, reason) => request.put(`/orders/admin/${id}/direct-return`, { reason }),
  updateStatus: (id, status) => request.put(`/orders/admin/${id}/status`, { status }),
  stats: () => request.get('/orders/admin/stats')
}

// 商店
export const storeApi = {
  list: () => request.get('/stores/list'),
  search: keyword => request.get('/stores/search', { params: { keyword } }),
  nearby: (lat, lng, radius = 10) => request.get('/stores/nearby', { params: { lat, lng, radius } }),
  detail: id => request.get(`/stores/detail/${id}`),
  adminList: () => request.get('/stores/admin'),
  create: data => request.post('/stores', data),
  update: (id, data) => request.put(`/stores/${id}`, data),
  delete: id => request.delete(`/stores/${id}`),
  // MongoDB 地理信息 API
  syncGeo: () => request.post('/stores/geo/sync'),
  nearbyGeo: (lat, lng, radius = 10) => request.get('/stores/geo/nearby', { params: { lat, lng, radius } }),
  searchGeo: keyword => request.get('/stores/geo/search', { params: { keyword } }),
  navigate: (fromLat, fromLng, toLat, toLng, toName, type = 'amap') =>
    request.get('/stores/geo/navigate', { params: { fromLat, fromLng, toLat, toLng, toName, type } })
}

// 视频
export const videoApi = {
  list: (page = 0, size = 12) => request.get('/videos/list', { params: { page, size } }),
  detail: id => request.get(`/videos/detail/${id}`),
  byProduct: productId => request.get(`/videos/product/${productId}`),
  byCategory: category => request.get(`/videos/category/${category}`),
  create: data => request.post('/videos', data),
  update: (id, data) => request.put(`/videos/${id}`, data),
  delete: id => request.delete(`/videos/${id}`)
}

// 评价
export const reviewApi = {
  create: data => request.post('/reviews', data),
  byProduct: productId => request.get(`/reviews/product/${productId}`),
  myReviews: () => request.get('/reviews/my'),
  delete: id => request.delete(`/reviews/${id}`),
  adminList: () => request.get('/reviews/admin')
}

// AI 助手
export const aiApi = {
  chat: data => request.post('/ai/chat', data)
}

// 推荐
export const recommendApi = {
  forMe: (size = 6) => request.get('/recommend/for-me', { params: { size } }),
  similar: (productId, size = 4) => request.get(`/recommend/similar/${productId}`, { params: { size } }),
  hot: (size = 6) => request.get('/recommend/hot', { params: { size } })
}

// 消息通知
export const notificationApi = {
  list: () => request.get('/notifications'),
  unreadCount: () => request.get('/notifications/unread-count'),
  markAsRead: id => request.put(`/notifications/${id}/read`),
  markAllAsRead: () => request.put('/notifications/read-all'),
  delete: id => request.delete(`/notifications/${id}`)
}

// 统计数据
export const statsApi = {
  dashboard: () => request.get('/stats/dashboard'),
  overview: () => request.get('/stats/overview'),
  salesTrend: () => request.get('/stats/sales-trend'),
  categoryDistribution: () => request.get('/stats/category-distribution'),
  topProducts: () => request.get('/stats/top-products'),
  userGrowth: () => request.get('/stats/user-growth'),
  memberDistribution: () => request.get('/stats/member-distribution')
}
