package com.ppx.cloud.repository.knowledge;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.repository.knowledge.pojo.Knowledge;

@Service
public class KnowledgeServiceImpl extends MyDaoSupport {
 
	public List<Knowledge> list(Page page, Knowledge pojo) {
		
		var c = createCriteria("where")
				.addAnd("k.k_title like ?", "%", pojo.getkTitle(), "%")
				.addAnd("k.cat_id = ?", pojo.getCatId());
		
		if (pojo.getRecommend() != null && pojo.getRecommend() >= 3) {
			c.addAnd("k.recommend >= ?", pojo.getRecommend());
		}
		else if (pojo.getRecommend() != null && pojo.getRecommend() <= -1) {
			c.addAnd("k.recommend <= ?", -pojo.getRecommend());
		}
		
		var cSql = new StringBuilder("select count(*) from repo_knowledge k").append(c);
		var qSql = new StringBuilder("select k.*, concat((select cat_name from repo_knowledge_category where cat_id = c.parent_id), '-', cat_name) cat_name"
				+ " from repo_knowledge k left join repo_knowledge_category c on k.cat_id = c.cat_id").append(c).append("order by modified desc");
		
		List<Knowledge> list = queryPage(Knowledge.class, page, cSql, qSql, c.getParaList());
		return list;
	}
	
	public Map<String, Object> changeCat(@RequestParam Integer id, @RequestParam Integer catId) {
		String updateSql = "update repo_knowledge set catId = ? where k_id = ?";
		getJdbcTemplate().update(updateSql, catId, id);
		return ReturnMap.of();
	}
	
	@Transactional
    public Map<String, Object> delete(Integer id) {
        getJdbcTemplate().update("delete from repo_knowledge where k_id = ?", id);
        getJdbcTemplate().update("delete from repo_knowledge_content where k_id = ?", id);
        getJdbcTemplate().update("delete from repo_knowledge_img where k_id = ?", id);
        return ReturnMap.of();
    }
	
}
