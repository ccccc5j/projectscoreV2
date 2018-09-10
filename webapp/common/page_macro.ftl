<#include "config.ftl">
<#include "common_function.ftl">

<#macro page navi={} title=macro_config.default_title body="" head="" simple="false" setReferUrl=false>
<#if setReferUrl>${action.setReferUrl()}</#if>
<#if (navi?size > 0)>
    <#assign keys = navi?keys>
	<#if title==macro_config.default_title>
		<#local title = navi[keys?last]>
	</#if>
</#if>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${macro_config.common_title}</title>
<meta http-equiv="Content-type" content="text/html; charset=${macro_config.charset}">
<meta http-equiv="X-UA-Compatible" content="IE=8" />

<meta HTTP-EQUIV="pragma" CONTENT="no-cache"> 
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
<meta HTTP-EQUIV="expires" CONTENT="0"> 

<link href="${base}${macro_config.css_path}" rel="stylesheet" type="text/css">

<script language="javascript" src="${base}${macro_config.js_path}"></script>
<script language="javascript" type="text/javascript" src="${base}/js/jquery-1.4.4.min.js"></script>

<link rel="stylesheet" href="${base}/css/zTreeStyle/zTreeStyle.css" type="text/css"/>
<script language="javascript" type="text/javascript" src="${base}/js/jquery.ztree.core-3.5.js"></script>


${head}
</head>
<body ${body}>
<div id="main_box">
<div id="header" class="layout">
  <div id="top">
   <!-- <div class="lable"><a href="${base}/score/score!scoreList.action">我的评分</a></div> -->
    <div class="lable"><a href="${base}/changePassView.action">修改密码</a></div>
    <div class="lable"><a href="${base}/logout.action">退出登录</a></div>

  </div>
  <div class="name">
    <div class="name-box">欢迎您
    <#if Session["_LOGIN_USER_"]?exists>， 
    	<#switch Session["_LOGIN_USER_"].TYPE>
    			<#case 0>${Session["_LOGIN_USER_"].STU_NAME}【学生，学号：${Session["_LOGIN_USER_"].STU_ID}】<#break>
			<#case 1>${Session["_LOGIN_USER_"].TEACHER_NAME}【教师】<#break>
	
		</#switch>
	</#if>！
	</div>
  </div>
  	 <div id="menu">  
	    <ul id="nav">
	     <li><a href="#">评分管理</a>
	        <ul>
	          <li><a href="${base}/score/score!scoreList.action">项目评分</a></li>
	          	<#if Session["_LOGIN_USER_"]?exists&&(Session["_LOGIN_USER_"].TYPE==1||
	          	(Session["_LOGIN_USER_"].TYPE==0&&Session["_LOGIN_USER_"].GROUP_LEADER==1))> 
	          	 <li><a href="${base}/score/score!scoreFactorView.action">得分系数</a></li>
	          	</#if> 
	        </ul>
	      </li>
   			<#if Session["_LOGIN_USER_"]?exists&&Session["_LOGIN_USER_"].TYPE==1> 
	     <li><a href="#">系统管理</a>
	        <ul>
	          <li><a href="${base}/score/project!projectList.action">项目管理</a></li>
	          <li><a href="${base}/score/project!projectList.action">小组管理</a></li>	
	          <li><a href="${base}/score/project!projectList.action">学生管理</a></li>
	        </ul>
	      </li>
	      </#if> 
	    </ul>
	  </div>

<div id="main_middle" class="layout">
        <div class="content-div">
		
          <#if (navi?size > 0)>
          <div class="navi-icon"><img alt="" src="${base}/images/icon.jpg" /></div>
          <div class="site">当前位置：       
            <#assign keys = navi?keys>
            <#list keys as key>&gt;&gt;<a href="${parseLink(navi[key])}"> ${key}</a> </#list>
            </div>
          </#if> 
              
          <br>
          <div align="center" class="layout">
          <#nested> 
          </div>
   		 </div>
  		</div>
	<br>
  <div id="footer" class="layout">
    <div class="text"> Copyright © 2014 - 2015   cj </div>
  </div>
 </div>
