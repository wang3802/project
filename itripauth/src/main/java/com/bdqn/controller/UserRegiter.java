package com.bdqn.controller;

import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import cn.itrip.pojo.Dto;
import cn.itrip.pojo.DtoUtil;
import cn.itrip.pojo.ItripUser;
import cn.itrip.pojo.userinfo.ItripUserVO;
import com.bdqn.biz.UserRegiterBiz;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
@Controller
@RequestMapping("/api")
public class UserRegiter {

    @Resource
    UserRegiterBiz biz;

    @Resource
    RedisAPI redisAPI;


    @RequestMapping(value="/registerbyphone",method=RequestMethod.POST,produces = "application/json")

    public @ResponseBody  Dto registerByPhone(
            @RequestBody ItripUserVO userVO) throws Exception {
         //根据手机号去发送验证码
        Long code=(long)Math.round(4);
        biz.setPhone(userVO.getUserCode(),""+code);
        //把验证码放入redis中
        redisAPI.set("code:"+userVO.getUserCode(),50,""+code);
        //插入到用户表中数据
        ItripUser user=new ItripUser();
        user.setUserName(userVO.getUserName());
        user.setUserCode(userVO.getUserCode());
        user.setUserPassword(MD5.getMd5( userVO.getUserPassword(),32));
        //系统给用户发送验证码，并向数据库插入数据，还未验证，所以是未激活状态
        user.setActivated(0);

        biz.InsertUser(user);
        return DtoUtil.returnSuccess();
    }

    //根据手机号，验证码验证
    @RequestMapping("/validatephone")
    @ResponseBody
    public Dto getlist(String user,String  code)
    {
        //判断redis中是否有
        //如果存在更改数据库状态，激活成功
         if(biz.IfExit(user,code))
         {
              //先取到user的数据
             //然后修改状态
             biz.UpdateStatus(user);
             return  DtoUtil.returnSuccess("激活成功");
         }
        return  DtoUtil.returnSuccess("激活失败");
    }
}
