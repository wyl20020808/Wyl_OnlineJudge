package com.wyl.backend.classes.judge.sql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.classes.judge.JudgeContent;
import com.wyl.backend.classes.problem.ProblemContent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JudgeContentSQL extends BaseMapper<JudgeContent> {
}
