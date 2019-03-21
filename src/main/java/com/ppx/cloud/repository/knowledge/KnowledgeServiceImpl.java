package com.ppx.cloud.repository.knowledge;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppx.cloud.auth.common.AuthContext;
import com.ppx.cloud.auth.config.AuthUtils;
import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.demo.product.ProductImg;

@Service
public class KnowledgeServiceImpl extends MyDaoSupport {
 
	public List<Knowledge> list(Page page, Knowledge pojo) {
		
		var c = createCriteria("where").addAnd("k.k_title like ?", "%", pojo.getkTitle(), "%");
		
		var cSql = new StringBuilder("select count(*) from repo_knowledge k").append(c);
		var qSql = new StringBuilder("select k.*, concat((select cat_name from repo_knowledge_category where cat_id = c.parent_id), '-', cat_name) cat_name"
				+ " from repo_knowledge k left join repo_knowledge_category c on k.cat_id = c.cat_id").append(c);
		
		List<Knowledge> list = queryPage(Knowledge.class, page, cSql, qSql, c.getParaList());
		return list;
	}
	
	@Transactional
	public Map<String, Object> insert(Knowledge pojo) {
		int userId = AuthContext.getLoginAccount().getUserId();
		pojo.setModifiedBy(userId);
		pojo.setCreatedBy(userId);
	
		String[] imgSrc = new String[]{};
		if (Strings.isNotEmpty(pojo.getImgSrc())) {
			imgSrc = pojo.getImgSrc().split(",");
			pojo.setMainImgSrc(imgSrc[0]);
		}
		
		insertEntity(pojo);
		int kId = super.getLastInsertId();
		// 附加图(第二个开始)
		for (int i = 1; i < imgSrc.length; i++) {
			KnowledgeImg img = new KnowledgeImg();
			img.setkId(kId);
			img.setkImgSrc(imgSrc[i]);
			img.setkImgPrio(i);
			insertEntity(img);
		}
		// 内容
		if (Strings.isNotEmpty(pojo.getkContent())) {
			String insertContentsSql = "insert into repo_knowledge_content(k_id, k_content) values(?, ?)";
			getJdbcTemplate().update(insertContentsSql, kId, pojo.getkContent());
		}
		return ReturnMap.of();
	}

	public Knowledge get(Integer id) {
		Knowledge pojo = getJdbcTemplate().queryForObject("select * from repo_knowledge where k_id = ?",
                BeanPropertyRowMapper.newInstance(Knowledge.class), id);    
        return pojo;
    }
    
    public Map<String, Object> update(Knowledge bean) {
        int r = updateEntity(bean);
        return ReturnMap.exists(r, "");
    }
    
    public Map<String, Object> delete(Integer id) {
        getJdbcTemplate().update("delete from repo_knowledge where k_id = ?", id);
        return ReturnMap.of();
    }

}
