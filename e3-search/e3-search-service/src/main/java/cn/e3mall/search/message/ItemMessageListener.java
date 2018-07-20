package cn.e3mall.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;

public class ItemMessageListener implements javax.jms.MessageListener{
	@Autowired
	private SolrServer server;
	@Autowired
	private ItemMapper mapper;
	@Override
	public void onMessage(Message message) {
		//从消息中取出商品id
		TextMessage textMessage = (TextMessage)message;
		String text;
		try {
			text = textMessage.getText();
			Long itemid = new Long(text);
			Thread.sleep(1000);
			//根据商品id查询数据库
			SearchItem searchItem = mapper.getItemById(itemid);
			//创建一个文档对象
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			//写入索引库
			server.add(document);
			server.commit();
		} catch (Exception e) {

			e.printStackTrace();
		}
		
	}

}
