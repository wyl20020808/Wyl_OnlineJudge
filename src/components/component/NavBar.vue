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
        <li
          class="nav-item dropdown"
          v-if="userloginstate === 'true'"
        >
          <el-dropdown>
            <span class="el-dropdown-link">
              <img
                class="avatar"
                :src="userpicture"
                alt="Avatar"
              />

              <el-icon class="el-icon--right">
                <arrow-down />
              </el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item
                  ><router-link :to="{ name: 'userhome' }" class="dropdown-item"
                    ><el-icon><UserFilled /></el-icon
                    >&ensp;个人资料</router-link
                  ></el-dropdown-item
                >
                <el-dropdown-item
                  ><router-link :to="{ name: 'home' }" class="dropdown-item"
                    ><el-icon><Setting /></el-icon> &ensp;设置</router-link
                  ></el-dropdown-item
                >
                <el-dropdown-item
                  ><router-link :to="{ name: 'home' }" class="dropdown-item"
                    ><el-icon><Bell /></el-icon>&ensp;通知</router-link
                  ></el-dropdown-item
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
import {
  Setting,
  UserFilled,
  Bell,
  SwitchButton,
} from "@element-plus/icons-vue";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap/dist/js/bootstrap.js";
import {sleep} from "@/js/functions/TimeAbout.js"
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
  computed:{
    userloginstate: function() {
      // return "false";
    return JSON.parse(localStorage.getItem('user')).userloginstate;
  },
  userpicture: function() {
      return JSON.parse(localStorage.getItem('user')).userpicture;
    }
  },
  created() {
    window.onbeforeunload = () => {

      // console.log(userinfo.userloginstate)

    };
  },
  setup() {},
  methods: {
    logout() {
      const userinfo = JSON.parse(localStorage.getItem("user"));
      this.$store.dispatch("notice", {
        title: "退出成功！",
        message: "再见！" + userinfo.username,
        type: "success",
        
      });
      this.$store.dispatch("SynchronizeInfo",{
          userinfo,
          loginState:false
      })
      sleep(500).then(()=>{
        window.location = "http://localhost:8080/";
      })
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
