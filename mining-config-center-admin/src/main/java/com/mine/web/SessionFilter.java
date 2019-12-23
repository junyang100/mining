package com.mine.web;

import com.alibaba.fastjson.JSON;
import com.mine.bean.JsonResult;
import com.mine.bean.User;
import com.mine.constants.Constants;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SessionFilter implements Filter {

    private static List<String> passedUrls = new ArrayList<>();
    private static Set<String> adminURLS = new HashSet();
    private static Set<String> adminUsers = new HashSet();
    private static String FORBIDDEN = "/403.html";
    private static String NO_LOGIN = "/login.html";
    static {
        passedUrls.add("/user/login");
        passedUrls.add("/js");
        passedUrls.add("/layui");
        passedUrls.add("/css");
        passedUrls.add("/image");
        passedUrls.add("/login.html");
        passedUrls.add("/fonts");
        passedUrls.add("/jquery");
        passedUrls.add("/403.html");

        adminURLS.add("/html/platform.html");
        adminURLS.add("/html/user.html");
        adminURLS.add("/html/menu.html");
        adminURLS.add("/html/application.html");
        adminURLS.add("/user/assignUserMenu");
        adminURLS.add("/user/assignUserDataPrivilege");
        adminURLS.add("/user/removeUser");

        adminURLS.add("/delete/application");




        adminUsers.add("admin");
        adminUsers.add("superAdmin");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;

        String path = httpServletRequest.getRequestURI();
        if(isPassed(path)){
            chain.doFilter(request,response);
        }else{
            HttpSession httpSession = httpServletRequest.getSession();
            if(httpSession.getAttribute(Constants.KEY_SESSION_USER) != null){
                 User u = (User) httpSession.getAttribute(Constants.KEY_SESSION_USER);
                 if(adminURLS.contains(path)){
                     if(!adminUsers.contains(u.getUserName())){
                         processJump(httpServletRequest,httpServletResponse,FORBIDDEN);
                     }else{
                         chain.doFilter(request,response);
                     }
                 }else{
                     chain.doFilter(request,response);
                 }
            }else{
                processJump(httpServletRequest,httpServletResponse,NO_LOGIN);
            }

        }

    }


    private void processJump(HttpServletRequest request,HttpServletResponse response,String type)throws IOException{
        boolean isAjaxRequest = false;
        String ajaxHeadValue = request.getHeader("x-requested-with");
        if(StringUtils.isNotBlank(ajaxHeadValue) && ajaxHeadValue.equalsIgnoreCase("XMLHttpRequest")){
            isAjaxRequest = true;
        }
        if(isAjaxRequest){
            JsonResult<Object> jsonResult ;
            if(type.equals(FORBIDDEN)){
                 jsonResult = new JsonResult<>(JsonResult.FORBIDDEN_ERROR,"未授权访问",FORBIDDEN);
            }else{
                 jsonResult = new JsonResult<>(JsonResult.USER_VALIDATE_ERROR,"未授权访问",NO_LOGIN);
            }
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(jsonResult));
            writer.close();
            response.flushBuffer();
        }else{
            response.sendRedirect(type);
        }
    }



    private boolean isPassed(String path){

        for(String passedUrl : passedUrls){
            if(path.contains(passedUrl)){
                return true;
            }
        }

        return false;
    }



    @Override
    public void destroy() {

    }
}
