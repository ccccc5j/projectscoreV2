package javadev.core.common;

/**
 * 此ACTION只是为实现页面之间的简单跳转而设计
 * @author 陈建
 *
 */
//public class CommonForwardAction extends CommonAnonymousAction {//不限制登录
public class CommonForwardAction extends CommonBaseAction {//限制登录
	private String nextUrl;// 需要简单跳转的下一个URL

	public String getNextUrl() {
		return nextUrl;
	}
	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}	
}
