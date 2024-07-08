const WebSocketService = {
    socket: null,
  
    connect() {
      this.socket = new WebSocket('ws://localhost:8088/ws');
  
      this.socket.onopen = () => {
        console.log('WebSocket connection opened');
      };
  
      this.socket.onmessage = (event) => {
        console.log('Received message from server: ', event.data);
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
  
    sendMessage(message) {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        this.socket.send(message);
      } else {
        console.error('WebSocket is not open');
      }
    }
  };
  
  export default WebSocketService;
  