package com.wyl.backend.keda.classes.auxiliary.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
public class WebSocketController {

    @Autowired
    private MyWebSocketHandler webSocketHandler;

    @GetMapping("/send")
    public void sendMessage(@RequestParam String message) {
//        try {
//            webSocketHandler.broadcast(message);
//            return "Message sent: " + message;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Error sending message: " + e.getMessage();
//        }
    }
}

