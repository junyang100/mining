package com.mine.vo.resp;


import com.mine.vo.BaseReq;

public class AccountResp extends BaseReq{
    private String userId;
    private String phone;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
