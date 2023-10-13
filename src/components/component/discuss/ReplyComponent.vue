<template>
  <a-row style="width: 100%">
    <a-col :span="1"
      ><img
        :src="discuss.userpicture"
        style="width: 50px; border-radius: 50%"
        alt="Avatar"
    /></a-col>
    <a-col :span="22" style="margin-left: 20px">
      <a-row class="card1">
        <!-- 这里为了布局所以用了div 2023年10月12日14:57:22 -->
        <div class="text">
          <a-row style="display: flex; align-items: center">
            <a-col style="color: #4183c4; font-size: 16px;">
              {{ discuss.username }}
            </a-col>
            <a-col style="margin-left: 5px;"  v-if="discuss.targettype === 'reply'">
                 回复 
                <span style="color: #4183c4; font-size: 16px" >@{{ discuss.targetname }} </span>
            </a-col>
            <a-col style="margin-left: 10px; color: gray; font-size: 16px">
              发表于：{{ discuss.createtime }}
            </a-col>
            <a-col
              v-if="discuss.createtime !== discuss.edittime"
              style="margin-left: 10px; color: gray; font-size: 16px"
            >
              更新于：{{ discuss.edittime }}
            </a-col>
          </a-row>
        </div>
      </a-row>

      <a-row class="card2">
        <a-row>
          <a-col>
            <div
              v-html="parsedDescription(formattedText(discuss.content))"
            ></div>
          </a-col>
        </a-row>
      </a-row>
      <a-row class="card3">
        <a-col
          :style="{
            padding: '10px',
            backgroundColor: myDiscussState.liked ? 'rgb(171, 255, 227)' : '',
            borderRight: '1px solid #c6c5c5',
            cursor: 'pointer',
          }"
          style="display: flex; align-items: center"
          @click="changeState(0)"
        >
          <img src="../../../assets/static/pictures/like.png" width="30" />

          <span
            v-if="discuss.likes"
            style="margin-left: 10px; font-size: 20px; color: #4183c4"
            >{{ discuss.likes }}</span
          >
        </a-col>
        <a-col
          :style="{
            padding: '10px',
            backgroundColor: myDiscussState.dislike ? 'rgb(171, 255, 227)' : '',
            borderRight: '1px solid #c6c5c5',
            cursor: 'pointer',
          }"
          style="display: flex; align-items: center"
          @click="changeState(1)"
        >
          <img src="../../../assets/static/pictures/dislike.png" width="30" />
          <span
            v-if="discuss.dislike"
            style="margin-left: 10px; font-size: 20px; color: #4183c4"
            >{{ discuss.dislike }}</span
          >
        </a-col>
        <a-col
          :style="{
            padding: '10px',
            backgroundColor: myDiscussState.funny ? 'rgb(171, 255, 227)' : '',
            borderRight: '1px solid #c6c5c5',
            cursor: 'pointer',
          }"
          style="display: flex; align-items: center"
          @click="changeState(2)"
        >
          <img src="../../../assets/static/pictures/funny.png" width="30" />
          <span
            v-if="discuss.funny"
            style="margin-left: 10px; font-size: 20px; color: #4183c4"
            >{{ discuss.funny }}</span
          >
        </a-col>
        <a-col
          :style="{
            padding: '10px',
            backgroundColor: myDiscussState.collect ? 'rgb(171, 255, 227)' : '',
            borderRight: '1px solid #c6c5c5',
            cursor: 'pointer',
          }"
          style="display: flex; align-items: center"
          @click="changeState(3)"
        >
          <img src="../../../assets/static/pictures/collect.png" width="30" />
          <span
            v-if="discuss.collect"
            style="margin-left: 10px; font-size: 20px; color: #4183c4"
            >{{ discuss.collect }}</span
          >
        </a-col>
        <a-col
          v-if="discuss.type === 'discuss'"
          :style="{
            padding: '10px',
            borderRight: '1px solid #c6c5c5',
            cursor: 'pointer',
          }"
          style=""
          @click="1"
        >
          <img src="../../../assets/static/pictures/forward2.png" width="30" />
        </a-col>
        <a-col
          v-else
          :style="{
            padding: '10px',
            borderRight: '1px solid #c6c5c5',
            cursor: 'pointer',
          }"
          style=""
          @click="commenting = true"
        >
          <img src="../../../assets/static/pictures/reply.png" width="30" />
        </a-col>
      </a-row>
      <a-row style="margin-top: 20px" v-if="commenting">
        <div style="width: 92%">
          <CommentComponent
            @addReply="addReply"
            @cancleComment="commenting = false"
            :reply="true"
            :discuss="props.discuss"
          />
        </div>
      </a-row>
    </a-col>
  </a-row>
  <a-row>
    <div
      style="
        border-left: 2px solid #c2c2c2; /* 设置竖线的样式，可以根据需要调整颜色和粗细 */
        height: 30px; /* 设置竖线的高度，可以根据需要调整 */
        margin-left: 100px;
      "
    ></div>
  </a-row>
