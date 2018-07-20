package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

public interface CartService {
	E3Result addCart(long userId,long itemId,int num);
	E3Result mergeCart(long userId,List<TbItem> itemList);
	E3Result updateCartNum(long userId,long itemId,int num);
	E3Result deleteCartNum(long userId,long itemId);
	E3Result clearCarItem(long userId);
	/**
	 * 根据用户id查询购物车列表
	 * @param userId
	 * @return
	 */
	List<TbItem> getCartList(long userId);
}
