<template>
  <a-row style="margin-top: 20px; align-items: center; justify-content: center">
    <a-col :span="4">
      <el-card style="width: 100%; min-height: 785px">
        <el-scrollbar style="width: 100%; min-height: 100%">
          <a-list
            v-if="data.length > 0"
            item-layout="horizontal"
            :data-source="data"
          >
            <template #renderItem="{ item }">
              <a-list-item
                @click="handleChoose(item.userid)"
                class="hover-shadow"
                :class="{ choose: chooseTarget === item.userid }"
                v-on:mouseover="isHovered = true"
                v-on:mouseout="isHovered = false"
              >
                <a-list-item-meta
                  style="
                    white-space: nowrap;
                    overflow: hidden;
                    text-overflow: ellipsis;
                  "
                  :description="item.message"
                >
                  <template #title>
                    <div href="https://www.antdv.com/">{{ item.name }}</div>
                  </template>
                  <template #avatar>
                    <a-avatar
                      style="border-radius: 50%; width: 50px; height: 50px"
                      :src="item.picture"
                    />
                  </template>
                </a-list-item-meta>
                <a-badge :count="item.unread" class="item"></a-badge>
              </a-list-item>
            </template>
          </a-list>
          <div v-else>
            <a-empty />
          </div>
        </el-scrollbar>
      </el-card>
    </a-col>

    <a-col :span="15" style="margin-top: 20px">
      <a-row>
        <el-scrollbar ref="scrollbar" height="600px" style="min-width: 101%">
          <el-card style="min-height: 600px; min-width: 100%">
            <div
              style="text-align: center; font-size: 20px; width: 100%; top: 0"
            >
              {{ targetName }}
            </div>
            <div
              @click="loadMoreMessages"
              style="
                padding: 10px;
                text-align: center;
                cursor: pointer;
                color: #2234;
              "
            >
              <template v-if="loading">
                <a-spin />
                <span style="color: gray"
                  >信息加载中</span
                >
              </template>
              <template v-else> 查看更多消息 </template>
            </div>

            <div
              v-for="(message, index) in messages"
              :key="message.time"
              class="message-container"
            >
              <div v-if="shouldShowTime(index)" class="message-time">
                {{ formatTime(message.sendtime) }}
              </div>
              <div
                class="message-content"
                :class="{
                  'message-right': message.sendername === userinfo.nickname,
                  'message-left': message.sendername !== userinfo.nickname,
                }"
              >
                <a-comment
                  :author="message.sendername"
                  :avatar="message.senderpicture"
                >
                  <template #content>
                    <a-card>
                    <v-md-preview
                      :text="parsedDescription(message.message)"
                    ></v-md-preview>
                  </a-card>
                    <!-- <el-card style="max-width: 400px; word-wrap: break-word">
                      {{ message.message }}
                    </el-card> -->
                  </template>
                </a-comment>
              </div>
            </div>
            <div
              v-if="isToAI"
              style="
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100%; /* 确保子元素的高度与父元素一致 */
              "
            >
              <a-spin />
              <span style="color: gray; margin-bottom: 7px; margin-left: 5px"
                >AI回复中，需要回复的信息越长，加载时间越长哦~</span
              >
            </div>
          </el-card>
        </el-scrollbar>
      </a-row>
      <a-row style="margin-top: 0px; width: 105%">
        <a-col :span="23"
          ><a-textarea
            style="width: 100%; border-radius: 0, 0, 0, 0"
            v-model:value="messageInput"
            placeholder="按回车键发送消息"
            :auto-size="{ minRows: 8, maxRows: 20 }"
            @keydown.enter.prevent="checkTrophy"
            @keydown.ctrl.enter="insertNewLine"
            show-count
            :maxlength="114514"
            :addonAfter="{ text: '发送', onClick: sendMessage }"
        /></a-col>
        <!-- <a-col><a-button @click="sendMessage">发送</a-button></a-col> -->
      </a-row>
    </a-col>
  </a-row>

  <a-modal v-model:open="open" title="温馨提示" @ok="handleOk">
    <p>本次预估花费超过{{ preCost }} 奖杯，确认要和AI对话吗？</p>
  </a-modal>
</template>
<script setup>
import MarkdownIt from "markdown-it";
import "markdown-it-texmath/css/texmath.css";
import mk from "markdown-it-katex";
import { onBeforeUnmount, watchEffect } from "vue";
import { ref, onMounted, nextTick } from "vue";
import { onBeforeRouteLeave } from "vue-router";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
import { getNowTime } from "@/js/functions/TimeAbout";
import { defineExpose } from "vue";

