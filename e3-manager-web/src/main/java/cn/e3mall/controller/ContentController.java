package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.Page;
import cn.e3mall.pojo.QueryVo;
import cn.e3mall.pojo.TbContent;

@Controller
public class ContentController {
	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value="/content/query/list")
	@ResponseBody
	public Page<TbContent> queryTbContentList(QueryVo queryVo) {
		//获取参数
		if(queryVo.getPage()!=null&&!queryVo.getPage().equals("")
				   &&queryVo.getRows()!=null&&!queryVo.getRows().equals("")	  
			) 
		{
			Page<TbContent> page = contentService.queryTbContentList(queryVo);
			return page;
		}else {
			return null;
		}
		
	}
}
