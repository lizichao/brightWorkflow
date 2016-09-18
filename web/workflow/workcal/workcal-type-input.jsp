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
<title>编辑工作日历类型</title>


<script type="text/javascript">
var iframeObj = window.parent.frames[0];
var paramObject = window.dialogVal() || iframeObj.dialogVal();
$(document).ready(function(){
	var typeId = paramObject.typeId;
	//var typeName = paramObject.typeName;
	if(typeId){
		var bcReq = new BcRequest('workflow','workcalTypeController','getWorkcalType');
		bcReq.setExtraPs({"workcalTypeId":typeId});
		bcReq.setSuccFn(function(data,status){
			  var _resultRec = data.Data[0];
			$("#workcalTypeId").val(_resultRec.id);
			$("#workcalTypeName").val(_resultRec.name);
		});
		bcReq.postData();
		//$("#workcalTypeId").val(typeId);
	}
//	if(typeName){
	//	$("#workcalTypeName").val(typeName);
//	}

});


function submitWorkcalType(){
	var workcalTypeId =  $("#workcalTypeId").val() || "";
	var workcalTypeName =$("#workcalTypeName").val();
	 
	var bcReq = new BcRequest('workflow','workcalTypeController','workcalTypeInput');
	bcReq.setExtraPs({"workcalTypeId":workcalTypeId,"workcalTypeName":workcalTypeName});
	bcReq.setSuccFn(function(data,status){
		 window.parent.location.reload();
		 iframeObj.closeDialog();
	});
	bcReq.postData();
}
</script>
</head>
<body>
<!--弹出-->

<div class="hi-box hi-xzdllc">   
<div class="hi-main">

<div class="hi-hd">
<h3><i class="ico-xzclr"></i>新增工作日历类型</h3> 
</div>

<div class="hi-form">
<ul>
<li><label class="form-label">名称：</label>
    <div class="formControls">
    	 <input type="hidden" id="workcalTypeId"  name="workcalTypeId" value=""/>
		 <input type="text" id="workcalTypeName" name="workcalTypeName"/>  
    </div> 
</li>
</ul>
</div>

<div class="hi-page">
<div class="hi-btn"><input name="" type="submit" value="保 存" class=" hi-radius3" onclick="submitWorkcalType()" /></div>  
</div>


</div>
</div>
<script type="text/javascript">
$(".hi-table tr:odd").addClass("odd-row");
</script>
<!-- 
<table align="center" width="288" border="0" cellpadding="0" cellspacing="0">  
  <tr align="center" width="230">
		<td height="14" width="130">名称</td>
		<td height="14" width="130">
		 <input type="hidden" id="workcalTypeId"  name="workcalTypeId" value=""/>
		 <input type="text" id="workcalTypeName" name="workcalTypeName"/>  
		</td>
   </tr>
</table> 
<input type='button' id="" value="提交" onclick="submitWorkcalType()" /> -->
</body>
</html>