import { computed, watch } from "vue";
import { useStore } from "vuex";

const store = useStore();

const message = computed(() => store.getters.getNewMessage.get("message"));
const confirmSendToAi = ref(false);
watch(message, async (newVal, oldValue) => {
  if (newVal.message) {
    await addMessage(newVal);
    console.log("希望加入信息", newVal.message);
  }
});
const open = ref(false);
const showModal = () => {
  open.value = true;
};
const handleOk = async () => {
  await sendMessage();
  isToAI.value = true;
  open.value = false;
};
let scrollbar = ref(null);
let isHovered = ref(false);
let messageInput = ref("");
let data = ref([]);
let messages = ref([]);
let allMessage = ref({});
let userinfo = ref(JSON.parse(localStorage.getItem("user")));
let chooseTarget = ref("");
let getIndex = ref(new Map());
let targetName = ref("");
let source = ref(null);
let timer = ref(null);
let MessageCount = ref(new Map()); // 每个对话选择加载了多少条
const md = new MarkdownIt({ html: true }).use(mk);

const cost = ref({
  input: "not",
  output: "",
  model: "gpt-3.5",
});
function calculatePriceInCNY(
  inputString,
  outputString,
  model = "gpt-3.5",
  exchangeRate = 7.27
) {
  // Function to estimate the number of tokens based on character type
  const estimateTokens = (text) => {
    let tokenCount = 0;
    console.log("传进来的参数是：", text, text.length, text.charAt(0));
    for (let i = 0; i < text.length; i++) {
      let char = text.charAt(i);
      // console.log(tokenCount,char,'需要的奖杯数',char)
      if (char.match(/[\u4e00-\u9fff]/)) {
        tokenCount += 2; // Chinese characters count as 2 tokens
      } else if (char.match(/[a-zA-Z0-9]/)) {
        tokenCount += 0.5; // English letters and digits count as 0.5 tokens
      } else {
        tokenCount += 1; // Punctuation and others count as 1 token
      }
    }

    return Math.ceil(tokenCount);
  };

  const inputTokens = estimateTokens(inputString);

  const outputTokens = estimateTokens(outputString);

  let pricePerInputToken, pricePerOutputToken;

  if (model === "gpt-4") {
    pricePerInputToken = 0.03 / 1000;
    pricePerOutputToken = 0.06 / 1000;
  } else {
    pricePerInputToken = 0.0015 / 1000;
    pricePerOutputToken = 0.002 / 1000;
  }

  const inputCostUSD = inputTokens * pricePerInputToken;
  const outputCostUSD = outputTokens * pricePerOutputToken;
  const totalCostUSD = inputCostUSD + outputCostUSD;
  // console.log(inputTokens,"要消耗多少个",pricePerInputToken,pricePerOutputToken)
  const totalCostCNY = totalCostUSD * exchangeRate;
  const totalCostJiao = Math.ceil(totalCostCNY * 10);

  return totalCostJiao;
}
const preCost = ref(0);
const isToAI = ref(false);
const checkTrophy = async () => {
  let target = data.value[getIndex.value.get(chooseTarget.value)].userid;
  if (target === 99999 || target === 88888) {
    
    cost.value.input = messageInput.value; //设置输入大小
    if (target === 99999) cost.value.model = "gpt-4";
  } else {
    sendMessage();
    return;
  }
  preCost.value = calculatePriceInCNY(
    cost.value.input,
    cost.value.output,
    cost.value.model
  );
  try {
    const response = await axios.get(`${SERVER_URL}/userextra/query/id`, {
      params: {
        userid: JSON.parse(localStorage.getItem("user")).userid,
      },
    });

    if (response.data.trophy < preCost.value) {
      store.dispatch("notice", {
        title: "奖杯不足",
        message: `您的奖杯数量不足，您的输入需要花费${preCost.cost}个奖杯，但是您只有${response.data.trophy}个奖杯`,
        type: "success",
      });
    } else {
      showModal(); //打开确认框
    }
  } catch (error) {}
};
function parsedDescription(content) {
  return md.render(String(content));
}
const handleRead = async (message) => {
  await axios
    .post(`${SERVER_URL}/message/haveread`, message) // 标记为已读
    .then((res) => {})
    .catch((err) => {
      console.log(err);
    });
};

const handleLoad = async (message) => {
  await axios
    .post(`${SERVER_URL}/message/haveload`, message) // 设置一下已经加载过了
    .then((res) => {})
    .catch((err) => {
      console.log(err);
    });
};

