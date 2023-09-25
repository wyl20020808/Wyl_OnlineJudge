package com.wyl.backend.classes.utils;

//import com.aliyun.oss.OSS;
//import com.aliyun.oss.model.*;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.wyl.backend.classes.judge.InAndOut;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Oss工具类
 */
@Component
@Data
@Slf4j
public class OssUtil {
    @Resource
    OSS ossClient;
    private String url = "https://zyt-ali-oss.oss-cn-shenzhen.aliyuncs.com/";
    private String bucketName = "zyt-ali-oss";

    public static String regexIn = "(?<=/in/)[^/]+(?=\\.in)";
    public static String regexOut = "(?<=/out/)[^/]+(?=\\.out)";

    /**
     * 向oss发送一个String并存储
     *
     * @param content 存储内容
     * @param path 要存储至oss的绝对路径
     * @return 是否存储成功
     */
    public boolean uploadString(String content,String path) {
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, new ByteArrayInputStream(content.getBytes()));
            // 上传字符串。
            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 上传文件
     * @param file 要上传的文件对象
     * @param path 上传至oss的绝对路径
     * @return 是否上传成功
     */
    public boolean uploadFile(File file, String path) {
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file);
            // 上传字符串。
            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 读取文件，
     * @param url 文件的绝对路径
     * @return 返回文件的内容
     * @throws IOException
     */
    public String readFileFromLocal(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }
    public StringBuffer readFileFromOss(String url) throws IOException {
        StringBuffer res = new StringBuffer();
        boolean objectExist = ossClient.doesObjectExist(getBucketName(), url);
        //如果文件存在，取文件最后位置
        if ( objectExist ){
            OSSObject ossObject = ossClient.getObject(getBucketName(), url);
            InputStreamReader reader = new InputStreamReader(ossObject.getObjectContent());

            char[] buffer = new char[1024];
            int length;
            while ((length = reader.read(buffer)) != -1) {
                res.append(buffer, 0, length);
            }

            ossObject.close();
        }
        return res;
    }

    /**
     * 删除oss中的文件或文件夹
     * @param path 要删除文件夹的绝对路径
     * @return 是否删除成功
     */
    public boolean delete(String path) {
        try {
            // 删除文件。
            ossClient.deleteObject(bucketName, path);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return true;
    }

    public List<InAndOut> getInOutFromLocal(String path) {
        File inDir = new File(path + "/in");
        File outDir = new File(path + "/out");
        File[] inFiles = inDir.listFiles();
        File[] outFiles = outDir.listFiles();

        List<String> inList = new ArrayList<>();
        List<String> outList = new ArrayList<>();
        List<InAndOut> res = new ArrayList<>();

        if (inFiles != null && outFiles != null) {
            for (File inFile : inFiles) {
                String inName = inFile.getName().replace(".in", "");
                for (File outFile : outFiles) {
                    String outName = outFile.getName().replace(".out", "");
                    if (inName.equals(outName)) {
                        inList.add(inFile.getName());
                        outList.add(outFile.getName());
                    }
                }
            }
        }

        InAndOut inAndOut;
        for (String str : inList) {
            if (!outList.contains(str))
                log.info("文件错乱了=>{}", path);
            inAndOut = new InAndOut(path + "/in/" + str, path + "/out/" + str.replace(".in", ".out"));
            res.add(inAndOut);
        }
        return res;
    }


    public List<InAndOut> getInOutFromOss(String url){
        ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest( getBucketName()).withPrefix(url));
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
        return res;
    }

    public boolean havePath(String path){
        return ossClient.doesObjectExist(bucketName, path);
    }

    public boolean delPath(String path){
        if (path==null)
            return false;
        // 列举所有包含指定前缀的文件并删除。
        String nextMarker = null;
        ObjectListing objectListing = null;
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName)
                    .withPrefix(path)
                    .withMarker(nextMarker);

            objectListing = ossClient.listObjects(listObjectsRequest);
            if (objectListing.getObjectSummaries().size() > 0) {
                List<String> keys = new ArrayList<String>();
                for (OSSObjectSummary s : objectListing.getObjectSummaries()) {
                    System.out.println("key name: " + s.getKey());
                    keys.add(s.getKey());
                }
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(keys).withEncodingType("url");
                DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(deleteObjectsRequest);
                List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
                try {
                    for(String obj : deletedObjects) {
                        String deleteObj =  URLDecoder.decode(obj, "UTF-8");
                        System.out.println(deleteObj);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
        return true;
    }
}
