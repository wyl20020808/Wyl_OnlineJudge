<template>
  <!-- <div style="margin-bottom: 5px;margin-top: 30px;">
    <el-switch v-model="isCollapse"></el-switch>
  </div>
  <el-menu
    default-active="2"
    class="el-menu-vertical-demo"
    :collapse="!isCollapse"
    @open="handleOpen"
    @close="handleClose"
  >
    <el-menu-item @click="chooseItem = 'myjobs'" index="1">
      <el-icon><List /></el-icon>
      <template #title>
        <span>我的任务</span>
      </template>
    </el-menu-item>
    <el-menu-item index="2">
      <el-icon><icon-menu /></el-icon>
      <template #title>Navigator Two</template>
    </el-menu-item>
    <el-menu-item index="3" disabled>
      <el-icon><document /></el-icon>
      <template #title>Navigator Three</template>
    </el-menu-item>
    <el-menu-item index="4">
      <el-icon><setting /></el-icon>
      <template #title>Navigator Four</template>
    </el-menu-item>
  </el-menu> -->
 <!-- <a-row>
  <iframe  style="width:1000px;height: 500px" src="https://kedaoi.cn/p"></iframe>
 </a-row> -->
  <a-row style="justify-content: center;align-items: center; margin-top: 20px;">
    <a-col :span="18" >
      <codeEditorComponent
            ref="editor"
            :problemsample="[]"
            :problemcontent="123"
          />

    </a-col>
  </a-row>
</template>

<script setup>
import { ref } from "vue";
import codeEditorComponent from "../component/code/codeEditorComponent.vue"
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
  List,
} from "@element-plus/icons-vue";
const chooseItem = ref("myjobs");
const isCollapse = ref(false);
const handleOpen = (key, keyPath) => {
  console.log(key, keyPath);
};
const handleClose = (key, keyPath) => {
  console.log(key, keyPath);
};
</script>

<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>
