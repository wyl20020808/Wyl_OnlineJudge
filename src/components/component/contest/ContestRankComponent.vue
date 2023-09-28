<template>
  <table>
    <thead style="background-color: #e8e8e8">
      <tr>
        <th
          v-for="(column, index) in columns"
          :key="index"
          :class="getClass(index)"
          @click="
            goToProblem(column.problemid, column.contestid, column.title, index)
          "
          class="centered-header"
        >
          <div
            style="display: flex; flex-direction: column; align-items: center"
          >
            <div style="position: relative; top: 8px">
              {{ column.title }}
            </div>
            <div
              style="font-weight: lighter; color: rgb(50, 202, 153)"
              v-if="index > 3"
            >
              {{ problemAC.get(column.title) }}
            </div>
            <div
              style="
                position: relative;
                bottom: 8px;
                font-weight: lighter;
                color: rgb(153, 153, 153);
              "
              v-if="index > 3"
            >
              {{ problemSubmit.get(column.title) }}
            </div>
          </div>
        </th>
      </tr>
    </thead>
    <tbody style="background-color: #f4f4f4">
      <tr v-for="(row, rowIndex) in tableData" :key="rowIndex" style="">
        <td
          v-for="(column, columnIndex) in columns"
          :key="columnIndex"
          :style="{
            width: column.width,
            position: 'relative',
            padding: '0',
          }"
        >
          <div
            :class="getClass(columnIndex)"
            :style="{
              height: getHeight(),
              ...getStyle(columnIndex, row, column),
            }"
            style="border-bottom: 1px dashed #ddd"
            @click="handleClick(columnIndex, row, column)"
          >
            {{ row[column.dataIndex] }}
            <div
              v-if="
                columnIndex > 3 && usersubmit.get(row.userid).get(column.title)
              "
            >
              (-{{ usersubmit.get(row.userid).get(column.title) }})
            </div>
          </div>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script>
