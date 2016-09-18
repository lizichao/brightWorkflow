<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>我被代理的流程</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>

<script id="delegateListRec" type="text/x-jsrender">	
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
<th>流程更新人</th>
<th>流程更新时间</th>
</tr>
</thead>
{{for Data}}
    <tr>
       <td>{{:#index+1}}</td>
        <input type="hidden" id="processinstanceid{{:#index+1}}"  name="processinstanceid{{:#index+1}}" value="{{:processinstanceid}}">
        <input type="hidden" id="processsuspension{{:#index+1}}"  name="processsuspension{{:#index+1}}" value="{{:processsuspension}}">
        <input type="hidden" id="processendtime{{:#index+1}}"  name="processendtime{{:#index+1}}" value="{{:processendtime}}">
      
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
    <td>{{:processupdatepeople}}</td>
      <td>{{:processupdatetime}}</td>
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
	var bcReq = new BcRequest('workflow','workSpaceAction','queryMyDelegatedProcess');
	bcReq.setExtraPs({"PageSize":"10","PageNo":pageNo});
	bcReq.setSuccFn(function(data,status){
		 $("#delegateList").empty();
		 if(data.Data.length>0){
			 var delegateContent=$("#delegateListRec").render(data);
			 $("#delegateList").append(delegateContent);
			 $("#ext-comp-1012").show();
			 $('input').iCheck({
				    checkboxClass: 'icheckbox_flat-red',
				    radioClass: 'iradio_flat-red'
			});
			 $(".hi-table tr:odd").addClass("odd-row");
             setPageInfo(data);
		 }else{
		   $("#delegateList").append("<span style='width:100%;color:red;'>暂无数据</span>");
			 $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
}



function searchProcess(){
	var processInstanceIdSearch =  $("#processInstanceIdSearch").val();
	var bcReq = new BcRequest('workflow','workSpaceAction','queryMyDelegatedProcess');
	bcReq.setExtraPs({"PageSize":"10","PageNo":pageNo,"processInstanceId":processInstanceIdSearch});
	bcReq.setSuccFn(function(data,status){
		 $("#delegateList").empty();
		 if(data.Data.length>0){
			 var delegateContent=$("#delegateListRec").render(data);
			 $("#delegateList").append(delegateContent);
			 $("#ext-comp-1012").show();
			 $('input').iCheck({
				    checkboxClass: 'icheckbox_flat-red',
				    radioClass: 'iradio_flat-red'
			});
			 $(".hi-table tr:odd").addClass("odd-row");
             setPageInfo(data);
		 }else{
		   $("#delegateList").append("<span style='width:100%;color:red;'>暂无数据</span>");
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
  
<h3><i class="ico-cs"></i>请输入流程实例id</h3> 
<div class="hi-reach hi-radius3">
    <input id="processInstanceIdSearch" name="processInstanceIdSearch" type="text" class="inputt"  />
    <input name="" type="button" class="btnn" onclick="searchProcess()" />
</div>
</div>

<div class="hi-hd"> 
<!-- 
<div class="hi-zd">
  <a href="#" class=" hi-radius3" onclick="suspendProcess()">挂起</a> 
  <a href="#" class=" hi-radius3" onclick="activateProcess()">激活</a> 
  <a href="#" class=" hi-radius3" onclick="deleteProcess()">删除</a> 
</div>  -->
</div>


<div id="delegateList" class="hi-table"></div>
 <%@ include file="/workflow/common/pageinfo.jsp"%>
</div>
</div>
 </body>
</html>