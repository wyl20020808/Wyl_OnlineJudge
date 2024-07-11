package com.wyl.backend.classes.message;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("message")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    @TableId
    private int id;
    private int sender;
    private int receiver;
    private String sendername;
    private String message;
    private String sendtime;
    private String senderpicture;

    public void init(int sender,int receiver,String sendername,String message,String sendtime,String senderpicture){
        this.sender = sender;
        this.receiver = receiver;
        this.sendername = sendername;
            this.message = message;
                this.sendtime = sendtime;
                    this.senderpicture = senderpicture;
    }

}
