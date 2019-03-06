package com.bdqn.biz;


import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import cn.itrip.common.UserAgentUtil;
import cn.itrip.pojo.ItripUser;
import com.alibaba.fastjson.JSON;
import cz.mallat.uasparser.UserAgentInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Token管理接口相关业务服务实现
 * 
 * @author hduser
 *
 */
@Service("tokenService")
public class TokenServiceImpl implements TokenService {
	private Logger logger = Logger.getLogger(TokenServiceImpl.class);
	/**
	 * 调用RedisAPI
	 */
	@Resource
	private RedisAPI redisAPI;
	private String tokenPrefix = "token:";//统一加入 token前缀标识

	/***
	 * @param agent Http头中的user-agent信息
	 * @param user 用户信息
	 * @return Token格式<br/>
	 * 	PC：“前缀PC-USERCODE-USERID-CREATIONDATE-RONDEM[6位]” 
	 *  <br/>
	 *  Android：“前缀ANDROID-USERCODE-USERID-CREATIONDATE-RONDEM[6位]”
	 */
	public String generateToken(String agent, ItripUser user) {
		// TODO Auto-generated method stub
		try {
			UserAgentInfo userAgentInfo = UserAgentUtil.getUasParser().parse(
					agent);
			StringBuilder sb = new StringBuilder();
			sb.append(tokenPrefix);//统一前缀
			if (userAgentInfo.getDeviceType().equals(UserAgentInfo.UNKNOWN)) {
				if (UserAgentUtil.CheckAgent(agent)) {
					sb.append("MOBILE-");
				} else {
					sb.append("PC-");
				}
			} else if (userAgentInfo.getDeviceType()
					.equals("Personal computer")) {
				sb.append("PC-");
			} else
				sb.append("MOBILE-");
//			sb.append(user.getUserCode() + "-");
			sb.append(MD5.getMd5(user.getUserCode(),32) + "-");//加密用户名称
			sb.append(user.getId() + "-");
			sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
					+ "-");
			sb.append(MD5.getMd5(agent, 6));// 识别客户端的简化实现——6位MD5码
			
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void save(String token, ItripUser user) {
		if (token.startsWith(tokenPrefix+"PC-"))
			redisAPI.set(token,60*60*2, JSON.toJSONString(user));
		else
			redisAPI.set(token, JSON.toJSONString(user));// 手机认证信息永不失效
	}


}
