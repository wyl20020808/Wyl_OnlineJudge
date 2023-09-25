package com.wyl.backend.classes.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
/**
 * 文件读取工具类
 * (后续应当开发或使用开源的文件系统，随便啦)
 */
@Slf4j
public class FileUtil {

    public static String readTextFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.lines(path)
                .collect(Collectors.joining(System.lineSeparator()));
    }


    public static void insertTextToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.append(content);
        }
    }

    //    使用缓冲区进行文件读取方法,读取更快，但在读取大文件的时候可能会导致较多的内存使用
    public static String readTextFileWithBuffer(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            char[] buffer = new char[4096];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                content.append(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content.toString();
    }

    //    读取文件并返回Base64编码
    public static String readFileToBase64(String filePath) {
        return Base64Util.encode(readTextFileWithBuffer(filePath));
    }

    public static boolean createFolder(String path) {
        File folder = new File(path);

        if (folder.exists()) {
            System.out.println("文件夹已存在=>"+path);
            return false;
        }

        if (folder.mkdirs()) {
            System.out.println("文件夹创建成功=>"+path);
            return true;
        } else {
            System.out.println("文件夹创建失败=>"+path);
            return false;
        }
    }



    //    删除文件夹及其子目录
    public static void deletDir(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);

        Files.walkFileTree(path,
                new SimpleFileVisitor<Path>() {
                    // 先去遍历删除文件
                    @Override
                    public FileVisitResult visitFile(Path file,
                                                     BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    // 再去遍历删除目录
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir,
                                                              IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                }
        );
        log.info("删除缓存文件夹=>{}",dirPath);
    }


    /**
     * 将MultipartFile转为File类型
     * @param multipartFile 接收到的文件
     * @return  file类型的文件
     * @throws IOException
     */
    public static File convertToFile(MultipartFile multipartFile) throws IOException {
        byte[] fileBytes = multipartFile.getBytes();
        File tempFile = File.createTempFile("temp", null);

        try (OutputStream outputStream = Files.newOutputStream(tempFile.toPath())) {
            outputStream.write(fileBytes);
        }

        return tempFile;
    }

    public static void copyFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}