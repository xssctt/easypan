package com.example.easypan.service;

import java.util.Date;
import com.example.easypan.entity.po.EmailCode;
import com.example.easypan.entity.po.Maill;
import com.example.easypan.entity.query.EmailCodeQuery;
import java.util.List;
import com.example.easypan.entity.vo.PaginationResultVo;


	/** 
	 *
	 * @Desoription 邮箱验证码service 逻辑层
	 * @Auther 摸鱼
	 * @Date 2024-06-04
	 */
public interface EmailCodeService {

	/** 
	 *
	 *  根据条件查询列表
	 */
	List<EmailCode> findListByParam(EmailCodeQuery query);
	/** 
	 *
	 *  根据条件查询多少数量
	 */
	Integer findCountByParam(EmailCodeQuery query);
	/** 
	 *
	 *  分页查询
	 */
	PaginationResultVo<EmailCode> findListByPage(EmailCodeQuery query);
	/** 
	 *
	 *  新增
	 */
	Integer add(EmailCode bean);
	/** 
	 *
	 *  批量新增
	 */
	Integer addBatch(List<EmailCode> listBean);
	/** 
	 *
	 *  批量新增/修改
	 */
	Integer addOrUpdateBatch(List<EmailCode> listBean);
	/** 
	 *
	 *  根据EmailAndCode查询
	 */

	EmailCode getEmailCodeByEmailAndCode(String email, String code);

	/** 
	 *
	 *  根据EmailAndCode更新
	 */

	Integer updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code);

	/** 
	 *
	 *  根据EmailAndCode删除
	 */

	Integer deleteEmailCodeByEmailAndCode(String email, String code);



	void sendEmailCode(String email, Integer type);

	void checkCode(String email,String emailCode,Integer type);

}
