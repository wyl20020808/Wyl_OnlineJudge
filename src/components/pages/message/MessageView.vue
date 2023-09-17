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
                {{ formatTime(message.time) }}
              </div>
              <div
                class="message-content"
                :class="{
                  'message-right': message.name === userinfo.nickname,
                  'message-left': message.name !== userinfo.nickname,
                }"
              >
                <a-comment :author="message.name" :avatar="message.avatar">
                  <template #content>
                    <el-card
                      style="
                        max-width: 200px;
                        word-wrap: break-word; /* 这会让长单词在达到最大宽度时换行 */
                      "
                      >{{ message.text }}</el-card
                    >
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

<script>
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
import { getBeijingTime } from "@/js/functions/TimeAbout";
export default {
  data() {
    const data = []; //存放消息列表
    let messages = []; //存放选择的消息
    return {
      isHovered: false,
      messageInput: "",
      data,
      messages,
      userinfo: JSON.parse(localStorage.getItem("user")),
      chooseTarget: "",
      getIndex: new Map(),
      targetName: "",
      source: null,
      timer: null,
    };
  },
  methods: {
    async handleChoose(userid) {
      this.chooseTarget = userid;
      await axios
        .get(`${SERVER_URL}/message/query`, {
          //查询两个人的发消息记录
          params: {
            sender: this.userinfo.userid,
            receiver: userid,
          },
        })
        .then((res) => {
          let data = res.data;
          this.messages = [];
          for (let i = 0; i < data.length; i++) {
            this.messages.push({
              name: data[i].sendername,
              // avatar:data[i].senderpicture,
              avatar:
                "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
              text: data[i].message,
              time: data[i].sendtime,
            });
          }
          this.targetName = this.data[this.getIndex.get(userid)].name;
          this.scrollToBottom();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    scrollToBottom() {
      this.$nextTick(() => {
        let container = this.$refs.scrollbar.$el.querySelector(
          ".el-scrollbar__wrap"
        );
        container.scrollTop = container.scrollHeight;
      });
      // let elements =
      //   this.$refs.scrollbar.$el.querySelectorAll(".message-container");
      // let lastElement = elements[elements.length - 1];
      // lastElement.scrollIntoView(false);
    },
    async sendMessage() {
      if (event.ctrlKey) {
        // 如果按下了Ctrl键，不发送消息
        return;
      }
      let now = getBeijingTime();
      let senderpicture = this.userinfo.userpicture;

      let target = this.data[this.getIndex.get(this.chooseTarget)].userid;
      await axios
        .post(`${SERVER_URL}/message/send`, {
          sender: this.userinfo.userid,
          receiver: target,
          message: this.messageInput,
          sendtime: now,
          sendername: this.userinfo.nickname,
          senderpicture, //数据库里要存真的
        })
        .then((res) => {
          this.messages.push({
            //同时把消息更新进去
            name: this.userinfo.nickname,
            // avatar:senderpicture,
            avatar:
              "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
            text: this.messageInput,
            time: now,
          });
          this.data[this.index()].message = this.messageInput;
          this.scrollToBottom();
        })
        .catch((err) => {
          console.log(err);
        });
      this.messageInput = "";
    },
    index() {
      return this.getIndex.get(this.chooseTarget);
    },
    insertNewLine() {
      event.preventDefault();
      // 在这里处理插入新行的逻辑
      this.messageInput += "\n";
    },
    shouldShowTime(index) {
      if (index === 0) return true;
      const prevMessage = this.messages[index - 1];
      const currMessage = this.messages[index];
      const diff = currMessage.time - prevMessage.time;
      return diff > 5 * 60 * 1000; // 如果两条消息的时间差大于5分钟，就显示时间
    },
    formatTime(time) {
      // 这里是你的时间格式化函数，你可以根据你的需求来实现它
      return new Date(time).toLocaleString();
    },
    updateIndex() {
      this.getIndex.clear();
      for (let i = 0; i < this.data.length; i++) {
        this.getIndex.set(this.data[i].target, i);
      }
    },
    async queryMessage() {
      let userid = this.userinfo.userid;
      await axios
        .get(`${SERVER_URL}/message/querychange?receiver=${userid}`) //监听后端数据库的变化
        .then(async (event) => {
          // // 注意这里我们使用箭头函数，以便能在函数内部访问到 Vue 组件的实例（即 this）
          // // 收到服务器消息时触发

          let message = event.data;
          console.log(message);
          if (!message.receiver) return; //如果不是我们希望的数据
          if (message.receiver !== userid) {
            //不是我接收的消息就无所谓
            return;
          }
          //判断一下消息列表里面现在有没有新来的这个人
          let target = message.sender; //发送者
          if (!this.getIndex.has(target)) {
            //没有就要新增一个
            this.data.unshift({
              userid: target, //就是对方的id
              name: message.sendername, //对方的名字
              // picture: temp[i].targetpicture,
              picture:
                "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
              time: message.sendtime, //最后的时间
              message: message.message, //最后的消息
            });
            this.updateIndex(); //更新一下索引
          }

          if (target !== this.data[this.index()].userid) {
            //如果不是当前聊天的消息，就不用继续处理
            return;
          }
          //如果是当前聊天的消息，就要新增消息
          this.messages.push({
            name: message.sendername,
            // avatar:data[i].senderpicture,
            avatar:
              "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
            text: message.message,
            time: message.sendtime,
          });
          this.scrollToBottom();
          //并且还要把这条消息标为已读

          await axios
            .post(`${SERVER_URL}/message/haveread`, message)
            .then((res) => {})
            .catch((err) => {
              console.log(err);
            });
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  beforeDestroy() {
    if(this.timer){
      clearInterval(this.timer);
    }
    
    if (this.source) {
      this.source.close(); // 关闭 EventSource 连接
    }
  },
  mounted() {
    //暂时没有作用
    // let userid = this.userinfo.userid;
    // this.source = new EventSource(
    //   `${SERVER_URL}/message/stream?clientId=${userid}`
    // ); //监听后端数据库的变化
    // this.source.onmessage = (event) => {
    //   // // 注意这里我们使用箭头函数，以便能在函数内部访问到 Vue 组件的实例（即 this）
    //   // // 收到服务器消息时触发
    //   let message = JSON.parse(event.data);
    //   console.log(message);
    //   if (!message.receiver) return; //如果不是我们希望的数据
    //   if (message.receiver !== userid) {
    //     //不是我接收的消息就无所谓
    //     return;
    //   }
    //   //判断一下消息列表里面现在有没有新来的这个人
    //   let target = message.sender; //发送者
    //   if (!this.getIndex.has(target)) {
    //     //没有就要新增一个
    //     this.data.unshift({
    //       userid: target, //就是对方的id
    //       name: message.sendername, //对方的名字
    //       // picture: temp[i].targetpicture,
    //       picture:
    //         "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
    //       time: message.sendtime, //最后的时间
    //       message: message.message, //最后的消息
    //     });
    //     this.updateIndex(); //更新一下索引
    //   }
    //   if (target !== this.data[this.index()].userid) {
    //     //如果不是当前聊天的消息，就不用继续处理
    //     return;
    //   }
    //   //如果是当前聊天的消息，就要新增消息
    //   this.messages.push({
    //     name: message.sendername,
    //     // avatar:data[i].senderpicture,
    //     avatar:
    //       "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
    //     text: message.message,
    //     time: message.sendtime,
    //   });
    // };
  },
  async created() {
    //读取我的消息列表
    await axios
      .get(`${SERVER_URL}/message/query/connect`, {
        params: {
          belong: this.userinfo.userid,
        },
      })
      .then((res) => {
        let temp = res.data;
        temp.sort((a, b) => {
          // 将时间字符串转换为Date对象
          const dateA = new Date(a.latestconnecttime);
          const dateB = new Date(b.latestconnecttime);
          // 按照时间的降序排序
          return dateB - dateA;
        });
        for (let i = 0; i < temp.length; i++) {
          this.data.push({
            userid: temp[i].target, //就是对方的id
            name: temp[i].targetname, //对方的名字
            // picture: temp[i].targetpicture,
            picture:
              "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
            time: temp[i].latestconnecttime,
            message: temp[i].latestmessage,
          });
          this.getIndex.set(temp[i].target, i); //设置一下每个对话者id对应下标的信息
        }
        this.handleChoose(this.data[0].userid); //默认选择最早的那个，然后更新消息
        this.scrollToBottom();
      })
      .catch((err) => {
        console.log(err);
      });
    this.timer = setInterval(this.queryMessage, 1000);
  },
};
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
