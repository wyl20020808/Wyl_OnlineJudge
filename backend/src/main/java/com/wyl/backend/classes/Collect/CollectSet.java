package com.wyl.backend.classes.Collect;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("collectset")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectSet {
    @TableId(value = "id")
    public int id;
    public int userid;
    public String username;
    public String name;
    public String createtime;
    public String modifytime;
    public String note;
    public String type;
    public String secret;
    @TableLogic // 标记逻辑删除注解
    private Boolean deleted;
}
