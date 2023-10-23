package com.wyl.backend.classes.contest.SQL;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.classes.contest.ContestJudgeContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ContestJudgeContentSQL extends BaseMapper<ContestJudgeContent> {
    @Update("UPDATE contestjudgecontent jc JOIN contestjudge j ON jc.userid = j.userid AND jc.problemid = j.problemid AND jc.submittime = j.submittime SET jc.judgeid = j.judgeid")
    void updateJudgeId();
    @Update("UPDATE contestjudgecontent jc JOIN contestjudge j ON jc.userid = j.userid AND jc.problemid = j.problemid AND jc.submittime = j.submittime SET jc.problemchar = j.problemchar")
    void updateJudgeProblemChar();
}
