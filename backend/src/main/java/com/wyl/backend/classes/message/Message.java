package com.wyl.backend.classes.message;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("message")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    private int id;
    private int sender;
    private int receiver;
    private String sendername;
    private String message;
    private String sendtime;
    private String senderpicture;
}
