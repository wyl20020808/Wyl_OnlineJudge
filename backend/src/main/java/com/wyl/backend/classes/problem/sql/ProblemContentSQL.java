package com.wyl.backend.classes.problem.sql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.classes.problem.ProblemContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProblemContentSQL extends BaseMapper<ProblemContent> {
    @Update("UPDATE problemcontent SET submitcount = submitcount + 1 WHERE problemid = #{problemid}")
    void incrementSubmitCount(int problemid);
    @Update("UPDATE problemcontent SET aceptedcount = aceptedcount + 1 WHERE problemid = #{problemid}")
    void incrementAceptedcountCount(int problemid);
}
