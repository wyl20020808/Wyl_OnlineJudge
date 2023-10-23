package com.wyl.backend.classes.discuss;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("discussstate")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscussState {
    public int userid;
    public int discussid;
    public boolean liked;
    public boolean dislike;
    public boolean collect;
    public boolean funny;
}
