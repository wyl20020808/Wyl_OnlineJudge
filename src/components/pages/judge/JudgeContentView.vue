<!-- 
1、跳转到当前页面必须要judgeid
2、如果是比赛则需要：[contestid,problemchar,problemname]
 -->
<template>
  <a-row style="margin-left: 200px;margin-top: 20px;">
    <a-col >
      <a-card class="nav" style="margin-bottom: 10px;">
        <h1>
          {{ titleinfo }}
        </h1>
      </a-card>
      <div class="card nav2" style="margin-bottom: 10px;">
        <div class="nav-links">
          <div
            @click="selectedTab = 'testInfo'"
            :class="{ 'active-link': selectedTab === 'testInfo' }"
          >
            测试点信息
          </div>
          <div
            @click="selectedTab = 'code'"
            :class="{ 'active-link': selectedTab === 'code' }"
          >
            代码
          </div>
        </div>
      </div>

      <div>
        <JudgeContentCardComponent
          v-if="selectedTab === 'testInfo'"
          :judgeinfo="judgeinfo"
        />
        <JudgeContentCodesComponent v-else :judgeinfo="judgeinfo" />
      </div>
    </a-col>
    <a-col style="margin-left: 20px;">
      <div><JudgeContentInfoComponent :judgeinfo="judgeinfo" /></div>
    </a-col>
  </a-row>
</template>
<!-- 

数据格式：
judgeinfo: {
      judgecontent: [],
      judge: {},
},
 -->
