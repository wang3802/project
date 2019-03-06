package com.bdqn.biz;

import cn.itrip.common.RedisAPI;
import cn.itrip.dao.itripUser.ItripUserMapper;
import cn.itrip.pojo.ItripUser;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.DeferredResultMethodReturnValueHandler;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

@Service
public class UserRegiterBiz {


    @Resource
    RedisAPI redisAPI;

    @Resource
    ItripUserMapper dao;


    //验证手机号注册码是否存在
    public boolean IfExit(String phone,String code)
    {
         String Cvalue=redisAPI.get("code:"+phone);
         if(Cvalue!=""&&Cvalue!=null&&Cvalue.equals(code))
         {
             return true;
         }
         return false;
    }

    //根据手机号去修改数据库状态
    public int UpdateStatus(String phone)
    {
        return  dao.updateItripUserByphone(phone);
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    //插入用户
    public int InsertUser(ItripUser user) throws Exception {
        return dao.insertItripUser(user);
    }

    /***
     * 根据手机号，验证码去发送短信
     * @param phone
     * @param code
     * @return
     */
    public boolean setPhone(String phone,String code)
    {
        HashMap<String, Object> result = null;

        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();

        restAPI.init("app.cloopen.com", "8883");

        restAPI.setAccount("8a216da8685986c2016863c56831044b", "ea14047bbf2c446486d6d6c831aba8dd");

        restAPI.setAppId("8a216da8685986c2016863c568890451");

        result = restAPI.sendTemplateSMS(phone,"1" ,new String[]{code,"1"});

        System.out.println("SDKTestGetSubAccounts result=" + result);
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
         /*   HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }*/
         return  true;
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
        return  false;

    }

}
