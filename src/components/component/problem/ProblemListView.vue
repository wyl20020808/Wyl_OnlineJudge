<template>
  <div ref="slideElement" >
  <a-row>
    <a-col>
      <div class="search slide-up" v-if="showDivs.includes(1)" style="display: flex; align-items: center">
        <a-input
          size="large"
          v-model:value="searchProblem"
          placeholder="输入题目名称或ID"
          style="width: 1200px"
        />
        <el-button :icon="Search" circle @click="handleSearch"></el-button>
      </div>
      <div class="card card1 slide-up" v-if="showDivs.includes(2)" style="">
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
          :data="displayedQuestions"
          style="width: 100%; font-size: 16px"
          :row-class-name="tableRowClassName"
        >
        <el-table-column align="center" width="70" prop="title" label="状态">
            <template v-slot="{ row }">
              <div
                class="hoverable"
                @click="jumpToQueue(row.problemid)"
              >
              <DislikeFilled style="font-size: 20px;color: rgb(216, 65, 90);" v-if="historyScore.get(row.problemid) && historyScore.get(row.problemid) !== 100 || historyScore.get(row.problemid) === 0" />
              <QuestionOutlined style="font-size: 20px;" v-else-if="!historyScore.get(row.problemid)"/>
              <LikeFilled style="font-size: 20px;color: rgb(10, 146, 10);" v-else />
              </div>
            </template>
          </el-table-column>
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
</div>
</template>

<script>
import { SERVER_URL } from "../../../js/functions/config";
import { Search } from "@element-plus/icons-vue";
import axios from "axios";
import { useRouter } from "vue-router";
import { QuestionOutlined , QuestionCircleFilled,CheckOutlined,CloseOutlined,LikeFilled,DislikeFilled } from "@ant-design/icons-vue";
import router from "@/router/router";
export default {
  props: ["questionBank", "difficulty", "algorithm"],
  components: {
    QuestionOutlined,
    CheckOutlined ,
    CloseOutlined ,
    LikeFilled,
    DislikeFilled ,
    QuestionCircleFilled ,
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
    };
  },
  data() {
    return {
      showDivs:[],
      searchText: "",
      questions: [],
      nowQuestions: [],
      currentPage: 1,
      pageSize: 20,
      displayedQuestions: [],
      searchResult: [],
      searchProblem: "1",
      timeout: null,
      historyScore: new Map(),
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
    this.applyAnimation();
    await this.getQuestions();
  },
  methods: {
    applyAnimation() {
      //有序出现
      for (let i = 0; i < 2; i++) {
        setTimeout(() => {
          this.showDivs.push(i + 1);
          this.$nextTick(() => {
            const elements = document.querySelectorAll(".slide-up");
            elements.forEach((element, index) => {
              console.log("ahgskjdhajks");
              const delay = index * 0.05;
              element.style.transitionDelay = `${delay}s`;
              element.classList.add("active");
            });
          });
        }, 150 * (i + 1)); // 每个div延时增加500ms
      }
    },
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
    tableRowClassName({ row, rowIndex }) {
      if (this.historyScore.get(row.problemid) || this.historyScore.get(row.problemid) === 0) {
        if (this.historyScore.get(row.problemid) !== 100){
          return 'danger-row';
        }else{
          return 'success-row';
        }
      }
      return '';
    },
    updateQuestions() {
      //更新算法和难度筛选
      let question = [];
      for (let i = 0; i < this.questions.length; i++) {
        if (
          !this.algorithm ||
          this.algorithm.length === 0 ||
          (this.algorithm &&
            this.algorithm.some((item) =>
              this.questions[i].algorithm.trimEnd().split(" ").includes(item)
            ))
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
    jumpToQueue(problemid) {
      router.push({
        name: "evaluationqueue",
        query: {
          problemid: problemid,
          userid:JSON.parse(localStorage.getItem("user")).userid,
        },
      });
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
    async getHistoryScore() {
      await axios
        .get(`${SERVER_URL}/judge/query/userid`, {
          params: {
            userid: JSON.parse(localStorage.getItem("user")).userid,
          },
        })
        .then((res) => {
          let data = res.data;
          for (let i = 0; i < data.length; i++) {
            let problemid = data[i].problemid;
            if (
              !this.historyScore.get(problemid) ||
              data[i].score > this.historyScore.get(problemid)
            )
              this.historyScore.set(problemid, data[i].score);
          }
          console.log(this.historyScore, "历史分数已经初始化完成");
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  async created() {
    await this.getHistoryScore();
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


::v-deep .el-table .danger-row {
  --el-table-tr-bg-color: var(--el-color-danger-light-8);
}
::v-deep .el-table .success-row {
  --el-table-tr-bg-color: var(--el-color-success-light-8);
}
</style>
