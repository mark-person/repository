package com.ppx.cloud.repository.mobile;

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

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.ppx.cloud.auth.common.AuthContext;
import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.LimitRecord;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.repository.knowledge.pojo.Knowledge;
import com.ppx.cloud.repository.knowledge.pojo.KnowledgeImg;
import com.ppx.cloud.repository.todo.pojo.Todo;

@Service
public class MobileServiceImpl extends MyDaoSupport {
	
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
		
		insertSearchWord(pojo.getkTitle(), kId, pojo.getCatId(), recommendPrio);
		
		return ReturnMap.of("kId", kId);
	}
	
	private void insertSearchWord(String kTitle, int kId, int catId, int recommendPrio) {
		// 分词，可能要做成二次分词
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
			Object[] obj = {w, kId, catId, recommendPrio};
			batchArgs.add(obj);
		}
		
		String batchInsertSql = "insert into repo_search_word(word, k_id, cat_id, recommend_prio) values(?, ?, ?, ?)";
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
		int recommendPrio = (int)(System.currentTimeMillis() / 1000) - NOW_SECOND + RECOMMEND_BASE_VALUE[pojo.getRecommend() - 1];
		pojo.setRecommendPrio(recommendPrio);
	
		String[] imgSrc = new String[]{};
		if (Strings.isNotEmpty(pojo.getImgSrc())) {
			imgSrc = pojo.getImgSrc().split(",");
			pojo.setMainImgSrc(imgSrc[0]);
		}
		
		// 只能修改自己创建的
		updateEntity(pojo, LimitRecord.newInstance("modified_by", userId));
		
		int kId = pojo.getkId();
		// delete附加图
		getJdbcTemplate().update("delete from repo_knowledge_img where k_id = ?", kId);
		// 附加图(第二个开始)
		for (int i = 1; i < imgSrc.length; i++) {
			KnowledgeImg img = new KnowledgeImg();
			img.setkId(kId);
			img.setkImgSrc(imgSrc[i]);
			img.setkImgPrio(i);
			insertEntity(img);
		}
		
		// delete内容
		getJdbcTemplate().update("delete from repo_knowledge_content where k_id = ?", kId);
		// 内容
		if (Strings.isNotEmpty(pojo.getkContent())) {
			String insertContentsSql = "insert into repo_knowledge_content(k_id, k_content) values(?, ?)";
			getJdbcTemplate().update(insertContentsSql, kId, pojo.getkContent());
		}
		
		// USP
		getJdbcTemplate().update("delete from repo_knowledge_map_usp where k_id = ?", kId);
		if (Strings.isNotEmpty(pojo.getUspIds())) {
			String[] uspId = pojo.getUspIds().split(",");
			String insertUspSql = "insert into repo_knowledge_map_usp(usp_id, k_id, recommend_prio) values(?, ?, ?)";
			for (String id : uspId) {
				getJdbcTemplate().update(insertUspSql, id, kId, recommendPrio);
			}
		}
		
		getJdbcTemplate().update("delete from repo_search_word where k_id = ?", pojo.getkId());
		insertSearchWord(pojo.getkTitle(), pojo.getkId(), pojo.getCatId(), recommendPrio);
		
		return ReturnMap.of("kId", kId);
    }
	
	/**
	 * 主页:全部或按类目找
	 * @param page
	 * @return
	 */
	public List<Knowledge> byCatSearch(MPage page, Integer catId, Integer orderBy) {
		
		String orderStr = "order by k.modified desc";
		if (orderBy != null && orderBy == 1) {
			orderStr = "order by k.recommend_prio desc";
		}
		
		var c = createCriteria("where").addAnd("k.cat_id = ?", catId);
		
		var cSql = new StringBuilder("select count(*) from repo_knowledge k").append(c);
		var qSql = new StringBuilder("select k.*, concat((select cat_name from repo_knowledge_category where cat_id = c.parent_id), '-', cat_name) cat_name"
				+ " from repo_knowledge k left join repo_knowledge_category c on k.cat_id = c.cat_id").append(c).append(orderStr);
		List<Knowledge> list = queryMPage(Knowledge.class, page, cSql, qSql, c.getParaList());
		
		return list;
	}
	
	/**
	 * 主页:分词查找
	 * @param page
	 * @return
	 */
	public List<Knowledge> byWordSearch(MPage page, String word, Integer catId, Integer orderBy) {
		String orderStr = "order by modified desc";
		if (orderBy != null && orderBy == 1) {
			orderStr = "order by recommend_prio desc";
		}
		
		var c = createCriteria("where").addAnd("word = ?", word).addAnd("cat_id = ?", catId);
		
		var cSql = new StringBuilder("select count(*) from repo_search_word").append(c);
		var qSql = new StringBuilder("select k_id from repo_search_word").append(c).append(orderStr);
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
		
		if (recommend != null) {
			if (recommend == 5) {
				c.addAnd("k.recommend_prio > ?", RECOMMEND_BASE_VALUE[4]);
			}
			else {
				c.addAnd("k.recommend_prio > ?", RECOMMEND_BASE_VALUE[recommend - 1]);
				c.addAnd("k.recommend_prio < ?", RECOMMEND_BASE_VALUE[recommend]);
			}
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
		
		if (recommend != null) {
			if (recommend == 5) {
				c.addAnd("recommend_prio > ?", RECOMMEND_BASE_VALUE[4]);
			}
			else {
				c.addAnd("recommend_prio > ?", RECOMMEND_BASE_VALUE[recommend - 1]);
				c.addAnd("recommend_prio < ?", RECOMMEND_BASE_VALUE[recommend]);
			}
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
	
	/**
	 * 我的知识页
	 * @param page
	 * @return
	 */
	public List<Knowledge> myKnowSearch(MPage page, Integer catId, Integer recommend) {
		var c = createCriteria("and").addAnd("cat_id = ?", catId);
		
		if (recommend != null) {
			if (recommend == 5) {
				c.addAnd("recommend_prio > ?", RECOMMEND_BASE_VALUE[4]);
			}
			else {
				c.addAnd("recommend_prio > ?", RECOMMEND_BASE_VALUE[recommend - 1]);
				c.addAnd("recommend_prio < ?", RECOMMEND_BASE_VALUE[recommend]);
			}
		}
		
		int userId = AuthContext.getLoginAccount().getUserId();
		c.addPrePara(userId);
		
		var cSql = new StringBuilder("select count(*) from repo_knowledge where modified_by = ?").append(c);
		var qSql = new StringBuilder("select * from repo_knowledge where modified_by = ?").append(c).append("order by modified desc");
		List<Knowledge> resultList = queryMPage(Knowledge.class, page, cSql, qSql, c.getParaList());
		return resultList;
	}
	
	/**
	 * favorite页
	 * @param page
	 * @return
	 */
	public List<Knowledge> favoriteSearch(MPage page, Integer catId) {
		var c = createCriteria("where").addAnd("cat_id = ?", catId);
		int userId = AuthContext.getLoginAccount().getUserId();
		c.addPrePara(userId);
		
		var cSql = new StringBuilder("select count(*) from repo_knowledge k join repo_favorite f"
				+ " on k.k_id = f.k_id and f.repo_user_id = ?").append(c);
		var qSql = new StringBuilder("select k.* from repo_knowledge k join repo_favorite f "
				+ " on k.k_id = f.k_id and f.repo_user_id = ?").append(c).append("order by k.modified desc");
		List<Knowledge> resultList = queryMPage(Knowledge.class, page, cSql, qSql, c.getParaList());
		return resultList;
	}
	
	public boolean isFavorite(int kId) {
		int userid = AuthContext.getLoginAccount().getUserId();
		String sql = "select count(*) from repo_favorite where repo_user_id = ? and k_id = ?";
		int r = getJdbcTemplate().queryForObject(sql, Integer.class, userid, kId);
		if (r == 1) return true;
		else return false;
	}
	
	@Transactional
	public int confirmFavorite(int kId) {
		int userId = AuthContext.getLoginAccount().getUserId();
		getJdbcTemplate().update("update repo_user set favorite_n = favorite_n + 1 where repo_user_id = ?", userId);
		return getJdbcTemplate().update("insert into repo_favorite(repo_user_id, k_id) values(?, ?)", userId, kId);
	}
	
	@Transactional
	public int cancelFavorite(int kId) {
		int userId = AuthContext.getLoginAccount().getUserId();
		getJdbcTemplate().update("update repo_user set favorite_n = favorite_n - 1 where repo_user_id = ?", userId);
		return getJdbcTemplate().update("delete from repo_favorite where repo_user_id = ? and k_id = ?", userId, kId);
	}
	
	
	
	// >>>>>>>>>>>>>todo>>>>>>>>>>>
	
	public List<Todo> todoList(MPage page, Todo todo) {
		var c = createCriteria("where").addAnd("t.todo_title like", "%", todo.getTodoTitle(), "%");
		
		var cSql = new StringBuilder("select count(*) from repo_todo t").append(c);
		var qSql = new StringBuilder("select t.* from repo_todo t order by t.modified desc");
		List<Todo> resultList = queryMPage(Todo.class, page, cSql, qSql, c.getParaList());
		
		return resultList;
	}
	
	public Todo getTodo(Integer id) {
		Todo pojo = getJdbcTemplate().queryForObject("select * from repo_todo where todo_id = ?",
                BeanPropertyRowMapper.newInstance(Todo.class), id);
        return pojo;
	}
	
	@Transactional
	public Map<String, Object> insertOrUpdateTodo(Todo todo)  {
		int userId = AuthContext.getLoginAccount().getUserId();
		Integer todoId = null;
		if (todo.getTodoId() == null) {
			todo.setModifiedBy(userId);
			insertEntity(todo);
			todoId = super.getLastInsertId();
		}
		else {
			todoId = todo.getTodoId();
			todo.setModified(new Date());
			updateEntity(todo, LimitRecord.newInstance("modified_by", userId));
		}
		
		return ReturnMap.of("todoId", todoId);
	}
	
	@Transactional
	public Map<String, Object> deleteTodo(Integer id) {
		String sql = "delete from repo_todo where todo_id = ?";
		getJdbcTemplate().update(sql, id);
		return ReturnMap.of();
	}
}
