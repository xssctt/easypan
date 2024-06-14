package com.example.easypan.service.impl;

import java.util.Date;

import com.example.easypan.config.EmailConfig;
import com.example.easypan.Util.StringTools;
import com.example.easypan.bean.Constants;
import com.example.easypan.bean.SysSetting;
import com.example.easypan.entity.po.EmailCode;
import com.example.easypan.entity.po.Maill;
import com.example.easypan.entity.po.UserInfo;
import com.example.easypan.entity.query.EmailCodeQuery;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.easypan.entity.query.SimplePage;
import com.example.easypan.entity.query.UserInfoQuery;
import com.example.easypan.entity.vo.PaginationResultVo;
import com.example.easypan.enums.PageSize;
import com.example.easypan.exception.BusinessException;
import com.example.easypan.mappers.UserInfoMapper;
import com.example.easypan.redis.RedisComponent;
import com.example.easypan.redis.RedisUtil;
import com.example.easypan.service.EmailCodeService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.example.easypan.mappers.EmailCodeMapper;
import org.springframework.transaction.annotation.Transactional;


/**
	 *
	 * @Desoription 邮箱验证码service 逻辑层
	 * @Auther 摸鱼
	 * @Date 2024-06-04
	 */
@Service("emailCodeService")
public class EmailCodeServiceImpl implements EmailCodeService{

	@Resource
	private EmailCodeMapper<EmailCode,EmailCodeQuery> emailCodeMapper;
	@Resource
	private JavaMailSender sender;
	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;
	@Resource
	private EmailConfig emailConfig;
	@Resource
	private RedisComponent redisComponent;
	@Resource
	private RedisUtil redisUtil;

