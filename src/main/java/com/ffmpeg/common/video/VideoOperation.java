package com.ffmpeg.common.video;

import com.ffmpeg.common.FFMpegExceptionn;
import com.ffmpeg.common.common.ProcessCommand;
import com.ffmpeg.common.common.StreamHanlerCommon;
import com.ffmpeg.common.response.Result;
import com.ffmpeg.common.utils.BaseFileUtil;
import com.ffmpeg.common.utils.StrUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @auther alan.chen
 * @time 2019/9/11 11:20 AM
 */
public class VideoOperation {

    /**
     *  ffmpeg�ļ�·��
     */
    private String ffmpegEXE;

    public VideoOperation(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public static VideoOperation builder(String ffmpegEXE) {
        return new VideoOperation(ffmpegEXE);
    }


    /**
     *  ��Ƶת����ʽ
     *
     * @param inputVideo ԭʼ��Ƶ����·��������Ƶ���ƣ�
     * @param outputVideo  �����Ƶ����·��������Ƶ���ƣ�
     * @return result ����ִ��code��message
     * @throws IOException
     */
    public Result videoConvert(String inputVideo, String outputVideo)  {
        //  ffmpeg -i input.mp4 -y out.mp4
        // ffmpeg -i in.mov -vcodec copy -acodec copy out.mp4  // mov --> mp4
        // ffmpeg -i in.flv -vcodec copy -acodec copy out.mp4
        try {
            if(StrUtils.checkBlank(inputVideo) || StrUtils.checkBlank(outputVideo)) {
                throw new FFMpegExceptionn("videoInputFullPath or videoOutFullPath must not be null");
            }
            BaseFileUtil.checkAndMkdir(inputVideo);

            List<String> commands = new ArrayList<String>();
            commands.add(ffmpegEXE);

            //commands.add("-y");
            commands.add("-i");
            commands.add(inputVideo);

            //commands.add("-vcodec");
            //commands.add("copy");
            //commands.add("-acodec");

            commands.add("-y");
            //commands.add("copy");
            commands.add(outputVideo);

            ProcessBuilder builder = new ProcessBuilder(commands).redirectErrorStream(true);
            Process proc = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(proc);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ������Ƶԭ���ϳ���Ƶ
     *
     * @param bgm ��������·��
     * @param inputVideo ������Ƶ·��
     * @param outputVideo �����Ƶ·��
     * @param seconds �����Ƶ����
     * @return
     */
    public Result mergeVideoAndBgmWithOrigin(String bgm, String inputVideo, String outputVideo, double seconds) {
//     ����ԭ���ϲ�����Ƶ ffmpeg -i bgm.mp3 -i input.mp4 -t 6 -filter_complex amix=inputs=2 output.mp4
        try {
            if(StrUtils.checkBlank(bgm) || StrUtils.checkBlank(inputVideo) || StrUtils.checkBlank(outputVideo) || seconds <= 0) {
                throw new FFMpegExceptionn("��������ȷ��������������Ϊ��");
            }
            BaseFileUtil.checkAndMkdir(outputVideo);

            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-i");
            commands.add(bgm);

            commands.add("-i");
            commands.add(inputVideo);

            commands.add("-t");
            commands.add(String.valueOf(seconds));

            commands.add("-filter_complex");
            commands.add("amix=inputs=2");

            commands.add("-y");
            commands.add(outputVideo);

            ProcessBuilder builder= new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ����Ƶ���н�ȡ����ȡ��Ƶ����ͼ
     *
     * @param inputVideo ԭʼ��Ƶ����·��
     * @param coverOut ͼƬ���·��
     * @return
     */
    public Result getVideoCoverImg(String inputVideo, String coverOut) {
//        ffmpeg -ss 00:00:01 -y -i input.mp4 -vframes 1 out.jpg
        try {
            if(StrUtils.checkBlank(inputVideo) || StrUtils.checkBlank(coverOut)) {
                throw new FFMpegExceptionn("��������Ƶ·�������ͼƬ�������");
            }
            BaseFileUtil.checkAndMkdir(coverOut);

            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-ss");
            commands.add("00:00:01");

            commands.add("-y");
            commands.add("-i");
            commands.add(inputVideo);

            commands.add("-vframes");
            commands.add("1");

            commands.add(coverOut);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ����Ƶ��ָ���뿪ʼ��ͼ���ɽض���ͼ
     *
     * @param startSeconds �����뿪ʼ��ͼ
     * @param inputVideo ��Ҫ��ͼ����Ƶ����·��
     * @param everySecondImg ÿ��ض�����ͼ
     * @param seconds һ��������ͼ������
     * @param coverOutPath ��ͼ���ɵ�·����ͼƬ���ƻ���001 002... ������
     * @return
     */
    public Result getVideoCoverImgs(Integer startSeconds, String inputVideo,Integer everySecondImg, Integer seconds,String coverOutPath) {
//        ffmpeg -y -ss 0 -i 2222.mp4 -f image2 -r 1 -t 3 -q:a 1 ./%2d.jpg
        try {
            if(StrUtils.checkBlank(inputVideo) || StrUtils.checkBlank(coverOutPath) || everySecondImg <= 0 || startSeconds <= 0 || seconds <= 0) {
                throw new FFMpegExceptionn("��������ȷ��������������Ϊ��");
            }
            BaseFileUtil.checkAndMkdir(coverOutPath);

            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");
            commands.add("-ss");
            commands.add(String.valueOf(startSeconds));

            commands.add("-i");
            commands.add(inputVideo);

            commands.add("-f");
            commands.add("image2");

            commands.add("-r");
            commands.add(String.valueOf(everySecondImg));

            commands.add("-t");
            commands.add(String.valueOf(seconds));

            commands.add("-q:a");
            commands.add("1");

            commands.add(coverOutPath + "/%3d.jpg");

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ȥ����Ƶ����Ƶ
     *
     * @param inputVideo ������Ƶ����·��
     * @param outputVideo �����Ƶ����·��
     * @return
     */
    public Result wipeAudio(String inputVideo, String outputVideo) {
//       ffmpeg -y -i source.mp4 -an -vcodec copy output.mp4
        if(StrUtils.checkBlank(inputVideo) || StrUtils.checkBlank(outputVideo)) {
            throw new FFMpegExceptionn("��������ȷ��������������Ϊ��");
        }
        BaseFileUtil.checkAndMkdir(outputVideo);

        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");
            commands.add("-i");
            commands.add(inputVideo);

            commands.add("-an");
            commands.add("-vcodec");

            commands.add("copy");
            commands.add(outputVideo);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ��Ƶ����
     *
     * @param inputVideo ��Ҫ������ԭʼ��Ƶ����·��
     * @param outWidth ����֮�����Ƶ�Ŀ��
     * @param outHeight ����֮�����Ƶ�߶�
     * @param outputVideo ����֮�����ɵ��µ���Ƶ����·��
     * @return
     */
    public Result videoScale(String inputVideo, String outWidth, String outHeight, String outputVideo) {
//       ffmpeg -y -i in.mp4 -vf scale=360:640 -acodec aac -vcodec h264 out.mp4

        if(StrUtils.checkBlank(inputVideo) || StrUtils.checkBlank(outputVideo)) {
            throw new FFMpegExceptionn("��������ȷ��������������Ϊ��");
        }
        BaseFileUtil.checkAndMkdir(outputVideo);

        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");
            commands.add("-i");
            commands.add(inputVideo);

            commands.add("-vf");
            commands.add("scale="+ outWidth + ":" + outHeight);

            commands.add("-acodec");
            commands.add("aac");

            commands.add("-vcodec");
            commands.add("h264");

            commands.add(outputVideo);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ��Ƶ��ҳ�泤����вü�
     *
     * @param inputVideo ��Ҫ������ԭʼ��Ƶ����·��
     * @param outWidth ����֮�����Ƶ�Ŀ��
     * @param outHeight ����֮�����Ƶ�߶�
     * @param outputVideo ����֮�����ɵ��µ���Ƶ����·��
     * @return
     */
    public Result videoCrop(String inputVideo, String outWidth, String outHeight, String x, String y, String outputVideo) {
//       ffmpeg -y -i in.mp4 -strict -2 -vf crop=1080:1080:0:420 out.mp4
        if(StrUtils.checkBlank(inputVideo) || StrUtils.checkBlank(outputVideo)) {
            throw new FFMpegExceptionn("��������ȷ��������������Ϊ��");
        }
        BaseFileUtil.checkAndMkdir(outputVideo);

        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");
            commands.add("-i");
            commands.add(inputVideo);

            commands.add("-strict");
            commands.add("-2");

            commands.add("-vf");
            commands.add("crop=" + outWidth + ":" + outHeight + ":" + x + ":" + y);

            commands.add(outputVideo);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ��Ƶ�Ƕ���ת
     *
     * @param inputVideo ��Ҫ������ԭʼ��Ƶ����·��
     * @param angleNum ��ת�ĽǶȣ�1��180�� 2��90��
     * @param outWidth �����Ƶ�Ŀ�ȣ������ָ����Ĭ����������Ƶ�Ŀ��
     * @param outHeight �����Ƶ�ĸ߶ȣ������ָ����Ĭ����������Ƶ�ĸ߶�
     * @param outputVideo ����֮�����ɵ��µ���Ƶ����·��
     * @return
     */
    public Result videoRotate(String inputVideo, Integer angleNum, String outWidth, String outHeight, String outputVideo) {
//       ffmpeg -i in.mp4 -vf rotate=PI/2:ow=1080:oh=1920 out.mp4
        if(StrUtils.checkBlank(inputVideo) || StrUtils.checkBlank(outputVideo)) {
            throw new FFMpegExceptionn("��������ȷ��������������Ϊ��");
        }
        BaseFileUtil.checkAndMkdir(outputVideo);

        if(angleNum != 1 && angleNum != 2) {
            throw new FFMpegExceptionn("�Ƿ���������ת�Ƕ���Ϊ-> 1��180deg or 2��90deg");
        }

        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");
            commands.add("-i");
            commands.add(inputVideo);

            commands.add("-vf");

            if(StrUtils.checkNotBlank(outWidth) && StrUtils.checkNotBlank(outHeight)) {
                commands.add("rotate=PI/"+ angleNum + ":ow=" + outWidth + ":oh" + outHeight);
            } else {
                commands.add("rotate=PI/"+ angleNum);
            }

            commands.add(outputVideo);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ������Ƶ֡��
     *
     * @param inputVideo ��Ҫ������ԭʼ��Ƶ����·��
     * @param fps ��Ҫ���ڵ�����֡
     * @param outputVideo ����֮�����ɵ��µ���Ƶ����·��
     * @return
     */
    public Result videoFps(String inputVideo, Integer fps, String outputVideo) {
//      ffmpeg -y -i in.mp4 -r 15 out.mp4
        if(StrUtils.checkBlank(inputVideo) || StrUtils.checkBlank(outputVideo)) {
            throw new FFMpegExceptionn("��������ȷ��������������Ϊ��");
        }
        BaseFileUtil.checkAndMkdir(outputVideo);

        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");
            commands.add("-i");
            commands.add(inputVideo);

            if(StrUtils.checkNotBlank(outputVideo)) {
                commands.add("-r");
                commands.add(String.valueOf(fps));
            }

            commands.add(outputVideo);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * gifת��Ϊvideo
     *
     * @param gif gif����·��
     * @param outputVideo �����Ƶ����·��
     * @return
     */
    public Result gifConvertToVideo(String gif, String outputVideo) {
//      ffmpeg -i in.gif -vf scale=420:-2,format=yuv420p out.mp4  // gif --> mp4
        if(StrUtils.checkBlank(gif) || StrUtils.checkBlank(outputVideo)) {
            throw new FFMpegExceptionn("��������ȷ��������������Ϊ��");
        }
        BaseFileUtil.checkAndMkdir(outputVideo);

        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");
            commands.add("-i");
            commands.add(gif);

            commands.add("-vf");

            commands.add("scale=420:-2,format=yuv420p");

            commands.add(outputVideo);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ��Ƶתgif
     *
     * @param inputVideo �������Ƶ�ľ���·��
     * @param outputGif ����gif���������·��
     * @param highQuality �Ƿ����ɸ�����gif
     * @return
     */
    public Result videoConvertToGif(String inputVideo,  String outputGif, boolean highQuality) {
//      ffmpeg -i src.mp4 -b 2048k des.gif
//      ffmpeg -i src.mp4 des.gif
        if(StrUtils.checkBlank(inputVideo) || StrUtils.checkBlank(outputGif)) {
            throw new FFMpegExceptionn("��������ȷ��������������Ϊ��");
        }
        BaseFileUtil.checkAndMkdir(outputGif);
        try {
            BaseFileUtil.checkAndMkdir(outputGif);

            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");
            commands.add("-i");
            commands.add(inputVideo);

            if(highQuality) {
                commands.add("-b");
                commands.add("2048k");
            }

            commands.add(outputGif);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }


    /**
     * ����Ƶ�Ĳ���ʱ����вü�
     *
     * @param inputVideo ԭʼ��Ҫ������Ƶ�ľ���·��
     * @param startTime ��ʼ�ü���ʱ�� ֧�ָ�ʽ�� 2  ��  00:00:02 ��2�뿪ʼ
     * @param seconds  ���ó�����ʱ�� ֧�ָ�ʽ�� 3 �� 00:00:03 ����3��
     * @param outputVideo �����Ƶ�ľ���·��
     * @return
     */
    public Result videoCut(String inputVideo,String startTime, String seconds, String outputVideo) {
//      ffmpeg -ss 10 -t 15 -accurate_seek -i test.mp4 -codec copy -avoid_negative_ts 1 cut.mp4
//      ffmpeg -i src.mp4  -ss 00:00:00 -t 00:00:20 des.mp4
        if(StrUtils.checkBlank(inputVideo) || StrUtils.checkBlank(startTime) || StrUtils.checkBlank(seconds) || StrUtils.checkBlank(outputVideo)) {
            throw new FFMpegExceptionn("��������ȷ��������������Ϊ��");
        }
        BaseFileUtil.checkAndMkdir(outputVideo);

        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

           // commands.add("-y");
            commands.add("-ss");
            commands.add(startTime);

            commands.add("-t");
            commands.add(seconds);

            commands.add("-accurate_seek");

            commands.add("-i");
            commands.add(inputVideo);

            commands.add("-codec");
            commands.add("copy");
            commands.add("-avoid_negative_ts");
            commands.add("1");

            commands.add(outputVideo);
           // System.out.println(commands);
            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     *  ��Ƶ�ϲ���Ƶ������Ƶ���ϱ������֣�����������Ƶԭ��
     *
     * ���˷�����Macƽ̨��Ч,ffmpeg version 4.2.1-tessus�����ݲ�����Ƿ�ffmpeg�汾����
     *  Macƽ̨ʹ�÷�����convertorWithBgmNoOriginCommon()
     *
     * @param videoInputPath ԭʼ��Ƶ����·��
     * @param videoOutPath  ����֮����Ƶ���·��
     * @param bgmInputPath  ��ӵı������־���·��
     * @param seconds   �����Ƶ������
     * @return
     */
    public Result mergeVideoAndBgmNoOrigin(String videoInputPath, String videoOutPath, String bgmInputPath, double seconds) {
//        ffmpeg -i input.mp4 -i bgm.mp3 -t 7 -y out.mp4
        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-i");
            commands.add(videoInputPath);

            commands.add("-i");
            commands.add(bgmInputPath);

            commands.add("-t");
            commands.add(String.valueOf(seconds));

            commands.add("-y");
            commands.add(videoOutPath);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ��Ƶ�ϲ���Ƶ������Ƶ���ϱ������֣�����������Ƶԭ�����˷����Ƚ�ͨ�ã�����Mac����ʹ��
     *
     * @param videoInputPath  ԭʼ��Ƶ����·��
     * @param videoOutPath  ����֮����Ƶ���·��
     * @param noSoundVideoPath  ԭʼ��Ƶȥ����Ƶ���������·��
     * @param bgmInputPath  ��ӵı������־���·��
     * @param seconds   �����Ƶ������
     * @return
     */
    public Result convertorWithBgmNoOriginCommon(String videoInputPath, String videoOutPath, String noSoundVideoPath, String bgmInputPath, double seconds) {
//        ffmpeg -i hi.mp4 -c:v copy -an nosound.mp4
//        ffmpeg -i nosound.mp4 -i songs.mp3 -t 7.1 -c copy output.mp4
        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-y");

            commands.add("-i");
            commands.add(videoInputPath);

            commands.add("-c:v");
            commands.add("copy");
            commands.add("-an");
            commands.add(noSoundVideoPath);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            StreamHanlerCommon.closeStreamQuietly(process);

            // ת��nosound.mp4 Ϊ�������ֵ�mp4
            return convertNoSoundVideoToBgmVideo(videoOutPath,noSoundVideoPath,bgmInputPath,seconds);
        } catch (Exception e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }

    /**
     * ��û�б������ֵ�videoת��Ϊ�б�������video
     * ������Ƶ���ϱ�������
     *
     * @param videoOutPath
     * @param noSoundVideoPath
     * @param bgmInputPath
     * @param seconds
     * @throws IOException
     */
    private Result convertNoSoundVideoToBgmVideo(String videoOutPath, String noSoundVideoPath,String bgmInputPath, double seconds) throws IOException {
        List<String> commands2 = new ArrayList<>();
        commands2.add(ffmpegEXE);

        commands2.add("-i");
        commands2.add(noSoundVideoPath);

        commands2.add("-i");
        commands2.add(bgmInputPath);

        commands2.add("-t");
        commands2.add(String.valueOf(seconds));

        commands2.add("-y");
        commands2.add(videoOutPath);

        ProcessBuilder builder2 = new ProcessBuilder(commands2);
        Process process2 = builder2.start();

        return StreamHanlerCommon.closeStreamQuietly(process2);
    }
    /**
     * �޸���Ƶ����ͼƬ
     *
     * @param videoInputPath ԭʼ��Ƶ����·��
     * @param imagePath  �滻�ķ���ͼƬ����·��
     * @param videoOutPath  �µ���Ƶ�������·��
     * @return
     */
    public Result transformVideoCover(String videoInputPath, String imagePath, String videoOutPath) {
        // -y ������ʾ����ͬ�����ļ�
        // ffmpeg -i x.mp4 -i 1.png -map 1 -map 0 -c copy -disposition:0 attached_pic -y y.mp4
        if(StrUtils.checkBlank(videoInputPath) || StrUtils.checkBlank(imagePath) || StrUtils.checkBlank(videoOutPath)) {
            throw new FFMpegExceptionn("��������ȷ��������������Ϊ��");
        }
        BaseFileUtil.checkAndMkdir(videoOutPath);
        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-i");
            commands.add(videoInputPath);

            commands.add("-i");
            commands.add(imagePath);

            commands.add("-map");
            commands.add("1");
            commands.add("-map");
            commands.add("0");
            commands.add("-c");
            commands.add("copy");
            commands.add("-disposition:0");
            commands.add("attached_pic");

            commands.add("-y");
            commands.add(videoOutPath);

            // TODO ʹ�õ���ģʽ�����߽��ö�����Ϊ��̬���Ա������ɣ�����ÿ��new
            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }
    
    
    /**
     * �ϲ������Ƶ��Shawnʵ�֣�, �����ļ���˳��ϲ�
     *
     * @param videoListFile ����·���µ���Ƶlist�ļ�
     * @apiNote               �ļ���ʽ��video.txt or video.list
     *
     *                           file 'http://xxxxx/filename1.ts'
     *                           file 'http://xxxxx/filename2.ts'
     *
     *
     * �ο�sample�ļ���docs/video-example/video-online-example.txt
     *
     *
     * @param videoOutPath ��Ƶ���·��
     * @return
     */
    public Result mergeMultiVideos(File videoListFile, String videoOutPath) {
    	  if(StrUtils.checkBlank(videoOutPath)) {
              throw new FFMpegExceptionn("videoOutPath must be valid");
          }
          BaseFileUtil.checkAndMkdir(videoOutPath);
          if(!videoListFile.exists() || !videoListFile.isFile()) {
              throw new FFMpegExceptionn("videoListFile not found");
          }
          
          try {
              List<String> commands = new ArrayList<>();
              commands.add(ffmpegEXE);

              commands.add("-f");
              commands.add("concat");

              commands.add("-safe");
              commands.add("0");

             
              commands.add("-i");
              commands.add(videoListFile.getAbsolutePath());
              commands.add("-codec");
              commands.add("copy");

              commands.add("-y");
              commands.add(videoOutPath);

              ProcessBuilder builder = new ProcessBuilder(commands);
              Process process = builder.start();

              return StreamHanlerCommon.closeStreamQuietly(process);
          } catch (IOException e) {
              throw new FFMpegExceptionn(e.getMessage());
          }
    }
    
    /**
     * ����Ƶ�����Ļ��Shawnʵ�֣�
     *
     * @param inputVideo ������Ƶ·��
     * @param outputVideo �����Ƶ·��
     * @param seconds �����Ƶ����
     * @return
     *
     *
     */
    // ffmpeg  -i  x.mp4  -vf subtitles=y.srt -y output.mp4
    public Result mergeVideoAndSubtitles(String videoInputPath, String subtitlesPath, String videoOutPath) {
    	if(StrUtils.checkBlank(videoInputPath) || StrUtils.checkBlank(subtitlesPath) || StrUtils.checkBlank(videoOutPath)) {
            throw new FFMpegExceptionn("��������ȷ��������������Ϊ��");
        }
        BaseFileUtil.checkAndMkdir(videoOutPath);
        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-i");
            commands.add(videoInputPath);

            commands.add("-vf");
        
            commands.add("subtitles="+subtitlesPath);

            commands.add("-y");
            commands.add(videoOutPath);

            // TODO ʹ�õ���ģʽ�����߽��ö�����Ϊ��̬���Ա������ɣ�����ÿ��new
            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }
    
    /**
     * �ϲ����������Ƶ��ts��ʽ��, �����ļ���������Ƶ��ַ˳��ϲ�
     *
     * @param videoListFile ����·���µ���Ƶlist�ļ�
     * @apiNote               �ļ���ʽ��video.txt or video.list
     *
     *                           file 'http://xxxxx/filename1.ts'
     *                           file 'http://xxxxx/filename2.ts'
     *
     *
     * �ο�sample�ļ���docs/video-example/video-online-example.txt
     *
     *
     * @param videoOutPath ��Ƶ���·��
     * @return
     */
    public Result mergeMultiOnlineVideos(File videoListFile, String videoOutPath) {
        // ffmpeg -f concat -safe 0 -protocol_whitelist "file,http,https,tcp,tls" -i video.txt -c copy -y out.mp4
        if(StrUtils.checkBlank(videoOutPath)) {
            throw new FFMpegExceptionn("videoOutPath must be valid");
        }
        BaseFileUtil.checkAndMkdir(videoOutPath);
        if(!videoListFile.exists() || !videoListFile.isFile()) {
            throw new FFMpegExceptionn("videoListFile not found");
        }

        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-f");
            commands.add("concat");

            commands.add("-safe");
            commands.add("0");

            commands.add("-protocol_whitelist");
            commands.add("file,http,https,tcp,tls");
            commands.add("-i");
            commands.add(videoListFile.getAbsolutePath());
            commands.add("-c");
            commands.add("copy");

            commands.add("-y");
            commands.add(videoOutPath);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e.getMessage());
        }
    }
    
    /**
     * �ϲ������Ƶ�ļ�(�˷���ֻ����ts��ʽ�ļ�,����mpg/mpeg��ʽ�ļ�)
     *
     * @param fileNameList ��Ҫ�ϲ�����Ƶ�ļ����ϣ��ļ�����Ϊ����·��
     * @param videoOutPath ��Ƶ�������·��
     * @return ���ؽ��code����Ϣ
     */
    public Result mergeMultiVideosOfTsOrMpegFormat(List<String> fileNameList, String videoOutPath) {
        // ffmpeg -f concat -safe 0 -i video.txt -c copy -y out.mp4
        if(StrUtils.checkBlank(videoOutPath)) {
            throw new FFMpegExceptionn("videoOutPath must be valid");
        }
        if(fileNameList == null || fileNameList.isEmpty()) {
            throw new FFMpegExceptionn("fileNameList must be valid");
        }
        BaseFileUtil.checkAndMkdir(videoOutPath);

        String filenames = VideoFormatter.fileNameFormat(fileNameList);

        try {
            List<String> commands = new ArrayList<>();
            commands.add(ffmpegEXE);

            commands.add("-i");
            commands.add("concat:" + filenames);

            commands.add("-c");
            commands.add("copy");

            commands.add("-y");
            commands.add(videoOutPath);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            return StreamHanlerCommon.closeStreamQuietly(process);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e);
        }
    }

    /**
     * ����ָ���ļ��ж�����Ƶ����·����Ϣ������˳��ϲ���Ƶ��
     *
     *
     * @param videoListFile ����ϲ���Ƶ���ļ�������ָ����ʽ����
     *                       �ļ���ʽ��video.txt or video.list
     *
     *                        file 'http://xxxxx/filename1.ts'
     *                        file 'http://xxxxx/filename2.ts'
     *
     *  �ļ�ʾ����docs/video-example/video-example.txt
     *
     *
     * @apiNote ע�⣺ �ϲ�����Ƶ������ͬ�ķֱ��ʺ͸�ʽ����������ϳ���Ƶ����ȷ
     *
     * @param videoOutPath ��Ƶ�������·���ļ���
     * @return
     */
    public Result mergeMultiVideosByFile(File videoListFile, String videoOutPath) {
        if(StrUtils.checkBlank(videoOutPath)) {
            throw new FFMpegExceptionn("videoOutPath must be valid");
        }
        if(!videoListFile.exists() || !videoListFile.isFile()) {
            throw new FFMpegExceptionn("videoListFile not found");
        }
        BaseFileUtil.checkAndMkdir(videoOutPath);

        List<String> commands = new ArrayList<>();
        commands.add(ffmpegEXE);

        commands.add("-f");
        commands.add("concat");

        commands.add("-safe");
        commands.add("0");

        commands.add("-i");
        commands.add(videoListFile.getAbsolutePath());
        commands.add("-c");
        commands.add("copy");

        commands.add("-y");
        commands.add(videoOutPath);

        return ProcessCommand.start(commands);
    }
    
    
    /**
     * �����ļ�Ŀ¼���Զ��ϲ���Ŀ¼��������Ƶ
     *
     * �ϳɵ�˳�����ļ����ƽ������򣬽��齫��������Ϊ���001��002��003...
     *
     * TODO ��������
     *
     * @param dir ��Ƶ�ļ�Ŀ¼����·��
     * @param videoOutPath ��Ƶ�������·��
     * @return
     */
    public Result autoMergeMultiVideosByDir(String dir, String videoOutPath) {
        // �����ļ���
        if(!BaseFileUtil.hashFile(dir)) {
            throw new FFMpegExceptionn("File must be not null");
        }
        BaseFileUtil.checkAndMkdir(videoOutPath);
        // �ж��ļ��Ƿ�ͳһ��׺�������ͳһ��׺���׳��쳣
        File[] files = BaseFileUtil.listFiles(dir);

        if(!BaseFileUtil.unifySuffix(files)) {
            throw new FFMpegExceptionn("All video files must have the same suffix");
        }

        // �����mpg/mpeg�ļ�����ֱ��ת�����÷����ϲ�
        if(BaseFileUtil.isMpgOrMpeg(files)) {
            // �ϲ�mpg/mpeg
            return mergeMultiVideosOfTsOrMpegFormat(Arrays.asList(BaseFileUtil.list(dir)), videoOutPath);
        }

        File tempVideoFile = null;
        try {
            tempVideoFile = VideoFormatter.createTempVideoFile(dir);

            return mergeMultiVideosByFile(tempVideoFile, videoOutPath);
        } catch (IOException e) {
            throw new FFMpegExceptionn(e);
        } finally {
            if(tempVideoFile != null) {
                tempVideoFile.deleteOnExit();
            }
        }
    }

}
