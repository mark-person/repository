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
import com.ppx.cloud.repository.usp.KnowledgeUspService;

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
    
    
    public Map<?, ?> get(@RequestParam Integer id) {
        return ReturnMap.of("pojo", impl.get(id));
    }
    
    
    public Map<?, ?> delete(@RequestParam Integer id) {
        return impl.delete(id);
    }
    
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    @Autowired
    private KnowledgeCategoryService categoryService;
    @Autowired
    private KnowledgeUspService uspService;
    
   
}
