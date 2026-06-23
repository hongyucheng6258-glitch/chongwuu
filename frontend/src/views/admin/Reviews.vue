<template>
  <div class="admin-page">
    <div class="page-head">
      <h2>⭐ 评价管理</h2>
    </div>

    <el-table :data="reviews" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="productId" label="商品ID" width="80" />
      <el-table-column prop="orderId" label="订单ID" width="80" />
      <el-table-column label="评分" width="180">
        <template #default="{ row }">
          <el-rate :model-value="row.rating" disabled show-score />
        </template>
      </el-table-column>
      <el-table-column prop="content" label="评价内容" min-width="250" show-overflow-tooltip />
      <el-table-column prop="createdAt" label="评价时间" width="170">
        <template #default="{ row }">{{ row.createdAt ? row.createdAt.replace('T', ' ').substring(0, 19) : '' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="80">
        <template #default="{ row }">
          <el-button link type="danger" @click="deleteReview(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { reviewApi } from '../../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const reviews = ref([])
const loading = ref(false)

onMounted(async () => { loading.value = true; try { reviews.value = await reviewApi.adminList() } catch (e) {}; loading.value = false })

const deleteReview = async (id) => {
  await ElMessageBox.confirm('确定删除该评价吗？', '提示', { type: 'warning' })
  try { await reviewApi.delete(id); ElMessage.success('已删除'); reviews.value = reviews.value.filter(r => r.id !== id) } catch (e) {}
}
</script>

<style scoped>
.admin-page { padding: 20px; }
.page-head { margin-bottom: 20px; }
.page-head h2 { margin: 0; font-size: 20px; }
</style>
