package com.mine.config;


import com.mine.bean.ApplicationConfig;
import com.mine.bean.JsonResult;
import com.mine.util.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@Aspect
public class AppAspect {

    /**
     * 验证用户数据权限
     */
   @Pointcut("@annotation(com.mine.annotation.AppPrivilege)")
   public void  appConfigPointcut(){}


   @Around(value = "appConfigPointcut()")
   public Object  doPrivelgeCheck(ProceedingJoinPoint proceedingJoinPoint) throws  Throwable{
       ApplicationConfig config  = (ApplicationConfig)proceedingJoinPoint.getArgs()[0];
       Set<String> applicationSet  = WebUtils.getUserPrivilegeFromSession();
       if(applicationSet.size() == 0){
           return new JsonResult(JsonResult.OK_STATUS,JsonResult.OK_MSG,Collections.EMPTY_LIST,0);
       }
       String paramApplication = config.getApplication();
       if(StringUtils.isNotBlank(paramApplication)){
           if(applicationSet.contains(paramApplication)){
               Set<String> tempSet = new HashSet<>(4);
               tempSet.add(paramApplication);
               config.setApplications(tempSet);
           }else{
               return new JsonResult(JsonResult.OK_STATUS,JsonResult.OK_MSG,Collections.EMPTY_LIST,0);
           }
       }else{
           config.setApplications(applicationSet);
       }

       return proceedingJoinPoint.proceed();

   }




}
