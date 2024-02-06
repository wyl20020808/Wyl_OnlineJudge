<template>
  <a-row style="justify-content: center">
    <a-row
      style="
        width: 95%;
        min-height: 600px;
        background-color: white;
        margin: 10px;
        padding: 10px;
        border-radius: 10px;

      "
    >
      <a-col>
        <a-menu
          id="dddddd"
          v-model:openKeys="openKeys"
          v-model:selectedKeys="selectedKeys"
          style="width: 256px"
          mode="inline"
          :items="items"
          @click="handleClick"
        ></a-menu>
      </a-col>
      <a-col v-if="selectedKeys[0] === '1'" style="padding: 20px; width: 82%;">
        <preview />
      </a-col>
      <a-col v-else-if="selectedKeys[0] === '2'" style="padding: 20px; width: 82%;">
        <!-- 替换为对应的组件或内容 -->
        <documentToPDF />
      </a-col>
      <a-col v-else-if="selectedKeys[0] === '3'" style="padding: 20px; width: 82%;">
        <!-- 替换为对应的组件或内容 -->
        <imageToPDF />
      </a-col>
      <a-col v-else-if="selectedKeys[0] === '4'" style="padding: 20px; width: 82%;">
        <!-- 替换为对应的组件或内容 -->
        <ExcelEdit />
      </a-col>
      <a-col v-else-if="selectedKeys[0] === '5'" style="padding: 20px; width: 82%;">
        <!-- 替换为对应的组件或内容 -->
        <PDFToImage />
      </a-col>
      <a-col v-else-if="selectedKeys[0] === '6'" style="padding: 20px; width: 82%;">
        <!-- 替换为对应的组件或内容 -->
        <PPTEdit />
      </a-col>
      <a-col v-else-if="selectedKeys[0] === '7'" style="padding: 20px; width: 82%;">
        <!-- 替换为对应的组件或内容 -->
        <PDFPreview />
      </a-col>
      <a-col v-else-if="selectedKeys[0] === '8'" style="padding: 20px; width: 82%;">
        <!-- 替换为对应的组件或内容 -->
        <ImagePreview />
      </a-col>
      
    </a-row>
  </a-row>
</template>

<script setup>
import { reactive, ref, watch, h } from "vue";
import {
  MailOutlined,
  AppstoreOutlined,
  SettingOutlined,
} from "@ant-design/icons-vue";
import preview from "./functions/preview";
import documentToPDF from "./functions/documentToPDF";
import imageToPDF from "./functions/imgeToPDF";
import ExcelEdit from "./functions/ExcelEdit";
import PPTEdit from "./functions/PPTEdit";
import PDFToImage from "./functions/PDFToImage";
import ImagePreview from "./functions/ImagePreview";
import PDFPreview from "./functions/PDFPreview";
const selectedKeys = ref(["1"]);
const openKeys = ref(["sub1"]);
function getItem(label, key, icon, children, type) {
  return {
    key,
    icon,
    children,
    label,
    type,
  };
}
const items = reactive([
  getItem("预览文件", "1"),
  getItem("文档转PDF", "2"),
  getItem("图片转PDF", "3"),
  getItem("EXCEL编辑", "4"),
  getItem("PDF转图片", "5"),
  getItem("PPT编辑", "6"),
  getItem("PDF预览", "7"),
  getItem("图片预览", "8"),
]);
const handleClick = (e) => {
  console.log("click", e);
};
watch(openKeys, (val) => {
  console.log("openKeys", val);
});
</script>

<style scoped>
</style>