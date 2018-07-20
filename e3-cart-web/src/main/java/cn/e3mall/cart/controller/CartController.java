package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

@Controller
public class CartController {
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,@RequestParam(defaultValue="1")Integer num
			,HttpServletRequest request,HttpServletResponse response) {
		//先判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null) {
			//写入redis
			cartService.addCart(user.getId(), itemId, num);
			//返回逻辑视图
			return "cartSuccess";
		}
		//如果是登录 将购物车写入cookies
		
		
		boolean flag = false;
		//从cookies中取购物车列表
		List<TbItem> list = getCartListFromCookies(request);
		//判断商品在商品列表中
		if(list.size()>0&&list!=null) {
			//如果存在，数量相加
			for(TbItem item:list) {
				if(item.getId()==itemId.longValue()) {
					item.setNum(item.getNum()+num);
					flag=true;
					break;
				}
			}
		}
		//如果不存在 根据商品id查询商品信息 得到一个tbItem对象
		if(!flag) {
			//把商品添加到商品列表
			TbItem tbItem = itemService.getItemById(itemId);
			tbItem.setNum(num);
			//取一张图片
			String[] str = tbItem.getImages();
			if(str.length>0&&!str.equals("")) {
				tbItem.setImage(str[0]);
			}
			//写入cookies
			list.add(tbItem);
		}
		
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list)
				,36000);
		//返回添加成功页面
		return "cartSuccess";
	}
	
	
	/**展示购物车列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request,HttpServletResponse response) {
		//从cookies取购物车列表
		//判断用户是否登录状态
		List<TbItem> cartList = getCartListFromCookies(request);
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null) {
			//如果是登录状态
			// 不为空 把cookies中的购物车商品和服务端购物车商品合并
			cartService.mergeCart(user.getId(), cartList);
			//把cookies中的购物车删除
			CookieUtils.deleteCookie(request, response, "cart");
			//从服务器端取购物车列表
			cartList = cartService.getCartList(user.getId());
		}	
		//未登录状态
		request.setAttribute("cartList", cartList);
		return "cart";
	}
	/**从cookies取购物车列表
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartListFromCookies(HttpServletRequest request){
		String json = CookieUtils.getCookieValue(request, "cart", true);
		//判断json是否为空
		if(json==null||json.equals("")) {
			return new ArrayList<TbItem>();
		}
		//把json转换成商品列表
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	
	/**
	 * 更新购物车商品数量
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response) {
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null) {
			cartService.updateCartNum(user.getId(), itemId, num);
			return E3Result.ok();
		}
		//从cookies中去购物车列表
		List<TbItem> cartList = getCartListFromCookies(request);
		//遍历商品列表找到对应商品
			for(TbItem item:cartList) {
				if(item.getId()==itemId.longValue()) {
					//更新数量
					item.setNum(num);
					break;
				}
			}
		//把购物车列表写回cookies
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList)
				,36000);	
		//返回成功
		return E3Result.ok();
	}
	
	/**
	 * 删除购物车商品
	 * 
	 */
	@RequestMapping("cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,
			HttpServletRequest request,HttpServletResponse response) {
		// 判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.deleteCartNum(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		//从cookies中去购物车列表
		List<TbItem> cartList = getCartListFromCookies(request);
		//遍历商品列表找到对应商品
		for(TbItem item:cartList) {
			if(item.getId()==itemId.longValue()) {
				//更新数量
				cartList.remove(item);
				break;
			}
		}
		//把购物车列表写回cookies
				CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList)
						,36000);	
		return "redirect:/cart/cart.html";
	}
	
	
}
