package com.mine.constant;


/**
 * 返回错误码枚举
 */
public enum ErrorMsg {
    SUCCESS("0", "success"),
    FAIL("-1", "fail"),
    URL_ERROR("10000", "url.error"),
    REQUEST_OSS_ERROR("10001", "request.oss.error"),
    RESPONSE_OSS_ERROR("10002", "response.oss.error"),
    UPLOAD_FILE_EMPTY("10003", "upload.file.empty"),
    RESULT_ERROR("10004", "result.error"),
    BIZTYPE_ERROR("10005", "biz.type.error"),
    BIZTYPE_CONF_ERROR("10006", "biz.type.conf.error");

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