</body>
</html>
</#macro>

<#macro intro tasks={}>
 <div class="explain-box">
 	<div class="explain"><#nested></div>
 </div>
<#if (tasks?size > 0)>
<div style="margin-top:5px;margin-top:5px">
<#assign keys = tasks?keys>
<#list keys as key>
<a href="${parseLink(tasks[key])}" class="navlink">${key}</a> 
</#list>
</div>
</#if>
</#macro>

<#macro trClass>
<#if lineNumber?exists>
<#assign lineNumber=lineNumber+1/>
<#else>
<#assign lineNumber=1/>
</#if>
class="style${lineNumber%2}"
</#macro>

<#macro explain width="100%" align="center">
<table class="explain" width="${width}" align="${align}">
<tr>
	<td>
	<#nested>
	</td>
</tr>
</table>
</#macro>

<#macro mustMark><span class="notNull">*</span></#macro>

<#macro menuMark><span style="font-family:color:#FF3300;font-weight:bold;">&gt;&gt;</span></#macro>

<#macro folderMark><span style="font-family:Wingdings;color:#FF3300;font-weight:normal;font-size:18px;">0</span></#macro>

<#macro fileMark><span style="font-family:Wingdings;color:#FF3300;font-weight:normal;font-size:22px;padding-left:3px;padding-right:2px;">3</span></#macro>

<#macro chartColumn width height="8" color="#0066CC"><span style="background:${color};height:${height};width:${width}"></span></#macro>

<#macro trChangeColor>
 onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#BFDFFF'"
</#macro>

<#macro trChangeDataColor>
 onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#E1F2B6'"
</#macro>

<#-- 处理分页参数 -->
<#function getPageUrl pageNum>
<#local pageUrl=base+fullUrlWithoutPageNum>
<#if pageUrl?ends_with("?")>
<#return pageUrl + "pageNum=" + pageNum>
<#else>
<#return pageUrl + "&pageNum=" + pageNum>
</#if>
</#function>

<#-- 分页信息 -->
<#-- 分页信息 -->
<#macro paging pagingList>
<#local pageCount=pagingList.pageCount>
<#local rowCount=pagingList.rowCount>
<#local pageNum=pagingList.pageNum>
<#local pageSize=pagingList.pageSize>
<#if rowCount == 0>
	<#if request.servletPath?starts_with("/manage")>
		<#if useFlag?exists>
		<div style="border:1px solid #666;padding:2 5 2 5;background:#efefef;color:#333">没有相关记录</div>
		<#else>
		<#assign useFlag = 1>
		</#if>
	<#else>
		<#if !useFlag?exists>
		<div style="border:1px solid #666;padding:2 5 2 5;background:#efefef;color:#333">没有相关记录</div>
		<#assign useFlag = 1>
		</#if>
	</#if>
