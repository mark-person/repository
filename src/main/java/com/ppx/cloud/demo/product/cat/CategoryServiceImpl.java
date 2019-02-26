package com.ppx.cloud.demo.product.cat;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;

@Service
public class CategoryServiceImpl extends MyDaoSupport implements CategoryService {
	
	public List<Category> listCategoy() {

		String sql = "select cat_id, cat_name from category where cat_status = 1";
		
		List<Category> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Category.class));
		
		return list;
	}
	
	
}
