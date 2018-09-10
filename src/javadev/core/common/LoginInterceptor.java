package javadev.core.common;

import javadev.core.Constants;
import javadev.core.util.QueryUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 登录拦截器，用于阻止未登录用户访问系统
 */
public class LoginInterceptor implements Interceptor {
	protected final Log log = LogFactory.getLog(getClass());

	public void destroy() {
	}

	public void init() {
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		Action action = (Action) invocation.getAction();
		String actionName = invocation.getInvocationContext().getName();
		//这里要求实现了Anonymous接口的Action以及固定的login和logoutAction可以跳过登录拦截
		//TODO:Action名是否可改为从配置文件读取
		if (action instanceof Anonymous || "login".equals(actionName) || "logout".equals(actionName)) {
			return invocation.invoke();
		} else {
			if (ActionContext.getContext().getSession().get(Constants.LOGIN_USER) != null) {
				return invocation.invoke();
			}
		}

		ActionContext.getContext().getSession().put(Constants.ORIGINAL_URL,
				QueryUtil.getRequestURL(ServletActionContext.getRequest()));
		return Action.LOGIN;
	}

}
