<template>
  <div class="container">
    <div style="font-size: 36px" class="title">{{ stateMent }}</div>
    <el-form label-position="left" label-width="100px">
      <el-form-item label="题目名称">
        <el-input
          :autosize="{ minRows: 1, maxRows: 5 }"
          v-model="problemcontent.title"
          type="textarea"
          placeholder="请输入题目名称"
          class="input"
        ></el-input>
      </el-form-item>
      <el-form-item label="题目背景">
        <v-md-editor
          placeholder="题目背景"
          v-model="problemcontent.background"
          height="400px"
        ></v-md-editor>
      </el-form-item>
      <el-form-item label="题目描述">
        <v-md-editor
          placeholder="题目描述"
          v-model="problemcontent.description"
          height="300px"
        ></v-md-editor>
        <!-- <el-input v-model="description" type="textarea" placeholder="请输入题目描述" class="input"></el-input> -->
      </el-form-item>
      <el-form-item label="输入格式">
        <v-md-editor
          placeholder="输入格式"
          v-model="problemcontent.inputformat"
          height="300px"
        ></v-md-editor>
        <!-- <el-input v-model="inputFormat" type="textarea" placeholder="请输入输入格式" class="input"></el-input> -->
      </el-form-item>
      <el-form-item label="输出格式">
        <v-md-editor
          placeholder="输出格式"
          v-model="problemcontent.outputformat"
          height="300px"
        ></v-md-editor>
        <!-- <el-input v-model="outputFormat" type="textarea" placeholder="请输入输出格式" class="input"></el-input> -->
      </el-form-item>
      <el-form-item label="样例组">
        <el-button @click="addExample" style="margin-left: 1090px"
          >添加样例</el-button
        >
      </el-form-item>
      <a-row v-for="(example, index) in examples" :key="index" class="example">
        <a-col
          :span="2"
          @mouseenter="handleMouseEnter(index)"
          @mouseleave="handleMouseLeave(index)"
          style="display: flex; align-items: center; margin-right: 0px"
        >
          样例 #{{ index + 1 }}
          <CloseOutlined
            style="margin-left: 5px; font-size: 12px"
            v-if="mouseOnSample.get(index)"
            class="hoverable"
            @click="removeExample(index)"
          />
        </a-col>
        <a-col :span="11" class="example-input-col">
          <el-input
            style="width: 580px"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 10 }"
            v-model="example.input"
            placeholder="样例输入"
          ></el-input>
        </a-col>
        <a-col :span="11">
          <el-input
            style="width: 580px"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 10 }"
            v-model="example.output"
            placeholder="样例输出"
          ></el-input>
        </a-col>
      </a-row>

      <el-form-item label="数据范围">
        <v-md-editor
          placeholder="提示/数据范围"
          v-model="problemcontent.hint"
          height="300px"
        ></v-md-editor>
        <!-- <el-input v-model="hint" type="textarea" placeholder="请输入提示" class="input"></el-input> -->
      </el-form-item>
    </el-form>

    <el-form label-position="left" >
      <a-row>
        <a-col
          ><el-form-item label="时间限制" >
            <a-select
            
              ref="select"
              v-model:value="problemcontent.timelimit"
              style="width: 100px;margin-left: 30px;"
              :options="timeoptions"
              @focus="focus"
            ></a-select>
            <!-- <el-input v-model="hint" type="textarea" placeholder="请输入提示" class="input"></el-input> -->
          </el-form-item></a-col
        >
        <a-col style="margin-left: 100px;"
          ><el-form-item label="空间限制" >
            <a-select
              ref="select"
              v-model:value="problemcontent.memorylimit"
              style="width: 100px;"
              :options="memoryoptions"
              @focus="focus"
            ></a-select>
            <!-- <el-input v-model="hint" type="textarea" placeholder="请输入提示" class="input"></el-input> -->
          </el-form-item></a-col
        >
        <a-col style="margin-left: 100px;">
          <el-form-item label="难度">
            <a-select
              ref="select"
              v-model:value="problemcontent.difficulty"
              style="width: 100px"
              :options="difficultys"
              @focus="focus"
            ></a-select>
          </el-form-item>
        </a-col>
        <a-col style="margin-left: 100px;"
          ><el-form-item label="算法">
            <a-select
              v-model:value="problemcontent.algorithm"
              mode="multiple"
              show-search
              placeholder="算法"
              style="width: 430px"
              :options="algorithmsAndDataStructuresOptions"
              :filter-option="filterOption"
              @focus="handleFocus"
              @blur="handleBlur"
              @change="handleChange"
            ></a-select> </el-form-item
        ></a-col>
        <a-col
          ><el-form-item label="所属题库">
            <a-select
              ref="select"
              v-model:value="problemcontent.questionbank"
              style="width: 200px;margin-left: 30px;"
              :options="questionbanks"
              @focus="focus"
            ></a-select>
            <!-- <el-input v-model="hint" type="textarea" placeholder="请输入提示" class="input"></el-input> -->
          </el-form-item></a-col
        >
      </a-row>
    </el-form>
    <a-row style="width: 100%;" justify="space-between">
      <a-col>
        <el-button
          style="color: aliceblue; width: 80px"
          type="primary"
          @click="save"
          class="save-button"
          >保存</el-button
        >
     
        <el-button
          v-if="problemid"
          type="primary"
          style="color: white; width: 100px; margin-left: 20px"
          @click="backQuestion"
          >返回题目</el-button
        >
        <el-button
          v-else
          type="primary"
          style="color: rgb(255, 252, 252);width: 80px; margin-left: 20px"
          @click="backQuestion"
          >取消</el-button
        >
      </a-col>
      <a-col  v-if="problemid">
        <el-button
          type="danger"
          style="color: white; width: 100px; margin-left: 20px"
          @click="deleteQuestion"
          >删除题目</el-button
        >
    
        <input
          style="margin-left: 20px;width: 200px;"
          type="file"
          accept=".zip"
          @change="handleFileUpload($event)"
        />
        <el-button
          type="primary"
          style="color: white; width: 100px"
          @click="upload"
          >上传数据</el-button
        >
      </a-col>
    </a-row>

  </div>
