package com.ppx.cloud.repository.mobile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.auth.common.AuthContext;
import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.repository.category.KnowledgeCategory;
import com.ppx.cloud.repository.category.KnowledgeCategoryService;
import com.ppx.cloud.repository.knowledge.pojo.Knowledge;
import com.ppx.cloud.repository.subject.KnowledgeSubject;
import com.ppx.cloud.repository.subject.KnowledgeSubjectService;
import com.ppx.cloud.repository.todo.pojo.Todo;
import com.ppx.cloud.repository.user.RepoUser;
import com.ppx.cloud.repository.user.RepoUserService;
import com.ppx.cloud.repository.usp.KnowledgeUsp;
import com.ppx.cloud.repository.usp.KnowledgeUspService;

@Controller
public class MobileController {
	
	private final static String INIT_CONTENT = "# \n* \n* ";
	
	@Autowired
	private MobileServiceImpl impl;
	@Autowired
    private RepoUserService repoUserService;
    @Autowired
    private KnowledgeCategoryService categoryService;
    @Autowired
    private KnowledgeUspService uspService;
    @Autowired
    private KnowledgeSubjectService subjectService;
    
    public ModelAndView home(ModelAndView mv) {
    	
		mv.addObject("list", homeSearch(new MPage(), null, null, null));
		mv.addObject("catList", categoryService.list());
		mv.addObject("uspList", uspService.list());
		return mv;
	}
    
    public Map<?, ?> homeSearch(MPage page, String word, Integer catId, Integer orderBy) {
    	if (Strings.isBlank(word)) {
    		return ReturnMap.of(page, impl.byCatSearch(page, catId, orderBy));
    	}
    	else {
    		return ReturnMap.of(page, impl.byWordSearch(page, word, catId, orderBy));
    	}
	}
    
    public ModelAndView add(ModelAndView mv) {
    	mv.setViewName("repository/mobile/mobile/know");
    	
    	Knowledge pojo = new Knowledge();
    	pojo.setkContent(INIT_CONTENT);
    	mv.addObject("initContent", INIT_CONTENT);
    	mv.addObject("pojo", pojo);
    	
    	List<KnowledgeCategory> catList = categoryService.list();
		mv.addObject("catList", categoryService.list());
		mv.addObject("uspList", uspService.list());
		
		// init_cat
		pojo.setCatId(catList.get(0).getCatId());
		pojo.setCatName(catList.get(0).getCatName());
		
		// init_recommend
		mv.addObject("starList", (List<String>)List.of("", "", ""));
		mv.addObject("noStarList", (List<String>)List.of("", ""));
		
		mv.addObject("action", "add");
		return mv;
	}
    
    public ModelAndView edit(ModelAndView mv, @RequestParam Integer id) {
    	mv.setViewName("repository/mobile/mobile/know");
    	Knowledge pojo = impl.get(id);
    	
    	if (Strings.isEmpty(pojo.getkContent())) {
    		pojo.setkContent(INIT_CONTENT);
    	}
    	mv.addObject("initContent", INIT_CONTENT);
		mv.addObject("pojo", pojo);
		mv.addObject("catList", categoryService.list());
		mv.addObject("uspList", uspService.list());
		
		if (Strings.isNotEmpty(pojo.getImgSrc())) {
			mv.addObject("imgList", pojo.getImgSrc().split(","));
    	}
		
		// init_recommend
		List<String> starList = new ArrayList<String>();
		for (int i = 0; i < pojo.getRecommend(); i++) {
			starList.add("");
		}
		mv.addObject("starList", starList);
		List<String> noStarList = new ArrayList<String>();
		for (int i = 0; i < 5 - pojo.getRecommend(); i++) {
			noStarList.add("");
		}
		mv.addObject("noStarList", noStarList);
		
		
		mv.addObject("action", "edit");
		return mv;
	}
    
    public Map<?, ?> insertOrUpdate(Knowledge pojo) {
    	if (pojo.getkId() == null) {
    		return impl.insert(pojo);
    	}
    	else {
    		return impl.update(pojo);
    	}
    }
    
    public Map<?, ?> delete(Integer id) {
    	return impl.delete(id);
    }
    
    public ModelAndView addView(ModelAndView mv, @RequestParam Integer id) {
    	mv.setViewName("repository/mobile/mobile/view");
    	mv.addObject("action", "readonly");
    	return view(mv, id);
    }
    
    public ModelAndView view(ModelAndView mv, @RequestParam Integer id) {
    	Knowledge pojo = impl.get(id);
    	
		mv.addObject("pojo", pojo);
		mv.addObject("uspList", uspService.list());
		
		if (Strings.isNotEmpty(pojo.getImgSrc())) {
			mv.addObject("imgList", pojo.getImgSrc().split(","));
    	}
		
		// init_recommend
		List<String> starList = new ArrayList<String>();
		for (int i = 0; i < pojo.getRecommend(); i++) {
			starList.add("");
		}
		mv.addObject("starList", starList);
		
		// favorite
		boolean isFavorite = impl.isFavorite(id);
		mv.addObject("isFavorite", isFavorite);
		
		int userId = AuthContext.getLoginAccount().getUserId();
		
		if (userId != pojo.getModifiedBy()) {
			mv.addObject("action", "readonly");
		}
		
		
		List<KnowledgeSubject> listSubject = subjectService.list();
		List<Integer> subjectIdList = subjectService.listSubjectId(id);
		
		
		for (KnowledgeSubject knowledgeSubject : listSubject) {
			for (Integer subjectId : subjectIdList) {
				if (subjectId.intValue() == knowledgeSubject.getSubjectId()) {
					knowledgeSubject.setChecked(true);
				}
			}
		}
		
		mv.addObject("subjectList", listSubject);
		return mv;
	}
    
    
    public Map<?, ?> insertSubjectMap(@RequestParam Integer subjectId, @RequestParam Integer kId) {
    	subjectService.insertSubjectMap(subjectId, kId);
    	return ReturnMap.of();
	}
    
