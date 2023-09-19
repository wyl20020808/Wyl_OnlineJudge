package com.wyl.backend.classes.message.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wyl.backend.classes.message.Message;
import com.wyl.backend.classes.message.MessageConntect;
import com.wyl.backend.classes.message.SQL.MessageConnectSQL;
import com.wyl.backend.classes.message.SQL.MessageSQL;
import com.wyl.backend.classes.user.Controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@CrossOrigin
@RequestMapping(value = "/message")
@RestController
public class MessageController {
    @Autowired
    private UserController userController;
    @Autowired
    MessageSQL messageSQL;
    @Autowired
    MessageConnectSQL  messageConnectSQL;

//    private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();
//
//    @GetMapping("/stream")
//    public SseEmitter handleSse(@RequestParam int clientId) {
//        SseEmitter emitter = new SseEmitter();
//
//        // 创建一个 AtomicBoolean 作为线程结束的标志
//        AtomicBoolean isEmitterActive = new AtomicBoolean(true);
//
//        // 启动一个新的线程，定期发送心跳消息
//        Thread heartBeatThread = new Thread(() -> {
//            while (isEmitterActive.get()) {
//                try {
//                    // 每隔5秒发送一次心跳消息
//                    Thread.sleep(5000);
//                    emitter.send(SseEmitter.event().name("heartbeat"));
//                } catch (InterruptedException | IOException e) {
//                    // 如果发送心跳消息失败，结束这个线程
//                    return;
//                }
//            }
//        });
//        heartBeatThread.start();
//
//        emitter.onCompletion(() -> {
//            this.emitters.remove(clientId);
//            isEmitterActive.set(false);  // 结束心跳线程
//        });
//        emitter.onTimeout(() -> {
//            this.emitters.remove(clientId);
//            isEmitterActive.set(false);  // 结束心跳线程
//        });
//
//        this.emitters.put(clientId, emitter);
//        return emitter;
//    }
    @GetMapping("/querychange")
    public Message queryMessageChange(@RequestParam int receiver){//查找第一个没有加载的元素
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver", receiver);
        queryWrapper.eq("loaded", "false");
        if(messageSQL.selectList(queryWrapper).size() == 0){
            return null;
        }
        return messageSQL.selectList(queryWrapper).get(0);
    }
    @GetMapping("/query/all")
    public List<Message> queryAll(@RequestParam int receiver){//把我的所有消息都获取过去
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver", receiver);
        if(messageSQL.selectList(queryWrapper).size() == 0){
            return null;
        }
        return messageSQL.selectList(queryWrapper);
    }
    @GetMapping("/query/unread")
    public List<Message> queryUnreadMessage(@RequestParam int receiver){
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver", receiver);
        queryWrapper.eq("readed", "false");
        return messageSQL.selectList(queryWrapper);
    }
    @PostMapping("/haveread")
    public void haveRead(@RequestBody Message message) {//更新消息状态，设置为已读
        UpdateWrapper<Message> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("sender", message.getSender())
                .eq("receiver", message.getReceiver())
                .eq("sendtime", message.getSendtime())
                .set("readed", "true");
        messageSQL.update(null,updateWrapper);
    }

    @PostMapping("/haveload")
    public void haveLoad(@RequestBody Message message) {//更新消息状态，设置为已经加载
        UpdateWrapper<Message> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("sender", message.getSender())
                .eq("receiver", message.getReceiver())
                .eq("sendtime", message.getSendtime())
                .set("loaded", "true");
        messageSQL.update(null,updateWrapper);
    }

    @PostMapping(value = "/send")
    public void send(@RequestBody Message message) {//接收新消息
        //同时还要更新最后联系时间和最后的消息
        UpdateWrapper<MessageConntect> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("belong", message.getSender())
                .eq("target", message.getReceiver())
                .set("latestconnecttime", message.getSendtime())
        .set("latestmessage", message.getMessage());
        messageConnectSQL.update(null,updateWrapper);

        //还要向前端发送数据更新，只发送给接收者
//        SseEmitter emitter = this.emitters.get(message.getReceiver());
//        if (emitter != null) {
//            try {
//
//                emitter.send(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        //还要更新对方和我的连接，因为我给他发了消息那么他那里也需要有连接
        MessageConntect messageConntect = new MessageConntect();
        messageConntect.setBelong(message.getReceiver());
        messageConntect.setTarget(message.getSender());
        messageConntect.setLatestconnecttime(message.getSendtime());
        messageConntect.setLatestmessage(message.getMessage());
        messageConntect.setTargetname(message.getSendername());
        messageConntect.setBelongname(userController.getUsername(message.getReceiver()));
        messageConntect.setTargetpicture(message.getSenderpicture());
        messageConntect.setConnecttime(message.getSendtime());
        addConnect(messageConntect);
        messageSQL.insert(message);
    }

    @GetMapping(value = "/query")//查询两个人之间的记录
    public List<Message> queryMessage(@RequestParam(value =  "sender") int sender,
                                      @RequestParam(value = "receiver") int receiver)  {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.nested(i -> i.eq("sender", sender).eq("receiver", receiver))
                .or(i -> i.eq("sender", receiver).eq("receiver", sender));
        return messageSQL.selectList(queryWrapper);
    }
    public void addConnect(MessageConntect messageConntect){
        //需要判重
        QueryWrapper<MessageConntect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("belong", messageConntect.getBelong())
                .eq("target", messageConntect.getTarget())       ;
        if(messageConnectSQL.selectList(queryWrapper).size() > 0) {
            //如果有就不在添加,而是更新最后连接时间，和消息
            UpdateWrapper<MessageConntect> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("belong", messageConntect.getBelong())
                    .eq("target", messageConntect.getTarget())
                    .set("latestconnecttime", messageConntect.getLatestconnecttime())
                    .set("latestmessage", messageConntect.getLatestmessage());

            messageConnectSQL.update(null,updateWrapper);
            return;
        }

        messageConnectSQL.insert(messageConntect);
    }

    @PostMapping(value = "/add/connect")//新增连接
    public void Connect(@RequestBody MessageConntect messageConntect){
        //需要判重
        addConnect(messageConntect);
    }

    @GetMapping(value = "/query/connect")//查看每个用户的连接列表
    public List<MessageConntect> queryConnect(@RequestParam(value = "belong") int belong){
        QueryWrapper<MessageConntect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("belong", belong);
        return messageConnectSQL.selectList(queryWrapper);
    }
}
