package com.example.demo.controller;

import com.example.demo.shiro.UsernamePasswordToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.RedirectView;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jibaojie on 2017/7/7.
 * @RestController可以返回json相当于Controller在加ResponseBody
 */
@RestController
@Api(description = "测试相关" )
@RequestMapping(value = "${restPath}/hello")
public class HelloController {

    @RequestMapping("/hello")
    @ApiOperation(value="测试hello world", notes="")
    public String index() {
        return "Hello World";
    }

    @RequestMapping("/login")
    @ApiOperation(value="测试hello world", notes="")
    public String testHelloworld(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam @NotNull(message = "请输入用户名") Integer userId,
                               @RequestParam @NotNull(message = "请输入密码") String password,
                               @RequestParam(value = "isRememberMe", defaultValue = "false") Boolean isRememberMe,
                               String verifyCode) {

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userId, null,  password.toCharArray(), verifyCode);

        try {
            //4、登录，即身份验证
            subject.login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            //5、身份验证失败
        }
        System.out.println(subject.isAuthenticated());
        //获取session
        Session session = subject.getSession();

        Serializable id = session.getId();

        String host = session.getHost();

        Date start = session.getStartTimestamp();
        Date lastTime = session.getLastAccessTime();

//        session.touch();//更新最后访问时间

        //6、退出
//        subject.logout();

        if(subject.isAuthenticated()){//登录成功
            return "redirect:/member/index";
        } else {

        }
        return "";
    }


}
