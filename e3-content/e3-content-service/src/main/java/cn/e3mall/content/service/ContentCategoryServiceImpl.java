package cn.e3mall.content.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.EasyUITreeNode;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理
 * @author MyPC
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{
	@Autowired
	private TbContentCategoryMapper mapper;
	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		// 根据子节点查询列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> catList = mapper.selectByExample(example);
		List<EasyUITreeNode> nodeList = new ArrayList();
		for(TbContentCategory tbContentCategory:catList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			node.setText(tbContentCategory.getName());
			nodeList.add(node);
		}
		return nodeList;
	}
	
	@Override
	public E3Result addContentCategory(long parentId, String name) {
		// 添加子节点
		TbContentCategory node = new TbContentCategory();
		node.setCreated(new Date());
		node.setIsParent(false);
		node.setName(name);
		node.setSortOrder(1);
		//'状态。可选值:1(正常),2(删除)',
		node.setStatus(1);
		node.setUpdated(new Date());
		node.setParentId(parentId);
		//插入数据库
		mapper.insert(node);
		//查询父节点 是否为parent 不是就改为是
		TbContentCategory parentNode = mapper.selectByPrimaryKey(parentId);
		if(!parentNode.getIsParent()) {
			parentNode.setIsParent(true);
			mapper.updateByPrimaryKey(parentNode);
		}
		
		return E3Result.ok(node);
	}

}
