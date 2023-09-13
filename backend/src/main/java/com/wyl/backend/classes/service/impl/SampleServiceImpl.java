package com.wyl.backend.classes.service.impl;

import com.wyl.backend.classes.problem.sql.SampleMapper;
import com.wyl.backend.classes.service.SampleService;
import com.wyl.backend.classes.utils.FileUtil;
import com.wyl.backend.classes.utils.OssUtil;
import com.wyl.backend.classes.utils.ZipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;


@Service
public class SampleServiceImpl implements SampleService {
    @Autowired
    OssUtil ossUtil;
    @Autowired
    SampleMapper sampleMapper;

    @Override
    public boolean uploadZipToOss(long problemId, File zipFile) {
        String ossPath = "oj/" + problemId + '/';
        String unZipPath = ZipUtil.getCachePath() + UUID.randomUUID() + '\\';
        String ossInPath = ossPath + "in/";
        String ossOutPath = ossPath + "out/";

        //先删除oss上原来的文件夹
        boolean isDel = ossUtil.delPath(ossPath);
        if (!isDel)
            return false;

        try {
            //创建爱缓存文件夹
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

            // 读取in文件夹中的文件
            for (File inFile : inFiles) {
                System.out.println("读取in文件：" + inFile.getName());
                //将inFile保存至oss
                ossUtil.uploadFile(inFile, ossInPath + inFile.getName());
            }

            // 读取out文件夹中的文件
            for (File outFile : outFiles) {
                System.out.println("读取out文件：" + outFile.getName());
                // 将out文件保存至oss
                ossUtil.uploadFile(outFile, ossOutPath + outFile.getName());
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
