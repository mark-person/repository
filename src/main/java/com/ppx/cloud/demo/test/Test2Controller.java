package com.ppx.cloud.demo.test;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.contoller.ReturnMap;

@Controller
public class Test2Controller {

	
	@Autowired
	private Test2ServiceImpl impl2;

	
	public ModelAndView testJson(ModelAndView mv) {
		RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>("", new HttpHeaders());
        String url = "http://localhost/auto/config/test";
        ResponseEntity<Map> result = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        System.out.println("99999999:" + result.getBody().get("errcode"));
		
		return mv;
	}
	
	public Map<?, ?> getJson2() {
		return ReturnMap.of();
	}
	
	public Map<?, ?> getJson(@RequestBody Test test) {
		System.out.println("...........a:" + test.getTestId());
		return ReturnMap.of();
	}
    
    public Map<?, ?> test2() {
    	impl2.test();
		return ReturnMap.of();
		
    }
	    
}
