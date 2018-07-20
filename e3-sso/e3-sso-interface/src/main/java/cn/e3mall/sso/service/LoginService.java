package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

public interface LoginService {
	//参数 ：用户名与密码
	//业务逻辑
	/*
	 * 1.判断用户名密码是否正确
	 * 2.如果不正确 返回登录失败
	 * 3.如果正确 先生成token
	 * 4.把用户信息写入redis key:token value:用户信息
	 * 5.设置session的过期时间
	 * 6.把token返回
	 */
	//返回e3result 其中包含token信息
	E3Result userLogin(String username,String password);
}
