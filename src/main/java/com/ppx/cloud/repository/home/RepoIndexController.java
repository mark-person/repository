package com.ppx.cloud.repository.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RepoIndexController {
	
	
    @GetMapping(value="/")
    public ModelAndView edit(ModelAndView mv) {
    	
    	
    	
    	mv.setViewName("repository/home/index");
    	
		return mv;
	}
    
    
}
