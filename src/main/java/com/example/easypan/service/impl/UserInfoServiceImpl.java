package com.example.easypan.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;


import com.example.easypan.Util.JsonUtil;
import com.example.easypan.Util.OKHttpUtil;
import com.example.easypan.Util.StringTools;
import com.example.easypan.bean.Constants;
import com.example.easypan.bean.SysSetting;
import com.example.easypan.config.AppConfig;
import com.example.easypan.entity.dto.QQInfoDto;
import com.example.easypan.entity.dto.SessionWebUserDto;
import com.example.easypan.entity.dto.UserSpaceDto;
import com.example.easypan.entity.po.EmailCode;
import com.example.easypan.entity.po.FileInfo;
import com.example.easypan.entity.po.UserInfo;
import com.example.easypan.entity.query.EmailCodeQuery;
import com.example.easypan.entity.query.FileInfoQuery;
import com.example.easypan.entity.query.UserInfoQuery;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.example.easypan.entity.query.SimplePage;
import com.example.easypan.entity.vo.PaginationResultVo;
import com.example.easypan.enums.PageSize;
import com.example.easypan.enums.UserStatusEnum;
import com.example.easypan.exception.BusinessException;
import com.example.easypan.mappers.EmailCodeMapper;
import com.example.easypan.mappers.FileInfoMapper;
import com.example.easypan.redis.RedisComponent;
import com.example.easypan.redis.RedisUtil;
import com.example.easypan.service.EmailCodeService;
import com.example.easypan.service.FileInfoService;
import com.example.easypan.service.UserInfoService;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.easypan.mappers.UserInfoMapper;
import org.springframework.transaction.annotation.Transactional;


