<template>
  <div>
    <NavBar />
    <router-view :key="$route.fullPath" />
    <floatButton style="z-index: 1000" />
  </div>
</template>

<script>
import "bootstrap/dist/css/bootstrap.css";
import axios from "axios";
import "bootstrap/dist/js/bootstrap.js";
import NavBar from "./components/component/NavBar.vue";
import MainView from "./components/pages/MainView.vue";
import { SERVER_URL } from "./js/functions/config";
import floatButton from "./components/component/floatButton/floatButton.vue";
import { computed, watch } from 'vue';
import { useRoute } from "vue-router";
export default {
  name: "App",
  components: {
    NavBar,
    MainView,
    floatButton,
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
