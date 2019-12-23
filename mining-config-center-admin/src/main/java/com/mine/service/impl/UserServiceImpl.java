package com.mine.service.impl;

import com.alibaba.fastjson.JSON;
import com.mine.bean.JsonResult;
import com.mine.bean.User;
import com.mine.bean.UserMenu;
import com.mine.bean.UserPrivillege;
import com.mine.mapper.UserMapper;
import com.mine.mapper.UserMenuMapper;
import com.mine.mapper.UserPrivillegeMapper;
import com.mine.service.UserService;
import com.mine.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMenuMapper userMenuMapper;

    @Autowired
    private UserPrivillegeMapper userPrivillegeMapper;

    @Override
    public JsonResult<User> login(User loginedUser) {

        try{
            User queryUserd = userMapper.selectByUsername(loginedUser.getUserName());
            if(queryUserd == null){
                return new JsonResult<>(JsonResult.USER_VALIDATE_ERROR,"用户名或者密码不正确");
            }
            String storedPwdsha1 = queryUserd.getPasswordSha1();
            /**
             * 这里使用两次sha1
             */
            if(StringUtils.equals(storedPwdsha1,DigestUtils.sha1Hex(loginedUser.getPasswordSha1()))){
                return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,queryUserd);
            }
           return new JsonResult<>(JsonResult.USER_VALIDATE_ERROR,"用户名或者密码不正确");
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }

     }


    @Override
    public JsonResult<List<User>> queryUserByCondition(User user) {
        try{
            int count = userMapper.selectCount(user);
            List<User> userList = Collections.EMPTY_LIST;
            if(count > 0){
                userList = userMapper.selectByCondition(user);
            }
            return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,userList,count);
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }


    @Override
    public JsonResult<Object> addUser(User addedUser) {
        try{
            String sha1PwdOne = addedUser.getPasswordSha1();
            String sha1PwdTwo = DigestUtils.sha1Hex(sha1PwdOne);
            addedUser.setPasswordSha1(sha1PwdTwo);
            int inserCount = userMapper.insert(addedUser);
            return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,null,inserCount);
        }catch (DuplicateKeyException e){
            return new JsonResult<>(JsonResult.BUSINESS_ERROR,"用户名已经存在");
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }

    @Transactional
    @Override
    public JsonResult<Object> removeUser(User removedUser) {
        try{
            int removedCount = userMapper.deleteByPrimaryKey(removedUser);
            int removehasMenuCount = userMenuMapper.deleteByUserId(removedUser.getId());
            int removeHasPrivilegeCount = userPrivillegeMapper.deleteByUserId(removedUser.getId());
            return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,null,(removedCount + removehasMenuCount +removeHasPrivilegeCount ));
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }


    @Override
    public JsonResult<Object> modifyPwd(User updatedUser) {
        try{
            User sessionUser = WebUtils.getUserFromSession();
            String inputOldPwdSha1 = updatedUser.getPasswordSha1();
            if(!StringUtils.equals(sessionUser.getPasswordSha1(),DigestUtils.sha1Hex(inputOldPwdSha1))){
                return new JsonResult<>(JsonResult.BUSINESS_ERROR,"密码不正确!");
            }
            User paramUser = new User();
            paramUser.setId(sessionUser.getId());
            paramUser.setPasswordSha1(DigestUtils.sha1Hex(updatedUser.getPasswordsha1New()));
            int updateCount = userMapper.updateByPrimaryKeySelective(paramUser);
            return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,null,updateCount);
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }


    @Transactional
    @Override
    public JsonResult<Object> assignUserMenu(String userMenuJsonStr,Integer userId) {
        try{
            if(userMenuJsonStr.equals("All")){
                userMenuMapper.deleteByUserId(userId);
                return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG);
            }
            userMenuMapper.deleteByUserId(userId);
            List<UserMenu> assignMenuList = JSON.parseArray(userMenuJsonStr,UserMenu.class);
            for(UserMenu userMenu : assignMenuList){
                userMenu.setUserId(userId);
            }
            userMenuMapper.batchInsert(assignMenuList);
            return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG);
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }




    @Transactional
    @Override
    public JsonResult<Object> assignUserDataPrivilege(String userDataPrivilegeJsonStr, Integer userId) {
        try{
            if(userDataPrivilegeJsonStr.equals("All")){
                userPrivillegeMapper.deleteByUserId(userId);
                return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG);
            }
            userPrivillegeMapper.deleteByUserId(userId);
            List<UserPrivillege> assignPrivelegeList = JSON.parseArray(userDataPrivilegeJsonStr,UserPrivillege.class);
            for(UserPrivillege userPrivillege : assignPrivelegeList){
                userPrivillege.setUserId(userId);
            }
            userPrivillegeMapper.batchInsert(assignPrivelegeList);
            return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG);
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }
}
