<template>
  <a-table :dataSource="datas" :pagination="false">
    <a-table-column
      title="题号"
      dataIndex="problemchar"
      key="problemchar"
      width="100px"
      align="center"
    />
    <a-table-column
      title="题目名称"
      dataIndex="problemname"
      key="problemname"
      align="center"
    >
      <template v-slot="{ record }">
        <div
          class="hoverable"
          @click="viewProblem(record.problemid, record.problemchar)"
        >
          {{ record.problemname }}
        </div>
      </template>
    </a-table-column>
    <a-table-column
      title="通过率"
      dataIndex="passrate"
      key="passrate"
      align="center"
    >
      <template v-slot="{ record }">
        <div>
          {{
            (submitCount.has(record.problemchar)
              ? submitCount.get(record.problemchar).get("acceptCount")
              : 0) +
            "/" +
            (submitCount.has(record.problemchar)
              ? submitCount.get(record.problemchar).get("submitCount")
              : 0)
          }}
        </div>
      </template>
    </a-table-column>
    <a-table-column
      title="我的状态"
      dataIndex="state"
      key="state"
      align="center"
    >
      <template v-slot="{ record }">
        <div
          style="display: flex; align-items: center; justify-content: center"
        >
          <div
            v-if="getStyle(record) === 'color:#25bb9b'"
            :style="getStyle(record)"
            style="display: flex; align-items: center"
          >
            <CheckOutlined /> 通过
          </div>
          <div
            v-else-if="getStyle(record) === 'color:#25bb9c'"
            :style="getStyle(record)"
            style="display: flex; align-items: center"
          >
            <CheckOutlined /> 通过
          </div>
          <div
            v-else
            :style="getStyle(record)"
            style="display: flex; align-items: center"
          >
            未通过
          </div>
        </div>
      </template>
    </a-table-column>
  </a-table>
</template>

<script>
import { CheckOutlined } from "@ant-design/icons-vue";
import { defineComponent } from "vue";
import { Table } from "ant-design-vue";
import router from "@/router/router";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
export default defineComponent({
  components: {
    "a-table": Table,
    "a-table-column": Table.Column,
    CheckOutlined,
  },
  props: {
    contest: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      datas: [],
      submitCount: new Map(),
      AC: new Map(),
    };
  },
  methods: {
    getStyle(record) {
      // #25bb9b
      // 239,249,247
      //191,230,222
      // 根据 record 或 index 返回不同的类名
      let userid = parseInt(JSON.parse(localStorage.getItem("user")).userid);
      if (
        this.AC.has(record.problemchar) &&
        this.AC.get(record.problemchar).get("firstAC") === userid
      ) {
        return "color:#25bb9c";
      } else if (
        this.AC.has(record.problemchar) &&
        this.AC.get(record.problemchar).get(userid)
      ) {
        console.log("acle");
        return "color:#25bb9b";
      }
      return "";
    },
    viewProblem(problemid, problemchar) {
      router.push({
        path: "/problem",
        query: {
          problemid,
          contestid: this.contest.contestcontent.contestid,
          problemchar: problemchar,
        },
      });
    },
    getSubmitInfo(data) {
      for (let i = 0; i < data.length; i++) {
        let state = data[i].judgestate;
        if (state === "Compilation Error") continue; //编译错误直接忽略
        let userid = data[i].userid;
        let problemchar = data[i].problemchar;

        if (!this.submitCount.has(problemchar))
          this.submitCount.set(problemchar, new Map());

        if (!this.submitCount.get(problemchar).has("submitCount"))
          this.submitCount.get(problemchar).set("submitCount", 0);
        if (!this.submitCount.get(problemchar).has("acceptCount"))
          this.submitCount.get(problemchar).set("acceptCount", 0);
        let submitCount = this.submitCount.get(problemchar).get("submitCount");
        this.submitCount.get(problemchar).set("submitCount", submitCount + 1);
        if (state === "Accepted") {
          let acceptCount = this.submitCount
            .get(problemchar)
            .get("acceptCount");
          this.submitCount.get(problemchar).set("acceptCount", acceptCount + 1);
        }

        //上面是求通过数量的，下面求一下AC和是否是一血
        if (!this.AC.has(problemchar)) this.AC.set(problemchar, new Map());

        if (state === "Accepted") {
          if (!this.AC.get(problemchar).has("firstAC"))
            //有没有人AC
            this.AC.get(problemchar).set("firstAC", userid);
          else this.AC.get(problemchar).set(userid, userid);
        }
      }
    },
  },
  watch: {
    async contest(newVal, oldVal) {
      await axios
        .get(`${SERVER_URL}/contest/query/alljudge`, {
          params: {
            contestid: newVal.contestcontent.contestid,
          },
        })
        .then((response) => {
          // console.log(response.data);
          let data = response.data;
          this.getSubmitInfo(data);
        })
        .catch((error) => {
          console.log(error);
        });

      let problems = newVal.contestproblem;
      for (let i = 0; i < problems.length; i++) {
        this.datas.push({
          problemchar: String.fromCharCode("A".charCodeAt(0) + i), //这里后续可以改，因为数据库存了
          problemname: problems[i].problemname,
          problemid: problems[i].problemid,
          submitcount: problems[i].submitcount,
          acceptedcount: problems[i].acceptedcount,
          state: "未通过",
        });
      }
    },
  },
  async created() {},
});
</script>

<style scoped>
.hoverable {
  color: #3498db;
  transition: color 0.3s ease, text-decoration 0.3s ease;
  cursor: pointer;
}

.hoverable:hover {
  filter: brightness(1.3);
  text-decoration: underline;
}
</style>
