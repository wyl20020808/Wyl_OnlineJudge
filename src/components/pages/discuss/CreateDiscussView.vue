<template>
  <a-row
    style="
      margin-top: 40px;
      margin-left: 70px;
      align-items: center;
      justify-content: center;
    "
  >
    <a-col>
      <a-row style="margin-top: 0px; margin-bottom: 20px">
        <a-breadcrumb>
          <!-- <a-breadcrumb-item>Home</a-breadcrumb-item>
          <a-breadcrumb-item
            ><a href="">Application Center</a></a-breadcrumb-item
          > -->
          <a-breadcrumb-item
            ><a @click="jump('discuss')">讨论</a></a-breadcrumb-item
          >
          <a-breadcrumb-item v-if="route.query.id"
            ><a @click="goBack">文章详情</a></a-breadcrumb-item
          >
          <a-breadcrumb-item v-if="route.query.id">编辑文章</a-breadcrumb-item>
          <a-breadcrumb-item v-if="!route.query.id">发表文章</a-breadcrumb-item>
        </a-breadcrumb>
      </a-row>
      <a-row style="margin-left: 60px; margin-bottom: 10px">
        <a-col>
          <a-input
            style="
              font-weight: bold;
              width: 1170px;
              height: 50px;
              font-size: 18px;
            "
            v-model:value="discuss.title"
            placeholder="标题"
          >
            <template #prefix>
              <img
                src="../../../assets/static/pictures/title2.png"
                width="26"
              />
            </template>
          </a-input>
        </a-col>
      </a-row>
      <a-row>
        <a-col>
          <img
            width="50"
            style="border-radius: 50%"
            src="../../../assets/static/pictures/wyl.jpg"
            alt="Avatar"
          />
        </a-col>
        <a-col style="margin-left: 10px; width: 1300px">
          <div class="card1">
            <div style="color: #4183c4" class="text">Elegant</div>
          </div>
          <div
            style="
              z-index: 1000;
              position: relative;
              bottom: 3px;
              border: 1px solid #c6c5c5;
              border-bottom: 1px solid white;

              width: 90%;
              height: 10px;
              background-color: white;
            "
          ></div>
          <div class="card2">
            <v-md-editor
              placeholder="发布一篇文章"
              v-model="discuss.content"
              height="400px"
            ></v-md-editor>
          </div>
        </a-col>
      </a-row>
      <a-row justify="space-between" style="width: 1230px">
        <a-col>
          <el-form-item label="类型">
            <a-select
              v-model:value="discuss.type"
              style="width: 80px; margin-left: 20px"
              :options="typeOptions"
              @focus="focus"
            ></a-select>
          </el-form-item>
        </a-col>
        <a-col>
          <a-button @click="cancel" style="width: 100px; margin-right: 20px"
            >取消</a-button
          >
          <a-button
            v-if="route.query.id"
            @click="deleteDiscuss"
            style="width: 100px; margin-right: 20px"
            danger
            >删除</a-button
          >
          <a-button
            @click="saveDiscuss"
            style="color: white; width: 100px"
            type="primary"
            >保存</a-button
          ></a-col
        >
      </a-row>
    </a-col>
  </a-row>
</template>

<script setup>
import { getNowTime } from "@/js/functions/TimeAbout";
import { onBeforeUnmount, onMounted } from "vue";
import { ref } from "vue";
import axios from "axios";
import { onBeforeRouteLeave } from "vue-router";
import { SERVER_URL } from "@/js/functions/config";
import { useStore } from "vuex";
import { useRouter, useRoute } from "vue-router";
import _ from "lodash";
import { sleep } from "@/js/functions/TimeAbout";
let store = useStore();
let discuss = ref({
  title: "",
  type: "discuss",
  content: "",
});
const router = useRouter();
const route = useRoute();
let discussBackup = ref({
  ...discuss.value,
});
let typeOptions = ref([
  {
    value: "notice",
    label: "公告",
  },
  {
    value: "discuss",
    label: "讨论",
  },
  {
    value: "solution",
    label: "题解",
  },
]);
function goBack() {
  router.go(-1);
}
function jump(total) {
  router.push({ path: "/" + total });
}
async function deleteDiscuss() {
  await axios
    .post(`${SERVER_URL}/discuss/operator`, {
      id: route.query.id,
      special: "delete",
    })
    .then((res) => {
      store.dispatch("notice", {
        title: "删除成功",
        message: "",
        type: "success",
      });
      sleep(500);
      jump("discuss");
    })
    .catch((err) => {
      console.log(err);
    });
}
async function getDiscuss(id, type) {
  //获取当前的讨论
  await axios
    .get(`${SERVER_URL}/discuss/query`, {
      params: {
        id: id,
        type: type,
      },
    })
    .then((res) => {
      discuss.value = res.data[0];
      discussBackup.value = {
        ...discuss.value,
      };
    })
    .catch((err) => {
      console.log(err);
    });
}
onMounted(() => {
  window.addEventListener("beforeunload", beforeUnload);
  if (route.query.id) {
    getDiscuss(route.query.id, route.query.type);
  }
});

onBeforeUnmount(() => {
  window.removeEventListener("beforeunload", beforeUnload);
});

function modifyed(){
  let temp = {
    ...discuss.value,
  };
  if (!_.isEqual(discussBackup.value, temp)) {
    return true;
  }
  return false;
}

function cancel() {
  router.go(-1);
}

async function saveDiscuss() {
  if (!String(discuss.value.title).trimEnd()) {
    store.dispatch("notice", {
      title: "标题不能为空！",
      message: "",
      type: "error",
    });
    return;
  }
  let data = {
    title: discuss.value.title,
    content: discuss.value.content,
    type: discuss.value.type,
    createtime: getNowTime(),
    edittime: getNowTime(),
    userid: JSON.parse(localStorage.getItem("user")).userid,
    username: JSON.parse(localStorage.getItem("user")).username,
    userpicture: JSON.parse(localStorage.getItem("user")).userpicture,
  };
  if (route.query.id) {
    data = {
      ...discuss.value,
    };
  }

  if(modifyed()){//如果改过了，就修改时间
    data.edittime = getNowTime();
  }

  await axios
    .post(`${SERVER_URL}/discuss/update`, data)
    .then((res) => {
      discussBackup.value = {
        ...discuss.value,
      };
      store.dispatch("notice", {
        title: "保存成功！",
        message: "",
        type: "success",
      });
      sleep(500);

      router.push("/discuss");
    })
    .catch((err) => {
      store.dispatch("notice", {
        title: "保存失败",
        message: err,
        type: "error",
      });
      console.log(err);
    });
}
function beforeUnload(event) {
  //刷新前判断
  let temp = {
    ...discuss.value,
  };
  if (!_.isEqual(discussBackup.value, temp)) {
    event.preventDefault();
    event.returnValue = "";
  }
}
onBeforeRouteLeave((to, from, next) => {
  //离开前判断
  let temp = {
    ...discuss.value,
  };
  console.log(discussBackup.value, temp);

  if (!_.isEqual(discussBackup.value, temp)) {
    if (window.confirm("你有未保存的更改，确定要离开吗？")) {
      next();
    } else {
      next(false);
    }
  } else {
    next();
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
  width: 90%;
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
