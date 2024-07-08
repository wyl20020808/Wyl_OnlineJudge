<template>
  <el-form :model="studentForm" :rules="rules" ref="studentFormRef">
    <el-form-item label="姓名" prop="name">
      <el-input v-model="studentForm.name"></el-input>
    </el-form-item>
    <el-form-item label="学号" prop="studentid">
      <el-input v-model="studentForm.studentid"></el-input>
    </el-form-item>
    <el-form-item label="年龄" prop="age">
      <el-input v-model="studentForm.age"></el-input>
    </el-form-item>
    <el-form-item label="性别" prop="sex">
      <el-input v-model="studentForm.sex"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input type="password" v-model="studentForm.password"></el-input>
    </el-form-item>
    <el-form-item label="班级" prop="classes">
      <el-select v-model="studentForm.classes" placeholder="请选择班级">
        <el-option
          v-for="item in classOptions"
          :key="item.classId"
          :label="item.className"
          :value="item.className"
        ></el-option>
      </el-select>
    </el-form-item>

  </el-form>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
import { useStore } from "vuex";

const store = useStore();
const studentForm = ref({
  name: "",
  studentid: "",
  age: "",
  sex: "",
  password: "",
  classes: "", // 班级ID将作为提交的值
  headmaster: "4123",
});

const rules = {
  name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  studentid: [{ required: true, message: "请输入学号", trigger: "blur" }],
  age: [{ required: false, message: "请输入年龄", trigger: "blur" }],
  sex: [{ required: false, message: "请输入性别", trigger: "blur" }],
  password: [{ required: false, message: "请输入密码", trigger: "blur" }],
  classes: [{ required: true, message: "请选择班级", trigger: "blur" }],
};

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

const submitForm = async () => {
  try {
    //匹配班主任的逻辑存在问题
    // studentForm.value.headmaster = toString(classOptions.value.find(option => option.classId === studentForm.value.classes).headmaster);
    
    const response = await axios.post(`${SERVER_URL}/student/update`, studentForm.value);
    store.dispatch("notice", {
      title: "学员添加成功！",
      message: "",
      type: "success",
    });
    resetForm();
  } catch (error) {
    console.error("Error submitting student form:", error);
  }
};

const resetForm = () => {
  studentForm.value = {
    name: "",
    studentid: "",
    age: "",
    sex: "",
    password: "",
    classes: "",
    headmaster: "",
  };
};

onMounted(() => {
  fetchClassOptions();
});

defineExpose({
  // 父组件调用子组件，暴露给父组件
  submitForm,
});
</script>

<style scoped>
.el-form-item {
  margin-bottom: 20px;
}
</style>
