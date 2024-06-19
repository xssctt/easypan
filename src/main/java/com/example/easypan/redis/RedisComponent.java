package com.example.easypan.redis;


import com.example.easypan.bean.Constants;
import com.example.easypan.bean.SysSetting;
import com.example.easypan.entity.dto.UserSpaceDto;
import com.example.easypan.entity.po.FileInfo;
import com.example.easypan.entity.po.UserInfo;
import com.example.easypan.entity.query.FileInfoQuery;
import com.example.easypan.mappers.FileInfoMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisComponent {

    @Resource
    RedisUtil redisUtil;
    @Resource
    private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;

    //系统设置 ——》 用户空间大小
    public SysSetting getSysSetting(){
        //在缓存
        SysSetting sysSetting =(SysSetting) redisUtil.getCacheObject(Constants.REDIS_KEYS_SYS_SETTING);

        //没有重新创建
        if (null == sysSetting){
            sysSetting=new SysSetting();
            redisUtil.setCacheObject(Constants.REDIS_KEYS_SYS_SETTING,sysSetting);
        }

        return sysSetting;
    }

    //设置用户空间设置
    public void setUserSpaceuse(String userinfoId, UserSpaceDto userSpaceDto){
       redisUtil.setCacheObject(Constants.REDIS_KEYS_USER_SPACE_USER+userinfoId,
               userSpaceDto,
               Constants.REDIS_KEY_EXPIRE_DAY,
               TimeUnit.SECONDS);
    }

    //获取用户的空间信息
    public UserSpaceDto getUserSpaceuse(String userinfoId){
        UserSpaceDto userSpaceDto =redisUtil.getCacheObject(Constants.REDIS_KEYS_USER_SPACE_USER + userinfoId);

        if (userSpaceDto == null){
            userSpaceDto=new UserSpaceDto();
            userSpaceDto.setTotalUserSpace(getSysSetting().getUserInitUserSpace()*Constants.MB);
            // TODO 需要查询用户使用的
            Long useSpace = fileInfoMapper.selectUserSpace(userinfoId);
            userSpaceDto.setUserSpace(useSpace);
            setUserSpaceuse(userinfoId,userSpaceDto);
        }
        return userSpaceDto;
    }





    //临时文件大小放缓存

    /**
     *
     * @param userId
     * @param fileId
     * @param fileSize  每一片大小
     */
    public void saveFileTempSize(String userId,String fileId,Long fileSize){
        Long currentSize = getFileTempSize(userId, fileId);

        //currentSize+fileSize  累计 用户总共用多少
        redisUtil.setCacheObject(Constants.REDIS_KEYS_USER_TEMP_SIZE+userId+fileId,
                currentSize+fileSize,
                Constants.REDIS_KEY_EXPIRE_ONE_HOUR,
                TimeUnit.SECONDS);
    }



    //获取 临时文件大小
    public Long getFileTempSize(String userId,String fileId){
        Long currentSize=getFileSizeFromRedis(Constants.REDIS_KEYS_USER_TEMP_SIZE+userId+fileId);
        return currentSize;
    }
    //从缓存 找到数据大小
    private Long getFileSizeFromRedis(String key){
        Object sizeObj = redisUtil.getCacheObject(key);
        if (sizeObj==null) {
            return 0L;
        }
        if (sizeObj instanceof  Integer) {
            return ((Integer) sizeObj).longValue();
        }else if (sizeObj instanceof Long){
            return (Long)sizeObj;
        }

        return 0L;
    }


    /**
     *
     * @param userId
     * @param fileId
     */
    public void deleteTempFile(String userId,String fileId){
        String key=Constants.REDIS_KEYS_USER_TEMP_SIZE+userId+fileId;
        redisUtil.expire(key,0);
        //redisUtil.deleteObject(key);
    }
}
