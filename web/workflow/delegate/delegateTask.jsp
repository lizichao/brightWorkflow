<%@ page contentType="text/html; charset=GBK" %>
<%
	String userid =(String)session.getAttribute("userid");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>���̴���</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">
<link href="/workflow/js/dialog/css/custom-theme/jquery-ui-1.9.0.custom.css" rel="stylesheet" />
<link href="/js/jquery/plugin/zebra_dialog/zebra_dialog.css" rel="stylesheet" type="text/css" />

<!--<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>  -->
<script type="text/javascript" src="/js/jquery/jquery-1.7.1.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/dialog/js/dialog1.0.js"></script>
<script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.js" type="text/javascript"></script>


<script id="delegateTaskListRec" type="text/x-jsrender">	
<table class="table table-bordered table-striped table-hover">
<thead> 
<tr>
<th>����</th>
<th>��ʼʱ��</th>
<th>����ʱ��</th>
<th>������</th>
<th>��������</th>
</tr>
</thead>
{{for Data}}
    <tr>
      <input type="hidden" id="delegateTaskId{{:#index+1}}"  name="delegateTaskId{{:#index+1}}" value="{{:id}}">
      <td><input type='checkbox' id='delegateTaskChk_{{:#index+1}}' name='delegateTaskChk_{{:#index+1}}' value='{{:#index+1}}'  /></td>
      <td>{{:starttime}}</td>
      <td>{{:endtime}}</td>
      <td>{{:delegate_user_name}}</td>
      <td>{{:delegate_process_name}}</td>
    </tr>
{{/for}}
</table>
</script>

<script>
$(document).ready(function(){
	setMethod("loadGridData","22");
	loadGridData("22",1);
});

