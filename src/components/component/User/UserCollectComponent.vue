<template>
  <a-row>
    <a-col style="width: 100%">
      <a-row justify="space-between">
        <a-col style="width: 97%">
          <a-tabs
            tabBarGutter="0px"
            class="my-tabs"
            size="small"
            v-model:activeKey="activeKey2"
            @change="chooseTab"
          >
            <a-tab-pane key="1" tab="题目收藏夹">
              <a-row>
                <a-col style="width: 100%">
                  <a-row
                    v-for="(collectSet, index) in problemCollectSet"
                    :key="collectSet.id"
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
                        <a-col style="font-size: 16px; margin-left: 15px">
                          {{ collectSet.count }} 条内容
                        </a-col>
                        <a-col
                          style="
                            display: flex;
                            align-items: center;
                            margin-left: 20px;
                            margin-top: 2px;
                            cursor: pointer;
                          "
                          @click="modifyCollectSet(collectSet)"
                        >
                          <img
                            style="width: 20px"
                            src="../../../assets/static/pictures/edit2.png"
                            alt=""
                          />
                          <span style="font-size: 16px">编辑</span>
                        </a-col>
                        <a-col
                          style="
                            margin-left: 20px;
                            margin-top: 4px;
                            cursor: pointer;
                          "
                        >
                          <a-popconfirm
                            title="你确定要删除此文件夹？删除之后里面的收藏也会消失"
                            ok-text="确定"
                            cancel-text="取消"
                            @confirm="deleteCollectSet(collectSet.id)"
                          >
                            <span style="display: flex; align-items: center"
                              ><img
                                style="width: 20px"
                                src="../../../assets/static/pictures/delete2.png"
                                alt=""
                              />
                              <span style="font-size: 16px">删除</span></span
                            >
                          </a-popconfirm>
                        </a-col>
                      </a-row>
                    </a-col>
                  </a-row>
                </a-col>
              </a-row>
            </a-tab-pane>
            <a-tab-pane  key="2" tab="文章收藏夹">
              <a-row>
                <a-col style="width: 100%">
                  <a-row
                    v-for="(collectSet, index) in discussCollectSet"
                    :key="collectSet.id"
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
                        <a-col style="font-size: 16px; margin-left: 15px">
                          {{ collectSet.count }} 条内容
                        </a-col>
                        <a-col
                          style="
                            display: flex;
                            align-items: center;
                            margin-left: 20px;
                            margin-top: 2px;
                            cursor: pointer;
                          "
                          @click="modifyCollectSet(collectSet)"
                        >
                          <img
                            style="width: 20px"
                            src="../../../assets/static/pictures/edit2.png"
                            alt=""
                          />
                          <span style="font-size: 16px">编辑</span>
                        </a-col>
                        <a-col
                          style="
                            margin-left: 20px;
                            margin-top: 4px;
                            cursor: pointer;
                          "
                        >
                          <a-popconfirm
                            title="你确定要删除此文件夹？删除之后里面的收藏也会消失"
                            ok-text="确定"
                            cancel-text="取消"
                            @confirm="deleteCollectSet(collectSet.id)"
                          >
                            <span style="display: flex; align-items: center"
                              ><img
                                style="width: 20px"
                                src="../../../assets/static/pictures/delete2.png"
                                alt=""
                              />
                              <span style="font-size: 16px">删除</span></span
                            >
                          </a-popconfirm>
                        </a-col>
                      </a-row>
                    </a-col>
                  </a-row>
                </a-col>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-col>
        <a-col style="position: absolute; left: 1030px">
          <a-button
            @click="openCreateCollectSet = !openCreateCollectSet"
            style="color: rgb(39, 158, 248); display: flex; align-items: center"
            ghost
          >
            <PlusOutlined />
            <span>新建收藏夹</span>
          </a-button>
        </a-col>
      </a-row>
    </a-col>
  </a-row>
  <CreateCollectSetComponent
    style="z-index: 1000"
    @addCollectSetWithType="addCollectSet"
    @modifyCollectSet="updateCollectSet"
    :open="openCreateCollectSet"
    :collectSet="editCollectSet"
  />
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
import { SearchOutlined, PlusOutlined } from "@ant-design/icons-vue";
import CreateCollectSetComponent from "./CreateCollectSetComponent.vue";
import { message } from "ant-design-vue";
const store = useStore();
const props = defineProps({
  userid: Number,
});


