package com.ppx.cloud.repository.knowledge;

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
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.repository.category.KnowledgeCategoryService;

@Controller
public class KnowledgeController {
	
	@Autowired
	private KnowledgeServiceImpl impl;
	

	public ModelAndView knowledge(ModelAndView mv) {
		mv.addObject("list", list(new Page(), new Knowledge()));
		mv.addObject("catList", categoryService.list());
		return mv;
	}
	
	public Map<?, ?> list(Page page, Knowledge pojo) {
		return ReturnMap.of(page, impl.list(page, pojo));
	}
	 
    public Map<?, ?> insertOrUpdate(Knowledge pojo) {
    	if (pojo.getkId() == null) {
    		return impl.insert(pojo);
    	}
    	else {
    		return impl.update(pojo);
    	}
    }
    
    public Map<?, ?> changeCat(@RequestParam Integer id, @RequestParam Integer catId) {
    	return impl.changeCat(id, catId);
    }
    
    private final static String INIT_CONTENT = "# \n* \n* ";
    
    public ModelAndView edit(ModelAndView mv, @RequestParam Integer id) {
    	mv.setViewName("repository/knowledge/knowledge/mAddKnowledge");
    	Knowledge pojo = impl.get(id);
    	if (Strings.isEmpty(pojo.getkContent())) {
    		pojo.setkContent(INIT_CONTENT);
    	}
		mv.addObject("pojo", pojo);
		mv.addObject("catList", categoryService.list());
		
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
    
    
    public Map<?, ?> get(@RequestParam Integer id) {
        return ReturnMap.of("pojo", impl.get(id));
    }
    
    
    public Map<?, ?> delete(@RequestParam Integer id) {
        return impl.delete(id);
    }
    
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    @Autowired
    private KnowledgeCategoryService categoryService;
    
    public ModelAndView mAddKnowledge(ModelAndView mv) {
    	Knowledge pojo = new Knowledge();
    	pojo.setkContent(INIT_CONTENT);
    	mv.addObject("pojo", pojo);
		mv.addObject("catList", categoryService.list());
		
		// init_recommend
		List<String> starList = List.of("", "", "");
		mv.addObject("starList", starList);
		List<String> noStarList = List.of("", "");
		mv.addObject("noStarList", noStarList);
		
		return mv;
	}

    
    public ModelAndView mListKnowledge(ModelAndView mv) {
		mv.addObject("list", mList(new MPage(), new Knowledge()));
		return mv;
	}
	
    public Map<?, ?> mList(MPage page, Knowledge pojo) {
		return ReturnMap.of(page, impl.mList(page, pojo));
	}
}
