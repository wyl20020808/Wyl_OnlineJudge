<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="collapse navbar-collapse">
      <ul class="navbar-nav">
        <li class="nav-item">
          <router-link class="nav-link" :to="{ name: 'home' }"
            >首页</router-link
          >
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{ name: 'problems' }"
            >题库</router-link
          >
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{ name: 'competition' }"
            >比赛</router-link
          >
        </li>
        <li class="nav-item logo">
          <router-link :to="{ name: 'home' }">
            <img
              class="avatar"
              src="../../assets/static/pictures/templogo.png"
              alt="Avatar"
            />
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{ name: 'ranklist' }"
            >排行榜</router-link
          >
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{ name: 'tissue' }"
            >组织</router-link
          >
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{ name: 'evaluationqueue' }"
            >评测队列</router-link
          >
        </li>
        <li class="nav-item dropdown" v-if="userloginstate === 'true'">
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
                  ><div @click="toUseInfo" class="dropdown-item"
                    ><el-icon><UserFilled /></el-icon
                    >&ensp;个人资料</div
                  ></el-dropdown-item
                >
                <el-dropdown-item
                  ><router-link :to="{ name: 'home' }" class="dropdown-item"
                    ><el-icon><Setting /></el-icon> &ensp;设置</router-link
                  ></el-dropdown-item
                >
                <el-dropdown-item
                  ><div @click="toMessage" class="dropdown-item"
                    ><el-icon><Bell /></el-icon>&ensp;消息</div
                  > <a-badge :count="unRead" class="item">
   
  </a-badge>
                  </el-dropdown-item
                >
                <el-dropdown-item
                  ><a @click="logout" class="dropdown-item"
                    ><el-icon><SwitchButton /></el-icon>&ensp;退出</a
                  ></el-dropdown-item
                >
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </li>
        <li class="nav-item" v-else>
          <router-link class="nav-link" :to="{ name: 'userlogin' }"
            >登录</router-link
          >
        </li>
      </ul>
    </div>
  </nav>
  <!-- <div>

        <UserSelectComponent />
    </div> -->
</template>

<script>
import { SERVER, SERVER_URL } from "../../js/functions/config";
import {
  Setting,
  UserFilled,
  Bell,
  SwitchButton,
} from "@element-plus/icons-vue";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap/dist/js/bootstrap.js";
import { sleep } from "@/js/functions/TimeAbout.js";
import router from "@/router/router";
import axios from "axios";

export default {
  name: "BaseComponentView",
  components: {
    Setting,
    UserFilled,
    Bell,
    SwitchButton,
  },
  data(){
    return{
      unRead:0,
    }
  },
  computed: {
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
    window.onbeforeunload = () => {
      // console.log(userinfo.userloginstate)
    };
    if(localStorage.getItem("user")){//如果登录了的话
      
      this.getUnreadMessage();
    }
    

  },
  setup() {},
  methods: {
    async getUnreadMessage(){
      //统计一下未读的消息
      await axios.get(`${SERVER_URL}/message/query/unread`,{
        params:{
          receiver:JSON.parse(localStorage.getItem("user")).userid
        }
      })
      .then(response => {
        console.log(response.data.length)
        if(response.data)
          this.unRead += response.data.length;
      })
      .catch(err => {
        console.log(err);
      })
      
    },
    toMessage(){
      router.push({path:'/message'});
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
     
      sleep(500).then(() => {
        window.location = `${SERVER}`;
      });
      // router.push({ name: "home" });
    },
  },
};
</script>

<style scoped>
.navbar {
  padding-left: 50px;
  border-bottom: 1px solid #ccc;
}
.navbar-nav {
  width: 100%;
  justify-content: space-between;
}
.navbar-nav .logo {
  flex-grow: 2.5;
}
.nav-item {
  flex-grow: 1;
  text-align: center;
  /* border-right: 1px solid #ccc; */
}
.nav-item:last-child {
  border-right: none;
  margin-right: 20px;
}
.navbar-brand {
  font-size: 2em;
}
.nav-link {
  font-size: 1.2em;
  outline: none;
}
.avatar {
  border-radius: 50%;
  width: 40px;
  height: 40px;
  outline: none;
}
</style>
