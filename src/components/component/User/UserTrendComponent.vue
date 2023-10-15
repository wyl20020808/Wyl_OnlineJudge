<template>
  <a-row>
    <a-col style="width: 100%">
      <a-row style="margin-bottom: 20px" v-for="item in discuss" :key="item.id">
        <a-col style="width: 100%">
          <a-row style="color: rgb(128, 128, 128)" justify="space-between">
            <a-col
              style="font-size: 18px;"
              v-if="['discuss', 'solution', 'notice'].includes(item.type)"
            >
              发表了文章
            </a-col>
            <a-col style="font-size: 18px" v-else>发表了评论</a-col>
            <a-col style="font-size: 18px">
              {{ item.createtime }}
            </a-col>
          </a-row>

          <a-row  style="margin-top: 20px;margin-bottom: 20px;">
            <a-col @click="jumpToDiscuss(item.id)" v-if="item.title">
              <h3 class="hoverable2" style="font-family: SimSun;">{{ item.title }}</h3>
            </a-col>
            <a-col @click="jumpToDiscuss(item.discussid)" v-if="item.discusstitle">
              <h3 class="hoverable2" style="font-family: SimSun;">{{ item.discusstitle }}</h3>
            </a-col>
          </a-row>
          <a-row style="width: "
            ><DiscussContentComponent :discuss="item" :type="'trend'"
          /></a-row>
          <a-divider />
        </a-col>
      </a-row>
      <a-row style="justify-content: center" v-if="discuss.length === 0">
        <a-empty />
      </a-row>
    </a-col>
  </a-row>
</template>

<script setup>
import { getNowTime } from "@/js/functions/TimeAbout";
import { onBeforeUnmount, onMounted, onBeforeMount } from "vue";
import router from "@/router/router";
import { ref, watch } from "vue";
import axios from "axios";
import { onBeforeRouteLeave } from "vue-router";
import DiscussContentComponent from "@/components/component/discuss/DiscussContentComponent.vue";
import { SERVER_URL } from "@/js/functions/config";
import { useStore } from "vuex";
import { useRoute } from "vue-router";
import _ from "lodash";
import CommentComponent from "@/components/component/discuss/CommentComponent.vue";
import { sleep } from "@/js/functions/TimeAbout";
import { defineProps } from "vue";
let discuss = ref([]);
const props = defineProps({
  userid: Number,
});
const jumpToDiscuss = (discussid) => {
  router.push({path: "/discusscontent",query:{
    id: discussid,
  }})
}
const getAllDiscuss = async () => {
  await axios
    .get(`${SERVER_URL}/discuss/query/all`, {
      params: {
        userid: props.userid,
      },
    })
    .then((res) => {
      discuss.value = res.data;
      console.log(discuss.value, "评论的值");
    })
    .catch((err) => {
      console.log(err);
    });
};
watch(
  () => props.userid,
  async (newValue, oldValue) => {
    // 在父组件属性发生变化时执行初始化逻辑
    // 执行初始化逻辑，例如获取讨论状态等
    await getAllDiscuss();
  }
);

onBeforeMount(async () => {
  await getAllDiscuss();
});
</script>

<style scoped>
.hoverable2 {
  /* color: #3498db; */
  transition: color 0.3s ease;
  cursor: pointer;
}

.hoverable2:hover {
  filter: brightness(1.3);
  color: rgb(67, 148, 254);
}
</style>
