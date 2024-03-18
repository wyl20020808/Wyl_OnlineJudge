<template>
  <!-- <v-md-editor v-model="text" height="400px"></v-md-editor>
    -->

  <div class="topcard">
    <a-row>
      <a-col :offset="3" style="color: white; font-size: 16px; margin-top: 5px">
        <a-breadcrumb style="color: white">
          <a-breadcrumb-item
            class="hoverable"
            @click="jump('/')"
            style="color: white"
            >主页</a-breadcrumb-item
          >
          <a-breadcrumb-item
            class="hoverable"
            @click="back"
            style="color: white"
            >比赛2</a-breadcrumb-item
          >
          <a-breadcrumb-item style="color: white">{{
            problemcontent.title
          }}</a-breadcrumb-item>
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
        >[{{ problemchar }}] {{ problemcontent.title }}
      </a-col>
    </a-row>
    <a-row class="nowrap-row" style="margin-top: 20px">
      <a-col :offset="3">
        <a-button @click="submitProblem" style="color: white" type="primary"
          >提交答案</a-button
        >
      </a-col>
      <a-col>
        <a-button
          @click="editProblem"
          style="color: white; margin-left: 10px"
          ghost
          >编辑题目</a-button
        >
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
        :offset="12"
      >
        <span style="color: white; display: block">提交</span>
        <span style="color: white; display: block; font-weight: bold">{{
          problemcontent.submitcount
        }}</span>
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
        <span style="color: white; display: block; font-weight: bold">{{
          problemcontent.aceptedcount
        }}</span>
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
          >{{ problemcontent.timelimit }}ms</span
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
          >{{ problemcontent.memorylimit }}MB</span
        >
      </a-col>
    </a-row>
  </div>
  <div class="center1">
    <a-row>
      <a-col style="margin-top: 30px" :span="13" :offset="3">
        <a-card>
          <a-row v-if="problemcontent.background !== '无'">
            <a-col>
              <h5 style="font-weight: bold">题目背景</h5>
              <div
                style="font-size: 16px"
                class=""
                v-html="
                  parsedDescription(formattedText(problemcontent.background))
                "
              ></div>
            </a-col>
          </a-row>
          <a-row
            :style="{
              'margin-top': problemcontent.background !== '无' ? '10px' : '0px',
            }"
          >
            <a-col>
              <h5 style="font-weight: bold">题目描述</h5>

              <div
                style="font-size: 16px"
                class=""
                v-html="
                  parsedDescription(formattedText(problemcontent.description))
                "
              ></div>
            </a-col>
          </a-row>
          <a-row style="margin-top: 10px">
            <a-col>
              <h5 style="font-weight: bold">输入格式</h5>

              <div
                style="font-size: 16px"
                class=""
                v-html="
                  parsedDescription(formattedText(problemcontent.inputformat))
                "
              ></div>
            </a-col>
          </a-row>
          <a-row style="margin-top: 10px">
            <a-col>
              <h5 style="font-weight: bold">输出格式</h5>

              <div
                style="font-size: 16px"
                class=""
                v-html="
                  parsedDescription(formattedText(problemcontent.outputformat))
                "
              ></div>
            </a-col>
          </a-row>
          <a-row style="margin-top: 10px">
            <a-col>
              <h5 style="font-weight: bold">输入输出样例</h5>
            </a-col>
          </a-row>
          <a-row
            v-for="(sample, index) in problemSample"
            :key="sample.input + sample.output"
            style="margin-top: 10px"
          >
            <a-col :span="11">
              <a-row type="flex" justify="space-between">
                <a-col>
                  <h6 style="font-weight: bold">输入 #{{ index + 1 }}</h6>
                </a-col>
                <a-col>
                  <a-tooltip placement="top">
                    <template #title>
                      <span>复制</span>
                    </template>
                    <CopyTwoTone
                      @click="copyText(sample.input)"
                      class="hoverable"
                    /> </a-tooltip
                ></a-col>
              </a-row>
              <a-typography-paragraph>
                <pre style="font-size: 16px">{{ sample.input }}</pre>
              </a-typography-paragraph>
            </a-col>
            <a-col style="margin-left: 20px" :span="11">
              <a-row type="flex" justify="space-between">
                <a-col>
                  <h6 style="font-weight: bold">输出 #{{ index + 1 }}</h6>
                </a-col>
                <a-col>
                  <a-tooltip placement="top">
                    <template #title>
                      <span>复制</span>
                    </template>
                    <CopyTwoTone
                      @click="copyText(sample.output)"
                      class="hoverable"
                    /> </a-tooltip
                ></a-col>
              </a-row>
              <a-typography-paragraph>
                <pre style="font-size: 16px">{{ sample.output }}</pre>
              </a-typography-paragraph>
            </a-col>
          </a-row>
          <a-row style="margin-top: 10px">
            <a-col>
              <h5 style="font-weight: bold">说明/提示</h5>

              <div
                style="font-size: 16px"
                v-html="parsedDescription(formattedText(problemcontent.hint))"
              ></div>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col style="margin-top: 30px; margin-left: 20px" :span="6">
        <a-row>
          <a-col :span="24">
            <a-card>
              <a-row type="flex" align="middle" justify="space-between">
                <a-col style="font-size: 16px">题目来源</a-col>
                <a-col :span="8">
                  <a-row
                    class="hoverable"
                    type="flex"
                    align="middle"
                    justify="space-between"
                  >
                    <a-col>
                      <img
                        :src="userinfo.userpicture"
                        class="avatar"
                        alt="Avatar"
                      />
                    </a-col>
                    <a-col style="font-size: 16px; color: #2c93d7">
                      {{ userinfo.username }}
                    </a-col>
                  </a-row>
                </a-col>
              </a-row>
              <a-row
                style="margin-top: 10px"
                type="flex"
                align="middle"
                justify="space-between"
              >
                <a-col style="font-size: 16px">难度</a-col>
                <a-col style="font-size: 16px; color: #2c93d7">
                  {{ problemcontent.difficulty }}
                </a-col>
              </a-row>
              <a-row
                style="margin-top: 15px"
                type="flex"
                align="middle"
                justify="space-between"
              >
                <a-col style="font-size: 16px">历史分数</a-col>
                <a-col style="font-size: 16px; color: #2c93d7"> 无 </a-col>
              </a-row>
              <a-row
                style="margin-top: 15px"
                type="flex"
                align="middle"
                class="hoverable"
              >
                <a-col style="font-size: 16px">
                  <PieChartTwoTone style="font-size: 24px"
                /></a-col>
                <a-col
                  style="font-size: 16px; margin-left: 5px; color: #2c93d7"
                >
                  提交记录</a-col
                >
              </a-row>
            </a-card>
          </a-col>
        </a-row>
        <a-row style="margin-top: 20px">
          <a-col :span="24">
            <a-card>
              <a-row>
                <h5>标签</h5>
              </a-row>
              <a-row style="margin-top: 5px">
                <a-col :span="24">
                  <a-tag
                    style="font-size: 14px"
                    v-for="algorithm in algorithm"
                    :key="algorithm"
                    color="green"
                    >{{ algorithm }}</a-tag
                  >
                </a-col>
              </a-row>
            </a-card>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import MarkdownIt from "markdown-it";
