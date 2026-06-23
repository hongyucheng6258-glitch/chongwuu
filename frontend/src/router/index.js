import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user'

const routes = [
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue'), meta: { title: '登录' } },
  { path: '/register', name: 'Register', component: () => import('../views/Register.vue'), meta: { title: '注册' } },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('../views/Home.vue'), meta: { title: '首页' } },
      { path: 'product/:id', name: 'ProductDetail', component: () => import('../views/ProductDetail.vue'), meta: { title: '商品详情' } },
      { path: 'cart', name: 'Cart', component: () => import('../views/Cart.vue'), meta: { title: '购物车', requireAuth: true } },
      { path: 'checkout', name: 'Checkout', component: () => import('../views/Checkout.vue'), meta: { title: '结算', requireAuth: true } },
      { path: 'orders', name: 'Orders', component: () => import('../views/Orders.vue'), meta: { title: '我的订单', requireAuth: true } },
      // 个人中心
      { path: 'profile', name: 'Profile', component: () => import('../views/Profile.vue'), meta: { title: '个人中心', requireAuth: true } },
      { path: 'addresses', name: 'Addresses', component: () => import('../views/Addresses.vue'), meta: { title: '收货地址', requireAuth: true } },
      // 商店浏览
      { path: 'stores', name: 'Stores', component: () => import('../views/Stores.vue'), meta: { title: '附近商店' } },
      { path: 'store/:id', name: 'StoreDetail', component: () => import('../views/StoreDetail.vue'), meta: { title: '商店详情' } },
      // 视频浏览
      { path: 'videos', name: 'Videos', component: () => import('../views/Videos.vue'), meta: { title: '宠物视频' } },
      { path: 'video/:id', name: 'VideoDetail', component: () => import('../views/VideoDetail.vue'), meta: { title: '视频详情' } },
      // AI 助手
      { path: 'ai-chat', name: 'AiChat', component: () => import('../views/AiChat.vue'), meta: { title: 'AI助手' } },
      // 消息通知
      { path: 'notifications', name: 'Notifications', component: () => import('../views/Notifications.vue'), meta: { title: '消息中心', requireAuth: true } },
      // 订单详情
      { path: 'order/:id', name: 'OrderDetail', component: () => import('../views/OrderDetail.vue'), meta: { title: '订单详情', requireAuth: true } },
      // 帮助中心
      { path: 'help/shopping-process', name: 'ShoppingProcess', component: () => import('../views/help/ShoppingProcess.vue'), meta: { title: '购物流程' } },
      { path: 'help/membership', name: 'Membership', component: () => import('../views/help/Membership.vue'), meta: { title: '会员介绍' } },
      { path: 'help/faq', name: 'Faq', component: () => import('../views/help/Faq.vue'), meta: { title: '常见问题' } },
      { path: 'help/self-pickup', name: 'SelfPickup', component: () => import('../views/help/SelfPickup.vue'), meta: { title: '上门自提' } },
      { path: 'help/express-delivery', name: 'ExpressDelivery', component: () => import('../views/help/ExpressDelivery.vue'), meta: { title: '极速配送' } },
      { path: 'help/shipping-fees', name: 'ShippingFees', component: () => import('../views/help/ShippingFees.vue'), meta: { title: '配送费用' } },
      { path: 'help/return-policy', name: 'ReturnPolicy', component: () => import('../views/help/ReturnPolicy.vue'), meta: { title: '退换货政策' } },
      { path: 'help/refund-guide', name: 'RefundGuide', component: () => import('../views/help/RefundGuide.vue'), meta: { title: '退款说明' } },
      { path: 'help/repair-return', name: 'RepairReturn', component: () => import('../views/help/RepairReturn.vue'), meta: { title: '返修退换' } }
    ]
  },
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    meta: { requireAuth: true, requireAdmin: true },
    children: [
      { path: '', name: 'AdminHome', component: () => import('../views/admin/Dashboard.vue'), meta: { title: '管理后台' } },
      { path: 'products', name: 'AdminProducts', component: () => import('../views/admin/Products.vue'), meta: { title: '商品管理' } },
      { path: 'categories', name: 'AdminCategories', component: () => import('../views/admin/Categories.vue'), meta: { title: '分类管理' } },
      { path: 'orders', name: 'AdminOrders', component: () => import('../views/admin/Orders.vue'), meta: { title: '订单管理' } },
      { path: 'stores', name: 'AdminStores', component: () => import('../views/admin/Stores.vue'), meta: { title: '商店管理' } },
      { path: 'videos', name: 'AdminVideos', component: () => import('../views/admin/Videos.vue'), meta: { title: '视频管理' } },
      { path: 'users', name: 'AdminUsers', component: () => import('../views/admin/Users.vue'), meta: { title: '会员管理' } },
      { path: 'reviews', name: 'AdminReviews', component: () => import('../views/admin/Reviews.vue'), meta: { title: '评价管理' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  document.title = (to.meta.title ? to.meta.title + ' - ' : '') + '初晴萌宠空间'
  const userStore = useUserStore()

  if (to.meta.requireAuth && !userStore.isLogin) {
    return next('/login')
  }
  if (to.meta.requireAdmin && !userStore.isAdmin) {
    return next('/')
  }
  next()
})

export default router
