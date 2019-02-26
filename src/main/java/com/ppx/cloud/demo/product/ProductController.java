package com.ppx.cloud.demo.product;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.exception.security.PermissionParamsException;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.demo.product.cat.CategoryService;

@Controller
public class ProductController {

	@Autowired
	private ProductServiceImpl impl;

	@Autowired
	private CategoryService catServ;

	private Set<Integer> USER_SET = Set.of(0, 100, 200);

	public ModelAndView product() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("list", list(new Page(), new Product()));

		return mv;
	}

	public Map<?, ?> list(Page page, Product pojo) {
		return ReturnMap.of(page, impl.list(page, pojo));
	}

	public ModelAndView addProduct(@RequestParam Integer u) {
		ModelAndView mv = new ModelAndView();
		if (!USER_SET.contains(u)) {
			throw new PermissionParamsException("用户ID:" + u + "错误");
		}

		mv.addObject("creator", u);
		mv.addObject("catList", catServ.listCategoy());
		return mv;
	}

	public Map<?, ?> insert(Product pojo, HttpServletRequest request) {
		pojo.setUserAgent(getUserAgent(request));
		return impl.insert(pojo);
	}
	
	private String getUserAgent(HttpServletRequest request) {
		String userAgent = (String) request.getHeader("user-agent");
		if (userAgent.indexOf("Android") > -1) {
			return "Android";
		} else if (userAgent.indexOf("iPhone") > -1) {
			return "iPhone";
		} else if (userAgent.indexOf("Windows") > -1 && userAgent.indexOf("Chrome") > -1) {
			return "Windows Chrome";
		}
		return "unknown";
	}
	

}