</template>
<!-- 2023年10月13日09:45:49 这个组件是用来加载各个评论的 -->
<script setup>
import { ref, watch } from "vue";
import MarkdownIt from "markdown-it";
import "markdown-it-texmath/css/texmath.css";
import mk from "markdown-it-katex";
import "katex/dist/katex.min.css";
import { onMounted } from "vue";
import { defineProps } from "vue";
import { SearchOutlined, PlusOutlined } from "@ant-design/icons-vue";
import router from "@/router/router";
import axios from "axios";
import { SERVER, SERVER_URL } from "@/js/functions/config";
import CommentComponent from "./CommentComponent.vue";
import { defineEmits } from "vue";

const emit = defineEmits();
const addReply = (obj) => {
  emit("addReply", obj);
};

const md = new MarkdownIt({ html: true }).use(mk);
const props = defineProps({
  discuss: Object,
});
let commenting = ref(false);
let myDiscussState = ref({
  liked: false,
  dislike: false,
  collect: false,
  funny: false,
  userid: JSON.parse(localStorage.getItem("user")).userid,
  discussid: props.discuss.id,
});
function jump(total) {
  router.push({ path: "/" + total });
}
async function updateDiscuss(special, detal) {
  await axios
    .post(`${SERVER_URL}/discuss/operator`, {
      id: props.discuss.id,
      special: special,
      detal: detal,
    })
    .then(() => {
      console.log("更新了", special);
    })
    .catch((err) => {
      console.log(err);
    });
}
async function changeState(idx) {
  //更新每篇文章的喜欢等情况
  if (idx === 0) {
    if (myDiscussState.value.dislike) {
      await updateDiscuss("dislike", -1);
      props.discuss.dislike -= 1;
    }
    myDiscussState.value.dislike = false;
    if (myDiscussState.value.liked) {
      await updateDiscuss("likes", -1);
      props.discuss.likes -= 1;
    } else {
      await updateDiscuss("likes", 1);
      props.discuss.likes += 1;
    }
    myDiscussState.value.liked = !myDiscussState.value.liked;
  } else if (idx === 1) {
    if (myDiscussState.value.liked) {
      await updateDiscuss("likes", -1);
      props.discuss.likes -= 1;
    }
    if (myDiscussState.value.dislike) {
      await updateDiscuss("dislike", -1);
      props.discuss.dislike -= 1;
    } else {
      await updateDiscuss("dislike", 1);
      props.discuss.dislike += 1;
    }
    myDiscussState.value.dislike = !myDiscussState.value.dislike;
    myDiscussState.value.liked = false;
  } else if (idx == 2) {
    if (myDiscussState.value.funny) {
      await updateDiscuss("funny", -1);
      props.discuss.funny -= 1;
    } else {
      await updateDiscuss("funny", 1);
      props.discuss.funny += 1;
    }
    myDiscussState.value.funny = !myDiscussState.value.funny;
  } else {
    if (myDiscussState.value.collect) {
      await updateDiscuss("collect", -1);
      props.discuss.collect -= 1;
    } else {
      await updateDiscuss("collect", 1);
      props.discuss.collect += 1;
    }
    myDiscussState.value.collect = !myDiscussState.value.collect;
  }
}
function parsedDescription(content) {
  return md.render(String(content));
}
function formattedText(text) {
  return text;
  if (typeof text !== "string") {
    text = String(text);
  }
  console.log(text.replace(/\n/g, "<br>"));
  return text.replace(/\n/g, "<br>"); //为了体现间距，更加美观
}
async function getReply() {
  await axios
    .get(`${SERVER_URL}/discuss/reply/query`, {
      params: {
        id: props.discuss.id,
      },
    })
    .then((res) => {
      if (res.data) {
        for(let i = 0 ; i < res.data.length; i++) {
            addReply(res.data[i]);//把查询到的记录返回给上一级
        }
        
      }
    })
    .catch((err) => {
      console.log(err);
    });
}
async function getDiscussState() {
  await axios
    .get(`${SERVER_URL}/discuss/state/query`, {
      params: {
        userid: JSON.parse(localStorage.getItem("user")).userid,
        discussid: props.discuss.id,
      },
    })
    .then((res) => {
      if (res.data) {
        myDiscussState.value = res.data;
        console.log(myDiscussState.value, "从后端获取到的discustate数据");
      }
    })
    .catch((err) => {
      console.log(err);
    });
}

