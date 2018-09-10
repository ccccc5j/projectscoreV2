package javadev.iip.service;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javadev.core.common.PagingList;
import javadev.core.util.CommonUtil;
import javadev.core.util.MapUtil;
import javadev.core.util.QueryHelper;

import org.springframework.dao.EmptyResultDataAccessException;

public class UserService extends BaseService {

	// 获取登录用户的职责信息列表
	private static final String SQL_GET_ROLE_DUTY = "SELECT DUTY_INFO FROM ROLE_DUTY "
			+ "WHERE ROLE_ID=? AND TYPE=? ORDER BY SEQ";

	/**
	 * 获取登录用户的职责信息列表
	 * @param 	String roleID,角色号
	 * 			String type,职责类型，1为系统内职责；2为系统外职责。
	 * @return List,职责列表
	 * @author 陈建
	 * @since 2013-05-30
	 */
	public List getUserDuty(String roleID,String type) {
		try {
			List duty = jt.queryForList(SQL_GET_ROLE_DUTY, new Object[] {roleID,type});
			return duty;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	// 通过登录名获取用户信息，另外包括用户所属机构和用户所属行政区划部分信息
	private static final String SQL_GET_TEACHER_BY_LOGIN_NAME = "SELECT * FROM TEACHER WHERE LOGIN_NAME=?";
	
	/**
	 * 通过登录名获取用户信息
	 */
	public Map getTeacherByLoginName(String userName) {
		try {
			Map user = jt.queryForMap(SQL_GET_TEACHER_BY_LOGIN_NAME,
					new Object[] { userName });

			return user;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	
	private static final String SQL_GET_STUDENT_BY_STU_ID = "SELECT * FROM STUDENT WHERE STU_ID=?";
	/**
	 * 通过登录名获取用户信息
	 */
	public Map getStudentByID(String userName) {
		try {
			Map user = jt.queryForMap(SQL_GET_STUDENT_BY_STU_ID,
					new Object[] { userName });

			return user;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	
	private static final String SQL_GET_PASS = "select password from staff  where staff_id=?";

	/**
	 * 得到用户的密码
	 */
	public String getPass(Long staffId) {
		String staffPass = null;
		try {
			staffPass = jt.queryForObject(SQL_GET_PASS,
					new Object[] { staffId }, String.class).toString();
		} catch (EmptyResultDataAccessException e) {
			staffPass = null;
		}
		return staffPass;
	}

	private static final String SQL_CHANGE_TEACHER_PASS = "update TEACHER set password=? where TEACHER_ID=?";

	/**
	 * 更改个人密码
	 */
	public boolean changeTeacherPass(String password1, String tId) {
		try {
			return (jt.update(SQL_CHANGE_TEACHER_PASS, new Object[] { password1,tId }) == 1);
		} catch (EmptyResultDataAccessException e) {
			return false;
		}
	}
	
	private static final String SQL_CHANGE_STUDENT_PASS = "update STUDENT set password=? where stu_id=?";

	/**
	 * 更改个人密码
	 */
	public boolean changeStudentPass(String password1, String sId) {
		try {
			return (jt.update(SQL_CHANGE_STUDENT_PASS, new Object[] { password1,sId }) == 1);
		} catch (EmptyResultDataAccessException e) {
			return false;
		}
	}
	/**
	 * 更新用户登录信息
	 * 
	 * @param userId
	 */
	public void updateLoginInfo(long userId) {
		jt.update(
				"update users set logintimes=logintimes+1, lastlogindate=sysdate where userid = ?",
				new Object[] { new Long(userId) });
	}

	private static final String SQL_USER_LIST = "select g.dirid,g.dirname,g.parentid,u.* from groupdirectory g, users u "
			+ "where u.groupid=? and u.groupdirid=? "
			+ "and g.groupid(+)=u.groupid and g.dirid(+)=u.groupdirid order by u.username";

	/**
	 * 进入到用户详细信息页面(机构下的具体部门中的用户)
	 */
	public PagingList selectUserList(String groupId, String dirId) {
		Object[] oj = new Object[] { groupId, dirId };
		return getPagingList(SQL_USER_LIST, oj);
	}

	private static final String SQL_USER_INFO = "select userid,username,password,question,answer,email,realname,"
			+ "idcard,sex,birthday,address,postalcode,phone,mobilephone,groupid from users where userid=?";

	/**
	 * 得到单个用户信息
	 */
	public Map getUser(long userId) {
		List list = jt.queryForList(SQL_USER_INFO, new Object[] { new Long(
				userId) });
		if (list.size() > 0) {
			return (Map) list.get(0);
		} else {
			return null;
		}
	}

	private static final String SQL_GROUP_USER_EDIT = "update users set realname=?,sex=?,idcard=?,"
			+ "address=?,postalcode=?,phone=?,mobilephone=?,email=? where userid=?";

	/**
	 * 更改组用户基本信息
	 */
	public void updateGroupUser(Map map, long userId) {
		Object[] oj = MapUtil
				.getObjectArrayFromMap(map,
						"realName,sex,idcard,address,postalCode,phone,mobilePhone,email");
		Object[] tmp = new Object[] { oj[0], oj[1], oj[2], oj[3], oj[4], oj[5],
				oj[6], oj[7], new Long(userId) };
		jt.update(SQL_GROUP_USER_EDIT, tmp);
	}

	private static final String SQL_USER_STATUS_NOUSED = "update users set state=0 where userid=?";

	/**
	 * 改变用户状态为不可用
	 */
	public void changeUserStatusNoUsed(String[] userId) {
		if (userId == null) {
			return;
		}
		for (int i = 0; i < userId.length; i++) {
			jt.update(SQL_USER_STATUS_NOUSED, new Object[] { (userId[i]) });
		}
	}

	private static final String SQL_USER_STATUS_USED = "update users set state=1 where userid=?";

	/**
	 * 改变用户状态为可用
	 */
	public void changeUserStatusUsed(String[] userId) {
		if (userId == null) {
			return;
		}
		for (int i = 0; i < userId.length; i++) {
			Object[] params = new Object[] { userId[i] };
			jt.update(SQL_USER_STATUS_USED, params);
		}
	}

	private static final String SQL_PERMIT_PASSCHANGE = "update users set changepass=1 where userid=?";

	/**
	 * 允许密码更改
	 */
	public void permitPassChange(String[] userId) {
		if (userId == null) {
			return;
		}
		for (int i = 0; i < userId.length; i++) {
			Object[] params = new Object[] { userId[i] };
			jt.update(SQL_PERMIT_PASSCHANGE, params);
		}
	}

	private static final String SQL_NOPERMIT_PASSCHANGE = "update users set changepass=0 where userid=?";

	/**
	 * 不允许密码更改
	 */
	public void noPermitPassChange(String[] userId) {
		if (userId == null) {
			return;
		}
		for (int i = 0; i < userId.length; i++) {
			Object[] params = new Object[] { userId[i] };
			jt.update(SQL_NOPERMIT_PASSCHANGE, params);
		}
	}

	private static final String SQL_INITIALIZE_USERPASS = "update users set password='123456' where userid=?";

	/**
	 * 初始化用户密码为"123456"
	 */
	public void initializeUserPass(String[] userId) {
		if (userId == null) {
			return;
		}
		for (int i = 0; i < userId.length; i++) {
			Object[] params = new Object[] { userId[i] };
			jt.update(SQL_INITIALIZE_USERPASS, params);
		}
	}

	private static final String SQL_ADD_USER_TO_GROUPDIR = "update users set groupid=?,groupdirid=? where userid=?";

	/**
	 * 添加用户到组内某个部门,可以批量进行
	 */
	public void addUserToGroupDir(long groupId, String groupDirId,
			String[] userId) {
		if (userId == null) {
			return;
		}
		for (int i = 0; i < userId.length; i++) {
			Object[] params = new Object[] { new Long(groupId), groupDirId,
					userId[i] };
			jt.update(SQL_ADD_USER_TO_GROUPDIR, params);
		}
	}

	private static final String SQL_BY_USERNAME = "select count(username) from users where username=?";

	/**
	 * 判断该用户明是否以注册
	 */
	public int getUserByUserName(Map map) {
		Object[] oj = MapUtil.getObjectArrayFromMap(map, "userName");
		int num = jt.queryForInt(SQL_BY_USERNAME, new Object[] { oj[0] });
		return num;
	}

	private static final String SQL_ADD_USERNAME = "insert into users(userid,username,"
			+ "password,question,answer,realname,sex,birthday,idcard,address,postalcode,"
			+ "phone,mobilephone,email,groupid,groupdirid) values(to_number(to_char(sysdate,'yyyymmdd'))*10000000+seq_users.nextval,"
			+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 注册用户
	 */
	public void registerGroupUser(Map map, Date birthday, long groupId) {
		Object[] oj = MapUtil
				.getObjectArrayFromMap(
						map,
						"userName,password,question,answer,realName,sex,"
								+ "idcard,address,postalCode,phone,mobilePhone,email,groupDirId");
		Object[] params = new Object[] { oj[0], oj[1], oj[2], oj[3], oj[4],
				oj[5], birthday, oj[6], oj[7], oj[8], oj[9], oj[10], oj[11],
				new Long(groupId), oj[12] };

		jt.update(SQL_ADD_USERNAME, params);
	}

	/**
	 * 搜索用户结果页面
	 */
	public PagingList queryGroupUser(Map params) {
		String userName = ((String[]) params.get("userName"))[0];
		String realName = ((String[]) params.get("realName"))[0];
		String email = ((String[]) params.get("email"))[0];
		String sex = ((String[]) params.get("sex"))[0];
		String group = ((String[]) params.get("group"))[0];
		String part = ((String[]) params.get("part"))[0];
		String queryUserSql = "select u.userid,u.username,u.realname,u.groupid,u.groupdirid,u.state,u.changepass,u.email,u.sex,g.groupname,gd.dirname from users u,groups g,groupdirectory gd";
		String queryUserSql2 = "order by u.groupid";
		QueryHelper qu = new QueryHelper(queryUserSql, queryUserSql2);
		qu.setParam(true, "u.groupid=g.groupid(+)");
		qu.setParam(true, "u.groupdirid=gd.dirid(+)");
		qu.setParam(userName != null && userName.length() > 0,
				"u.username like ?", "%" + userName + "%");
		qu.setParam(realName != null && realName.length() > 0,
				"u.realName like ?", "%" + realName + "%");
		qu.setParam(email != null && email.length() > 0, "u.email like ?", "%"
				+ email + "%");
		qu.setParam(sex != null && !"".equals(sex), "u.sex=?", sex);
		qu.setParam(group != null && !"".equals(group), "u.groupid=?", group);
		qu.setParam(part != null && !"".equals(part), "u.groupdirid=?", part);

		return getPagingList(qu.getQuerySql(), qu.getParams());

	}

	private static final String SQL_KEY = "select dirname, dirid from groupdirectory where groupid = ?";

	/**
	 * 返回已Name作为Key的部门Map
	 */
	public Map getDirNameMapByGroupId(long groupId) {
		return CommonUtil.simpleListToMap(
				jt.queryForList(SQL_KEY, new Object[] { new Long(groupId) }),
				"DIRNAME", "DIRID");
	}


	/**
	 * 查询用户(用户组里的用户查找)
	 */
	public PagingList queryGroupDirUser(Map params) {
		String userName = ((String[]) params.get("userName"))[0];
		String realName = ((String[]) params.get("realName"))[0];
		String email = ((String[]) params.get("email"))[0];
		long groupId = Long.parseLong(((String[]) params.get("groupId"))[0]);
		String groupDirId = ((String[]) params.get("groupDirId"))[0];

		String queryUserSql = "select u.userid,u.username,u.realname,u.groupid,u.groupdirid,u.state,u.changepass,u.email,gd.dirname from users u, groupdirectory gd";

		QueryHelper qu = new QueryHelper(queryUserSql);

		qu.setParam(true, "u.groupid = gd.groupid(+)");
		qu.setParam(true, "u.groupdirid = gd.dirid(+)");
		qu.setParam(groupId >= 0, "u.groupid = ?", new Long(groupId));
		qu.setParam(userName != null && userName.length() > 0,
				"u.username like ?", "%" + userName + "%");
		qu.setParam(realName != null && realName.length() > 0,
				"u.realName like ?", "%" + realName + "%");
		qu.setParam(email != null && email.length() > 0, "u.email like ?", "%"
				+ email + "%");
		qu.setParam(groupDirId != null && !"0".equals(groupDirId),
				"u.groupDirId like ?", groupDirId + "%");

		return getPagingList(qu.getQuerySql(), qu.getParams());
	}

	private static final String SQL_GET_USERINFO = "select userid,username,password,question,answer,email,realname,"
			+ "idcard,sex,birthday,address,postalcode,phone,mobilephone,groupid from users where userid=?";

	/**
	 * 得到用户基本信息
	 */
	public Map getGroupUserInfo(long userId) {
		List list = jt.queryForList(SQL_GET_USERINFO, new Object[] { new Long(
				userId) });
		if (list.size() > 0) {
			return (Map) list.get(0);
		} else {
			return null;
		}
	}

	private static final String SQL_GET_USERPASS = "select password from users where userid=?";

	/**
	 * 得到用户的密码
	 */
	public String getUserPass(Long userId) {
		String userPass;
		userPass = jt.queryForObject(SQL_GET_USERPASS, new Object[] { userId },
				String.class).toString();
		return userPass;
	}

	private static final String SQL_REGISTERUSER = "insert into users(userid,username,"
			+ "password,question,answer,realname,sex,birthday,idcard,address,postalcode,"
			+ "phone,mobilephone,email)values(to_number(to_char(sysdate,'yyyymmdd'))*10000000 + seq_users.nextval,"
			+ "?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 注册单个新用户
	 */
	public void register(Map map, Date birthday) {
		Object[] oj = MapUtil
				.getObjectArrayFromMap(
						map,
						"userName, password, question, answer, realName, sex, idcard, address, postalCode, phone, mobilePhone, email");

		Object[] params = new Object[] { oj[0], oj[1], oj[2], oj[3], oj[4],
				oj[5], birthday, oj[6], oj[7], oj[8], oj[9], oj[10], oj[11] };

		jt.update(SQL_REGISTERUSER, params);
	}

	private static final String SQL_GET_BY_USERNAME = "select userid,username,password,question,answer,email from users where username=?";

	/**
	 * 注册时得到用户名，判断是否已经存在
	 */
	public Map getByUserName(String userName) {

		List users = jt.queryForList(SQL_GET_BY_USERNAME,
				new Object[] { userName });
		if (users.size() > 0) {
			return (Map) users.get(0);
		}
		return null;
	}
}
