<template>
  <a-row style="margin-top: 20px;align-items: center;justify-content: center;">
    <a-col  :xs="24" :sm="24" :md="16" :lg="16" :xl="16">
      <h2>直播课列表</h2>
      <a-spin :spinning="loading" size="large">
        <div v-for="courseInfo in courseList" :key="courseInfo.classId" class="class-item">
          <a-card :title= courseInfo.courseName  style="margin-bottom: 16px;">
            <p><strong>上课班级：</strong> {{ courseInfo.className }}</p>
            <p><strong>主讲老师：</strong> {{ courseInfo.mainTeacher }}</p>
            <p><strong>上课时间：</strong> {{ courseInfo.startTime }} - {{ courseInfo.endTime }}</p>
            <a-button style="color: white;" type="primary" @click="enterLiveClass(courseInfo)">进入教室</a-button>
          </a-card>
        </div>
      </a-spin>
    </a-col>

  </a-row>
</template>

<script setup>
import { ref } from 'vue';
import { onMounted } from 'vue';
import router from '@/router/router';
import { Spin, Row, Col, Card, Button, message } from 'ant-design-vue';
import axios from 'axios';
import { SERVER_URL } from '@/js/functions/config';

// Register Ant Design Vue components globally or locally
const components = {
  Spin,
  Row,
  Col,
  Card,
  Button,
};

const courseList = ref([]);
const loading = ref(false);

onMounted(() => {
  fetchClassList();
});

const fetchClassList = async () => {
  loading.value = true;
  try {
    const response = await axios.get(`${SERVER_URL}/course/query`);
    courseList.value = response.data;
  } catch (error) {
    console.error('Error fetching class list:', error);
    message.error('Failed to fetch class list.');
  } finally {
    loading.value = false;
  }
};

const enterLiveClass = (courseInfo) => {
  // Handle navigation to the live class page or other action
  router.push({
        path: "/classRoom",
        query: {
          
          courseid:courseInfo.id,
        },
      });
  // console.log('Entering class:', courseInfo.className);
};

</script>

<style scoped>
.class-item {
  margin-bottom: 16px;
}
</style>
