package cn.e3mall.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.Page;
import cn.e3mall.pojo.QueryVo;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService { 
	public TbItem getItemById(long itemId);
	public Page<TbItem> queryTbitemList(QueryVo queryVo);
	
	E3Result addItem(TbItem item,String desc);
	TbItemDesc getTbItemDescByItemID(Long itemId);
	E3Result upateItem(TbItem item,TbItemDesc desc);
}
