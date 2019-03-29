package com.ppx.cloud.repository.mobile;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppx.cloud.auth.common.AuthContext;
import com.ppx.cloud.auth.common.LoginAccount;
import com.ppx.cloud.auth.config.AuthUtils;
import com.ppx.cloud.common.util.CookieUtils;

@Controller
public class HomeController {
	
	
	@GetMapping("/index")
	public ModelAndView mIndex(ModelAndView mv, HttpServletResponse response) {
		
		mv.setViewName("repository/mobile/home/mIndex");
		
		LoginAccount account = AuthContext.getLoginAccount();
		if (account == null) {
			// modified用来校验帐号或密码被修改
			Algorithm algorithm = Algorithm.HMAC256(AuthUtils.getJwtPassword());
			String token = JWT.create().withIssuedAt(new Date()).withClaim("accountId", -1)
					.withClaim("loginAccount", "admin").withClaim("userId", -1)
					.withClaim("userName", "admin").withClaim("modified", new Date().getTime())
					.sign(algorithm);
			CookieUtils.setCookie(response, AuthUtils.PPXTOKEN, token);
		}
		
		
		return mv;
	}
	

	
}
