<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.system.pcmc.util.MenuUtil"%>
<%@ page import="cn.brightcom.system.pcmc.pm.PmInformations" %>
<%@ page import="cn.brightcom.jraf.conf.BrightComConfig"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>我的代办</title>
<%@ include file="/mooc/public/header.jsp"%>
<link rel="stylesheet" type="text/css" href="/images/mooc/question/base.css" /> 
<link rel="stylesheet" type="text/css" href="/images/mooc/question/common.css" /> 
<link rel="stylesheet" type="text/css" href="/images/mooc/question/teacher.css" /> 
<link href="/images/mooc/question/jquery-ui-1.css" rel="stylesheet" type="text/css" />

<style type="text/css">
.table {width: 100%;margin-bottom: 20px;max-width: 100%;background-color: transparent;border-collapse: collapse;border-spacing: 0}
.table th,.table td {padding: 8px;line-height: 15px;text-align: left;vertical-align: top;border-top: 1px solid #ddd}
.table th {font-weight: bold}
.table thead th {vertical-align: bottom}
.table caption+thead tr:first-child th,.table caption+thead tr:first-child td,.table colgroup+thead tr:first-child th,.table colgroup+thead tr:first-child td,.table thead:first-child tr:first-child th,.table thead:first-child tr:first-child td {border-top: 0}
.table tbody+tbody {border-top: 2px solid #ddd}
.table .table {background-color: #fff}
.table-bordered {border: 1px solid #ddd;border-collapse: separate;*border-collapse: collapse;border-left: 0;-webkit-border-radius: 4px;-moz-border-radius: 4px;border-radius: 4px}
.table-bordered th,.table-bordered td {border-left: 1px solid #ddd}
.table-striped tbody>tr:nth-child(odd)>td,.table-striped tbody>tr:nth-child(odd)>th {background-color: #f9f9f9}
.table-hover tbody tr:hover>td,.table-hover tbody tr:hover>th {background-color: #f5f5f5}
</style>

<script id="taskListRec" type="text/x-jsrender">	
<table class="table table-bordered table-striped table-hover">
<thead> 
<tr>
<th>任务Id</th>
<th>流程实例Id</th>
<th>任务名称</th>
<th>任务创建时间</th>
</tr>
</thead>
{{for Data}}
    <tr>
      <td><a href="javascript:void(0)" onclick="viewTaskDetail({{:taskid}})" class="cBtnNormal topNavcBtnNormal"> {{:taskid}}</a></td>
      <td>{{:processinstanceid}}</td>
      <td>{{:taskname}}</td>
      <td>{{:taskcreatetime}}</td>
    </tr>
{{/for}}
</table>
</script>

<script>
function GetQueryString(name) {  
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");  
	var r = window.location.search.substr(1).match(reg);  
	if (r != null) return unescape(r[2]); return "";  
} 

$.views.converters({
    nameConverter:function(value){
    if (value.length > 9){ 
	     return value.substr(0,9)+'...';
	 }
    return value;
	}
});

function viewTaskDetail(taskId){
	 window.location.href = "/workflow/template/completeTaskForm.jsp?taskId="+taskId;
}

$(document).ready(function(){
	
});

</script>
</head> 
<body class="headerQuestion">
  <!--主要部分开始--> 
  <div class="wrap"> 
   <div class="mainContent fl"> 
     <div style="height: 500px;" class="mainContainer" pagename="paper_question" status="paper_question"> 
      <div class="child-content fn-clear"> 
       <div style="height: 500px; " class="content"> 
         <div class="filter-box"> 
              <!-- 班级 -->   
         </div> 
         <div style="width:90%;margin-left:20px;margin-top:10px;text-align:center" id="taskList">
              <!-- 学生信息 -->
         </div>
          <!-- 分页begin -->
           <%@ include file="/mooc/public/pageinfo.jsp"%>
          <!-- 分页end -->
       </div>      
      </div> 
     </div> 
   </div> 
  </div> 
  
  <!--主要部分结束--> 
  <div id="errormsg" style="display:none"></div> 
  <div class="footer" id="footer" style="display:none"> 
    <span>2015</span> 
  </div>
 </body>
</html>