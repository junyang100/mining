package com.mine.autoconfigure;


import com.mine.share.PushRefreshResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/refresh")
public class RefershController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private PushRefreshResultService pushRefreshResultService;

    @Autowired
    private ContextRefresher contextRefresher;

    public RefershController(){
        logger.info("http refresher init 0k..........");
    }

    @RequestMapping(value="/refreshConfig",method = RequestMethod.POST)
   public Map<String,Object> refreshContext(@RequestParam Map<String,String> params){
        Map<String,Object> map = new HashMap<>(4);
        String msg = "0k";
        int status = 1;
        Object id = null;
        try{
                id = params.get("id");
                long beginTime = System.currentTimeMillis();
                logger.info("recieve http notice,begin to refresh application [{}]",applicationName);
                contextRefresher.refresh();
                pushRefreshResultService.pushRefreshResult("2",id.toString());
                logger.info("refresh [{}] finished,cost time [{}] mills",applicationName,(System.currentTimeMillis() - beginTime));

        }catch (Exception e){
            status = 2;
            msg = "error:" + e.getMessage();
            pushRefreshResultService.pushRefreshResult("3",id.toString());
            logger.error("refersh config error",e);
        }
        map.put("status",status);
        map.put("msg",msg);
        return map;
   }

}
