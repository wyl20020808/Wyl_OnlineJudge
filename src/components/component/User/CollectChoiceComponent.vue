<template>
  <a-modal width="700px" v-model:open="open" :confirm-loading="confirmLoading">
    <template #title>
      <div style="text-align: center; color: rgb(3, 3, 3); font-size: 20px">
        选择收藏夹
      </div>
    </template>
    <a-row
      style="
        margin-top: 20px;
        max-height: 400px;
        overflow: auto;
        min-height: 400px;
      "
    >
      <a-col style="width: 100%">
        <a-row v-if="myCollectSet.length === 0" style="justify-content: center">
          <a-empty />
        </a-row>
        <a-row
          v-for="(collectSet, index) in myCollectSet"
          :key="collectSet.id"
          style="position: relative"
        >
          <a-col style="width: 100%">
            <a-row v-if="index">
              <a-divider />
            </a-row>
            <a-row>
              <span
                @click="handleOpenCollectSet(collectSet)"
                style="
                  font-size: 20px;
                  font-family: Arial, Helvetica, sans-serif;
                  font-weight: bold;
                  cursor: pointer;
                "
                >{{ collectSet.name }}</span
              >
            </a-row>

            <a-row style="margin-top: 5px; color: gray">
              <a-col style="font-size: 16px">
                {{ collectSet.count }} 条内容
              </a-col>
            </a-row>
          </a-col>
          <a-col
            :style="{
              position: 'absolute',
              left: '570px',
              top: index > 0 ? '70px' : '20px',
            }"
            style="z-index: 1000"
          >
            <a-button
              @click="collectThing(collectSet.id)"
              style="
                border: 1px solid rgb(39, 158, 248);
                color: rgb(39, 158, 248);
                display: flex;
                align-items: center;
              "
            >
              收藏
            </a-button>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
    <a-row style="margin-top: 20px; justify-content: center">
      <a-col>
        <a-button
          @click="openCreateCollectSet = !openCreateCollectSet"
          style="
            color: white;
            font-size: 16px;
            display: flex;
            align-items: center;
          "
          type="primary"
          >创建新的收藏夹</a-button
        >
      </a-col>
    </a-row>
    <template #footer>
      <a-button type="default" @click="handleCancel">取消</a-button>
    </template>
  </a-modal>
  <CreateCollectSetComponent
    @addCollectSet="addCollectSet"
    :open="openCreateCollectSet"
    :type="props.type"
  />
</template>

<script setup>
import { getNowTime } from "@/js/functions/TimeAbout";
import { onBeforeUnmount, onMounted, onBeforeMount } from "vue";
import router from "@/router/router";
import { ref, watch } from "vue";
import axios from "axios";
import { onBeforeRouteLeave } from "vue-router";
import DiscussContentComponent from "@/components/component/discuss/DiscussContentComponent.vue";
import { SERVER, SERVER_URL } from "@/js/functions/config";
import { useStore } from "vuex";
import { useRoute } from "vue-router";
import _ from "lodash";
import CommentComponent from "@/components/component/discuss/CommentComponent.vue";
import { sleep } from "@/js/functions/TimeAbout";
import { defineProps } from "vue";
import { getColumnByCell } from "element-plus/es/components/table/src/util";
import { defineEmits } from "vue";
import CreateCollectSetComponent from "./CreateCollectSetComponent.vue";

const emit = defineEmits();
const collectThing = (obj) => {
  emit("collectThing", obj);
};

const openCreateCollectSet = ref(false);
const store = useStore();
const open = ref(0);
const props = defineProps({
  open: Boolean,
  type: String,
});
const myCollectSet = ref([]);
const addCollectSet = (value) => {
  myCollectSet.value.unshift(value);
};
const getCollectSet = async () => {
  await axios
    .get(`${SERVER_URL}/collect/query/set`, {
      params: {
        userid: JSON.parse(localStorage.getItem("user")).userid,
        type: props.type,
      },
    })
    .then((res) => {
      myCollectSet.value = res.data.reverse();
    })
    .catch((err) => {
      console.log(err);
    });
};
const handleCancel = () => {
  open.value = false;
};
const initData = async () => {
  await getCollectSet();
};
watch(
  () => props.open,
  async (newValue, oldValue) => {
    // 在父组件属性发生变化时执行初始化逻辑
    // 执行初始化逻辑，例如获取讨论状态等
    open.value = newValue;
    await getCollectSet();
  }
);

watch(
  () => props.type,
  async (newValue, oldValue) => {
    // 在父组件属性发生变化时执行初始化逻辑
    // 执行初始化逻辑，例如获取讨论状态等
    await initData();
  }
);
onMounted(async () => {
  await initData();
});
</script>

<style scoped></style>