	/** 
	 *
	 *  根据条件查询列表
	 */
	public List<EmailCode> findListByParam(EmailCodeQuery query){
	return this.emailCodeMapper.selectList(query);
	}
	/** 
	 *
	 *  根据条件查询多少数量
	 */
	public Integer findCountByParam(EmailCodeQuery query){
		return this.emailCodeMapper.selectCount(query);
	}
	/** 
	 *
	 *  分页查询
	 */
	public PaginationResultVo<EmailCode> findListByPage(EmailCodeQuery query){
		Integer count=this.findCountByParam(query);
		Integer pageSze= query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSze);
		query.setSimplePage(page);
		List<EmailCode> list=this.findListByParam(query);
		PaginationResultVo<EmailCode> result=new PaginationResultVo<EmailCode>(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);
		return result;
	}
	/** 
	 *
	 *  新增
	 */
	public Integer add(EmailCode bean){
		return this.emailCodeMapper.insert(bean);
	}
	/** 
	 *
	 *  批量新增
	 */
	public Integer addBatch(List<EmailCode> listBean){
		if (listBean == null || listBean.isEmpty()){
			return 0;
		}
		return this.emailCodeMapper.insertBatch(listBean);
	}
	/** 
	 *
	 *  批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<EmailCode> listBean){
		if (listBean == null || listBean.isEmpty()){
			return 0;
		}
		return this.emailCodeMapper.insertOrUpdateBatch(listBean);
	}
	/** 
	 *
	 *  根据EmailAndCode查询
	 */

	public EmailCode getEmailCodeByEmailAndCode(String email, String code){
		return this.emailCodeMapper.selectByEmailAndCode(email, code);
	}

	/** 
	 *
	 *  根据EmailAndCode更新
	 */

	public Integer updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code){
		return this.emailCodeMapper.updateByEmailAndCode(bean,email, code);
	}

	/** 
	 *
	 *  根据EmailAndCode删除
	 */

	public Integer deleteEmailCodeByEmailAndCode(String email, String code){
		return this.emailCodeMapper.deleteByEmailAndCode(email, code);
	}


		/**
		 *
		 * @param email
		 * @param type 0注册 1找回密码
		 */

	@Transactional(rollbackFor = Exception.class)
	public void sendEmailCode(String email, Integer type){
		//email
		if(StringTools.isEmpty(email)){
			throw new BusinessException("邮箱不能为空");
		}


		if (type == Constants.ZERO){
			UserInfo userInfo=userInfoMapper.selectByEmail(email);
			if (userInfo != null){
				throw new BusinessException("邮箱已存在");
			}
		}


		//看看缓存有没有
		String codeRedis =null;
		String code = null;
		if (type == Constants.ZERO){
			codeRedis =(String) redisUtil.getCacheObject(Constants.REDIS_KEYS_EMAIL_CODE_REGISTER);
		}else if (type == Constants.ONE){
			codeRedis =(String) redisUtil.getCacheObject(Constants.REDIS_KEYS_EMAIL_CODE_BACK);
		}

		if(codeRedis == null || "".equals(codeRedis)){
			//没有在生成code
			codeRedis= StringTools.getRandomNumber(Constants.LENGTH_5);

			//缓存没有 数据库有过期或者没有 需要重置
			emailCodeMapper.disableEmailCode(email);

			EmailCode emailCode=new EmailCode();
			emailCode.setCode(codeRedis);
			emailCode.setEmail(email);
			emailCode.setStatus(Constants.ZERO);
			emailCode.setCreateTime(new Date());

			emailCodeMapper.insert(emailCode);

		}
		//缓存有数据库就有  不需要新增 直接用
		code=codeRedis;


		//发送code  这里需要异步操作  或者MQ消息通知
		sendMailCode(email,code);
		//sendMailop(Maill.builder().maill(email).content(code).header("head").build());

		//redisUtil.setCacheObject(Constants.REDIS_KEYS_EMAIL_CODE,code);
		//放缓存
		if(type == Constants.ONE){
			redisUtil.setCacheObject(Constants.REDIS_KEYS_EMAIL_CODE_BACK,code,15, TimeUnit.MINUTES);
		}else if (type == Constants.ZERO){
			redisUtil.setCacheObject(Constants.REDIS_KEYS_EMAIL_CODE_REGISTER,code,15, TimeUnit.MINUTES);
		}


	}


	/**
	 *
	 * @param email
	 * @param emailCode
	 * @param type  0 register 1 resetPwd
	 */
	public void checkCode(String email,String emailCode,Integer type){
		String codeRedis =null;
		//验证邮箱验证码是否正确
		if (type == Constants.ZERO){
			 codeRedis =(String) redisUtil.getCacheObject(Constants.REDIS_KEYS_EMAIL_CODE_REGISTER);
		}else if (type == Constants.ONE){
			 codeRedis =(String) redisUtil.getCacheObject(Constants.REDIS_KEYS_EMAIL_CODE_BACK);
		}

		EmailCode codeJdbc = emailCodeMapper.selectByEmailAndCode(email, emailCode);

		if(codeRedis == null || System.currentTimeMillis()-codeJdbc.getCreateTime().getTime()>Constants.LENGTH_15 * 1000 * 60 || codeJdbc.getStatus() == 1 ){
			emailCodeMapper.disableEmailCode(email);
			throw new BusinessException("邮箱验证码不存在或者过期");
		}
		if (codeJdbc == null || !codeRedis.equals(emailCode)){
			throw new BusinessException("邮箱验证码错误");
		}

	}



	public void sendMailCode(String email, String code) {

		SysSetting sysSetting = redisComponent.getSysSetting();

		//SimpleMailMessage是一个比较简易的邮件封装，支持设置一些比较简单内容
		SimpleMailMessage message = new SimpleMailMessage();
		//设置邮件标题
		message.setSubject(sysSetting.getRedisterMailTitle());

		//设置邮件内容
		message.setText(String.format(sysSetting.getRedisterMailContent(),code));

		//设置邮件发送给谁，可以多个，这里就发给你的QQ邮箱
		message.setTo(email);

		//邮件发送者，这里要与配置文件中的保持一致
		message.setFrom(emailConfig.getSendUserName());

		message.setSentDate(new Date());
		//OK，万事俱备只欠发送
		sender.send(message);
	}

	public void sendMailop(Maill maill){
		MimeMessage message = sender.createMimeMessage();
		//使用MimeMessageHelper来帮我们修改MimeMessage中的信息
		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setSubject(maill.getHeader());
			helper.setText(maill.getContent());
			helper.setTo(maill.getMaill());
			helper.setFrom(emailConfig.getSendUserName());
			helper.setSentDate(new Date());
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		//发送修改好的MimeMessage
		sender.send(message);
	}


}
