package com.wyl.backend;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.wyl.backend.entity.judge.InAndOut;
import com.wyl.backend.service.SampleService;
import com.wyl.backend.utils.OssUtil;
import com.wyl.backend.utils.RegxUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Slf4j
public class OssTest {
    @Resource
    OSS ossClient;
    @Autowired
    OssUtil ossUtil;
    @Autowired
    SampleService sampleService;

    @Test
    void upLoadStringToOss() {
        boolean isUpload = ossUtil.uploadString("jfskla\n" +
                "fslkaf\n" +
                "\n" +
                "sfafsafafsafas\n" +
                "fsafs", "test.in");
        System.out.println(isUpload ? "上传成功" : "上传失败");
    }

    @Test
    void readFile() throws IOException {
        String s = String.valueOf(ossUtil.readFileFromOss("oj/1/out/1-1.out"));
        System.out.println(s + ',');
    }

    @Test
    void delFile() {
        boolean isDelet = ossUtil.delete("oj/testZip3");
        System.out.println(isDelet ? "删除成功" : "删除失败");
    }

    @Test
    void uploadFile() {
        File file = new File("D:\\download\\zipOrExe\\test.in");
        boolean isUpload = ossUtil.uploadFile(file, "test/test.in");
        System.out.println(isUpload ? "上传文件成功" : "上传文件失败");
    }

    @Test
    void combineInOut() {
        String url = "oj/testZip2";
        ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(ossUtil.getBucketName()).withPrefix(url));
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();

//        in文件夹下的.in文件放在inList中，out文件夹下的.out文件放在out里面outList中
        List<String> inList = new ArrayList<>();
        List<String> outList = new ArrayList<>();
//        与in的匹配结果和与out的匹配结果
        String inMatches;
        String outMatches;

        List<InAndOut> res = new ArrayList<>();

        String save;
        for (OSSObjectSummary s : sums) {
            save = s.getKey();
            inMatches = RegxUtil.getMatches(save, OssUtil.regexIn);
            outMatches = RegxUtil.getMatches(save, OssUtil.regexOut);
            if (inMatches!=null)
                inList.add(inMatches);
            if (outMatches!=null)
                outList.add(outMatches);
        }

        InAndOut inAndOut;
        for (String str:inList){
            if (!outList.contains(str))
                log.info("文件错乱了=>{}",url);
            inAndOut = new InAndOut(url+"/in/"+str+".in",url+"/out/"+str+".out");
            res.add(inAndOut);
        }
        System.out.println(res.toString());
    }

    @Test
    void getMatches(){
        String matches = RegxUtil.getMatches("oj/testZip2/out/1-3.in", "(?<=/out/)[^/]+(?=\\.out)");
        System.out.println(matches);
    }

    @Test
    void uploadZipToOss(){

        sampleService.uploadZipToOss(1,new File("D:\\CodeLife\\Code\\webStudy\\code\\Wyl_OnlineJudge\\file\\file.zip"));


    }


    @Test
    void testHavePath(){
        System.out.println(ossUtil.havePath("oj/1/"));;
    }


    @Test
    void testDelPath(){
        ossUtil.delPath("oj/testZip3/");
    }
}
