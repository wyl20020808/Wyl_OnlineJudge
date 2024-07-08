package com.wyl.backend.keda.classes.Course;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("course_student_info")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseStudentInfo {
    @TableId(value = "id")
    public int id;

    @TableField(value = "student_id")
    private String studentId;

    @TableField(value = "student_name")
    private String studentName;

    @TableField(value = "trophy_count")
    private int trophyCount;

    @TableField(value = "course_id")
    private String courseId;

    @TableField(value = "course_name")
    private String courseName;

    @TableField(value = "class_name")
    private String className;

    @TableField(value = "class_id")
    private String classId;

    @TableField(value = "create_time")
    private String createTime;

    @TableLogic
    @TableField(value = "deleted")
    private Boolean deleted = false;
}
