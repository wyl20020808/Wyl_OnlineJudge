package com.wyl.backend.keda.classes.auxiliary.websocket;

import com.wyl.backend.classes.message.Message;
import com.wyl.backend.keda.classes.auxiliary.api.student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
@Component//这个可能有问题
@Service
public class MyWebSocketHandler extends TextWebSocketHandler {

    private static CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
//    @Autowired
//    private  MyWebSocketHandler myWebSocketHandler;
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);

        session.sendMessage(new TextMessage("Hello, " + payload));
    }

    public void broadcast(student x) throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {

                session.sendMessage(new TextMessage(x.getUid() + " " + x.getPid()));
            }
        }
    }
    public void noticeNewMessage(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {

                session.sendMessage(new TextMessage(message));
            }
        }
    }
}

