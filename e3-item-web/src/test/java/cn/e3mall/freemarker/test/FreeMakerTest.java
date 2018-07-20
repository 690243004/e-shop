package cn.e3mall.freemarker.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMakerTest {
	@Test
	public void test() throws IOException, TemplateException {
		//1.创建一个模板文件 
	
		//2.创建一个configuration对象 构造方法的参数就是freemarker对于的版本号。
		Configuration configuration  = new Configuration(Configuration.getVersion());
		//3.设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("C:\\Users\\MyPC\\eclipse-workspace\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
		//4.模板文件的编码格式 一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		//5.加载一个模板文件 创建一个模板对象
		Template  template = configuration.getTemplate("hello.ftl");
		//6.创建一个数据集 可以是pojo 也可以是 map 推荐使用map
		Map data = new HashMap<>();
		data.put("hello", "this is my first freemarker test.");
		//7.创建一个witer对象 指定输出文件的路径 及文件名 
		Writer out = new FileWriter(new File("D:/freemaker_test/hello.txt"));
		//8.生成静态页面
		template.process(data, out);
		//9.关闭流
		out.close();
	}
}	
