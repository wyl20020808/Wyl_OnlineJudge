package com.wyl.backend.classes.user.sql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.classes.user.userinfo.UserExtra;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserExtraOperator extends BaseMapper<UserExtra> {
    @Update("UPDATE userextra SET submitcount = submitcount + 1 WHERE userid = #{userid}")
    void incrementSubmitCount(int userid);
    @Update("UPDATE userextra SET aceptedcount = aceptedcount + 1 WHERE userid = #{userid}")
    void incrementAceptedcountCount(int userid);
}
