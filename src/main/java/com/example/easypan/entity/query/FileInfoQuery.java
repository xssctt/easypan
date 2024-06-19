package com.example.easypan.entity.query;

import java.util.Arrays;
import java.util.Date;
import com.example.easypan.entity.query.BaseQuery;


	/** 
	 *
	 * @Desoription 文件信息表查询对象
	 * @Auther 摸鱼
	 * @Date 2024-06-13
	 */
public class FileInfoQuery extends BaseQuery{
	/** 
	 *
	 *  文件id
	 */
	private String fileId;

	private String fileIdFuzzy;

	/** 
	 *
	 *  用户id
	 */
	private String userId;

	private String userIdFuzzy;

	/** 
	 *
	 *  文件md5值
	 */
	private String fileMd5;

	private String fileMd5Fuzzy;

	/** 
	 *
	 *  父级别id
	 */
	private String filePid;

	private String filePidFuzzy;

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

	private String fileNameFuzzy;

	/** 
	 *
	 *  文件封面 图片/视频
	 */
	private String fileCover;

	private String fileCoverFuzzy;

	/** 
	 *
	 *  文件路径
	 */
	private String filePath;

	private String filePathFuzzy;

	/** 
	 *
	 *  文件创建时间
	 */
	private Date createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/** 
	 *
	 *  文件更新时间
	 */
	private Date lastUpdateTime;

	private String lastUpdateTimeStart;

	private String lastUpdateTimeEnd;

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
	private Date recoveryTime;

	private String recoveryTimeStart;

	private String recoveryTimeEnd;

	/** 
	 *
	 *  0 删除1回收站2 正常 
	 */
	private Integer delFlag;

	private String[] fileIdArray;


		public String[] getFileIdArray() {
			return fileIdArray;
		}

		public void setFileIdArray(String[] fileIdArray) {
			this.fileIdArray = fileIdArray;
		}

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

	public void setFileIdFuzzy(String fileIdFuzzy) {
		this.fileIdFuzzy=fileIdFuzzy;
	}

	public String getFileIdFuzzy() {
		return this.fileIdFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy=userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setFileMd5Fuzzy(String fileMd5Fuzzy) {
		this.fileMd5Fuzzy=fileMd5Fuzzy;
	}

	public String getFileMd5Fuzzy() {
		return this.fileMd5Fuzzy;
	}

	public void setFilePidFuzzy(String filePidFuzzy) {
		this.filePidFuzzy=filePidFuzzy;
	}

	public String getFilePidFuzzy() {
		return this.filePidFuzzy;
	}

	public void setFileNameFuzzy(String fileNameFuzzy) {
		this.fileNameFuzzy=fileNameFuzzy;
	}

	public String getFileNameFuzzy() {
		return this.fileNameFuzzy;
	}

	public void setFileCoverFuzzy(String fileCoverFuzzy) {
		this.fileCoverFuzzy=fileCoverFuzzy;
	}

	public String getFileCoverFuzzy() {
		return this.fileCoverFuzzy;
	}

	public void setFilePathFuzzy(String filePathFuzzy) {
		this.filePathFuzzy=filePathFuzzy;
	}

	public String getFilePathFuzzy() {
		return this.filePathFuzzy;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart=createTimeStart;
	}

	public String getCreateTimeStart() {
		return this.createTimeStart;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd=createTimeEnd;
	}

	public String getCreateTimeEnd() {
		return this.createTimeEnd;
	}

	public void setLastUpdateTimeStart(String lastUpdateTimeStart) {
		this.lastUpdateTimeStart=lastUpdateTimeStart;
	}

	public String getLastUpdateTimeStart() {
		return this.lastUpdateTimeStart;
	}

	public void setLastUpdateTimeEnd(String lastUpdateTimeEnd) {
		this.lastUpdateTimeEnd=lastUpdateTimeEnd;
	}

	public String getLastUpdateTimeEnd() {
		return this.lastUpdateTimeEnd;
	}

	public void setRecoveryTimeStart(String recoveryTimeStart) {
		this.recoveryTimeStart=recoveryTimeStart;
	}

	public String getRecoveryTimeStart() {
		return this.recoveryTimeStart;
	}

	public void setRecoveryTimeEnd(String recoveryTimeEnd) {
		this.recoveryTimeEnd=recoveryTimeEnd;
	}

	public String getRecoveryTimeEnd() {
		return this.recoveryTimeEnd;
	}

}