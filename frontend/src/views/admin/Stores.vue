<template>
  <div class="admin-page">
    <div class="page-head">
      <h2>🏪 商店管理</h2>
      <el-button type="primary" @click="showDialog(null)">新增商店</el-button>
    </div>

    <el-table :data="stores" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="商店名称" min-width="180" />
      <el-table-column prop="address" label="地址" min-width="220" show-overflow-tooltip />
      <el-table-column prop="phone" label="电话" width="140" />
      <el-table-column prop="businessHours" label="营业时间" width="120" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '营业' : '歇业' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button link type="primary" @click="showDialog(row)">编辑</el-button>
          <el-button link type="danger" @click="deleteStore(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="editing ? '编辑商店' : '新增商店'" v-model="visible" width="550px" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="商店名称" required>
          <el-input v-model="form.name" placeholder="请输入商店名称" />
        </el-form-item>
        <el-form-item label="地址" required>
          <el-input v-model="form.address" type="textarea" :rows="2" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="纬度">
              <el-input v-model="form.latitude" placeholder="如: 39.9087" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度">
              <el-input v-model="form.longitude" placeholder="如: 116.4715" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="营业时间">
          <el-input v-model="form.businessHours" placeholder="如: 09:00-21:00" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="商店描述..." />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="营业中" inactive-text="歇业" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="save" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { storeApi } from '../../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const stores = ref([])
const loading = ref(false)
const visible = ref(false)
const editing = ref(null)
const saving = ref(false)
const form = ref({ name: '', address: '', phone: '', latitude: '', longitude: '', businessHours: '', description: '', status: 1 })

const loadStores = async () => { loading.value = true; try { stores.value = await storeApi.adminList() } catch (e) {}; loading.value = false }
onMounted(loadStores)

const showDialog = (store) => {
  editing.value = store
  if (store) {
    form.value = { ...store }
  } else {
    form.value = { name: '', address: '', phone: '', latitude: '', longitude: '', businessHours: '', description: '', status: 1 }
  }
  visible.value = true
}

const save = async () => {
  if (!form.value.name || !form.value.address) { ElMessage.warning('请填写名称和地址'); return }
  saving.value = true
  try {
    if (editing.value) {
      await storeApi.update(editing.value.id, form.value)
    } else {
      await storeApi.create(form.value)
    }
    ElMessage.success(editing.value ? '已更新' : '已创建')
    visible.value = false
    loadStores()
  } catch (e) {}
  saving.value = false
}

const deleteStore = async (id) => {
  await ElMessageBox.confirm('确定删除该商店吗？', '提示', { type: 'warning' })
  try { await storeApi.delete(id); ElMessage.success('已删除'); loadStores() } catch (e) {}
}
</script>

<style scoped>
.admin-page { padding: 20px; }
.page-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-head h2 { margin: 0; font-size: 20px; }
</style>
