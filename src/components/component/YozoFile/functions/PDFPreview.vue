<template>
  <a-row
    style="min-width: 90%; min-height: 80%; padding: 20px"
    class="shadow-border"
  >
    <a-col :span="24">
      <a-row
        class="alert alert-primary"
        role="alert"
        style="
          width: 85%;
          height: 17%;
          border-left: 5px solid rgb(61, 167, 243);
          background-color: rgb(255, 255, 255);
        "
      >
        <div style="font-weight: bold; font-size: 20px">PDF预览功能</div>
      </a-row>
      <a-row style="align-items: center">
        <div style="color: red">*</div>
        <div style="padding: 5px">选择文件：</div>
        <a-upload :before-upload="beforeUpload" :file-list="fileList">
          <a-button>
            <UploadOutlined style="position: relative; bottom: 4px" />
            点击上传PDF
          </a-button>
        </a-upload>
      </a-row>
      <a-row style="margin-top: 10px; margin-bottom: 10px">
        <a-checkbox v-model:checked="addWm">加入水印</a-checkbox>
      </a-row>
      <a-row v-if="addWm">
        <a-textarea
          v-model:value="wm"
          placeholder="输入水印内容"
          allow-clear
        />
      </a-row>
      <a-row>
        <a-button @click="uploadFile" style="margin-top: 10px"
          >预览PDF</a-button
        >
      </a-row>
    </a-col>
  </a-row>
</template>
    
    <script setup>
import { ref } from "vue";
import { UploadOutlined } from "@ant-design/icons-vue";
import { message } from "ant-design-vue";
import axios from "axios";
import { SERVER } from "@/js/functions/config";
const SERVER_URL = SERVER + ":88/dp";

const fileList = ref([]);

//
const addWm = ref(false);
const wm = ref('')
//

const beforeUpload = (file) => {
  fileList.value = [file]; // 只保留最新的文件
  return false; // 阻止自动上传
};

const uploadFile = async () => {
  if (fileList.value.length > 0) {
    const formData = new FormData();
    formData.append("file", fileList.value[0]); // 添加文件到表单数据中
    formData.append("hasWm", addWm.value); // 添加只读模式到表单数据中
    formData.append("wmContent", wm.value); // 添加加入用户信息到表单数据中

  
    console.log(formData.get("userInfo"), "userinfo");
    console.log(formData.get("readOnly"), "readOnly");
    try {
      const response = await axios.post(
        `${SERVER_URL}/file/pdf-view/local`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            // 这里添加其他必要的头部信息，例如认证头部
          },
        }
      );
      // 处理响应
      message.success("文件上传成功");
      console.log(response.data);
      // 假设后端返回的是预览文件的URL
      const previewUrl = response.data;
      window.open(previewUrl, "_blank"); // 在新标签页中打开预览URL
    } catch (error) {
      message.error("文件上传失败");
      console.error(error);
    }
  } else {
    message.warning("请选择文件");
  }
};
</script>
    
    <style scoped>
.shadow-border {
  border: 1px solid #e0e0e0; /* 添加边框，颜色可以根据需要更改 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影，可以根据需要调整 */
  border-radius: 5px;
}
</style>
    