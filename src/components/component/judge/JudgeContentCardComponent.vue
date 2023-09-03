<template>
  <div class="card main">
    <div :style="{ color: statusColor }">
      <div v-if="loading" class="loading">
        <div class="loader" :class="{ fast: fast }"></div>
        <div class="notice">
          {{ statusText }} ({{ elapsedTime }} seconds elapsed)
        </div>
      </div>
      <div v-else>
        <div class="ok">
          <span v-if="score === 100"
            ><el-icon><Check /></el-icon
          ></span>
          <!-- 显示 "√" -->
          <span v-else
            ><el-icon><Close /></el-icon
          ></span>
          <!-- 显示 "×" -->
          {{ judgestate }} {{ score }}
        </div>
        <div
          v-for="judgecontent in judgeinfo.judgecontent"
          :key="judgecontent.judgecontentid"
        >
      
      </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Check, Close } from "@element-plus/icons";
export default {
  props: {
    judgeinfo: {
      type: Object,
      required: true,
    },
  },
  components: {
    Check,
    Close,
  },

  data() {
    return {
      score: 0,
      judgestate: "Wrong Answer",
      loading: true,
      startTime: Date.now(),
      elapsedTime: 0,
      fast: false,
      randomTime: Math.floor(Math.random() * 5) + 1, // 生成1到5之间的随机数
    };
  },
  computed: {
    statusColor() {
      if (this.loading) {
        return "orange";
      } else {
        return this.score === 100 ? "green" : "red";
      }
    },
    statusText() {
      return this.fast ? "Running Code...." : "Compile Code....";
    },
  },
  methods: {},
  watch: {
    judgeinfo: {
      handler(newJudgeInfo, oldJudgeInfo) {
        this.loading = false;
        this.score = this.judgeinfo.judge.score;
        this.judgestate = this.judgeinfo.judge.judgestate;
      },
      deep: true,
    },
  },
  created() {
    this.interval = setInterval(() => {
      this.elapsedTime = ((Date.now() - this.startTime) / 1000).toFixed(2);
      if (this.elapsedTime > this.randomTime) {
        this.fast = true;
      }
    }, 1000);
  },
  beforeDestroy() {
    clearInterval(this.interval);
  },
};
</script>

<style scoped>
.icon-check {
  color: green;
}

.icon-times {
  color: red;
}
.main {
  width: 800px;
  position: relative;
  left: 200px;
  top: 50px;
  min-height: 90px;
  height: auto;
}
.loader {
  border: 8px solid #f3f3f3;
  border-top: 8px solid #3498db;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 2s linear infinite;
  position: relative;
}
.loading {
  position: absolute;
  top: 20px;
  left: 20px;
}
.notice {
  position: relative;
  left: 60px;
  bottom: 33px;
}
.loader.fast {
  animation: spin 1s linear infinite;
}
.ok {
  position: relative;
  font-size: 24px;
  left: 20px;
  top: 20px;
}
@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
