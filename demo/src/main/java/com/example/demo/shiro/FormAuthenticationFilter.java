/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.example.demo.shiro;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 表单验证（包含验证码）过滤类
 * @author ThinkGem
 * @version 2013-5-19
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

    public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
    public static final String DEFAULT_USERID_PARAM = "userId";

    private String captchaParam = DEFAULT_CAPTCHA_PARAM;
    private String userIdParam = DEFAULT_USERID_PARAM;

    public String getCaptchaParam() {
        return captchaParam;
    }

    public String getUserIdParam() {
		return userIdParam;
	}

	protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        // TODO 不知道什么原因，在Spring Boot应用中，无法初始化WebSubject
        WebSubject.Builder builder = new WebSubject.Builder(request, response);
        WebSubject webSubject = builder.buildWebSubject();
        ThreadContext.bind(webSubject);

        Integer userId = Integer.parseInt(getUserId(request));
        String username = getUsername(request);
        String password = getPassword(request);
        if (password == null) {
            password = "";
        }
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        String captcha = getCaptcha(request);
        return new UsernamePasswordToken(userId, username, password.toCharArray(), rememberMe, host, captcha);
    }

    private String getUserId(ServletRequest request) {
    	return WebUtils.getCleanParam(request, getUserIdParam());
	}
    
    
    
    

}