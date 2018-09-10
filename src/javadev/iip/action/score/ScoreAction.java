package javadev.iip.action.score;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javadev.core.common.PagingList;
import javadev.iip.action.BaseAction;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class ScoreAction extends BaseAction {
	public static final double TEACHER_SCORE_PERCENT = 0.33333;

	private PagingList scoreList;
	private PagingList projectList;
	private String teacherId;
	private String studentId;
	private int classId;
	private List groupList;
	private List classList;
	private int groupId;
	private String projectName;
	private int projectId;
	private Map scoreMap;

	private float score;
	private List scoreInfoList;

	private int scored;
	private int stuNum;
	private boolean selectAllGroup;// 是否选择全部组？

	private Integer[] projectIds;

	private List scoreFactorList;

	private String[] studentIds;
	private Float[] factors;
	private String phone;
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String[] getStudentIds() {
		return studentIds;
	}

	public void setStudentIds(String[] studentIds) {
		this.studentIds = studentIds;
	}

	public Float[] getFactors() {
		return factors;
	}

	public void setFactors(Float[] factors) {
		this.factors = factors;
	}

	public List getScoreFactorList() {
		return scoreFactorList;
	}

	public void setScoreFactorList(List scoreFactorList) {
		this.scoreFactorList = scoreFactorList;
	}

	public Integer[] getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(Integer[] projectIds) {
		this.projectIds = projectIds;
	}

	public boolean isSelectAllGroup() {
		return selectAllGroup;
	}

	public void setSelectAllGroup(boolean selectAllGroup) {
		this.selectAllGroup = selectAllGroup;
	}

	public List getClassList() {
		return classList;
	}

	public void setClassList(List classList) {
		this.classList = classList;
	}

	public int getScored() {
		return scored;
	}

	public void setScored(int scored) {
		this.scored = scored;
	}

	public int getStuNum() {
		return stuNum;
	}

	public void setStuNum(int stuNum) {
		this.stuNum = stuNum;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public List getScoreInfoList() {
		return scoreInfoList;
	}

	public void setScoreInfoList(List scoreInfoList) {
		this.scoreInfoList = scoreInfoList;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public Map getScoreMap() {
		return scoreMap;
	}

	public void setScoreMap(Map scoreMap) {
		this.scoreMap = scoreMap;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List getGroupList() {
		return groupList;
	}

	public void setGroupList(List groupList) {
		this.groupList = groupList;
	}

	public PagingList getScoreList() {
		return scoreList;
	}

	public void setScoreList(PagingList scoreList) {
		this.scoreList = scoreList;
	}

	public PagingList getProjectList() {
		return projectList;
	}

	public void setProjectList(PagingList projectList) {
		this.projectList = projectList;
	}

	public String scoreList() throws Exception {

		String type = getLoginUser().get("TYPE").toString();
		if ("1".equals(type)) {
			teacherId = getLoginUser().get("TEACHER_ID").toString();
			scoreList = getServMgr().getScoreService().getTeacherPro(teacherId);
		} else {
			studentId = getLoginUser().get("STU_ID").toString();
			scoreList = getServMgr().getScoreService().getStuPro(studentId);
		}
		return "scoreList";
	}

	public String projectList() throws Exception {
		String type = getLoginUser().get("TYPE").toString();
		if ("1".equals(type)) {
			teacherId = getLoginUser().get("TEACHER_ID").toString();
			projectList = getServMgr().getScoreService().getTeacherProGroupInfo(teacherId);
			return "projectList";
		} else {
			projectList = null;
			return ERROR;
		}
	}

	public String addProjectView() throws Exception {
		String type = getLoginUser().get("TYPE").toString();
		if ("1".equals(type)) {
			teacherId = getLoginUser().get("TEACHER_ID").toString();
			classList = getServMgr().getScoreService().getTeacherClassList(teacherId);
			if (classList != null && classList.size() > 0) {
				classId = new Integer(((Map) classList.get(0)).get("CLASS_ID").toString());
				groupList = getServMgr().getScoreService().getClassGroupList(classId);
			}
			return "addProjectView";
		} else {
			classList = null;
			return ERROR;
		}
	}

	// projectIds
	public String openSelected() {
		String type = getLoginUser().get("TYPE").toString();
		if ("1".equals(type)) {
			teacherId = getLoginUser().get("TEACHER_ID").toString();
			int successCount = 0;
			for (int i = 0; i < projectIds.length; i++) {
				Map projectMap = getServMgr().getScoreService().getProjectInfo(projectIds[i]);
				if (getServMgr().getScoreService().turnProject(1, teacherId, projectIds[i]) == 1) {// 开放项目
					successCount++;
				} else {
					setResult(ERROR);
					addMessage("开放评分项目" + projectMap.get("PROJECT_NAME").toString() + "失败！");
					addRedirURL("返回", "project!projectList.action");
					return EXECUTE_RESULT;
				}
			}
			if (successCount == projectIds.length) {// 开放项目
				setResult(SUCCESS);
				addMessage("总分成功！共开放" + successCount + "个项目！");
				addRedirURL("返回", "project!projectList.action");
			}
		} else {
			setResult(ERROR);
			addMessage("您没有此权限！");
			addRedirURL("返回", "project!projectList.action");
		}
		return EXECUTE_RESULT;
	}

	public String closeSelected() {
		String type = getLoginUser().get("TYPE").toString();
		if ("1".equals(type)) {
			teacherId = getLoginUser().get("TEACHER_ID").toString();
			int successCount = 0;
			for (int i = 0; i < projectIds.length; i++) {
				Map projectMap = getServMgr().getScoreService().getProjectInfo(projectIds[i]);
				if (getServMgr().getScoreService().turnProject(0, teacherId, projectIds[i]) == 1) {// 开放项目
					successCount++;
				} else {
					setResult(ERROR);
					addMessage("关闭评分项目" + projectMap.get("PROJECT_NAME").toString() + "失败！");
					addRedirURL("返回", "project!projectList.action");
					return EXECUTE_RESULT;
				}
			}
			if (successCount == projectIds.length) {// 开放项目
				setResult(SUCCESS);
				addMessage("关闭项目成功！共关闭" + successCount + "个项目！");
				addRedirURL("返回", "project!projectList.action");
			}
		} else {
			setResult(ERROR);
			addMessage("您没有此权限！");
			addRedirURL("返回", "project!projectList.action");
		}
		return EXECUTE_RESULT;
	}

	public String computeSelected() {
		String type = getLoginUser().get("TYPE").toString();
		int successCount = 0;
		if ("1".equals(type)) {
			teacherId = getLoginUser().get("TEACHER_ID").toString();
			for (int i = 0; i < projectIds.length; i++) {
				Map teacherScoreMap = getServMgr().getScoreService().getTeacherScoreValue(teacherId, projectIds[i]);
				Map studentScoreMap = getServMgr().getScoreService().getStudentAvgScoreValue(projectIds[i]);
				Map projectMap = getServMgr().getScoreService().getProjectInfo(projectIds[i]);
				if (teacherScoreMap == null || teacherScoreMap.get("TEACHER_SCORE") == null) {
					setResult(ERROR);
					addMessage(projectMap.get("PROJECT_NAME") + "项目教师还没有评分！此次总分成功" + successCount + "个项目。");
					addRedirURL("返回", "@back");
					return EXECUTE_RESULT;
				} else if (studentScoreMap == null || studentScoreMap.get("STU_SCORE") == null) {
					setResult(ERROR);
					addMessage(projectMap.get("PROJECT_NAME") + "学生都还没有有评分！此次总分成功" + successCount + "个项目。");
					addRedirURL("返回", "@back");
					return EXECUTE_RESULT;
				} else {
					// 总分计算公式
					Double totalScore = (new Double(teacherScoreMap.get("TEACHER_SCORE").toString()))
							* TEACHER_SCORE_PERCENT
							+ (new Double(studentScoreMap.get("STU_SCORE").toString())) * (1 - TEACHER_SCORE_PERCENT);

					int result = getServMgr().getScoreService().setProjectScore(Math.round(totalScore * 100) / 100.0,
							teacherId, projectIds[i]);
					if (result == 1) {// 开放项目
						successCount++;
					} else {
						setResult(ERROR);
						addMessage(projectMap.get("PROJECT_NAME") + "总分失败！本次总分成功" + successCount + "个项目。");
						addRedirURL("返回", "project!projectList.action");
						return EXECUTE_RESULT;
					}
				}
			}
			if (successCount == projectIds.length) {// 开放项目
				setResult(SUCCESS);
				addMessage("总分成功！共完成" + successCount + "个项目的总分！");
				addRedirURL("返回", "project!projectList.action");
			}

		} else {
			setResult(ERROR);
			addMessage("您没有此权限！");
			addRedirURL("返回", "@back");
		}
		return EXECUTE_RESULT;
	}

	public String deleteSelected() {
		String type = getLoginUser().get("TYPE").toString();
		int successCount = 0;
		if ("1".equals(type)) {
			teacherId = getLoginUser().get("TEACHER_ID").toString();

			for (int i = 0; i < projectIds.length; i++) {
				Map projectMap = getServMgr().getScoreService().getProjectInfo(projectIds[i]);
				int deleteResult = getServMgr().getScoreService().deleteProject(teacherId, projectIds[i]);
				if (deleteResult == 1) {// 开放项目
					successCount++;
				} else if (deleteResult == -1) {
					setResult(ERROR);
					addMessage("删除" + projectMap.get("PROJECT_NAME") + "评分项目失败！没有此项目或您没有此权限！");
					addRedirURL("返回", "project!projectList.action");
					return EXECUTE_RESULT;
				} else if (deleteResult == -2) {
					setResult(ERROR);
					addMessage("删除" + projectMap.get("PROJECT_NAME") + "评分项目失败！此项目处于开放状态，不能删除！");
					addRedirURL("返回", "project!projectList.action");
					return EXECUTE_RESULT;
				} else {
					setResult(ERROR);
					addMessage("删除" + projectMap.get("PROJECT_NAME") + "评分项目失败！");
					addRedirURL("返回", "project!projectList.action");
					return EXECUTE_RESULT;
				}
			}
			if (projectIds.length == successCount) {
				setResult(SUCCESS);
				addMessage("删除评分项目成功！");
				addRedirURL("返回", "project!projectList.action");
			}

		} else {
			setResult(ERROR);
			addMessage("您没有此权限！");
			addRedirURL("返回", "project!projectList.action");
		}
		return EXECUTE_RESULT;
	}

	public String getGroupListOfClass() throws Exception {
		String type = getLoginUser().get("TYPE").toString();
		if ("1".equals(type)) {
			groupList = getServMgr().getScoreService().getClassGroupList(classId);
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");// 避免中文乱码
			PrintWriter out = response.getWriter();

			out.println("document.getElementById('groupId').length = " + groupList.size());
			out.println("document.getElementById('groupId').selectedIndex = 0");

			for (int i = 0; i < groupList.size(); i++) {
				Map groupMap = (Map) groupList.get(i);
				out.println("document.getElementById('groupId').options[" + i + "].value = '" + groupMap.get("GROUP_ID")
						+ "'");
				out.println("document.getElementById('groupId').options[" + i + "].text = '"
						+ groupMap.get("GROUP_NAME") + "'");
			}

		} else {
			groupList = null;
		}
		return null;
	}

	public String addProject() throws Exception {
		String type = getLoginUser().get("TYPE").toString();
		if ("1".equals(type)) {
			teacherId = getLoginUser().get("TEACHER_ID").toString();
			if (selectAllGroup) {
				getServMgr().getScoreService().addProject(projectName, teacherId, classId);
			} else {
				getServMgr().getScoreService().addProject(projectName, teacherId, classId, groupId);
			}
			setResult(SUCCESS);
			addMessage("添加评分项目成功！");
			addRedirURL("返回", "project!projectList.action");
		} else {
			setResult(ERROR);
			addMessage("您没有此权限！");
			addRedirURL("返回", "project!projectList.action");
		}
		return EXECUTE_RESULT;
	}

	public String openPro() throws Exception {
		String type = getLoginUser().get("TYPE").toString();
		if ("1".equals(type)) {
			teacherId = getLoginUser().get("TEACHER_ID").toString();
			if (getServMgr().getScoreService().turnProject(1, teacherId, projectId) == 1) {// 开放项目
				setResult(SUCCESS);
				addMessage("开放评分项目成功！");
				addRedirURL("返回", "project!projectList.action");
			} else {
				setResult(ERROR);
				addMessage("开放评分项目失败！");
				addRedirURL("返回", "project!projectList.action");
			}
		} else {
			setResult(ERROR);
			addMessage("您没有此权限！");
			addRedirURL("返回", "project!projectList.action");
		}
		return EXECUTE_RESULT;
	}

	public String closePro() throws Exception {
		String type = getLoginUser().get("TYPE").toString();
		if ("1".equals(type)) {
			teacherId = getLoginUser().get("TEACHER_ID").toString();
			if (getServMgr().getScoreService().turnProject(0, teacherId, projectId) == 1) {// 开放项目
				setResult(SUCCESS);
				addMessage("关闭评分项目成功！");
				addRedirURL("返回", "project!projectList.action");
			} else {
				setResult(ERROR);
				addMessage("关闭评分项目失败！");
				addRedirURL("返回", "project!projectList.action");
			}
		} else {
			setResult(ERROR);
			addMessage("您没有此权限！");
			addRedirURL("返回", "project!projectList.action");
		}
		return EXECUTE_RESULT;
	}

	public String deletePro() throws Exception {
		String type = getLoginUser().get("TYPE").toString();
		if ("1".equals(type)) {
			teacherId = getLoginUser().get("TEACHER_ID").toString();
			int deleteResult = getServMgr().getScoreService().deleteProject(teacherId, projectId);
			if (deleteResult == 1) {// 开放项目
				setResult(SUCCESS);
				addMessage("删除评分项目成功！");
				addRedirURL("返回", "project!projectList.action");
			} else if (deleteResult == -1) {
				setResult(ERROR);
				addMessage("删除评分项目失败！没有此项目或您没有此权限！");
				addRedirURL("返回", "project!projectList.action");
			} else if (deleteResult == -2) {
				setResult(ERROR);
				addMessage("删除评分项目失败！此项目处于开放状态，不能删除！");
				addRedirURL("返回", "project!projectList.action");
			} else {
				setResult(ERROR);
				addMessage("删除评分项目失败！");
				addRedirURL("返回", "project!projectList.action");
			}
		} else {
			setResult(ERROR);
			addMessage("您没有此权限！");
			addRedirURL("返回", "project!projectList.action");
		}
		return EXECUTE_RESULT;
	}

	public String editScoreView() throws Exception {
		String type = getLoginUser().get("TYPE").toString();
		if ("1".equals(type)) {
			scoreMap = getServMgr().getScoreService().getTeacherScore(projectId);
		} else {
			studentId = getLoginUser().get("STU_ID").toString();
			scoreMap = getServMgr().getScoreService().getStudentScore(studentId, projectId);
		}
		return "editScoreView";
	}

	public String editScore() throws Exception {
		Map stateMap = getServMgr().getScoreService().getProjectState(projectId);
		if (stateMap == null || !"1".equals(stateMap.get("STATE").toString())) {
			setResult(ERROR);
			addMessage("评分项目经已关闭，不能评分！");
			addRedirURL("返回", "score!scoreList.action");
		} else {
			String type = getLoginUser().get("TYPE").toString();
			int result = 0;
			score = Math.round(score);
			if ("1".equals(type)) {
				teacherId = getLoginUser().get("TEACHER_ID").toString();
				result = getServMgr().getScoreService().setTeacherScore(score, teacherId, projectId);
			} else {
				studentId = getLoginUser().get("STU_ID").toString();
				result = getServMgr().getScoreService().setStudentScore(score, studentId, projectId);
			}

			if (result == 1) {// 开放项目
				setResult(SUCCESS);
				addMessage("评分成功！");
				addRedirURL("返回", "score!scoreList.action");
			} else {
				setResult(ERROR);
				addMessage("评分失败！");
				addRedirURL("返回", "score!scoreList.action");
			}
		}
		return EXECUTE_RESULT;
	}

	// 查询评分信息
	public String queryScoreInfo() throws Exception {
		scoreMap = getServMgr().getScoreService().getTeacherScore(projectId);
		scoreInfoList = getServMgr().getScoreService().queryScoreInfo(projectId);
		scored = getServMgr().getScoreService().queryScoredCount(projectId);
		stuNum = scoreInfoList.size();
		return "queryScoreInfo";
	}

	// 计算项目总分
	public String computeScore() throws Exception {
		String type = getLoginUser().get("TYPE").toString();
		if ("1".equals(type)) {
			teacherId = getLoginUser().get("TEACHER_ID").toString();
			Map teacherScoreMap = getServMgr().getScoreService().getTeacherScoreValue(teacherId, projectId);
			Map studentScoreMap = getServMgr().getScoreService().getStudentAvgScoreValue(projectId);
			if (teacherScoreMap == null || teacherScoreMap.get("TEACHER_SCORE") == null) {
				setResult(ERROR);
				addMessage("教师还没有评分！");
				addRedirURL("返回", "@back");
			} else if (studentScoreMap == null || studentScoreMap.get("STU_SCORE") == null) {
				setResult(ERROR);
				addMessage("学生都还没有有评分！");
				addRedirURL("返回", "@back");
			} else {
				// 总分计算公式
				Double totalScore = (new Double(teacherScoreMap.get("TEACHER_SCORE").toString()))
						* TEACHER_SCORE_PERCENT
						+ (new Double(studentScoreMap.get("STU_SCORE").toString())) * (1 - TEACHER_SCORE_PERCENT);

				int result = getServMgr().getScoreService().setProjectScore(Math.round(totalScore * 100) / 100.0,
						teacherId, projectId);
				if (result == 1) {// 开放项目
					setResult(SUCCESS);
					addMessage("总分成功！");
					addRedirURL("返回", "project!projectList.action");
				} else {
					setResult(ERROR);
					addMessage("总分失败！");
					addRedirURL("返回", "project!projectList.action");
				}
			}

		} else {
			setResult(ERROR);
			addMessage("您没有此权限！");
			addRedirURL("返回", "@back");
		}
		return EXECUTE_RESULT;
	}

	// scoreFactorView
	// 组长给定打分系数
	// 计算项目总分
	public String scoreFactorView() throws Exception {
		if ("true".equalsIgnoreCase(getServMgr().getScoreService().querySysConf("OpenFactor"))) {
			String type = getLoginUser().get("TYPE").toString();
			if ("1".equals(type)) {
				// 教师查询
				teacherId = getLoginUser().get("TEACHER_ID").toString();
				scoreFactorList = getServMgr().getScoreService().getScoreFactorList(teacherId);
				return "scoreFactorList";
			} else if ("1".equals(getLoginUser().get("GROUP_LEADER").toString())) {
				// 组长查询
				groupId = new Integer(getLoginUser().get("GROUP_ID").toString());
				scoreFactorList = getServMgr().getScoreService().getScoreFactorList(groupId);
				phone = getServMgr().getScoreService().getGroupPhone(groupId);
				return "scoreFactorList";
			} else {
				setResult(ERROR);
				addMessage("您没有此权限！");
				addRedirURL("返回", "@back");
			}
		} else {
			setResult(ERROR);
			addMessage("对不起，组长的得分系数功能还没有开放！");
			addRedirURL("返回", "@back");
		}
		return EXECUTE_RESULT;
	}

	public String scoreFactor() throws Exception {
		if ("true".equalsIgnoreCase(getServMgr().getScoreService().querySysConf("OpenFactor"))) {
			String type = getLoginUser().get("TYPE").toString();
			if ("0".equals(type) && "1".equals(getLoginUser().get("GROUP_LEADER").toString())) {
				// 组长提交
				groupId = new Integer(getLoginUser().get("GROUP_ID").toString());
				for (int i = 0; i < studentIds.length; i++) {
					getServMgr().getScoreService().updateScoreFactors(factors[i], studentIds[i], groupId);
				}
				getServMgr().getScoreService().updateGroupPhone(phone, groupId);
				phone = getServMgr().getScoreService().getGroupPhone(groupId);
				scoreFactorList = getServMgr().getScoreService().getScoreFactorList(groupId);
			
				setResult(SUCCESS);
				addMessage("填写得分系数成功！");
				addRedirURL("返回", "@back");
			} else {
				setResult(ERROR);
				addMessage("您没有此权限！");
				addRedirURL("返回", "@back");
			}
		} else {
			setResult(ERROR);
			addMessage("对不起，组长的得分系数功能还没有开放！");
			addRedirURL("返回", "@back");
		}
		return EXECUTE_RESULT;
	}
}