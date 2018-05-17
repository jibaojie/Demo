package com.example.demo.shiro;

import com.example.demo.config.GlobalConfig;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration implements EnvironmentAware {

    @Override
    public void setEnvironment(Environment env) {
        // 用Autowire的方式注入不了restPath
        GlobalConfig.setRestApiPath(env.getProperty("restPath"));
    }

    @Bean(name = "ehCacheManagerFactoryBean")
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("cache/ehcache-local.xml");
        ehCacheManagerFactoryBean.setConfigLocation(classPathResource);
        return ehCacheManagerFactoryBean;
    }

    @Bean(name = "shiroCacheManager")
    @DependsOn({"ehCacheManagerFactoryBean"})
    public EhCacheManager shiroCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
        return ehCacheManager;
    }
	
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        shiroFilterFactoryBean.setLoginUrl(GlobalConfig.getRestApiPath() + "/hello/login");
        shiroFilterFactoryBean.setSuccessUrl("/index.html");
        shiroFilterFactoryBean.setUnauthorizedUrl("/login.html");//未经授权的页面
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("authc", new FormAuthenticationFilter());//配置为基于表单认证的过滤器
        filterMap.put("role", new RoleAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        /**
         * anon 表示匿名访问（不需要认证与授权）
         * authc 表示需要认证
         * perms[SECURITY_ACCOUNT_VIEW] 表示用户需要提供值为“SECURITY_ACCOUNT_VIEW”Permission 信息
         * 连接地址配置为 authc 或 perms[XXX] 表示为受保护资源
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //swagger相关不拦截
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");//加载api
        //登录 注册页面，获取验证码，静态资源不拦截
        filterChainDefinitionMap.put(GlobalConfig.getRestApiPath() + "/login", "anon");
        filterChainDefinitionMap.put(GlobalConfig.getRestApiPath() + "/hello/login", "anon");
        filterChainDefinitionMap.put(GlobalConfig.getRestApiPath() + "/verifyCode", "anon");
        filterChainDefinitionMap.put(GlobalConfig.getRestApiPath() + "/user/supplierRegistry/*", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        //api路径下拦截
        filterChainDefinitionMap.put(GlobalConfig.getRestApiPath() + "/**", "user");
        filterChainDefinitionMap.put("/index.html", "user");
        filterChainDefinitionMap.put("/**/**", "user");

        filterChainDefinitionMap.put("/**", "user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    
    @Bean(name = "securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(systemAuthorizingRealm());
        defaultWebSecurityManager.setCacheManager(shiroCacheManager());

        SecurityUtils.setSecurityManager(defaultWebSecurityManager);

        return defaultWebSecurityManager;
    }

    @Bean
    public SystemAuthorizingRealm systemAuthorizingRealm() {
        return new SystemAuthorizingRealm();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

}
