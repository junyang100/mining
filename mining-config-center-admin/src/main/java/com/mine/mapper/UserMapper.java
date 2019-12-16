package com.mine.mapper;


import com.mine.bean.User;

import java.util.List;

/**
 * 操作用户的Mapper
 */
public interface UserMapper {

    /**
     * 分页查询用户信息
     * @param user
     * @return
     */
    List<User> selectByCondition(User user);

    /**
     * 查询记录条数
     * @param user
     * @return
     */
    int selectCount(User user);


    /**
     * 插入用户信息
     * @param user
     * @return
     */
    int insert(User user);

    /**
     * 根据用户名查询 用户信息
     * @param username
     * @return
     */
    User selectByUsername(String username);

    /**
     * 根据字段是否为空更新记录信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);


    /**
     * 根据字段是否为空更新记录信息
     * @param record
     * @return
     */
    int deleteByPrimaryKey(User record);


}