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
<script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.js" type="text/javascript"></script>
<title>编辑工作日历时间段</title>


<script type="text/javascript">
var iframeObj = window.parent.frames[0];
var paramObject = window.dialogVal() || iframeObj.dialogVal();
$(document).ready(function(){
	var workcalPartId = paramObject.workcalPartId;
	/*
	var shift = paramObject.shift;
	var start_time = paramObject.start_time;
	var end_time = paramObject.end_time;
	var workcalRuleId = paramObject.workcalRuleId;
	var workcalRuleName = paramObject.workcalRuleName;*/
	if(workcalPartId){
		var bcReq = new BcRequest('workflow','workcalPartController','getWorkcalPartSingle');
		bcReq.setExtraPs({"workcalPartId":workcalPartId});
		bcReq.setSuccFn(function(data,status){
			var _resultRec = data.Data[0];
			var shift = _resultRec.shift;
			var start_time = _resultRec.start_time;
			var end_time = _resultRec.end_time;
			var workcalRuleId = _resultRec.workcalruleid;
			var workcalRuleName = _resultRec.workcalrulename;
			if(workcalPartId){
				$("#workcalPartId").val(workcalPartId);
			}
			if(shift){
				$("#shift").val(shift);
			}
			if(start_time){
				$("#startTime").val(start_time);
			}
			if(end_time){
				$("#endTime").val(end_time);
			}
			
			for(var i=0;i<_resultRec.Rules.length;i++){
				var ruleObject = _resultRec.Rules[i];
				var workcalRuleObject = $("#workcalRuleId");
				if(workcalRuleId == ruleObject.workcalRuleId){
					workcalRuleObject.append( "<option value=\'"+ruleObject.workcalRuleId+"\' selected='selected'>"+ruleObject.workcalRuleName+"</option>");
				}else{
					workcalRuleObject.append( "<option value=\'"+ruleObject.workcalRuleId+"\'>"+ruleObject.workcalRuleName+"</option>");
				}
			}
			
			//if(workcalRuleId){
			//	$("#workcalRuleId").val(workcalRuleId);
			//}
			//if(workcalRuleName){
			//	$("#workcalRuleName").val(workcalRuleName);
			//}
		});
		bcReq.postData();
	}else{
		var bcReq = new BcRequest('workflow','workcalRuleController','queryWorkcalRuleNoPage');
		bcReq.setSuccFn(function(data,status){
			for(var i=0;i<data.Data.length;i++){
				var _resultRec = data.Data[i];
				var workcalRuleObject = $("#workcalRuleId");
				workcalRuleObject.append( "<option value=\'"+_resultRec.id+"\'>"+_resultRec.name+"</option>");
			}
		});
		bcReq.postData();
	}
});


function submitWorkcalPart(){
	var workcalPartId =  $("#workcalPartId").val() || "";
	var shift =$("#shift").val();
	var start_time =$("#startTime").val();
	var end_time =$("#endTime").val();
	var workcalRuleId =$("#workcalRuleId").val();
	var workcalRuleName =$("#workcalRuleName").val();
	 
	var bcReq = new BcRequest('workflow','workcalPartController','workcalPartInput');
	bcReq.setExtraPs({
		"workcalPartId":workcalPartId,
		"shift":shift,
		"start_time":start_time,
		"end_time":end_time,
		"workcalRuleId":workcalRuleId,
		"workcalRuleName":workcalRuleName
	});
	bcReq.setSuccFn(function(data,status){
		 window.parent.location.reload();
		 iframeObj.closeDialog();
	});
	bcReq.postData();
}
</script>
</head>
<body>

<div class="hi-box hi-xzdllc" >   
<div class="hi-main">

<div class="hi-hd">
<h3><i class="ico-xzclr"></i>新增工作日历时间段</h3> 
</div>

<div class="hi-form">
<ul>
<li><label class="form-label">Part：</label>
    <div class="formControls hi-select">
         <input type="hidden" id="workcalPartId"  name="workcalPartId" value=""/>
		  <select id="shift" name="shift">
		    <option value="0">上午</option>
		    <option value="1">下午</option>
		    <option value="2">前半夜</option>
		    <option value="3">后半夜</option>
		  </select>
    </div> 
</li>

<li><label class="form-label">开始时间：</label>
    <div class="formControls">
       <input id="startTime" type="text" name="startTime"  size="40"  minlength="1" maxlength="5">
    </div> 
</li>

<li><label class="form-label">结束时间：</label>
    <div class="formControls">
        <input id="endTime" type="text" name="endTime"  size="40" minlength="1" maxlength="5">
    </div> 
</li>


<li><label class="form-label" style="width:90px">工作日历规则：</label>
    <div class="formControls hi-select">
          <select id="workcalRuleId" name="workcalRuleId">
		  </select>
    </div> 
</li>



</ul>
</div>

<div class="hi-page">
<div class="hi-btn"><input name="" type="submit" value="保 存" class=" hi-radius3" onclick="submitWorkcalPart()" /></div>  
</div>


</div>
</div>
<script type="text/javascript">
$(".hi-table tr:odd").addClass("odd-row");
</script>
<!--  
<table align="center" width="288" border="0" cellpadding="0" cellspacing="0">  
  <tr align="center" width="230">
		<td height="14" width="130">Part</td>
		<td height="14" width="130">
		   <input type="hidden" id="workcalPartId"  name="workcalPartId" value=""/>
		  <select id="shift" name="shift">
		    <option value="0">上午</option>
		    <option value="1">下午</option>
		    <option value="2">前半夜</option>
		    <option value="3">后半夜</option>
		  </select>
		</td>
   </tr>
    <tr align="center" width="230">
		<td height="14" width="130">开始时间</td>
		<td height="14" width="130">
		  <input id="startTime" type="text" name="startTime"  size="40"  minlength="1" maxlength="5">
	 	</td>
   </tr>
    <tr align="center" width="230">
		<td height="14" width="130">结束时间</td>
		<td height="14" width="130">
		  <input id="endTime" type="text" name="endTime"  size="40" minlength="1" maxlength="5">
		</td>
   </tr>
   <tr align="center" width="230">
		<td height="14" width="130">工作日历规则</td>
		<td height="14" width="130">
		  <select id="workcalRuleId" name="workcalRuleId">
		  </select>
		</td>
   </tr>
</table> 
<input type='button' id="" value="提交" onclick="submitWorkcalPart()" />
-->
</body>
</html>