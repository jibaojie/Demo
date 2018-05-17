package com.example.demo.util;

import com.example.demo.config.GlobalConfig;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.shiro.SystemAuthorizingRealm;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 封装关于获取User的方法
 */
public class UserUtils {

    public static final String CURRENT_USER = "currentUser";
    public static final String CACHE_ROLE_LIST = "roleList";
    public static final String CACHE_MENU_LIST = "menuList";
    public static final String CACHE_MENU_TREE = "menuTree";
    public static final String CACHE_AREA_LIST = "areaList";
    public static final String CACHE_OFFICE_LIST = "officeList";
    public static final  String LOGIN_FAIL_COUNT = "LOGIN_FAIL_COUNT";
    private static final Long LOGIN_FAIL_COUNT_LIMIT_TIME = 60 * 10L;

    //不可以使用@Autowired，static方法找不到userService
    private static UserService userService = SpringContextHolder.getBean(UserService.class);

//    private static SysMenuEOService sysMenuService = SpringContextHolder.getBean(SysMenuEOService.class);
//    private static SysRoleEOService sysRoleEOService = SpringContextHolder.getBean(SysRoleEOService.class);
    private static RedisUtil redisUtil = SpringContextHolder.getBean(RedisUtil.class);

    /**
     * 退出
     */
    public static void logout() {
        try {
            SecurityUtils.getSubject().logout();
        } catch (UnavailableSecurityManagerException e) {
            e.printStackTrace();
        } catch (InvalidSessionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在shiro中获取当前登录用户ID
     *
     */
    public static int getUserId() {
    	Integer userId = 0;
        try {
            Subject subject = SecurityUtils.getSubject();
            SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) subject.getPrincipal();
            if (principal != null) {
                userId = principal.getUserId();
            }
        } catch (UnavailableSecurityManagerException e) {
        } catch (InvalidSessionException e) {
        }
        return userId;
    }

    /**
     * 获取当前登录用户信息
     *
     */
    public static User getUser() {
        User user = (User) CacheUtils.getCache(CURRENT_USER);
        if (user == null) {
            int userId = getUserId();
            User userInDb = userService.getUserByUserId(userId);
            user = ObjectUtils.clone(userInDb);
            user.setPassword(null);
            //将当前用户缓存到CacheUtils
            CacheUtils.putCache(CURRENT_USER, user);
        }
        return user;
    }

    public static void updateUserInfo(User userInfoEO) {
        User user = (User) CacheUtils.getCache(CURRENT_USER);
        if (user != null) {
        	int userId = getUserId();

            User userInDb = userService.getUserByUserId(userId);
            userInDb.setName(userInfoEO.getName());
//                userInDb.setIdentityNumber(userInfoEO.getIdentityNumber());

            userService.insert(userInDb);

            CacheUtils.removeCache(CURRENT_USER);
        }
    }

    /**
     * 获取当前登录用户角色列表
     *
     */
//    public static List<SysRoleEO> getRoleList() {
//        List<SysRoleEO> roleList = (List<SysRoleEO>) CacheUtils.getCache(CACHE_ROLE_LIST);
//        if (roleList == null) {
//            UserInfoEO user = getUser();
////            if(user != null) {
////                roleList = sysRoleEOService.getSysRoleListByUserId(user.getUserId());
////            }
//            CacheUtils.putCache(CACHE_ROLE_LIST, roleList);
//        }
//        return roleList;
//    }

//    public static String getRoleIds() {
//        List<SysRoleEO> roleList = getRoleList();
//        if (CollectionUtils.isEmpty(roleList)) {
//            return "";
//        }
//        StringBuilder roleIds = new StringBuilder();
//        for (SysRoleEO sysRoleEO : roleList) {
//            roleIds.append(sysRoleEO.getId()).append(",");
//        }
//        return roleIds.substring(0, roleIds.length() - 1);
//    }


    /**
     * 获取当前登录用户菜单列表
     *
     */
//    public static List<SysMenuEO> getMenuList() {
//        List<SysMenuEO> menuList = (List<SysMenuEO>) CacheUtils.getCache(CACHE_MENU_LIST);
//        if (menuList == null) {
//            UserInfoEO user = getUser();
//            if (user == null) {
//                return null;
//            }
//
//            if (isAdmin(user)) {
//                menuList = sysMenuService.findAll();
//            } else {
//                menuList = sysMenuService.listSysMenuEOByUserId(user.getUserId());
//            }
//            CacheUtils.putCache(CACHE_MENU_LIST, menuList);
//        }
//        return menuList;
//    }

    /**
     * 获取当前登录用户菜单树
     *
     */
//    public static SysMenuEO getMenuTree() {
//        SysMenuEO menu = (SysMenuEO) CacheUtils.getCache(CACHE_MENU_TREE);
//        if (menu == null) {
//            List<SysMenuEO> sysMenuEOList = getMenuList();
//            if (sysMenuEOList != null) {
//                menu = sysMenuService.organizeListAsTree(sysMenuService.get(1), sysMenuEOList);
//                CacheUtils.putCache(CACHE_MENU_TREE, menu);
//            }
//        }
//        return menu;
//    }

    /**
     * 判断用户是否是超级管理员
     * @param userInfoEO
     * @return
     */
    public static boolean isAdmin(User userInfoEO) {
        return userInfoEO != null && userInfoEO.getUserId() == 1;
    }

    /**
     * 获取用户菜单权限信息
     * @return
     */
//    public static SimpleAuthorizationInfo getAuthInfo() {
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        List<SysMenuEO> list = UserUtils.getMenuList();
//        for (SysMenuEO menu : list) {
//            if (StringUtils.isNotBlank(menu.getPermission())) {
//                // 添加基于Permission的权限信息
//                for (String permission : StringUtils.split(menu.getPermission(), ",")) {
//                    info.addStringPermission(permission);
//                }
//            }
//        }
//        return info;
//    }

    /**
     * 更新用户的登录时间以及登录IP
     */
    public static void flushUserLoginTimeAndIp() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            String loginIp = SecurityUtils.getSubject().getSession().getHost();
//            userService.updateUserLoginInfo(UserUtils.getUserId(), loginIp);
        }
    }

    public static void flush() {
        CacheUtils.removeCache(CURRENT_USER);
        CacheUtils.removeCache(CACHE_MENU_LIST);
        CacheUtils.removeCache(CACHE_MENU_TREE);
    }

    /**
     * 判断是否需要验证验证码
     *
     * @param userId
     * @return
     */
    public static boolean isNeedValidCode(Integer userId) {
        Integer loginFailNum = redisUtil.get(LOGIN_FAIL_COUNT + "_" + userId);
        if (loginFailNum == null) {
            loginFailNum = 0;
        }

        return loginFailNum >= GlobalConfig.getMaxLoginErrorCount();
    }


    /**
     * 登录失败次数增加一次
     *
     * @param userId
     * @return
     */
    public static void increaseLoginErrorCount(Integer userId) {
        Integer loginFailNum = redisUtil.get(LOGIN_FAIL_COUNT + "_" + userId);
        if (loginFailNum == null) {
            loginFailNum = 0;
        }

        loginFailNum++;
        redisUtil.set(LOGIN_FAIL_COUNT + "_" + userId, loginFailNum, LOGIN_FAIL_COUNT_LIMIT_TIME);
    }


    /**
     * shiro缓存
     */
    private static final class CacheUtils {
        public static Object getCache(String key) {
            return getCache(key, null);
        }

        public static Object getCache(String key, Object defaultValue) {
            Object obj = getCacheMap().get(key);
            return obj == null ? defaultValue : obj;
        }

        public static void putCache(String key, Object value) {
            getCacheMap().put(key, value);
        }

        public static void removeCache(String key) {
            getCacheMap().remove(key);
        }

        public static Map<String, Object> getCacheMap() {
            Map<String, Object> map = Maps.newHashMap();
            try {
                Subject subject = SecurityUtils.getSubject();
                SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) subject.getPrincipal();
                return principal != null ? principal.getCacheMap() : map;
            } catch (UnavailableSecurityManagerException e) {
                e.printStackTrace();
            } catch (InvalidSessionException e) {
                e.printStackTrace();
            }
            return map;
        }
    };

}
