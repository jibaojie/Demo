package com.example.demo.shiro;

import com.example.demo.util.MD5Util;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha384Hash;

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher{

	@Override   
	public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {    
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String accountCredentials = (String) getCredentials(info);
		accountCredentials = accountCredentials.toLowerCase();
		//  String pwd =encrypt32(String.valueOf(token.getPassword()));//md5 32位加密  
		String pwdType =String.valueOf(token.getPassword());// 判断一下密码是否是用户输入的，还是JCIS传过来的  
		if(pwdType.length() == 32){  
			return equals(pwdType, accountCredentials); //密码长度=32位，说明是md5加密过，是从xx传进来的 32位加密。  
		}
		String pwdUser = MD5Util.MD5Encode(String.valueOf(token.getPassword()), "utf-8");//不等于32 是用户输入的密码。 如果用户输入的密码长度位32那么里面会有一个bug
//		return equals(pwdUser, accountCredentials);
		//将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
		boolean b = equals(pwdUser, accountCredentials);
		return b;
		//return super.doCredentialsMatch(token, info) ;  
	}
	
}
