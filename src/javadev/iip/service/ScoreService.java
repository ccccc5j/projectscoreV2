package javadev.iip.service;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javadev.core.common.PagingList;
import javadev.core.util.CommonUtil;
import javadev.core.util.MapUtil;
import javadev.core.util.QueryHelper;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

public class ScoreService extends BaseService {
	// 获取教师用户的项目列表（包含组信息）
	private static final String SQL_GET_TEACHER_PRO_GROUP_LIST = "SELECT p.*,s.GROUP_NAME FROM PROJECT  p "
			+ "INNER JOIN STU_GROUP s ON p.GROUP_ID=s.GROUP_ID "
			+ "WHERE p.TEACHER_ID=?  ORDER BY p.STATE DESC,p.PROJECT_ID DESC";

	public PagingList getTeacherProGroupInfo(String teacherId) {
		return getPagingList(SQL_GET_TEACHER_PRO_GROUP_LIST, new Object[] { teacherId });
	}

	// 获取教师用户的评分列表
	private static final String SQL_GET_TEACHER_PRO_LIST = "SELECT * FROM PROJECT  "
			+ "WHERE TEACHER_ID=?  ORDER BY STATE DESC,PROJECT_ID DESC";

	public PagingList getTeacherPro(String teacherId) {
		return getPagingList(SQL_GET_TEACHER_PRO_LIST, new Object[] { teacherId });
	}

	// 获取学生用户的评分列表
	private static final String SQL_GET_STU_PRO_LIST = "SELECT p.*,s.STU_ID,"
			+ "s.STU_SCORE,s.SCORE_TIME,s.STU_STATE FROM PROJECT p join SCORE s "
			+ "on p.PROJECT_ID=s.PROJECT_ID WHERE s.STU_ID=? AND p.GROUP_ID <>"
			+ "(SELECT GROUP_ID FROM STUDENT WHERE STU_ID=?) ORDER BY p.STATE DESC," + "p.PROJECT_ID DESC";

	public PagingList getStuPro(String studentId) {
		return getPagingList(SQL_GET_STU_PRO_LIST, new Object[] { studentId, studentId });
	}

	private static final String SQL_GET_TEACHER_CLASS_LIST = "SELECT * FROM CLASS  WHERE TEACHER_ID=?  ORDER BY CLASS_NAME";

	public List getTeacherClassList(String teacherId) {
		return jt.queryForList(SQL_GET_TEACHER_CLASS_LIST, new Object[] { teacherId });
	}

	private static final String SQL_GET_CLASS_GROUP_LIST = "SELECT GROUP_ID,GROUP_NAME FROM  STU_GROUP   "
			+ "WHERE CLASS_ID=?  ORDER BY GROUP_ID";

	public List getClassGroupList(int cId) {
		return jt.queryForList(SQL_GET_CLASS_GROUP_LIST, new Object[] { cId });
	}

	private static final String SQL_GET_GROUPNAME_BY_GROUP_ID = "SELECT GROUP_NAME FROM  STU_GROUP   "
			+ "WHERE GROUP_ID=?";

	public Map getGroupName(int gId) {
		return jt.queryForMap(SQL_GET_GROUPNAME_BY_GROUP_ID, new Object[] { gId });
	}

	private static final String SQL_ADD_PROJECT = "INSERT INTO PROJECT(PROJECT_NAME,TEACHER_ID,CLASS_ID,CREATE_TIME,GROUP_ID,STATE)VALUES(?,?,?,now(),?,0)";
	private static final String SQL_GET_PROJECT_ID = "SELECT MAX(PROJECT_ID) FROM PROJECT WHERE PROJECT_NAME=?";
	private static final String SQL_ADD_CLASS_SCORE_STU_LIST = "INSERT INTO SCORE(PROJECT_ID,STU_ID) "
			+ "(SELECT  ?,STU_ID FROM STUDENT s JOIN STU_GROUP g "
			+ "on s.GROUP_ID=g.GROUP_ID  WHERE g.CLASS_ID=? AND s.GROUP_ID <>?)";

	public void addProject(String projectName, String teacherId, int classId) {
		List groupList = getClassGroupList(classId);
		for (int i = groupList.size() - 1; i >= 0; i--) {// 倒序
			Map aClass = (Map) groupList.get(i);
			int groupId = new Integer(aClass.get("GROUP_ID").toString());
			addProject(projectName, teacherId, classId, groupId);
		}
	}

	public void addProject(String projectName, String teacherId, int classId, int groupId) {
		Map grouNameMap = getGroupName(groupId);
		String grouName = grouNameMap.get("GROUP_NAME").toString();
		int result = jt.update(SQL_ADD_PROJECT, new Object[] { grouName + projectName, teacherId, classId, groupId });
		if (result == 1) {
			int projectId = jt.queryForInt(SQL_GET_PROJECT_ID, new Object[] { grouName + projectName });
			jt.update(SQL_ADD_CLASS_SCORE_STU_LIST, new Object[] { projectId, classId, groupId });
		}
	}

