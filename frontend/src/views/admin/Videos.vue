<template>
  <div class="admin-page">
    <div class="page-head">
      <h2>🎬 视频管理</h2>
      <el-button type="primary" @click="showDialog(null)">上传视频</el-button>
    </div>

    <el-table :data="videos" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column label="分类" width="100">
        <template #default="{ row }">{{ categoryName(row.category) }}</template>
      </el-table-column>
      <el-table-column prop="viewCount" label="播放量" width="80" />
      <el-table-column label="关联商品" width="80">
        <template #default="{ row }">{{ row.productId ? '是' : '否' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button link type="primary" @click="showDialog(row)">编辑</el-button>
          <el-button link type="danger" @click="deleteVideo(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="editing ? '编辑视频' : '上传视频'" v-model="visible" width="500px" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" placeholder="视频标题" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" placeholder="选择分类" style="width:100%">
            <el-option label="萌宠日常" value="PET_PLAY" />
            <el-option label="喂养指南" value="PET_FOOD" />
            <el-option label="宠物护理" value="PET_CARE" />
            <el-option label="商品介绍" value="PRODUCT_INTRO" />
          </el-select>
        </el-form-item>
        <el-form-item label="视频URL">
          <el-input v-model="form.videoUrl" placeholder="视频链接地址" />
        </el-form-item>
        <el-form-item label="封面URL">
          <el-input v-model="form.coverUrl" placeholder="封面图片链接" />
        </el-form-item>
        <el-form-item label="关联商品ID">
          <el-input v-model="form.productId" placeholder="填商品ID，留空则不关联" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="视频描述..." />
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
import { videoApi } from '../../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const videos = ref([])
const loading = ref(false)
const visible = ref(false)
const editing = ref(null)
const saving = ref(false)
const form = ref({ title: '', category: '', videoUrl: '', coverUrl: '', productId: null, description: '' })

onMounted(async () => { loading.value = true; try { const data = await videoApi.list(0, 100); videos.value = data.content || [] } catch (e) {}; loading.value = false })

const showDialog = (video) => {
  editing.value = video
  form.value = video ? { ...video } : { title: '', category: '', videoUrl: '', coverUrl: '', productId: null, description: '' }
  visible.value = true
}

const save = async () => {
  if (!form.value.title) { ElMessage.warning('请输入标题'); return }
  saving.value = true
  try {
    if (editing.value) {
      await videoApi.update(editing.value.id, form.value)
    } else {
      await videoApi.create(form.value)
    }
    ElMessage.success(editing.value ? '已更新' : '已创建')
    visible.value = false
    const data = await videoApi.list(0, 100)
    videos.value = data.content || []
  } catch (e) {}
  saving.value = false
}

const deleteVideo = async (id) => {
  await ElMessageBox.confirm('确定删除该视频吗？', '提示', { type: 'warning' })
  try { await videoApi.delete(id); ElMessage.success('已删除'); const data = await videoApi.list(0, 100); videos.value = data.content || [] } catch (e) {}
}

const categoryName = (c) => ({ PET_PLAY: '萌宠日常', PET_FOOD: '喂养指南', PET_CARE: '宠物护理', PRODUCT_INTRO: '商品介绍' }[c] || c)
</script>

<style scoped>
.admin-page { padding: 20px; }
.page-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-head h2 { margin: 0; font-size: 20px; }
</style>
