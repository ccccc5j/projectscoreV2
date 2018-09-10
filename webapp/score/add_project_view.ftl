<@p.page navi = {
	"项目管理":"${base}/score/project!projectList.action" } 
setReferUrl=true>
<@p.intro >这里您可以新增评分项目。</@p.intro>
<script type="text/javascript">
        var xmlHttpRes;  
           
        function createXMLHttpRequest() {
            if(window.XMLHttpRequest) {  
                xmlHttpRes = new XMLHttpRequest();  
            } else if(window.ActiveXObject) {  
                xmlHttpRes = new ActiveXobject("Microsoft.XMLHTTP");  
            }
        } 


	<!--采用Ajax获取课程小组-->
	function selectGroupOld(classId){
		$.ajax({
		    async:false,
		    type: "POST",
		    dataType: "json",
		    url: "${base}/score/project!getGroupListOfClass.action",
		    data: {cId:classId},
			timeout:30000, //超时设置，毫秒
			error:function (XMLHttpRequest, textStatus, errorThrown) {
			    if (textStatus == 'timeout') {
			        alert("您的操作请求已经超时！");
			    } else {
			        alert("系统操作出错！");
			    }
			},
			success:function (dataResult) {
				alert(dataResult.groupList);
			}
		});	
	}

        function selectGroup(classId) {
            createXMLHttpRequest();  
            var url = "${base}/score/project!getGroupListOfClass.action?classId="+classId;
            xmlHttpRes.open("post", url, true);  
            xmlHttpRes.onreadystatechange = callback;  
            xmlHttpRes.send(null);  
        }  
           
        function callback() { 
           if(xmlHttpRes.readyState == 4) {  
                if(xmlHttpRes.status == 200) {
					eval(xmlHttpRes.responseText);  
                }  
            }  
        }

    function back() {		
		window.self.location="javascript:history.go(-1)";
	}
	function projectNameInfo(){
		document.getElementById("projectName").value="注意项目名不要包含组名!";
	}
</script>

<form name="addProject" action="project!addProject.action" method="post" onsubmit="return validateForm(this)"> 
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
			<tr class="title" ><td colspan="2">添加评分项目</td></tr>
			<tr><td>请选择班级</td>
				<td>
					<select name="classId" class="input" style="width:220px"  onchange="selectGroup(this.value)">
						<#list classList as aClass>					
							<option value="${aClass.CLASS_ID }">${aClass.CLASS_NAME}</option>
						</#list>
					</select>					
				</td>
			</tr>
			<tr><td>是否选择全部小组？</td>
				<td>
				选择全部小组<input type="checkbox" id="selectAllGroup" name="selectAllGroup" value="true" class="input" >					
				</td>
			</tr>
			<tr><td>可选择小组</td>
				<td>
					<select name="groupId"  id="groupId" class="input" style="width:220px">					
						<#list groupList as aGroup>					
							<option value="${aGroup.GROUP_ID}">${aGroup.GROUP_NAME}</option>
						</#list>
					</select>
				</td>
			</tr>
			<tr><td>评分项目名称</td>
				<td>
					<input type="text" name="projectName" id="projectName" 
					class="input" emptyInfo="请输入项目名！" style="width:220px" 
					onfocus="projectNameInfo()">
				</td>
			</tr>			
			<tr>
				<td colspan="2">				
					<input type="submit" name="addarea" value="确定">
					<input type="reset" name="reset" value="重置">
				</td>
			<tr>
		</table>
</form>

</@p.page>