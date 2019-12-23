package com.mine.web;

import com.mine.bean.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @ExceptionHandler
  public JsonResult<Object> handleException(Throwable e){

        logger.error("controller error",e);
        return new JsonResult<>(JsonResult.FAIL_STATUS,"系统异常",e.getMessage());

  }


}
