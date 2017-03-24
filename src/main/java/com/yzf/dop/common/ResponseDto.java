package com.yzf.dop.common;

import com.alibaba.fastjson.JSONObject;

public class ResponseDto {
    public int code;
    public String codeMsg;
    public Object data;
    public String token;

    public ResponseDto() {
    }

    /**
     * 构造方法 带有token信息
     *
     * @param code
     * @param codeMsg
     */
    public ResponseDto(int code, String codeMsg) {
        this.code = code;
        this.codeMsg = codeMsg;
        this.token = ThreadLocalHolder.getToken();
    }

    /**
     * 构造方法 带有token信息
     *
     * @param code
     * @param codeMsg
     * @param data
     */
    public ResponseDto(int code, String codeMsg, Object data) {
        this.code = code;
        this.codeMsg = codeMsg;
        this.data = data;
        this.token = ThreadLocalHolder.getToken();
    }

    /**
     * 构造方法 指定token信息
     *
     * @param code
     * @param codeMsg
     * @param data
     */
    public ResponseDto(int code, String codeMsg, Object data, String token) {
        this.code = code;
        this.codeMsg = codeMsg;
        this.data = data;
        this.token = token;
    }

    /**
     * 返回不带错误message的 ResponseDto
     *
     * @param code
     * @return
     */
    public static ResponseDto createErrorDto(int code) {
        return new ResponseDto(code, "");
    }

    /**
     * 返回错误ResponseDto - 指定code message
     *
     * @param code
     * @param codeMsg
     * @return
     */
    public static ResponseDto createErrorDto(int code, String codeMsg) {
        return new ResponseDto(code, codeMsg);
    }

    /**
     * 返回错误ResponseDto 不带data信息
     *
     * @param errorEnum
     * @return
     */
    public static ResponseDto createErrorDto(ErrorEnum errorEnum) {
        return new ResponseDto(errorEnum.getCode(), errorEnum.getDesc());
    }

    /**
     * 返回错误ResponseDto 带data信息
     *
     * @param errorEnum
     * @return
     */
    public static ResponseDto createErrorDto(ErrorEnum errorEnum, Object data) {
        return new ResponseDto(errorEnum.getCode(), errorEnum.getDesc(), data);
    }

    /**
     * 返回成功信息
     *
     * @param data
     * @return
     */
    public static ResponseDto createSuccessDto(Object data) {
        return new ResponseDto(ErrorEnum.SUCCESS.getCode(), ErrorEnum.SUCCESS.getDesc(), data);
    }

    /**
     * 返回成功信息
     *
     * @param data
     * @return
     */
    public static ResponseDto createSuccessDtoWithMsg(Object data, String message) {
        return new ResponseDto(ErrorEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 返回成功信息
     *
     * @param codeMsg 指定成功信息
     * @return
     */
    public static ResponseDto createSuccessWithMsgDto(String codeMsg) {
        return new ResponseDto(ErrorEnum.SUCCESS.getCode(), codeMsg);
    }

    /**
     * 返回成功数据 指定token
     *
     * @param data
     * @param token
     * @return
     */
    public static ResponseDto createSuccessDto(Object data, String token) {
        return new ResponseDto(ErrorEnum.SUCCESS.getCode(), ErrorEnum.SUCCESS.getDesc(), data, token);
    }

    /**
     * 返回成功信息
     *
     * @param codeMsg 指定成功信息 指定token
     * @return
     */
    public static ResponseDto createSuccessWithMsgDto(String codeMsg, Object data, String token) {
        return new ResponseDto(ErrorEnum.SUCCESS.getCode(), codeMsg, data, token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(String codeMsg) {
        this.codeMsg = codeMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
