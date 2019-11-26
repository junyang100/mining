package com.mine.interceptor;

import com.alibaba.fastjson.JSON;
import com.mine.common.MessageUtils;
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

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        String method = getTargetMethod(pjp);
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