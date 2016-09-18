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
		height: 368px;
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
	<!--
	function setTab(name,cursel,n){
		for(i=1;i<=n;i++){
			var menu=document.getElementById(name+i);
			var con=document.getElementById("con_"+name+"_"+i);
			menu.className=i==cursel?"hover":"";
			con.style.display=i==cursel?"block":"none";
		}
	}
	//-->
</script>
</head>
<body>
<div class="load">
	<div class="loadin">
		<div class="loadhd">
		<span class="close" onclick="windowclose();"><img style="cursor:pointer;" src="/images/close.png" alt="" /></span>
			<span class="loadspan">修改密码</span>
		</div>
		<div class="loadbd">
			<input type="text"      id="userpwdtxt"  value="密码"                           onfocus="txtpwdfocus(this)" />
			<input type="password"  id="userpwd"     name="userpwd"  style="display:none;" onkeypress="checkPwd(event);" onblur="pwdblur(this)"/>
			<input type="text"      id="repwdtxt"    value="确认密码"                         onfocus="retxtpwdfocus(this)" />
			<input type="password"  id="reuserpwd"   style="display:none;"                 onkeypress="checkRePwd(event);" onblur="repwdblur(this)"/>
			<div class="forget">
				<div class="fotz fl">
				<!-- <input type="checkbox" name="sex" id="male" class="radiotxt" />
					 <label for="male">自动登陆</label> -->
				</div>
				<ul class="fotul fr" >
				</ul>
			</div>
			<button class="btn" onclick="updatePwd()" style="cursor:pointer;background:#44b549">修&nbsp改&nbsp密&nbsp码</button>
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
//密码框控制
function txtpwdfocus(eve) {
	$(eve).hide();
	$("#userpwd").show().focus();
}
function pwdblur(eve) {
	if($(eve).val() == '') {
		$(eve).hide();
		$("#userpwdtxt").show();
	}
}

function retxtpwdfocus(eve) {
	$(eve).hide();
	$("#reuserpwd").show().focus();
}
function repwdblur(eve) {
	if($(eve).val() == '') {
		$(eve).hide();
		$("#repwdtxt").show();
	}
}


function checkPwd(evt)
{
  	evt = (evt) ? evt : ((window.event) ? window.event : ""); //兼容IE和Firefox获得keyBoardEvent对象
 	var key = evt.keyCode?evt.keyCode:evt.which;//兼容IE和Firefox获得keyBoardEvent对象的键值 
 	if(13 == key)
	{ 
 		if("" == $("#userpwdtxt").val())
	    {			
			top.toastr.clear();
			top.toastr.error("请输入密码！");
			$("#userpwd").focus();
			return ;
		}
		$("#repwdtxt").focus();
	}
}
function checkRePwd(evt)
{
  	evt = (evt) ? evt : ((window.event) ? window.event : "");
 	var key = evt.keyCode?evt.keyCode:evt.which; 
 	if(13 == key)
	{ 
		updatePwd();
	}
}
function updatePwd(){
	if($("#userpwd").val().length<6 || $("#userpwd").val().length>12){
		toastr.clear();
		toastr.error("密码长度必须在6~12位之间！");	
		$("#userpwd").focus();		
		return ;
	}
	if ($("#reuserpwd").val() != $("#userpwd").val()) {
		toastr.clear();
		toastr.error("两次密码输入不同！");
		$("#reuserpwd").focus();
		return ;
	}
	var bcReq = new BcRequest('yuexue','student','uptStudentPwd');
    bcReq.setExtraPs({"userpwd":strEnc($("#userpwd").val())});
    bcReq.setSuccFn(function(data,status){
         toastr.success("修改成功，请牢记新密码！");
         $.fancybox.close();
    });
    bcReq.setFailFn(function(data,status){
		toastr.error("修改失败!"+data.error_description);
	});
    bcReq.postData();
}
</script>
</html>