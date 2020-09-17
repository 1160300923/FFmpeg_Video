package com.ffmpeg.common.audio;

import com.ffmpeg.common.FFMpegExceptionn;
import com.ffmpeg.common.common.StreamHanlerCommon;
import com.ffmpeg.common.response.Result;
import com.ffmpeg.common.utils.BaseFileUtil;
import com.ffmpeg.common.utils.StrUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @auther alan.chen
 * @time 2019/9/17 3:51 PM
 */
public class AudioOperation {


    /**
     *  ffmpeg�ļ�·��
     */
    private String ffmpegEXE;

    public AudioOperation(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public static AudioOperation builder(String ffmpegEXE) {
        return new AudioOperation(ffmpegEXE);
    }



    /**
     * �������Ƶ�ļ�ƴ��Ϊһ����Ƶ�ļ������
     *
     * @param bgmOutPath �����Ƶ�ļ�
     * @param bgmInputPath �������Ƶ�ļ�, ��ֵ����
     * @return
     */
    public Result audioConcat(String bgmOutPath, String... bgmInputPath) {
        if(StrUtils.checkBlank(bgmOutPath) || bgmInputPath.length <= 0) {
            throw new FFMpegExceptionn("��������ȷ����Ƶ��������·��");
        }
        BaseFileUtil.checkAndMkdir(bgmOutPath);
        try {
            List<String> bgmList = Arrays.asList(bgmInputPath);

            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-f");
            commands.add("-concat");

            bgmList.forEach(item -> {
                commands.add("-i");
                commands.add(item);
            });

            commands.add("-c");
            commands.add("copy");
            commands.add(bgmOutPath);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ͨ��ָ����ʼʱ��ͽ���ʱ�� �ü���Ƶ
     *
     * @param bgmInputPath ��Ƶ�������·��
     * @param bgmOutPath ��Ƶ�������·��
     * @param startTime ��ȡ�Ŀ�ʼʱ��
     * @param endTime ��ȡ�Ľ���ʱ��
     * @return
     */
    public Result audioCut(String bgmInputPath, String bgmOutPath, String startTime, String endTime) {
//         ffmpeg -i out.mp3 -ss 00:00:00 -t 00:06:38 -acodec copy love3.mp3
        String str = "^([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(startTime);
        Matcher matcher1 = pattern.matcher(endTime);
        if(!matcher.matches() || !matcher1.matches()) {
            throw new FFMpegExceptionn("�����ʱ���ʽ����");
        }
        try {
            BaseFileUtil.checkAndMkdir(bgmOutPath);

            Stream<String> stream = Stream.of(ffmpegEXE, "-i", bgmInputPath, "-ss", startTime, "-t",
                    endTime, "-acodec", "copy", bgmOutPath);

            List<String> commands = stream.collect(Collectors.toList());

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }


    /**
     * ����Ƶ����ȡ��Ƶ
     *
     * @param inputVideo ��Ƶ����·��
     * @param outAudio �����Ƶ����·��
     * @return
     */
    public Result getBgmFromVideo(String inputVideo, String outAudio) {
        //ffmpeg -y -i source.mp4 -vn output.wav
        if(StrUtils.checkBlank(inputVideo) || StrUtils.checkBlank(outAudio)) {
            throw new FFMpegExceptionn("��������ȷ��·��");
        }
        BaseFileUtil.checkAndMkdir(outAudio);
        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");
            commands.add("-i");
            commands.add(inputVideo);

            commands.add("-vn");
            commands.add(outAudio);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ת����Ƶ��ʽ
     *
     * @param inputAudio ������Ƶ����·��
     * @param outAudio �����Ƶ����·��
     * @return
     */
    public Result transFormatAudio(String inputAudio, String outAudio) {
        //ffmpeg -y -i source.amr  output.mp3
        if(StrUtils.checkBlank(inputAudio) || StrUtils.checkBlank(outAudio)) {
            throw new FFMpegExceptionn("��������ȷ��·��");
        }
        BaseFileUtil.checkAndMkdir(outAudio);
        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");
            commands.add("-i");
            commands.add(inputAudio);

            commands.add(outAudio);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ��������ʽ����Ƶ����Ƶת��AMR
     *
     * @param inputAudio ������Ƶ/��Ƶ����·��
     * @param outAudio �����Ƶ����·��
     * @return
     */
    public Result transFormatAmrAudio(String inputAudio, String outAudio) {
        // ffmpeg -i test.mp3 -c:a libopencore_amrnb -ac 1 -ar 8000 -b:a 12.20k -y test.amr
        if(StrUtils.checkBlank(inputAudio) || StrUtils.checkBlank(outAudio)) {
            throw new FFMpegExceptionn("��������ȷ��·��");
        }
        BaseFileUtil.checkAndMkdir(outAudio);
        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");
            commands.add("-i");
            commands.add(inputAudio);

            commands.add("-c:a");
            commands.add("libopencore_amrnb");
            commands.add("-ac");
            commands.add("1");
            commands.add("-ar");
            commands.add("8000");
            commands.add("-b:a");
            commands.add("12.20k");

            commands.add(outAudio);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }


    /**
     * ת����Ƶ��ʽ mp3���뷽ʽ���õ���libmp3lame
     *
     * @param inputAudio ������Ƶ����·��
     * @param outAudio �����Ƶ����·��
     * @return
     */
    public Result transFormatToMp3Audio(String inputAudio, String outAudio) {
        // ffmpeg -y -i amr.amr -acodec libmp3lame mp33.mp3
        if(StrUtils.checkBlank(inputAudio) || StrUtils.checkBlank(outAudio)) {
            throw new FFMpegExceptionn("��������ȷ��·��");
        }
        BaseFileUtil.checkAndMkdir(outAudio);
        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");
            commands.add("-i");
            commands.add(inputAudio);

            commands.add("-acodec");
            commands.add("libmp3lame");

            commands.add(outAudio);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }


}
