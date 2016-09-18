<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ page import="cn.brightcom.jraf.conf.BrightComConfig"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%session.invalidate();%>
<% 
    String ServerName=request.getServerName();  
    String logoPath = "/images/yuexue2/xbkt_1.png";
	if (ServerName.startsWith("xiben")){
	    logoPath =  "/images/yuexue2/xbkt.png";
	}
	request.setCharacterEncoding("GBK"); 
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>校长职级评审系统</title>
<style type="text/css">
<!--
.STYLE1 {
	font-family: "华康海报体W12(P)";
	font-size: 25.93px;
	color: #89cdef;
}
.STYLE2 {
	font-family: "方正少儿简体";
	font-size: 17.87px;
	color: #FFFFFF;
}
.STYLE3 {
	font-family: "华文细黑";
	font-size: 18px;
	color: #000000;
	text-decoration: none;
}
body {
    width:100%;
    height:100%;
    background:url(/images/yuexue2/bg.jpg);
	background-repeat:no-repeat;
}
.STYLE14 {font-family: "华文细黑";
	font-size: 15px;
	color: #787878;
	text-decoration: none;
}

 .login, .login-field {
    border-radius: 0.25em;
  }

  .login {
    background-color: #e6e6e6;
  }

  .login-field {
    background-color: #fff;
    border: 1px solid #ccc;
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
    border-top-right-radius: 1.5em;
    border-bottom-right-radius: 1.5em;
    color: inherit;
    display: block;
    font-family: inherit;
    font-size: 20pt;
    margin: 0;
	height:46px;
    position: relative;
    vertical-align: middle;
    width: 100%;

    -webkit-appearance: none;
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
  }

  .login-field:focus {
    border-color: #0088cc;
    outline: transparent;
    z-index: 2;
  }

  .login-option {
    padding: 0.25em 0.5em 0.5em;
  }

  .login-option input {
    vertical-align: baseline;
  }
-->
</style>
<link href="/js/jquery/plugin/toastr/toastr.min.css" rel="stylesheet" type="text/css">
<link href="/css/yuexue/common.css" rel="stylesheet" rel="stylesheet" type="text/css">

