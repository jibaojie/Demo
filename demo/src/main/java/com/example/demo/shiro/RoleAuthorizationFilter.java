package com.example.demo.shiro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.http.ResponseMessage;
import com.example.demo.http.Result;
import net.sf.json.JSONObject;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;


@Service
public class RoleAuthorizationFilter extends AuthorizationFilter  {
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		
		Subject subject = getSubject(request, response);  
		String[] rolesArray = (String[]) mappedValue;  
		
//		if (rolesArray == null || rolesArray.length == 0) {  
//			// no roles specified, so nothing to check - allow access.  
//			return true;  
//		}  
		if(subject.getPrincipal() != null || isLoginRequest(request, response)){
			 return true;
		}
		
		
		
//		Set<String> roles = CollectionUtils.asSet(rolesArray);  
//		
//		for (String role : roles) {  
//			if (subject.hasRole(role)) {  
//				return true;  
//			}  
//		}  
		return false;  
	}
	
	//isAccessAllowed 返回false时调用
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {  
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;  
		HttpServletResponse httpResponse = (HttpServletResponse) response;  
		
		Subject subject = getSubject(request, response);  
		
		if (subject.getPrincipal() == null) {  
			if (isAjax(httpRequest)) {  
				out(response, Result.error("403", "您尚未登录或登录时间过长,请重新登录!"));
			} else {  
//				out(response, Result.error("403", "您尚未登录或登录时间过长,请重新登录!"));
				String unauthorizedUrl = getUnauthorizedUrl();  
				WebUtils.issueRedirect(request, response, unauthorizedUrl);  
//				saveRequestAndRedirectToLogin(request, response);  
			}  
		} else {  
			if (isAjax(httpRequest)) {  
//				com.silvery.utils.WebUtils.sendJson(httpResponse, JsonUtils.toJSONString(new ViewResult(false,  
//						"您没有足够的权限执行该操作!")));  
				out(response, Result.error("403", "您没有足够的权限执行该操作!"));
			} else {  
				String unauthorizedUrl = getUnauthorizedUrl();  
				if (StringUtils.hasText(unauthorizedUrl)) {  
					WebUtils.issueRedirect(request, response, unauthorizedUrl);  
				} else {  
					WebUtils.toHttp(response).sendError(401);  
				}  
			}  
		}  
		return false;  
	}  
	
	/**
	 * 是否是Ajax请求
	 * @param request
	 * @return
	 */
	public boolean isAjax(ServletRequest request){
		String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
		if("XMLHttpRequest".equalsIgnoreCase(header)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 *  使用	response 输出JSON
	 * @param hresponse
	 * @param resultMap
	 * @throws IOException
	 */
	public static void out(ServletResponse response, ResponseMessage responseMessage){
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");//设置编码
			response.setContentType("application/json");//设置返回类型
			out = response.getWriter();
			out.println(JSONObject.fromObject(responseMessage).toString());//输出
		} catch (Exception e) {
//			LoggerUtils.fmtError(CLAZZ, e, "输出JSON报错。");
			e.printStackTrace();
		}finally{
			if(null != out){
				out.flush();
				out.close();
			}
		}
	}
	
	
}
