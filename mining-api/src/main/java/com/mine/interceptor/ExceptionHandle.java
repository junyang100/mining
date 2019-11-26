package com.mine.interceptor;

import com.mine.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一错误拦截
 */
@ControllerAdvice
public class ExceptionHandle {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public String handle(BizException e) {
        logger.info("BizException:", e);
        return null;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String handle(Exception e) {
        logger.error("Exception:", e);
        return null;
    }
}
