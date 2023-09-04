<template>
  <div class="">
    <div class="">
      <div class="search">
        <el-input v-model="searchText" placeholder="请输入搜索内容"></el-input>
        <el-button :icon="Search" circle @click="handleSearch"></el-button>
        
      </div>
      <div class="card card1">
          <el-table
            class="table1"
            :data="displayedQuestions"
            style="width: 100%"
          >
            <el-table-column prop="problemid" label="ID"></el-table-column>
            <el-table-column prop="title" label="标题"></el-table-column>
            <el-table-column prop="difficulty" label="难度"></el-table-column>
            <el-table-column prop="algorithm" label="知识点"></el-table-column>
            <el-table-column prop="source" label="来源"></el-table-column>
            <el-table-column
              prop="aceptedcount"
              label="通过数"
            ></el-table-column>
            <el-table-column
              prop="submitcount"
              label="提交数"
            ></el-table-column>
          </el-table>
          <el-pagination class="pagination-container"
            @current-change="handlePageChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="prev, pager, next"
            :total="questions.length"
          ></el-pagination>
        </div>
    </div>
  </div>
</template>

<script>
import { SERVER_URL } from "../../../js/functions/config";
import { Search } from "@element-plus/icons-vue";
import axios from "axios";
import { useRouter } from "vue-router";
import ProblemCreateComponent from "@/components/component/problem/ProblemCreateComponent.vue";
export default {
  components: {
    ProblemCreateComponent,
  },
  setup() {
    const router = useRouter();
    const push_to_problemcontent = (problem) => {
      router.push({
        path: "/problemdetail",
        query: {
          problemid: problem.problemid,
        },
      });
    };
    return {
      Search,
      push_to_problemcontent,
      ProblemCreateComponent,
    };
  },
  data() {
    return {
      searchText: "",
      questions: [],
      currentPage: 1,
      pageSize: 20,
      displayedQuestions: [],
    };
  },
  async created() {
    await axios
      .get(`${SERVER_URL}/problem/query`)
      .then((response) => {
        this.questions = response.data;
        this.updateDisplayedQuestions();
      })
      .catch((error) => {
        console.log(error);
      });
  },
  methods: {
    handleSearch() {
      // 在这里处理搜索逻辑
      console.log(this.searchText);
    },
    handlePageChange(newPage) {
      this.currentPage = newPage;
      this.updateDisplayedQuestions();
    },
    updateDisplayedQuestions() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      this.displayedQuestions = this.questions
        .slice(start, end)
        .map((question) => ({
          problem: question.problemid,
          title: question.title,
          difficulty: question.difficulty,
          algorithm: question.algorithm,
          source: question.source,
          aceptedcount: question.aceptedcount,
          submitcount: question.submitcount,
          problemid: question.problemid,
        }));
    },
  },
};
</script>

<style scoped>
.search {
  display: flex;
  position: relative;
  width: 1000px;
  left: 300px;
  top: -100px;
}
.card1{
  position: relative;
  width: 970px;
  left: 300px;
  top: -90px;
}
.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
