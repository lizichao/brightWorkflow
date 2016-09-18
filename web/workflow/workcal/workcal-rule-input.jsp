<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/js/jquery/plugin/zebra_dialog/zebra_dialog.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/js/jquery/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/DatePicker/WdatePicker.js"></script>
<script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.js" type="text/javascript"></script>
<title>�༭������������</title>

<script type="text/javascript">
var iframeObj = window.parent.frames[0];
var paramObject = window.dialogVal() || iframeObj.dialogVal();
$(document).ready(function(){
	//var workcalRuleObject = paramObject.workcalRuleObject;
	var workcalRuleId = paramObject.workcalRuleId;
	if(workcalRuleId){
		var bcReq = new BcRequest('workflow','workcalRuleController','getWorkcalRuleSingle');
		bcReq.setExtraPs({"workcalRuleId":workcalRuleId});
		bcReq.setSuccFn(function(data,status){
			var _resultRec = data.Data[0];
			var year = _resultRec.year;
			var week = _resultRec.week;
			var workcalRuleName = _resultRec.name;
			var workDate = _resultRec.work_date;
			var status = _resultRec.status;
			var workcalTypeId = _resultRec.type_id;
			if(workcalRuleId){
				$("#workcalRuleId").val(workcalRuleId);
			}
			if(year){
				$("#year").val(year);
			}
			if(week){
				$("#week").val(week);
			}
			if(workcalRuleName){
				$("#workcalRuleName").val(workcalRuleName);
			}
			if(workDate){
				$("#workDate").val(workDate);
			}
			if(status){
				$("#status").val(status);
				if(status != '0'){
					$("#liweek").hide();
				}else{
					$("#liweek").show();
				}
			}
			for(var i=0;i<_resultRec.Types.length;i++){
				var typeObject = _resultRec.Types[i];
				var workcalTypeObject = $("#workcalTypeId");
				if(workcalTypeId == typeObject.workcalTypeId){
					workcalTypeObject.append( "<option value=\'"+typeObject.workcalTypeId+"\' selected='selected'>"+typeObject.workcalTypeName+"</option>");
				}else{
					workcalTypeObject.append( "<option value=\'"+typeObject.workcalTypeId+"\'>"+typeObject.workcalTypeName+"</option>");
				}
			}
			/*
			if(workcalTypeId){
				 $("#workcalTypeId").val(workcalTypeId);
			}*/
		});
		  bcReq.postData();	
		}else{
			var bcReq = new BcRequest('workflow','workcalTypeController','queryWorkcalType');
			bcReq.setSuccFn(function(data,status){
				for(var i=0;i<data.Data.length;i++){
					var _resultRec = data.Data[i];
					var workcalTypeObject = $("#workcalTypeId");
					workcalTypeObject.append( "<option value=\'"+_resultRec.id+"\'>"+_resultRec.name+"</option>");
				}
			});
			bcReq.postData();
		}
	/*
	var year = paramObject.year;
	var week = paramObject.week;
	var workcalRuleName = paramObject.workcalRuleName;
	var workDate = paramObject.workDate;
	var status = paramObject.status;
	var workcalTypeId = paramObject.workcalTypeId;
	if(workcalRuleId){
		$("#workcalRuleId").val(workcalRuleId);
	}
	if(year){
		$("#year").val(year);
	}
	if(week){
		$("#week").val(week);
	}
	if(workcalRuleName){
		$("#workcalRuleName").val(workcalRuleName);
	}
	if(workDate){
		$("#workDate").val(workDate);
	}
	if(status){
		$("#status").val(week);
	}
	if(workcalTypeId){
	}*/
});


function submitWorkcalRule(){
	var workcalRuleId =  $("#workcalRuleId").val() || "";
	var year =$("#year").val();
	var week =$("#week").val();
	var workcalRuleName =$("#workcalRuleName").val();
	var workDate =$("#workDate").val();
	var status =$("#status").val();
	var workcalTypeId =$("#workcalTypeId").val();
	 
	var bcReq = new BcRequest('workflow','workcalRuleController','workcalRuleInput');
	bcReq.setExtraPs({
		"workcalRuleId":workcalRuleId,
		"year":year,
		"week":week,
		"workcalRuleName":workcalRuleName,
		"workDate":workDate,
		"status":status,
		"workcalTypeId":workcalTypeId
	});
	bcReq.setSuccFn(function(data,status){
		 window.parent.location.reload();
		 iframeObj.closeDialog();
		 
	});
	bcReq.postData();
}


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