/**
	 *
	 * @Desoription 用户信息service 逻辑层
	 * @Auther 摸鱼
	 * @Date 2024-06-04
	 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService{

	private static final Logger logger= LoggerFactory.getLogger(UserInfoServiceImpl.class);


	@Resource
	private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;
	@Resource
	private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;
	@Resource
	private EmailCodeService emailCodeService;
	@Resource
	private RedisUtil redisUtil;

	@Resource
	private RedisComponent redisComponent;
	@Resource
	private AppConfig appConfig;
	@Resource
	private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;

	/** 
	 *
	 *  根据条件查询列表
	 */
	public List<UserInfo> findListByParam(UserInfoQuery query){
	return this.userInfoMapper.selectList(query);
	}
	/** 
	 *
	 *  根据条件查询多少数量
	 */
	public Integer findCountByParam(UserInfoQuery query){
		return this.userInfoMapper.selectCount(query);
	}
	/** 
	 *
	 *  分页查询
	 */
	public PaginationResultVo<UserInfo> findListByPage(UserInfoQuery query){
		Integer count=this.findCountByParam(query);
		Integer pageSze= query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSze);
		query.setSimplePage(page);
		List<UserInfo> list=this.findListByParam(query);
		PaginationResultVo<UserInfo> result=new PaginationResultVo<UserInfo>(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);
		return result;
	}
	/** 
	 *
	 *  新增
	 */
	public Integer add(UserInfo bean){
		return this.userInfoMapper.insert(bean);
	}
	/** 
	 *
	 *  批量新增
	 */
	public Integer addBatch(List<UserInfo> listBean){
		if (listBean == null || listBean.isEmpty()){
			return 0;
		}
		return this.userInfoMapper.insertBatch(listBean);
	}
	/** 
	 *
	 *  批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<UserInfo> listBean){
		if (listBean == null || listBean.isEmpty()){
			return 0;
		}
		return this.userInfoMapper.insertOrUpdateBatch(listBean);
	}
	/** 
	 *
	 *  根据UserId查询
	 */

	public UserInfo getUserInfoByUserId(String userId){
		return this.userInfoMapper.selectByUserId(userId);
	}

	/** 
	 *
	 *  根据UserId更新
	 */

	public Integer updateUserInfoByUserId(UserInfo bean, String userId){
		return this.userInfoMapper.updateByUserId(bean,userId);
	}

	/** 
	 *
	 *  根据UserId删除
	 */

	public Integer deleteUserInfoByUserId(String userId){
		return this.userInfoMapper.deleteByUserId(userId);
	}

	/** 
	 *
	 *  根据Email查询
	 */

	public UserInfo getUserInfoByEmail(String email){
		return this.userInfoMapper.selectByEmail(email);
	}

	/** 
	 *
	 *  根据Email更新
	 */

	public Integer updateUserInfoByEmail(UserInfo bean, String email){
		return this.userInfoMapper.updateByEmail(bean,email);
	}

	/** 
	 *
	 *  根据Email删除
	 */

	public Integer deleteUserInfoByEmail(String email){
		return this.userInfoMapper.deleteByEmail(email);
	}

	/** 
	 *
	 *  根据QqOpenId查询
	 */

	public UserInfo getUserInfoByQqOpenId(String qqOpenId){
		return this.userInfoMapper.selectByQqOpenId(qqOpenId);
	}

	/** 
	 *
	 *  根据QqOpenId更新
	 */

	public Integer updateUserInfoByQqOpenId(UserInfo bean, String qqOpenId){
		return this.userInfoMapper.updateByQqOpenId(bean,qqOpenId);
	}

	/** 
	 *
	 *  根据QqOpenId删除
	 */

	public Integer deleteUserInfoByQqOpenId(String qqOpenId){
		return this.userInfoMapper.deleteByQqOpenId(qqOpenId);
	}

	/** 
	 *
	 *  根据NickName查询
	 */

	public UserInfo getUserInfoByNickName(String nickName){
		return this.userInfoMapper.selectByNickName(nickName);
	}

	/** 
	 *
	 *  根据NickName更新
	 */

	public Integer updateUserInfoByNickName(UserInfo bean, String nickName){
		return this.userInfoMapper.updateByNickName(bean,nickName);
	}

	/** 
	 *
	 *  根据NickName删除
	 */

	public Integer deleteUserInfoByNickName(String nickName){
		return this.userInfoMapper.deleteByNickName(nickName);
	}

	@Transactional(rollbackFor = Exception.class)
	public void register(String email, String nickName, String password,String emailCode){
		//验证邮箱验证码是否正确
//		String codeRedis =(String) redisUtil.getCacheObject(Constants.REDIS_KEYS_EMAIL_CODE_REGISTER);
//		EmailCode codeJdbc = emailCodeMapper.selectByEmailAndCode(email, emailCode);
//		if(codeRedis == null || codeJdbc.getStatus() == 1){
//			throw new BusinessException("邮箱验证码不存在或者过期");
//		}
//		if (codeJdbc == null || !codeRedis.equals(emailCode)){
//			throw new BusinessException("邮箱验证码错误");
//		}


		UserInfo userInfoByEmail =userInfoMapper.selectByEmail(email);
		if (userInfoByEmail != null){
			throw new BusinessException("用户已注册");
		}
		UserInfo userInfoByNickName = userInfoMapper.selectByNickName(nickName);
		if (userInfoByNickName != null){
			throw new BusinessException("昵称已存在");
		}

		emailCodeService.checkCode(email,emailCode,0);


		UserInfo userInfo=new UserInfo();
		userInfo.setEmail(email);
		userInfo.setNickName(nickName);
		userInfo.setPassword(StringTools.encodingByMd5(password));
		userInfo.setJoinTime(new Date());
		userInfo.setUserId(StringTools.getRandomNumber(Constants.LENGTH_10));
		userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
		SysSetting sysSetting = redisComponent.getSysSetting();

		//用户空间
		userInfo.setTotalSpace(sysSetting.getUserInitUserSpace() * Constants.MB );
		userInfo.setUseSpace(0L);
		userInfoMapper.insert(userInfo);

		//注册成功
		//缓存验证码清除 数据库清除
		redisUtil.expire(Constants.REDIS_KEYS_EMAIL_CODE_REGISTER,0, TimeUnit.SECONDS);
		emailCodeMapper.disableEmailCode(email);
	}


	public SessionWebUserDto login(String email,String password){

		String passwordMd5 = StringTools.encodingByMd5(password);

		UserInfo userInfo = userInfoMapper.selectByEmail(email);

		if (userInfo == null || !passwordMd5.equals(userInfo.getPassword())){
			throw new BusinessException("账户或者密码错误");
		}

		if (userInfo.getStatus() == 0){
			throw new BusinessException("账户状态异常");
		}

		UserInfo updateUser = new UserInfo();
		updateUser.setLastLoginTime(new Date());
		userInfoMapper.updateByUserId(updateUser,updateUser.getUserId());

		SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
		sessionWebUserDto.setUserid(userInfo.getUserId());
		sessionWebUserDto.setNickname(userInfo.getNickName());

		if (ArrayUtils.contains( appConfig.getAdminEmail().split(",") , email)){
			sessionWebUserDto.setAdmin(true);
		}else {
			sessionWebUserDto.setAdmin(false);
		}
		//sessionWebUserDto.setAvatar();

		UserSpaceDto userSpaceDto=new UserSpaceDto();
		userSpaceDto.setTotalUserSpace(userInfo.getTotalSpace());
		//userSpaceDto.setUserSpace();  计算用户的使用空间
		// TODO 需要查询用户使用的
		Long userSpace = fileInfoMapper.selectUserSpace(userInfo.getUserId());
		userSpaceDto.setUserSpace(userSpace);
		redisComponent.setUserSpaceuse(userInfo.getUserId(),userSpaceDto);


		return sessionWebUserDto;
	}


	@Transactional(rollbackFor = Exception.class)
	public void sesetPwd(String email, String password, String emailCode){

		//验证email
		UserInfo userInfo = userInfoMapper.selectByEmail(email);
		if (userInfo == null){
			throw new BusinessException("账户不存在");
		}
		//yanzheng code
		emailCodeService.checkCode(email,emailCode,1);
		//更新密码
		UserInfo updateUser=new UserInfo();
		updateUser.setPassword(StringTools.encodingByMd5(password));
		userInfoMapper.updateByEmail(updateUser,email);

	}



	public SessionWebUserDto qqlogin(String code){

		//通过回调code 获取accessToken
		String qqAccessToken = getQQAccessToken(code);
		//获取 qq opentID
		String qqOpenID = getQqOpenID(qqAccessToken);

		UserInfo userInfo = userInfoMapper.selectByQqOpenId(qqOpenID);
		String avatar=null;
		if (null == userInfo){
			QQInfoDto qqUserInfo = getQQUserInfo(qqAccessToken, qqOpenID);

			userInfo=new UserInfo();
			String nickName=qqUserInfo.getNickName();

			nickName=nickName.length() > Constants.LENGTH_15 ? nickName.substring(0,Constants.LENGTH_15) : nickName;
			avatar=StringTools.isEmpty(qqUserInfo.getFigureurl_qq_2()) ? qqUserInfo.getFigureurl_qq_1() : qqUserInfo.getFigureurl_qq_2();
			Date date = new Date();

			userInfo.setQqAvatar(avatar);
			userInfo.setUserId(StringTools.getRandomNumber(Constants.LENGTH_10));
			userInfo.setNickName(nickName);
			userInfo.setQqOpenId(qqOpenID);
			userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
			userInfo.setJoinTime(date);
			userInfo.setLastLoginTime(date);

			userInfo.setTotalSpace(redisComponent.getSysSetting().getUserInitUserSpace() * Constants.MB);
			userInfo.setUseSpace(0L);

			userInfoMapper.insert(userInfo);
			userInfo = userInfoMapper.selectByQqOpenId(qqOpenID);
		}else {
			UserInfo updateuserInfo = new UserInfo();
			updateuserInfo.setLastLoginTime(new Date());
			avatar = userInfo.getQqAvatar();
			userInfoMapper.updateByQqOpenId(updateuserInfo,qqOpenID);

		}
		//获取用户基本信息


		SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
		sessionWebUserDto.setAvatar(avatar);
		sessionWebUserDto.setUserid(userInfo.getUserId());
		sessionWebUserDto.setNickname(userInfo.getNickName());

		if (ArrayUtils.contains(appConfig.getAdminEmail().split(","),userInfo.getEmail() == null ? "" : userInfo.getEmail())){
			sessionWebUserDto.setAdmin(true);
		}else {
			sessionWebUserDto.setAdmin(false);
		}

		UserSpaceDto userSpaceDto=new UserSpaceDto();
		//TODO 获取用户已使用的空间
		Long userSpace = fileInfoMapper.selectUserSpace(userInfo.getUserId());
		userSpaceDto.setUserSpace(userSpace);
		userSpaceDto.setTotalUserSpace(userInfo.getTotalSpace());

		redisComponent.setUserSpaceuse(userInfo.getUserId(),userSpaceDto);
		return sessionWebUserDto;
	}


