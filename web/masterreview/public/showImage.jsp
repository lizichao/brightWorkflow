<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.jraf.auth.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.brightcom.jraf.conf.BrightComConfig"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String webpath = request.getScheme()+"://"+request.getServerName();

String imgSrc =(String)request.getParameter("imgSrc");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>校长职级系统注册</title>
<link href="/js/jquery/plugin/toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
<link href="/js/jquery/plugin/selectpick/selectpick.css" rel="stylesheet" type="text/css"  />

<script src="/js/jquery/jquery-1.7.1.min.js?v=2.0.5" type="text/javascript"></script>
<script src="/js/jquery/plugin/toastr/toastr.min.js" type="text/javascript"></script>
<script src="/js/des.js" type="text/javascript"></script>
<script src="/js/jquery/common.js" type="text/javascript"></script>
<script src="/js/jquery/plugin/selectpick/selectpick.js" type="text/javascript"></script>
<style type="text/css">
	*{
		padding:0;
		margin:0;
	}
	.load{
		width: 445px;
		height: 376px;
		background: #fff;
		border-bottom:1px solid #565656;
		margin-bottom: 30px;
	}
	.loadin{
		width: 100%;
		height: 100%;
		border:1px solid #dddddd;
	}
	.loadhd{
		padding-top:18px;
		line-height: 35px;
		padding-left: 44px;
		font-size: 16px;
		color:#44b549;
		position: relative;
		border-bottom: 1px solid #d4d4d4;
		margin-bottom: 30px;
	}
	.loadspan{
		display: inline-block;
		width: 161px;
		border-bottom: 4px solid #44b549;
		font-weight: bold;
	}
	.close{
	    float:right;
	    margin-right:10px;
	    margin-top:10px;
		top:20px;
		right:15px;
	}
	.loadbd{
		padding:0 40px;
	}
	input,button{
		outline: none;
		border:0;
	}
	.loadbd input{
		width: 100%;
		font-size: 18px;
		margin-bottom: 15px;
		height: 45px;
		color:#cccccc;
		padding-left: 10px;
		line-height: 45px;
		background: #f1f1f1;
		border:1px solid #dfdfdf;
	}
	.loadbd input.passtxt{
		margin-bottom: 50px;
	}
	.loadbd input.radiotxt,.loadbd label{
		width: auto;
		color:#999999;
		height: auto;
	}
	.fl{
		float: left;
	}
	.fr{
		float: right;
	}
	.fotul li{
		float: left;
		margin-right: 5px;
		color:#999999;
	}
	a{
		text-decoration: none;
	}
	ul{
		list-style-type: none;
	}
	.fotul li a{
		font-size: 14px;
		color:#ef810c;
	}
	.btnregister{
		height: 45px;
		border-radius: 5px;
		background: #44b549;
		color:white;
		text-align: center;
		width: 100%;
		clear: both;
		margin-top: 10px;
	}
	.bd1{
		overflow: hidden;
		margin-bottom: 15px;
		height:62px;
	}
	.bd1 span{
		width: 90px;
		float: left;
		line-height: 45px;
		color:gray;
	}
	.bd1 input{
		width: 260px;
		float: left;
	}
	.load1,.loadbd{
		height: auto;
		
	}
	.loadbd{
			padding-bottom: 50px;
	}
	.top1{
	  top:359px;
	}
</style>
<script type="text/javascript">
$(document).ready(function(){
	$("#imgDiv").append("<img id='imgUpload'    border='0' src=\"<%=imgSrc%>\">");
})
</script> 
</head>
<body>
<div>
		<div style="height: 10px;" >
		    <span class="close" onclick="windowclose();"><img style="cursor:pointer;" src="/images/close.png" alt="" /></span>
		</div>
		<div style="margin-top: 20px;">
				 <div id ="imgDiv" style="text-align: center" >
		         </div>
		</div>
</div>


</body>
<script type="text/javascript">
function windowclose () {
	top.$.fancybox.close();
}
</script>

</html>