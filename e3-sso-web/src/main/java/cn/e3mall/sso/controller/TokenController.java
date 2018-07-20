package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.sso.service.TokenService;

@Controller
public class TokenController {
	@Autowired
	private TokenService tokenService;
	
/*	@RequestMapping(value="/user/token/{token}",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback) {
		E3Result result = tokenService.getUserByToken(token);
		if(callback!=null&&!callback.equals("")) {
			return callback+"("+JsonUtils.objectToJson(result)+");";
		}
		return JsonUtils.objectToJson(result);
	}*/
	
	@RequestMapping(value="/user/token/{token}",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback) {
		E3Result result = tokenService.getUserByToken(token);
		if(callback!=null&&!callback.equals("")) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
}
