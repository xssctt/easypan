package com.example.easypan.Util;

import com.example.easypan.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;


/**
 * 执行cmd命令工具
 */
public class ProcessUtil {
    final static Logger logger= LoggerFactory.getLogger(ProcessUtil.class);

    public static String executeCommand(String cmd,Boolean outprintLog) throws BusinessException{
        if (StringTools.isEmpty(cmd)) {
            logger.error("--- 指令执行失败  ffpemg命令为空");
            return null;
        }

        Runtime runtime = Runtime.getRuntime();
        Process process=null;

        try {

            process=Runtime.getRuntime().exec(cmd);
            //执行ffmpeg命令
            //取出输出流和错误流
            //必须取所有产生输出信息
            PrintStream errorStream = new PrintStream(process.getErrorStream());
            PrintStream inputStream = new PrintStream(process.getInputStream());
            errorStream.start();
            inputStream.start();
            //
            process.waitFor();
            //
            String result=errorStream.stringBuffer.append(inputStream.stringBuffer+"\n").toString();

            //
            if (outprintLog){
                logger.info("执行命令:{}，已执行完毕执行结果:{}",cmd,result);
            }else {
                logger.info("执行命令，已执行完毕执行结果",cmd);
            }

            return result;
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException("视频转化失败");
        }finally {
            if (null != process){
                ProcessKiller ffmpefKiller = new ProcessKiller(process);
                runtime.addShutdownHook(ffmpefKiller);
            }
        }


    }

    private static class ProcessKiller extends Thread{
        private Process process;
        public ProcessKiller(Process process){this.process=process;}

        @Override
        public void run() {
            this.process.destroy();
        }
    }


    static class PrintStream extends Thread{
        InputStream inputStream=null;
        BufferedReader bufferedReader=null;
        StringBuffer stringBuffer=new StringBuffer();
        public PrintStream(InputStream inputStream){
            this.inputStream=inputStream;
        }

        @Override
        public void run() {

            try {
                if (null == inputStream){
                    return;
                }
                bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line=null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
            }catch (Exception e){
                logger.error("读取输入流出错了",e.getMessage());
            }finally {

                try {
                    if (null != bufferedReader ){
                        bufferedReader.close();
                    }
                    if (inputStream !=null) {
                        inputStream.close();
                    }
                }catch (Exception e){
                    logger.error("使用PrintStream，关闭流出错");
                }

            }

        }



    }


}
