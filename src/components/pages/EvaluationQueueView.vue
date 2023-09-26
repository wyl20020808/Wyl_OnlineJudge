<template>
  <a-row style="justify-content: center; align-items: center; margin-top: 20px">
    <a-col>
      <a-row>
        <a-card style="width: 1200px">
          <a-row>
            <el-button type="primary" class="button1" @click="resetFilters"
              >重置所有筛选</el-button
            >
          </a-row>
          <a-row style="margin-top: 20px">
            <a-col style="margin-right: 50px" class="input-container">
              <label style="font-size: 16px; color: gray" for="name"
                >题目或ID</label
              >
              <a-select
                v-model:value="problems"
                show-search
                placeholder="输入题目名称、ID进行搜索"
                style="width: 295px"
                :default-active-first-option="false"
                :show-arrow="false"
                :filter-option="false"
                :not-found-content="null"
                :options="problemsdata"
                @search="handleSearch"
                @change="handleChange"
              ></a-select>
            </a-col>

            <a-col style="margin-right: 50px" class="input-container">
              <label style="font-size: 16px; color: gray" for="name"
                >用户名或ID</label
              >
              <a-select
                v-model:value="administrators"
                show-search
                placeholder="输入管理员名称、ID进行搜索"
                style="width: 295px"
                :default-active-first-option="false"
                :show-arrow="false"
                :filter-option="false"
                :not-found-content="null"
                :options="adminsdata"
                @search="handleSearchAdmin"
                @change="handleChangeAdmin"
              ></a-select>
            </a-col>
            <a-col style="margin-right: 50px" class="input-container">
              <label style="font-size: 16px; color: gray" for="name">状态</label
              ><el-select v-model="judgevalue" clearable placeholder="状态">
                <el-option
                  v-for="item in judgestates"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                /> </el-select
            ></a-col>

            <a-col style="" class="input-container">
              <label style="font-size: 16px; color: gray" for="name">语言</label
              ><el-select v-model="languagevalue" clearable placeholder="语言">
                <el-option
                  v-for="item in languages"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                /> </el-select
            ></a-col>
          </a-row>
        </a-card>
      </a-row>

      <a-row style="margin-top: 20px">
        <a-col>
          <a-card style="width: 1200px">
            <a-row
              style="
                padding: 10px;
                border-bottom: 1px solid rgb(227, 227, 227);
                background-color: white;
              "
              v-for="judgeinfo in tableData"
              :key="judgeinfo.judgeid"
            >
              <a-col>
                <a-row
                  class="hoverable2"
                  @click="jumpToUserInfo(judgeinfo.userid)"
                  style="display: flex; align-items: center"
                >
                  <a-col>
                    <img
                      style="width: 50px; height: 50px; border-radius: 50%"
                      :src="judgeinfo.userpicture"
                      alt=""
                    />
                  </a-col>
                  <a-col style="margin-left: 10px">
                    <a-row style="font-size: 18px; color: #3498db">{{
                      judgeinfo.username
                    }}</a-row>
                    <a-row>{{ judgeinfo.submittime }}</a-row>
                  </a-col>
                </a-row>
              </a-col>

              <a-col
                @click="goToJudgeContent(judgeinfo.judgeid)"
                class="hoverable2"
                style="margin-left: 100px"
              >
                <div
                  :style="{
                    backgroundColor: getJudgeStateColor(judgeinfo.judgestate),
                  }"
                  style="color: white; padding: 2px; border-radius: 4px"
                >
                  {{ judgeinfo.judgestate }}
                </div>
                <div
                  :style="{
                    color: getJudgeStateColor(judgeinfo.judgestate),
                  }"
                  style="font-weight: bold"
                >
                  {{ judgeinfo.score }}
                </div>
              </a-col>
              <a-col style="position: absolute; margin-top: 10px; left: 577px">
                <div
                  style="font-size: 16px; color: #3498db"
                  class="hoverable2"
                  @click="goToProblem(judgeinfo.problemid)"
                >
                  P{{ judgeinfo.problemid }} {{ judgeinfo.problemname }}
                </div>
              </a-col>
              <a-col
                style="
                  color: gray;
                  position: absolute;
                  margin-top: 10px;
                  left: 900px;
                "
              >
                <div style="display: flex; align-items: center">
                  <ClockCircleOutlined /> {{ judgeinfo.totaltime }} /
                  <DatabaseOutlined /> {{ judgeinfo.memory }} /
                  <CodeOutlined />{{ judgeinfo.language }}
                </div>
              </a-col>
            </a-row>
            <a-row
              style="
                margin-top: 20px;
                justify-content: center;
                align-items: center;
              "
            >
              <a-col style="">
                <el-pagination
                  background
                  @current-change="handlePageChange"
                  :current-page="currentPage"
                  :page-size="pageSize"
                  layout="prev, pager, next"
                  :total="judge.length"
                ></el-pagination>
              </a-col>
            </a-row>
          </a-card> </a-col
      ></a-row>
    </a-col>
  </a-row>
