<template>
  <div class="admin-page">
    <div class="page-head">
      <h2>📋 订单管理</h2>
    </div>

    <el-table :data="orders" v-loading="loading" border stripe>
      <el-table-column prop="orderNo" label="订单号" width="200" show-overflow-tooltip />
      <el-table-column prop="userId" label="用户ID" width="80" />
      <el-table-column label="状态" width="110">
        <template #default="{ row }">
          <el-tag :type="statusTag(row.status)" size="small">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="receiver" label="收货人" width="90" />
      <el-table-column label="金额" width="100">
        <template #default="{ row }">¥{{ row.totalAmount }}</template>
      </el-table-column>
      <el-table-column label="商品" min-width="160">
        <template #default="{ row }">
          <span v-for="(item, i) in row.items" :key="i">
            {{ item.productName }}×{{ item.quantity }}<span v-if="i < row.items.length - 1">, </span>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="退单原因" min-width="120" show-overflow-tooltip>
        <template #default="{ row }">
          <span v-if="row.returnReason" style="color:#f56c6c;font-size:12px;">{{ row.returnReason }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" fixed="right">
        <template #default="{ row }">
          <template v-if="row.status === 1">
            <el-button link type="primary" size="small" @click="ship(row.id)">发货</el-button>
          </template>
          <template v-if="row.status === -2">
            <el-button link type="success" size="small" @click="returnReview(row.id, true)">同意退单</el-button>
            <el-button link type="danger" size="small" @click="returnReview(row.id, false)">驳回</el-button>
          </template>
          <template v-if="row.status === 3">
            <el-button link type="danger" size="small" @click="directReturn(row)">直接退单</el-button>
          </template>
          <el-select v-else-if="row.status >= 0" :model-value="String(row.status)" @change="(v) => updateStatus(row.id, v)" size="small" style="width:100px">
            <el-option v-for="s in statusOptions" :key="s.value" :label="s.label" :value="s.value" />
          </el-select>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="returnReasonVisible" title="直接退单" width="400px">
      <el-input v-model="returnReason" type="textarea" :rows="2" placeholder="请输入退单理由..." />
      <template #footer>
        <el-button @click="returnReasonVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDirectReturn" :loading="acting">确认退单</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { orderApi } from '../../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const orders = ref([])
const loading = ref(false)
const acting = ref(false)
const returnReasonVisible = ref(false)
const returnReason = ref('')
const currentOrderId = ref(null)

const loadOrders = async () => { loading.value = true; try { orders.value = await orderApi.adminList() } catch (e) {}; loading.value = false }
onMounted(loadOrders)

const statusText = (s) => {
  const map = { 0: '待支付', 1: '已支付', 2: '已发货', 3: '已收货', 4: '已完成', '-1': '已取消', '-2': '退单中', '-4': '已退单' }
  return map[String(s)] || '未知'
}

const statusTag = (s) => {
  const map = { 0: 'warning', 1: '', 2: 'info', 3: 'success', 4: 'success', '-1': 'info', '-2': 'warning', '-4': 'info' }
  return map[String(s)] || 'info'
}

const statusOptions = [
  { label: '待支付', value: '0' }, { label: '已支付', value: '1' }, { label: '已发货', value: '2' },
  { label: '已收货', value: '3' }, { label: '已完成', value: '4' }
]

const ship = async (id) => {
  await ElMessageBox.confirm('确认已发货？', '发货确认', { type: 'info' })
  try { await orderApi.ship(id); ElMessage.success('已发货'); loadOrders() } catch (e) {}
}

const returnReview = async (id, approved) => {
  await ElMessageBox.confirm(`确认${approved ? '同意退单' : '驳回退单申请'}？`, '退单审核', { type: approved ? 'success' : 'warning' })
  acting.value = true
  try { await orderApi.returnReview(id, approved); ElMessage.success(approved ? '退单审核通过' : '退单已驳回'); loadOrders() } catch (e) {}
  acting.value = false
}

const directReturn = (row) => {
  currentOrderId.value = row.id
  returnReason.value = ''
  returnReasonVisible.value = true
}

const confirmDirectReturn = async () => {
  if (!returnReason.value.trim()) { ElMessage.warning('请输入退单理由'); return }
  acting.value = true
  try {
    await orderApi.directReturn(currentOrderId.value, returnReason.value)
    ElMessage.success('已退单')
    returnReasonVisible.value = false
    loadOrders()
  } catch (e) {}
  acting.value = false
}

const updateStatus = async (id, status) => {
  try { await orderApi.updateStatus(id, status); ElMessage.success('状态已更新'); loadOrders() } catch (e) {}
}
</script>

<style scoped>
.admin-page { padding: 20px; }
.page-head { margin-bottom: 20px; }
.page-head h2 { margin: 0; font-size: 20px; }
</style>
