package com.wyl.backend.keda.classes.student.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyl.backend.keda.classes.student.SQL.StudentInfoSql;
import com.wyl.backend.keda.classes.student.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping(value = "/student")
@RestController
public class StudentController {

    @Autowired
    private StudentInfoSql studentInfoSql;

    @PostMapping(value = "/update")//包含插入、更新、删除
    public StudentInfo updateCollect(@RequestBody StudentInfo studentInfo, @RequestParam(required = false) String delete){
        if(delete != null){
            studentInfoSql.delete(new QueryWrapper<StudentInfo>().eq("studentid",studentInfo.getStudentid()));
            return null;
        }
        if(studentInfoSql.selectCount(new QueryWrapper<StudentInfo>().eq("studentid", studentInfo.getStudentid())) != 0){//如果有那就更新
            studentInfoSql.updateById(studentInfo);
        }else{
            studentInfoSql.insert(studentInfo);
        }
        return studentInfoSql.selectOne(new QueryWrapper<StudentInfo>().eq("studentid",studentInfo.getStudentid()));
    }

    @GetMapping(value = "/query")//包含插入和更新
    public List<StudentInfo> queryCollect(@RequestParam(required = false) Integer studentid, @RequestParam(required = false) Integer belong,

                                          @RequestParam(required = false) String className,
                                          @RequestParam(required = false) Integer collectid){
        QueryWrapper<StudentInfo> query = new QueryWrapper<>();

        if(studentid != null) {
            query.eq("userid",studentid);
        }
        if(belong != null){
            query.eq("belong",belong);
        }
        if(className != null){

            query.eq("class",className);
        }

        return studentInfoSql.selectList(query);
    }


}
