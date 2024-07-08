package com.wyl.backend.keda.classes.Course.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyl.backend.keda.classes.Course.CourseInfo;
import com.wyl.backend.keda.classes.Course.CourseStudentInfo;
import com.wyl.backend.keda.classes.Course.SQL.CourseInfoMapper;

import com.wyl.backend.keda.classes.Course.SQL.CourseStudentInfoMapper;
import com.wyl.backend.keda.classes.student.SQL.StudentInfoSql;
import com.wyl.backend.keda.classes.student.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping(value = "/course")
@RestController
public class CourseController {
    @Autowired
    private StudentInfoSql studentInfoSql;
    @Autowired
    private CourseInfoMapper courseInfoMapper;
    @Autowired
    private CourseStudentInfoMapper courseStudentInfoMapper;
    @PostMapping(value = "/update")
    public CourseInfo updateCourse(@RequestBody CourseInfo courseInfo) {
        if (courseInfoMapper.selectCount(new QueryWrapper<CourseInfo>().eq("course_name", courseInfo.getCourseName())) != 0) {
            courseInfoMapper.updateById(courseInfo);
        } else {
            courseInfoMapper.insert(courseInfo);
            courseInfo = courseInfoMapper.selectOne(new QueryWrapper<CourseInfo>().eq("create_time",courseInfo.getCreateTime()));
            syncStudentsToCourse(courseInfo);
        }
        return courseInfoMapper.selectById(courseInfo.getId());
    }

    @GetMapping(value = "/query")
    public List<CourseInfo> queryCourse(@RequestParam(required = false) String courseName,
                                        @RequestParam(required = false) String classId,
                                        @RequestParam(required = false) String mainTeacher) {
        QueryWrapper<CourseInfo> queryWrapper = new QueryWrapper<>();
        if (courseName != null) {
            queryWrapper.like("course_name", courseName);
        }
        if (classId != null) {
            queryWrapper.eq("class_id", classId);
        }
        if (mainTeacher != null) {
            queryWrapper.eq("main_teacher", mainTeacher);
        }
        return courseInfoMapper.selectList(queryWrapper);
    }
    @GetMapping(value = "/query/info")
    public List<CourseStudentInfo> queryCourseInfo(@RequestParam(required = false) String courseName,
                                        @RequestParam(required = false) String courseId,
                                        @RequestParam(required = false) String mainTeacher) {
        QueryWrapper<CourseStudentInfo> queryWrapper = new QueryWrapper<>();
        System.out.println("asdasd" + courseId);
        if (courseName != null) {
            queryWrapper.like("course_name", courseName);
        }
        if (courseId != null) {
            queryWrapper.eq("course_id", courseId);
        }
        if (mainTeacher != null) {
            queryWrapper.eq("main_teacher", mainTeacher);
        }
        return courseStudentInfoMapper.selectList(queryWrapper);
    }

    @PostMapping(value = "/update/courseinfo")
    public CourseStudentInfo updateCourseInfo(@RequestBody CourseStudentInfo courseStudenInfo) {
        if (courseStudentInfoMapper.selectCount(new QueryWrapper<CourseStudentInfo>().eq("id", courseStudenInfo.getId())) != 0) {
            courseStudentInfoMapper.updateById(courseStudenInfo);
        } else {
            courseStudentInfoMapper.insert(courseStudenInfo);
//            courseInfo = courseStudentInfoMapper.selectOne(new QueryWrapper<CourseStudentInfo>().eq("create_time",courseInfo.getCreateTime()));

        }
        return courseStudentInfoMapper.selectById(courseStudenInfo.getId());
    }


    private void syncStudentsToCourse(CourseInfo courseInfo) {
        // 获取班级所有学生
        List<StudentInfo> students = studentInfoSql.selectList(new QueryWrapper<StudentInfo>().eq("class", courseInfo
                .getClassName()));
        for (StudentInfo student : students) {
            CourseStudentInfo courseStudentInfo = new CourseStudentInfo();
            courseStudentInfo.setStudentId(student.getStudentid());
            courseStudentInfo.setStudentName(student.getName());
            courseStudentInfo.setTrophyCount(0); // 初始奖杯数为0
            courseStudentInfo.setCourseId(String.valueOf(courseInfo.getId()));
            courseStudentInfo.setCourseName(courseInfo.getCourseName());
            courseStudentInfo.setClassName(courseInfo.getClassName());
            courseStudentInfo.setClassId(String.valueOf(courseInfo.getClassName()));
            courseStudentInfo.setCreateTime(courseInfo.getCreateTime());

            courseStudentInfoMapper.insert(courseStudentInfo);
        }
    }
}