const handleChoose = async (userid) => {
  chooseTarget.value = userid;
  data.value[getIndex.value.get(userid)].unread = 0;
  await axios
    .get(`${SERVER_URL}/message/query`, {
      // 查询两个人的发消息记录
      params: {
        sender: userinfo.value.userid,
        receiver: userid,
      },
    })
    .then(async (res) => {
      let data2 = res.data;
      messages.value = [];
      let initialLoad = MessageCount.value.get(userid) || 10;
      let len = data2.length;
      for (let i = Math.max(0, len - initialLoad); i < len; i++) {
        //从最新发送的逻辑开始
        messages.value.push(data2[i]);
        await handleRead(data2[i]);
        await handleLoad(data2[i]);
      }
      MessageCount.value.set(userid, initialLoad);
      targetName.value = data.value[getIndex.value.get(userid)].name;

      scrollToBottom();
    })
    .catch((err) => {
      console.log(err);
    });
};

let loading = ref(false); // 新增loading状态

const loadMoreMessages = async () => {
  loading.value = true; // 开始加载动画
  let currentLoad = MessageCount.value.get(chooseTarget.value) || 10;
  let newLoad = currentLoad + 10;
  MessageCount.value.set(chooseTarget.value, newLoad);
  const oldScrollHeight = scrollbar.value.scrollHeight;
  // 模拟加载时间
  await new Promise((resolve) => setTimeout(resolve, 1000));

  await axios
    .get(`${SERVER_URL}/message/query`, {
      params: {
        sender: userinfo.value.userid,
        receiver: chooseTarget.value,
        limit: newLoad,
      },
    })
    .then(async (res) => {
      let data2 = res.data;
      messages.value = [];
      let len = data2.length;
      for (let i = Math.max(0, len - newLoad); i < len; i++) {
        messages.value.push(data2[i]);
        await handleRead(data2[i]);
        await handleLoad(data2[i]);
      }
      nextTick(() => {
        const newScrollHeight = scrollbar.value.scrollHeight;
        scrollbar.value.scrollTop = newScrollHeight - oldScrollHeight - 250; // 保持滚动位置
      });
      MessageCount.value.set(chooseTarget.value, newLoad);
      loading.value = false; // 停止加载动画
    })
    .catch((err) => {
      console.log(err);
      loading.value = false; // 出错也停止加载动画
    });
};

const scrollToBottom = () => {
  nextTick(() => {
    if (scrollbar.value) {
      scrollbar.value.scrollTop = scrollbar.value.scrollHeight;
    }
  });
};

const sendMessage = async () => {
  if (event.ctrlKey) {
    return;
  }

  let now = getNowTime();
  let senderpicture = userinfo.value.userpicture;

  let target = data.value[getIndex.value.get(chooseTarget.value)].userid;

  let message = {
    sender: userinfo.value.userid,
    receiver: target,
    message: messageInput.value,
    sendtime: now,
    sendername: userinfo.value.nickname,
    senderpicture,
  };
  const res = axios
    .post(`${SERVER_URL}/message/send`, message)
    .then((res) => {})
    .catch((err) => {
      console.log(err);
    });
  messages.value.push(message);
  data.value[getIndex.value.get(chooseTarget.value)].message =
    messageInput.value;
  scrollToBottom();
  messageInput.value = "";
};

const index = () => {
  return getIndex.value.get(chooseTarget.value);
};

const insertNewLine = () => {
  event.preventDefault();
  messageInput.value += "\n";
};

const shouldShowTime = (index) => {
  if (index === 0) return true;
  const prevMessage = messages.value[index - 1];
  const currMessage = messages.value[index];
  const diff = new Date(currMessage.sendtime) - new Date(prevMessage.sendtime);
  // console.log(currMessage.sendtime, prevMessage.sendtime, diff);
  return diff > 3 * 60 * 1000;
};

const formatTime = (time) => {
  return new Date(time).toLocaleString();
};

const updateIndex = () => {
  for (let i = 0; i < data.value.length; i++) {
    getIndex.value.set(data.value[i].userid, i);
  }
};

const getUnReadMessage = async () => {
  await axios
    .get(`${SERVER_URL}/message/query/unread`, {
      params: {
        receiver: userinfo.value.userid,
      },
    })
    .then((res) => {
      let data2 = res.data;
      for (let i = 0; i < data2.length; i++) {
        let sender = data2[i].sender;
        let index = getIndex.value.get(sender);
        data.value[index].unread += 1;
        handleLoad(data2[i]);
      }
    })
    .catch((err) => {
      console.log(err);
    });
};

