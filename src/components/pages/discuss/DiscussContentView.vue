<template>
  <a-row
    style="
      margin-left: 80px;
      margin-top: 30px;
      align-items: center;
      justify-content: center;
    "
  >
    <a-col>
      <a-row style="margin-top: 10px; margin-bottom: 20px">
        <a-breadcrumb>
          <!-- <a-breadcrumb-item>Home</a-breadcrumb-item>
          <a-breadcrumb-item
            ><a href="">Application Center</a></a-breadcrumb-item
          > -->
          <a-breadcrumb-item
            ><a @click="jump('discuss')">讨论</a></a-breadcrumb-item
          >
          <a-breadcrumb-item>文章详情</a-breadcrumb-item>
        </a-breadcrumb>
      </a-row>
      <a-row style="margin-bottom: 10px; width: 1150px" justify="space-between">
        <a-col
          ><h3 style="">{{ discuss.title }}</h3></a-col
        >
        <a-col
          ><a-button
            @click="editDiscuss"
            style="display: flex; align-items: center"
            ><img src="../../../assets/static/pictures/edit.png" width="23" />
            <div style="font-size: 16px; margin-left: 10px">
              编辑文章
            </div></a-button
          ></a-col
        >
      </a-row>
      <a-row>
        <a-col
          ><img
            :src="discuss.userpicture"
            style="width: 50px; border-radius: 50%"
            alt="Avatar"
          />
        </a-col>
        <a-col style="width: 1200px; margin-left: 20px">
          <DiscussContentComponent :discuss="discuss" />
        </a-col>
      </a-row>
    </a-col>
  </a-row>
</template>
<!-- 2023年10月12日14:57:40 -->
<script setup>
import { getNowTime } from "@/js/functions/TimeAbout";
import { onBeforeUnmount, onMounted } from "vue";
import router from "@/router/router";
import { ref } from "vue";
import axios from "axios";
import { onBeforeRouteLeave } from "vue-router";
import DiscussContentComponent from "@/components/component/discuss/DiscussContentComponent.vue";
import { SERVER_URL } from "@/js/functions/config";
import { useStore } from "vuex";
import { useRoute } from "vue-router";
import _ from "lodash";
import { sleep } from "@/js/functions/TimeAbout";

let route = useRoute();
let id = ref(1);
let type = ref("discuss");
let discuss = ref({});
function editDiscuss() {
  router.push({
    path: "/creatediscuss",
    query: {
      id: id.value,
      type: type.value,
    },
  });
}
function jump(total) {
  router.push({ path: "/" + total });
}
async function getDiscuss() {
  //获取当前的讨论
  await axios
    .get(`${SERVER_URL}/discuss/query`, {
      params: {
        id: id.value,
        type: type.value,
      },
    })
    .then((res) => {
      discuss.value = res.data[0];
    })
    .catch((err) => {
      console.log(err);
    });
}
function addBrowse(){
    axios
    .post(`${SERVER_URL}/discuss/operator`, {
      id: route.query.id,
      special: "browse",
      detal:1,
    })
    .then((res) => {
      
    })
    .catch((err) => {
      console.log(err);
    });
}
onMounted(async () => {
  id.value = route.query.id;
  type.value = route.query.type;
  await getDiscuss();
  addBrowse();
});
</script>

<style scoped></style>
