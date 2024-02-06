<template>
  <div style="width: 90%">
    <a-row
      style="
        min-height: 40px;
        background-color: rgb(205, 205, 205);
        border-radius: 10px 10px 0px 0px;
      "
    >
      <a-col
        style="
          display: flex;
          align-items: center;
          margin-left:13px;
   
        "
      >
        <img
          style="width: 30px"
          src="../../../assets/static/pictures/公告.png"
          alt=""
        />
      </a-col>
      <a-col
        style="
          display: flex;
          align-items: center;
          margin-left: 5px;
          margin-top: 5px;
        "
      >
        <div
          class="notice"
          style="font-family: 'Arial'; font-weight: bold; font-size: 20px"
        >
          公告
        </div>
      </a-col>
    </a-row>
    <a-row>
      <div
        style="
          width: 100%;
          min-height: 250px;
          background-color: rgb(255, 255, 255);
          border-radius: 0px 0px 10px 10px;

          padding: 15px;
        "
      >
        <div style="display: flex; justify-content: space-between">
          <span style="font-weight: bold; font-size: 18px">标题</span>
          <span style="font-weight: bold; font-size: 18px">时间</span>
        </div>

        <div style="" v-for="(content, index) in notice" :key="index">
          <div
            style="
              margin-top: 10px;
              width: 100%;
              height: 1px;
              transform: scaleY(0.7);
              background-color: rgb(208, 208, 208);
            "
          ></div>

          <div
            style="
              font-size: 18px;
              margin-top: 10px;
              display: flex;
              justify-content: space-between;
            "
          >
            <span @click="jumpTodiscuss(content)" style="cursor: pointer; color: #4183c4" class="hoverable2">
              {{ content.title }}
            </span>
            <span>
              {{ content.edittime.slice(0, 10) }}
            </span>
          </div>
        </div>
      </div>
    </a-row>
  </div>
</template>

<script setup>
import { SERVER_URL } from "@/js/functions/config";
import axios from "axios";
import { onMounted, ref } from "vue";
import router from "@/router/router";

const notice = ref([]);
function jumpTodiscuss(data) {
  router.push({
    path: "/discusscontent",
    query: {
      id: data.id,
      type: data.type,
    },
  });
}
onMounted(async () => {
  // 获取公告列表
  await axios
    .get(`${SERVER_URL}/discuss/query/all`, {
      params: {
        userid: 0,
      },
    })
    .then((res) => {
      // console.log(res.data);
      res.data.forEach((element) => {
        //把公告放进来
        if (element.type === "notice") {
          notice.value.push(element);
        }
      });

      console.log(notice);
    })
    .catch(() => {
      console.log("");
    });
});
</script>

<style scoped>
.hoverable2 {
  /* color: #3498db; */
  transition: color 0.3s ease;
  cursor: pointer;
}

.hoverable2:hover {
  filter: brightness(1.5);
}
</style>