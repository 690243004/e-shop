package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	public void testJedis() {
		//创建一个连接jedis对象 host,port
		Jedis jedis = new Jedis("192.168.171.3", 6379);
		//直接使用jedis操作redis
		jedis.set("test123", "my first jedis test");
		System.out.println(jedis.get("test123"));
		jedis.close();
	}
	
	public void testJedisPool() {
		//创建连接池对象 host,port
		JedisPool jedisPool = new JedisPool("192.168.171.3", 6379);
		//从连接池中获得 jedis对象(连接)
		Jedis jedis = jedisPool.getResource();
		//操作jedis
		System.out.println(jedis.get("test123"));
		//关闭连接（每次使用完毕后 关闭连接） 连接池回收资源
		jedis.close();
		//关闭连接池
		jedisPool.close();
	}
	

	public void testJedisCluster() throws Exception{
		//创建一个JedisCluster对象(自带连接池) 有个参数nodes是一个set类型,set中包含HostAndPort对象
		try {
			Set<HostAndPort> nodes = new HashSet<>();
			nodes.add(new HostAndPort("192.168.171.3",7001));
			nodes.add(new HostAndPort("192.168.171.3",7002));
			nodes.add(new HostAndPort("192.168.171.3",7003));
			nodes.add(new HostAndPort("192.168.171.3",7004));
			nodes.add(new HostAndPort("192.168.171.3",7005));
			nodes.add(new HostAndPort("192.168.171.3",7006));
			
			JedisCluster jedisCluster = new JedisCluster(nodes);
			//直接使用JedisCluster对象操作redis
			jedisCluster.set("test", "123");
			String str = jedisCluster.get("test");
			System.out.println(str);
			//关闭JedisCluster对象
			jedisCluster.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
