package com.mine.vo.req;


import com.mine.vo.BaseReq;

public class AccountReq extends BaseReq{
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