</template>

<script>
import { CloseOutlined } from "@ant-design/icons-vue";
import axios from "axios";
import { SERVER_URL } from "../../../js/functions/config";
import { sleep } from "@/js/functions/TimeAbout";
import router from "@/router/router";
import _ from "lodash";
export default {
  components: {
    CloseOutlined,
  },
  data() {
    const timeoptions = [];
    for (let i = 1; i <= 50; i++) {
      timeoptions.push({
        value: i * 100,
        label: `${i * 100}ms`,
      });
    }
    const memoryoptions = [];
    const difficultys = [
      {
        value: "暂无评定",
        label: "暂无评定",
      },
      {
        value: "入门",
        label: "入门",
      },
      {
        value: "简单",
        label: "简单",
      },
      {
        value: "还行",
        label: "还行",
      },
      {
        value: "一般",
        label: "一般",
      },
      {
        value: "小难",
        label: "小难",
      },
      {
        value: "好难",
        label: "好难",
      },
      {
        value: "骚难",
        label: "骚难",
      },
    ];
    const questionbanks = [
      {
        value: "jxust",
        label: "jxust",
      },
      {
        value: "loj",
        label: "loj",
        disabled: false,
      },
      {
        value: "luogu",
        label: "洛谷",
        disabled: true,
      },
      {
        value: "codeforces",
        label: "CodeForces",
        disabled: true,
      },
    ];
    for (let i = 0; i < 10; i++) {
      memoryoptions.push({
        value: 2 ** i,
        label: `${2 ** i}MB`,
      });
    }
    let algorithmsAndDataStructures = [
      // 数据结构
      "数组",
      "链表",
      "栈",
      "队列",
      "哈希表",
      "堆",
      "二叉树",
      "二叉搜索树",
      "平衡二叉树（如AVL树）",
      "红黑树",
      "B树和B+树",
      "线段树",
      "树状数组",
      "并查集",
      "字典树",
      "后缀树",
      "图（邻接矩阵、邻接表）",
      "稀疏表",
      "跳表",
      "布隆过滤器",

      // 算法
      "深度优先搜索",
      "广度优先搜索",
      "二分搜索",
      "快速排序",
      "归并排序",
      "堆排序",
      "动态规划",
      "贪心算法",
      "回溯算法",
      "分治算法",
      "最短路径（Dijkstra、Floyd、Bellman-Ford）",
      "最小生成树（Prim、Kruskal）",
      "拓扑排序",
      "并查集算法",
      "KMP字符串匹配",
      "Rabin-Karp字符串匹配",
      "Z-algorithm字符串匹配",
      "Manacher's Algorithm",
      "Tarjan强连通分量",
      "Ford-Fulkerson最大流",
      "Edmonds-Karp最大流",
      "Dinic's Algorithm",
      "Hungarian Algorithm（匈牙利算法）",
      "数论算法（如欧几里得算法、中国剩余定理）",
      "几何算法（如凸包、线段交点）",
      "NP问题的近似算法",
      "随机化算法",
      "概率算法",
      "线性规划",
      "网络流",
      "最大公约数和最小公倍数",
      "素数筛法",
      "快速幂",
      "高精度计算",
      "FFT（快速傅里叶变换）",
      "N皇后问题",
      "旅行商问题",
      "图着色问题",
    ];
    let algorithmsAndDataStructuresOptions = algorithmsAndDataStructures.map(
      (item) => {
        return {
          value: item,
          label: item,
        };
      }
    );
    return {
      algorithmsAndDataStructuresOptions,
      problemcontent: {
        difficulty: "简单",
        algorithm: [],
        title: "",
        background: "",
        description: "",
        inputformat: "",
        outputformat: "",
        timelimit: 1000,
        memorylimit: 64,
        hint: "",
        questionbank: "jxust",
      },
      file: null,
      difficultys,
      timeoptions,
      memoryoptions,
      examples: [],
      stateMent: "新建题目",
      mouseOnSample: new Map(),
      questionbanks,
      problemid: this.$route.query.problemid,
      isModified: false,
      backup: {}, //保存原来的，看看是否有更改
    };
  },
  methods: {
    beforeUnload(e) {
      if (this.isModified) {
        e.preventDefault();
        e.returnValue = "";
      }
    },
    deleteQuestion() {
      axios
        .post(`${SERVER_URL}/problem/delete/problem`, {
          problemid: this.problemid,
        })
        .then((response) => {
          this.$store.dispatch("notice", {
            title: "删除成功！",
            message: "",
            type: "success",
          });
          sleep(500);
          router.push({ name: "problems" });
        })
        .catch((error) => {
          this.$store.dispatch("notice", {
            title: "删除失败！",
            message: "服务器异常" + error,
            type: "error",
          });
        });
    },
    backQuestion() {
      router.go(-1);
    },
    saveProblemInfo() {
      let problemsample = [];
      let algorithm = "";
      for (let i = 0; i < this.problemcontent.algorithm.length; i++) {
        //以空格的形式分开存储
        algorithm += this.problemcontent.algorithm[i] + " ";
      }
      this.problemcontent.algorithm = algorithm;
      this.problemcontent.problemid = this.problemid;
      this.examples.forEach((sample) => {
        problemsample.push({
          problemid: this.problemid,
          input: sample.input,
          output: sample.output,
        });
      });

      let problem = {
        problemcontent: this.problemcontent,
        problemsample: problemsample,
      };
      axios
        .post(`${SERVER_URL}/problem/update/problem`, problem)
        .then((response) => {
          this.$store.dispatch("notice", {
            title: "保存成功！",
            message: "",
            type: "success",
          });
          
          this.problemcontent.algorithm = algorithm.trim().split(" ");//恢复
          this.backup = {
            ...this.problemcontent,
            ...this.examples,
          }
        })
        .catch((error) => {
          this.$store.dispatch("notice", {
            title: "保存失败！",
            message: "服务器异常" + error,
            type: "error",
          });
        });
    },
    handleFileUpload(event) {
      this.file = event.target.files[0];
    },
    async upload() {
      let formData = new FormData();
      formData.append("zipFile", this.file);
      formData.append("problemId", parseInt(this.problemid));
      console.log(this.file, this.problemid);
      await axios
        .post(`${SERVER_URL}/sample/upload`, formData)
        .then(async (response) => {
          this.$store.dispatch("notice", {
            title: "数据上传成功！",
            message: "",
            type: "success",
          });
        })
        .catch((error) => {
          this.$store.dispatch("notice", {
            title: "数据上传失败！",
            message: error,
            type: "error",
          });
        });
      // 然后你可以使用你的HTTP库（例如axios）来发送这个formData到服务器
      // axios.post('/upload', formData)
    },
    filterOption(input, option) {
      return option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    },
    handleMouseEnter(index) {
      // console.log("鼠标进入了元素");
      this.mouseOnSample.set(index, true);
    },
    handleMouseLeave(index) {
      // console.log("鼠标离开了元素");
      this.mouseOnSample.set(index, false);
    },
    addExample() {
      this.examples.push({ input: "", output: "" });
    },
    removeExample(index) {
      this.examples.splice(index, 1);
    },
    rollBackInfo(dataSend) {
      this.title = dataSend.title;
      this.background = dataSend.background;
      this.description = dataSend.description;
      this.inputFormat = dataSend.inputformat;
      this.outputFormat = dataSend.outputformat;
      this.hint = dataSend.hint;
    },
    save() {
      if (this.problemid) {
        this.saveProblemInfo();
        return;
      }
      let algorithms = this.algorithm[0];
      for (let i = 1; i < this.algorithm.length; i++)
        algorithms = algorithms + " " + this.algorithm[i];
      //向后端传送基本的题目数据
      const dataSend = {
        title: this.title,
        background: this.background,
        description: this.description,
        inputformat: this.inputFormat,
        outputformat: this.outputFormat,
        hint: this.hint,
        problemsample: this.examples,
        questionbank: this.questionbank,
        difficulty: this.difficulty,
        algorithm: algorithms,
        timelimit: this.selectedTime,
        memorylimit: this.selectedMemory,
        source: JSON.parse(localStorage.getItem("user")).userid,
      };
      axios
        .post(`${SERVER_URL}/problem/insert`, dataSend)
        .then((response) => {
          if (response.data === "success") {
            this.$store.dispatch("notice", {
              title: "保存成功！",
              message: "",
              type: "success",
            });
            sleep(500);
            this.$router.push({ path: "/problems" });
          } else {
            // alert("保存失败！" + response.data)
            this.$store.dispatch("notice", {
              title: "保存失败！",
              message: response.data,
              type: "error",
            });
            this.rollBackInfo(dataSend);
          }
        })
        .catch((error) => {
          this.$store.dispatch("notice", {
            title: "保存失败！",
            message: error,
            type: "error",
          });
          this.rollBackInfo(dataSend);
        });
    },
  },
  async created() {
    this.backup = {
            ...this.problemcontent,
            ...this.examples,
          };
    if (this.problemid) {
      this.stateMent = "编辑题目 - P" + this.problemid;
      axios
        .get(`${SERVER_URL}/problem/query/probleminfo/${this.problemid}`)
        .then((response) => {
          let data = response.data;
          this.problemcontent = data.problemcontent;
          this.examples = data.problemsample;
          this.problemcontent.algorithm = String(data.problemcontent.algorithm)
            .trim()
            .split(" ");
          this.backup = {
            ...this.problemcontent,
            ...this.examples,
          };
        })
        .catch((error) => {});
    }
  },
  beforeRouteLeave(to, from, next) {
    let temp = {
      ...this.problemcontent,
      ...this.examples,
    };
    if (!_.isEqual(this.backup, temp)) {
      //判断两个对象是否相等
      if (window.confirm("你有未保存的更改，确定要离开吗？")) {
        next();
      } else {
        next(false);
      }
    } else {
      next();
    }
  },
  mounted() {
    window.addEventListener("beforeunload", this.beforeUnload);
  },
  beforeDestroy() {
    window.removeEventListener("beforeunload", this.beforeUnload);
  },
};
</script>

<style>
.container {
  width: 100%;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}

.title {
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: bold;
}

.input {
  width: 800px;
  min-height: 50px;
}

.example {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.example-input {
  width: 50%;
  min-height: 50px;
}

.example-button {
  width: 8%;
}

.add-button {
  margin-bottom: 20px;
}

.save-button {
  width: 50%;
}
.hoverable {
  transition: color 0.3s ease, text-decoration 0.3s ease;
  cursor: pointer;
}

.hoverable:hover {
  filter: brightness(1.3);
  text-decoration: underline;
}
.el-form-item__label {
  font-weight: bold;
}
</style>
