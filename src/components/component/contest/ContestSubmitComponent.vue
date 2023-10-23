<template>
  <a-table :dataSource="tableData" :pagination="false">
    <a-table-column
      title="运行ID"
      dataIndex="judgeid"
      key="judgeid"
      align="center"
    >
      <template v-slot="{ record }">
        <div
          class="hoverable2"
          @click="
            goToJudge(record.judgeid, record.contestid, record.problemchar,record.problemname)
          "
        >
          {{ record.judgeid }}
        </div>
      </template>
    </a-table-column>
    <a-table-column
      title="用户名"
      dataIndex="username"
      key="username"
      align="center"
    >
      <template v-slot="{ record }">
        <div @click="goToUser(record.userid)" class="hoverable2">
          {{ record.username }}
        </div>
      </template>
    </a-table-column>
    <a-table-column
      title="题号"
      dataIndex="problemchar"
      key="problemchar"
      align="center"
    >
      <template v-slot="{ record }">
        <div
          class="hoverable2"
          @click="
            goToProblem(record.problemid, record.contestid, record.problemchar)
          "
        >
          {{ record.problemchar }}
        </div>
      </template>
    </a-table-column>
    <a-table-column
      title="运行结果"
      dataIndex="judgestate"
      key="judgestate"
      align="center"
    >
    <template v-slot="{ record }">
        <div
          class="hoverable2"
          @click="
            goToJudge(record.judgeid, record.contestid, record.problemchar,record.problemname)
          "
          :style="{ color: getJudgeStateColor(record.judgestate) }"
        >
          {{ record.judgestate }}
        </div>
      </template>
    </a-table-column>
    <a-table-column
      title="运行时间"
      dataIndex="totaltime"
      key="totaltime"
      align="center"
    />
    <a-table-column
      title="内存"
      dataIndex="memory"
      key="memory"
      align="center"
    />
    <a-table-column
      title="语言"
      dataIndex="language"
      key="language"
      align="center"
    />
    <a-table-column
      title="提交时间"
      dataIndex="submittime"
      key="state"
      align="center"
    />
  </a-table>
</template>

<script>
import axios from "axios";
import { SERVER_URL } from "../../../js/functions/config.js";
import router from "@/router/router";
export default {
  props: {
    contest: {
      type: Object,
      required: true,
    },
  },
  watch: {
    async contest(newVal, oldVal) {
      console.log("here");
    },
  },
  data() {
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
    return {
      judgestates: [],
      languages,
      tableData: [],
    };
  },
  methods: {
    updateTableData() {},
    getJudgeStateColor(judgestate) {
      if (judgestate === "Accepted") return "#25ad40";
      else if (judgestate === "Compilation Error") return "orange";
      else return "red";
    },
    goToJudge(judgeid, contestid, problemchar,problemname) {
      router.push({
        name: "judgecontent",
        query: {
          judgeid: judgeid,
          contestid,
          problemchar,
          problemname,
        },
      });
    },
    goToProblem(problemid, contestid, problemchar) {
      router.push({
        path: "/contest/problem",
        query: {
          problemid,
          contestid: contestid,
          problemchar: problemchar,
        },
      });
    },
    goToUser(userid) {
      router.push({
        name: "userhome",
        query: {
          userid: userid,
        },
      });
    },
  },
  async created() {
    console.log(this.contest);
    await axios
      .get(`${SERVER_URL}/contest/query/alljudge`, {
        params: {
          contestid: this.contest.contestcontent.contestid,
        },
      })
      .then((response) => {
        console.log(response.data);
        let data = response.data.reverse();
        for (let i = 0; i < data.length; i++) {
          this.tableData.push({
            problemchar: data[i].problemchar,
            userid:data[i].userid,
            username: data[i].username,
            judgeid: data[i].judgeid,
            submittime: data[i].submittime,
            judgestate: data[i].judgestate,
            totaltime: data[i].totaltime + "ms",
            memory: data[i].memory + "KB",
            language: data[i].language,
            problemid: data[i].problemid,
            contestid: data[i].contestid,
            problemname: data[i].problemname,
          });
        }
      })
      .catch((error) => {
        console.log(error);
      });
    this.updateTableData();
  },
};
</script>

<style scoped>
.hoverable2 {
  color: #3498db;
  transition: color 0.3s ease;
  cursor: pointer;
}

.hoverable2:hover {
  filter: brightness(1.3);
}
</style>
