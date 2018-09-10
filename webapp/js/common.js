<!--
function back(){
	history.go(-1);
}

function onclickdivbutton()
{
	var left_tree = document.getElementById("left_tree");
	var divl = document.getElementById("lrdiv");
	var rightTree = document.getElementById("m_right_tree");
	var m_left = document.getElementById("m_left");	
	if (divl.className == "div_hoverimg")
	{
		divl.className = "divclose_hoverimg";
		rightTree.className='m_right_tree_close';
		left_tree.style.display = 'none';
		m_left.style.width='9';
	}
	else
	{
		divl.className = "div_hoverimg";
		left_tree.style.display = 'block';
		rightTree.className='m_right_tree';
		m_left.style.width='229';
	}
}

function resizeWindow(){
	var heightR=document.getElementById("m_right_tree").scrollHeight; 
	var heightL=document.getElementById("m_left").scrollHeight; 
	if (heightR>heightL){ 
		document.getElementById("m_left").style.height=heightR+"px"; 
		document.getElementById("lrdiv").style.height=heightR+"px"; 
	} else{
		document.getElementById("lrdiv").style.height=heightL+"px"; 
		document.getElementById("m_right_tree").style.height=heightL+"px";
	}
	ifIE8Up();
}
// 自动调整 div的高度
function showHideTree(objname)
{
	// 只对主菜单设置cookie
	var obj = document.getElementById(objname);
		if(obj.style.display == 'block')
			obj.style.display = 'none';
		else
			obj.style.display = 'block';
		return true;
}

function showHide(objname)
{
	// 只对主菜单设置cookie
	var obj = document.getElementById(objname);
	if(objname.indexOf('_1')<0 || objname.indexOf('_10')>0)
	{
		if(obj.style.display == 'block' || obj.style.display =='')
			obj.style.display = 'none';
		else
			obj.style.display = 'block';
		return true;
	}
  // 正常设置cookie
	var ckstr = getCookie('menuitems');
	var ckstrs = null;
	var okstr ='';
	var ischange = false;
	if(ckstr==null) ckstr = '';
	ckstrs = ckstr.split(',');
	objname = objname.replace('items','');
	if(obj.style.display == 'block' || obj.style.display =='')
	{
		obj.style.display = 'none';
		for(var i=0; i < ckstrs.length; i++)
		{
			if(ckstrs[i]=='') continue;
			if(ckstrs[i]==objname){  ischange = true;  }
			else okstr += (okstr=='' ? ckstrs[i] : ','+ckstrs[i] );
		}
		if(ischange) setCookie('menuitems',okstr,7);
	}
	else
	{
		obj.style.display = 'block';
		ischange = true;
		for(var i=0; i < ckstrs.length; i++)
		{
			if(ckstrs[i]==objname) {  ischange = false; break; }
		}
		if(ischange)
		{
			ckstr = (ckstr==null ? objname : ckstr+','+objname);
			setCookie('menuitems',ckstr,7);
		}
	}
}
// 读写cookie函数
function getCookie(c_name)
{
	if (document.cookie.length > 0)
	{
		c_start = document.cookie.indexOf(c_name + "=")
		if (c_start != -1)
		{
			c_start = c_start + c_name.length + 1;
			c_end   = document.cookie.indexOf(";",c_start);
			if (c_end == -1)
			{
				c_end = document.cookie.length;
			}
			return unescape(document.cookie.substring(c_start,c_end));
		}
	}
	return null
}
function setCookie(c_name,value,expiredays)
{
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + expiredays);
	document.cookie = c_name + "=" +escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString()); // 使设置的有效时间正确。增加toGMTString()
}
// 检查以前用户展开的菜单项
var totalitem = 12;
function CheckOpenMenu()
{
	// setCookie('menuitems','');
	var ckstr = getCookie('menuitems');
	var curitem = '';
	var curobj = null;
	
	// cross_obj = document.getElementById("staticbuttons");
	// setInterval("initializeIT()",20);
	
	if(ckstr==null)
	{
		ckstr='1_1,2_1,3_1';
		setCookie('menuitems',ckstr,7);
	}
	ckstr = ','+ckstr+',';
	for(i=0;i<totalitem;i++)
	{
		curitem = i+'_'+curopenItem;
		curobj = document.getElementById('items'+curitem);
		if(ckstr.indexOf(curitem) > 0 && curobj != null)
		{
			curobj.style.display = 'block';
		}
		else
		{
			if(curobj != null) curobj.style.display = 'none';
		}
	}
}

