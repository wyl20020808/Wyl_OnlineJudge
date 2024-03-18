<template>
  <div :style="animationStyle">
    <div class="countdown">距离比赛开始还有 {{ countdownText }}</div>
    <div class="relax">深呼吸，放松一下吧</div>
  </div>
</template>
  
  <script>
export default {
  props: ["startdate"],
  data() {
    return {
      countdownText: "",
      animationDuration: "5s", // 动画持续时间
      minOpacity: 0.1, // 最小不透明度（此示例中未使用）
    };
  },
  computed: {
    animationStyle() {
      return {
        animation: `fadeInOut ${this.animationDuration} infinite`,
        color: "black",
      };
    },
  },
  methods: {
    updateCountdown() {
      let now = new Date();
      let start = new Date(this.startdate);
      let diff = Math.floor((start - now) / 1000);
      let hours = Math.floor(diff / 3600);
      let minutes = Math.floor((diff % 3600) / 60);
      let seconds = diff % 60;
      this.countdownText = ` ${hours}小时${minutes}分钟${seconds}秒 `;
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
<style >
@keyframes fadeInOut {
  0%,
  100% {
    opacity: 0.4;
    transform: scale(0.8); /* 缩小效果 */
  }
  50% {
    opacity: 1;
    transform: scale(1); /* 正常大小 */
  }
}

.countdown {
  font-size: 30px; /* 较大的字体大小 */
  font-weight: bold; /* 加粗 */
  text-align: center; /* 居中显示 */
  margin-bottom: 10px; /* 与下方文字的间距 */
}

.relax {
  font-size: 20px; /* 较小的字体大小 */
  font-style: italic; /* 斜体 */
  text-align: center; /* 居中显示 */
  color: #858585; /* 较淡的字体颜色 */
}

/* 应用动画到整个容器 */
.container-animation {
  animation: fadeInOut 5s infinite;
  color: gray; /* 字体颜色 */
}
</style>
  