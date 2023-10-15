<template>
  <a-row>
    <a-col style="width: 100%">
      <a-row justify="space-between">
        <a-col>
          <a-tabs
            tabBarGutter="0px"
            class="my-tabs"
            size="small"
            v-model:activeKey="activeKey2"
            @change="chooseTab"
          >
            <a-tab-pane key="1" tab="题目收藏夹"> </a-tab-pane>
            <a-tab-pane key="2" tab="文章收藏夹"> </a-tab-pane>
          </a-tabs>
        </a-col>
        <a-col style="margin-top: 5px; margin-right: 20px">
          <a-button
            @click="open = true"
            style="color: rgb(39, 158, 248); display: flex; align-items: center"
            ghost
          >
            <PlusOutlined />
            <span>新建收藏夹</span>
          </a-button>
        </a-col>
      </a-row>
      <a-modal
        width="700px"
        v-model:open="open"
        title="创建收藏夹"
        :confirm-loading="confirmLoading"
        @ok="createColletSet"
      >
        <a-row>
          <a-textarea
            style="margin-top: 10px; margin-bottom: 20px"
            v-model:value="collectSet.name"
            placeholder="收藏标题"
            auto-size
          />
        </a-row>
        <a-row>
          <a-textarea
            style="margin-bottom: 20px"
            v-model:value="collectSet.note"
            placeholder="收藏描述（可选）"
            :auto-size="{ minRows: 7, maxRows: 20 }"
          />
        </a-row>
        <a-row>
          <a-radio-group
            style="margin-bottom: 20px"
            v-model:value="collectSet.secret"
          >
            <a-radio :style="radioStyle" value="private"
              >私密（只有自己可以查看此文件夹）</a-radio
            >
            <a-radio :style="radioStyle" value="public"
              >公开（所有人都可以查看）</a-radio
            >
          </a-radio-group>
        </a-row>
        <a-row style="display: flex;align-items: center;">
          <a-col style="margin-right: 20px;font-size: 16px;">收藏夹类型</a-col>
          <a-col>
            <a-radio-group v-model:value="collectSet.type" button-style="solid">
              <a-radio-button value="problem">题目</a-radio-button>
              <a-radio-button value="discuss">文章</a-radio-button>
            </a-radio-group>
          </a-col>
        </a-row>
      </a-modal>
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
import { SearchOutlined, PlusOutlined } from "@ant-design/icons-vue";
const props = defineProps({
  userid: Number,
});
const activeKey2 = ref("1");
const collectSet = ref({
  secret: "public",
  name: "",
  note: "",
  type: "problem",
});
const createColletSet = async () => {
  let data = {
    ...collectSet.value,
    createtime: getNowTime(),
    modifytime: getNowTime(),
    userid: JSON.parse(localStorage.getItem("user")).userid,
    username: JSON.parse(localStorage.getItem("user")).username,
  };
  confirmLoading.value = true;
  setTimeout(() => {
    open.value = false;
    confirmLoading.value = false;
  }, 2000);
};

const open = ref(false);
const confirmLoading = ref(false);
</script>

<style scoped>
.ant-btn-primary {
  color: white;
}
</style>
