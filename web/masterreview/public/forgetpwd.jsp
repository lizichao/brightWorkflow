<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.jraf.auth.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="cn.com.bright.yuexue.util.HttpWebLuohuedu"%>
<%
String rand = (String)request.getSession().getAttribute("rand");
if (StringUtil.isNotEmpty(rand)){
	request.getSession().setAttribute("rand",null);
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>罗湖中小学慕课系统登录</title>
<link href="/js/jquery/plugin/toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
<script src="/js/jquery/jquery-1.7.1.min.js?v=2.0.5" type="text/javascript"></script>
<script src="/js/jquery/plugin/toastr/toastr.min.js" type="text/javascript"></script>
<script src="/js/des.js" type="text/javascript"></script>
<script src="/js/jquery/common.js" type="text/javascript"></script>
<style type="text/css">
	*{
		padding:0;
		margin:0;
	}
	.load{
		width: 327px;
		height: 268px;
		background: #fff;
		border:1px solid #565656;
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
		height:20px;
		width:20px;
		position: absolute;
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
	.btn{
		height: 45px;
		border-radius: 5px;
		background: #ef810c;
		color:white;
		text-align: center;
		width: 100%;
		clear: both;
		margin-top: 10px;
	}
	.bd1{
		overflow: hidden;
		margin-bottom: 15px;
	}
	.bd1 span{
		width: 90px;
		float: left;
		line-height: 45px;
		color:#cccccc;
	}
	.bd1 input{
		width: 263px;
		float: left;
	}
	.load1,.loadbd{
		height: auto;
		
	}
	.loadbd{
			padding-bottom: 50px;
	}
</style>
<script type=text/javascript>

</script>
</head>
<body>
<div class="load">
	<div class="loadin">
		<div class="loadhd">
		<span  class="close" onclick="windowclose();"><img style="cursor:pointer;" src="/images/close.png" alt="" /></span>
			<span class="loadspan" >找回密码</span>
		</div>
		<div class="loadbd">
			<input type="text" id="usercode" name="usercode" value="手机号" onkeypress="checkUser(event);"  onfocus="if(value==defaultValue){value='';}" onblur="if(!value){value=defaultValue;}"/>
			<div id="RequiredFieldValidator" style="margin-top:-40px;height:40px; line-height:40px;color:red;"></div>
			<div class="forget">
				<div class="fotz fl">
				<!-- <input type="checkbox" name="sex" id="male" class="radiotxt" />
					 <label for="male">自动登陆</label> -->
				</div>
				<ul class="fotul fr" >
				</ul>
			</div>
			<button class="btn" onclick="findpwd()" style="cursor:pointer;background:#44b549">发&nbsp送&nbsp验&nbsp证&nbsp码</button>
		</div>
	</div>
</div>
</body>
<script language="JavaScript">
String.prototype.trim = function()
{
    return this.replace(/(^\s*)|(\s*$)/g,"");
}

function windowclose () {
	top.$.fancybox.close();
}
function regist() {
	top.$.fancybox.close();
	top.register();
}
function checkUser(evt)
{
  	evt = (evt) ? evt : ((window.event) ? window.event : ""); //兼容IE和Firefox获得keyBoardEvent对象
 	var key = evt.keyCode?evt.keyCode:evt.which;//兼容IE和Firefox获得keyBoardEvent对象的键值 
 	if(13 == key)
	{ 
		findpwd();
	}
}
function findpwd(){
	if ("手机号" == $("#usercode").val()) 
	{
		toastr.clear();
		toastr.error("请输入手机号！");
		$("#usercode").focus();
		return ;
	}
	if (!(/^1[3|4|5|8][0-9]\d{4,8}$/.test($("#usercode").val())) || $("#usercode").val().length != 11) 
	{
    	toastr.clear();
		toastr.error("请输入正确手机号！");
		$("#usercode").focus();
        return ; 
    }
	var bcReq = new BcRequest('yuexue','Register','findPwdByNote');
    bcReq.setExtraPs({"usercode":$("#usercode").val()});
    bcReq.setSuccFn(function(data,status){
         toastr.success(data.hint_description);
         $.fancybox.close();
	     login();
    });
    bcReq.setFailFn(function(data,status){
		toastr.error("找回失败!"+data.error_description);
	});
    bcReq.postData();
}
</script>
</html>