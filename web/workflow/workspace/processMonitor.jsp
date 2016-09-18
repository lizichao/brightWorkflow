<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>���̼��</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>

<script id="monitorListRec" type="text/x-jsrender">	
<table class="table-border table-bordered table-striped table">
<thead> 
<tr>
<th>����</th>
<th>���</th>
<th>����ʵ��Id</th>
<th>���̱���</th>
<th>��������</th>
<th>��ǰ�ڵ�</th>
<th>���̴�����</th>
<th>���̴�����</th>
<th>���̷���ʱ��</th>
<th>���̽���ʱ��</th>
<th>�Ƿ����</th>
<th>�Ƿ����</th>
<th>���̸�����</th>
<th>���̸���ʱ��</th>
</tr>
</thead>
{{for Data}}
    <tr>
       <td>{{:#index+1}}</td>
        <input type="hidden" id="processinstanceid{{:#index+1}}"  name="processinstanceid{{:#index+1}}" value="{{:processinstanceid}}">
        <input type="hidden" id="processsuspension{{:#index+1}}"  name="processsuspension{{:#index+1}}" value="{{:processsuspension}}">
        <input type="hidden" id="processendtime{{:#index+1}}"  name="processendtime{{:#index+1}}" value="{{:processendtime}}">
      
      <td><input type='checkbox' id='applicantChk_{{:#index+1}}' name='applicantChk_{{:#index+1}}' value='{{:#index+1}}'  /></td>
      <td><a class="color3" href="javascript:void(0)" onclick="viewProcessDetail({{:processinstanceid}})"> {{:processinstanceid}}</a></td>
      <td>{{:processinstancename}}</td>
     <td>{{:processdefname}}</td>
     <td>
	   {{:processcurrenttask}}
      </td>
     <td>
	 {{:processhandler}}
      </td>
     <td>{{:startusername}}</td>
      <td>{{:processstarttime}}</td>
      <td>{{:processendtime}}</td>
      <td>	
        {{if processendtime === ""}}
				��
		{{else}}
				��
		{{/if}} 
      </td>
      <td>     
        {{if processsuspension === "2"}}
				��
		{{else}}
				��
		{{/if}} 
    </td>
    <td>{{:processupdatepeople}}</td>
      <td>{{:processupdatetime}}</td>
    </tr>
{{/for}}
</table>
</script>

<script>
function viewProcessDetail(processInstanceId){
	 window.location.href = "/workflow/template/processViewAdminForm.jsp?processInstanceId="+processInstanceId;
}

$(document).ready(function(){
	setMethod("loadGridData","22");
	loadGridData("22",1);
});


function loadGridData(gridNum,pageNo){
	var bcReq = new BcRequest('workflow','workSpaceAction','queryProcessMonitor');
	bcReq.setExtraPs({"PageSize":"10","PageNo":pageNo});
	bcReq.setSuccFn(function(data,status){
		 $("#monitorList").empty();
		 if(data.Data.length>0){
			 var monitorContent=$("#monitorListRec").render(data);
			 $("#monitorList").append(monitorContent);
			 $("#ext-comp-1012").show();
			 $('input').iCheck({
				    checkboxClass: 'icheckbox_flat-red',
				    radioClass: 'iradio_flat-red'
			});
			 $(".hi-table tr:odd").addClass("odd-row");
             setPageInfo(data);
		 }else{
		   $("#monitorList").append("<span style='width:100%;color:red;'>��������</span>");
			 $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
}

function suspendProcess(){
	var processInstanceIds = [];
	var isSuspend = false;
	var isEnd = false;
	var suspendRows = [];
	var endRows = [];
	$('input:checkbox:checked').each(function(i){
		var selIndex = $(this).val();
		var processsuspension =  $("#processsuspension"+selIndex).val();
		if(processsuspension == 2){
			suspendRows.push(selIndex);
			isSuspend = true;
		}
		var processendtime =  $("#processendtime"+selIndex).val();
		if(processendtime){
			endRows.push(selIndex);
			isEnd = true;
		}
		var processinstanceId =  $("#processinstanceid"+selIndex).val();
		processInstanceIds.push(processinstanceId);
	}); 
	if(isSuspend){
		alert("��"+suspendRows.join(",")+"���Ѿ��ǹ���״̬��");
		return false;
	}
	if(isEnd){
		alert("��"+endRows.join(",")+"���Ѿ�������");
		return false;
	}
	var bcReq = new BcRequest('workflow','workSpaceAction','suspendProcess');
	bcReq.setExtraPs({"processInstanceIds":processInstanceIds});
	bcReq.setSuccFn(function(data,status){
		 window.location.href = "/workflow/workspace/processMonitor.jsp";
	});
	bcReq.postData(); 
}


function activateProcess(){
	var processInstanceIds = [];
	var isActivate = false;
	var isEnd = false;
	var activateRows = [];
	var endRows = [];
	$('input:checkbox:checked').each(function(i){
		var selIndex = $(this).val();
		var processsuspension =  $("#processsuspension"+selIndex).val();
		if(processsuspension == 1){
			activateRows.push(selIndex);
			isActivate = true;
		}
		var processendtime =  $("#processendtime"+selIndex).val();
		if(processendtime){
			endRows.push(selIndex);
			isEnd = true;
		}
		var processinstanceId =  $("#processinstanceid"+selIndex).val();
		processInstanceIds.push(processinstanceId);
	}); 
	if(isActivate){
		alert("��"+activateRows.join(",")+"���Ѿ��Ǽ���״̬��");
		return false;
	}
	if(isEnd){
		alert("��"+endRows.join(",")+"���Ѿ�������");
		return false;
	}
	var bcReq = new BcRequest('workflow','workSpaceAction','activateProcess');
	bcReq.setExtraPs({"processInstanceIds":processInstanceIds});
	bcReq.setSuccFn(function(data,status){
		 window.location.href = "/workflow/workspace/processMonitor.jsp";
	});
	bcReq.postData(); 
}


function deleteProcess(){
	var processInstanceIds = [];
	var endRows = [];
	$('input:checkbox:checked').each(function(i){
		var selIndex = $(this).val();
		var processinstanceId =  $("#processinstanceid"+selIndex).val();
		processInstanceIds.push(processinstanceId);
		
		var processendtime =  $("#processendtime"+selIndex).val();
		if(processendtime){
			endRows.push(selIndex);
			isEnd = true;
		}
	}); 
	if(isEnd){
		alert("��"+endRows.join(",")+"���Ѿ�������");
		return false;
	}
	var bcReq = new BcRequest('workflow','workSpaceAction','deleteProcess');
	bcReq.setExtraPs({"processInstanceIds":processInstanceIds});
	bcReq.setSuccFn(function(data,status){
		 window.location.href = "/workflow/workspace/processMonitor.jsp";
	});
	bcReq.postData(); 
}

function searchProcess(){
	var processInstanceIdSearch =  $("#processInstanceIdSearch").val();
	var bcReq = new BcRequest('workflow','workSpaceAction','queryProcessMonitor');
	bcReq.setExtraPs({"PageSize":"10","PageNo":pageNo,"processInstanceId":processInstanceIdSearch});
	bcReq.setSuccFn(function(data,status){
		 $("#monitorList").empty();
		 if(data.Data.length>0){
			 var monitorContent=$("#monitorListRec").render(data);
			 $("#monitorList").append(monitorContent);
			 $("#ext-comp-1012").show();
			 $('input').iCheck({
				    checkboxClass: 'icheckbox_flat-red',
				    radioClass: 'iradio_flat-red'
			});
			 $(".hi-table tr:odd").addClass("odd-row");
             setPageInfo(data);
		 }else{
		   $("#monitorList").append("<span style='width:100%;color:red;'>��������</span>");
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
  
<h3><i class="ico-cs"></i>����������ʵ��id</h3> 
<div class="hi-reach hi-radius3">
    <input id="processInstanceIdSearch" name="processInstanceIdSearch" type="text" class="inputt"  />
    <input name="" type="button" class="btnn" onclick="searchProcess()" />
</div>
</div>

<div class="hi-hd"> 
<div class="hi-zd">
  <a href="#" class=" hi-radius3" onclick="suspendProcess()">����</a> 
  <a href="#" class=" hi-radius3" onclick="activateProcess()">����</a> 
  <a href="#" class=" hi-radius3" onclick="deleteProcess()">ɾ��</a> 
</div> 
</div>


<div id="monitorList" class="hi-table"></div>
 <%@ include file="/workflow/common/pageinfo.jsp"%>
</div>
</div>
  <!--  
   <div class="mainContent fl"> 

	  <div id="operateDiv">
		<input type="button" id="suspendButton"  value="����" onclick="suspendProcess()">
		<input type="button" id="activateButton"  value="����" onclick="activateProcess()">
		<input type="button" id="deleteButton"  value="ɾ��" onclick="deleteProcess()">
	  </div>
     <div style="height: 500px;" class="mainContainer" pagename="paper_question" status="paper_question"> 
      <div class="child-content fn-clear"> 
       <div style="height: 500px; " class="content"> 
         <div class="filter-box"> 
         </div> 
         <div style="width:90%;margin-left:20px;margin-top:10px;text-align:center" id="monitorList">
         </div>
       </div>      
      </div> 
     </div> 
   </div> 
  
  <div id="errormsg" style="display:none"></div> 
  <div class="footer" id="footer" style="display:none"> 
    <span>2015</span> 
  </div>
  -->
 </body>
</html>