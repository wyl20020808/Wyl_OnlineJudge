package com.wyl.backend.classes.message.SQL;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.classes.message.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageSQL extends BaseMapper<Message> {
}
