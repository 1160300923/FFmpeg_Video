package com.ffmpeg.common.response;

/**
 * @auther alan.chen
 * @time 2019/9/11 1:51 PM
 */
public class Result {

    /**
     * ״̬�룺 0 ���� �������쳣
     */
    private Integer code;

    /**
     * ������Ϣ
     */
    private String errMessage;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
