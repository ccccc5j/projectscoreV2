<@p.page navi = {
	"项目评分":"" 
	} 
setReferUrl=true>
<@p.intro >这里您可以为项目评分。</@p.intro>
<form action="score!editScore.action" method="post" onsubmit="return validateForm(this)"> 
<table width="98%" >
	<tr>
		<td align="left"  nowrap class="rd19">
			<input type="button"  class="btn" value="返回" name="FindReadBtn" onclick=back();>
		</td>
	</tr>
</table>
		<table width="70%" class="default">
			<col width="30%"/>
			<col width="40%"/>
			
			<!--<tr >
				<td>区域ID</td>
				<td><input type="text" name="areaId" id="areaId" class="input" value="0"></td>
			</tr>
			-->
			<tr class="title" ><td colspan="2">项目评分</td></tr>
			<tr><td>评分项目名称</td>
				<td>
					${scoreMap.PROJECT_NAME}
					<input type="hidden" name="projectId" value="${scoreMap.PROJECT_ID}">
				</td>
			</tr>

			<tr><td>给出您的分数<br>（0-100）</td>
				<td>
					<#if Session["_LOGIN_USER_"]?exists>
    					<#if Session["_LOGIN_USER_"].TYPE==1>
							<input type="text" name="score"  class="input" emptyInfo="请输入分数（0-100）！" value="${scoreMap.TEACHER_SCORE?default('')}"
							 numberInfo="请输入0-100的整数！" minValue="0" minValueInfo="分数不能小于0!" maxValue="100" maxValueInfo="分数不能大于100!">
						<#else>
							<input type="text" name="score"  class="input" emptyInfo="请输入分数（0-100）！" value="${scoreMap.STU_SCORE?default('')}"
							  numberInfo="请输入0-100的整数！" minValue="0" minValueInfo="分数不能小于0!" maxValue="100" maxValueInfo="分数不能大于100!">
						</#if>
					</#if>
				</td>
			</tr>			
			<tr>
				<td colspan="2">				
					<input type="submit" name="submit" value="确定">
					<input type="reset" name="reset" value="重置">
				</td>
			<tr>
		</table>
</form>
</@p.page>