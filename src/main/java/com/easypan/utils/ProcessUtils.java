package com.easypan.utils;

import com.easypan.exception.BusinessException;
import com.fasterxml.jackson.core.util.BufferRecycler;
import javafx.util.Builder;
import org.apache.tomcat.jni.Proc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ProcessUtils {
    private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);
    public static String executeCommand(String cmd,Boolean outprintLog) throws BusinessException{
        if(StringTools.isEmpty(cmd)){
            logger.error("---指令执行失败，要执行的ffmpeg指令为空---");
            return null;
        }
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            //执行ffmpeg指令
            //取出输出流和错误流的信息
            //必须要去除ffmpeg在执行命令中产生的输出信息，如果不取的话当输出流信息填满jvm存储输出流信息的缓存区时，线程会被堵塞
            PrintStream errorStream = new PrintStream(process.getErrorStream());
            PrintStream inputStream = new PrintStream(process.getInputStream());
            errorStream.start();
            inputStream.start();
            //等待ffmpeg执行完
            process.waitFor();
            //获取执行结果字符串
            String result = errorStream.stringBuffer.append(inputStream.stringBuffer+"\n").toString();
            //输出执行的命令信息
            if(outprintLog){
                logger.info("执行命令：{}，已执行完毕，执行结果{}",cmd,result);
            }else {
                logger.info("执行命令：{}，已执行完毕");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("视频转换失败");
        }finally {
            if(null != process){
                ProcessKiller ffmpegKiller = new ProcessKiller(process);
                runtime.addShutdownHook(ffmpegKiller);
            }
        }
    }


    private static class ProcessKiller extends Thread{
        private Process process;
        public ProcessKiller(Process process) {
            this.process = process;
        }
        @Override
        public void run(){
            this.process.destroy();
        }
    }
    static class PrintStream extends  Thread{
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();
        public PrintStream(InputStream inputStream){
            this.inputStream = inputStream;
        }

        @Override
        public void run(){
            try {
                if(null ==inputStream){
                    return;
                }
                bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                String line= null;
                while((line=bufferedReader.readLine())!=null){
                    stringBuffer.append(line);
                }
            }catch (Exception e){
                logger.error("读取输入流出错了，错误信息："+e.getMessage());
            }finally {
                try {
                    if(null!=bufferedReader){
                        bufferedReader.close();
                    }
                    if(null!=inputStream){
                        inputStream.close();
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    logger.error("调用PrintStream读取输出流后，关闭流时出错！");
                }
            }
        }

    }
}
