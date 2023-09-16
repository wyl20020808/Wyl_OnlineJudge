package com.wyl.backend.classes.user.sql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.classes.user.userinfo.UserFans;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserFansSQL extends BaseMapper<UserFans> {
}
