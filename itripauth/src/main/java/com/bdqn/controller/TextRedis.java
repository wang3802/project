package com.bdqn.controller;

import cn.itrip.common.RedisAPI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class TextRedis {

    @Resource
    RedisAPI redisAPI;

    @RequestMapping("/textRedis")
    @ResponseBody
    public Object GetRedis()
    {
        try {
            redisAPI.set("k1",40,"测试的值");
            return "redis插入成功";
        }
        catch (Exception ex)
        {
            return "redis插入失败";
        }

    }


}