	private static final String SQL_GET_TURN_PROJECT = "UPDATE PROJECT SET STATE=? WHERE TEACHER_ID=? AND PROJECT_ID=?";

	public int turnProject(int state, String teacherId, int projectId) {
		return jt.update(SQL_GET_TURN_PROJECT, new Object[] { state, teacherId, projectId });
	}

	private static final String SQL_GET_PROJECT_STATE = "SELECT STATE FROM PROJECT WHERE TEACHER_ID=? AND PROJECT_ID=?";
	private static final String SQL_DELETE_PROJECT_SCORE_STU_LIST = "DELETE FROM SCORE WHERE PROJECT_ID=?";
	private static final String SQL_DELETE_PROJECT = "DELETE FROM PROJECT WHERE  STATE=0 AND TEACHER_ID=? AND PROJECT_ID=?";

	public int deleteProject(String teacherId, int projectId) {
		Map stateMap = jt.queryForMap(SQL_GET_PROJECT_STATE, new Object[] { teacherId, projectId });
		if (stateMap == null || stateMap.get("STATE") == null)
			return -1;
		if ("1".equals(stateMap.get("STATE").toString()))
			return -2;
		jt.update(SQL_DELETE_PROJECT_SCORE_STU_LIST, new Object[] { projectId });
		return jt.update(SQL_DELETE_PROJECT, new Object[] { teacherId, projectId });

	}

	// 获取项目状态
	private static final String SQL_GET_PRO_STATE = "SELECT STATE FROM PROJECT  WHERE  PROJECT_ID=?";

