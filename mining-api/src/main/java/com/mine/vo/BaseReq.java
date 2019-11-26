package com.mine.vo;

import java.io.Serializable;


public class BaseReq implements Serializable {
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
