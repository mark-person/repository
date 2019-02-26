package com.ppx.cloud.demo.test;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;

@Service
public class TestServiceImpl extends MyDaoSupport {

	public List<Test> list(Page page, Test pojo) {
		
		
		// 默认排序，后面加上需要从页面传过来的排序的，防止SQL注入
		// page.addDefaultOrderName("test_id").addPermitOrderName("test_price").addUnique("test_id");

		// 分开两条sql，mysql在count还会执行子查询, 总数返回0将不执行下一句
		
		//  mysql8支持order by和字查询count(*)优化，不支持left join优化
		var c = createCriteria("where").addAnd("t.test_name like ?", "%", pojo.getTestName(), "%");
		
		//String[] id = {"1", "2"};// "1,2,3"
		//c.addAnd("t.test_id in ?", id);
		 
		page.addDefaultOrderName("t.test_id").addPermitOrderName("t.test_name").addUnique("t.test_id");
		
		
		var cSql = new StringBuilder("select count(*) from test t").append(c);
		var qSql = new StringBuilder("select * from test t").append(c);
		
		List<Test> list = queryPage(Test.class, page, cSql, qSql, c.getParaList());
		return list;
	}
	
	public Map<String, Object> insert(Test pojo) {
        // 后面带不允许重名的字段（该字段需要建索引）
		int r = insertEntity(pojo, "test_name");
        return ReturnMap.exists(r, "测试名称");
    }
	
	public Test get(Integer id) {
		Test pojo = getJdbcTemplate().queryForObject("select * from test where test_id = ?",
                BeanPropertyRowMapper.newInstance(Test.class), id);     
        return pojo;
    }
    
    public Map<String, Object> update(Test bean) {
        // 后面带不允许重名的字段（该字段需要建索引）
        int r = updateEntity(bean, "test_name");
        return ReturnMap.exists(r, "测试名称");
    }
    
    public Map<String, Object> delete(Integer id) {
        getJdbcTemplate().update("delete from test where test_id = ?", id);
        return ReturnMap.of();
    }

}
