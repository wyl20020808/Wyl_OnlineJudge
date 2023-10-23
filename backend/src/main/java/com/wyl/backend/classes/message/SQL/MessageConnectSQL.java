package com.wyl.backend.classes.message.SQL;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyl.backend.classes.message.MessageConntect;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageConnectSQL extends BaseMapper<MessageConntect> {
}
