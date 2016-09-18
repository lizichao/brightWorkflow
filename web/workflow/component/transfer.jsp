<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>转审</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>


<script type="text/javascript">
var iframeObj = window.parent.frames[0];
var paramObject = window.dialogVal() || iframeObj.dialogVal();
$(document).ready(function(){
	var processInstanceId = paramObject.processInstanceId;
	var processDefId = paramObject.processDefId;
	var bcReq = new BcRequest('workflow','componentAction','queryTransferUsers');
	//bcReq.setExtraPs({"PageSize":"10","PageNo":1,"query_processInstanceId":processInstanceId,'query_processDefId':processDefId});
	bcReq.setSuccFn(function(data,status){
		 for (var i=0;i<data.Data.length;i++){
		      var _resultRec = data.Data[i];
		      var transferSel = $("#transferSel");
		      transferSel.append( "<option value=\'"+_resultRec.userid+"\'>"+_resultRec.username+"</option>" );
		  }
	});
	bcReq.postData();
});

function submitTransfer(){
	var transferSelVal = $("#transferSel option:selected").val();
	var transferUserName = $("#transferSel option:selected").text();
	var bcReq = new BcRequest('workflow','componentAction','transferComponent');
	
	// var iframeObj = $(window.parent).find("iframe");
	$(window.parent).find("iframe:first")
	 var iframeObj = window.parent.frames[0];
	 var taskId = paramObject.taskId;
	 var processInstanceId = paramObject.processInstanceId;
	 var processDefKey = paramObject.processDefKey;
	 var processDefName = paramObject.processDefName;
	 var processDefId = paramObject.processDefId;
	 var taskDefKey = paramObject.taskDefKey;
	 
	//var taskId = $("#taskId").val();
	var componentParam = {
			'processDefKey':processDefKey,
			'processDefName':processDefName,
			'processInstanceId':processInstanceId,
			'processDefId':processDefId,
			'taskId':taskId,
			'taskDefKey':taskDefKey,
			'transferUserId':transferSelVal,
			'transferUserName':transferUserName
	};
	bcReq.setExtraPs({"componentName":"transfer","componentParam":JSON.stringify(componentParam)});
	bcReq.setSuccFn(function(data,status){
		 window.parent.location.href = "/workflow/workspace/myTask.jsp";
		 iframeObj.closeDialog();
	});
	bcReq.postData();
}

</script>
</head>
<body>

<div class="hi-box hi-zs">   
<div class="hi-main">

<div class="hi-hd">
<h3><i class="ico-zs"></i>转审</h3> 
</div>


<div class="hi-form">
<ul>
<li>

<div class="formControls hi-select" > 
	<select name="transferSel" id="transferSel">
	</select>   
</div>  
</li>
</ul>
</div>

<div class="hi-page">
<div class="hi-btn"><input name="" type="button" value="提 交" class="hi-radius3"  onclick="submitTransfer()"/></div>  
</div>


</div>
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
<!--  
<table align="center" width="288" border="0" cellpadding="0" cellspacing="0">  
    <tr>  
        <td colspan="4">  
            <select name="transferSel" id="transferSel"  style="width:100%">  
                <!-- <option value="1">管理员</option>  
                <option value="2">超级管理员</option>   -
            </select>  
        </td>  
    </tr>  
</table> 
<input type='button' id="" value="提交" onclick="submitTransfer()" />
-->
</body>
</html>