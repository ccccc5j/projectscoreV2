<@p.page navi = {
	"查看评分":"" } 
setReferUrl=true>
<@p.intro >这里您可以查看项目评分情况。</@p.intro>
<table width="98%" >
	<tr>
		<td align="left"  nowrap class="rd19">
			<input type="button"  class="btn" value="返回"  onclick="javascript:history.go(-1)">
		</td>
	</tr>
</table>
		<table width="70%" class="default">
			<col width="30%"/>
			<col width="40%"/>			
			<!--
			<tr>
				<td>区域ID</td>
				<td><input type="text" name="areaId" id="areaId" class="input" value="0"></td>
			</tr>
			-->
			<tr class="title" ><td colspan="2">项目评分</td></tr>
			<tr>
				<td class="item">评分项目名称</td>
				<td>
					${scoreMap.PROJECT_NAME}
					<input type="hidden" name="projectId" value="${scoreMap.PROJECT_ID}">
				</td>
			</tr>
			<#if Session["_LOGIN_USER_"]?exists&&Session["_LOGIN_USER_"].TYPE==1>
			<tr>
				<td class="item">教师评分</td>
				<td>
		     		<#if scoreMap.TEACHER_SCORE?exists>
		     			已评
		     		<#else>
		     			<font color="red">未评</font>		     		
		     		</#if>
				</td>
			</tr>
			<tr>
				<td class="item">学生评分情况</td>
				<td>
		     		已评人数：${scored} ， 应评人数：${stuNum}
				</td>
			</tr>
			<tr>
				<td class="item">项目总分</td>
				<td>
					<#if scoreMap.SCORE?exists>
		     			已汇总：${scoreMap.SCORE}
		     		<#else>
		     			<font color="red">未汇总</font>		     		
		     		</#if>
				</td>
			</tr>	
			
			</#if>		
		</table>
		
	<table width="70%" class="default">
    <col width="5%"/>
    <col width="5%"/>
    <col width="15%"/>
    <col width="15%"/>
    <col width="10%"/>
    <col width="20%"/>        
    <tr class="title">
   	  <td>序号</td>
      <td>组号</td>      
      <td>学号</td>
      <td>姓名</td>
      <td>是否评分</td>
      <td>评分时间</td>
    </tr>
    <#assign webIndex=1>
 		<#list scoreInfoList as score>	
    	<tr <@p.trChangeColor/>>
    		<td>${webIndex}</td>
     		<td>${score.GROUP_ID}</td>     		
      		<td>${score.STU_ID}</td>
      		<td>
      		${score.STU_NAME}
     		<#if score.GROUP_LEADER==1>
     		(组长)
     		</#if>     		
     		</td>
     		<td>
     		<#if score.STU_STATE==1>
     		已评
     		<#else>
     		<font color="red">未评</font>	
     		</#if>
     		<td>
     		<#if score.SCORE_TIME?exists>
     			${score.SCORE_TIME?string('yyyy年M月d日 HH时mm分ss秒')}
     		<#else>
     			-
     		</#if>
     		</td>
     		<#assign webIndex=webIndex+1>     		
		</#list>	
  </table>	
</@p.page>