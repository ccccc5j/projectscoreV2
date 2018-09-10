package javadev.iip.service;

import java.util.Map;

import javadev.core.bean.BeanManager;
import javadev.core.common.CommonBaseService;

public class BaseService extends CommonBaseService {
	/* 获取通用的 ServiceManager */
	public ServiceManager getServMgr() {
		return (ServiceManager) BeanManager.getBean("serviceManager");
	}
	/**
	 * 全角字符转半角字符
	 * 
	 * @author shihanfa
	 * @version 创建时间：2013-1-11 上午11:55:34
	 */
	public String toDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
}
