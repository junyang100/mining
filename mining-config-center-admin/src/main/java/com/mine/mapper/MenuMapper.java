package com.mine.mapper;

import com.mine.bean.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作菜单的url
 */
public interface MenuMapper {


    /**
     * 查询用户的菜单
     * @param userId
     * @return
     */
    List<Menu> selectMenuByUserId(@Param("userId") Integer userId);


    /**
     * 查询所有树形菜单
     * @return
     */
    List<Menu> selectTreeMenus();


    /**
     * 根据条件分页查询菜单
     * @param menu
     * @return
     */
    List<Menu> selectMenuByCondition(Menu menu);


    /**
     * 查询符合条件的记录数
     * @param menu
     * @return
     */
    int selectCountByCondition(Menu menu);

    /**
     * 根据主键删除菜单
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @param record
     * @return
     */
    int insert(Menu record);

    /**
     * 更新菜单
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Menu record);


    /**
     * 查询所有的父菜单
     * @return
     */
    List<Menu> selectAllParentMenu();


    

}