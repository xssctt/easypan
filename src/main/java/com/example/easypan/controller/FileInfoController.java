package com.example.easypan.controller;

import java.util.Date;

import com.example.easypan.Annotation.GlobalInterceptor;
import com.example.easypan.Annotation.VerifyParam;
import com.example.easypan.entity.dto.SessionWebUserDto;
import com.example.easypan.entity.dto.UploadResultDto;
import com.example.easypan.entity.po.FileInfo;
import com.example.easypan.entity.query.FileInfoQuery;
import java.util.List;
import com.example.easypan.entity.query.SimplePage;
import com.example.easypan.entity.vo.FileInfoVo;
import com.example.easypan.entity.vo.PaginationResultVo;
import com.example.easypan.enums.FileCategoryEnum;
import com.example.easypan.enums.FileDelFlagEnum;
import com.example.easypan.enums.PageSize;
import com.example.easypan.service.FileInfoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.easypan.service.impl.FileInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.easypan.mappers.FileInfoMapper;
import com.example.easypan.controller.ABaseController;
import com.example.easypan.entity.vo.ResponseVo;
import org.springframework.web.multipart.MultipartFile;


/**
	 *
	 * @Desoription 文件信息表controller 控制层
	 * @Auther 摸鱼
	 * @Date 2024-06-13
	 */
@RestController("fileInfoController")
@RequestMapping("/fileInfo")
public class FileInfoController extends CommonFileController{

	private static final Logger logger= LoggerFactory.getLogger(FileInfoServiceImpl.class);

	@Resource
	private FileInfoService fileInfoService;

	/** 
	 *
	 *  
	 */
	@RequestMapping("/loadDateList")
	@GlobalInterceptor
	public ResponseVo loadDataList(HttpSession session,
								   FileInfoQuery query,
								   String category){
		FileCategoryEnum categoryEnum=FileCategoryEnum.getByCode(category);

		if (categoryEnum != null){
			query.setFileCategory(categoryEnum.getCategory());
		}

		query.setUserId(getSessionWebUserDto(session).getUserid());
		query.setOrderBy("last_update_time desc");
		query.setDelFlag(FileDelFlagEnum.USING.getFlag());
		PaginationResultVo<FileInfo> result = fileInfoService.findListByPage(query);

		return getSuccessResponseVo(convert2PaginationVo(result, FileInfoVo.class));
	}


	/**
	 *
	 * @param session
	 * @param fileId
	 * @param multipartFile
	 * @param fileName
	 * @param fileMd5
	 * @param filePid
	 * @param chunkIndex
	 * @param chunks
	 * @return
	 */
		@RequestMapping("/uploadFile")
		@GlobalInterceptor(chackparams = true)
		public ResponseVo uploadFile(HttpSession session,
									   String fileId,
									   MultipartFile multipartFile,
									   @VerifyParam(required = true) String fileName,
									   @VerifyParam(required = true) String fileMd5,
									   @VerifyParam(required = true) String filePid,
									   @VerifyParam(required = true) Integer chunkIndex,
									   @VerifyParam(required = true) Integer chunks){

			SessionWebUserDto sessionWebUserDto = getSessionWebUserDto(session);
			logger.info("===============================>>>>"+multipartFile.getOriginalFilename());
			logger.info("===============================>>>>"+multipartFile.getName());
			//UploadResultDto uploadResultDto = fileInfoService.uploadFile(sessionWebUserDto, fileId, multipartFile, multipartFile.getOriginalFilename(), fileMd5, filePid, chunkIndex, chunks);
			UploadResultDto uploadResultDto = fileInfoService.uploadFile(sessionWebUserDto, fileId, multipartFile, fileName, fileMd5, filePid, chunkIndex, chunks);


			return getSuccessResponseVo(uploadResultDto);
		}


	/**
	 *
	 * @param response
	 * @param imageFolder
	 * @param imageName
	 * @return
	 */

