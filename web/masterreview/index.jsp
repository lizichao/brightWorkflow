<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%
	//String userid = (String)session.getAttribute("userid");
	//String username =(String)session.getAttribute("username");
	//String usertype =(String)session.getAttribute("usertype");
	// String usertype =(String)session.getAttribute("usertype");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<title>校长职级评审系统</title>
<!-- 
<link href="/workflow/css/public.css" rel="stylesheet" type="text/css" />
<link href="/workflow/css/layout.css" rel="stylesheet" type="text/css" />
<link href="/workflow/js/dialog/css/custom-theme/jquery-ui-1.9.0.custom.css" rel="stylesheet" />
 -->
   <link rel="stylesheet" href="/masterreview/Css/HuanYu.css">
 <link href="/js/jquery/plugin/toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
 <link href="/js/jquery/plugin/fancybox/jquery.fancybox.css" rel="stylesheet" type="text/css" media="screen"/>

    <link href="/js/jquery/plugin/zebra_dialog/zebra_dialog.css" rel="stylesheet" type="text/css" />
 
  <script type="text/javascript" src="/js/des.js"></script>
  <script type="text/javascript" src="/js/jquery/jquery-1.7.1.min.js"></script>
  <script src="/js/jquery/jsrender.js" type="text/javascript"></script>
  <script type="text/javascript" src="/workflow/workflow.js"></script>
  <script type="text/javascript" src="/js/jquery/common.js"></script>
    <script src="/js/jquery/plugin/fancybox/jquery.mousewheel.pack.js" type="text/javascript"></script>
   <script src="/js/jquery/plugin/fancybox/jquery.fancybox.js" type="text/javascript" ></script>
   <script src="/js/jquery/plugin/toastr/toastr.min.js" type="text/javascript"></script>
     <script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.js" type="text/javascript"></script>