var curitem = 1;
function ShowMainMenu(n)
{
	var curLink = $DE('link'+curitem);
	var targetLink = $DE('link'+n);
	var curCt = $DE('ct'+curitem);
	var targetCt = $DE('ct'+n);
	if(curitem==n) return false;
	if(targetCt.innerHTML!='')
	{
		curCt.style.display = 'none';
		targetCt.style.display = 'block';
		curLink.className = 'mm';
		targetLink.className = 'mmac';
		curitem = n;
	}
	else
	{
		var myajax = new DedeAjax(targetCt);
		myajax.SendGet2("index_menu_load.php?openitem="+n);
		if(targetCt.innerHTML!='')
		{
			curCt.style.display = 'none';
			targetCt.style.display = 'block';
			curLink.className = 'mm';
			targetLink.className = 'mmac';
			curitem = n;
		}
		DedeXHTTP = null;
	}
}

// CheckBox全选
function selectAll(formName, cbName) {
	var o = document.forms(formName).item(cbName);
	if (o.length) {// 判断是否只有一项
		for (i = 0; i < o.length; i++) {
			document.forms(formName).item(cbName)[i].checked = true;
		}
	} else {
		o.checked = true;
	}
}
// CheckBox取消全选
function unSelectAll(formName, cbName) {
	var o = document.forms(formName).item(cbName);
	if (o.length) {// 判断是否只有一项
		for (i = 0; i < o.length; i++) {
			document.forms(formName).item(cbName)[i].checked = false;
		}
	} else {
		o.checked = false;
	}
}

/* shf添加 */
// 删除字符串左边的空格
function ltrim(str) { 
	if(str.length==0)
		return(str);
	else {
		var idx=0;
		while(str.charAt(idx).search(/\s/)==0)
			idx++;
		return(str.substring(idx));
	}
}



/* 以下来自于原框架项目 */

/* 表单验证相关脚本 */
function trim(str) {
	if(str==null) return "";
	if(str.length==0) return "";
	var i=0,j=str.length-1,c;
	for(;i<str.length;i++) {
		c=str.charAt(i);
		if(c!=' ') break;
	}
	for(;j>-1;j--) {
		c=str.charAt(j);
		if(c!=' ') break;
	}
	if(i>j) return "";
	return str.substring(i,j+1); 
}

function getStringLength(str) {
	var endvalue=0;
	var sourcestr=new String(str);
	var tempstr;
	for (var strposition = 0; strposition < sourcestr.length; strposition ++) {
		tempstr=sourcestr.charAt(strposition);
		if (tempstr.charCodeAt(0)>255 || tempstr.charCodeAt(0)<0) {
			endvalue=endvalue+2;
		} else {
			endvalue=endvalue+1;
		}
	}
	return(endvalue);
}
//为数字
function isNumber(input) {
	var isNumber = /^[\d]+$/;
	if(isNumber.test(input)){
		return true;
	}
	return false;
}

//为小数或整数
function isFloat(input) {
	var isNumber = /^[\d]+[.]*[\d]*$/;
	if(isNumber.test(input)){
		return true;
	}
	return false;
}
//为空或数字
function isNullOrNumber(input) {
	var isNumber = /^[\d]*$/;
	if(isNumber.test(input)){
		return true;
	}
	return false;
}
// 匹配包括下划线的任何单词字符
function isNotChinese(input) {
	var isNotChinese = /^\w*$/;
	if(isNotChinese.test(input)){
		return true;
	}
	return false;
}



