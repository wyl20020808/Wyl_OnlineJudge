package com.wyl.backend.classes.judge;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("judgecontent")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JudgeContent {
    @TableId("judgecontentid")
    public int judgecontentid;
    public int judgeid;
    public String judgestate;//
    public double runtime;//
    public String memory;//
    public String loaded;//判断某个提交有没有被用户查询过，这个是为了实时更新判题进度的，可能存在问题
    public int userid;//后面的三个变量是用来同步两个表中的信息的 现在发生了 这个局部先插入数据库，再整体插入数据库的情况
    public int problemid;//先插入了judgecontent，再插入了judge
    public String submittime;
    //有可能的问题就是，当用户提交之后，直接不看结果了，就会出问题，后续要改进
    //不过目前可以通过提交时间来增加一个判断
}
