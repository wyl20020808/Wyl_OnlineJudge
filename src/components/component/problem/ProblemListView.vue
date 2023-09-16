<template>
  <div class="">
    <div class="">
      <div class="search">
        <el-input v-model="searchText" placeholder="请输入搜索内容"></el-input>
        <el-button :icon="Search" circle @click="handleSearch"></el-button>
      </div>
      <div class="card card1 " style="">
        <el-pagination
          class="pagination-container"
          @current-change="handlePageChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="prev, pager, next"
          :total="questions.length"
        ></el-pagination>
        <el-table class="table1" :data="displayedQuestions" style="width: 100%;font-size: 16px;">
          <el-table-column
            align="center"
            width="80"
            prop="problemid"
            label="ID"
          >
          </el-table-column>
          <el-table-column align="center" width="400" prop="title" label="标题">
            <template v-slot="{ row }">
              <div
                class="hoverable"
                @click="push_to_problemcontent(row.problemid)"
                
              >
                {{ row.title }}
              </div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            width="70"
            prop="difficulty"
            label="难度"
          ></el-table-column>
          <el-table-column align="center" prop="algorithm" label="算法">
            <template v-slot="{ row }">
              <div
                class="hoverable"
                @click="1"
              >
              <a-tag
                    style="font-size: 14px"
                    v-for="algorithm in row.algorithm"
                    :key="algorithm"
                    color="green"
                    >{{ algorithm }}</a-tag
                  >
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="aceptedcount"
            label="通过数"
            width="70"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="submitcount"
            label="提交数"
            width="70"
            align="center"
          ></el-table-column>
        </el-table>
        <el-pagination
          class="pagination-container"
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
    const push_to_problemcontent = (problemid) => {
      router.push({
        path: "/problem",
        query: {
          problemid: problemid,
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
          algorithm: question.algorithm.trimEnd().split(" "),
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
  width: 1200px;
  left: 200px;
  margin-bottom:20px ;
}
.card1 {
  position: relative;
  width: 1170px;
  left: 200px;
}
.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
}
.hoverable {
  color: #3498db;
  transition: color 0.3s ease, text-decoration 0.3s ease;
  cursor: pointer;
}

.hoverable:hover {
  filter: brightness(1.3);
  text-decoration: underline;
}
</style>
