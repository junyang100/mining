package com.mine.service.impl;

import com.mine.bean.UserPrivillege;
import com.mine.mapper.UserPrivillegeMapper;
import com.mine.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
   private UserPrivillegeMapper userPrivillegeMapper;

    @Override
    public Set<String> queryUserPrivilege(int userId) {
         Set<String> privilegeSet = new HashSet<>();
         List<UserPrivillege> privilegeList =  userPrivillegeMapper.selectPrivilegeByUserId(userId);
         if(privilegeList.size() > 0){
             for(UserPrivillege privilege : privilegeList){
                 privilegeSet.add(privilege.getPrivilegeId());
             }
         }
         return privilegeSet;
    }






}