<#else>
<table>
<tr >	
	<td style="line-height:150%" >
	
	共 ${rowCount} 条记录 ${pageCount} 页
	<#if (pageCount <= 11)>
		<#local startPage = 1>
		<#local endPage = pageCount>
	<#elseif (pageNum + 5 > pageCount)>
		<#local startPage = pageCount - 10>
		<#local endPage = pageCount>
	<#elseif (pageNum - 5 < 1)>
		<#local startPage = 1>
		<#local endPage = 11>
	<#else>
		<#local startPage = pageNum - 5>
		<#local endPage = pageNum + 5>
	</#if>
	<#if (pageCount > 1)>
	<#if (pageNum != 1)>
	<#if (pageCount > 11)>
	<a class="page" href="${getPageUrl(1)}" style="font-family:Webdings" title="首页">9</a>
	</#if>
	<a class="page" href="${getPageUrl(pageNum-1)}" style="font-family:Webdings" title="上页">&lt;</a>
	<#else>
	<#if (pageCount > 11)><span style="font-family:Webdings;color:#999">9</span></#if>
	<span style="font-family:Webdings;color:#999">&lt;</span></#if>
	<#list startPage..endPage as x>
	<#if x=pageNum>
	<span class="selectedPage">${x}</span>
	<#else>
	<span class="noSelectedPage"><a class="page" href="${getPageUrl(x)}">${x}</a></span>
	</#if>
	</#list>
	<#if (pageCount != pageNum)>
	<a class="page" href="${getPageUrl(pageNum+1)}" style="font-family:Webdings" title="下页">&gt;</a>
	<#if (pageCount > 11)>
	<a class="page" href="${getPageUrl(pageCount)}" style="font-family:Webdings" title="尾页">:</a>
	</#if>
	<#else><span style="font-family:Webdings;color:#999">&gt;</span>
	<#if (pageCount > 11)><span style="font-family:Webdings;color:#999">:</span></#if>
	</#if>
	</#if>	
	</td>	
</tr>
</table>
</#if>
</#macro>

<#-- 处理缩进 len 为ID长度，pre为前置内容 marker为项标记 -->
<#macro indent len pre="　" marker="|-">
<#local indentNum = len/3?int>
<#if (indentNum>1)><#list 2..indentNum as x>${pre}</#list></#if>${marker}
</#macro>

<#-- 日历控件 -->
<#macro cal name format="%Y-%m-%d" text="选择日期">
<#if calcount?exists>
<#assign calcount=calcount+1/>
<#else>
<#assign calcount=0/>
</#if>
<img id="cal${calcount}" src="${base}/components/calendar/skins/aqua/cal.gif" border="0" alt="${text}" style="cursor:pointer">
<script language="JavaScript">var cal${calcount} = calendar("${name}", "cal${calcount}", "${format}");</script>
</#macro>

<#-- 分割线 -->
<#macro tabLine colspan="1">
<tr>
	<td colspan="${colspan}" style="height:10px"></td>
</tr>
<tr>
	<td colspan="${colspan}" style="height:1px" background="${base}/images/table_bz_03.gif"></td>
</tr>
<tr>
	<td colspan="${colspan}" style="height:10px"></td>
</tr>
</#macro>

<#-- 交替变色 -->
<#macro trClass>
<#if count?exists>
<#assign count=count+1/>
<#else>
<#assign count=0/>
</#if>
<#if count%2=0>class="contenttd1"<#else>class="contenttd2"</#if>
</#macro>

<#-- 学习页面头部 -->
<#macro learnhead>
<table width="94%" border="0" cellspacing="0" cellpadding="0" align="center" bgcolor="F5F5F4">
    <tr>
        <td align="center"><a href="learning!latelyList.action"><img src="images/xxzx_09.gif" alt="" width="89" height="76" border="0"></a></td>
		<td align="center"><a href="learning!allList.action"><img src="images/xxzx_07.gif" alt="" width="65" height="76" border="0"></a></td>
	    <td align="center"><a href="learning!finishList.action"><img src="images/xxzx_11.gif" alt="" width="89" height="76" border="0"></a></td>
	    <td align="center"><a href="learning!overList.action"><img src="images/xxzx_13.gif" alt="" width="89" height="76" border="0"></a></td>
	</tr>
	<tr>
	    <td align="center"><a href="learning!latelyList.action"><font color="#000000">最近学习的课程</font></a></td>
		<td align="center"><a href="learning!allList.action"><font color="#000000">全部课程</font></a></td>
	    <td align="center"><a href="learning!finishList.action"><font color="#000000">已完成的课程</font></a></td>
	    <td align="center"><a href="learning!overList.action"><font color="#000000">过期课程</font></a></td>
	</tr>
</table>
</#macro>

<#-- 表格数据上方内容格式 -->
<#macro info>
<div class="info"><#nested></div>
</#macro>