package com.wyl.backend.classes.Collect;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("collect")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Collect {
    @TableId(value = "id")
    public int id;
    public int userid;
    public String username;
    public int collectid;
    public String createtime;
    public String type;
    public int belong = 1;
    @TableLogic // 标记逻辑删除注解
    private Boolean deleted;
}
