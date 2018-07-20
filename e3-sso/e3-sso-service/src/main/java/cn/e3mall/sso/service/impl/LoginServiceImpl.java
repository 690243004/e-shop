package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;
@Service
public class LoginServiceImpl implements LoginService{
	@Autowired
	private TbUserMapper mapper;
	@Autowired
	private JedisClient JedisClient;
	/*
	 * 1.判断用户名密码是否正确
	 * 2.如果不正确 返回登录失败
	 * 3.如果正确 先生成token
	 * 4.把用户信息写入redis key:token value:用户信息
	 * 5.设置session的过期时间
	 * 6.把token返回
	 */
	@Override
	public E3Result userLogin(String username, String password) {
		//根据用户名查询用户信息
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = mapper.selectByExample(example);
		if(list.size()==0||list==null) {
			return E3Result.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		//读用户信息 判断用户密码是否一致
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return E3Result.build(400, "用户名或密码错误");
		}
		// * 3.如果正确 先生成token
		String token = UUID.randomUUID().toString();
		//4.把用户信息写入redis key:token value:用户信息
		user.setPassword(null);
		JedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		//5.设置session的过期时间
		JedisClient.expire("SESSION"+token, 3600);
		return E3Result.ok(token);
	}

}
