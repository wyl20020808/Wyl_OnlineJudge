<template>
  <div style="margin-bottom: 20px">
    <a-row style="justify-content: center">
      <div class="card main1">
        <a-row style="margin-top: 20px">
          <a-col :offset="1" :span="24">
            <div style="font-size: 36px">比赛信息</div>
          </a-col>
        </a-row>
        <a-row style="margin-top: 20px">
          <a-col class="input-container" :offset="1" :span="6">
            <label for="name">赛制：</label>
            <a-select
              ref="select"
              v-model:value="contestformat"
              style="width: 200px"
              :options="contestFormat"
              @focus="focus"
            ></a-select>
          </a-col>
          <a-col class="input-container" :span="14">
            <label for="name">标题：</label>
            <a-textarea
              v-model:value="title"
              placeholder="输入比赛的标题"
              auto-size
            />
          </a-col>
        </a-row>
        <a-row style="margin-top: 20px">
          <a-col class="input-container" :offset="1">
            <label for="name">比赛时间：</label>
            <a-space direction="vertical" :size="20">
              <el-date-picker
                v-model="contestdate"
                type="datetimerange"
                :shortcuts="shortcuts"
                range-separator="To"
                start-placeholder="Start date"
                end-placeholder="End date"
              />
            </a-space>
          </a-col>
          <a-col class="input-container" :offset="1" :span="6">
            <label for="name">比赛等级：</label>
            <a-select
              ref="select"
              v-model:value="grade"
              style="width: 200px"
              :options=grades
              @focus="focus"
            ></a-select>
          </a-col>
        </a-row>
        <a-row style="margin-top: 20px">
          <a-col class="input-container" :offset="1">
            <label for="name">比赛题目：</label>
            <a-select
              v-model:value="problems"
              show-search
              mode="multiple"
              placeholder="输入题目名称、ID进行搜索"
              style="width: 700px"
              :default-active-first-option="false"
              :show-arrow="false"
              :filter-option="false"
              :not-found-content="null"
              :options="problemsdata"
              @search="handleSearch"
              @change="handleChange"
            ></a-select>
          </a-col>
        </a-row>

        <a-row style="margin-top: 20px">
          <a-col class="input-container" :offset="1" :span="20">
            <label for="name">比赛说明：</label>
            <a-textarea
              v-model:value="contestdescription"
              placeholder="输入比赛说明"
              :auto-size="{ minRows: 14, maxRows: 30 }"
              show-count
              :maxlength="114514"
            />
          </a-col>
        </a-row>
        <a-row style="margin-top: 20px">
          <a-col :offset="1" :span="24">
            <div style="font-size: 36px">比赛权限</div>
          </a-col>
        </a-row>
        <a-row style="margin-top: 20px">
          <a-col class="input-container" :offset="1">
            <label for="name">管理员：</label>
            <a-select
              v-model:value="administrators"
              show-search
              mode="multiple"
              placeholder="输入管理员名称、ID进行搜索"
              style="width: 700px"
              :default-active-first-option="false"
              :show-arrow="false"
              :filter-option="false"
              :not-found-content="null"
              :options="adminsdata"
              @search="handleSearchAdmin"
              @change="handleChangeAdmin"
            ></a-select>
          </a-col>
        </a-row>
        <a-row style="margin-top: 20px">
          <a-col class="input-container" :offset="1" :span="6">
            <label for="name">权限：</label>
            <a-select
              ref="select"
              v-model:value="contestlimit"
              style="width: 200px"
              :options="[
                {
                  value: '公开',
                  label: '公开',
                },
                {
                  value: '私密(需要密码)',
                  label: '私密(需要密码)',
                },
              ]"
              @focus="focus"
            ></a-select>
          </a-col>
          <a-col
            v-if="contestlimit === '私密(需要密码)'"
            class="input-container"
            :span="10"
          >
            <label for="name">密码：</label>
            <a-textarea
              v-model:value="contestpassword"
              placeholder="输入比赛的密码"
              auto-size
            />
          </a-col>
        </a-row>
        <a-row style="margin-top: 20px">
          <a-col :offset="1" :span="24">
            <h2>比赛设置</h2>
          </a-col>
        </a-row>
        <a-row style="margin-top: 20px">
          <a-col :offset="1" :span="2">
            <label for="name">Rated</label>
            <a-switch v-model:checked="rated" />
          </a-col>
          <a-col :offset="1" :span="2">
            <label for="name">赛时封榜</label>
            <a-switch v-model:checked="blockedlist" />
          </a-col>
        </a-row>
        <a-row style="margin-top: 20px; color: white">
          <a-col :offset="1" :span="2">
            <a-button
              v-if="!$route.query.contestid"
              @click="creatContest"
              style="margin-bottom: 10px"
              type="primary"
              >创建</a-button
            >
            <a-button
              v-else
              @click="creatContest"
              style="margin-bottom: 10px"
              type="primary"
              >保存</a-button
            >
          </a-col>
          <a-col style="color: black" :offset="1" :span="2">
            <a-button
              @click="
                () => {
                  this.$router.push({ path: '/contest' });
                }
              "
              >取消</a-button
            >
          </a-col>
        </a-row>
      </div>
    </a-row>
  </div>
