<template>
  <a-row>
    <a-col>
      <a-row>
        <a-col>
          <div
            style="
              background-color: white;
              padding: 15px;
              width: 1140px;
              border-bottom: 1px solid rgb(217, 216, 216);
            "
          >
            <a-row style="width: 100%" type="flex" justify="space-between">
              <a-col><h4>代码编辑器</h4></a-col
              ><a-col>
                <a-select
                  ref="select2"
                  v-model:value="selectedTheme"
                  @change="changeTheme"
                  :options="themes"
                  @focus="focus"
                  style="width: 200px;margin-right: 20px;"
                >
                </a-select>
                <a-select
                  ref="select"
                  v-model:value="selectedLanguage"
                  style="width: 200px"
                  :options="languages"
                  @focus="focus"
                ></a-select
              ></a-col>
            </a-row>
          </div>
          <!-- <div  style="position: absolute;bottom: 0px;;width: 1140px;background-color: rgb(255, 255, 255);height: 5px;"></div> -->
        </a-col>
      </a-row>
      <a-row
        ><a-col>
          <div :style="editorStyle" ref="editor">
            <div
              v-if="showAutocomplete && items.length > 0"
              :style="{
                position: 'absolute',
                left: `${coords.left}px`,
                top: `${coords.top + 20}px`,
                zIndex: 1001,
              }"
            >
              <select
                v-model="selected"
                @change="onSelect"
                size="5"
                style="width: 200px"
              >
                <option v-for="item in items" :key="item" :value="item">
                  {{ item }}
                </option>
              </select>
            </div>
          </div>
        </a-col></a-row
      >
      <a-row>
        <a-col>
          <div
            style="
              background-color: white;
              padding: 15px;
              width: 1140px;
              border-bottom: 1px solid rgb(217, 216, 216);
              border-top: 1px solid rgb(217, 216, 216);
            "
          >
            <a-row style="width: 100%">
              <a-col :offset="18">
                <el-button
                  @click="runCode"
                  style="height: 40px; font-size: 16px; margin-left: 0px"
                  round
                  ><SendOutlined style="margin-right: 5px" />运行代码</el-button
                >
              </a-col>
              <a-col>
                <el-button
                  style="height: 40px; font-size: 16px; margin-left: 30px"
                  type="success"
                  @click="submitCode"
                  round
                  ><CloudUploadOutlined
                    style="margin-right: 5px"
                  />提交代码</el-button
                ></a-col
              >
            </a-row>
          </div>
          <!-- <div  style="position: absolute;bottom: 0px;;width: 1140px;background-color: rgb(255, 255, 255);height: 5px;"></div> -->
        </a-col>
      </a-row>
      <a-row style="margin-top: 20px">
        <a-col>
          <div
            style="
              border-radius: 10px;
              width: 1140px;
              background-color: white;
              color: black;
              margin-bottom: 20px;
              position: relative;
            "
          >
            <div
              style="
                position: absolute;
                top: 6px;
                left: 0;
                height: 60px;
                width: 100%;
                background-color: rgb(255, 255, 255);
                border-bottom: 1px solid rgb(227, 226, 226);
              "
            ></div>
            <a-row>
              <a-col style="font-size: 22px; padding: 14px"
                >代码运行状态：
                <span v-if="codeStatus === 'upload'">uploading</span>
                <span
                  style="color: rgb(1, 110, 193)"
                  v-else-if="codeStatus === 'running'"
                >
                  <a-spin /> Running....
                </span>
                <span
                  style="color: rgb(68, 157, 0)"
                  v-else-if="codeStatus === 'finished'"
                  >Finished</span
                >
                <span :style="{ color: getColor(codeStatus) }" v-else>{{
                  codeStatus
                }}</span>
                <!-- <span v-else-if="codeStatus === 'error'">错误</span> -->
              </a-col>
            </a-row>
            <a-row
              v-if="
                !submitting &&
                codeStatus !== 'Compilation Error' &&
                codeStatus !== 'running'
              "
            >
              <a-col :span="24">
                <a-row>
                  <a-col
                    :offset="1"
                    :span="24"
                    style="
                      display: flex;
                      flex-direction: column;
                      margin-top: 30px;
                    "
                    ><label style="font-size: 18px" for="name">输入</label>
                    <a-textarea
                      style="
                        width: 90%;
                        font-size: 16px;
                        font-family: monospace; /* 使用等宽字体 */
                        letter-spacing: 1px; /* 增加字母之间的间距 */
                      "
                      v-model:value="input"
                      auto-size
                  /></a-col>
                </a-row>
                <a-row>
                  <a-col
                    :offset="1"
                    :span="24"
                    style="
                      display: flex;
                      flex-direction: column;
                      margin-top: 20px;
                      margin-bottom: 30px;
                    "
                    ><label style="font-size: 18px" for="name">输出</label>
                    <a-textarea
                      style="width: 90%; font-size: 16px"
                      v-model:value="output"
                      auto-size
                      readonly
                  /></a-col>
                </a-row>
                <a-row v-if="judgeData">
                  <a-col
                    :offset="1"
                    style="position: relative; bottom: 20px; font-size: 16px"
                  >
                    运行时间：{{ judgeData.time ? judgeData.time * 1000 : "0" }}
                    ms
                  </a-col>
                </a-row>
              </a-col>
            </a-row>
            <!-- 提交状态 -->
            <a-row v-if="submitting">
              <a-col style="margin-left: 25px" class="container">
                <div
                  class="box"
                  v-for="(result, index) in judgeInfo"
                  :key="index"
                >
                  <span
                    style="font-size: 30px; position: relative; bottom: 0px"
                    class="tick"
                    v-if="result.judgestate && result.judgestate === 'Accepted'"
                    >√</span
                  >
                  <span
                    style="font-size: 50px; position: relative; bottom: 5px"
                    class="cross"
                    v-else-if="result.judgestate"
                    >×</span
                  >
                </div>
              </a-col>
            </a-row>
            <a-row v-if="codeStatus === 'Compilation Error'">
              <a-col
                :offset="1"
                :span="22"
                style="
                  display: flex;
                  flex-direction: column;
                  margin-top: 10px;
                  margin-bottom: 20px;
                "
                ><label style="font-size: 18px" for="name">编译器输出</label>
                <a-typography-paragraph>
                  <pre style="font-size: 14px">{{
                    decodeBase64(compileoutput)
                  }}</pre>
                </a-typography-paragraph>
              </a-col>
            </a-row>
          </div>
        </a-col>
      </a-row>
    </a-col>
  </a-row>
