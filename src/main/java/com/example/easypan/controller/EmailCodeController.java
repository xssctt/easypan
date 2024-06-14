package com.example.easypan.controller;

import java.util.Date;
import com.example.easypan.entity.po.EmailCode;
import com.example.easypan.entity.query.EmailCodeQuery;
import java.util.List;
import com.example.easypan.entity.query.SimplePage;
import com.example.easypan.entity.vo.PaginationResultVo;
import com.example.easypan.enums.PageSize;
import com.example.easypan.service.EmailCodeService;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.easypan.mappers.EmailCodeMapper;
import com.example.easypan.controller.ABaseController;
import com.example.easypan.entity.vo.ResponseVo;


	/** 
	 *
	 * @Desoription 邮箱验证码controller 控制层
	 * @Auther 摸鱼
	 * @Date 2024-06-04
	 */
@RestController("emailCodeController")
@RequestMapping("/emailCode")
public class EmailCodeController extends ABaseController{

	@Resource
	private EmailCodeService emailCodeService;

	/** 
	 *
	 *  
	 */
	@RequestMapping("/loadDateList")
	public ResponseVo loadDataList(EmailCodeQuery query){
		return getSuccessResponseVo(emailCodeService.findListByParam(query));
	}
	/** 
	 *
	 *  新增
	 */
	@RequestMapping("/add")
	public ResponseVo add(EmailCode bean){
		return getSuccessResponseVo(emailCodeService.add(bean));
	}
	/** 
	 *
	 *  批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVo addBatch(@RequestBody List<EmailCode> listBean){
		return getSuccessResponseVo(emailCodeService.addBatch(listBean));
	}
	/** 
	 *
	 *  批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVo addOrUpdateBatch(@RequestBody List<EmailCode> listBean){
		return getSuccessResponseVo(emailCodeService.addOrUpdateBatch(listBean));
	}
	/** 
	 *
	 *  根据EmailAndCode查询
	 */
	@RequestMapping("/getEmailCodeByEmailAndCode")
	public ResponseVo getEmailCodeByEmailAndCode(String email, String code){
		return getSuccessResponseVo(emailCodeService.getEmailCodeByEmailAndCode(email, code));
	}

	/** 
	 *
	 *  根据EmailAndCode更新
	 */
	@RequestMapping("/updateEmailCodeByEmailAndCode")
	public ResponseVo updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code){
		return getSuccessResponseVo(emailCodeService.updateEmailCodeByEmailAndCode(bean,email, code));
	}

	/** 
	 *
	 *  根据EmailAndCode删除
	 */
	@RequestMapping("/deleteEmailCodeByEmailAndCode")
	public ResponseVo deleteEmailCodeByEmailAndCode(String email, String code){
		return getSuccessResponseVo(emailCodeService.deleteEmailCodeByEmailAndCode(email, code));
	}

}
