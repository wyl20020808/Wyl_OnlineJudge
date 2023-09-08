<template>
  <!-- <v-md-editor v-model="text" height="400px"></v-md-editor>
    <div v-html="htmlText"></div> -->

  <!-- <a-card  title="Card Title">
    <a-card-grid style="width: 25%; text-align: center">Content</a-card-grid>
    <a-card-grid style="width: 25%; text-align: center">Content</a-card-grid>
    <a-card-grid style="width: 25%; text-align: center">Content</a-card-grid>
    <a-card-grid style="width: 25%; text-align: center">Content</a-card-grid>
    <a-card-grid style="width: 25%; text-align: center">Content</a-card-grid>
    <a-card-grid style="width: 25%; text-align: center">Content</a-card-grid>
    <a-card-grid style="width: 25%; text-align: center">Content</a-card-grid>
    <a-card-grid style="width: 25%; text-align: center">Content</a-card-grid>
  </a-card> -->
  <div class="topcard">
    <a-row>
      <a-col :offset="3" style="color: white; font-size: 16px; margin-top: 5px">
        <a-breadcrumb style="color: white">
          <a-breadcrumb-item style="color: white">主页</a-breadcrumb-item>
          <a-breadcrumb-item style="color: white">题库</a-breadcrumb-item>
          <a-breadcrumb-item style="color: white">A + B</a-breadcrumb-item>
        </a-breadcrumb></a-col
      >
    </a-row>
    <a-row style="">
      <a-col
        :offset="3"
        style="
          color: white;
          font-size: 30px;
          font-weight: bold;
          margin-top: 10px;
        "
        >P0001 A + B
      </a-col>
    </a-row>
    <a-row style="margin-top: 20px">
      <a-col :offset="3">
        <a-button style="color: white" type="primary">提交答案</a-button>
      </a-col>
      <a-col
        style="
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          height: 100%;

          margin-right: 5px;
        "
        :offset="13"
      >
        <span style="color: white; display: block">提交</span>
        <span style="color: white; display: block; font-weight: bold">20</span>
      </a-col>
      <a-col>
        <a-divider
          type="vertical"
          style="height: 44px; background-color: white"
        />
      </a-col>
      <a-col
        style="
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          height: 100%;
          margin-left: 5px;
          margin-right: 5px;
        "
      >
        <span style="color: white; display: block">通过</span>
        <span style="color: white; display: block; font-weight: bold">20</span>
      </a-col>
      <a-col>
        <a-divider
          type="vertical"
          style="height: 44px; background-color: white"
        />
      </a-col>
      <a-col
        style="
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          height: 100%;
          margin-left: 5px;
          margin-right: 5px;
        "
      >
        <span style="color: white; display: block">时间限制</span>
        <span style="color: white; display: block; font-weight: bold"
          >1000ms</span
        >
      </a-col>
      <a-col>
        <a-divider
          type="vertical"
          style="height: 44px; background-color: white"
        />
      </a-col>
      <a-col
        style="
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          height: 100%;
          margin-left: 5px;
          margin-right: 5px;
        "
      >
        <span style="color: white; display: block">空间限制</span>
        <span style="color: white; display: block; font-weight: bold"
          >128MB</span
        >
      </a-col>
    </a-row>
  </div>
  <div class="center1">
    <a-row>
      <a-col style="margin-top: 30px" :span="13" :offset="3">
        <a-card>
          <a-row>
            <a-col>
              <h5 style="font-weight: bold">题目背景</h5>
              
            </a-col>
          </a-row>
          <a-row style="margin-top: 10px">
            <a-col>
              <h5 style="font-weight: bold">题目描述</h5>
            </a-col>
          </a-row>
          <a-row style="margin-top: 10px">
            <a-col>
              <h5 style="font-weight: bold">输入格式</h5>
            </a-col>
          </a-row>
          <a-row style="margin-top: 10px">
            <a-col>
              <h5 style="font-weight: bold">输出格式</h5>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import MarkdownIt from "markdown-it";
import mk from "markdown-it-katex";

export default {
  data() {
    return {
      markdownText: "# $x$ This is a LaTeX formula: $E=mc^2$",
      text: "",
    };
  },
  computed: {
    htmlText() {
      const md = new MarkdownIt();
      md.use(mk);
      return md.render(this.markdownText);
    },
  },
  computed: {
    formattedText() {
      return this.text.replace(/\n/g, '<br>');//为了体现间距，更加美观
    }
  },
  // ...
};
</script>

<style scoped>
.topcard {
  width: 100%;
  height: 150px;
  background: linear-gradient(to right, rgb(35, 37, 38), rgb(66, 67, 70));
}
.a-breadcrumb /deep/ .ant-breadcrumb-separator::before {
  color: white;
}

.center1 {
  background: rgb(239, 239, 239);
  min-height: 490px;
}
.card-grid:hover,
.card-grid:hover * {
  font-size: 1.2em;
}

p {
     line-height: 1.7;
     margin-bottom: 1em;
   }

   div br {
  line-height: 2em;
}
</style>