    public Map<?, ?> deleteSubjectMap(@RequestParam Integer subjectId, @RequestParam Integer kId) {
    	subjectService.deleteSubjectMap(subjectId, kId);
    	return ReturnMap.of();
	}
    
    
    public ModelAndView nice(ModelAndView mv, Integer uspId) {
    	mv.addObject("list", niceSearch(new MPage(), uspId, null));
    	mv.addObject("uspId", uspId);
    	List<KnowledgeUsp> uspList = uspService.list();
    	if (uspId == null) {
    		mv.addObject("uspName", "全部UPS");
    	}
    	else {
    		for (KnowledgeUsp usp : uspList) {
    			if (uspId.intValue() == usp.getUspId()) {
    				mv.addObject("uspName", usp.getKey());
    				break;
    			}
    		}
    	}
    	
		mv.addObject("uspList", uspList);
		return mv;
	}
    
    public Map<?, ?> niceSearch(MPage page, Integer uspId, Integer recommend) {
    	if (uspId == null) {
    		return ReturnMap.of(page, impl.byRecommendSearch(page, recommend));
    	}
    	else {
    		return ReturnMap.of(page, impl.byUspSearch(page, uspId, recommend));
    	}
	}
    
    public ModelAndView subject(ModelAndView mv) {
    	List<KnowledgeSubject> listSubject = subjectService.list();
    	
    	if (listSubject.isEmpty()) {
    		mv.addObject("list", new ArrayList<KnowledgeSubject>());
    		mv.addObject("subjectId", -1);
        	mv.addObject("subjectName", "");
    	}
    	else {
    		// 默认第1个
    		mv.addObject("list", subjectSearch(new MPage(), listSubject.get(0).getSubjectId(), null));
    		mv.addObject("subjectId", listSubject.get(0).getSubjectId());
        	mv.addObject("subjectName", listSubject.get(0).getSubjectName());
    	}
    	
    	mv.addObject("uspList", uspService.list());
		mv.addObject("subjectList", listSubject);
		return mv;
	}
    
    public Map<?, ?> subjectSearch(MPage page, @RequestParam Integer subjectId, Integer orderBy) {
    	return ReturnMap.of(page, impl.bySubjectSearch(page, subjectId, orderBy));
	}
    
    public Map<?, ?> confirmFavorite(@RequestParam Integer kId) {
    	impl.confirmFavorite(kId);
    	return ReturnMap.of();
	}
    
    public Map<?, ?> cancelFavorite(@RequestParam Integer kId) {
    	impl.cancelFavorite(kId);
    	return ReturnMap.of();
	}
    
    public ModelAndView my(ModelAndView mv) {
    	int userId = AuthContext.getLoginAccount().getUserId();
    	RepoUser u = repoUserService.get(userId);
    	mv.addObject("u", u);
		return mv;
	}
    
    public ModelAndView favorite(ModelAndView mv) {
    	mv.addObject("uspList", uspService.list());
    	mv.addObject("list", favoriteSearch(new MPage(), null));
		mv.addObject("catList", categoryService.list());
		return mv;
	}
    
    public Map<?, ?> favoriteSearch(MPage page, Integer catId) {
		return ReturnMap.of(page, impl.favoriteSearch(page, catId));
	}
    
    public ModelAndView myKnow(ModelAndView mv) {
    	mv.addObject("list", myKnowSearch(new MPage(), null, null));
		mv.addObject("catList", categoryService.list());
		mv.addObject("uspList", uspService.list());
		return mv;
	}
    
    public Map<?, ?> myKnowSearch(MPage page, Integer catId, Integer recommend) {
		return ReturnMap.of(page, impl.myKnowSearch(page, catId, recommend));
	}
    
    // >>>>>>>>>>>>>>>>todo>>>>>>>>>>>>>>
    
    public ModelAndView todoList(ModelAndView mv) {
    	Todo todo = new Todo();
    	MPage page = new MPage();
    	page.setPageSize(16);
    	
    	// 1:待办
    	todo.setTodoStatus(1);
		mv.addObject("list", listTodo(page, todo));
		return mv;
	}
    
    public Map<?, ?> listTodo(MPage page, Todo todo) {
    	return ReturnMap.of(page, impl.todoList(page, todo));
	}
    
    public ModelAndView todo(ModelAndView mv, Integer id) {
    	Todo pojo = null; 
    	if (id != null) {
    		pojo = impl.getTodo(id);
    		mv.addObject("action", "edit");
    	}
    	else {
    		pojo = new Todo();
    		pojo.setTodoImportant(1);
    		pojo.setTodoEmergent(1);
    		mv.addObject("action", "add");
    	}
    	mv.addObject("pojo", pojo);
    	return mv;
    }
    
    public Map<?, ?> insertOrUpdateTodo(Todo todo) {
    	return impl.insertOrUpdateTodo(todo);
    }
    
    public Map<?, ?> deleteTodo(@RequestParam Integer id) {
    	return impl.deleteTodo(id);
    }
    
    // >>>>>>>>>>>>>>>>>about>>>>>>>>>>>>>>>>
    public ModelAndView about(ModelAndView mv) {
		return mv;
	}
    
    
    
    
}
