<template>
  <div style="width: 100%; margin-top: 20px">
    <a-row>
      <a-col :span="3" class="menu-container" style="align-items: center;">
        <el-menu
        style="border-radius: 3px;"
          default-active="1-1"
          class="el-menu-vertical-demo"
          :collapse="isCollapse"
          @open="handleOpen"
          @close="handleClose"
        >
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Location /></el-icon>
              <span>学员管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="1-1" @click="chooseComponent = studentInfo">学员信息管理</el-menu-item>
              <el-menu-item index="1-2" @click="chooseComponent = classInfo"> 班级管理</el-menu-item>
              <el-menu-item index="1-3" @click="chooseComponent = courseInfo">课程管理</el-menu-item>
            </el-menu-item-group>
            <el-sub-menu index="1-4">
              <template #title>其他信息</template>
              <el-menu-item index="1-4-1" @click="chooseComponent = studentSolvedSituation">做题情况</el-menu-item>
            </el-sub-menu>
          </el-sub-menu>
          <el-menu-item index="2">
            <el-icon><IconMenu /></el-icon>
            <span>导航二</span>
          </el-menu-item>
          <el-menu-item index="3" disabled>
            <el-icon><Document /></el-icon>
            <span>导航三</span>
          </el-menu-item>
          <el-menu-item index="4">
            <el-icon><Setting /></el-icon>
            <span>导航四</span>
          </el-menu-item>
        </el-menu>
        <!-- 开关 -->
        <!-- <div class="switch-container"> 
          <el-switch 
            size="large"
            :active-value="false"
            :inactive-value="true"
            :active-action-icon="View"
            :inactive-action-icon="Hide"
            v-model="isCollapse"
          />
        </div> -->
      </a-col>
      <a-col :span="20"> 
        <component :is="chooseComponent"></component>
      </a-col>
    </a-row>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { Hide, View } from "@element-plus/icons-vue";
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
} from "@element-plus/icons-vue";

import studentInfo from "./studentManage/studentInfo.vue"
import classInfo from "./classManage/classInfo.vue"
import courseInfo from "./courseManage/CourseInfo.vue"
import studentSolvedSituation from "./studentManage/studentSolvedSituation.vue"

const chooseComponent = ref(studentInfo)

const isCollapse = ref(false);
const handleOpen = (key, keyPath) => {
  console.log("open:", key, keyPath);
};
const handleClose = (key, keyPath) => {
  console.log("close:", key, keyPath);
};
</script>

<style scoped>
.menu-container {
  position: relative;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 150px;
  min-height: 250px;
}
.el-menu-vertical-demo.el-menu--collapse {
  width: 70px; /* 设置折叠时的宽度 */
  min-height: 250px; /* 这里可以根据需要调整折叠时的最小高度 */
}
.el-menu-item-group__title,
.el-sub-menu__title {
  margin: 0 !important;
  padding: 0 !important;
}
.el-menu-item {
  margin: 0 !important;
}
.switch-container {
  position: absolute;
  bottom: -0px;
  left: 10px;
  align-content: center;
}
</style>