</template>

<script setup>
import {
  onMounted,
  ref,
  computed,
  nextTick,
  onUnmounted,
  watch,
  defineProps,
  reactive,
} from "vue";
import {
  FullscreenOutlined,
  FullscreenExitOutlined,
  CloudUploadOutlined,
  SendOutlined,
} from "@ant-design/icons-vue";
import axios from "axios";
import { getNowTime } from "@/js/functions/TimeAbout";
import { useStore } from "vuex";
import { JUDGE_URL, SERVER_URL } from "@/js/functions/config";
import { getMatchingItems } from "@/js/Editor/autoComplete";
import CodeMirror from "codemirror";
import { languages } from "@/js/values/value";
import "codemirror/lib/codemirror.css";
import "codemirror/mode/clike/clike.js";
import "codemirror/addon/display/fullscreen.js";
import "codemirror/addon/display/fullscreen.css";
import "codemirror/addon/hint/show-hint.js";
import "codemirror/addon/hint/show-hint.css";
import "codemirror/addon/hint/anyword-hint.js";
import "codemirror/addon/edit/closebrackets.js";
import "codemirror/addon/search/search.js";
import "codemirror/addon/dialog/dialog.js";
import "codemirror/addon/dialog/dialog.css";
import "codemirror/addon/search/searchcursor.js";
import "codemirror/addon/search/jump-to-line.js";
import "codemirror/addon/selection/active-line.js";

