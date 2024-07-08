package com.wyl.backend.keda.classes.student.SQL;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.keda.classes.student.StudentInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentInfoSql extends BaseMapper<StudentInfo> {
}
