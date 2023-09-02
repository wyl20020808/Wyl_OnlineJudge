<template>
  <div>
    <div class="card out2">
      <div class="title1">
        <h2>提交代码 - P{{ problemid }}</h2>
      </div>
      <div class="buttons">
        <el-button @click="backToProblem" class="button2" type="primary"
          >返回题目</el-button
        >
      </div>
      <div class="select">
        <div style="position: relative; left: 10px">代码语言</div>
        <el-select
          v-model="value"
          class="m-2 select1"
          placeholder="Select"
          size="large"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
    </div>
    <div class="input">
      <el-input
        v-model="codes"
        :autosize="{ minRows: 20, maxRows: 40 }"
        type="textarea"
        placeholder="Please input"
      />
    </div>
    <div class="buttons">
      <el-button @click="submitCode" class="button1" type="primary"
        >提交代码</el-button
      >
    </div>
    <div class="辅助" style="position: relative; top: 60px; width: 100px">
      &ensp;
    </div>
  </div>
</template>

<script>
import { SERVER_URL } from "@/js/functions/config";
import { ref } from "vue";
import router from "@/router/router";
import axios from "axios";
import {getNowTime} from "../../../js/functions/TimeAbout";
export default {
  props: ["problemid"],
  data() {
    let value = ref("54");
    const options = [
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
      options,
      value,
      codes: "",
      judgeid:0,
    };
  },
  methods: {
    backToProblem() {
      router.go(-1);
    },
    saveJudgeInfo(judgedata, submittime) {
      let judgeinfo = {
        judgestate: judgedata.data.results[0].status.description,
        problemid: this.problemid,
        userid: JSON.parse(localStorage.getItem("user")).userid,
        language: this.value,
        submittime,
      };
      const results = judgedata.data.results;
      let runtime = 0;
      let memory = 0;
      let score = 0;
      for (let i = 0; i < results.length; i++) {
        runtime = runtime + parseFloat(results[i].time) * 1000;
        memory = Math.max(memory, parseFloat(results[i].memory));
        if (results[i].status.description == "Accepted")
          score = score + 100 / results.length;
      }
      judgeinfo.runtime = runtime;
      judgeinfo.memory = memory;
      axios
        .post(`${SERVER_URL}/judge/insert/judge`, judgeinfo)
        .then((response) => {
            this.judgeid = response.data;
        })
        .catch((error) => {
          this.$store.dispatch("notice", {
            title: "数据保存失败！",
            message: "服务器异常" + error,
            type: "error",
          });
        });
      let infolist = [];
      for (let i = 0; i < results.length; i++) {
        infolist.push({
          judgeid:this.judgeid,
          runtime: results[i].time,
          memory: results[i].memory,
          judgestate: results[i].status.description,
          code: this.codes,
          compileoutput:
            results[i].compile_output === null
              ? results[i].status.description
              : results[i].compile_output,
          userid: JSON.parse(localStorage.getItem("user")).userid,
          problemid: this.problemid,
          language: this.value,
          submittime,
          score,
          totaltime: runtime,
          username:JSON.parse(localStorage.getItem("user")).username,
        });
      }
      axios
        .post(`${SERVER_URL}/judge/insert/judgecontent`, infolist)
        .then((response) => {})
        .catch((error) => {
          this.$store.dispatch("notice", {
            title: "数据保存失败！",
            message: "服务器异常" + error,
            type: "error",
          });
        });
    },
    submitCode() {
      let submittime = getNowTime();
      let formData = new FormData();
      formData.append("source_code", this.codes);
      formData.append("problemId", this.problemid);
      formData.append("languageId", this.value.toString());
      axios
        .post("https://784y2154s6.zicp.fun/judge/judgeForm", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          this.$store.dispatch("notice", {
            title: "提交成功！",
            message: "",
            type: "success",
          });
          this.saveJudgeInfo(response.data, submittime);
        })
        .catch((error) => {
          console.error(error);
        });
    },
  },
};
</script>

<style scoped>
.buttons {
  color: white;
}
.button2 {
  position: relative;
  width: 150px;
  top: 100px;
  left: 20px;
}
.button1 {
  position: relative;
  top: 60px;
  width: 100px;
  left: 350px;
}
.title1 {
  position: relative;
  left: 20px;
  top: 20px;
}
.input {
  /* width: 100%; */
  /* margin-top: 20px; */
  /* min-width: 600px; */
  position: relative;
  top: 40px;
  left: 350px;
  width: 800px;
}
.select {
  position: relative;
  left: 450px;
  top: 30px;
}
.select1 {
  width: 300px;
}
.out2 {
  background-color: #ffffff;
  /* width: 100%; */
  /* margin-top: 20px; */
  /* min-width: 600px; */
  top: 20px;
  left: 350px;
  width: 800px;
  min-height: 200px;
  height: auto;
}
</style>
