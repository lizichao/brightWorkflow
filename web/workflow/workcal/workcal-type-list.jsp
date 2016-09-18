<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>������������</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">
<link href="/workflow/js/dialog/css/custom-theme/jquery-ui-1.9.0.custom.css" rel="stylesheet" />

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/dialog/js/dialog1.0.js"></script>


<script id="workcalTypeListRec" type="text/x-jsrender">	
<table class="table-border table-bordered table-striped table">
<thead> 
<tr>
<th>����</th>
<th>���</th>
<th>����</th>
<th>�༭</th>
</tr>
</thead>
{{for Data}}
    <tr>
      <td><input type='checkbox' id='workcalTypeChk_{{:#index+1}}' name='workcalTypeChkName' value='{{:id}}' /></td>
      <td>{{:id}}</td>
      <td>{{:name}}</td>
      <td><a class="color3" href="javascript:void(0)" onclick="editWorkcalType({{:id}})"> �༭</a></td>
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
	var bcReq = new BcRequest('workflow','workcalTypeController','queryWorkcalType');
	bcReq.setExtraPs({"PageSize":"10","PageNo":pageNo});
	bcReq.setSuccFn(function(data,status){
		 $("#workcalTypeList").empty();
		 if(data.Data.length>0){
			 var _stuContent=$("#workcalTypeListRec").render(data);
			 $("#workcalTypeList").append(_stuContent);
			 $(".hi-table tr:odd").addClass("odd-row");
			 $('input').iCheck({
				    checkboxClass: 'icheckbox_flat-red',
				    radioClass: 'iradio_flat-red'
			});
			 $("#ext-comp-1012").show();
             setPageInfo(data);
		 }else{
		   $("#workcalTypeList").append("<span style='width:100%;color:red;'>��������</span>");
		   $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
}

function addWorkcalType(){
	var url = "/workflow/workcal/workcal-type-input.jsp";
	var dlgid = "addWorkcalTypeDialog";
    var options = {};
    options.title = "����������������";
    options.width = "308";
    options.height = "325";
    
    options.param = {
    		
    };
    options.callback = function(data) {
    };
    $.Dialog.open(url, dlgid, options);
}

function editWorkcalType(typeId){
	var url = "/workflow/workcal/workcal-type-input.jsp";
	var dlgid = "editWorkcalTypeDialog";
    var options = {};
    options.title = "�༭������������";
    options.width = "308";
    options.height = "325";
    
    options.param = {
    		 'typeId':typeId
    };
    options.callback = function(data) {
    };
    $.Dialog.open(url, dlgid, options);
}

function deleteWorkcalType(){
	var workcalTypeChks = [];
	 $('input:checkbox[name=workcalTypeChkName]:checked').each(function(i){
		 workcalTypeChks.push($(this).val());
	});
	 
	var bcReq = new BcRequest('workflow','workcalTypeController','deleteWorkcalType');
	bcReq.setExtraPs({"workcalTypeIds":workcalTypeChks});
	bcReq.setSuccFn(function(data,status){
		 window.location.reload();
	});
	bcReq.postData();
}
</script>
</head> 
<body>
<div class="hi-box hi-cs" style="width: 1000px;height: 500px">   
<div class="hi-main" >

<div class="hi-hd">
<!--  
<h3><i class="ico-cs"></i>��ѡ���Ͷ���</h3> 
<div class="hi-reach hi-radius3">
    <input id="userInfo" name="userInfo" type="text" class="inputt" value="�û���/�ʺ�" />
    <input name="" type="button" class="btnn" onclick="searchUser()" />
</div> -->
</div>

<div class="hi-hd"> 
<div class="hi-zd">
  <a href="#" class=" hi-radius3" onclick="addWorkcalType()">����</a> 
  <a href="#" class=" hi-radius3" onclick="deleteWorkcalType()">ɾ��</a> 
</div> 
</div>


<div id="workcalTypeList" class="hi-table"></div>
 <%@ include file="/workflow/common/pageinfo.jsp"%>
</div>
</div>
<!--  
<div class="mainContent fl"> 
     <div >
        <ul>
		  <li class="crane-form-row">	    
		     <label id="workcalTypeLabel" name="workcalTypeLabel">����</label>
		     <span class="crane-form-input">
		        <input type="text" id="workcalTypeName" name="workcalTypeName" maxlength="200" />		
		     </span>		
		  </li>		
      </div>
  
  <div>
	<input type="button" id="addButton"  value="�½�" onclick="addWorkcalType()">
	<input type="button" id="deleteButton"  value="ɾ��" onclick="deleteWorkcalType()">
  </div>
   
     <div style="height: 500px;" class="mainContainer" pagename="paper_question" status="paper_question"> 
      <div class="child-content fn-clear"> 
       <div style="height: 500px; " class="content"> 
         <div class="filter-box"> 
         </div> 
         <div style="width:90%;margin-left:20px;margin-top:10px;text-align:center" id="workcalTypeList">
         </div>
       </div>      
      </div> 
     </div> 
</div> 
  
  <div id="errormsg" style="display:none"></div> 
  -->
</body>
</html>