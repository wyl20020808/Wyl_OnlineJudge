<template>
  <a-row style="display: flex; align-items: center; justify-content: center;margin-bottom: 20px;">
    <a-col>
      <a-row
        style="
          width: 1000px;
          background-color: white;
          border-radius: 5px;
          padding: 20px;
        "
      >
        <a-col>
          <a-row>
            <span
              style="font-size: 22px; font-family: Arial, Helvetica, sans-serif"
              >{{ collectSet.name }}</span
            >
          </a-row>
          <a-row
            style="color: rgb(58, 58, 58); margin-top: 10px"
            v-if="collectSet.note"
          >
            {{ collectSet.note }}
          </a-row>
          <a-row style="margin-top: 10px; color: gray">
            <a-col style="font-size: 16px">
              {{ String(collectSet.modifytime).slice(0, 10) }} 更新
            </a-col>

            
          </a-row>
        </a-col>
      </a-row>
      <a-row
        style="
          margin-top: 10px;
          width: 1000px;
          background-color: white;
          border-radius: 5px;
          padding: 20px;
        "
      >
        <a-col style="width: 100%">
          <a-row style="font-size: 16px; font-weight: bold">
            {{ infoList.length }} 条内容
          </a-row>
          <a-row>
            <a-divider />
          </a-row>
          <a-row style="justify-content: center" v-if="infoList.length === 0">
            <a-empty />
          </a-row>
          <a-row v-if="collectSet.type === 'problem'">
            <a-col style="width: 100%">
              <a-row
                v-for="(problem, index) in infoList"
                :key="problem.problemid"
              >
                <a-col style="width: 100%">
                  <a-row v-if="index">
                    <a-divider />
                  </a-row>
                  <a-row
                    justify="space-between"
                    style="
                      width: 100%;
                      height: 50px;
                      display: flex;
                      padding: 0 8px 0 8px;
                      align-items: center;
                      border: 1px solid rgb(224, 224, 224);
                      background-color: rgb(249, 249, 249);
                    "
                  >
                    <a-col>
                      <a-row>
                        <a-col
                          style="
                            font-size: 18px;
                            color: rgb(105, 105, 105);
                            margin-left: 10px;
                          "
                        >
                          {{ problem.problemid }}
                        </a-col>
                        <a-col
                          style="
                            font-size: 18px;
                            color: #1296db;
                            margin-left: 10px;
                          "
                        >
                          {{ problem.title }}
                        </a-col>
                        <a-col
                          style="
                            font-size: 18px;
                            color: rgb(105, 105, 105);
                            margin-left: 10px;
                          "
                        >
                          <a-tag
                            style="font-size: 14px"
                            v-for="algorithm in String(problem.algorithm)
                              .trim()
                              .split(' ')"
                            :key="algorithm"
                            color="green"
                            >{{ algorithm }}</a-tag
                          >
                        </a-col>
                      </a-row>
                    </a-col>
                    <a-col>
                      <a-row>
                        <a-col
                          style="
                            display: flex;
                            align-items: center;
                            font-size: 18px;
                            color: rgb(105, 105, 105);
                            margin-right: 100px;
                          "
                        >
                          <img
                            style="width: 30px"
                            src="../../../assets/static/pictures/hard.png"
                            alt=""
                          />
                          <span>{{ problem.difficulty }}</span>
                        </a-col>
                        <a-col
                          style="
                            display: flex;
                            align-items: center;
                            font-size: 18px;
                            color: rgb(105, 105, 105);
                            margin-right: 10px;
                          "
                        >
                          <img
                            style="width: 25px"
                            src="../../../assets/static/pictures/submit.png"
                            alt=""
                          />
                          <span style="margin-left: 5px">{{
                            problem.submitcount
                          }}</span>
                        </a-col>

                        <a-col
                          style="
                            display: flex;
                            align-items: center;
                            font-size: 18px;
                            color: rgb(105, 105, 105);
                            margin-right: 10px;
                          "
                        >
                          <img
                            style="width: 25px"
                            src="../../../assets/static/pictures/pass.png"
                            alt=""
                          />
                          <span style="margin-left: 5px">{{
                            problem.aceptedcount
                          }}</span>
                        </a-col>
                      </a-row>
                    </a-col>
                  </a-row>
                </a-col>
              </a-row>
            </a-col>
          </a-row>
          <a-row
            v-else
            style="margin-bottom: 20px"
            v-for="item in infoList"
            :key="item.id"
          >
            <a-col style="width: 100%">
              <a-row style="color: rgb(128, 128, 128)" justify="space-between">
                <a-col
                  style="font-size: 18px"
                  v-if="['discuss', 'solution', 'notice'].includes(item.type)"
                >
                  发表了文章
                </a-col>
                <a-col style="font-size: 18px" v-else>发表了评论</a-col>
                <a-col style="font-size: 18px">
                  {{ item.createtime }}
                </a-col>
              </a-row>

              <a-row style="margin-top: 20px; margin-bottom: 20px">
                <a-col @click="jumpToDiscuss(item.id)" v-if="item.title">
                  <h3 class="hoverable2" style="font-family: SimSun">
                    {{ item.title }}
                  </h3>
                </a-col>
                <a-col
                  @click="jumpToDiscuss(item.discussid)"
                  v-if="item.discusstitle"
                >
                  <h3 class="hoverable2" style="font-family: SimSun">
                    {{ item.discusstitle }}
                  </h3>
                </a-col>
              </a-row>
              <a-row style="width: "
                ><DiscussContentComponent :discuss="item" :type="'trend'"
              /></a-row>
              <a-divider />
            </a-col>
          </a-row>
          
        </a-col>
      </a-row>
    </a-col>
  </a-row>
