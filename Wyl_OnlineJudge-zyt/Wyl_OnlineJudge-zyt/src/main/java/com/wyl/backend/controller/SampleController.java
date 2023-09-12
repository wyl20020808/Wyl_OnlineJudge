package com.wyl.backend.controller;


import com.wyl.backend.entity.utils.Result;
import com.wyl.backend.mapper.ProblemMapper;
import com.wyl.backend.service.SampleService;
import com.wyl.backend.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 管理题目示例的类
 */
@CrossOrigin
@RequestMapping("/sample")
@RestController
@Slf4j
public class SampleController {
    @Autowired
    SampleService sampleService;
    @Autowired
    ProblemMapper problemMapper;

    /**
     * 上传zip文件至oss
     *
     * @param problemId 题目id
     * @param zipFile   zip文件
     * @return 上传结果
     */
    @PostMapping("/upload")
    public Result<Object> pushMulToJudge0Form(@RequestParam("problemId") long problemId,
                                              @RequestParam("zipFile") MultipartFile zipFile) throws IOException {
        //判断problemId对应的题目是否存在


        File file = FileUtil.convertToFile(zipFile);
        if (sampleService.uploadZipToOss(problemId, file))
            return Result.success();
        else
            return Result.error("上传失败");
    }
}
