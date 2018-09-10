package javadev.iip.service;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ServiceManager {
	protected final Log log = LogFactory.getLog(getClass());

	/* 项目所用到的 Service */

	private UserService userService;
	private ScoreService scoreService;


	public UserService getUserService() {
		return userService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public ScoreService getScoreService() {
		return scoreService;
	}


	public void setScoreService(ScoreService scoreService) {
		this.scoreService = scoreService;
	}


	public Log getLog() {
		return log;
	}
}
