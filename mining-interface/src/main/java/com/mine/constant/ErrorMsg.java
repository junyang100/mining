package com.mine.constant;


/**
 * 返回错误码枚举
 */
public enum ErrorMsg {
    SUCCESS("0", "success"),
    FAIL("-1", "fail"),
    SIGN_ERROR("10000", "sign.error");

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误key
     */
    private String key;

    ErrorMsg(String code, String key) {
        this.code = code;
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
