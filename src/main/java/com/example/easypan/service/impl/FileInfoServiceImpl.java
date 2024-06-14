package com.example.easypan.service.impl;

import java.io.File;
import java.util.Date;

import com.example.easypan.Util.DateUtils;
import com.example.easypan.Util.StringTools;
import com.example.easypan.bean.Constants;
import com.example.easypan.bean.SysSetting;
import com.example.easypan.config.AppConfig;
import com.example.easypan.entity.dto.SessionWebUserDto;
import com.example.easypan.entity.dto.UploadResultDto;
import com.example.easypan.entity.dto.UserSpaceDto;
import com.example.easypan.entity.po.FileInfo;
import com.example.easypan.entity.po.UserInfo;
import com.example.easypan.entity.query.FileInfoQuery;
import java.util.List;
import com.example.easypan.entity.query.SimplePage;
import com.example.easypan.entity.query.UserInfoQuery;
import com.example.easypan.entity.vo.PaginationResultVo;
import com.example.easypan.enums.*;
import com.example.easypan.exception.BusinessException;
import com.example.easypan.mappers.UserInfoMapper;
import com.example.easypan.redis.RedisComponent;
import com.example.easypan.service.FileInfoService;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.easypan.mappers.FileInfoMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
	 *
	 * @Desoription 文件信息表service 逻辑层
	 * @Auther 摸鱼
	 * @Date 2024-06-13
	 */
@Service("fileInfoService")
public class FileInfoServiceImpl implements FileInfoService{

	private static final Logger logger= LoggerFactory.getLogger(FileInfoServiceImpl.class);

	@Resource
	private FileInfoMapper<FileInfo,FileInfoQuery> fileInfoMapper;
	@Resource
	private RedisComponent redisComponent;
	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;
	@Resource
	private AppConfig appConfig;

