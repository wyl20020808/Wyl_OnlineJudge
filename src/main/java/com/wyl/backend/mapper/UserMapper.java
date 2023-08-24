package com.wyl.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.entity.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
    @Insert("INSERT INTO userinfo(registertime, username, password,useremail) " +
            "VALUES(#{userinfo.registertime}, #{userinfo.username}, #{userinfo.password},#{userinfo.useremail})")
    int insert(@Param("userinfo") UserInfo userinfo);

    @Select("SELECT * FROM userinfo")
    List<UserInfo> select();

    UserInfo selectUById();
}
