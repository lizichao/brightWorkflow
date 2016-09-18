<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%
	String userid = (String)session.getAttribute("userid");
	String username =(String)session.getAttribute("username");
	String usertype =(String)session.getAttribute("usertype");
	// String usertype =(String)session.getAttribute("usertype");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";

	String newsId =(String)request.getParameter("newsId");
	
%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>审批任务</title>

<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<!--<link href="/workflow/css/jalor.min.css" rel="stylesheet" type="text/css" />  

<link href="/workflow/css/public.css" rel="stylesheet" type="text/css" />
<link href="/workflow/css/layout.css" rel="stylesheet" type="text/css" />
<link href="/workflow/js/dialog/css/custom-theme/jquery-ui-1.9.0.custom.css" rel="stylesheet" />
<link href="/workflow/js/picture-preview/css/picture_preview.css" rel="stylesheet" />-->
<link rel="stylesheet" href="/masterreview/Css/HuanYu.css">
  <link href="/js/jquery/plugin/fancybox/jquery.fancybox.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="/js/jquery/plugin/zebra_dialog/zebra_dialog.css" rel="stylesheet" type="text/css" />

<!--<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>-->
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




<script type="text/javascript">
var newsId = "<%=newsId%>"
$(document).ready(function(){
	var bcReq = new BcRequest('headmaster','newsAction','findSingleNews');
	bcReq.setExtraPs({"id":newsId});
	bcReq.setSuccFn(function(data,status){
		var result = data.Data[0];
		$("#newsTitleId").html(result.news_title);
		$("#newsContentId").html(result.news_content);
	});
	bcReq.postData();
})

$.views.tags({
	subtaskEndTimeContent:function(endTime){
	   return Brightcom.workflow.getDateByLongStr(endTime);
	}
})	

function goBack(){
	 window.location.href="/masterreview/news/newslist.jsp";
}

</script> 
</head>


<body class="grey">
	<div class="top bbm">
		<div class="container">
		
			<div class="logo"><a href=""><img src="/masterreview/images/logo.jpg" alt=""></a></div>
			<div class="dang">当前用户：guest 
			<!--   <a href="javascript:void();" onclick="updatepwd()">修改密码</a>
			  <a href="/masterreview/public/logoff.jsp">退出</a> -->
			</div>
		</div>
	</div>
	
	
	<div class="container">
		<div class="xiao_bg">
			<div class="new-title"><b id="newsTitleId"></b>
			<a href="#" onclick="goBack()" title=""  class="goback">返回&nbsp;&gt;</a></div>
			
			<div id ="newsContentId" class="new-content">
			</div>
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
