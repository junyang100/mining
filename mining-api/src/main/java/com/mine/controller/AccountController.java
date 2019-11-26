package com.mine.controller;

import com.alibaba.fastjson.JSON;
import com.mine.interceptor.ApiAnnotation;
import com.mine.vo.ApiResult;
import com.mine.vo.req.AccountReq;
import com.mine.vo.resp.AccountResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    private static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @ApiAnnotation
    @RequestMapping("/info")
    public ApiResult accountInfo(AccountReq req) {
        System.out.println(JSON.toJSONString(req));
        AccountResp resp = new AccountResp();
        resp.setPhone("18611112222");
        resp.setUserId(req.getUserId());
        return ApiResult.success(resp);
    }
}
