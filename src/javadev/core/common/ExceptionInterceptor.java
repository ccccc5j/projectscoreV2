package javadev.core.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javadev.core.Constants;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class ExceptionInterceptor implements Interceptor {

	protected final Log log = LogFactory.getLog(getClass());

	public void destroy() {
	}

	public void init() {
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		try {
			return invocation.invoke();
		} catch (Exception e) {	
			handleException(invocation, e);
			return Constants.EXCEPTION;
		}		
	}

	private void handleException(ActionInvocation invocation, Exception e) {
		String message = "在执行"+ invocation.getInvocationContext().getName()+".action时发生异常：";
		log.error(message, e);

		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		invocation.getInvocationContext().put(Constants.CONTEXT_EXCEPTION,
				message+e.toString());
		invocation.getInvocationContext().put(Constants.CONTEXT_EXCEPTION_INFO,
				sw.toString());
	}

}
