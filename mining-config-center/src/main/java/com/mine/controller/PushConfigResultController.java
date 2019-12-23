package com.mine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/pushConfigResult")
public class PushConfigResultController {

     @Autowired
     private JdbcTemplate jdbcTemplate;

     private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 更新应用配置变更推送状态
     * @return
     */
    @RequestMapping(value = "/modifyConfigPushResult",method = RequestMethod.POST)
    public  Object modifyConfigPushInfo(@RequestParam Map<String,String> paramMap){
        int status = 1;
        String msg = "0k";
        try{
            String sql = "update config_change_push set push_status = ?,push_finish_time= ? where id = ?";
            byte pushStatus = Byte.valueOf(paramMap.get("status"));
            int id = Integer.parseInt(paramMap.get("id"));
            Date finishTime = new Date();
            jdbcTemplate.update(sql,new Object[]{pushStatus,finishTime,id});
            logger.info("modify config push result status {},id {}",status,id);
        }catch (Exception e){
             status = 0;
             msg = "更新失败";
            logger.error("更新推送结果失败",e);
        }

        Map<String,Object> resutMap = new HashMap<>(4);
        resutMap.put("status",status);
        resutMap.put("msg",msg);
        return resutMap;

    }


}
