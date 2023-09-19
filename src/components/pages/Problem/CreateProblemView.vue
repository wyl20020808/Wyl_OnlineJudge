<template>
  <div class="container">
    <h1 style="font-size: 36px" class="title">新建题目</h1>
    <el-form label-position="left" label-width="100px">
      <el-form-item label="题目名称">
        <el-input
          :autosize="{ minRows: 1, maxRows: 5 }"
          v-model="title"
          type="textarea"
          placeholder="请输入题目名称"
          class="input"
        ></el-input>
      </el-form-item>
      <el-form-item label="题目背景">
        <v-md-editor
          placeholder="题目背景"
          v-model="background"
          height="400px"
        ></v-md-editor>
      </el-form-item>
      <el-form-item label="题目描述">
        <v-md-editor
          placeholder="题目描述"
          v-model="description"
          height="300px"
        ></v-md-editor>
        <!-- <el-input v-model="description" type="textarea" placeholder="请输入题目描述" class="input"></el-input> -->
      </el-form-item>
      <el-form-item label="输入格式">
        <v-md-editor
          placeholder="输入格式"
          v-model="inputFormat"
          height="300px"
        ></v-md-editor>
        <!-- <el-input v-model="inputFormat" type="textarea" placeholder="请输入输入格式" class="input"></el-input> -->
      </el-form-item>
      <el-form-item label="输出格式">
        <v-md-editor
          placeholder="输出格式"
          v-model="outputFormat"
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
          v-model="hint"
          height="300px"
        ></v-md-editor>
        <!-- <el-input v-model="hint" type="textarea" placeholder="请输入提示" class="input"></el-input> -->
      </el-form-item>
      <el-form-item label="时间限制">
        <a-select
          ref="select"
          v-model:value="selectedTime"
          style="width: 200px"
          :options="timeoptions"
          @focus="focus"
        ></a-select>
        <!-- <el-input v-model="hint" type="textarea" placeholder="请输入提示" class="input"></el-input> -->
      </el-form-item>
      <el-form-item label="空间限制">
        <a-select
          ref="select"
          v-model:value="selectedMemory"
          style="width: 200px"
          :options="memoryoptions"
          @focus="focus"
        ></a-select>
        <!-- <el-input v-model="hint" type="textarea" placeholder="请输入提示" class="input"></el-input> -->
      </el-form-item>
      <el-form-item label="来源">
        <el-input
          :autosize="{ minRows: 1, maxRows: 5 }"
          v-model="source"
          type="textarea"
          placeholder="来源"
          class="input"
        ></el-input>
      </el-form-item>
      <el-form-item label="难度">

        <a-select
          ref="select"
          v-model:value="difficulty"
          style="width: 200px"
          :options="difficultys"
          @focus="focus"
        ></a-select>
      </el-form-item>
      <el-form-item label="算法">
        <el-input
          :autosize="{ minRows: 1, maxRows: 5 }"
          v-model="algorithm"
          type="textarea"
          placeholder="算法"
          class="input"
        ></el-input>
      </el-form-item>
      <el-form-item label="所属题库">
        <a-select
          ref="select"
          v-model:value="questionbank"
          style="width: 200px"
          :options="questionbanks"
          @focus="focus"
        ></a-select>
        <!-- <el-input v-model="hint" type="textarea" placeholder="请输入提示" class="input"></el-input> -->
      </el-form-item>
    </el-form>
    <el-button
      style="color: aliceblue; width: 80px"
      type="primary"
      @click="save"
      class="save-button"
      >保存</el-button
    >
  </div>
</template>

<script>
import { CloseOutlined } from "@ant-design/icons-vue";
import axios from "axios";
import { SERVER_URL } from "../../../js/functions/config";
import { sleep } from "@/js/functions/TimeAbout";
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
        value: "introduction",
        label: "入门",
      },
      {
        value: "easy",
        label: "简单",
      },
      {
        value: "ok",
        label: "还行",
      },
      {
        value: "common",
        label: "一般",
      },
      {
        value: "little",
        label: "小难",
      },
      {
        value: "hard",
        label: "好难",
      },
      {
        value: "disturb",
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
    return {
      difficulty: "easy",
      difficultys,
      algorithm: "",
      source: "",
      timeoptions,
      memoryoptions,
      title: "",
      background: "",
      description: "",
      inputFormat: "",
      outputFormat: "",
      selectedTime: 1000,
      selectedMemory: 64,
      examples: [{ input: "", output: "" }],
      hint: "",
      mouseOnSample: new Map(),
      questionbank: "jxust",
      questionbanks,
    };
  },
  methods: {
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
  setup() {},
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
