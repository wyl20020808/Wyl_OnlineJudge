package com.wyl.backend.classes.judge;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用来缓存zip文件的读取结果
 */
@Data
@AllArgsConstructor
public class InAndOut {
    private String in;
    private String out;
}
