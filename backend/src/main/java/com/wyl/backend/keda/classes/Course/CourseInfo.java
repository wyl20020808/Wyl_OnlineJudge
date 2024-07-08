package com.wyl.backend.keda.classes.Course;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("course_info")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseInfo {
    @TableId(value = "id")
    public int id;

    @TableField(value = "course_name")
    private String courseName;

    @TableField(value = "class_id")
    private String className;

    @TableField(value = "main_teacher")
    private String mainTeacher;

    @TableField(value = "course_date")
    private String courseDate;

    @TableField(value = "create_time")
    private String createTime;

    @TableField(value = "start_time") // 开始时间
    private String startTime;

    @TableField(value = "end_time") // 结束时间
    private String endTime;

    @TableLogic // 逻辑删除标记
    private Boolean deleted = false;
}
