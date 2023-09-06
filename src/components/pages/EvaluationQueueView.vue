<template>
  <div class="card card1">
    <el-button type="primary" class="button1"  @click="resetFilters">重置所有筛选</el-button>
    <div class="select1">
      <el-select v-model="judgevalue" clearable placeholder="状态">
        <el-option
          v-for="item in judgestates"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </div>
    <div class="select2">
      <el-select v-model="languagevalue" clearable placeholder="语言">
        <el-option
          v-for="item in languages"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </div>

    <div class="input1">
      <el-input
        v-model="problemidinput"
        placeholder="输入题目的id或者名称"
        clearable
      />
    </div>
    <div class="input2">
      <el-input
        v-model="useridinput"
        placeholder="输入用户的id或者名称"
        clearable
      />
    </div>
  </div>

  <div class="card card2">
    
    <el-table ref="tableRef" :data="tableData" style="width: 100%" >
      <el-table-column align="center"
      @filter-change="handleFilterChange"
        prop="judgestate"
        label="状态"
        sortable
        :filters=judgestatefilters
        :filter-method="filterHandler"
      >
      <template v-slot:default="{ row }">
          <div class="hoverable" @click="goToJudgeContent(row.judgeid)"  :style="{color:getJudgeStateColor(row.judgestate)}" >
            {{ row.judgestate }}
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="problemname" label="题目">
        <!-- 这个row指的就是tableData里面存放的信息 -->
        <template  v-slot:default="{ row }">
          <div class="hoverable" @click="goToProblem(row.problemid)">
            {{ row.problemname }}
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="username" label="提交者"> </el-table-column>
      <el-table-column align="center" prop="runtime" label="运行时间"> </el-table-column>
      <el-table-column align="center" prop="memory" label="占用内存"> </el-table-column>
      <el-table-column align="center" prop="language" label="语言"> </el-table-column>
      <el-table-column align="center" prop="submittime" label="提交时间"> </el-table-column>
    </el-table>
    <div class="pagination-container">
    <el-pagination
      @current-change="handlePageChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="prev, pager, next"
      :total="judge.length"
    ></el-pagination>
  </div>
  </div>
</template>

<script>
import axios from "axios";
import { SERVER_URL } from "../../js/functions/config.js";
import router from "@/router/router";
export default {
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
        value:"Compilation Error",
        label: "Compilation Error",
      }
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
      { value: "54", label: "C++ (GCC 9.2.0)", is_archived: false },
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
    ];
    const tableData = [];

    return {
      judgevalue: "",
      judgestates,
      languages,
      languagevalue: "",
      problemidinput: "",
      useridinput: "",
      tableData,
      judge: [],
      currentPage: 1,
      pageSize: 15,
      judgestatefilters:[
        {text: "Compilation Error", value:"Compilation Error"},
        {text: "Accepted", value:"Accepted"},
        {text: "Time Limit Exceeded", value:"Time Limit Exceeded"},
        {text: "Runtime Error", value:"Runtime Error"},
        {text: "Wrong Answer", value:"Wrong Answer"},
      ],
      originalJudge: [],
      
    };
  },
  methods: {
    resetFilters() {
    this.$refs.tableRef.clearFilter();
    this.judge = [...this.originalJudge];
    this.updateTableData();
  },
    filterHandler(value, row, column) {

    const property = column["property"];
    
    if (value) {
      this.judge = this.originalJudge.filter(item => item[property] === value);
      this.updateTableData();
    }
  },
  handleFilterChange(filters) {
    if (Object.keys(filters).every(key => filters[key] === undefined)) {  // 如果所有的筛选都被重置，恢复到原始的judge数组
      this.judge = [...this.originalJudge];
      this.updateTableData();
    }
  },
    updateTableData() {
  this.tableData = [];
  const start = (this.currentPage - 1) * this.pageSize;
  const end = Math.min(this.currentPage * this.pageSize, this.judge.length);  // 添加了Math.min
  for (let i = start; i < end; i++) {  // 修改了循环的条件
    this.tableData.push({
      judgestate: this.judge[i].judgestate,
      problemname: this.judge[i].problemname,
      username: this.judge[i].username,
      runtime: this.judge[i].runtime + ` ms`,
      memory: this.judge[i].memory + ` KB`,
      language: this.judge[i].language,
      submittime: this.judge[i].submittime,
      problemid: this.judge[i].problemid,
      judgeid:this.judge[i].judgeid,
    });
  }

},
    handlePageChange(newPage) {
      this.currentPage = newPage;
      this.updateTableData();
    },
    getJudgeStateColor(judgestate){
      if(judgestate === "Accepted")
        return "#25ad40";
      else if(judgestate === "Compilation Error")
        return "orange";
      else 
        return "red";
    },
    goToJudgeContent(judgeid){
      router.push({name:"judgecontent",query:{
        judgeid:judgeid,
      }})
    },
    goToProblem(problemid){
      router.push({
          path: '/problemdetail',
          query: {
            problemid: problemid,
          }
        })
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
  color:#3498db;
  transition: color 0.3s ease, text-decoration 0.3s ease;
  cursor: pointer;
}

.hoverable:hover {
  filter: brightness(1.3);
  text-decoration: underline;
}
.card2 {
  position: relative;
  top: 200px;
  width: 1300px;
  left: 100px;
}
.card1 {
  width: 1300px;
  position: absolute;
  left: 100px;
  top: 90px;
}
.select1 {
  position: relative;
  left: 1030px;
  top: 80px;
  z-index: 1;
}
.select2 {
  position: relative;
  left: 720px;
  top: 49px;
}
.input1 {
  position: relative;
  left: 360px;
  width: 200px;
  top: 18px;
}
.input2 {
  position: relative;
  width: 200px;
  bottom: 12px;
  left: 50px;
}
.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
}
.button1{
  width: 100px;
  color: white;
  position: relative;
  left: 1130px;
  top: 70px;
}
</style>
