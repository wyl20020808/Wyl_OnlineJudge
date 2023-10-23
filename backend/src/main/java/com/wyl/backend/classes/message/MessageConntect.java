package com.wyl.backend.classes.message;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("messageconnect")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageConntect {
    private int id;
    private int belong;
    private int target;
    private String belongname;
    private String targetname;
    private String connecttime;
    private String latestmessage;
    private String latestconnecttime;
    private String targetpicture;
}
