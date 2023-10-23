<template>
  <a-row style="width: 100%">
    <a-col style="width: 95%">
      <a-tabs
        tabBarGutter="0px"
        class="my-tabs"
        size="small"
        v-model:activeKey="activeKey"
        @change="chooseTab"
      >
        <a-tab-pane key="1" tab="我参加的比赛">
          <a-row>
            <!-- <a-col style="width: 100px;height: 100px;background-color:rgb(232, 7, 7) ;"></a-col> -->
            <a-col style="width: 100%">
              <a-row
                style="justify-content: center"
                v-if="myJoinContest.length === 0"
              >
                <a-empty />
              </a-row>
              <a-row
                v-for="(contest, index) in myJoinContest"
                :key="contest.contestid"
              >
                <a-col style="width: 100%">
                  <a-row v-if="index">
                    <a-divider />
                  </a-row>
                  <a-row
                    justify="space-between"
                    style="
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
                          :style="{
                            color: getColorByStatus(
                              getContestStatus(
                                contest.startdate,
                                contest.enddate,
                                getNowTime()
                              )
                            ),
                          }"
                          style="font-size: 18px; font-weight: bold"
                        >
                          {{
                            getContestStatus(
                              contest.startdate,
                              contest.enddate,
                              getNowTime()
                            )
                          }}
                        </a-col>
                        <a-col
                          style="
                            font-size: 18px;
                            color: rgb(10, 139, 225);
                            margin-left: 10px;
                            cursor: pointer;
                          "
                          @click="jumpToContest(contest.contestid)"
                        >
                          {{ contest.contestname }}
                        </a-col>
                      </a-row>
                    </a-col>
                    <a-col>
                      <a-row>
                        <a-col
                          style="
                            font-family: Verdana;
                            font-size: 18px;
                            color: rgb(114, 18, 199);
                          "
                        >
                          {{ contest.contestformat }}
                        </a-col>
                        <a-col
                          style="
                            font-size: 18px;
                            color: rgb(144, 144, 144);
                            margin-left: 10px;
                          "
                        >
                          {{ contest.startdate }}
                          ~ {{ contest.enddate }}
                        </a-col>
                        <a-col
                          style="
                            font-size: 18px;
                            color: rgb(143, 143, 143);
                            margin-left: 10px;
                          "
                        >
                          <img
                            style="width: 33px"
                            src="../../../assets/static/pictures/peoples2.png"
                            alt=""
                          />
                          <span>{{ contest.joinpeople }}</span>
                        </a-col>
                        <a-col
                          style="
                            font-size: 18px;
                            color: #1296db;
                            margin-left: 10px;
                            cursor: pointer;
                          "
                          @click="jumpToUserInfo(contest.userid)"
                        >
                          {{ contest.username }}
                        </a-col>
                      </a-row>
                    </a-col>
                  </a-row>
                </a-col>
              </a-row>
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane key="2" tab="我创建的比赛">
          <a-row>
            <!-- <a-col style="width: 100px;height: 100px;background-color:rgb(232, 7, 7) ;"></a-col> -->
            <a-col style="width: 100%">
              <a-row
                style="justify-content: center"
                v-if="myCreateContest.length === 0"
              >
                <a-empty />
              </a-row>
              <a-row
                v-for="(contest, index) in myCreateContest"
                :key="contest.contestid"
              >
                <a-col style="width: 100%">
                  <a-row v-if="index">
                    <a-divider />
                  </a-row>
                  <a-row
                    justify="space-between"
                    style="
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
                          :style="{
                            color: getColorByStatus(
                              getContestStatus(
                                contest.startdate,
                                contest.enddate,
                                getNowTime()
                              )
                            ),
                          }"
                          style="font-size: 18px; font-weight: bold"
                        >
                          {{
                            getContestStatus(
                              contest.startdate,
                              contest.enddate,
                              getNowTime()
                            )
                          }}
                        </a-col>
                        <a-col
                          style="
                            font-size: 18px;
                            color: rgb(10, 139, 225);
                            margin-left: 10px;
                            cursor: pointer;
                          "
                          @click="jumpToContest(contest.contestid)"
                        >
                          {{ contest.contestname }}
                        </a-col>
                      </a-row>
                    </a-col>
                    <a-col>
                      <a-row>
                        <a-col
                          style="
                            font-family: Verdana;
                            font-size: 18px;
                            color: rgb(114, 18, 199);
                          "
                        >
                          {{ contest.contestformat }}
                        </a-col>
                        <a-col
                          style="
                            font-size: 18px;
                            color: rgb(144, 144, 144);
                            margin-left: 10px;
                          "
                        >
                          {{ contest.startdate }}
                          ~ {{ contest.enddate }}
                        </a-col>
                        <a-col
                          style="
                            font-size: 18px;
                            color: rgb(143, 143, 143);
                            margin-left: 10px;
                          "
                        >
                          <img
                            style="width: 33px"
                            src="../../../assets/static/pictures/peoples2.png"
                            alt=""
                          />
                          <span>{{ contest.joinpeople }}</span>
                        </a-col>
                        <a-col
                          style="
                            font-size: 18px;
                            color: #1296db;
                            margin-left: 10px;
                            cursor: pointer;
                          "
                          @click="jumpToUserInfo(contest.userid)"
                        >
                          {{ contest.username }}
                        </a-col>
                      </a-row>
                    </a-col>
                  </a-row>
                </a-col>
              </a-row>
            </a-col>
          </a-row>
        </a-tab-pane>
      </a-tabs>
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
const props = defineProps({
  userid: Number,
});

