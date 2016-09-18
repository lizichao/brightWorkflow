<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>工作日历规则</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">
<link href="/workflow/js/dialog/css/custom-theme/jquery-ui-1.9.0.custom.css" rel="stylesheet" />

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/dialog/js/dialog1.0.js"></script>


<script id="workcalRuleListRec" type="text/x-jsrender">	
<table class="table table-bordered table-striped table-hover">
<thead> 
<tr>
        <th >操作</th>
        <th >编号</th>
        <th >年度</th>
        <th >周</th>
        <th >名称</th>
        <th >日期</th>
        <th >状态</th>
        <th >类型</th>
        <th width="80">编辑</th>
</tr>
</thead>
{{for Data}}
    <tr>
      <td><input type='checkbox' id='workcalRuleChk_{{:#index+1}}' name='workcalRuleChkName' value='{{:id}}' /></td>
      <td>{{:id}}</td>
      <td>{{:year}}</td>
      <td>
	     {{if week === '1'}}
				周日
		 {{else week === '2'}} 
				周一
		 {{else week === '3'}}
				周二			
         {{else week === '4'}}
				周三	
         {{else week === '5'}}
				周四		
        {{else week === '6'}}
				周五		
        {{else week === '7'}}
				周六			
		 {{/if}}
      </td>
      <td>{{:name}}</td>
      <td>{{:work_date}}</td>
      <td>   
         {{if status === '0'}}
			规则
		 {{else status === '1'}} 
				节假日
		 {{else status === '2'}}
				调休		
         {{else status === '3'}}
				补休
		 {{/if}}
     </td>
      <td>{{:workcaltypename}}</td>
      <td><a class="color3"  href="javascript:void(0)" onclick="editWorkcalRule({{:id}})">编辑</a></td>
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
	var bcReq = new BcRequest('workflow','workcalRuleController','queryWorkcalRule');
	bcReq.setExtraPs({"PageSize":"10","PageNo":pageNo});
	bcReq.setSuccFn(function(data,status){
		 $("#workcalRuleList").empty();
		 if(data.Data.length>0){
			 var _stuContent=$("#workcalRuleListRec").render(data);
			 $("#workcalRuleList").append(_stuContent);
			 $('input').iCheck({
				    checkboxClass: 'icheckbox_flat-red',
				    radioClass: 'iradio_flat-red'
			 });
			 $(".hi-table tr:odd").addClass("odd-row");
			 $("#ext-comp-1012").show();
             setPageInfo(data);
		 }else{
		   $("#workcalRuleList").append("<span style='width:100%;color:red;'>暂无数据</span>");
		   $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
}

function addWorkcalRule(){
	var url = "/workflow/workcal/workcal-rule-input.jsp";
	var dlgid = "addWorkcalRuleDialog";
    var options = {};
    options.title = "新增工作日历规则";
    options.width = "309";
    options.height = "365";
    
    options.param = {
    		
    };
    options.callback = function(data) {
    };
    $.Dialog.open(url, dlgid, options);
}

function editWorkcalRule(id){
	var url = "/workflow/workcal/workcal-rule-input.jsp";
	var dlgid = "editWorkcalRuleDialog";
    var options = {};
    options.title = "编辑工作日历规则";
    options.width = "309";
    options.height = "365";
    
    options.param = {
    		'workcalRuleId': id
    		/*'year': year,
    		'week': week,
    		'workcalRuleName': workcalRuleName,
    		'work_date': work_date,
    		'status': status,
    		'type_id': type_id*/
    };
    options.callback = function(data) {
    };
    $.Dialog.open(url, dlgid, options);
}

function deleteWorkcalRule(){
	var workcalRuleChks = [];
	 $('input:checkbox[name=workcalRuleChkName]:checked').each(function(i){
		 workcalRuleChks.push($(this).val())
	});
	 
	var bcReq = new BcRequest('workflow','workcalRuleController','deleteWorkcalRule');
	bcReq.setExtraPs({"workcalRuleIds":workcalRuleChks});
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
</div> -->
</div>

<div class="hi-hd"> 
<div class="hi-zd">
  <a href="#" class=" hi-radius3" onclick="addWorkcalRule()">新增</a> 
  <a href="#" class=" hi-radius3" onclick="deleteWorkcalRule()">删除</a> 
</div> 
</div>


<div id="workcalRuleList" class="hi-table"></div>
 <%@ include file="/workflow/common/pageinfo.jsp"%>
</div>
</div>
<!--  
<div class="mainContent fl"> 
     <div >
        <ul>
		  <li class="crane-form-row">	    
		     <label id="workcalRuleLabel" name="workcalRuleLabel">名称</label>
		     <span class="crane-form-input">
		        <input type="text" id="workcalRuleName" name="workcalRuleName" maxlength="200" />		
		     </span>		
		  </li>		
      </div>
  
  <div>
	<input type="button" id="addButton"  value="新建" onclick="addWorkcalRule()">
	<input type="button" id="deleteButton"  value="删除" onclick="deleteWorkcalRule()">
  </div>
   
     <div style="height: 500px;" class="mainContainer" pagename="paper_question" status="paper_question"> 
      <div class="child-content fn-clear"> 
       <div style="height: 500px; " class="content"> 
         <div class="filter-box"> 
         </div> 
         <div style="width:90%;margin-left:20px;margin-top:10px;text-align:center" id="workcalRuleList">
         </div>
       </div>      
      </div> 
     </div> 
</div> 
  
  <div id="errormsg" style="display:none"></div> 
  -->
 </body>
</html>