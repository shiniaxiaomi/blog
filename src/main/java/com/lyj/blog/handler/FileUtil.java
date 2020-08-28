package com.lyj.blog.handler;

import com.lyj.blog.exception.MessageException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class FileUtil {

    // 创建一个zip压缩文件
    public static String createZipFile(File sourceFile,String targetPath){
        // 将创建好的文件夹生成一个压缩包，并添加上备份日期
        try {
            // 目标zip文件
            File zipFile = new File(targetPath + "/Md-" +
                    DateTimeFormatter.ofPattern("yyyy-MM-dd&HH:mm:ss")
                            .format(LocalDateTime.now()) + ".zip");
            ZipOutputStream zipOutputStream = new ZipOutputStream(
                    new BufferedOutputStream(new FileOutputStream(zipFile)));
            compressedFile(zipOutputStream,sourceFile);//file源文件
            zipOutputStream.close();
            return zipFile.getName();
        } catch (IOException e) {
            log.error("md文件夹压缩失败",e);
        }

        return null;
    }

    // 递归的压缩文件夹或文件
    private static void compressedFile(ZipOutputStream zipOutputStream, File file){
        if(file==null) return;

        boolean isFile = file.isFile();
        if(isFile){
            try(BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file))){
                // 创建压缩包中的文件节点
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                byte[] buffer=new byte[100]; //缓冲区100字节

                while(reader.read(buffer)!=-1){
                    zipOutputStream.write(buffer);
                }
            } catch (IOException e) {
                log.error("压缩文件时，"+file.getAbsolutePath()+"文件压缩失败");
            }
        }

        File[] files = file.listFiles();
        if(files==null) return;

        // 创建压缩包中的文件夹节点
        try {
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            for (File zipFile:files){
                compressedFile(zipOutputStream,zipFile);
            }
        } catch (IOException e) {
            log.error("压缩文件时，"+file.getAbsolutePath()+"文件夹压缩失败");
        }
    }

    // 递归删除文件夹或文件
    public static void deleteFileOrDir(File file){
        if(file==null) return;

        boolean isFile = file.isFile();
        if(isFile){
            boolean delete = file.delete();
            if(!delete){
                throw new MessageException(file.getAbsolutePath()+"文件删除失败");
            }
            return;
        }

        File[] files = file.listFiles();
        if(files==null) return;

        for(File deleteFile:files){
            deleteFileOrDir(deleteFile);
        }

        // 删除自己
        boolean delete = file.delete();
        if(!delete){
            throw new MessageException(file.getAbsolutePath()+"文件夹删除失败");
        }
    }

}
