package com.wyl.backend.classes.user.sql;

import com.wyl.backend.classes.problem.ProblemContent;
import com.wyl.backend.classes.user.userinfo.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserOperator {
    @Insert("INSERT INTO userinfo(registertime, username, password,useremail) " +
            "VALUES(#{userinfo.registertime}, #{userinfo.username}, #{userinfo.password},#{userinfo.useremail})")
    int insert(@Param("userinfo") UserInfo userinfo);
    @Select("SELECT * FROM userinfo")
    List<UserInfo> select();

    @Update("UPDATE userinfo SET nickname=#{userinfo.nickname},usermotto=#{userinfo.usermotto} where userid =  #{userinfo.userid}")
    int updateUsername(@Param("userinfo") UserInfo userinfo);

    @Update("UPDATE userinfo SET userpicture=#{userinfo.userpicture} where userid =  #{userinfo.userid}")
    int updateUserPictrue(@Param("userinfo") UserInfo userinfo);
}
