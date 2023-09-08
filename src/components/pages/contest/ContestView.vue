<template>
  <div class="">
    <a-row class="row1">
      <a-col :offset="3" :span="24">
        <a-row style="margin-top: 20px; display: flex; align-items: center">
          <el-icon size="40" color="rgb(24, 144, 255)" style="margin-right: 8px"
            ><Trophy
          /></el-icon>
          <h1>{{ contest.contestcontent?.contestname }}</h1>
        </a-row>
        <a-row style="margin-top: 20px; display: flex; align-items: center">
          <NotificationTwoTone style="margin-right: 3px" />
          <a-col style="font-size: 18px" :span="8"
            >比赛时间：{{ toBeijingTime(contest.contestcontent?.startdate) }} ~
            {{ toBeijingTime(contest.contestcontent?.enddate) }}</a-col
          >
          <el-icon size="20" color="rgb(24, 144, 255)"><Timer /></el-icon>
          <a-col style="font-size: 18px" :span="6"
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
          <a-col style="font-size: 18px">参赛人数：{{ contest.contestcontent?.joinpeople }}</a-col>
        </a-row>
      </a-col>
    </a-row>
    <a-row style="margin-top: 40px">
      <a-col :offset="3" :span="16">
        <a-tabs class="my-tabs" v-model:activeKey="activeKey">
          <a-tab-pane key="1" tab="比赛说明"
            ><ContestDescriptionComponent :contest="contest"
          /></a-tab-pane>
          <a-tab-pane key="2" tab="题目" force-render
            ><ContestProblemComponent :contest="contest"
          /></a-tab-pane>
          <a-tab-pane key="3" tab="提交">Content of Tab Pane 3</a-tab-pane>
          <a-tab-pane key="4" tab="排名">Content of Tab Pane 4</a-tab-pane>
        </a-tabs></a-col
      >
    </a-row>
  </div>
</template>

<script>
import { NotificationTwoTone, HomeTwoTone } from "@ant-design/icons-vue";
import { User, Timer, Flag, Trophy } from "@element-plus/icons-vue";
import ContestDescriptionComponent from "@/components/component/contest/ContestDescriptionComponent.vue";
import ContestProblemComponent from "@/components/component/contest/ContestProblemComponent.vue";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
export default {
  components: {
    NotificationTwoTone,
    ContestDescriptionComponent,
    ContestProblemComponent,
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
      })
      .then((res) => {
        this.contest = res.data;

        console.log(this.contest, new Date());
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

<style>
.row1 {
  margin-top: 20px;
}
.my-tabs .ant-tabs-tab {
  font-size: 24px;
}
.my-tabs .ant-tabs-tab:not(:last-child) {
  margin-right: 40px; /* 设置标签之间的间距 */
}
</style>
