<template>
  <div class="card card0">
    <h3 style="margin: 10px 0px 0px 30px">题目编辑 - P{{ problemid }}</h3>
    <el-button
      type="primary"
      style="
        color: white;
        width: 100px;
        position: relative;
        top: 20px;
        left: 30px;
      "
      @click="backQuestion"
      >返回题目</el-button
    >
    <div></div>
    <el-button
      type="danger"
      style="
        color: white;
        width: 100px;
        position: relative;
        left: 150px;
        bottom: 10px;
      "
      @click="deleteQuestion"
      >删除题目</el-button
    >
  </div>
  <div class="card card1">
    <div class="input-field" v-for="(field, index) in fields" :key="index">
      <span style="font-size: 18px; width: 90px">{{ field.label }}</span>
      <el-select
        v-if="field.valueName === 'timelimit'"
        v-model="field.value"
        class="m-2 select1"
        placeholder="Select"
        size="large"
      >
        <el-option
          v-for="item in timeoptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select 
        v-else-if="field.valueName === 'memorylimit'"
        v-model="field.value"
        class="m-2 select1"
        placeholder="Select"
        size="large"
      >
        <el-option
          v-for="item in memoryoptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-input v-else
        class="input"
        v-model="field.value"
        :autosize="{ minRows: field.minRows, maxRows: field.maxRows }"
        type="textarea"
        :placeholder="field.label"
        @input="onModify"
      />
    </div>
    <div
      class="sample"
      v-for="(sample, index) in samples"
      :key="'sample' + index"
    >
      <div class="input-field">
        <span style="font-size: 18px; width: 90px"
          >输入样例 {{ index + 1 }}</span
        >
        <el-input
          class="input"
          v-model="sample.input"
          :autosize="{ minRows: 2, maxRows: 4 }"
          type="textarea"
          :placeholder="'输入样例 ' + (index + 1)"
          @input="onModify"
        />
      </div>
      <div class="input-field">
        <span style="position: relative; font-size: 18px; width: 90px"
          >输出样例 {{ index + 1 }}</span
        >
        <el-input
          class="input"
          v-model="sample.output"
          :autosize="{ minRows: 2, maxRows: 4 }"
          type="textarea"
          :placeholder="'输出样例 ' + (index + 1)"
          @input="onModify"
        />
      </div>
    </div>
    <div
      class="sampleButton"
      style="position: relative; left: 1080px; bottom: 150px"
    >
      <div style="margin-bottom: 20px">
        <el-button type="primary" style="color: white" @click="addSample"
          >增加样例</el-button
        >
      </div>
      <el-button
        type="danger"
        style="color: white"
        @click="removeSample"
        v-if="samples.length > 0"
        >减少样例</el-button
      >
    </div>

    <div>
      <el-button
        type="primary"
        style="
          position: relative;
          left: 500px;
          bottom: 40px;
          width: 200px;
          color: white;
        "
        @click="saveProblemInfo"
        >保存</el-button
      >
    </div>
 
  </div>
</template>

