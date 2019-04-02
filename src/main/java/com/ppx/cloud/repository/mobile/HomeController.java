package com.ppx.cloud.repository.mobile;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppx.cloud.auth.config.AuthUtils;
import com.ppx.cloud.common.util.CookieUtils;

@Controller
public class HomeController {
	
	
	@GetMapping("/m")
	public ModelAndView mIndex(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Algorithm algorithm = Algorithm.HMAC256(AuthUtils.getJwtPassword());
		String token = JWT.create().withIssuedAt(new Date()).withClaim("accountId", -1)
				.withClaim("loginAccount", "admin").withClaim("userId", -1)
				.withClaim("userName", "admin").withClaim("modified", new Date().getTime())
				.sign(algorithm);
		CookieUtils.setCookie(response, AuthUtils.PPXTOKEN, token);
		
		response.setHeader("Content-Type", "text/html; charset=utf-8");
		request.getRequestDispatcher("auto/mobile/home").include(request, response);
		return null;
	}
	
	

	
}
