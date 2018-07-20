package cn.e3mall.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * @author MyPC
 *根据token取用户信息
 */
@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private JedisClient jedisClient;
	@Override
	public E3Result getUserByToken(String token) {
		// 根据token到redis取用户信息
		String json = jedisClient.get("SESSION:"+token);
		if(json==null||json.equals("")) {
			return E3Result.build(201, "用户登录已过期");
		}
		TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
		// 取不到用户信息 登录已经过期 返回 登录过期
		jedisClient.expire("SESSION:"+token, 3600);
		// 取到用户信息
		// 更新token过期时间
		return E3Result.ok(tbUser);
	}

}
