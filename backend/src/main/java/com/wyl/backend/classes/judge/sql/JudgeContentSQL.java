package com.wyl.backend.classes.judge.sql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.classes.judge.JudgeContent;
import com.wyl.backend.classes.problem.ProblemContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface JudgeContentSQL extends BaseMapper<JudgeContent> {
    @Update("UPDATE judgecontent jc JOIN judge j ON jc.userid = j.userid AND jc.problemid = j.problemid AND jc.submittime = j.submittime SET jc.judgeid = j.judgeid")
    void updateJudgeId();
}
