<template>
  <el-form :model="productForm" :rules="rules" ref="productFormRef" label-width="120px" style="margin-top: 20px;">
    <el-form-item label="商品名称" prop="name">
      <el-input v-model="productForm.name"></el-input>
    </el-form-item>
    <el-form-item label="商品描述" prop="description">
      <el-input type="textarea" v-model="productForm.description"></el-input>
    </el-form-item>
    <el-form-item label="商品价格" prop="price">
      <el-input v-model="productForm.price"></el-input>
    </el-form-item>
    <el-form-item label="商品库存" prop="stock">
      <el-input v-model="productForm.stock"></el-input>
    </el-form-item>
    <el-form-item label="商品标签" prop="tags">
      <el-input v-model="productForm.tags"></el-input>
    </el-form-item>
    <el-form-item label="上架时间" prop="launchDate">
      <el-date-picker
        v-model="productForm.launchDate"
        type="date"
        placeholder="选择日期"
        style="width: 100%"
        :picker-options="{ firstDayOfWeek: 1 }"
      ></el-date-picker>
    </el-form-item>

    <el-form-item label="商品图片" prop="images">
      <input type="file" multiple @change="handleFilesUpload" />
      <div class="preview" v-if="filePreviews.length">
        <div v-for="(file, index) in filePreviews" :key="index" class="preview-item">
          <img :src="file.url" alt="Image preview" />
          <el-button type="danger" icon="el-icon-delete" @click="removeFile(index)">删除</el-button>
        </div>
      </div>
    </el-form-item>
    <el-form-item label="包邮" prop="freeShipping">
      <a-switch v-model:checked="productForm.freeShipping"></a-switch>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm">提交</el-button>
      <el-button @click="resetForm">重置</el-button>
    </el-form-item>
  </el-form>
</template>
<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { SERVER_URL } from '@/js/functions/config';
import { Switch } from 'ant-design-vue';
import { useStore } from 'vuex';

const store = useStore();
const initialProductForm = {
  name: '',
  price: '',
  stock: '',
  tags: '',
  launchDate: '',
  description: '',
  images: [],
  freeShipping: false
};

const productForm = ref({ ...initialProductForm });
const filePreviews = ref([]);
const files = ref([]);

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入商品价格', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入商品库存', trigger: 'blur' }],
  tags: [{ required: true, message: '请输入商品标签', trigger: 'blur' }],
  launchDate: [{ required: true, message: '请选择上架时间', trigger: 'change' }],
  description: [{ required: true, message: '请输入商品描述', trigger: 'blur' }],
  images: [{ required: true, message: '请上传商品图片', trigger: 'change' }],
  freeShipping: [{ required: true, message: '请选择是否包邮', trigger: 'change' }],
};

const productFormRef = ref(null);

const handleFilesUpload = (event) => {
  const selectedFiles = Array.from(event.target.files);
  selectedFiles.forEach(file => {
    files.value.push(file);
    const reader = new FileReader();
    reader.onload = (e) => {
      filePreviews.value.push({ url: e.target.result, file });
    };
    reader.readAsDataURL(file);
  });
};

const removeFile = (index) => {
  filePreviews.value.splice(index, 1);
  files.value.splice(index, 1);
};

const submitForm = async () => {
  axios({
  method: 'post',
  url: 'https://api.gpts.vin/v1/chat/completions/',
  headers: {
    'Authorization': 'Bearer sk-CJFWmw6jUnXhX2EYF60670A0317048A69eB0025e32A655C5',
    'Content-Type': 'application/json'
  },
  data: {
    model: "gpt-3.5-turbo",
    messages: [
      {
        role: "user",
        content: "说个笑话"
      }
    ]
  }
})
.then(response => {
  console.log(response.data);
})
.catch(error => {
  console.error('Error:', error);
});
  const formData = new FormData();
  files.value.forEach(file => {
    formData.append('files', file);
  });

  // 将日期转换为标准格式的北京时间
  const launchDate = new Date(productForm.value.launchDate).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  }).replace(/\//g, '-');

  const payload = {
    ...productForm.value,
    launchDate,
    images: [] // 初始设为空，待上传图片路径返回后填充
  };

  try {
    const uploadResponse = await axios.post(`${SERVER_URL}/product/upload`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });

    const uploadedPaths = uploadResponse.data; // 假设服务器返回一个文件路径数组
    payload.images = JSON.stringify(uploadedPaths);

    const response = await axios.post(`${SERVER_URL}/product/add`, payload);
    console.log('商品添加成功:', response.data);
    store.dispatch("notice", {
          title: "商品添加成功！",
          message: "",
          type: "success",
        });
    resetForm();
  } catch (error) {
    console.error('添加商品出错:', error);
  }
};

const resetForm = () => {
  productFormRef.value.resetFields();
  filePreviews.value = [];
  files.value = [];
};
</script>
<style scoped>
.preview {
  display: flex;
  flex-wrap: wrap;
}
.preview-item {
  position: relative;
  margin-right: 10px;
  margin-bottom: 10px;
}
.preview-item img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border: 1px solid #dcdcdc;
  border-radius: 4px;
}
.preview-item .el-button {
  position: absolute;
  top: 0;
  right: 0;
  padding: 2px 5px;
  background: rgba(255, 0, 0, 0.7);
  border: none;
  color: white;
  cursor: pointer;
  border-radius: 50%;
}
</style>
