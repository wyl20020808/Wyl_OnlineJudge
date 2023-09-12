package com.wyl.backend;


import com.wyl.backend.service.SampleService;
import com.wyl.backend.utils.FileUtil;
import com.wyl.backend.utils.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;


@SpringBootTest
@Slf4j
public class ZipUtilTest {
    @Autowired
    SampleService sampleService;
    /**
     * 将zip上传至oss中
     */
    @Test
    void readZipFormOssToOss() {
        String zipFilePath = "D:\\CodeLife\\Code\\webStudy\\code\\Wyl_OnlineJudge\\file\\file.zip";
        String unZipPath = ZipUtil.getCachePath() + UUID.randomUUID() +'\\';
        String inDirPath = unZipPath+"in\\";
        String outDirPath = unZipPath+"out\\";
        File zipFile = new File(zipFilePath);

        try {

//             解压zip文件
            ZipUtil.unzip(zipFile, unZipPath);


            File inDir = new File(inDirPath);
            File outDir = new File(outDirPath);
            File[] inFiles = inDir.listFiles();
            File[] outFiles = outDir.listFiles();

            //校验用户传递的zip的合法性

            // 读取in文件夹中的文件
            if (inFiles != null) {
                for (File inFile : inFiles) {
                    System.out.println("读取in文件：" + inFile.getName());
                    //将inFile保存至oss

                }
            }

            // 读取out文件夹中的文件
            if (outFiles != null) {
                for (File outFile : outFiles) {
                    System.out.println("读取out文件：" + outFile.getName());
                    // 将out文件保存至oss

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void getRootPath() throws FileNotFoundException {
        String path = ResourceUtils.getFile("classpath:cache/path").getPath();
        path = path.substring(0, path.length() - 4);
        System.out.println(path);
    }

    @Test
    void testDeletDir() throws IOException {
        FileUtil.deletDir("D:\\CodeLife\\Code\\webStudy\\code\\Wyl_OnlineJudge\\Wyl_OnlineJudge_Hou\\target\\classes\\cache\\7096109273033474048");
    }

    @Test
    void testUploadZipToOss(){
        String zipFilePath = "D:\\CodeLife\\Code\\webStudy\\code\\Wyl_OnlineJudge\\file\\file.zip";
        File zipFile = new File(zipFilePath);

        boolean isUpload = sampleService.uploadZipToOss(1,zipFile);
        System.out.println(isUpload?"上传成功":"上传失败");
    }

    @Test
    void uploadZip(){
        String zipFilePath = "D:\\CodeLife\\Code\\webStudy\\code\\Wyl_OnlineJudge\\file\\file.zip";
        File zipFile = new File(zipFilePath);
        boolean success = sampleService.uploadZipToOss(1, zipFile);
        System.out.println(success?"上传成功":"上传失败");
    }
}
