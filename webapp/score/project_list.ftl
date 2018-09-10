<@p.page navi = {
	"我的信息":"${base}/home/index.action"	
} 
setReferUrl=true>
<script type="text/javascript">
<!--

function checkSelected(){
	var an=document.getElementsByName("projectIds");
	var w=0,y=0;
	for(w=0;w<an.length;w++){
		if(an[w].checked){
		y=y+1;
		}
	}
	if(y<=0){
		alert("请至少选择一个项目!");
		return false;
	}else{
		return true;
	}
}
function openSelected(){
	if(checkSelected()){
	document.projectsForm.action="project!openSelected.action";
	document.projectsForm.submit();
	}
}
function closeSelected(){
	if(checkSelected()){
	document.projectsForm.action="project!closeSelected.action";
	document.projectsForm.submit();
	}
}
function computeSelected(){
	if(checkSelected()){
	document.projectsForm.action="project!computeSelected.action";
	document.projectsForm.submit();
	}
}
function deleteSelected(){
	if(checkSelected()){
	document.projectsForm.action="project!deleteSelected.action";
	document.projectsForm.submit();
	}
}
//-->
</script>
<form id="addProjectForm" name="addProjectForm" action="" method="post" >
<div class="buttons" style="float: left;display: inline;margin:5px 10px 5px 10px;align:middle;">
    <span class="btnSpan"  style="border: 0px;float: left;margin:2px 20px 2px 2px;padding:2px 2px 2px 2px;">
 	<input type="button"  style="float: left;height:24px;" value="添加项目" 
 	onclick="window.self.location='project!addProjectView.action'">
 	</span> 	
 </div>
</form>

<br/>

<div>
<form id="projectsForm" name="projectsForm" action="" method="post" >
  <table width="90%" class="default">
  	<col width="5%" align="right" />
    <col width="5%" align="right" />
    <col width="20%" align="left" />
    <col width="10%" align="right" />
    <col width="10%" align="left" />
    <col width="10%" align="right" />
    <col width="10%" align="left" /> 
	<col width="20%" align="left" />   
    <tr class="title">
      <td>选择 </td>
      <td>序号 </td>
      <td>评分项目名</td>
      <td>讲解组</td>
      <td>创建时间</td>
      <td>状态</td>
      <td>项目总分</td>
      <td>操作</td>
    </tr>
    <#assign webIndex=1>
 	<#list projectList.list as project>	

    	<tr <@p.trChangeColor/>>
    	 	<td nowrap>	
		    <input type="checkbox" name="projectIds" value="${project.PROJECT_ID}" >
		</td>	
     	<td>${webIndex}</td>
     	<td>${project.PROJECT_NAME}</td>
     	<td>${project.GROUP_NAME}</td>
     	<td>${project.CREATE_TIME?string('yyyy年M月d日')}</td>
     	<td>
     	<#if project.STATE==0>
     	关闭
     	<#else>
     	开放
     	</#if>
     	</td>
     	<td>
	     	<#if project.SCORE?exists> 
		    	已汇总:${project.SCORE}
		    <#else>		     	
		     	<font color="red">未汇总</font>
		    </#if>
		</td> 
		<td align="left">
		<#if project.STATE==0>
	     	<a href="project!deletePro.action?projectId=${project.PROJECT_ID}" onclick="return confirm('是否确定要删除?')">删除</a>
	     	|<a href="project!openPro.action?projectId=${project.PROJECT_ID}">开放</a>
	     	|<a href="project!computeScore.action?projectId=${project.PROJECT_ID}">总分</a>
	     	<#else>
	     	<a href="project!closePro.action?projectId=${project.PROJECT_ID}">关闭</a>
	     	</#if>
	     	|<a href="project!queryScoreInfo.action?projectId=${project.PROJECT_ID}">评分情况</a>
		 </td>		
    	</tr>
	<#assign webIndex=webIndex+1>
	</#list>	
  </table>
  	<table width="95%" align="right">
	<tr width="95%">
		<td align="right"><@p.paging  projectList/></td>
	</tr>
	</table>
	  <table width="95%">
      	<tr>
	      	<td>      		
			<input type="button"  onclick="openSelected()" value="批量开放"/>
			<input type="button"  onclick="closeSelected()" value="批量关闭"/>
			<input type="button"  onclick="computeSelected()" value="批量总分"/>
			<input type="button"  onclick="deleteSelected()"  value="批量删除"/>
			</td>
		</tr>
	</table>
	</form>
  </div>
</@p.page>
