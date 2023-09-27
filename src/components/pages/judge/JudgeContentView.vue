<!-- 
1、跳转到当前页面必须要judgeid
2、如果是比赛则需要：[contestid,problemchar,problemname]
 -->
<template>
  <a-row style="margin-left: 200px;margin-top: 20px;">
    <a-col >
      <a-card class="nav" style="margin-bottom: 10px;">
        <div style="font-size: 36px;">
          {{ titleinfo }}
        </div>
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
          `U` +
          this.judgeid +
          ` - P` +
          this.problemid +
          `  ` +
          this.problemname;
      }
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
            this.judgeinfo.judge = response.data;
        })
        .catch(function (error) {
          console.log(error);
        });

        //展示数据
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
