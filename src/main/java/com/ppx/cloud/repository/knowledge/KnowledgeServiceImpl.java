package com.ppx.cloud.repository.knowledge;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.util.Strings;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.ppx.cloud.auth.common.AuthContext;
import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.common.page.Page;

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
	
	private int[] RECOMMEND_BASE_VALUE = {500000000, 600000000, 700000000, 800000000, 900000000};
	private int NOW_SECOND = 1554386286;
	
	@Transactional
	public Map<String, Object> insert(Knowledge pojo) {
		int userId = AuthContext.getLoginAccount().getUserId();
		pojo.setModifiedBy(userId);
		int recommendPrio = (int)(System.currentTimeMillis() / 1000) - NOW_SECOND + RECOMMEND_BASE_VALUE[pojo.getRecommend() - 1];
		pojo.setRecommendPrio(recommendPrio);
	
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
		
		// USP
		if (Strings.isNotEmpty(pojo.getUspIds())) {
			String[] uspId = pojo.getUspIds().split(",");
			String insertUspSql = "insert into repo_knowledge_map_usp(usp_id, k_id, recommend_prio) values(?, ?, ?)";
			for (String id : uspId) {
				getJdbcTemplate().update(insertUspSql, id, kId, recommendPrio);
			}
		}
		
		insertSearchIndex(pojo.getkTitle(), kId, pojo.getCatId());
		
		return ReturnMap.of("kId", kId);
	}
	
	private void insertSearchIndex(String kTitle, int kId, int catId) {
		// 分词
		// https://github.com/hankcs/HanLP
		
		// https://github.com/hankcs/HanLP
		// CustomDictionary.add("包欢迎");
		// nature:词性 w表示标点符号
		Set<String> wordSet = new HashSet<String>(); 
		
		List<Term> termList = HanLP.segment(kTitle);
		for (Term term : termList) {
			if (!"w".equals(term.nature.toString())) {
				wordSet.add(term.word);
			}
		}
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (String w : wordSet) {
			Object[] obj = {w, kId, catId};
			batchArgs.add(obj);
		}
		
		String batchInsertSql = "insert into repo_search(word, k_id, cat_id) values(?, ?, ?)";
		getJdbcTemplate().batchUpdate(batchInsertSql, batchArgs);
	}

	public Knowledge get(Integer id) {
		Knowledge pojo = getJdbcTemplate().queryForObject("select k.*,"
				+ " (select k_content from repo_knowledge_content where k_id = k.k_id) k_content,"
				+ " concat((select cat_name from repo_knowledge_category where cat_id = c.parent_id), '-', cat_name) cat_name"
				+ " from repo_knowledge k left join repo_knowledge_category c on k.cat_id = c.cat_id"
				+ " where k_id = ?",
                BeanPropertyRowMapper.newInstance(Knowledge.class), id);
		
		if (Strings.isNotEmpty(pojo.getMainImgSrc())) {
			String imgSql = "select k_img_src from repo_knowledge_img where k_id = ? order by k_img_prio";
			List<String> imgList = getJdbcTemplate().queryForList(imgSql, String.class, id);
			if (imgList.isEmpty()) {
				pojo.setImgSrc(pojo.getMainImgSrc());
			}
			else {
				pojo.setImgSrc(pojo.getMainImgSrc() + "," + String.join(",", imgList));
			}
		}
        return pojo;
    }
    
	@Transactional
    public Map<String, Object> update(Knowledge pojo) {
    	int userId = AuthContext.getLoginAccount().getUserId();
    	pojo.setModified(new Date());
		pojo.setModifiedBy(userId);
		int recommendPrio = (int)(System.currentTimeMillis() / 1000) - NOW_SECOND + RECOMMEND_BASE_VALUE[pojo.getRecommend() - 1];
		pojo.setRecommendPrio(recommendPrio);
	
		String[] imgSrc = new String[]{};
		if (Strings.isNotEmpty(pojo.getImgSrc())) {
			imgSrc = pojo.getImgSrc().split(",");
			pojo.setMainImgSrc(imgSrc[0]);
		}
		
		updateEntity(pojo);
		int kId = pojo.getkId();
		// delete附加图
		String deleteImgSql = "delete from repo_knowledge_img where k_id = ?";
		getJdbcTemplate().update(deleteImgSql, kId);
		// 附加图(第二个开始)
		for (int i = 1; i < imgSrc.length; i++) {
			KnowledgeImg img = new KnowledgeImg();
			img.setkId(kId);
			img.setkImgSrc(imgSrc[i]);
			img.setkImgPrio(i);
			insertEntity(img);
		}
		
		// delete内容
		String deleteContentSql = "delete from repo_knowledge_content where k_id = ?";
		getJdbcTemplate().update(deleteContentSql, kId);
		// 内容
		if (Strings.isNotEmpty(pojo.getkContent())) {
			String insertContentsSql = "insert into repo_knowledge_content(k_id, k_content) values(?, ?)";
			getJdbcTemplate().update(insertContentsSql, kId, pojo.getkContent());
		}
		
		// USP
		String delUspSql = "delete from repo_knowledge_map_usp where k_id = ?";
		getJdbcTemplate().update(delUspSql, kId);
		if (Strings.isNotEmpty(pojo.getUspIds())) {
			String[] uspId = pojo.getUspIds().split(",");
			String insertUspSql = "insert into repo_knowledge_map_usp(usp_id, k_id, recommend_prio) values(?, ?, ?)";
			for (String id : uspId) {
				getJdbcTemplate().update(insertUspSql, id, kId, recommendPrio);
			}
		}
		
		// TODO
		// 更新两个搜索字段
		
		getJdbcTemplate().update("delete from repo_search where k_id = ?", pojo.getkId());
		insertSearchIndex(pojo.getkTitle(), pojo.getkId(), pojo.getCatId());
		
		return ReturnMap.of("kId", kId);
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
	
	// >>>>>>>> search
	// 1.排序问题 2.传参搜索
	// 1.主页按更新时间排序 2.精选页排星级+时间,加上USP (查看USP时跑到精品页)
	
	
	/**
	 * 主页:全部或按类目找
	 * @param page
	 * @return
	 */
	public List<Knowledge> byCatSearch(MPage page, Integer catId) {
		var c = createCriteria("where").addAnd("k.cat_id = ?", catId);
		
		var cSql = new StringBuilder("select count(*) from repo_knowledge k").append(c);
		var qSql = new StringBuilder("select k.*, concat((select cat_name from repo_knowledge_category where cat_id = c.parent_id), '-', cat_name) cat_name"
				+ " from repo_knowledge k left join repo_knowledge_category c on k.cat_id = c.cat_id").append(c).append("order by k.modified desc");
		List<Knowledge> list = queryMPage(Knowledge.class, page, cSql, qSql, c.getParaList());
		
		return list;
	}
	
	/**
	 * 主页:分词查找
	 * @param page
	 * @return
	 */
	public List<Knowledge> byWordSearch(MPage page, String word, Integer catId) {
		var c = createCriteria("where").addAnd("word = ?", word).addAnd("cat_id = ?", catId);
		
		var cSql = new StringBuilder("select count(*) from repo_search").append(c);
		var qSql = new StringBuilder("select k_id from repo_search").append(c).append("order by modified desc");
		List<Knowledge> kIdList = queryMPage(Knowledge.class, page, cSql, qSql, c.getParaList());
		if (kIdList.isEmpty()) {
			return kIdList;
		}
		
		List<Integer> kIdPara = new ArrayList<Integer>();
		for (Knowledge knowledge : kIdList) {
			kIdPara.add(knowledge.getkId());
		}
		NamedParameterJdbcTemplate nameTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
		var para = new HashMap<String, Object>();
		para.put("kId", kIdPara);
		var resultSql = "select k.*, concat((select cat_name from repo_knowledge_category where cat_id = c.parent_id), '-', cat_name) cat_name"
				+ " from repo_knowledge k left join repo_knowledge_category c on k.cat_id = c.cat_id where k.k_id in (:kId)";
		List<Knowledge> resultList = nameTemplate.query(resultSql, para, BeanPropertyRowMapper.newInstance(Knowledge.class));
		
		return resultList;
	}
	
	/**
	 * nice页:全部或按星级
	 * @param page
	 * @param recommendPrio
	 * @return
	 */
	public List<Knowledge> byRecommendSearch(MPage page, Integer recommend) {
		var c = createCriteria("where");
		if (recommend == 5) {
			c.addAnd("k.recommend_prio > ?", RECOMMEND_BASE_VALUE[4]);
		}
		else {
			c.addAnd("k.recommend_prio > ?", RECOMMEND_BASE_VALUE[recommend - 1]);
			c.addAnd("k.recommend_prio < ?", RECOMMEND_BASE_VALUE[recommend]);
		}
		
		var cSql = new StringBuilder("select count(*) from repo_knowledge k").append(c);
		var qSql = new StringBuilder("select k.*, concat((select cat_name from repo_knowledge_category where cat_id = c.parent_id), '-', cat_name) cat_name"
				+ " from repo_knowledge k left join repo_knowledge_category c on k.cat_id = c.cat_id").append(c).append("order by k.recommend_prio desc");
		List<Knowledge> list = queryMPage(Knowledge.class, page, cSql, qSql, c.getParaList());
		
		return list;
	}
	
	/**
	 * nice页:usp查找
	 * @param page
	 * @return
	 */
	public List<Knowledge> byUspSearch(MPage page, Integer uspId, Integer recommend) {
		var c = createCriteria("where").addAnd("usp_id = ?", uspId);
		
		if (recommend == 5) {
			c.addAnd("recommend_prio > ?", RECOMMEND_BASE_VALUE[4]);
		}
		else {
			c.addAnd("recommend_prio > ?", RECOMMEND_BASE_VALUE[recommend - 1]);
			c.addAnd("recommend_prio < ?", RECOMMEND_BASE_VALUE[recommend]);
		}
		
		var cSql = new StringBuilder("select count(*) from repo_knowledge_map_usp").append(c);
		var qSql = new StringBuilder("select k_id from repo_knowledge_map_usp").append(c).append("order by recommend_prio desc");
		List<Knowledge> kIdList = queryMPage(Knowledge.class, page, cSql, qSql, c.getParaList());
		if (kIdList.isEmpty()) {
			return kIdList;
		}
		
		List<Integer> kIdPara = new ArrayList<Integer>();
		for (Knowledge knowledge : kIdList) {
			kIdPara.add(knowledge.getkId());
		}
		NamedParameterJdbcTemplate nameTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
		var para = new HashMap<String, Object>();
		para.put("kId", kIdPara);
		var resultSql = "select k.*, concat((select cat_name from repo_knowledge_category where cat_id = c.parent_id), '-', cat_name) cat_name"
				+ " from repo_knowledge k left join repo_knowledge_category c on k.cat_id = c.cat_id where k.k_id in (:kId)";
		List<Knowledge> resultList = nameTemplate.query(resultSql, para, BeanPropertyRowMapper.newInstance(Knowledge.class));
		
		return resultList;
	}
	

}
