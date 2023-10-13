<template>
  <svg :width="width" :height="height">
    <rect
      v-for="(value, index) in data"
      :key="index"
      :x="Math.floor(index / 7) * cellSize + margin.left"
      :y="(index % 7) * cellSize + margin.top"
      :width="cellSize - padding"
      :height="cellSize - padding"
      :fill="colorScale(value)"
    />
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
</template>

<script setup>
import { ref, computed } from "vue";
import { defineProps } from "vue";
import { onMounted } from "vue";
import { getNowTime } from "@/js/functions/TimeAbout";
import { watch } from "vue";

const props = defineProps({
  userid: Number,
});
const data = ref(generateData());
const width = 53 * 20 + 40;
const height = 7 * 20 + 40;
const cellSize = 20;
const padding = 3;
const margin = { top: 20, right: 20, bottom: 20, left: 35 };
const months = [
  "Nov",
  "Dec",
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
];
const days = ["Mon", "Wed", "Fri"];

const Ac = new Set();

function generateData() {
  return Array.from({ length: 53 * 7 }, () =>
    Math.random() < 0.2 ? 0 : Math.random()
  );
}

const colorScale = (value) => {
  if (value > 1.2) return "#004d00";
  if (value > 0.9) return "#006600";
  if (value > 0.7) return "#008000";
  if (value > 0.5) return "#009900";
  if (value > 0.3) return "#00b300";
  if (value > 0) return "#00cc00";
  return "rgb(230,230,230)";
};
function calculateDate(time) {
  const date = new Date(time);
  date.setDate(date.getDate() - 53 * 7);
  return date.toISOString().slice(0, 10);
}
function isBetween(date, start, end) {
  const dateTimestamp = new Date(date).getTime();
  const startTimestamp = new Date(start).getTime();
  const endTimestamp = new Date(end).getTime();
  return dateTimestamp >= startTimestamp && dateTimestamp <= endTimestamp;
}
const initData = async () => {
  await axios
    .get(`${SERVER_URL}/judge/query/userid`, {
      params: {
        userid: props.userid,
      },
    })
    .then((res) => {
      let data = res.data;
      let end = String(getNowTime()).slice(0, 10);
      let start = calculateDate(end);
      for (let i = 0; i < data.length; i++) {
        let problemid = data[i].problemid;
        let score = data[i].score;
        let time = data[i].submittime;
        if (isBetween(time, start, end)) {
          //应该要从53 * 7天开始计算，计算每一天的，而不是这样算，明天要改
        }
        // if (
        //   !this.historyScore.get(problemid) ||
        //   data[i].score > this.historyScore.get(problemid)
        // )
        //   this.historyScore.set(problemid, data[i].score);
      }
    })
    .catch((error) => {
      console.log(error);
    });
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