import axios from "axios";
import { SERVER_URL } from "../../../js/functions/config.js";
import router from "@/router/router";
import moment from "moment";
export default {
  props: {
    contest: {
      type: Object,
      required: true,
    },
  },
  watch: {
    async contest(newVal, oldVal) {
      // console.log("here");
    },
  },
  data() {
    return {
      tableData: [],
      usersubmit: new Map(), //记录每道题的第一次通过前的提交次数
      userActime: new Map(), //记录每道题的通过时间 （没通过就没有数据）
      userinfo: new Map(), //记录通过数和罚时
      userlist: new Set(), //记录有哪些用户
      username: new Map(), //记录每个用户的用户名
      firstAC: new Map(), //记录每个题的第一次AC时间
      firstACId: new Map(), //记录每个题的第一次AC的人
      judgeId: new Map(), //记录，每个人的提交记录
      problemSubmit: new Map(),
      problemAC: new Map(),
      columns: [
        {
          title: "名次",
          dataIndex: "rank",
          key: "rank",
          align: "center",
          width: "60px",
        },
        {
          title: "参赛者",
          dataIndex: "username",
          key: "username",
          align: "center",
          width: "80px",
        },
        {
          title: "通过",
          dataIndex: "passedcountd",
          key: "passedcountd",
          align: "center",
          width: "60px",
        },
        {
          title: "罚时",
          dataIndex: "punishtime",
          key: "punishtime",
          align: "center",
          width: "60px",
        },
      ],
    };
  },
  methods: {
    getHeight() {
      let tableWidth = 950;
      let fixedWidth = 80 * 4; // 前4列的总宽度
      let remainingWidth = tableWidth - fixedWidth;
      let columnCount = this.columns.length - 4; // 减去前4列
      let cellWidth = remainingWidth / columnCount;
      return Math.min(cellWidth, 100) + "px";
    },
    getStyle(index, record, column) {
      let baseStyle = {
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
      };
      if (index === 3) {
        baseStyle.borderRight = "1px solid rgb(220, 220, 220)"; // 添加右边框
      }
      if (index > 3 && this.userActime.get(record.userid).get(column.title)) {
        //已经AC
        // 添加额外的样式
        let problemchar = column.title;
        if (this.firstACId.get(problemchar) === record.userid) {
          baseStyle.backgroundColor = "rgb(191,230,222)";
        } else {
          baseStyle.backgroundColor = "#eff9f7";
        }
      } else if (
        index > 3 &&
        this.usersubmit.get(record.userid).get(column.title)
      ) {
        baseStyle.backgroundColor = "#ffecec";
      }

      return baseStyle;
    },
    updateTableData() {
      let data = []; //为了最后排序后计算rank
      console.log(this.userinfo);
      for (let userid of this.userlist) {
        let temp = {
          username: this.username.get(userid),
          passedcountd: this.userinfo.get(userid).get("passedcount"),
          punishtime: this.userinfo.get(userid).get("punishtime"),
          userid,
        };

        //再存一个map呗
        for (let i = 0; i < this.contest.contestproblem.length; i++) {
          //存每道题的通过时间
          let problemchar = this.contest.contestproblem[i].problemchar;

          if (this.userActime.get(userid).get(problemchar)) {
            //如果有时间就代表AC
            // console.log(problemchar,this.userActime.get(userid).get(problemchar))
            temp[problemchar] = Math.floor(
              this.userActime.get(userid).get(problemchar)
            );
            // temp.acMap.set(problemchar, true);
          } else if (this.usersubmit.get(userid).get(problemchar)) {
          }
        }
        data.push(temp);
      }
      data.sort((a, b) => {
        // 首先比较passedcount，大的在前
        if (a.passedcountd !== b.passedcountd) {
          return b.passedcountd - a.passedcountd;
        }
        // 如果passedcount相同，那么比较punishtime，小的在前
        return a.punishtime - b.punishtime;
      });
      for (let i = 0; i < data.length; i++) {
        data[i].punishtime = Math.floor(data[i].punishtime);
      }
      for (let i = 0; i < data.length; i++) {
        this.tableData.push({
          ...data[i],
          rank: i + 1,
        });
      }
    },
    getJudgeStateColor(judgestate) {
      if (judgestate === "Accepted") return "#25ad40";
      else if (judgestate === "Compilation Error") return "orange";
      else return "red";
    },
    goToJudge(judgeid, contestid, problemchar, problemname) {
      router.push({
        name: "judgecontent",
        query: {
          judgeid: judgeid,
          contestid,
          problemchar,
          problemname,
        },
      });
    },
    goToProblem(problemid, contestid, problemchar, index) {
      if (index <= 3) return;
      router.push({
        path: "/contest/problem",
        query: {
          problemid,
          contestid: contestid,
          problemchar: problemchar,
        },
      });
    },

    initMap(data) {
      for (let i = 0; i < data.length; i++) {
        let userid = data[i].userid;
        this.usersubmit.set(userid, new Map()); //为每个用户建立一个map
        this.userActime.set(userid, new Map());
        this.userinfo.set(userid, new Map());
        this.judgeId.set(userid, new Map());
      }
    },
    goToUser(userid) {
      router.push({
        name: "userhome",
        query: {
          userid: userid,
        },
      });
    },
    handleClick(columnIndex, row, column) {
      if (columnIndex === 1) {
        this.goToUser(row.userid);
      } else if (columnIndex > 3) {
        this.goToJudge(
          this.judgeId.get(row.userid).get(column.title),
          column.contestid,
          column.title,
          column.problemname
        );
      }
    },
    getInfo(data) {
      for (let i = 0; i < data.length; i++) {
        this.userlist.add(data[i].userid);
        this.username.set(data[i].userid, data[i].username);
        let state = data[i].judgestate;
        let judgeid = data[i].judgeid;
        if (state === "Compilation Error") continue; //编译错误直接忽略
        let userid = data[i].userid;
        let problemchar = data[i].problemchar;

        if (!this.usersubmit.get(userid).get(problemchar))
          this.usersubmit.get(userid).set(problemchar, 0); //初始化
        if (!this.userinfo.get(userid).get("passedcount"))
          this.userinfo.get(userid).set("passedcount", 0); //初始化通过数量
        if (!this.userinfo.get(userid).get("punishtime"))
          this.userinfo.get(userid).set("punishtime", 0); //初始化罚时
        if (!this.judgeId.get(userid).get(problemchar))
          this.judgeId.get(userid).set(problemchar, judgeid); //初始化judgeid
        if (!this.problemAC.get(problemchar))
          this.problemAC.set(problemchar, 0);
        if (!this.problemSubmit.get(problemchar))
          this.problemSubmit.set(problemchar, 0);
        else if (state === "Accepted")
          this.judgeId.get(userid).set(problemchar, judgeid);
        //下面是计算每个题提交数和通过数
        this.problemSubmit.set(
          problemchar,
          this.problemSubmit.get(problemchar) + 1
        );
        if (state === "Accepted") {
          this.problemAC.set(problemchar, this.problemAC.get(problemchar) + 1);
        }
        if (
          !this.userActime.get(userid).get(problemchar) &&
          state !== "Accepted"
        ) {
          //如果这道题到现在还没AC
          let submitcount = this.usersubmit.get(userid).get(problemchar);
          this.judgeId.get(userid).set(problemchar, judgeid);
          this.usersubmit.get(userid).set(problemchar, submitcount + 1); //提交次数+1
        } else if (state === "Accepted") {
          if (!this.firstAC.get(problemchar)) {
            this.firstAC.set(problemchar, data[i].submittime);
            this.firstACId.set(problemchar, data[i].userid);
          } else {
            if (
              this.diffMinutes(
                this.firstAC.get(problemchar),
                data[i].submittime
              ) < 0
            ) {
              this.firstAC.set(problemchar, data[i].submittime);
              this.firstACId.set(problemchar, data[i].userid);
            }
          }
          if (!this.userActime.get(userid).get(problemchar)) {
            //如果这题是第一次AC的话，就+1

            let passedcount = this.userinfo.get(userid).get("passedcount");

            this.userinfo.get(userid).set("passedcount", passedcount + 1);

            let time = this.userinfo.get(userid).get("punishtime");
            this.userinfo
              .get(userid)
              .set(
                "punishtime",
                this.diffMinutes(
                  this.contest.contestcontent.startdate,
                  data[i].submittime
                ) + time
              ); //累计罚时
            console.log(
              data[i].submittime,
              this.contest.contestcontent.startdate
            );
            this.userActime
              .get(userid)
              .set(
                problemchar,
                this.diffMinutes(
                  this.contest.contestcontent.startdate,
                  data[i].submittime
                )
              ); //记录每道题是否AC
          }
        }
      }
      let problemset = new Set(); //用来判重
      //最后单独计算一下罚时，因为要总体来看每道题是否AC,AC了才能计算罚时
      for (let i = 0; i < data.length; i++) {
        let state = data[i].judgestate;
        if (state === "Compilation Error") continue; //编译错误直接忽略
        let userid = data[i].userid;
        let problemchar = data[i].problemchar;
        //如果这道题AC了，就要记录罚时
        if (
          this.userActime.get(userid).get(problemchar) &&
          !problemset.has(problemchar)
        ) {
          problemset.add(problemchar);
          let submitcount = this.usersubmit.get(userid).get(problemchar); //某道题的提交次数
          let time = this.userinfo.get(userid).get("punishtime");
          this.userinfo.get(userid).set("punishtime", time + submitcount * 20); //每次提交多20分钟罚时
        }
      }

      // console.log(this.usersubmit, this.userActime, this.userinfo);
    },
    getClass(columnIndex) {
      if (columnIndex === 1 || columnIndex > 3) {
        return "hoverable2";
      } else {
        return "";
      }
    },
    diffMinutes(startDateStr, endDateStr) {
      const startDate = moment(startDateStr, "YYYY-MM-DD HH:mm:ss");
      const endDate = moment(endDateStr, "YYYY-MM-DD HH:mm:ss");
      const diffInMinutes = endDate.diff(startDate, "minutes");
      return diffInMinutes;
    },
  },
  async created() {
    // console.log(this.contest);
    for (let i = 0; i < this.contest.contestproblem.length; i++) {
      this.columns.push({
        title: this.contest.contestproblem[i].problemchar,
        dataIndex: this.contest.contestproblem[i].problemchar,
        key: this.contest.contestproblem[i].problemchar,
        align: "center",
        width: 100, //这里可能会有问题
        problemid: this.contest.contestproblem[i].problemid,
        contestid: this.contest.contestproblem[i].contestid,
        problemname: this.contest.contestproblem[i].problemname,
      });
      let problemchar = this.contest.contestproblem[i].problemchar;
      if (!this.problemAC.get(problemchar)) this.problemAC.set(problemchar, 0);
      if (!this.problemSubmit.get(problemchar))
        this.problemSubmit.set(problemchar, 0);
    }
    await axios
      .get(`${SERVER_URL}/contest/query/alljudge`, {
        params: {
          contestid: this.contest.contestcontent.contestid,
        },
      })
      .then((response) => {
        // console.log(response.data);
        let data = response.data;
        this.initMap(data);
        this.getInfo(data);
        this.updateTableData();
      })
      .catch((error) => {
        console.log(error);
      });
  },
};
</script>

<style scoped>
.hoverable2 {
  /* color: #3498db; */
  transition: color 0.3s ease;
  cursor: pointer;
}

.hoverable2:hover {
  filter: brightness(1.3);
}
table {
  width: 100%; /* 设置表格的宽度 */
  border-collapse: collapse; /* 合并相邻的边框 */
}

th,
td {
  padding: 10px; /* 设置单元格的内边距 */
  /* border: 1px solid #ddd; 设置单元格的边框 */
}

th {
  text-align: center;
}
</style>
