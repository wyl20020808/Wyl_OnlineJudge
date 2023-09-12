package com.wyl.backend.entity.judge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

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
