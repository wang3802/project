package com.bdqn.controller;

import cn.itrip.common.MD5;
import cn.itrip.pojo.Dto;
import cn.itrip.pojo.DtoUtil;
import cn.itrip.pojo.ItripTokenVO;
import cn.itrip.pojo.ItripUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bdqn.biz.TokenService;
import com.bdqn.biz.UserBiz;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Controller
@RequestMapping("/api")
@Api(value = "login",description = "用户模块接口")
public class UserController {
    @Resource
    UserBiz biz;
    @Resource
    TokenService tokenService;


    @ApiOperation(value = "根据手机号和密码",notes = "标题")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="form",required=true,value="用户名",name="name",defaultValue="itrip@163.com"),
            @ApiImplicitParam(paramType="form",required=true,value="密码",name="password",defaultValue="111111"),
    })

    @RequestMapping(value = "/dologin",method = RequestMethod.POST)
    @ResponseBody
    public  Object login(HttpServletRequest request,@RequestParam("name")String dd, String password)
    {
        System.out.println(dd);
        System.out.println(password);
        System.out.println(password);
         //验证手机号及密码是否正确
        ItripUser user=biz.getUserByPhone(dd,MD5.getMd5(password,32));
        if(user!=null) {
            //根据用户的请求信息及当前用户生成一个key:token
           String token=tokenService.generateToken(request.getHeader("User-Agent"),user);
           //把这个token 存入到redis 中。
            tokenService.save(token,user);
            //生成tokenvo实体，方便前台传递
            ItripTokenVO tokenVO=new ItripTokenVO(token,60*60*2,Calendar.getInstance().getTimeInMillis());
            //返回前台数据
            return DtoUtil.returnDataSuccess(tokenVO);
        }
        return DtoUtil.returnFail("用户名密码错误","1000");
    }


    @ApiOperation(value = "测试",notes = "标题")
    @ApiImplicitParam(paramType="path",required=true,value="用户名",name="id",defaultValue="1")

    @RequestMapping(value = "/list_{id}",method =RequestMethod.GET )
    @ResponseBody
    public Object getid(@PathVariable("id")String id)
    {
        System.out.println(id);
        return JSONArray.toJSONString("字符串");
    }

}