</template>

<script>
import axios from "axios";
import { SERVER_URL } from "../../js/functions/config.js";
import router from "@/router/router";

import {
  ClockCircleOutlined,
  DatabaseOutlined,
  CodeOutlined,
} from "@ant-design/icons-vue";
export default {
  components: {
    ClockCircleOutlined,
    DatabaseOutlined,
    CodeOutlined,
  },
  watch: {
    async administrators(newVal, oldVal) {
      this.updateJudgeDate();
    },
    async problems(newVal, oldVal) {
      this.updateJudgeDate();
    },
    async languagevalue(newVal, oldVal) {
      this.updateJudgeDate();
    },
    async judgevalue(newVal, oldVal) {
      this.updateJudgeDate();
    },
  },
  data() {
    const judgestates = [
      {
        value: "Accepted",
        label: "Accepted",
      },
      {
        value: "Time Limit Exceeded",
        label: "Time Limit Exceeded",
      },
      {
        value: "Runtime Error",
        label: "Runtime Error",
      },
      {
        value: "Memory Limit Exceeded",
        label: "Memory Limit Exceeded",
      },
      {
        value: "Wrong Answer",
        label: "Wrong Answer",
      },
      {
        value: "Compilation Error",
        label: "Compilation Error",
      },
    ];
    const languages = [
      { value: 45, label: "Assembly (NASM 2.14.02)", is_archived: false },
      { value: 46, label: "Bash (5.0.0)", is_archived: false },
      { value: 47, label: "Basic (FBC 1.07.1)", is_archived: false },
      { value: 75, label: "C (Clang 7.0.1)", is_archived: false },
      { value: 76, label: "C++ (Clang 7.0.1)", is_archived: false },
      { value: 48, label: "C (GCC 7.4.0)", is_archived: false },
      { value: 52, label: "C++ (GCC 7.4.0)", is_archived: false },
      { value: 49, label: "C (GCC 8.3.0)", is_archived: false },
      { value: 53, label: "C++ (GCC 8.3.0)", is_archived: false },
      { value: 50, label: "C (GCC 9.2.0)", is_archived: false },
      { value: 54, label: "C++ (GCC 9.2.0)", is_archived: false },
      { value: 86, label: "Clojure (1.10.1)", is_archived: false },
      { value: 51, label: "C# (Mono 6.6.0.161)", is_archived: false },
      { value: 77, label: "COBOL (GnuCOBOL 2.2)", is_archived: false },
      { value: 55, label: "Common Lisp (SBCL 2.0.0)", is_archived: false },

      { value: 56, label: "D (DMD 2.089.1)", is_archived: false },

      { value: 57, label: "Elixir (1.9.4)", is_archived: false },

      { value: 58, label: "Erlang (OTP 22.2)", is_archived: false },
      { value: 44, label: "Executable", is_archived: false },
      { value: 87, label: "F# (.NET Core SDK 3.1.202)", is_archived: false },
      { value: 59, label: "Fortran (GFortran 9.2.0)", is_archived: false },
      { value: 60, label: "Go (1.13.5)", is_archived: false },

      { value: 88, label: "Groovy (3.0.3)", is_archived: false },

      { value: 61, label: "Haskell (GHC 8.8.1)", is_archived: false },

      { value: 62, label: "Java (OpenJDK 13.0.1)", is_archived: false },

      { value: 63, label: "JavaScript (Node.js 12.14.0)", is_archived: false },
      { value: 78, label: "Kotlin (1.3.70)", is_archived: false },
      { value: 64, label: "Lua (5.3.5)", is_archived: false },
      { value: 89, label: "Multi-file program", is_archived: false },
      { value: 79, label: "Objective-C (Clang 7.0.1)", is_archived: false },

      { value: 65, label: "OCaml (4.09.0)", is_archived: false },

      { value: 66, label: "Octave (5.1.0)", is_archived: false },
      { value: 67, label: "Pascal (FPC 3.0.4)", is_archived: false },
      { value: 85, label: "Perl (5.28.1)", is_archived: false },
      { value: 68, label: "PHP (7.4.1)", is_archived: false },
      { value: 43, label: "Plain Text", is_archived: false },
      { value: 69, label: "Prolog (GNU Prolog 1.4.5)", is_archived: false },

      { value: 70, label: "Python (2.7.17)", is_archived: false },

      { value: 71, label: "Python (3.8.1)", is_archived: false },
      { value: 80, label: "R (4.0.0)", is_archived: false },

      { value: 72, label: "Ruby (2.7.0)", is_archived: false },

      { value: 73, label: "Rust (1.40.0)", is_archived: false },
      { value: 81, label: "Scala (2.13.2)", is_archived: false },
      { value: 82, label: "SQL (SQLite 3.27.2)", is_archived: false },
      { value: 83, label: "Swift (5.2.3)", is_archived: false },
      { value: 74, label: "TypeScript (3.7.4)", is_archived: false },
      {
        value: 84,
        label: "Visual Basic.Net (vbnc 0.0.0.5943)",
        is_archived: false,
      },
    ].map((item) => ({
      ...item,
      value: item.label,
    }));

    console.log(languages);
    const tableData = [];

    return {
      handleChange: (val) => {
        //比赛题目改变
        console.log(val);
        this.problems = val;
        this.handleSearch("");
      },
      handleSearch: (val) => {
        //搜索比赛题目
        this.fetch(val, "problem", (d) => (this.problemsdata = d));
      },
      handleChangeAdmin: (val) => {
        //比赛管理员改变
        console.log(val);
        this.administrators = val;
        this.handleSearch("");
      },
      handleSearchAdmin: (val) => {
        //比赛管理员搜索
        this.fetch(val, "user", (d) => (this.adminsdata = d));
      },
      problemsdata: [],
      adminsdata: [],
      administrators: null,
      problems: null,
      judgevalue: "",
      judgestates,
      languages,
      languagevalue: "",
      tableData,
      judge: [],
      currentPage: 1,
      pageSize: 15,
      judgestatefilters: [
        { text: "Compilation Error", value: "Compilation Error" },
        { text: "Accepted", value: "Accepted" },
        { text: "Time Limit Exceeded", value: "Time Limit Exceeded" },
        { text: "Runtime Error", value: "Runtime Error" },
        { text: "Wrong Answer", value: "Wrong Answer" },
      ],
      originalJudge: [],
    };
  },
  methods: {
    updateJudgeDate() {
      let temp = [];
      for (let i = 0; i < this.originalJudge.length; i++) {
        //judgevalue languagevalue problems administrators

        if (
          !this.judgevalue ||
          this.judgevalue === this.originalJudge[i].judgestate
        ) {
          if (
            !this.languagevalue ||
            this.languagevalue === this.originalJudge[i].language
          ) {
            if (
              !this.problems ||
              this.problems === this.originalJudge[i].problemid
            ) {
              if (
                !this.administrators ||
                this.administrators === this.originalJudge[i].userid
              ) {
                temp.push(this.originalJudge[i]);
              }
            }
          }
        }
      }
      this.judge = temp;
      this.updateTableData();
    },
    fetch: async function (value, type, callback) {
      this.currentValue = value;
      await axios
        .post(`${SERVER_URL}/contest/query/${type}`, {
          //查询不同类型的数据
          special: value, //有可能因为不是string类型出错
        })
        .then((res) => {
          const data = [];
          for (let i = 0; i < res.data.length; i++) {
            if (type === "problem")
              data.push({
                value: res.data[i].problemid,
                label: res.data[i].problemid + " " + res.data[i].title,
              });
            else
              data.push({
                value: res.data[i].userid,
                label: res.data[i].userid + " " + res.data[i].nickname,
              });
          }
          callback(data);
        })
        .catch((err) => {
          console.log(err, "ahsjdhas");
        });
    },
    jumpToUserInfo(userid) {
      router.push({
        name: "userhome",
        query: { userid },
      });
    },
    resetFilters() {
      this.judge = [...this.originalJudge];
      this.currentPage = 1;
      //judgevalue languagevalue problems administrators
      this.judgevalue =
        this.languagevalue =
        this.problems =
        this.administrators =
          null;
      this.updateTableData();
    },
    updateTableData() {
      //根据judge的值来更新显示的数据
      this.tableData = [];
      const start = (this.currentPage - 1) * this.pageSize;
      const end = Math.min(this.currentPage * this.pageSize, this.judge.length); // 添加了Math.min
      for (let i = start; i < end; i++) {
        // 修改了循环的条件
        this.tableData.push({
          judgestate: this.judge[i].judgestate,
          problemname: this.judge[i].problemname,
          username: this.judge[i].username,
          totaltime: this.judge[i].totaltime + `ms`,
          memory: this.judge[i].memory + `KB`,
          language: this.judge[i].language,
          submittime: this.judge[i].submittime,
          problemid: this.judge[i].problemid,
          judgeid: this.judge[i].judgeid,
          userpicture: this.judge[i].userpicture,
          score: this.judge[i].score,
          userid: this.judge[i].userid,
        });
      }
    },
    handlePageChange(newPage) {
      this.currentPage = newPage;
      this.updateTableData();
    },
    getJudgeStateColor(judgestate) {
      if (judgestate === "Accepted") return "#25ad40";
      else if (judgestate === "Compilation Error") return "orange";
      else return "red";
    },
    goToJudgeContent(judgeid) {
      router.push({
        name: "judgecontent",
        query: {
          judgeid: judgeid,
        },
      });
    },
    goToProblem(problemid) {
      router.push({
        path: "/problem",
        query: {
          problemid: problemid,
        },
      });
    },
  },
  async created() {
    await axios
      .get(`${SERVER_URL}/judge/query/alljudge`)
      .then((response) => {
        console.log(response.data);
        this.originalJudge = response.data.reverse();
        this.judge = [...this.originalJudge];
      })
      .catch((error) => {
        console.log(error);
      });
    this.updateTableData();
  },
};
</script>

<style scoped>
.hoverable {
  transition: color 0.3s ease;
  cursor: pointer;
}

.hoverable:hover {
  filter: brightness(1.3);
}
.hoverable2 {
  transition: color 0.3s ease;
  cursor: pointer;
}

.hoverable2:hover {
  filter: brightness(1.3);
}
.card2 {
  position: relative;
  width: 1300px;
}
.card1 {
  width: 1200px;
}
.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
}
.button1 {
  width: 100px;
  color: white;
  position: relative;
}
.input-container {
  display: flex;
  flex-direction: column;
}
</style>
