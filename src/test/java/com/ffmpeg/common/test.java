package com.ffmpeg.common;

import com.ffmpeg.common.response.Result;
import com.ffmpeg.common.video.VideoOperation;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * @auther alan.chen
 * @time 2019/9/11 3:11 PM
 */
public class test {

    private static final String ffmpegEXE = "/Users/mac/eclipse-workspace/Video_Process/src/ffmpeg";
    public static final Integer EXPECTED = 0;
    @Test
    public void testConverTest() throws IOException {
        String inputPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/input1.avi";
        String outPutPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/out10.mp4";
        VideoOperation ffmpeg = VideoOperation.builder(ffmpegEXE);
        Result result = ffmpeg.videoConvert(inputPath, outPutPath);
        System.out.println(result.getCode());
        System.out.println(result.getErrMessage());
    }
    @Test
    public void testvideoCutTest() throws IOException {
        String inputPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/all.avi";
        String outPutPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/output1.avi";
        VideoOperation ffmpeg = VideoOperation.builder(ffmpegEXE);
        Result result = ffmpeg.videoCut(inputPath,"0", "3", outPutPath);
        System.out.println(result.getCode());
        System.out.println(result.getErrMessage());
    }
    @Test
    public void videoRotateTest() throws IOException {
        String inputPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/input1.avi";
        String outPutPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/output2.avi";
        VideoOperation ffmpeg = VideoOperation.builder(ffmpegEXE);
        Result result = ffmpeg.videoRotate(inputPath,2,"","" , outPutPath);
        System.out.println(result.getCode());
        System.out.println(result.getErrMessage());
    }

    @Test
    public void videoConverToGifTest() throws IOException {
        String inputPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/input1.avi";
        String outPutPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/output3.gif";
        VideoOperation ffmpeg = VideoOperation.builder(ffmpegEXE);
        Result result = ffmpeg.videoConvertToGif(inputPath,outPutPath, true);
        System.out.println(result.getCode());
        System.out.println(result.getErrMessage());
    }
    @Test
    public void mergeMultiVideostest() throws IOException {
    		String inputPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/list.txt";
        String outPutPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/mutinout.avi";
        VideoOperation ffmpeg = VideoOperation.builder(ffmpegEXE);
        Result result = ffmpeg.mergeMultiOnlineVideos(new File(inputPath), outPutPath);
        Assert.assertEquals(EXPECTED, result.getCode());  
        System.out.println(result.getErrMessage());
        System.out.println(result);
    }
    
    @Test
    public void convertorWithBgmTest() throws IOException {
        String inputPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/5622.mp4";
        String outPutPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/outputBGM.mp4";
        String noSoundPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/nosound.mp4";
        String audioPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/sound.mp3";
        VideoOperation ffmpeg = VideoOperation.builder(ffmpegEXE);
        Result result = ffmpeg.convertorWithBgmNoOriginCommon(inputPath, outPutPath, noSoundPath, audioPath, 10);
        Assert.assertEquals(EXPECTED, result.getCode());
        System.out.println(result.getErrMessage());
    }
    
    @Test
    public void mergeVideoAndSubtitlesTest() throws IOException {
    		String inputPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/5622.mp4";
        String substitlesPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/words.srt";
        String outPutPath = "/Users/mac/eclipse-workspace/Video_Process/src/video_list/subtitlesoutput2.mp4";
        VideoOperation ffmpeg = VideoOperation.builder(ffmpegEXE);
        Result result = ffmpeg.mergeVideoAndSubtitles(inputPath, substitlesPath, outPutPath);
        Assert.assertEquals(EXPECTED, result.getCode());
        System.out.println(result.getErrMessage());
    }
    /*@Test
    public void convertorWithBgmTest() throws IOException {
        String inputPath = "/Users/alan.chen/Documents/notes/test/11111.mp4";
        String outPutPath = "/Users/alan.chen/Documents/notes/test/1/222.mp4";
        String noSoundPath = "/Users/alan.chen/Documents/notes/test/1/nosound.mp4";
        String audioPath = "/Users/alan.chen/Documents/notes/young.mp3";
        VideoOperation ffmpeg = VideoOperation.builder(ffmpegEXE);
        Result result = ffmpeg.convertorWithBgmNoOriginCommon(inputPath, outPutPath, noSoundPath, audioPath, 5);
        Integer expected = 0;
        Assert.assertEquals(expected, result.getCode());
        System.out.println(result.getErrMessage());
    }
    */


}
