package com.ppx.cloud.demo.test;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.page.Page;

@Controller
public class TestController {
	
	// private Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private TestServiceImpl impl;
	
	public ModelAndView t1(ModelAndView mv) {
		return mv;
	}
	
	public ModelAndView t2(ModelAndView mv) {
		try {
			Thread.sleep(5*1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mv;
	}

	public ModelAndView test(ModelAndView mv) {
		
		// logger.debug("99999999");
		// System.out.println("xxxxx111:" + MonitorSwitchConfig.IS_DEBUG);
		
		
		
		mv.addObject("list", list(new Page(), new Test()));
		
		var m0 = Map.of("typeId", 0, "typeName", "type0");
		var m1 = Map.of("typeId", 1, "typeName", "type1");
		mv.addObject("listType", Arrays.asList(m0, m1)); 
		
		return mv;
	}
	
	public Map<?, ?> list(Page page, Test pojo) {
		return ReturnMap.of(page, impl.list(page, pojo));
	}
	 
    public Map<?, ?> insert(Test pojo) {
        return impl.insert(pojo);
    }
    
    
    public Map<?, ?> get(@RequestParam Integer id) {
        return ReturnMap.of("pojo", impl.get(id));
    }
    
    public Map<?, ?> update(Test pojo) {
        return impl.update(pojo);
    }
    
    public Map<?, ?> delete(@RequestParam Integer id) {
        return impl.delete(id);
    }

	
}
