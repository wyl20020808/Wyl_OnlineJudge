<template>
  <div class="card card1">
    <div @click="jumpToUserInfo(judgeinfo.judge.userid)" class="user1 hoverable">
      <img class="image1" :src="userinfo.userpicture" alt="Avatar" />
      <div style="position: relative;color: #5f9fd6; left: 15px; font-size: 24px; top: 15px">
        {{ judgeinfo.judge.username }}
      </div>
    </div>
    <div class="problem1">
      题目：
      <div style=" color: #5f9fd6;" @click="push_to_problemcontent(judgeinfo.judge.problemid)" class="hoverable">
        P{{ judgeinfo.judge.problemid }} - {{ judgeinfo.judge.problemname }}
      </div>
    </div>
    <div class="language1">语言：{{ judgeinfo.judge.language }}</div>
    <div class="judgestate1">评测状态：
        <div :style="{color:getColor(judgeinfo.judge.judgestate)}"> {{ judgeinfo.judge.judgestate }}</div>
        
    </div>
    <div class="submittime1">提交时间：{{ judgeinfo.judge.submittime }}</div>
    <div class="runtime1">运行时间：{{ judgeinfo.judge.runtime }} ms</div>
    <div class="memory1">内存消耗：{{ judgeinfo.judge.memory }} KB</div>
  </div>
</template>

<script>
import { SERVER_URL } from "@/js/functions/config";
import axios from "axios";
import router from "@/router/router";
export default {
  props: {
    judgeinfo: {
      type: Object,
      required: true,
    },
  },
  methods: {
    jumpToUserInfo(userid) {
      router.push({
        name: "userhome",
        query: { userid },
      });
    },
    push_to_problemcontent(problemid) {
      router.push({
        path: "/problem",
        query: {
          problemid: problemid,
        },
      });
    },
    getColor(judgestate) {
        if (judgestate === "Accepted") return "#25ad40";
      else if (judgestate === "Compilation Error") return "orange";
      else return "red";
    }
  },
  data() {
    return {
      userinfo: {},
    };
  },
  async created() {
    await axios
      .post(`${SERVER_URL}/user/query`, {
        userid: this.judgeinfo.judge.userid,
      })
      .then((response) => {
        this.userinfo = response.data;
      })
      .catch((error) => {});
  },
};
</script>

<style scoped>
.card1 {
  width: 350px;
  height: 370px;
  position: absolute;
}
.image1 {
  border-radius: 50%;
  width: 70px;
  height: 70px;
  outline: none;
}
.user1 {
  position: relative;
  left: 15px;
  top: 20px;
  display: flex;
}
.problem1 {
  position: relative;
  left: 15px;
  top: 40px;
  display: flex;
  font-size: 20px;
}
.language1 {
  position: relative;
  left: 15px;
  top: 50px;

  font-size: 20px;
}
.judgestate1 {
  position: relative;
  left: 15px;
  top: 60px;
  font-size: 20px;
  display: flex;
}
.submittime1 {
  position: relative;
  left: 15px;
  top: 70px;

  font-size: 20px;
}
.runtime1 {
  position: relative;
  left: 15px;
  top: 80px;

  font-size: 20px;
}
.memory1 {
  position: relative;
  left: 15px;
  top: 90px;

  font-size: 20px;
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
