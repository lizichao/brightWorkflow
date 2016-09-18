<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ page import="cn.brightcom.jraf.conf.BrightComConfig"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%session.invalidate();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<title>云南师范大学附属世纪金源学校</title>
<link rel="stylesheet" type="text/css" href="/images/yunan/yunan.css" />
<link href="/js/jquery/plugin/toastr/toastr.min.css" rel="stylesheet" type="text/css">
<script src="/js/jquery/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="/js/jquery/plugin/toastr/toastr.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/des.js"></script></head>
</head>
<body bgcolor="#D2E9FF">
<!-- Top start -->
<div class="loginTop">
	<img src="/images/yunan/LoginLogo.jpg" width="461" height="75" alt="" title="" class="fl" />
	<img src="/images/yunan/Top.png" alt="" height="71" width="522" title="" class="fr" />
</div>
<!-- Top end -->
<!-- Main start -->
<div class="loginMain">
	<div class="forBg">
		<div class="login">
			<img src="/images/yunan/DengLu.png" alt="" height="21" width="94" class="denglu" />
			<form name="form1" method="post" action="/httpprocesserservlet">
				<input type="hidden" name="sysName" value="<%=Crypto.encode(request,"pcmc")%>">
				<input type="hidden" name="oprID" value="<%=Crypto.encode(request,"sm_permission")%>">
				<input type="hidden" name="actions" value="<%=Crypto.encode(request,"login")%>">
				<span class="userName">用户名:</span><input type="text" id="usercode" name="usercode" value="" class="userNameInput"  onkeypress="checkUser();"/>
				<span class="passWord">密&nbsp;&nbsp;码:</span><input type="password" id="userpwd" name="userpwd" value="" class="passWordInput" onkeypress="checkPwd();"/>
				<input type="text" name="captchaCode" id="captchaCode" value="" class="codeInput" onkeypress="checkCaptchaCode();" />
				<span class="captCode">验证码:</span><img src="/platform/public/img.jsp?t=<%=Math.random()%>" id="codeImg" class="codeImg" onclick="changeImg()"  />
                <a href="javascript:;" onclick="changeImg()"><span class="captReflush">点击刷新</span></a>
				<input type="button" value="" name="" class="submit" onclick="checklogin();" />
			</form>
		</div>
	</div>
</div>
<!-- Main end -->
</body>
</html>
<Script language="JavaScript" type="">
String.prototype.trim = function()
{
    return this.replace(/(^\s*)|(\s*$)/g,"");
}
$(document).ready(function(){
	var _height=document.body.scrollHeight-75;
	$(".loginMain").css("height",_height);	
});
function changeImg(){
		var obj=document.getElementById("codeImg");
		obj.src="/platform/public/img.jsp?t=" + Math.random();
}
function checkUser()
{
	if(13 == event.keyCode)
	{
		if("" == form1.usercode.value.trim())
		{
			toastr.error("请输入用户名","错误信息");
			return;
		}
		form1.userpwd.focus();
	}
}
function checkPwd()
{
	if(13 == event.keyCode)
	{
		if("" == form1.userpwd.value.trim())
		{
			toastr.error("请输入用户密码","错误信息");
			return;
		}
		form1.captchaCode.focus();
	}
}
function checkCaptchaCode()
{
	if(13 == event.keyCode)
	{
		if("" == form1.captchaCode.value.trim())
		{
			toastr.error("请输入验证码","错误信息");
			return;
		}
		checklogin();
	}
}

function checklogin(){	    
		if("" == $("#usercode").val())		
		{			
			toastr.error("请输入帐号","错误信息");	
			$("#usercode").focus();
			return ;
		}
		if("" == $("#userpwd").val())
		{			
			toastr.error("请输入密码","错误信息");
			$("#userpwd").focus();
			return ;
		}
		if($("#userpwd").val().length<3 || $("#userpwd").val().length>12){			
			toastr.error("密码长度必须在3~12位之间","错误信息");
			return ;		    
		}
		 $(".loading02_spinner").show();
		$.post("/P.tojson?sysName=pcmc&oprID=sm_permission&actions=login",
		     {"usercode":$("#usercode").val(),"userpwd":strEnc($("#userpwd").val()),"captchaCode":$("#captchaCode").val()},
		     function(data,status){
			     $(".loading02_spinner").hide();
				 if (data.result=="1"){
					 toastr.error(data.error_description,"错误信息");
					 changeImg();
				 }
				 else{
					 window.location.href=data.Forward.forward;
				 }
		     }
		);
}
</Script>


