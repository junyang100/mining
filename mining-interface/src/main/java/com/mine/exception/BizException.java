package com.mine.exception;


import com.mine.constant.ErrorMsg;

public class BizException extends RuntimeException {
    private String code;
    private String msg;

    public BizException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BizException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public static void throwException(ErrorMsg msgEnum) {
        throw new BizException(msgEnum.getCode(), msgEnum.getKey());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
