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
        <a-tab-pane key="1" tab="我的关注">
          <a-row
            style="justify-content: center"
            v-if="concernInfo.length === 0"
          >
            <a-empty />
          </a-row>
          <a-row
            v-for="(user, index) in concernInfo"
            :key="user.userinfo.userid"
          >
            <a-col style="width: 100%">
              <a-row v-if="index">
                <a-divider />
              </a-row>
              <a-row>
                <a-col>
                  <img
                    @click="jumpToUserInfo(user.userinfo.userid)"
                    style="width: 70px; border-radius: 50%"
                    :src="user.userinfo.userpicture"
                    alt=""
                  />
                </a-col>
                <a-col style="margin-left: 10px">
                  <a-row>
                    <a-col
                      @click="jumpToUserInfo(user.userinfo.userid)"
                      style="
                        font-size: 16px;
                        font-weight: bold;
                        cursor: pointer;
                      "
                    >
                      {{ user.userinfo.username }}
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col style="font-size: 14px; color: gray">
                      {{ user.userinfo.usermotto }}
                    </a-col>
                  </a-row>
                  <a-row style="font-size: 16px">
                    <a-col> 关注 {{ user.userextra.concern }} </a-col>
                    <a-col style="margin-left: 10px">
                      粉丝 {{ user.userextra.fans }}
                    </a-col>
                    <a-col style="margin-left: 10px">
                      提交 {{ user.userextra.submitcount }}
                    </a-col>
                    <a-col style="margin-left: 10px">
                      通过 {{ user.userextra.aceptedcount }}
                    </a-col>
                  </a-row>
                </a-col>
              </a-row>
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane key="2" tab="我的粉丝">
          <a-row style="justify-content: center" v-if="fansInfo.length === 0">
            <a-empty />
          </a-row>
          <a-row v-for="(user, index) in fansInfo" :key="user.userinfo.userid">
            <a-col style="width: 100%">
              <a-row v-if="index">
                <a-divider />
              </a-row>
              <a-row>
                <a-col>
                  <img
                    @click="jumpToUserInfo(user.userinfo.userid)"
                    style="width: 70px; border-radius: 50%; cursor: pointer"
                    :src="user.userinfo.userpicture"
                    alt=""
                  />
                </a-col>
                <a-col style="margin-left: 10px">
                  <a-row>
                    <a-col
                      @click="jumpToUserInfo(user.userinfo.userid)"
                      style="
                        font-size: 16px;
                        font-weight: bold;
                        cursor: pointer;
                      "
                    >
                      {{ user.userinfo.username }}
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col style="font-size: 14px; color: gray">
                      {{ user.userinfo.usermotto }}
                    </a-col>
                  </a-row>
                  <a-row style="font-size: 16px">
                    <a-col> 关注 {{ user.userextra.concern }} </a-col>
                    <a-col style="margin-left: 10px">
                      粉丝 {{ user.userextra.fans }}
                    </a-col>
                    <a-col style="margin-left: 10px">
                      提交 {{ user.userextra.submitcount }}
                    </a-col>
                    <a-col style="margin-left: 10px">
                      通过 {{ user.userextra.aceptedcount }}
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

let fansId = new Set();
let concernId = new Set();
let fansInfo = ref([]);
let concernInfo = ref([]);
const jumpToUserInfo = (userid) => {
  router.push({
    name: "userhome",
    query: { userid },
  });
};
const getFansId = async () => {
  await axios
    .get(`${SERVER_URL}/userfans/query/myfans`, {
      params: {
        userid: props.userid,
      },
    })
    .then((res) => {
      for (let i = 0; i < res.data.length; i++) {
        fansId.add(res.data[i].fansuserid);
      }
    })
    .catch((err) => {
      console.log(err);
    });
};
const getConcernId = async () => {
  await axios
    .get(`${SERVER_URL}/userfans/query/myconcern`, {
      params: {
        userid: props.userid,
      },
    })
    .then((res) => {
      for (let i = 0; i < res.data.length; i++) {
        concernId.add(res.data[i].userid);
      }
    })
    .catch((err) => {
      console.log(err);
    });
};
const initFansInfoAndConcernInfo = async () => {
  let userInfo = [];
  await axios
    .get(`${SERVER_URL}/user/query/all`)
    .then((res) => {
      userInfo = res.data;
    })
    .catch((err) => {
      console.log(err);
    });
  console.log(fansId, concernId);
  for (let i = 0; i < userInfo.length; i++) {
    let userid = userInfo[i].userinfo.userid;
    if (fansId.has(userid)) {
      fansInfo.value.push(userInfo[i]);
    }
    if (concernId.has(userid)) {
      concernInfo.value.push(userInfo[i]);
    }
  }
};
const initData = async () => {
  await getFansId();
  await getConcernId();
  await initFansInfoAndConcernInfo();
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
