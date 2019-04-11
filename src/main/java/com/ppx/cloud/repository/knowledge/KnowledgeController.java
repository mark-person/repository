package com.ppx.cloud.repository.knowledge;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.repository.category.KnowledgeCategoryService;
import com.ppx.cloud.repository.knowledge.pojo.Knowledge;

@Controller
public class KnowledgeController {
	
	@Autowired
	private KnowledgeServiceImpl impl;
	
	@Autowired
    private KnowledgeCategoryService categoryService;

	public ModelAndView knowledge(ModelAndView mv) {
		mv.addObject("list", list(new Page(), new Knowledge()));
		mv.addObject("catList", categoryService.list());
		return mv;
	}
	
	public Map<?, ?> list(Page page, Knowledge pojo) {
		return ReturnMap.of(page, impl.list(page, pojo));
	}
	
    public Map<?, ?> changeCat(@RequestParam Integer id, @RequestParam Integer catId) {
    	return impl.changeCat(id, catId);
    }
    
    public Map<?, ?> delete(@RequestParam Integer id) {
        return impl.delete(id);
    }
    
}