<script src="/js/jquery/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="/js/jquery/plugin/toastr/toastr.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/des.js"></script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="100%" align="center" valign="top">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td height="100">&nbsp;</td>
      </tr>
      <tr>
        <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>            
            <td align="center" valign="middle">
			  <form id="form1" name="form1">
			  <input type="hidden" id="loginFailure" name="loginFailure" value="0">
			  <table width="200" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td align="center" valign="top" colspan="3"><img src="<%=logoPath%>" width="439" height="89" /></td>
              </tr>
              <tr>
                <td height="50" colspan="3">&nbsp;</td>
              </tr>
              <tr>
                <td height="50" align="left" width="153"><img src="/images/yuexue2/name.png" width="153" height="50" /></td>
				<td colspan="2"><input id="usercode" name="usercode" type="text" class="login-field" autocapitalize="off" autocomplete="off" autocorrect="off" onkeypress="checkUser();"/></td>
              </tr>
              <tr>
                <td height="13" align="center" valign="top" colspan="3"><img src="/images/yuexue2/bg_03.png" width="0" height="0" /></td>
              </tr>
              <tr>
                <td height="50" align="left" width="153"><img src="/images/yuexue2/password.png" width="153" height="50" /></td>
				<td height="50" colspan="2"><input id="userpwd"  name="userpwd" type="password" class="login-field" autocapitalize="off" autocomplete="off" autocorrect="off"  onkeypress="checkPwd();"/></td>
              </tr>
              <tr class="catpcha" style="display:none">
                <td height="13" align="center" valign="top" colspan="3"><img src="/images/yuexue2/bg_03.png" width="0" height="0" /></td>
              </tr>
              <tr class="catpcha" style="display:none">
                <td height="50" align="left" width="153"><img src="/images/yuexue2/number.png" width="153" height="50" /></td>
				<td height="50" width="150" ><input id="captchaCode"  name="captchaCode" type="text" class="login-field" autocapitalize="off" autocomplete="off" autocorrect="off"  onkeypress="checkPwd();"></td>
                <td height="50"><img src="/platform/public/img.jsp?t=<%=Math.random()%>" id="codeImg" class="codeImg" onclick="changeImg()"/></td>
              </tr>
              <tr class="catpcha" style="display:none">
                <td height="13" align="center" valign="top" colspan="3"><img src="/images/yuexue2/bg_03.png" width="0" height="0" /></td>
              </tr>
			  <tr class="catpcha" style="display:none">
				<td colspan="3" align="center" valign="middle">
					<table width="88%" border="0" cellpadding="0" cellspacing="0">
					  <tr>
						<td width="45"><img src="/images/yuexue2/tishi_01.png" width="45" height="38" /></td>
						<td align="center" background="/images/yuexue2/tishi_02.png"><span id="msgText" class="STYLE2">用户名还是不对呀，请找回用户名!</span></td>
						<td width="20"><img src="/images/yuexue2/tishi_03.png" width="20" height="38" /></td>
					  </tr>
					</table>				   
				</td>
			  </tr>
              <tr>
                <td height="30" colspan="3">&nbsp;</td>
              </tr>			  
              <tr class="loginObj">
                <td align="center" colspan="3">
				   <div class="loading02_spinner" style="height:100px;display:none;"></div>
				   <img src="/images/yuexue2/button_denglu.png" width="185" height="59" onclick="checklogin();"/>
				</td>
              </tr>
              <tr class="loginFailureObj" style="display:none">
                <td align="center" colspan="3">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					  <tr>
						<td width="185" align="center" valign="bottom"><img src="/images/yuexue2/button_zhyhm.png" width="185" height="61" /></td>
						<td>&nbsp;</td>
						<td width="193" align="center" valign="bottom"><img src="/images/yuexue2/button_denglu_01.png" width="193" height="77" onclick="checklogin();"/></td>
					  </tr>
					</table>				    
				</td>
              </tr>
              </table>
			  </form>
			</td>            
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="50">&nbsp;</td>
      </tr>
      <tr>
        <td align="center">
		<table width="449" border="0" cellpadding="0" cellspacing="0">
          <tr>
		    <td width="77"><a href="/yuexue/register/student.jsp"><img src="/images/yuexue2/reg_stu.png" width="77" height="21" /></a></td>
			<td width="20" align="center"><img src="/images/yuexue2/reg_line.png" width="6" height="22" /></td>
		    <td width="77"><a href="/yuexue/register/teacher.jsp"><img src="/images/yuexue2/reg_teacher.png" width="77" height="21" /></td>
			<!--
			<td width="20" align="center"><img src="/images/yuexue2/reg_line.png" width="6" height="22" /></td>
            <td width="77"><img src="/images/yuexue2/zhmm.png" width="77" height="21" /></td>
			-->
            <td width="275"><img src="/images/yuexue2/number01.png" width="275" height="21" /></td>
          </tr>
        </table></td>
      </tr>
      
    </table></td>
  </tr>
</table>
</body>
</html>
<Script language="JavaScript" type="">
String.prototype.trim = function()
{
    return this.replace(/(^\s*)|(\s*$)/g,"");
}
/*
$(document).ready(function(){	
	var _height=$(document).height();
	var _width=$(document).width();
    document.body.style.backgroundSize =_width+"px "+_height+"px";
	var userAgent = navigator.userAgent.toLowerCase(); 
    if (userAgent.indexOf("chrome")>0 || $.browser.msie || userAgent.indexOf("rv:11.0")>0){
		if ($.browser.msie){
			if ($.browser.version!="9.0" && $.browser.version!="10.0" && $.browser.version!="11.0"){
				window.location.href="/yuexue/upgrade/browser.jsp";
			}
		}
	}
	else {
	    window.location.href="/yuexue/upgrade/browser.jsp";
	} 
});
*/
function changeImg(){
	$("#codeImg").attr("src","/platform/public/img.jsp?t=" + Math.random());
}
function checkUser()
{
	if(13 == event.keyCode)
	{
		if("" == $("#usercode").val())
		{
			toastr.error("请输入帐号","错误信息");			
			$("#usercode").focus();
			return ;
		}		
	}
}
function checkPwd()
{
	if(13 == event.keyCode)
	{
		if("" == $("#userpwd").val())
	   {			
			toastr.error("请输入密码","错误信息");
			$("#userpwd").focus();
			return ;
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
        if ("" == $("#captchaCode").val() && $("#loginFailure").val()=="1"){
			toastr.error("请输入验证码","错误信息");
			$("#captchaCode").focus();
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
					 changeImg();
					 $("#loginFailure").val("1");
					 $(".catpcha").show();
					 $(".loginObj").hide();
					 $(".loginFailureObj").show();
					 $("#msgText").text(data.error_description);
				 }
				 else{
                     
					 window.location.href=data.Forward.forward;
				 }
		     }
		);
}
</Script>
