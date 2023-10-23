package com.wyl.backend.classes.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Component
public class ZipUtil {
    @Autowired
    OssUtil ossUtil;

    /**
     * 获取缓存路径
     * @return 缓存路径
     */
    public static String getCachePath() {
        String path = null;
        try {
            path = ResourceUtils.getFile("classpath:cache/path").getPath();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return path.substring(0, path.length() - 4);
    }

    /**
     * 将zip文件解压至对应的目录下
     * @param zipFile   zip文件
     * @param destDirPath   目标路径
     * @throws IOException
     */
    public static void unzip(File zipFile, String destDirPath) throws IOException {
        byte[] buffer = new byte[1024];

        ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile.toPath()));
        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {
            File newFile = newFile(destDirPath, zipEntry);
            if (zipEntry.isDirectory()) {
                newFile.mkdirs();
            } else {
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }


    /**
     * 创建新的文件
     * @param destDirPath 目标路径
     * @param zipEntry zip
     * @return
     */
    public static File newFile(String destDirPath, ZipEntry zipEntry) {
        String filePath = destDirPath + File.separator + zipEntry.getName();
        String[] pathParts = filePath.split("/");
        String fileName = pathParts[pathParts.length - 1];
        StringBuilder dirPathBuilder = new StringBuilder();
        for (int i = 0; i < pathParts.length - 1; i++) {
            dirPathBuilder.append(pathParts[i]);
            if (i != pathParts.length - 2) {
                dirPathBuilder.append(File.separator);
            }
        }
        String dirPath = dirPathBuilder.toString();
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dirPath + File.separator + fileName);
    }

    /**
     * 校验zip中的文件是否合乎规范，并获取文件列表
     * in和out要一一对应
     *
     * @return zip是否合规
     */
    public static boolean checkZipAndGetFileList(String unZipPath,List<File> inFiles,List<File> outFiles) {
        File unZipFile = new File(unZipPath);
        File[] files = unZipFile.listFiles();

        List<String> inFilesName = new ArrayList<>();
        List<String> outFilesName = new ArrayList<>();


        if (files == null)
            return false;

        String fileName;
        for (File file : files) {
            fileName = file.getName();
            //如果是in文件
            if (RegxUtil.isMatches(fileName,"^(.+?)\\.in")){
                inFiles.add(file);
                inFilesName.add(fileName.replaceAll(".in",""));
            }
            //如果是out文件
            if (RegxUtil.isMatches(fileName,"^(.+?)\\.out")){
                outFiles.add(file);
                outFilesName.add(fileName.replaceAll(".out",""));
            }
        }
        for (String inName:inFilesName){
            if (!outFilesName.contains(inName))
                return false;
        }

        return true;
    }
}
