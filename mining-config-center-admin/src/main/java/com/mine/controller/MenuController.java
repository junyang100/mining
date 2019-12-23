package com.mine.controller;

import com.mine.bean.JsonResult;
import com.mine.bean.Menu;
import com.mine.bean.User;
import com.mine.constants.Constants;
import com.mine.service.MenuServcie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

/**
 * 菜单控制器
 */
@RequestMapping("/menu")
@RestController
public class MenuController {


  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private MenuServcie menuServcie;

  @RequestMapping(value = "/queryUserMenu",method = RequestMethod.POST)
  public JsonResult<List<Menu>> queryUserMenus(HttpSession httpSession){

      JsonResult<List<Menu>> jsonResult = null;
      try{
          User u = (User)httpSession.getAttribute(Constants.KEY_SESSION_USER);
          if(u == null){
              jsonResult = new JsonResult<>(JsonResult.USER_VALIDATE_ERROR,JsonResult.OK_MSG);
          }else{
              List<Menu> menuList = (List<Menu>) httpSession.getAttribute(Constants.KEY_MENU_USER_PRE  + String.valueOf(u.getId()));
              jsonResult = new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,menuList);
          }
      }catch (Exception e){
          logger.error("queryUserMenus error",e);
          jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
      }
      return jsonResult;

  }


    @RequestMapping(value = "/queryMenuTree",method = {RequestMethod.POST,RequestMethod.GET})
    public Object  queryMenuTree(Integer userId){

        Object result = null;
        try{
            result = menuServcie.queryTreeMenu(userId);
        }catch (Exception e){
            logger.error("queryMenuTree error",e);
            result = Collections.EMPTY_LIST;
        }
        return result;

    }


    @RequestMapping(value = "/queryMenuList",method = {RequestMethod.POST,RequestMethod.GET})
    public JsonResult<List<Menu>>  queryMenuList(Menu menu){

        JsonResult<List<Menu>> jsonResult = null;
        try{
            jsonResult = menuServcie.queryMenuList(menu);
        }catch (Exception e){
            logger.error("query menu error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;

    }


    @RequestMapping(value = "/addMenu",method = {RequestMethod.POST,RequestMethod.GET})
    public JsonResult<Object>  addMenu(Menu menu){

        JsonResult<Object> jsonResult = null;
        try{
            jsonResult = menuServcie.addMenu(menu);
        }catch (Exception e){
            logger.error("addMenu error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;

    }


    @RequestMapping(value = "/modifyMenu",method = {RequestMethod.POST,RequestMethod.GET})
    public JsonResult<Object>  modifyMenu(Menu menu){

        JsonResult<Object> jsonResult = null;
        try{
            jsonResult = menuServcie.modifyMenu(menu);
        }catch (Exception e){
            logger.error("modifyMenu error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;

    }


    @RequestMapping(value = "/removeMenu",method = {RequestMethod.POST,RequestMethod.GET})
    public JsonResult<Object>  removeMenu(Menu menu){

        JsonResult<Object> jsonResult = null;
        try{
            jsonResult = menuServcie.removeMenu(menu);
        }catch (Exception e){
            logger.error("removeMenu error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;

    }


    @RequestMapping(value = "/queryParentMenu",method = {RequestMethod.POST,RequestMethod.GET})
    public JsonResult<Object>  queryParentMenu(){

        JsonResult<Object> jsonResult = null;
        try{
            jsonResult = menuServcie.queryParentMenu();
        }catch (Exception e){
            logger.error("removeMenu error",e);
            jsonResult = new JsonResult<>(JsonResult.FAIL_STATUS,JsonResult.FAIL_MSG);
        }
        return jsonResult;

    }


}
