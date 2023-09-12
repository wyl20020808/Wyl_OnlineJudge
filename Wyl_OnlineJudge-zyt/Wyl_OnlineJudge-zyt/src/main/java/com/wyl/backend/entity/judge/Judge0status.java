package com.wyl.backend.entity.judge;

import lombok.Data;

/**
 * judge0的返回标识
 */
@Data
public class Judge0status {
    //    >2则表示题目已经判断结束
    private Integer id;
    //    具体地判题信息描述
    private String description;
}
