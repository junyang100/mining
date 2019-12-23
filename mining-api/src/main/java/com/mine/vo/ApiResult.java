package com.mine.vo;


import com.mine.constant.ErrorMsg;
import org.apache.commons.lang.StringUtils;

public class ApiResult<T> {
    private String code;
    private T data;
    private String msg;

    public static <T> ApiResult<T> success(T t) {
        return new ApiResult(ErrorMsg.SUCCESS.getCode(),
                t, ErrorMsg.SUCCESS.getKey());
    }

    public static ApiResult fail() {
        return new ApiResult(ErrorMsg.FAIL.getCode(), ErrorMsg.FAIL.getKey());
    }

    public static ApiResult fail(ErrorMsg msg) {
        return new ApiResult(msg.getCode(), msg.getKey());
    }

    public ApiResult() {
    }

    public ApiResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResult(String code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return StringUtils.equals(code, ErrorMsg.SUCCESS.getCode()) ? true : false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
