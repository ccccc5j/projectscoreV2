<#include "/common/config.ftl">
<#include "/common/common_function.ftl">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
<title>${macro_config.common_title}-欢迎登录！</title>
<script language="javascript" src="${base}${macro_config.js_path}"></script>
<meta http-equiv="Content-type" content="text/html; charset=${macro_config.charset}">
<link rel="Stylesheet" href="${base}/css/login.css" type="text/css" />
<script type="text/javascript">
  function reloadCode(obj){
  var rand=new Date().getTime(); //这里用当前时间作为参数加到url中，是为了使URL发生变化，这样验证码才会动态加载，
            //只是一个干扰作用，无确实意义，但却又非常巧妙。
  obj.src="login_view!getCode.action?abc="+rand; //其实服务器端是没有abc的字段的。
  }
</script>
</head>
<body>
<form action="login.action" name="loginForm" onsubmit="return validateForm(this)" method="post" >
  <div class="Login-box">
    <div class="Login-textbox">
      <div class="lable-box">
        <div class="lable">登录名：</div>
        <input type="text"  name="userName" id="userName" size="25" emptyInfo="请输入用户名！" value="${userName?if_exists}"/>
      </div>
      <div class="lable-box">
        <div class="lable">密  码：</div>
        <input type="password" name="password" id="password" size="27" emptyInfo="请输入密码！"  minLen="6" lengthMinInfo="密码长度不能少于6位！" maxLen="50" lengthMaxInfo="密码长度不能多于50个字符！" />
      </div>     
      <div class="lable-box">
        <div class="lable">验证码：</div>
        <input id="randomCode" type="text" name="randomCode" size="5"  emptyInfo="请输入验证码！"/>
        <div class="randomCode-img">
         <img title="看不清楚，点击换一张！" width="75" height="25" src="login_view!getCode.action" id="safecode" onclick="reloadCode(this)" /> 
         </div>
      </div>
      <div class="lable-box">
        <input class="loginBtn" name="submit" type="submit" value="登录" />
        <input class="loginBtn" name="reset" type="reset"  value="重置"  />
      </div>
    </div>
  </div>
</form>
<#include "/common/actionerrors.ftl">
</body>
</html>