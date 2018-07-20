package cn.e3mall.content.service;

import java.util.List;

import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.Page;
import cn.e3mall.pojo.QueryVo;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;
@Service
public class ContentServiceImpl implements ContentService{
	@Autowired
	private JedisClient client;
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	@Autowired
	private TbContentMapper mapper;
	@Override
	public Page<TbContent> queryTbContentList(QueryVo queryVo) {
		// 计算起始位置
		queryVo.setStart((queryVo.getPage()-1)*queryVo.getRows());
		//查询集合
		List<TbContent> list = mapper.queryTbContentListByQueryVo(queryVo);
		//查询总数
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(queryVo.getCategoryId());
		int count = mapper.countByExample(example);
		Page<TbContent> page = new Page();
		page.setPage(queryVo.getPage());
		page.setSize(queryVo.getRows());
		page.setTotal(count);
		page.setRows(list);
		return page;
	}
	@Override
	public List<TbContent> getContentListByCid(long cid) {
		//查询缓存
		try {
			String json = client.hget(CONTENT_LIST, cid+"");
			if(!StringUtils.isEmpty(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		}catch(Exception e) {
			
		}
		//如果缓存中有直接响应结果
		//如果没有查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = mapper.selectByExample(example);
		//把结果添加到缓存
		try {
			client.hset(CONTENT_LIST, cid+"",JsonUtils.objectToJson(list));
		}catch(Exception e) {
			
		}
		
		return list;
	}

}
