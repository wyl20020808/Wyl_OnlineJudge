package com.wyl.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.entity.problem.ProblemContent;
import com.wyl.backend.entity.problem.ProblemSample;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProblemMapper extends BaseMapper<ProblemContent> {
    @Insert("INSERT INTO problemcontent(title, background, description, inputformat, outputformat, hint) " +
            "VALUES(#{problemContent.title}, #{problemContent.background}, #{problemContent.description}, " +
            "#{problemContent.inputformat}, #{problemContent.outputformat}, #{problemContent.hint})")
    int insert(@Param("problemContent") ProblemContent problemContent);

    @Select("SELECT * FROM problemcontent")
    List<ProblemContent> selectAll();

    @Select("SELECT * FROM problemcontent where problemid=#{problemid}")
    ProblemContent selectById(int problemId);

    @Select("SELECT * FROM problemsample where problemid=#{problemid}")
    List<ProblemSample> selectSampleById(int problemId);

    @Insert("INSERT INTO problemsample(problemid, input, output) " +
            "VALUES(#{problemSample.problemid}, #{problemSample.input}, #{problemSample.output})")
    int insertToSample(@Param("problemSample") ProblemSample sample);
}
