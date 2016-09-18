<%@ page contentType="text/html; charset=GBK" %>
<%
	String componentType = request.getParameter("componentType");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>选择用户</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>


<script id="userListRec" type="text/x-jsrender">	
<table class="table-border table-bordered table-striped table">
<thead> 
<tr>
<th>勾选</th>
<th>用户账号</th>
<th>用户名称</th>
<th>所属机构</th>
</tr>
</thead>
{{for Data}}
    <tr>
       <input type="hidden" id="userid{{:#index+1}}"  name="userid{{:#index+1}}" value="{{:userid}}">
       <input type="hidden" id="usercode{{:#index+1}}"  name="usercode{{:#index+1}}" value="{{:usercode}}">
       <input type="hidden" id="username{{:#index+1}}"  name="username{{:#index+1}}" value="{{:username}}">
      <td><input type='checkbox' id='userMultiChk_{{:#index+1}}' name='userMultiChk_{{:#index+1}}' value='{{:#index+1}}' /></td>
      <td>{{:usercode}}</td>
      <td>{{:username}}</td>
      <td>{{:deptname}}</td>
    </tr>
{{/for}}
</table>
</script>

<script type="text/javascript">
var iframeObj = window.parent.frames[0];
var paramObject = window.dialogVal() || iframeObj.dialogVal();
var componentType = paramObject.componentType;
$(document).ready(function(){
	setMethod("loadGridData","22");
	loadGridData("22",1);
});

function loadGridData(gridNum,pageNo){
	var userOrigin = paramObject.userOrigin;
	var bcReq;
	//if(userOrigin =='nodeConfigUser'){
		// bcReq = new BcRequest('workflow','componentAction','queryTransferUsers');
		// bcReq.setExtraPs({'isPage':'true',"PageNo":pageNo, "PageSize": 10});
	//}else{
		 bcReq = new BcRequest('workflow','componentAction','getUserList');
		 bcReq.setExtraPs({"PageNo":pageNo, "PageSize": 10});
	//}
	bcReq.setSuccFn(function(data,status){
		searchSucess(data);
	});
	bcReq.postData();
}

function confirmSelectUser(){
	var selectedUsers = [];
	var selectedUserIds = [];
	var selectedUserNames = [];
	$('input:checkbox:checked').each(function(i){
		    var userIndex = $(this).val();
			var userId =  $('#userid'+userIndex).val();
			var usercode =  $('#usercode'+userIndex).val();
			var username =  $('#username'+userIndex).val();
		    var userObject = {
		    	"userId" : userId,
		    	"userCode" : usercode,
		    	"userName" : username
		    };
		    selectedUsers.push(userObject);
		    selectedUserIds.push(userId);
		    selectedUserNames.push(username);
	});
	var returnUser = {
			'selectedUsers':selectedUsers,
			'selectedUserIds':selectedUserIds,
			'selectedUserNames':selectedUserNames
	};
	//if(componentType == 'addSubTask'){
	//	window.parent.location.href = "/workflow/template/completeTaskForm.jsp?taskId=310034"
	//}
	iframeObj.closeDialogAndReturn(returnUser);
	//window.opener.Brightcom.workflow.selectMultiUserCallBack(componentType,selectedUsers);
	//window.close()
}

function searchUser(){
	var userInfo = $("#userInfo").val();
	var bcReq = new BcRequest('workflow','componentAction','getUserList');
	bcReq.setExtraPs({
		"userInfo":userInfo
	});
	bcReq.setSuccFn(function(data,status){
		searchSucess(data);
	});
	bcReq.postData();
} 	

function searchSucess(data){
	 $(".hi-table tr:odd").addClass("odd-row");
	 $("#userList").empty();
	 if(data.Data.length>0){
		 var userContent=$("#userListRec").render(data);
		 $("#userList").append(userContent);
		 $(".hi-table tr:odd").addClass("odd-row");
		 
		 $('input').iCheck({
			    checkboxClass: 'icheckbox_flat-red',
			    radioClass: 'iradio_flat-red'
		});
		$("#ext-comp-1012").show();
        setPageInfo(data);
	 }else{
	    $("#userList").append("<span style='width:100%;color:red;'>暂无数据</span>");
		$("#ext-comp-1012").hide();
	 }
}
</script>
</head>
<body>
<input type="hidden" id="sysName" name="sysName" value="">
<input type="hidden" id="oprID" name="oprID" value="">
<input type="hidden" id="actions" name="actions" value="">

<div class="hi-box hi-cs">   
<div class="hi-main">

<div class="hi-hd">
<h3><i class="ico-cs"></i>请选择抄送对象</h3> 
<div class="hi-reach hi-radius3">
    <input id="userInfo" name="userInfo" type="text" class="inputt"  />
    <input name="" type="button" class="btnn" onclick="searchUser()" />
</div> 
<div class="hi-btn"><input name="" type="submit" value="确 定" class=" hi-radius3" onclick="confirmSelectUser()" /></div>  
</div>

<div id="userList" class="hi-table">
</div>
   <%@ include file="/workflow/common/pageinfo.jsp"%>
</div>
</div>

</body>
</html>