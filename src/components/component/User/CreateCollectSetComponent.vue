<template>
  <a-row>
    <a-col style="width: 100%">
      <a-modal
        width="700px"
        v-model:open="open"
        :confirm-loading="confirmLoading"
        @ok="createColletSet"
      >
      <template #title>
      <div style="text-align: center; color: rgb(3, 3, 3); font-size: 20px">
        创建收藏夹
      </div>
    </template>
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
        <a-row style="display: flex; align-items: center">
          <a-col style="margin-right: 20px; font-size: 16px">收藏夹类型</a-col>
          <a-col v-if="!props.type">
            <a-radio-group :disabled="collectSet.count > 0" v-model:value="collectSet.type" button-style="solid">
              <a-radio-button value="problem">题目</a-radio-button>
              <a-radio-button value="discuss">文章</a-radio-button>
            </a-radio-group>
          </a-col>
        </a-row>
        <template #footer>
          <a-button type="default" @click="handleCancel">取消</a-button>
          <a-button
            type="primary"
            :loading="confirmLoading"
            @click="createColletSet"
            :disabled="isConfirmDisabled"
            >确认</a-button
          >
        </template>
      </a-modal>
    </a-col>
  </a-row>
</template>

<script setup>
import { getNowTime } from "@/js/functions/TimeAbout";
import { onBeforeUnmount, onMounted, onBeforeMount } from "vue";
import router from "@/router/router";
import { ref, watch, computed } from "vue";
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
import { defineEmits } from "vue";
import { SearchOutlined, PlusOutlined } from "@ant-design/icons-vue";

const emit = defineEmits();
const addCollectSet = (obj) => {
  emit("addCollectSet", obj);
};
const addCollectSetWithType = (obj,type) => {
  emit("addCollectSetWithType", obj,type);
};
const modifyCollectSet = (obj) => {
  emit("modifyCollectSet", obj);
};
const store = useStore();
const props = defineProps({
  open: Boolean,
  type: String,
  collectSet:Object,
});
const isConfirmDisabled = computed(() => !collectSet.value.name.trim());
const collectSet = ref({
  secret: "public",
  name: "",
  note: "",
  type: props.type ? props.type : "problem",
});
//
const open = ref(false);
const confirmLoading = ref(false);
const handleCancel = () => {
  open.value = false;
};
const updateCollectSet = async () =>{
  collectSet.value.modifytime = getNowTime();
  await axios
    .post(`${SERVER_URL}/collect/update/set`, collectSet.value)
    .then((res) => {
      open.value = false;
      store.dispatch("notice", {
        title: "修改成功！",
        message: "",
        type: "success",
      });
      modifyCollectSet(res.data);
      collectSet.value = {
        secret: "public",
        name: "",
        note: "",
        type: "problem",
      };
    })
    .catch((err) => {
      console.log(err);
      open.value = false;
      confirmLoading.value = false;
    });
}
const createColletSet = async () => {
  if(props.collectSet !== null){
    await updateCollectSet();
    return ;
  }
  let data = {
    ...collectSet.value,
    createtime: getNowTime(),
    modifytime: getNowTime(),
    userid: JSON.parse(localStorage.getItem("user")).userid,
    username: JSON.parse(localStorage.getItem("user")).username,
  };
  confirmLoading.value = true;
  await axios
    .post(`${SERVER_URL}/collect/update/set`, data)
    .then((res) => {
      open.value = false;
      confirmLoading.value = false;
      store.dispatch("notice", {
        title: "创建成功！",
        message: "",
        type: "success",
      });
      addCollectSet(res.data);
      addCollectSetWithType(res.data,res.data.type);
      collectSet.value = {
        secret: "public",
        name: "",
        note: "",
        type: "problem",
      };
    })
    .catch((err) => {
      console.log(err);
      open.value = false;
      confirmLoading.value = false;
    });
};

const initData = async () => {};
watch(
  () => props.open,
  async (newValue, oldValue) => {
    // 在父组件属性发生变化时执行初始化逻辑
    // 执行初始化逻辑，例如获取讨论状态等
    open.value = true
  }
);

watch(
  () => props.collectSet,
  async (newValue, oldValue) => {
    // 在父组件属性发生变化时执行初始化逻辑
    // 执行初始化逻辑，例如获取讨论状态等
    if(newValue !== null){
      collectSet.value = newValue;
    }
  }
);

onBeforeMount(async () => {});
</script>

<style scoped>
.ant-btn-primary {
  color: white;
}
</style>
