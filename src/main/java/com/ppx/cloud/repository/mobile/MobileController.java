package com.ppx.cloud.repository.mobile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.page.MPage;
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
		mv.addObject("list", homeSearch(new MPage(), null, null));
		mv.addObject("catList", categoryService.list());
		mv.addObject("uspList", uspService.list());
	
		return mv;
	}
    
    public Map<?, ?> homeSearch(MPage page, String word, Integer catId) {
    	
    	if (Strings.isBlank(word) && catId == null) {
    		return ReturnMap.of(page, impl.mAllList(page));
    	}
    	else {
    		return ReturnMap.of(page, impl.homeSearch(page, word, catId));
    	}
	}
    
    public ModelAndView add(ModelAndView mv) {
    	mv.setViewName("repository/mobile/mobile/know");
    	
    	Knowledge pojo = new Knowledge();
    	pojo.setkContent(INIT_CONTENT);
    	mv.addObject("initContent", INIT_CONTENT);
    	mv.addObject("pojo", pojo);
		mv.addObject("catList", categoryService.list());
		mv.addObject("uspList", uspService.list());
		
		// init_recommend
		mv.addObject("starList", (List<String>)List.of("", "", ""));
		mv.addObject("noStarList", (List<String>)List.of("", ""));		
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
		return mv;
	}
    
    
    public ModelAndView nice(ModelAndView mv, Integer uspId) {
    	mv.addObject("list", niceSearch(new MPage(), null, null));
    	mv.addObject("uspId", uspId);
		mv.addObject("uspList", uspService.list());
		return mv;
	}
    
    public Map<?, ?> niceSearch(MPage page, String word, Integer catId) {
    	
    	if (Strings.isBlank(word) && catId == null) {
    		return ReturnMap.of(page, impl.mAllList(page));
    	}
    	else {
    		return ReturnMap.of(page, impl.niceSearch(page, word, catId));
    	}
	}
    
    
}
