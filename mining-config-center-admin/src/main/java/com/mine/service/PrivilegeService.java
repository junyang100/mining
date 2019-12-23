package com.mine.service;

import java.util.Set;

/**
 * 权限服务接口
 */
public interface PrivilegeService {


    /**
     * 查询用户权限
     * @param userId
     * @return
     */
    Set<String> queryUserPrivilege(int userId);


}
