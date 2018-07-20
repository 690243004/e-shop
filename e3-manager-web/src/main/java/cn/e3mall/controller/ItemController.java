package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.EasyUITreeNode;
import cn.e3mall.pojo.Page;
import cn.e3mall.pojo.QueryVo;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemParam;
import cn.e3mall.service.ItemParamService;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ItemParamService itemParamService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemByid(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public Page<TbItem> queryTbitemList(QueryVo queryVo) {
		//获取参数 自动获取 判断参数是否为空
		if(queryVo.getPage()!=null&&!queryVo.getPage().equals("")
		   &&queryVo.getRows()!=null&&!queryVo.getRows().equals("")) {
			//不为空 service调用query方法
			Page<TbItem> page = itemService.queryTbitemList(queryVo);
			//返回page对象
			//添加注解 返回json
			return page;
		}else {
			return null;
		}
	
	}
	
	@RequestMapping(value = "/item/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem tbItem,String desc) {
		E3Result e3Result = itemService.addItem(tbItem, desc);
		return e3Result;
	}
	
	
	@RequestMapping(value="/rest/page/item-edit")
	public String pageItem_edit() {
		return "item-edit";
	}
	
	@RequestMapping(value="/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public TbItemDesc queryItemDescByItemId(@PathVariable Long itemId) {
		TbItemDesc desc = itemService.getTbItemDescByItemID(itemId);
		return desc;
	}
	
	@RequestMapping(value="/rest/item/param/item/query/{itemId}")
	@ResponseBody
	public TbItemParam queryTbItemParamByItemId(@PathVariable Long itemId) {
		TbItem item = itemService.getItemById(itemId);
		Long cid = item.getCid();
		TbItemParam itemParam = itemParamService.queryTbItemParamByItemId(cid);
		return itemParam;
	}
	
	@RequestMapping(value="/rest/item/update")
	@ResponseBody
	public E3Result updateItem(TbItem item,TbItemDesc desc) {
		E3Result e3Result = itemService.upateItem(item, desc);
		return e3Result;
	}
}
