package com.example.easypan.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import com.example.easypan.Util.DateUtils;
import com.example.easypan.Util.ProcessUtil;
import com.example.easypan.Util.ScaleFilter;
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
import java.util.RandomAccess;

import com.example.easypan.entity.query.SimplePage;
import com.example.easypan.entity.query.UserInfoQuery;
import com.example.easypan.entity.vo.PaginationResultVo;
import com.example.easypan.enums.*;
import com.example.easypan.exception.BusinessException;
import com.example.easypan.mappers.UserInfoMapper;
import com.example.easypan.redis.RedisComponent;
import com.example.easypan.service.FileInfoService;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.easypan.mappers.FileInfoMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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
	//@Lazy 懒加载使用异步操作@Resource自动装配spring管理异步生效

	@Lazy
	@Resource
	private FileInfoServiceImpl fileInfoService;

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
		Boolean uploadSuccess=true;

		File tempFolder =null;
		try {

			//没有传入文件id
			if (StringTools.isEmpty(fileId)) {
				//创一个
				fileId=StringTools.getRandomString(Constants.LENGTH_10);
			}
			//给一个文件id
			resultDto.setFileId(fileId);
			//上传时间
			Date curDate = new Date();
			//用户的空间大小 useSpace  totalSpace
			UserSpaceDto spaceDto = redisComponent.getUserSpaceuse(sessionWebUserDto.getUserid());


			//TODO 第一块 校验是不是秒传
			if (chunkIndex == 0) {
				logger.info("秒传入口判断");
				//文件查询
				FileInfoQuery infoQuery = new FileInfoQuery();

				//MD5用来区分文件
				infoQuery.setFileMd5(fileMd5);
				infoQuery.setSimplePage(new SimplePage(0, 1));//分页
				infoQuery.setStatus(FileStatusEnum.USING.getStatus());//状态
				//数据库  服务器是否有这个文件
				List<FileInfo> dbFileList = fileInfoMapper.selectList(infoQuery);


				//logger.info(dbFileList.toString());
				//如果有  则是秒传
				if (!dbFileList.isEmpty()) {
					logger.info("是秒传");
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
					fileInfoMapper.insertOrUpdate(dbFile);

					//属于秒传
					resultDto.setStatus(UploadStatusEnum.UPLOAD_SECONDS.getCode());

//					//更新用户空间
//					updateUserSpace(sessionWebUserDto, dbFile.getFileSize());


					return resultDto;

				}

				logger.info("不是秒传");
			}

			logger.info("临时存储进入");
			// ---------------------------  临时空间         ---------------------
			//文件分片上传  不一定一下更新  放缓存  >从缓存上传具体空间< 上传完成更新具体空间
			//获取 >文件临时大小<  每传一片  计算空间
			Long currentTempSize=redisComponent.getFileTempSize(sessionWebUserDto.getUserid(),fileId);
			//判断空间大小
			if (multipartFile.getSize() + currentTempSize +spaceDto.getUserSpace() > spaceDto.getTotalUserSpace()) {
				throw new BusinessException(ResponseCodeEnum.CODE_904);
			}


			//临时文件目录
			//D:XXXX + temp   》temp目录名字《     目录
			String tempFolderName=appConfig.getPath()+ Constants.FILE_FOLDER_TEMP;
			//33333 + 444aaa   》扩展名字 用户id加文件id《    目录
			String currentUserFolderName = sessionWebUserDto.getUserid() + fileId;

			//临时   完整文件目录路径    文件最终  目录
			String url=tempFolderName+currentUserFolderName;
			tempFolder = new File(url);
			if (!tempFolder.exists()){
				tempFolder.mkdirs();
			}

			//--------------临时 文件上传-----------------------------------------
			//tempFolder.getPath() + "/" + chunkIndex  ->>tempFolderName  完整文件目录路径+第几片   文件路径
			File newFile = new File(tempFolder.getPath() + "/" + chunkIndex);
			multipartFile.transferTo(newFile);
			logger.info("临时文件上传成功");
			//--------------临时 文件上传成功-----------------------------------------

			//分片小于总的分片数  返回保存状态
			if (chunkIndex < chunks -1 ){
				//返回状态 上传中
				resultDto.setStatus(UploadStatusEnum.UPLOADING.getCode());
				logger.info("--------------------------------"+String.valueOf(multipartFile.getSize()));
				//保存临时大小
				redisComponent.saveFileTempSize(sessionWebUserDto.getUserid(),fileId,multipartFile.getSize());
				return resultDto;
			}
			//最后一片，或者不需要分片
			//保存临时大小
			redisComponent.saveFileTempSize(sessionWebUserDto.getUserid(),fileId,multipartFile.getSize());

			//临时    最后一个分片上传完成  异步合成文件

//			//事务没有提交会有问题  需要事务提交后执行
//			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//				@Override
//				public void afterCommit() {
//					fileInfoService.transferFile(,sessionWebUserDto);
//				}
//			});


			//更新数据库信息
			//根据时间 二次扩展名字  --   》month《
			String month = DateUtils.format(curDate,DateTimePatternEnum.YYYY_MM.getPattern());
			//文件后缀
			String fileSuffix = StringTools.getFileNameSuffix(fileName);
//			logger.info("========================="+fileSuffix+"========================");
//			logger.info(fileName);
			//真实的文件名字
			String realFileName = currentUserFolderName + fileSuffix;
			logger.info(realFileName);


			//获取详细文件类型
			FileTypeEnum fileTypeEnum = FileTypeEnum.getFileTypeBySuffix(fileSuffix);
			//自动重命名
			fileName=autoReName(filePid,sessionWebUserDto.getUserid(),fileName);
			//--------------------------------
			//TODO 文件id相同
			//-------------------------------------
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileId(fileId);
			fileInfo.setUserId(sessionWebUserDto.getUserid());
			fileInfo.setFileMd5(fileMd5);
			fileInfo.setFileName(fileName);
			fileInfo.setFilePath(month+"/"+realFileName);
			fileInfo.setFilePid(filePid);
			fileInfo.setCreateTime(curDate);
			fileInfo.setLastUpdateTime(curDate);
			//文件分类
			fileInfo.setFileCategory(fileTypeEnum.getFileCategoryEnum().getCategory());
			//文件具体类型
			fileInfo.setFileType(fileTypeEnum.getType());
			fileInfo.setStatus(FileStatusEnum.TRANSFER.getStatus());
			fileInfo.setFolderType(FileFolderTypeEnum.FILE.getFolderType());
			fileInfo.setDelFlag(FileDelFlagEnum.USING.getFlag());
			//-------------------------------------

			//保存
			fileInfoMapper.insertOrUpdate(fileInfo);

			logger.info("========================================更新用户使用空间");
			//更新用户使用空间
			Long thisFileTotalSpace = redisComponent.getFileTempSize(sessionWebUserDto.getUserid(), fileId);
			logger.info(String.valueOf(thisFileTotalSpace));//0
			updateUserSpace(sessionWebUserDto,thisFileTotalSpace);

			//更新上传状态
			resultDto.setStatus(UploadStatusEnum.UPLOAD_FINISH.getCode());

			//临时   TODO 最后一个分片上传完成  异步合成文件
			//事务没有提交会有问题  需要事务提交后执行
			logger.info("异步合成文件入口");
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					fileInfoService.transferFile(fileInfo.getFileId(),sessionWebUserDto);
				}
			});



			return resultDto;
		}catch (BusinessException e){
			logger.error("文件上传失败",e);
			uploadSuccess=false;
			throw e;
		}catch (Exception e){
			logger.error("文件上传失败",e);
			uploadSuccess=false;
		}finally {
			logger.info(uploadSuccess.toString());
			//TODO 失败操作  清除之前已经下载的
			if (!uploadSuccess && tempFolder != null){
				try {
					//目录  tempFolder  文件清除
					logger.info("tempFolder文件清除");
					FileUtils.deleteDirectory(tempFolder);
				} catch (IOException e) {
					logger.error("删除临时目录失败",e);
					e.printStackTrace();
				}
			}
		}

		//resultDto.setStatus(UploadStatusEnum.UPLOAD_FINISH.getCode());
		return resultDto;
	}


	/**
	 *
	 * @param filePid
	 * @param userId
	 * @param fileName
	 * @return 自动更新文件名  如果有该文件 重新命名
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
			String suffix = StringTools.getFileNameSuffix(fileName);
			fileName = StringTools.rename(fileName)+suffix;

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
	@Transactional(rollbackFor = Exception.class)
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

	/**
	 *   合并文件
	 * @param fileId  文件id
	 * @param sessionWebUserDto userid
	 */

	@Async
	public void transferFile(String fileId,SessionWebUserDto sessionWebUserDto){
		logger.info("异步合成文件进入");

		Boolean transferSuccess=true;
		//保存目标文件
		String targetFilePath=null;
		//文件封面
		String cover=null;
		//fileTypeEnum
		FileTypeEnum fileTypeEnum=null;
		//缩略图的大小
		Long coverSize =0L;
		//  待转码 文件
		FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, sessionWebUserDto.getUserid());



		try{
			//文件是否存在 以及文件按是否待转码
			if (fileInfo == null || !FileStatusEnum.TRANSFER.getStatus().equals(fileInfo.getStatus())){
				return;
			}

			//临时目录  mmm / temo/
			String tempFolserName = appConfig.getPath() + Constants.FILE_FOLDER_TEMP;
			// 8238489jhkdf
			String currentUserFolderName=sessionWebUserDto.getUserid()+fileId;
			//mmm / temp/8238489jhkdf/
			File fileFolder = new File(tempFolserName + currentUserFolderName);

			//----------------   文件要保存路径    fileInfo.getFilePath()--> moth / userid+fileId + .suffix          --------------------
			String fileSuffix = StringTools.getFileNameSuffix(fileInfo.getFileName());
			String month=DateUtils.format(fileInfo.getCreateTime(),DateTimePatternEnum.YYYY_MM.getPattern());

			//文件需要保存目录  mmm / file/
			String targetFileName = appConfig.getPath() + Constants.FILE_FOLDER_FILE;
			//targetFileName +month  -->  mmm/file/>month<     目录
			File targetFolder = new File(targetFileName + "/" + month);


			if (!targetFolder.exists()){
				targetFolder.mkdirs();
			}
			//fileInfo.getFilePath()---> moth / userid+fileId + .suffix
			String realFileName = currentUserFolderName + fileSuffix;
			//文件完整路径   jfff/file/moth/useridfileid.suffix
			targetFilePath = targetFolder.getPath() + "\\" + realFileName;

			//TODO{ targetFileName （到file）主目录  targetFolder（存放目标的目录）目标目录  targetFilePath 文件路径
			//   tempFolserName 临时主目录     currentUserFolderName 第一此扩展名（userid+fileid）  month 二次扩展名(date)}
			//合并文件
			union(fileFolder.getPath(),targetFilePath,fileInfo.getFileName(),true);
			//TODO 此时temp已经清除
			//redis 也清除
			//redisComponent.deleteTempFile(sessionWebUserDto.getUserid(),fileId);



			fileTypeEnum=FileTypeEnum.getFileTypeByType(fileInfo.getFileType());


			//如果文件是视频  切割
			if (FileTypeEnum.VIDEO==fileTypeEnum){
				logger.info("视频缩略图进入");
				cutFile4Video(fileId,targetFilePath);

				//视频缩略图
				cover=month+"/"+currentUserFolderName+Constants.IMAGE_PNG_SUFFIX;
				String covrePath=targetFileName+"/"+cover;
				ScaleFilter.createCover(new File(targetFilePath),
						Constants.LENGTH_150,
						new File(covrePath),
						false);



//				//扣除缩略图大小
				 coverSize = new File(covrePath).length();

				logger.info("视频缩略图完成");
			} else if (FileTypeEnum.IMAGE==fileTypeEnum) {
				logger.info("图片缩略图进入");
				//图片缩略图 命名 xx_.png
				cover=month+"/"+realFileName.replace(".","_.");
				String coverPath=targetFileName+"/"+cover;
				Boolean created = ScaleFilter.createThumbnailWidthFFmpeg(new File(targetFilePath),
						Constants.LENGTH_150,
						new File(coverPath),
						false,
						false);

				if (!created){
					FileUtils.copyFile(new File(targetFilePath),new File(coverPath));
				}

				coverSize = new File(coverPath).length();
				logger.info("图片缩略图完成");
			}


		}catch (Exception e){
			logger.error("文件转码失败文件id:{}用户id:{}",fileId,sessionWebUserDto.getUserid(),e);
			transferSuccess=false;
		}finally {
			//TODO 最后才更新文件大小
			//文件更新状态
			FileInfo updateFile=new FileInfo();
			updateFile.setFileSize(new File(targetFilePath).length()+coverSize);
			//cover null
			updateFile.setFileCover(cover);
			updateFile.setStatus(transferSuccess ? FileStatusEnum.USING.getStatus() : FileStatusEnum.TRANSFER_FAIL.getStatus());

			//数据库
			fileInfoMapper.updateFileStatusWithOldStatus(fileId,
					sessionWebUserDto.getUserid(),
					updateFile,
					FileStatusEnum.TRANSFER.getStatus());

			//缩略图大小 更新用
			logger.info("========================================更新用户使用空间2");
			//更新数据库
			Integer count=userInfoMapper.updateUserSpace(sessionWebUserDto.getUserid(),coverSize,null);
			if (count == 0) {
				throw new BusinessException(ResponseCodeEnum.CODE_904);
			}
			//更新 缓存
			UserSpaceDto userSpaceuse = redisComponent.getUserSpaceuse(sessionWebUserDto.getUserid());
			Long userSpace = userSpaceuse.getUserSpace();
			userSpaceuse.setUserSpace(coverSize+userSpace);
			redisComponent.setUserSpaceuse(sessionWebUserDto.getUserid(),userSpaceuse);
		}

	}

	/**
	 *
	 * @param dirPath  临时文件夹目录
	 * @param toFilePath  要保存成那个文件的路径
	 * @param fileName  文件名
	 * @param delSource  是否删除临时文件
	 */
	private void union(String dirPath,String toFilePath,String fileName,Boolean delSource){

		//临时目录
		File dir = new File(dirPath);
		if (!dir.exists()){
			throw new BusinessException("目录不存在");
		}
		// 找到下面所有 分片   dir结束
		File[] fileList = dir.listFiles();

		//保存文件流  targetFile最终合并的文件 file
		File targetFile = new File(toFilePath);
		logger.info(String.valueOf(targetFile.length()));
		//输出流   RandomAccessFile
		RandomAccessFile writeFile=null;

		try {
			//RandomAccessFile  targetFile  输出流
			writeFile=new RandomAccessFile(targetFile,"rw");
			//缓存
			byte[] bytes = new byte[1024 * 10];
			// 找到下面所有 分片
			for (int i = 0; i < fileList.length; i++) {

				int len=-1;
				//每个分片 dirPath/i
				File chunkFile = new File(dirPath + "/" + i);

				//
				RandomAccessFile readFile=null;
				try {
					//读取所有分片
					readFile=new RandomAccessFile(chunkFile,"r");

					//读取到 bytes
					while ( ( len=readFile.read(bytes) ) != -1){
						//写出 writeFile  RandomAccessFile 可以把所有分片合并
						writeFile.write(bytes,0,len);
					}

				}catch (Exception e){
					logger.error("合并分片失败",e);
					throw new BusinessException("合并分片失败");
				}finally {
					readFile.close();
				}


			}


		}catch (Exception e){
			logger.error("合并文件:{}失败",fileName,e);
			throw new BusinessException("合并文件"+fileName+"出错了");

		}finally {
			if (null != writeFile){
				try {
					writeFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			//临时文件使用好删除临时文件
			if (delSource && dir.exists()){
				try {
					//TODO 成功清除缓存
					FileUtils.deleteDirectory(dir);
					//缓存的临时文件信息删除

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}





	}


	//TODO  ffmpeg mp4->ts  ts切割
	private void cutFile4Video(String fileId,String videoFilePath){
		logger.info(" ffmpeg mp4->ts入口");
		logger.info(videoFilePath);
		//创建同名切片目录
		File toFolder = new File(videoFilePath.substring(0, videoFilePath.lastIndexOf(".")));
		if (!toFolder.exists()){
			toFolder.mkdirs();
		}
		logger.info(videoFilePath.substring(0, videoFilePath.lastIndexOf(".")));
		//TODO  ffmpeg mp4->ts  ts切割
		//ffmpeg -y -i %s -vcodec copy -acodec copy -bsf:v h264_mp4toannexb %s
		//ffmpeg -y -i %s  -vcodec copy -acodec copy -vbsf h264_mp4toannexb %s
		final String CMD_TRANSFER_2TS="ffmpeg -y -i %s -vcodec copy -acodec copy -bsf:v h264_mp4toannexb %s";
		final String CMD_CUT_TS="ffmpeg -i %s -c copy -map 0 -f segment -segment_list %s -segment_time 30 %s/%s_%%4d.ts";


		String tsPath = toFolder+"\\"+Constants.TS_NAME;
		logger.info(tsPath);
		//生成.ts
		String cmd=String.format(CMD_TRANSFER_2TS,videoFilePath,tsPath);
		ProcessUtil.executeCommand(cmd,true);

		//生成索引文件 .m3u8 和切片.ts
		cmd=String.format(CMD_CUT_TS,
				tsPath,
				toFolder.getPath()+"\\"+Constants.M3U8_NAME,
				toFolder.getPath(),
				fileId);
		ProcessUtil.executeCommand(cmd,true);
		//删除index.ts
		new File(tsPath).delete();

	}



	@Override
	public FileInfo newFolder(String filePid, String userId, String folderName){
		checkFileName(filePid,userId,folderName,FileFolderTypeEnum.FOLDER.getFolderType());
		Date curDate = new Date();

		FileInfo fileInfo = new FileInfo();
		fileInfo.setFileId(StringTools.getRandomString(Constants.LENGTH_10));
		fileInfo.setUserId(userId);
		fileInfo.setFilePid(filePid);
		fileInfo.setCreateTime(curDate);
		fileInfo.setLastUpdateTime(curDate);
		fileInfo.setFileName(folderName);
		fileInfo.setStatus(FileStatusEnum.USING.getStatus());
		fileInfo.setFolderType(FileFolderTypeEnum.FOLDER.getFolderType());
		fileInfo.setDelFlag(FileDelFlagEnum.USING.getFlag());

		fileInfoMapper.insert(fileInfo);
		return fileInfo;
	}

	private void checkFileName(String filePid, String userId, String folderName,Integer folderType){

		FileInfoQuery fileInfoQuery=new FileInfoQuery();
		fileInfoQuery.setFilePid(filePid);
		fileInfoQuery.setUserId(userId);
		fileInfoQuery.setFileName(folderName);
		fileInfoQuery.setFolderType(folderType);
		Integer count = fileInfoMapper.selectCount(fileInfoQuery);

		if (count > 0){
			throw new BusinessException("此目录下已经存在重名，请重新创建");
		}

	}



}








































