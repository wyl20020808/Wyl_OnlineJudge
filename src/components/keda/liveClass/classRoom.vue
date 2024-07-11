<template>
  <a-row style="margin-top: 20px">
    <!-- ashjdkha -->
    <a-col :span="24">
      <a-row>
        <a-col >
          <h2>Classroom</h2>
        </a-col>
        <a-col >
          <a-select
            style="width: 120px; z-index: 1234"
            v-model:value="trophyCount"
            :options="[
              { value: 1, label: '1' },
              { value: 2, label: '2' },
              { value: 3, label: '3' },
              { value: 4, label: '4' },
              { value: 5, label: '5' },

              { value: 6, label: '6' },
              { value: 7, label: '7' },
              { value: 8, label: '8' },
              { value: 9, label: '9' },
              { value: 10, label: '10' },

              { value: 11, label: '11' },
              { value: 12, label: '12' },
              { value: 13, label: '13' },
              { value: 14, label: '14' },
              { value: 15, label: '15' },

              { value: 16, label: '16' },
              { value: 17, label: '17' },
              { value: 18, label: '18' },
              { value: 19, label: '19' },
              { value: 20, label: '20' },
            ]"
          ></a-select>
        </a-col>
      </a-row>
      <a-card style="margin-bottom: 16px">
        <p><strong>Class Status:</strong> {{ classStatus }}</p>
        <p><strong>Remaining Time:</strong> {{ remainingTime }} minutes</p>
      </a-card>
      <a-card style="margin-bottom: 16px">
        <a-row :gutter="16">
          <a-col
            :span="3"
            v-for="(student, idx) in CourseStudentInfo"
            :key="student.id"
          >
            <studentAvatar
              :student-name="student.studentName"
              :avatar-url="student.avatarUrl"
              :trophyCount="student.trophyCount"
              :idx="idx"
              @trophyGiven="trophyGiven"
            />
          </a-col>
        </a-row>
      </a-card>
    </a-col>
  </a-row>
</template>
  
  <script setup>
import { ref, onMounted } from "vue";
import { Row, Col, Card } from "ant-design-vue";
import studentAvatar from "./studentAtvar.vue"; // Ensure this path is correct
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";

const classStatus = ref("In Progress");
const remainingTime = ref(45); // Remaining time in minutes
import { useRoute } from "vue-router";
// import { tr } from "element-plus/es/locale";
const route = useRoute();

const courseId = route.query.courseid;

const CourseStudentInfo = ref({});

// const idx = ref(0)
const trophyCount = ref(1);

const students = ref([
  { id: 1, name: "Student A", avatarUrl: "path/to/avatar1.png" },
  { id: 2, name: "Student B", avatarUrl: "path/to/avatar2.png" },
  { id: 3, name: "Student C", avatarUrl: "path/to/avatar3.png" },
  // Add more students as needed
]);

// Example function to update remaining time
const updateRemainingTime = () => {
  setInterval(() => {
    if (remainingTime.value > 0) {
      remainingTime.value--;
    }
  }, 60000); // Decrease remaining time by 1 minute every 60 seconds
};

const asyncUpdatetrophycount = async (studentInfo) => {
  try {
    const response = await axios.post(
      `${SERVER_URL}/course/update/courseinfo`,
      studentInfo
    );
  } catch (error) {
    console.error("Error fetching class list:", error);
  }
};

const trophyGiven = async (studentName) => {
  console.log(studentName);
  for (let i = 0; i < CourseStudentInfo.value.length; i++) {
    if (CourseStudentInfo.value[i].studentName == studentName) {
      CourseStudentInfo.value[i].trophyCount += trophyCount.value;
      console.log(CourseStudentInfo.value[i].trophyCount);
      asyncUpdatetrophycount(CourseStudentInfo.value[i]);
      break;
    }
  }
};

onMounted(() => {
  updateRemainingTime();
  fetchClassList(); // Ensure fetchClassList is called on mount
});

const fetchClassList = async () => {
  try {
    const response = await axios.get(`${SERVER_URL}/course/query/info`, {
      params: {
        courseId: courseId ? courseId.toString() : undefined,
      },
    });
    CourseStudentInfo.value = response.data;
  } catch (error) {
    console.error("Error fetching class list:", error);
  }
};

const enterLiveClass = (classInfo) => {
  // Handle navigation to the live class page or other action
  console.log("Entering class:", classInfo.className);
};
</script>
  
  <style scoped>
.class-item {
  margin-bottom: 16px;
}
.student-avatar {
  text-align: center;
}

.avatar-img {
  width: 100%;
  border-radius: 50%;
}
</style>
  