<script>
import JudgeContentCardComponent from "@/components/component/judge/JudgeContentCardComponent.vue";
import JudgeContentCodesComponent from "@/components/component/judge/JudgeContentCodesComponent.vue";
import JudgeContentInfoComponent from "@/components/component/judge/JudgeContentInfoComponent.vue";
import { JUDGE_URL, SERVER_URL } from "@/js/functions/config";
import axios from "axios";
export default {
  props: ["judgeid"],
  data() {
    return {
      selectedTab: "testInfo",
      judgeinfo: {
        judgecontent: [],
        judge: {},
      },
      hadinfo: true,
      judgeid: 0,
      problemid: this.$route.query.problemid,
      problemname: this.$route.query.problemname, //注意这里有可能别的地方没传过来，所以就需要更新数据
      titleinfo: "",
    };
  },
  methods: {
    updateInfo() {
      this.problemid = this.judgeinfo.judge.problemid;
      this.problemname = this.judgeinfo.judge.problemname;

      if (!this.$route.query.contestid) {
        this.titleinfo =
          `测评详情 - U` +
          this.judgeid +
          ` - P` +
          this.problemid +
          `  ` +
          this.problemname;
      }
    },
    async addSubmitCount(problemid, userid) {
      axios
        .post(`${SERVER_URL}/problem/update/problemcontent/special`, {
          problemid: problemid,
          special: "submitcount",
        })
        .then((res) => {})
        .catch((error) => {
          console.log(error);
        });
      axios
        .post(`${SERVER_URL}/userextra/update/special`, {
          userid,
          special: "submitcount",
        })
        .then((res) => {})
        .catch((error) => {
          console.log(error);
        });
    },
    async addAceptedCount(problemid, userid) {
      await axios
        .post(`${SERVER_URL}/problem/update/problemcontent/special`, {
          problemid: problemid,
          special: "aceptedcount",
        })
        .then((res) => {})
        .catch((error) => {
          console.log(error);
        });
      await axios
        .post(`${SERVER_URL}/userextra/update/special`, {
          userid: userid,
          special: "aceptedcount",
        })
        .then((res) => {})
        .catch((error) => {
          console.log(error);
        });
    },
    async saveJudgeInfo(judgedata, submittime) {
      //保存评测信息到数据库
      this.addSubmitCount(
        this.$route.query.problemid,
        JSON.parse(localStorage.getItem("user")).userid
      );
      let judgeinfo = {
        //这里为什么能用$router，是因为到这里来的肯定是提交代码的
        problemid: this.$route.query.problemid,
        userid: JSON.parse(localStorage.getItem("user")).userid,
        submittime,
        code: this.$route.query.source_code,
        username: JSON.parse(localStorage.getItem("user")).username,
        language: this.$route.query.language,
        problemname: this.$route.query.problemname,
        userpicture: JSON.parse(localStorage.getItem("user")).userpicture,
      };
      const results = judgedata.data.results;
      let runtime = 0;
      let memory = 0;
      let score = 0;
      let compileoutput = "";
      let judgestate = "Accepted";
      for (let i = 0; i < results.length; i++) {
        runtime = runtime + parseFloat(results[i].time) * 1000;
        memory = Math.max(memory, parseFloat(results[i].memory));
        if (results[i].status.description == "Accepted")
          score = score + 100 / results.length;
        else judgestate = results[i].status.description;
        compileoutput =
          results[i].compile_output === null
            ? results[i].status.description
            : results[i].compile_output;
      }
      judgeinfo.judgestate = judgestate;
      if (judgestate === "Accepted") {
        this.addAceptedCount(
          this.$route.query.problemid,
          JSON.parse(localStorage.getItem("user")).userid
        );
      }

      judgeinfo.compileoutput = compileoutput;
      judgeinfo.runtime = runtime;
      judgeinfo.memory = memory;
      judgeinfo.score = score >= 99 ? 100 : score;
      judgeinfo.totaltime = runtime;
      let type = "judge";
      if (this.$route.query.contestid) {
        //如果是比赛传过来的话
        judgeinfo.contestid = this.$route.query.contestid; //比赛id
        judgeinfo.problemchar = this.$route.query.problemchar; //题目对应的字母
        type = "contest";
      }
      await axios
        .post(`${SERVER_URL}/${type}/insert/judge`, judgeinfo)
        .then((response) => {
          console.log(response.data);
          this.judgeid = response.data;
        })
        .catch((error) => {
          this.$store.dispatch("notice", {
            title: "数据保存失败！",
            message: "服务器异常" + error,
            type: "error",
          });
        });
      // 上面是保存单个信息，下面是保存每个测试点的信息
      let infolist = [];
      for (let i = 0; i < results.length; i++) {
        infolist.push({
          judgeid: this.judgeid,
          runtime: results[i].time,
          memory: results[i].memory,
          judgestate: results[i].status.description,
        });
      }
      if (this.$route.query.contestid) {
        //如果是比赛传过来的话
        for (let i = 0; i < infolist.length; i++) {
          infolist[i].contestid = this.$route.query.contestid;
          infolist[i].problemchar = this.$route.query.problemchar;
        }
      }
      await axios
        .post(`${SERVER_URL}/${type}/insert/judgecontent`, infolist)
        .then((response) => {})
        .catch((error) => {
          this.$store.dispatch("notice", {
            title: "数据保存失败！",
            message: "服务器异常" + error,
            type: "error",
          });
        });
      //
    },
    async getData() {
      let type = "judge";
      if (this.$route.query.contestid) {
        //如果是比赛传过来的话
        this.judgeinfo.contestid = this.$route.query.contestid;
        type = "contest";
      }
      await axios //查询一下数据库里面有没有数据
        .get(`${SERVER_URL}/${type}/query/judgebyid`, {
          params: {
            judgeid: this.judgeid,
          },
        })
        .then((response) => {
          this.hadinfo = response.data !== ""; //如果查不到数据
          if (this.hadinfo === true) {
            this.judgeinfo.judge = response.data;
          }
        })
        .catch(function (error) {
          console.log(error);
        });
      if (this.hadinfo) {
        //如果有数据，就展示数据
        await axios
          .get(`${SERVER_URL}/${type}/query/judgecontent`, {
            params: {
              judgeid: this.judgeid,
            },
          })
          .then((response) => {
            this.judgeinfo.judgecontent = response.data; //外面的if保证了这里肯定有数据，否则就是数据库有问题
          })
          .catch(function (error) {
            console.log(error);
          });
        this.updateInfo();
      }
    },
  },
  async created() {
    if (this.$route.query.contestid) {
      //如果是比赛需要修改信息
      this.titleinfo =
        `[` + this.$route.query.problemchar + `]` + `  ` + this.problemname;
    }
    this.judgeid = this.$route.query.judgeid; //首先给我们的judgeid赋值
    await this.getData();
    if (this.hadinfo === false) {
      let formData = new FormData();
      formData.append("source_code", this.$route.query.source_code);
      formData.append("problemId", this.$route.query.problemid);
      formData.append("languageId", this.$route.query.languageId);
      await axios //提交代码给后端
        .post(`${JUDGE_URL}/judge/judgeForm`, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then(async (response) => {
          this.$store.dispatch("notice", {
            title: "提交成功！",
            message: "",
            type: "success",
          });
          await this.saveJudgeInfo(response.data, this.$route.query.submittime);
        })
        .catch((error) => {
          console.error(error);
        });
      await this.getData();
    }
  },
  components: {
    JudgeContentCardComponent,
    // CodeComponent
    JudgeContentCodesComponent,
    JudgeContentInfoComponent,
  },
};
</script>

<style scoped>
.nav {
  position: relative;
  width: 800px;
  min-height: 70px;
  height: auto;
}
.nav2 {
  position: relative;
  width: 800px;
  min-height: 40px;
  height: auto;
}
.nav-links {
  display: flex;
  justify-content: flex-start;
  gap: 20px;
  font-size: 24px;
  position: relative;
  left: 15px;
  margin-top: 10px;
  margin-bottom: 10px;
}
.nav-links > div {
  cursor: pointer;
}
.active-link {
  color: rgb(2, 106, 243);
  border-bottom: 2px solid rgb(2, 106, 243);
}
</style>
