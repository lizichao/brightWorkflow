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

	
%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>评分标准</title>

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
$(document).ready(function(){
	$("#newsContentId").text("wwcsc");
})



</script> 
</head>


<body >
		<div class="loadhd">
		<span class="close" onclick="windowclose();"><img style="cursor:pointer;" src="/images/close.png" alt="" /></span>
			<span class="loadspan">修改密码</span>
		</div>
	
	<div class="container">
		<div class="xiao_bg">
			<div class="new-title"><b id="newsTitleId"></b>
			<a href="#" onclick="goBack()" title=""  class="goback">返回&nbsp;&gt;</a></div>
			
			<div id ="newsContentId" class="new-content">
			</div>
		</div>
	</div>

	
</body>
</html>
