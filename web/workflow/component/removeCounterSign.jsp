<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>减签</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>


<script id="removeCounterSignListRec" type="text/x-jsrender">	
<table id="counterSignTab" class="table-border table-bordered table-striped table">
<thead> 
<tr>
<th>操作</th>
<th>序号</th>
<th>部门名称</th>
</tr>
</thead>
<tbody id="counterSignTby"> 
{{for Data}}
    <tr>
      <td><input type='checkbox' id='departmentChk_{{:#index+1}}' name='departmentChkName' value='{{:deptId}}' /></td>
      <input type="hidden" id="departmentId_{{:#index+1}}"  name="departmentId_{{:#index+1}}" value="{{:deptId}}">
      <td>{{:#index+1}}</td>
      <td>{{:deptName}}</td>
    </tr>
{{/for}}
</tbody> 
</table>
</script>

<script type="text/javascript">
var iframeObj = window.parent.frames[0];
var paramObject = window.dialogVal() || iframeObj.dialogVal();
var toRemoveCounterNode = paramObject.toRemoveCounterNode;
var multiKind = paramObject.multiKind;
var multiInstance = toRemoveCounterNode.multiType;
$(document).ready(function(){
	$(".hi-table tr:odd").addClass("odd-row");
	var dataObject = {'Data': []};
	//var resultDepartments = [];
	/*
	for(var i =0;i<toRemoveCounterNode.selectedMultiDepartments.length;i++){
		var eachSelectedMultiDepartmentId = toRemoveCounterNode.selectedMultiDepartments[i];
			//resultDepartments.push(eachToremoveCounterNodeDepartment);
		dataObject.Data.push(eachSelectedMultiDepartmentId)
	}*/
	for(var i =0;i<toRemoveCounterNode.removeCounterDepartments.length;i++){
		var eachToRemoveCounterNodeDepartment = toRemoveCounterNode.removeCounterDepartments[i];
			//resultDepartments.push(eachToAddCounterNodeDepartment);
			dataObject.Data.push(eachToRemoveCounterNodeDepartment)
	}
	 var content=$("#removeCounterSignListRec").render(dataObject);
	 $("#removeCounterSignList").append(content);
});

function submitremoveCounterSign(){
	var processInstanceId = paramObject.processInstanceId;
	var departmentChks = [];
	 $('input:checkbox[name=departmentChkName]:checked').each(function(i){
			departmentChks.push($(this).val())
	});
	//$("#counterSignTby checkbox").each(function(i){
	//	departmentChks.push($(this).val())
	//});
	
	var removeCounterSignParam = JSON.stringify({
		'collectionVariable' : toRemoveCounterNode.collectionVariable,
		'collectionElementVariable' : toRemoveCounterNode.collectionElementVariable,
		'counterSignNodeId' :toRemoveCounterNode.id,
		'counterDepartmentId': departmentChks.join(",")
	});
	
	var bcReq = new BcRequest('workflow','componentAction','counterSignOperate');
	//var taskId = paramObject.taskId;
	 
	//var taskId = $("#taskId").val();
	//var componentParam = {'taskId':taskId,'transferUserId':transferSelVal};
	bcReq.setExtraPs({
		"componentName":"counterSignOperate",
		"processInstanceId":processInstanceId,
		"counterSignOperate":"remove",
		'processDefKey' :paramObject.processDefKey,
		'processDefId' :paramObject.processDefId,
		'processDefName' :paramObject.processDefName,
		"componentParam":removeCounterSignParam,
		"multiKind":multiKind,
		"multiInstance":multiInstance
		}
	);
	bcReq.setSuccFn(function(data,status){
		// window.location.hash.substring(2);
		 window.parent.location.href = "/workflow/workspace/myTask.jsp";
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
<h3><i class="ico-jq"></i>减签</h3> 
</div>

<div id="removeCounterSignList" class="hi-table">

</div>

<div class="hi-page">
 <div class="hi-btn"><input name="" type="submit" value="提 交" class=" hi-radius3" onclick="submitremoveCounterSign()"/></div>  
</div>

</div>
</div>
</body>
</html>