function validateDate(date,format,alt) {
	var time=trim(date.value);
	if(time=="") return;
	var reg=format;
	var reg=reg.replace(/yyyy/,"[0-9]{4}");
	var reg=reg.replace(/yy/,"[0-9]{2}");
	var reg=reg.replace(/MM/,"((0[1-9])|1[0-2])");
	var reg=reg.replace(/M/,"(([1-9])|1[0-2])");
	var reg=reg.replace(/dd/,"((0[1-9])|([1-2][0-9])|30|31)");
	var reg=reg.replace(/d/,"([1-9]|[1-2][0-9]|30|31))");
	var reg=reg.replace(/HH/,"(([0-1][0-9])|20|21|22|23)");
	var reg=reg.replace(/H/,"([0-9]|1[0-9]|20|21|22|23)");
	var reg=reg.replace(/mm/,"([0-5][0-9])");
	var reg=reg.replace(/m/,"([0-9]|([1-5][0-9]))");
	var reg=reg.replace(/ss/,"([0-5][0-9])");
	var reg=reg.replace(/s/,"([0-9]|([1-5][0-9]))");
	reg=new RegExp("^"+reg+"$");
	if(reg.test(time)==false) {// 验证格式是否合法
		alert(alt);
		date.focus();
		return false;
	}
	return true;
}
function validateDateGroup(year,month,day,alt) {
	var array=new Array(31,28,31,30,31,30,31,31,30,31,30,31);
	var y=parseInt(year.value,10);
	var m=parseInt(month.value,10);
	var d=parseInt(day.value,10);
	var maxday=array[m-1];
	if(m==2) {
		if((y%4==0&&y%100!=0)||y%400==0) {
			maxday=29;
		}
	}
	if(d>maxday) {
		alert(alt);
		return false;
	}
	return true;
}
function validateCheckbox(obj,alt) {
	var rs=false;
	if(obj!=null) {
		if(obj.length==null) {
			return obj.checked;
		}
		for(i=0;i<obj.length;i++) {
			if(obj[i].checked==true) {
				return true;
			}
		}
	}
	alert(alt);
	return rs;
}
function validateRadio(obj,alt) {
	var rs=false;
	if(obj!=null) {
		if(obj.length==null) {
			return obj.checked;
		}
		for(i=0;i<obj.length;i++) {
			if(obj[i].checked==true) {
				return true;
			}
		}
	}
	alert(alt);
	return rs;
}
function validateSelect(obj,alt) {
	var rs=false;
	if(obj!=null) {
		for(i=0;i<obj.options.length;i++) {
			if(obj.options[i].selected==true) {
				return true;
			}
		}
	}
	alert(alt);
	return rs;
}
function validateEmail(email,alt,separator) {
	var mail=trim(email.value);
	if(mail=="") return;
	var em;
	var myReg = /^[_a-z0-9]+@([_a-z0-9]+\.)+[a-z0-9]{2,3}$/;
	if(separator==null) {
		if(myReg.test(email.value)==false) {
			alert(alt);
			email.focus();
			return false;
		}
	} else {
		em=email.value.split(separator);
		for(i=0;i<em.length;i++) {
			em[i]=em[i].trim();
			if(em[i].length>0&&myReg.test(em[i])==false) {
				alert(alt);
				email.focus();
				return false;
			}
		}
	}
	return true;
}

// 是否是电话号
function isTel(tel) {
	var charcode;
	if (tel.length!=8&&tel.length!=11)return false;//8位或者11位
	for (var i=0; i<tel.length; i++)	
	{
		charcode = tel.charCodeAt(i);
		if (charcode < 48 && charcode != 45 || charcode > 57)	
			return false;
	}
	return true;
}


/**  
 * 身份证15位编码规则：dddddd yymmdd xx p   
 * dddddd：地区码   
 * yymmdd: 出生年月日   
 * xx: 顺序类编码，无法确定   
 * p: 性别，奇数为男，偶数为女  
 * <p />  
 * 身份证18位编码规则：dddddd yyyymmdd xxx y   
 * dddddd：地区码   
 * yyyymmdd: 出生年月日   
 * xxx:顺序类编码，无法确定，奇数为男，偶数为女   
 * y: 校验码，该位数值可通过前17位计算获得  
 * <p />  
 * 18位号码加权因子为(从右到左) Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2,1 ]  
 * 验证位 Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]   
 * 校验位计算公式：Y_P = mod( ∑(Ai×Wi),11 )   
 * i为身份证号码从右往左数的 2...18 位; Y_P为脚丫校验码所在校验码数组位置  
 *   
 */  
  
