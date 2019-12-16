package com.mine.service;


import com.mine.bean.JsonResult;
import com.mine.bean.User;

import java.util.List;

/**
 * 用户service 接口
 */
public interface UserService {

    /**
    * 用户登陆
    * @param loginedUser
    * @return
    */
    JsonResult<User> login(User loginedUser);

    /**
     * 查询用户列表
     * @param user
     * @return
     */
    JsonResult<List<User>> queryUserByCondition(User user);


    /**
     * 添加用户
     * @param addedUser
     * @return
     */
    JsonResult<Object> addUser(User addedUser);


    /**
     * 删除用户
     * @param removedUser
     * @return
     */
    JsonResult<Object> removeUser(User removedUser);


    /**
     * 修改密码
     * @param updatedUser
     * @return
     */
    JsonResult<Object> modifyPwd(User updatedUser);


    /**
     * 给用户分配菜单
     * @param userMenuJsonStr
     * @return
     */
    JsonResult<Object> assignUserMenu(String userMenuJsonStr, Integer userId);


    /**
     * 给用户分配应用权限
     * @param userDataPrivilegeJsonStr
     * @return
     */
    JsonResult<Object> assignUserDataPrivilege(String userDataPrivilegeJsonStr, Integer userId);



}
