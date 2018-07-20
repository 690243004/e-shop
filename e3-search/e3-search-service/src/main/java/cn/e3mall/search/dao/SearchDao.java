package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

/**
 * @author MyPC
 *商品搜索dao
 */
@Repository
public class SearchDao {
	@Autowired
	private SolrServer solrServer;
	/**
	 * @param soloQuery
	 * @return
	 * 根据查询条件查询索引
	 * @throws SolrServerException 
	 */
	public SearchResult search(SolrQuery soloQuery) throws SolrServerException {
		//根据query查询索引库
		QueryResponse response = solrServer.query(soloQuery);
		//查询结果
		SolrDocumentList doc = response.getResults();
		//取查询结果总记录数
		long num = doc.getNumFound();
		SearchResult searchResult = new SearchResult();
		searchResult.setRecordCount(num);
		List<SearchItem> itemList = new ArrayList<>();
		
		Map<String, Map<String, List<String>>> hightLighting = response.getHighlighting();
		//取商品列表
		for(SolrDocument solrDocument:doc) {
			SearchItem item = new SearchItem();
			item.setId((String)solrDocument.get("id"));
			item.setCategory_name((String)solrDocument.get("item_category_name"));
			item.setImage((String)solrDocument.get("item_image"));
			item.setPrice((long)solrDocument.get("item_price"));
			item.setSell_point((String)solrDocument.get("item_sell_point"));
			//取高亮显示
			String title = "";
			
			List<String> list = hightLighting.get(solrDocument.get("id")).get("item_title");
			if(list!=null&&list.size()>0) {
				title = list.get(0);
				item.setTitle(title);
			}else{
				item.setTitle((String)solrDocument.get("item_title"));
			}
			//添加到商品列表
			itemList.add(item);
		}
		searchResult.setItemList(itemList);
		//返回结果
		return searchResult;
	}
}
