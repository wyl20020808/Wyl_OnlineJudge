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
            <a-row style="display: flex; align-items: center">
              <el-icon
                size="40"
                color="rgb(24, 144, 255)"
                style="margin-right: 8px"
                ><Trophy
              /></el-icon>
              <h2
                @click="viewContest(contest.contestid)"
                class="card-title hoverable"
              >
                {{ contest.contestname }}
              </h2></a-row
            >

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
              @click="viewContest(contest.contestid)"
              v-if="
                cmpNowTime(contest.enddate) === true &&
                joinSet.has(contest.contestid) === true &&
                getContestStatus(contest) === '进行中'
              "
              >进入比赛</a-button
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
              @click="cancleJoinContest(contest.contestid)"
              v-else-if="
                cmpNowTime(contest.enddate) === true &&
                joinSet.has(contest.contestid) === true &&
                getContestStatus(contest) === '未开始'
              "
              >已报名
            </a-button>
            <a-button
              size="large"
              style="
                color: white;
                width: 150px;
                margin-right: 20px;
                background-color: #1dad8e;
              "
              type="primary"
              @click="
                joinContest(
                  contest.contestid,
                  contest.contestlimit,
                  contest.contestpassword
                )
              "
              v-else-if="cmpNowTime(contest.enddate) === true"
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
              @click="viewContest(contest.contestid)"
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
  <!-- <a-button @click="dialogVisible = true" >uye</a-button> -->
  <el-dialog
    title="Please Input The Password"
    v-model="dialogVisible"
    width="30%"
  >
    <el-input v-model="password" placeholder="password"></el-input>
    <template v-slot:footer>
      <el-button
        @click="
          dialogVisible = false;
          password = '';
        "
        >取 消</el-button
      >
      <el-button style="color: white" type="primary" @click="handleConfirm"
        >确 定</el-button
      >
    </template>
  </el-dialog>
</template>

<script>
import { SERVER_URL } from "../../../js/functions/config";
import { NotificationTwoTone, HomeTwoTone } from "@ant-design/icons-vue";
import { User, Trophy } from "@element-plus/icons-vue";
import axios from "axios";
import { ElDialog, ElInput } from "element-plus";

import Countdown from "../others/Countdown.vue";
export default {
  name: "Card",
  components: {
    NotificationTwoTone,
    HomeTwoTone,
    Trophy,
    User,
    Countdown,
    ElDialog,
    ElInput,
  },

  methods: {
    async cancleJoinContest(contestid) {
      await axios
        .post(`${SERVER_URL}/contest/join/personal/cancel`, {
          contestid: contestid,
          userid: JSON.parse(localStorage.getItem("user")).userid,
        })
        .then(async (res) => {
          if (res.data > 0) {
            this.$store.dispatch("notice", {
              title: "取消成功！",
              message: "",
              type: "success",
            });
          }
          this.joinSet.delete(contestid); //把这个比赛的id去掉
          for (let i = 0; i < this.contestList.length; i++) {
            if (this.contestList[i].contestid === contestid) {
              this.contestList[i].joinpeople -= 1;
            }
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    viewContest(contestid) {
      this.$router.push({
        name: "contestcontest",
        query: {
          contestid: contestid,
        },
      });
    },
    async joinContest(contestid, contestlimit, contestpassword) {
      if (contestlimit === "私密(需要密码)") {
        // 如果比赛需要密码，显示对话框
        console.log("123");
        this.contestpassword = contestpassword;
        this.dialogVisible = true;
        this.contestid = contestid; // 添加这一行
      } else {
        // 如果比赛不需要密码，直接报名
        await this.registerContest(contestid);
      }
    },
    async handleConfirm() {
      // 检查密码是否正确
      if (this.password === this.contestpassword) {
        // 如果密码正确，报名比赛并关闭对话框
        await this.registerContest(this.contestid);
        this.dialogVisible = false;
      } else {
        // 如果密码错误，显示错误信息
        this.$message.error("密码错误，请重新输入");
      }
      // 清空密码
      this.password = "";
    },
    async registerContest(contestid) {
      await axios
        .post(`${SERVER_URL}/contest/join/personal`, {
          contestid: contestid,
          userid: JSON.parse(localStorage.getItem("user")).userid,
        })
        .then(async (res) => {
          if (res.data === 1) {
            this.$store.dispatch("notice", {
              title: "报名成功！",
              message: "",
              type: "success",
            });
          }
          this.joinSet.add(contestid); //把这个比赛的id放入进去
          for (let i = 0; i < this.contestList.length; i++) {
            if (this.contestList[i].contestid === contestid) {
              this.contestList[i].joinpeople += 1;
            }
          }
        })
        .catch((err) => {
          console.log(err);
        });
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
    async checkJoinState() {
      this.joinSet.clear(); //清空一下
      //查询一下用户的报名信息
      await axios
        .get(`${SERVER_URL}/contest/query/join/personal`, {
          params: {
            userid: JSON.parse(localStorage.getItem("user")).userid,
          },
        })
        .then((res) => {
          for (let i = 0; i < res.data.length; i++) {
            //把已注册的id放进去
            this.joinSet.add(res.data[i].contestid);
          }
        })
        .catch((err) => {
          this.$store.dispatch("notice", {
            title: "查询报名信息失败！",
            message: "服务器异常" + err,
            type: "error",
          });
        });
    },
  },
  data() {
    return {
      SERVER_URL: `${SERVER_URL}/images/icpc_logo.png`,
      contestList: [],
      joinSet: new Set(),
      dialogVisible: false,
      password: "",
      contestpassword: "",
      contestid: null,
      dialog: false,
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
          let aStatus = now < aStart ? 1 : now < aEnd ? 2 : 0;
          let bStatus = now < bStart ? 1 : now < bEnd ? 2 : 0;

          if (aStatus !== bStatus) {
            // 如果状态不同，按照 "进行中" > "未开始" > "已结束" 的顺序排序
            return bStatus - aStatus;
          } else {
            // 如果状态相同，按照开始时间排序
            return aStart - bStart;
          }
        });
      })
      .catch((err) => {
        console.log(err);
      });
    await this.checkJoinState();
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
.hoverable {
  transition: color 0.3s ease, text-decoration 0.3s ease;
  cursor: pointer;
}

.hoverable:hover {
  filter: brightness(1.3);
  text-decoration: underline;
}
</style>