const editCollectSet = ref(null);
const activeKey2 = ref("1");
const openCreateCollectSet = ref(false);
const addCollectSet = (value, type) => {
  if (type === "problem") {
    problemCollectSet.value.unshift(value);
  } else {
    discussCollectSet.value.unshift(value);
  }
};
//
const allCollect = ref([]);
const problemCollectSet = ref([]);
const discussCollectSet = ref([]);
const updateCollectSet = (collectSet) => {
  for (let i = 0; i < problemCollectSet.value.length; i++) {
    if (problemCollectSet.value[i].id === collectSet.id) {
      if (collectSet.type === "problem") {
        problemCollectSet.value[i] = collectSet;
      } else {
        problemCollectSet.value.splice(i, 1);
        discussCollectSet.value.unshift(collectSet);
      }

      return; // 找到后就退出循环
    }
  }

  // 如果在problemCollectSet中没找到，就在discussCollectSet中查找
  for (let i = 0; i < discussCollectSet.value.length; i++) {
    if (discussCollectSet.value[i].id === collectSet.id) {
      if (collectSet.type === "discuss") {
        discussCollectSet.value[i] = collectSet;
      } else {
        discussCollectSet.value.splice(i, 1);
        problemCollectSet.value.unshift(collectSet);
      }
      return; // 找到后就退出循环
    }
  }
};
const modifyCollectSet = (collectSet) => {
  editCollectSet.value = collectSet;
  openCreateCollectSet.value = !openCreateCollectSet.value;
};
const handleOpenCollectSet = (collectSet) => {
  router.push({
    path: "/collectset",
    query: {
      id: collectSet.id,
      userid: collectSet.userid,
    },
  });
};

const deleteCollectSet = async (id) => {
  await axios
    .post(
      `${SERVER_URL}/collect/update/set`,
      {
        id,
      },
      {
        params: {
          delete: "yes",
        },
      }
    )
    .then((res) => {
      for (let i = 0; i < problemCollectSet.value.length; i++) {
        if (problemCollectSet.value[i].id === id) {
          // 如果找到了匹配的id，就从数组中删除这个元素
          problemCollectSet.value.splice(i, 1);
          break; // 找到后就退出循环
        }
      }

      // 如果在problemCollectSet中没找到，就在discussCollectSet中查找
      for (let i = 0; i < discussCollectSet.value.length; i++) {
        if (discussCollectSet.value[i].id === id) {
          // 如果找到了匹配的id，就从数组中删除这个元素
          discussCollectSet.value.splice(i, 1);
          break; // 找到后就退出循环
        }
      }
      message.success("删除成功！");
      // store.dispatch("notice", {
      //   title: "删除成功！",
      //   message: "",
      //   type: "success",
      // });
    })
    .catch((err) => {
      console.log(err);
    });
};

const getCollectSet = async () => {
  await axios
    .get(`${SERVER_URL}/collect/query/set`, {
      params: {
        userid: props.userid,
      },
    })
    .then((res) => {
      for (let i = 0; i < res.data.length; i++) {
        if (res.data[i].type === "problem") {
          problemCollectSet.value.unshift(res.data[i]);
        } else {
          discussCollectSet.value.unshift(res.data[i]);
        }
      }
    })
    .catch((err) => {
      console.log(err);
    });
};
const getAllCollet = () => {
  axios
    .get(`${SERVER_URL}/collect/query`, {
      params: {
        userid: props.userid,
      },
    })
    .then((res) => {
      allCollect.value = res.data;
    })
    .catch((err) => {
      console.log(err);
    });
};
const initData = async () => {
  await getCollectSet();
  getAllCollet();
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