const getMessageConnect = async () => {
  await axios
    .get(`${SERVER_URL}/message/query/connect`, {
      params: {
        belong: userinfo.value.userid,
      },
    })
    .then(async (res) => {
      let temp = res.data;
      temp.sort((a, b) => {
        const dateA = new Date(a.latestconnecttime);
        const dateB = new Date(b.latestconnecttime);
        return dateB - dateA;
      });
      for (let i = 0; i < temp.length; i++) {
        data.value.push({
          userid: temp[i].target,
          name: temp[i].targetname,
          picture: temp[i].targetpicture,
          time: temp[i].latestconnecttime,
          message: temp[i].latestmessage,
          unread: 0,
        });
        getIndex.value.set(temp[i].target, i);
      }
      await handleChoose(data.value[0].userid);
      scrollToBottom();
    })
    .catch((err) => {
      console.log(err);
    });
};
const calcCost = async () => {
  //根据汇率计算花了多少角
  let thisCost = calculatePriceInCNY(
    cost.value.input,
    cost.value.output,
    cost.value.model
  );
  let data = null;
  try {
    data = {
      special: "trophycount",
      userid: JSON.parse(localStorage.getItem("user")).userid,
    };
  } catch (error) {}
  await axios.post(`${SERVER_URL}/userextra/update/special`, data, {
    params:{
      count: thisCost * -1,
    }
   
  }).then(res => {
    store.dispatch("notice", {
        title: "响应成功",
        message: `您的奖杯数量已更新，您花费了${thisCost}个奖杯`,
        type: "success",
       
      });
  })
};

const addMessage = async (message) => {
  try {
    let userid = userinfo.value.userid;
    console.log(!message.receiver || !userid, message.receiver, !userid);
    if (!message.receiver || !userid) return;
    console.log("扫测试");
    if (parseInt(message.receiver) !== parseInt(userid)) {
      return;
    }
  } catch (error) {
    console.log(error);
  }

  isToAI.value = false;
  let target = message.sender;
  if (!getIndex.value.has(target)) {
    data.value.unshift({
      userid: target,
      name: message.sendername,
      picture:
        "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
      time: message.sendtime,
      message: message.message,
    });
    updateIndex();
  } else {
    let index = getIndex.value.get(target);
    data.value[index].time = message.sendtime;
    data.value[index].message = message.message;
    let temp = data.value.splice(index, 1)[0];
    data.value.unshift(temp);
    updateIndex();
  }
  if (cost.value.input !== "not") {
    //计算一下花费
    // cost.value.input = messageInput.value //这里不能写，因为这里被清楚了，用之前的就行，发之前存储了的
    cost.value.output = message.message;

    calcCost();
  }
  handleLoad(message);
  try {
    if (target !== data.value[index()].userid) {
      let index = getIndex.value.get(target);
      data.value[index].unread += 1;
      return;
    }
  } catch (error) {
    console.log(error);
  }

  messages.value.push(message);
  scrollToBottom();
  await handleRead(message);
};

const queryMessage = async () => {
  let userid = userinfo.value.userid;
  await axios
    .get(`${SERVER_URL}/message/querychange?receiver=${userid}`)
    .then(async (event) => {
      let message = event.data;
      addMessage(message);
    })
    .catch((err) => {
      console.log(err);
    });
};

onMounted(async () => {
  nextTick(() => {
    scrollbar.value = document.querySelectorAll(".el-scrollbar__wrap")[1];
  });
  if (timer.value) {
    clearInterval(timer.value);
  } else {
    queryMessage();
    // setTimeout(() => {
    //   timer.value = setInterval(queryMessage, 1000000);
    // }, 50000000);
  }
  await getMessageConnect();
  await getUnReadMessage();
});

onBeforeRouteLeave((to, from, next) => {
  if (timer.value) {
    clearInterval(timer.value);
    timer.value = null;
  }
  next();
});

onBeforeUnmount(() => {
  if (timer.value) {
    clearInterval(timer.value);
    timer.value = null;
  }
});
</script>
<style scoped>
.choose {
  background-color: rgba(0, 0, 0, 0.1);
}
.hover-shadow:hover {
  background-color: rgba(0, 0, 0, 0.1);
  cursor: pointer;
}
.message-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.message-time {
  width: 100%;
  text-align: center;
}

.message-content {
  width: 100%;
  display: flex;
}

.message-content.message-right {
  justify-content: flex-end;
}

.message-content.message-left {
  justify-content: flex-start;
}
</style>
