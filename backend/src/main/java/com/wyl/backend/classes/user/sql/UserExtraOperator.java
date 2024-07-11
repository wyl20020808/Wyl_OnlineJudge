package com.wyl.backend.classes.user.sql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.classes.user.userinfo.UserExtra;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserExtraOperator extends BaseMapper<UserExtra> {
    @Update("UPDATE userextra SET submitcount = submitcount + 1 WHERE userid = #{userid}")
    void incrementSubmitCount(@Param("userid") int userid);

    @Update("UPDATE userextra SET aceptedcount = aceptedcount + 1 WHERE userid = #{userid}")
    void incrementAceptedcountCount(@Param("userid") int userid);

    @Update("UPDATE userextra SET trophy = trophy + #{count} WHERE userid = #{userid}")
    void incrementTrophyCont(@Param("userid") int userid, @Param("count") int count);
}
