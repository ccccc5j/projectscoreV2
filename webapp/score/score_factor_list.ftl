<@p.page navi = {
	"设置或查看得分系数":"" } 
setReferUrl=true>
<@p.intro >这里您可以设置或查看得分系数。</@p.intro>
<table width="98%" >
	<tr>
		<td align="left"  nowrap class="rd19">
			<input type="button"  class="btn" value="返回"  onclick="javascript:history.go(-1)">
		</td>
	</tr>
</table>
<form action="score!scoreFactor.action" method="post" onsubmit="return validateForm(this)"> 
<table width="80%" class="default">
    <col width="5%"/>
    <col width="15%"/>
    <col width="15%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="5%"/> 
    <col width="20%"/>           
    <tr class="title">
   	  <td>序号</td>
      <td>班级</td>      
      <td>组名</td>
      <td>学号</td>
      <td>姓名</td>
      <td>是否组长</td>
      <td>得分系数</td>
    </tr>
    <#assign webIndex=1>
 		<#list scoreFactorList as factor>	
    	<tr <@p.trChangeColor/>>
    		<td>${webIndex}</td>
     		<td>${factor.CLASS_NAME}</td>     		
      		<td>${factor.GROUP_NAME}</td>
      		<td>${factor.STU_ID}</td>
      		<td>
      		${factor.STU_NAME}
      		</td>
      		<td>
     		<#if factor.GROUP_LEADER==1>
     		组长
     		</#if>     		
     		</td>
     		<td>
     		<#if Session["_LOGIN_USER_"]?exists&&Session["_LOGIN_USER_"].TYPE==1> 
     			<#if factor.SCORE_FACTOR?exists>
     			已填写
     			<#else>
     			未填写
     			</#if>
			<#else>  	
				<input type="hidden" name="studentIds" value="${factor.STU_ID}"> 
				<#if factor.SCORE_FACTOR ?exists>
     				<input type="password" name="factors"  class="input" emptyInfo="请输入小数（0-1）！" value="${factor.SCORE_FACTOR?string('#.###')}"
							 numberInfo="请输入0-1之间的小数！" minValue="0" minValueInfo="分数不能小于0!" maxValue="1" maxValueInfo="分数不能大于1!">
     		    <#else>
     				<input type="password" name="factors"  class="input" emptyInfo="请输入小数（0-1）！" value=""
							 numberInfo="请输入0-1之间的小数！" minValue="0" minValueInfo="分数不能小于0!" maxValue="1" maxValueInfo="分数不能大于1!">
     		    </#if>			 			
     	</#if>
     		</td>
     		<#assign webIndex=webIndex+1>     		
		</#list>
		<#if Session["_LOGIN_USER_"]?exists&&Session["_LOGIN_USER_"].TYPE==0> 
		<tr>
			<td colspan="3" align="right">				
				为便于联系请组长留下电话：
			</td>
			<td colspan="4" align="left">				
				<input type="text" name="phone" value="${phone}" emptyInfo="请输入您的联系电话！"
				noTelnumInfo="请输入正确的电话号码！"
				> 
			</td>
		<tr>
		
		<tr>
			<td colspan="7">				
				<input type="submit" name="submit" value="确定">
				<input type="reset" name="reset" value="重置">
			</td>
		<tr>
		</#if>	
  </table>	
  </form>
</@p.page>