package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

/**
 * @author MyPC
 *根据token查询用户信息
 */
public interface TokenService {
	E3Result getUserByToken(String token);
}
