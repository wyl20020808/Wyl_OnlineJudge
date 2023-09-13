package com.wyl.backend.classes.judge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用来获取Judge0的API：/submissions/batch的返回结果
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Judge0Token {
    String token;
}
