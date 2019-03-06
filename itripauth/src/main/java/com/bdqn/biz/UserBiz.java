package com.bdqn.biz;

import cn.itrip.dao.itripUser.ItripUserMapper;
import cn.itrip.pojo.ItripUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserBiz {

    @Resource
   ItripUserMapper mapper;
    public ItripUser getUserByPhone(@Param(value = "code") String phone, @Param(value = "ps")String ps)
    {
        return mapper.getUserByPhone(phone,ps);
    }

}
