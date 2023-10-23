package com.wyl.backend.classes.judge.sql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.classes.judge.Judge;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JudgeSql extends BaseMapper<Judge> {
}
