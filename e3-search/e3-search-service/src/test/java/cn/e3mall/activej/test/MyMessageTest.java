package cn.e3mall.activej.test;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.search.message.MessageListener;

public class MyMessageTest {
	@Test
	public void test() throws IOException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-activemq.xml");
		ac.getBean(MessageListener.class);
		System.in.read();
	}
}
