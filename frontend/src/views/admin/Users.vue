<template>
  <div class="admin-page">
    <div class="page-head">
      <h2>👥 会员管理</h2>
    </div>

    <el-table :data="users" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="nickname" label="昵称" width="120" />
      <el-table-column prop="email" label="邮箱" min-width="160" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column label="会员等级" width="120">
        <template #default="{ row }">
          <el-select :model-value="row.memberLevel" @change="(v) => updateLevel(row.id, v)" size="small" style="width:110px">
            <el-option :value="0" label="普通用户" />
            <el-option :value="1" label="银卡会员" />
            <el-option :value="2" label="金卡会员" />
            <el-option :value="3" label="钻石会员" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="角色" width="80">
        <template #default="{ row }">
          <el-tag :type="row.role === 'ADMIN' ? 'danger' : 'info'" size="small">{{ row.role === 'ADMIN' ? '管理员' : '用户' }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { userApi } from '../../api'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store/user'

const users = ref([])
const loading = ref(false)
const userStore = useUserStore()

onMounted(async () => { loading.value = true; try { users.value = await userApi.adminList() } catch (e) {}; loading.value = false })

const updateLevel = async (id, level) => {
  try {
    const res = await userApi.updateMemberLevel(id, level)
    ElMessage.success('会员等级已更新')
    // 如果修改的是自己，更新store中的token和用户信息
    if (res.data.token) {
      userStore.setLogin({ token: res.data.token, user: res.data.user })
      ElMessage.success('您的会员等级已更新，已自动刷新')
    }
    // 更新本地列表数据
    const idx = users.value.findIndex(u => u.id === id)
    if (idx !== -1) users.value[idx].memberLevel = level
  } catch (e) {}
}
</script>

<style scoped>
.admin-page { padding: 20px; }
.page-head { margin-bottom: 20px; }
.page-head h2 { margin: 0; font-size: 20px; }
</style>
