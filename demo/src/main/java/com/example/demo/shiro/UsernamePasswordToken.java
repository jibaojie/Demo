package com.example.demo.shiro;

/**
 * Created by jibaojie on 2017/7/12.
 */
public class UsernamePasswordToken  extends org.apache.shiro.authc.UsernamePasswordToken {

    private String captcha;
    private Integer userId;

    public UsernamePasswordToken() {
        super();
    }

    public UsernamePasswordToken(String username, char[] password) {
        super(username, password);
    }

    public UsernamePasswordToken(Integer userId, String username, char[] password) {

        super(username, password);
        this.userId = userId;
    }

    public UsernamePasswordToken(String username, char[] password, String captcha) {
        super(username, password);
        this.captcha = captcha;
    }

    public UsernamePasswordToken(Integer userId, String username, char[] password, String captcha) {
        super(username, password);
        this.captcha = captcha;
        this.userId = userId;
    }

    public UsernamePasswordToken(String username, char[] password, boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
    }

    public UsernamePasswordToken(Integer userId, String username, char[] password, boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.userId = userId;
    }



    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}
