package com.ppx.cloud.repository.mobile;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppx.cloud.auth.config.AuthUtils;
import com.ppx.cloud.common.util.CookieUtils;

@Controller
public class HomeController {
	
	@GetMapping("/admin")
	public void adminIndex(HttpServletResponse response) throws Exception {
		go(response, -1, null);
	}
	
	private Map<String, Integer> uMap = Map.of("guest", -2, 
			"xinyi", -3, "yingying", -4);
	
	@GetMapping("/m")
	public void m(HttpServletResponse response, String u) throws Exception {
		if (u == null || uMap.containsKey(u) == false) {
			response.setHeader("Content-Type", "text/html; charset=utf-8");
			try (PrintWriter pw = response.getWriter();) {
				pw.write("用户" + u + "不存在");
			}
		}
		else {
			String args = null;
			if ("xinyi".equals(u) || "yingying".equals(u)) {
				// 5:产品
				args = "5";
			}
			go(response, uMap.get(u), args);
		}
	}
	
	
	private void go(HttpServletResponse response, int repoUserId, String args) throws Exception {
		Algorithm algorithm = Algorithm.HMAC256(AuthUtils.getJwtPassword());
		String token = JWT.create().withIssuedAt(new Date()).withClaim("accountId", repoUserId)
				.withClaim("loginAccount", "admin").withClaim("userId", repoUserId)
				.withClaim("userName", "admin").withClaim("modified", new Date().getTime()).withClaim("args", args)
				.sign(algorithm);
		CookieUtils.setCookie(response, AuthUtils.PPXTOKEN, token);
		response.sendRedirect("auto/mobile/home");
	}
	
	

	
}
