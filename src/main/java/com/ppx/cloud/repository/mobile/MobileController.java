package com.ppx.cloud.repository.mobile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.repository.category.KnowledgeCategory;
import com.ppx.cloud.repository.category.KnowledgeCategoryService;
import com.ppx.cloud.repository.knowledge.Knowledge;
import com.ppx.cloud.repository.knowledge.KnowledgeServiceImpl;
import com.ppx.cloud.repository.usp.KnowledgeUspService;

@Controller
public class MobileController {
	
	@Autowired
	private KnowledgeServiceImpl impl;
	
    private final static String INIT_CONTENT = "# \n* \n* ";
   
    @Autowired
    private KnowledgeCategoryService categoryService;
    @Autowired
    private KnowledgeUspService uspService;
    
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
		return mv;
	}
    
    public ModelAndView nice(ModelAndView mv, Integer uspId) {
    	mv.addObject("list", niceSearch(new MPage(), uspId, null));
    	mv.addObject("uspId", uspId);
		mv.addObject("uspList", uspService.list());
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
    
    public Map<?, ?> confirmFavorite(Integer kId) {
    
    	
    	impl.confirmFavorite(kId);
    	return ReturnMap.of();
	}
    
    public Map<?, ?> cancelFavorite(Integer kId) {
    	
    	impl.cancelFavorite(kId);
    	return ReturnMap.of();
	}
     
    public ModelAndView my(ModelAndView mv) {
		return mv;
	}
    
    public ModelAndView favorite(ModelAndView mv) {
    	
    	
    	
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
		return mv;
	}
    
    public Map<?, ?> myKnowSearch(MPage page, Integer catId, Integer recommend) {
		return ReturnMap.of(page, impl.myKnowSearch(page, catId, recommend));
	}
    
}