//身份证号码简单验证，若是15位应全为数字，若是18位前17应全为数字
function isCardNo(idCard) {
	
    idCard = trim(idCard.replace(/ /g, "")); 
	var charcode;	
	if (!(idCard.length == 15 || idCard.length == 18))	{
		alert( "身份证号码位数不对，应该为15位或者18位!");
		return false;
	}	
	for (var i=0; i<idCard.length-1; i++)	
	{ 
		charcode=idCard.charCodeAt(i);
		if (charcode < 48 || charcode > 57)	{
			alert( "身份证号码包含了非法字符!");
			return false;
		}
	}
	charcode=idCard.charCodeAt(idCard.length-1);
	
	if (idCard.length == 15 && (charcode<48||charcode> 57))	{
		alert( "身份证号码包含了非法字符!");
		return false;
	}
	if (idCard.length == 18 && (charcode<48||charcode> 57)&&(charcode!= 88)&&(charcode!=120))	{
		alert( "身份证号码包含了非法字符!");
		return false;
	}

	if(!isCorrectAreaCode(idCard)){
		alert("身份证号码前两位地区码数字错误！");
		return false;
	}

    if (!isCorrectBrithDay(idCard)) {   
    	alert("身份证号码出生日期错误！");
		return false;  
    } 

    if (idCard.length == 18&&!isCorrect18IdCard(idCard)){   
    	alert( "身份证号码错误，请仔细核实!");
    	return false;   
     } 
	return true;
}

function isCorrectAreaCode(idCard) { 
	var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",
			21:"辽宁",22:"吉林",23:"黑龙江 ",
			31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",
			41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",
			51:"四川",52:"贵州",53:"云南",54:"西藏 ",
			61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",
			71:"台湾",81:"香港",82:"澳门",91:"国外"};	
	//地区检验 
	if (area[parseInt(idCard.substring(0, 2),10)] == null)
	{
		return false;
	}
	 return true;
}   
/**  
 * 判断身份证号码为18位时最后的验证位是否正确  
 * @param idCard 身份证号码数组  
 * @return  
 */  
function isCorrect18IdCard(idCard) { 
	var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];// 加权因子   
	var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];// 身份证验证位值.10代表X  
	var a_idCard = idCard.split("");// 得到身份证数组 
    var sum = 0; // 声明加权求和变量   
    if (a_idCard[17].toLowerCase() == 'x') {   
        a_idCard[17] = 10;// 将最后位为x的验证码替换为10方便后续操作   
    }   
    for ( var i = 0; i < 17; i++) {   
        sum += Wi[i] * a_idCard[i];// 加权求和   
    }   
    var valCodePosition = sum % 11;// 得到验证码所位置   
    if (a_idCard[17] == ValideCode[valCodePosition]) {   
        return true;   
    } else {
        return false;   
    }   
}   
   
 /**  
  * 验证身份证号码中的生日是否是有效生日  
  * @param idCard 身份证字符串  
  * @return  
  */  
