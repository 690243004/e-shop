package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	@RequestMapping(value="/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId,Model model) {
		//调用服务取商品信息
		TbItem tbItem = itemService.getItemById(itemId);
		//取商品描述信息
		TbItemDesc itemDesc = itemService.getTbItemDescByItemID(itemId);
		//把信息传递给页面
		model.addAttribute("item", tbItem);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
