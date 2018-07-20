package cn.e3mall.jedis;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.jedis.JedisClientPool;

public class JedisClientTest {
	@Test
	public void testJedisClient() {
		try {
			ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
			JedisClient jedisClient = ac.getBean(JedisClient.class);

			String str = jedisClient.hget("CONTENT_LIST", "89");
			System.out.println(str);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
