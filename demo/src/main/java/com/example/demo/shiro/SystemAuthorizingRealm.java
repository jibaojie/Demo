package com.example.demo.shiro;

import com.example.demo.entity.User;
import com.example.demo.http.CookieUtils;
import com.example.demo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.Md5CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统安全认证实现类
 * @author ThinkGem
 * @version 2013-5-29
 */
@Service("systemAuthorizingRealm")
public class SystemAuthorizingRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(SystemAuthorizingRealm.class);

//    private static final String HASH_ALGORITHM = "SHA-1";
    private static final String HASH_ALGORITHM = "MD5";
    private static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;

    @Autowired
    private UserService userService;

//    @Autowired
//    private RedisUtil redisUtil;

    /**
     * 认证回调函数, 登录时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

        HttpServletRequest request = WebUtils.getHttpRequest(SecurityUtils.getSubject());
        String cookieValue = CookieUtils.getCookieValue(request);
//        String validCode = getRedisUtil().get(cookieValue + "_" + LoginRestController.LOGIN_VALID_CODE);
//            if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(validCode)) {
//                throw new CaptchaException("验证码错误.");
//            }
//        getRedisUtil().remove(cookieValue + "_" + LoginRestController.LOGIN_VALID_CODE);

        User user = userService.getUserByUserId(token.getUserId());
        if (user == null) {
            return null;
        }
        return new SimpleAuthenticationInfo(new Principal(user), user.getPassword(), getName());
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal principal = (Principal) getAvailablePrincipal(principals);
        User user = userService.getUserByUserId(principal.getUserId());
        if (user != null) {
//            UserUtils.flushUserLoginTimeAndIp();
//            return UserUtils.getAuthInfo();
        } else {

        }
        return null;
    }
    /**
     * 设定密码校验的Hash算法与迭代次数
     */
    @PostConstruct
    public void initCredentialsMatcher() {
//        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Md5Hash.ALGORITHM_NAME);
//        matcher.setHashIterations(HASH_INTERATIONS);
        setCredentialsMatcher(new CustomCredentialsMatcher());
    }

    /**
     * 清空用户关联权限认证，待下次使用时重新加载
     */
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清空所有关联认证
     */
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }


    /**
     * 授权用户信息
     */
    public static class Principal implements Serializable {

        private static final long serialVersionUID = 1L;

        private int userId;
        private String loginName;
        private String name;
        private Map<String, Object> cacheMap;

        public Principal(User user) {
            this.userId = user.getUserId();
            this.loginName = user.getName();
            this.name = user.getName();
        }

        public int getUserId() {
            return userId;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getName() {
            return name;
        }

        public Map<String, Object> getCacheMap() {
            if (cacheMap == null) {
                cacheMap = new HashMap<String, Object>();
            }
            return cacheMap;
        }

    }
}
