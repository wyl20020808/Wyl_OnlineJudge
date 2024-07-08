<template>
  <el-form
    :model="courseForm"
    :rules="rules"
    ref="courseFormRef"
    label-width="120px"
  >
    <el-form-item label="课程名称" prop="courseName">
      <el-input v-model="courseForm.courseName"></el-input>
    </el-form-item>
    <el-form-item label="上课班级" prop="classId">
        <el-select v-model="courseForm.className" placeholder="请选择班级">
        <el-option
          v-for="item in classOptions"
          :key="item.classId"
          :label="item.className"
          :value="item.className"
        ></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="主讲老师" prop="mainTeacher">
      <el-input v-model="courseForm.mainTeacher"></el-input>
    </el-form-item>
    <el-form-item label="课程日期" prop="courseDate">
      <el-date-picker
        v-model="courseForm.courseDate"
        type="date"
        placeholder="选择日期"
        style="width: 100%"
        :picker-options="{ firstDayOfWeek: 1 }"
      ></el-date-picker>
    </el-form-item>
    <el-form-item label="上课时间段" prop="courseTime">
      <el-time-picker
        v-model="courseForm.courseTime"
        is-range
        range-separator="至"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        placeholder="选择时间段"
        style="width: 100%"
      ></el-time-picker>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="courseForm.createTime" disabled></el-input>
    </el-form-item>
  </el-form>
</template>
  
  <script setup>
import { ref,onMounted } from "vue";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
import { useStore } from "vuex";
import { getNowTime } from "@/js/functions/TimeAbout";

const store = useStore();

const classOptions = ref([]);

const fetchClassOptions = async () => {
  try {
    const response = await axios.get(`${SERVER_URL}/class/query`);
    classOptions.value = response.data.map(item => ({
      classId: item.classId,
      className: item.className,  // 取出 className 属性
      headmaster:item.mainTeacher,
    }));
  } catch (error) {
    console.error("Error fetching class options:", error);
  }
};

onMounted(async () =>{
    fetchClassOptions();
})

const initialCourseForm = {
  courseName: "",
  className: "",
  mainTeacher: "",
  courseDate: "",
  courseTime: [],
  createTime: getNowTime(),
};

const courseForm = ref({ ...initialCourseForm });

function getTimeFromISOString(dateTimeString) {
  const dateObj = new Date(dateTimeString);
  const hours = dateObj.getUTCHours().toString().padStart(2, "0");
  const minutes = dateObj.getUTCMinutes().toString().padStart(2, "0");
  const seconds = dateObj.getUTCSeconds().toString().padStart(2, "0");
  return `${hours}:${minutes}:${seconds}`;
}

const rules = {
  courseName: [{ required: true, message: "请输入课程名称", trigger: "blur" }],
  className: [{ required: true, message: "请输入上课班级", trigger: "blur" }],
  mainTeacher: [{ required: true, message: "请输入主讲老师", trigger: "blur" }],
  courseDate: [
    { required: true, message: "请选择课程日期", trigger: "change" },
  ],
  courseTime: [
    { required: true, message: "请选择上课时间段", trigger: "change" },
  ],
};

const courseFormRef = ref(null);

const submitForm = async () => {
  courseForm.value.startTime = getTimeFromISOString(
    courseForm.value.courseTime[0]
  );
  courseForm.value.endTime = getTimeFromISOString(
    courseForm.value.courseTime[1]
  );
  courseForm.value.createTime = getNowTime()
  await axios
    .post(`${SERVER_URL}/course/update`, courseForm.value)
    .then((res) => {
      store.dispatch("notice", {
        title: "课程信息添加成功！",
        message: "",
        type: "success",
      });
      courseForm.value = { ...initialCourseForm };
    })
    .catch((err) => {
      console.error("Error adding course:", err);
    });
};

const resetForm = () => {
  courseFormRef.value.resetFields();
};

defineExpose({
  // 父组件调用子组件，暴露给父组件
  submitForm,
});
</script>
  
  <style scoped>
/* Add custom styles here */
</style>
  