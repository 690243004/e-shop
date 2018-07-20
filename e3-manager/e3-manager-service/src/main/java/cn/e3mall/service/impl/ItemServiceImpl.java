package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.Page;
import cn.e3mall.pojo.QueryVo;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired	 
	private TbItemMapper tbItemMapper;
	@Autowired	 
	private TbItemDescMapper tbItemDescMapper;
	@Autowired	
	private JmsTemplate jmsTemplate;
	@Resource(name="topicDestination")
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Override
	public TbItem getItemById(long itemId) {
		//redis:查询缓存
		try {
			String json = jedisClient.get("ITEM_INFO:"+itemId+":BASE");
			if(!StringUtils.isEmpty(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//缓存中没有 查询数据库
		//根据主键查询
		//TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = tbItemMapper.selectByExample(example);
		if(list!=null&&list.size()>0) {
			try {
				//redis:添加到缓存
				jedisClient.set("ITEM_INFO:"+itemId+":BASE", JsonUtils.objectToJson(list.get(0)));
				//redis:设置过期时间
				jedisClient.expire("ITEM_INFO:"+itemId+":BASE", 3600);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		
		}else {
			return null;
		}
		
	}
	@Override
	public Page<TbItem> queryTbitemList(QueryVo queryVo) {
		//计算起始值 
		queryVo.setStart((queryVo.getPage()-1)*queryVo.getRows());
		//调用tbItemMapper方法返回list
		List<TbItem> rows = tbItemMapper.queryTbItemByQueryVo(queryVo);
		//调用tbItemMapper方法返回总数
		int count = tbItemMapper.countByExample(new TbItemExample());
		//创建page对象 赋值
		Page<TbItem> page = new Page<TbItem>();
		page.setPage(queryVo.getPage());
		page.setRows(rows);
		page.setSize(queryVo.getRows());
		page.setTotal(count);
		return page;
	}
	
	
	@Override
	public E3Result addItem(TbItem item, String desc) {
		//生成商品id
		final long id = IDUtils.genItemId();
		//封装商品信息
		item.setId(id);
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		tbItemMapper.insert(item);
		//创建一个商品描述对应的Pojo对象
		TbItemDesc tbItemDesc = new TbItemDesc();
		//补全对象信息
		tbItemDesc.setItemId(id);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setUpdated(new Date());
		//向商品描述表插入数据
		tbItemDescMapper.insert(tbItemDesc);
		//发送一个商品添加消息
		jmsTemplate.send(topicDestination,new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage text = session.createTextMessage(id+"");
				return text;
			}
			
		});
		return E3Result.ok();
	}

	@Override
	public TbItemDesc getTbItemDescByItemID(Long itemId) {
		try {
			String json = jedisClient.get("ITEM_INFO:"+itemId+":DESC");
			if(!StringUtils.isEmpty(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		TbItemDesc desc = tbItemDescMapper.selectByPrimaryKey(itemId);
		try {
			//redis:添加到缓存
			jedisClient.set("ITEM_INFO:"+itemId+":DESC", JsonUtils.objectToJson(desc));
			//redis:设置过期时间
			jedisClient.expire("ITEM_INFO:"+itemId+":DESC", 3600);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return desc;
	}
	@Override
	public E3Result upateItem(TbItem item, TbItemDesc desc) {
		tbItemMapper.updateByPrimaryKeySelective(item);
		
		tbItemDescMapper.updateByPrimaryKey(desc);
		return E3Result.ok();
	}
}
