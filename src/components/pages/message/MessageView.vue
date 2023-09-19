<template>
  <a-row style="margin-left: 150px; margin-top: 10px">
    <a-col>
      <el-card>
        <el-scrollbar style="width: 250px" height="555px">
          <a-list
            v-if="data.length > 0"
            item-layout="horizontal"
            :data-source="data"
          >
            <template #renderItem="{ item }">
              <a-list-item
                @click="handleChoose(item.userid)"
                class="hover-shadow"
                :class="{
                  choose: chooseTarget === item.userid,
                }"
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
                      src="https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis"
                    />
                  </template>
                </a-list-item-meta>
                <a-badge :count="item.unread" class="item">
                  </a-badge
                >
              </a-list-item>
            </template>
          </a-list>
          <div v-else>
            <a-empty />
          </div>
        </el-scrollbar>
      </el-card>
    </a-col>

    <a-col>
      <a-row>
        <el-scrollbar ref="scrollbar" height="410px">
          <el-card style="min-height: 410px; min-width: 850px">
            <div
              style="text-align: center; font-size: 20px; width: 100%; top: 0"
            >
              {{ targetName }}
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
                    <el-card
                      style="
                        max-width: 200px;
                        word-wrap: break-word; /* 这会让长单词在达到最大宽度时换行 */
                      "
                    >
                      {{ message.message }}
                    </el-card>
                  </template>
                </a-comment>
              </div>
            </div>
          </el-card>
        </el-scrollbar>
      </a-row>
      <a-row style="margin-top: 0px">
        <a-textarea
          style="width: 850px"
          v-model:value="messageInput"
          placeholder="消息"
          :auto-size="{ minRows: 8, maxRows: 20 }"
          @keydown.enter.prevent="sendMessage"
          @keydown.ctrl.enter="insertNewLine"
          show-count
          :maxlength="114514"
          :addonAfter="{
            text: '发送',
            onClick: sendMessage,
          }"
        />
        <a-button @click="sendMessage">发送</a-button>
      </a-row>
    </a-col>
  </a-row>
</template>

<script setup>
import { onBeforeUnmount, watchEffect } from "vue";
import { ref, onMounted, nextTick } from "vue";
import { onBeforeRouteLeave } from "vue-router";
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
import { getBeijingTime } from "@/js/functions/TimeAbout";
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

