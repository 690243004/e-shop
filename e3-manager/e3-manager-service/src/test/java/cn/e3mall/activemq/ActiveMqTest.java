package cn.e3mall.activemq;


import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ActiveMqTest {
	@Test
	public void test1() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-activemq.xml");
		//从容器中获得JmsTemplate对象。
		JmsTemplate jmsTemplate = ac.getBean(JmsTemplate.class);
		//从容器中获得一个Destination对象。
		Destination destination = (Destination) ac.getBean("queueDestination");
		//发送消息
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("send activemq message");
			}
		});
	}
}
