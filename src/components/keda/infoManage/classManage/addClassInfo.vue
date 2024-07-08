<template>
  <el-form
    :model="classForm"
    :rules="rules"
    ref="classFormRef"
    label-width="120px"
  >
    <el-form-item label="班级ID" prop="classId">
      <el-input v-model="classForm.classId"></el-input>
    </el-form-item>
    <el-form-item label="班级名称" prop="className">
      <el-input v-model="classForm.className"></el-input>
    </el-form-item>
    <el-form-item label="班主任名称" prop="headTeacher">
      <el-input v-model="classForm.headTeacher"></el-input>
    </el-form-item>
    <el-form-item label="开班日期" prop="startDate">
      <el-date-picker
        v-model="classForm.startDate"
        type="date"
        placeholder="选择日期"
        style="width: 100%"
        :picker-options="{ firstDayOfWeek: 1 }"
      ></el-date-picker>
    </el-form-item>
    <el-form-item label="主讲老师" prop="mainTeacher">
      <el-input v-model="classForm.mainTeacher"></el-input>
    </el-form-item>

    <el-form-item label="上课时间段" prop="classTime">
      <el-time-picker
        v-model="classForm.classTime"
        is-range
        range-separator="至"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        placeholder="选择时间段"
        style="width: 100%"
      ></el-time-picker>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="classForm.createTime" disabled></el-input>
    </el-form-item>
    <el-form-item label="班级等级" prop="classLevel">
      <el-input v-model="classForm.classLevel"></el-input>
    </el-form-item>
  </el-form>
</template>
  
<script setup>
import { ref } from "vue";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
import { useStore } from "vuex";
import { getNowTime } from "@/js/functions/TimeAbout";

const store = useStore();

const initialClassForm = {
  classId: "",
  className: "",
  headTeacher: "",
  startDate: "",
  mainTeacher: "",
  createTime: getNowTime(),
  classLevel: "",
  classTime: [], // 新增上课时间段字段
};

const classForm = ref({ ...initialClassForm });
function getTimeFromISOString(dateTimeString) { //把2024-06-28T08:54:12.000Z 提取时间点
  const dateObj = new Date(dateTimeString);
  const hours = dateObj.getUTCHours().toString().padStart(2, '0');
  const minutes = dateObj.getUTCMinutes().toString().padStart(2, '0');
  const seconds = dateObj.getUTCSeconds().toString().padStart(2, '0');
  
  return `${hours}:${minutes}:${seconds}`;
}

const rules = {
  classId: [{ required: true, message: "请输入班级ID", trigger: "blur" }],
  className: [{ required: true, message: "请输入班级名称", trigger: "blur" }],
  headTeacher: [
    { required: true, message: "请输入班主任名称", trigger: "blur" },
  ],
  startDate: [{ required: true, message: "请选择开班日期", trigger: "change" }],
  mainTeacher: [{ required: true, message: "请输入主讲老师", trigger: "blur" }],
  classLevel: [{ required: true, message: "请输入班级等级", trigger: "blur" }],
  classTime: [
    { required: true, message: "请选择上课时间段", trigger: "change" },
  ], // 添加验证规则
};

const classFormRef = ref(null);

const submitForm = async () => {
  classForm.value.startTime =getTimeFromISOString( classForm.value.classTime[0]),
  classForm.value.endTime = getTimeFromISOString(classForm.value.classTime[1])
  await axios
    .post(`${SERVER_URL}/class/update`, classForm.value)
    .then((res) => {
      store.dispatch("notice", {
        title: "班级信息添加成功！",
        message: "",
        type: "success",
      });
      classForm.value = { ...initialClassForm };
      // Add logic to prevent duplicate class additions here if needed
    })
    .catch((err) => {
      console.error("Error adding class:", err);
    });
};

const resetForm = (formName) => {
  classFormRef.value.resetFields();
};
defineExpose({
  // 父组件调用子组件，暴露给父组件
  submitForm,
});
</script>

  <style scoped>
/* Add custom styles here */
</style>
  