let myJoinContest = ref([]);
let myCreateContest = ref([]);
let allContest = ref([]);
const jumpToUserInfo = (userid) => {
  router.push({
    name: "userhome",
    query: { userid },
  });
};
const jumpToContest = (contestid) => {
  router.push({
    name: "contestcontest",
    query: {
      contestid: contestid,
    },
  });
};
function getColorByStatus(status) {
  switch (status) {
    case "未开始":
      return "green"; // 未开始的比赛显示为蓝色
    case "正在进行":
      return "orange"; // 进行中的比赛显示为绿色
    case "已结束":
      return "rgb(232, 7, 7)"; // 已结束的比赛显示为红色
    default:
      return "黑色"; // 其他状态显示为黑色
  }
}
function getContestStatus(startTime, endTime, currentTime) {
  // 将时间字符串转换为 Date 对象
  let start = new Date(startTime);
  let end = new Date(endTime);
  let current = new Date(currentTime);

  // 比较时间并返回比赛状态
  if (current < start) {
    return "未开始";
  } else if (current > end) {
    return "已结束";
  } else {
    return "正在进行";
  }
}
const getMyJoinContest = async () => {
  await axios
    .get(`${SERVER_URL}/contest/query/join/personal`, {
      params: {
        userid: props.userid,
      },
    })
    .then((res) => {
      let set = new Set();
      for (let i = 0; i < res.data.length; i++) {
        set.add(res.data[i].contestid);
      }

      for (let i = 0; i < allContest.value.length; i++) {
        if (set.has(allContest.value[i].contestid)) {
          myJoinContest.value.push(allContest.value[i]);
        }
      }
    })
    .catch((err) => {
      console.log(err);
    });
};
const getAllContest = async () => {
  await axios
    .get(`${SERVER_URL}/contest/query`)
    .then((res) => {
      allContest.value = res.data.reverse();
    })
    .catch((err) => {
      console.log(err);
    });
};
const getMyCreateContest = async () => {
  await axios
    .get(`${SERVER_URL}/contest/query`, {
      params: {
        userid: props.userid,
      },
    })
    .then((res) => {
      myCreateContest.value = res.data.reverse();
    })
    .catch((err) => {
      console.log(err);
    });
};
const initData = async () => {
  await getAllContest();
  await getMyCreateContest();
  await getMyJoinContest();
  //   await initFansInfoAndConcernInfo();
};

watch(
  () => props.userid,
  async (newValue, oldValue) => {
    // 在父组件属性发生变化时执行初始化逻辑
    // 执行初始化逻辑，例如获取讨论状态等
    await initData();
  }
);

onBeforeMount(async () => {
  await initData();
});
</script>

<style scoped></style>