function isCorrectBrithDay(idCard){ 
	  if (idCard.length == 15) 
	  {    //验证15位数身份证号码中的生日是否是有效生日  
		  var year =  idCard.substring(6,8);   
		  var month = idCard.substring(8,10);   
		  var day = idCard.substring(10,12);   
		  var temp_date = new Date(year,parseInt(month,10)-1,parseInt(day,10));   
		  // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
		  if(temp_date.getYear()!=parseInt(year,10)   
		          ||temp_date.getMonth()!=parseInt(month,10)-1   
		          ||temp_date.getDate()!=parseInt(day,10)){			 
		      return false;   
		  }else{   
		      return true;   
		  } 
	  } 
	  else if (idCard.length == 18) 
	  {   //验证15位数身份证号码中的生日是否是有效生日 
		 // 510721197008050697
		    var year =  idCard.substring(6,10);   
		    var month = idCard.substring(10,12);   
		    var day = idCard.substring(12,14); 
		    var temp_date = new Date(parseInt(year,10),parseInt(month,10)-1,parseInt(day,10));   
		    // 这里用getFullYear()获取年份，避免千年虫问题   
		    if(temp_date.getFullYear()!=parseInt(year,10)   
		          ||temp_date.getMonth()!=parseInt(month,10)-1   
		          ||temp_date.getDate()!=parseInt(day,10)){		    
		        return false;   
		    }else{   
		        return true;   
		    } 	    	
	  }  
}   




// 验证是否有空格
function haveSpace(str) {
	var flage = false;  
	ss = str.split(" ");// 半角空格
	if(ss.length!=1){
		flage =  true;
	}
	ss = str.split("　");// 全角空格
	if(ss.length!=1){
		flage =  true;
	}
	return flage;
}
// 是否选中单选按钮
function isSelectRadio(rName) {  
    var flage = false;  
    var radios = document.getElementsByName(rName);  
    for (var i = 0; i < radios.length; i++) {  
        if (radios[i].checked == true) {  
            flage = true;  
            alert("选择了第" + i + "个单选按钮");  
            return true;  
        }  
    }  
    if (!flage) {  
        // alert("一个单选按钮都没有选择！");
        return false;  
    }  
}  

// ,a,b,
function comparetime3(a,b,alt)
{
	var time1 = a.substring(0,2).concat(a.substring(3,5)).concat(a.substring(6,10));
	var time2 = b.substring(0,2).concat(b.substring(3,5)).concat(b.substring(6,10));
	
	if(time2 == null || time2 == "")
	{
		return true;
	}
	else if((time1)>time2)
	{
		alert(alt);
		return false;
	}
	else
	{	
		return true;
	}
}
// 时间比较
function duibi(a,b)
{
	var arr=a.split("-");
	var starttime=new Date(arr[0],arr[1],arr[2]);
	var starttimes=starttime.getTime();
	var arrs=b.split("-");
	var lktime=new Date(arrs[0],arrs[1],arrs[2]);
	var lktimes=lktime.getTime();
	if(starttimes>lktimes){
		return false;
	}else
		return true;
}

/* 验证表单，若通过则返回true */