import "codemirror/theme/3024-day.css";
import "codemirror/theme/3024-night.css";
import "codemirror/theme/abcdef.css";
import "codemirror/theme/ambiance.css";
import "codemirror/theme/base16-dark.css";
import "codemirror/theme/bespin.css";
import "codemirror/theme/base16-light.css";
import "codemirror/theme/blackboard.css";
import "codemirror/theme/cobalt.css";
import "codemirror/theme/colorforth.css";
import "codemirror/theme/dracula.css";
import "codemirror/theme/duotone-dark.css";
import "codemirror/theme/duotone-light.css";
import "codemirror/theme/eclipse.css";
import "codemirror/theme/elegant.css";
import "codemirror/theme/erlang-dark.css";
import "codemirror/theme/gruvbox-dark.css";
import "codemirror/theme/hopscotch.css";
import "codemirror/theme/icecoder.css";
import "codemirror/theme/isotope.css";
import "codemirror/theme/lesser-dark.css";
import "codemirror/theme/liquibyte.css";
import "codemirror/theme/lucario.css";
import "codemirror/theme/material.css";
import "codemirror/theme/mbo.css";
import "codemirror/theme/mdn-like.css";
import "codemirror/theme/midnight.css";
import "codemirror/theme/monokai.css";
import "codemirror/theme/neat.css";
import "codemirror/theme/neo.css";
import "codemirror/theme/night.css";
import "codemirror/theme/panda-syntax.css";
import "codemirror/theme/paraiso-dark.css";
import "codemirror/theme/paraiso-light.css";
import "codemirror/theme/pastel-on-dark.css";
import "codemirror/theme/railscasts.css";
import "codemirror/theme/rubyblue.css";
import "codemirror/theme/seti.css";
import "codemirror/theme/solarized.css";
import "codemirror/theme/the-matrix.css";
import "codemirror/theme/tomorrow-night-bright.css";
import "codemirror/theme/tomorrow-night-eighties.css";
import "codemirror/theme/twilight.css";
import "codemirror/theme/vibrant-ink.css";
import "codemirror/theme/xq-dark.css";
import "codemirror/theme/xq-light.css";
import "codemirror/theme/yeti.css";
import "codemirror/theme/yonce.css";
import "codemirror/theme/zenburn.css";

import { useRoute } from "vue-router";

const props = defineProps({
  problemsample: Array,
  problemcontent: Object,
});
let store = useStore();
// 用户定义的变量或函数
let input = ref("");
let output = ref("");
const editor = ref(null);
let cmInstance = null;
const isFullscreen = ref(false);
let resizeObserver = null;
const route = useRoute();
const showAutocomplete = ref(false);
const items = ref([]);
const selected = ref(null);
const coords = ref({ left: 0, top: 0 });
const select = ref(null);
const select2 = ref(null);
let isSelecting = false;
let selectedLanguage = ref(54);
let source_code = ref("");
let selectedTheme = ref("default");
let themes = [
  "default",
  "3024-day",
  "3024-night",
  "abcdef",
  "ambiance",
  "base16-dark",
  "bespin",
  "base16-light",
  "blackboard",
  "cobalt",
  "colorforth",
  "dracula",
  "duotone-dark",
  "duotone-light",
  "eclipse",
  "elegant",
  "erlang-dark",
  "gruvbox-dark",
  "hopscotch",
  "icecoder",
  "isotope",
  "lesser-dark",
  "liquibyte",
  "lucario",
  "material",
  "mbo",
  "mdn-like",
  "midnight",
  "monokai",
  "neat",
  "neo",
  "night",
  "panda-syntax",
  "paraiso-dark",
  "paraiso-light",
  "pastel-on-dark",
  "railscasts",
  "rubyblue",
  "seti",
  "solarized",
  "the-matrix",
  "tomorrow-night-bright",
  "tomorrow-night-eighties",
  "twilight",
  "vibrant-ink",
  "xq-dark",
  "xq-light",
  "yeti",
  "yonce",
  "zenburn",
].map((theme) => ({ label: theme, value: theme }));

