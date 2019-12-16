package com.mine.mapper;


import com.mine.bean.UserPrivillege;

import java.util.List;

public interface UserPrivillegeMapper {

    /**
     * 根据用户id删除
     * @param userId
     * @return
     */
    int deleteByUserId(Integer userId);

    /**
     * 根据数据权限删除 权限
     * @param privilegeId
     * @return
     */
    int deleteByPrivilegeId(String privilegeId);

    /**
     * 插入
     * @param record
     * @return
     */
    int insert(UserPrivillege record);

    /**
     * 批量插入
     * @param records
     * @return
     */
    int batchInsert(List<UserPrivillege> records);

    /**
     * 根据用户id 查询用户的权
     * @param userId
     * @return
     */
    List<UserPrivillege> selectPrivilegeByUserId(int userId);
}