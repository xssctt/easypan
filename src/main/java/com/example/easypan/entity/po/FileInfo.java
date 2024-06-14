package com.example.easypan.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.example.easypan.enums.DateTimePatternEnum;
import com.example.easypan.Util.DateUtils;


	/** 
	 *
	 * @Desoription 文件信息表
	 * @Auther 摸鱼
	 * @Date 2024-06-13
	 */
public class FileInfo implements Serializable{
	/** 
	 *
	 *  文件id
	 */
	private String fileId;

	/** 
	 *
	 *  用户id
	 */
	private String userId;

	/** 
	 *
	 *  文件md5值
	 */
	private String fileMd5;

	/** 
	 *
	 *  父级别id
	 */
	private String filePid;

	/** 
	 *
	 *  文件大小
	 */
	private Long fileSize;

	/** 
	 *
	 *  文件名
	 */
	private String fileName;

	/** 
	 *
	 *  文件封面 图片/视频
	 */
	private String fileCover;

	/** 
	 *
	 *  文件路径
	 */
	private String filePath;

	/** 
	 *
	 *  文件创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/** 
	 *
	 *  文件更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateTime;

	/** 
	 *
	 *  文件 目录
	 */
	private Integer folderType;

	/** 
	 *
	 *  1视频 2音频 3图片 4文档 5其他
	 */
	private Integer fileCategory;

	/** 
	 *
	 *  1视频 2音频 3图片 4pdf 5doc 6exce 7txt 8 code 9 zip 10其他
	 */
	private Integer fileType;

	/** 
	 *
	 *  0 转码中1转码成功 2转码失败 
	 */
	private Integer status;

	/** 
	 *
	 *  回收站存放时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date recoveryTime;

	/** 
	 *
	 *  0 删除1回收站2 正常 
	 */
	private Integer delFlag;

	public void setFileId(String fileId) {
		this.fileId=fileId;
	}

	public String getFileId() {
		return this.fileId;
	}

	public void setUserId(String userId) {
		this.userId=userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5=fileMd5;
	}

	public String getFileMd5() {
		return this.fileMd5;
	}

	public void setFilePid(String filePid) {
		this.filePid=filePid;
	}

	public String getFilePid() {
		return this.filePid;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize=fileSize;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileName(String fileName) {
		this.fileName=fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileCover(String fileCover) {
		this.fileCover=fileCover;
	}

	public String getFileCover() {
		return this.fileCover;
	}

	public void setFilePath(String filePath) {
		this.filePath=filePath;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setCreateTime(Date createTime) {
		this.createTime=createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime=lastUpdateTime;
	}

	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setFolderType(Integer folderType) {
		this.folderType=folderType;
	}

	public Integer getFolderType() {
		return this.folderType;
	}

	public void setFileCategory(Integer fileCategory) {
		this.fileCategory=fileCategory;
	}

	public Integer getFileCategory() {
		return this.fileCategory;
	}

	public void setFileType(Integer fileType) {
		this.fileType=fileType;
	}

	public Integer getFileType() {
		return this.fileType;
	}

	public void setStatus(Integer status) {
		this.status=status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setRecoveryTime(Date recoveryTime) {
		this.recoveryTime=recoveryTime;
	}

	public Date getRecoveryTime() {
		return this.recoveryTime;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag=delFlag;
	}

	public Integer getDelFlag() {
		return this.delFlag;
	}

	@Override
	public String toString() {
	return " FieIdInfo{ " + 
	 " 文件id : FileId='" +(fileId==null ? "空" : fileId) + "' "+
	 " 用户id : UserId='" +(userId==null ? "空" : userId) + "' "+
	 " 文件md5值 : FileMd5='" +(fileMd5==null ? "空" : fileMd5) + "' "+
	 " 父级别id : FilePid='" +(filePid==null ? "空" : filePid) + "' "+
	 " 文件大小 : FileSize='" +(fileSize==null ? "空" : fileSize) + "' "+
	 " 文件名 : FileName='" +(fileName==null ? "空" : fileName) + "' "+
	 " 文件封面 图片/视频 : FileCover='" +(fileCover==null ? "空" : fileCover) + "' "+
	 " 文件路径 : FilePath='" +(filePath==null ? "空" : filePath) + "' "+
	 " 文件创建时间 : CreateTime='" +(createTime==null ? "空" : DateUtils.format(createTime,DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "' "+
	 " 文件更新时间 : LastUpdateTime='" +(lastUpdateTime==null ? "空" : DateUtils.format(lastUpdateTime,DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "' "+
	 " 文件 目录 : FolderType='" +(folderType==null ? "空" : folderType) + "' "+
	 " 1视频 2音频 3图片 4文档 5其他 : FileCategory='" +(fileCategory==null ? "空" : fileCategory) + "' "+
	 " 1视频 2音频 3图片 4pdf 5doc 6exce 7txt 8 code 9 zip 10其他 : FileType='" +(fileType==null ? "空" : fileType) + "' "+
	 " 0 转码中1转码成功 2转码失败  : Status='" +(status==null ? "空" : status) + "' "+
	 " 回收站存放时间 : RecoveryTime='" +(recoveryTime==null ? "空" : DateUtils.format(recoveryTime,DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "' "+
	 " 0 删除1回收站2 正常  : DelFlag='" +(delFlag==null ? "空" : delFlag) + "' "+ '}'; 
	}
}