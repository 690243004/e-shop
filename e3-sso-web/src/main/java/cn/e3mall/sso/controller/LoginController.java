package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.LoginService;

@Controller
public class LoginController {
	@Value("{TOKEN_KEY}")
	private String TOKEN_KEY;
	@Autowired
	private LoginService loginService;
	@RequestMapping("/page/login")
	public String showLogin(String redirect,Model model) {
		model.addAttribute("redirect",redirect);
		return "login";
	}
	
	@RequestMapping("/")
	public String showIndex() {
		return "login";
	}
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3Result login(String username,String password,HttpServletRequest request
			,HttpServletResponse response) {
		E3Result e3Result =null;
		e3Result = loginService.userLogin(username, password);
		/*if(!StringUtils.isEmpty(username)&&!StringUtils.isEmpty(password)) {
			e3Result = loginService.userLogin(username, password);
		}else {
			return E3Result.build(400, "用户名与密码不能为空");
		}*/
		
		if(e3Result.getStatus()==200) {
			String token = e3Result.getData().toString();
			//如果登录成功就把token写入cookie
			CookieUtils.setCookie(request, response,"token", token);
		}
		return e3Result;
	}
}
