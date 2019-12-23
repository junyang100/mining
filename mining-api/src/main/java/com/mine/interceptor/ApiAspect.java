package com.mine.interceptor;

import com.alibaba.fastjson.JSON;
import com.mine.common.MD5Util;
import com.mine.common.MessageUtils;
import com.mine.constant.ErrorMsg;
import com.mine.exception.BizException;
import com.mine.vo.ApiResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 统一拦截切面
 */
@Aspect
@Component
@Order(2)
public class ApiAspect {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ApiAspect.class);

    @Value("${app.auth.key}")
    private String authKey;

    @Autowired
    private MessageUtils messageUtils;

    @Pointcut("@annotation(com.mine.interceptor.ApiAnnotation)")
    public void pointCut() {
    }

    @Around("pointCut()&& @annotation(annotation)")
    public Object doAround(ProceedingJoinPoint pjp, ApiAnnotation annotation) throws Throwable {
        long start = System.currentTimeMillis();
        String method = getTargetMethod(pjp);
        // 校验签名
        if (annotation.checkSign() && !verifySign()) {
            return transformResult(ApiResult.fail(ErrorMsg.SIGN_ERROR));
        }
        // 校验登陆
        if (annotation.needLogin()) {
            //todo
        }
        // 执行业务
        Object[] args = pjp.getArgs();
        Object rs;
        try {
            rs = pjp.proceed(args);
        } catch (BizException e) {
            logger.error("{}.BizException:code={},msg={}", method, e.getCode(), e.getMsg());
            rs = new ApiResult(e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("Exception:", e);
            rs = ApiResult.fail();
        }
        rs = transformResult(rs);
        logger.info("{}.response:{}", method, JSON.toJSONString(rs));
        logger.info("{}.time.length:{}", method, System.currentTimeMillis() - start);
        return rs;
    }

    private boolean verifySign() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        try {
            return MD5Util.verifySign(authKey, attributes.getRequest());
        } catch (Exception e) {
            logger.error("verifySign.error", e);
        }
        return false;
    }

    private Object transformResult(Object obj) {
        try {
            if (obj instanceof ApiResult) {
                ApiResult rs = (ApiResult) obj;
                String msg = messageUtils.getMessage(rs.getMsg());
                rs.setMsg(msg);
                return rs;
            }
        } catch (Exception e) {
            logger.error("transformResult.error", e);
        }

        return obj;
    }

    private String getTargetMethod(ProceedingJoinPoint pjp) {
        return pjp.getTarget().getClass() + "." + pjp.getSignature().getName();
    }

}