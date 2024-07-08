<template>
  <div>
    <!-- 目的是想和源码教育平台里面的展示数据一样的，但是不重要，就先不做了 -->
    <a-row style="align-items: center; justify-content: center; margin-top: 200px;">
    <a-col >
      <h1>小编正在开发中，敬请期待！</h1>
    </a-col>
    <h1>WebSocket Demo</h1>
    <button @click="sendMessage">Send Message</button>
  </a-row>
    <!-- <a-select
      mode="multiple"
      v-model="selectedClass"
      style="width: 200px"
      :options="classOptions"
      placeholder="请选择班级"
      @search="handleSearchClass"
    /> -->
    <!-- <a-select
      mode="multiple"
      v-model="selectedStudents"
      style="width: 200px"
      :options="studentOptions"
      placeholder="请选择学员"
    />

    <a-select
      :options="mainCategories"
      v-model="selectedDoc"
      style="width: 200px"
      @change="updateSubCategories"
      placeholder="请选择题单"
    >
    </a-select>

    <a-select
      :options="filteredSubCategories"
      v-model="selectedDoc"
      placeholder="请选择章节"
      style="width: 200px"
     
    >
    </a-select>

    <a-select
      mode="multiple"
      :options="filerProblems"
      v-model="selectedPid"
      placeholder="Select a title"
      style="width: 200px"
      :disabled="!selectedDoc"
    >
    </a-select>


    <a-table
      :columns="columns"
      :dataSource="tableData"
      rowKey="id"
      v-if="tableData.length"
    /> -->
  </div>
</template>
  
  <script setup>
import { ref, onMounted, watch,computed  } from "vue";
import {
  fetchProblemSituation,
  getClass,
  getProblemList,
  getStudents,
} from "./getInfo";

import WebSocketService from "@/websocket";

const sendMessage = () => {
  WebSocketService.sendMessage('实时通信！');
};

const classOptions = ref([]); //班级的选项
const selectedClass = ref([]);

const studentOptions = ref([]); //学生的选项
const selectedStudents = ref([]);

const tableData = ref([]); //学生做题情况数据
const columns = ref([
  {
    title: "学生ID",
    dataIndex: "id",
    key: "id",
  },
  {
    title: "学生姓名",
    dataIndex: "name",
    key: "name",
  },
  {
    title: "总分",
    dataIndex: "totalScore",
    key: "totalScore",
  },
]);

watch(selectedClass, (newValue, oldValue) => {
  studentOptions.value = getStudents(newValue);
  selectedStudents.value = [];
  // 在这里添加你想要在值变化时执行的逻辑
});

const cleanData = (data) => {
  const tableData = [];
  const problemIds = new Set();
 return {};
  data.user.forEach((user) => {
    const userData = {
      id: user._id,
      name: user.uname,
      totalScore: 0,
    };

    const userScores = data.table[user._id];
    for (const pid in userScores) {
      const score = userScores[pid].score;
      userData[pid] = score;
      userData.totalScore += score;
      problemIds.add(pid);
    }

    tableData.push(userData);
  });

  return { tableData, problemIds: Array.from(problemIds) };
};

const problemList = ref({});
//下面是更新题单的逻辑
const cleanedData = cleanData(problemList);

const mainCategories = ref(cleanedData.mainCategories === undefined ? null : null);
const subCategories = ref(cleanedData.subCategories === undefined ? null : null);
const selectedDoc = ref(null);
const selectedPid = ref([]);

watch(selectedPid, (newValue, oldValue) => {
    if(newValue === null || selectedStudents.value == []){
        return;
    }
//   fetchData(newValue)
  // 在这里添加你想要在值变化时执行的逻辑
});

const filteredSubCategories = computed(() => {
  return selectedDoc.value ? subCategories.value[selectedDoc.value] : [];
});

const filerProblems = computed(() => {
  return filteredSubCategories.value
    ? filteredSubCategories.value.map((row) => ({
        label: row , // 使用 className，如果不存在则使用 name
        value: row,
      }))
    : [];
});

function updateSubCategories() {
  selectedPid.value = null;
}

const fetchData = async (pids) => {
  //获取学生做题情况数据，需要学生的ids以及题目的ids
  try {
    const data = {
      uids: selectedStudents.value,
      pids,
    };

    const response = await fetchProblemSituation(data);
    const cleanedData = response;

    tableData.value = cleanedData.tableData;

    const problemColumns = cleanedData.problemIds.map((pid) => ({
      title: `题目 ${pid} (得分)`,
      dataIndex: pid,
      key: pid,
    }));

    columns.value = [
      {
        title: "学生ID",
        dataIndex: "id",
        key: "id",
      },
      {
        title: "学生姓名",
        dataIndex: "name",
        key: "name",
      },
      ...problemColumns,
      {
        title: "总分",
        dataIndex: "totalScore",
        key: "totalScore",
      },
    ];
  } catch (error) {
    console.error("请求失败", error);
 
  }
};



onMounted(() => {
  // classOptions.value = getClass();
  // console.log("classOptions", classOptions.value);
  // problemList.value = getProblemList()
  // problemList.value = getProblemList();
 
});
</script>
  
  <style scoped>
/* Add any additional styling if needed */
</style>
  