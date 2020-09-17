package com.ffmpeg.common.common;


import com.ffmpeg.common.FFMpegExceptionn;
import com.ffmpeg.common.response.Result;

import java.io.IOException;
import java.util.List;

/**
 * @author alan.chen
 * @date 2020/6/7 9:07 PM
 */
public class ProcessCommand {

    /**
     * ִ������
     *
     * @param command �����������
     * @return ִ�н����Ϣ����
     */
    public static Result start(List<String> command) {
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = null;
        try {
            process = builder.start();
            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e);
        }
    }

}
