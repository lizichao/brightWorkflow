<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ include file="/masterreview/public/sessionoff.jsp"%>
<%
	String userid = (String)session.getAttribute("userid");
	String username =(String)session.getAttribute("username");
	String usertype =(String)session.getAttribute("usertype");
	// String usertype =(String)session.getAttribute("usertype");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>校长职级评审系统</title>
<link rel="stylesheet" href="/masterreview/Css/HuanYu.css">

<script type="text/javascript" src="/js/jquery/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/workflow/workflow.js"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" src="/workflow/js/dialog/js/dialog1.0.js"></script>
<script type="text/javascript" src="/workflow/js/picture-preview/js/picture_preview.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
  <script src="/js/jquery/plugin/fancybox/jquery.mousewheel.pack.js" type="text/javascript"></script>
  <script src="/js/jquery/plugin/fancybox/jquery.fancybox.js" type="text/javascript" ></script>
<script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.js" type="text/javascript"></script>
<script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.src.js" type="text/javascript"></script>


<script id="taskListRec" type="text/x-jsrender">	
<table border="0" cellpadding="0" cellspacing="0">
<tr>
<th>序号</th>
<th>姓名</th>
<th>任职资历</th>
<th>管理难度</th>
<th>专业能力</th>
<th>工作成效</th>
<th>总分</th>
<th>获得职级</th>
<th>详细情况</th>
</tr>

{{for Data}}
    <tr>
      <td class="color-01">{{:#index+1}}</td>
      <td class="color-01">{{:headermastername}}</td>
      <td>{{:ziligrade}}</td>
      <td>{{:managementgrade}}</td>
      <td>{{:zhuanyegrade}}</td>
      <td>{{:professorgrade}}</td>
      <td>{{:sumgrade}}</td>
      <td>{{:apply_level}}</td>
      <td><a  href="javascript:void(0)" onclick="viewTaskDetail({{:processinstanceid}})">查看详情</a></td>
    </tr>
{{/for}}
				
</table>
</script>

<script>
function viewTaskDetail(processInstanceId){
	 window.location.href = "/workflow/template/processViewForm.jsp?processInstanceId="+processInstanceId;
}

$(document).ready(function(){
	setMethod("loadGridData","22");
	loadGridData("22",1);
});

function loadGridData(gridNum,pageNo){
	var bcReq = new BcRequest('headmaster','leaderAction','findApproveResult');
	bcReq.setExtraPs({"PageSize":"10","PageNo":pageNo});
	bcReq.setSuccFn(function(data,status){
		 $("#taskList").empty();
		 if(data.Data.length>0){
			 var _stuContent=$("#taskListRec").render(data);
			 $("#taskList").append(_stuContent);
			 $("#ext-comp-1012").show();
	         setPageInfo(data);
			
		 }else{
		   $("#taskList").append("<span style='width:100%;color:red;'>暂无数据</span>");
		   $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
}

function updatepwd() {
	$.fancybox.open({href:"/masterreview/public/updatepwd.jsp",type:'iframe',width:327,height:298,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'no',preload:false}});
}

function searchTask(){
	var username= $("#username").val();
	var usercode= $("#usercode").val();
	var identitycard= $("#identitycard").val();
	var bcReq = new BcRequest('headmaster','leaderAction','findApproveResult');
	bcReq.setExtraPs({
		"PageSize":"10",
		"PageNo":1,
		"username":username,
		"usercode":usercode,
		"identitycard":identitycard
	});
	bcReq.setSuccFn(function(data,status){
		 $("#taskList").empty();
		 if(data.Data.length>0){
			 var _stuContent=$("#taskListRec").render(data);
			 $("#taskList").append(_stuContent);
			 $("#ext-comp-1012").show();
	         setPageInfo(data);
		 }else{
		   $("#taskList").append("<span style='width:100%;color:red;'>暂无数据</span>");
		   $("#ext-comp-1012").hide();
		 }
	});
	bcReq.postData();
}

function exportTask(){
    var iTop = (window.screen.availHeight-500)/2;
	var iWidth = (window.screen.availWidth-850)/2;
	// window.open('/reportMasterService?reportName=headmasterApproveList','校长职级审批结果','height=500,width=850,top='+iTop+',left='+iWidth+',toolbar=no,menubar=yes,scrollbars=no, resizable=no,location=no, status=no');
	 window.open('/reportMasterService?reportName=headmasterApproveList');
}
</script>
</head>

<body class="grey">
	<div class="top bbm">
		<div class="container">
			<div class="logo"><a href=""><img src="/masterreview/images/logo.jpg" alt=""></a></div>
			<div class="dang">当前用户：<%=username%> 
			   <a href="javascript:void();" onclick="updatepwd()">修改密码</a>
			   <a href="/masterreview/public/logoff.jsp">退出</a>
			</div>
		</div>
	</div>
	
	
	<div class="container">
		<div class="xiao_bg">
			
			<div class="leader-search">
				<form>
					<span>姓名：</span><input type="text" id="username" style="width:110px"  />
					<span>账号：</span><input type="text" id="usercode" style="width:110px"/>
					<span>身份证号：</span><input type="text"  id="identitycard"/>
					<input type="button"  value="查询" onclick="searchTask()" />&nbsp;&nbsp;&nbsp;&nbsp;
					
					<input type="button"  value="导出" onclick="exportTask()" />&nbsp;&nbsp;&nbsp;&nbsp;
					<i class="clear-both"></i>
				</form>
			</div>
			
			<div id="taskList" class="leader-list">
			
			</div>
			<%@ include file="/workflow/common/pageinfo.jsp"%>
			

		</div>
	</div>

	<div class="footer">
		<div class="container">
			深州市教育局版权所有 | 亮信科技技术支持：0755-33018116（工作日9:00-12:00  14:00-18:00） <br>
			Copyright &copy; 2016 . All Rights Reserved.
		</div>
	</div>
</body>
</html>
