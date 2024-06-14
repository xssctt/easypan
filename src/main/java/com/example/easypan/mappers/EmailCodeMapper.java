package com.example.easypan.mappers;

import com.example.easypan.mappers.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
	/** 
	 *
	 * @Desoription 邮箱验证码mappers
	 * @Auther 摸鱼
	 * @Date 2024-06-04
	 */
public interface EmailCodeMapper<T, P> extends BaseMapper{
	/** 
	 *
	 *  根据EmailAndCode查询
	 */

	T selectByEmailAndCode(@Param("email") String email, @Param("code") String code);

	/** 
	 *
	 *  根据EmailAndCode更新
	 */

	Integer updateByEmailAndCode(@Param("bean") T t, @Param("email") String email, @Param("code") String code);

	/** 
	 *
	 *  根据EmailAndCode删除
	 */

	Integer deleteByEmailAndCode(@Param("email") String email, @Param("code") String code);


	Integer disableEmailCode(@Param("email") String email);


	@Delete("delete from email_code where where status=1 ")
	Integer deleteAllStatusIsOne();

    }