<%@ page contentType="text/html; charset=GBK" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>������������</title>

<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/js/jquery/plugin/zebra_dialog/zebra_dialog.css" rel="stylesheet" type="text/css" />

<!--<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>  -->
<script type="text/javascript" src="/js/jquery/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/workflow/js/divselect.js"></script>
<script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.js" type="text/javascript"></script>

<script>
var iframeObj = window.parent.frames[0];
var paramObject = window.dialogVal() || iframeObj.dialogVal();
function addDelegateTask(){
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	if(endtime<starttime){
		alert("����ʱ�������ڿ�ʼʱ�䣬������ѡ��");
		return false;
	}
	var delegateUser = $("#delegateuser option:selected").val();
	if(delegateUser==""){
		alert("�����˲���Ϊ�գ�������ѡ��");
		return false;
	}
	var delegateUserName = $("#delegateuser option:selected").text();
	var delegateProcess = $("#delegateprocess option:selected").val();
	var delegateProcessName = $("#delegateprocess option:selected").text();
	
	var addDelegateObject  = {
			"startTime":starttime,
			"endTime":endtime,
			"delegateUser":delegateUser,
			"delegateUserName":delegateUserName,
			"processDefKey":delegateProcess,
			"processDefName":delegateProcessName
	}
	iframeObj.closeDialogAndReturn(addDelegateObject);
	/*
	var bcReq = new BcRequest('workflow','delegateTaskAction','addDelegateTask');
	
	bcReq.setExtraPs({
		"startTime":starttime,
		"endTime":endtime,
		"delegateUser":delegateUser,
		"delegateUserName":delegateUserName,
		"processDefKey":delegateProcess,
		"processDefName":delegateProcessName,
	});
	bcReq.setSuccFn(function(data,status){
		//iframeObj.closeDialog();
		iframeObj.closeDialogAndReturn(returnUser);
		//window.parent.location.href="/workflow/delegate/delegateTask.jsp";
		
	});
	bcReq.postData();*/
}
	

$(document).ready(function(){	
	var bcReq = new BcRequest('workflow','delegateTaskAction','searchDelegateAddConfig');
	
	bcReq.setExtraPs({
	});
	bcReq.setSuccFn(function(data,status){
		$(".hi-table tr:odd").addClass("odd-row");
		
		var recordData = data.Data[0];
		var delegateUserArray = recordData.DelegateUser;
		var delegateProcessArray = recordData.DelegateProcess;
		
		var delegateUserSel = $("#delegateuser");
		 delegateUserSel.append( "<option value=''>��ѡ�������</option>" );
		for (var i=0;i<delegateUserArray.length;i++){
	      var delegateUserSingle = delegateUserArray[i];
	      delegateUserSel.append( "<option value=\'"+delegateUserSingle.userId+"\'>"+delegateUserSingle.userName+"</option>" );
		}
		 
		 
		for (var i=0;i<delegateProcessArray.length;i++){
	      var delegateProcessSingle = delegateProcessArray[i];
	      var delegateprocessSel = $("#delegateprocess");
	      delegateprocessSel.append( "<option value=\'"+delegateProcessSingle.processDefKey+"\'>"+delegateProcessSingle.processDefName+"</option>" );
		}
		delegateprocessSel.append( "<option value='all' selected='selected' >All</option>" );
		$(".hi-table tr:odd").addClass("odd-row");
	});
	bcReq.postData();
});	

function selectDeleteTime(timeObject){
	WdatePicker({
		readOnly:true,
		isShowClear:false,
		isShowOthers:false,
		maxDate:'2099-12-31 23:59:59',
		onpicked:function(dp){
			//getMessageRecord(dp)
		}
	});
}
</script>
</head>

<body>
<!--����-->

<div class="hi-box hi-xzdllc">   
<div class="hi-main">

<div class="hi-hd">
<h3><i class="ico-xzclr"></i>������������</h3> 
</div>

<div class="hi-form">
<ul>
<li><label class="form-label">��ʼʱ�䣺</label>
    <div class="formControls"><input id="starttime" type="text" onclick="selectDeleteTime()" >
    </div> 
</li>
<li><label class="form-label">����ʱ�䣺</label><div class="formControls"><input id="endtime" type="text" onclick="selectDeleteTime()"  ></div>  </li>
<li>
 	<label class="form-label">�����ˣ�</label>
	<div class="formControls hi-select" >
	   <select name="delegateuser" id="delegateuser"class="select">
	   </select>   
	</div>  
</li>
<li>
    <label class="form-label">�������̣�</label>
    <div class="formControls hi-select" >
        <select id="delegateprocess" name="delegateprocess" class="select">
		</select>   
	</div>    
</li>
</ul>
</div>

<div class="hi-page">
<div class="hi-btn"><input name="" type="submit" value="�� ��" class=" hi-radius3" onclick="addDelegateTask()" /></div>  
</div>


</div>
</div>

<!--����-->

<script type="text/javascript">
$(".hi-table tr:odd").addClass("odd-row");
</script>
</body>



