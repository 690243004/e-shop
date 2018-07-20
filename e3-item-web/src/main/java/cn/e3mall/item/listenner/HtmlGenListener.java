package cn.e3mall.item.listenner;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class HtmlGenListener implements MessageListener {
	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTEM_GEN_PATH}") 
	private String HTEM_GEN_PATH;
	@Override
	public void onMessage(Message message) {
		try {
			// 创建一个模板 
			//从消息中取出id
			TextMessage textMessage = (TextMessage) message;
			//根据id查询商品信息 ，商品基本信息 与商品描述信息
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待事务提交
			Thread.sleep(1000);
			TbItem tbItem = itemService.getItemById(itemId);
			TbItemDesc itemDesc = itemService.getTbItemDescByItemID(itemId);
			//创建一个数据集 封装到模板
			Map data = new HashMap<>();
			data.put("item", tbItem);
			data.put("itemDesc", itemDesc);
			//加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			//创建一个输出流 指定输出的目录及文件名
			Writer out =new FileWriter(new File(HTEM_GEN_PATH+itemId+".html"));
			//生成静态页面
			template.process(data, out);
			//关闭流
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
