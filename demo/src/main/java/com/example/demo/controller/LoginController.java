package com.example.demo.controller;

import com.example.demo.config.GlobalConfig;
import com.example.demo.exception.CaptchaException;
import com.example.demo.http.CookieUtils;
import com.example.demo.http.ResponseMessage;
import com.example.demo.http.Result;
import com.example.demo.service.UserService;
import com.example.demo.shiro.UsernamePasswordToken;
import com.example.demo.util.RedisUtil;
import com.example.demo.util.RequestUtils;
import com.example.demo.util.UserUtils;
import com.example.demo.validatecode.IVerifyCodeGen;
import com.example.demo.validatecode.SimpleCharVerifyCodeGenImpl;
import com.example.demo.validatecode.VerifyCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;


/**
 * Created by jibaojie on 2017/7/12.
 */
@Validated//可以接收页面form 表单里面的数据
@RestController
@RequestMapping(value="${restPath}")//映射路径
@Api(description = "登录接口")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    public static final String LOGIN_VALID_CODE = "LOGIN_VALID_CODE";

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 验证码
     *
     * @param response
     * @param request
     */
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 28);

            String cookieValue = CookieUtils.getCookieValueIfNullThenSetCookie(request, response);
            redisUtil.set(cookieValue + "_" + LOGIN_VALID_CODE, verifyCode.getCode(), GlobalConfig.getRegistryCodeExpireTime());

            request.getSession().setAttribute("VerifyCode", verifyCode.getCode());
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     * 1，进行登录验证
     * 2，RedisUtil 缓存当前用户信息
     *
     * @param request
     * @param response
     * @param userId
     * @param password
     * @param isRememberMe
     * @param verifyCode
     * @return
     */
    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseMessage loginRest(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam @NotNull(message = "请输入用户名") Integer userId,
                                     @RequestParam @NotNull(message = "请输入密码") String password,
                                     @RequestParam(value = "isRememberMe", defaultValue = "false") Boolean isRememberMe,
                                     String verifyCode) {
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userId, null,  password.toCharArray(), verifyCode);
            subject.login(usernamePasswordToken);


            String cookieValue = CookieUtils.setCookie(request, response);
            RequestUtils.cacheCookieInfo(cookieValue + "_" + RequestUtils.LOGIN_USER, UserUtils.getUser());
            RequestUtils.cacheCookieInfo(cookieValue + "_" + RequestUtils.LOGIN_USER_ID, UserUtils.getUserId());
            RequestUtils.cacheCookieInfo(cookieValue + "_" + RequestUtils.LOGIN_USER_NAME, UserUtils.getUser().getName());
//            RequestUtils.cacheCookieInfo(cookieValue + "_" + RequestUtils.LOGIN_ROLE_ID, UserUtils.getRoleIds());
        } catch (CaptchaException e) {
            logger.info("验证码验证失败");
            return Result.error("0002", "您输入的验证码不正确");
        } catch (UnknownAccountException e) {
            UserUtils.increaseLoginErrorCount(userId);
            logger.info("用户[{}]身份验证失败", userId);
            boolean isNeedValidCode = UserUtils.isNeedValidCode(userId);
            return Result.error("0001", "您输入的帐号或密码有误", isNeedValidCode);
        } catch (IncorrectCredentialsException e) {
            UserUtils.increaseLoginErrorCount(userId);
            logger.info("用户[{}]密码验证失败", userId);
            return Result.error("0001", "您输入的帐号或密码有误");
        } catch (AuthenticationException e) {
            // 记录日志，有未处理的验证失败
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }

        return Result.success();
    }

    @ApiOperation(value = "退出登录")
    @GetMapping("/logout")
    @ResponseBody
    public ResponseMessage logout(HttpServletRequest request, HttpServletResponse response) {
        RequestUtils.clearAll(request);
        CookieUtils.removeCookie(request, response);
        UserUtils.logout();
        return Result.success();
    }

}
