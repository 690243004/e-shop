package cn.e3mall.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;

@Controller
public class PictureController {
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		try {
			//把图片上传到服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:client.conf");
			//得到图片地址和文件名
			//取文件扩展名
			String orignalFilename = uploadFile.getOriginalFilename();
			String exname = orignalFilename.substring(orignalFilename.lastIndexOf(".")+1);
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(),exname);
			//补充为完整的url
			url = IMAGE_SERVER_URL+url;
			//封装到Map中返回
			Map result = new HashMap<>();
			result.put("error",0);
			result.put("url", url);
			System.out.println(url);
			return JsonUtils.objectToJson(result);
		}  catch (Exception e) {
			e.printStackTrace();
			Map result = new HashMap<>();
			result.put("error",1);
			result.put("message", "上传失败");
			return JsonUtils.objectToJson(result);
		}
		
	}
}