function validateForm(theForm) {
	var disableList=new Array();
	var field = theForm.elements;// 将表单中的所有元素放入数组
	for(var i = 0; i < field.length; i++) {
	
		// 先判断是否设定了不验证表单
		/*
		 * var vali=theForm.getAttribute("validate"); if(vali!=null) {
		 * if(vali.value=="0") { var fun=vali.functionName; if(fun!=null) {
		 * alert("validateForm 384"); return eval(fun+"()"); } else {
		 * 
		 * return true; } } }
		 */
		var empty=false;
		var value=trim(field[i].value);
		if(value.length==0) {// 是否空值
			empty=true;
		}
		var notFocus=field[i].getAttribute("notFocus");// 不置焦点
		if (notFocus!=null) {
			notFocus = true;
		}
		var emptyInfo=field[i].getAttribute("emptyInfo");// 空值验证
		if(emptyInfo!=null&&empty==true) {
			alert(emptyInfo);
			if (!notFocus){
				field[i].focus();
			}
			return false;
		}
		var spaceInfo=field[i].getAttribute("spaceInfo");// 空格验证
		if(spaceInfo!=null) {
			if (haveSpace(value)) {
				alert(spaceInfo);
				field[i].focus();
				return false;
			}
		}
		
		var nfEmptyInfo=field[i].getAttribute("emptyInfo");// 不focus的空值验证
		if(nfEmptyInfo!=null&&empty==true) {
			alert(nfEmptyInfo);
			return false;
		}
		var lengthMaxInfo=field[i].getAttribute("lengthMaxInfo");// 最大长度验证
		if(lengthMaxInfo!=null&&getStringLength(value)>field[i].getAttribute("maxLen")) {
			alert(lengthMaxInfo);
			field[i].focus();
			return false;
		}
		var lengthMinInfo=field[i].getAttribute("lengthMinInfo");// 最小长度验证
		if(lengthMinInfo!=null&&getStringLength(value)<field[i].getAttribute("minLen")) {
			alert(lengthMinInfo);
			field[i].focus();
			return false;
		}
		var minValueInfo=field[i].getAttribute("minValueInfo");// 最小数值验证
		if(minValueInfo!=null&&parseFloat(value)<parseFloat(field[i].getAttribute("minValue"))) {
			alert(minValueInfo);
			field[i].focus(); 
			return false;
		}
		
		var maxValueInfo=field[i].getAttribute("maxValueInfo");// 最大数值验证
		if(maxValueInfo!=null&&parseFloat(value)>parseFloat(field[i].getAttribute("maxValue"))) {			
			alert(maxValueInfo);
			field[i].focus();
			return false;
		}
		var numberInfo=field[i].getAttribute("numberInfo");// 是否是正整数
		if (numberInfo!=null) {
			if (!isFloat(value)) {
				alert(numberInfo);
				field[i].select();
				return false;
			}
		}
		
		var haveIdcardInfo=field[i].getAttribute("haveIdcardInfo");// 是否已经入库
		if (haveIdcardInfo!=null) {
			// alert(haveIdcardInfo);
			// alert(value);
			// var flag=true;
			var xmlHttp= new ActiveXObject("Microsoft.XMLHTTP");
			createXMLHttpRequest();
			var url = "checkup!validate.action?factor="+value;
			// alert(url);
			xmlHttp.open("get", url, true);
		xmlHttp.onreadystatechange=function(){
			if(xmlHttp.readyState == 4) { 
				if (xmlHttp.status == 200) { 
					alert(xmlHttp.responseText);
					window.self.location="checkup!search.action?factor="+value;
				}else {
					alert("验证身份证号请求失败，错误码=" + xmlHttp.status);
				}
			 }
		}
			xmlHttp.send(null);
		}
		
		var noTelnumInfo=field[i].getAttribute("noTelnumInfo");// 是否为电话号码
		if (noTelnumInfo!=null) {
			if (!isTel(value)) {
				alert(noTelnumInfo);
				field[i].focus();
				return false;
			}
		}
		

		var noCardNo=field[i].getAttribute("noCardNo");// 验证身份证号码
		if (noCardNo!=null) {
			if (!isCardNo(value)) {
				//alert(noCardNo);
				field[i].focus();
				return false;
				}
		}		
		var noChineseInfo=field[i].getAttribute("noChineseInfo");// 是否为汉字
		if (noChineseInfo!=null) {
			if (!isNotChinese(value)) {
				alert(noChineseInfo);
				field[i].focus();
				return false;
			}
		}
		
		
		var numberOrNullInfo=field[i].getAttribute("numberOrNullInfo");// 是否是空或者正整数
		if (numberOrNullInfo!=null) {
			if (!isNullOrNumber(value)) {
				alert(numberOrNullInfo);
				field[i].select();
				return false;
			}
		}
		
		var NoChineseInfo=field[i].getAttribute("NoChineseInfo");// 是否为汉字
																	// 且可以为空
		if (NoChineseInfo!=null) {
			if (!isNotChinese(value)&&value.length!=0) {
				alert(NoChineseInfo);
				field[i].focus();
				return false;
			}
		}	
		
		var errorTimeInfo=field[i].getAttribute("errorTimeInfo");// 判断日期区间合法性
		if (errorTimeInfo!=null) {
			var a=document.getElementById("startDate").value;
			var b=document.getElementById("endDate").value;
			if (!duibi(a,b)) {
				alert(errorTimeInfo);
				// field[i].focus();
				return false;
			}
		}	
		
		var disabledFalse=field[i].getAttribute("disabledFalse");// 提交时设置disabled字段设置为false
		if (disabledFalse!=null) {
			field[i].disabled=false;
		}
		
		var validatorType=field[i].getAttribute("validatorType");	
		if(validatorType!=null) {// 其它javascript
			var rs=true;
			if(validatorType=="javascript") {
				eval("rs="+field[i].getAttribute("functionName")+"()");
				if(rs==false) {
					return false;
				} else {
					continue;
				}
			} else if (validatorType=="disable") {// 提交表单前disable的按钮
				disableList.length++;
				disableList[disableList.length-1]=field[i];
				continue;
			} else if (validatorType=="date") {
				rs=validateDate(theForm.elements(field[i].name),field[i].getAttribute("format"),field[i].getAttribute("errorInfo"));
			} else if (validatorType=="dateGroup") {
				rs=validateDateGroup(theForm.elements(field[i].getAttribute("year")),theForm.elements(field[i].getAttribute("month")),theForm.elements(field[i].getAttribute("day")),field[i].getAttribute("errorInfo"));
			} else if(validatorType=="timeCmp") {
			    rs=comparetime3(theForm.elements(field[i].getAttribute("startdate")).value,theForm.elements(field[i].getAttribute("enddate")).value,field[i].getAttribute("errorInfo"));
		    } else if (validatorType=="checkbox") {
				rs=validateCheckbox(theForm.elements(field[i].name),field[i].getAttribute("errorInfo"));
			} else if (validatorType=="radio") {
				rs=validateRadio(theForm.elements(field[i].name),field[i].getAttribute("errorInfo"));
			} else if (validatorType=="select") {
				rs=validateSelect(theForm.elements(field[i].name),field[i].getAttribute("errorInfo"));
			} else if (validatorType=="email") {
				rs=validateEmail(theForm.elements(field[i].name),field[i].getAttribute("errorInfo"));
			} else {
				alert("验证类型不被支持, fieldName: "+field[i].name);
				return false;
			}

			if(rs==false) {
				return false;
			}
		} else {// 一般验证
			if(empty==false) {
				var v = field[i].getAttribute("validator"); // 获取其validator属性
				if(!v) continue;            // 如果该属性不存在,忽略当前元素
				var reg=new RegExp(v);
				if(reg.test(field[i].value)==false) {
					alert(field[i].getAttribute("errorInfo"));
					field[i].focus();
					return false;
				}
			}
		}
	}
	for(i=0;i<disableList.length;i++) {
		disableList[i].disabled=true;
	}
	return true;
}
/* iframe 高度自适应 */
var getFFVersion=navigator.userAgent.substring(navigator.userAgent.indexOf("Firefox")).split("/")[1]
var FFextraHeight=getFFVersion>=0.1? 16 : 0 

