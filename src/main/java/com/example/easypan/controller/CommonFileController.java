package com.example.easypan.controller;

import com.example.easypan.Annotation.VerifyParam;
import com.example.easypan.Util.StringTools;
import com.example.easypan.bean.Constants;
import com.example.easypan.config.AppConfig;
import com.example.easypan.entity.dto.SessionWebUserDto;
import com.example.easypan.entity.po.FileInfo;
import com.example.easypan.entity.query.FileInfoQuery;
import com.example.easypan.entity.vo.ResponseVo;
import com.example.easypan.enums.FileCategoryEnum;
import com.example.easypan.enums.FileFolderTypeEnum;
import com.example.easypan.enums.FileTypeEnum;
import com.example.easypan.service.FileInfoService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

public class CommonFileController extends ABaseController{
    @Resource
    private AppConfig appConfig;

    @Resource
    private FileInfoService fileInfoService;

    protected void getImage(HttpServletResponse response,String imageFolder, String imageName){
        if (StringTools.isEmpty(imageFolder) || StringTools.isEmpty(imageName) || !StringTools.pathIsOk(imageFolder) || !StringTools.pathIsOk(imageName)){
            return;
        }

        String fileFolder = appConfig.getPath()+ Constants.FILE_FOLDER_FILE;

        String folderName=fileFolder+imageFolder;

        String filePath=folderName+"/"+imageName;

        String suffix = StringTools.getFileNameSuffix(imageName);
        suffix=suffix.replace(".","");

        String ContenType="image/"+suffix;
        response.setContentType(ContenType);

        response.setHeader("Cache-Control","max-age=2592000");

        readFile(response,filePath);



    }

    /**
     *
     * @param response
     * @param userId
     * @param fileId   请求过来  aa (请求视频的index.m3u8)    (视频的分片)aa_0000.ts   aaa.txt
     */
    protected void getFile(HttpServletResponse response,String userId,String fileId){

        String path="";

        //(视频的分片)aa_0000.ts
        if (fileId.endsWith(".ts")){
            String[] split = fileId.split("_");
            String fileSId = split[0];
            FileInfo fileInfo = fileInfoService.getFileInfoByFileIdAndUserId(fileSId, userId);
            if (fileInfo == null) {
                return;
            }
            // month.userid+fileid
            String fileNameFolder = StringTools.getFileNameNoSuffix(fileInfo.getFilePath());
            // mm
            String fileFolderMain = appConfig.getPath();
            // mm/file
            String fileFolderName=fileFolderMain+Constants.FILE_FOLDER_FILE;

            path=fileFolderName+fileNameFolder+"/"+fileId;

            File file = new File(path);
            if (!file.exists()) {
                return;
            }

        }else {

            FileInfo fileInfo = fileInfoService.getFileInfoByFileIdAndUserId(fileId, userId);
            if (fileInfo == null) {
                return;
            }
            //   month/userId+fileId  ==  fileNameFolder
            String fileNameFolder = StringTools.getFileNameNoSuffix(fileInfo.getFilePath());
            //   mmm
            String fileFolderMain = appConfig.getPath();
            //  mmm/file
            String fileFolderName=fileFolderMain+Constants.FILE_FOLDER_FILE;

            //aa (请求视频的index.m3u8)  mmm/file/month/uesrid+fileid/index.m3u8
            if (FileCategoryEnum.VIDEO.getCategory() == fileInfo.getFileCategory()) {
                path=fileFolderName+fileNameFolder+"/"+Constants.M3U8_NAME;

            }else {
                //txt   mmm/file     /month/aa.txt
                path=fileFolderName+fileInfo.getFilePath();
            }
            File file = new File(path);
            if (!file.exists()) {
                return;
            }

        }

        readFile(response,path);

    }

    public ResponseVo getFolderInfo(String userId,String path){

        String[] urlPid = path.split("/");

        FileInfoQuery fileInfoQuery=new FileInfoQuery();
        fileInfoQuery.setUserId(userId);
        fileInfoQuery.setFolderType(FileFolderTypeEnum.FOLDER.getFolderType());
        fileInfoQuery.setFileIdArray(urlPid);
        String orderBy="fileId(file_id,\""+ StringUtils.join(urlPid,"\",\"") +"\")";
        fileInfoQuery.setOrderBy(orderBy);

        List<FileInfo> listByParam = fileInfoService.findListByParam(fileInfoQuery);

        return getSuccessResponseVo(listByParam);

    }


}




//        String fileCover = fileInfo.getFileCover();
//        String fileName = fileInfo.getFileName();
//        String suffix = StringTools.getFileNameSuffix(fileName);
//        String month = getMonthByPath(filePath);
//        String tsPathFolder=folderName+month+fileId+userId;
//        String tsPath=tsPathFolder+"/"+Constants.TS_NAME;
//        String m3u8Path=tsPathFolder+"/"+Constants.M3U8_NAME;
//        FileTypeEnum fileTypeEnum = FileTypeEnum.getFileTypeBySuffix(suffix);
////        FileCategoryEnum fileCategoryEnum = fileTypeEnum.getFileCategoryEnum();
