<%@ page contentType="text/html; charset=GBK" %>
<!-- ���� -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/yuexue/public/header.jsp"%>
<link href="/workflow/js/dialog/css/custom-theme/jquery-ui-1.9.0.custom.css" rel="stylesheet" />
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/dialog/js/dialog1.0.js"></script>
<title>���������</title>


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
		      var subTaskSel = $("#subTaskSel");
		      subTaskSel.append( "<option value=\'"+_resultRec.userid+"\'>"+_resultRec.username+"</option>" );
		  }
	});
	bcReq.postData();
});


function submitAddSubTask(){
	var subTaskName = $("#subTaskName").val();
	//var subTaskUserId = $("#subTaskUserId").val();
	//var subTaskUserId = $("#subTaskSel option:selected").val();
	//var subTaskUserName = $("#subTaskSel option:selected").text();
	
	var subTaskUserId = $("#subTaskUserId").val();
	var subTaskUserName = $("#subTaskUserName").val();
	var taskId = paramObject.taskId;
	 
	var bcReq = new BcRequest('workflow','componentAction','addSubTask');
	var componentParam = {'taskId':taskId,'subTaskUserId':subTaskUserId,'subTaskUserName':subTaskUserName,'subTaskName':'����'};
	bcReq.setExtraPs({"componentName":"addSubTask","componentParam":JSON.stringify(componentParam)});
	bcReq.setSuccFn(function(data,status){
		 window.parent.location.href = "/workflow/template/completeTaskForm.jsp?taskId="+taskId
		 iframeObj.closeDialog();
		
	});
	bcReq.postData();
}



function openSelectSubTaskHandler(){
	  var _height = window.screen.availHeight-380;
		var _width = window.screen.availWidth-560;

		var _top = (window.screen.availHeight-30-_height)/2;       //��ô��ڵĴ�ֱλ��;
	  	var _left = (window.screen.availWidth-10-_width)/2;           //��ô��ڵ�ˮƽλ��;
	 
	var url = "/workflow/component/selectUser.jsp";
	window.open(url,"ѡ���û�",'height='+_height+',width='+_width+',top='+_top+',left='+_left+',toolbar=no,menubar=yes,scrollbars=yes,resizable=no,location=no, status=no');
	//$.fancybox.open({href:url,type:'iframe',width:1030,height:630,padding:0,margin:0,closeBtn:true,afterClose:reloadExamPaper});     
	return;
    var dlgid = "selectUserDialog";
    var options = {};
    options.title = "ѡ���û�";
    options.width = "3000";
    options.height = "300";
    options.zIndex   = "3999";
    
    var taskId = $("#taskId").val();
    var processInstanceId = $("#processInstanceId").val();
    options.param = {
    	//	 'toAddCounterNode': toAddCounterNode,
    	//	 'processInstanceId':processInstanceId
    };
    options.callback = function(data) {
        //���ǻص����� ��data���ж��壬��ģ̬���ڹرպ�Ա�ҳ����в��� 
    };
    $.Dialog.open(url, dlgid, options);
}

</script>
</head>
<body>
<table align="center" width="288" border="0" cellpadding="0" cellspacing="0">  
  <tr align="center" width="230">
		<td height="14" width="130">����������</td>
		<td height="14" width="130">
		
		 <input type="text" id="subTaskName" name="subTaskName">  
		</td>
   </tr>
   <tr align="center" width="230">
		<td width="130">����������</td>
		<td width="130"><!-- 
		  <select name="subTaskSel" id="subTaskSel"  style="width:100%">  
                <option value="1">����Ա</option>  
                <option value="2">��������Ա</option>  
            </select>   -->
		 <input type="hidden" id="subTaskUserId" name="subTaskUserId">
		 <input type="text" id="subTaskUserName" name="subTaskUserName">
		 <input type='button' id="" value="ѡ��" onclick="openSelectSubTaskHandler()" />
		</td>
   </tr>
</table> 
<input type='button' id="" value="�ύ" onclick="submitAddSubTask()" />
</body>
</html>