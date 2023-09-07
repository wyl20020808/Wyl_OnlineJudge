<template>
  <div v-for="(contest, index) in contestList" :key="contest.contestid">
    <div
      v-if="
        index === 0 ||
        getContestStatus(contestList[index - 1]) !== getContestStatus(contest)
      "
    >
      <h2>{{ getContestStatusText(contest) }}</h2>
    </div>
    <div class="card mb-3 card1" style="max-width: 1000px; max-height: 400px">
      <div class="row g-0">
        <div class="col-md-3 d-flex justify-content-center align-items-center">
          <img :src="SERVER_URL" class="img-fluid rounded-start" alt="..." />
        </div>
        <div class="col-md-7">
          <div class="card-body">
            <h2 class="card-title">{{ contest.contestname }}</h2>
            <div class="contesttime1 d-flex align-items-center">
              <NotificationTwoTone style="fontsize: 24px" />
              <span style="font-size: 16px; margin-left: 5px">
                比赛时间：{{ toBeijingTime(contest.startdate) }} ~
                {{ toBeijingTime(contest.enddate) }} ({{
                  getTimeDifference(contest.startdate, contest.enddate)
                }})
              </span>
            </div>
            <div class="organizer1 d-flex align-items-center">
              <HomeTwoTone style="fontsize: 24px" />
              <span style="font-size: 16px; margin-left: 5px"
                >主办方：{{ contest.username }}</span
              >
            </div>
            <div class="people1 d-flex align-items-center">
              <el-icon size="20" color="rgb(24, 144, 255)"><User /></el-icon>
              <span style="font-size: 16px; margin-left: 5px"
                >参赛人数：{{ contest.joinpeople }}</span
              >
            </div>
          </div>
        </div>
        <div class="col-md-2">
          <div
            class="card-body d-flex flex-column justify-content-center align-items-center"
            style="height: 100%"
          >
            <a-button
              size="large"
              style="
                color: white;
                width: 150px;
                margin-right: 20px;
                background-color: #1dad8e;
              "
              type="primary"
              @click="joinContest(contest.id)"
              v-if="cmpNowTime(contest.enddate) === true"
              >报名</a-button
            >
            <a-button
              size="large"
              style="
                color: white;
                width: 150px;
                margin-right: 20px;
                background-color: rgb(166, 164, 164);
              "
              type="primary"
              v-else
              >回顾比赛</a-button
            >
            <div class="countdown">
              <Countdown
                :startdate="contest.startdate"
                :enddate="contest.enddate"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { SERVER_URL } from "../../../js/functions/config";
import { NotificationTwoTone, HomeTwoTone } from "@ant-design/icons-vue";
import { User } from "@element-plus/icons-vue";
import axios from "axios";

import Countdown from "../others/Countdown.vue";
export default {
  name: "Card",
  components: {
    NotificationTwoTone,
    HomeTwoTone,
    User,
    Countdown,
  },
  methods: {
    joinContest(contestid){

    },
    getContestStatus(contest) {
      let now = new Date();
      let start = new Date(contest.startdate);
      let end = new Date(contest.enddate);
      if (now < start) return "未开始";
      if (now >= start && now < end) return "进行中";
      return "已结束";
    },
    getContestStatusText(contest) {
      let status = this.getContestStatus(contest);
      if (status === "未开始") return "即将开始的比赛";
      if (status === "进行中") return "正在进行的比赛";
      return "已结束的比赛";
    },
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
    cmpNowTime(time) {
      return new Date() <= new Date(time);
    },
  },
  data() {
    return {
      SERVER_URL: `${SERVER_URL}/images/icpc_logo.png`,
      contestList: [],
    };
  },
  async created() {
    await axios
      .get(`${SERVER_URL}/contest/query`)
      .then((res) => {
        this.contestList = res.data;
        // 对比赛列表进行排序
        this.contestList.sort((a, b) => {
          let now = new Date();
          let aStart = new Date(a.startdate);
          let aEnd = new Date(a.enddate);
          let bStart = new Date(b.startdate);
          let bEnd = new Date(b.enddate);
          // 比较比赛的状态
          if (now < aStart && now >= bStart) return 1;
          if (now >= aStart && now < aEnd && (now < bStart || now >= bEnd))
            return -1;
          if (now >= aEnd && now < bEnd) return 1;
          // 如果比赛的状态相同，比较开始时间
          if (aStart < bStart) return -1;
          if (aStart > bStart) return 1;
          return 0;
        });
      })
      .catch((err) => {
        console.log(err);
      });
  },
};
</script>

<style scoped>
.d-flex {
  display: flex;
}
.justify-content-center {
  justify-content: center;
}
.align-items-center {
  align-items: center;
}
.contesttime1 {
  margin-top: 20px;
}
.organizer1 {
  margin-top: 10px;
}
.people1 {
  margin-top: 10px;
  position: relative;
  right: 4px;
}

.countdown {
  margin-top: 5px;
}
</style>
