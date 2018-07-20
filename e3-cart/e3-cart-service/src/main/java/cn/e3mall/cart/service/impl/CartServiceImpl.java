package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
@Service
public class CartServiceImpl implements CartService{
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbItemMapper tbItemMapper;
	@Override
	public E3Result addCart(long userId, long itemId,int num) {
		// 向redis中添加购物车
		//数据类型是hash key :用户id field:商品id value:商品信息
		Boolean hexists = jedisClient.hexists("CART"+":"+userId, itemId+"");
		//判断商品是否存在
		//如果存在 数量相加
		if(hexists) {
			String json = jedisClient.hget("CART"+":"+userId, itemId+"");
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(tbItem.getNum()+num);
			jedisClient.hset("CART"+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
			return E3Result.ok();
		}
		//如果不存在 根据商品id
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
		item.setNum(num);
		String image = item.getImage();
		if(image!=null&&!"".equals(image)) {
			item.setImage(image.split(",")[0]);
		}
		jedisClient.hset("CART"+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}
	@Override
	public List<TbItem> getCartList(long userId) {
		// 根据用户id查询购物车列表
		List<String> jsonList = jedisClient.hvals("CART"+":"+userId);
		List<TbItem> itemList = new ArrayList<>();
		for(String string:jsonList) {
			//创建一个 TbItem对象
			TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
			//添加到列表
			itemList.add(item);
		}
		return itemList;
	}
	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		// 遍历商品
		for(TbItem tbItem:itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		// 把列表添加到购物车
		// 判断购物车中是否有此商品
		// 如果有 数量相加
		
		return E3Result.ok();
	}
	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {
		String json = jedisClient.hget("CART"+":"+userId, itemId+"");
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		jedisClient.hset("CART"+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}
	@Override
	public E3Result deleteCartNum(long userId, long itemId) {
		jedisClient.hdel("CART"+":"+userId, itemId+"");
		return E3Result.ok();
	}
	@Override
	public E3Result clearCarItem(long userId) {
		//删除购物车信息
		jedisClient.del("CART"+":"+userId);
		return E3Result.ok();
	}

}
