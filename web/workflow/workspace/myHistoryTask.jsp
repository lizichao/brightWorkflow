<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>我的已办</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>

<script id="historyTaskListRec" type="text/x-jsrender">	
<table class="table-border table-bordered table-striped table">
<thead> 
<tr>
<th>序号</th>
<th>任务Id</th>
<th>任务名称</th>
<th>流程实例Id</th>
<th>流程标题</th>
<th>流程类型</th>
<th>流程处理人</th>
<th>流程创建人</th>
<th>任务创建时间</th>
<th>任务结束时间</th>
<!--
<th>处理结果</th>
 -->
</tr>
</thead>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td><a class="color3" href="javascript:void(0)" onclick="viewProcessDetail({{:processinstanceid}})"> {{:taskid}}</a></td>
      <td>{{:taskname}}</td>
      <td>{{:processinstanceid}}</td>
      <td>{{:processinstancename}}</td>
      <td>{{:processdefname}}</td>
      <td>
        {{:processhandler}}
      </td>
      <td>{{:startusername}}</td>
      <td>{{:taskcreatetime}}</td>
      <td>{{:taskendtime}}</td>
<!--
      <td>  
        {{if taskdeletereason == 'completed'}}
				完成
		{{else}}
				删除
		{{/if}} 
       </td>
 -->
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
	var bcReq = new BcRequest('workflow','workSpaceAction','queryMyHistoryTasks');
	bcReq.setExtraPs({"PageSize":"10","PageNo":pageNo});
	bcReq.setSuccFn(function(data,status){
		 $("#historyTaskList").empty();
		 if(data.Data.length>0){
			 var _stuContent=$("#historyTaskListRec").render(data);
			 $("#historyTaskList").append(_stuContent);
			 $(".hi-table tr:odd").addClass("odd-row");
			 $("#ext-comp-1012").show();
             setPageInfo(data);
		 }else{
		     $("#historyTaskList").append("<span style='width:100%;color:red;'>暂无数据</span>");
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
<!--  
<h3><i class="ico-cs"></i>请选择抄送对象</h3> 
<div class="hi-reach hi-radius3">
    <input id="userInfo" name="userInfo" type="text" class="inputt" value="用户名/帐号" />
    <input name="" type="button" class="btnn" onclick="searchUser()" />
</div> -->
</div>

<div class="hi-hd"> 
<!-- 
<div class="hi-zd">
  <a href="#" class=" hi-radius3">新增</a> 
  <a href="#" class=" hi-radius3">删除</a> 
</div>  -->
</div>


<div id="historyTaskList" class="hi-table"></div>
 <%@ include file="/workflow/common/pageinfo.jsp"%>
</div>
</div>
<!-- 
  <div class="wrap"> 
   <div class="mainContent fl"> 
     <div style="height: 500px;" class="mainContainer" pagename="paper_question" status="paper_question"> 
      <div class="child-content fn-clear"> 
       <div style="height: 500px; " class="content"> 
         <div class="filter-box"> 
         </div> 
         <div style="width:90%;margin-left:20px;margin-top:10px;text-align:center" id="historyTaskList">
         </div>
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