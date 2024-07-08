package com.wyl.backend.keda.classes.Class.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyl.backend.keda.classes.Class.ClassInfo;
import com.wyl.backend.keda.classes.Class.SQL.ClassInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping(value = "/class")
@RestController
public class ClassController {

    @Autowired
    private ClassInfoMapper classInfoMapper;

    @PostMapping(value = "/update")
    public ClassInfo updateClass(@RequestBody ClassInfo classInfo) {
        if (classInfoMapper.selectCount(new QueryWrapper<ClassInfo>().eq("class_id", classInfo.getClassId())) != 0) {
            classInfoMapper.updateById(classInfo);
        } else {
            classInfoMapper.insert(classInfo);
        }
        System.out.println(classInfo + "hsjdhajsd");
        return classInfoMapper.selectById(classInfo.getClassId());
    }

    @GetMapping(value = "/query")
    public List<ClassInfo> queryClass(@RequestParam(required = false) String classId,
                                      @RequestParam(required = false) String className,
                                      @RequestParam(required = false) String mainTeacher) {
        QueryWrapper<ClassInfo> queryWrapper = new QueryWrapper<>();
        if (classId != null) {
            queryWrapper.eq("class_id", classId);
        }
        if (className != null) {
            queryWrapper.like("class_name", className);
        }
        if (mainTeacher != null) {
            queryWrapper.eq("main_teacher", mainTeacher);
        }
        return classInfoMapper.selectList(queryWrapper);
    }


}