function loadGridData(gridNum,pageNo){
	var bcReq = new BcRequest('workflow','delegateTaskAction','searchDelegateTask');
	bcReq.setExtraPs({
		"PageSize":"10",
		"PageNo":pageNo,
		"originaluser":'<%=userid%>'
	});
	bcReq.setSuccFn(function(data,status){
		 $("#delegateTaskList").empty();
		 if(data.Data.length>0){
			 var delegateContent=$("#delegateTaskListRec").render(data);
			 $("#delegateTaskList").append(delegateContent);
			 $(".hi-table tr:odd").addClass("odd-row");
			 $("#ext-comp-1012").show();
			 $('input').iCheck({
				    checkboxClass: 'icheckbox_flat-red',
				    radioClass: 'iradio_flat-red'
			});
             setPageInfo(data);
		 }else{
		   $("#delegateTaskList").append("<span style='width:100%;color:red;'>��������</span>");
		   $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
}

function addDelegateTask(){
	var url = "/workflow/delegate/addDelegateTask.jsp";
    var dlgid = "addDelegateTaskDialog";
    var options = {};
    options.title = "������������";
    options.width = "308";
    options.height = "325";
    
    options.param = {
    		 
    };
    options.callback = function(data) {
    	var bcReq = new BcRequest('workflow','delegateTaskAction','addDelegateTask');
    	
    	bcReq.setExtraPs({
    		"startTime":data.startTime,
    		"endTime":data.endTime,
    		"delegateUser":data.delegateUser,
    		"delegateUserName":data.delegateUserName,
    		"processDefKey":data.processDefKey,
    		"processDefName":data.processDefName,
    	});
    	bcReq.setSuccFn(function(data,status){
    		window.location.reload();
    		//iframeObj.closeDialog();
    	//	iframeObj.closeDialogAndReturn(returnUser);
    		//window.parent.location.href="/workflow/delegate/delegateTask.jsp";
    	});
    	//bcReq.setFailFn(function(data,status){
    		// iframeObj.closeDialog();
//    	});
    	bcReq.postData();
        //���ǻص����� ��data���ж��壬��ģ̬���ڹرպ�Ա�ҳ����в��� 
    };
    $.Dialog.open(url, dlgid, options);
}

function deleteDelegatTask(){
	 var delegateTaskIds = [];
	 $('input:checkbox:checked').each(function(i){
		 var selIndex = $(this).val();
		 var delegateTaskId =  $("#delegateTaskId"+selIndex).val()
		 delegateTaskIds.push(delegateTaskId);
	 }); 
		var bcReq = new BcRequest('workflow','delegateTaskAction','deleteDelegateTask');
		bcReq.setExtraPs({"delegateTaskIds":delegateTaskIds});
		bcReq.setSuccFn(function(data,status){
			window.location.href="/workflow/delegate/delegateTask.jsp";
		});
		bcReq.postData(); 
}

function searchDelegateList(){
	var bcReq = new BcRequest('workflow','delegateTaskAction','searchDelegateTask');
	bcReq.setExtraPs({
		"PageSize":"10",
		"PageNo":pageNo,
		"originaluser":'<%=userid%>',
		"delegateuser":$("#delegateuser").val()
	});
	bcReq.setSuccFn(function(data,status){
		 $("#delegateTaskList").empty();
		 if(data.Data.length>0){
			 var delegateContent=$("#delegateTaskListRec").render(data);
			 $("#delegateTaskList").append(delegateContent);
			 $(".hi-table tr:odd").addClass("odd-row");
			 $("#ext-comp-1012").show();
			 $('input').iCheck({
				    checkboxClass: 'icheckbox_flat-red',
				    radioClass: 'iradio_flat-red'
			});
             setPageInfo(data);
		 }else{
		   $("#delegateTaskList").append("<span style='width:100%;color:red;'>��������</span>");
		   $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
}
</script>
</head> 
<body>
<div class="hi-box hi-cs" style="width: 1000px;height: 500px">   
<div class="hi-main" >

<div class="hi-hd">
<h3><i class="ico-cs"></i>������</h3> 
<div class="hi-reach hi-radius3">
    <input id="delegateuser" name="delegateuser" type="text" class="inputt"  />
    <input name="" type="button" class="btnn" onclick="searchDelegateList()" />
</div> 


</div>

<div class="hi-hd"> 
<div class="hi-zd">
  <a href="#" class="hi-radius3" onclick="addDelegateTask()">����</a> 
  <a href="#" class="hi-radius3" onclick="deleteDelegatTask()">ɾ��</a> 
</div> 
</div>


<div id="delegateTaskList" class="hi-table"></div>
 <%@ include file="/workflow/common/pageinfo.jsp"%>
</div>
</div>
<!--  
<div class="mainContent fl"> 
  <div>
        <ul>
		  <li class="crane-form-row">	    
		      <label id="processDefKeyLabel" name="processDefKeyLabel">��������</label>
		     <span class="crane-form-input">
		        <input type="text" id="processDefKeyText" name="processDefKeyText" maxlength="200" />		
		     </span>		
		  </li>		
		    <li>
		        <label id="processDelegateTimeLabel" name="processDelegateTimeLabel">����ʱ��</label>
			    <span>
			        <input type="text" id="processDelegateTime" name="processDelegateTime"/>
			    </span>	
		  </li>  
       </ul>
  </div>
  <div>
	<input type="button" id="addButton"  value="����" onclick="addDelegateTask()">
	<input type="button" id="addButton"  value="ɾ��" onclick="deleteDelegatTask()">
  </div>
   
     <div style="height: 500px;" class="mainContainer" pagename="paper_question" > 
      <div class="child-content fn-clear"> 
       <div style="height: 500px; " class="content"> 
         <div class="filter-box"> 
         </div> 
         <div style="width:90%;margin-left:20px;margin-top:10px;text-align:center" id="delegateTaskList">
         </div>
       </div>      
      </div> 
     </div> 
</div> 
  
  <div id="errormsg" style="display:none"></div> 
  -->
</body>
</html>