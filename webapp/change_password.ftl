<@p.page navi = {
	"修改密码":"#"	
} 
setReferUrl=true>
<@p.intro >这里您可以修改自己的密码。</@p.intro>
 <form  name="updateForm" action="changePass.action"  method="post" onsubmit="return validateForm(this)"> 
	<table width="60%" class="default" align="center" >
	<col width="5%" align="center" />
	<col width="5%" align="center" />
	<col width="20%" align="center" />
	<col width="20%" align="center" />
	<tr class="title">
		<td colspan="2">修改密码</th>	
	</tr>
	   <tr>
	      <td align="right"><span class="bold_text"><b>原密码</b></span></td>
          <td align="left"><input type="password" name="password0"  class="input" emptyInfo="原密码不能为空！"  minLen="6" lengthMinInfo="原密码长度不能少于6位！" maxLen="50" lengthMaxInfo="原密码长度不能多于50个字符！"   size="30"/></td>
      </tr>
	   <tr>
	      <td align="right"><span class="bold_text"><b>新密码</b></span> </td>
          <td align="left"><input type="password" name="password1"  class="input" emptyInfo="新密码不能为空！" minLen="6" lengthMinInfo="新密码长度不能少于6位！" maxLen="50" lengthMaxInfo="新密码长度不能多于50个字符！"  size="30"/></td>
      </tr>
	  <tr>
	      <td align="right"><span class="bold_text"><b>确认新密码</b></span> </td>
          <td align="left"> <input type="password" name="password2" class="input" emptyInfo="确认新密码不能为空！" minLen="6" lengthMinInfo="确认新密码长度不能少于6位！" maxLen="50" lengthMaxInfo="确认新密码长度不能多于50个字符！" size="30"/></td>
      </tr>
			 <tr>
			 	<td colspan="2" align="center">
				    <input type="submit" name="sumbit" value="确定" class="btn">&nbsp;&nbsp;&nbsp;&nbsp;
				    <input type="reset" name="reset"value="重置" class="btn" >
				</td>
			</tr>
	</table>
</form>
</@p.page>