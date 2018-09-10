package javadev.iip.action.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javadev.core.Constants;
import javadev.core.common.PagingList;
import javadev.core.util.MD5;
import javadev.iip.action.BaseAction;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

public class UserAction extends BaseAction {
	private String errorInfo = null;

	private String userName;

	private String password;

	private String randomCode;

	private String indexUrl;

	private MD5 md5 = new MD5();

	public String getErrorInfo() {
		return errorInfo;
	}

	public String getUserName() {
		return userName;
	}

	/**
	 * 用户登录
	 */
	public String login() throws Exception {
		String sessionCode = null;

		Object sessionCodeObj = getSession().remove(Constants.RANDOM_CODE);
		if (sessionCodeObj == null || randomCode == null || password == null
				|| userName == null || "".equals(randomCode)
				|| "".equals(password) || "".equals(userName)) {
			errorInfo = "您输入的验证码不正确！";
			return ERROR;
		}
		/**/
		sessionCode = sessionCodeObj.toString().trim();
		if (!randomCode.equalsIgnoreCase(sessionCode)) {
			errorInfo = "您输入的验证码不正确！";
			return ERROR;
		}
		int type = 0;//默认是学生
		Map dbUser = getServMgr().getUserService().getStudentByID(userName);
		if (dbUser == null) {
			dbUser = getServMgr().getUserService().getTeacherByLoginName(
					userName);
			if (dbUser != null) {
				type = 1;// 教师
				dbUser.put("TYPE", 1);
			}
		} else{
			dbUser.put("TYPE", 0);
		}
		
		String passwordMd5 = md5.getMD5ofStr(password);
		if (dbUser != null
				&& passwordMd5.equals((String) dbUser.get("PASSWORD"))) {
			// 更新用户登录信息
			indexUrl = (String) getSession().remove(Constants.ORIGINAL_URL);
			getSession().clear();// 清空原始Session信息
			getSession().put(Constants.LOGIN_USER, dbUser);
			if (indexUrl == null) {
				if(type==1)
					return "homepageOfTeacher";
				else
					return "homepageOfStudent";
			}
			return SUCCESS;
		}
		if (dbUser == null) {
			errorInfo = "您输入的用户名不存在！";
			return ERROR;
		}
		if (!password.equals((String) dbUser.get("PASSWORD"))) {
			errorInfo = "您输入的密码错误！";
			return ERROR;
		}
		return ERROR;
	}

	// }

	/**
	 * 用户登出
	 */
	public String logout() throws Exception {
		Map user = (Map) getSession().get(Constants.LOGIN_USER);
		if (user != null) {
			log.debug("当前用户名" + (String) user.get("STAFF_NAME") + "退出系统！");
		}
		ServletActionContext.getRequest().getSession().invalidate();
		return SUCCESS;
	}

	/**
	 * 进入修改密码页面
	 */
	public String viewChangePass() throws Exception {
		return SUCCESS;
	}

	/**
	 * 更改密码
	 */
	public String changePass() throws Exception {
		Map params = getParameters();
		String password0 = ((String[]) (params.get("password0")))[0];
		String password1 = ((String[]) (params.get("password1")))[0];
		String password2 = ((String[]) (params.get("password2")))[0];
		String password0Md5 = md5.getMD5ofStr(password0.trim());
		// String userPass = getServMgr().getUserService().getPass(
		// getLoginUserId());
		String userPass = getLoginUser().get("PASSWORD").toString().trim();
		if (!password0Md5.equals(userPass)) {

			setResult(ERROR);
			addMessage("你输入的旧密码不正确，请重试！");
			addRedirURL("返回", "@back");

		} else if (!password1.equals(password2)) {

			setResult(ERROR);
			addMessage("你输入的新密码两次不一致，请重试！");
			addRedirURL("返回", "@back");

		} else {
			String type = getLoginUser().get("TYPE").toString();
			if ("1".equals(type)) {
				String tId = getLoginUser().get("TEACHER_ID").toString();
				if (getServMgr().getUserService().changeTeacherPass(
						md5.getMD5ofStr(password1), tId)) {
					setResult(SUCCESS);
					addMessage("用户密码修改成功！");
					addRedirURL("返回", "@back");

				} else {
					setResult(ERROR);
					addMessage("不可以修改密码！");
					addRedirURL("返回", "@back");
				}
			} else {
				String sId = getLoginUser().get("STU_ID").toString();
				if (getServMgr().getUserService().changeStudentPass(
						md5.getMD5ofStr(password1), sId)) {
					setResult(SUCCESS);
					addMessage("用户密码修改成功！");
					addRedirURL("返回", "@back");
				} else {
					setResult(ERROR);
					addMessage("不可以修改密码！");
					addRedirURL("返回", "@back");
				}
			}
		}
		return EXECUTE_RESULT;
	}

	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}

	public String getIndexUrl() {
		return indexUrl;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}