import "markdown-it-texmath/css/texmath.css";
import mk from "markdown-it-katex";
import "katex/dist/katex.min.css";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
import { message } from "ant-design-vue";
import { CopyTwoTone, PieChartTwoTone } from "@ant-design/icons-vue";

import router from "@/router/router";
export default {
  components: {
    CopyTwoTone,
    PieChartTwoTone,
  },
  data() {
    return {
      markdownText: "# $x$ This is a LaTeX formula: $E=mc^2$",
      text: "",
      problemcontent: "",
      problemSample: [],
      md: new MarkdownIt({ html: true }).use(mk),
      userinfo: {},
      algorithm: [],
      problemchar:this.$route.query.problemchar,
    };
  },
  methods: {
    back() {
      router.go(-1);
    },
    editProblem() {
      router.push({
        name: "problemedit",
        params: { problemid: this.problemcontent.problemid },
      });
    },
    jump(path) {
      router.push({ path: path });
    },
    copyText(text) {
      if (navigator.clipboard) {
        // 使用新的 Clipboard API
        navigator.clipboard
          .writeText(text)
          .then(() => {
            message.success("复制成功", 3);
          })
          .catch(() => {
            message.error("复制失败", 3);
          });
      } else {
        // 使用旧的 document.execCommand 方法
        var textarea = document.createElement("textarea");
        textarea.text = text;
        document.body.appendChild(textarea);
        textarea.select();
        try {
          document.execCommand("copy");
          message.success("复制成功", 3);
        } catch (err) {
          message.error("复制失败", 3);
        }
        document.body.removeChild(textarea);
      }
    },
    parsedDescription(content) {
      return this.md.render(String(content));
    },
    formattedText(text) {
      if (typeof text !== "string") {
        text = String(text);
      }
      return text.replace(/\n/g, "<br>"); //为了体现间距，更加美观
    },
    submitProblem() {
       
      router.push({
        path: "/contest/submit",
        query: {
          problemid: this.problemcontent.problemid,
          problemname: this.problemcontent.title,
          contestid: this.$route.query.contestid,
          problemchar: this.problemchar,
        },
      });
    },
  },
  computed: {
    htmlText(text) {
      const md = new MarkdownIt();
      md.use(mk);
      return md.render(String(text));
    },
  },
  async created() {
    await axios
      .get(`${SERVER_URL}/problem/query/${this.$route.query.problemid}`)
      .then((res) => {
        this.problemcontent = res.data;
        // while(String(this.problemcontent.problemid).length < 4)this.problemcontent.problemid = '0' + String(this.problemcontent.problemid);
        this.algorithm = res.data.algorithm.trimEnd().split(" ");
      })
      .catch((err) => {
        console.log(err);
      });

    await axios
      .get(
        `${SERVER_URL}/problem/query/problemsample/${this.$route.query.problemid}`
      )
      .then((response) => {
        this.problemSample = response.data;
      })
      .catch((error) => {
        console.log(error);
      });

    await axios
      .post(`${SERVER_URL}/user/query`, {
        userid: this.problemcontent.source,
      })
      .then((response) => {
        this.userinfo = response.data;
      })
      .catch((error) => {
        console.log(error);
      });
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

/* div br {
  line-height: 1em;
} */
.hoverable {
  transition: color 0.3s ease, text-decoration 0.3s ease;
  cursor: pointer;
}

.hoverable:hover {
  filter: brightness(1.3);
}

.nowrap-row {
  display: flex;
  flex-wrap: nowrap;
}

.avatar {
  border-radius: 50%;
  width: 40px;
  height: 40px;
  outline: none;
}
</style>
