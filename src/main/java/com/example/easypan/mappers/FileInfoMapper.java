package com.example.easypan.mappers;

import com.example.easypan.mappers.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
	/** 
	 *
	 * @Desoription 文件信息表mappers
	 * @Auther 摸鱼
	 * @Date 2024-06-13
	 */
public interface FileInfoMapper<T, P> extends BaseMapper{
	/** 
	 *
	 *  根据FileIdAndUserId查询
	 */

	T selectByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") String userId);

	/** 
	 *
	 *  根据FileIdAndUserId更新
	 */

	Integer updateByFileIdAndUserId(@Param("bean") T t, @Param("fileId") String fileId, @Param("userId") String userId);

	/** 
	 *
	 *  根据FileIdAndUserId删除
	 */

	Integer deleteByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") String userId);


	Long selectUserSpace(@Param("userId") String userId);

	void updateFileStatusWithOldStatus(@Param("fileId") String fileId,
									   @Param("userId") String userId,
									   @Param("bean") T t,
									   @Param("oldStatus") Integer oldStatus);

	}