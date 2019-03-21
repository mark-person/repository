package com.ppx.cloud.repository.knowledge;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.exception.security.PermissionParamsException;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.repository.category.KnowledgeCategoryService;

@Controller
public class KnowledgeController {
	
	@Autowired
	private KnowledgeServiceImpl impl;
	

	public ModelAndView knowledge(ModelAndView mv) {
		
		mv.addObject("list", list(new Page(), new Knowledge()));
		
		return mv;
	}
	
	public Map<?, ?> list(Page page, Knowledge pojo) {
		return ReturnMap.of(page, impl.list(page, pojo));
	}
	 
    public Map<?, ?> insert(Knowledge pojo) {
        return impl.insert(pojo);
    }
    
    
    public Map<?, ?> get(@RequestParam Integer id) {
        return ReturnMap.of("pojo", impl.get(id));
    }
    
    public Map<?, ?> update(Knowledge pojo) {
        return impl.update(pojo);
    }
    
    public Map<?, ?> delete(@RequestParam Integer id) {
        return impl.delete(id);
    }
    
    
    // >>>>>>>>>>>>>>>>>>
    
    @Autowired
    private KnowledgeCategoryService categoryService;
    
    public ModelAndView mAddKnowledge(ModelAndView mv) {
    	
		mv.addObject("catList", categoryService.list());
		return mv;
	}

	
}
