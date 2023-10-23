package com.wyl.backend.classes.judge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于多个程序提交的实体类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submissions{
    private List<Submission> submissions;
}
