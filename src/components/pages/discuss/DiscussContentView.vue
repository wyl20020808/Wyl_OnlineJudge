<template>
  <a-row
    style="
      margin-left: 150px;
      margin-top: 30px;
      margin-bottom: 20px;
      align-items: center;
      justify-content: center;
    "
  >
    <a-col style="width: 1300px">
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
          ><h3 style="font-family: SimSun;">{{ discuss.title }}</h3></a-col
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
      <!-- 下面是展示信息的组件 -->
      <a-row style="margin-bottom: 20px">
        <DiscussContentComponent :type="'discuss'" :discuss="discuss" />
      </a-row>
      <a-row style="margin-bottom: 20px">
        <a-col style="width: 88%">
          <a-collapse
            style="background-color: #f5f5f5"
            v-model:activeKey="activeKey"
          >
            <a-collapse-panel
              key="1"
              :header="'评论区' + ' (' + '共' + comment.length + '条评论' + ')'"
            >
              <a-row v-for="item in comment" :key="item.id">
                <DiscussContentComponent
                  @deleteComment="deleteComment"
                  :discuss="item"
                />
              </a-row>
            </a-collapse-panel>
          </a-collapse>
        </a-col>
      </a-row>

      <div
        style="
          border-top: 2px solid #c5c5c5; /* 设置分割线的样式，可以根据需要调整颜色和粗细 */
          margin-bottom: 10px; /* 设置分割线的上下间距，可以根据需要调整 */
          width: 88%;
        "
      ></div>
      <a-row>
        <a-col style="width: 90%">
          <CommentComponent @addComment="onAddComment" :discuss="discuss" />
        </a-col>
      </a-row>
    </a-col>
  </a-row>
</template>
<!-- 2023年10月12日14:57:40 -->
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
let route = useRoute();
let id = ref(1);
let type = ref("discuss");
let discuss = ref({});
let comment = ref([]);

const onAddComment = (value) => {
  comment.value.push(value);
  console.log(value); // 输出：{ key1: 'value1', key2: 'value2' }
};
function editDiscuss() {
  router.push({
    path: "/creatediscuss",
    query: {
      id: id.value,
      type: type.value,
    },
  });
}
function deleteComment(value) {
  for (let i = 0; i < comment.value.length; i++) {
    if (parseInt(comment.value[i].id) === parseInt(value)) {
      comment.value.splice(i, 1);
      break;
    }
  }
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
      },
    })
    .then((res) => {
      discuss.value = res.data[0];
    })
    .catch((err) => {
      console.log(err);
    });
}

async function getComment() {
  //获取该文章的所有评论
  await axios
    .get(`${SERVER_URL}/discuss/comment/query`, {
      params: {
        target: id.value,
      },
    })
    .then((res) => {
      comment.value = res.data;
      console.log(comment.value, "评论的值");
    })
    .catch((err) => {
      console.log(err);
    });
}
function addBrowse() {
  axios
    .post(`${SERVER_URL}/discuss/operator`, {
      id: route.query.id,
      special: "browse",
      detal: 1,
    })
    .then((res) => {})
    .catch((err) => {
      console.log(err);
    });
}
onBeforeMount(async () => {
  id.value = route.query.id;
  type.value = route.query.type;
  await getDiscuss();
  await getComment();
  addBrowse();
});
</script>

<style scoped>
.vertical-line {
}
</style>
