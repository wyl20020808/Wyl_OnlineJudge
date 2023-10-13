<template>
  <a-row>
    <a-col :span="1">
      <img
        style="width: 50px; border-radius: 50%"
        :src="userpicture"
        alt="Avatar"
      />
    </a-col>
    <a-col :span="22" style="margin-left: 24px">
      <div class="card1">
        <div style="color: #4183c4" class="text">{{ username }}</div>
      </div>
      <div
        style="
          z-index: 1000;
          position: relative;
          bottom: 3px;
          border: 1px solid #c6c5c5;
          border-bottom: 1px solid white;
          height: 10px;
          background-color: white;
        "
      ></div>
      <div class="card2">
        <v-md-editor
          placeholder="回复"
          v-model="discuss.content"
          height="200px"
        ></v-md-editor>
      </div>
    </a-col>
  </a-row>
  <a-row style="width: 98%" justify="space-between">
    <a-col> </a-col>
    <a-col>
      <el-button
        v-if="props.reply"
        @click="cancleComment"
        style="color: white"
        type="primary"
        >取消</el-button
      >
      <el-button @click="saveComment" style="color: white" type="primary"
        >回复</el-button
      >
    </a-col>
  </a-row>
</template>
<!-- 2023年10月13日09:45:32 这个组件是纯粹用来发表评论的 -->
<script setup>
import { defineEmits } from "vue";
import { ref, watch } from "vue";
import MarkdownIt from "markdown-it";
import "markdown-it-texmath/css/texmath.css";
import mk from "markdown-it-katex";
import "katex/dist/katex.min.css";
import { onMounted } from "vue";
import { defineProps } from "vue";
import { SearchOutlined, PlusOutlined } from "@ant-design/icons-vue";
import { useStore } from "vuex";
import { useRouter, useRoute } from "vue-router";
import axios from "axios";
import { getNowTime } from "@/js/functions/TimeAbout";
import { SERVER, SERVER_URL } from "@/js/functions/config";
const props = defineProps({
  discuss: Object,
  reply: Boolean,
});
const emit = defineEmits();
const addComment = (obj) => {
  emit("addComment", obj);
};
const addReply = (obj) => {
  emit("addReply", obj);
};
const cancleComment = () => {
  emit("cancleComment", true);
};

const userpicture = JSON.parse(localStorage.getItem("user")).userpicture;
const username = JSON.parse(localStorage.getItem("user")).username;
let store = useStore();
const router = useRouter();
const route = useRoute();
let discuss = ref({
  type: "comment",
  content: "",
});
async function saveComment() {
  //评论暂时不支持修改
  let data = {
    content: discuss.value.content,
    type: discuss.value.type,
    target: props.discuss.id, //我的评论目标是谁，id和名字就不存了，因为在显示的时候可以获取到
    targetname:props.discuss.username,
    targettype:props.discuss.type,
    targetuserid:props.discuss.userid,
    createtime: getNowTime(),
    edittime: getNowTime(),
    userid: JSON.parse(localStorage.getItem("user")).userid,
    username: JSON.parse(localStorage.getItem("user")).username,
    userpicture: JSON.parse(localStorage.getItem("user")).userpicture,
  };
  if (props.reply) {
    //是评论还是回复
    data.type = 'reply';
  }
  //   if(modifyed()){//如果改过了，就修改时间
  //     data.edittime = getNowTime();
  //   }
  await axios
    .post(`${SERVER_URL}/discuss/update`, data)
    .then((res) => {
      //   discussBackup.value = {
      //     ...discuss.value,
      //   };
      if (props.reply) {
        //是评论还是回复
        addReply(res.data);
        cancleComment();
      }
      else
      addComment(res.data);
      console.log(res.data, "回复完之后的结果");

      store.dispatch("notice", {
        title: "评论成功！",
        message: "",
        type: "success",
      });
    })
    .catch((err) => {
      store.dispatch("notice", {
        title: "评论失败！",
        message: err,
        type: "error",
      });
      console.log(err);
    });
}
</script>

<style scoped>
.card1 {
  display: flex;
  align-items: center;
  position: relative;

  height: 40px;
  background-color: rgb(238, 238, 238);
  border-radius: 5px;
  border: 1px solid #c6c5c5;
  box-sizing: border-box;
}
.card2 {
  position: relative;
  bottom: 7px;
  display: flex;
  align-items: center;
  position: relative;

  padding: 10px;
  border-radius: 5px;
  background-color: rgb(255, 255, 255);
  border-left: 1px solid #c6c5c5;
  border-right: 1px solid #c6c5c5;
  border-bottom: 1px solid #c6c5c5;
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
