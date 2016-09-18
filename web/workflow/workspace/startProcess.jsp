<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>发起流程列表</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>

<script id="startListRec" type="text/x-jsrender">	
<table class="table-border table-bordered table-striped table">
<thead> 
<tr>
<th>序号</th>
<th>发起流程</th>
<th>流程名称</th>
<th>流程key</th>
<th>图形</th>
</tr>
</thead>
{{for Data}}
    <tr>
      <td>{{:#index+1}}</td>
      <td><a class="color3"  href="/workflow/template/startProcessForm.jsp?processkey={{:processdefkey}}">发起流程</a></td>
      <td> {{:processdefname}}</td>
      <td>{{:processdefkey}}</td>
      <td><a class="color3"  href="/processDiagram?processDefinitionId={{:processdefid}}&&isProcessEnd=1" target="_blank">预览图形</a></td>
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
	var bcReq = new BcRequest('workflow','workSpaceAction','queryStartProcess');
	bcReq.setExtraPs({"PageSize":"10","PageNo":pageNo});
	bcReq.setSuccFn(function(data,status){
		 $("#startList").empty();
		 if(data.Data.length>0){
			 var startContent=$("#startListRec").render(data);
			 $("#startList").append(startContent);
			 $("#ext-comp-1012").show();
			 $('input').iCheck({
				    checkboxClass: 'icheckbox_flat-red',
				    radioClass: 'iradio_flat-red'
			});
			 $(".hi-table tr:odd").addClass("odd-row");
             setPageInfo(data);
		 }else{
		   $("#startList").append("<span style='width:100%;color:red;'>暂无数据</span>");
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
<h3><i class="ico-cs"></i>请输入流程实例id</h3> 
<div class="hi-reach hi-radius3">
    <input id="processInstanceIdSearch" name="processInstanceIdSearch" type="text" class="inputt"  />
    <input name="" type="button" class="btnn" onclick="searchProcess()" />
</div>-->

</div>

<div class="hi-hd"> 
<!-- 
<div class="hi-zd">
  <a href="#" class=" hi-radius3" onclick="suspendProcess()">挂起</a> 
  <a href="#" class=" hi-radius3" onclick="activateProcess()">激活</a> 
  <a href="#" class=" hi-radius3" onclick="deleteProcess()">删除</a> 
</div>  -->
</div>


<div id="startList" class="hi-table"></div>
 <%@ include file="/workflow/common/pageinfo.jsp"%>
</div>
</div>
 </body>
</html>