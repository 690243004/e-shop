package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

@Controller
public class HtmlController {
	@Autowired
	private FreeMarkerConfigurer freeMakerConfigurer;
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		Configuration configuration = freeMakerConfigurer.getConfiguration();
		Template templete = configuration.getTemplate("hello.ftl");
		Writer out = new FileWriter(new File("D:/freemaker_test/hello2.txt"));
		
		return null;
	}
}
