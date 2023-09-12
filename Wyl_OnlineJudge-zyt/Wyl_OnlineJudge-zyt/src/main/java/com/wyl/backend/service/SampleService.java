package com.wyl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyl.backend.entity.problem.ProblemSample;

import java.io.File;


public interface SampleService{
    /**
     * 将用户提供的zip存入oss中
     * 要求：zip必须只包含两个文件夹（in,out），且in中的文件和out中的文件一一对应
     *
     * @param problemId 目标问题id
     * @param zipFile 用户提供的zip文件
     * @return 是否存入成功
     */
    public boolean uploadZipToOss(long problemId,File zipFile);
}
