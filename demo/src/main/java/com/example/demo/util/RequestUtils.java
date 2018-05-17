package com.example.demo.util;

import com.example.demo.config.GlobalConfig;
import com.example.demo.exception.LoginInvalidException;
import com.example.demo.http.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version 2017-05-04.
 * @auth Licw
 * 缓存当前用户信息
 */
public class RequestUtils {
    public static final String LOGIN_USER = "LOGIN_USER";	//login_user
    public static final String LOGIN_USER_ID = "LOGIN_USER_ID";	//login_user_id
    public static final String LOGIN_SUPPLIER_ID = "LOGIN_SUPPLIER_ID";	//login_supplier_id
    public static final String LOGIN_SUPPLIER = "LOGIN_SUPPLIER";//login_supplier
    public static final String LOGIN_USER_NAME = "LOGIN_USER_NAME";//login_user_name
    public static final String LOGIN_ROLE_ID = "LOGIN_ROLE_ID";//login_role_id
    //不可以使用@Autowired，static方法找不到redisUtil
    private static RedisUtil redisUtil = SpringContextHolder.getBean(RedisUtil.class);

    /**
     * 获取客户端IP地址
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (StringUtils.isEmpty(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }


    public static String getLoginUserId(HttpServletRequest request) {
        return getCacheValue(CookieUtils.getCookieValue(request) + "_" + LOGIN_USER_ID);
    }

    public static String getLoginSupplierId(HttpServletRequest request) {
        return getCacheValue(CookieUtils.getCookieValue(request) + "_" + LOGIN_SUPPLIER_ID);
    }

    public static String getLoginRoleId(HttpServletRequest request) {
        return getCacheValue(CookieUtils.getCookieValue(request) + "_" + LOGIN_ROLE_ID);
    }

    public static String getLoginUserName(HttpServletRequest request) {
        return getCacheValue(CookieUtils.getCookieValue(request) + "_" + LOGIN_USER_NAME);
    }

    private static String getCacheValue(String key) {
        String value = redisUtil.get(key);
        if (StringUtils.isEmpty(value)) {
            throw new LoginInvalidException();
        }
        redisUtil.expire(key, GlobalConfig.getCookieExpireTime());
        return value;
    }

    public static void cacheCookieInfo(String key, Object value) {
        redisUtil.set(key, value, GlobalConfig.getCookieExpireTime());
    }

    public static void clearAll(HttpServletRequest request) {
        String sessionId = CookieUtils.getCookieValue(request);
        redisUtil.remove(sessionId + "_" + LOGIN_USER,
                sessionId + "_" + LOGIN_USER_ID,
                sessionId + "_" + LOGIN_USER_NAME,
                sessionId + "_" + LOGIN_ROLE_ID,
                sessionId + "_" + LOGIN_SUPPLIER_ID,
                sessionId + "_" + LOGIN_SUPPLIER);
    }

    public static void writeExcelBytesToResponse(HttpServletResponse response, byte[] bytes, String fileName) throws IOException {
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(fileName));
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
    }
}
