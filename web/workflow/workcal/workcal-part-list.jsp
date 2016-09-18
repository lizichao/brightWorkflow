<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>工作日历时间段</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">
<link href="/workflow/js/dialog/css/custom-theme/jquery-ui-1.9.0.custom.css" rel="stylesheet" />

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/dialog/js/dialog1.0.js"></script>

<script id="workcalPartListRec" type="text/x-jsrender">	
<table class="table table-bordered table-striped table-hover">
<thead> 
<tr>
        <th >操作</th>
        <th >编号</th>
        <th >时间段</th>
        <th >开始时间</th>
        <th >结束时间</th>
        <th >日期</th>
        <th width="80">编辑</th>
</tr>
</thead>
{{for Data}}
    <tr>
      <td><input type='checkbox' id='workcalPartChk_{{:#index+1}}' name='workcalPartChkName' value='{{:id}}' /></td>
      <td>{{:id}}</td>
      <td>
	     {{if shift==0}}
				上午
		 {{else shift==1}} 
				下午
		 {{else shift==2}}
				前半夜		
         {{else shift==3}}
				后半夜
		 {{/if}}
      </td>
       <td>{{:start_time}}</td>
        <td>{{:end_time}}</td>
        <td>{{:workcalrulename}}</td>
      <td><a class="color3"  href="javascript:void(0)" onclick="editWorkcalPart({{:id}})"> 编辑</a></td>
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
	var bcReq = new BcRequest('workflow','workcalPartController','queryWorkcalPart');
	bcReq.setExtraPs({"PageSize":"10","PageNo":pageNo});
	bcReq.setSuccFn(function(data,status){
		 $("#workcalPartList").empty();
		 if(data.Data.length>0){
			 var _stuContent=$("#workcalPartListRec").render(data);
			 $("#workcalPartList").append(_stuContent);
			 $(".hi-table tr:odd").addClass("odd-row");
			 $('input').iCheck({
				    checkboxClass: 'icheckbox_flat-red',
				    radioClass: 'iradio_flat-red'
			});
			 $("#ext-comp-1012").show();
             setPageInfo(data);
		 }else{
		   $("#workcalPartList").append("<span style='width:100%;color:red;'>暂无数据</span>");
		   $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
}

function addWorkcalPart(){
	var url = "/workflow/workcal/workcal-part-input.jsp";
	var dlgid = "addWorkcalPartDialog";
    var options = {};
    options.title = "新增工作日历时间段";
    options.width = "307";
    options.height = "322";
    
    options.param = {
    		
    };
    options.callback = function(data) {
    };
    $.Dialog.open(url, dlgid, options);
}

function editWorkcalPart(id){
	var url = "/workflow/workcal/workcal-part-input.jsp";
	var dlgid = "editWorkcalPartDialog";
    var options = {};
    options.title = "编辑工作日历时间段";
    options.width = "307";
    options.height = "322";
    
    options.param = {
    		'workcalPartId': id
    };
    options.callback = function(data) {
    };
    $.Dialog.open(url, dlgid, options);
}

function deleteWorkcalPart(){
	var workcalPartChks = [];
	 $('input:checkbox[name=workcalPartChkName]:checked').each(function(i){
		 workcalPartChks.push($(this).val())
	});
	 
	var bcReq = new BcRequest('workflow','workcalPartController','deleteWorkcalPart');
	bcReq.setExtraPs({"workcalPartIds":workcalPartChks});
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
<h3><i class="ico-cs"></i>请选择抄送对象</h3> 
<div class="hi-reach hi-radius3">
    <input id="userInfo" name="userInfo" type="text" class="inputt" value="用户名/帐号" />
    <input name="" type="button" class="btnn" onclick="searchUser()" />
</div> 
</div>-->

<div class="hi-hd"> 
<div class="hi-zd">
  <a href="#" class=" hi-radius3" onclick="addWorkcalPart()">新增</a> 
  <a href="#" class=" hi-radius3" onclick="deleteWorkcalPart()">删除</a> 
</div> 
</div>


<div id="workcalPartList" class="hi-table"></div>
 <%@ include file="/workflow/common/pageinfo.jsp"%>
</div>
</div>
<!--  
<div class="mainContent fl"> 
     <div >
        <ul>
		  <li class="crane-form-row">	    
		     <label id="workcalPartLabel" name="workcalPartLabel">名称</label>
		     <span class="crane-form-input">
		        <input type="text" id="workcalPartName" name="workcalPartName" maxlength="200" />		
		     </span>		
		  </li>		
      </div>
  
  <div>
	<input type="button" id="addButton"  value="新建" onclick="addWorkcalPart()">
	<input type="button" id="deleteButton"  value="删除" onclick="deleteWorkcalPart()">
  </div>
   
     <div style="height: 500px;" class="mainContainer" pagename="paper_question" status="paper_question"> 
      <div class="child-content fn-clear"> 
       <div style="height: 500px; " class="content"> 
         <div class="filter-box"> 
         </div> 
         <div style="width:90%;margin-left:20px;margin-top:10px;text-align:center" id="workcalPartList">
         </div>
       </div>      
      </div> 
     </div> 
</div> 
  
  <div id="errormsg" style="display:none"></div> 
  -->
</body>
</html>