	/** 
	 *
	 *  根据条件查询列表
	 */
	@Override
	public List<FileInfo> findListByParam(FileInfoQuery query){
	return this.fileInfoMapper.selectList(query);
	}
	/** 
	 *
	 *  根据条件查询多少数量
	 */
	@Override
	public Integer findCountByParam(FileInfoQuery query){
		return this.fileInfoMapper.selectCount(query);
	}
	/** 
	 *
	 *  分页查询
	 */
	@Override
	public PaginationResultVo<FileInfo> findListByPage(FileInfoQuery query){
		Integer count=this.findCountByParam(query);
		Integer pageSze= query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSze);
		query.setSimplePage(page);
		List<FileInfo> list=this.findListByParam(query);
		PaginationResultVo<FileInfo> result=new PaginationResultVo<FileInfo>(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);
		return result;
	}
	/** 
	 *
	 *  新增
	 */
	@Override
	public Integer add(FileInfo bean){
		return this.fileInfoMapper.insert(bean);
	}
	/** 
	 *
	 *  批量新增
	 */
	@Override
	public Integer addBatch(List<FileInfo> listBean){
		if (listBean == null || listBean.isEmpty()){
			return 0;
		}
		return this.fileInfoMapper.insertBatch(listBean);
	}
	/** 
	 *
	 *  批量新增/修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<FileInfo> listBean){
		if (listBean == null || listBean.isEmpty()){
			return 0;
		}
		return this.fileInfoMapper.insertOrUpdateBatch(listBean);
	}
	/** 
	 *
	 *  根据FileIdAndUserId查询
	 */

	@Override
	public FileInfo getFileInfoByFileIdAndUserId(String fileId, String userId){
		return this.fileInfoMapper.selectByFileIdAndUserId(fileId, userId);
	}

	/** 
	 *
	 *  根据FileIdAndUserId更新
	 */

	@Override
	public Integer updateFileInfoByFileIdAndUserId(FileInfo bean, String fileId, String userId){
		return this.fileInfoMapper.updateByFileIdAndUserId(bean,fileId, userId);
	}

	/** 
	 *
	 *  根据FileIdAndUserId删除
	 */
	@Override
	public Integer deleteFileInfoByFileIdAndUserId(String fileId, String userId){
		return this.fileInfoMapper.deleteByFileIdAndUserId(fileId, userId);
	}


	/**
	 *
	 * @param sessionWebUserDto
	 * @param fileId
	 * @param multipartFile
	 * @param fileName
	 * @param fileMd5
	 * @param filePid
	 * @param chunkIndex  第几分片
	 * @param chunks  多少分片
	 * @return UploadResultDto  上传文件   秒传
	 */
	@Transactional(rollbackFor = Exception.class)
	public UploadResultDto uploadFile(SessionWebUserDto sessionWebUserDto, String fileId,
									  MultipartFile multipartFile,
									  String fileName, String fileMd5, String filePid,
									  Integer chunkIndex, Integer chunks){

		UploadResultDto resultDto=new UploadResultDto();
		try {



			//没有传入文件id
			if (StringTools.isEmpty(fileId)) {
				//
				fileId=StringTools.getRandomString(Constants.LENGTH_10);
			}
			//给一个
			resultDto.setFileId(fileId);
			//上传时间
			Date curDate = new Date();
			//用户的空间大小
			UserSpaceDto spaceDto = redisComponent.getUserSpaceuse(sessionWebUserDto.getUserid());

			//第一块
			if (chunkIndex == 0) {
				//文件查询
				FileInfoQuery infoQuery = new FileInfoQuery();

				//MD5用来区分文件
				infoQuery.setFileMd5(fileMd5);
				infoQuery.setSimplePage(new SimplePage(0, 1));//分页
				infoQuery.setStatus(FileStatusEnum.USING.getStatus());//状态
				//数据库  服务器是否有这个文件
				List<FileInfo> dbFileList = fileInfoMapper.selectList(infoQuery);


				//如果有  则是秒传
				if (!dbFileList.isEmpty()) {
					//这个文件
					FileInfo dbFile = dbFileList.get(0);

					//获取文件大小
					if (dbFile.getFileSize() + spaceDto.getUserSpace() > spaceDto.getTotalUserSpace()) {
						throw new BusinessException(ResponseCodeEnum.CODE_904);
					}

					//文件信息
					dbFile.setFileId(fileId);
					dbFile.setFilePid(filePid);
					dbFile.setUserId(sessionWebUserDto.getUserid());
					dbFile.setCreateTime(curDate);
					dbFile.setLastUpdateTime(curDate);
					dbFile.setStatus(FileStatusEnum.USING.getStatus());
					dbFile.setDelFlag(FileDelFlagEnum.USING.getFlag());
					dbFile.setFileMd5(fileMd5);

					//重命名  有相同名字就更新
					fileName = autoReName(filePid, sessionWebUserDto.getUserid(), fileName);
					dbFile.setFileName(fileName);

					//插入  数据库
					fileInfoMapper.insert(dbFile);

					//属于秒传
					resultDto.setStatus(UploadStatusEnum.UPLOAD_SECONDS.getCode());

					//更新用户空间
					updateUserSpace(sessionWebUserDto, dbFile.getFileSize());


					return resultDto;

				}


			}

			// ---------------------------  临时空间         ---------------------
			//文件分片上传  不一定一下更新  放缓存  >从缓存上传具体空间< 上传完成更新具体空间
			//获取文件临时大小
			Long currentTempSize=redisComponent.getFileTempSize(sessionWebUserDto.getUserid(),fileId);
			//判断空间大小
			if (multipartFile.getSize() + currentTempSize +spaceDto.getUserSpace() > spaceDto.getTotalUserSpace()) {
				throw new BusinessException(ResponseCodeEnum.CODE_904);
			}


			//临时文件目录
			//D:XXXX + temp
			String tempFolderName=appConfig.getPath()+ Constants.FILE_FOLDER_TEMP;
			//33333 + 444aaa
			String currentUserFolderName = sessionWebUserDto.getUserid() + fileId;
			String url=tempFolderName+currentUserFolderName;
			File tempFolder = new File(url);
			if (!tempFolder.exists()){
				tempFolder.mkdirs();
			}

			//--------------文件上传-----------------------------------------
			//tempFolder.getPath() + "/" + chunkIndex  ->>tempFolderName
			File newFile = new File(tempFolder.getPath() + "/" + chunkIndex);
			multipartFile.transferTo(newFile);
			//

			if (chunkIndex < chunks -1 ){
				resultDto.setStatus(UploadStatusEnum.UPLOADING.getCode());
				//保存临时大小
				redisComponent.saveFileTempSize(sessionWebUserDto.getUserid(),fileId,multipartFile.getSize());
				return resultDto;
			}

			//最后一个分片上传完成  一部合成文件
			String month = DateUtils.format(new Date(),DateTimePatternEnum.YYYY_MM.getPattern());


		}catch (Exception e){
			logger.error("文件上传失败");
		}



		return resultDto;
	}


	/**
	 *
	 * @param filePid
	 * @param userId
	 * @param fileName
	 * @return 自动更新文件名
	 */
	private String autoReName(String filePid,String userId,String fileName){

		//
		FileInfoQuery fileInfoQuery=new FileInfoQuery();
		fileInfoQuery.setUserId(userId);
		fileInfoQuery.setFilePid(filePid);
		fileInfoQuery.setDelFlag(FileDelFlagEnum.USING.getFlag());

		//是否存在这个文件
		Integer count = fileInfoMapper.selectCount(fileInfoQuery);

		if (count > 0) {
			//重命名
			fileName = StringTools.rename(fileName);
		}

		return fileName;
	}


	/**
	 *
	 *  两部分更新   数据库  和缓存  redis存放的userspaceDto
	 *
	 * @param sessionWebUserDto
	 * @param useSpace
	 */
	public void updateUserSpace(SessionWebUserDto sessionWebUserDto,Long useSpace){

		String userId = sessionWebUserDto.getUserid();

		//更新数据库
		Integer count=userInfoMapper.updateUserSpace(userId,useSpace,null);

		if (count == 0) {
			throw new BusinessException(ResponseCodeEnum.CODE_904);
		}
		//更新 缓存
		UserSpaceDto userSpaceuse = redisComponent.getUserSpaceuse(userId);
		userSpaceuse.setUserSpace(useSpace);
		redisComponent.setUserSpaceuse(userId,userSpaceuse);

	}




}








