</template>

<script>
import { SERVER_URL } from "@/js/functions/config";
import axios from "axios";
import { getNowTime, sleep } from "@/js/functions/TimeAbout";
import router from "@/router/router";
import moment from "moment";
export default {
  methods: {
    async saveContest(contest) {
      if (this.$route.query.contestid) {
        await axios
          .post(`${SERVER_URL}/contest/update`, contest)
          .then((res) => {
            this.$store.dispatch("notice", {
              title: "保存成功！",
              message: "",
              type: "success",
            });
            sleep(500);
            router.push({
              path: "/contest/content",
              query: { contestid: this.$route.query.contestid },
            });
          })
          .catch((err) => {
            console.log(err);
          });
        return;
      }
      await axios
        .post(`${SERVER_URL}/contest/create`, contest)
        .then((res) => {
          this.$store.dispatch("notice", {
            title: "比赛创建成功！",
            message: "",
            type: "success",
          });
          sleep(500);
          router.push({ path: "/contest" });
        })
        .catch((err) => {
          console.log(err);
        });
    },
    creatContest: async function () {
      let contestcontent = {
        userid: JSON.parse(localStorage.getItem("user")).userid,
        contestname: this.title,
        startdate:  moment(this.contestdate[0]).utcOffset(8).format('YYYY-MM-DD HH:mm:ss'),
        enddate: moment(this.contestdate[1]).utcOffset(8).format('YYYY-MM-DD HH:mm:ss'),
        contestformat: this.contestformat,
        contestlimit: this.contestlimit,
        rated: this.rated,
        blockedlist: this.blockedlist,
        createtime: getNowTime(),
        contestpassword: this.contestpassword,
        description: this.contestdescription,
        grade:this.grade,
      };
      if (this.$route.query.contestid) {
        //更新
        contestcontent.contestid = this.$route.query.contestid;
        contestcontent.username = this.contest.contestcontent.username;
        contestcontent.joinpeople = this.contest.contestcontent.joinpeople;
      }
      let contestadmin = [];
      for (let admin in this.administrators) {
        let data = {
          userid: this.administrators[admin],
        };
        if (this.$route.query.contestid)
          data.contestid = this.$route.query.contestid;

        contestadmin.push(data);
      }
      //提交数和通过数需要去备份
      if (this.$route.query.contestid) {
        //更新
        for (let i = 0; i < this.contest.contestproblem.length; i++) {
          this.backCount.set(
            this.contest.contestproblem[i].problemid,
            this.contest.contestproblem[i].submitcount
          );
          this.backCount.set(
            this.contest.contestproblem[i].problemid + "x",
            this.contest.contestproblem[i].acceptedcount
          );
        }
      }
      let contestproblem = [];
      for (let i = 0; i < this.problems.length; i++) {
        let data = {
          problemid: this.problems[i],
          problemchar: String.fromCharCode("A".charCodeAt(0) + i),
        };
        if (this.$route.query.contestid) {
          data.contestid = this.$route.query.contestid;
          if (this.backCount.has(this.problems[i])) {
            //如果有这个题，就要恢复
            data.submitcount = this.backCount.get(this.problems[i]);
            data.acceptedcount = this.backCount.get(this.problems[i] + "x");
          }
        }
        contestproblem.push(data);
      }
      let contest = {
        contestcontent: contestcontent,
        contestadmin: contestadmin,
        contestproblem: contestproblem,
      };
      //name和contestid在后端去计算
      let allFieldsFilled = Object.values(contestcontent).every(
        (x) => x !== null && x !== "" && x !== undefined
      );
      allFieldsFilled =
        allFieldsFilled &&
        this.administrators.length > 0 &&
        this.problems.length > 0;
      if (!allFieldsFilled) {
        this.$store.dispatch("notice", {
          title: "比赛信息不完整",
          message: "",
          type: "error",
        });
      } else {
        this.saveContest(contest);
      }
    },
    fetch: async function (value, type, callback) {
      this.currentValue = value;
      await axios
        .post(`${SERVER_URL}/contest/query/${type}`, {
          //查询不同类型的数据
          special: value, //有可能因为不是string类型出错
        })
        .then((res) => {
          const data = [];
          for (let i = 0; i < res.data.length; i++) {
            if (type === "problem")
              data.push({
                value: res.data[i].problemid,
                label: res.data[i].problemid + " " + res.data[i].title,
              });
            else
              data.push({
                value: res.data[i].userid,
                label: res.data[i].userid + " " + res.data[i].nickname,
              });
          }
          callback(data);
        })
        .catch((err) => {
          console.log(err, "ahsjdhas");
        });
    },
    initData() {
      this.title = this.contest.contestcontent.contestname;
      this.contestdate = [
        moment(this.contest.contestcontent.startdate, "YYYY-MM-DD HH:mm:ss"),
        moment(this.contest.contestcontent.enddate, "YYYY-MM-DD HH:mm:ss"),
      ];
      this.contestformat = this.contest.contestcontent.contestformat;
      this.contestlimit = this.contest.contestcontent.contestlimit;
      this.rated = this.contest.contestcontent.rated;
      this.blockedlist = this.contest.contestcontent.blockedlist;
      this.contestpassword = this.contest.contestcontent.contestpassword;
      this.problems = [];
      // console.log(this.contest.contestproblem,'aa')
      for (let i = 0; i < this.contest.contestproblem.length; i++) {
        this.problems.push(this.contest.contestproblem[i].problemid);
      }
      this.administrators = [];
      for (let i = 0; i < this.contest.contestadmin.length; i++) {
        this.administrators.push(this.contest.contestadmin[i].userid);
      }
      this.contestdescription = this.contest.contestcontent.description;
    },
  },
  data() {
    const grades = [];
    //如果可以到这个界面说明已经登陆 当然目前还不完善
    let userGrade = JSON.parse(localStorage.getItem("user")).grade;
    for(let i = 1; i <= userGrade; i++){
      grades.push({
                  value: i,
                  label: i,
                })
    }
    return {
      contestFormat: [
        { value: "ACM/ICPC", label: "ACM/ICPC" },
        { value: "IOI", label: "IOI", disabled: true },
        { value: "OI", label: "OI", disabled: true },
      ],

      focus: () => {
        console.log("focus");
      },
      handleChange: (val) => {
        //比赛题目改变
        console.log(val);
        this.problems = val;
        this.handleSearch("");
      },
      handleSearch: (val) => {
        //搜索比赛题目
        this.fetch(val, "problem", (d) => (this.problemsdata = d));
      },
      handleChangeAdmin: (val) => {
        //比赛管理员改变
        console.log(val);
        this.administrators = val;
        this.handleSearch("");
      },
      handleSearchAdmin: (val) => {
        //比赛管理员搜索

        this.fetch(val, "user", (d) => (this.adminsdata = d));
      },
      grades,
      grade:1,//等级
      title: "", //比赛题目
      contestdate: "", //比赛日期
      contestformat: "", //比赛模式
      problemsdata: [], //需要筛选的题目数据
      adminsdata: [], //需要筛选的管理员数据
      problems: [], //选中的题目数据
      administrators: [], //选中的管理员数据
      timeout: null,
      currentValue: "",
      contestlimit: "公开", //比赛限制
      rated: false, //是否积分
      blockedlist: true, //是否封榜
      contestpassword: "wyloj.com", //比赛密码
      contestdescription: `【比赛规范】
为保证比赛公平公正，营造健康积极的竞赛环境，我们采取以下措施：
1.赛后对代码系统查重+人工查重，不同账号相似度100%的提交直接算作弊处理，不区分提交先后。某些简单的签到题视情况处理。相似度80%以上进行人工核对。赛后提交代码全部公开，接受用户投诉。（相似度非文本比较）
2.同一用户使用多账号提交相同代码也按违规处理。
3.比赛期间在互联网公开寻求题目答案按违规处理，接受用户举报。
4.比赛结束前公开答案按违规处理
5.其它影响到比赛公平的行为按违规处理
对于违规用户：
第一次违规：取消本场比赛获得奖品的资格，扣除rating200分，公开违规用户名单在比赛说明页面。
第二次违规：封停账号`, //比赛描述
      contest: {},
      backCount: new Map(),
    };
  },
  async created() {
    if (this.$route.query.contestid) {
      await axios
        .get(`${SERVER_URL}/contest/query/contest`, {
          params: {
            contestid: this.$route.query.contestid,
          },
        })
        .then((res) => {
          let data = res.data;
          this.contest = data;
          console.log(data);
          this.initData();
        });
    }
  },
};
</script>

<style scoped>
.main1 {
  background-color: white;
  /* border: black; */
  width: 1000px;
  position: relative;
  top: 10px;
}

.input-container {
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 5px; /* 调整这个值可以改变文字和输入框之间的距离 */
  font-size: 18px;
  color: rgb(96, 95, 95);
}
</style>
