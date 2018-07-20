package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {
	@Autowired
	private TbUserMapper tbUserMapper;

	@Override
	public E3Result checkData(String param, int type) {
		// 根据不同的type类型 生成不同的查询条件
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		if (type == 1) {
			criteria.andUsernameEqualTo(param);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(param);
		} else if (type == 3) {
			criteria.andEmailEqualTo(param);
		} else {
			return E3Result.build(400, "数据类型错误");
		}
		// 执行查询
		List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
		// 判断结果是否包含数据
		if (list.size() > 0 && list != null) {
			return E3Result.ok(false);
		} else {
			return E3Result.ok(true);
		}
		// 如果有数据 返回false
		// 没有数据 返回true 可用 没有重复
	}

	@Override
	public E3Result register(TbUser tbUser) {
		// 数据有效性校验
		if (StringUtils.isEmpty(tbUser.getUsername())||StringUtils.isEmpty(tbUser.getPassword())||
				StringUtils.isEmpty(tbUser.getPhone())) {
			return E3Result.build(400, "用户数据不完整，注册失败");
		}
		//1.用户名 2.手机号 3.邮箱
		E3Result result = checkData(tbUser.getUsername(),1);
		if(!(boolean)result.getData()) {
			return E3Result.build(400, "此用户名被占用");
		}
		result = checkData(tbUser.getPhone(), 2);
		if(!(boolean)result.getData()) {
			return E3Result.build(400, "此手机号被占用");
		}
		// 补全pojo属性
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		// 对password进行md5加密
		String md5Pass = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		tbUser.setPassword(md5Pass);
		// 把用户数据插入数据库
		tbUserMapper.insert(tbUser);
		// 返回添加成功
		return E3Result.ok();
	}

}
