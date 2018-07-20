package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor{
	@Autowired
	private TokenService tokenService;
	@Value("${SSO_URL}")
	private String SSO_URL;
	@Autowired
	private CartService cartService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//从cookies中取token 
		String token = CookieUtils.getCookieValue(request, "token");
		//判断token是否存在
		if(token==null||token.equals("")) {
			//如果token不存在 未登录状态 跳转到sso系统的登录页面 用户登录成功后 跳转到当前请求url
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			return false;
		}
		//如果token存在 调用sso用户信息 取不到 用户登录过期  需要用户登录
		E3Result e3Result = tokenService.getUserByToken(token);
		if(e3Result.getStatus()!=200) {
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			return false;
		}
		//取到用户信息 说明用户登录状态 将用户信息写入request
		TbUser user = (TbUser) e3Result.getData();
		request.setAttribute("user", user);
		//判断cookies是否有购物车数据 如果有合并到服务端 放行
		String jsonCartList = CookieUtils.getCookieValue(request, "cart", true);
		if(jsonCartList!=null&&!jsonCartList.equals("")) {
			cartService.mergeCart(user.getId(),JsonUtils.jsonToList(jsonCartList, TbItem.class));
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
