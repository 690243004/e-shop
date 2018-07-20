package cn.e3mall.search.service.impl;

import java.io.UnsupportedEncodingException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;
@Service
public class SearchServiceImpl implements SearchService{
	@Autowired
	private SearchDao searchDao;
	@Override
	public SearchResult search(String keyword, int page, int rows) throws SolrServerException, UnsupportedEncodingException {
		// 创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("item_keywords:"+keyword);
		//设置默认搜索域
		if(page<=0) page=1;
		query.setStart((page-1)*rows);
		query.setRows(rows);
		//开启高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		//调用dao查询
		SearchResult result = searchDao.search(query);
		//计算总页数
		int totalPage = (int) (result.getRecordCount()/rows);
		if((totalPage%rows)>0) totalPage++;
		result.setRecordCount(totalPage);
		return result;
	}

}
