package com.example.easypan.service;

import java.util.Date;

import com.example.easypan.Annotation.VerifyParam;
import com.example.easypan.entity.dto.SessionWebUserDto;
import com.example.easypan.entity.dto.UploadResultDto;
import com.example.easypan.entity.po.FileInfo;
import com.example.easypan.entity.query.FileInfoQuery;
import java.util.List;
import com.example.easypan.entity.vo.PaginationResultVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;


/**
	 *
	 * @Desoription 文件信息表service 逻辑层
	 * @Auther 摸鱼
	 * @Date 2024-06-13
	 */
public interface FileInfoService {

	/** 
	 *
	 *  根据条件查询列表
	 */
	List<FileInfo> findListByParam(FileInfoQuery query);
	/** 
	 *
	 *  根据条件查询多少数量
	 */
	Integer findCountByParam(FileInfoQuery query);
	/** 
	 *
	 *  分页查询
	 */
	PaginationResultVo<FileInfo> findListByPage(FileInfoQuery query);
	/** 
	 *
	 *  新增
	 */
	Integer add(FileInfo bean);
	/** 
	 *
	 *  批量新增
	 */
	Integer addBatch(List<FileInfo> listBean);
	/** 
	 *
	 *  批量新增/修改
	 */
	Integer addOrUpdateBatch(List<FileInfo> listBean);
	/** 
	 *
	 *  根据FileIdAndUserId查询
	 */

	FileInfo getFileInfoByFileIdAndUserId(String fileId, String userId);

	/** 
	 *
	 *  根据FileIdAndUserId更新
	 */

	Integer updateFileInfoByFileIdAndUserId(FileInfo bean, String fileId, String userId);

	/** 
	 *
	 *  根据FileIdAndUserId删除
	 */

	Integer deleteFileInfoByFileIdAndUserId(String fileId, String userId);


	UploadResultDto uploadFile(SessionWebUserDto sessionWebUserDto, String fileId,
							   MultipartFile multipartFile,
							   String fileName, String fileMd5, String filePid,
							   Integer chunkIndex, Integer chunks);


}
