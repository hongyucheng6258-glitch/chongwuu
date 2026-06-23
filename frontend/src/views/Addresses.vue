<template>
  <div class="address-page">
    <div class="page-card">
      <div class="page-header">
        <h2>收货地址</h2>
        <el-button type="primary" @click="showDialog(null)">添加新地址</el-button>
      </div>
      <el-empty v-if="addresses.length === 0 && !loading" description="暂无收货地址" />
      <div v-else class="address-list" v-loading="loading">
        <div v-for="addr in addresses" :key="addr.id" class="address-item" :class="{ default: addr.isDefault === 1 }">
          <div class="addr-tags">
            <el-tag v-if="addr.isDefault === 1" type="danger" size="small">默认</el-tag>
          </div>
          <div class="addr-info">
            <div class="addr-contact">
              <strong>{{ addr.name }}</strong>
              <span class="addr-phone">{{ addr.phone }}</span>
            </div>
            <div class="addr-detail">{{ addr.address }}</div>
          </div>
          <div class="addr-actions">
            <el-button link type="primary" @click="showDialog(addr)">编辑</el-button>
            <el-button link type="danger" @click="deleteAddr(addr.id)">删除</el-button>
            <el-button v-if="addr.isDefault !== 1" link @click="setDefault(addr.id)">设为默认</el-button>
          </div>
        </div>
      </div>
    </div>

    <el-dialog :title="editing ? '编辑地址' : '添加地址'" v-model="visible" width="500px" destroy-on-close>
      <el-form :model="form" label-width="80px" ref="formRef">
        <el-form-item label="收货人" required>
          <el-input v-model="form.name" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="手机号" required>
          <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11" />
        </el-form-item>
        <el-form-item label="详细地址" required>
          <el-input v-model="form.address" type="textarea" :rows="2" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="默认地址">
          <el-switch v-model="form.isDefault" :active-value="1" :inactive-value="0" />
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
import { addressApi } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const addresses = ref([])
const loading = ref(false)
const visible = ref(false)
const editing = ref(null)
const saving = ref(false)
const formRef = ref(null)
const form = ref({ name: '', phone: '', address: '', isDefault: 0 })

onMounted(() => loadAddresses())

const loadAddresses = async () => {
  loading.value = true
  try { addresses.value = await addressApi.list() } catch (e) {}
  loading.value = false
}

const showDialog = (addr) => {
  editing.value = addr
  if (addr) {
    form.value = { name: addr.name, phone: addr.phone, address: addr.address, isDefault: addr.isDefault }
  } else {
    form.value = { name: '', phone: '', address: '', isDefault: 0 }
  }
  visible.value = true
}

const save = async () => {
  if (!form.value.name || !form.value.phone || !form.value.address) {
    ElMessage.warning('请填写完整信息')
    return
  }
  saving.value = true
  try {
    if (editing.value) {
      await addressApi.update(editing.value.id, form.value)
      ElMessage.success('地址已更新')
    } else {
      await addressApi.create(form.value)
      ElMessage.success('地址已添加')
    }
    visible.value = false
    await loadAddresses()
  } catch (e) {}
  saving.value = false
}

const deleteAddr = async (id) => {
  await ElMessageBox.confirm('确定删除该地址吗？', '提示', { type: 'warning' })
  try {
    await addressApi.delete(id)
    ElMessage.success('已删除')
    await loadAddresses()
  } catch (e) {}
}

const setDefault = async (id) => {
  try {
    await addressApi.setDefault(id)
    ElMessage.success('已设为默认地址')
    await loadAddresses()
  } catch (e) {}
}
</script>

<style scoped>
.address-page { padding: 24px; max-width: 800px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h2 { margin: 0; font-size: 22px; }
.address-item { padding: 16px; border: 1px solid var(--el-border-color); border-radius: 8px; margin-bottom: 12px; display: flex; gap: 16px; align-items: flex-start; }
.address-item.default { border-color: var(--paw-coral); background: var(--paw-coral-light); }
.addr-info { flex: 1; }
.addr-contact { margin-bottom: 6px; }
.addr-phone { margin-left: 12px; color: #909399; font-size: 14px; }
.addr-detail { color: #606266; font-size: 14px; }
.addr-actions { display: flex; flex-direction: column; gap: 4px; flex-shrink: 0; }
.addr-tags { margin-bottom: 4px; }
</style>
