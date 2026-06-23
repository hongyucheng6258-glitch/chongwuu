<template>
  <div class="products-admin">
    <div class="toolbar">
      <el-button type="primary" :icon="Plus" @click="openDialog()">新增商品</el-button>
      <el-input v-model="searchKey" placeholder="搜索商品名称" style="width: 240px" clearable @clear="loadProducts" @keyup.enter="loadProducts">
        <template #append><el-button :icon="Search" @click="loadProducts" /></template>
      </el-input>
    </div>

    <el-table :data="products" v-loading="loading" border stripe>
      <el-table-column label="ID" prop="id" width="60" />
      <el-table-column label="图片" width="80">
        <template #default="{ row }">
          <img :src="row.image" class="table-img" @error="imgError" v-if="row.image" />
          <span v-else>🐾</span>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" prop="name" min-width="180" show-overflow-tooltip />
      <el-table-column label="分类" width="100">
        <template #default="{ row }">{{ categoryName(row.categoryId) }}</template>
      </el-table-column>
      <el-table-column label="价格" prop="price" width="100">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column label="库存" prop="stock" width="80" />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '上架' : '下架' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button text type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button text type="danger" @click="del(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        background
        layout="prev, pager, next, total"
        :total="total"
        :page-size="pageSize"
        v-model:current-page="currentPage"
        @current-change="loadProducts"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="editId ? '编辑商品' : '新增商品'" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="商品名称" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="选择分类" clearable>
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="form.stock" :min="0" />
        </el-form-item>
        <el-form-item label="图片URL">
          <el-input v-model="form.image" placeholder="商品图片地址" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="上架" inactive-text="下架" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus, Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { productApi, categoryApi } from '../../api'

const products = ref([])
const categories = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchKey = ref('')

const dialogVisible = ref(false)
const editId = ref(null)
const submitting = ref(false)
const formRef = ref()

const form = reactive({
  name: '', categoryId: null, price: 0, stock: 0, description: '', image: '', status: 1
})

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }]
}

const categoryName = (id) => categories.value.find(c => c.id === id)?.name || '-'

const loadProducts = async () => {
  loading.value = true
  try {
    const data = await productApi.adminList({ page: currentPage.value - 1, size: pageSize.value })
    let list = data.content || []
    if (searchKey.value) {
      list = list.filter(p => p.name.includes(searchKey.value))
    }
    products.value = list
    total.value = data.totalElements || 0
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  categories.value = await categoryApi.list()
}

const openDialog = (row) => {
  editId.value = row?.id || null
  Object.assign(form, {
    name: row?.name || '', categoryId: row?.categoryId || null, price: row?.price || 0,
    stock: row?.stock || 0, description: row?.description || '', image: row?.image || '',
    status: row?.status ?? 1
  })
  dialogVisible.value = true
}

const submit = async () => {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (editId.value) {
      await productApi.update(editId.value, { ...form })
      ElMessage.success('更新成功')
    } else {
      await productApi.create({ ...form })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadProducts()
  } finally {
    submitting.value = false
  }
}

const del = (row) => {
  ElMessageBox.confirm(`确定删除商品【${row.name}】吗？`, '提示', { type: 'warning' })
    .then(async () => {
      await productApi.delete(row.id)
      ElMessage.success('已删除')
      loadProducts()
    })
    .catch(() => {})
}

const imgError = (e) => {
  e.target.style.display = 'none'
}

onMounted(async () => {
  await loadCategories()
  loadProducts()
})
</script>

<style scoped>
.products-admin {
  background: var(--paw-card);
  border-radius: var(--paw-radius-lg);
  padding: 24px;
  box-shadow: var(--paw-shadow-sm);
  border: 1px solid var(--paw-border-light);
}

.toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.table-img {
  width: 50px;
  height: 50px;
  border-radius: var(--paw-radius-sm);
  object-fit: cover;
  border: 1px solid var(--paw-border-light);
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
