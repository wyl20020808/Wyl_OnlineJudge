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
          <div class="compileoutput">
            <span
              >编译器输出：{{
                decodeBase64(judgeinfo.judge.compileoutput)
              }}</span
            >
          </div>
          <div class="state" v-if="tableData.length > 0">
            <el-table
              class="table1"
              :data="tableData"
              :row-class-name="tableRowClassName"
            >
              <el-table-column prop="judgeid" label="编号"></el-table-column>
              <el-table-column prop="judgestate" label="状态"></el-table-column>
              <el-table-column prop="runtime" label="耗时"></el-table-column>
              <el-table-column prop="memory" label="空间"></el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Check, Close } from "@element-plus/icons";
import { ElTable, ElTableColumn } from "element-plus";
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
    ElTable,
    ElTableColumn,
  },

  data() {
    return {
      score: null,
      judgestate: "Wrong Answer",
      loading: true,
      startTime: Date.now(),
      elapsedTime: 0,
      fast: false,
      randomTime: Math.floor(Math.random() * 5) + 1, // 生成1到5之间的随机数
      tableData: [
        // { judgeid: "1", judgestate: "状态1", runtime: "耗时1", memory: "空间1" },
        // { judgeid: "2", judgestate: "状态2", runtime: "耗时2", memory: "空间2" },
        // ...其他行数据...
      ],
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
  methods: {
    decodeBase64(input) {
      const state = [
        "Accepted",
        "Time Limit Exceeded",
        "Wrong Answer",
        "Runtime Error",
        "Memory Limit Exceeded",
      ];
      if(state.includes(input)) {
        return input;
      }
      return atob(input);
    },
    tableRowClassName({ row, rowIndex }) {
      // 根据行数据和行索引返回类名
      return rowIndex % 2 === 0 ? "odd-row" : "even-row";
    },
    updateInfo() {
      this.loading = false;
      this.score = this.judgeinfo.judge.score;
      this.judgestate = this.judgeinfo.judge.judgestate;
      if (this.judgeinfo.judgecontent.length !== undefined) {
        for (let i = 0; i < this.judgeinfo.judgecontent.length; i++) {
          this.tableData.push({
            judgeid: i + 1,
            judgestate: this.judgeinfo.judgecontent[i].judgestate,
            runtime: this.judgeinfo.judgecontent[i].runtime,
            memory: this.judgeinfo.judgecontent[i].memory,
          });
        }
      }
    },
  },
  watch: {
    judgeinfo: {
      handler(newJudgeInfo, oldJudgeInfo) {
        this.updateInfo();
      },
      deep: true,
    },
  },
  created() {
    if (this.judgeinfo.judge.score !== undefined) {
      //组件重新加载的时候
      this.updateInfo();
    }
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
.odd-row {
  background-color: #f2f2f2;
}
.even-row {
  background-color: #ffffff;
}
.span1 {
  margin-right: 150px;
  font-size: 20px;
  position: relative;
  top: 20px;
}
.state {
  position: relative;
  color: gray;
  top: 50px;
  right: 10px;
}
.compileoutput {
  color: gray;
  position: relative;
  left: 0px;
  top: 30px;
  font-size: 18px;
}
.table1 {
  width: 780px;
  font-size: 16px;
}
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