onMounted(async () => {
  let isUserTyping = false;
  let typingTimer = null;
  const typingDelay = 500; // 500毫秒

  setTimeout(() => {
    // 这里是你想要在页面加载完1秒后执行的操作
    if (props.problemsample[0]) input.value = props.problemsample[0].input;
  }, 500);
  if (editor.value) {
    cmInstance = CodeMirror(editor.value, {
      lineNumbers: true,
      mode: "text/x-c++src",
      viewportMargin: Infinity,
      extraKeys: {
        Esc: function (cm) {
          showAutocomplete.value = false;
        },
        Enter: function (cm) {
          if (showAutocomplete.value) {
            onSelect();
          } else {
            // 默认的回车键行为
            cm.execCommand("newlineAndIndent");
          }
        },
        Up: function (cm) {
          if (showAutocomplete.value) {
            // Move the selection up in the dropdown
            const index = items.value.indexOf(selected.value);
            if (index > 0) {
              cm.execCommand("goLineDown");
              selected.value = items.value[index - 1];
              // Prevent the default behavior
              return CodeMirror.Pass;
            }
          }
          // If showAutocomplete.value is false, let CodeMirror handle the up key event.
          else {
            cm.execCommand("goLineUp");
          }
        },
        Down: function (cm) {
          if (showAutocomplete.value) {
            // Move the selection down in the dropdown
            const index = items.value.indexOf(selected.value);
            if (index < items.value.length - 1) {
              selected.value = items.value[index + 1];
              // Prevent the default behavior
              cm.execCommand("goLineUp");
              return CodeMirror.Pass;
            }
          }
          // If showAutocomplete.value is false, let CodeMirror handle the down key event.
          else {
            cm.execCommand("goLineDown");
          }
        },
      },
      autoCloseBrackets: true,
      styleActiveLine: true,
    });
    cmInstance.setOption("theme", "neat");
    cmInstance.on("cursorActivity", () => {
      if (!isUserTyping) {
        showAutocomplete.value = false;
      }
    });
    // 确保 DOM 已经更新
    await nextTick(); // 更新全屏图标的位置
    updateIconPosition();
    // 创建一个新的 ResizeObserver 实例并开始监听编辑器的尺寸变化
    resizeObserver = new ResizeObserver(updateIconPosition);
    resizeObserver.observe(editor.value);

    cmInstance.on("change", () => {
      isUserTyping = true;
      if (isSelecting) {
        isSelecting = false;
        return;
      }
      typingTimer = setTimeout(() => {
        isUserTyping = false;
      }, typingDelay);
      const cursor = cmInstance.getCursor();
      const token = cmInstance.getTokenAt(cursor);
      const currentWord = token.string;
      let allTextInEditor = cmInstance.getRange({ line: 0, ch: 0 }, cursor);
      // Generate the list of matching items
      items.value = getMatchingItems(currentWord, allTextInEditor);
      // Get the cursor's coordinates
      // Get the cursor's coordinates relative to the window
      let cursorCoords = cmInstance.cursorCoords(cursor, "window");
      source_code.value = cmInstance.getValue();
      // Get the editor's coordinates
      let editorCoords = editor.value.getBoundingClientRect();

      // Adjust the cursor's coordinates
      coords.value = {
        left: cursorCoords.left - editorCoords.left,
        top: cursorCoords.top - editorCoords.top,
      };

      // Check if the current word fully matches all the autocomplete options
      // const isFullMatch = items.value.every((item) => item === currentWord);
      const isFullMatch = false;
      if (items.value.length > 0 && !isFullMatch) {
        showAutocomplete.value = true;
        selected.value = items.value[0];
      } else {
        showAutocomplete.value = false;
      }
    });
  }
});

function decodeBase64(input) {
  const state = [
    "Accepted",
    "Time Limit Exceeded",
    "Wrong Answer",
    "Runtime Error",
    "Memory Limit Exceeded",
  ];
  if (!input) {
    return "";
  }
  if (state.includes(input)) {
    return input;
  }
  try {
    const raw = atob(input);
    const rawLength = raw.length;
    const array = new Uint8Array(new ArrayBuffer(rawLength));
    for (let i = 0; i < rawLength; i++) {
      array[i] = raw.charCodeAt(i);
    }

    const textDecoder = new TextDecoder("utf-8");
    return textDecoder.decode(array);
  } catch (e) {
    console.error(e);
    return input;
  }
}
onUnmounted(() => {
  // 停止监听编辑器的尺寸变化
  if (resizeObserver) {
    resizeObserver.disconnect();
  }
});

const toggleFullscreen = () => {
  isFullscreen.value = !isFullscreen.value;
  updateIconPosition();
};

const iconStyle = ref({ position: "fixed", zIndex: 1000 });

const updateIconPosition = () => {
  const rect = editor.value.getBoundingClientRect();
  iconStyle.value.right = `${window.innerWidth - rect.right + 10}px`;
  iconStyle.value.bottom = `${window.innerHeight - rect.bottom + 10}px`;
};

const editorStyle = computed(() => {
  if (isFullscreen.value) {
    return {
      width: "50%",
      height: "100%",
      position: "fixed",
      right: 0,
      top: 0,
      zIndex: 1000,
    };
  } else {
    return {
      width: "1140px",
    };
  }
});

