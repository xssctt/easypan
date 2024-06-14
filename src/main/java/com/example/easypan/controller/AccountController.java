package com.example.easypan.controller;

import java.io.*;
import java.net.URLEncoder;
import java.util.Date;

import cn.hutool.http.HttpStatus;
import com.example.easypan.Annotation.GlobalInterceptor;
import com.example.easypan.Annotation.VerifyParam;
import com.example.easypan.Util.CreateImageCodeUtil;
import com.example.easypan.Util.StringTools;
import com.example.easypan.bean.Constants;
import com.example.easypan.bean.SysSetting;
import com.example.easypan.config.AppConfig;
import com.example.easypan.entity.dto.SessionWebUserDto;
import com.example.easypan.entity.po.EmailCode;
import com.example.easypan.entity.po.Maill;
import com.example.easypan.entity.po.UserInfo;
import com.example.easypan.entity.query.EmailCodeQuery;
import com.example.easypan.entity.query.UserInfoQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.easypan.entity.query.SimplePage;
import com.example.easypan.entity.vo.PaginationResultVo;
import com.example.easypan.enums.PageSize;
import com.example.easypan.enums.VerifyRefexEnum;
import com.example.easypan.exception.BusinessException;
import com.example.easypan.mappers.EmailCodeMapper;
import com.example.easypan.redis.RedisComponent;
import com.example.easypan.redis.RedisUtil;
import com.example.easypan.service.EmailCodeService;
import com.example.easypan.service.MaillService;
import com.example.easypan.service.UserInfoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import com.example.easypan.mappers.UserInfoMapper;
import com.example.easypan.controller.ABaseController;
import com.example.easypan.entity.vo.ResponseVo;
import org.springframework.web.multipart.MultipartFile;


/**
	 *
	 * @Desoription 用户信息controller 控制层
	 * @Auther 摸鱼
	 * @Date 2024-06-03
	 */
@RestController("userInfoController")
@RequestMapping("/userInfo")
public class AccountController extends ABaseController{


		private static final String CONTENT_TYPE="Content-Type";
		private static final String CONTENT_TYPE_VALUE="application/json;charset=UTF-8";
		private static final Logger logger= LoggerFactory.getLogger(AccountController.class);


	@Resource
	private UserInfoService userInfoService;
	@Resource
	private MaillService maillService;
	@Resource
	private EmailCodeService emailCodeService;
	@Resource
	private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;
	@Resource
	private	RedisUtil redisUtil;
	@Resource
	private AppConfig appConfig;
	@Resource
	private RedisComponent redisComponent;

		/**
		 * 获取验证码 图
		 * @param response
		 * @param session
		 * @param type null/0  通用code  1 请求email code
		 *
		 */
	@GetMapping("/checkCode")
	public void checkCode(HttpServletResponse response, HttpSession session,Integer type) throws IOException {
		CreateImageCodeUtil codeUtil=new CreateImageCodeUtil(103,38,5,10);
		response.setHeader("Pragma","no-cache");
		response.setHeader("Cache-Control","no-cache");
		response.setDateHeader("Expires",0);
		response.setContentType("image/jpeg");
		String code=codeUtil.getCode();

		if (type == null || type == 0){
			System.out.println(code);
			session.setAttribute(Constants.CHECK_CODE_KEY,code);

		}else {
			System.out.println(code);
			session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL,code);
		}

