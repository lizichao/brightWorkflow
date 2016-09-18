<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>加签</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>

<script id="addCounterSignListRec" type="text/x-jsrender">	
<table id="counterSignTab" class="table table-bordered table-striped table-hover">
<thead> 
<tr>
<th>操作</th>
<th>序号</th>
<th>用户账号</th>
<th>用户名称</th>
</tr>
</thead>
<tbody id="counterSignTby"> 
{{for Data}}
    <tr>
      <td><input type='checkbox' id='userChk_{{:#index+1}}' name='userChkName' value='{{:userId}}' /></td>
      <input type="hidden" id="userId_{{:#index+1}}"  name="userId_{{:#index+1}}" value="{{:userId}}">
      <td>{{:#index+1}}</td>
      <td>{{:userCode}}</td>
      <td>{{:userName}}</td>
    </tr>
{{/for}}
</tbody> 
</table>
</script>

<script type="text/javascript">
var iframeObj = window.parent.frames[0];
var paramObject = window.dialogVal() || iframeObj.dialogVal();
var toAddCounterNode = paramObject.toAddCounterNode;
var multiKind = paramObject.multiKind;
var multiInstance = toAddCounterNode.multiType;
$(document).ready(function(){
	var dataObject = {'Data': []};
	/*
	for(var i =0;i<toAddCounterNode.multiDepartments.length;i++){
		var eachToAddCounterNodeDepartment = toAddCounterNode.multiDepartments[i];
		if(getUncheckedDepartment(toAddCounterNode.selectedMultiDepartments,eachToAddCounterNodeDepartment.deptId)){
			resultDepartments.push(eachToAddCounterNodeDepartment);
			dataObject.Data.push(eachToAddCounterNodeDepartment)
		}
	}*/
	for(var i =0;i<toAddCounterNode.addCounterUsers.length;i++){
		var eachToAddCounterNodeUser = toAddCounterNode.addCounterUsers[i];
		//	resultDepartments.push(eachToAddCounterNodeDepartment);
			dataObject.Data.push(eachToAddCounterNodeUser)
	}
	 var content=$("#addCounterSignListRec").render(dataObject);
	 $("#addCounterSignList").append(content);
});


function submitAddCounterSign(){
	var processInstanceId = paramObject.processInstanceId;
	var userChks = [];
	 $('input:checkbox[name=userChkName]:checked').each(function(i){
		 userChks.push($(this).val())
	});
	//$("#counterSignTby checkbox").each(function(i){
	//	departmentChks.push($(this).val())
	//});
	
	var addCounterSignParam = JSON.stringify({
		'collectionVariable' : toAddCounterNode.collectionVariable,
		'collectionElementVariable' : toAddCounterNode.collectionElementVariable,
		'counterSignNodeId' :toAddCounterNode.id,
		'counterUserId': userChks.join(",")
	});
	
	var bcReq = new BcRequest('workflow','componentAction','counterSignOperate');
	//var taskId = paramObject.taskId;
	 
	//var taskId = $("#taskId").val();
	//var componentParam = {'taskId':taskId,'transferUserId':transferSelVal};
	bcReq.setExtraPs({
		"componentName":"counterSignOperate",
		"processInstanceId":processInstanceId,
		"counterSignOperate":"add",
		'processDefKey' :paramObject.processDefKey,
		'processDefId' :paramObject.processDefId,
		'processDefName' :paramObject.processDefName,
		"componentParam":addCounterSignParam,
		"multiKind":multiKind,
		"multiInstance":multiInstance
		}
	);
	bcReq.setSuccFn(function(data,status){
		 window.parent.location.reload();
		// window.location.hash.substring(2);
		 iframeObj.closeDialog();
	});
	bcReq.postData();
}
</script>
</head>
<body>
<div class="hi-box hi-jq">   
<div class="hi-main">

<div class="hi-hd">
<h3><i class="ico-jq"></i>加签</h3> 
</div>

<div id="addCounterSignList" class="hi-table">

</div>

<div class="hi-page">
 <div class="hi-btn"><input name="" type="submit" value="提 交" class=" hi-radius3" onclick="submitAddCounterSign()"/></div>  
</div>

</div>
</div>
</body>
</html>