	public Map getProjectState(int projectId) {
		try {
			return jt.queryForMap(SQL_GET_PRO_STATE, new Object[] { projectId });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	// 获取项目状态
	private static final String SQL_GET_PRO_INFO = "SELECT * FROM PROJECT  " + "WHERE  PROJECT_ID=?";

	public Map getProjectInfo(int projectId) {
		try {
			return jt.queryForMap(SQL_GET_PRO_INFO, new Object[] { projectId });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	// 获取教师用户的评分信息
	private static final String SQL_GET_TEACHER_PRO = "SELECT * FROM PROJECT  WHERE PROJECT_ID=?";

	public Map getTeacherScore(int projectId) {
		try {
			return jt.queryForMap(SQL_GET_TEACHER_PRO, new Object[] { projectId });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	// 获取学生用户的评分信息
	private static final String SQL_GET_STU_PRO = "SELECT p.*,s.STU_ID,s.STU_SCORE,s.SCORE_TIME,s.STU_STATE FROM PROJECT p join SCORE s on p.PROJECT_ID=s.PROJECT_ID WHERE s.STU_ID=? AND p.GROUP_ID <>(SELECT GROUP_ID FROM STUDENT WHERE STU_ID=?) AND s.PROJECT_ID=?";

	public Map getStudentScore(String studentId, int projectId) {
		try {
			return jt.queryForMap(SQL_GET_STU_PRO, new Object[] { studentId, studentId, projectId });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	// 获取教师用户的评分信息
	private static final String SQL_UPDATE_TEACHER_PRO = "UPDATE PROJECT SET TEACHER_SCORE=? WHERE TEACHER_ID=?  AND PROJECT_ID=?";

	public int setTeacherScore(float score, String teacherId, int projectId) {
		try {
			return jt.update(SQL_UPDATE_TEACHER_PRO, new Object[] { score, teacherId, projectId });
		} catch (Exception e) {
			return 0;
		}
	}

	// 获取学生用户的评分信息
	private static final String SQL_UPDATE_STU_PRO = "UPDATE SCORE SET STU_SCORE=?,SCORE_TIME=now(),STU_STATE=1 WHERE  STU_ID=? AND PROJECT_ID=?";

	public int setStudentScore(float score, String studentId, int projectId) {
		try {
			return jt.update(SQL_UPDATE_STU_PRO, new Object[] { score, studentId, projectId });
		} catch (Exception e) {
			return 0;
		}
	}

	private static final String SQL_GET_SCORE_LIST = "SELECT s.*,st.STU_NAME,st.GROUP_ID,st.GROUP_LEADER FROM STUDENT st join SCORE s on"
			+ " st.STU_ID=s.STU_ID  WHERE s.PROJECT_ID=?  ORDER BY st.GROUP_ID,st.GROUP_LEADER DESC,st.STU_ID";

	public List queryScoreInfo(int projectId) {
		return jt.queryForList(SQL_GET_SCORE_LIST, new Object[] { projectId });
	}

	// 获取教师用户的评分信息
	private static final String SQL_GET_TEACHER_SCORE_VALUE = "SELECT TEACHER_SCORE FROM PROJECT  WHERE TEACHER_ID=?  AND PROJECT_ID=?";

	public Map getTeacherScoreValue(String teacherId, int projectId) {
		try {
			return jt.queryForMap(SQL_GET_TEACHER_SCORE_VALUE, new Object[] { teacherId, projectId });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	// 获取教师用户的评分信息
	private static final String SQL_GET_STUDENT_SCORE_VALUE = "SELECT AVG(STU_SCORE) STU_SCORE FROM SCORE WHERE PROJECT_ID=?";

	public Map getStudentAvgScoreValue(int projectId) {
		try {
			return jt.queryForMap(SQL_GET_STUDENT_SCORE_VALUE, new Object[] { projectId });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private static final String SQL_UPDATE_PROJECT_SCORE = "UPDATE PROJECT SET SCORE=? WHERE TEACHER_ID=?  AND PROJECT_ID=?";

	public int setProjectScore(Double totalScore, String teacherId, int projectId) {
		try {
			return jt.update(SQL_UPDATE_PROJECT_SCORE, new Object[] { totalScore, teacherId, projectId });
		} catch (Exception e) {
			return 0;
		}
	}

	private static final String SQL_GET_SCORED_COUNT = "SELECT count(*) FROM  SCORE   WHERE PROJECT_ID=?  AND STU_STATE=1";

	public int queryScoredCount(int projectId) {
		return jt.queryForInt(SQL_GET_SCORED_COUNT, new Object[] { projectId });
	}

	// ;
	private static final String SQL_GET_STRVALUE_BY_STRKEY = 
			"SELECT STRVALUE FROM `sys_conf` where STRKEY like ?";

	public String querySysConf(String key) {
		try {
			Map map = jt.queryForMap(SQL_GET_STRVALUE_BY_STRKEY, new Object[] { key });
			if (map != null)
				return map.get("STRVALUE").toString();
			else
				return null;
		} catch (DataAccessException e) {
			return null;
		}
	}

	private static final String SQL_GET_SCOREFACOR_BY_TEACHER = "SELECT c.CLASS_NAME,g.GROUP_ID,g.GROUP_NAME,s.STU_ID,s.STU_NAME,"
			+ "s.GROUP_LEADER,s.SCORE_FACTOR FROM `class` c join stu_group g "
			+ "on g.CLASS_ID=c.CLASS_ID join student s ON s.GROUP_ID=g.GROUP_ID  "
			+ "WHERE c.TEACHER_ID=? ORDER BY g.GROUP_ID,s.GROUP_LEADER desc,s.STU_ID;";

	// 查询教师负责班级的得分系数等信息
	public List getScoreFactorList(String teacherId) {
		return jt.queryForList(SQL_GET_SCOREFACOR_BY_TEACHER, new Object[] { teacherId });
	}

	private static final String SQL_GET_SCOREFACOR_BY_GROUP_ID = "SELECT c.CLASS_NAME,g.GROUP_ID,g.GROUP_NAME,s.STU_ID,s.STU_NAME,"
			+ "s.GROUP_LEADER,s.SCORE_FACTOR FROM `class` c join stu_group g "
			+ "on g.CLASS_ID=c.CLASS_ID join student s ON s.GROUP_ID=g.GROUP_ID  "
			+ "WHERE g.GROUP_ID=? ORDER BY g.GROUP_ID,s.GROUP_LEADER desc,s.STU_ID;";

	// 查询某个组的得分系数等信息
	public List getScoreFactorList(int groupId) {
		return jt.queryForList(SQL_GET_SCOREFACOR_BY_GROUP_ID, new Object[] { groupId });
	}
	private static final String SQL_UPDATE_SCOREFACOR_BY_STU_ID_GROUP_ID = "update student "
			+ "set SCORE_FACTOR=? where STU_ID like ? AND GROUP_ID=?";
	public void updateScoreFactors(Float factor, String stuNO, int groupId) {
		jt.update(SQL_UPDATE_SCOREFACOR_BY_STU_ID_GROUP_ID , new Object[] {factor,stuNO, groupId });
		
	}
	private static final String SQL_GET_GROUP_PHONE_BY_GROUP_ID = "select PHONE from "
			+ "stu_group  where  GROUP_ID=?";
	public String getGroupPhone(int groupId) {
		// TODO Auto-generated method stub
		//return jt.queryForObject(SQL_GET_GROUP_PHONE_BY_GROUP_ID, new Object[] {groupId },String.class);
		try {
			Map map = jt.queryForMap(SQL_GET_GROUP_PHONE_BY_GROUP_ID, new Object[] { groupId});
			if (map != null)
				return map.get("PHONE").toString();
			else
				return "";
		} catch (Exception e) {
			return "";
		}	
	}
	
	private static final String SQL_UPDATE_GROUP_PHONE_BY_GROUP_ID = "update stu_group "
			+ "set PHONE=? where GROUP_ID=?";
	public void updateGroupPhone(String phone, int groupId) {
		jt.update(SQL_UPDATE_GROUP_PHONE_BY_GROUP_ID , new Object[] {phone, groupId });
		
	}
}