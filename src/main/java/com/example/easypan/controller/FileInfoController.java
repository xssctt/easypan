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
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
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
public class FileInfoController extends ABaseController{

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
			UploadResultDto uploadResultDto = fileInfoService.uploadFile(sessionWebUserDto, fileId, multipartFile, fileName, fileMd5, filePid, chunkIndex, chunks);




			return getSuccessResponseVo(uploadResultDto);
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
