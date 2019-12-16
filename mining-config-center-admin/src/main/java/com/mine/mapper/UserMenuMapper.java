package com.mine.mapper;


import com.mine.bean.UserMenu;

import java.util.List;

/**
 * 用户菜单关联表映射器
 */
public interface UserMenuMapper {

    int deleteByUserId(Integer userId);

    int deleteByMenuId(Integer menuId);

    int insert(UserMenu record);

    int batchInsert(List<UserMenu> userMenuList);


}