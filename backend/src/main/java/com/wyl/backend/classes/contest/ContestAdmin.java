package com.wyl.backend.classes.contest;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("contestadmin")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContestAdmin {
    @TableId(value = "id")
    private int id;
    private int contestid;
    private int userid;
    private String username;

}
