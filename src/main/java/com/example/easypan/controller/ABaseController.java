package com.example.easypan.controller;



import com.example.easypan.Util.CopyTools;
import com.example.easypan.Util.StringTools;
import com.example.easypan.bean.Constants;
import com.example.easypan.entity.dto.SessionWebUserDto;
import com.example.easypan.entity.vo.PaginationResultVo;
import com.example.easypan.entity.vo.ResponseVo;

import com.example.easypan.enums.ResponseCodeEnum;

import com.example.easypan.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;


public class ABaseController {
    protected static final String STATUS_SUCCESS="success";
    protected static final String STATUS_ERROR="error";
    private static final Logger logger= LoggerFactory.getLogger(ABaseController.class);

    /**
     *  正确响应
     * @param t
     * @param <T>
     * @return
     */
    protected <T> ResponseVo getSuccessResponseVo(T t){
        ResponseVo<T> responseVo=new ResponseVo<>();
        responseVo.setStatus(STATUS_SUCCESS);
        responseVo.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVo.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVo.setData(t);

        return responseVo;
    }

    /**
     *  错误响应
     * @param e
     * @param t
     * @param <T>
     * @return
     */
    protected <T> ResponseVo getBusinessErrorResponseVo(BusinessException e, T t){
        ResponseVo<T> responseVo=new ResponseVo<>();
        responseVo.setStatus(STATUS_ERROR);


        return responseVo;
    }

    /**
     * 错误响应
     * @param t
     * @param <T>
     * @return
     */
    protected <T> ResponseVo getServerErrorResponseVo(T t){
        ResponseVo<T> responseVo=new ResponseVo<>();
        responseVo.setStatus(STATUS_ERROR);
        responseVo.setCode(ResponseCodeEnum.CODE_500.getCode());
        responseVo.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        responseVo.setData(t);

        return responseVo;
    }

    /**
     *  转换流拷贝  将PaginationResultVo<A> 转换PaginationResultVo<B>
     * @param result A
     * @param classz B.class
     * @param <S>
     * @param <T>
     * @return  PaginationResultVo<B>
     */
    protected <S,T> PaginationResultVo<T> convert2PaginationVo(PaginationResultVo<S> result,Class<T> classz){
        PaginationResultVo<T> resultVo =new PaginationResultVo<>();
        resultVo.setList(CopyTools.copyList(result.getList(),classz));
        resultVo.setPageNo(result.getPageNo());
        resultVo.setPageSize(result.getPageSize());
        resultVo.setPageTotal(result.getPageTotal());
        resultVo.setTotalCount(result.getTotalCount());
        return resultVo;
    }


    /**
     *  读文件
     * @param response
     * @param filepath
     */
    protected void readFile(HttpServletResponse response,String filepath){
        if (!StringTools.pathIsOk(filepath)){
            return;
        }
        //OutputStream OutputStreamWriter FileOutputStream BufferedWriter
        FileInputStream fileInputStream=null;
        OutputStream outputStream=null;

        try{
            File file = new File(filepath);
            if (!file.exists()){
                return;
            }
            fileInputStream=new FileInputStream(file);
            byte[] bytes = new byte[1024];
            outputStream=response.getOutputStream();

            int len=0;
            while ( (len=fileInputStream.read(bytes) )!= -1){
                outputStream.write(bytes,0,len);
            }
            outputStream.flush();
        }catch (Exception e){
            logger.error("读取文件异常",e);
        }finally {

            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("IO异常",e);
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error("IO异常",e);
                    e.printStackTrace();
                }
            }


        }

    }

    /**
     * 保存文件
     * @param avatarFile
     * @param multipartFile
     */
    protected void saveFile(String avatarFile, MultipartFile multipartFile){

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



        InputStream inputStream = null;
        FileOutputStream outputStream =null;

        try{
            inputStream = multipartFile.getInputStream();
            outputStream = new FileOutputStream(avatarFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }catch (Exception e){
            logger.error("保存文件异常",e);
        }finally {

            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("IO异常",e);
                    e.printStackTrace();
                }
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("IO异常",e);
                    e.printStackTrace();
                }
            }


        }

    }


    /**
     *   很具session获取用户信息
     * @param session
     * @return
     */
    protected SessionWebUserDto getSessionWebUserDto(HttpSession session){
        return (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
    }

    protected String getMonthByPath(String path){
        return path.substring(0,path.indexOf("/"));
    }
}




























