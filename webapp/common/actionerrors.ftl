<#if errorInfo?exists>
<table class="errorBox" align="center">
<tr>
<td class="errorMessage">
<ul>
	<li><font color="red">${errorInfo?default("")}</font>
	</li>
</ul>
</td>
</tr>
</table>
</#if>