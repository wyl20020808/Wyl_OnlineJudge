<template>
  <div v-if="!timesUp" style="min-height: 250px;align-items: center;display: flex;justify-content: center;">
  <timeDown  :startdate="startDate"/>
  </div>
  <!-- 需要弄一个倒计时的东西，这里只是做了一个权限的测试，目前的权限不太行，哈哈哈 -->
  <div v-else>
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
  </div>
</template>

<script>
import timeDown from "../others/timeCountdown/timeDown.vue"
import { CheckOutlined } from "@ant-design/icons-vue";
import { defineComponent, onMounted } from "vue";
import { Table } from "ant-design-vue";
import router from "@/router/router";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
import { isLogin } from "@/js/functions/login";
export default defineComponent({
  components: {
    "a-table": Table,
    "a-table-column": Table.Column,
    CheckOutlined,
    timeDown,
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
      hasPower: false,
      timesUp:false,//判断是否到达比赛的时间
      startDate:"123",
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
    compareTime(specificDateTime) {
      const now = new Date();
      const targetDateTime = new Date(specificDateTime);
      if (now >= targetDateTime) {
        return true; // 当前时间晚于给定时间
      }
      return false; // 当前时间早于给定时间
    },
    checkHasPower(grade) {//检测是否有权限
      if (isLogin === false) {
        return;
      }
      let userGrade = JSON.parse(localStorage.getItem("user")).grade;
   
      if (parseInt(userGrade) >= parseInt(grade)) {
        this.hasPower = true;
      }
    },
  },
  watch: {
    async contest(newVal, oldVal) {
      this.checkHasPower(this.contest.contestcontent.grade);
      // console.log(this.hasPower,"有权利吗")
      this.timesUp = this.compareTime(this.contest.contestcontent.startdate)//判断一下是否到了比赛时间
      this.startDate = this.contest.contestcontent.startdate
      console.log(this.startDate,"比赛时间到了吗")
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
  async onMounted() {
    //先要验证权限是否满足
  },
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