const handleRead = async (message) => {
  await axios
    .post(`${SERVER_URL}/message/haveread`, message) //标记为已读
    .then((res) => {})
    .catch((err) => {
      console.log(err);
    });
};
const handleLoad = async (message) => {
  await axios
    .post(`${SERVER_URL}/message/haveload`, message) //设置一下已经加载过了
    .then((res) => {})
    .catch((err) => {
      console.log(err);
    });
};
// const loadAllConnectMessage = async () =>{
//   await axios.get(`${SERVER_URL}/message/query/all`,{
//     params:{
//       receiver:userinfo.value.userid,
//     }
//   })
//   .then(temp => {
//     let had = new Set();
//     let res = temp.data;
//     for(let i=0;i<res.length;i++){//存放消息
//       let sender = res[i].sender;
//       if(had.has(sender)){
//         allMessage.value[sender].push(res[i]);
//       }else{
//         allMessage.value[sender] = [res[i]];
//         had.add(sender);
//       }
//     }
//     // console.log(allMessage.value)
//   })
//   .catch(err =>{
//     console.log(err);
//   })
// }
const handleChoose = async (userid) => {
  chooseTarget.value = userid;
  data.value[getIndex.value.get(userid)].unread = 0;
  await axios
    .get(`${SERVER_URL}/message/query`, {
      //查询两个人的发消息记录
      params: {
        sender: userinfo.value.userid,
        receiver: userid,
      },
    })
    .then(async (res) => {
      let data2 = res.data;
      messages.value = [];
      for (let i = 0; i < data2.length; i++) {
        messages.value.push(data2[i]);
        await handleRead(data2[i]);
        await handleLoad(data2[i]);
      }
      targetName.value = data.value[getIndex.value.get(userid)].name;
      
      scrollToBottom();
    })
    .catch((err) => {
      console.log(err);
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
    // 如果按下了Ctrl键，不发送消息
    return;
  }
  let now = getBeijingTime();
  let senderpicture = userinfo.value.userpicture;

  let target = data.value[getIndex.value.get(chooseTarget.value)].userid;
  let message = {
    sender: userinfo.value.userid,
    receiver: target,
    message: messageInput.value,
    sendtime: now,
    sendername: userinfo.value.nickname,
    senderpicture, //数据库里要存真的
  };
  await axios
    .post(`${SERVER_URL}/message/send`, message)
    .then((res) => {
      messages.value.push(message);
      data.value[getIndex.value.get(chooseTarget.value)].message =
        messageInput.value;
      scrollToBottom();
    })
    .catch((err) => {
      console.log(err);
    });
  messageInput.value = "";
};
const index = () => {
  return getIndex.value.get(chooseTarget.value);
};
const insertNewLine = () => {
  event.preventDefault();
  // 在这里处理插入新行的逻辑
  messageInput.value += "\n";
};
const shouldShowTime = (index) => {
  if (index === 0) return true;
  const prevMessage = messages.value[index - 1];
  const currMessage = messages.value[index];
  const diff = currMessage.time - prevMessage.time;
  return diff > 5 * 60 * 1000; // 如果两条消息的时间差大于5分钟，就显示时间
};
const formatTime = (time) => {
  // 这里是你的时间格式化函数，你可以根据你的需求来实现它
  return new Date(time).toLocaleString();
};
const updateIndex = () => {
  for (let i = 0; i < data.value.length; i++) {
    getIndex.value.set(data.value[i].userid, i);
  }
};
const getUnReadMessage = async () => {
  //获取一下所有的未读消息
  await axios
    .get(`${SERVER_URL}/message/query/unread`, {
      params: {
        receiver: userinfo.value.userid,
      },
    })
    .then((res) => {
      let data2 = res.data;
      // console.log(data2);
      for (let i = 0; i < data2.length; i++) {
        let sender = data2[i].sender;
        let index = getIndex.value.get(sender); //这个人的下标
        data.value[index].unread += 1;
        handleLoad(data2[i]);//标记一下这个消息被加载过了
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
        // 将时间字符串转换为Date对象
        const dateA = new Date(a.latestconnecttime);
        const dateB = new Date(b.latestconnecttime);
        // 按照时间的降序排序
        return dateB - dateA;
      });
      for (let i = 0; i < temp.length; i++) {
        data.value.push({
          userid: temp[i].target, //就是对方的id
          name: temp[i].targetname, //对方的名字
          // picture: temp[i].targetpicture,
          picture:
            "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
          time: temp[i].latestconnecttime,
          message: temp[i].latestmessage,
          unread: 0, //没有读的消息
        });
        getIndex.value.set(temp[i].target, i); //设置一下每个对话者id对应下标的信息
      }
      await handleChoose(data.value[0].userid); //默认选择最早的那个，然后更新消息
      scrollToBottom();
    })
    .catch((err) => {
      console.log(err);
    });
};
const queryMessage = async () => {
  //处理实时接受消息
  let userid = userinfo.value.userid;
  await axios
    .get(`${SERVER_URL}/message/querychange?receiver=${userid}`) //监听后端数据库的变化
    .then(async (event) => {
      let message = event.data;
      if (!message.receiver) return; //如果不是我们希望的数据
      if (message.receiver !== userid) {
        //不是我接收的消息就无所谓
        return;
      }
      //判断一下消息列表里面现在有没有新来的这个人
      let target = message.sender; //发送者
      if (!getIndex.value.has(target)) {
        //没有就要新增一个
        data.value.unshift({
          userid: target, //就是对方的id
          name: message.sendername, //对方的名字
          picture:
            "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
          time: message.sendtime, //最后的时间
          message: message.message, //最后的消息
        });
        updateIndex(); //更新一下索引
      } else {
        //如果有就要更新
        let index = getIndex.value.get(target); //找一下它的下标
        data.value[index].time = message.sendtime; //更新时间
        data.value[index].message = message.message; //更新消息
        let temp = data.value.splice(index, 1)[0]; //从数组中移除元素
        data.value.unshift(temp); //将元素添加到数组的开头
        updateIndex(); //更新一下索引，因为上面的ifelse修改了
      }
      
      handleLoad(message); //更新一下已加载
      // console.log('更新了已加载')
      if (target !== data.value[index()].userid) {
        // console.log('更新加载数量')
        //如果不是当前聊天的消息，就处理一下未读数量
        let index = getIndex.value.get(target);
        data.value[index].unread += 1;
        return;
      }
      console.log('新增消息')
      //如果是当前聊天的消息，就要新增消息
      messages.value.push(
        message
      );
      scrollToBottom();
      //并且还要把这条消息标为已读
      await handleRead(message);
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
    setTimeout(() => {
      timer.value = setInterval(queryMessage, 1000);
    }, 5000);
  }
  await getMessageConnect(); //读取我的连接列表

  await getUnReadMessage(); //读取我的未读情况列表
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
// watchEffect(
//   () => {
//     if (messages.value.length > 0) {
//       scrollToBottom();
//     }
//   },
//   { deep: true }
// );
</script>

<style scoped>
.choose {
  background-color: rgba(
    0,
    0,
    0,
    0.1
  ); /* 这会添加一个透明度为 0.1 的黑色背景，看起来像灰色阴影 */
}
.hover-shadow:hover {
  background-color: rgba(
    0,
    0,
    0,
    0.1
  ); /* 这会添加一个透明度为 0.1 的黑色背景，看起来像灰色阴影 */
  cursor: pointer;
}
.message-container {
  width: 800px;
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* 修改这里 */
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
