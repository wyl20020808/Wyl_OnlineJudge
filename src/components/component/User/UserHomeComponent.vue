<template>
  <a-row>
    <a-col>
      <a-row>
        <a-col>
          <svg :width="width" :height="height">
            <text
              v-for="(month, index) in months"
              :key="index"
              :x="
                33 +
                (index * (width - margin.left - margin.right)) / months.length +
                margin.left
              "
              :y="margin.top - 5"
              font-size="13"
              fill="gray"
            >
              {{ month }}
            </text>
            <text
              v-for="(day, index) in days"
              :key="index"
              :x="margin.left - 5"
              :y="
                20 +
                (index * (height - margin.top - margin.bottom)) / days.length +
                margin.top +
                5
              "
              text-anchor="end"
              font-size="13"
              fill="gray"
            >
              {{ day }}
            </text>
          </svg>

          <div
            v-for="(item, index) in data"
            :key="index"
            :style="{
              position: 'absolute',
              left: `${Math.floor(index / 7) * cellSize + margin.left}px`,
              top: `${(index % 7) * cellSize + margin.top}px`,
              width: `${cellSize - padding}px`,
              height: `${cellSize - padding}px`,
              backgroundColor: colorScale(item.value),
            }"
          >
            <el-tooltip
              class="box-item"
              effect="dark"
              :content="`${item.date}，通过：${item.value}`"
              placement="top"
              :popper-options="{ boundariesElement: 'viewport' }"
              :visible-arrow="false"
              :offset="10"
            >
              <div style="width: 100%; height: 100%"></div>
            </el-tooltip>
          </div>
        </a-col>
      </a-row>
      <a-row
        style="
          display: flex;
          align-items: center;
          justify-content: center;
          margin-left: 180px;
        "
      >
        <a-col span="8">
          <a-row style="font-size: 25px"
            >{{ judgeinfo.allSolve }} Problems</a-row
          >
          <a-row style="color: gray">solved for all time</a-row>
        </a-col>
        <a-col span="8">
          <a-row style="font-size: 25px"
            >{{ judgeinfo.yearSolve }} Problems</a-row
          >
          <a-row style="color: gray">solved for last year</a-row>
        </a-col>
        <a-col span="8">
          <a-row style="font-size: 25px"
            >{{ judgeinfo.monthSolve }} Problems</a-row
          >
          <a-row style="color: gray">solved for last month</a-row>
        </a-col>
      </a-row>
      <a-row
        style="
          display: flex;
          align-items: center;
          justify-content: center;
          margin-top: 20px;
          margin-left: 180px;
        "
      >
        <a-col span="8">
          <a-row style="font-size: 25px">{{ judgeinfo.allSubmit }} Times</a-row>
          <a-row style="color: gray">submit for all time</a-row>
        </a-col>
        <a-col span="8">
          <a-row style="font-size: 25px"
            >{{ judgeinfo.yearSubmit }} Times</a-row
          >
          <a-row style="color: gray">submit for last year</a-row>
        </a-col>
        <a-col span="8">
          <a-row style="font-size: 25px"
            >{{ judgeinfo.monthSubmit }} Times</a-row
          >
          <a-row style="color: gray">submit for last month</a-row>
        </a-col>
      </a-row>
    </a-col>
  </a-row>
</template>
<script setup>
import { ref, computed } from "vue";
import { defineProps } from "vue";
import { onMounted } from "vue";
import { getNowTime } from "@/js/functions/TimeAbout";
import { watch } from "vue";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
const props = defineProps({
  userid: Number,
});
const data = ref([]);
const width = 53 * 20 + 40;
const height = 7 * 20 + 40;
const cellSize = 20;
const padding = 3;
const margin = { top: 20, right: 20, bottom: 20, left: 35 };
const months = ref([]);
const days = ["Mon", "Wed", "Fri"];
const Ac = new Set();
const datePass = new Map();
let judgeData = ref([]);
let judgeinfo = ref({
  allSolve: 0,
  yearSolve: 0,
  monthSolve: 0,
  allSubmit: 0,
  yearSubmit: 0,
  monthSubmit: 0,
});

function generateMonths(time) {
  const monthNames = [
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sep",
    "Oct",
    "Nov",
    "Dec",
  ];
  const currentMonth = new Date(time).getMonth();
  const months = [];
  for (let i = currentMonth + 1; i < 12; i++) {
    months.push(monthNames[i]);
  }
  for (let i = 0; i <= currentMonth; i++) {
    months.push(monthNames[i]);
  }
  return months;
}

