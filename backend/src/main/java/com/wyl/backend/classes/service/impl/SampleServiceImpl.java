package com.wyl.backend.classes.service.impl;

import com.wyl.backend.classes.problem.ProblemContent;
import com.wyl.backend.classes.problem.controller.ProblemControllerMapper;
import com.wyl.backend.classes.problem.sql.SampleMapper;
import com.wyl.backend.classes.service.SampleService;
import com.wyl.backend.classes.utils.FileUtil;
import com.wyl.backend.classes.utils.OssUtil;
import com.wyl.backend.classes.utils.ZipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


@Service
public class SampleServiceImpl implements SampleService {
    @Autowired
    SampleMapper sampleMapper;
    @Autowired
    private ProblemControllerMapper problemControllerMapper;
    @Override
    public boolean uploadZipToOss(long problemId, File zipFile) {
        String projectPath = System.getProperty("user.dir");
        String absolutePath = projectPath + File.separator + "dataSouce" + File.separator;
        String localPath = absolutePath + problemId + File.separator;
        System.out.println(localPath +"  abcde");
        File dir2 = new File(localPath);
        if (!dir2.exists()) {
            dir2.mkdirs();
        }
        String unZipPath = ZipUtil.getCachePath() + UUID.randomUUID() + '\\';
        String localInPath = localPath + "in/";
        String localOutPath = localPath + "out/";

        //先删除本地原来的文件夹
        try {
            FileUtil.deletDir(localPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            //创建缓存文件夹
            boolean makeFolder = FileUtil.createFolder(unZipPath);
            if (!makeFolder)
                return false;

            //解压zip文件
            ZipUtil.unzip(zipFile, unZipPath);

            //文件夹
            File dir = new File(unZipPath);
            File[] files = dir.listFiles();

            ArrayList<File> inFiles = new ArrayList<>();
            ArrayList<File> outFiles = new ArrayList<>();

            //校验用户传递的zip的合法性(合法指的是文件夹中只包含in和out文件，并且in文件和out文件一一对应)
            if (files != null) {
                boolean isformatOk = ZipUtil.checkZipAndGetFileList(unZipPath, inFiles, outFiles);
                if (!isformatOk) {
                    FileUtil.deletDir(unZipPath);
                    return false;
                }
            } else
                return false;
            //更新题目数据的长度
            int len = inFiles.size();
            ProblemContent temp = new ProblemContent();
            temp.setDatalength(len);
            temp.setProblemid((int)problemId);
            problemControllerMapper.updateProblemContent(temp);
            // 读取in文件夹中的文件
            for (File inFile : inFiles) {
                System.out.println("读取in文件：" + inFile.getName());
                //将inFile保存至本地
                File destDir = new File(localInPath);
                if (!destDir.exists()) {
                    boolean result = destDir.mkdirs();
                    if (!result) {
                        System.out.println("无法创建目标目录: " + localInPath);
                        return false;
                    }
                }
                FileUtil.copyFile(inFile, new File(localInPath + inFile.getName()));
            }

// 读取out文件夹中的文件
            for (File outFile : outFiles) {
                System.out.println("读取out文件：" + outFile.getName());
                // 将out文件保存至本地
                File destDir = new File(localOutPath);
                if (!destDir.exists()) {
                    boolean result = destDir.mkdirs();
                    if (!result) {
                        System.out.println("无法创建目标目录: " + localOutPath);
                        return false;
                    }
                }
                FileUtil.copyFile(outFile, new File(localOutPath + outFile.getName()));
            }

            //删除缓存文件
            FileUtil.deletDir(unZipPath);

        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }
}