function onSelect() {
  const cursor = cmInstance.getCursor();
  const token = cmInstance.getTokenAt(cursor);

  // Replace the current word with the selected item
  cmInstance.replaceRange(
    selected.value,
    { line: cursor.line, ch: token.start },
    { line: cursor.line, ch: token.end }
  );
  isSelecting = true;
  showAutocomplete.value = false;
}
let codeStatus = ref("finished");
const judgeData = ref();
let submitting = ref(false); //正在提交
const runCode = async () => {
  source_code.value = source_code.value.trimEnd();
  if (!source_code.value) {
    store.dispatch("notice", {
      title: "编辑器里的代码不能为空！",
      message: "",
      type: "error",
    });
    return;
  }
  output.value = "";
  submitting.value = false;
  codeStatus.value = "upload";
  let formData = new FormData();
  formData.append("source_code", source_code.value);
  console.log(props.problemcontent.problemid, selectedLanguage.value);
  formData.append("problemId", parseInt(props.problemcontent.problemid));
  formData.append("languageId", parseInt(selectedLanguage.value));
  formData.append("stdin", input.value);
  codeStatus.value = "running";
  await axios //提交代码给后端
    .post(`${JUDGE_URL}/judge/judgeone`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((res) => {
      if (res.data.status.description === "Compilation Error") {
        codeStatus.value = res.data.status.description;
        compileoutput = res.data.compile_output;
      } else codeStatus.value = "finished";
      judgeData.value = res.data;

      console.log(res.data);
      output.value = decodeBase64(judgeData.value.stdout);
      nextTick(function () {
        window.scrollTo(0, document.body.scrollHeight);
      });
    })
    .catch((err) => {
      console.log(err);
    });
};
function getColor(judgestate) {
  if (judgestate === "Accepted") return "#25ad40";
  else if (judgestate === "Compilation Error") return "orange";
  else return "red";
}
const addSubmitCount = async (problemid, userid) => {
  axios
    .post(`${SERVER_URL}/problem/update/problemcontent/special`, {
      problemid: problemid,
      special: "submitcount",
    })
    .then((res) => {})
    .catch((error) => {
      console.log(error);
    });
  axios
    .post(`${SERVER_URL}/userextra/update/special`, {
      userid: userid,
      special: "submitcount",
    })
    .then((res) => {})
    .catch((error) => {
      console.log(error);
    });
};
const addAceptedCount = async (problemid, userid) => {
  await axios
    .post(`${SERVER_URL}/problem/update/problemcontent/special`, {
      problemid: problemid,
      special: "aceptedcount",
    })
    .then((res) => {})
    .catch((error) => {
      console.log(error);
    });
  await axios
    .post(`${SERVER_URL}/userextra/update/special`, {
      userid: userid,
      special: "aceptedcount",
    })
    .then((res) => {})
    .catch((error) => {
      console.log(error);
    });
};
let judgestate = "Accepted";
let compileoutput = "";
const saveJudgeInfo = async (judgedata, submittime) => {
  judgestate = "Accepted";
  let problemid = props.problemcontent.problemid;
  //保存评测信息到数据库
  addSubmitCount(problemid, JSON.parse(localStorage.getItem("user")).userid);
  let language = "";
  for (let i = 0; i < languages.length; i++) {
    if (languages[i].value === selectedLanguage.value) {
      language = languages[i].label;
      break;
    }
  }
  let judgeinfo = {
    problemid: problemid,
    userid: JSON.parse(localStorage.getItem("user")).userid,
    submittime,
    code: source_code.value,
    username: JSON.parse(localStorage.getItem("user")).username,
    language: language,
    problemname: props.problemcontent.title,
    userpicture: JSON.parse(localStorage.getItem("user")).userpicture,
  };
  const results = judgedata.data.results;
  let runtime = 0;
  let memory = 0;
  let score = 0;

  for (let i = 0; i < results.length; i++) {
    runtime = runtime + parseFloat(results[i].time) * 1000;
    memory = Math.max(memory, parseFloat(results[i].memory));
    if (results[i].status.description == "Accepted")
      score = score + 100 / results.length;
    else judgestate = results[i].status.description;
    compileoutput =
      results[i].compile_output === null
        ? results[i].status.description
        : results[i].compile_output;
  }
  judgeinfo.judgestate = judgestate;
  if (judgestate === "Accepted") {
    addAceptedCount(problemid, JSON.parse(localStorage.getItem("user")).userid);
  }

  judgeinfo.compileoutput = compileoutput;
  judgeinfo.runtime = runtime;
  judgeinfo.memory = memory;
  judgeinfo.score = score >= 99 ? 100 : score;
  judgeinfo.totaltime = runtime;
  let type = "judge";
  if (route.query.contestid) {
    //gpt教的用法
    //如果是比赛传过来的话
    judgeinfo.contestid = route.query.contestid; //比赛id
    judgeinfo.problemchar = route.query.problemchar; //题目对应的字母
    type = "contest";
  }
  let judgeid = null;
  await axios
    .post(`${SERVER_URL}/${type}/insert/judge`, judgeinfo) //插入到不同里面
    .then((response) => {
      judgeid = response.data;
    })
    .catch((error) => {
      store.dispatch("notice", {
        title: "数据保存失败！",
        message: "服务器异常" + error,
        type: "error",
      });
    });

  return;
  // 上面是保存单个信息，下面是保存每个测试点的信息
  let infolist = [];
  for (let i = 0; i < results.length; i++) {
    infolist.push({
      judgeid: judgeid,
      runtime: results[i].time,
      memory: results[i].memory,
      judgestate: results[i].status.description,
    });
  }
  if (route.query.contestid) {
    //如果是比赛传过来的话
    for (let i = 0; i < infolist.length; i++) {
      infolist[i].contestid = route.query.contestid;
      infolist[i].problemchar = route.query.problemchar; //这里貌似有问题，以后再改
    }
  }
  await axios
    .post(`${SERVER_URL}/${type}/insert/judgecontent`, infolist)
    .then((response) => {})
    .catch((error) => {
      store.dispatch("notice", {
        title: "数据保存失败！",
        message: "服务器异常" + error,
        type: "error",
      });
    });
  //
};

let judgeInfo = ref([]);
const submitCode = async () => {
  source_code.value = source_code.value.trimEnd();
  if (!source_code.value) {
    store.dispatch("notice", {
      title: "编辑器里的代码不能为空！",
      message: "",
      type: "error",
    });
    return;
  }
  judgeInfo.value = [];
  let datalength = props.problemcontent.datalength;
  for (let i = 0; i < datalength; i++) {
    judgeInfo.value.push(0);
  }
  codeStatus.value = "upload";
  let formData = new FormData();
  let time = getNowTime();
  formData.append("source_code", source_code.value);
  formData.append("problemId", parseInt(props.problemcontent.problemid));
  formData.append("languageId", parseInt(selectedLanguage.value));
  formData.append("submittime", time); //为了同步
  formData.append(
    "userid",
    parseInt(JSON.parse(localStorage.getItem("user")).userid)
  );
  let type = "judge";
  if (route.query.contestid) {
    type = "contest";
    formData.append("contestid", parseInt(route.query.contestid));
  }
  codeStatus.value = "running";
  submitting.value = true;
  // 设置interval
  let intervalId = setInterval(async () => {
    let data = new FormData();
    data.append("problemId", parseInt(props.problemcontent.problemid));
    data.append("submittime", time); //为了同步
    data.append(
      "userid",
      parseInt(JSON.parse(localStorage.getItem("user")).userid)
    );

    if (route.query.contestid) {
      type = "contest";
      data.append("contestid", parseInt(route.query.contestid));
    }
    await axios //提交代码给后端
      .post(`${JUDGE_URL}/${type}/query/judgemany`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((res) => {
        judgeInfo.value = res.data;
        while (judgeInfo.value.length < datalength) {
          judgeInfo.value.push(0);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  }, 500); // 每秒执行一次
  nextTick(function () {
    window.scrollTo(0, document.body.scrollHeight);
  });
  await axios //提交代码给后端
    .post(`${JUDGE_URL}/${type}/judgemany`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then(async (res) => {
      await saveJudgeInfo(res.data, time);
      codeStatus.value = judgestate;
      setTimeout(() => {
        // 这里是你想要在页面加载完1秒后执行的操作
        clearInterval(intervalId); // 销毁interval
      }, 2000);
      nextTick(function () {
        window.scrollTo(0, document.body.scrollHeight);
      });
    })
    .catch((err) => {
      console.log(err);
      clearInterval(intervalId); // 销毁interval
    });
};

let changeTheme = function () {
  cmInstance.setOption("theme", selectedTheme.value);
};
</script>

<style>
@import "codemirror/lib/codemirror.css";
@import "codemirror/addon/display/fullscreen.css";
@import "codemirror/addon/hint/show-hint.css";
@import "codemirror/addon/dialog/dialog.css";

.CodeMirror {
  font-family: "Consolas", monospace;
  height: 400px;
}

.CodeMirror-activeline-background {
  background: #e8f2ff;
}

.box {
  width: 30px;
  height: 30px;
  border: 1px solid rgb(208, 208, 208);
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 5px;
}
.container {
  display: flex;
  flex-wrap: wrap;
}

.tick {
  color: green;
}

.cross {
  color: red;
}
</style>
