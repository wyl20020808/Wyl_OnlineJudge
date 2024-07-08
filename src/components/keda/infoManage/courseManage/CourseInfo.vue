<template>
    <a-row>
      <a-col :span="22">
        <el-table :data="courseList" border stripe>
          <el-table-column prop="courseName" label="课程名称"></el-table-column>
          <el-table-column prop="className" label="上课班级"></el-table-column>
          <el-table-column prop="mainTeacher" label="主讲老师"></el-table-column>
          <el-table-column prop="courseDate" label="课程日期"></el-table-column>
          <el-table-column prop="courseTime" label="上课时间"></el-table-column>
          <el-table-column label="操作">
            <template #default="{ row }">
              <el-button type="text" @click="handleRowClick(row)">查看课程详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </a-col>
      <a-col>
        <el-button plain @click="dialogFormVisible = true">添加课程</el-button>
      </a-col>
    </a-row>
  
    <el-dialog title="课程详情" v-model="dialogVisible" width="50%">
      <el-table :data="studentList" border>
        <el-table-column prop="studentid" label="学生ID"></el-table-column>
        <el-table-column prop="name" label="学生姓名"></el-table-column>
        <el-table-column prop="age" label="学生年龄"></el-table-column>
        <el-table-column prop="sex" label="学生性别"></el-table-column>
      </el-table>
    </el-dialog>
  
    <el-dialog v-model="dialogFormVisible" title="添加课程" width="500">
      <addNewCourse ref="addNewCourseRef" />
      <template #footer>
        <div class="dialog-footer">
          <el-button type="danger" style="color: white" @click="dialogFormVisible = false">取消</el-button>
          <el-button @click="addCourse()">添加</el-button>
        </div>
      </template>
    </el-dialog>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import addNewCourse from './addCourseInfo.vue'; // Assuming you have a component for adding new courses
  import axios from 'axios';
  import { SERVER_URL } from '@/js/functions/config';
  
  const courseList = ref([]);
  const studentList = ref([]);
  const dialogVisible = ref(false);
  
  const dialogFormVisible = ref(false);
  const addNewCourseRef = ref(null);
  
  const addCourse = () => {
    dialogFormVisible.value = false;
    if (addNewCourseRef.value) {
      addNewCourseRef.value.submitForm(); // Assuming your addNewCourse component has a method named submitForm for form submission
    }
  };
  
  const fetchCourseList = async () => {
    try {
      const response = await axios.get(`${SERVER_URL}/course/query`);
      courseList.value = response.data.map(courseItem => ({
        ...courseItem,
        courseTime: `${courseItem.startTime} ~ ${courseItem.endTime}`
      }));
    } catch (error) {
      console.error('Error fetching course list:', error);
    }
  };
  
  // 获取课程同学列表
  const fetchStudentList = async (courseId) => {
    try {
      const response = await axios.get(`${SERVER_URL}/course/query`);
      studentList.value = response.data;
      dialogVisible.value = true;
    } catch (error) {
      console.error('Error fetching student list:', error);
    }
  };
  
  onMounted(async () => {
    fetchCourseList();
  });
  
  // 点击行展开按钮
  const handleRowClick = (row) => {
    fetchStudentList(row.id);
  };
  </script>
  
  <style scoped>
  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
  }
  </style>
  