<template>
  <div>
    <BaseComponentView />
    <router-view :key="$route.fullPath" />
  </div>
</template>

<script>
import "bootstrap/dist/css/bootstrap.css";
import axios from "axios";
import "bootstrap/dist/js/bootstrap.js";
import BaseComponentView from "./components/component/NavBar.vue";
import MainView from "./components/pages/MainView.vue";
import { SERVER_URL } from './js/functions/config';
export default {
  name: "App",
  components: {
    BaseComponentView,
    MainView,
  },
  async created() {
    await axios
      .post(`${SERVER_URL}/user/query`, {
        userid: 17,
      })
      .then((res) => {
        localStorage.setItem("user", JSON.stringify(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  },
};
</script>

<style>
body {
  background-color: rgb(240, 240, 240); /* 你想要的颜色 */
}
</style>
