<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.util.Crypto" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.system.pcmc.util.ITEMS" %>
<%@ page import="cn.brightcom.tags.util.*" %>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.jraf.auth.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="sl" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
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
<title>�����б�</title>
<link href="/js/jquery/plugin/fancybox/jquery.fancybox.css" rel="stylesheet" type="text/css" media="screen"/>
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


<!--δ������ -->
<script id="noApproveRec" type="text/x-jsrender">	
{{for Data}}
   <li><a class="shu" href="javascript:void(0)" onclick="viewTaskDetail({{:id_}})">{{:headermastername}}({{:school_name}})</a></li>
{{/for}}
</script>

<!--�������� -->
<script id="approveRec" type="text/x-jsrender">	
{{for Data}}
   <li><a class="shu" href="javascript:void(0)" onclick="viewProcessDetail({{:processinstanceid}})">{{:headermastername}}({{:school_name}})</a></li>
{{/for}}
</script>


<script type="text/javascript">
function viewTaskDetail(taskId){
	 window.location.href = "/workflow/template/completeTaskForm.jsp?taskId="+taskId;
}

function viewProcessDetail(processInstanceId){
	 window.location.href = "/workflow/template/processViewForm.jsp?processInstanceId="+processInstanceId;
}
 
$(document).ready(function(){
	bulidNoApproveData();
	bulidNoApproveMinorData();
	
	bulidApproveData();
	bulidApproveMinorData();
})

/*
 * ��У��δ���
 */
function bulidNoApproveData(){
	var noApproveText = $("#noApproveText").val();
	var bcReq = new BcRequest('headmaster','personalLeaderAction','findNoApproveData');
	bcReq.setExtraPs({
		'username':noApproveText,
		'ispositive':'1'
	});
	bcReq.setSuccFn(function(data,status){
		 $("#noApproveDiv").empty();
		 if(data.Data.length>0){
			 $("#noApproveCount").text("("+data.Data.length+"��)");
			 var _stuContent=$("#noApproveRec").render(data);
			 $("#noApproveDiv").append(_stuContent);
		 }else{
		 }
	});
	bcReq.postData();
}

/*
 * ��У��δ���
 */
function bulidNoApproveMinorData(){
	var noApproveText = $("#noApproveMinorText").val();
	var bcReq = new BcRequest('headmaster','personalLeaderAction','findNoApproveData');
	bcReq.setExtraPs({
		'username':noApproveText,
		'ispositive':'0'
	});
	bcReq.setSuccFn(function(data,status){
		 $("#noApproveMinorDiv").empty();
		 if(data.Data.length>0){
			 $("#noApproveMinorCount").text("("+data.Data.length+"��)");
			 var _stuContent=$("#noApproveRec").render(data);
			 $("#noApproveMinorDiv").append(_stuContent);
		 }else{
		 }
	});
	bcReq.postData();
}

function bulidApproveData(){
	var approveText = $("#approveText").val();
	var bcReq = new BcRequest('headmaster','personalLeaderAction','findApproveData');
	bcReq.setExtraPs({
		'username':noApproveText,
		'ispositive':'1'
	});
	bcReq.setSuccFn(function(data,status){
		 $("#approveDiv").empty();
		 if(data.Data.length>0){
			 $("#approveCount").text("("+data.Data.length+"��)");
			 var _stuContent=$("#approveRec").render(data);
			 $("#approveDiv").append(_stuContent);
		 }else{
		 }
	});
	bcReq.postData();
}

function bulidApproveMinorData(){
	var approveText = $("#approveMinorText").val();
	var bcReq = new BcRequest('headmaster','personalLeaderAction','findApproveData');
	bcReq.setExtraPs({
		'username':noApproveText,
		'ispositive':'0'
	});
	bcReq.setSuccFn(function(data,status){
		 $("#approveMinorDiv").empty();
		 if(data.Data.length>0){
			 $("#approveMinorCount").text("("+data.Data.length+"��)");
			 var _stuContent=$("#approveRec").render(data);
			 $("#approveMinorDiv").append(_stuContent);
		 }else{
		 }
	});
	bcReq.postData();
}

function updatepwd() {
	$.fancybox.open({href:"/masterreview/public/updatepwd.jsp",type:'iframe',width:327,height:298,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'no',preload:false}});
}
</script> 

</head>

<body class="grey">
	<div class="top bbm">
		<div class="container">
			<div class="logo"><a href=""><img src="/masterreview/images/logo.jpg" alt=""></a></div>
			<div class="dang">��ǰ�û���<%=username%> 
			  <a href="javascript:void(0);" onclick="updatepwd()">�޸�����</a>
			  <a href="/masterreview/public/logoff.jsp">�˳�</a>
			</div>
		</div>
	</div>
	
	
	<div class="container">
		<div class="xiao_bg">
		   <div class="xiaozhang">
				<div><b>δ���У��<span id="noApproveCount"/></b>
				<input id="noApproveText" type="text" value="">
				<input type="button" value="����" onclick="bulidNoApproveData()"></div>
			    <ul class="clear-fix" id="noApproveDiv"></ul>
			</div>
			
			<div class="xiaozhang">
				<div><b>δ��˸�У��<span id="noApproveMinorCount"/></b>
				<input id="noApproveMinorText" type="text" value="">
				<input type="button" value="����" onclick="bulidNoApproveMinorData()"></div>
			    <ul class="clear-fix" id="noApproveMinorDiv"></ul>
			</div>
			
			<div class="xiaozhang mt24">
				<div><b>�����У��<span id="approveCount"/></b>
				 <input id="approveText" type="text" value="">
				 <input type="button" value="����" onclick="bulidApproveData()"></div>
				<ul class="clear-fix" id="approveDiv">
				</ul>
			</div>
			
			<div class="xiaozhang mt24">
				<div><b>����˸�У��<span id="approveMinorCount"/></b>
				 <input id="approveMinorText" type="text" value="">
				 <input type="button" value="����" onclick="bulidApproveMinorData()"></div>
				<ul class="clear-fix" id="approveMinorDiv">
				</ul>
			</div>

		</div>
	</div>

	<div class="footer">
		<div class="container">
			�����н����ְ�Ȩ���� | ���ſƼ�����֧�֣�0755-33018116��������9:00-12:00  14:00-18:00�� <br>
			Copyright &copy; 2016 . All Rights Reserved.
		</div>
	</div>
</body>
</html>