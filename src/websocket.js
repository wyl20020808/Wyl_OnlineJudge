import axios from "axios";

import { SERVER_URL } from "./js/functions/config";
import { newMessage } from "./js/functions/info";

const WebSocketService = {
  socket: null,

  connect(store) {
    this.socket = new WebSocket('ws://localhost:8088/ws');

    this.socket.onopen = () => {
      console.log('WebSocket connection opened');
    };

    this.socket.onmessage = async (event) => {
      console.log("从后端接收到了：", event.data)

      //可能有问题
      try {
        const message = JSON.parse(event.data);
        const temp = JSON.parse(localStorage.getItem("user"));

        if (!temp) return;
        console.log("接受者id为：", parseInt(message.receiver), "您的id为：", parseInt(temp.userid));
        // console.log("message.receiver:", message.receiver, "类型:", typeof message.receiver);
        // console.log("temp.userid:", temp.userid, "类型:", typeof temp.userid);

        if (parseInt(message.receiver) !== parseInt(temp.userid)) {
          console.log("接收者ID不匹配，跳过此消息");
          return;
        }
        store.dispatch('updateNewMessage', { key: 'message', value: message });
        // console.log("你是接收者，", newMessage.value);
        // newMessage.set("message", message);
        

      } catch (e) {
        console.log(e)
      }

      const arr = event.data.split(' ');
      const temp = JSON.parse(localStorage.getItem("user"));
      if (!temp) return;
      console.log('Received message from server: ', arr[0], arr[1]);
      // this.getUnreadMessage();//更新一下未读的消息数量
      await axios
        .get(`${SERVER_URL}/message/query/unread`, {
          params: {
            receiver: JSON.parse(localStorage.getItem("user")).userid,
          },
        })
        .then((response) => {
          if (response.data) {
            store.dispatch('setUnRead', response.data.length);
            console.log(response.data.length, "未读消息数量", store.state.unRead);
          }
        })
        .catch((err) => {
          console.log(err);
        });

      // console.log('你的id: ', temp.userid);
      if (parseInt(arr[0]) !== temp.userid) return;
      store.dispatch("notice", {
        title: "您有一条新消息~",
        message: "",
        type: "success",
      });



      // 在这里处理接收到的消息，例如更新UI
    };

    this.socket.onclose = (event) => {
      console.log('WebSocket connection closed: ', event);
      // 自动重连逻辑（可选）
      setTimeout(() => {
        this.connect();
      }, 5000); // 5秒后重连
    };

    this.socket.onerror = (error) => {
      console.error('WebSocket error: ', error);
    };
  },
  async getUnreadMessage() {
    //统计一下未读的消息

  },
  sendMessage(message) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(message);
    } else {
      console.error('WebSocket is not open');
    }
  }
};

export default WebSocketService;