</template>

<script setup>
import { getNowTime } from "@/js/functions/TimeAbout";
import { onBeforeUnmount, onMounted, onBeforeMount } from "vue";
import router from "@/router/router";
import { ref } from "vue";
import axios from "axios";
import { onBeforeRouteLeave } from "vue-router";
import DiscussContentComponent from "@/components/component/discuss/DiscussContentComponent.vue";
import { SERVER_URL } from "@/js/functions/config";
import { useStore } from "vuex";
import { useRoute } from "vue-router";
import _ from "lodash";
import CommentComponent from "@/components/component/discuss/CommentComponent.vue";
import { sleep } from "@/js/functions/TimeAbout";
// import DiscussContentComponent from "@/components/component/discuss/DiscussContentComponent.vue";
let route = useRoute();
let infoList = ref([]);
const collectSet = ref({});
const getProblems = async (collect) => {
  let list = [];
  for (let i = 0; i < collect.length; i++) {
    list.push(collect[i].collectid);
  }

  await axios
    .post(`${SERVER_URL}/problem/query/bylist`, list)
    .then((res) => {
      infoList.value = res.data;
    })
    .catch((err) => {
      console.log(err);
    });
};
const getDiscuss = async (collect) => {
  let list = [];
  for (let i = 0; i < collect.length; i++) {
    list.push(collect[i].collectid);
  }

  await axios
    .post(`${SERVER_URL}/discuss/query/bylist`, list)
    .then((res) => {
      infoList.value = res.data;
    })
    .catch((err) => {
      console.log(err);
    });
};
const getAllCollect = async () => {
  await axios
    .get(`${SERVER_URL}/collect/query`, {
      params: {
        userid: route.query.userid,
        belong: route.query.id,
      },
    })
    .then(async (res) => {
      console.log(res.data, "collect2");
      if (res.data.length === 0) return;
      if (res.data[0].type === "problem") {
        await getProblems(res.data);
      } else {
        await getDiscuss(res.data);
      }
    });
};
const getCollectSet = async () => {
  await axios
    .get(`${SERVER_URL}/collect/query/set`, {
      params: {
        id: route.query.id,
      },
    })
    .then((res) => {
      collectSet.value = res.data[0];
      console.log(collectSet.value, "collectSet");
    })
    .catch((err) => {});
};
const initData = async () => {
  await getCollectSet();
  await getAllCollect();
};
onMounted(async () => {
  await initData();
});
</script>

<style scoped></style>
