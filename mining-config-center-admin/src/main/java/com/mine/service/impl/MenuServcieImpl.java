package com.mine.service.impl;

import com.mine.bean.JsonResult;
import com.mine.bean.Menu;
import com.mine.mapper.MenuMapper;
import com.mine.mapper.UserMenuMapper;
import com.mine.service.MenuServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MenuServcieImpl implements MenuServcie {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserMenuMapper userMenuMapper;

    @Override
    public List<Menu> queryUserMenus(int userId) {

        try{
            List<Menu> menuList = menuMapper.selectMenuByUserId(userId);
            return menuList;
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }


    @Override
    public List<Map<String,Object>> queryTreeMenu(Integer userId) {
        try{
            Set<Integer> hasMenuIdSet = new HashSet<>();
            List<Menu> hasMenuList = queryUserMenus(userId);
            for(Menu menu : hasMenuList){
                hasMenuIdSet.add(menu.getId());
                for(Menu subMenus : menu.getChildrenMenu()){
                    hasMenuIdSet.add(subMenus.getId());
                }
            }

            List<Map<String,Object>> dataMap = new ArrayList<>();
            List<Menu> menuList = menuMapper.selectTreeMenus();
            for(Menu menu : menuList){
                Map<String,Object> tempMap = new HashMap();
                List<Map<String,Object>> children = new ArrayList<>();
                tempMap.put("title",menu.getMenuDisplayname());
                tempMap.put("value",menu.getId());
                tempMap.put("data",children);
                if(hasMenuIdSet.contains(menu.getId())){
                    tempMap.put("checked",true);
                }
                dataMap.add(tempMap);
                Set<Menu> childrenMenuSet = menu.getChildrenMenu();
                for(Menu subMenu : childrenMenuSet){
                    tempMap = new HashMap<>(4);
                    tempMap.put("title",subMenu.getMenuDisplayname());
                    tempMap.put("value",subMenu.getId());
                    tempMap.put("data",Collections.EMPTY_LIST);
                    if(hasMenuIdSet.contains(subMenu.getId())){
                        tempMap.put("checked",true);
                    }
                    children.add(tempMap);
                }
            }
            return  dataMap;
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }


    @Override
    public JsonResult<List<Menu>> queryMenuList(Menu menu) {
        try{
            int totalCount = menuMapper.selectCountByCondition(menu);
            List<Menu> menuList  = Collections.EMPTY_LIST;
            if(totalCount > 0){
                menuList = menuMapper.selectMenuByCondition(menu);
            }
            return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,menuList,totalCount);
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }


    @Override
    public JsonResult<Object> addMenu(Menu menu) {
        try{
             int count = menuMapper.insert(menu);
             return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,null,count);
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }

    @Override
    public JsonResult<Object> modifyMenu(Menu menu) {
        try{
            Menu updatedMenu = new Menu();
            updatedMenu.setUpdateTime(new Date());
            updatedMenu.setId(menu.getId());
            updatedMenu.setMenuDisplayname(menu.getMenuDisplayname());
            updatedMenu.setMenuName(menu.getMenuName());
            updatedMenu.setMenuParent(menu.getMenuParent());
            updatedMenu.setMenuAction(menu.getMenuAction());
            updatedMenu.setMenuOrder(menu.getMenuOrder());
            int count = menuMapper.updateByPrimaryKeySelective(updatedMenu);
            return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,null,count);
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }

    @Transactional
    @Override
    public JsonResult<Object> removeMenu(Menu menu) {
        try{
            int count1 = userMenuMapper.deleteByMenuId(menu.getId());
            int count2 = menuMapper.deleteByPrimaryKey(menu.getId());
            return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,null,( count1 + count2 ));
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }

    @Override
    public JsonResult<Object> queryParentMenu() {
        try{
            List<Menu> menuList =  menuMapper.selectAllParentMenu();
            return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,menuList,menuList.size());
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }
}
