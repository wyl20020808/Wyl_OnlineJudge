package com.wyl.backend.keda.classes.Class;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("class_info")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassInfo {
    @TableId(value = "id")
    public int id;

    @TableField(value = "class_id")
    private String classId;

    @TableField(value = "class_name")
    private String className;

    @TableField(value = "head_teacher")
    private String headTeacher;

    @TableField(value = "start_date")
    private String startDate;

    @TableField(value = "main_teacher")
    private String mainTeacher;

    @TableField(value = "create_time")
    private String createTime;

    @TableField(value = "class_level")
    private String classLevel;

    @TableField(value = "start_time") // 开始时间
    private String startTime;

    @TableField(value = "end_time") // 结束时间
    private String endTime;

    @TableLogic // 逻辑删除标记
    private Boolean deleted = false;
}