<script src="/js/jquery/plugin/zebra_dialog/zebra_dialog.src.js" type="text/javascript"></script>
   
   
<!--新闻 -->
<script id="newsRec" type="text/x-jsrender">	
<ul>
{{for Data}}
   <li> <a class="cha" href="/masterreview/news/newsdisplay.jsp?newsId={{:id}}"  target="_blank">{{:news_title}}</a>
   {{if #index <4}}
      <img src="/masterreview/images/new.gif" alt="">
   {{/if}}
   </li>
{{/for}}
</ul>
<a href="/masterreview/news/newslist.jsp" target="_blank" title="更多" class="more"><img src="images/more.png" alt="" title="" /></a>
</script>

<script type="text/javascript">
$(document).ready(function(){
	if ($.cookie("rmbUser") == "true") { 
		$("#ck_rmbUser").prop("checked", true); 
		$("#usercode").val($.cookie("usercode")); 
		$("#username").val($.cookie("username")); 
		//$("#password").remove(); 
		//$("#pass").append("<input id='password' type='password' class='txt2'/>"); 
		//$("#password").val($.cookie("password")); 
	} 
	
	var bcReq = new BcRequest('headmaster','newsAction','findIndexNews');
	//bcReq.setExtraPs({"PageSize":"10","PageNo":1,"query_processInstanceId":processInstanceId,'query_processDefId':processDefId});
	bcReq.setSuccFn(function(data,status){
		 $("#newsDiv").empty();
		 if(data.Data.length>0){
			 var userContent=$("#newsRec").render(data);
			 $("#newsDiv").append(userContent);
		 }else{
		   $("#newsDiv").append("<span style='width:100%;color:red;'>暂无数据</span>");
		 }
	});
	bcReq.postData();
})



function save() { 
	if ($("#ck_rmbUser").prop("checked")) { 
		var usercode = $("#usercode").val(); 
		var username = $("#username").val(); 
	//	var password = $("#password").val(); 
		$.cookie("rmbUser", "true", { expires: 7 }); //存储一个带7天期限的cookie 
		$.cookie("usercode", usercode, { expires: 7 }); 
		$.cookie("username", username, { expires: 7 }); 
		//$.cookie("password", password, { expires: 7 }); 
	}else{ 
		$.cookie("rmbUser", "false", { expire: -1 }); 
		$.cookie("usercode", "", { expires: -1 }); 
		$.cookie("username", "", { expires: -1 }); 
		//$.cookie("password", "", { expires: -1 }); 
	} 
}; 

function viewNews(id){
	$.fancybox.close();
	$.fancybox.open({href:"/masterreview/news/newsdisplay.jsp?newsId="+id,type:'iframe',width:827,height:568,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'no',preload:false}});
}
</script> 
</head>

<body>
<div class="top">
		<div class="container">
			<div class="logo"><a href=""><img src="images/logo.jpg" alt="" ></a></div>
			<div class="dang">技术支持：0755-33018116</div>
		</div>
	</div>

	<div class="login_bg" style="height: 430px">
		<div class="container position">
			<div class="login">
				<ul class="clear-fix">
					<li class="child-01">登录</li>
					<li class="child-02" style="height: 10px" ></li>
					<li class="child-03">
					  <input type="text" id="usercode" style="width:180px"  onkeypress="checkUser(event);" onfocus="if(value==defaultValue){value='';}" onblur="if(!value){value=defaultValue;checkIdnum()}" placeholder="请输入身份证号" >
					 </li>
				<!-- 	<li class="child-04">
			           <input type="text" style="" id="username" name="username"    placeholder="姓名"/>
			        </li> -->
			        <li class="child-04"><!-- <input type="password" value="密码" onkeypress="checkUser(event);"  onfocus="txtpwdfocus(this)"></li> -->
			           <input type="password" style="" id="userpwd" name="userpwd"  onkeypress="checkPwd(event);"  placeholder="密码"/>
			        </li>
					<li class="child-05">
					   <a href="javascript:void(0);" onclick = "regist()">立即注册</a>
					   <span style="float:right;">||&nbsp;</span>
					   <a href="javascript:void(0);" onclick = "forgetpwd()">忘记密码？</a> 
					   <input id="ck_rmbUser" type="checkbox" value=""> 记住账号 
					</li>
					<li class="child-06">
					  <input type="submit" value="登录" onclick="checklogin()" />
					</li>
				</ul>
			</div>
		</div>
	</div>
	
	<div class="container mt24">
		<div id="newsDiv" class="login_notice">
		</div>
	</div>
	
	
	
	<div class="footer mt24"">
		<div class="container">
			深州市教育局版权所有 | 亮信科技技术支持：0755-33018116（工作日9:00-12:00  14:00-18:00） <br>
			Copyright &copy; 2016 . All Rights Reserved.
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
/** 密码框控制  */
function txtpwdfocus(eve) {
	$(eve).hide();
	$("#userpwd").show().focus();
}

function pwdblur(eve) {
	if($(eve).val() == '' || $(eve).val() == '密码') {
		$(eve).hide();
		$("#userpwdtxt").show();
	}
}

function checkPwd(evt)
{
 evt = (evt) ? evt : ((window.event) ? window.event : ""); //兼容IE和Firefox获得keyBoardEvent对象
 var key = evt.keyCode?evt.keyCode:evt.which;//兼容IE和Firefox获得keyBoardEvent对象的键值 

	if(13 == key)
	{
		if("" == $("#userpwd").val())
	    {			
			toastr.clear();
			toastr.error("请输入密码！");
			$("#userpwd").focus();
			return ;
		}
    	checklogin();
	}
}

 function checkIdnum(){
	   var usercodeVal= $("#usercode").val();
	   // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
	   var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
	   if(usercodeVal && reg.test(usercodeVal) === false)  
	   {  
		   toastr.error("身份证输入不合法");  
	       return  false;  
	   }  
	   return true;
 }


function checkUser(evt)
{
  evt = (evt) ? evt : ((window.event) ? window.event : ""); //兼容IE和Firefox获得keyBoardEvent对象
 var key = evt.keyCode?evt.keyCode:evt.which;//兼容IE和Firefox获得keyBoardEvent对象的键值 
	if(13 == key)
	{ 
		if("" == $("#usercode").val())
		{
			toastr.clear();
			toastr.error("请输入身份证号码！");
			$("#usercode").focus();
			return ;
		}
		$("#usercode").focus();
		return ;
	}
}

function checklogin(){    
	if("" == $("#usercode").val())		
	{			
		toastr.clear();
		toastr.error("请输入身份证号码！");
		$("#usercode").focus();
		return ;
	}
	$("#RequiredFieldValidator").hide();
	if("" == $("#userpwd").val())
	{			
		toastr.clear();
		toastr.error("请输入密码！");
		$("#userpwd").focus();
		return ;
	}
	//if($("#userpwd").val().length<6 || $("#userpwd").val().length>12){
		//top.toastr.clear();
		//top.toastr.error("密码长度必须在6~12位之间！");			
		//return ;
	//}
	$("#RequiredFieldValidator").hide();
	$.post("/P.tojson?sysName=pcmc&oprID=sm_permission&actions=login",
			 {"usercode":$("#usercode").val(),
		      "userpwd":strEnc($("#userpwd").val()),
		      "username":$("#username").val(),
		      "captchaCode":""
		      },
     function(data,status){
			 if (data.result=="-1"){
				 toastr.error(data.error_description);
			 } else {
				 save(); 
			 	 //if(data.Data[0].usertype==1){
			 	 //  top.location.reload();
			 	  // return;
			 	// }
			 	 top.location.href=data.Forward.forward;
			 }
     }
	);
}

function forgetpwd() {
	$.fancybox.close();
	forgetpwdOpen();
}

function forgetpwdOpen() {
	$.fancybox.open({href:"/masterreview/public/forgetpwd.jsp",type:'iframe',width:327,height:268,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'no',preload:false}});
}

function regist() {
	$.fancybox.open({href:"/masterreview/public/register.jsp",type:'iframe',width:449,height:722,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'auto',preload:false}});
}
</script>
</html>
