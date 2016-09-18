<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>我的申请单</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>



<script id="applicantListRec" type="text/x-jsrender">	
<table class="table-border table-bordered table-striped table">
<thead> 
<tr>
<th>序号</th>
<th>流程实例Id</th>
<th>流程标题</th>
<th>流程类型</th>
<th>当前节点</th>
<th>流程处理人</th>
<th>流程创建人</th>
<th>流程发起时间</th>
<th>流程结束时间</th>
<!--
<th>流程更新人</th>
<th>流程更新时间</th>-->
</tr>
</thead>
{{for Data}}
    <tr>
      <input type="hidden" id="processinstanceid{{:#index+1}}"  name="processinstanceid{{:#index+1}}" value="{{:processinstanceid}}">
    <!--  <td><input type='checkbox' id='applicantChk_{{:#index+1}}' name='applicantChk_{{:#index+1}}' value='{{:#index+1}}'  /></td>-->
      <td>{{:#index+1}}</td>
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
<!--
      <td>{{:processupdatepeople}}</td>
      <td>{{:processupdatetime}}</td>-->
    </tr>
{{/for}}
</table>
</script>

<script>
function viewProcessDetail(processInstanceId){
	 window.location.href = "/workflow/template/processViewForm.jsp?processInstanceId="+processInstanceId;
}

$(document).ready(function(){
	setMethod("loadGridData","22");
	loadGridData("22",1);
});

function loadGridData(gridNum,pageNo){
	var bcReq = new BcRequest('workflow','workSpaceAction','queryMyApplication');
	bcReq.setExtraPs({"PageSize":"10","PageNo":pageNo});
	bcReq.setSuccFn(function(data,status){
		 $("#applicantList").empty();
		 if(data.Data.length>0){
			 var _stuContent=$("#applicantListRec").render(data);
			 $("#applicantList").append(_stuContent);
			 $(".hi-table tr:odd").addClass("odd-row");
			 $('input').iCheck({
				    checkboxClass: 'icheckbox_flat-red',
				    radioClass: 'iradio_flat-red'
			});
			 $("#ext-comp-1012").show();
             setPageInfo(data);
		 }else{
		   $("#applicantList").append("<span style='width:100%;color:red;'>暂无数据</span>");
		   $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
}

function revokeProcess(){
	 var processInstanceIds = [];
	 $('input:checkbox:checked').each(function(i){
		 var selIndex = $(this).val();
		 var processinstanceId =  $("#processinstanceid"+selIndex).val()
		 processInstanceIds.push(processinstanceId);
	 }); 
	 var bcReq = new BcRequest('workflow','workSpaceAction','revokeProcess');
	 bcReq.setExtraPs({"processInstanceIds":processInstanceIds});
	 bcReq.setSuccFn(function(data,status){
	
	 });
	 bcReq.postData(); 
}
</script>
</head> 
<body >
<div class="hi-box hi-cs" style="width: 1000px;height: 500px">   
<div class="hi-main" >

<div class="hi-hd">
<!--  
<h3><i class="ico-cs"></i>请选择抄送对象</h3> 
<div class="hi-reach hi-radius3">
    <input id="userInfo" name="userInfo" type="text" class="inputt" value="用户名/帐号" />
    <input name="" type="button" class="btnn" onclick="searchUser()" />
</div> -->
</div>

<div class="hi-hd"> 
<div class="hi-zd">
<!--  
  <a href="#" class=" hi-radius3">新增</a> 
  <a href="#" class=" hi-radius3">删除</a> -->
</div> 
</div>


<div id="applicantList" class="hi-table"></div>
 <%@ include file="/workflow/common/pageinfo.jsp"%>
</div>
</div>

<!--
<div class="mainContent fl"> 
     <div >
        <ul>
		  <li class="crane-form-row">	    
		      <label id="processInstanceIdLabel" name="processInstanceIdLabel">流程实例id</label>
		     <span class="crane-form-input">
		        <input type="text" id="processInstanceId" name="processInstanceId" maxlength="200" />		
		     </span>		
		  </li>		
		    <li>
		        <label id="processDefKeyLabel" name="processDefKeyLabel">流程类型</label>
			    <span>
			          <input type="text" id="processDefKey" name="processDefKey"/>
			    </span>	
		  </li>  
		  
		
      </ul>
      </div>
      <div class="crane-operate">
         
      </div>
  
  
  <div>
	<input type="button" id="revokeButton"  value="回收" onclick="revokeProcess()">
  </div>
   
     <div style="height: 500px;" class="mainContainer" pagename="paper_question" status="paper_question"> 
      <div class="child-content fn-clear"> 
       <div style="height: 500px; " class="content"> 
         <div class="filter-box"> 
         </div> 
         <div style="width:90%;margin-left:20px;margin-top:10px;text-align:center" id="applicantList">
         </div>
       </div>      
      </div> 
     </div> 
</div> 
  
  <div id="errormsg" style="display:none"></div> 
    -->
 </body>
</html>