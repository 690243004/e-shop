package cn.e3mall.item.pojo;

import cn.e3mall.pojo.TbItem;

public class Item extends TbItem{
	public String[] getImages() {
		String image2 = this.getImage();
		if(image2!=null&&!"".equals(image2)) {
			return image2.split(",");
		}
		return null;
	}
}
