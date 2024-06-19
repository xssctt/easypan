package com.example.easypan.Util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScaleFilter {

    private static final Logger logger= LoggerFactory.getLogger(ScaleFilter.class);

    //用于从视频文件中提取一帧图片，并对图片进行缩放。以下是每个参数的含义：
    //-i %s: 输入视频文件的路径，%s 是一个占位符，表示视频文件的路径。
    //-y: 强制覆盖输出文件，如果输出文件已经存在。
    //-vframes 1: 只提取一帧。
    //-vf scale=%d:%d/a: 应用视频滤镜 scale 进行缩放，其中 %d 和 %d 是占位符，分别表示宽度和高度。/a 表示按原视频的宽高比进行调整。
    //%s: 输出文件的路径，占位符表示输出图片文件的路径。
    public static void createCover(File sourceFile, Integer width, File targeFile,Boolean outprintLog){
        try {
            String cmd="ffmpeg -i %s -y -vframes 1 -vf scale=%d:%d/a %s";
            ProcessUtil.executeCommand(String.format(cmd,sourceFile.getAbsolutePath(),width,width,targeFile.getAbsolutePath()),
                    outprintLog);
        }catch (Exception e){
            logger.error("生成封面失败",e);
        }
    }



    /**
     *   图片压缩
     * @param file
     * @param thumbnailWith
     * @param targetFile
     * @param delSource
     * @return
     */
    public static Boolean createThumbnailWidthFFmpeg(File file,int thumbnailWith,File targetFile,Boolean delSource,Boolean outprintLog){

        try {
            BufferedImage src= ImageIO.read(file);
            //
            int sorceW=src.getWidth();
            int sorceH=src.getHeight();

            //
            if (sorceW <= thumbnailWith){
                return false;
            }
            compressImage(file,thumbnailWith,targetFile,delSource,outprintLog);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void compressImage(File sourceFile,int width,File targetFile,Boolean delSource,Boolean outprintLog){

        try {
            String cmd = "ffmpeg -i %s -vf scale=%d:-1 %s -y";
            ProcessUtil.executeCommand(String.format(cmd,sourceFile.getAbsolutePath(),width,targetFile.getAbsolutePath()),outprintLog);
            if (delSource){
                FileUtils.deleteDirectory(sourceFile);
            }
        }catch (Exception e){
            logger.error("压缩图片失败");
        }
    }
}


