<script>
import router from "@/router/router";
import { ref } from "vue";
import axios from "axios";
import { sleep } from "@/js/functions/TimeAbout";
export default {
  props: ["problemid"],
  methods: {
    deleteQuestion(){
        axios
        .post(`http://localhost:8088/problem/delete/problem`,{problemid: this.problemid} )
        .then((response) => {
          this.$store.dispatch("notice", {
            title: "删除成功！",
            message: "",
            type: "success",
          });
          sleep(500);
          router.push({name:"problems"})
        }
        )
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
    onModify() {
      this.isModified = true;
    },
    beforeUnload(e) {
      if (this.isModified) {
        e.preventDefault();
        e.returnValue = "";
      }
    },
    saveProblemInfo() {
      let problemcontent = {
        problemid: this.problemid,
      };
      let problemsample = [];
      this.fields.forEach((field) => {
        problemcontent[field.valueName] = field.value;
      });
      this.samples.forEach((sample) => {
        problemsample.push({
          problemid: this.problemid,
          input: sample.input,
          output: sample.output,
        });
      });
      let problem = {
        problemcontent: problemcontent,
        problemsample: problemsample,
      };
      axios
        .post(`http://localhost:8088/problem/update/problem`, problem)
        .then((response) => {
          this.$store.dispatch("notice", {
            title: "保存成功！",
            message: "",
            type: "success",
          });
          this.isModified = false;
        })
        .catch((error) => {
          this.$store.dispatch("notice", {
            title: "保存失败！",
            message: "服务器异常" + error,
            type: "error",
          });
        });
    },
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
    for (let i = 0; i < 10; i++) {
      memoryoptions.push({
        value: 2 ** i,
        label: `${2 ** i}MB`,
      });
    }

    let fields = ref([
      {
        valueName: "title",
        label: "题目名称",
        value: "",
        minRows: 1,
        maxRows: 4,
      },
      {
        valueName: "background",
        label: "题目背景",
        value: "",
        minRows: 10,
        maxRows: 15,
      },
      {
        valueName: "description",
        label: "题目描述",
        value: "",
        minRows: 10,
        maxRows: 15,
      },
      {
        valueName: "inputformat",
        label: "输入格式",
        value: "",
        minRows: 10,
        maxRows: 15,
      },
      {
        valueName: "outputformat",
        label: "输出格式",
        value: "",
        minRows: 10,
        maxRows: 15,
      },
      {
        valueName: "hint",
        label: "数据范围",
        value: "",
        minRows: 10,
        maxRows: 15,
      },
      {
        valueName: "timelimit",
        label: "时间限制",
        value: "",
        minRows: 1,
        maxRows: 5,
      },
      {
        valueName: "memorylimit",
        label: "空间限制",
        value: "",
        minRows: 1,
        maxRows: 5,
      },
      {
        valueName: "source",
        label: "来源",
        value: "",
        minRows: 1,
        maxRows: 5,
      },
      {
        valueName: "difficulty",
        label: "难度",
        value: "",
        minRows: 1,
        maxRows: 5,
      },
      {
        valueName: "algorithm",
        label: "算法",
        value: "",
        minRows: 1,
        maxRows: 5,
      },
    ]);
    let samples = ref([]);
    return {
      memoryoptions,
      timeoptions,
      isModified: false,
      probleminfo: {},
      problemid: this.problemid,
      fields,
      samples,
      addSample() {
        samples.value.push({ input: "", output: "" });
      },
      removeSample() {
        samples.value.pop();
      },
    };
  },
  created() {
    axios
      .get(`http://localhost:8088/problem/query/probleminfo/${this.problemid}`)
      .then((response) => {
        this.probleminfo = response.data;
        this.fields.forEach((field) => {
          field.value = this.probleminfo.problemcontent[field.valueName];
        });
        //放到样例里面
        this.probleminfo.problemsample.forEach((sample) => {
          this.samples.push({ input: sample.input, output: sample.output });
        });
      })
      .catch((error) => {});
  },
  beforeRouteLeave(to, from, next) {
    if (this.isModified) {
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

<style scoped>
.input-field {
  display: flex;
  position: relative;
  left: 30px;
  margin-top: 30px; /* 添加间隔 */
}
.select1{
  width: 70%;
  font-size: 18px;
  position: relative;
  left: 90px;
}
.input {
  width: 70%;
  font-size: 18px;
  position: relative;
  left: 100px;
}
.card1 {
  width: 80%;
  position: relative;
  left: 150px;
  min-height: 200px; /* 设置最小高度 */
  height: auto; /* 自动调整大小以适应内容 */
}
.card0 {
  display: flex;
  width: 80%;
  position: relative;
  left: 150px;
  min-height: 100px; /* 设置最小高度 */
  height: auto; /* 自动调整大小以适应内容 */
  margin-bottom: 30px;
  margin-top: 20px;
}
</style>
