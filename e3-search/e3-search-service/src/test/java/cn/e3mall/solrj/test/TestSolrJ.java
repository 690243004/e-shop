package cn.e3mall.solrj.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.common.pojo.SearchItem;

public class TestSolrJ {
	
	public void test() throws Exception {
		String baseUrl = "http://192.168.171.5:8080/solr/collection1";
		// 创建solrClient对象
		try {
			System.out.println("TestSolrJ.test()");
		    SolrServer solrServer = new HttpSolrServer(baseUrl);
			// 创建document对象
			SolrInputDocument document = new SolrInputDocument();
			document.setField("id","haha");
			solrServer.add(document);
			solrServer.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void test2() {
		 ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-service.xml");
		 SolrServer solrServer = (SolrServer) ac.getBean("httpSolrServer");
		 SolrQuery solrQuery = new SolrQuery();
		 solrQuery.add("q","id:*");
		 try {
			QueryResponse response = solrServer.query(solrQuery);
			SolrDocumentList docs = response.getResults();
			long numFound = docs.getNumFound();
			
			for(SolrDocument doc:docs){
				System.out.println(doc.get("id"));
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} 
		 
	}
	
	//@Test
	public void test3() {
		 ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		 ItemMapper itemMapper = ac.getBean(ItemMapper.class);
		 List<SearchItem> list = itemMapper.getItemList();
		 System.out.println(list.get(0).getTitle());
	}
	@Test
	public void test4() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		 ItemMapper itemMapper = ac.getBean(ItemMapper.class);
		 SearchItem item = itemMapper.getItemById(536563);
		 System.out.println(item.toString());
	}
	
}
