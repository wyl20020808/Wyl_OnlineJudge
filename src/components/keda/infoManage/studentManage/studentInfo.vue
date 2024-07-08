<template>
  <a-row >
    <a-col :span="20">
      <el-table :data="studentList" border stripe>
        <el-table-column prop="studentid" label="学生ID"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="age" label="年龄"></el-table-column>
        <el-table-column prop="sex" label="性别"></el-table-column>
        <el-table-column prop="classes" label="班级"></el-table-column>
        <el-table-column prop="headmaster" label="班主任"></el-table-column>
      </el-table>
    </a-col>
    <a-col>
      <el-button plain @click="dialogFormVisible = true"> 添加学员 </el-button>
    </a-col>
  </a-row>

  <el-dialog v-model="dialogFormVisible" title="学员信息" width="500">
    <addStudentInfo ref="addStudentInfoRef" />
    <template #footer>
      <div class="dialog-footer">
        <el-button
          type="danger"
          style="color: white"
          @click="dialogFormVisible = false"
          >取消</el-button
        >
        <el-button @click="addStudentInfos()"> 添加 </el-button>
      </div>
    </template>
  </el-dialog>
</template>
  
  <script setup>
import { ref,onMounted } from "vue";
import addStudentInfo from "./addStudentInfo.vue";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
// 定义一个ref变量，用于引用子组件实例
const dialogFormVisible = ref(false);
const addStudentInfoRef = ref(null);
const studentList = ref([]);

import WebSocketService from "@/websocket";

const fetchStudentList = async () => {
  try {
    const response = await axios.get(`${SERVER_URL}/student/query`);
    studentList.value = response.data;
  } catch (error) {
    console.error("Error fetching student list:", error);
  }
};

onMounted(async () => {
  await fetchStudentList(); // 初始加载学员信息
 
});


// 定义一个方法，调用子组件的submitForm方法
const addStudentInfos = () => {
  dialogFormVisible.value = false;
  if (addStudentInfoRef.value) {
    // console.log("hasjdhasd")
    addStudentInfoRef.value.submitForm(); //调用父组件方法
  }
};


</script>
  
  <style scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>
  