package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * 用户登录方法拦截器
 * 
 * @author MyPC
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	@Autowired
	private TokenService tokenService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 前处理 在执行handler之前执行该方法 返回true 放行
		// 1.从cookies中取出token
		String token = CookieUtils.getCookieValue(request, "token");
		// 2.如果没有token 未登录状态 直接放行
		if (token == null || token.equals("")) {
			return true;
		}
		// 3.取到token 需要调用sso系统的服务 根据token取用户信息
		E3Result result = tokenService.getUserByToken(token);
		// 4.如果没有取到用户信息 登录过期 直接放行
		if(result.getStatus()!=200) {
			return true;
		}
		// 5.取到用户信息 登录状态
		TbUser user = (TbUser) result.getData();
		// 6.把用户信息放入request中 只需要在controller中 判断request是否包含user信息
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// handler执行后 返回modelview之前

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 完成处理后 返回modelview之后
		// 可以在此处处理异常

	}

}
