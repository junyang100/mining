package com.mine.service;


import com.mine.bean.JsonResult;
import com.mine.bean.Menu;

import java.util.List;
import java.util.Map;

public interface MenuServcie {

    /**
     * 查询用户的菜单
     * @param userId
     * @return
     */
    List<Menu> queryUserMenus(int userId);


    /**
     * 查询树形菜单
     * @return
     * @param userId
     */
   List<Map<String,Object>> queryTreeMenu(Integer userId);

    /**
     * 分页查询 菜单
     * @param menu
     * @return
     */
   JsonResult<List<Menu>>  queryMenuList(Menu menu);


    /**
     * 添加菜单
     * @param menu
     * @return
     */
    JsonResult<Object> addMenu(Menu menu);


    /**
     * 修改菜单
     * @param menu
     * @return
     */
    JsonResult<Object> modifyMenu(Menu menu);


    /**
     * 删除菜单
     * @param menu
     * @return
     */
    JsonResult<Object> removeMenu(Menu menu);



    /**
     * 查询所有父菜单
     * @return
     */
    JsonResult<Object> queryParentMenu();





}