function dyniframesize(iframename) {
  var pTar = null;
  if (document.getElementById){
    pTar = document.getElementById(iframename);
  }
  else{
    eval('pTar = ' + iframename + ';');
  }
  if (pTar && !window.opera){
    // begin resizing iframe
    pTar.style.display="block"
    
    if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){
      // ns6 syntax
      pTar.height = pTar.contentDocument.body.offsetHeight+FFextraHeight; 
    }
    else if (pTar.Document && pTar.Document.body.scrollHeight){
      // ie5+ syntax
      pTar.height = pTar.Document.body.scrollHeight;
    }
  }
}
// CheckBox删除
function del(formname,cbname){
	
	var a = document.getElementsByName(cbname);
	var sum=0;
	var c=false;
	for(i=0;i< a.length; i++)
	{
		if(a[i].checked){
		sum++;
		}
	}
	if(sum>0)
	{
	c=window.confirm('确定删除？')
		if(c)
		{
		document.forms(formname).submit();
		}
	}
	else
	{
	alert('您还没有选择');
	}	
}
// CheckBox操作
function docheck(formname,cbname){
	
	var a = document.getElementsByName(cbname);
	var c=false;
	for(i=0;i< a.length; i++)
	{
		if(a[i].checked){
		sum++;
		}
	}
	if(sum>0)
	{
	document.forms(formname).submit();
	}
	else
	{
	alert('您还没有选择');
	}	
}
// 判断页面上输入的两个值是否相同,相同提示错误,不同执行操作
function unequal(formname,inputname0,inputname2)
{
	var a = trim(inputname0.value);
	var b = trim(inputname2.value);
	if(a==b)
	{
	alert('不可以重复');	
	}
	else
	{
	document.forms(formname).submit();
	}
}
// 判断时间先后顺序,a为前面的时间,b为后面的时间,错误则提示
function comparetime(a,b)
{
	var time1 = a.substring(0,4).concat(a.substring(5,7)).concat(a.substring(8,10));
	var time2 = b.substring(0,4).concat(b.substring(5,7)).concat(b.substring(8,10));
	if((time1)>time2)
	{
		alert('时间顺序错误');
		return false;
	}
	else
	{	
		return true;
	}
}




