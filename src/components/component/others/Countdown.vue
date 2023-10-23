<template>
  <div style="color:gray;">{{ countdownText }}</div>
</template>

<script>
export default {
  props: ["startdate", "enddate"],
  data() {
    return {
      countdownText: "",
    };
  },
  methods: {
    updateCountdown() {
      let now = new Date();
      let start = new Date(this.startdate);
      let end = new Date(this.enddate);
      if (now < start) {
        let diff = Math.floor((start - now) / 1000);
        let days = Math.floor(diff / 86400);
        let hours = Math.floor((diff % 86400) / 3600);
        let minutes = Math.floor((diff % 3600) / 60);
        let seconds = diff % 60;
        this.countdownText = `距比赛：${days}天${hours}:${minutes}:${seconds}`;
      } else if (now < end) {
        let diff = Math.floor((end - now) / 1000);
        let days = Math.floor(diff / 86400);
        let hours = Math.floor((diff % 86400) / 3600);
        let minutes = Math.floor((diff % 3600) / 60);
        let seconds = diff % 60;
        this.countdownText = `距结束：${days}天${hours}:${minutes}:${seconds}`;
      } else {
        this.countdownText = `比赛已结束`;
      }
    },
  },
  created() {
    this.intervalId = setInterval(this.updateCountdown, 1000);
  },
  beforeDestroy() {
    clearInterval(this.intervalId);
  },
};
</script>
