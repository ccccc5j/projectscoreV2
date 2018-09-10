<@p.page navi = {
	"项目评分":"${base}/score/score!scoreList.action"	
} 
setReferUrl=true>
<@p.intro subTasks>这里是您的评分项目列表</@p.intro>
<div class="layout">
  <table width="80%" class="default">
    <col width="5%"/>
    <col width="15%"/>
    <col width="15%"/>
    <col width="10%"/>
    <col width="15%"/>
    <col width="10%"/>
    <col width="10%"/>         
    <tr class="title">
      <td>序号 </td>
      <td>评分项目名</td>
      <td>创建时间</td>
      <td>项目状态</td>
      <td>我的评分</td>
      <td>总分情况</td>
      <td>评分详情</td>
    </tr>
    <#assign webIndex=1>
 	<#list scoreList.list as score>	
    	<tr <@p.trChangeColor/>>
     		<td>${webIndex}</td>
     		<td>${score.PROJECT_NAME}</td>
     		<td>${score.CREATE_TIME?string('yyyy年M月d日')}</td>
     		<td>
     		<#if score.STATE==0>
     		关闭
     		<#else>
     		开放
     		</#if>
     		</td>
     		<td>
     		<#if Session["_LOGIN_USER_"]?exists>
    			<#if Session["_LOGIN_USER_"].TYPE==0>
		     		<#if score.STU_SCORE?exists>
		     		    <#if score.STATE==0> 
			     			已评分：${score.STU_SCORE},评分状态关闭
			     		<#else>
			     			已评分：${score.STU_SCORE},<a href="score!editScoreView.action?projectId=${score.PROJECT_ID}">点击修改评分</a>			     		
			     		</#if> 
		     		<#else>
		     			<#if score.STATE==0> 
			     			未评分,评分状态关闭
			     		<#else>
			     			未评分,<a href="score!editScoreView.action?projectId=${score.PROJECT_ID}">点击评分</a>			     		
			     		</#if>
		     		</#if>
		     	<#else>
		     		<#if score.TEACHER_SCORE?exists>
		     		    <#if score.STATE==0> 
			     			已评分：${score.TEACHER_SCORE},评分状态关闭
			     		<#else>
			     			已评分：${score.TEACHER_SCORE},<a href="score!editScoreView.action?projectId=${score.PROJECT_ID}">点击修改评分</a>			     		
			     		</#if> 
		     		<#else>
		     			<#if score.STATE==0> 
			     			未评分,评分状态关闭
			     		<#else>
			     			未评分,<a href="score!editScoreView.action?projectId=${score.PROJECT_ID}">点击评分</a>			     		
			     		</#if>
		     		</#if>
		     	</#if>
		    </#if>
     		</td>
     		 <td>
		     	<#if score.SCORE?exists> 
			    	汇总分:${score.SCORE}
			    <#else>	
			    	未汇总
			    </#if>
			</td>
     		 <td>
			    <a href="project!queryScoreInfo.action?projectId=${score.PROJECT_ID}">评分详情</a>
			</td>
    		</tr>
			<#assign webIndex=webIndex+1>
		</#list>	
  	</table>
	<table width="96%" align="right">
		<tr width="96%">
			<td align="right"><@p.paging scoreList/></td>
		</tr>
	</table>
</div>
</@p.page>