//通过回调code 获取accessToken
	private String getQQAccessToken(String code){

		String accessTokrn=null;
		String url=null;
		String qqUrlAccessToken = appConfig.getQqUrlAccessToken();
		String qqAppId = appConfig.getQqAppId();
		String qqAppKey = appConfig.getQqAppKey();
		String qqUrlRedirect = appConfig.getQqUrlRedirect();


		try {
			url = String.format(qqUrlAccessToken, qqAppId, qqAppKey, code, URLEncoder.encode(qqUrlRedirect, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("encode失败");
		}
		String tokenResult= OKHttpUtil.getRequest(url);

		if (tokenResult == null || tokenResult.indexOf(Constants.VIEW_OBJ_RESULT_KEY) != -1){
			logger.error("获取qqtoken失败:{}",tokenResult);
			throw new BusinessException("获取qqtoken失败");
		}
		String[] params = tokenResult.split("%");
		if (params != null && params.length > 0) {
			for (String pa : params) {
				if (pa.indexOf("access_token") != -1){
					accessTokrn = pa.split("=")[1];
					break;
				}
			}
		}


		return accessTokrn;
	}




	//获取 qq opentID
	public String getQqOpenID(String accessToken){
		String url = String.format(appConfig.getQqUrlOpenid(), accessToken);
		String openIdResult = OKHttpUtil.getRequest(url);
		String tmpJson=this.getQqResp(openIdResult);
		if (tmpJson==null) {
			logger.error("调用qq接口获取openid失败：{}",tmpJson);
			throw new BusinessException("调用qq接口获取openid失败");
		}
		Map jsonData= JsonUtil.converTJson2Obj(tmpJson,Map.class);
		if (jsonData==null || jsonData.containsKey(Constants.VIEW_OBJ_RESULT_KEY)) {
			logger.error("调用qq接口获取openid失败：{}",jsonData);
			throw new BusinessException("调用qq接口获取openid失败");
		}

		return String.valueOf(jsonData.get("openid"));
	}



	public String getQqResp(String openIdResult){

		if (StringUtils.isNotBlank(openIdResult)){
			int callback = openIdResult.indexOf("callback");
			if (callback != -1) {
				int start = openIdResult.indexOf("(");
				int end = openIdResult.lastIndexOf(")");
				String jsonStr = openIdResult.substring(start + 1, end - 1);
				return jsonStr;
			}

		}

		return null;

	}

	private QQInfoDto getQQUserInfo(String accrsssToken,String qqOpenId) throws BusinessException{
		String url = String.format(appConfig.getQqUrlUserInfo(), accrsssToken, appConfig.getQqAppId(), qqOpenId);
		String respones = OKHttpUtil.getRequest(url);
		if (StringUtils.isNotBlank(respones)) {
			QQInfoDto qqInfoDto=JsonUtil.converTJson2Obj(respones,QQInfoDto.class);
			if(qqInfoDto.getRet() !=0){
				logger.error("qqinfo:{}",respones);
				throw new BusinessException("调用qq接口获取用户信息异常");
			}
			return qqInfoDto;
		}
		throw new BusinessException("调用qq接口获取用户信息异常");
	}


























}
