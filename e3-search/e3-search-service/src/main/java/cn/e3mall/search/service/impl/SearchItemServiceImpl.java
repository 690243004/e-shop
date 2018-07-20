package cn.e3mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {
	@Autowired
	private HttpSolrServer httpSolrServer;
	
	@Autowired
	private ItemMapper itemMapper;
	@Override
	public E3Result importAllItems() {
		try {
			//查询商品列表
			List<SearchItem> list = itemMapper.getItemList();
			//遍历商品列表
			for(SearchItem searchitem:list) {
				//创建文档对象
				//向文档对象中添加域
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", searchitem.getId());
				document.addField("item_title", searchitem.getTitle());
				document.addField("item_price", searchitem.getPrice());
				document.addField("item_sell_point", searchitem.getSell_point());
				document.addField("item_image", searchitem.getImage());
				document.addField("item_category_name", searchitem.getCategory_name());
				//写入索引库
				httpSolrServer.add(document);
			}
			//提交
			httpSolrServer.commit();
			//返回导入成功
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(500, "数据导入异常");
		}
	}

	

	
}
