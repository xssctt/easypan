package com.example.easypan.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandExecutor {

    private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    public static String executeCommand(String cmd, Boolean outprintLog) throws BusinessException {
        if (cmd == null || cmd.trim().isEmpty()) {
            logger.error("--- 指令执行失败  ffpemg命令为空");
            throw new BusinessException("FFmpeg command is empty.");
        }

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        StringBuilder output = new StringBuilder();

        try {
            process = runtime.exec(cmd);

            // 启动新的线程来处理错误流和输出流
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR", outprintLog);
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT", outprintLog);

            errorGobbler.start();
            outputGobbler.start();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                logger.error("FFmpeg command execution failed with exit code: " + exitCode);
                throw new BusinessException("FFmpeg command execution failed with exit code: " + exitCode);
            }

            output.append(outputGobbler.getOutput());

        } catch (IOException | InterruptedException e) {
            logger.error("Exception during command execution: ", e);
            throw new BusinessException("Exception during command execution: " + e.getMessage(), e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }

        return output.toString();
    }

    private static class StreamGobbler extends Thread {
        private final BufferedReader reader;
        private final String streamType;
        private final Boolean outprintLog;
        private final StringBuilder output = new StringBuilder();

        public StreamGobbler(InputStream inputStream, String streamType, Boolean outprintLog) {
            this.reader = new BufferedReader(new InputStreamReader(inputStream));
            this.streamType = streamType;
            this.outprintLog = outprintLog;
        }

        @Override
        public void run() {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    if (outprintLog) {
                        if ("ERROR".equals(streamType)) {
                            logger.error(line);
                        } else {
                            logger.info(line);
                        }
                    }
                    output.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                logger.error("Error reading stream: ", e);
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("Error closing stream: ", e);
                }
            }
        }

        public String getOutput() {
            return output.toString();
        }
    }

    public static void main(String[] args) {
        try {
            String command = "ffmpeg -y -i input.mp4 -vcodec copy -vbsf h264_mp4toannexb output.ts";
            String result = executeCommand(command, true);
            System.out.println(result);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }
}

class BusinessException extends Exception {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
