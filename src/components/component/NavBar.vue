<template>
  <div>
    <v-layout class="overflow-visible" style="height: 56px">
      <v-bottom-navigation
        height="70"
        v-model="value"
        grow
        :bg-color="color"
        mode="shift"
      >
        <v-btn @click="jump('')">
          <v-icon><HomeFilled style="font-size: 25px" /></v-icon>
          <span style="font-size: 16px; margin-top: 5px">主页</span>
        </v-btn>
        <v-btn @click="jump('problems')">
          <v-icon
            ><img src="../../assets/static/pictures/题库.png" width="30"
          /></v-icon>
          <span style="font-size: 16px; margin-top: 5px">题库</span>
        </v-btn>
        <v-btn @click="jump('competition')">
          <v-icon><TrophyFilled style="font-size: 25px" /></v-icon>
          <span style="font-size: 16px; margin-top: 5px">比赛</span>
        </v-btn>
        <v-btn @click="jump('ranklist')">
          <v-icon
            ><img src="../../assets/static/pictures/rank.png" width="30"
          /></v-icon>
          <span style="font-size: 16px; margin-top: 5px">排行榜</span>
        </v-btn>
        <v-btn @click="jump('tissue')">
          <v-icon
            ><img src="../../assets/static/pictures/acm.png" width="30"
          /></v-icon>
          <span style="font-size: 16px; margin-top: 5px">工作室</span>
        </v-btn>
        <v-btn @click="jump('evaluationqueue')">
          <v-icon><HourglassFilled style="font-size: 25px" /></v-icon>
          <span style="font-size: 16px; margin-top: 5px">评测队列</span>
        </v-btn>
        <v-btn @click="jump('discuss')">
          <v-icon><img src="../../assets/static/pictures/discuss.png" width="30"
          /></v-icon>
          <span style="font-size: 16px; margin-top: 5px">讨论</span>
        </v-btn>
        <div
          style="margin-left: 100px; margin-top: 15px"
          class="nav-item dropdown"
          v-if="userloginstate === 'true'"
        >
          <el-dropdown>
            <span class="el-dropdown-link">
              <img class="avatar" :src="userpicture" alt="Avatar" />
              <a-badge v-if="unRead > 0" dot></a-badge>
              <el-icon class="el-icon--right">
                <arrow-down />
              </el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item
                  ><div @click="toUseInfo" class="dropdown-item">
                    <el-icon><UserFilled /></el-icon>&ensp;个人资料
                  </div></el-dropdown-item
                >
                <el-dropdown-item
                  ><router-link :to="{ name: 'home' }" class="dropdown-item"
                    ><el-icon><Setting /></el-icon> &ensp;设置</router-link
                  ></el-dropdown-item
                >
                <el-dropdown-item
                  ><div @click="toMessage" class="dropdown-item">
                    <el-icon><Bell /></el-icon>&ensp;消息
                  </div>
                  <a-badge :count="unRead" class="item"> </a-badge>
                </el-dropdown-item>
                <el-dropdown-item
                  ><a @click="logout" class="dropdown-item"
                    ><el-icon><SwitchButton /></el-icon>&ensp;退出</a
                  ></el-dropdown-item
                >
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <v-btn class="nav-item" v-else @click="jump('userlogin')">
          <v-icon
            ><img src="../../assets/static/pictures/login.png" width="30"
          /></v-icon>
          <span style="font-size: 16px; margin-top: 5px">登录</span>
        </v-btn>
      </v-bottom-navigation>
    </v-layout>
  </div>
</template>
<script>
import {
  Setting,
  UserFilled,
  Bell,
  SwitchButton,
} from "@element-plus/icons-vue";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap/dist/js/bootstrap.js";
import {
  HomeFilled,
  HourglassFilled,
  TrophyFilled,
  BookFilled,
} from "@ant-design/icons-vue";
import { SERVER, SERVER_URL } from "../../js/functions/config";
import { sleep } from "@/js/functions/TimeAbout.js";
import router from "@/router/router";
import axios from "axios";
export default {
  components: {
    HomeFilled,
    HourglassFilled,
    TrophyFilled,
    BookFilled,
    Setting,
    UserFilled,
    Bell,
    SwitchButton,
  },
  data: () => ({
    value: 0,
    unRead: 0,
  }),
  watch: {
    $route(to, from) {
      this.updateNavState();
      console.log(this.value)
      localStorage.setItem('nav',this.value);
    },
  },
  computed: {
    currentPath() {
      return this.$route.path;
    },
    color() {
      switch (this.value) {
        case 0:
          return "purple";
        case 1:
          return "teal";
        case 2:
          return "brown";
        case 3:
          return "indigo";
        case 4:
          return "blue";
        case 5:
          return "red";
        default:
          return "blue-grey";
      }
    },
    userloginstate: function () {
      let user = localStorage.getItem("user");
      if (user) {
        return JSON.parse(user).userloginstate;
      } else {
        return "false";
      }
    },
    userpicture: function () {
      let user = localStorage.getItem("user");
      if (user) {
        return JSON.parse(user).userpicture;
      } else {
        return ""; // 返回一个默认的图片URL或者空字符串
      }
    },
  },
  
  async created() {
    this.updateNavState();
    window.onbeforeunload = () => {
      // console.log(userinfo.userloginstate)
    };
    if (localStorage.getItem("user")) {
      //如果登录了的话
      this.getUnreadMessage();
    }
    
  },
  methods: {
    updateNavState() {
      switch (this.currentPath) {
        case "/":
          this.value = 0;
          break;
        case "/problems":
          this.value = 1;
          break;
        case "/competition":
          this.value = 2;
          break;
        case "/ranklist":
          this.value = 3;
          break;
        case "/tissue":
          this.value = 4;
          break;
        case "/evaluationqueue":
          this.value = 5;
          break;
        case "/discuss":
          this.value = 6;
          break;
        default:
          this.value = localStorage.getItem('nav');
          break;
      }
    },
    jump(total) {
      router.push({ path: "/" + total });
    },
    async getUnreadMessage() {
      //统计一下未读的消息
      await axios
        .get(`${SERVER_URL}/message/query/unread`, {
          params: {
            receiver: JSON.parse(localStorage.getItem("user")).userid,
          },
        })
        .then((response) => {
          console.log(response.data.length);
          if (response.data) this.unRead += response.data.length;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    toMessage() {
      router.push({ path: "/message" });
    },
    toUseInfo() {
      router.push({
        name: "userhome",
        query: { userid: JSON.parse(localStorage.getItem("user")).userid },
      });
    },
    logout() {
      console.log("logout");
      const userinfo = JSON.parse(localStorage.getItem("user"));

      this.$store.dispatch("SynchronizeInfo", {
        userinfo,
        loginState: false,
      });
      localStorage.setItem("user", null);
      sleep(500).then(() => {
        window.location = `${SERVER}`;
      });
      // router.push({ name: "home" });
    },
  },
};
</script>

<style scoped>
.v-bottom-navigation {
  position: fixed;
  top: 0;
  width: 100%;
}

.avatar {
  border-radius: 50%;
  width: 40px;
  height: 40px;
  outline: none;
}
</style>
