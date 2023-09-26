<template>
  <a-row>
    <a-col>
      <div class="search" style="display: flex; align-items: center">
        <a-input
          size="large"
          v-model:value="searchProblem"
          placeholder="输入题目名称或ID"
          style="width: 1200px"
        />
        <el-button :icon="Search" circle @click="handleSearch"></el-button>
      </div>
      <div class="card card1" style="">
        <el-pagination
          style="margin: 10px"
          background
          class="pagination-container"
          @current-change="handlePageChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="prev, pager, next"
          :total="nowQuestions.length"
        ></el-pagination>
        <el-table
          class="table1"
          :data="displayedQuestions"
          style="width: 100%; font-size: 16px"
        >
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
              <div class="hoverable" @click="1">
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
          style="margin: 10px"
          background
          class="pagination-container"
          @current-change="handlePageChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="prev, pager, next"
          :total="nowQuestions.length"
        ></el-pagination>
      </div>
    </a-col>
  </a-row>
</template>

<script>
import { SERVER_URL } from "../../../js/functions/config";
import { Search } from "@element-plus/icons-vue";
import axios from "axios";
import { useRouter } from "vue-router";

export default {
  props: ["questionBank", "difficulty", "algorithm"],
  components: {},
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
    };
  },
  data() {
    return {
      searchText: "",
      questions: [],
      nowQuestions: [],
      currentPage: 1,
      pageSize: 20,
      displayedQuestions: [],
      searchResult: [],
      searchProblem: "1",
      timeout: null,
    };
  },
  watch: {
    algorithm: {
      handler(newVal, oldVal) {
        console.log(oldVal, newVal);
        this.updateQuestions();
      },
    },
    async difficulty(newVal, oldVal) {
      this.updateQuestions(); //不用传参数，用全局的就行
    },

    async questionBank(newVal, oldVal) {
      await this.getQuestions();
      this.updateQuestions(); //换题库的时候也要更新一下
    },
    async searchProblem(newVal, oldVal) {
      if (!newVal) {
        this.currentPage = 1;
        await this.getQuestions();
      } else {
        await this.fetch(newVal, "problem", null);
        this.currentPage = 1;
        this.updateDisplayedQuestions(this.searchResult);
      }
    },
  },

  async mounted() {
    this.searchProblem = "";
    await this.getQuestions();
  },
  methods: {
    fetch: async function (value, type, callback) {
      await axios
        .post(`${SERVER_URL}/contest/query/${type}`, {
          //查询不同类型的数据
          special: value, //有可能因为不是string类型出错
        })
        .then((res) => {
          this.searchResult = res.data;

          // console.log(this.searchMap);
          // callback();
        })
        .catch((err) => {
          console.log(err, "ahsjdhas");
        });
    },
    updateQuestions() {
      //更新算法和难度筛选
      let question = [];
      for (let i = 0; i < this.questions.length; i++) {
        if (
          !this.algorithm || this.algorithm.length === 0 || 
          (this.algorithm &&
          this.algorithm.some(item => this.questions[i].algorithm.trimEnd().split(" ").includes(item))
          )
        ) {
          if (
            !this.difficulty ||
            (this.difficulty &&
              this.questions[i].difficulty === this.difficulty)
          ) {
            question.push(this.questions[i]); //层层筛选
          }
        }
      }
      this.updateDisplayedQuestions(question);
    },
    async getQuestions() {
      await axios
        .get(`${SERVER_URL}/problem/query`, {
          params: {
            questionbank: this.questionBank,
          },
        })
        .then((response) => {
          this.questions = response.data;
          this.updateDisplayedQuestions(this.questions);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    handleSearch() {
      // 在这里处理搜索逻辑
      console.log(this.searchText);
    },
    handlePageChange(newPage) {
      this.currentPage = newPage;
      this.updateDisplayedQuestions(this.nowQuestions);
    },
    updateDisplayedQuestions(questions) {
      this.nowQuestions = questions;
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      this.displayedQuestions = questions.slice(start, end).map((question) => ({
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
  margin-bottom: 20px;
}
.card1 {
  position: relative;
  width: 1170px;
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
