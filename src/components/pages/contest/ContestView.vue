<template>
  <a-row style="justify-content: center;align-items: center;">
    <a-card style="width: 1015px;  margin-top: 20px">
      <a-row class="row1">
        <a-col>
          <a-row style="display: flex; align-items: center">
            <el-icon
              size="40"
              color="rgb(24, 144, 255)"
              style="margin-right: 8px"
              ><Trophy
            /></el-icon>
            <div style="font-size: 36px;">{{ contest.contestcontent?.contestname }}</div>
          </a-row>
          <a-row style="margin-top: 20px; display: flex; align-items: center">
            <NotificationTwoTone style="margin-right: 3px" />
            <a-col style="font-size: 18px; margin-right: 40px"
              >比赛时间：{{
                toBeijingTime(contest.contestcontent?.startdate)
              }}
              ~ {{ toBeijingTime(contest.contestcontent?.enddate) }}</a-col
            >
            <el-icon size="20" color="rgb(24, 144, 255)"><Timer /></el-icon>
            <a-col style="font-size: 18px"
              >时长：{{
                getTimeDifference(
                  contest.contestcontent?.startdate,
                  contest.contestcontent?.enddate
                )
              }}</a-col
            >
          </a-row>
          <a-row
            style="
              margin-top: 20px;
              display: flex;
              align-items: center;
              position: relative;
              right: 3px;
            "
          >
            <el-icon style="" size="20" color="rgb(24, 144, 255)"
              ><User
            /></el-icon>
            <a-col style="font-size: 18px"
              >参赛人数：{{ contest.contestcontent?.joinpeople }}</a-col
            >
          </a-row>
        </a-col>
      </a-row>
    </a-card>
    <a-card style="margin-top: 20px; width: 1015px;margin-bottom: 20px;">
      <a-row>
        <a-col style="">
          <a-tabs class="my-tabs" v-model:activeKey="activeKey">
            <a-tab-pane style="width:950px ;" key="1" tab="比赛说明"
              ><ContestDescriptionComponent :contest="contest"
            /></a-tab-pane>
            <a-tab-pane style="width:950px ;" key="2" tab="题目" force-render
              ><ContestProblemComponent :contest="contest"
            /></a-tab-pane >
            <a-tab-pane style="width:950px ;" key="3" tab="提交"
              ><ContestSubmitComponent :contest="contest"
            /></a-tab-pane>
            <a-tab-pane style="width:950px ;" key="4" tab="排名"
              ><ContestRankComponent :contest="contest"
            /></a-tab-pane> </a-tabs
        ></a-col>
      </a-row>
    </a-card>
  </a-row>
</template>

<script>
import { NotificationTwoTone, HomeTwoTone } from "@ant-design/icons-vue";
import { User, Timer, Flag, Trophy } from "@element-plus/icons-vue";
import ContestDescriptionComponent from "@/components/component/contest/ContestDescriptionComponent.vue";
import ContestProblemComponent from "@/components/component/contest/ContestProblemComponent.vue";
import ContestSubmitComponent from "@/components/component/contest/ContestSubmitComponent.vue";
import ContestRankComponent from "@/components/component/contest/ContestRankComponent.vue";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
export default {
  components: {
    NotificationTwoTone,
    ContestDescriptionComponent,
    ContestProblemComponent,
    ContestSubmitComponent,
    ContestRankComponent,
    User,
    Timer,
    Flag,
    Trophy,
  },
  data() {
    return {
      activeKey: "1",
      contest: {},
      contestid: this.$route.query.contestid, //这里可能需要重构，因为有可能用户从浏览器输入比赛id进入比赛页面
    };
  },
  watch: {},
  async created() {
    await axios
      .get(`${SERVER_URL}/contest/query/contest`, {
        params: {
          contestid: this.$route.query.contestid,
        },

        //  @GetMapping("/query/contest")
        // public Contest queryContest(@RequestParam int contestid){
        //     Contest result = new Contest();
        //     result.setContestcontent(contestContentSQL.selectById(contestid));
        //     result.setContestadmin(contestAdminSQL.selectList(new QueryWrapper<ContestAdmin>().eq("contestid", contestid)));;
        //     result.setContestproblem(contestProblemSQL.selectList(new QueryWrapper<ContestProblem>().eq("contestid", contestid)));
        //     return result;
        // }
      })
      .then((res) => {
        this.contest = res.data;

        // console.log(this.contest, new Date());
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    toBeijingTime(value) {
      let date = new Date(value);
      let beijingTime = date.toLocaleString("zh-CN", {
        timeZone: "Asia/Shanghai",
      });
      return beijingTime;
    },
    getTimeDifference(startdate, enddate) {
      let start = new Date(startdate);
      let end = new Date(enddate);
      let diff = Math.abs(end - start); // 获取毫秒数的差值
      let days = Math.floor(diff / (1000 * 60 * 60 * 24)); // 计算天数
      diff -= days * (1000 * 60 * 60 * 24);
      let hours = Math.floor(diff / (1000 * 60 * 60)); // 计算小时数
      diff -= hours * (1000 * 60 * 60);
      let minutes = Math.floor(diff / (1000 * 60)); // 计算分钟数
      return `${days}天${hours}小时${minutes}分钟`;
    },
  },
};
</script>

<style scope>
.my-tabs {
  background-color: white; /* 你可以选择你想要的颜色 */
}
.my-tabs .ant-tabs-tab {
  font-size: 24px;
}
.my-tabs .ant-tabs-tab:not(:last-child) {
  margin-right: 40px; /* 设置标签之间的间距 */
}
</style>
