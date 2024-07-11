package com.wyl.backend.classes.message;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("messageconnect")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageConntect {
//    private int id;
    private int belong;
    private int target;
    private String belongname;
    private String targetname;
    private String connecttime;
    private String latestmessage;
    private String latestconnecttime;
    private String targetpicture;

    public void init(int belong, int target, String belongname, String targetname, String targetpicture,String connecttime, String latestmessage, String latestconnecttime) {
        this.belong = belong;
        this.target = target;
        this.belongname = belongname;
        this.targetname = targetname;
        this.targetpicture = targetpicture;

        this.connecttime = connecttime;

        this.latestmessage = latestmessage;

        this.latestconnecttime = latestconnecttime;
    }
}
