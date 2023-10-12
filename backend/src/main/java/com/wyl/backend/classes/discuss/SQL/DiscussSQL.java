package com.wyl.backend.classes.discuss.SQL;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.classes.discuss.Discuss;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiscussSQL extends BaseMapper<Discuss> {
}
