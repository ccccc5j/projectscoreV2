<#-- 解析通用链接地址 -->
<#function parseLink link>
<#switch link>
	<#case "@back"><#return "javascript:history.go(-1)"><#break>
<#default>
	<#return link>
</#switch>
</#function>


<#-- 截短文本 -->
<#function cutText text maxLen suffix="">
<#if (maxLen < 0)><#return text></#if>
<#if (text?string?length > maxLen)>
	<#return action.substring(text, maxLen*2, suffix)>
<#else>
	<#return text>
</#if>
</#function>