package com.wyl.backend.keda.classes.auxiliary.SQL;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.keda.classes.Class.ClassInfo;
import com.wyl.backend.keda.classes.auxiliary.api.student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface studentMapper extends BaseMapper<student> {
}
