package com.example.easypan.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.example.easypan.enums.DateTimePatternEnum;
import com.example.easypan.Util.DateUtils;


	/** 
	 *
	 * @Desoription 用户信息
	 * @Auther 摸鱼
	 * @Date 2024-06-04
	 */
public class UserInfo implements Serializable{
	/** 
	 *
	 *  用户id
	 */
	private String userId;

	/** 
	 *
	 *  昵称
	 */
	private String nickName;

	/** 
	 *
	 *  邮箱
	 */
	private String email;

	/** 
	 *
	 *  qq用户id
	 */
	private String qqOpenId;

	/** 
	 *
	 *  qq用户头像
	 */
	private String qqAvatar;

	/** 
	 *
	 *  密码
	 */
	private String password;

	/** 
	 *
	 *  加入时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date joinTime;

	/** 
	 *
	 *  最后登录时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;

	/** 
	 *
	 *  0禁用1正常
	 */
	private Integer status;

	/** 
	 *
	 *  使用空间
	 */
	private Long useSpace;

	/** 
	 *
	 *  总空间
	 */
	private Long totalSpace;

	public void setUserId(String userId) {
		this.userId=userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setNickName(String nickName) {
		this.nickName=nickName;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setEmail(String email) {
		this.email=email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setQqOpenId(String qqOpenId) {
		this.qqOpenId=qqOpenId;
	}

	public String getQqOpenId() {
		return this.qqOpenId;
	}

	public void setQqAvatar(String qqAvatar) {
		this.qqAvatar=qqAvatar;
	}

	public String getQqAvatar() {
		return this.qqAvatar;
	}

	public void setPassword(String password) {
		this.password=password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime=joinTime;
	}

	public Date getJoinTime() {
		return this.joinTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime=lastLoginTime;
	}

	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setStatus(Integer status) {
		this.status=status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setUseSpace(Long useSpace) {
		this.useSpace=useSpace;
	}

	public Long getUseSpace() {
		return this.useSpace;
	}

	public void setTotalSpace(Long totalSpace) {
		this.totalSpace=totalSpace;
	}

	public Long getTotalSpace() {
		return this.totalSpace;
	}

	@Override
	public String toString() {
	return " FieIdInfo{ " + 
	 " 用户id : UserId='" +(userId==null ? "空" : userId) + "' "+
	 " 昵称 : NickName='" +(nickName==null ? "空" : nickName) + "' "+
	 " 邮箱 : Email='" +(email==null ? "空" : email) + "' "+
	 " qq用户id : QqOpenId='" +(qqOpenId==null ? "空" : qqOpenId) + "' "+
	 " qq用户头像 : QqAvatar='" +(qqAvatar==null ? "空" : qqAvatar) + "' "+
	 " 密码 : Password='" +(password==null ? "空" : password) + "' "+
	 " 加入时间 : JoinTime='" +(joinTime==null ? "空" : DateUtils.format(joinTime,DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "' "+
	 " 最后登录时间 : LastLoginTime='" +(lastLoginTime==null ? "空" : DateUtils.format(lastLoginTime,DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "' "+
	 " 0禁用1正常 : Status='" +(status==null ? "空" : status) + "' "+
	 " 使用空间 : UseSpace='" +(useSpace==null ? "空" : useSpace) + "' "+
	 " 总空间 : TotalSpace='" +(totalSpace==null ? "空" : totalSpace) + "' "+ '}';
	}
}