watch(
  myDiscussState,
  async (newValue, oldValue) => {
    // 在 myDiscussState.value 的属性发生变化时执行逻辑
    console.log(`myDiscussState 变化了：${oldValue} -> ${newValue}`);
    myDiscussState.value.discussid = props.discuss.id;
    await axios
      .post(`${SERVER_URL}/discuss/state/update`, myDiscussState.value)
      .then((res) => {})
      .catch((err) => {
        console.log(err);
      });
  },
  { deep: true }
);
//处理父组件传值的情况，要等有值再获取
watch(
  () => props.discuss,
  async (newValue, oldValue) => {
    // 在父组件属性发生变化时执行初始化逻辑
    // 执行初始化逻辑，例如获取讨论状态等
    await getReply();
    await getDiscussState();
  }
);

onMounted(async () => {
  if (props.discuss.id) {
    await getReply();
    await getDiscussState();
  }
});
</script>

<style scoped>
.card1 {
  display: flex;
  align-items: center;
  position: relative;
  width: 90%;
  height: 40px;
  background-color: rgb(238, 238, 238);
  border-radius: 5px 5px 0 0;
  border: 1px solid #c6c5c5;
  box-sizing: border-box;
}
.card2 {
  display: flex;
  align-items: center;
  position: relative;
  width: 90%;
  padding-top: 10px;
  padding-right: 10px;
  padding-bottom: 0px;
  padding-left: 10px;
  border-radius: 0 0 0 0;
  background-color: rgb(255, 255, 255);
  border-left: 1px solid #c6c5c5;
  border-right: 1px solid #c6c5c5;
  box-sizing: border-box;
}

.card3 {
  position: relative;
  display: flex;
  align-items: center;
  position: relative;
  width: 90%;
  border-radius: 0px 0 5px 5px;
  background-color: rgb(255, 255, 255);
  border: 1px solid #c6c5c5;
  box-sizing: border-box;
}

.card1::before {
  content: "";
  position: absolute;
  top: 13px;
  left: -6px;
  width: 10px;
  height: 10px;
  border-left: 1px solid #c6c5c5;
  border-bottom: 1px solid #c6c5c5;
  transform: rotate(45deg);
  background-color: rgb(238, 238, 238);
}

.text {
  padding: 0 10px;
}
</style>
