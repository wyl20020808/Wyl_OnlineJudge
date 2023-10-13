<template>
  <a-row style="margin-top: 30px; align-items: center; justify-content: center">
    <a-col>
      <a-row style="margin-bottom: 10px">
        <a-button
          @click="jump('creatediscuss')"
          style="
            padding: 14px;
            color: white;
            display: flex;
            align-items: center;
          "
          type="primary"
        >
          <PlusOutlined style="font-size: 20px" />
          <div style="margin-left: 10px; font-size: 18px; font-weight: bold">
            新建讨论
          </div>
        </a-button>
      </a-row>
      <a-row>
        <a-col style="width: 1200px">
          <a-table style="font-size: 16px;" :columns="columns" :data-source="data">
            <template #bodyCell="{ record, column, text }">
              <div
                class="hoverable2"
                @click="jumpTodiscuss(record)"
                v-if="column"
                :style="{
                  color:
                    column.dataIndex === 'title' ||
                    column.dataIndex === 'username'
                      ? '#4183c4'
                      : 'inherit',
                }"
              >
                <a style="height: 100px"> {{ text }}</a>
              </div>
            </template>
          </a-table>
        </a-col>
      </a-row>
    </a-col>
  </a-row>
</template>
<!-- 2023年10月12日14:57:35 -->
<script setup>
import router from "@/router/router";
import { SearchOutlined, PlusOutlined } from "@ant-design/icons-vue";
import { onMounted, ref } from "vue";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";

const columns = [
  {
    title: "标题",
    dataIndex: "title",
    key: "title",
  },
  {
    title: "发布者",
    dataIndex: "username",
    key: "username",
    width: 200,
    align: "right",
  },
  {
    title: "浏览",
    dataIndex: "browse",
    key: "browse",
    ellipsis: true,
    width: 80,
    align: "center",
  },
  {
    title: "支持",
    dataIndex: "likes",
    key: "likes",
    width: 80,
    ellipsis: true,
    align: "center",
  },
  {
    title: "更新时间",
    dataIndex: "edittime",
    key: "edittime",
    width: 200,
    ellipsis: true,
    align: "center",
  },
];
const data = ref([]);
function jumpTodiscuss(data) {
  router.push({
    path: "/discusscontent",
    query: {
      id: data.id,
      type: data.type,
    },
  });
}
function jump(total) {
  router.push({ path: "/" + total });
}
onMounted(async () => {
  await axios
    .get(`${SERVER_URL}/discuss/query`, {
      params: {
        id: 0,
        type: "discuss",
      },
    })
    .then((res) => {
      data.value = res.data;
    });
});
</script>

<style scoped>
.ant-table {
  height: 100px; /* 设置你想要的行高 */
}
.hoverable2 {
  /* color: #3498db; */
  transition: color 0.3s ease;
  cursor: pointer;
}

.hoverable2:hover {
  filter: brightness(1.5);
}
</style>
