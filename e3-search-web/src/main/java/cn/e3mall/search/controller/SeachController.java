package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

/**
 * @author MyPC
 *商品搜索
 */
@Controller
public class SeachController {
	
	@Value("${SERACH_ROWS}")
	private Integer SERACH_ROWS;
	@Autowired
	private SearchService service;
	@RequestMapping(value="/search.html")
	public String seacherItemList(String keyword,@RequestParam(defaultValue="1")Integer page,Model model) throws Exception {
		keyword = new String(keyword.getBytes("iso-8859-1"),"UTF-8");
		SearchResult searchResult = service.search(keyword, page, SERACH_ROWS);
		model.addAttribute("query",keyword);
		model.addAttribute("totalPages",searchResult.getTotalPage());
		model.addAttribute("page",page);
		model.addAttribute("recourdCount",searchResult.getRecordCount());
		model.addAttribute("itemList",searchResult.getItemList());
		//返回逻辑视图
		return "search";
	}
}
