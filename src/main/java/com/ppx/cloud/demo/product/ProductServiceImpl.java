package com.ppx.cloud.demo.product;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;

@Service
public class ProductServiceImpl extends MyDaoSupport {
	
	public List<Product> list(Page page, Product pojo) {

		// 默认排序，后面加上需要从页面传过来的排序的，防止SQL注入
		// page.addDefaultOrderName("test_id").addPermitOrderName("test_price").addUnique("test_id");

		// 分开两条sql，mysql在count还会执行子查询, 总数返回0将不执行下一句
		var c = createCriteria("where").addAnd("t.prod_title like ?", "%", pojo.getProdTitle(), "%");
		 
		page.addDefaultOrderName("t.prod_id").addUnique("t.prod_id");
		
		
		var cSql = new StringBuilder("select count(*) from product t").append(c);
		var qSql = new StringBuilder("select t.*, c.cat_name from product t left join category c on t.cat_id = c.cat_id ").append(c);
		
		List<Product> list = queryPage(Product.class, page, cSql, qSql, c.getParaList());
		return list;
	}
	
	
	
	@Transactional
	public Map<String, Object> insert(Product pojo) {
		String[] imgSrc = pojo.getImgSrc().split(",");
		pojo.setMainImgSrc(imgSrc[0]);
		insertEntity(pojo);
		int prodId = super.getLastInsertId();
		
		for (int i = 1; i < imgSrc.length; i++) {
			ProductImg img = new ProductImg();
			img.setProdId(prodId);
			img.setProdImgSrc(imgSrc[i]);
			img.setProdImgPrio(i);
			insertEntity(img);
		}
		
        return ReturnMap.of();
    }
}