		codeUtil.write(response.getOutputStream());

	}

		/**
		 *
		 * @param session
		 * @param email
		 * @param checkCode  验证码
		 * @param type 0注册 1找回密码
		 * @return
		 */
	@PostMapping("/sendEmailCode")
	@GlobalInterceptor(chackparams = true,chackLogin = false)
	public ResponseVo sendEmailCode(HttpSession session,
									@VerifyParam(required = true,regex = VerifyRefexEnum.EMAIL) String email,
									@VerifyParam(required = true) String checkCode,
									@VerifyParam Integer type) {


		try {
			if ( !checkCode.equalsIgnoreCase( (String) session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL)) ){
				throw new BusinessException("验证码验证错误");
			}

			emailCodeService.sendEmailCode(email,type);
			return getSuccessResponseVo(null);


		} finally {
			session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
		}


	}

		/**
		 *
		 * @param session
		 * @param email
		 * @param nickName
		 * @param password
		 * @param checkCode 图片验证码
		 * @param emailCode 邮箱验证码
		 *
		 * @return
		 */
		@GlobalInterceptor(chackparams = true,chackLogin = false)
		@PostMapping("/register")
		public ResponseVo register(HttpSession session,
								   @VerifyParam(required = true,regex = VerifyRefexEnum.EMAIL) String email,
								   @VerifyParam(required = true) String nickName,
								   @VerifyParam(required = true) String password,
								   @VerifyParam(required = true) String checkCode,
								   @VerifyParam(required = true) String emailCode) {


			try {
				//图片验证码是否正确
				if ( !checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY)) ){
					throw new BusinessException("图片验证码验证错误");
				}
				//注册
				userInfoService.register(email,nickName,password,emailCode);
				return getSuccessResponseVo(null);

			} finally {
				session.removeAttribute(Constants.CHECK_CODE_KEY);
			}

		}


		/**
		 *
		 * @param session
		 * @param email
		 * @param password
		 * @param checkCode
		 * @return
		 */

		@GlobalInterceptor(chackparams = true,chackLogin = false)
		@PostMapping("/login")
		public ResponseVo login(HttpSession session,
								   @VerifyParam(required = true) String email,
								   @VerifyParam(required = true) String password,
								   @VerifyParam(required = true) String checkCode) {


			try {
				//图片验证码是否正确
				if ( !checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY)) ){
					throw new BusinessException("图片验证码验证错误");
				}

				SessionWebUserDto sessionWebUserDto = userInfoService.login(email, password);
				session.setAttribute(Constants.SESSION_KEY,sessionWebUserDto);
				return getSuccessResponseVo(sessionWebUserDto);
			} finally {
				session.removeAttribute(Constants.CHECK_CODE_KEY);
			}

		}




		@GlobalInterceptor(chackparams = true)
		@PostMapping("/resetPwd")
		public ResponseVo resetPwd(HttpSession session,
								   @VerifyParam(required = true,regex = VerifyRefexEnum.EMAIL) String email,
								   @VerifyParam(required = true) String password,
								   @VerifyParam(required = true) String checkCode,
								   @VerifyParam(required = true) String emailCode) {
			try {
				//图片验证码是否正确
				if ( !checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY)) ){
					throw new BusinessException("图片验证码验证错误");
				}
				userInfoService.sesetPwd(email,password,emailCode);
				return getSuccessResponseVo(null);
			} finally {
				session.removeAttribute(Constants.CHECK_CODE_KEY);
			}

		}


		/**
		 *
		 * @param session
		 * @param userId
		 * @return
		 */
		@GlobalInterceptor(chackparams = true,chackLogin = false)
		@GetMapping("/getAvatar/{userId}")
		public void getAvatar(HttpServletResponse response,
									HttpSession session,
									@VerifyParam(required = true) @PathVariable("userId") String userId){


			// D;/ddd/ddd        +/file/ +avatar/
			String avatarFloderName=Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_AVATAR_NAME;
			String avatarFloderPath=appConfig.getPath()+avatarFloderName;
			//
			File filefloder=new File(avatarFloderPath);
			if (!filefloder.exists()){
				filefloder.mkdirs();
			}
			//D;/ddd/ddd        +/file/ +avatar/  + userid   .jpg
			String avatarPath=avatarFloderPath+userId+Constants.AVATAR_SUFFIX;

			File avatarFile=new File(avatarPath);
			if (!avatarFile.exists()){
				if (!new File(avatarFloderPath+Constants.AVATAR_DEFULT).exists()){
					//默认图片不存在
					printNoDefultImage(response);
				}
				avatarPath=avatarFloderPath+Constants.AVATAR_DEFULT;

			}
			response.setContentType("image/jpg");
			readFile(response,avatarPath);
		}

		/**
		 *
		 * @param response
		 * @param session
		 * @return  session存放SessionWebUserDto
		 */
		@GetMapping("/getUserInfo")
		public ResponseVo getUserInfo(HttpServletResponse response,
							  HttpSession session){

			return getSuccessResponseVo(getSessionWebUserDto(session));
		}


		/**
		 *
		 * @param response
		 * @param session
		 * @return  redisComponent存放用户空间信息
		 */

		@GetMapping("/getUserSpace")
		public ResponseVo getUserSpace(HttpServletResponse response,
									  HttpSession session){

			return getSuccessResponseVo( redisComponent.getUserSpaceuse( getSessionWebUserDto(session).getUserid() ) );
		}


		/**
		 *
		 * @param response
		 * @param session
		 * @return
		 */

		@GetMapping("/loginout")
		public ResponseVo loginout(HttpServletResponse response,
									  HttpSession session){
			//退出登录 清空sesson
			session.invalidate();
			return getSuccessResponseVo(null);
		}


		@GlobalInterceptor(chackparams = true,chackLogin = true)
		@GetMapping("/updateUserAvatar")
		public ResponseVo updateUserAvatar(HttpSession session, MultipartFile multipartFile){

			//对文件 multipartFile是否存在判断
			// 检查 multipartFile 是否存在且不为空
			if (multipartFile == null || multipartFile.isEmpty()) {
				throw new BusinessException("文件为空，请上传文件");
			}

			// 检查文件类型是否为图片
			String contentType = multipartFile.getContentType();
			if (contentType == null || !contentType.startsWith("image/")) {
				throw new BusinessException("文件类型不支持，请上传图片文件");
			}

			//获取seession 的用户信息
			SessionWebUserDto sessionWebUserDto = getSessionWebUserDto(session);

			//图片目录设置信息 在application配置
			String avatarfolder=Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_AVATAR_NAME;
			String avatarfolderPath=appConfig.getPath()+avatarfolder;

			File file=new File(avatarfolder);
			if (!file.exists()){
				file.mkdirs();
			}

			//
			String avatarName=sessionWebUserDto.getUserid()+Constants.AVATAR_SUFFIX;
			//文件路径
			String avatarPath=avatarfolderPath+avatarName;

			// appConfig.getPath() + file.getPath() + avatarName
			File avatarFile=new File(avatarPath);


			//存储图片
			//saveFile(avatarPath,multipartFile);
			try {
				//存储图片
				multipartFile.transferTo(avatarFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 因为上传头像  数据库的默认qq头像无论有没有 不用

			UserInfo userInfo=new UserInfo();
			userInfo.setQqAvatar("");
			userInfoService.updateUserInfoByUserId(userInfo,sessionWebUserDto.getUserid());

			//
			sessionWebUserDto.setAvatar(null);
			session.setAttribute(Constants.SESSION_KEY,sessionWebUserDto);
			return getSuccessResponseVo(null);
		}


	/**
	 *
	 * @param session
	 * @param password
	 * @return
	 */
	@GlobalInterceptor(chackparams = true)
	@PostMapping("/updatePassword")
	public ResponseVo updatePassword(HttpSession session,
							   @VerifyParam(required = true) String password) {
		SessionWebUserDto sessionWebUserDto = getSessionWebUserDto(session);
		UserInfo userInfo=new UserInfo();
		userInfo.setPassword(password);
		userInfoService.updateUserInfoByUserId(userInfo,sessionWebUserDto.getUserid());
		return getSuccessResponseVo(null);
	}



	@GetMapping("/qqlogin")
	@GlobalInterceptor(chackparams = true,chackLogin = false)
	public ResponseVo qqlogin(HttpSession session,String callBackUrl){

		String state = StringTools.getRandomNumber(Constants.LENGTH_30);

		if (!StringTools.isEmpty(callBackUrl)) {
			session.setAttribute(state,callBackUrl);
		}

		String qqUrlAuthorization = appConfig.getQqUrlAuthorization();
		String qqAppId = appConfig.getQqAppId();
		String qqUrlRedirect = appConfig.getQqUrlRedirect();

		String url="";
		try {
			url=String.format(qqUrlAuthorization,qqAppId, URLEncoder.encode(qqUrlRedirect,"utf-8"),state);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return getSuccessResponseVo(url);
	}



	@GetMapping("/qqlogin/callback")
	@GlobalInterceptor(chackparams = true,chackLogin = false)
	public ResponseVo qqloginCallback(HttpSession session,
									  @VerifyParam(required = true) String code,
									  @VerifyParam(required = true) String state){


		SessionWebUserDto sessionWebUserDto=userInfoService.qqlogin(code);
		session.setAttribute(Constants.SESSION_KEY,sessionWebUserDto);

		Map<String,Object> result=new HashMap<>();
		result.put("callbackUrl",session.getAttribute(state));
		result.put("userInfo",sessionWebUserDto);
		return getSuccessResponseVo(result);
	}


	/**
	 *
	 * 判断是否存在默认图片
	 *
	 * @param response
	 */
	private void printNoDefultImage(HttpServletResponse response){
			response.setHeader(CONTENT_TYPE,CONTENT_TYPE_VALUE);
			response.setStatus(HttpStatus.HTTP_OK);
			PrintWriter writer=null;
			try {
				writer=response.getWriter();
				writer.print("需要放置存储默认图");
				writer.close();
			}catch (Exception e){
				logger.error("输出默认图失败",e);
			}finally {
				writer.close();
			}

		}











	/** 
	 *
	 *  
	 */
	@RequestMapping("/loadDateList")
	public ResponseVo loadDataList(UserInfoQuery query){
		return getSuccessResponseVo(userInfoService.findListByParam(query));
	}
	/** 
	 *
	 *  新增
	 */
	@RequestMapping("/add")
	public ResponseVo add(UserInfo bean){
		return getSuccessResponseVo(userInfoService.add(bean));
	}
	/** 
	 *
	 *  批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVo addBatch(@RequestBody List<UserInfo> listBean){
		return getSuccessResponseVo(userInfoService.addBatch(listBean));
	}
	/** 
	 *
	 *  批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVo addOrUpdateBatch(@RequestBody List<UserInfo> listBean){
		return getSuccessResponseVo(userInfoService.addOrUpdateBatch(listBean));
	}
	/** 
	 *
	 *  根据UserId查询
	 */
	@RequestMapping("/getUserInfoByUserId")
	public ResponseVo getUserInfoByUserId(String userId){
		return getSuccessResponseVo(userInfoService.getUserInfoByUserId(userId));
	}

	/** 
	 *
	 *  根据UserId更新
	 */
	@RequestMapping("/updateUserInfoByUserId")
	public ResponseVo updateUserInfoByUserId(UserInfo bean, String userId){
		return getSuccessResponseVo(userInfoService.updateUserInfoByUserId(bean,userId));
	}

	/** 
	 *
	 *  根据UserId删除
	 */
	@RequestMapping("/deleteUserInfoByUserId")
	public ResponseVo deleteUserInfoByUserId(String userId){
		return getSuccessResponseVo(userInfoService.deleteUserInfoByUserId(userId));
	}

	/** 
	 *
	 *  根据Email查询
	 */
	@RequestMapping("/getUserInfoByEmail")
	public ResponseVo getUserInfoByEmail(String email){
		return getSuccessResponseVo(userInfoService.getUserInfoByEmail(email));
	}

	/** 
	 *
	 *  根据Email更新
	 */
	@RequestMapping("/updateUserInfoByEmail")
	public ResponseVo updateUserInfoByEmail(UserInfo bean, String email){
		return getSuccessResponseVo(userInfoService.updateUserInfoByEmail(bean,email));
	}

	/** 
	 *
	 *  根据Email删除
	 */
	@RequestMapping("/deleteUserInfoByEmail")
	public ResponseVo deleteUserInfoByEmail(String email){
		return getSuccessResponseVo(userInfoService.deleteUserInfoByEmail(email));
	}

	/** 
	 *
	 *  根据QqOpenId查询
	 */
	@RequestMapping("/getUserInfoByQqOpenId")
	public ResponseVo getUserInfoByQqOpenId(String qqOpenId){
		return getSuccessResponseVo(userInfoService.getUserInfoByQqOpenId(qqOpenId));
	}

	/** 
	 *
	 *  根据QqOpenId更新
	 */
	@RequestMapping("/updateUserInfoByQqOpenId")
	public ResponseVo updateUserInfoByQqOpenId(UserInfo bean, String qqOpenId){
		return getSuccessResponseVo(userInfoService.updateUserInfoByQqOpenId(bean,qqOpenId));
	}

	/** 
	 *
	 *  根据QqOpenId删除
	 */
	@RequestMapping("/deleteUserInfoByQqOpenId")
	public ResponseVo deleteUserInfoByQqOpenId(String qqOpenId){
		return getSuccessResponseVo(userInfoService.deleteUserInfoByQqOpenId(qqOpenId));
	}

	/** 
	 *
	 *  根据NickName查询
	 */
	@RequestMapping("/getUserInfoByNickName")
	public ResponseVo getUserInfoByNickName(String nickName){
		return getSuccessResponseVo(userInfoService.getUserInfoByNickName(nickName));
	}

	/** 
	 *
	 *  根据NickName更新
	 */
	@RequestMapping("/updateUserInfoByNickName")
	public ResponseVo updateUserInfoByNickName(UserInfo bean, String nickName){
		return getSuccessResponseVo(userInfoService.updateUserInfoByNickName(bean,nickName));
	}

	/** 
	 *
	 *  根据NickName删除
	 */
	@RequestMapping("/deleteUserInfoByNickName")
	public ResponseVo deleteUserInfoByNickName(String nickName){
		return getSuccessResponseVo(userInfoService.deleteUserInfoByNickName(nickName));
	}

}