// Cookie操作

function Setcookie (name, value) { // 设置名称为name,值为value的Cookie
	document.cookie = name + "=" + value + "";
} 

function Deletecookie (name) { // 删除名称为name的Cookie
	var exp = new Date(); 
	exp.setTime (exp.getTime() - 1); 
	var cval = GetCookie (name); 
	document.cookie = name + "=" + cval + "; expires=" + exp.toGMTString(); 
} 

function getCookieVal (offset) { // 取得项名称为offset的cookie值
	var endstr = document.cookie.indexOf (";", offset); 
	if (endstr == -1) 
	endstr = document.cookie.length; 
	return unescape(document.cookie.substring(offset, endstr)); 
} 

function GetCookie (name) { // 取得名称为name的cookie值
	var arg = name + "="; 
	var alen = arg.length; 
	var clen = document.cookie.length; 
	var i = 0; 
	while (i < clen) { 
		var j = i + alen; 
		if (document.cookie.substring(i, j) == arg) 
		return getCookieVal (j); 
		i = document.cookie.indexOf(" ", i) + 1; 
		if (i == 0) break; 
	} 
	return null; 
} 

function comparetime2(a,a2,a3,b,b2,b3){ // 比较时间顺序，精确到分钟
	var time1 = a.substring(0,4).concat(a.substring(5,7)).concat(a.substring(8,10)).concat(a2).concat(a3);
	var time2 = b.substring(0,4).concat(b.substring(5,7)).concat(b.substring(8,10)).concat(b2).concat(b3);

	if((time1)>time2)
	{
		alert('时间顺序错误');
		return false;
	}
	else
	{	
		return true;
	}
}

/* 判断选项中重复项 */
function checkOptionSame(formSrc, optNum, optPre) {
	var z=0;
	var a=new Array(optNum);
	for (var i=0; i<optNum; i++) {
		var index = i+1;
		a[i] = formSrc[optPre + index].value;
		if (a[i]=="") {
			z = i;
			break;
		}
	}
	for(var i=0; i<z; i++) {
		for (var j=i+1; j<=z; j++) {
			if(a[i]==a[j]) {
				alert("选项中有重复的内容！");
				return false;
			}
		}
	}
	return true;
}

/* div 显示隐藏相关脚本 */
function changediv(divid) {
	clearalldiv();
	showdiv(divid);
}
function cleardiv(divid) {
	document.getElementById(divid).style.display="none";
}
function showdiv(divid) {
	document.getElementById(divid).style.display="";
}

//检测浏览器版本是否是IE8以上
function ifIE8Up(){
	var userAgent = navigator.userAgent.toLowerCase();
	rMsie = /(msie\s|trident.*rv:)([\w.]+)/;
	var match = rMsie.exec(userAgent);
	if ((match != null)) {
		if ((match[2]<7)) {
			alert("经检测，您当前使用的浏览器是IE，但系统要求使用IE8以上（含IE8）版本！调试信息："+match[2]);
			location.replace("/iip/logout.action");
		}
	}else{
		alert("经检测，您当前使用的浏览器不是IE，系统要求使用IE8以上（含IE8）版本！调试信息："+match[2]);
		location.replace("/iip/logout.action");
	}
}

-->