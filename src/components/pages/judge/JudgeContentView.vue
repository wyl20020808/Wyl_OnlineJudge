<!-- 跳转到当前页面必须要judgeid -->
<template>
  <div class="nav">
    <h1 style="position: relative; left: 15px; top: 10px">
      测评详情 - U{{ judgeid }} - P{{ problemid }} {{ problemname  }}
    </h1>
  </div>
  <div class="card nav2">
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
    <JudgeContentCardComponent v-if="selectedTab === 'testInfo'" :judgeinfo="judgeinfo" />
    <!-- Replace with your Code component -->
    <CodeComponent v-else />
  </div>
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
// Import your Code component
// import CodeComponent from "@/components/CodeComponent.vue"
import { SERVER_URL } from "@/js/functions/config";
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
      hadinfo:true,
      judgeid: 0,
      problemid: this.$route.query.problemid,
      problemname:this.$route.query.problemname,//注意这里有可能别的地方没传过来，所以就需要更新数据
    };
  },
  methods:{
    updateInfo(){
      this.problemid = this.judgeinfo.judge.problemid;
      this.problemname = this.judgeinfo.judge.problemname;
    },
    async saveJudgeInfo(judgedata, submittime) {
      let judgeinfo = {
        problemid: this.$route.query.problemid,
        userid: JSON.parse(localStorage.getItem("user")).userid,
        submittime,
        code: this.$route.query.source_code,
        username:JSON.parse(localStorage.getItem("user")).username,
        language:this.$route.query.language,
        problemname:this.$route.query.problemname,
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
      judgeinfo.compileoutput = compileoutput;
      judgeinfo.runtime = runtime;
      judgeinfo.memory = memory;
      judgeinfo.score = score > 99 ? 100:score;
      judgeinfo.totaltime = runtime;
      await axios
        .post(`${SERVER_URL}/judge/insert/judge`, judgeinfo)
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
      let infolist = [];
      for (let i = 0; i < results.length; i++) {
        infolist.push({
          judgeid: this.judgeid,
          runtime: results[i].time,
          memory: results[i].memory,
          judgestate: results[i].status.description,
        });
      }
      await axios
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
    async getData(){
      await axios//查询一下数据库里面有没有数据
      .get( `${SERVER_URL}/judge/query/judgebyid`, {
        params: {
          judgeid: this.judgeid,
        },
      })
      .then((response) => {
        this.hadinfo = response.data !== "";//如果查不到数据
        if(this.hadinfo === true){
          this.judgeinfo.judge = response.data;
        }
            
      })    
      .catch(function (error) {
        console.log(error);
      });
    if(this.hadinfo){//如果有数据，就展示数据
        await axios
      .get( `${SERVER_URL}/judge/query/judgecontent`, {
        params: {
          judgeid: this.judgeid,
        },
      })
      .then((response) => {
        this.judgeinfo.judgecontent = response.data;//外面的if保证了这里肯定有数据，否则就是数据库有问题
      })    
      .catch(function (error) {
        console.log(error);
      });
      this.updateInfo();
    }

  },
},
  async created() {
    this.judgeid = this.$route.query.judgeid;//首先给我们的judgeid赋值
    await this.getData();
    if(this.hadinfo === false){
      let formData = new FormData();
      formData.append("source_code", this.$route.query.source_code);
      formData.append("problemId", this.$route.query.problemid);
      formData.append("languageId", this.$route.query.languageId);
      await axios //提交代码给后端
        .post(`http://8.134.100.87:8088/judge/judgeForm`, formData, {
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

    //如果没有数据，说明是提交页面过来的，那就提交代码，天才
      
    
    
    

    
  },
  components: {
    JudgeContentCardComponent,
    // CodeComponent
  },
};
</script>

<style scoped>
.nav {
  position: relative;
  left: 200px;
  top: 20px;
  width: 800px;
  min-height: 70px;
  height: auto;
}
.nav2 {
  position: relative;
  left: 200px;
  top: 30px;
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
