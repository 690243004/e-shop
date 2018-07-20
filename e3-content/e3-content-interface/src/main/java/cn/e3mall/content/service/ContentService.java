package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.pojo.Page;
import cn.e3mall.pojo.QueryVo;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	Page<TbContent> queryTbContentList(QueryVo queryVo);
	List<TbContent> getContentListByCid(long cid);
}
