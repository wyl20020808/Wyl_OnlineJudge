package com.wyl.backend.classes.user.userinfo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("userfans")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserFans {
    private int userid;
    private int fansuserid;
}
