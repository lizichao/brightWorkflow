<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/yuexue/public/header.jsp"%>
<title>加减签</title>

<script id="counterNodeListRec" type="text/x-jsrender">	
<table id="counterSignTab" class="table table-bordered table-striped table-hover">
<thead> 
<tr>
<th>节点名字</th>
<th>分配人</th>
</tr>
</thead>
<tbody id="counterSignTby"> 
{{for Data}}
    <tr>
      <input type="hidden" id="collectionVariable_{{:#index+1}}"  name="collectionVariable_{{:#index+1}}" value="{{:collectionVariable}}">
      <input type="hidden" id="collectionElementVariable_{{:#index+1}}"  name="collectionElementVariable_{{:#index+1}}" value="{{:collectionElementVariable}}">
      <input type="hidden" id="counterSignNodeId_{{:#index+1}}"  name="counterSignNodeId_{{:#index+1}}" value="{{:id}}">
      <td>{{:name}}</td>
      <td><input type="text" id="assignee_{{:#index+1}}" name="assignee_{{:#index+1}}" value=""></td>
    </tr>
{{/for}}
</tbody> 
</table>
</script>

<script type="text/javascript">
var iframeObj = window.parent.frames[0];
var paramObject = iframeObj.dialogVal();
$(document).ready(function(){
	var processInstanceId = paramObject.processInstanceId;
	var processDefId = paramObject.processDefId;
	var bcReq = new BcRequest('workflow','componentAction','queryCounterNodes');
	bcReq.setExtraPs({"PageSize":"10","PageNo":1,"query_processInstanceId":processInstanceId,'query_processDefId':processDefId});
	bcReq.setSuccFn(function(data,status){
		 $("#counterNodeList").empty();
		 if(data.Data.length>0){
			 var _stuContent=$("#counterNodeListRec").render(data);
			 $("#counterNodeList").append(_stuContent);
		//	 $("#ext-comp-1012").show();
             //setPageInfo(data);
		 }else{
		   $("#counterNodeList").append("<span style='width:100%;color:red;'>暂无数据</span>");
		  // $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
});

function submitCounterSign(counterSignOperate){
	var counterSignArray = [];
	var processInstanceId = paramObject.processInstanceId;
	 $("#counterSignTby tr").each(function(i){
		var collectionVariable = $(this).find("input[id='collectionVariable_"+(i+1)+"'").val();
		var collectionElementVariable = $(this).find("input[id='collectionElementVariable_"+(i+1)+"'").val();
		var counterSignNodeId = $(this).find("input[id='counterSignNodeId_"+(i+1)+"'").val();
		var assignee = $(this).find("input[id='assignee_"+(i+1)+"'").val();
		counterSignArray.push(JSON.stringify({
			'collectionVariable' : collectionVariable,
			'collectionElementVariable' : collectionElementVariable,
			'counterSignNodeId' :counterSignNodeId,
			'assignee': assignee
		}));
	});
	//JSON.stringify(counterSignArray);
	 
	var bcReq = new BcRequest('workflow','componentAction','counterSignOperate');
	//var taskId = paramObject.taskId;
	 
	//var taskId = $("#taskId").val();
	//var componentParam = {'taskId':taskId,'transferUserId':transferSelVal};
	bcReq.setExtraPs({
		"componentName":"counterSignOperate",
		"processInstanceId":processInstanceId,
		"counterSignOperate":counterSignOperate,
		"componentParam":counterSignArray
		}
	);
	bcReq.setSuccFn(function(data,status){
	});
	bcReq.postData();
}
</script>
</head>
<body>
<div style="width:90%;margin-left:20px;margin-top:10px;text-align:center" id="counterNodeList">
</div>
<!--  
<div id="modal" class="modal hide fade">
  <div class="modal-body">
  <form>
    <input type="hidden" name="humanTaskId" value="${humanTaskId}"/>
       <div class="input-append userPicker">
	  <input type="hidden" name="userId" class="input-medium" value="">
	  <input type="text" style="width: 175px;" value="">
	  <span class="add-on"><i class="icon-user"></i></span>
       </div>
	<br>
	<button class="btn">提交</button>
  </div>
</div>
-->
<table align="center" width="288" border="0" cellpadding="0" cellspacing="0">  
   
</table> 
<input type='button' id="" value="加签" onclick="submitCounterSign('add')" />
<input type='button' id="" value="减签" onclick="submitCounterSign('remove')" />
</body>
</html>