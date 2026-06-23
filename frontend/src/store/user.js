import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || 'null')
  }),
  getters: {
    isLogin: state => !!state.token,
    isAdmin: state => state.user?.role === 'ADMIN',
    username: state => state.user?.username || '',
    nickname: state => state.user?.nickname || state.user?.username || '',
    memberLevel: state => state.user?.memberLevel || 0,
    memberLevelName: state => {
      const levels = ['普通用户', '银卡会员', '金卡会员', '钻石会员']
      return levels[state.user?.memberLevel || 0] || '普通用户'
    }
  },
  actions: {
    setLogin(data) {
      this.token = data.token
      this.user = data.user
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(data.user))
    },
    updateUser(userData) {
      this.user = { ...this.user, ...userData }
      localStorage.setItem('user', JSON.stringify(this.user))
    },
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})