function isWithinOneMonth(date1, date2) {
  const d1 = new Date(date1);
  const d2 = new Date(date2);
  const oneMonthLater = new Date(d1.getTime());
  oneMonthLater.setMonth(oneMonthLater.getMonth() + 1);
  return d2 >= d1 && d2 < oneMonthLater;
}

function isWithinOneYear(date1, date2) {
  const d1 = new Date(date1);
  const d2 = new Date(date2);
  const oneYearLater = new Date(d1.getTime());
  oneYearLater.setFullYear(oneYearLater.getFullYear() + 1);
  return d2 >= d1 && d2 < oneYearLater;
}

function generateData(dates) {
  return dates.map((date) => ({
    date: date,
    value: datePass.get(date) ? datePass.get(date) : 0,
  }));
}

function getJudgeData() {
  const yearSet = new Set();
  const allSet = new Set();
  const monthSet = new Set();
  let now = String(getNowTime());
  for (let i = 0; i < judgeData.value.length; i++) {
    let time = judgeData.value[i].submittime;
    let score = judgeData.value[i].score;
    let problemid = judgeData.value[i].problemid;
    //计算全部提交
    judgeinfo.value.allSubmit += 1;
    if (parseInt(score) === 100) {
      //计算全部通过
      if (!allSet.has(problemid)) {
        allSet.add(problemid);
        judgeinfo.value.allSolve += 1;
      }
    }
    if (isWithinOneMonth(time, now)) {
      judgeinfo.value.monthSubmit += 1; //计算一个月提交
      if (parseInt(score) === 100) {
        //计算一个月通过
        if (!monthSet.has(problemid)) {
          monthSet.add(problemid);
          judgeinfo.value.monthSolve += 1;
        }
      }
    }
    if (isWithinOneYear(time, now)) {
      judgeinfo.value.yearSubmit += 1; //计算一年提交
      if (parseInt(score) === 100) {
        //计算一年通过
        if (!yearSet.has(problemid)) {
          yearSet.add(problemid);
          judgeinfo.value.yearSolve += 1;
        }
      }
    }
  }
}

function generateDates(time) {
  const dates = [];
  const startDate = new Date(time);
  startDate.setDate(startDate.getDate() - 370); // 371天前的日期
  for (let i = 0; i < 371; i++) {
    const date = new Date(startDate);
    date.setDate(startDate.getDate() + i);
    dates.push(date.toISOString().slice(0, 10)); // 使用push方法
  }
  return dates;
}

const colorScale = (value) => {
  if (value >= 10) return "#006600";
  if (value >= 5) return "rgb(13, 158, 13)";
  if (value >= 1) return "rgb(97, 220, 97)";

  return "rgb(230,230,230)";
};
function calculateDate(time) {
  const date = new Date(time);
  date.setDate(date.getDate() - 370); // 设置日期为371天前
  return date.toISOString().slice(0, 10);
}
function isBetween(date, start, end) {
  const dateTimestamp = new Date(date).getTime();
  const startTimestamp = new Date(start).getTime();
  const endTimestamp = new Date(end).getTime();
  return dateTimestamp >= startTimestamp && dateTimestamp <= endTimestamp;
}

const initData = async () => {
  let newData = [];
  months.value = generateMonths(String(getNowTime()).slice(0, 10)); //动态生成月份顺序
  await axios
    .get(`${SERVER_URL}/judge/query/userid`, {
      params: {
        userid: props.userid,
      },
    })
    .then((res) => {
      let data = res.data;
      judgeData.value = res.data;
      getJudgeData(); //计算提交数据
      let end = String(getNowTime()).slice(0, 10);
      let start = calculateDate(end); //当年的一月一号
      for (let i = 0; i < data.length; i++) {
        let problemid = data[i].problemid;
        let score = data[i].score;
        let time = String(data[i].submittime).slice(0, 10);
        if (parseInt(score) !== 100 || Ac.has(problemid)) continue;
        if (isBetween(time, start, end)) {
          Ac.add(problemid); //去重 当然以后可以加一个如果半年之内没有AC，就算重新AC了，现在不加
          if (!datePass.has(time)) {
            datePass.set(time, 0);
          }
          datePass.set(time, datePass.get(time) + 1);
        }
      }

      newData = generateDates(end);
    })
    .catch((error) => {
      console.log(error);
    });
  data.value = generateData(newData); // 将新数据赋给data.value
  // data.value = newData; // 将新数据赋给data.value
  // console.log(data.value);
};
watch(
  () => props.userid,
  async (newVal, oldVal) => {
    await initData();
  }
);
onMounted(async () => {
  if (props.userid) {
    await initData();
  }
});
</script>

<style scoped>
.ceshi {
  color: rgb(176, 255, 167);
}
</style>