function statusChange(){
	var statusVal = $("#status").val();
	if(statusVal != '0'){
		$("#week").val("");
		$("#liweek").hide();
		//$("#week").attr("disabled",true);
	}else{
		$("#liweek").show();
		//$("#week").attr("disabled",false);
	}
}
</script>
</head>
<body>
<div class="hi-box hi-xzdllc" style="height: 330px;">   
<div class="hi-main">

<div class="hi-hd">
<h3><i class="ico-xzclr"></i>������������</h3> 
</div>

<div class="hi-form">
<ul>
<li><label class="form-label">��ȣ�</label>
    <div class="formControls">
    	 <input type="hidden" id="workcalRuleId"  name="workcalRuleId" value=""/>
		 <input type="text" id="year" name="year"  size="40"  minlength="2" maxlength="10">
    </div> 
</li>
<li id="liweek">
 	<label class="form-label">�ܣ�</label>
	<div class="formControls hi-select" >
	 	<select id="week" name="week">
		    <option value=""></option>
		    <option value="2">��һ</option>
		    <option value="3">�ܶ�</option>
		    <option value="4">����</option>
		    <option value="5">����</option>
		    <option value="6">����</option>
		    <option value="7">����</option>
		    <option value="1">����</option>
	     </select> 
	</div>  
</li>
<li><label class="form-label">���ƣ�</label>
    <div class="formControls">
    	<input type="text" id="workcalRuleName" name="workcalRuleName"  size="40"  minlength="2" maxlength="10">
    </div> 
</li>
<li><label class="form-label">���ڣ�</label>
    <div class="formControls">
      <input id="workDate" type="text" name="workDate"  size="40" class="text" onclick="selectDeleteTime()">
    </div> 
</li>

<li>
 	<label class="form-label">״̬��</label>
	<div class="formControls hi-select" >
	 	 <select id="status" name="status" onchange="statusChange()">
		    <option value="0">����</option>
		    <option value="1">�ڼ���</option>
		    <option value="2">����</option>
		    <option value="3">����</option>
		  </select>
	</div>  
</li>

<li>
 	<label class="form-label" style="width:90px">�����������ͣ�</label>
	<div class="formControls hi-select" >
	 	 <select id="workcalTypeId" name="workcalTypeId">
	     </select>
	</div>  
</li>
</ul>
</div>

<div class="hi-page">
<div class="hi-btn"><input name="" type="submit" value="�� ��" class=" hi-radius3" onclick="submitWorkcalRule()" /></div>  
</div>


</div>
</div>
<script type="text/javascript">
$(".hi-table tr:odd").addClass("odd-row");
</script>

<!-- 
<table align="center" width="288" border="0" cellpadding="0" cellspacing="0">  
  <tr align="center" >
		<td height="14" width="130">���</td>
		<td height="14" width="130">
		 <input type="hidden" id="workcalRuleId"  name="workcalRuleId" value=""/>
		 <input type="text" id="year" name="year"  size="40"  minlength="2" maxlength="10">
		</td>
   </tr>
    <tr align="center" >
		<td height="14" width="130">��</td>
		<td height="14" width="130">
		<select id="week" name="week">
		    <option value="2">��һ</option>
		    <option value="3">�ܶ�</option>
		    <option value="4">����</option>
		    <option value="5">����</option>
		    <option value="6">����</option>
		    <option value="7">����</option>
		    <option value="1">����</option>
	     </select>
	 	</td>
   </tr>
    <tr align="center" >
		<td height="14" width="130">����</td>
		<td height="14" width="130">
		  <input type="text" id="workcalRuleName" name="workcalRuleName"  size="40"  minlength="2" maxlength="10">
		</td>
   </tr>
   <tr align="center" >
		<td height="14" width="130">����</td>
		<td height="14" width="130">
		  <input id="workDate" type="text" name="workDate"  size="40" class="text">
		</td>
   </tr>
  <tr align="center">
		<td height="14" width="130">״̬</td>
		<td height="14" width="130">
		 <select id="status" name="status">
		    <option value="0">����</option>
		    <option value="0">�ڼ���</option>
		    <option value="0">����</option>
		    <option value="0">����</option>
		  </select>
		</td>
   </tr>
   <tr align="center" >
		<td height="14" width="130">������������</td>
		<td height="14" width="130">
		 <select id="workcalTypeId" name="workcalTypeId">
	     </select>
		</td>
   </tr>
</table> 
<input type='button' id="" value="�ύ" onclick="submitWorkcalRule()" />
 -->
</body>
</html>