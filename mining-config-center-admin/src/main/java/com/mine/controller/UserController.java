package com.mine.controller;

import com.mine.bean.JsonResult;
import com.mine.bean.Menu;
import com.mine.bean.User;
import com.mine.constants.Constants;
import com.mine.service.MenuServcie;
import com.mine.service.PrivilegeService;
import com.mine.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private MenuServcie menuServcie;

    /**
     * 登陆接口
     * @param httpSession
     * @param userParam
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public JsonResult<User> login(HttpSession httpSession, User userParam){
        JsonResult<User> jsonResult = null;
        try{
            jsonResult = userService.login(userParam);
            if(jsonResult.ok()){
                httpSession.setAttribute(Constants.KEY_SESSION_USER,jsonResult.getData());
                Integer userId = jsonResult.getData().getId();
                /** 查询用户数据权限     **/
                Set<String> privilegeList = privilegeService.queryUserPrivilege(jsonResult.getData().getId());
                httpSession.setAttribute(Constants.PRIVILEGE_KEY_PRE + jsonResult.getData().getId(),privilegeList);
                /**   查询菜单权限      **/
                List<Menu> menuList  = menuServcie.queryUserMenus(userId);
                    httpSession.setAttribute(Constants.KEY_MENU_USER_PRE + String.valueOf(userId),menuList);
                    Set<String> urlSet = new HashSet<>();
                    for(Menu menu : menuList){
                        urlSet.add(menu.getMenuAction());
                        for(Menu subMenu : menu.getChildrenMenu()){
                            urlSet.add(subMenu.getMenuAction());
                        }
                    }
                    httpSession.setAttribute(Constants.KEY_MENU_URL_PRE + String.valueOf(userId),urlSet);

                }
            jsonResult.setData(null);
        }catch (Exception e){
            logger.error("login error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;
    }


    /**
     * 退出接口
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public JsonResult<Object>  logout(HttpSession httpSession){
        JsonResult<Object> jsonResult = null;
        try{
            httpSession.invalidate();
            jsonResult = new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG);
        }catch (Exception e){
            logger.error("login error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;
    }


    /**
     * 查询用户信息，
     * 以便在web端展示
     * @param httpSession
     * @return
     */
   @RequestMapping(value = "/queryUserInfo",method = RequestMethod.POST)
   public JsonResult<User>  queryUseInfoFromSession(HttpSession httpSession){

       JsonResult<User> jsonResult = null;
       try{
            User sessionUser = (User) httpSession.getAttribute(Constants.KEY_SESSION_USER);
            User acturalUser = new User();
            acturalUser.setNickName(sessionUser.getNickName());
            jsonResult = new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,acturalUser);
       }catch (Exception e){
           logger.error("login error",e);
           jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
       }
       return jsonResult;
   }


    /**
     * 分页查询用户列表
     * @param user
     * @return
     */
    @RequestMapping(value = "/queryUserList",method = RequestMethod.POST)
    public JsonResult<List<User>>  queryUserList(User user){

        JsonResult<List<User>> jsonResult = null;
        try{
            jsonResult = userService.queryUserByCondition(user);
        }catch (Exception e){
            logger.error("query user list error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;
    }



    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public JsonResult<Object>  addUser(User user){
        JsonResult<Object> jsonResult = null;
        try{
            jsonResult = userService.addUser(user);
        }catch (Exception e){
            logger.error("add user error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;
    }




    /**
     * 删除用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/removeUser",method = RequestMethod.POST)
    public JsonResult<Object>  removeUser(User user){
        JsonResult<Object> jsonResult = null;
        try{
            jsonResult = userService.removeUser(user);
        }catch (Exception e){
            logger.error("remove user error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;
    }


    /**
     * 修改密码
     * @param user
     * @return
     */
    @RequestMapping(value = "/modifyPwd",method = RequestMethod.POST)
    public JsonResult<Object>  modifyPwd(User user){
        JsonResult<Object> jsonResult = null;
        try{
            jsonResult = userService.modifyPwd(user);
        }catch (Exception e){
            logger.error("modify pwd error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;
    }

    /**
     * 给用户授权
     * @param userMenuJsonStr
     * @return
     */
    @RequestMapping(value = "/assignUserMenu",method = RequestMethod.POST)
    public JsonResult<Object>  assignUserMenu(String userMenuJsonStr,Integer userId){
        JsonResult<Object> jsonResult = null;
        try{
            jsonResult = userService.assignUserMenu(userMenuJsonStr,userId);
        }catch (Exception e){
            logger.error("assign user menu error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;
    }



    /**
     * 给用户授权
     * @param userPrivilegeJsonStr
     * @return
     */
    @RequestMapping(value = "/assignUserDataPrivilege",method = RequestMethod.POST)
    public JsonResult<Object>  assignUserDataPrivilege(String userPrivilegeJsonStr,Integer userId){
        JsonResult<Object> jsonResult = null;
        try{
            jsonResult = userService.assignUserDataPrivilege(userPrivilegeJsonStr,userId);
        }catch (Exception e){
            logger.error("assign user data privilege error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;
    }


}
