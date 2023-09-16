<template>
  <a-row style="margin-left: 200px; margin-top: 10px">
    <a-col>
      <el-scrollbar style="width: 250px" height="400px">
        <a-list item-layout="horizontal" :data-source="data">
          <template #renderItem="{ item }">
            <a-list-item
              class="hover-shadow"
              v-on:mouseover="isHovered = true"
              v-on:mouseout="isHovered = false"
            >
              <a-list-item-meta
                style="
                  white-space: nowrap;
                  overflow: hidden;
                  text-overflow: ellipsis;
                "
                description="Ant Design, a design language for background applications, is refined by Ant UED Team"
              >
                <template #title>
                  <a href="https://www.antdv.com/">{{ item.title }}</a>
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
      </el-scrollbar>
    </a-col>

    <a-col>
      <a-row>
        <el-scrollbar height="400px">
          <a-card>
            <div
              v-for="(message, index) in messages"
              :key="message.id"
              class="message-container"
            >
              <div v-if="shouldShowTime(index)" class="message-time">
                {{ formatTime(message.time) }}
              </div>
              <div
                class="message-content"
                :class="{
                  'message-right': message.user === 'me',
                  'message-left': message.user !== 'me',
                }"
              >
                <a-comment :author="message.user" :avatar="message.avatar">
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
          </a-card>
        </el-scrollbar>
      </a-row>
      <a-row style="margin-top: 20px">
        <a-textarea
          style="width: 850px"
          v-model:value="messageInput"
          placeholder="消息"
          :auto-size="{ minRows: 10, maxRows: 20 }"
          @keydown.enter.prevent="sendMessage"
          @keydown.ctrl.enter="insertNewLine"
          show-count :maxlength="114514"
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
export default {
  data() {
    const data = [
      {
        title: "Ant Design Title 1",
      },
      {
        title: "Ant Design Title 2",
      },
      {
        title: "Ant Design Title 3",
      },
      {
        title: "Ant Design Title 4",
      },
    ];
    const users = ["User1", "User2", "User3", "User4", "User5", "me"];
    const avatars = [
      "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
      "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
      "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
      "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
      "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
      "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/mirror-assets/16bd473a5dfbad9687e~tplv-t2oaga2asx-jj-mark:60:60:0:0:q75.avis",
    ];
    const texts = [
      "Hello!",
      "How are you?",
      "What are you doing?",
      "Good morning!",
      "Good night!",
      "ghasjdgajhsgdjahsgdjahsgdjhasgdjhagsdjagsdjhagsjhdgajshdgajshdgajsdgajsdgha",
    ];

    const messages = [];
    let time = Date.now();

    for (let i = 0; i < 20; i++) {
      const userIndex = Math.floor(Math.random() * users.length);
      const textIndex = Math.floor(Math.random() * texts.length);

      messages.push({
        id: i,
        user: users[userIndex],
        avatar: avatars[userIndex],
        text: texts[textIndex],
        time: new Date(time),
      });

      // 在1到10分钟之间随机增加时间
      time += Math.floor(Math.random() * 10 + 1) * 60 * 1000;
    }
    return {
      isHovered: false,
      messageInput: "",
      data,
      messages,
    };
  },
  methods: {
    sendMessage() {
      if (event.ctrlKey) {
        // 如果按下了Ctrl键，不发送消息
        return;
      }
      // 在这里处理发送消息的逻辑
      console.log(this.messageInput);
      this.messageInput = "";
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
  },
};
</script>

<style scoped>
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
