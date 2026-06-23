<template>
  <div class="categories-admin">
    <div class="toolbar">
      <el-button type="primary" :icon="Plus" @click="openDialog()">新增分类</el-button>
    </div>

    <el-table :data="categories" v-loading="loading" border stripe>
      <el-table-column label="ID" prop="id" width="80" />
      <el-table-column label="图标" width="80">
        <template #default="{ row }"><span class="cat-icon">{{ row.icon }}</span></template>
      </el-table-column>
      <el-table-column label="分类名称" prop="name" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button text type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button text type="danger" @click="del(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑分类' : '新增分类'" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="图标">
          <el-input v-model="form.icon" placeholder="输入 Emoji，如 🐱" />
        </el-form-item>
        <el-form-item label="分类名称">
          <el-input v-model="form.name" placeholder="分类名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { categoryApi } from '../../api'

const categories = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const editId = ref(null)

const form = reactive({ name: '', icon: '' })

const load = async () => {
  loading.value = true
  try {
    categories.value = await categoryApi.list()
  } finally {
    loading.value = false
  }
}

const openDialog = (row) => {
  editId.value = row?.id || null
  form.name = row?.name || ''
  form.icon = row?.icon || ''
  dialogVisible.value = true
}

const submit = async () => {
  if (!form.name.trim()) {
    ElMessage.warning('请输入分类名称')
    return
  }
  if (editId.value) {
    await categoryApi.update(editId.value, { ...form })
    ElMessage.success('更新成功')
  } else {
    await categoryApi.create({ ...form })
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  load()
}

const del = (row) => {
  ElMessageBox.confirm(`确定删除分类【${row.name}】吗？`, '提示', { type: 'warning' })
    .then(async () => {
      await categoryApi.delete(row.id)
      ElMessage.success('已删除')
      load()
    })
    .catch(() => {})
}

onMounted(load)
</script>

<style scoped>
.categories-admin {
  background: var(--paw-card);
  border-radius: var(--paw-radius-lg);
  padding: 24px;
  box-shadow: var(--paw-shadow-sm);
  border: 1px solid var(--paw-border-light);
}

.toolbar {
  margin-bottom: 20px;
}

.cat-icon {
  font-size: 24px;
}
</style>
