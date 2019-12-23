package com.mine.util;

import com.mine.bean.User;
import com.mine.constants.Constants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Set;

public class WebUtils {



  public static Set<String> getUserPrivilegeFromSession(){

      /**  从session中获取用户的权限      **/
      ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
      HttpSession httpSession = requestAttributes.getRequest().getSession();
      User u = (User)httpSession.getAttribute(Constants.KEY_SESSION_USER);
      Set<String> applicationSet  = (Set<String> ) httpSession.getAttribute(Constants.PRIVILEGE_KEY_PRE + u.getId());
      return applicationSet;
  }


  public static User getUserFromSession(){

      ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
      HttpSession httpSession = requestAttributes.getRequest().getSession();
      return  (User)httpSession.getAttribute(Constants.KEY_SESSION_USER);
  }



}