		//通过文件名获取路径
		@RequestMapping("/getImage/{imageFolder}/{imageName}")
		@GlobalInterceptor(chackparams = true)
		public void getImage(HttpServletResponse response,
									 @PathVariable("imageFolder") String imageFolder,
									 @PathVariable("imageName") String imageName){
			super.getImage(response,imageFolder,imageName);

		}

	/**
	 *    获取视频的  .m3u8/.ts
	 * @param response
	 * @param session
	 * @param fileId   获取index.m3u8传值 fileId(moyu/xss)   获取.ts  传值 fileId_0000.ts
	 * @return
	 */

		@RequestMapping("/ts/getVideoInfo/{fileId}")
		@GlobalInterceptor(chackparams = true)
		public void getVideoInfo(HttpServletResponse response,
									   HttpSession session,
									  @PathVariable("fileId") String fileId){
			super.getFile(response,getSessionWebUserDto(session).getUserid(),fileId);
		}

	/**
	 *   获取文本文件
	 * @param response
	 * @param session
	 * @param fileId  找 hq.txt  传值hq
	 * @return
	 */
		@RequestMapping("/getFile/{fileId}")
		@GlobalInterceptor(chackparams = true)
		public void getFile(HttpServletResponse response,
									   HttpSession session,
									   @PathVariable("fileId") String fileId){
			super.getFile(response,getSessionWebUserDto(session).getUserid(),fileId);
		}



		@RequestMapping("/newFolder")
		@GlobalInterceptor(chackparams = true)
		public ResponseVo newFolder(HttpServletResponse response,
							HttpSession session,
							@VerifyParam(required = true) String filePid,
							@VerifyParam(required = true)  String fileName){

			SessionWebUserDto sessionWebUserDto = getSessionWebUserDto(session);


			FileInfo fileInfo = fileInfoService.newFolder(filePid, sessionWebUserDto.getUserid(), fileName);

			return getSuccessResponseVo(fileInfo);
		}


	@RequestMapping("/getFolderInfo")
	@GlobalInterceptor(chackparams = true)
	public ResponseVo getFolderInfo(HttpServletResponse response,
								HttpSession session,
								@VerifyParam(required = true) String path){

		SessionWebUserDto sessionWebUserDto = getSessionWebUserDto(session);

		super.getFolderInfo(sessionWebUserDto.getUserid(),path);

		return getSuccessResponseVo(null);
	}














	/** 
	 *
	 *  新增
	 */
	@RequestMapping("/add")
	public ResponseVo add(FileInfo bean){
		return getSuccessResponseVo(fileInfoService.add(bean));
	}
	/** 
	 *
	 *  批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVo addBatch(@RequestBody List<FileInfo> listBean){
		return getSuccessResponseVo(fileInfoService.addBatch(listBean));
	}
	/** 
	 *
	 *  批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVo addOrUpdateBatch(@RequestBody List<FileInfo> listBean){
		return getSuccessResponseVo(fileInfoService.addOrUpdateBatch(listBean));
	}
	/** 
	 *
	 *  根据FileIdAndUserId查询
	 */
	@RequestMapping("/getFileInfoByFileIdAndUserId")
	public ResponseVo getFileInfoByFileIdAndUserId(String fileId, String userId){
		return getSuccessResponseVo(fileInfoService.getFileInfoByFileIdAndUserId(fileId, userId));
	}

	/** 
	 *
	 *  根据FileIdAndUserId更新
	 */
	@RequestMapping("/updateFileInfoByFileIdAndUserId")
	public ResponseVo updateFileInfoByFileIdAndUserId(FileInfo bean, String fileId, String userId){
		return getSuccessResponseVo(fileInfoService.updateFileInfoByFileIdAndUserId(bean,fileId, userId));
	}

	/** 
	 *
	 *  根据FileIdAndUserId删除
	 */
	@RequestMapping("/deleteFileInfoByFileIdAndUserId")
	public ResponseVo deleteFileInfoByFileIdAndUserId(String fileId, String userId){
		return getSuccessResponseVo(fileInfoService.deleteFileInfoByFileIdAndUserId(fileId, userId));
	}

}
